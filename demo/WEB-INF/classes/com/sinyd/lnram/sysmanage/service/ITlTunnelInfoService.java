package com.sinyd.lnram.sysmanage.service;

import java.util.Map;
import java.util.List;

import com.sinyd.lnram.util.gen.domain.TlTunnelInfo;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.lnram.sysmanage.vo.TlTunnelInfoSearchBeanVO;
import com.sinyd.sframe.util.vo.UserBean;

/*
 * @version 2.0
 */

public interface ITlTunnelInfoService {
	/**
	 * Description :get combox list by vo queryInfo
	 * @param queryInfo
	 * @return combox list
	 * @author auto_generator
	 */
	public List<Map<String, Object>> getComboxList(TlTunnelInfoSearchBeanVO queryInfo);

	/**
	 * Description :get record list records by vo queryInfo withnot paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	public Map<String, Object> getListWithoutPaging(TlTunnelInfoSearchBeanVO queryInfo);
	
	/**
	 * Description :get record list records by vo queryInfo with paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	public Map<String, Object> getListWithPaging(TlTunnelInfoSearchBeanVO queryInfo);
	
	/**
	 * Description :get single-table information by primary key 
	 * @param primary key 
	 * @return TlTunnelInfoVO
	 * @author auto_generator
	 */	
	public TlTunnelInfoVO getInfoByPK(String id);	
	
	/**
	 * Description :add method
	 * @param bean
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */
	public String save(TlTunnelInfo bean, UserBean user);
	
	/**
	 * Description :update method
	 * @param bean contains pk at least
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */
	public String update(TlTunnelInfo bean, UserBean user);

	/**
	 * Description :delete method
	 * @param pk
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */	
	public String delete(TlTunnelInfo bean);
}