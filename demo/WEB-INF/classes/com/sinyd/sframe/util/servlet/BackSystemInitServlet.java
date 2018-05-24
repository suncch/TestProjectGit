package com.sinyd.sframe.util.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;

import com.sinyd.lnram.hikinterface.persist.IHikDeviceCarConfigDao;
import com.sinyd.lnram.hikinterface.vo.HikDeviceCarConfigSearchBeanVO;
import com.sinyd.platform.syscontext.PlatformGlobalVar;
import com.sinyd.sframe.util.GlobalVal;

public class BackSystemInitServlet extends HttpServlet {
	private static final long serialVersionUID = 4124099489926662290L;

	@Override
	public void init() throws ServletException {
		// 进行系统的初始化信息
		GlobalVal.init();

		// 初始的时候查询海康接口的device_id配置表和car_info连接查询到的车辆信息
		ApplicationContext ctx = PlatformGlobalVar.APPLICATION_CONTEXT;
		HikDeviceCarConfigSearchBeanVO queryInfo = new HikDeviceCarConfigSearchBeanVO();
		queryInfo.setSortname("id");
		IHikDeviceCarConfigDao hikdevicecarconfigDao = (IHikDeviceCarConfigDao) ctx
				.getBean("hikdevicecarconfigDao");
		List<Map<String, Object>> deviceCarConfigList = hikdevicecarconfigDao
				.getListWithoutPaging(queryInfo);
		GlobalVal.HIK_DEVICE_CAR_CONFIG_LIST = deviceCarConfigList;
	}
}
