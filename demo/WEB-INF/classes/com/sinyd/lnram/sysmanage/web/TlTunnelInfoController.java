package com.sinyd.lnram.sysmanage.web;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinyd.lnram.sysmanage.service.ITlTunnelInfoService;
import com.sinyd.lnram.util.gen.domain.TlTunnelInfo;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.lnram.sysmanage.vo.TlTunnelInfoSearchBeanVO;
import com.sinyd.sframe.util.GlobalVal;
import com.sinyd.sframe.util.vo.UserBean;

/*
 * @version 2.0
 * @author
 */

@Controller
public class TlTunnelInfoController {
	private static Logger log = LoggerFactory.getLogger(TlTunnelInfoController.class);
	
	@Autowired
	private ITlTunnelInfoService tltunnelinfoService;

	/**
	 * Description :get combox list by vo queryInfo
	 * @param queryInfo
	 * @return combox list
	 * @author auto_generator
	 */
	@RequestMapping(value = "/sysmanage/tltunnelinfogetcomboxlist.do", method = {RequestMethod.GET})
	@ResponseBody
	public List<Map<String, Object>> getComboxList(TlTunnelInfoSearchBeanVO queryInfo) {
		return tltunnelinfoService.getComboxList(queryInfo);
	}
		
	/**
	 * Description :get record list records by vo queryInfo withnot paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	@RequestMapping(value = "/sysmanage/tltunnelinfowithoutpaginglist.do", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> getListWithoutPaging(TlTunnelInfoSearchBeanVO queryInfo) {
		return tltunnelinfoService.getListWithoutPaging(queryInfo);
	}

	/**
	 * Description :get record list records by vo queryInfo with paging
	 * @param queryInfo
	 * @return record list
	 * @author auto_generator
	 */
	@RequestMapping(value = "/sysmanage/tltunnelinfowithpaginglist.do", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> getListWithPaging(TlTunnelInfoSearchBeanVO queryInfo) {
		return tltunnelinfoService.getListWithPaging(queryInfo);
	}
	
	/**
	 * Description :get single-table information by primary key 
	 * @param primary key 
	 * @return TlTunnelInfoVO
	 * @author auto_generator
	 */	
	@RequestMapping(value = "/sysmanage/tltunnelinfoinfobypk.do", method = {RequestMethod.GET})
	@ResponseBody
	public TlTunnelInfoVO getInfoByPK(String id) {
		return tltunnelinfoService.getInfoByPK(id);
	}	

	/**
	 * Description :add method
	 * @param bean
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */	
	@RequestMapping(value = "/sysmanage/tltunnelinfosave.do", method = {RequestMethod.POST})
	@ResponseBody
	public String doSave(HttpServletRequest request, TlTunnelInfo bean) {
		String result = "";
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(GlobalVal.USER_SESSION);
		try {
			result = tltunnelinfoService.save(bean, user);
		} catch (Exception e) {
			log.error(e.getMessage());
			result = "error";
		}
		/*
		// record log	
		if("success".equals(result)){
			String msg = "log content";
			SysTools.saveLog(request, msg); // record log method
		}
		*/
		return result;
	}

	/**
	 * Description :update method
	 * @param bean
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */	
	@RequestMapping(value = "/sysmanage/tltunnelinfoupdate.do", method = {RequestMethod.POST})
	@ResponseBody
	public String doUpdate(HttpServletRequest request, TlTunnelInfo bean) {
		String result = "";
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(GlobalVal.USER_SESSION);
		try {
			result = tltunnelinfoService.update(bean, user);
		} catch (Exception e) {
			log.error(e.getMessage());
			result = "error";
		}
		/*			
		// record log	
		if("success".equals(result)){
			String msg = "log content";
			SysTools.saveLog(request, msg); // record log method
		}
		*/
		return result;
	}

	/**
	 * Description :delete method
	 * @param pk
	 * @return "success" or "error" or user defined
	 * @author auto_generator
	 */		
	@RequestMapping(value = "/sysmanage/tltunnelinfodelete.do", method = {RequestMethod.POST})
	@ResponseBody
	public String doDelete(HttpServletRequest request, TlTunnelInfo bean) {
		String result = tltunnelinfoService.delete(bean);
		/*
		// record log	
		if("success".equals(result)){
			String msg = "log content";
			SysTools.saveLog(request, msg); // record log method
		}
		*/
		return result;
	}
}