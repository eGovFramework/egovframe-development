package egovframework.bdev.imp.batch.wizards.joblauncher.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Launcher에서 Ref datasource를 사용할 경우 datasourceBeanID의 이름을 제공하는 LabelProvider
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.09.18
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.09.18	배치개발환경 개발팀  최초생성
 *
 * 
 * </pre>
 */
public class RefDatasourceBeanIDLabelProvider implements ITableLabelProvider {

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

	/** popup dialog에 datasourceBeanID 및 파일경로를 return한다. */
	public String getColumnText(Object element, int columnIndex) {

		if(element instanceof String){

			String tableContent = (String) element;
			String[] setTableContent = tableContent.split(">");
			
			switch(columnIndex){
			case 0:
				return setTableContent[0];
			case 1:
				return setTableContent[1];
			}
		}

		return "Unknown Value";
	}

}
