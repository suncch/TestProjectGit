package com.sinyd.sframe.util.database;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import com.sinyd.platform.database.ConnectionUtil;
import com.sinyd.platform.database.SqlTemplateUtil;

public class AbstractSimpleDao  extends
com.sinyd.platform.database.AbstractDao{

	private static Logger log = LoggerFactory.getLogger(AbstractSimpleDao.class);

	private SimpleJdbcInsert simpleJdbcInsert;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SqlTools sqlTools;

	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate(
					ConnectionUtil.getDataSourceConByName(getDataSourceName()));
		}
		return jdbcTemplate;
	}

	private SimpleJdbcInsert getSimpleJdbcInsert(String tableName, String pkName) {
		simpleJdbcInsert = new SimpleJdbcInsert(
				ConnectionUtil.getDataSourceConByName(getDataSourceName()))
				.withTableName(tableName).usingGeneratedKeyColumns(pkName);
		return simpleJdbcInsert;
	}

	/**
	 * 插入javaBean，并返回插入记录的主键
	 * 注意：调用此方法时必须是数据库自动生成的int型主键
	 * @param bean 可以是普通的数据库对象，也可以是封装好的MAP,如果为普通javaBean要求具有getInfoDateMap方法
	 * @param tableName
	 * @param pkName
	 * @return 返回插入记录的主键值，如果插入失败返回0
	 */
	@SuppressWarnings("unchecked")
	public <T> int insert(T bean, String tableName, String pkName) {
		try {
			Map<String,Object> o = null;
			if(bean instanceof java.util.Map){//判断传入的对象是否为Map
				o = (Map<String,Object>)bean;
			}else{
				o = (Map<String, Object>)bean.getClass().getMethod("getInfoMap").invoke(bean);
			}
			//增加"create_timestamp"�?last_update_timestamp"
			Date now = new Date();
			o.put("enable_flag","1");
			o.put("create_timestamp", now);
			o.put("last_update_timestamp", now);
			Number newId = (Number) getSimpleJdbcInsert(tableName, pkName)
					.executeAndReturnKey(o);
			return newId.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}
	
	/**
	 * 批量插入指定对象，对象类型：可以是普通的数据库对象，也可以是封装好的Map<String,Object>，如果是普通的javaBean要求具有getInfoMap方法
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> void insertBatch(List<T> list,String insertSqlKey) {
		if(list == null || list.size() == 0){
			return;
		}
		List<Map<String,Object>> paramList= new ArrayList<Map<String,Object>>();
		if(list.get(0) instanceof java.util.Map){//判断传入的对象是否为Map
			paramList = (List<Map<String, Object>>) list;
		}else{
			for(T bean : list){
				try {
					// support to gen2.0 and gen single template 
					Map<String, Object> map = (Map<String, Object>) bean
							.getClass()
							.getMethod("setSymbolInsert", Map.class)
							.invoke(bean,
									bean.getClass().getMethod("getInfoMap")
											.invoke(bean));
					paramList.add(map);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		batchUpdateByTemplate(insertSqlKey, paramList.get(0), paramList);
	}
	
	/**
	 * 批量更新指定对象，对象类型：可以是普通的数据库对象，也可以是封装好的Map<String,Object>，
	 * 如果是普通的javaBean要求具有getInfoMap方法
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> void updateBatch(List<T> list, String updateSqlKey) {
		if (list == null || list.size() == 0) {
			return;
		}
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		if (list.get(0) instanceof java.util.Map) {// 判断传入的对象是否为Map
			paramList = (List<Map<String, Object>>) list;
		} else {
			for (T bean : list) {
				try {
					// support to gen2.0 and gen single template
					Map<String, Object> map = (Map<String, Object>) bean
							.getClass()
							.getMethod("setSymbolUpdate", Map.class)
							.invoke(bean,
									bean.getClass().getMethod("getInfoMap")
											.invoke(bean));
					paramList.add(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		batchUpdateByTemplate(updateSqlKey, paramList.get(0), paramList);
	}
	
	/**
	 * 将查询结果封装为指定对象返回
	 * @param sqlKey
	 * @param paramMap
	 * @param clazz
	 * @return
	 */
	public <T> List<T> qryObjList(String sqlKey,Map<String,Object> paramMap,Class<T> clazz) {
		String sql = SqlTemplateUtil.getSqlString(sqlKey, paramMap);
		return getSqlTemplateCon().query(sql, paramMap, new BeanPropertyRowMapper<T>(clazz));
	}
	
	/**
	 * 将查询结果封装为指定对象返回
	 * @param sqlKey
	 * @param paramMap
	 * @param clazz
	 * @return
	 */
	public <T> T qryObj(String sqlKey,Map<String,Object> paramMap,Class<T> clazz) {
		String sql = SqlTemplateUtil.getSqlString(sqlKey, paramMap);
		return getSqlTemplateCon().queryForObject(sql, paramMap, new BeanPropertyRowMapper<T>(clazz));
	}
	
	/**
	 * 查询结果封装为前提liger UI表格要求的格式
	 * @param sqlKey
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> getGridNoPageData(String sqlKey,Map<String,Object> paramMap){
		//String sql = SqlTemplateUtil.getSqlString(sqlKey, paramMap);
		List<Map<String,Object>> list = queryForListByTemplate(sqlKey, paramMap);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("Rows", list);
		return resMap;
	}
	/**
	 * 插入javaBean，并返回插入记录的主键
	 * 注意：调用此方法时必须是数据库自动生成的int型主键
	 * @param bean 可以是普通的数据库对象，也可以是封装好的MAP,如果为普通javaBean要求具有getInfoDateMap方法
	 * @param tableName
	 * @param pkName
	 * @return 返回插入记录的主键值，如果插入失败返回0
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	public <T> int insertData(T bean, String tableName, String pkName) throws Exception {
		
			Map<String,Object> o = null;
			if(bean instanceof java.util.Map){//判断传入的对象是否为Map
				o = (Map<String,Object>)bean;
			}else{
				o = (Map<String, Object>)bean.getClass().getMethod("getInfoMap").invoke(bean);
			}
			//增加"create_timestamp"�?last_update_timestamp"
			Date now = new Date();
			o.put("create_timestamp", now);
			o.put("last_update_timestamp", now);
			Number newId = (Number) getSimpleJdbcInsert(tableName, pkName)
					.executeAndReturnKey(o);
			
			return newId.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> String insertData2(T bean, String tableName, String pkName) throws Exception {
		
			Map<String,Object> o = null;
			if(bean instanceof java.util.Map){//判断传入的对象是否为Map
				o = (Map<String,Object>)bean;
			}else{
				o = (Map<String, Object>)bean.getClass().getMethod("getInfoMap").invoke(bean);
			}
			//增加"create_timestamp"�?last_update_timestamp"
			Date now = new Date();
			o.put("create_timestamp", now);
			o.put("last_update_timestamp", now);
			o.put("operate_timestamp", now);
			KeyHolder newId = (KeyHolder) getSimpleJdbcInsert(tableName, pkName)
					.executeAndReturnKeyHolder(o);
			
			return (String)newId.getKeys().get(pkName);
	}
	
	/**
	 * 重写分页查询
	 * @param sqlKey SqlString中的Key
	 * @param pagesize 每页显示条数
	 * @param pagenum 第几页
	 * @param paramMap 参数
	 * @return 查询结果
	 */	
	@Override
	public List<Map<String, Object>> pagingForListByTemplate(String sqlKey,
			int pagesize, int pagenum, Map<String, Object> paramMap) {
		if ("sqlserver".equals(sqlTools.getDatabaseType())) {
			String sql = "";
			String sqlStr = SqlTemplateUtil.getSqlString(sqlKey, paramMap)
					.toLowerCase();
			sql = new SqlTools(sqlStr).getPageSql(pagesize, pagenum);
			return super.getSqlTemplateCon().queryForList(sql, paramMap);
		}if ("oracle".equals(sqlTools.getDatabaseType())) {
			String sql = "";
			String sqlStr = SqlTemplateUtil.getSqlString(sqlKey, paramMap);
			sql = new SqlTools(sqlStr).getOraclePageSql(pagesize, pagenum);
			return super.getSqlTemplateCon().queryForList(sql, paramMap);
		} else {
			return super.pagingForListByTemplate(sqlKey, pagesize, pagenum,
					paramMap);
		}
	}

}

