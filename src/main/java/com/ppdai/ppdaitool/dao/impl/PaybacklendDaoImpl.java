package com.ppdai.ppdaitool.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.ppdai.ppdaitool.dao.BaseDao;
import com.ppdai.ppdaitool.dao.PaybacklendDao;
import com.ppdai.ppdaitool.utils.PPDUtil;
import com.ppdai.ppdaitool.vo.LateBackVo;
import com.ppdai.ppdaitool.vo.OverTimeRecordLast6MonthVo;
import com.ppdai.ppdaitool.vo.PaybacklendDetailVo;
import com.ppdai.ppdaitool.vo.PaybacklendVo;

@SuppressWarnings("rawtypes")
public class PaybacklendDaoImpl extends BaseDao implements PaybacklendDao{
	@Override
	public List<PaybacklendVo> getPaybacklendList(HtmlPage htmlPage, String dataType) {
		List<PaybacklendVo> dataList = new ArrayList<PaybacklendVo>();
		
		List trList = htmlPage.getByXPath("//table[@class='receivetab']//tr");
		if(trList != null && trList.size() > 0){
			for(int i=1; i<trList.size(); i++){
				HtmlTableRow row = (HtmlTableRow)trList.get(i);
				List<HtmlElement> tdList = row.getElementsByTagName("td");
				if(tdList != null && tdList.size() == 7){
					PaybacklendVo vo = new PaybacklendVo();
					vo.setTenderFinishDate(tdList.get(0).asText());
					vo.setPaidAmount(Double.valueOf(tdList.get(1).asText().replaceAll("¥", "").replaceAll(",", "")));
					vo.setCode(tdList.get(2).asText());
					
					HtmlAnchor a = (HtmlAnchor)tdList.get(2).getElementsByTagName("a").get(0);
					String href = a.getHrefAttribute();
					href = href.substring(href.lastIndexOf("/") + 1);
					vo.setBorrower(href);
					
					vo.setTenderAmount(Double.valueOf(tdList.get(5).asText().replaceAll("¥", "").replaceAll(",", "")));
					vo.setDataType(dataType);
					
					dataList.add(vo);
				}
			}
		}
		
		return dataList;
	}

	@Override
	public void savePaybacklendList(List<PaybacklendVo> paybacklendVoList) {
		if(paybacklendVoList == null || paybacklendVoList.size() == 0){
			return;
		}
		
		try {
			final List<PaybacklendVo> tempPaybacklendVoList = paybacklendVoList;
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO `ppdai`.`paybacklend` ");
			sql.append(" (`tender_finish_date`, `paid_amount`, `code`, `borrower`, `tender_amount`, `data_type`) VALUES (?, ?, ?, ?, ?, ?) ");
			sql.append(" ON DUPLICATE KEY UPDATE ");
			sql.append(" `tender_finish_date`=?, `paid_amount`=?, `borrower`=?, `tender_amount`=?, `data_type`=? ");
			
			getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					PaybacklendVo vo = tempPaybacklendVoList.get(i);
					ps.setString(1, vo.getTenderFinishDate());
					ps.setDouble(2, vo.getPaidAmount());
					ps.setString(3, vo.getCode());
					ps.setString(4, vo.getBorrower());
					ps.setDouble(5, vo.getTenderAmount());
					ps.setString(6, vo.getDataType());

					ps.setString(7, vo.getTenderFinishDate());
					ps.setDouble(8, vo.getPaidAmount());
					ps.setString(9, vo.getBorrower());
					ps.setDouble(10, vo.getTenderAmount());
					ps.setString(11, vo.getDataType());
				}
				
				@Override
				public int getBatchSize() {
					return tempPaybacklendVoList.size();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HtmlPage loadDetailListForPaybacklend(HtmlPage htmlPage) {
		List trList = htmlPage.getByXPath("//table[@class='receivetab']//tr");
		if(trList != null && trList.size() > 0){
			for(int i=1; i<trList.size(); i++){
				try {
					HtmlTableRow row = (HtmlTableRow)trList.get(i);
					List<HtmlElement> tdList = row.getElementsByTagName("td");
					if(tdList != null && tdList.size() == 7){
						htmlPage = tdList.get(6).getElementsByTagName("a").get(1).click();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				PPDUtil.sleep();
			}
		}
		return htmlPage;
	}
	
	@Override
	public List<PaybacklendDetailVo> getPaybacklendDetailList(HtmlPage htmlPage, String dataType) {
		List<PaybacklendDetailVo> dataList = new ArrayList<PaybacklendDetailVo>();
		
		List innerList = htmlPage.getByXPath("//table[@class='receivetab']//tr[@class='inner-data']");
		if(innerList != null && innerList.size() > 0){
			for(int i=0; i<innerList.size(); i++){
				HtmlTableRow row = (HtmlTableRow)innerList.get(i);
				List<HtmlElement> tableList = row.getElementsByTagName("table");
				if(tableList == null || tableList.size() <= 0){
					continue;
				}
				
				String code = row.getAttribute("id").replaceAll("div_", "");
				
				List<HtmlElement> trList = tableList.get(0).getElementsByTagName("tr");
				if(trList != null && trList.size() > 0){
					for(int j=1; j<trList.size() - 1; j++){
						try {
							List<HtmlElement> tdList = trList.get(j).getElementsByTagName("td");
							if(tdList != null && tdList.size() == 7){
								PaybacklendDetailVo vo = new PaybacklendDetailVo();
								vo.setCode(code);
								vo.setPaymentDate(tdList.get(0).asText());
								vo.setYisbx(Double.valueOf(tdList.get(1).asText().replaceAll("¥", "").replaceAll(",", "")));
								vo.setYisbj(Double.valueOf(tdList.get(2).asText().replaceAll("¥", "").replaceAll(",", "")));
								vo.setYislx(Double.valueOf(tdList.get(3).asText().replaceAll("¥", "").replaceAll(",", "")));
								vo.setYingsbx(Double.valueOf(tdList.get(4).asText().replaceAll("¥", "").replaceAll(",", "")));
								vo.setOverTimeInterest(Double.valueOf(tdList.get(5).asText().split("/")[0].replaceAll("¥", "").replaceAll(",", "")));
								vo.setOverDays(Integer.valueOf(tdList.get(5).asText().split("/")[1]));
								vo.setStatus(tdList.get(6).asText());
								vo.setDataType(dataType);
								
								dataList.add(vo);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		
		return dataList;
	}

	@Override
	public void savePaybacklendDetailList(List<PaybacklendDetailVo> paybacklendDetailVoList) {
		if(paybacklendDetailVoList == null || paybacklendDetailVoList.size() == 0){
			return;
		}
		
		try {
			final List<PaybacklendDetailVo> tempPaybacklendDetailVoList = paybacklendDetailVoList;
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO `ppdai`.`paybacklend_details` ");
			sql.append(" (`code`, `payment_date`, `yisbx`, `yisbj`, `yislx`, `yingsbx`, `over_time_interest`, `over_days`, `status`, `data_type`) ");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			sql.append(" ON DUPLICATE KEY UPDATE  ");
			sql.append(" `yisbx`=?, `yisbj`=?, `yislx`=?, `yingsbx`=?, `over_time_interest`=?, `over_days`=?, `status`=?, `data_type`=? ");
			
			getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					PaybacklendDetailVo vo = tempPaybacklendDetailVoList.get(i);
					ps.setString(1, vo.getCode());
					ps.setString(2, vo.getPaymentDate());
					ps.setDouble(3, vo.getYisbx());
					ps.setDouble(4, vo.getYisbj());
					ps.setDouble(5, vo.getYislx());
					ps.setDouble(6, vo.getYingsbx());
					ps.setDouble(7, vo.getOverTimeInterest());
					ps.setInt(8, vo.getOverDays());
					ps.setString(9, vo.getStatus());
					ps.setString(10, vo.getDataType());

					ps.setDouble(11, vo.getYisbx());
					ps.setDouble(12, vo.getYisbj());
					ps.setDouble(13, vo.getYislx());
					ps.setDouble(14, vo.getYingsbx());
					ps.setDouble(15, vo.getOverTimeInterest());
					ps.setInt(16, vo.getOverDays());
					ps.setString(17, vo.getStatus());
					ps.setString(18, vo.getDataType());
				}
				
				@Override
				public int getBatchSize() {
					return tempPaybacklendDetailVoList.size();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> getPaybacklendDetailURLList(HtmlPage htmlPage) {
		List<String> dataList = new ArrayList<String>();
		List trList = htmlPage.getByXPath("//table[@class='receivetab']//tr");
		if(trList != null && trList.size() > 0){
			for(int i=1; i<trList.size(); i++){
				HtmlTableRow row = (HtmlTableRow)trList.get(i);
				List<HtmlElement> tdList = row.getElementsByTagName("td");
				if(tdList != null && tdList.size() == 7){
					HtmlAnchor a = (HtmlAnchor)tdList.get(2).getElementsByTagName("a").get(0);
					String href = a.getHrefAttribute();
					dataList.add(href);
				}
			}
		}
		return dataList;
	}
	
	@Override
	public List<LateBackVo> getLateBackList(HtmlPage htmlPage) {
		List<LateBackVo> dataList = new ArrayList<LateBackVo>();
		
		List trList = htmlPage.getByXPath("//form[@id='paybacklendForm']//table//tr");
		if(trList != null && trList.size() > 0){
			for(int i=1; i<trList.size(); i++){
				HtmlTableRow row = (HtmlTableRow)trList.get(i);
				List<HtmlElement> tdList = row.getElementsByTagName("td");
				if(tdList != null && tdList.size() == 7){
					LateBackVo vo = new LateBackVo();
					vo.setPaymentDate(tdList.get(0).asText());
					vo.setCode(tdList.get(1).asText());
					
					HtmlAnchor a = (HtmlAnchor)tdList.get(2).getElementsByTagName("a").get(0);
					String href = a.getHrefAttribute();
					href = href.substring(href.lastIndexOf("/") + 1);
					vo.setBorrower(href);
					
					vo.setCapitalRecovery(Double.valueOf(tdList.get(3).asText().replaceAll("¥", "").replaceAll(",", "")));
					vo.setInterestRecovery(Double.valueOf(tdList.get(4).asText().replaceAll("¥", "").replaceAll(",", "")));
					vo.setOverDays(Integer.valueOf(tdList.get(5).asText()));
					
					String[] arr = tdList.get(6).asText().split("/");
					vo.setNeedReturnAmount(Double.valueOf(arr[0].replaceAll("¥", "").replaceAll(",", "")));
					vo.setPeriods(Integer.valueOf(arr[1]));
					
					dataList.add(vo);
				}
			}
		}
		
		return dataList;
	}

	@Override
	public void saveLateBackList(List<LateBackVo> lateBackList) {
		if(lateBackList == null || lateBackList.size() == 0){
			return;
		}
		
		try {
			final List<LateBackVo> tempLateBackList = lateBackList;
			
			StringBuffer sql = new StringBuffer();
			sql.append(" INSERT INTO `ppdai`.`late_back` (`payment_date`, `code`, `borrower`, `capital_recovery`, ");
			sql.append(" `interest_recovery`, `over_days`, `need_return_amount`, `periods`) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");
			sql.append(" ON DUPLICATE KEY UPDATE ");
			sql.append(" `borrower`=?, `capital_recovery`=?, `interest_recovery`=?, `over_days`=?, `need_return_amount`=?, `periods`=? ");
			
			getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					LateBackVo vo = tempLateBackList.get(i);
					ps.setString(1, vo.getPaymentDate());
					ps.setString(2, vo.getCode());
					ps.setString(3, vo.getBorrower());
					ps.setDouble(4, vo.getCapitalRecovery());
					ps.setDouble(5, vo.getInterestRecovery());
					ps.setInt(6, vo.getOverDays());
					ps.setDouble(7, vo.getNeedReturnAmount());
					ps.setInt(8, vo.getPeriods());

					ps.setString(9, vo.getBorrower());
					ps.setDouble(10, vo.getCapitalRecovery());
					ps.setDouble(11, vo.getInterestRecovery());
					ps.setInt(12, vo.getOverDays());
					ps.setDouble(13, vo.getNeedReturnAmount());
					ps.setInt(14, vo.getPeriods());
				}
				
				@Override
				public int getBatchSize() {
					return tempLateBackList.size();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void test(String dataType) {
		try {
			List<OverTimeRecordLast6MonthVo> overTimeRecordLast6MonthVos = new ArrayList<OverTimeRecordLast6MonthVo>();
			OverTimeRecordLast6MonthVo vo = new OverTimeRecordLast6MonthVo();
			vo.setFullUsername("cjm");
			vo.setListingid("1234");
			vo.setDateTime("2017-10-26");
			vo.setDayNumber(10);
			vo.setDataType(dataType);
			overTimeRecordLast6MonthVos.add(vo);
			
			final List<OverTimeRecordLast6MonthVo> tempOverTimeRecordLast6MonthVos = overTimeRecordLast6MonthVos;
			
			String sql = "INSERT INTO `ppdai`.`over_time_record_last_6_month`"
					+ " ("
					+ " `full_username`,"
					+ " `listingid`,"
					+ " `date_time`,"
					+ " `day_number`, `data_type`)"
					+ " VALUES (?, ?, ?, ?, ?)"
					+" ON DUPLICATE KEY UPDATE "
					+" `data_type` = ?";
			
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					OverTimeRecordLast6MonthVo overTimeRecordLast6MonthVo = tempOverTimeRecordLast6MonthVos.get(i);
					
					ps.setString(1, overTimeRecordLast6MonthVo.getFullUsername());
					ps.setString(2, overTimeRecordLast6MonthVo.getListingid());
					ps.setString(3, overTimeRecordLast6MonthVo.getDateTime());
					ps.setInt(4, overTimeRecordLast6MonthVo.getDayNumber());
					ps.setString(5, overTimeRecordLast6MonthVo.getDataType());
					
					ps.setString(6, overTimeRecordLast6MonthVo.getDataType());
				}
				
				@Override
				public int getBatchSize() {
					return tempOverTimeRecordLast6MonthVos.size();
				}
			});
			
//			int x = 1/0;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
