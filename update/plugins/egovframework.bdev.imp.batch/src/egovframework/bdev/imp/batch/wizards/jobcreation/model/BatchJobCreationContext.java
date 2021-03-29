package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import org.eclipse.core.internal.resources.Project;

import egovframework.bdev.imp.batch.wizards.com.BatchJobContext;

/**
 * Batch Job 생성 Wizard의 Context
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */

@SuppressWarnings("restriction")
public class BatchJobCreationContext extends BatchJobContext{

	/** Wizard의 JobList에 있는 JobVo들을 저장 */
	private JobVo[] jobVoList = null;
	
	/**
	 * jobVoList의 값을 가져온다
	 *
	 * @return the jobVoList
	 */
	public JobVo[] getJobVoList() {
		return jobVoList;
	}

	/**
	 * jobVoList의 값을 설정한다.
	 *
	 * @param jobVoList
	 */
	public void setJobVoList(JobVo[] jobVoList) {
		this.jobVoList = jobVoList;
	}
	
	/**
	 * BatchJobCreationContext의 생성자
	 * 
	 * @param jobVoList
	 * 
	 */
	public BatchJobCreationContext() {
	}

	/**
	 * BatchJobCreationContext의 생성자
	 * 
	 * @param project
	 * @param fileName
	 * @param filePath
	 * @param jobVoList
	 *
	 */
	public BatchJobCreationContext(Project project, String fileName, String filePath, JobVo[] jobVoList) {
		super(project, fileName, filePath);
		this.jobVoList = jobVoList;
	}
	
	/**
	 * Context를 초기화 한다.
	 * 
	 */
	public void clearValues(){
		jobVoList = null;
	}
	
	
}
