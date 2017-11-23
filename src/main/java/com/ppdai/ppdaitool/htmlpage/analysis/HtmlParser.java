package com.ppdai.ppdaitool.htmlpage.analysis;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.ppdai.ppdaitool.utils.HtmlCleanerUtils;
import com.ppdai.ppdaitool.vo.HtmlParseVo;

/**
 * HTML页面解析器类
 */
public class HtmlParser {
	private static final Logger logger = Logger.getLogger(HtmlParser.class);
	
	public static HtmlParseVo parse(String filename, LinkedHashMap<String, String> searchParamsMap){
		HtmlParseVo bean = new HtmlParseVo();
		try{
			//init ext fields
			if(searchParamsMap.size() > 0){
				for(Iterator<String> it=searchParamsMap.keySet().iterator(); it.hasNext();){
					bean.getExtFields().put(it.next().toUpperCase(), "0");
				}
			}
			
			File file = new File(filename);
			
			//文件名
			bean.setField1(file.getName().toLowerCase().replace(".html", ""));
			
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean(file);
			
			
			//魔镜等级
			TagNode tagNode = HtmlCleanerUtils.getSingleNode(rootNode, "//div[@class='newLendDetailInfoLeft']/a[@class='altQust']/span");
			if(tagNode != null){
				String creditRating = StringUtils.trimToEmpty(tagNode.getAttributeByName("class")).replace("creditRating", "").trim();
				bean.setField2(creditRating);
			}
			
			//标的编号
			tagNode = HtmlCleanerUtils.getSingleNode(rootNode, "//div[@class='newLendDetailInfoLeft']/a[@class='username']");
			if(tagNode != null){
				String username = tagNode.getAttributeByName("href");
				username = username.substring(username.lastIndexOf("/") + 1);
				bean.setField2_1(username);
			}
			
			TagNode[] tagNodes = HtmlCleanerUtils.getMultiNode(rootNode, "//div[@class='newLendDetailMoneyLeft']/dl");
			if(tagNodes != null){
				TagNode node;
				
				//借款金额
				node = HtmlCleanerUtils.getSingleElementByName(tagNodes[0], "dd");
				if(node != null){
					bean.setField3(node.getText().toString().replace("¥", "").replace(",", "").trim());
				}
				
				//年利率
				node = HtmlCleanerUtils.getSingleElementByName(tagNodes[1], "dd");
				if(node != null){
					bean.setField4(node.getText().toString().replace("%", "").trim());
				}
				
				//期限
				node = HtmlCleanerUtils.getSingleElementByName(tagNodes[2], "dd");
				if(node != null){
					bean.setField5(node.getText().toString().replace("个月", "").trim());
				}
			}
			
			//还款结束时间
			TagNode repayNode = HtmlCleanerUtils.getSingleNode(rootNode, "//div[@class='newLendDetailRefundLeft']//div[@class='item']");
			if(repayNode != null){
				if(StringUtils.trimToEmpty(repayNode.getText().toString()).indexOf("结束时间") >= 0){
					String leftTime = HtmlCleanerUtils.getSingleNodeText(repayNode, "//span[@id='leftTime']/text()");
					bean.setField5_1(leftTime);
				}
			}
			
			tagNodes = HtmlCleanerUtils.getMultiNode(rootNode, "//div[@class='main']//div[@class='tab-contain']");
			if(tagNodes != null){
				for(TagNode tabNode : tagNodes){
					TagNode h3Node = HtmlCleanerUtils.getSingleElementByName(tabNode, "h3");
					if(h3Node != null){
						String h3Text = h3Node.getText().toString().trim();
						
						//借款人信息
						if(h3Text.indexOf("借款人信息") >= 0){
							TagNode[] spanArr = HtmlCleanerUtils.getMultiNode(tabNode, "//span");
							if(spanArr != null){
								//性别
								if(spanArr.length > 0){
									String sex = spanArr[0].getText().toString().trim();
									bean.setField6((sex.equals("男") ? "M" : "F"));
								}
								
								//年龄
								if(spanArr.length > 1)
									bean.setField7(spanArr[1].getText().toString().trim());
								
								//注册时间
								if(spanArr.length > 2)
									bean.setField7_1(spanArr[2].getText().toString().trim());
								
								//文化程度
								if(spanArr.length > 3)
									bean.setField8(spanArr[3].getText().toString().trim());
								
								//毕业院校
								if(spanArr.length > 4)
									bean.setField8_1(spanArr[4].getText().toString().trim());
								
								//学习形式
								if(spanArr.length > 5)
									bean.setField8_2(spanArr[5].getText().toString().trim());
							}
						}
						
						//统计信息
						if(h3Text.indexOf("统计信息") >= 0){
							TagNode[] spanArr = HtmlCleanerUtils.getMultiNode(tabNode, "//span[@class='num']");
							if(spanArr != null){
								//成功借款次数
								if(spanArr.length > 0)
									bean.setField9(spanArr[0].getText().toString().replace("次", "").trim());
								
								//第一次成功借款时间
								if(spanArr.length > 1)
									bean.setField11(spanArr[1].getText().toString().trim());
								
								//历史记录
								if(spanArr.length > 2)
									bean.setField10(spanArr[2].getText().toString().replace("次流标", "").replace("次撤标", "").replace("次失败", "").trim().replace("，", "-"));
								
								//成功还款次数
								if(spanArr.length > 3)
									bean.setField14(spanArr[3].getText().toString().replace("次", "").trim());
								
								//逾期
								if(spanArr.length > 5)
									bean.setField15(spanArr[5].getText().toString().replace("次", "").trim() +  "-" + ((TagNode)spanArr[6]).getText().toString().replace("次", "").trim());
								
								//累计借款金额
								if(spanArr.length > 7)
									bean.setField18(spanArr[7].getText().toString().replace("¥", "").replace(",", "").trim());
								
								//待还金额
								if(spanArr.length > 8)
									bean.setField19(spanArr[8].getText().toString().replace("¥", "").replace(",", "").trim());

								//单笔最高借款金额
								if(spanArr.length > 10)
									bean.setField20(spanArr[10].getText().toString().replace("¥", "").replace(",", "").trim());

								//历史最高负债
								if(spanArr.length > 11)
									bean.setField21(spanArr[11].getText().toString().replace("¥", "").replace(",", "").trim());
							}

							TagNode[] tableArr = HtmlCleanerUtils.getMultiNode(tabNode, "//table[@class='lendDetailTab_tabContent_table1 normal']");
							if(tableArr != null){
								if(tableArr.length > 0){
									//历史成功借款列表
									TagNode trNode = HtmlCleanerUtils.getSingleNode(tableArr[0], "//tr[@class='tab-list'][1]");
									if(trNode != null){
										TagNode[] tdArr = HtmlCleanerUtils.getMultiElementByName(trNode, "td");
										
										//是否短借
										String qx = tdArr[2].getText().toString().trim();
										if(qx.indexOf("天") > 0){
											bean.setField13("Y");
										}else{
											bean.setField13("N");
										}
										
										//最后一次成功借款时间
										bean.setField12(tdArr[4].getText().toString().trim());
									}
								}
								
								if(tableArr.length > 1){
									//未来6个月的待还记录
									double repay = 0.0;
									TagNode[] tdArr = HtmlCleanerUtils.getMultiNode(tableArr[1], "//tr[2]/td");
									for(TagNode tdNode : tdArr){
										String value = tdNode.getText().toString().replace("¥", "").replace(",", "").trim();
										if(Double.parseDouble(value) > repay){
											repay = Double.parseDouble(value);
										}
									}
									
									//未来6个月的最大待还金额
									bean.setField16(String.valueOf(repay));
								}
								
								if(tableArr.length > 2){
									//过去6个月有回款记录的逾期天数
									int days = 0;
									TagNode[] tdArr = HtmlCleanerUtils.getMultiNode(tableArr[2], "//tr[2]/td");
									for(TagNode tdNode : tdArr){
										String value = tdNode.getText().toString().trim();
										days += Integer.parseInt(value);
									}
									
									//过去6个月的逾期总天数
									bean.setField17(String.valueOf(days));
								}
							}
						}
						
						//投标记录
						if(h3Text.indexOf("投标记录") >= 0){
							TagNode[] olArr = HtmlCleanerUtils.getMultiNode(tabNode, "//div[@class='scroll-area']/ol");
							if(olArr != null){
								String currentTime = "";
								String lastTime = "";
								String amount = "0";
								
								for(int i=0; i<olArr.length; i++){
									TagNode olNode = olArr[i];
									
									//投标人
									String name = HtmlCleanerUtils.getSingleNodeText(olNode, "//li[1]/a[@class='listname']/text()");
									
									//投标时间
									currentTime = HtmlCleanerUtils.getSingleNodeText(olNode, "//li[5]/text()");
									if(i == 0){
										lastTime = currentTime;
										
										//标的没有结束时间，用最新的投标记录时间代替
										if(StringUtils.isEmpty(bean.getField5_1())){
											if(lastTime.indexOf(" ") > 0){
												bean.setField5_1(lastTime.substring(0, lastTime.indexOf(" ")).trim());
											}else{
												bean.setField5_1(lastTime);
											}
										}
									}
									
									//投标金额
									amount = HtmlCleanerUtils.getSingleNodeText(olNode, "//li[4]/text()").toString().replace("¥", "").trim();
									
									if(name.endsWith("37825") && "0".equals(bean.getField22())){
										//pdu4212037825（鱼投）
										bean.setField22(amount);
									}
									
									//动态查询若干个鱼投名称
									if(searchParamsMap.size() > 0){
										for(Iterator<String> it=searchParamsMap.keySet().iterator(); it.hasNext();){
											String username = it.next();
											String keyword = searchParamsMap.get(username);
											
											if(name.indexOf(keyword) >= 0 && "0".equals(bean.getExtFields().get(username.toUpperCase()))){
												bean.getExtFields().put(username.toUpperCase(), amount);
												break;
											}
										}
									}
								}
								
								bean.setField22_1(currentTime);
								bean.setField22_2(lastTime);
								
								//投标耗时秒数
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
								Date date2 = sdf.parse(lastTime);
								Date date1 = sdf.parse(currentTime);
								long seconds = (date2.getTime() - date1.getTime()) / 1000;
								bean.setField22_3(String.valueOf(seconds));
							}
						}
						
						//还款记录
						/*
						if(h3Text.indexOf("还款记录") >= 0){
							TagNode[] trArr = HtmlCleanerUtils.getMultiNode(tabNode, "//table[@class='lendDetailTab_tabContent_table1 pay-record']//tr");
							if(trArr != null){
								for(int i=1; i<trArr.length; i++){ //行
									TagNode trNode = trArr[i];
									TagNode[] tdArr = HtmlCleanerUtils.getMultiElementByName(trNode, "td");
									String repayStatus = HtmlCleanerUtils.getSingleNodeText(tdArr[6], "//span/text()");
									if(repayStatus.indexOf("等待还款") >= 0){
										//逾期的期次
										bean.setField22_4(String.valueOf(i));
										
										//逾期天数
										String overdueDays = HtmlCleanerUtils.getSingleNodeText(tdArr[5], "//span/text()");
										bean.setField22_5(overdueDays.replace("/", "").replace("天", "").trim());
									}
								}
							}
						}
						*/
						
					}
					
				}
			}
			
			bean.setField24(""); //gen
			
			DecimalFormat df1 = new DecimalFormat("####");
			DecimalFormat df2 = new DecimalFormat("####.######");
			
			bean.setField25(df1.format(Double.parseDouble(bean.getField3()) + Double.parseDouble(bean.getField19()))); //借+待 = 借款金额 + 待还金额
			if(Double.parseDouble(bean.getField21()) > 0){
				bean.setField26(df2.format(Double.parseDouble(bean.getField25()) / Double.parseDouble(bean.getField21()))); //负债比 = 借+待 / 最高负债
			}
			if(Double.parseDouble(bean.getField20()) > 0){
				bean.setField27(df2.format(Double.parseDouble(bean.getField3()) / Double.parseDouble(bean.getField20()))); //单笔比 = 借款金额 / 单笔最高借款金额
			}
			if(Double.parseDouble(bean.getField9()) > 0){
				bean.setField28(df2.format(Double.parseDouble(bean.getField14()) / Double.parseDouble(bean.getField9()))); //借款比 = 成功还款次数 / 成功借款次数
			}
			
			if(Double.parseDouble(bean.getField18()) > 0){
				bean.setField30(df2.format(Double.parseDouble(bean.getField19()) / Double.parseDouble(bean.getField18()))); //待还/累计借款 = 待还金额 / 累计借款金额
			}
			
			if(Double.parseDouble(bean.getField21()) > 0){
				bean.setField31(df2.format(Double.parseDouble(bean.getField19()) / Double.parseDouble(bean.getField21()))); //待还/最高债 = 待还金额 / 最高负债
			}
			
			//平均金额   = 累计借款金额/成功借款次数
			if(Double.parseDouble(bean.getField9()) > 0){
				bean.setField33(df2.format(Double.parseDouble(bean.getField18()) / Double.parseDouble(bean.getField9())));
			}
			
			//本次金额/平均金额
			if(Double.parseDouble(bean.getField33()) > 0){
				bean.setField32(df2.format(Double.parseDouble(bean.getField3()) / Double.parseDouble(bean.getField33()))); // 借款金额 / 平均金额
			}
			
			
			//本次/最高单次
			if(Double.parseDouble(bean.getField20()) > 0){
				bean.setField35(df2.format(Double.parseDouble(bean.getField3()) / Double.parseDouble(bean.getField20()))); // 借款金额 / 单笔
			}
			
			//借后负债/最高债
			if(Double.parseDouble(bean.getField21()) > 0){
				bean.setField36(df2.format(Double.parseDouble(bean.getField25()) / Double.parseDouble(bean.getField21()))); //借+待 / 最高债
			}
			
			//成功还款次数/借款次数
			if(Double.parseDouble(bean.getField9()) > 0){
				bean.setField37(df2.format(Double.parseDouble(bean.getField14()) / Double.parseDouble(bean.getField9()))); // 正常还 / 借次
			}
			
			
			//本次借款距注册时间
			if(StringUtils.isNotEmpty(bean.getField5_1()) && StringUtils.isNotEmpty(bean.getField7_1())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");
				Date d5_1 = sdf.parse(bean.getField5_1()); //借款结束时间
				Date d7_1 = sdf.parse(bean.getField7_1()); //注册时间
				String field34 = String.valueOf((d5_1.getTime() - d7_1.getTime()) / 1000 / 60 / 60 / 24); 	//天
				bean.setField34(field34);
			}
			
			//时差
			if(StringUtils.isNotEmpty(bean.getField12()) && StringUtils.isNotEmpty(bean.getField5_1())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");
				Date d12 = sdf.parse(bean.getField12()); //最后一次成功借款时间
				Date d5_1 = sdf.parse(bean.getField5_1()); //借款结束时间
				String field29 = String.valueOf((d5_1.getTime() - d12.getTime()) / 1000 / 60 / 60 / 24); 	//天
				bean.setField29(field29);
			}
			
			//排除无关键数据的记录
			if(StringUtils.isEmpty(bean.getField10()) && (StringUtils.isEmpty(bean.getField18()) || Double.parseDouble(bean.getField18()) <= 0)){
				return null;
			}
			
			return bean;
		}catch(Exception ex){
			logger.error("", ex);
			return null;
		}
	}
	
}
