package com.sinyd.sframe.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.sinyd.lnram.sysmanage.service.ILoginManageService;
import com.sinyd.lnram.util.MenuTreeBean;
import com.sinyd.lnram.util.SysTools;
import com.sinyd.lnram.util.gen.domain.SysFunction;
import com.sinyd.platform.syscontext.PlatformGlobalVar;
import com.sinyd.platform.uivo.vo.MenuBean;

public class GlobalVal {
	private static Logger log = LoggerFactory.getLogger(GlobalVal.class);
	public static  ApplicationContext ctx;
	
	public static final String USER_SESSION = "fam_userSession";
	public static final String ALL_DEFAULT_VAL = "0";
	public static final String PHONE_FUNC_ID_LIST= PlatformGlobalVar.SYS_PROPERTIES.get("phoneFunction");
	public static final BigDecimal STANDART_WORKTIME = new BigDecimal("7.5");
	public static final String USER_GRADE = "10";
	public static List<Map<String, Object>> HIK_DEVICE_ONLINE_LIST = new ArrayList<Map<String,Object>>();
	public static List<Map<String, Object>> HIK_DEVICE_OFFLINE_LIST = new ArrayList<Map<String,Object>>();
	public static List<Map<String, Object>> HIK_DEVICE_CAR_CONFIG_LIST = new ArrayList<Map<String,Object>>();
	
	private static List<MenuBean> menuList = new ArrayList<MenuBean>();
	private static List<SysFunction> funcList = new ArrayList<SysFunction>();
	private static Map<Integer, List<SysFunction>> menuFuncMap = new HashMap<Integer, List<SysFunction>>();
	/**
	 *工程标段Map，内部没有顺序，使用时需要自己排序
	 *在使用该MAP时不要直接修改器内部QaspTreeBean，否则对于系统可能会产生问题，切记！
	 */
	private static Map<String, MenuTreeBean> projeSectMap = null;
	
	public static synchronized void init(){
		try{
			ctx=PlatformGlobalVar.APPLICATION_CONTEXT;
			ILoginManageService loginService = (ILoginManageService) ctx.getBean("loginmanageService");
			SysTools.fillGlobalMenu(loginService.getAllMenu(),loginService.getAllFunc(), menuList, funcList, menuFuncMap);
			
		}catch(Exception e){
			
			log.error("==Application init ERROR!!=="+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static List<MenuBean> getMenuList(){
		return menuList;
	}
	
	public static List<SysFunction> getFunctionList(){
		return funcList;
	}
	public static Map<Integer, List<SysFunction>> getMenuFuncMap(){
		return menuFuncMap;
	}

	public static Map<String, MenuTreeBean> getProjeSectMap() {
		return projeSectMap;
	}
}
