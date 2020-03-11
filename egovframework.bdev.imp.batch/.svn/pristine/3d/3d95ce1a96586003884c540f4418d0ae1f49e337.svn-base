package egovframework.bdev.imp.batch.wizards.joblauncher.pages;

import org.eclipse.jface.viewers.ISelection;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.BatchJobSelectProjectPage;
import egovframework.bdev.imp.batch.wizards.joblauncher.model.BatchJobLauncherContext;

/**
 * EgovNewBatchJobLauncherWizard에서 Project를 선택하는 wizard page
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.09.18	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class BatchJoblauncherSelectProjectPage extends
		BatchJobSelectProjectPage {

	/**
	 * BatchJoblauncherSelectProjectPage 생성자
	 * 
	 * @param pageName
	 * @param context
	 * @param selection
	 *
	 */
	public BatchJoblauncherSelectProjectPage(String pageName,
			BatchJobLauncherContext context, ISelection selection) {
		super(pageName, context, selection);
		setTitle(BatchMessages.BatchJoblauncherSelectProjectPage_TITLE);
		setDescription(BatchMessages.BatchJoblauncherSelectProjectPage_DESCRIPTION);
		setDefaultFileName("newJobLauncher.xml");
		
		String noteContent = BatchMessages.BatchJoblauncherSelectProjectPage_NOTE_CONTENTS;
		setNoteContent(noteContent);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible){
			BatchJobLauncherContext context = (BatchJobLauncherContext)getContext();
			context.clearValues();
		}
	}

}
