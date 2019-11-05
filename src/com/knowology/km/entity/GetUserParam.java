package com.knowology.km.entity;

public class GetUserParam {
	public String workerid;
	public String name;
	public String role;
	public String sessionid=" ";
	public String industryOrganizationApplication;
	public String serviceroot;
	public String customer;
	
    
	public String getServiceroot() {
		return serviceroot;
	}

	public void setServiceroot(String serviceroot) {
		this.serviceroot = serviceroot;
	}

	public String getIndustryOrganizationApplication() {
		return industryOrganizationApplication;
	}

	public void setIndustryOrganizationApplication(
			String industryOrganizationApplication) {
		this.industryOrganizationApplication = industryOrganizationApplication;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getWorkerid() {
		return workerid;
	}

	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	
}

