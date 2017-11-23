package com.ppdai.ppdaitool.pptools;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

import com.ppdai.ppdaitool.export.HtmlParseResultExporter;
import com.ppdai.ppdaitool.utils.PropertiesUtil;


public class HtmlParseTest {
	public static void main(String[] args) {
		String pageBasePath = PropertiesUtil.getInstance().getProperty("parse.pageBasePath");
		String exportFileName = PropertiesUtil.getInstance().getProperty("parse.exportFileName");
		String yutou_usernames = PropertiesUtil.getInstance().getProperty("parse.yutou.usernames");
		String yutou_keywords = PropertiesUtil.getInstance().getProperty("parse.yutou.keywords");
		
		System.out.println("pageBasePath=" + pageBasePath);
		System.out.println("exportFileName=" + exportFileName);
		System.out.println("yutou_usernames=" + yutou_usernames);
		System.out.println("yutou_keyworks=" + yutou_keywords);
		
		//search parameters
		LinkedHashMap<String, String> searchParamsMap = new LinkedHashMap<String, String>();
		if(StringUtils.isNotEmpty(yutou_usernames) && StringUtils.isNotEmpty(yutou_keywords)){
			String[] usernamesArr = yutou_usernames.split(";");
			String[] keywordsArr = yutou_keywords.split(";");
			for(int i=0; i<usernamesArr.length; i++){
				String username = StringUtils.trimToEmpty(usernamesArr[i]);
				String keyword = StringUtils.trimToEmpty(keywordsArr[i]);
				searchParamsMap.put(username, keyword);
			}
		}
		
		HtmlParseResultExporter.execute(pageBasePath, exportFileName, searchParamsMap);
		
		System.out.println("finish");
	}
	
}
