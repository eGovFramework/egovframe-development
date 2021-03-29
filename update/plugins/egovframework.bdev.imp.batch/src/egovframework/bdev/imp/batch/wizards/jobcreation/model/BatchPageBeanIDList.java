package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import java.util.ArrayList;

import egovframework.bdev.imp.confmngt.preferences.listeners.model.ChunkListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.JobListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.StepListenerInfo;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * Batch Job, Step, Decision Page의 Bean ID List
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

public class BatchPageBeanIDList {
	
	/** Job, Step, Decision의 ID */
	private String pageID = null;
	
	/** Page내 Bean ID의 List */
	private ArrayList<String> beanIDList = null;
	
	/** BatchPageBeanIDList 생성자 */
	public BatchPageBeanIDList(){
		beanIDList = new ArrayList<String>();
	}
	
	/**
	 * BatchPageBeanIDList 생성자
	 * 
	 * @param id
	 */
	public BatchPageBeanIDList(String id){
		this();
		pageID = id;
	}
	
	/**
	 * pageID의 값을 가져온다
	 *
	 * @return the pageID
	 */
	public String getPageID() {
		return pageID;
	}

	/**
	 * pageID의 값을 설정한다.
	 *
	 * @param pageID
	 */
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	/**
	 * <pre>
	 * bean ID를 저장.
	 * 중복된 값이 존재할 경우 false를 return
	 * 
	 * 단, beanID가 null인 경우는 저장하지 않지만 true를 return.
	 * </pre>
	 * @param beanID
	 * @return
	 */
	public boolean addBeanID(String beanID){
		if(NullUtil.isEmpty(beanID)){
			return true;
		}
		
		if(beanIDList.indexOf(beanID) < 0){
			return beanIDList.add(beanID);
		}else{
			return false;
		}
	}
	
	/** Bean ID를 중복 검사 없이 입력한다. */
	public boolean addBeanIDWithoutValidate(String beanID){
		if(NullUtil.isEmpty(beanID)){
			return true;
		}
		
		return beanIDList.add(beanID);
	}
	
	/** Bean ID를 제거한다 */
	public boolean removeBeanID(String beanID){
		return beanIDList.remove(beanID);
	}
	
	/**
	 * 여러개의 Bean ID를 제거한다.
	 * 
	 * @param beanIDs
	 * @return
	 */
	public boolean removeBeanID(String[] beanIDs){
		if(NullUtil.isEmpty(beanIDs)){
			return false;
		}
		
		boolean result = true;
		
		for(String beanID : beanIDs){
			if(!removeBeanID(beanID)){
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Bean ID를 교체한다.
	 * 
	 * @param preBeanID
	 * @param postBeanID
	 * @return
	 */
	public boolean changeBeanID(String preBeanID, String postBeanID){
		
		if(NullUtil.isEmpty(preBeanID) || NullUtil.isEmpty(postBeanID)){
			return false;
		}
		
		if(removeBeanID(preBeanID)){
			beanIDList.add(postBeanID);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Bean ID의 존재 여부를 확인한다.
	 * 
	 * @param beanID
	 * @return
	 */
	public boolean isBeanIDExist(String beanID){
		
		if(NullUtil.isEmpty(beanID)){
			return false;
		}
		
		if(beanIDList.indexOf(beanID) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * beanIDList의 값을 가져온다
	 *
	 * @return the beanIDList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getBeanIDList() {		
		return (ArrayList<String>)beanIDList.clone();
	}

	/**
	 * beanIDList의 값을 설정한다.
	 *
	 * @param beanIDList
	 */
	public void setBeanIDList(ArrayList<String> beanIDList) {
		this.beanIDList = beanIDList;
	}
	/**
	 * <pre>
	 * JobVo의 bean ID를 저장.
	 * 중복된 값이 존재할 경우 false를 return
	 * 
	 * 단, JobVo가 null인 경우는 저장하지 않지만 true를 return.
	 * </pre>
	 * @param beanID
	 * @return
	 */
	public boolean setJobInfoPageBeanIDs(JobVo jobVo){
		
		if(NullUtil.isNull(jobVo)){
			return false;
		}
		
		resetList();
		
		return performJobElement(jobVo);
	}
	
	/**
	 * <pre>
	 * StepVo의 bean ID를 저장.
	 * 중복된 값이 존재할 경우 false를 return
	 * 
	 * 단, StepVo가 null인 경우는 저장하지 않지만 true를 return.
	 * </pre>
	 * @param beanID
	 * @return
	 */
	public boolean setStepInfoPageBeanIDs(StepVo stepVo){
		
		if(NullUtil.isNull(stepVo)){
			return true;
		}
		
		resetList();
		
		return performStepElement(stepVo);
	}
	
	/**
	 * Decivion Info화면의 BeanID를 입력한다.
	 * 
	 * @param decisionVo
	 */
	public void setDecisionInfoPageBeanIDs(DecisionVo decisionVo){
		
		if(NullUtil.isNull(decisionVo)){
			return;
		}
		
		resetList();
		
		performDecisionElement(decisionVo);
	}
	
	/** Bean ID list를 초기화 한다. */
	public void resetList(){
		beanIDList = new ArrayList<String>();
	}
	
	/** 현재 BatchPageBeanIDList와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public BatchPageBeanIDList clone(){
		BatchPageBeanIDList clone = new BatchPageBeanIDList();
		if(!NullUtil.isNull(pageID)){
			clone.setPageID(pageID);
		}
		
		ArrayList<String> cloneArray = new ArrayList<String>(beanIDList);
		clone.setBeanIDList(cloneArray);
		
		return clone;
	}
	
	/**
	 * JobVo에 있는 Bean ID를 저장한다.
	 * 
	 * @param jobVo
	 * @return
	 */
	protected boolean performJobElement(JobVo jobVo){
		boolean result = true;
		
		if(!addBeanID(jobVo.getJobName())){
			result = false;
		}
		
		if(!setJobListenerBeanIDs(jobVo)){
			result = false;
		}
		
		if(!setSharedValuesBeanIDs(jobVo)){
			result = false;
		}
		
		return result;
	}
	
	/**
	 * StepVo 또는 DecisionVo에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	protected void performStepAndDecisionElement(StepAndDecisionVo[] stepAndDecisionVos){
		
		if(!NullUtil.isEmpty(stepAndDecisionVos)){
			for(StepAndDecisionVo stepAndDecisionVo : stepAndDecisionVos){
				if(stepAndDecisionVo instanceof StepVo){
					
					performStepElement((StepVo)stepAndDecisionVo);
					
				}else if(stepAndDecisionVo instanceof DecisionVo){
					
					performDecisionElement((DecisionVo)stepAndDecisionVo);
					
				}
			}
		}
	}
	
	/**
	 * StepVo에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	private boolean performStepElement(StepVo stepVo){		
		boolean result = true;
		
		if(!addBeanID(stepVo.getName())){
			result = false;
		}
		
		if(!addBeanID(stepVo.getSubStepID())){
			result = false;
		}
		
		if(!setStepListenerBeanIDs(stepVo)){
			result = false;
		}
		
		if(!setChunkListenerBeanIDs(stepVo)){
			result = false;
		}
		
		return result;
	}
	
	/**
	 * DecisionVo에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	private void performDecisionElement(DecisionVo decisionVo){
		
		addBeanID(decisionVo.getName());		
	}
	
	/** Job Listener Name 입력*/
	private boolean setJobListenerBeanIDs(JobVo jobVo){
		boolean result = true;
		
		JobListenerInfo[] jobListenerList = jobVo.getJobListenerInfoList();
		
		if(!NullUtil.isEmpty(jobListenerList)){
			for(JobListenerInfo info: jobListenerList){
				if(!addBeanID(info.getName())){
					result = false;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * JobVo의 SharedValueVo에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	private boolean setSharedValuesBeanIDs(JobVo jobVo){
		boolean result = true;
		
		SharedValueVo[] sharedValues = jobVo.getSharedValues();
		
		if(!NullUtil.isEmpty(sharedValues)){
			for(SharedValueVo vo : sharedValues){
				if(!addBeanID(vo.getKey())){
					result = false;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Item Reader / Writer에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	public boolean addJobRWBeanID(StepVo stepVo){
		boolean result = true;
		
		String jobReaderFullName = stepVo.getJobReaderFullName();
		if(!NullUtil.isEmpty(jobReaderFullName)){
			if(!addBeanID(jobReaderFullName)){
				result = false;
			}
		}
		
		String jobWriterFullName = stepVo.getJobWriterFullName();
		if(!NullUtil.isEmpty(jobWriterFullName)){
			if(!addBeanID(jobWriterFullName)){
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Job RW의 Bean ID를 갱신한다.
	 * 
	 * @param preStepVo
	 * @param newStepVo
	 * @return
	 */
	public boolean updateJobRWBeanID(StepVo preStepVo, StepVo newStepVo){
		
		String preJobReaderFullName = preStepVo.getJobReaderFullName();
		if(!NullUtil.isEmpty(preJobReaderFullName)){
			removeBeanID(preJobReaderFullName);
		}
		
		String preJobWriterFullName = preStepVo.getJobWriterFullName();
		if(!NullUtil.isEmpty(preJobWriterFullName)){
			removeBeanID(preJobWriterFullName);
		}
		
		return addJobRWBeanID(newStepVo);
	}
	
	/**
	 * Step Listener에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	private boolean setStepListenerBeanIDs(StepVo stepVo){
		boolean result = true;
		
		StepListenerInfo[] infos = stepVo.getStepListenerInfoList();
		
		if(!NullUtil.isEmpty(infos)){
			for(StepListenerInfo info : infos){
				if(!addBeanID(info.getName())){
					result = false;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Chunk Listener에 있는 Bean ID를 저장한다.
	 * 
	 * @param stepAndDecisionVos
	 */
	private boolean setChunkListenerBeanIDs(StepVo stepVo){
		boolean result = true;
		
		ChunkListenerInfo[] infos = stepVo.getChunkListenerInfoList();
		
		if(!NullUtil.isEmpty(infos)){
			for(ChunkListenerInfo info : infos){
				if(!addBeanID(info.getName())){
					result = false;
				}
			}
		}
		
		return result;
	}
}
