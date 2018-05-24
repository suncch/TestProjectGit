package com.sinyd.lnram.sysmanage.persist.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sinyd.lnram.sysmanage.persist.ITlTunnelInfoDao;
import com.sinyd.lnram.sysmanage.vo.TlTunnelInfoSearchBeanVO;
import com.sinyd.lnram.util.gen.SqlString;
import com.sinyd.lnram.util.gen.domain.TlTunnelInfo;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.platform.utiltools.util.StringUtil;
import com.sinyd.sframe.util.database.AbstractSimpleDao;

/*
 * @version 2.0
 */

@Repository("tltunnelinfoDao")
public class TlTunnelInfoDaoImpl extends AbstractSimpleDao implements ITlTunnelInfoDao {

    @Override
    public List<Map<String, Object>> getListWithoutPaging(TlTunnelInfoSearchBeanVO queryInfo) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if(StringUtil.isNotBlank(queryInfo.getOffice_code())) {
            paramMap.put("office_code", queryInfo.getOffice_code());
        }
        if (StringUtil.isNotBlank(queryInfo.getTunnel_code())) {
            paramMap.put("tunnel_code", queryInfo.getTunnel_code());
        }

        if (StringUtil.isNotBlank(queryInfo.getSortname())) {
            paramMap.put("sortname", queryInfo.getSortname());
            paramMap.put("sortorder", queryInfo.getSortorder());
        } else {
            paramMap.put("sortname", "tunnel_name");
            paramMap.put("sortorder", "desc");
        }

        return this.queryForListByTemplate(SqlString.AUTOSINGLE2_TL_TUNNEL_INFO_LIST, paramMap);
    }

    @Override
    public List<Map<String, Object>> getListWithPaging(TlTunnelInfoSearchBeanVO queryInfo) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (StringUtil.isNotBlank(queryInfo.getOffice_code())) {
            paramMap.put("office_code", queryInfo.getOffice_code());
        }
        if (StringUtil.isNotBlank(queryInfo.getTunnel_code())) {
            paramMap.put("tunnel_code", queryInfo.getTunnel_code());
        }

        if (StringUtil.isNotBlank(queryInfo.getSortname())) {
            paramMap.put("sortname", queryInfo.getSortname());
            paramMap.put("sortorder", queryInfo.getSortorder());
        } else {
            paramMap.put("sortname", "tunnel_name");
            paramMap.put("sortorder", "desc");
        }

        return this.pagingForListByTemplate(SqlString.AUTOSINGLE2_TL_TUNNEL_INFO_LIST, queryInfo.getPagesize(),
                queryInfo.getPage(), paramMap);
    }

    @Override
    public int getListCountNum(TlTunnelInfoSearchBeanVO queryInfo) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (StringUtil.isNotBlank(queryInfo.getOffice_code())) {
            paramMap.put("office_code", queryInfo.getOffice_code());
        }
        if (StringUtil.isNotBlank(queryInfo.getTunnel_code())) {
            paramMap.put("tunnel_code", queryInfo.getTunnel_code());
        }

        return this.recordNumberForListByTemplate(SqlString.AUTOSINGLE2_TL_TUNNEL_INFO_LIST, paramMap);
    }

    @Override
    public TlTunnelInfoVO getInfoByPK(String id) {
        return TlTunnelInfo.getRecordVOByPK(this, id);
    }

    @Override
    public TlTunnelInfo getDomainByPK(String id) {
        return TlTunnelInfo.getRecordDomainByPK(this, id);
    }

    @Override
    public int addNewRecord(TlTunnelInfo bean) {
        return bean.insertRecord(this);
    }

    @Override
    public int updateRecordAll(TlTunnelInfo bean) {
        return bean.updateRecordAll(this);
    }

    @Override
    public int updateRecordCols(TlTunnelInfo bean) {
        return bean.updateRecordColumn(this);
    }

    @Override
    public int deleteRecordByPK(String id) {
        return TlTunnelInfo.deleteRecordsByPK(this, id);
    }

    @Override
    public int deleteRecordByDomain(TlTunnelInfo bean) {
        return bean.deleteRecordsByDomain(this);
    }

    @Override
    public List<TlTunnelInfo> getSingleTableListByAndMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag) {
        return TlTunnelInfo.getSingleTableListByAndMethod(this, queryInfo, isExcludePKFlag);
    }

    @Override
    public List<TlTunnelInfo> getSingleTableListByOrMethod(TlTunnelInfo queryInfo, Boolean isExcludePKFlag) {
        return TlTunnelInfo.getSingleTableListByOrMethod(this, queryInfo, isExcludePKFlag);
    }
}