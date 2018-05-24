package com.sinyd.lnram.util.gen.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sinyd.lnram.util.gen.SqlString;
import com.sinyd.lnram.util.gen.vo.TlTunnelInfoVO;
import com.sinyd.platform.utiltools.util.DateUtil;
import com.sinyd.sframe.util.database.AbstractSimpleDao;

/*
 * @version 2.0
 */
public class TlTunnelInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String tunnel_name;

    private String tunnel_code;

    private String construction_company;

    private String tunnel_length;

    private String tunnel_height;

    private java.sql.Timestamp traffic_time;

    private String tunnel_direction;

    private java.math.BigDecimal longitude;

    private java.math.BigDecimal latitude;

    private String office_code;

    private java.sql.Timestamp create_timestamp;

    private String office_name;

    /**
     * default val cols name array
     */
    private static String[] defaultValColArr = { "id", "create_timestamp" };

    /**
     * pk cols name array
     */
    private static String[] pkColArr = { "id" };

    private static String[] columnNameArr = { "id", "tunnel_name", "tunnel_code", "construction_company",
            "tunnel_length", "tunnel_height", "traffic_time", "tunnel_direction", "longitude", "latitude",
            "office_code", "create_timestamp" };

    public String getId() {
        return id;
    }

    public void setId(String obj) {
        id = obj;
    }

    public String getTunnel_name() {
        return tunnel_name;
    }

    public void setTunnel_name(String obj) {
        tunnel_name = obj;
    }

    public String getTunnel_code() {
        return tunnel_code;
    }

    public void setTunnel_code(String obj) {
        tunnel_code = obj;
    }

    public String getConstruction_company() {
        return construction_company;
    }

    public void setConstruction_company(String obj) {
        construction_company = obj;
    }

    public String getTunnel_length() {
        return tunnel_length;
    }

    public void setTunnel_length(String obj) {
        tunnel_length = obj;
    }

    public String getTunnel_height() {
        return tunnel_height;
    }

    public void setTunnel_height(String obj) {
        tunnel_height = obj;
    }

    public java.sql.Timestamp getTraffic_time() {
        return traffic_time;
    }

    public void setTraffic_time(java.sql.Timestamp obj) {
        traffic_time = obj;
    }

    public String getTunnel_direction() {
        return tunnel_direction;
    }

    public void setTunnel_direction(String obj) {
        tunnel_direction = obj;
    }

    public java.math.BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(java.math.BigDecimal obj) {
        longitude = obj;
    }

    public java.math.BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(java.math.BigDecimal obj) {
        latitude = obj;
    }

    public String getOffice_code() {
        return office_code;
    }

    public void setOffice_code(String obj) {
        office_code = obj;
    }

    public java.sql.Timestamp getCreate_timestamp() {
        return create_timestamp;
    }

    public void setCreate_timestamp(java.sql.Timestamp obj) {
        create_timestamp = obj;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    /**
     * put all columns into a map
     */
    public void putInMap(Map<String, Object> paramMap) {
        paramMap.put("id", this.id);
        paramMap.put("tunnel_name", this.tunnel_name);
        paramMap.put("tunnel_code", this.tunnel_code);
        paramMap.put("construction_company", this.construction_company);
        paramMap.put("tunnel_length", this.tunnel_length);
        paramMap.put("tunnel_height", this.tunnel_height);
        paramMap.put("traffic_time", this.traffic_time);
        paramMap.put("tunnel_direction", this.tunnel_direction);
        paramMap.put("longitude", this.longitude);
        paramMap.put("latitude", this.latitude);
        paramMap.put("office_code", this.office_code);
        paramMap.put("create_timestamp", this.create_timestamp);
    }

    /**
     * return the columns map
     */
    public Map<String, Object> getInfoMap() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        this.putInMap(paramMap);
        return paramMap;
    }

    /**
     * remove default value and pk if it is null
     */
    private Map<String, Object> dealWithMap(Map<String, Object> paramMap) {
        Set<String> set = new HashSet<String>();
        for (String colName : defaultValColArr) {
            set.add(colName);
        }
        for (String colName : pkColArr) {
            set.add(colName);
        }
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String colName = iterator.next();
            if (paramMap.get(colName) == null) {
                paramMap.remove(colName);
            }
        }
        return paramMap;
    }

    public Map<String, Object> setSymbolInsert(Map<String, Object> paramMap) {
        paramMap = dealWithMap(paramMap);
        Boolean flag = true;
        for (String ss : columnNameArr) {
            if (flag) {
                if (paramMap.containsKey(ss) && paramMap.get(ss) != null) {
                    paramMap.put(ss + "Symbol", " ");
                    flag = false;
                }
            } else {
                paramMap.put(ss + "Symbol", ", ");
            }
        }
        return paramMap;
    }

    private Map<String, Object> setSymbolUpdateWithNullValue(Map<String, Object> paramMap) {
        return setSymbolUpdate(dealWithMap(paramMap));
    }

    private Map<String, Object> setSymbolUpdateWithoutNullValue(Map<String, Object> paramMap) {
        return setSymbolUpdate(dealWithMapNullVal(paramMap));
    }

    public Map<String, Object> setSymbolUpdate(Map<String, Object> paramMap) {
        Boolean flag = true;
        for (String ss : columnNameArr) {
            if (flag) {
                if (paramMap.containsKey(ss) && paramMap.get(ss) != null && !Arrays.asList(pkColArr).contains(ss)) {
                    paramMap.put(ss + "Symbol", " ");
                    flag = false;
                }
            } else {
                paramMap.put(ss + "Symbol", ", ");
            }
        }
        return paramMap;
    }

    /**
     * remove null
     */
    private Map<String, Object> dealWithMapNullVal(Map<String, Object> paramMap) {
        if (paramMap.get("id") == null) {
            paramMap.remove("id");
        }
        if (paramMap.get("tunnel_name") == null) {
            paramMap.remove("tunnel_name");
        }
        if (paramMap.get("tunnel_code") == null) {
            paramMap.remove("tunnel_code");
        }
        if (paramMap.get("construction_company") == null) {
            paramMap.remove("construction_company");
        }
        if (paramMap.get("tunnel_length") == null) {
            paramMap.remove("tunnel_length");
        }
        if (paramMap.get("tunnel_height") == null) {
            paramMap.remove("tunnel_height");
        }
        if (paramMap.get("traffic_time") == null) {
            paramMap.remove("traffic_time");
        }
        if (paramMap.get("tunnel_direction") == null) {
            paramMap.remove("tunnel_direction");
        }
        if (paramMap.get("longitude") == null) {
            paramMap.remove("longitude");
        }
        if (paramMap.get("latitude") == null) {
            paramMap.remove("latitude");
        }
        if (paramMap.get("office_code") == null) {
            paramMap.remove("office_code");
        }
        if (paramMap.get("create_timestamp") == null) {
            paramMap.remove("create_timestamp");
        }
        return paramMap;
    }

    /**
     * this table insert function, nonsupport null val
     */
    public int insertRecord(AbstractSimpleDao dao) {
        return dao.updateByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_INSERT, setSymbolInsert(this.getInfoMap()));
    }

    /**
     * delete records by primary key
     */
    public static int deleteRecordsByPK(AbstractSimpleDao dao, String id) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        return dao.updateByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_DELETE, paramMap);
    }

    /**
     * this table update row function, need primary key, support null val
     */
    public int updateRecordAll(AbstractSimpleDao dao) {
        return dao.updateByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_UPDATE_ALL,
                setSymbolUpdateWithNullValue(this.getInfoMap()));
    }

    /**
     * this table update column function, need primary key, nonsupport null val
     */
    public int updateRecordColumn(AbstractSimpleDao dao) {
        return dao.updateByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_UPDATE,
                setSymbolUpdateWithoutNullValue(this.getInfoMap()));
    }

    /**
     * return single record domain by primary key
     */
    public static TlTunnelInfo getRecordDomainByPK(AbstractSimpleDao dao, String id) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        TlTunnelInfo bean = dao.qryObj(SqlString.AUTO2_TL_TUNNEL_INFO_LIST, paramMap, TlTunnelInfo.class);
        return bean;
    }

    /**
     * return single record vo by primary key
     */
    public static TlTunnelInfoVO getRecordVOByPK(AbstractSimpleDao dao, String id) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        List<Map<String, Object>> resList = dao.queryForListByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_LIST, paramMap);
        TlTunnelInfoVO bean = new TlTunnelInfoVO();
        if (resList.size() > 0) {
            Map<String, Object> row = resList.get(0);
            if (row.get("id") != null) {
                bean.setId((String) row.get("id"));
            }
            if (row.get("tunnel_name") != null) {
                bean.setTunnel_name((String) row.get("tunnel_name"));
            }
            if (row.get("tunnel_code") != null) {
                bean.setTunnel_code((String) row.get("tunnel_code"));
            }
            if (row.get("construction_company") != null) {
                bean.setConstruction_company((String) row.get("construction_company"));
            }
            if (row.get("tunnel_length") != null) {
                bean.setTunnel_length((String) row.get("tunnel_length"));
            }
            if (row.get("tunnel_height") != null) {
                bean.setTunnel_height((String) row.get("tunnel_height"));
            }
            if (row.get("traffic_time") != null) {
                bean.setTraffic_time(DateUtil.date2String((java.util.Date) row.get("traffic_time"),
                        "yyyy-MM-dd HH:mm:ss"));
            }
            if (row.get("tunnel_direction") != null) {
                bean.setTunnel_direction((String) row.get("tunnel_direction"));
            }
            if (row.get("longitude") != null) {
                bean.setLongitude((java.math.BigDecimal) row.get("longitude"));
            }
            if (row.get("latitude") != null) {
                bean.setLatitude((java.math.BigDecimal) row.get("latitude"));
            }
            if (row.get("office_code") != null) {
                bean.setOffice_code((String) row.get("office_code"));
            }
            if (row.get("create_timestamp") != null) {
                bean.setCreate_timestamp(DateUtil.date2String((java.util.Date) row.get("create_timestamp"),
                        "yyyy-MM-dd HH:mm:ss"));
            }
        }
        return bean;
    }

    /**
     * delete records by domain, nonsupport null val
     */
    public int deleteRecordsByDomain(AbstractSimpleDao dao) {
        return dao.updateByTemplate(SqlString.AUTO2_TL_TUNNEL_INFO_DELETE, dealWithMapNullVal(this.getInfoMap()));
    }

    /**
     * get list by "and" method, need new TlTunnelInfo() include query-params
     */
    public static List<TlTunnelInfo> getSingleTableListByAndMethod(AbstractSimpleDao dao, TlTunnelInfo queryInfo,
            Boolean isExcludePKFlag) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (queryInfo.getId() != null) {
            paramMap.put("id", queryInfo.getId());
        }
        if (queryInfo.getTunnel_name() != null) {
            paramMap.put("tunnel_name", queryInfo.getTunnel_name());
        }
        if (queryInfo.getTunnel_code() != null) {
            paramMap.put("tunnel_code", queryInfo.getTunnel_code());
        }
        if (queryInfo.getConstruction_company() != null) {
            paramMap.put("construction_company", queryInfo.getConstruction_company());
        }
        if (queryInfo.getTunnel_length() != null) {
            paramMap.put("tunnel_length", queryInfo.getTunnel_length());
        }
        if (queryInfo.getTunnel_height() != null) {
            paramMap.put("tunnel_height", queryInfo.getTunnel_height());
        }
        if (queryInfo.getTraffic_time() != null) {
            paramMap.put("traffic_time", queryInfo.getTraffic_time());
        }
        if (queryInfo.getTunnel_direction() != null) {
            paramMap.put("tunnel_direction", queryInfo.getTunnel_direction());
        }
        if (queryInfo.getLongitude() != null) {
            paramMap.put("longitude", queryInfo.getLongitude());
        }
        if (queryInfo.getLatitude() != null) {
            paramMap.put("latitude", queryInfo.getLatitude());
        }
        if (queryInfo.getOffice_code() != null) {
            paramMap.put("office_code", queryInfo.getOffice_code());
        }
        if (queryInfo.getCreate_timestamp() != null) {
            paramMap.put("create_timestamp", queryInfo.getCreate_timestamp());
        }
        return dao.qryObjList(SqlString.AUTO2_TL_TUNNEL_INFO_LIST, setSymbolPKPrior(paramMap, isExcludePKFlag, false),
                TlTunnelInfo.class);
    }

    /**
     * get list by "or" method, need new TlTunnelInfo() include query-params
     */
    public static List<TlTunnelInfo> getSingleTableListByOrMethod(AbstractSimpleDao dao, TlTunnelInfo queryInfo,
            Boolean isExcludePKFlag) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (queryInfo.getId() != null) {
            paramMap.put("id", queryInfo.getId());
        }
        if (queryInfo.getTunnel_name() != null) {
            paramMap.put("tunnel_name", queryInfo.getTunnel_name());
        }
        if (queryInfo.getTunnel_code() != null) {
            paramMap.put("tunnel_code", queryInfo.getTunnel_code());
        }
        if (queryInfo.getConstruction_company() != null) {
            paramMap.put("construction_company", queryInfo.getConstruction_company());
        }
        if (queryInfo.getTunnel_length() != null) {
            paramMap.put("tunnel_length", queryInfo.getTunnel_length());
        }
        if (queryInfo.getTunnel_height() != null) {
            paramMap.put("tunnel_height", queryInfo.getTunnel_height());
        }
        if (queryInfo.getTraffic_time() != null) {
            paramMap.put("traffic_time", queryInfo.getTraffic_time());
        }
        if (queryInfo.getTunnel_direction() != null) {
            paramMap.put("tunnel_direction", queryInfo.getTunnel_direction());
        }
        if (queryInfo.getLongitude() != null) {
            paramMap.put("longitude", queryInfo.getLongitude());
        }
        if (queryInfo.getLatitude() != null) {
            paramMap.put("latitude", queryInfo.getLatitude());
        }
        if (queryInfo.getOffice_code() != null) {
            paramMap.put("office_code", queryInfo.getOffice_code());
        }
        if (queryInfo.getCreate_timestamp() != null) {
            paramMap.put("create_timestamp", queryInfo.getCreate_timestamp());
        }
        return dao.qryObjList(SqlString.AUTO2_TL_TUNNEL_INFO_LIST_BY_OR,
                setSymbolPKPrior(paramMap, isExcludePKFlag, true), TlTunnelInfo.class);
    }

    private static Map<String, Object> setSymbolPKPrior(Map<String, Object> paramMap, Boolean exclude_pk, Boolean isOr) {
        if (paramMap == null || paramMap.size() == 0) {
            return paramMap;
        }
        if (exclude_pk) {
            for (String ss : pkColArr) {
                paramMap.put(ss + "Symbol", "exists");
            }
            paramMap.put("exclude_pk", true);
        }
        Boolean flag = true;
        if (isOr) {
            paramMap.put("params_exists", true);
            for (String ss : columnNameArr) {
                if (flag) {
                    if (paramMap.containsKey(ss) && !paramMap.containsKey(ss + "Symbol")) {
                        paramMap.put(ss + "Symbol", " ");
                        flag = false;
                    }
                } else {
                    paramMap.put(ss + "Symbol", " or ");
                }
            }
        }
        return paramMap;
    }
}