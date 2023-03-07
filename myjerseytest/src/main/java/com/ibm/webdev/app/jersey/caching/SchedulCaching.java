package com.ibm.webdev.app.jersey.caching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.webdev.app.jersey.resources.dto.SchedulDto;

public class SchedulCaching {

	private static SchedulCaching             store     = new SchedulCaching();
    private static final Map<String, SchedulDto> scheduls  = Collections.synchronizedMap(new HashMap<String, SchedulDto>());

    static {
//    	String thereid = String.valueOf(++id);
//        scheduls.put(thereid, new SchedulDto(thereid, "1","1",6,108));
//        thereid = String.valueOf(++id);
//        scheduls.put(thereid, new SchedulDto(thereid, "1","2",6,108));
//    	thereid = String.valueOf(++id);
//        scheduls.put(thereid, new SchedulDto(thereid, "2","1",6,250));
//    	thereid = String.valueOf(++id);
//        scheduls.put(thereid, new SchedulDto(thereid, "2","3",6,400));
    }

    /**
     * 提供该类的实例(单例)
     * 
     * @return 类的实例
     */
    public static SchedulCaching getInstance() {
        return store;
    }
    public static SchedulCaching getInstance(List<SchedulDto> instanceList) {
    	if (store.scheduls.isEmpty()) {
	    	for(SchedulDto dto: instanceList) {
	        	store.putSchedul(dto.getId(), dto);    		
	    	}
    	}
    	return store;
    }
    /**
     * 初始化方法
     */
    private SchedulCaching() {
    }

    /**
     * 返回包含所有的缓存对象
     * 
     * @return 全对象
     */
    public Map<String, SchedulDto> getScheduls() {
        return Collections.unmodifiableMap(scheduls);
    }

    /**
     * 对指定的ID更新或创建对象
     * 
     * @param ID
     * @param 对象
     */
    public void putSchedul(String key, SchedulDto obj) {
        scheduls.put(key, obj);
    }

    /**
     * 获取单个对象by ID
     * 
     * @param 关键字
     * @return 对象
     */
    public SchedulDto getSchedul(String key) {
        return scheduls.get(key);
    }

    /**
     * 是否包含关键字by ID
     * 
     * @param 关键字
     * @return 真假
     */
    public boolean containsSchedul(String key) {
        return scheduls.containsKey(key);
    }

    /**
     * 创建一个新的 ID
     * 
     * @return ID
     */
    public static String getNewId() {
    	String newId = "0";
    	for(String key : scheduls.keySet()) {
    		if(Integer.valueOf(newId).compareTo(Integer.valueOf(key)) < 0) {
    			newId = key;
    		}
    	}
    	return String.valueOf(1+Integer.valueOf(newId).intValue());
    }

    /**
     * 删除对象by ID
     * 
     * @param ID.
     */
    public void deleteSchedul(String key) {
        scheduls.remove(key);
    }

}
