package com.ppdai.ppdaitool.fetch;

public interface IPageDataFetcher {
	/**
	 * 黑名单
	 */
	String dataType_HMD = "黑名单";
	/**
	 * 收款中
	 */
	String dataType_SKZ = "收款中";
	/**
	 * 已还清
	 */
	String dataType_YHQ = "已还清";

	public void execute();
}
