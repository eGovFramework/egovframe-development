package pkg.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.cmmn.SampleDefaultVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import pkg.service.EgovSample2Service;
import pkg.service.Sample2VO;


/**
 * @Class Name : EgovSample2ServiceImpl.java
 * @Description : Sample2 Business Implement class
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

@Service("sample2Service")
public class EgovSample2ServiceImpl extends AbstractServiceImpl implements
        EgovSample2Service {

    @Resource(name="sample2DAO")
    private Sample2DAO sample2DAO;
    
    /** ID Generation */
    @Resource(name="egovSample2IdGnrService")    
    private EgovIdGnrService egovIdGnrService;

	/**
	 * SAMPLE2을 등록한다.
	 * @param vo - 등록할 정보가 담긴 Sample2VO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertSample2(Sample2VO vo) throws Exception {
    	log.debug(vo.toString());
    	
    	/** ID Generation Service */
    	String id = egovIdGnrService.getNextStringId();
    	vo.setId(id);
    	log.debug(vo.toString());
    	
    	sample2DAO.insertSample2(vo);    	
        return id;
    }

    /**
	 * SAMPLE2을 수정한다.
	 * @param vo - 수정할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
    public void updateSample2(Sample2VO vo) throws Exception {
        sample2DAO.updateSample2(vo);
    }

    /**
	 * SAMPLE2을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 Sample2VO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteSample2(Sample2VO vo) throws Exception {
        sample2DAO.deleteSample2(vo);
    }

    /**
	 * SAMPLE2을 조회한다.
	 * @param vo - 조회할 정보가 담긴 Sample2VO
	 * @return 조회한 SAMPLE2
	 * @exception Exception
	 */
    public Sample2VO selectSample2(Sample2VO vo) throws Exception {
        Sample2VO resultVO = sample2DAO.selectSample2(vo);
        if (resultVO == null)
            throw processException("info.nodata.msg");
        return resultVO;
    }

    /**
	 * SAMPLE2 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 목록
	 * @exception Exception
	 */
    public List selectSample2List(SampleDefaultVO searchVO) throws Exception {
        return sample2DAO.selectSample2List(searchVO);
    }

    /**
	 * SAMPLE2 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 총 갯수
	 * @exception
	 */
    public int selectSample2ListTotCnt(SampleDefaultVO searchVO) {
		return sample2DAO.selectSample2ListTotCnt(searchVO);
	}
    
}
