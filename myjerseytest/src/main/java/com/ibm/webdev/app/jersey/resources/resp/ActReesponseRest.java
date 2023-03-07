package com.ibm.webdev.app.jersey.resources.resp;

import java.io.Serializable;
import java.net.URI;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("含用户操作的响应对象实体类")
public class ActReesponseRest<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="数据库操作")
	private String action;
	@ApiModelProperty(value="实体类内容")
	private T content;
	@ApiModelProperty(value="发布地址")
	private URI link;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	public URI getLink() {
		return link;
	}
	public void setLink(URI link) {
		this.link = link;
	}
}
