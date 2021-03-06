package com.sinyd.sframe.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定短时间缓存的注解，对应于ehcache.xml中的short缓存设置
 * 
 * @author Allen
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShortDataCacheAnnotation {
}
