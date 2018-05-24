package com.sinyd.sframe.util.cache.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.stereotype.Component;

import com.sinyd.sframe.util.cache.IDataCache;

/**
 * 使用ehcache来实现缓存公共接口方法
 * 
 * @author Allen
 * */
@Component("dataEhcache")
public class DataEhcacheImpl implements IDataCache {
	private CacheManager manage;
	private Cache shortCache;
	private Cache mediumCache;
	private Cache longCache;

	public DataEhcacheImpl() {
		manage = CacheManager.create(this.getClass().getResource("/ehcache.xml"));
		shortCache = manage.getCache("short");
		mediumCache = manage.getCache("medium");
		longCache = manage.getCache("long");
	}

	@Override
	public Object getCacheValue(String key, String type) {
		Element element = null;
		if ("short".equals(type)) {
			element = shortCache.get(key);
		} else if ("medium".equals(type)) {
			element = mediumCache.get(key);
		} else if ("long".equals(type)) {
			element = longCache.get(key);
		}
		if (element != null) {
			return element.getObjectValue();
		}
		return null;
	}

	@Override
	public void putCacheValue(String key, Object value, String type) {
		if ("short".equals(type)) {
			shortCache.put(new Element(key, value));
		} else if ("medium".equals(type)) {
			mediumCache.put(new Element(key, value));
		} else if ("long".equals(type)) {
			longCache.put(new Element(key, value));
		}
	}
}
