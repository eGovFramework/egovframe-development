package pkg.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.cmmn.SampleDefaultVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import pkg.service.Sample2VO;

/**
 * @Class Name : Sample2DAO.java
 * @Description : Sample2 DAO Class
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2009.02.01    홍길동          최초 생성
 *
 *  @author 실행환경 개발팀 홍길동
 *  @since 2009.02.01
 *  @version 1.0
 *  @see
 *  
 *  Copyright (C) 2009 by MOSPA  All right reserved.
 */

@Repository("sample2DAO")
public class Sample2DAO extends EgovAbstractDAO {

	/**
	 * SAMPLE2을 등록한다.
	 * @param vo - 등록할 정보가 담긴 Sample2VO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertSample2(Sample2VO vo) throws Exception {
        return (String)insert("sample2DAO.insertSample2_S", vo);
    }

    /**
	 * SAMPLE2을 수정한다.
	 * @param vo - 수정할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
    public void updateSample2(Sample2VO vo) throws Exception {
        update("sample2DAO.updateSample2_S", vo);
    }

    /**
	 * SAMPLE2을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 Sample2VO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteSample2(Sample2VO vo) throws Exception {
        delete("sample2DAO.deleteSample2_S", vo);
    }

    /**
	 * SAMPLE2을 조회한다.
	 * @param vo - 조회할 정보가 담긴 Sample2VO
	 * @return 조회한 SAMPLE2
	 * @exception Exception
	 */
    public Sample2VO selectSample2(Sample2VO vo) throws Exception {
        return (Sample2VO) selectByPk("sample2DAO.selectSample2_S", vo);
    }

    /**
	 * SAMPLE2 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return SAMPLE2 목록
	 * @exception Exception
	 */
    public List selectSample2List(SampleDefaultVO searchVO) throws Exception {
        return list("sample2DAO.selectSample2List_D", searchVO);
    }

    /**
	 * SAMPLE2 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return SAMPLE2 총 갯수
	 * @exception
	 */
    public int selectSample2ListTotCnt(SampleDefaultVO searchVO) {
        return (Integer)getSqlMapClientTemplate().queryForObject("sample2DAO.selectSample2ListTotCnt_S", searchVO);
    }

}
