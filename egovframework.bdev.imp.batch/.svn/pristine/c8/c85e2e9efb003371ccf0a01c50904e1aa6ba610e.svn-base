package egovframework.bdev.imp.batch.wizards.com;

import org.eclipse.core.internal.resources.Project;

/**
 * Job 생성, Job Execution 생성의 부모 context Class
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
public class BatchJobContext {

	/** 파일을 생성할 Project */
	private Project project = null;
	
	/** 생성할 파일의 이름  */
	private String fileName = null;
	
	/** 생성할 파일의 경로  */
	private String filePath = null;

	/** BatchJobContext의 생성자 */
	public BatchJobContext() {
	}

	/**
	 * BatchJobContext의 생성자
	 * 
	 * @param project
	 * @param fileName
	 * @param filePath
	 *
	 */
	public BatchJobContext(Project project, String fileName, String filePath) {
		this.project = project;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	/**
	 * project의 값을 가져온다
	 *
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * project의 값을 설정한다.
	 *
	 * @param project
	 */
	public void setProject(Project project) {
		this.project = project;
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
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * filePath의 값을 가져온다
	 *
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * filePath의 값을 설정한다.
	 *
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}	
}
