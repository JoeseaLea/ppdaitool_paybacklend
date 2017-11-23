package com.ppdai.ppdaitool.pptools;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ppdai.ppdaitool.dao.PaybacklendDao;
import com.ppdai.ppdaitool.dao.impl.PaybacklendDaoImpl;

public class TransactionalTest {
	public static void main(String[] args) {
		ConfigurableApplicationContext appContext = null;
		try{
			appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			PaybacklendDao paybacklendDao = appContext.getBean("paybacklendDao", PaybacklendDaoImpl.class);
			paybacklendDao.test("收款中");
			paybacklendDao.test("已还清");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(appContext != null){
				appContext.close();
			}
		}
	}
	
}
