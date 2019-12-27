package com.comparator.domain;

import com.comparator.annotation.Index;

public class Account {

	@Index(0)
	private String accNo;

	@Index(2)
	private String endpoint;

	@Index(3)
	private String resource;

	
	
	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "Account [accNo=" + accNo + ", endpoint=" + endpoint
				+ ", resource=" + resource + "]";
	}

}
