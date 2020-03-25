package mm.aeon.com.vcs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mm.aeon.com.vcs.domain.NRCInfo;

@Mapper
public interface NRCInfoMapper {

	List<Integer> selectSataeDivisionInfo();

	void insertTownshipInfo(List<NRCInfo> townshipInfoList);

}
