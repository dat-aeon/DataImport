package mm.aeon.com.vcs.domain;

import java.io.Serializable;

public class NRCInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7765924828946170319L;

	private Integer stateId;

	private String townshipCode;

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getTownshipCode() {
		return townshipCode;
	}

	public void setTownshipCode(String townshipCode) {
		this.townshipCode = townshipCode;
	}

}
