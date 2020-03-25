/**************************************************************************
 * $Date : $
 * $Author : $
 * $Rev : $
 * Copyright (c) 2014 DIR-ACE Technology Ltd. All Rights Reserved.
 *************************************************************************/
package mm.aeon.com.vcs.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ImportCustomerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4554234826524225612L;

	private Integer age;
	private String companyName;
	private Date createdTime;
	private Integer customerId;
	private String customerNo;
	private Integer delFlag;
	private Date dob;
	private Integer gender;
	private String memberCardId;
	private Integer memberCardStatus;
	private Integer memberFlag;
	private String name;
	private String nrcNo;
	private String phoneNo;
	private Double salary;
	private Integer status;
	private String township;
	private Date updatedTime;

	private List<CustAgreementList> custAgreementListList;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getMemberCardId() {
		return memberCardId;
	}

	public void setMemberCardId(String memberCardId) {
		this.memberCardId = memberCardId;
	}

	public Integer getMemberFlag() {
		return memberFlag;
	}

	public void setMemberFlag(Integer memberFlag) {
		this.memberFlag = memberFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNrcNo() {
		return nrcNo;
	}

	public void setNrcNo(String nrcNo) {
		this.nrcNo = nrcNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getMemberCardStatus() {
		return memberCardStatus;
	}

	public void setMemberCardStatus(Integer memberCardStatus) {
		this.memberCardStatus = memberCardStatus;
	}

	public List<CustAgreementList> getCustAgreementListList() {
		return custAgreementListList;
	}

	public void setCustAgreementListList(List<CustAgreementList> custAgreementListList) {
		this.custAgreementListList = custAgreementListList;
	}

}
