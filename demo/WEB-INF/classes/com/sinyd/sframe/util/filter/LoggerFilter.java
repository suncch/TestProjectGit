package com.sinyd.sframe.util.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sinyd.platform.syscontext.PlatformGlobalVar;
import com.sinyd.sframe.util.SysLoggerUtil;
import com.sinyd.sframe.util.SysUtil;
import com.sinyd.sframe.util.vo.UserBean;
/**
 * 输出系统日志过滤器
 * @author Administrator
 *
 */
public class LoggerFilter implements Filter {
	private static Logger log = LoggerFactory.getLogger(LoggerFilter.class);
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
			String isSaveLog = PlatformGlobalVar.SYS_PROPERTIES.get("isSaveLog");
			if("true".equals(isSaveLog)){
				HttpServletRequest request = (HttpServletRequest) req;
				long startTime = System.currentTimeMillis();
				chain.doFilter(req, resp);
				long endTime = System.currentTimeMillis();
				Map<String,String[]> paramMap = req.getParameterMap();
				String paramStr = new Gson().toJson(paramMap);
				String url = request.getRequestURI();
				long handleTime = endTime-startTime;
				String ip = SysUtil.getIP(request);
				UserBean user = SysUtil.getUser(request);
				String userAgent = SysUtil.getUserAgent(request);
				SysLoggerUtil.saveLogToFile(user, url,ip,paramStr,handleTime,userAgent);
			}else{
				chain.doFilter(req, resp);
			}
			
	}
	
	@Override
	public void destroy() {
	}
}
