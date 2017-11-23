package com.ppdai.ppdaitool.vo;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class ColumnMetaDataVo {
	private String name;
	private String label;
	private int width;
	private int cellType; //0数值，1字符串

	public ColumnMetaDataVo(){

	}
	
	public ColumnMetaDataVo(String name, String label, int width){
		this(name, label, width, HSSFCell.CELL_TYPE_STRING);
	}
	
	public ColumnMetaDataVo(String name, String label, int width, int cellType){
		this.name = name;
		this.label = label;
		this.width = width;
		this.cellType = cellType;
	}

	/**
	 * 字段名
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 字段标签
	 */
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 字段宽度
	 */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getCellType() {
		return cellType;
	}

	public void setCellType(int cellType) {
		this.cellType = cellType;
	}

}