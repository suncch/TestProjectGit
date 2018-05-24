package com.sinyd.sframe.util.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("sqlTools")
public class SqlTools {
	private StringBuffer queryField;
	private StringBuffer sql = null;
	private static Logger log = LoggerFactory.getLogger(SqlTools.class);

	@Value("#{settings['jdbc.type']}")
	public String databaseType;

	public SqlTools() {
		this.sql = new StringBuffer();
	}

	public SqlTools(String sql) {
		this.sql = new StringBuffer(sql);
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public StringBuffer getSql() {
		return sql;
	}

	public void setSql(StringBuffer sql) {
		this.sql = sql;
	}

	public String getPageSql(int pagesize, int pagenum) {
		pagenum = pagenum > 0 ? pagenum : 1;
		pagesize = pagesize > 0 ? pagesize : 15;
		int toRows = pagenum * pagesize;
		int fromRows = (pagenum - 1) * pagesize + 1;

		StringBuffer bf = new StringBuffer(
				" SELECT * FROM (  SELECT ROW_.*, ROW_NUMBER() OVER( ");
		String orderByStr = "ORDER BY ID"; // if sql is not contains "order by" default sort by id, if uncontains id column then error
		StringBuffer orderBy = new StringBuffer();
		try {
			int index = this.sql.lastIndexOf("order by");
			if (index > 0) {
				queryField = new StringBuffer(this.sql.substring(0, index));
				orderBy = new StringBuffer(this.sql.substring(index));
				orderByStr = orderBy.toString();
			} else {
				queryField = new StringBuffer(this.sql);
			}
			StringBuffer finalSql = new StringBuffer();
			finalSql.append(queryField);
			finalSql.append(" ) ROW_ )  PAGINGTABLE  WHERE ROWNUM BETWEEN "
					+ fromRows + " AND " + toRows + " ");
			bf.append(orderByStr);
			bf.append(") AS ROWNUM  FROM ( ");
			bf.append(finalSql);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.debug("***********ReturnSql:" + bf.toString() + "*****************");
		return bf.toString();
	}
	
	/**
	 * Oracle分页查询方法
	 * @param pagesize
	 * @param pagenum
	 * @return 分页查询SQL语句
	 * @author 果占先
	 */
	public String getOraclePageSql(int pagesize, int pagenum) {
		pagenum = pagenum > 0 ? pagenum : 1;
		pagesize = pagesize > 0 ? pagesize : 15;
		int toRows = pagenum * pagesize;
		int fromRows = (pagenum - 1) * pagesize + 1;

		StringBuffer bf = new StringBuffer(
				" SELECT * FROM (  SELECT A.*,ROWNUM rn FROM ( ");
		//String orderByStr = "ORDER BY ID"; // if sql is not contains "order by" default sort by id, if uncontains id column then error
		//StringBuffer orderBy = new StringBuffer();
		try {
			/*
			int index = this.sql.lastIndexOf("order by");
			if (index > 0) {
				queryField = new StringBuffer(this.sql.substring(0, index));
				orderBy = new StringBuffer(this.sql.substring(index));
				orderByStr = orderBy.toString();
			} else {
				queryField = new StringBuffer(this.sql);
			}*/
			StringBuffer finalSql = new StringBuffer();
			//finalSql.append(queryField);
			finalSql.append(this.sql);
			finalSql.append(" ) A WHERE ROWNUM<=" + toRows + ") WHERE rn>=" + fromRows + " ");
			
			bf.append(finalSql);
			//bf.append(orderByStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.debug("***********ReturnSql:" + bf.toString() + "*****************");
		return bf.toString();
	}
}
