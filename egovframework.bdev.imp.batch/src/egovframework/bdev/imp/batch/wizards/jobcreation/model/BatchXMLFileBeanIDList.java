package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import java.util.ArrayList;

import egovframework.dev.imp.core.utils.NullUtil;

/**
 * BatchJob BeanID List들의 List
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.09.04
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class BatchXMLFileBeanIDList {

	/** BathJob BeanID List의 List */
	private ArrayList<BatchJobBeanIDList> jobList = null;
	
	/** Job RW의 Bean ID */
	private BatchPageBeanIDList jobRWBeanIDList = null;

	/** 추가적으로 사용할 수 없는 Bean ID */
	final private String[] defaultInvalidBeanIDs = new String[] { "async",
			"datasource", "transactionManager", "jdbcTemplate",
			"egovDecider"};

	/** BatchJobListBeanIDList의 생성자 */
	public BatchXMLFileBeanIDList() {
		jobList = new ArrayList<BatchJobBeanIDList>();
		jobRWBeanIDList = new BatchPageBeanIDList();
	}

	/**
	 * BatchJobListBeanIDList의 생성자
	 * 
	 * @param jobBeanList
	 * @return
	 */
	private boolean addBatchJobBeanIDList(BatchJobBeanIDList jobBeanList) {

		String jobID = jobBeanList.getJobID();

		if (jobList.size() > 0) {
			for (BatchJobBeanIDList jobBeans : jobList) {
				if (jobBeans.getJobID().equals(jobID)) {
					return false;
				}
			}
		}

		return jobList.add(jobBeanList);
	}

	/**
	 * ID에 해당하는 Job 전체 Bean ID list를 제거한다.
	 * 
	 * @param jobID
	 * @return
	 */
	public boolean removeJobBeanIDList(String jobID) {
		int length = jobList.size();

		for (int i = 0; i < length; i++) {
			if (jobID.equals(jobList.get(i).getJobID())) {
				jobList.remove(i);
				return true;
			}
		}

		return false;
	}

	/**
	 * ID에 해당하는 Job 전체 Bean ID list를 가져온다.
	 * 
	 * @param jobID
	 * @return
	 */
	public BatchJobBeanIDList getJobBeanIDList(String jobID) {
		if(!NullUtil.isEmpty(jobID)){
			if (jobList.size() > 0) {
				for (BatchJobBeanIDList jobBeans : jobList) {
					if (jobBeans.getJobID().equals(jobID)) {
						return jobBeans;
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * Job ID를 제외한 나머지 BeanID를 변경한다.
	 * 
	 * @param jobBeanList
	 * @return
	 */
	public boolean updateJobBeanIDList(BatchJobBeanIDList jobBeanList) {
		return updateJobBeanIDList(jobBeanList.getJobID(), jobBeanList);
	}

	/**
	 * JobVo 변경시 해당 Bean ID list도 변경한다.
	 * 
	 * @param jobID
	 * @param jobBeanList
	 * @return
	 */
	public boolean updateJobBeanIDList(String jobID,
			BatchJobBeanIDList jobBeanList) {
		if (removeJobBeanIDList(jobID)) {
			addBatchJobBeanIDList(jobBeanList);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Job RW의 Bean ID를 포함한 전체 Bean ID목록에 해당 Bean ID가 존재하는지 확인
	 * 
	 * @param beanID
	 * @return
	 */
	public boolean isBeanIDExistIncludeJobRWBeanList(String beanID) {
		for (int i = 0; i < defaultInvalidBeanIDs.length; i++) {
			if (beanID.equals(defaultInvalidBeanIDs[i])) {
				return true;
			}
		}

		for (int i = 0; i < jobList.size(); i++) {
			BatchJobBeanIDList jobBeanIDs = jobList.get(i);
			if (jobBeanIDs.isBeanIDExist(beanID)) {
				return true;
			}
		}
		
		if(jobRWBeanIDList.isBeanIDExist(beanID)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Job RW의 Bean ID를 제외한 Bean ID목록에  해당 Bean ID가 존재하는지 확인
	 * 
	 * @param beanID
	 * @return
	 */
	public boolean isBeanIDExistExceptJobRWBeanList(String beanID) {
		for (int i = 0; i < defaultInvalidBeanIDs.length; i++) {
			if (beanID.equals(defaultInvalidBeanIDs[i])) {
				return true;
			}
		}

		for (int i = 0; i < jobList.size(); i++) {
			BatchJobBeanIDList jobBeanIDs = jobList.get(i);
			if (jobBeanIDs.isBeanIDExist(beanID)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * jobList의 값을 설정한다.
	 * 
	 * @param jobList
	 */
	public void setBatchJobBeanIDListArray(ArrayList<BatchJobBeanIDList> jobList) {
		this.jobList = jobList;
	}
	
	/**
	 * Job RW의 Bean ID List를 설정한다.
	 * 
	 * @param jobRWBeanIDList
	 */
	public void setJobRWBeanIDList(BatchPageBeanIDList jobRWBeanIDList){
		this.jobRWBeanIDList = jobRWBeanIDList;
	}

	/** 현재 BatchJobListBeanIDList와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public BatchXMLFileBeanIDList clone() {
		BatchXMLFileBeanIDList clone = new BatchXMLFileBeanIDList();

		ArrayList<BatchJobBeanIDList> cloneJobList = new ArrayList<BatchJobBeanIDList>();
		for (int i = 0; i < jobList.size(); i++) {
			cloneJobList.add(jobList.get(i).clone());
		}

		clone.setBatchJobBeanIDListArray(cloneJobList);
		clone.setJobRWBeanIDList(jobRWBeanIDList.clone());

		return clone;
	}
	
	/**
	 * JobVo의 BeanID를 BatchJobBeanIDList로 생성하여 저장한다.
	 * 
	 * @param jobVo
	 * @return
	 */
	public boolean addNewJobVoBeanIDList(JobVo jobVo){
		BatchJobBeanIDList jobBeanIDList = getJobBeanIDList(jobVo.getJobName());
		
		if(NullUtil.isNull(jobBeanIDList)){
			BatchJobBeanIDList newJobBeanIDList = new BatchJobBeanIDList(jobVo);
			addBatchJobBeanIDList(newJobBeanIDList);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * JobVO에서 변경된 정보를 수정한다.
	 * 
	 * @param preJobName
	 * @param newJobVo
	 * @return
	 */
	public boolean updateJobVoBeanIDList(String preJobName, JobVo newJobVo){
		BatchJobBeanIDList jobBeanIDList = getJobBeanIDList(preJobName);
		
		if(!NullUtil.isNull(jobBeanIDList)){
			jobBeanIDList.updateJobBeanIDs(newJobVo);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 해당 JobVo에 새로운 StepVo의 Bean ID를 추가한다.
	 * 
	 * @param jobVo
	 * @param newStepVo
	 */
	public void addStepVoBeanIDList(JobVo jobVo, StepVo newStepVo){
		BatchJobBeanIDList jobBeanList = getJobBeanIDList(jobVo.getJobName());
		
		if(!NullUtil.isNull(jobBeanList)){
			jobBeanList.addStepBeanIDs(jobVo, newStepVo);
			jobRWBeanIDList.addJobRWBeanID(newStepVo);
		}
	}
	
	/**
	 * 해당 JobVo에 있는 StepVo의 Bean ID를 갱신한다.
	 * 
	 * @param preStepVo
	 * @param jobVo
	 * @param newStepVo
	 */
	public void updateStepVoBeanIDList(StepVo preStepVo, JobVo jobVo, StepVo newStepVo){
		BatchJobBeanIDList jobBeanList = getJobBeanIDList(jobVo.getJobName());

		if(!NullUtil.isNull(jobBeanList)){
			jobBeanList.updateStepBeanIDs(preStepVo.getName(), jobVo, newStepVo);
			jobRWBeanIDList.updateJobRWBeanID(preStepVo, newStepVo);
		}
	}
	
	/**
	 * 해당 JobVo에 새로운 StepVo의 Bean ID를 추가한다.
	 * 
	 * @param jobVo
	 * @param newDecisionVo
	 */
	public void addDecisionVoBeanIDList(JobVo jobVo, DecisionVo newDecisionVo){
		BatchJobBeanIDList jobBeanList = getJobBeanIDList(jobVo.getJobName());
		
		if(!NullUtil.isNull(jobBeanList)){
			jobBeanList.addDecisionBeanIDs(newDecisionVo);
		}
	}
	
	/**
	 * 해당 JobVo에 있는 DecisionVo의 Bean ID를 갱신한다.
	 * 
	 * @param preDecisionVo
	 * @param jobVo
	 * @param newDecisionVo
	 */
	public void updateDecisionVoBeanIDList(DecisionVo preDecisionVo, JobVo jobVo, DecisionVo newDecisionVo){
		BatchJobBeanIDList jobBeanList = getJobBeanIDList(jobVo.getJobName());
		
		if(!NullUtil.isNull(jobBeanList)){
			jobBeanList.updateDecisionBeanIDs(preDecisionVo.getName(), newDecisionVo);
		}
	}
}
