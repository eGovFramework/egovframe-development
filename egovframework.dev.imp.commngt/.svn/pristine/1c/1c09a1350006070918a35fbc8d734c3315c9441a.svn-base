package egovframework.dev.imp.commngt.wizards.model;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 컴포넌트 정보 인터페이스 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public interface IComponentElement {

	/** 이름 */
	public String getName();
	 
    /** 설명 */
	public String getDesc();
	
    /** 패키지명 */
    //private IPath locationPath;
	public String getPackageName();
	 
    /** 연관테이블 */
	public String getUseTable();
	 
    /** dependency 정보 */
	@SuppressWarnings("rawtypes")
	public List getDependencyPackage();
	 
    /** 이미지 */
	public ImageDescriptor getImage();

    /** 설치여부 */
	public boolean isCreatedComponent();

    /** 버전 */
	public String getVersion();
	

    /** 파일명 */
	public String getFileName();
	
	/** 자식노드 */
	public List<IComponentElement> getChildren();

	/** 컴포넌트 선택 여부 */
	public boolean isSelection();
	
	/** web.xml 배포여부 */
	public boolean isWebExist();
	
	/** 댓글, 만족도, 스크랩 기능 사용여부 */
	public boolean isAddedOptions();
	
	/** create table 성공실패반영 */
	public String getSuccessOrFail();

}