package com.guesswhat.server.persistence.jpa.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ImageHolder {

	@Id
	private Long id;
	private Long xxhdpiImageId;
	private Long xhdpiImageId;
	private Long hdpiImageId;
	private Long mdpiImageId;
	private Long ldpiImageId;

	public ImageHolder() {
		
	}

	public boolean isFull() {
		if (xxhdpiImageId != null && xhdpiImageId != null && hdpiImageId != null && mdpiImageId != null && ldpiImageId != null) {
			return true;
		}
		
		return false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getXxhdpiImageId() {
		return xxhdpiImageId;
	}

	public void setXxhdpiImageId(Long xxhdpiImageId) {
		this.xxhdpiImageId = xxhdpiImageId;
	}

	public Long getXhdpiImageId() {
		return xhdpiImageId;
	}

	public void setXhdpiImageId(Long xhdpiImageId) {
		this.xhdpiImageId = xhdpiImageId;
	}

	public Long getHdpiImageId() {
		return hdpiImageId;
	}

	public void setHdpiImageId(Long hdpiImageId) {
		this.hdpiImageId = hdpiImageId;
	}

	public Long getMdpiImageId() {
		return mdpiImageId;
	}

	public void setMdpiImageId(Long mdpiImageId) {
		this.mdpiImageId = mdpiImageId;
	}

	public Long getLdpiImageId() {
		return ldpiImageId;
	}

	public void setLdpiImageId(Long ldpiImageId) {
		this.ldpiImageId = ldpiImageId;
	}	
}
