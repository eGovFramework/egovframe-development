package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Decision의 정보를 저장하는 VO
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
public class DecisionVo extends StepAndDecisionVo{

	/** Decision의 이름 */
	private String name = "";
	
	/** Decision의 Next On, Next To의 값이 있는 NextVO의 Array */
	private NextVo[] nextVo = new NextVo[0];
	
	/** * Decision의 End On */
	private String endOn = "";
	
	/** Decision의 End의 ExitCode */
	private String endExitCode = "";
	
	/** Decision의 Fail On */
	private String failOn = "";
	
	/** Decision의 Fail의 ExitCode */
	private String failExitCode = "";
	
	/** Decision의 Stop On */
	private String stopOn = "";
	
	/** Decision의 Stop의 ExitCode */
	private String stopRestart = "";
	
	/** DecisionVo의 생성자 */
	public DecisionVo() { 
		super(null);
	}
	
	/**
	 * DecisionVo의 생성자
	 * @param jobName
	 * @param decisionName
	 * @param nextVo
	 * @param endOn
	 * @param endExitCode
	 * @param failOn
	 * @param failExitCode
	 *
	 */
	public DecisionVo(String jobName, String decisionName, NextVo[] nextVo, String endOn, String endExitCode, String failOn,
			String failExitCode, String stopOn, String stopRestart) {
		super(jobName);
		this.name = decisionName;
		this.nextVo = nextVo;
		this.endOn = endOn;
		this.endExitCode = endExitCode;
		this.failOn = failOn;
		this.failExitCode = failExitCode;
		this.stopOn = stopOn;
		this.stopRestart = stopRestart;
	}

	/**
	 * name의 값을 가져온다
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * name의 값을 설정한다.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * nextVo의 값을 가져온다
	 *
	 * @return the nextVo
	 */
	public NextVo[] getNextVo() {
		return nextVo;
	}

	/**
	 * nextVo의 값을 설정한다.
	 *
	 * @param nextVo
	 */
	public void setNextVo(NextVo[] nextVo) {
		this.nextVo = nextVo;
	}

	/**
	 * endOn의 값을 가져온다
	 *
	 * @return the endOn
	 */
	public String getEndOn() {
		return endOn;
	}

	/**
	 * endOn의 값을 설정한다.
	 *
	 * @param endOn
	 */
	public void setEndOn(String endOn) {
		this.endOn = endOn;
	}

	/**
	 * endExitCode의 값을 가져온다
	 *
	 * @return the endExitCode
	 */
	public String getEndExitCode() {
		return endExitCode;
	}

	/**
	 * endExitCode의 값을 설정한다.
	 *
	 * @param endExitCode
	 */
	public void setEndExitCode(String endExitCode) {
		this.endExitCode = endExitCode;
	}

	/**
	 * failOn의 값을 가져온다
	 *
	 * @return the failOn
	 */
	public String getFailOn() {
		return failOn;
	}

	/**
	 * failOn의 값을 설정한다.
	 *
	 * @param failOn
	 */
	public void setFailOn(String failOn) {
		this.failOn = failOn;
	}

	/**
	 * failExitCode의 값을 가져온다
	 *
	 * @return the failExitCode
	 */
	public String getFailExitCode() {
		return failExitCode;
	}

	/**
	 * failExitCode의 값을 설정한다.
	 *
	 * @param failExitCode
	 */
	public void setFailExitCode(String failExitCode) {
		this.failExitCode = failExitCode;
	}

	/**
	 * stopOn의 값을 가져온다
	 *
	 * @return the stopOn
	 */
	public String getStopOn() {
		return stopOn;
	}

	/**
	 * stopOn의 값을 설정한다.
	 *
	 * @param stopOn
	 */
	public void setStopOn(String stopOn) {
		this.stopOn = stopOn;
	}

	/**
	 * stopExitCode의 값을 가져온다
	 *
	 * @return the stopExitCode
	 */
	public String getStopRestart() {
		return stopRestart;
	}

	/**
	 * stopExitCode의 값을 설정한다.
	 *
	 * @param stopExitCode
	 */
	public void setStopRestart(String stopRestart) {
		this.stopRestart = stopRestart;
	}
	
	/**
	 * Parameter로 넘어온 DecisionVo의 값들을 복사해 온다.
	 * 
	 * @param originalDecisionVo
	 */
	public void copyValues(DecisionVo originalDecisionVo){
		if(NullUtil.isNull(originalDecisionVo)){
			return;
		}
		
		setJobName(originalDecisionVo.getJobName());
		name = originalDecisionVo.getName();

		NextVo[] originalNextVos = originalDecisionVo.getNextVo();
		if(!NullUtil.isEmpty(originalNextVos)){
			NextVo[] newNextVo = new NextVo[originalNextVos.length];
			for(int i = 0; i< originalNextVos.length; i++){
				newNextVo[i] = originalNextVos[i].clone();
			}
			setNextVo(newNextVo);
		}else{
			setNextVo(new NextVo[0]);
		}
		
		endOn = originalDecisionVo.getEndOn();
		endExitCode = originalDecisionVo.getEndExitCode();
		failOn = originalDecisionVo.getFailOn();
		failExitCode = originalDecisionVo.getFailExitCode();
		stopOn = originalDecisionVo.getStopOn();
		stopRestart = originalDecisionVo.getStopRestart();
	}
	
	/** 현재 DecisionVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public DecisionVo clone(){
		DecisionVo clone = new DecisionVo();
		clone.copyValues(this);
		
		return clone;
	}
	
}
