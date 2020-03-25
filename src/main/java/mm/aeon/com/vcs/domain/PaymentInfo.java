package mm.aeon.com.vcs.domain;

import java.io.Serializable;
import java.util.Date;

public class PaymentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7719607261179985462L;

	private String agreementCd;

	private Date paymentDate;

	private Date updatedTime;

	private String updatedBy;

	public String getAgreementCd() {
		return agreementCd;
	}

	public void setAgreementCd(String agreementCd) {
		this.agreementCd = agreementCd;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
