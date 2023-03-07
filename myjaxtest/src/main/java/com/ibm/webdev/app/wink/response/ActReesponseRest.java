package com.ibm.webdev.app.wink.response;

import java.io.Serializable;
import java.net.URI;

import org.json.JSONObject;

public class ActReesponseRest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action;
	private JSONObject content;
	private URI link;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
	public URI getLink() {
		return link;
	}
	public void setLink(URI link) {
		this.link = link;
	}
}
