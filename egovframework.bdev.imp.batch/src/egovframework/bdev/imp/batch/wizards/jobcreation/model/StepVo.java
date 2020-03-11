package egovframework.bdev.imp.batch.wizards.jobcreation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.bdev.imp.confmngt.preferences.listeners.model.ChunkListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.StepListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Step의 정보를 저장하는 VO
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
public class StepVo extends StepAndDecisionVo {

	/** Step의 이름 */
	private String name = "";

	/**
	 * <pre>
	 * TRUE = partiontion mode
	 * FALSE = normal mode
	 * </pre>
	 */
	private boolean partitionMode = false;

	/** Step의 Job Reader */
	private JobReaderInfo jobReaderInfo = null;

	/** Step의 Job Writer */
	private JobWriterInfo jobWriterInfo = null;

	/** JobID.StepID.JobReaderName 형태의 String */
	private String jobReaderFullName = null;

	/** JobID.StepID.JobWriterName 형태의 String */
	private String jobWriterFullName = null;

	/** Step의 Commit-Interval */
	private Integer commitInterval = null;

	/** Step의 Next Step */
	private String nextStep = "";

	/** Step의 Chunk Listener List */
	private ChunkListenerInfo[] chunkListenerInfoList = new ChunkListenerInfo[0];

	/** Step의 Step Listener List */
	private StepListenerInfo[] stepListenerInfoList = new StepListenerInfo[0];

	/** Step의 Grid-Size */
	private Integer gridSize = null;

	/** Step의 Partitioner Type */
	private String partitionerType = "ColumnRange Partitioner";

	/** Step의 SubStep ID */
	private String subStepID = "";
	
	/** Resource 정보*/
	private String resource = "";
	
	/** Job Reader의 상세 정보를 갖는 Context */
	private Map<String, String> jobReaderContext = new HashMap<String, String>();
	
	/** Job Writer의 상세 정보를 갖는 Context */
	private Map<String, String> jobWriterContext = new HashMap<String, String>();
	
	/** SqlPagingQueryJdbcItemReader에서 입력한 Key, Value값 목록*/
	private List<TypeCSqlKeyValueVo> jobReaderSqlKeyValueVo = new ArrayList<TypeCSqlKeyValueVo>();
	
	/** StepVo의 생성자 */
	public StepVo() {
		super(null);
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
	 * partitionMode의 값을 가져온다
	 * 
	 * @return the partitionMode
	 */
	public boolean isPartitionMode() {
		return partitionMode;
	}

	/**
	 * partitionMode의 값을 설정한다.
	 * 
	 * @param partitionMode
	 */
	public void setPartitionMode(boolean partitionMode) {
		this.partitionMode = partitionMode;
	}

	/**
	 * jobReaderInfo의 값을 가져온다
	 * 
	 * @return the jobReaderInfo
	 */
	public JobReaderInfo getJobReaderInfo() {
		return jobReaderInfo;
	}

	/**
	 * jobReaderInfo의 값을 설정한다.
	 * 
	 * @param jobReaderInfo
	 */
	public void setJobReaderInfo(JobReaderInfo jobReaderInfo) {
		this.jobReaderInfo = jobReaderInfo;
	}

	/**
	 * jobWriterInfo의 값을 가져온다
	 * 
	 * @return the jobWriterInfo
	 */
	public JobWriterInfo getJobWriterInfo() {
		return jobWriterInfo;
	}

	/**
	 * jobWriterInfo의 값을 설정한다.
	 * 
	 * @param jobWriterInfo
	 */
	public void setJobWriterInfo(JobWriterInfo jobWriterInfo) {
		this.jobWriterInfo = jobWriterInfo;
	}

	/**
	 * jobReaderFullName의 값을 가져온다
	 *
	 * @return the jobReaderFullName
	 */
	public String getJobReaderFullName() {
		return jobReaderFullName;
	}

	/**
	 * jobReaderFullName의 값을 설정한다.
	 *
	 * @param jobReaderFullName
	 */
	public void setJobReaderFullName(String jobReaderFullName) {
		this.jobReaderFullName = jobReaderFullName;
	}

	/**
	 * jobWriterFullName의 값을 가져온다
	 *
	 * @return the jobWriterFullName
	 */
	public String getJobWriterFullName() {
		return jobWriterFullName;
	}

	/**
	 * jobWriterFullName의 값을 설정한다.
	 *
	 * @param jobWriterFullName
	 */
	public void setJobWriterFullName(String jobWriterFullName) {
		this.jobWriterFullName = jobWriterFullName;
	}

	/**
	 * commitInterval의 값을 가져온다
	 * 
	 * @return the commitInterval
	 */
	public Integer getCommitInterval() {
		return commitInterval;
	}

	/**
	 * commitInterval의 값을 설정한다.
	 * 
	 * @param commitInterval
	 */
	public void setCommitInterval(Integer commitInterval) {
		this.commitInterval = commitInterval;
	}

	/**
	 * nextStep의 값을 가져온다
	 * 
	 * @return the nextStep
	 */
	public String getNextStep() {
		return nextStep;
	}

	/**
	 * nextStep의 값을 설정한다.
	 * 
	 * @param nextStep
	 */
	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}

	/**
	 * chunkListenerInfoList의 값을 가져온다
	 * 
	 * @return the chunkListenerInfoList
	 */
	public ChunkListenerInfo[] getChunkListenerInfoList() {
		return chunkListenerInfoList;
	}

	/**
	 * chunkListenerInfoList의 값을 설정한다.
	 * 
	 * @param chunkListenerInfoList
	 */
	public void setChunkListenerInfoList(
			ChunkListenerInfo[] chunkListenerInfoList) {
		this.chunkListenerInfoList = chunkListenerInfoList;
	}

	/**
	 * stepListenerInfoList의 값을 가져온다
	 * 
	 * @return the stepListenerInfoList
	 */
	public StepListenerInfo[] getStepListenerInfoList() {
		return stepListenerInfoList;
	}

	/**
	 * stepListenerInfoList의 값을 설정한다.
	 * 
	 * @param stepListenerInfoList
	 */
	public void setStepListenerInfoList(StepListenerInfo[] stepListenerInfoList) {
		this.stepListenerInfoList = stepListenerInfoList;
	}

	/**
	 * gridSize의 값을 가져온다
	 * 
	 * @return the gridSize
	 */
	public Integer getGridSize() {
		return gridSize;
	}

	/**
	 * gridSize의 값을 설정한다.
	 * 
	 * @param gridSize
	 */
	public void setGridSize(Integer gridSize) {
		this.gridSize = gridSize;
	}

	/**
	 * partitionerType의 값을 가져온다
	 * 
	 * @return the partitionerType
	 */
	public String getPartitionerType() {
		return partitionerType;
	}

	/**
	 * partitionerType의 값을 설정한다.
	 * 
	 * @param partitionerType
	 */
	public void setPartitionerType(String partitionerType) {
		this.partitionerType = partitionerType;
	}

	/**
	 * subStepID의 값을 가져온다
	 * 
	 * @return the subStepID
	 */
	public String getSubStepID() {
		return subStepID;
	}

	/**
	 * subStepID의 값을 설정한다.
	 * 
	 * @param subStepID
	 */
	public void setSubStepID(String subStepID) {
		this.subStepID = subStepID;
	}

	/**
	 * resource의 값을 가져온다
	 *
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * resource의 값을 설정한다.
	 *
	 * @param resource
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	

	/**
	 * jobReaderContext의 값을 가져온다
	 *
	 * @return the jobReaderContext
	 */
	public Map<String, String> getJobReaderContext() {
		return jobReaderContext;
	}

	/**
	 * jobReaderContext의 값을 설정한다.
	 *
	 * @param jobReaderContext
	 */
	public void setJobReaderContext(Map<String, String> jobReaderContext) {
		this.jobReaderContext = jobReaderContext;
	}

	/**
	 * jobWriterContext의 값을 가져온다
	 *
	 * @return the jobWriterContext
	 */
	public Map<String, String> getJobWriterContext() {
		return jobWriterContext;
	}

	/**
	 * jobWriterContext의 값을 설정한다.
	 *
	 * @param jobWriterContext
	 */
	public void setJobWriterContext(Map<String, String> jobWriterContext) {
		this.jobWriterContext = jobWriterContext;
	}

	/**
	 * jobReadersqlKeyValueVo의 값을 가져온다
	 *
	 * @return the jobReadersqlKeyValueVo
	 */
	public List<TypeCSqlKeyValueVo> getJobReadersqlKeyValueVo() {
		return jobReaderSqlKeyValueVo;
	}

	/**
	 * jobReadersqlKeyValueVo의 값을 설정한다.
	 *
	 * @param jobReadersqlKeyValueVo
	 */
	public void setJobReadersqlKeyValueVo(
			List<TypeCSqlKeyValueVo> jobReadersqlKeyValueVo) {
		this.jobReaderSqlKeyValueVo = jobReadersqlKeyValueVo;
	}

	/**
	 * Parameter로 넘어온 StepVo의 값들을 복사해 온다.
	 * 
	 * @param originalDecisionVo
	 */
	public void copyValues(StepVo originalStepVo) {
		if (NullUtil.isNull(originalStepVo)) {
			return;
		}

		setJobName(originalStepVo.getJobName());
		name = originalStepVo.getName();
		partitionMode = originalStepVo.isPartitionMode();
		jobReaderInfo = originalStepVo.getJobReaderInfo().clone();
		jobReaderFullName = originalStepVo.getJobReaderFullName();
		jobWriterInfo = originalStepVo.getJobWriterInfo().clone();
		jobWriterFullName = originalStepVo.getJobWriterFullName();
		commitInterval = originalStepVo.getCommitInterval();
		nextStep = originalStepVo.getNextStep();

		ChunkListenerInfo[] originalChunkListenerInfo = originalStepVo
				.getChunkListenerInfoList();
		if (!NullUtil.isEmpty(originalChunkListenerInfo)) {
			ChunkListenerInfo[] chunkListenerInfos = new ChunkListenerInfo[originalChunkListenerInfo.length];
			for (int i = 0; i < originalChunkListenerInfo.length; i++) {
				chunkListenerInfos[i] = originalChunkListenerInfo[i].clone();
			}
			setChunkListenerInfoList(chunkListenerInfos);
		}else{
			setChunkListenerInfoList(new ChunkListenerInfo[0]);
		}

		StepListenerInfo[] originalStepListenerInfos = originalStepVo
				.getStepListenerInfoList();
		if (!NullUtil.isEmpty(originalStepListenerInfos)) {
			StepListenerInfo[] stepListenerInfos = new StepListenerInfo[originalStepListenerInfos.length];
			for (int i = 0; i < originalStepListenerInfos.length; i++) {
				stepListenerInfos[i] = originalStepListenerInfos[i].clone();
			}
			setStepListenerInfoList(stepListenerInfos);
		}else{
			setStepListenerInfoList(new StepListenerInfo[0]);
		}

		gridSize = originalStepVo.getGridSize();
		partitionerType = originalStepVo.getPartitionerType();
		subStepID = originalStepVo.getSubStepID();
		resource = originalStepVo.getResource();
		
		Map<String, String> originalJobReaderContext = originalStepVo.getJobReaderContext();
		if(!NullUtil.isEmpty(originalJobReaderContext)){
			jobReaderContext = new HashMap<String, String>(originalJobReaderContext);
		}else{
			jobReaderContext = new HashMap<String, String>();
		}
		
		Map<String, String> originalJobWriterContext = originalStepVo.getJobWriterContext();
		if(!NullUtil.isEmpty(originalJobWriterContext)){
			jobWriterContext = new HashMap<String, String>(originalJobWriterContext);
		}else{
			jobWriterContext =  new HashMap<String, String>();
		}
		
		List<TypeCSqlKeyValueVo> originalJobReaderSqlKeyValueVo = originalStepVo.getJobReadersqlKeyValueVo();
		if(!NullUtil.isEmpty(originalJobReaderSqlKeyValueVo)){
			ArrayList<TypeCSqlKeyValueVo> jobReaderSqlKeyValueVo = new ArrayList<TypeCSqlKeyValueVo>();
			for(TypeCSqlKeyValueVo vo : originalJobReaderSqlKeyValueVo){
				jobReaderSqlKeyValueVo.add(vo.clone());
			}
			this.jobReaderSqlKeyValueVo = jobReaderSqlKeyValueVo;
		}else{
			jobReaderSqlKeyValueVo = new ArrayList<TypeCSqlKeyValueVo>();
		}
	}

	/** 현재 StepVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public StepVo clone() {
		StepVo clone = new StepVo();
		clone.copyValues(this);

		return clone;
	}

}
