package com.ppdai.ppdaitool.fetch;

import java.util.List;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ppdai.ppdaitool.dao.BorrowDetailDao;
import com.ppdai.ppdaitool.dao.PaybacklendDao;
import com.ppdai.ppdaitool.dao.impl.BorrowDetailDaoImpl;
import com.ppdai.ppdaitool.dao.impl.PaybacklendDaoImpl;
import com.ppdai.ppdaitool.htmlpage.analysis.BlackList;
import com.ppdai.ppdaitool.htmlpage.analysis.BorrowDetail;
import com.ppdai.ppdaitool.utils.DownloadHtmlPageUtil;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.vo.BidDebtRecordVo;
import com.ppdai.ppdaitool.vo.BidRecordVo;
import com.ppdai.ppdaitool.vo.BorrowDetailPageVo;
import com.ppdai.ppdaitool.vo.BorrowDetailVo;
import com.ppdai.ppdaitool.vo.NeedReturnRecordNext6MonthVo;
import com.ppdai.ppdaitool.vo.OverTimeRecordLast6MonthVo;
import com.ppdai.ppdaitool.vo.PaybacklendDetailVo;
import com.ppdai.ppdaitool.vo.PaybacklendVo;

/**
 * 页面数据抓取
 * 	收款中：  http://invest.ppdai.com/account/paybacklendnew?Type=1
 *	已还清：  http://invest.ppdai.com/account/paybacklendnew?Type=2
 */
public class Type1_2PageDataFetcher extends AbstractPageDataFetcher {
	private static final Logger logger = Logger.getLogger(Type1_2PageDataFetcher.class);
	
	private PaybacklendDao paybacklendDao = null;
	private BorrowDetailDao borrowDetailDao = null;
	private String url;
	private String dataType;
	
	public Type1_2PageDataFetcher(String url, String dataType){
		super();
		
		paybacklendDao = appContext.getBean("paybacklendDao", PaybacklendDaoImpl.class);
		borrowDetailDao = appContext.getBean("borrowDetailDao", BorrowDetailDaoImpl.class);
		
		this.url = url;
		this.dataType = dataType;
	}
	
	@Override
	public void execute() {
		try{
			BlackList blackList = new BlackList();
			BorrowDetail borrowDetail = new BorrowDetail();
			
			HtmlPage htmlPage = PPDUtil.getUrlPage(url);
			
			while (true) {
				if(htmlPage == null){
					break;
				}
				logger.info(htmlPage.getBaseURL().toString());
				
				try {
					//1、列表记录信息
					List<PaybacklendVo> paybacklendList = paybacklendDao.getPaybacklendList(htmlPage, dataType);
					paybacklendDao.savePaybacklendList(paybacklendList);
					
					//详情页面的URL地址
					List<String> detailURLlist = paybacklendDao.getPaybacklendDetailURLList(htmlPage);
					
					//2、在列表页面中，记载每条记录的详情列表信息
					htmlPage = paybacklendDao.loadDetailListForPaybacklend(htmlPage);
					//DownloadHtmlPageUtil.saveHtmlPage("http://invest.ppdai.com/account/paybacklendnew_" + dataType, htmlPage.asXml());
					
					//3、详情列表信息
					List<PaybacklendDetailVo> paybacklendDetailList = paybacklendDao.getPaybacklendDetailList(htmlPage, dataType);
					paybacklendDao.savePaybacklendDetailList(paybacklendDetailList);
					
					//详情页面
					if(detailURLlist != null && detailURLlist.size() > 0){
						for (String detailURL : detailURLlist) {
							try {
								logger.info(detailURL);
								
								String listingid = detailURL.split("/")[detailURL.split("/").length-1];
								borrowDetail.setListingid(listingid);
								
								HtmlPage detailHtmlPage = null;
								while (true) {
									detailHtmlPage = PPDUtil.getUrlPage(detailURL);
									if (!detailHtmlPage.asXml().contains("502错误")) {
										break;
									}
								}
								
								//保存明细页面到文件
								DownloadHtmlPageUtil.saveHtmlPage(detailURL, detailHtmlPage.asXml());
								
								//获取username信息
								borrowDetail.loadPage(detailHtmlPage, listingid);
								
								//1、借款详情页基本信息
								BorrowDetailPageVo borrowDetailPageVo = borrowDetail.getBorrowDetailPageVo();
								borrowDetailPageVo.setDataType(dataType);
								borrowDetailDao.addBorrowDetailPageVo(borrowDetailPageVo);
								
								//2、投标记录列表数据
								List<BidRecordVo> bidRecords = borrowDetail.getBidRecords(dataType);
								//插入投标记录数据到数据库
								borrowDetailDao.addBidRecords(bidRecords);
								
								//3、债权转让记录列表数据
								List<BidDebtRecordVo> bidDebtRecords = borrowDetail.getBidDebtRecords(dataType);
								//插入债权转让记录数据到数据库
								borrowDetailDao.addBidDebtRecords(bidDebtRecords);
								
								//4、历史成功借款列表
								List<BorrowDetailVo> borrowDetails = borrowDetail.getBorrowDetails(dataType);
								//插入借款记录数据到数据库
								borrowDetailDao.addBorrowDetails(borrowDetails);
								
								//5、获取未来6个月的待还记录数据
								List<NeedReturnRecordNext6MonthVo> needReturnRecordNext6MonthVos = borrowDetail.getNeedReturnRecordNext6MonthVos(dataType);
								//插入未来6个月的待还记录数据到数据库
								borrowDetailDao.addNeedReturnRecordNext6MonthVos(needReturnRecordNext6MonthVos);
								
								//6、获取过去6个月有回款记录的逾期天数数据
								List<OverTimeRecordLast6MonthVo> overTimeRecordLast6MonthVos = borrowDetail.getOverTimeRecordLast6MonthVos(dataType);
								//插入过去6个月有回款记录的逾期天数到数据库
								borrowDetailDao.addOverTimeRecordLast6MonthVos(overTimeRecordLast6MonthVos);
								
							} catch (Exception e) {
								logger.error("", e);
							}
						}
					}
					
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
