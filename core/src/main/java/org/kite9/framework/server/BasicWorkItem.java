package org.kite9.framework.server;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("item")
public class BasicWorkItem implements WorkItem, Serializable {

	public BasicWorkItem(Object designItem, String name, String subjectId, String projectSecretKey, String userSecretKey) {
		this(designItem, name, subjectId, projectSecretKey, userSecretKey, "PNG,MAP");
	}
	
	public BasicWorkItem(Object designItem, String name, String subjectId, String projectSecretKey, String userSecretKey, String formats) {
		super();
		this.designItem = designItem;
		this.name = name;
		this.subjectId = subjectId;
		this.projectSecretKey = projectSecretKey;
		this.userSecretKey = userSecretKey;
		this.formats = formats;
	}

	private static final long serialVersionUID = -3337957680833965191L;

	private Object designItem;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String subjectId;
	
	@XStreamAsAttribute
	private String projectSecretKey;

	@XStreamAsAttribute
	private String userSecretKey;
	
	@XStreamAsAttribute
	private String formats;

	
	public Object getDesignItem() {
		return designItem;
	}

	public String getName() {
		return name;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public String getProjectSecretKey() {
		return projectSecretKey;
	}

	public String getUserSecretKey() {
		return userSecretKey;
	}

	public String getFormats() {
		return formats;
	}

}
