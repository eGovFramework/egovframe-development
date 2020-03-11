package egovframework.mdev.imp.commngt.wizards.model;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 컴포넌트 정보 클래스
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
public class Component implements IComponentElement, IParent {

	/** 이름 */
	private String name = "";
	/** 설명 */
	private String desc = "";
	/** 패키지명 */
	private String packageName = "";
	/** 연관테이블 */
	private String useTable = "";
	/** 디펜던시패키지 */
	private List<?> dependencyPackage = null;
	/** 이미지정보 */
	private ImageDescriptor image = null;
	/** 자식컴포넌트정보 */
	//private Object children = null;
	/** 설치여부 */
	private boolean isCreatedComponent = false;
    /** 버전 */
	private String version = "";
    /** 파일명 */
	private String fileName = "";
	/** 부모 카테고리 정보 */
	private IComponentElement parent = null ;
    /** 부모카테고리명 */
	private String parentName = "";
	/** 컴포넌트 선택 여부 */
	private boolean isSelection = false;
	/** web.xml 배포여부 */
	private boolean isWebExist = false;
	/** 댓글, 만족도, 스크랩 기능 사용여부 */
	private boolean isAddedOptions = false;
	/** create table 성공실패여부 */
	private String successOrFail = "";
	
	/**
	 * @return the isAddedOptions
	 */
	public boolean isAddedOptions() {
		return isAddedOptions;
	}

	/**
	 * setter
	 * @param isAddedOptions the isAddedOptions to set
	 */
	public void setAddedOptions(boolean isAddedOptions) {
		this.isAddedOptions = isAddedOptions;
	}

	/**
	 * @return the webExist
	 */
	public boolean isWebExist() {
		return isWebExist;
	}

	/**
	 * setter
	 * @param webExist the webExist to set
	 */
	public void setWebExist(boolean isWebExist) {
		this.isWebExist = isWebExist;
	}

	/**
	 * @return the isSelection
	 */
	public boolean isSelection() {
		return isSelection;
	}

	/**
	 * setter
	 * @param isSelection the isSelection to set
	 */
	public void setSelection(boolean isSelection) {
		this.isSelection = isSelection;
	}

	/**
	 * @return the cheildren
	 */
	public List<IComponentElement> getChildren() {
		return null;
	}

	/**
	 * @return the packageName equals
	 */
	public boolean equals(Object obj){
		if ( obj instanceof Component){
			if ( this.packageName.equals( ((Component)obj).getPackageName()))
				return true;
		}
		return false;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * setter
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * setter
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the useTable
	 */
	public String getUseTable() {
		return useTable;
	}

	/**
	 * setter
	 * @param useTable the useTable to set
	 */
	public void setUseTable(String useTable) {
		this.useTable = useTable;
	}

	/**
	 * @return the dependencyPackage
	 */
	@SuppressWarnings("rawtypes")
	public List getDependencyPackage() {
		return dependencyPackage;
	}

	/**
	 * setter
	 * @param dependencyPackage the dependencyPackage to set
	 */
	public void setDependencyPackage(List<?> dependencyPackage) {
		this.dependencyPackage = dependencyPackage;
	}

	/**
	 * @return the image
	 */
	public ImageDescriptor getImage() {
		return image;
	}

	/**
	 * setter
	 * @param image the image to set
	 */
	public void setImage(ImageDescriptor image) {
		this.image = image;
	}

	/**
	 * @return the isCreatedComponent
	 */
	public boolean isCreatedComponent() {
		return isCreatedComponent;
	}

	/**
	 * setter
	 * @param isCreatedComponent the isCreatedComponent to set
	 */
	public void setCreatedComponent(boolean isCreatedComponent) {
		this.isCreatedComponent = isCreatedComponent;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * setter
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * setter
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the parent
	 */
	public IComponentElement getParent() {
		return parent;
	}

	/**
	 * setter
	 * @param parent the parent to set
	 */
	public void setParent(IComponentElement parent) {
		this.parent = parent;
	}
	
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * setter
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	/**
	 * @return the parent
	 */
	public String getSuccessOrFail() {
		return successOrFail;
	}

	/**
	 * setter
	 * @param parent the parent to set
	 */
	public void setSuccessOrFail(String successOrFail) {
		this.successOrFail = successOrFail;
	}


}