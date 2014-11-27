package com.elevenpaths.latch.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.api.exception.CommunicationException;
import com.elevenpaths.latch.api.response.StatusResponse;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.service.UserLocalServiceUtil;

public class LatchAuthenticator implements Authenticator {
	private static Log log = LogFactoryUtil.getLog(LatchAuthenticator.class);
	private static Map<Long, String> usersSecondFactor = new ConcurrentHashMap<Long, String>();
	
	public int authenticateByEmailAddress(long companyId, String emailAddress,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {
		
		if(!LatchConfig.isConfigured()){
			//Continues with regular authentication
			return SUCCESS;
		}
		
		try {
			User user = UserLocalServiceUtil.fetchUserByEmailAddress(companyId, emailAddress);
			long userId = user.getUserId();
			return authenticateInLatch(userId);
		} catch (SystemException e) {
			log.error("Error loading user by email: ", e);
		}
		
		return SUCCESS;
	}

	public int authenticateByScreenName(long companyId, String screenName,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {
		
		if(!LatchConfig.isConfigured()){
			return SUCCESS;
		}
		
		try {
			User user = UserLocalServiceUtil.fetchUserByScreenName(companyId, screenName);
			long userId = user.getUserId();
			return authenticateInLatch(userId);
		} catch (SystemException e) {
			log.error("Error loading user by screen name: ", e);
		}
		
		return SUCCESS;
	}

	public int authenticateByUserId(long companyId, long userId,
			String password, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) throws AuthException {
		
		if(!LatchConfig.isConfigured()){
			return SUCCESS;
		}
		
		return authenticateInLatch(userId);
	}
	
	private int authenticateInLatch(long userId) throws AuthException{
		try {
			boolean hasLatch = LatchService.hasLatch(userId);
			if(hasLatch){
				//Authenticate with Latch
				StatusResponse status = LatchService.authenticate(userId);
				if( status.isOff() ){
					//Account locked in Latch
					return FAILURE;
				}
				
				//Remove previous token if exists
				usersSecondFactor.remove(userId);
				if(status.hasSecondFactor()){
					usersSecondFactor.put(userId, status.getToken());
				}
			}
		} catch (CommunicationException e) {
			log.error("Communication error with Latch Service: ", e);
			//Checks force fail on communication error is active
			if( LatchConfig.isForceLoginFail() ){
				return FAILURE;
			}
		} catch (Exception e) {
			log.error("Unxepected error authenticating with Latch: ", e);
		}
		
		//Continue regular authentication
		return SUCCESS;
	}
	
	public static boolean has2Factor(long userId){
		return usersSecondFactor.containsKey(userId);
	}
	
	public static String get2Factor(long userId){
		return usersSecondFactor.get(userId);
	}
	
	public static synchronized void clean2Factor(long userId){
		usersSecondFactor.remove(userId);
	}

}
