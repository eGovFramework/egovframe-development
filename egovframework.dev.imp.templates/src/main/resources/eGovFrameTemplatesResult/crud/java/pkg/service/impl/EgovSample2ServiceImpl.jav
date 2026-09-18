package pkg.service.impl;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pkg.service.Sample2Service;
import pkg.service.Sample2VO;
import pkg.service.impl.Sample2Mapper;
import jakarta.annotation.Resource;

/**
 * @Class Name : Sample2ServiceImpl.java
 * @Description : Sample2 Business Implement class
 * @Modification Information
 *
 * @author 홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see
 *
 *  Copyright (C)  All right reserved.
 */

@Service("sample2Service")
public class Sample2ServiceImpl extends EgovAbstractServiceImpl implements Sample2Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sample2ServiceImpl.class);

	/** Sample2Mapper */
	@Resource(name = "sample2Mapper")
	private Sample2Mapper sample2Mapper;

	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;

	/**
	 * SAMPLE2을 등록한다.
	 * @param vo - 등록할 정보가 담긴 Sample2VO
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override
	public void insertSample2(Sample2VO vo) throws Exception {
		LOGGER.debug(vo.toString());

		/** ID Generation Service */
		//TODO 해당 테이블 속성에 따라 ID 제너레이션 서비스 사용
		String id = egovIdGnrService.getNextStringId();
		vo.setId(id);
		LOGGER.debug(vo.toString());

		sample2Mapper.insertSample2(vo);
	}

	/**
	 * SAMPLE2을 수정한다.
	 * @param vo - 수정할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateSample2(Sample2VO vo) throws Exception {
		sample2Mapper.updateSample2(vo);
	}

	/**
	 * SAMPLE2을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteSample2(Sample2VO vo) throws Exception {
		sample2Mapper.deleteSample2(vo);
	}

	/**
	 * SAMPLE2을 조회한다.
	 * @param vo - 조회할 정보가 담긴 Sample2VO
	 * @return 조회한 SAMPLE2
	 * @exception Exception
	 */
	@Override
	public Sample2VO selectSample2(Sample2VO vo) throws Exception {
		Sample2VO resultVO = sample2Mapper.selectSample2(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}

	/**
	 * SAMPLE2 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectSample2List(Sample2VO vo) throws Exception {
		return sample2Mapper.selectSample2List(vo);
	}

	/**
	 * SAMPLE2 총 갯수를 조회한다.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 총 갯수
	 * @exception
	 */
	@Override
	public int selectSample2ListTotCnt(Sample2VO vo) {
		return sample2Mapper.selectSample2ListTotCnt(vo);
	}

}
