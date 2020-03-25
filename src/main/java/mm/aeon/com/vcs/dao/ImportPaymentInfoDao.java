package mm.aeon.com.vcs.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mm.aeon.com.vcs.domain.PaymentInfo;

@Component
public class ImportPaymentInfoDao {

	private final SqlSession sqlSession;

	public ImportPaymentInfoDao(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Transactional
	public void update(PaymentInfo info) {

		if (info.getAgreementCd() != null && info.getPaymentDate() != null) {

			sqlSession.insert("updatePaymentInfo", info);
		}

	}

	@Transactional
	public Integer checkExistCount(PaymentInfo paymentInfo) {

		Integer existCount = sqlSession.selectOne("checkExistCount", paymentInfo);
		return existCount;

	}

}
