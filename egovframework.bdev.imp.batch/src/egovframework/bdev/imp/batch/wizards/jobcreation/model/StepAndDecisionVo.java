package egovframework.bdev.imp.batch.wizards.jobcreation.model;
/**
 * DecisionVO와 StepVo의 부모 VO Class
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

public abstract class StepAndDecisionVo {
	
	/** Step 또는 Decision이 속하는 Job의 이름을 저장 */
	private String jobName = null;
	
	/**
	 * StepAndDecisionVo의 생성자
	 * @param jobName
	 *
	 */
	public StepAndDecisionVo(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * jobName의 값을 가져온다
	 *
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * jobName의 값을 설정한다.
	 *
	 * @param jobName
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	/**
	 * Step 또는 Decision의 이름을 가져온다.
	 * @return
	 */
	public abstract String getName();
	
	/** 현재 StepAndDecisionVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public StepAndDecisionVo clone(){
		if(this instanceof StepVo){
			return ((StepVo)this).clone();
		}else if(this instanceof DecisionVo){
			return ((DecisionVo)this).clone();
		}else{
			return null;
		}
	}
	
}
