package com.ibm.webdev.app.jersey.resources.resp;

import java.io.Serializable;
import java.util.List;

public class SimpleMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> msg;
	public SimpleMsg(List<String> msg) {
		this.msg = msg;
	}
	public List<String> getMsg() {
		return msg;
	}
	public void setMsg(List<String> msg) {
		this.msg = msg;
	}
	
}
