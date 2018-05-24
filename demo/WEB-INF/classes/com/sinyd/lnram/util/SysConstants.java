package com.sinyd.lnram.util;

import java.util.ResourceBundle;

public class SysConstants {

    /*
     * static { Properties properties = new Properties(); InputStream
     * inputStream =
     * Object.class.getClassLoader().getResourceAsStream("sys.properties");
     * 
     * try { properties.load(inputStream); } catch (IOException e) {
     * e.printStackTrace(); } finally { try { inputStream.close(); } catch
     * (IOException e) { e.printStackTrace(); } } }
     */

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("sys");

    public final static String UNIT_LEVEL = "1";
    public final static String PROJECT_P_PROJ_ID = "0";
    public final static String SECTION_P_SECT_ID = "0";
    public final static String COMBOX_ALL = "-1";
    public final static String COMBOX_SELECT = "-2";
    /**
     * 公有材料类型
     */
    public final static String PUB_MTRL_TYPE = "0";
    /**
     * 私有材料类型
     */
    public final static String PRIVATE_MTRL_TYPE = "1";
    /**
     * 是
     */
    public final static String IS_YES = "1";

    /**
     * 否
     */
    public final static String IS_NO = "0";

    /**
     * 字典表全部
     */
    public final static String DICT_ALL = "dict_all";
    /**
     * 字典表空
     */
    public final static String DICT_EMPTY = "dict_empty";

    /**
     * 省局代码
     */
    // public final static String OFFICE_CODE_SHENGJU = "SJ";
    public final static String OFFICE_CODE_SHENGJU = RESOURCE_BUNDLE.getString("office_code_sj");

    public final static String HIGH_WAY_ROUTE_TYPE_TRACK = "2";

    public static final String PIC_PHYSICAL_SAVE_PATH = RESOURCE_BUNDLE.getString("pic_physical_path");
    public static final String PIC_PHYSICAL_SAVE_PATH2 = RESOURCE_BUNDLE.getString("pic_physical_path2");

    public static final String PIC_VIRTUAL_SAVE_PATH = RESOURCE_BUNDLE.getString("pic_virtual_path");
    public static final String PIC_VIRTUAL_SAVE_PATH2 = RESOURCE_BUNDLE.getString("pic_virtual_path2");

    public static final String WARN_RECEIVE_TYPE_MANAGER = "1";
    public static final String WARN_RECEIVE_TYPE_STAFF = "2";

    // 检修保养计划主表--审批状态 //新建: 0,已提交: 1,已通过: 2,已实施: 3,已确认:4.被驳回:9
    public static final String MAINTAIN_PLAN_STATUS0 = "0";
    public static final String MAINTAIN_PLAN_STATUS1 = "1";
    public static final String MAINTAIN_PLAN_STATUS2 = "2";
    public static final String MAINTAIN_PLAN_STATUS3 = "3";
    public static final String MAINTAIN_PLAN_STATUS4 = "4";
    public static final String MAINTAIN_PLAN_STATUS9 = "9";

    // 检修保养计划子表_设备 --审批状态 //默认未保养 0， 已保养 1， 已确认 2 ， 不认可 9
    public static final String PLAN_EQUIPMENT_STATUS0 = "0";
    public static final String PLAN_EQUIPMENT_STATUS1 = "1";
    public static final String PLAN_EQUIPMENT_STATUS2 = "2";
    public static final String PLAN_EQUIPMENT_STATUS9 = "9";

    //附件所属模块 默认:0,  养护:1, 日精检:2, 设备维修:3, 应急事件:4,预警设置:5;车辆驶入:6;车辆驶出:7 
    public static final String ATTACHMENT_PARENT_TYPE_STATUS0 = "0";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS1 = "1";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS2 = "2";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS3 = "3";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS4 = "4";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS5 = "5";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS6 = "6";
    public static final String ATTACHMENT_PARENT_TYPE_STATUS7 = "7";

    //异常信息
    public static final String SAVE_ERROR = "保存信息失败！";
    public static final String UPDATE_ERROR = "更新信息失败！";
    public static final String DELETE_ERROR = "删除信息失败！";
    public static final String VEHICLE_QUERY_ERROR = "未查询到车辆信息！";
    public static final String ATTACHMENT_QUERY_ERROR = "未查询到附件信息！";

    public static final String NET_RESPONSE_EQUIP_CHECK_UPLOAD_RESULT_OK = "check_upload_ok";
    public static final String NET_RESPONSE_EQUIP_CHECK_UPLOAD_RESULT_NG = "check_upload_ng";
    public static final String NET_RESPONSE_EQUIP_MAINT_APPLY_UPLOAD_RESULT_OK = "maint_apply_upload_ok";
    public static final String NET_RESPONSE_EQUIP_MAINT_APPLY_UPLOAD_RESULT_NG = "maint_apply_upload_ng";

    //车辆行驶相关
    public static final String PIC_DRIVING_PATH = "/Driving/";
    public static final String NO_VEHICLE_CODE = "车牌未识别";

}
