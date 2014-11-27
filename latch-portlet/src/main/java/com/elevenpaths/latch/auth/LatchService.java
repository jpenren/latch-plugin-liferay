package com.elevenpaths.latch.auth;

import java.sql.SQLException;
import java.util.Date;

import com.elevenpaths.latch.LatchConfig;
import com.elevenpaths.latch.api.Latch;
import com.elevenpaths.latch.api.exception.CommunicationException;
import com.elevenpaths.latch.api.exception.MethodException;
import com.elevenpaths.latch.api.response.PairResponse;
import com.elevenpaths.latch.api.response.StatusResponse;
import com.elevenpaths.latch.domain.UserLatch;
import com.elevenpaths.latch.exception.LatchAuthenticationException;
import com.elevenpaths.latch.exception.UserNotLatchedException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class LatchService {
	private static Log log = LogFactoryUtil.getLog(LatchService.class);
	private static UserLatchRepository repo = new UserLatchRepository();
	
	public static UserLatch create(){
		UserLatch userLatch = new UserLatch();
		userLatch.setCreateDate(new Date());
		
		return userLatch;
	}
	
	public static void doPair(long userId, String token) throws CommunicationException, LatchAuthenticationException {
		try {
			//Calling to Latch and retrieving response
			String appId = LatchConfig.getAppId();
			String secretKey = LatchConfig.getSecretKey();
			PairResponse resp = Latch.with(appId, secretKey).pair(token);
			String accountId = resp.getAccountId();
			
			//Save user latch account data
			UserLatch userLatch = LatchService.create();
			userLatch.setUserId(userId);
			userLatch.setAccountId(accountId);
			repo.save(userLatch);
		} catch (MethodException e) {
			throw handleException("API method error: ", e);
		} catch (SQLException e) {
			throw handleException("DataBase error: ", e);
		} 
	}
	
	public static StatusResponse authenticate(long userId) throws CommunicationException, UserNotLatchedException, LatchAuthenticationException {
		try {
			UserLatch userLatch = repo.findByUserId(userId);
			if(userLatch==null){
				throw new UserNotLatchedException();
			}
			
			String appId = LatchConfig.getAppId();
			String secretKey = LatchConfig.getSecretKey();
			String accountId = userLatch.getAccountId();
			StatusResponse status = Latch.with(appId, secretKey).status(accountId);
			
			return status;
		} catch (SQLException e) {
			throw handleException("DataBase error: ", e);
		} catch (MethodException e) {
			throw handleException("API method error: ", e);
		}
	}
	
	public static boolean hasLatch(long userId) throws LatchAuthenticationException{
		try {
			return repo.hasLatch(userId);
		} catch (SQLException e) {
			throw handleException("DataBase error: ", e);
		}
	}
	
	public static void doUnPair(long userId) throws CommunicationException, LatchAuthenticationException{
		try {
			UserLatch userLatch = repo.findByUserId(userId);
			if(userLatch!=null){
				//Calling to Latch to unpair account
				String appId = LatchConfig.getAppId();
				String secretKey = LatchConfig.getSecretKey();
				try {
					Latch.with(appId, secretKey).unpair(userLatch.getAccountId());
				} catch (Exception e) {
					log.error("Error accesing database: ", e);
				}
				
				repo.delete(userId);
			}
		} catch (SQLException e) {
			throw handleException("DataBase error: ", e);
		}
	}
	
	private static LatchAuthenticationException handleException(String message, Exception cause) {
		log.error(message, cause);
		return new LatchAuthenticationException(message, cause);
	}

}
