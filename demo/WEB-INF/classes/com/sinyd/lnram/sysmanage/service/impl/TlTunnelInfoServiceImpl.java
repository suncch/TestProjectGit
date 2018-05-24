package com.sinyd.lnram.sysmanage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinyd.lnram.equipment.persist.ITlEquipmentInfoDao;
import com.sinyd.lnram.equipment.persist.ITlEquipmentInfoExtendedDao;
import com.sinyd.lnram.equipment.vo.TlEquipmentInfoExtendedSearchBeanVO;
import com.sinyd.lnram.equipment.vo.TlEquipmentInfoSearchBeanVO;
import com.sinyd.lnram.sysmanage.persist.ITlTunnelInfoDao;
import com.sinyd.lnram.sysmanage.service.ITlTunnelInfoService;
import com.sinyd.lnram.sysmanage.vo.TlTunnelInfoSearchBeanVO;
import com.sinyd.lnram.util.SysConstants;
import com.sinyd.lnram.util.gen.domain.TlTunnelInfo;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.lnram.vehicle.persist.ITlVehicleDrivingInfoDao;
import com.sinyd.lnram.vehicle.persist.ITlVehicleWarningInfoDao;
import com.sinyd.lnram.vehicle.vo.TlVehicleDrivingInfoSearchBeanVO;
import com.sinyd.lnram.vehicle.vo.TlVehicleWarningInfoSearchBeanVO;
import com.sinyd.platform.uivo.vo.GridDataBean;
import com.sinyd.sframe.util.SysUtil;
import com.sinyd.sframe.util.vo.UserBean;

/*
 * @version 2.0
 */

@Service("tltunnelinfoService")
public class TlTunnelInfoServiceImpl implements ITlTunnelInfoService {
    private static Logger log = LoggerFactory.getLogger(TlTunnelInfoServiceImpl.class);

    @Autowired
    private ITlTunnelInfoDao tltunnelinfoDao;

    @Autowired
    private ITlEquipmentInfoDao tlequipmentinfoDao;

    @Autowired
    private ITlEquipmentInfoExtendedDao tlequipmentinfoextendedDao;

    @Autowired
    private ITlVehicleWarningInfoDao tlvehiclewarninginfoDao;

    @Autowired
    private ITlVehicleDrivingInfoDao tlvehicledrivinginfoDao;

    @Override
    public List<Map<String, Object>> getComboxList(TlTunnelInfoSearchBeanVO queryInfo) {
        List<Map<String, Object>> list = tltunnelinfoDao.getListWithoutPaging(queryInfo);

        if (SysConstants.COMBOX_SELECT.equals(queryInfo.getIsCanEmpty())) {
            return SysUtil.addComboxAllOrSelect(list, "TUNNEL_NAME", SysConstants.COMBOX_SELECT);
        } else if (SysConstants.COMBOX_ALL.equals(queryInfo.getIsCanEmpty())) {
            return SysUtil.addComboxAllOrSelect(list, "TUNNEL_NAME", SysConstants.COMBOX_ALL);
        }

        return list;
    }

    @Override
    public Map<String, Object> getListWithoutPaging(TlTunnelInfoSearchBeanVO queryInfo) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("Rows", tltunnelinfoDao.getListWithoutPaging(queryInfo));
        return resMap;
    }

    @Override
    public Map<String, Object> getListWithPaging(TlTunnelInfoSearchBeanVO queryInfo) {
        GridDataBean bean =
                new GridDataBean(queryInfo.getPage(), tltunnelinfoDao.getListCountNum(queryInfo),
                        tltunnelinfoDao.getListWithPaging(queryInfo));
        return bean.getGridData();
    }

    @Override
    public TlTunnelInfoVO getInfoByPK(String id) {
        return tltunnelinfoDao.getInfoByPK(id);
    }

    @Override
    @Transactional
    public String save(TlTunnelInfo bean, UserBean user) {
        String resStr = "success";

        TlTunnelInfoSearchBeanVO queryInfo = new TlTunnelInfoSearchBeanVO();
        queryInfo.setTunnel_code(bean.getTunnel_code());

        List<Map<String, Object>> existList = tltunnelinfoDao.getListWithoutPaging(queryInfo);

        if (existList != null && !existList.isEmpty()) {
            return "duplicate";
        }

        int ret = 0;
        ret = tltunnelinfoDao.addNewRecord(bean);
        if (ret == 0) {
            resStr = "error";
        }
        return resStr;
    }

    @Override
    @Transactional
    public String update(TlTunnelInfo bean, UserBean user) {
        String resStr = "success";

        TlTunnelInfoSearchBeanVO queryInfo = new TlTunnelInfoSearchBeanVO();
        queryInfo.setTunnel_code(bean.getTunnel_code());

        List<Map<String, Object>> existList = tltunnelinfoDao.getListWithoutPaging(queryInfo);

        if (existList != null && !existList.isEmpty()) {
            if (existList.size() > 1) {
                return "duplicate";
            } else {
                for (Map<String, Object> map : existList) {
                    if (!bean.getId().equals((String) map.get("ID"))) {
                        return "duplicate";
                    }
                }
            }
        }

        int ret = 0;
        //		ret = tltunnelinfoDao.updateRecordAll(bean); // update a record replace all, support null val
        ret = tltunnelinfoDao.updateRecordCols(bean); // update a record replace columns, nonsupport null val
        if (ret == 0) {
            resStr = "error";
        }
        return resStr;
    }

    @Override
    @Transactional
    public String delete(TlTunnelInfo bean) {
        String resStr = "success";
        int ret = 0;
        try {

            //隧道如果被设备信息管理使用，则不允许删除
            TlEquipmentInfoSearchBeanVO queryEquipment = new TlEquipmentInfoSearchBeanVO();
            queryEquipment.setTunnel_code(bean.getTunnel_code());
            List<Map<String, Object>> resMap1 = tlequipmentinfoDao.getListWithoutPaging(queryEquipment);
            if (resMap1.size() > 0) {
                return "used";
            }

            //隧道如果被设备信息管理拓展使用，则不允许删除
            TlEquipmentInfoExtendedSearchBeanVO queryEquipmentextended = new TlEquipmentInfoExtendedSearchBeanVO();
            queryEquipmentextended.setTunnel_code(bean.getTunnel_code());
            List<Map<String, Object>> resMap2 = tlequipmentinfoextendedDao.getListWithoutPaging(queryEquipmentextended);
            if (resMap2.size() > 0) {
                return "used";
            }

            //隧道如果被车辆预警信息管理使用，则不允许删除
            TlVehicleWarningInfoSearchBeanVO queryVehiclewarning = new TlVehicleWarningInfoSearchBeanVO();
            queryVehiclewarning.setTunnel_code(bean.getTunnel_code());
            List<Map<String, Object>> resMap3 = tlvehiclewarninginfoDao.getListWithoutPaging(queryVehiclewarning);
            if (resMap3.size() > 0) {
                return "used";
            }

            //隧道如果被车辆行驶情况使用，则不允许删除
            TlVehicleDrivingInfoSearchBeanVO queryVehicledriving = new TlVehicleDrivingInfoSearchBeanVO();
            queryVehicledriving.setTunnel_code(bean.getTunnel_code());
            List<Map<String, Object>> resMap4 = tlvehicledrivinginfoDao.getListWithoutPaging(queryVehicledriving);
            if (resMap4.size() > 0) {
                return "used";
            }

            ret = tltunnelinfoDao.deleteRecordByDomain(bean); // nonsupport null val
        } catch (Exception e) {
            log.error(e.getMessage());
            resStr = "fkerror"; // default foreign key error
        }
        if (ret == 0) {
            resStr = "error";
        }
        return resStr;
    }

    /**
     * Description :check repeat by "and" method, union checking, need new TlTunnelInfo() include check-params
     * 
     * @param queryInfo
     * @param isExludePKFlag, true is exclude primary key, false is include primary key
     * @return "success" or "repeat"
     * @author auto_generator
     */
    private String checkParamsRepeatByAndMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag) {
        if (isExcludePKFlag) {
            Boolean setAllPk = true;
            if (queryInfo.getId() == null) {
                setAllPk = false;
            }
            if (!setAllPk) {
                log.info("**************please set pk into the queryInfo.**************");
                return "error";
            }
        }
        String resStr = "success";
        List<TlTunnelInfo> beanList = tltunnelinfoDao.getSingleTableListByAndMethod(queryInfo, isExcludePKFlag);
        if (beanList != null && beanList.size() > 0) {
            resStr = "repeat";
        }
        return resStr;
    }

    /**
     * Description :check repeat by "or" method, each checking, need new TlTunnelInfo() include check-params
     * 
     * @param queryInfo
     * @param isExludePKFlag, true is exclude primary key, false is include primary key
     * @return "success" or "repeat"
     * @author auto_generator
     */
    private String checkParamsRepeatByOrMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag) {
        if (isExcludePKFlag) {
            Boolean setAllPk = true;
            if (queryInfo.getId() == null) {
                setAllPk = false;
            }
            if (!setAllPk) {
                log.info("**************please set pk into the queryInfo.**************");
                return "error";
            }
        }
        String resStr = "success";
        List<TlTunnelInfo> beanList = tltunnelinfoDao.getSingleTableListByOrMethod(queryInfo, isExcludePKFlag);
        if (beanList != null && beanList.size() > 0) {
            resStr = "repeat";
        }
        return resStr;
    }
}
