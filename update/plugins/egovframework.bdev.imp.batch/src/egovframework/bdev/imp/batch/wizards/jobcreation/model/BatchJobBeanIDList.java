package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import java.util.ArrayList;

import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Batch Job 의 Bean ID List
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.09.04
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
public class BatchJobBeanIDList {
	
	/** Job의 ID*/
	private String jobID = null;
	
	/** Job Info Page의 Bean ID와 SharedValueVo의 Bean ID를 저장 */
	private BatchPageBeanIDList jobPage = null;
	
	/** JobVo에 속하는 StepVo, DecisionVo의 Bean ID 목록 */
	private ArrayList<BatchPageBeanIDList> stepDecisionPages = null;
	
	
	/** BatchJobBeanIDList 생성자 */
	public BatchJobBeanIDList() {
		jobPage = new BatchPageBeanIDList();
		stepDecisionPages = new ArrayList<BatchPageBeanIDList>();
	}
	
	/**
	 * BatchJobBeanIDList 생성자
	 * 
	 * @param jobVo
	 */
	public BatchJobBeanIDList(JobVo jobVo){
		this();
		setJobBeanIDs(jobVo);
	}
	
	/**
	 * Job Info Page내의 Bean ID 입력
	 * 
	 * @param jobVo
	 */
	public void setJobBeanIDs(JobVo jobVo){
		
		if(NullUtil.isNull(jobVo)){
			return;
		}
		
		jobID = jobVo.getJobName();
		jobPage.setPageID(jobID);
		jobPage.setJobInfoPageBeanIDs(jobVo);
	}
	
	/**
	 * Job Info 화면 변경시 Job Info 화면내의 Bean ID 목록 변경
	 * 
	 * @param jobVo
	 */
	public void updateJobBeanIDs(JobVo jobVo){
		jobPage = new BatchPageBeanIDList();
		setJobBeanIDs(jobVo);
	}
	
	/**
	 * <pre>
	 * Step Info 화면의 BeanID 저장
	 * SharedvalueVo 저장을 위해 JobVo도 필요.
	 * </pre>
	 * @param jobVo
	 * @param stepVo
	 * @return
	 */
	public boolean addStepBeanIDs(JobVo jobVo, StepVo stepVo){
		
		if(NullUtil.isNull(stepVo) && NullUtil.isNull(jobVo)){
			return false;
		}
		
		BatchPageBeanIDList stepBeanIDs = new BatchPageBeanIDList();
		stepBeanIDs.setPageID(stepVo.getName());
		stepBeanIDs.setStepInfoPageBeanIDs(stepVo);
		
		//SharedValue 때문에 jobVo의 BeanIDList도 갱신
		updateJobBeanIDs(jobVo);
		
		return stepDecisionPages.add(stepBeanIDs);
	}
	
	/**
	 * Step 변경시 Bean ID 변경
	 * 
	 * @param preStepID
	 * @param jobVo
	 * @param stepVo
	 * @return
	 */
	public boolean updateStepBeanIDs(String preStepID, JobVo jobVo, StepVo stepVo){
		if(removeStepDecision(preStepID)){
			return addStepBeanIDs(jobVo, stepVo);
		}else{
			return false;
		}
	}
	
	/**
	 * Decision Info 화면의 BeanID 저장
	 * 
	 * @param decisionVo
	 * @return
	 */
	public boolean addDecisionBeanIDs(DecisionVo decisionVo){
		if(NullUtil.isNull(decisionVo)){
			return false;
		}
		
		BatchPageBeanIDList decisionBeanIDs = new BatchPageBeanIDList();
		decisionBeanIDs.setPageID(decisionVo.getName());
		decisionBeanIDs.setDecisionInfoPageBeanIDs(decisionVo);
		
		return stepDecisionPages.add(decisionBeanIDs);
	}
	
	/**
	 * Decision 변경시 Bean ID 변경
	 * 
	 * @param preDecisionID
	 * @param decisionVo
	 * @return
	 */
	public boolean updateDecisionBeanIDs(String preDecisionID, DecisionVo decisionVo){
		if(removeStepDecision(preDecisionID)){
			return addDecisionBeanIDs(decisionVo);
		}else{
			return false;
		}
	}
	
	/**
	 * Job ID를 가져온다.
	 * 
	 * @return
	 */
	public String getJobID(){
		return jobID;
	}
	
	/**
	 * id에 해당하는 Step, Decision Vo의 Bean ID list를 제거한다.
	 * 
	 * @param id
	 * @return
	 */
	public boolean removeStepDecision(String id){
		BatchPageBeanIDList searchBeanID = getStepDecisionBeanIDList(id);
		if(!NullUtil.isNull(searchBeanID)){
			return stepDecisionPages.remove(searchBeanID);
		}else{
			return false;
		}
	}
	
	/**
	 * id에 해당하는 Step, Decision Vo의 Bean ID list를 가져온다.
	 * 
	 * @param id
	 * @return
	 */
	public BatchPageBeanIDList getStepDecisionBeanIDList(String id){
		if(!NullUtil.isEmpty(stepDecisionPages)){
			for(BatchPageBeanIDList pageBeanIDList : stepDecisionPages){
				if(pageBeanIDList.getPageID().equals(id)){
					return pageBeanIDList;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * BeanID가 존재하는지 확인한다.
	 * 
	 * @param beanID
	 * @return
	 */
	public boolean isBeanIDExist(String beanID){
		if(jobPage.isBeanIDExist(beanID)){
			return true;
		}
		
		for(int i = 0; i<stepDecisionPages.size(); i++){
			BatchPageBeanIDList beanIDList = stepDecisionPages.get(i);
			if(beanIDList.isBeanIDExist(beanID)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <pre>
	 * Job Page의 Bean ID list를 제거한다.
	 * 단, Job ID는 제거하지 않는다
	 * (수정을 위해)
	 * </pre>
	 */
	public void removeJobBeanPageToAvailableList(){
		jobPage = new BatchPageBeanIDList();
	}
	
	/** 현재 BatchJobBeanIDList와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public BatchJobBeanIDList clone(){
		BatchJobBeanIDList clone = new BatchJobBeanIDList();
		clone.setJobID(jobID);
		clone.setJobPage(jobPage.clone());
		
		ArrayList<BatchPageBeanIDList> cloneStepDecisionPages = new ArrayList<BatchPageBeanIDList>();
		for(int i = 0 ; i<stepDecisionPages.size(); i++){
			cloneStepDecisionPages.add(stepDecisionPages.get(i).clone());
		}
		
		clone.setStepDecisionPages(cloneStepDecisionPages);
		
		return clone;
	}
	
	/**
	 * jobPage의 값을 가져온다
	 *
	 * @return the jobPage
	 */
	public BatchPageBeanIDList getJobPage() {
		return jobPage;
	}

	/**
	 * jobPage의 값을 설정한다.
	 *
	 * @param jobPage
	 */
	public void setJobPage(BatchPageBeanIDList jobPage) {
		this.jobPage = jobPage;
	}

	/**
	 * stepDecisionPages의 값을 가져온다
	 *
	 * @return the stepDecisionPages
	 */
	public ArrayList<BatchPageBeanIDList> getStepDecisionPages() {
		return stepDecisionPages;
	}

	/**
	 * stepDecisionPages의 값을 설정한다.
	 *
	 * @param stepDecisionPages
	 */
	public void setStepDecisionPages(
			ArrayList<BatchPageBeanIDList> stepDecisionPages) {
		this.stepDecisionPages = stepDecisionPages;
	}

	/**
	 * jobID의 값을 설정한다.
	 *
	 * @param jobID
	 */
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	
}
