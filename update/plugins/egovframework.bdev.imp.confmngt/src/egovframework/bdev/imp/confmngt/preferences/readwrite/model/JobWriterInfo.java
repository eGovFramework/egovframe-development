package egovframework.bdev.imp.confmngt.preferences.readwrite.model;

/**
 * JobWriterContents에 사용되는 VO객체
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
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
public class JobWriterInfo extends JobRWInfo{

	/** Job Writer Name */
	private String name;
	
	/** Job Writer ItemType */
	private String itemType;
	
	/** Job Writer ClassValue */
	private String classValue;
	
	/** Job Writer의 Resource Type */
	private String resourceType;

	/** Job Writer의 Resource Deatail Type */
	private String resourceDetailType = null;

	public String getResourceDetailType() {
		return resourceDetailType;
	}

	public void setResourceDetailType(String resourceDetailType) {
		this.resourceDetailType = resourceDetailType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getClassValue() {
		return classValue;
	}

	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * ItemWriterInfo의 생성자
	 * 
	 * @param name
	 * @param itemType
	 * @param classValue
	 */
	public JobWriterInfo(String name, String itemType, String classValue, String resourceType){
		this.name = name;
		this.itemType = itemType;
		this.classValue = classValue;
		this.resourceType = resourceType;
	}
	
	/** ItemWriterInfo의 생성자 */
	public JobWriterInfo(){
		
	}
	
	/** 현재 Info와 동일한 정보를 갖는 새로운 Info를 생성한다. */
	public JobWriterInfo clone() {
		JobWriterInfo clone = new JobWriterInfo();
		clone.setName(name);
		clone.setItemType(itemType);
		clone.setClassValue(classValue);
		clone.setResourceType(resourceType);
		
		return clone;
	}
}
