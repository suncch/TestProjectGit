package com.sinyd.lnram.util;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinyd.lnram.sysmanage.service.ISysLogService;
import com.sinyd.lnram.sysmanage.vo.QaspMenuBean;
import com.sinyd.lnram.util.gen.domain.SysFunction;
import com.sinyd.lnram.util.gen.domain.SysLog;
import com.sinyd.platform.uivo.vo.MenuBean;
import com.sinyd.platform.uivo.vo.TreeBean;
import com.sinyd.platform.utiltools.util.StringUtil;
import com.sinyd.sframe.util.GlobalVal;
import com.sinyd.sframe.util.vo.UserBean;

/**
 * @author Allen 描述：是系统管理模块的工具类
 * */
public class SysTools {
	private static Logger log = LoggerFactory.getLogger(SysTools.class);

	/**
	 * @author Allen 描述：系统加载的时候构建全局的菜单树结构，并取得菜单下的所有功能点，用于各个登录用户构建其自身的菜单树
	 * @param list
	 * */
	public static void fillGlobalMenu(List<Map<String, Object>> menuDataList,
			List<Map<String, Object>> funcDataList, List<MenuBean> menuList,
			List<SysFunction> funcList,
			Map<Integer, List<SysFunction>> menuFuncMap) {

		int index = 1;
		List<Map<String, Object>> topMenuList = getSamePropertyList(
				menuDataList, "menu_level", SysConstants.UNIT_LEVEL);// 获取一级菜单
		// 递归菜单下的所有菜单和功能按钮
		for (Map<String, Object> map : topMenuList) {
			String menuId = "" + map.get("id");
			String menu_name = (String) map.get("menu_name");
			String remark = (String) map.get("menu_remark");
			String menuArrange = (String) map.get("menu_arrange");
			QaspMenuBean bean = new QaspMenuBean(menu_name, "");
			bean.setId(menuId);
			bean.setIcon(index);
			bean.setRemark(remark);
			bean.setMenuArrange(menuArrange);
			index++;

			bean.setChildrenTree(getChildMenu(menuDataList, bean));
			menuList.add(bean);
		}
		// 将权限按钮导入menuFuncMap
		for (Map<String, Object> map : funcDataList) {
			SysFunction funcBean = new SysFunction();
			funcBean.setId((String) map.get("func_id"));
			funcBean.setFunc_name(map.get("func_name").toString());
			funcBean.setFunc_type(map.get("func_type").toString());
			funcBean.setFunc_method(map.get("func_method").toString());
			funcList.add(funcBean);
			Integer menuId = (Integer) map.get("menu_id");

			if (menuFuncMap.get(menuId) == null) {
				menuFuncMap.put(menuId, new ArrayList<SysFunction>());
			}
			menuFuncMap.get(menuId).add(funcBean);
		}
	}

	private static List<TreeBean> getChildMenu(
			List<Map<String, Object>> menuList, MenuBean parentTreeBean) {
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		String parentId = parentTreeBean.getId();
		for (Map<String, Object> map : menuList) {
			String pid = map.get("p_menu_id").toString();
			if (parentId.equals(pid)) {
				String menuId = "" + map.get("id");
				String menu_name = (String) map.get("menu_name");
				String menuUrl = map.get("menu_url").toString();
				String remark = (String) map.get("menu_remark");
				String menuArrange = (String) map.get("menu_arrange");
				MenuTreeBean treeBean = new MenuTreeBean(menu_name, "");
				treeBean.setUrl(menuUrl);
				treeBean.setId(menuId);
				treeBean.setRemark(remark);
				treeBean.setMenuArrange(menuArrange);
				treeList.add(treeBean);
				treeBean.setChildren(getChildMenu(menuList, treeBean));
			}
		}

		if (treeList.size() == 0) {
			return null;
		}
		return treeList;

	}

	private static List<TreeBean> getChildMenu(
			List<Map<String, Object>> menuList, TreeBean parentTreeBean) {
		List<TreeBean> treeList = new ArrayList<TreeBean>();
		String parentId = parentTreeBean.getId();
		for (Map<String, Object> map : menuList) {
			String pid = map.get("p_menu_id").toString();
			if (parentId.equals(pid)) {
				String menuId = "" + map.get("id");
				String menu_name = (String) map.get("menu_name");
				String menuUrl = map.get("menu_url").toString();
				String remark = (String) map.get("menu_remark");
				String menuArrange = (String) map.get("menu_arrange");
				MenuTreeBean treeBean = new MenuTreeBean(menu_name, "");
				treeBean.setUrl(menuUrl);
				treeBean.setId(menuId);
				treeBean.setRemark(remark);
				treeBean.setMenuArrange(menuArrange);
				treeList.add(treeBean);
				treeBean.setChildren(getChildMenu(menuList, treeBean));
			}
		}

		if (treeList.size() == 0) {
			return null;
		}
		return treeList;
	}

	/**
	 * 获取List中指定属性的值为propertyVal的列表
	 * 
	 * @param List
	 * @param propertyName
	 * @param propertyVal
	 */
	public static List<Map<String, Object>> getSamePropertyList(
			List<Map<String, Object>> list, String propertyName,
			String propertyVal) {
		if (list == null || list.size() == 0) {
			return null;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			String val = map.get(propertyName).toString();
			if (val == null) {
				continue;
			}
			if (val.equals(propertyVal)) {
				resultList.add(map);
			}
		}
		if (resultList.size() == 0) {
			return null;
		}
		return resultList;
	}

	/**
	 * 根据传入的属性名称、遍历list返回属性值与propertyVal相等的记录
	 * 
	 * @param list
	 * @param propertyName
	 * @param propertyVal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> getSamePropertyListBean(Collection<T> list,
			String propertyName, Object propertyVal) {
		if (list == null || list.size() == 0) {
			return null;
		}
		List<T> resultList = new ArrayList<T>();
		for (T bean : list) {
			Object val = null;
			try {
				if (bean instanceof java.util.Map) {
					val = ((java.util.Map) bean).get(propertyName);
				} else {
					PropertyDescriptor p = new PropertyDescriptor(propertyName,
							bean.getClass());
					val = p.getReadMethod().invoke(bean);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (val == null) {
				continue;
			}
			if (val.equals(propertyVal)) {
				resultList.add(bean);
			}
		}
		if (resultList.size() == 0) {
			return null;
		}
		return resultList;
	}

	/**
	 * 返回与传入属性propertyName值相同的一个对象
	 * 
	 * @param list
	 * @param propertyName
	 * @param propertyVal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T getSamePropertyBean(Collection<T> list,
			String propertyName, Object propertyVal) {
		if (list == null || list.size() == 0) {
			return null;
		}
		for (T bean : list) {
			Object val = null;
			try {
				if (bean instanceof java.util.Map) {
					val = ((java.util.Map) bean).get(propertyName);
				} else {
					PropertyDescriptor p = new PropertyDescriptor(propertyName,
							bean.getClass());
					val = p.getReadMethod().invoke(bean);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (val == null) {
				continue;
			}
			if (val.equals(propertyVal)) {
				return bean;
			}
		}
		return null;
	}

	

	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (!checkIP(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (!checkIP(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (!checkIP(ip)) {
			ip = request.getRemoteAddr();
		}
		if("0:0:0:0:0:0:0:1".equals(ip)){
			ip = "127.0.0.1";
		}
		return ip;
	}

	private static boolean checkIP(String ip) {
		if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
				|| ip.split("\\.").length != 4) {
			return false;
		}
		return true;
	}

	/**
	 * 模糊匹配时将传入的参数的特殊字符(%\_)进行转义，并将前后增加通配符%
	 * 
	 * @param sqlParam
	 * @return
	 */
	public static String getSqlLikeParam(String sqlParam) {
		sqlParam = sqlParam.replace("\\", "\\\\");
		sqlParam = sqlParam.replace("%", "\\%");
		sqlParam = sqlParam.replace("_", "\\_");
		return "%" + sqlParam + "%";
	}

	

	
	
	public static void saveLog(HttpServletRequest request, String msg) {
		try {
			HttpSession session = request.getSession();
			UserBean user = (UserBean) session
					.getAttribute(GlobalVal.USER_SESSION);

			SysLog bean = new SysLog();
			ISysLogService logService = (ISysLogService) GlobalVal.ctx
					.getBean("syslogService");

			// 插入日志表记录
			bean.setOper_behavior(msg);
			bean.setOper_ip(getIP(request));
			bean.setUser_name(user.getRealname());
			bean.setUser_code(user.getUserCode());
			bean.setOper_timestamp(new Timestamp(new Date().getTime()));
			logService.save(bean);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	
	
	/**
	 * 如果val为null或为空字符串，则返回defVal，否则直接返回val
	 * @param val
	 * @param defVal
	 * @return
	 */
	public static String getVal(String val,String defVal){
		if(StringUtil.isBlank(val)){
			return defVal;
		}else{
			return val;
		}
	}
	
	public static String ajaxFileTextEncoding(String srcStr, HttpServletRequest request){
		String res;
		String agent = request.getHeader("User-Agent").toLowerCase();
		if(agent.indexOf("firefox") > 0 || agent.indexOf("msie 10") > 0 || agent.indexOf("msie 9") > 0 || agent.indexOf("chrome") > 0){
			try {
				res = new String(srcStr.getBytes("UTF-8"), "ISO8859-1");
				return res;
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
			} 
		}else if(agent.indexOf("msie 8") > 0){
			return srcStr;
		}
		
		return srcStr;
	}
	
	/**
	 * 设置office_code属性
	 * @param user
	 * @param queryInfo
	 * @author lvq
	 */
	public static void setOfficeCodeQueryInfo(UserBean user, BaseSearchBean queryInfo) {
		if (user == null) {
			return;
		}
		if (queryInfo == null) {
			return;
		}
		if (StringUtil.isNotBlank(queryInfo.getOffice_code())) {
			if (SysConstants.OFFICE_CODE_SHENGJU.equals(queryInfo.getOffice_code())) {
				queryInfo.setOffice_code(null);
			}
			return;
		}
		if (SysConstants.OFFICE_CODE_SHENGJU.equals(user.getOffice_code())) {
			queryInfo.setOffice_code(null);
			return;
		}
		
		queryInfo.setOffice_code(user.getOffice_code());
	}
	
	/**
     * 设置maintain_code属性
     * @param user
     * @param queryInfo
     * @author lvq
     */
    public static void setMaintainCodeQueryInfo(UserBean user, BaseSearchBean queryInfo) {
        if (user == null) {
            return;
        }
        if (queryInfo == null) {
            return;
        }
        if (StringUtil.isNotBlank(queryInfo.getMaintain_code())) {
            if (SysConstants.OFFICE_CODE_SHENGJU.equals(queryInfo.getMaintain_code())) {
                queryInfo.setMaintain_code(null);
            }
            return;
        }
        if (SysConstants.OFFICE_CODE_SHENGJU.equals(user.getOffice_code())) {
            queryInfo.setMaintain_code(null);
            return;
        }
        
        queryInfo.setMaintain_code(user.getOffice_code());
    }
}
