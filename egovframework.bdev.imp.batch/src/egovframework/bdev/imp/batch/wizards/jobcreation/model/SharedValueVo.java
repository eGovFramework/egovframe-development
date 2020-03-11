package egovframework.bdev.imp.batch.wizards.jobcreation.model;
/**
 * SharedValue의 정보를 저장하는 VO
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
public class SharedValueVo{
	/** Shared Value의 Key값 */
	private String key = null;
	
	/** Shared Value를 사용하는 Step의 ID */
	private String stepID = null;
	
	/** SharedValueVo의 생성자 */
	public SharedValueVo() {
	}
	
	/**
	 * SharedValueVo의 생성자
	 * @param key
	 *
	 */
	public SharedValueVo(String key, String stepID) {
		this.key = key;
		this.stepID = stepID;
	}

	/**
	 * key의 값을 가져온다
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * key의 값을 설정한다.
	 *
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * stepId의 값을 가져온다
	 *
	 * @return the stepId
	 */
	public String getStepId() {
		return stepID;
	}

	/**
	 * stepId의 값을 설정한다.
	 *
	 * @param stepId
	 */
	public void setStepId(String stepId) {
		this.stepID = stepId;
	}
	
	/** 현재 SharedValueVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public SharedValueVo clone(){
		SharedValueVo clone = new SharedValueVo();
		clone.setKey(key);
		clone.setStepId(stepID);
		
		return clone;
	}

}