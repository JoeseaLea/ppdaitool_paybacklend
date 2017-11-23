package com.ppdai.ppdaitool.export;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.util.IOUtils;

import com.ppdai.ppdaitool.htmlpage.analysis.HtmlParser;
import com.ppdai.ppdaitool.vo.ColumnMetaDataVo;
import com.ppdai.ppdaitool.vo.HtmlParseVo;

public class HtmlParseResultExporter {
	private static final Logger logger = Logger.getLogger(HtmlParseResultExporter.class);
	
	public static void execute(String fileBasePath, String destFilePath, LinkedHashMap<String, String> searchParamsMap) {
		FileOutputStream out = null;
		BufferedOutputStream bos = null;
		
		//column meta data
		List<ColumnMetaDataVo> columnList = new ArrayList<ColumnMetaDataVo>();
		columnList.add(new ColumnMetaDataVo("FIELD1", "文件名", 50));
		columnList.add(new ColumnMetaDataVo("FIELD4", "利率", 25, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD3", "金额", 40, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD2", "等级", 25));
		columnList.add(new ColumnMetaDataVo("FIELD8", "学历", 40));
		columnList.add(new ColumnMetaDataVo("FIELD9", "借次", 30, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD10", "历史", 40));
		columnList.add(new ColumnMetaDataVo("FIELD14", "正常还", 30, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD15", "逾期", 40));
		columnList.add(new ColumnMetaDataVo("FIELD22", "鱼投", 40, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD19", "待还", 45, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD25", "借+待", 50, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD29", "时差", 30, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD30", "待还/累借款", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD31", "待还/最高债", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD35", "本次/最高单次", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD36", "借后负债/最高债", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD32", "本次金额/平均金额", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD37", "成功还款次数/借款次数", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD8_2", "学习形式", 40));
		columnList.add(new ColumnMetaDataVo("FIELD5_1", "借款结束时间", 55));
		columnList.add(new ColumnMetaDataVo("FIELD12", "最后一次借款时间", 60));
		columnList.add(new ColumnMetaDataVo("FIELD18", "累借", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD5", "期限", 25, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD7", "年龄", 25, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD6", "性别", 25));
		columnList.add(new ColumnMetaDataVo("FIELD13", "短借", 25));
		columnList.add(new ColumnMetaDataVo("FIELD21", "最高债", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD20", "单笔", 45, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD16", "月均", 50, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD33", "平均金额累计借款/借款次数", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD34", "本次借款距注册时间", 60, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD17", "6最逾天", 40, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD22_1", "投标开始日期", 60));
		columnList.add(new ColumnMetaDataVo("FIELD22_2", "投标结束日期", 60));
		columnList.add(new ColumnMetaDataVo("FIELD11", "第一次", 60));
		columnList.add(new ColumnMetaDataVo("FIELD22_3", "耗时秒数", 35, HSSFCell.CELL_TYPE_NUMERIC));
		columnList.add(new ColumnMetaDataVo("FIELD7_1", "注册时间", 55));
		columnList.add(new ColumnMetaDataVo("FIELD2_1", "用户名", 80));
		columnList.add(new ColumnMetaDataVo("FIELD8_1", "毕业院校", 50));
		
//		columnList.add(new ColumnMetaDataVo("FIELD26", "负债比", 60, HSSFCell.CELL_TYPE_NUMERIC));
//		columnList.add(new ColumnMetaDataVo("FIELD27", "单笔比", 60, HSSFCell.CELL_TYPE_NUMERIC));
//		columnList.add(new ColumnMetaDataVo("FIELD28", "借款比", 60, HSSFCell.CELL_TYPE_NUMERIC));
		
		//ext fields
		if(searchParamsMap.size() > 0){
			for(Iterator<String> it=searchParamsMap.keySet().iterator(); it.hasNext();){
				String username = it.next();
				columnList.add(new ColumnMetaDataVo(username.toUpperCase(), username, 60, HSSFCell.CELL_TYPE_NUMERIC));
			}
		}
		
		try{
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			
			File dirs = new File(fileBasePath);
			File[] files = dirs.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if(name.endsWith(".html")){
						return true;
					}else{
						return false;
					}
				}
			});
			
			if(files != null && files.length > 0){
				logger.info("待处理的页面数： " + files.length);
				
				for(File file : files){
					HtmlParseVo bean = HtmlParser.parse(file.getAbsolutePath(), searchParamsMap);
					if(bean != null){
						dataList.add(entity2Map(bean));
					}
				}

				logger.info("导出到Excel的记录数： " + dataList.size());
			}

			out = new FileOutputStream(new File(destFilePath));
			bos = new BufferedOutputStream(out);
			
			POIExcelCreater creater = new POIExcelCreater("", columnList, dataList, bos);
			creater.setTitleHeight(60);
			creater.setShowSequenceColumn(false);
			creater.setStartRowIndex(0);
			creater.setStartColIndex(0);
			HSSFCellStyle titleStyle = creater.getCellStyle("宋体", (short)15, HSSFColor.WHITE.index, true, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, false, HSSFColor.DARK_YELLOW.index);
			creater.setTitleStyle(titleStyle);
			creater.execute();
			
		}catch(Exception ex){
			logger.error("", ex);
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bos);
		}
	}
	
	private static Map<String, String> entity2Map(HtmlParseVo obj){
		if(obj == null){  
            return null;  
        }
		
        Map<String, String> map = new HashMap<String, String>();  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] arr = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor propDesc : arr) {  
                String key = propDesc.getName();  
   
                if (!"class".equalsIgnoreCase(key) && !"extFields".equalsIgnoreCase(key)) {
                    Method getter = propDesc.getReadMethod();  
                    Object objectValue = getter.invoke(obj);
                    map.put(key.toUpperCase(), convertToStringValue(objectValue)); //key大写
                }
            }
            
            Map<String, String> extFileds = obj.getExtFields();
            if(extFileds.size() > 0){
            	for(Iterator<String> it=extFileds.keySet().iterator(); it.hasNext();){
            		String username = it.next();
            		String value = extFileds.get(username);
                    map.put(username, value);
            	}
            }
            
        } catch (Exception ex) {  
            logger.error("", ex);
        } 
        
        return map;  
	}
	
	private static String convertToStringValue(Object objectValue) {
		if(objectValue == null) {
			return null;
		}
		
		String typeName = objectValue.getClass().getSimpleName();
		
		if("String".equals(typeName)){
			return String.valueOf(objectValue);
		}else if("Long".equals(typeName)){
			return objectValue.toString();
		}else if("Integer".equals(typeName)){
			return objectValue.toString();
		}else if("Double".equals(typeName)){
			return objectValue.toString();
		}else if("Float".equals(typeName)){
			return objectValue.toString();
		}else if("Boolean".equals(typeName)){
			return objectValue.toString();
		}else if("Short".equals(typeName)){
			return objectValue.toString();
		}else if("Byte".equals(typeName)){
			return objectValue.toString();
		}else{
			return String.valueOf(objectValue);
		}
	}
	
}
