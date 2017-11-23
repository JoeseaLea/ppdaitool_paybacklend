package com.ppdai.ppdaitool.fetch;

import java.util.List;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ppdai.ppdaitool.dao.PaybacklendDao;
import com.ppdai.ppdaitool.dao.impl.PaybacklendDaoImpl;
import com.ppdai.ppdaitool.htmlpage.analysis.BlackList;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.vo.LateBackVo;

/**
 * 页面数据抓取
 * 	逾期收回：  http://invest.ppdai.com/account/latebacknew
 */
public class OverdueRecoveryPageDataFetcher extends AbstractPageDataFetcher {
	private static final Logger logger = Logger.getLogger(OverdueRecoveryPageDataFetcher.class);
	
	private PaybacklendDao paybacklendDao = null;
	private String url;
	
	public OverdueRecoveryPageDataFetcher(String url){
		super();
		paybacklendDao = appContext.getBean("paybacklendDao", PaybacklendDaoImpl.class);
		this.url = url;
	}
	
	@Override
	public void execute() {
		try{
			BlackList blackList = new BlackList();
			
			HtmlPage htmlPage = PPDUtil.getUrlPage(url);
			
			while (true) {
				if(htmlPage == null){
					break;
				}
				logger.info(htmlPage.getBaseURL().toString());
				
				try {
					//1、列表记录信息
					List<LateBackVo> lateBackList = paybacklendDao.getLateBackList(htmlPage);
					paybacklendDao.saveLateBackList(lateBackList);
					
					if (blackList.isLastPage(htmlPage)) {
						break;
					} else {
						htmlPage = PPDUtil.getUrlPage(blackList.getNextHtmlPageURL(htmlPage));
					}
					
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			
		}catch(Exception ex){
			logger.error("", ex);
		}finally{
			if(appContext != null){
				appContext.close();
			}
		}
	}

}
