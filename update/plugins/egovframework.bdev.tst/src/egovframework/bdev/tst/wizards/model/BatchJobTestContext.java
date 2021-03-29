package egovframework.bdev.tst.wizards.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterInfo;
/**
 * 배치 테스트 컨택스트 클래스
 * @author 조용현
 * @since 2012.07.24
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
public class BatchJobTestContext{

	/** 선택한 Project 이름 */
	private String projectName;
	
	/** Test할 Job VO */
	private JobVo jobVo;
	
	/** Job Test시 실행 유형*/
	private String executorType;
	
	/** Job Parameter List */
	private JobParameterInfo[] jobParameterList;
	
	/** Test 할 파일의 경로 */
	private String folderPath = null;
	
	/** Test 할 파일의 파일명 */
	private String fileName = null;
	
	/** 선택한 프로젝트 내 <job 이 존재하는 XML파일 리스트 */
	private Map<String, IFile> foundJobXMLfiles = new HashMap<String, IFile>();
	
	/** 선택한 프로젝트 내 EgovBatchRunner 이 존재하는 XML파일 리스트 */
	private Map<String, IFile> foundJobExecXMLfiles = new HashMap<String, IFile>();
	
	/** 선택한 프로젝트 정보 */
	private IStructuredSelection selection;
	
	/** 선택한 Job Execution의 이름*/
	private String jobExecName;
	
	/** 생성한 Job Test 파일의 위치정보*/
	private String jobTestFile;
	
	/** jobRepository라는 ID가 있는 XML 파일*/
	private IFile jobRepositoryXMLFile = null;
	
	/** jobRepository하위의 datasource라는 ID가 있는 XML 파일*/
	private IFile datasourceXMLFile = null;
	
	/** transactionManager라는 ID가 있는 XML 파일*/
	private IFile transactionManagerXMLFile = null;
	
	/** 선택한 launcher 파일의 비동기 여부*/
	private Boolean isAsync = false;
	
	/** BatchJobTestContext의 생성자*/
	public BatchJobTestContext() {
	}

	/**
	 * projectName의 값을 가져온다
	 *
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * projectName의 값을 설정한다.
	 *
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * jobVo의 값을 가져온다
	 *
	 * @return the jobVo
	 */
	public JobVo getJobVo() {
		return jobVo;
	}

	/**
	 * jobVo의 값을 설정한다.
	 *
	 * @param jobVo the jobVo to set
	 */
	public void setJobVo(JobVo jobVo) {
		this.jobVo = jobVo;
	}

	/**
	 * executorType의 값을 가져온다
	 *
	 * @return the executorType
	 */
	public String getExecutorType() {
		return executorType;
	}

	/**
	 * executorType의 값을 설정한다.
	 *
	 * @param executorType the executorType to set
	 */
	public void setExecutorType(String executorType) {
		this.executorType = executorType;
	}

	/**
	 * jobParameterList의 값을 가져온다
	 *
	 * @return the jobParameterList
	 */
	public JobParameterInfo[] getJobParameterList() {
		return jobParameterList;
	}

	/**
	 * jobParameterList의 값을 설정한다.
	 *
	 * @param jobParameterList the jobParameterList to set
	 */
	public void setJobParameterList(JobParameterInfo[] jobParameterList) {
		this.jobParameterList = jobParameterList;
	}

	/**
	 * foundJobXMLfiles의 값을 가져온다
	 *
	 * @return the foundXMLfiles
	 */
	public Map<String, IFile> getFoundJobXMLfiles() {
		return foundJobXMLfiles;
	}

	/**
	 * foundJobXMLfiles의 값을 설정한다.
	 *
	 * @param foundJobXMLfiles the foundXMLfiles to set
	 */
	public void setFoundJobXMLfiles(Map<String, IFile> foundJobXMLfiles) {
		this.foundJobXMLfiles = foundJobXMLfiles;
	}
	
	/**
	 * 선택한 프로젝트 정보를 셋팅
	 * 
	 * @param selection
	 */
	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}
	
	/**
	 * 선택한 프로젝트 정보를 가져온다
	 * 
	 * @return IStructuredSelection
	 */
	public IStructuredSelection getSelection() {
		return selection;
	}
	
	/**
	 * foundJobExecXMLfiles의 값을 설정한다.
	 * 
	 * @param foundJobExecXMLfiles
	 */
	public void setFoundJobExecXMLfiles(Map<String, IFile> foundJobExecXMLfiles) {
		this.foundJobExecXMLfiles = foundJobExecXMLfiles;
	}

	/**
	 * foundJobExecXMLfiles의 값을 가져온다
	 * 
	 * @return foundJobExecXMLfiles
	 */
	public Map<String, IFile> getFoundJobExecXMLfiles() {
		return foundJobExecXMLfiles;
	}

	/**
	 * folderPath의 값을 가져온다
	 *
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * folderPath의 값을 설정한다.
	 *
	 * @param string the folderPath to set
	 */
	public void setFolderPath(String string) {
		this.folderPath = string;
	}

	/**
	 * fileName의 값을 가져온다
	 *
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * fileName의 값을 설정한다.
	 *
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 선택한 Job Execution을 설정한다.
	 * 
	 * @param jobExecName
	 */
	public void setJobExecName(String jobExecName) {
		this.jobExecName = jobExecName;
	}

	/**
	 * 선택한 Job Execution의 값을 가져온다.
	 * 
	 * @return jobExecName
	 */
	public String getJobExecName() {
		return jobExecName;
	}

	/**
	 * 생성한 Job Test 파일의 위치정보를 설정한다.
	 * 
	 * @param jobTestFile
	 */
	public void setJobTestFile(String jobTestFile) {
		this.jobTestFile = jobTestFile;
	}

	/**
	 * 생성한 Job Test 파일의 위치정보를 가져온다.
	 * 
	 * @return jobTestFile
	 */
	public String getJobTestFile() {
		return jobTestFile;
	}
	
	/**
	 * jobRepository 라는 ID를 갖고 있는 XMl 파일을 설정한다.
	 * 
	 * @param jobRepositoryXMLFile
	 */
	public void setJobRepositoryXMLFile(IFile jobRepositoryXMLFile) {
		this.jobRepositoryXMLFile = jobRepositoryXMLFile;
	}
	/**
	 * jobRepository 라는 ID를 갖고 있는 XMl 파일을 가져온다.
	 * 
	 * @return jobRepositoryXMLFile
	 */
	public IFile getJobRepositoryXMLFile() {
		return jobRepositoryXMLFile;
	}
	/**
	 * datasource 라는 ID를 갖고 있는 XMl 파일을 설정한다.
	 * 
	 * @param datasourceXMLFile
	 */
	public void setDatasourceXMLFile(IFile datasourceXMLFile) {
		this.datasourceXMLFile = datasourceXMLFile;
	}
	/**
	 * datasource 라는 ID를 갖고 있는 XMl 파일을 가져온다.
	 * 
	 * @return datasourceXMLFile
	 */
	public IFile getDatasourceXMLFile() {
		return datasourceXMLFile;
	}
	/**
	 * 비동기 여부를 설정한다.
	 * 
	 * @param isAsync
	 */
	public void setIsAsync(Boolean isAsync) {
		this.isAsync = isAsync;
	}
	/**
	 * 비동기 여부를 가져온다.
	 * 
	 * @return isAsync
	 */
	public Boolean getIsAsync() {
		return isAsync;
	}

	/**
	 * transactionManager 라는 ID를 갖고 있는 XMl 파일을 설정한다.
	 * 
	 * @return datasourceXMLFile
	 */
	public void setTransactionManagerXMLFile(IFile transactionManagerXMLFile) {
		this.transactionManagerXMLFile = transactionManagerXMLFile;
	}

	/**
	 * transactionManager 라는 ID를 갖고 있는 XMl 파일을 가져온다.
	 * 
	 * @return datasourceXMLFile
	 */
	public IFile getTransactionManagerXMLFile() {
		return transactionManagerXMLFile;
	}

	/** Context 값을 초기화 시킨다. */
	public void clearValues(){
		projectName = null;
		jobVo = null;
		executorType = null;
		jobParameterList = null;
		folderPath = null;
		fileName = null;
		foundJobXMLfiles = new HashMap<String, IFile>();
		foundJobExecXMLfiles = new HashMap<String, IFile>();
		selection = null;
		jobExecName = null;
		jobTestFile = null;
		transactionManagerXMLFile = null;
	}

}
