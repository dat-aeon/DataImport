package mm.aeon.com.vcs.domain;

import java.io.Serializable;
import java.util.Date;

public class CustomerInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3656879991157350219L;
	private String customerNo;
	private String phoneNo;
	private String updatedBy;
	private Date updatedTime;

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
