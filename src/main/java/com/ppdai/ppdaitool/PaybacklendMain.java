package com.ppdai.ppdaitool;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ppdai.ppdaitool.fetch.IPageDataFetcher;
import com.ppdai.ppdaitool.fetch.OverdueRecoveryPageDataFetcher;
import com.ppdai.ppdaitool.fetch.Type1_2PageDataFetcher;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.utils.PropertiesUtil;

/**
 * 页面数据抓取：收款中、已还清、逾期收回
 */
public class PaybacklendMain {
	private static final Logger logger = Logger.getLogger(PaybacklendMain.class);
	
	public static void main(String[] args) {
		String dataType = "";
		if(args != null && args.length > 0){
			dataType = args[0];
		} else {
			dataType = "收款中";
		}
		logger.info("dataType=" + dataType);
		
		try {
			String username = PropertiesUtil.getInstance().getProperty("username", "zhangsan");
			String password = PropertiesUtil.getInstance().getProperty("password", "******");
			boolean loginSuccess = PPDUtil.tryLogin(username, password);
			
			if(loginSuccess){
				if("收款中".equalsIgnoreCase(dataType)){
					skzFetch();
					
				}else if("已还清".equalsIgnoreCase(dataType)){
					yhqFetch();
					
				}else if("逾期收回".equalsIgnoreCase(dataType)){
					overdueFetch();
					
				}else{
					//收款中
					Thread t1 = new Thread(new Runnable() {
						public void run() {
							skzFetch();
						}
					});
					t1.setDaemon(true);
					t1.start();
					
					
					//已还清
					Thread t2 = new Thread(new Runnable() {
						public void run() {
							yhqFetch();
						}
					});
					t2.setDaemon(true);
					t2.start();
					
					
					//逾期收回
					Thread t3 = new Thread(new Runnable() {
						public void run() {
							overdueFetch();
						}
					});
					t3.setDaemon(true);
					t3.start();
					
					while(true){
						logger.info("sleep...");
						TimeUnit.SECONDS.sleep(5);
					}
				}
				
			}else{
				throw new RuntimeException("login error");
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 收款中
	 */
	private static void skzFetch() {
		Type1_2PageDataFetcher fetcher = new Type1_2PageDataFetcher("http://invest.ppdai.com/account/paybacklendnew?Type=1", IPageDataFetcher.dataType_SKZ);
		fetcher.execute();
	}

	/**
	 * 已还清
	 */
	private static void yhqFetch() {
		Type1_2PageDataFetcher fetcher = new Type1_2PageDataFetcher("http://invest.ppdai.com/account/paybacklendnew?Type=2", IPageDataFetcher.dataType_YHQ);
		fetcher.execute();
	}

	/**
	 * 逾期收回
	 */
	private static void overdueFetch() {
		OverdueRecoveryPageDataFetcher fetcher = new OverdueRecoveryPageDataFetcher("http://invest.ppdai.com/account/latebacknew");
		fetcher.execute();
	}
	
}
