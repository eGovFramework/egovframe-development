package pkg.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import pkg.service.Sample2VO;

/**
 * Sample2에 관한 데이터처리 매퍼 클래스
 *
 * @author  홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *          수정일          수정자           수정내용
 *  ----------------    ------------    ---------------------------
 *   실행환경 개발팀        홍길동          최초 생성
 *
 * </pre>
 */
@EgovMapper("sample2Mapper")
public interface Sample2Mapper {

	/**
	 * SAMPLE2을 등록한다.
	 * @param vo - 등록할 정보가 담긴 Sample2VO
	 * @return 등록 결과
	 * @exception Exception
	 */
	void insertSample2(Sample2VO vo) throws Exception;

	/**
	 * SAMPLE2을 수정한다.
	 * @param vo - 수정할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
	void updateSample2(Sample2VO vo) throws Exception;

	/**
	 * SAMPLE2을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 Sample2VO
	 * @return void형
	 * @exception Exception
	 */
	void deleteSample2(Sample2VO vo) throws Exception;

	/**
	 * SAMPLE2을 조회한다.
	 * @param vo - 조회할 정보가 담긴 Sample2VO
	 * @return 조회한 SAMPLE2
	 * @exception Exception
	 */
	Sample2VO selectSample2(Sample2VO vo) throws Exception;

	/**
	 * SAMPLE2 목록을 조회한다.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 목록
	 * @exception Exception
	 */
	List<?> selectSample2List(Sample2VO vo) throws Exception;

	/**
	 * SAMPLE2 총 갯수를 조회한다.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return SAMPLE2 총 갯수
	 * @exception
	 */
	int selectSample2ListTotCnt(Sample2VO vo);

}
