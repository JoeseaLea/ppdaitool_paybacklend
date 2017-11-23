package com.ppdai.ppdaitool;

import org.apache.log4j.Logger;

import com.ppdai.ppdaitool.fetch.BlacklistPageDataFetcher;
import com.ppdai.ppdaitool.fetch.IPageDataFetcher;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.utils.PropertiesUtil;

/**
 * 页面数据抓取： 黑名单
 */
public class PPDmain {
	private static final Logger logger = Logger.getLogger(PPDmain.class);
	
	public static void main(String[] args) {
		try {
			String username = PropertiesUtil.getInstance().getProperty("username", "zhangsan");
			String password = PropertiesUtil.getInstance().getProperty("password", "******");
			boolean loginSuccess = PPDUtil.tryLogin(username, password);
			
			if(loginSuccess){
				BlacklistPageDataFetcher fetcher = new BlacklistPageDataFetcher("http://invest.ppdai.com/account/blacklistnew", IPageDataFetcher.dataType_HMD);
				fetcher.execute();
			}else{
				throw new RuntimeException("login error");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
}
