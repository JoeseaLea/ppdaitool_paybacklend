package com.ppdai.ppdaitool.htmlpage.analysis;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ppdai.ppdaitool.utils.FileUtils;
import com.ppdai.ppdaitool.utils.PPDUtil;

/**
 * 投标记录页面抓取
 * 	如： http://www.ppdai.com/user/pdu4212037825
 */
public class TenderPageFetcher {
	private static final Logger logger = Logger.getLogger(TenderPageFetcher.class);
	
	private static final String BASE_URL = "http://www.ppdai.com";
	private String saveBasePath;
	
	public TenderPageFetcher(String saveBasePath){
		this.saveBasePath = saveBasePath;
	}
	
	public void execute(List<String> pageUrlList){
		if(pageUrlList != null && pageUrlList.size() > 0){
			for(String pageUrl : pageUrlList){
				fetchPage(pageUrl);
			}
		}
	}
	
	/**
	 * 页面抓取并解析
	 */
	private void fetchPage(String pageUrl){
		try{
			String uid = pageUrl.substring(pageUrl.lastIndexOf("/") + 1);
			logger.info("uid=" + uid);
			
			//页面所在文件夹
			String dirPath = getSaveBasePath() + uid;
			File dirFile = new File(dirPath);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			
			//页面抓取
			HtmlPage htmlPage = PPDUtil.getUrlPage(pageUrl);
	        
	        //投标记录列表数据
	        @SuppressWarnings("rawtypes")
			List aList = htmlPage.getByXPath("//div[@id='memberinfocontent']/div[@id='div2']//tr//a");
	        if(aList != null && aList.size() > 0){
	        	for(int i=0; i<aList.size(); i++){
	        		try{
	        			String href = ((HtmlAnchor)aList.get(i)).getAttribute("href");
		        		if(href != null && href.length() > 0){
		        			//明细页面URL地址
		        			String pageFullPath = BASE_URL + href;
		        			String id = pageFullPath.substring(pageFullPath.lastIndexOf("/") + 1);
		        			logger.info(id + " --> " + pageFullPath);
		        			
		        			//保存页面内容到文件
		        			String pageFilePath = dirPath + File.separator + id + ".html";
		        			logger.info("pageFilePath=" + pageFilePath);
		        			File pageFile = new File(pageFilePath);
		        			if(!pageFile.exists()){
			        			//页面内容
			        			HtmlPage detailPage = PPDUtil.getUrlPage(pageFullPath);
			        			String pageContent = detailPage.asXml();

			        			if(pageContent.indexOf("class=\"arrow down\"") > 0){
			        				pageContent = pageContent.replace("class=\"arrow down\"", "");
			        			}
			        			
		        				FileUtils.writeFile(pageFilePath, pageContent);
		        			}
		        		}
	        		}catch(Exception ex){
	        			//logger.error("", ex);
	        		}
	        	}
	        }
	        
	        logger.info("page fetch finish: " + pageUrl);
	        
		}catch(Exception ex){
			//logger.error("", ex);
		}
	}
	
	private String getSaveBasePath() {
		if(!saveBasePath.endsWith("/") && !saveBasePath.endsWith("\\")){
			saveBasePath += File.separator;
		}
		return saveBasePath;
	}
	
}
