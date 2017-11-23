package com.ppdai.ppdaitool.fetch;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractPageDataFetcher implements IPageDataFetcher {
	protected ConfigurableApplicationContext appContext = null;
	
	public AbstractPageDataFetcher(){
		appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
	
}
