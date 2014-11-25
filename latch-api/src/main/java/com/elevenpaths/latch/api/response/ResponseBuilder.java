package com.elevenpaths.latch.api.response;

import java.util.HashMap;
import java.util.Map;

import com.eclipsesource.json.JsonObject;
import com.elevenpaths.latch.api.exception.MethodException;

/**
 * Class responsible to transform JSON response into java a object
 * @author jpenren
 */
public final class ResponseBuilder {
	protected static final int ALREADY_PAIRED_CODE = 205;
	private static Map<Class<?>, Deserializer<?>> map = new HashMap<Class<?>, ResponseBuilder.Deserializer<?>>();
	
	private ResponseBuilder() {
	}
	
	static{
		//Define available deserializers
		//Deserializer for pair response
		map.put(PairResponse.class, new Deserializer<PairResponse>() {
			
			public PairResponse parse(final JsonObject json) {
				final PairResponse response = new PairResponse();
				final String accountId = json.get("data").asObject().get("accountId").asString();
				response.setAccountId(accountId);
				
				//Parse error message if exists
				boolean hasError = json.get("error")!=null;
				if(hasError){
					final int errorCode = json.get("error").asObject().get("code").asInt();
					response.setAlreadyPaired(ALREADY_PAIRED_CODE==errorCode);
				}
				
				return response;
			}
		});
		
		//Deserializer for status response
		map.put(StatusResponse.class, new Deserializer<StatusResponse>() {

			public StatusResponse parse(JsonObject json) {
				final StatusResponse response = new StatusResponse();
				final JsonObject operations = json.get("data").asObject().get("operations").asObject();
				final JsonObject operationData = operations.iterator().next().getValue().asObject();
				final String status = operationData.get("status").asString();
				response.setStatus(status);
				 
				//Parse Two Factor data if exists
				if(operationData.get("two_factor")!=null){
					final JsonObject twoFactor = operationData.get("two_factor").asObject();
					final String token = twoFactor.get("token").asString();
					final long generated = twoFactor.get("generated").asLong();
					response.setToken(token);
					response.setGenerated(generated);
				}
				
				return response;
			}
		});
	}
	
	/**
	 * Parse JSON response to an object
	 * @param json response
	 * @param target class parse to
	 * @return instance of LatchResponse
	 * @throws MethodException
	 */
	public static <T extends LatchResponse> T build(final String json, Class<T> target) throws MethodException{
		final JsonObject el = JsonObject.readFrom(json);
		final boolean hasData = el.get("data")!=null;
		if(!hasData){
			//Unexpected data received
			throw new MethodException("Unexpected response: " + json);
		}
		
		return deserialize(el, target);
	}
	
	/**
	 * Find deserializer to parse a JSON object
	 * @param json JSON object instance to parse
	 * @param target class parse to
	 * @return instance of LatchResponse
	 * @throws MethodException
	 */
	@SuppressWarnings("unchecked")
	private static <T extends LatchResponse> T deserialize(final JsonObject json, Class<T> target) throws MethodException{
		if(!map.containsKey(target)){
			throw new MethodException("ResponseType not available");
		}
		
		return (T) map.get(target).parse(json);
	}
	
	/**
	 * Interface for deserializers
	 * @author jpenren
	 *
	 * @param <E> return type
	 */
	private interface Deserializer<E>{
		public E parse(final JsonObject json);
	}
	
}
