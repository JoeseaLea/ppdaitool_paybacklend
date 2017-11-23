package com.ppdai.ppdaitool.dao;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ppdai.ppdaitool.vo.LateBackVo;
import com.ppdai.ppdaitool.vo.PaybacklendDetailVo;
import com.ppdai.ppdaitool.vo.PaybacklendVo;

public interface PaybacklendDao {
	public List<PaybacklendVo> getPaybacklendList(HtmlPage htmlPage, String dataType);
	public void savePaybacklendList(List<PaybacklendVo> paybacklendList);
	
	public HtmlPage loadDetailListForPaybacklend(HtmlPage htmlPage);

	public List<PaybacklendDetailVo> getPaybacklendDetailList(HtmlPage htmlPage, String dataType);
	public void savePaybacklendDetailList(List<PaybacklendDetailVo> paybacklendDetailList);
	
	public List<String> getPaybacklendDetailURLList(HtmlPage htmlPage);
	
	public List<LateBackVo> getLateBackList(HtmlPage htmlPage);
	public void saveLateBackList(List<LateBackVo> lateBackList);
	
	public void test(String dataType);
}
