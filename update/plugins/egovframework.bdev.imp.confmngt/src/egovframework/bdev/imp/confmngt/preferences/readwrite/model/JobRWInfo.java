package egovframework.bdev.imp.confmngt.preferences.readwrite.model;

import egovframework.bdev.imp.confmngt.preferences.com.model.Info;

/**
 * JobRWContents에 사용되는 VO객체의 부모객체
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
public abstract class JobRWInfo extends Info {
	
	final static public String FILE = "File";
	final static public String DB = "DB";
	
	/**
	 * name의 값을 가져온다
	 *
	 * @return the name
	 */
	abstract public String getName();

	/**
	 * name의 값을 설정한다.
	 *
	 * @param name the name to set
	 */
	abstract public void setName(String name);

	/**
	 * itemType의 값을 가져온다
	 *
	 * @return the itemType
	 */
	abstract public String getItemType();

	/**
	 * itemType의 값을 설정한다.
	 *
	 * @param itemType the itemType to set
	 */
	abstract public void setItemType(String itemType);

	/**
	 * classValue의 값을 가져온다
	 *
	 * @return the classValue
	 */
	abstract public String getClassValue();

	/**
	 * classValue의 값을 설정한다.
	 *
	 * @param classValue the classValue to set
	 */
	abstract public void setClassValue(String classValue);
	
	/**
	 * resourceType의 값을 가져온다
	 *
	 * @return the resourceType
	 */
	abstract public String getResourceType();

	/**
	 * resourceType의 값을 설정한다.
	 *
	 * @param resourceType the resourceType to set
	 */
	abstract public void setResourceType(String resourceType);
	
	/**
	 * resourceDetailType의 값을 가져온다
	 *
	 * @return the resourceDetailType
	 */
	abstract public String getResourceDetailType();

	/**
	 * resourceDetailType의 값을 설정한다.
	 *
	 * @param resourceDetailType the resourceDetailType to set
	 */
	abstract public void setResourceDetailType(String resourceDetailType);
}
