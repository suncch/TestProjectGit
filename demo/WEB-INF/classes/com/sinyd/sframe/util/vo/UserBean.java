package com.sinyd.sframe.util.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinyd.lnram.util.gen.domain.SysRole;

/**
 * 登录用户信息表
 * 
 * @author gx
 * 
 */
public class UserBean implements Serializable {

	private static final long serialVersionUID = -6289280101558471306L;

	private String userId;
	private String userCode;
	private String userPasswd;
	private String realname;
	private String deptId;
	private String userGrade;
	private String currentRoleId;
	private String currentRoleName;
	//新增身份证号
	private String user_id_num;
	private List<SysRole> roleIdList = new ArrayList<SysRole>();

	private List<String> menuIdList = new ArrayList<String>();
	
	private Map<String, String> funcMap = new HashMap<String, String>();
	
	private String deptName;

	private String office_code;
	
	public List<SysRole> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<SysRole> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public Map<String, String> getFuncMap() {
		return funcMap;
	}

	public void setFuncMap(Map<String, String> funcMap) {
		this.funcMap = funcMap;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void putFuncName(String funcName) {
		funcMap.put(funcName, "1");
	}

	public boolean hasFuncName(String funcName) {
		if (funcMap.get(funcName) == null) {
			return false;
		}
		return true;
	}

	public void cleanFuncNameMap() {
		funcMap = new HashMap<String, String>();
	}

	public void addRoleList(SysRole roleBean) {
		roleIdList.add(roleBean);
	}

	public String getCurrentRoleId() {
		return currentRoleId;
	}

	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
	}

	public String getCurrentRoleName() {
		return currentRoleName;
	}

	public void setCurrentRoleName(String currentRoleName) {
		this.currentRoleName = currentRoleName;
	}

	public List<SysRole> getRoleList() {
		return roleIdList;
	}

	public List<String> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		this.menuIdList = menuIdList;
	}

	/**
	 * @return the office_code
	 */
	public String getOffice_code() {
		return office_code;
	}

	/**
	 * @param office_code the office_code to set
	 */
	public void setOffice_code(String office_code) {
		this.office_code = office_code;
	}

	public String getUser_id_num() {
		return user_id_num;
	}

	public void setUser_id_num(String user_id_num) {
		this.user_id_num = user_id_num;
	}

}
