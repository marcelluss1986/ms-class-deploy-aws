package com.mdss.mscatalog.dto;

import java.io.Serializable;

import com.mdss.mscatalog.entities.Role;

public class RoleDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String authority;
	
	public RoleDto() {
		
	}

	public RoleDto(Long id, String authority) {
		super();
		this.id = id;
		this.authority = authority;
	}
	
	public RoleDto(Role entity) {
		id = entity.getId();
		authority = entity.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
