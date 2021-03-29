package egovframework.bdev.tst.wizards.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * test파일 재사용시 정보를 제공하는 LabelProvider
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.08.27
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.08.27	배치개발환경 개발팀  최초생성
 *
 * 
 * </pre>
 */
public class JobTestFilesListLabelProvider  implements ITableLabelProvider {

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
	
	/**
	 *  Wizard의 test파일정보 List를 return한다.
	 *  
	 */
	public String getColumnText(Object element, int columnIndex) {
		
		if((element instanceof String)){
			String[] realTemp = ((String)element).split("::");
			for(int i =0; i < realTemp.length; i++){
				if(columnIndex == i){
					return realTemp[i];
				}
			}
		}
		return "Unknown Value";
	}
}
