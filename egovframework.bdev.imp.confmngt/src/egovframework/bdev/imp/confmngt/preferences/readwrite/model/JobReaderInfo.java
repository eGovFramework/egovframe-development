package egovframework.bdev.imp.confmngt.preferences.readwrite.model;
/**
 * JobReaderContents에 사용되는 VO객체
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
public class JobReaderInfo extends JobRWInfo {
	
	/** Job Reader Name */
	private String name;
	
	/** Job Reader ItemType */
	private String itemType;
	
	/** Job Reader ClassValue */
	private String classValue;
	
	/** Job Reader의 Resource Type */
	private String resourceType;
	
	/** Job Reader의 Resource Deatail Type */
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
	 * ItemReaderInfo의 생성자
	 * 
	 * @param name
	 * @param itemType
	 * @param classValue
	 */
	public JobReaderInfo(String name, String itemType, String classValue, String resourceType){
		this.name = name;
		this.itemType = itemType;
		this.classValue = classValue;
		this.resourceType = resourceType;
	}
	
	/** ItemReaderInfo의 생성자 */
	public JobReaderInfo(){
		
	}
	
	/** 현재 Info와 동일한 정보를 갖는 새로운 Info를 생성한다. */
	public JobReaderInfo clone() {
		JobReaderInfo clone = new JobReaderInfo();
		clone.setName(name);
		clone.setItemType(itemType);
		clone.setClassValue(classValue);
		clone.setResourceType(resourceType);
		
		return clone;
	}	
}
