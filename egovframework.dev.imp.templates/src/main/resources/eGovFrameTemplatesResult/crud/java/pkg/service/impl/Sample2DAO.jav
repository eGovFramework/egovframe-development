package pkg.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import pkg.service.Sample2VO;
import pkg.service.Sample2DefaultVO;

/**
 * @Class Name : Sample2DAO.java
 * @Description : Sample2 DAO Class
 * @Modification Information
 *
 * @author 홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("sample2DAO")
public class Sample2DAO extends EgovAbstractMapper {

	/**
	 * SAMPLE2을 등록한다.
	 * @param vo - 등록할 정보가 담긴 Sample2VO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public void insertSample2(Sample2VO vo) throws Exception {
        insert("sample2DAO.insertSample2", vo);
    }

    /**
	 * SAMPLE2을 수정한다.
	 * @param vo - 수정할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
    public void updateSample2(Sample2VO vo) throws Exception {
        update("sample2DAO.updateSample2", vo);
    }

    /**
	 * SAMPLE2을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 Sample2VO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteSample2(Sample2VO vo) throws Exception {
        delete("sample2DAO.deleteSample2", vo);
    }

    /**
	 * SAMPLE2을 조회한다.
	 * @param vo - 조회할 정보가 담긴 Sample2VO
	 * @return 조회한 SAMPLE2
	 * @exception Exception
	 */
    public Sample2VO selectSample2(Sample2VO vo) throws Exception {
        return (Sample2VO) selectOne("sample2DAO.selectSample2", vo);
    }

    /**
	 * SAMPLE2 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 List<Sample2DefaultVO>
	 * @return SAMPLE2 목록
	 * @exception Exception
	 */
    public List<Sample2DefaultVO> selectSample2List(Sample2DefaultVO searchVO) throws Exception {
        return selectList("sample2DAO.selectSample2List", searchVO);
    }

    /**
	 * SAMPLE2 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return SAMPLE2 총 갯수
	 * @exception
	 */
    public int selectSample2ListTotCnt(Sample2DefaultVO searchVO) {
        return (Integer)selectOne("sample2DAO.selectSample2ListTotCnt", searchVO);
    }

}