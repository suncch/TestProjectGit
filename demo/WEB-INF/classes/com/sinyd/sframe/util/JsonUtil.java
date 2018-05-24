package com.sinyd.sframe.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {

	/**
	 * 根据传入的json数组，返回指定包含类型的集合
	 * 
	 * @param jsonStr [{id:1,name:gx},{id:2,name:wxm}]
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonArrayToList(String jsonStr, Class<T> clazz) {
		if (jsonStr == null || jsonStr.trim().equals("")) {
			return null;
		}
		List<T> resultList = new ArrayList<T>();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonStr); // 将json字符串转换成JsonElement
		JsonArray jsonArray = jsonElement.getAsJsonArray(); // 将JsonElement转换成JsonArray
		Iterator<JsonElement> it = jsonArray.iterator(); // Iterator处理
		while (it.hasNext()) { // 循环
			jsonElement = (JsonElement) it.next(); // 提取JsonElement
			String menu = jsonElement.toString(); // JsonElement转换成String
			T bean = gson.fromJson(menu, clazz); // String转化成JavaBean
			resultList.add(bean); // 加入List
		}
		return resultList;
	}

	/**
	 * 将传入的对象转为json字符串，支持不固定参数 
	 * 使用示例：allToJson(1,2,3)，allToJson()， allToJson(null)， allToJson(new TreeBean(), "aaa")
	 * 
	 * @author Allen
	 * @return json字符串
	 * */
	public static String allToJson(Object... objArray) {
		Gson gson = new Gson();
		if(objArray!=null){
			return gson.toJson(objArray);
		}
		return gson.toJson(new Object[0]);
	}
}
