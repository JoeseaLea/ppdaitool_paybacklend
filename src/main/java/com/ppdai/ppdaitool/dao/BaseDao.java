package com.ppdai.ppdaitool.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;


public abstract class BaseDao {
	private static final Logger logger = Logger.getLogger(BaseDao.class);
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
	
	public void commit() {
		try {
			this.jdbcTemplate.getDataSource().getConnection().commit();
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
}
