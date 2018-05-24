package com.sinyd.sframe.util;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sinyd.lnram.util.SysConstants;
import com.sinyd.sframe.util.vo.UserBean;

public class SysUtil {
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

	/**
	 * 获取浏览器的ip地址
	 * 
	 * @param request
	 * @return
	 */
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
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	/**
	 * 获取用户对象
	 * 
	 * @param request
	 * @return
	 */
	public static UserBean getUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute(GlobalVal.USER_SESSION);
		return user;
	}

	/**
	 * 获取浏览器类型
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");
		return agent;
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

	/**
	 * 前台下拉框加全部或者请选择
	 * 
	 * @param sqlParam
	 * @return
	 */
	public static List<Map<String, Object>> addComboxAllOrSelect(
			List<Map<String, Object>> list, String column, String flag) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (SysConstants.COMBOX_ALL.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_ALL);
			map.put(column, "全部");
			list.add(0, map);
		} else if (SysConstants.COMBOX_SELECT.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_SELECT);
			map.put(column, "请选择");
			list.add(0, map);
		}

		return list;

	}

	public static List<Map<String, Object>> addComboxAllOrSelect(
			List<Map<String, Object>> list, String column, String flag,
			String key) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (SysConstants.COMBOX_ALL.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_ALL);
			map.put(key, SysConstants.COMBOX_ALL);
			map.put(column, "全部");
			list.add(0, map);
		} else if (SysConstants.COMBOX_SELECT.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_SELECT);
			map.put(key, SysConstants.COMBOX_SELECT);
			map.put(column, "请选择");
			list.add(0, map);
		}

		return list;
	}

	/**
	 * 前台下拉框加全部或者请选择
	 * 
	 * @param sqlParam
	 * @return
	 */
	public static List<Map<String, Object>> addDictComboxAllOrSelect(
			List<Map<String, Object>> list, String column, String flag) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (SysConstants.COMBOX_ALL.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_ALL);
			map.put("DICT_CODE", SysConstants.COMBOX_ALL);
			map.put(column, "全部");
			list.add(0, map);
		} else if (SysConstants.COMBOX_SELECT.equals(flag)) {
			map.put("ID", SysConstants.COMBOX_SELECT);
			map.put("DICT_CODE", SysConstants.COMBOX_SELECT);
			map.put(column, "请选择");
			list.add(0, map);
		}

		return list;

	}

	/**
	 * 前台下拉框加全部或者请选择
	 * 
	 * @param sqlParam
	 * @return
	 */
	public static List<Map<String, Object>> addOfficeComboxAllOrSelect(
			List<Map<String, Object>> list, String column, String flag) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (SysConstants.COMBOX_ALL.equals(flag)) {
			map.put("OFFICE_CODE", SysConstants.COMBOX_ALL);
			map.put(column, "全部");
			list.add(0, map);
		} else if (SysConstants.COMBOX_SELECT.equals(flag)) {
			map.put("OFFICE_CODE", SysConstants.COMBOX_SELECT);
			map.put(column, "请选择");
			list.add(0, map);
		}

		return list;

	}

	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
			c.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 待办任务类别下拉菜单
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static List<Map<String, Object>> addMsgCombox(
			List<Map<String, Object>> list, String flag) {
		List<String> temp = new ArrayList<String>();
		if (list != null && !list.isEmpty()) {
			Iterator<Map<String, Object>> iterator = list.iterator();

			while (iterator.hasNext()) {
				Map<String, Object> tempmMap = (Map<String, Object>) iterator
						.next();
				String service_type = (String) tempmMap.get("service_type");
				if (!temp.contains(service_type)) {
					temp.add(service_type);
				}
			}
		}

		List<Map<String, Object>> retlList = new ArrayList<Map<String, Object>>();
		if (!temp.isEmpty()) {
			Iterator<String> iterator2 = temp.iterator();
			Map<String, Object> temMap;

			while (iterator2.hasNext()) {
				temMap = new HashMap<String, Object>();
				String service_type = (String) iterator2.next();
				temMap.put("service_type_label", service_type);
				temMap.put("service_type_value", service_type);

				retlList.add(temMap);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (SysConstants.COMBOX_ALL.equals(flag)) {
			map.put("id", SysConstants.COMBOX_ALL);
			map.put("service_type_value", SysConstants.COMBOX_ALL);
			map.put("service_type_label", "全部");

			retlList.add(0, map);
		} else if (SysConstants.COMBOX_SELECT.equals(flag)) {
			map.put("id", SysConstants.COMBOX_SELECT);
			map.put("service_type_value", SysConstants.COMBOX_SELECT);
			map.put("service_type_label", "请选择");

			retlList.add(0, map);
		}

		return retlList;

	}

}
