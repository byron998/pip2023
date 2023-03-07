package com.ibm.webdev.app.jersey.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import com.ibm.webdev.app.jersey.caching.MovieCaching;
import com.ibm.webdev.app.jersey.resources.dto.MovieDto;
import com.ibm.webdev.app.jersey.resources.dto.Validgroup;
import com.ibm.webdev.app.jersey.resources.resp.ActReesponseRest;
import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.service.MovieService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "电影信息管理API", produces=MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
@Path("/movies")
public class MovieResource {
    private static final String SUB_RESOURCE_PATH = "{movie}";
	private static final JsonBuilderFactory bf = Json.createBuilderFactory(null);
    
	@Autowired
    private MovieService service;
		
    @GET
    @ApiOperation("获取全部电影信息")
    @ApiResponses({
    	@ApiResponse(code=200,message="正常处理",responseContainer="List",response=ActReesponseRest.class),
        @ApiResponse(code=400,message="请求参数输入有误")
    })
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest<MovieDto>> getMovies(@Context UriInfo uriInfo) throws Exception{
    	this.initialize();
    	List<ActReesponseRest<MovieDto>> restList = new ArrayList<>();
        Map<String, MovieDto> movies = MovieCaching.getInstance().getMovies();
       
        for (String key : movies.keySet()) {
        	ActReesponseRest<MovieDto> restObj = new ActReesponseRest<MovieDto>();
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	restObj.setAction("getone");
        	restObj.setLink(location);
        	restObj.setContent(movies.get(key));
        	restList.add(restObj);
        }
        return restList;
    }

    @POST
    @ApiOperation("提交电影信息")
    @ApiImplicitParams({
//        @ApiImplicitParam(paramType="body",name="name",dataType="String",required=true,value="电影的姓名",defaultValue="泰坦尼克号"),
//        @ApiImplicitParam(paramType="body",name="actor",dataType="String",required=true,value="电影的主演",defaultValue="刘德华"),
//        @ApiImplicitParam(paramType="body",name="director",dataType="String",required=true,value="电影的导演",defaultValue="刘德华")
    })
    @ApiResponses({
       	@ApiResponse(code=200,message="正常处理",responseContainer="Map",response=ActReesponseRest.class),
        @ApiResponse(code=400,message="请求参数输入有误")
    })
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest<MovieDto> createMovie(@Valid @Validated({Validgroup.insert.class}) MovieDto content,
                                   @Context UriInfo uriInfo) throws Exception  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        //new ID
        String movieId = MovieCaching.getNewId();
        content.setId(movieId);
        
        // 插入新的缓存记录
        MovieCaching.getInstance().putMovie(movieId, content);
        // 插入数据库
        Movie insertdata = new Movie();
        BeanUtils.copyProperties(content, insertdata);
        insertdata.setId(Long.valueOf(movieId));
        boolean insertResult = service.insertMovieWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(movieId).build();

        ActReesponseRest<MovieDto> rest = new ActReesponseRest<MovieDto>();
        rest.setAction("inserted");
        rest.setContent(content);
        rest.setLink(location);
        return rest;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @ApiOperation("获取单个电影信息")
    @ApiImplicitParams({
//        @ApiImplicitParam(paramType="path",name="{movie}",dataType="String",required=true,value="电影的ID",defaultValue="1"),
    })
    @ApiResponses({
    	@ApiResponse(code=200,message="正常处理",responseContainer="Map",response=MovieDto.class),
        @ApiResponse(code=404,message="找不到对应的数据")
    })
    @Produces( MediaType.APPLICATION_JSON )
    public Object getMovie(@Context UriInfo uriInfo,
                               @PathParam("movie") String movieId) throws Exception  {
    	this.initialize();
    	// 检查是否存在
    	MovieDto movie = MovieCaching.getInstance().getMovie(movieId);
        if (movie == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return movie;
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @ApiOperation("修改单个电影信息")
    @ApiImplicitParams({
//        @ApiImplicitParam(paramType="path",name="{movie}",dataType="String",required=true,value="电影的ID",defaultValue="1"),
//        @ApiImplicitParam(paramType="body",name="name",dataType="String",required=true,value="电影的姓名",defaultValue="泰坦尼克号"),
//        @ApiImplicitParam(paramType="body",name="actor",dataType="String",required=true,value="电影的主演",defaultValue="刘德华"),
//        @ApiImplicitParam(paramType="body",name="director",dataType="String",required=true,value="电影的导演",defaultValue="刘德华")
    })
    @ApiResponses({
    	@ApiResponse(code=200,message="正常处理",responseContainer="Map",response=ActReesponseRest.class),
        @ApiResponse(code=400,message="请求参数输入有误"),
        @ApiResponse(code=404,message="找不到对应的数据")
    })
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest<MovieDto> updateMovie(@Valid @Validated({Validgroup.insert.class}) MovieDto content,
                                    @Context UriInfo uriInfo,
                                    @PathParam("movie") String movieId) throws Exception {
    	this.initialize();
    	// 检查是否存在
    	MovieDto value = MovieCaching.getInstance().getMovie(movieId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        // 更新缓存
        MovieCaching.getInstance().putMovie(movieId, content);
        // 更新数据库
        Movie updatedata = new Movie();
        BeanUtils.copyProperties(content, updatedata);
        updatedata.setId(Long.valueOf(movieId));
        boolean updateResult = service.updateMovieById(updatedata);
        if (!updateResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment().build();
        
        ActReesponseRest<MovieDto> rest = new ActReesponseRest<MovieDto>();
        rest.setAction("updated");
        rest.setContent(content);
        rest.setLink(location);
        return rest;
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @ApiOperation("删除单个电影信息")
    @ApiImplicitParams({
//        @ApiImplicitParam(paramType="path",name="movieId",dataType="String",required=true,value="电影的ID",defaultValue="1"),
    })
    @ApiResponses({
    	@ApiResponse(code=200,message="正常处理",responseContainer="Map",response=ActReesponseRest.class),
        @ApiResponse(code=404,message="找不到对应的数据")
    })
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest<MovieDto> deleteMovie(@Context UriInfo uriInfo,
                                    @PathParam("movie") String movieId) throws Exception {
    	this.initialize();
    	//JsonObject json = new JsonObject();
    	// 检查是否存在
    	MovieDto movie = MovieCaching.getInstance().getMovie(movieId);
        if (movie == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除缓存
        MovieCaching.getInstance().deleteMovie(movieId);
        // 删除数据库
        service.deleteMovieById(Long.valueOf(movieId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(movie.getId()).build();
        ActReesponseRest<MovieDto> rest = new ActReesponseRest<MovieDto>();
        rest.setAction("deleted");
        rest.setLink(location);
        return rest;
    }
    
    /**
     * 初期化缓存
     */
    public void initialize() {
    	if(MovieCaching.getInstance().getMovies().isEmpty()) {
    		List<Movie> datalist = service.findAllMovies();
			List<MovieDto> instancelist = new ArrayList<>();
			for(Movie data : datalist) {
				MovieDto dto = new MovieDto();
				BeanUtils.copyProperties(data, dto);
				dto.setId(String.valueOf(data.getId()));
				instancelist.add(dto);
			}
			MovieCaching.getInstance(instancelist);
    	}
    }
    
    /**
     * JsonObject build
     */
    public static JsonObject parseDto (MovieDto dto) {
        return bf.createObjectBuilder()
                .add("id", dto.getId())
                .add("name", dto.getName())
                .add("actor", dto.getActor())
                .add("director", dto.getDirector())
                .add("placard", dto.getPlacard())
                .add("year", dto.getYear())
                .add("mins", dto.getMins())
                .build();
    }
}
