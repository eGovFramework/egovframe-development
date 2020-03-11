package egovframework.mdev.imp.commngt.wizards.model;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 카테고리 정보 클래스
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
public class Category implements IComponentElement{
	/** 자식 컴포넌트 정보 */
	private List<IComponentElement> children ;
	/** 이름 */
	private String name = "";
	/** 설명 */
	private String desc = "";
	/** 이미지 정보 */
	public ImageDescriptor image = null;


	/**
	 * @return the children
	 */
	public List<IComponentElement> getChildren() {
		return children;
	}
	/**
	 * setter
	 * @param children the children to set
	 */
	public void setChildren(List<IComponentElement> children) {
		this.children = children;
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
	 * @return the createdComponent
	 */
	public boolean isCreatedComponent() {
		return false;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return null;
	}
	/**
	 * @return the useTable
	 */
	public String getUseTable() {
		return null;
	}
	/**
	 * @return the dependencyPackage
	 */
	public List<?> getDependencyPackage() {
		return null;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return null;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return null;
	}
	
	/**
	 * @return the isSelection
	 */
	public boolean isSelection() {
		return false;
	}
	
	/**
	 * @return the iswebExist
	 */
	public boolean isWebExist() {
		return false;
	}
	
	/**
	 * @return the isAddedOptions
	 */
	public boolean isAddedOptions() {
		return false;
	}
	
	/**
	 * @return the successOrFail
	 */
	public String getSuccessOrFail() {
		return null;
	}
}