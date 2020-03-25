package mm.aeon.com.vcs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mm.aeon.com.vcs.domain.CustAgreementList;
import mm.aeon.com.vcs.domain.CustomerInfoDto;
import mm.aeon.com.vcs.domain.ImportCustomerInfo;

@Mapper
public interface ImportCustomerInfoMapper {

	// void insert(ImportCustomerInfo info);
	//
	// ImportCustomerInfo getCustomerInfoById(int id);

	void insertUpdateCustomerInfo(ImportCustomerInfo info);

	void insertUpdateCustomerAgreeList(List<CustAgreementList> custList);

	void updateCustomerInfo(CustomerInfoDto customerInfoDto);

}
