package com.sinyd.lnram.sysmanage.persist;

import java.util.List;
import java.util.Map;

import com.sinyd.lnram.util.gen.domain.TlTunnelInfo;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.lnram.sysmanage.vo.TlTunnelInfoSearchBeanVO;

/*
 * @version 2.0
 */

public interface ITlTunnelInfoDao {

	/**
	 * Description :get record list records by vo queryInfo withnot paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	public List<Map<String, Object>> getListWithoutPaging(TlTunnelInfoSearchBeanVO queryInfo);
	
	/**
	 * Description :get record list records by vo queryInfo with paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	public List<Map<String, Object>> getListWithPaging(TlTunnelInfoSearchBeanVO queryInfo);
	
	/**
	 * Description :get record list count num by vo queryInfo
	 * @param queryInfo
	 * @return records count num
	 * @author auto_generator
	 */
	public int getListCountNum(TlTunnelInfoSearchBeanVO queryInfo);
	
	/**
	 * Description :get single-table information vo by primary key 
	 * @param primary key 
	 * @return TlTunnelInfoVO
	 * @author auto_generator
	 */	
	public TlTunnelInfoVO getInfoByPK(String id);	
	
	/**
	 * Description :get single-table information domain by primary key 
	 * @param primary key 
	 * @return TlTunnelInfo
	 * @author auto_generator
	 */	
	public TlTunnelInfo getDomainByPK(String id);	

	/**
	 * Description :insert a record, nonsupport null val
	 * @param bean
	 * @return success num or 0
	 * @author auto_generator
	 */
	public int addNewRecord(TlTunnelInfo bean);

	/**
	 * Description :update a record replace all, need primary key, support null val
	 * @param bean
	 * @return success num or 0
	 * @author auto_generator
	 */
	public int updateRecordAll(TlTunnelInfo bean);

	/**
	 * Description :update a record replace columns, need primary key, nonsupport null val
	 * @param bean
	 * @return success num or 0
	 * @author auto_generator
	 */
	public int updateRecordCols(TlTunnelInfo bean);

	/**
	 * Description :delete record by primary key
	 * @param pk
	 * @return success num or 0
	 * @author auto_generator
	 */	
	public int deleteRecordByPK(String id);

	/**
	 * Description :delete record by domain bean, nonsupport null val
	 * @param bean
	 * @return success num or 0
	 * @author auto_generator
	 */	
	public int deleteRecordByDomain(TlTunnelInfo bean);
	
	/**
	 * Description :get list by "and" method, need new TlTunnelInfo() include query-params
	 * @param queryInfo
	 * @param isExludePKFlag, true is exclude primary key, false is include primary key
	 * @return TlTunnelInfo list
	 * @author auto_generator
	 */
	public List<TlTunnelInfo> getSingleTableListByAndMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag);
	
	/**
	 * Description :get list by "or" method, need new TlTunnelInfo() include query-params
	 * @param queryInfo
	 * @param isExludePKFlag, true is exclude primary key, false is include primary key
	 * @return TlTunnelInfo list
	 * @author auto_generator
	 */
	public List<TlTunnelInfo> getSingleTableListByOrMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag);
}