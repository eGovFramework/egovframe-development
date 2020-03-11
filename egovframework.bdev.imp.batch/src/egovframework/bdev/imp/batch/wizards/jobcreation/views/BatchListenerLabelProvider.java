package egovframework.bdev.imp.batch.wizards.jobcreation.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
/**
 * Job, Step, Chunk Listener tableViewer의 Label Provider
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
public class BatchListenerLabelProvider implements ITableLabelProvider {

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/** Job, Step, Chunk Listener tableViewer의 column index에 맞게 값을 return한다. */
	public String getColumnText(Object element, int columnIndex) {

		if(element instanceof ListenerInfo){
			
			switch(columnIndex){
			case 0:
				return ((ListenerInfo) element).getName();
			case 1:
				return ((ListenerInfo) element).getClassValue();
			}
		}
			
		return "Unknown Value";
	}

}
