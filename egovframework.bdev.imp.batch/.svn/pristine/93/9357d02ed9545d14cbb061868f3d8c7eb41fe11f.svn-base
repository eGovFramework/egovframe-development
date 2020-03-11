package egovframework.bdev.imp.batch.wizards.joblauncher.model;

import java.util.List;

import org.eclipse.core.internal.resources.Project;

import egovframework.bdev.imp.batch.wizards.com.BatchJobContext;

/**
 * Batch Job Execution 생성 Wizard의 Context
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
public class BatchJobLauncherContext extends BatchJobContext{
	
	final public static String DB_REFERENCE = "DB(Reference)";
	final public static String DB_NEW = "DB(New)";
	final public static String MEMORY = "Memory";

	/** 입력한 Job Launcher ID */
	private String jobLauncerId = null;
	
	/** 입력한 Repository ID */
	private String repositoryId = null;
	
	/**
	 * <pre>
	 * TRUE = Synchronous
	 * FALSE = Asynchronous
	 * </pre>
	 */
	private boolean syncMode = true;
	
	/** 입력한 Repository Type */
	private String repositoryType = null;
	
	/** 입력한 datasource BeanID*/
	private String DatasourceBeanID = null;
	
	/** 선택한 DataBase */
	private String selectDB = null;
	
	/** 선택한 DataBase의 type */
	private String dbType = null;
	
	/** 선택한 DataBase의 Class Name */
	private String driverClassName = null;
	
	/** 선택한 DataBase의 URL */
	private String url = null;
	
	/** 선택한 DataBase의 User Name */
	private String userName = null;
	
	/** 선택한 DataBase의 비밀번호 */
	private String passwd = null;
	
	/** 입력한 Operation ID */
	private String operatorId = null;
	
	/** 입력한 Explorer ID */
	private String explorerId = null;
	
	/** 입력한 Register ID */
	private String registerId = null;
	
	/** 기존에 TransactionManager 라는 ID의 존재여부 */
	private boolean isTransactionManagerExist = false;
	
	/** 기존에 LobHandler 라는 ID의 존재여부 */
	private boolean isLobHandlerExist = false;
	
	/** 기존에 JdbcTemplate 라는 ID의 존재여부 */
	private boolean isJdbcTemplateExist = false;
	
	/** 기존에 jobRepository 라는 ID의 존재여부 */
	private boolean isJobRepositoryExist = false;
	
	/** 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 class 정보 */
	private String jobRepositoryClass = null;
	
	/** 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 p:dataSource-ref 정보 */
	private String dataSourceRef = null;
	
	/** 기존에 존재하는 beanID 리스트 */
	private List<String> beanList;
	
	/** BatchJobExecutorContext의 생성자  */
	public BatchJobLauncherContext() {
	}
	
	/**
	 * BatchJobExecutorContext의 생성자
	 * 
	 * @param project
	 * @param fileName
	 * @param filePath
	 *
	 */
	public BatchJobLauncherContext(Project project, String fileName, String filePath){
		super(project, fileName, filePath);
	}

	/**
	 * jobLauncerId의 값을 가져온다
	 *
	 * @return the jobLauncerId
	 */
	public String getJobLauncerId() {
		return jobLauncerId;
	}

	/**
	 * jobLauncerId의 값을 설정한다.
	 *
	 * @param jobLauncerId
	 */
	public void setJobLauncerId(String jobLauncerId) {
		this.jobLauncerId = jobLauncerId;
	}

	/**
	 * repositoryId의 값을 가져온다
	 *
	 * @return the repositoryId
	 */
	public String getRepositoryId() {
		return repositoryId;
	}

	/**
	 * repositoryId의 값을 설정한다.
	 *
	 * @param repositoryId
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	/**
	 * syncMode의 값을 가져온다
	 *
	 * @return the syncMode
	 */
	public boolean isSyncMode() {
		return syncMode;
	}

	/**
	 * syncMode의 값을 설정한다.
	 *
	 * @param syncMode
	 */
	public void setSyncMode(boolean syncMode) {
		this.syncMode = syncMode;
	}

	/**
	 * repositoryType의 값을 가져온다
	 *
	 * @return the repositoryType
	 */
	public String getRepositoryType() {
		return repositoryType;
	}

	/**
	 * repositoryType의 값을 설정한다.
	 *
	 * @param repositoryType
	 */
	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}

	/**
	 * selectDB의 값을 가져온다
	 *
	 * @return the selectDB
	 */
	public String getSelectDB() {
		return selectDB;
	}

	/**
	 * selectDB의 값을 설정한다.
	 *
	 * @param selectDB
	 */
	public void setSelectDB(String selectDB) {
		this.selectDB = selectDB;
	}

	/**
	 * dbType의 값을 가져온다
	 *
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * dbType의 값을 설정한다.
	 *
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * driverClassName의 값을 가져온다
	 *
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * driverClassName의 값을 설정한다.
	 *
	 * @param driverClassName
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * url의 값을 가져온다
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * url의 값을 설정한다.
	 *
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * userName의 값을 가져온다
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * userName의 값을 설정한다.
	 *
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * passwd의 값을 가져온다
	 *
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * passwd의 값을 설정한다.
	 *
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * operatorId의 값을 가져온다
	 *
	 * @return the operatorId
	 */
	public String getOperatorId() {
		return operatorId;
	}

	/**
	 * operatorId의 값을 설정한다.
	 *
	 * @param operatorId
	 */
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * explorerId의 값을 가져온다
	 *
	 * @return the explorerId
	 */
	public String getExplorerId() {
		return explorerId;
	}

	/**
	 * explorerId의 값을 설정한다.
	 *
	 * @param explorerId
	 */
	public void setExplorerId(String explorerId) {
		this.explorerId = explorerId;
	}

	/**
	 * registerId의 값을 가져온다
	 *
	 * @return the registerId
	 */
	public String getRegisterId() {
		return registerId;
	}

	/**
	 * registerId의 값을 설정한다.
	 *
	 * @param registerId
	 */
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	/**
	 * datasourceBeanID의 값을 가져온다
	 *
	 * @return the datasourceBeanID
	 */
	public String getDatasourceBeanID() {
		return DatasourceBeanID;
	}

	/**
	 * datasourceBeanID의 값을 설정한다.
	 *
	 * @param datasourceBeanID
	 */
	public void setDatasourceBeanID(String datasourceBeanID) {
		DatasourceBeanID = datasourceBeanID;
	}

	/**
	 * TransactionManager의 값을 설정한다
	 *
	 * @param the isTransactionManagerExist
	 */
	public void setIsTransactionManagerExist(boolean isTransactionManagerExist) {
		this.isTransactionManagerExist = isTransactionManagerExist;
	}
	/**
	 * TransactionManager의 값을 가져온다
	 *
	 * @return the isTransactionManagerExist
	 */
	public boolean getIsTransactionManagerExist() {
		return isTransactionManagerExist;
	}
	/**
	 * LobHandler의 값을 설정한다
	 *
	 * @param the isLobHandlerExist
	 */
	public void setIsLobHandlerExist(boolean isLobHandlerExist) {
		this.isLobHandlerExist = isLobHandlerExist;
	}
	/**
	 * LobHandler의 값을 가져온다
	 *
	 * @return the isLobHandlerExist
	 */
	public boolean getIsLobHandlerExist() {
		return isLobHandlerExist;
	}
	/**
	 * JdbcTemplate의 값을 설정한다
	 *
	 * @param the isJdbcTemplateExist
	 */
	public void setIsJdbcTemplateExist(boolean isJdbcTemplateExist) {
		this.isJdbcTemplateExist = isJdbcTemplateExist;
	}
	/**
	 * JdbcTemplate의 값을 가져온다
	 *
	 * @return the isJdbcTemplateExist
	 */
	public boolean getIsJdbcTemplateExist() {
		return isJdbcTemplateExist;
	}
	/**
	 * JobRepository의 값을 설정한다
	 *
	 * @param the isJdbcTemplateExist
	 */
	public void setIsJobRepositoryExist(boolean isJobRepositoryExist) {
		this.isJobRepositoryExist = isJobRepositoryExist;
	}
	/**
	 * JobRepository의 값을 가져온다
	 *
	 * @return the isJobRepositoryExist
	 */
	public boolean getIsJobRepositoryExist() {
		return isJobRepositoryExist;
	}
	/**
	 * 기존에 존재하는 beanID 리스트 값을 설정한다
	 * @param beanList
	 */
	public void setBeanList(List<String> beanList) {
		this.beanList = beanList;
	}
	/**
	 * 기존에 존재하는 beanID 리스트 값을 가져온다
	 * @return beanList
	 */
	public List<String> getBeanList() {
		return beanList;
	}
	

	/**
	 * 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 class 정보를 설정한다
	 * 
	 * @param jobRepositoryClass
	 */
	public void setJobRepositoryClass(String jobRepositoryClass) {
		this.jobRepositoryClass = jobRepositoryClass;
	}

	/**
	 * 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 class 정보 가져온다
	 * 
	 * @return jobRepositoryClass
	 */
	public String getJobRepositoryClass() {
		return jobRepositoryClass;
	}

	/**
	 * 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 dataSourceRef 정보를 설정한다
	 * 
	 * @param dataSourceRef
	 */
	public void setDataSourceRef(String dataSourceRef) {
		this.dataSourceRef = dataSourceRef;
	}

	/**
	 * 기존에 jobRepository 라는 ID의 존재시 사용하고 있는 dataSourceRef 정보 가져온다
	 * 
	 * @return dataSourceRef
	 */
	public String getDataSourceRef() {
		return dataSourceRef;
	}
	
	/** Context내 정보를 모두 초기화 시킨다. */
	public void clearValues(){
		jobLauncerId = null;
		repositoryId = null;
		syncMode = true;
		repositoryType = null;
		DatasourceBeanID = null;
		selectDB = null;
		dbType = null;
		driverClassName = null;
		url = null;
		userName = null;
		passwd = null;
		operatorId = null;
		explorerId = null;
		registerId = null;
		isTransactionManagerExist = false;
		isLobHandlerExist = false;
		isJdbcTemplateExist = false;
		isJobRepositoryExist = false;
		jobRepositoryClass = null;
		dataSourceRef = null;
	}

	

	
}
