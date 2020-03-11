package egovframework.bdev.imp.batch.wizards.jobcreation.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.NextVo;
/**
 * Next On, Next To TableViewer의 LabelProvider
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
public class NextTableViewerProvider implements ITableLabelProvider {

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

	/** Next On, Next To의 TableViewer에 해당하는 Column index에 맞는 값을 return한다.  */
	public String getColumnText(Object element, int columnIndex) {
		if(!(element instanceof NextVo)){
			return "NOT NextVo Type";
		}
		NextVo data = (NextVo)element;
		switch(columnIndex){
		case 0:
			return data.getNextOn();
		case 1:
			return data.getNextTo();
		default :
			return 	"Unkown Value";
		}
	}

}
