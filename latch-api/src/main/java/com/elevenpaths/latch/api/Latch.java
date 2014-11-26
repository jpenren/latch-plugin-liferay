package com.elevenpaths.latch.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elevenpaths.latch.api.exception.CommunicationException;
import com.elevenpaths.latch.api.exception.InitalizeCommunicationException;
import com.elevenpaths.latch.api.exception.MethodException;
import com.elevenpaths.latch.api.response.PairResponse;
import com.elevenpaths.latch.api.response.ResponseBuilder;
import com.elevenpaths.latch.api.response.StatusResponse;

/**
 * Latch API methods
 * @author jpenren
 */
public final class Latch {
	
	private final Logger log = LoggerFactory.getLogger(Latch.class);
	private static final String DEFAULT_BASE_URL = "https://latch.elevenpaths.com";
	private static final String PATH="/api/";
	private static final String DEFAULT_VERSION="0.9";
	private static final String STATUS = "/status/";
	private static final String PAIR = "/pair/";
	private static final String UNPAIR = "/unpair/";
	private static final String UNPAIR_SUCCESS = "{}";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String HEADER_DATE = "X-11paths-Date";
	private static final String EPATHS = "11PATHS";
    private static final String BLANK = " ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ALGORITHM = "HmacSHA1";
    private static final String ENCODING = "ISO-8859-1";
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static SSLConnectionSocketFactory socketFactory;
    private String baseUrl = DEFAULT_BASE_URL;
    private String version = DEFAULT_VERSION;
	private String appId;
    private String secretKey;
    
    private Latch(String appId, String secretKey) {
		this.appId = appId;
		this.secretKey = secretKey;
	}
    
    /**
     * Creates an API instance with your appId and secretKey
     * @param appId defined on Latch
     * @param secretKey defined on Latch
     * @return instance of API
     */
    public static Latch with(final String appId, final String secretKey){
    	return new Latch(appId, secretKey);
    }
    
    public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Call to Latch service to pair a device with the appId
	 * @param token user token
	 * @return API response data
	 * @throws CommunicationException
	 * @throws MethodException
	 */
    public PairResponse pair(String token) throws CommunicationException, MethodException{
    	final String query = concat(PATH, version, PAIR, token);
    	final String response = doGet(query);
    	
    	return ResponseBuilder.build(response, PairResponse.class);
    }
    
    /**
     * Make a request to Latch service and retrieve an account status info 
     * @param accountId account to request for status
     * @return API response data
     * @throws CommunicationException
     * @throws MethodException
     */
    public StatusResponse status(String accountId) throws CommunicationException, MethodException{
    	final String query = concat(PATH, version, STATUS, accountId);
    	final String response = doGet(query);
    	
    	return ResponseBuilder.build(response, StatusResponse.class);
    }
    
    /**
     * Call to Latch service to unpair the account
     * @param accountId account to unpair
     * @throws CommunicationException
     * @throws MethodException
     */
    public void unpair(String accountId) throws CommunicationException, MethodException{
    	final String query = concat(PATH, version, UNPAIR, accountId);
    	final String response = doGet(query);
    	
    	if(!UNPAIR_SUCCESS.equals(response)){
    		throw new MethodException("Unexpected response: " + response);
    	}
    }
    
    /**
     * Instance, configure and make a GET request to Latch service
     * @param query API method to execute
     * @return response content
     * @throws CommunicationException
     */
    private String doGet(String query) throws CommunicationException{
    	final String url = concat(baseUrl, query);
    	final CloseableHttpClient connection = initializeConnection();
    	try {
    		final HttpGet method = new HttpGet(url);
    		addHeaders(method, query);
    		
    		// Response as String
            final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    final int status = response.getStatusLine().getStatusCode();
                    if (status == 200) {
                        final HttpEntity entity = response.getEntity();
                        if(entity==null){
                        	throw new IOException("Empty response");
                        }
                        
                        return EntityUtils.toString(entity);
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            
            log.debug("Executing request " + method.getRequestLine());
            final String response = connection.execute(method, responseHandler);
            log.debug("Response: " + response);
            
            return response;
		} catch (Exception e) {
			log.error("Latch API communication error: ", e);
			throw new CommunicationException("Latch API communication error: ", e);
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (Exception e2) {
				log.error("Error closing connection: ", e2);
			}
		}
    }
    
    /**
     * Initializes SSL connection
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private CloseableHttpClient initializeConnection() throws InitalizeCommunicationException{
    	try {
    		if(socketFactory==null){
    	    	//Generates a keystore with Latch SSL CA 
    	    	final CertificateFactory cf = CertificateFactory.getInstance("X.509");
    			final InputStream in = Latch.class.getResourceAsStream("/StartComCA.cer");
    			final Certificate ca = cf.generateCertificate(in);
    			 
    			final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    			keyStore.load(null, null);
    			keyStore.setCertificateEntry("ca", ca);
    	        
    	        //SSL context with custom keystore
    	    	final SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keyStore).build();
    	    	socketFactory = new SSLConnectionSocketFactory(sslcontext);
        	}
            
            //Initialize and configure connection
        	return HttpClients.custom().setSSLSocketFactory(socketFactory).build();
		} catch (Exception e) {
			log.error("Error initializing connection: ", e);
			throw new InitalizeCommunicationException("Error initializing connection: ", e);
		}
    }
    
    /**
     * Attach authentication headers to the request
     * @param request
     * @param query
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    private void addHeaders(final HttpRequest request, final String query) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
    	final String currentDate = getCurrentDate();
    	final String method = request.getRequestLine().getMethod();
    	final String requestData = new StringBuilder().append(method).append("\n").append(currentDate).append("\n\n").append(query).toString();
        final String signedData = signRequest(requestData);
        final String authorizationHeader = new StringBuilder(EPATHS).append(BLANK).append(appId).append(BLANK).append(signedData).toString();

        request.addHeader(HEADER_AUTHORIZATION, authorizationHeader);
        request.addHeader(HEADER_DATE, currentDate);
	}
    
    /**
     * Encrypt data to create authentication header 
     * @param data content to encrypt
     * @return encrypted data
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    private String signRequest(String data) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
    	final SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    	final Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secret);
        
        return Base64.encodeBase64String(mac.doFinal(data.getBytes(ENCODING)));
    }
    
    private String getCurrentDate() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(UTC);
        
        return sdf.format(new Date());
    }
    
    private String concat(String...strings){
    	final StringBuffer buffer = new StringBuffer();
    	for (String string : strings) {
			buffer.append(string);
		}
    	
    	return buffer.toString();
    }
    
}
