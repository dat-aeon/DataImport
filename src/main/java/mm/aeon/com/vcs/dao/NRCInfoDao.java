package mm.aeon.com.vcs.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mm.aeon.com.vcs.domain.NRCInfo;

@Component
public class NRCInfoDao {

	private final SqlSession sqlSession;

	public NRCInfoDao(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<Integer> getStateIdList() {
		return sqlSession.selectList("selectSataeDivisionInfo");
	}

	@Transactional
	public void insertUpdate(List<NRCInfo> townshipInfoList) {
		sqlSession.insert("insertTownshipInfo", townshipInfoList);
	}

}
