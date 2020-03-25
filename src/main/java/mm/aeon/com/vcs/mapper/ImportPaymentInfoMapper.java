package mm.aeon.com.vcs.mapper;

import org.apache.ibatis.annotations.Mapper;

import mm.aeon.com.vcs.domain.PaymentInfo;

@Mapper
public interface ImportPaymentInfoMapper {

	void updatePaymentInfo(PaymentInfo paymentInfo);

	Integer checkExistCount(PaymentInfo paymentInfo);

}
