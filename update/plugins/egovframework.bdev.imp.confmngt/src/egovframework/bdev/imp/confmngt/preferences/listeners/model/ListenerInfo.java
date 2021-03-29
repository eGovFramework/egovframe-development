package egovframework.bdev.imp.confmngt.preferences.listeners.model;

import egovframework.bdev.imp.confmngt.preferences.com.model.Info;

/**
 * ListenerContents에 사용되는 VO객체들(Job / Step / Chunk Listener VO)의 부모객체
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
abstract public class ListenerInfo extends Info {
	
	/**
	 * listenerType의 값을 가져온다
	 *
	 * @return the listenerType
	 */
	abstract public String getListenerType();

	/**
	 * listenerType의 값을 설정한다.
	 *
	 * @param listenerType the listenerType to set
	 */
	abstract public void setListenerType(String listenerType);

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
	
}
