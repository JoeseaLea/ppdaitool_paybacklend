package com.ppdai.ppdaitool.fetch;

import java.util.List;

import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ppdai.ppdaitool.dao.BlackListDao;
import com.ppdai.ppdaitool.dao.BorrowDetailDao;
import com.ppdai.ppdaitool.dao.impl.BlackListDaoImpl;
import com.ppdai.ppdaitool.dao.impl.BorrowDetailDaoImpl;
import com.ppdai.ppdaitool.htmlpage.analysis.BlackList;
import com.ppdai.ppdaitool.htmlpage.analysis.BorrowDetail;
import com.ppdai.ppdaitool.utils.DownloadHtmlPageUtil;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.vo.BidDebtRecordVo;
import com.ppdai.ppdaitool.vo.BidRecordVo;
import com.ppdai.ppdaitool.vo.BlacklistVo;
import com.ppdai.ppdaitool.vo.BorrowDetailPageVo;
import com.ppdai.ppdaitool.vo.BorrowDetailVo;
import com.ppdai.ppdaitool.vo.NeedReturnRecordNext6MonthVo;
import com.ppdai.ppdaitool.vo.OverTimeRecordLast6MonthVo;
import com.ppdai.ppdaitool.vo.RepaymentDetailVo;

/**
 * 页面数据抓取
 * 	黑名单：  http://invest.ppdai.com/account/blacklistnew
 */
public class BlacklistPageDataFetcher extends AbstractPageDataFetcher {
	private static final Logger logger = Logger.getLogger(BlacklistPageDataFetcher.class);
	
	private BlackListDao blackListDao = null;
	private BorrowDetailDao borrowDetailDao = null;
	private String url;
	private String dataType;
	
	public BlacklistPageDataFetcher(String url, String dataType){
		super();
		
		blackListDao = appContext.getBean("blackListDao", BlackListDaoImpl.class);
		borrowDetailDao = appContext.getBean("borrowDetailDao", BorrowDetailDaoImpl.class);
		
		this.url = url;
		this.dataType = dataType;
	}
	
	@Override
	public void execute() {
		try{
			HtmlPage htmlPage = PPDUtil.getUrlPage(url);

			BlackList blackList = new BlackList();
			BorrowDetail borrowDetail = new BorrowDetail();
			
			while (true) {
				if(htmlPage == null){
					break;
				}
				logger.info(htmlPage.getBaseURL().toString());
				
				try {
					//1、列表记录信息
					List<BlacklistVo> blacklist = blackList.getBlackListInfoNew(htmlPage);
					blackListDao.addBlackLists(blacklist);
					
					//2、在列表页面中，记载每条黑名单记录的还款详情信息，并返回页面
					htmlPage = blackList.getRepaymentDetailsHtmlPageNew(htmlPage);
					//DownloadHtmlPageUtil.saveHtmlPage("http://invest.ppdai.com/account/blacklistnewDetails", htmlPage.asXml());
					
					//3、获取黑名单还款详情列表信息
					List<RepaymentDetailVo> repaymentDetails = blackList.getRepaymentDetails(htmlPage);
					blackListDao.addRepaymentDetails(repaymentDetails);
					
					//获取所有手机app用户的闪电借款按钮
					List<String> detailURLlist = blackList.getBlacklistdetailURL(htmlPage);
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
								
								blackListDao.updateFullUserName(borrowDetail.getFullUsername(), borrowDetail.getListingid());
								
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
