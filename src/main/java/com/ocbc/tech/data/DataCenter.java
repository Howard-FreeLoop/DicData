package com.ocbc.tech.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ocbc.tech.entity.SysNameBean;

public class DataCenter {
	
	private static Map<String, SysNameBean> sysNameMap = new ConcurrentHashMap<>();

	public static void addSysName(String shortName, SysNameBean bean) {
		sysNameMap.put(shortName, bean);
	}
	
	public static SysNameBean getNameBean(String shortName) {
		return sysNameMap.get(shortName);
	}
}
