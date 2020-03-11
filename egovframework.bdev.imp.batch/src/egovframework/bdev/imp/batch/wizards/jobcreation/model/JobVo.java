package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import egovframework.bdev.imp.confmngt.preferences.listeners.model.JobListenerInfo;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * Job의 정보를 저장하는 VO
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
public class JobVo {
	
	/** Job의 이름 */
	private String jobName = "";
	
	/**
	 * <pre>
	 * TRUE = Restartable true
	 * FALSE = Restartable false
	 * 
	 * TRUE가 기본값.
	 * </pre>
	 */
	private boolean restartable = true;
	
	/** Job의 Decision과 Step List */
	private StepAndDecisionVo[] stepAndDecisionVoList = new StepAndDecisionVo[0];
	
	/** Job의 Job Listener List */
	private JobListenerInfo[] jobListenerInfoList = new JobListenerInfo[0];
	
	/** Job내에 Step이 공유하는 Shared Value List */
	private SharedValueVo[] sharedValues = new SharedValueVo[0];

	/** JobVo의 생성자 */
	public JobVo() { }

	/**
	 * JobVo의 생성자	
	 * @param jobName
	 * @param restartable
	 * @param stepAndDecisionVoList
	 * @param jobListenerInfoList
	 * @param sharedValues
	 *
	 */
	public JobVo(String jobName, boolean restartable,
			StepAndDecisionVo[] stepAndDecisionVoList,
			JobListenerInfo[] jobListenerInfoList, SharedValueVo[] sharedValues) {
		super();
		this.jobName = jobName;
		this.restartable = restartable;
		this.stepAndDecisionVoList = stepAndDecisionVoList;
		this.jobListenerInfoList = jobListenerInfoList;
		this.sharedValues = sharedValues;
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
	 * restartable의 값을 가져온다
	 *
	 * @return the restartable
	 */
	public boolean isRestartable() {
		return restartable;
	}

	/**
	 * restartable의 값을 설정한다.
	 *
	 * @param restartable
	 */
	public void setRestartable(boolean restartable) {
		this.restartable = restartable;
	}

	/**
	 * stepAndDecisionVoList의 값을 가져온다
	 *
	 * @return the stepAndDecisionVoList
	 */
	public StepAndDecisionVo[] getStepAndDecisionVoList() {
		return stepAndDecisionVoList;
	}

	/**
	 * stepAndDecisionVoList의 값을 설정한다.
	 *
	 * @param stepAndDecisionVoList
	 */
	public void setStepAndDecisionVoList(StepAndDecisionVo[] stepAndDecisionVoList) {
		this.stepAndDecisionVoList = stepAndDecisionVoList;
	}

	/**
	 * jobListenerInfoList의 값을 가져온다
	 *
	 * @return the jobListenerInfoList
	 */
	public JobListenerInfo[] getJobListenerInfoList() {
		return jobListenerInfoList;
	}

	/**
	 * jobListenerInfoList의 값을 설정한다.
	 *
	 * @param jobListenerInfoList
	 */
	public void setJobListenerInfoList(JobListenerInfo[] jobListenerInfoList) {
		this.jobListenerInfoList = jobListenerInfoList;
	}

	/**
	 * sharedValues의 값을 가져온다
	 *
	 * @return the sharedValues
	 */
	public SharedValueVo[] getSharedValues() {
		return sharedValues;
	}

	/**
	 * sharedValues의 값을 설정한다.
	 *
	 * @param sharedValues
	 */
	public void setSharedValues(SharedValueVo[] sharedValues) {
		this.sharedValues = sharedValues;
	}
	
	/**
	 * Parameter로 넘어온 JobVo의 값들을 복사해 온다.
	 * 
	 * @param originalDecisionVo
	 */
	public void copyValues(JobVo originalJobVo){
		if(NullUtil.isNull(originalJobVo)){
			return;
		}
		
		setJobName(originalJobVo.getJobName());

		restartable = originalJobVo.isRestartable();
		
		SharedValueVo[] originalSharedValueVo = originalJobVo.getSharedValues();
		if(!NullUtil.isEmpty(originalSharedValueVo)){
			SharedValueVo[] newSVVo = new SharedValueVo[originalSharedValueVo.length];
			for(int i = 0; i < originalSharedValueVo.length; i++){
				newSVVo[i] = originalSharedValueVo[i].clone();
			}
			setSharedValues(newSVVo);
		}else{
			setSharedValues(new SharedValueVo[0]);
		}

		StepAndDecisionVo[] originalStepDecisionVo = originalJobVo.getStepAndDecisionVoList();
		if(!NullUtil.isEmpty(originalStepDecisionVo)){
			StepAndDecisionVo[] newStepDecisionVos = new StepAndDecisionVo[originalStepDecisionVo.length];
			for(int i = 0 ; i< originalStepDecisionVo.length; i++){
				newStepDecisionVos[i] = originalStepDecisionVo[i].clone();
			}
			setStepAndDecisionVoList(newStepDecisionVos);
		}else{
			setStepAndDecisionVoList(new StepAndDecisionVo[0]);
		}

		JobListenerInfo[] originalJobListenerInfoList = originalJobVo.getJobListenerInfoList();
		if(!NullUtil.isEmpty(originalJobListenerInfoList)){
			JobListenerInfo[] newJobListenerInfoList = new JobListenerInfo[originalJobListenerInfoList.length];
			for(int i =0; i < originalJobListenerInfoList.length; i++){
				newJobListenerInfoList[i] = originalJobListenerInfoList[i].clone();
			}
			setJobListenerInfoList(newJobListenerInfoList);
		}else{
			setJobListenerInfoList(new JobListenerInfo[0]);
		}
	}
	
	/** 현재 JobVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public JobVo clone(){
		JobVo clone = new JobVo();
		clone.copyValues(this);
		
		return clone;
	}

}
