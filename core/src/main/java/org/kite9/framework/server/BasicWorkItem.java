package org.kite9.framework.server;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("item")
public class BasicWorkItem implements WorkItem, Serializable {

	public BasicWorkItem(Object designItem, String name, String subjectId, int projectId, String secretKey) {
		super();
		this.designItem = designItem;
		this.name = name;
		this.subjectId = subjectId;
		this.projectId = projectId;
		this.secretKey = secretKey;
	}

	private static final long serialVersionUID = -3337957680833965191L;

	private Object designItem;

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String subjectId;
	
	@XStreamAsAttribute
	private int projectId;
	
	@XStreamAsAttribute
	private String secretKey;

	public Object getDesignItem() {
		return designItem;
	}

	public String getName() {
		return name;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public int getProjectId() {
		return projectId;
	}

	public String getSecretKey() {
		return secretKey;
	}

}
