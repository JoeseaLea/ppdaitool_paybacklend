package com.ppdai.ppdaitool.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class DownloadHtmlPageUtil {
	private static final Logger logger = Logger.getLogger(DownloadHtmlPageUtil.class);
	
	/**
	 * 保存html页面<br>
	 * 说明：保存html页面时，文件名以访问的url的末尾数字作为文件名
	 * @param url html页面访问url
	 * @param htmlPageContent html页面内容
	 */
	public static void saveHtmlPage(String url, String htmlPageContent) {
		try {
			String[] urlSplit = url.split("/");
			String htmlFileName = urlSplit[urlSplit.length - 1];
			
			String filePath = PropertiesUtil.getInstance().getProperty("file_path", "D:\\ppdai") + File.separator + htmlFileName + ".html";
			
			FileUtils.writeFile(filePath, htmlPageContent);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
