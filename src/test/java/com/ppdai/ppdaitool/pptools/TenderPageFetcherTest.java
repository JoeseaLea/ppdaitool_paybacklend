package com.ppdai.ppdaitool.pptools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.ppdai.ppdaitool.htmlpage.analysis.TenderPageFetcher;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.utils.PropertiesUtil;

public class TenderPageFetcherTest {
	public static void main(String[] args) {
		String fetcher_pageUrlList = PropertiesUtil.getInstance().getProperty("fetcher.pageUrlList"); //多个url地址用分号(;)分割
		String fetcher_saveBasePath = PropertiesUtil.getInstance().getProperty("fetcher.saveBasePath"); //存储页面的根路径地址
		String fetcher_intervalMinutes = PropertiesUtil.getInstance().getProperty("fetcher.intervalMinutes"); //间隔分钟数
		
		System.out.println("pageUrlList=" + fetcher_pageUrlList);
		System.out.println("saveBasePath=" + fetcher_saveBasePath);
		System.out.println("intervalMinutes=" + fetcher_intervalMinutes);
		
		String[] arr = fetcher_pageUrlList.split(";"); //分号(;)分隔多个URL地址
		if(arr != null && arr.length > 0){
			List<String> pageUrlList = new ArrayList<String>();
			for(String url : arr){
				if(StringUtils.isNotEmpty(url)){
					pageUrlList.add(url);
				}
			}
			
			if(pageUrlList.size() > 0){
				//登录
		        String username = PropertiesUtil.getInstance().getProperty("username");
		        String password = PropertiesUtil.getInstance().getProperty("password");
				boolean loginSuccess = PPDUtil.tryLogin(username, password);
				
				if(!loginSuccess){
					throw new RuntimeException("login error");
				}
				
				//Fetch
				TenderPageFetcher fetcher = new TenderPageFetcher(fetcher_saveBasePath);
				while(true){
					System.out.println("开始抓取...");
					fetcher.execute(pageUrlList);
					
					try {
						//每隔N分钟执行一次抓取
						TimeUnit.SECONDS.sleep(60 * Integer.parseInt(fetcher_intervalMinutes));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
}
