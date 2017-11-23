package com.ppdai.ppdaitool.pptools;

import org.junit.Test;

import com.ppdai.ppdaitool.utils.Dama2Util;
import com.ppdai.ppdaitool.utils.Dama2Util.DecodeResult;
import com.ppdai.ppdaitool.utils.FileUtils;

public class Dama2UtilTest {

	@Test
	public void testGetValidateCodeString() {
		Dama2Util dama = new Dama2Util(51147, "0a7c1144d71e933c3356de4ba526f12c", "damasecurity", "joesea");
    	
		DecodeResult decodeResult = dama.d2File(4, 60, FileUtils.getFileHex("F:/imageSort/targetPIC/valiCodeImg.jpg"));
		System.out.println(decodeResult.result);
	}

}
