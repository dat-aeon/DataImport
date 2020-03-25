package mm.aeon.com.vcs.domain;

import java.io.Serializable;
import java.util.Date;

public class CustAgreementList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -122807271358951412L;

	private String agreementNo;
	private Integer qrShow;
	private Integer financialTerm;
	private Double financialAmt;
	private Integer financialStatus;
	private Integer custAgreementId;
	private Integer importCustomerId;
	private Date updatedTime;
	private Date createdTime;
	private String applicationNo;
	private Integer judgementResult;
	private Date judgementDate;
	private Date paymentDate;

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public Integer getQrShow() {
		return qrShow;
	}

	public void setQrShow(Integer qrShow) {
		this.qrShow = qrShow;
	}

	public Integer getFinancialTerm() {
		return financialTerm;
	}

	public void setFinancialTerm(Integer financialTerm) {
		this.financialTerm = financialTerm;
	}

	public Double getFinancialAmt() {
		return financialAmt;
	}

	public void setFinancialAmt(Double financialAmt) {
		this.financialAmt = financialAmt;
	}

	public Integer getFinancialStatus() {
		return financialStatus;
	}

	public void setFinancialStatus(Integer financialStatus) {
		this.financialStatus = financialStatus;
	}

	public Integer getCustAgreementId() {
		return custAgreementId;
	}

	public void setCustAgreementId(Integer custAgreementId) {
		this.custAgreementId = custAgreementId;
	}

	public Integer getImportCustomerId() {
		return importCustomerId;
	}

	public void setImportCustomerId(Integer importCustomerId) {
		this.importCustomerId = importCustomerId;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Integer getJudgementResult() {
		return judgementResult;
	}

	public void setJudgementResult(Integer judgementResult) {
		this.judgementResult = judgementResult;
	}

	public Date getJudgementDate() {
		return judgementDate;
	}

	public void setJudgementDate(Date judgementDate) {
		this.judgementDate = judgementDate;
	}

}
