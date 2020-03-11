package egovframework.bdev.imp.confmngt.preferences.listeners.model;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
/**
 * Listener Info의 Label Provider를 관리하는 클래스
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
public class ListenerProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		ListenerInfo listenerVo = (ListenerInfo) element;
		
		switch(columnIndex){
		case 0:
			return listenerVo.getName();
		case 1:
			return listenerVo.getClassValue();
		case 2:
			return listenerVo.getListenerType();
		default :
			return "Unknown Value";
		}
	}
}
