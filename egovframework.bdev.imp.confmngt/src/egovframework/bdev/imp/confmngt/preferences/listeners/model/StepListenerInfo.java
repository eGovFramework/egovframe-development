package egovframework.bdev.imp.confmngt.preferences.listeners.model;
/**
 * StepListenerContents에 사용되는 VO객체
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
public class StepListenerInfo extends ListenerInfo {
	
	/** Step Listener의 Listener Type */
	private String listenerType;
	
	/** Step Listener의 Name */
	private String name;
	
	/** Step Listener의 Class Value */
	private String classValue;
	
	/**
	 * listenerType의 값을 가져온다
	 *
	 * @return the listenerType
	 */
	public String getListenerType() {
		return listenerType;
	}

	/**
	 * listenerType의 값을 설정한다.
	 *
	 * @param listenerType the listenerType to set
	 */
	public void setListenerType(String listenerType) {
		this.listenerType = listenerType;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * classValue의 값을 가져온다
	 *
	 * @return the classValue
	 */
	public String getClassValue() {
		return classValue;
	}

	/**
	 * classValue의 값을 설정한다.
	 *
	 * @param classValue the classValue to set
	 */
	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}

	public StepListenerInfo(String listenerType, String name, String classValue){
		this.listenerType = listenerType;
		this.name = name;
		this.classValue = classValue;
	}

	/** StepListenerInfo의 생성자 */
	public StepListenerInfo() {
	}
	
	/** 현재 StepListenerInfo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public StepListenerInfo clone(){
		StepListenerInfo clone = new StepListenerInfo();
		clone.setName(name);
		clone.setClassValue(classValue);
		clone.setListenerType(listenerType);
		
		return clone;
	}

}
