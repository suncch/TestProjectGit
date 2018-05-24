package com.sinyd.sframe.util.cache;
/**
 * 用于处理缓存公共接口方法
 * 
 * @author Allen
 * */
public interface IDataCache {
	public Object getCacheValue(String key, String type);
	public void putCacheValue(String key, Object value, String type);
}
