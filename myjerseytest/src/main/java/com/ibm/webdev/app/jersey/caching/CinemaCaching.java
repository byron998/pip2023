package com.ibm.webdev.app.jersey.caching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.webdev.app.jersey.resources.dto.CinemaDto;

public class CinemaCaching {

    private static CinemaCaching             store     = new CinemaCaching();
    private static final Map<String, CinemaDto> cinemas   = Collections.synchronizedMap(new HashMap<String, CinemaDto>());

    static {
//    	String thereid = String.valueOf(++id);
//        cinemas.put(thereid, new CinemaDto(thereid, "大光明影院（博山路店）","上海","上海市-浦东新区-博山东路88号4层1号",9));
//    	thereid = String.valueOf(++id);
//        cinemas.put(thereid, new CinemaDto(thereid, "上海国际影城（国华广场店）","上海","上海市-杨浦区-闸殷路1599号上海国华广场5楼",5));
//        thereid = String.valueOf(++id);
//        cinemas.put(thereid, new CinemaDto(thereid, "万达国际影城（五角场店）","上海","上海市-杨浦区-淞沪路77号万达广场",8));
//        thereid = String.valueOf(++id);
//        cinemas.put(thereid, new CinemaDto(thereid, "万达国际影城（南丰城店）","上海","上海市-长宁区-天山街道茅台社区遵义路100号-虹桥南丰城4楼",11));
    }

    /**
     * 提供该类的实例(单例)
     * 
     * @return 类的实例
     */
    public static CinemaCaching getInstance() {
        return store;
    }
    public static CinemaCaching getInstance(List<CinemaDto> instanceList) {
    	if (store.cinemas.isEmpty()) {
	    	for(CinemaDto dto: instanceList) {
	        	store.putCinema(dto.getId(), dto);    		
	    	}
    	}
    	return store;
    }
    /**
     * 初始化方法
     */
    private CinemaCaching() {
    }

    /**
     * 返回包含所有的缓存对象
     * 
     * @return 全对象
     */
    public Map<String, CinemaDto> getCinemas() {
        return Collections.unmodifiableMap(cinemas);
    }

    /**
     * 对指定的ID更新或创建对象
     * 
     * @param ID
     * @param 对象
     */
    public void putCinema(String key, CinemaDto obj) {
        cinemas.put(key, obj);
    }

    /**
     * 获取单个对象by ID
     * 
     * @param 关键字
     * @return 对象
     */
    public CinemaDto getCinema(String key) {
        return cinemas.get(key);
    }

    /**
     * 是否包含关键字by ID
     * 
     * @param 关键字
     * @return 真假
     */
    public boolean containsCinema(String key) {
        return cinemas.containsKey(key);
    }

    /**
     * 创建一个新的 ID
     * 
     * @return ID
     */
    public static String getNewId() {
    	String newId = "0";
    	for(String key : cinemas.keySet()) {
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
    public void deleteCinema(String key) {
        cinemas.remove(key);
    }

}
