package com.sinyd.lnram.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sinyd.lnram.vehicle.vo.TlVehicleDrivingInfoSearchBeanVO;
import com.sinyd.platform.utiltools.util.StringUtil;
import com.sinyd.sframe.util.GlobalFileUtil;

public class ExcelUtil {

    /**
     * 综合巡查事件导出excel功能
     * 
     * @param response
     * @param request
     * @param paramList DAO中查询得到的结果集
     * @param queryInfo 页面查询条件
     * @throws FileNotFoundException
     * @throws IOException
     * @author lvq
     */
    /*public static void exportXlsForComplexCase(HttpServletResponse response, HttpServletRequest request, List<Map<String, Object>> paramList, RoadMngCplxIspctEgcyCaseSearchBeanVO queryInfo) throws FileNotFoundException, IOException {
    	//模板路径
    	String nowPath = GlobalFileUtil.class.getClassLoader().getResource("/").getPath();
    	nowPath = nowPath.substring(0, nowPath.indexOf("WEB-INF"));
    	String realPath = nowPath + "/resources/printtemplate/complexCaseTemplate.xls";
    	
    	//读取模板
    	HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(realPath));
    	HSSFSheet sheet = wb.getSheetAt(0);
    	
    	//各种样式
    	HSSFFont font = wb.createFont();
    	font.setFontName("宋体");
    	font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	font.setColor(HSSFFont.COLOR_NORMAL);
    	
    	HSSFCellStyle redStyle = wb.createCellStyle();
    	redStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	redStyle.setFillForegroundColor(new HSSFColor.RED().getIndex());
    	//redStyle.setFillBackgroundColor(new HSSFColor.RED().getIndex());
    	redStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	redStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	//redStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	redStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	redStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    	redStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    	redStyle.setFont(font);
    	
    	HSSFCellStyle greenStyle = wb.createCellStyle();
    	greenStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	greenStyle.setFillForegroundColor(new HSSFColor.GREEN().getIndex());
    	//greenStyle.setFillBackgroundColor(new HSSFColor.GREEN().getIndex());
    	greenStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	greenStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	//greenStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	greenStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	greenStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    	greenStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    	greenStyle.setFont(font);
    	
    	HSSFCellStyle commonCellStyle = wb.createCellStyle();
    	commonCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	commonCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	//commonCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	commonCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	commonCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    	commonCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    	commonCellStyle.setFont(font);
    	
    	//时间
    	String timeString = "日期: ";
    	String subTimeString = "";
    	if (StringUtil.isNotBlank(queryInfo.getReport_start_time())) {
    		subTimeString += queryInfo.getReport_start_time().substring(0, 10);
    	}
    	
    	subTimeString += " - ";
    	
    	if (StringUtil.isNotBlank(queryInfo.getReport_end_time())) {
    		subTimeString += queryInfo.getReport_end_time().substring(0, 10);
    	}
    	
    	HSSFRow timeRow = sheet.getRow(2);
    	HSSFCell timeCell = timeRow.getCell(0);
    	timeCell.setCellValue(timeString + subTimeString);
    	
    	//sheet页名字
    	wb.setSheetName(0, subTimeString);
    	
    	//标题
    	String office_name;
    	if (null == queryInfo.getOffice_code() || SysConstants.COMBOX_SELECT.equals(queryInfo.getOffice_code())) {
    		office_name = "全省管理处";
    	} else {
    		office_name = queryInfo.getOffice_name();
    	}
    	HSSFRow nameRow = sheet.getRow(1);
    	HSSFCell nameCell = nameRow.getCell(0);
    	String tempNameString = nameCell.getStringCellValue();
    	tempNameString = tempNameString.replace("office_name", office_name);
    	nameCell.setCellValue(tempNameString);
    	
    	//从第6行开始填写
    	int rowIndex = 5;
    	int num = 0;
    	
    	if (null != paramList && !paramList.isEmpty()) {
    		//数据
    		for (Map<String,Object> map : paramList) {
    			HSSFRow row = sheet.createRow(rowIndex);
    			rowIndex++;
    			
    			row.createCell(0).setCellValue(String.valueOf(++num));
    			row.getCell(0).setCellStyle(commonCellStyle);
    			
    			row.createCell(1).setCellValue((String) map.get("report_people_name"));
    			row.getCell(1).setCellStyle(commonCellStyle);
    			
    			row.createCell(2).setCellValue((String) map.get("report_time"));
    			row.getCell(2).setCellStyle(commonCellStyle);
    			
    			row.createCell(3).setCellValue((String) map.get("happen_place"));
    			row.getCell(3).setCellStyle(commonCellStyle);
    			
    			row.createCell(4).setCellValue((String) map.get("case_content"));
    			row.getCell(4).setCellStyle(redStyle);
    			
    			row.createCell(5).setCellValue((String) map.get("photo_flag"));
    			row.getCell(5).setCellStyle(commonCellStyle);
    			
    			row.createCell(6).setCellValue((String) map.get("dispatcher_name_1"));
    			row.getCell(6).setCellStyle(commonCellStyle);
    			
    			row.createCell(7).setCellValue((String) map.get("dispatch_time"));
    			row.getCell(7).setCellStyle(commonCellStyle);
    			
    			row.createCell(8).setCellValue((String) map.get("case_type"));
    			row.getCell(8).setCellStyle(commonCellStyle);
    			
    			row.createCell(9).setCellValue((String) map.get("case_handle_time"));
    			row.getCell(9).setCellStyle(commonCellStyle);
    			
    			row.createCell(10).setCellValue((String) map.get("main_responsibility_name"));
    			row.getCell(10).setCellStyle(commonCellStyle);
    			
    			row.createCell(11).setCellValue((String) map.get("implement_start_time"));
    			row.getCell(11).setCellStyle(commonCellStyle);
    			
    			row.createCell(12).setCellValue((String) map.get("coherent_units"));
    			row.getCell(12).setCellStyle(commonCellStyle);
    			
    			row.createCell(13).setCellValue((String) map.get("implement_limit_time"));
    			row.getCell(13).setCellStyle(commonCellStyle);
    			
    			row.createCell(14).setCellValue((String) map.get("implement_end_time"));
    			row.getCell(14).setCellStyle(commonCellStyle);
    			
    			row.createCell(15).setCellValue((String) map.get("confirm_result"));
    			String confirm_flag = (String) map.get("confirm_flag");
    			if (null != confirm_flag && "1".equals(confirm_flag)) {		//1为绿色
    				row.getCell(15).setCellStyle(greenStyle);
    			} else {		//0为红色
    				row.getCell(15).setCellStyle(redStyle);
    			}
    			
    			row.createCell(16).setCellValue((String) map.get("confirm_people_name"));
    			row.getCell(16).setCellStyle(commonCellStyle);
    			
    			row.createCell(17).setCellValue((String) map.get("confirm_time"));
    			row.getCell(17).setCellStyle(commonCellStyle);
    			
    			row.createCell(18).setCellValue((String) map.get("dispatcher_name_2"));
    			row.getCell(18).setCellStyle(commonCellStyle);
    		}
    	}
    	
    	OutputStream os = response.getOutputStream();
    	response.reset();
    	response.setHeader("Pragma", "public");
    	response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode("综合巡查闭合管理流程表", "UTF-8") + ".xls\"");
    	// 设置HTTP响应的MIME类型为excel
    	response.setContentType("application/vnd.ms-excel");

    	wb.write(os);
    }*/

    public static void exportXlsForVehicleDrving(HttpServletResponse response, HttpServletRequest request,
            List<Map<String, Object>> paramList, TlVehicleDrivingInfoSearchBeanVO queryInfo)
            throws FileNotFoundException, IOException {

        //模板路径
        String nowPath = GlobalFileUtil.class.getClassLoader().getResource("/").getPath();
        nowPath = nowPath.substring(0, nowPath.indexOf("WEB-INF"));
        String realPath = nowPath + "/resources/printtemplate/vehicleDrving.xls";

        //读取模板
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(realPath));
        HSSFSheet sheet = wb.getSheetAt(0);

        //各种样式
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setColor(HSSFFont.COLOR_NORMAL);

        HSSFCellStyle commonCellStyle = wb.createCellStyle();
        commonCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        commonCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        commonCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        commonCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        commonCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        commonCellStyle.setFont(font);

        //时间
        String timeString = "日期: ";
        String subTimeString = "";
        if (StringUtil.isNotBlank(queryInfo.getBegin_entry_time())) {
            subTimeString += queryInfo.getBegin_entry_time().substring(0, 10);
        }

        subTimeString += " - ";

        if (StringUtil.isNotBlank(queryInfo.getBegin_out_of_time())) {
            subTimeString += queryInfo.getBegin_out_of_time().substring(0, 10);
        }

        HSSFRow timeRow = sheet.getRow(2);
        HSSFCell timeCell = timeRow.getCell(0);
        timeCell.setCellValue(timeString + subTimeString);

        //sheet页名字
        wb.setSheetName(0, subTimeString);

        //标题
        String office_name;
        if (null == queryInfo.getOffice_code() || SysConstants.COMBOX_SELECT.equals(queryInfo.getOffice_code())) {
            office_name = "全省管理处";
        } else {
            office_name = queryInfo.getOffice_code();
        }
        HSSFRow nameRow = sheet.getRow(1);
        HSSFCell nameCell = nameRow.getCell(0);
        String tempNameString = nameCell.getStringCellValue();
        tempNameString = tempNameString.replace("office_name", office_name);
        nameCell.setCellValue(tempNameString);

        //从第6行开始填写
        int rowIndex = 5;
        int num = 0;

        if (null != paramList && !paramList.isEmpty()) {
            //数据
            for (Map<String, Object> map : paramList) {
                HSSFRow row = sheet.createRow(rowIndex);
                rowIndex++;

                row.createCell(0).setCellValue(String.valueOf(++num));
                row.getCell(0).setCellStyle(commonCellStyle);

                row.createCell(1).setCellValue((String) map.get("office_name"));
                row.getCell(1).setCellStyle(commonCellStyle);

                row.createCell(2).setCellValue((String) map.get("tunnel_name"));
                row.getCell(2).setCellStyle(commonCellStyle);

                row.createCell(3).setCellValue((String) map.get("vehicle_type_name"));
                row.getCell(3).setCellStyle(commonCellStyle);

                row.createCell(4).setCellValue((String) map.get("vehicle_code"));
                row.getCell(4).setCellStyle(commonCellStyle);

                row.createCell(5).setCellValue((String) map.get("entry_time"));
                row.getCell(5).setCellStyle(commonCellStyle);

                row.createCell(6).setCellValue((String) map.get("out_of_time"));
                row.getCell(6).setCellStyle(commonCellStyle);

            }
        }

        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + java.net.URLEncoder.encode("车辆通行记录表", "UTF-8") + ".xls\"");
        // 设置HTTP响应的MIME类型为excel
        response.setContentType("application/vnd.ms-excel");

        wb.write(os);

    }
}
