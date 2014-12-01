package com.elevenpaths.latch;

import static com.elevenpaths.latch.LatchKeys.APP_ID;
import static com.elevenpaths.latch.LatchKeys.FORCE_LOGIN_FAIL;
import static com.elevenpaths.latch.LatchKeys.SECRET_KEY;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;


public final class LatchConfig {
	private static final String CONFIG_FILE = "latch-config.properties";
	private static Log log = LogFactoryUtil.getLog(LatchConfig.class);
	private static PropertiesConfiguration config = new PropertiesConfiguration();
	
	static {
		try {
			final String path = getConfigPath();
			config.load(path);
		} catch (ConfigurationException e) {
			log.error("Error loading configuration: ", e);
		}
	}
	
	public static boolean isConfigured(){
		return getAppId()!=null && !"".equals(getAppId());
	}
	
	public static String getAppId(){
		return config.getString(APP_ID);
	}
	
	public static String getSecretKey(){
		return config.getString(SECRET_KEY);
	}
	
	public static void setAppId(String value){
		config.setProperty(APP_ID, value);
	}
	
	public static void setSecretKey(String value){
		config.setProperty(SECRET_KEY, value);
	}
	
	public static void setForceLoginFail(boolean forceLoginFail) {
		config.setProperty(FORCE_LOGIN_FAIL, forceLoginFail);
	}
	
	public static boolean isForceLoginFail() {
		return config.getBoolean(FORCE_LOGIN_FAIL, false);
	}
	
	public static void store() throws ConfigurationException{
		final String path = getConfigPath();
		config.save(path);
	}
	
	private static String getConfigPath(){
		return PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/"+CONFIG_FILE;
	}
	
}
