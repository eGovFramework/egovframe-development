package egovframework.bdev.imp.confmngt.preferences.com;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Batch 정보들의 상위 구조를 관리하는 클래스
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2012.07.02	배치개발환경 개발팀	 최초 생성
 *
 * 
 * </pre>
 */
public class EmptybatchConfPreperencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public EmptybatchConfPreperencePage() {
	}

	public EmptybatchConfPreperencePage(String title) {
		super(title);
	}

	public EmptybatchConfPreperencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void init(IWorkbench workbench) {
		//init
		
	}

	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();

		String description = "Expand the tree to edit preferences for a specific eGovFrame Batch feature.";  

		Text text = new Text(parent, SWT.READ_ONLY); 
		text.setBackground(parent.getBackground()); 
		text.setText(description); 
		
		return parent;
	}
}
