package mm.aeon.com.vcs.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mm.aeon.com.vcs.domain.CustAgreementList;
import mm.aeon.com.vcs.domain.CustomerInfoDto;
import mm.aeon.com.vcs.domain.ImportCustomerInfo;

@Component
public class ImportCustomerInfoDao {

	private final SqlSession sqlSession;

	public ImportCustomerInfoDao(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Transactional
	public void insertUpdate(ImportCustomerInfo info) {

		sqlSession.insert("insertUpdateCustomerInfo", info);

		if (info.getCustAgreementListList() != null && info.getCustAgreementListList().size() > 0) {

			for (CustAgreementList custAgree : info.getCustAgreementListList()) {
				custAgree.setImportCustomerId(info.getCustomerId());
			}

			sqlSession.insert("insertUpdateCustomerAgreeList", info.getCustAgreementListList());
		}
		CustomerInfoDto customerInfoDto = new CustomerInfoDto();
		customerInfoDto.setCustomerNo(info.getCustomerNo());
		customerInfoDto.setPhoneNo(info.getPhoneNo());
		customerInfoDto.setUpdatedBy("Updated by import jar");
		sqlSession.insert("updateCustomerInfo", customerInfoDto);

	}

}
