package com.ibm.webdev.app.jersey.caching;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.webdev.app.jersey.resources.dto.MovieDto;



public class MovieCaching {

	private static MovieCaching             store     = new MovieCaching();
    private static final Map<String, MovieDto> movies    = Collections.synchronizedMap(new HashMap<String, MovieDto>());

    static {
    }

    /**
     * 提供该类的实例(单例)
     * 
     * @return 类的实例
     */
    public static MovieCaching getInstance() {
        return store;
    }
    public static MovieCaching getInstanceOld() {
    	int id=0;
	  	String thereid = String.valueOf(++id);
	    movies.put(thereid, new MovieDto(thereid,"流浪地球 2","郭帆","吴京","https://img1.doubanio.com/view/photo/m/public/p2885955777.webp",2023,173));
	    thereid = String.valueOf(++id);
	    movies.put(thereid, new MovieDto(thereid,"满江红","张艺谋","易烊千玺","https://img1.doubanio.com/view/photo/m/public/p2886465677.webp",2023,159));
	    thereid = String.valueOf(++id);
	    movies.put(thereid, new MovieDto(thereid,"流浪地球","郭帆","吴京","https://img2.doubanio.com/view/photo/m/public/p2545472803.webp",2019,125));
	    thereid = String.valueOf(++id);
	    movies.put(thereid, new MovieDto(thereid,"无名","程耳","梁朝伟","https://img1.doubanio.com/view/photo/m/public/p2886187418.webp",2023,128));
        return store;
    }
    
    public static MovieCaching getInstance(List<MovieDto> instanceList) {
    	if (store.movies.isEmpty()) {
	    	for(MovieDto dto: instanceList) {
	        	store.putMovie(dto.getId(), dto);    		
	    	}
    	}
    	return store;
    }
    /**
     * 初始化方法
     */
    private MovieCaching() {
    }

    /**
     * 返回包含所有的缓存对象
     * 
     * @return 全对象
     */
    public Map<String, MovieDto> getMovies() {
        return Collections.unmodifiableMap(movies);
    }

    /**
     * 对指定的ID更新或创建对象
     * 
     * @param ID
     * @param 对象
     */
    public void putMovie(String key, MovieDto obj) {
        movies.put(key, obj);
    }

    /**
     * 获取单个对象by ID
     * 
     * @param 关键字
     * @return 对象
     */
    public MovieDto getMovie(String key) {
        return movies.get(key);
    }

    /**
     * 是否包含关键字by ID
     * 
     * @param 关键字
     * @return 真假
     */
    public boolean containsMovie(String key) {
        return movies.containsKey(key);
    }

    /**
     * 创建一个新的 ID
     * 
     * @return ID
     */
    public static String getNewId() {
    	String newId = "0";
    	for(String key : movies.keySet()) {
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
    public void deleteMovie(String key) {
        movies.remove(key);
    }

}
