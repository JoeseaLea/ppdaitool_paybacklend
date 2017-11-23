package com.ppdai.ppdaitool.pptools;

import com.ppdai.ppdaitool.utils.PropertiesUtil;

public class ConfigTest {
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getInstance().getProperty("username", "123"));
		System.out.println(PropertiesUtil.getInstance().getProperty("usernamew", "123"));
	}
}
