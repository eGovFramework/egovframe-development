package egovframework.bdev.imp.confmngt.preferences.readwrite;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRWMap;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
/**
 * Batch Writer Info를 등록하는 Dialog를 생성
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
public class JobWriterDialog extends JobRWDialog{

	/**
	 * JobWriterDialog의 생성자
	 * 
	 * @param parent
	 * @param isAddButton
	 * @param jobRWInfo
	 * @param existingIdList
	 */
	public JobWriterDialog(Shell parent, boolean isAddButton,
			JobRWInfo jobRWInfo, List<String> existingIdList) {
		super(parent, isAddButton, jobRWInfo, existingIdList);
		
		if (isAddButton) {
			setTitle(BConfMngtMessages.JobWriterDialog_ADD_TITLE);
		} else {
			setTitle(BConfMngtMessages.JobWriterDialog_EDIT_TITLE);
		}
		
		setDescription(BConfMngtMessages.JobWriterDialog_DESCRIPTION);
	}
	
	@Override
	protected DefaultJobRWMap getDefaultJobType() {
		DefaultJobRWMap defaultJobType = new DefaultJobRWMap();
		defaultJobType.resetDefaultWriterType();
		
		return defaultJobType;
	}
	
	@Override
	protected Composite createNote(Composite control) {
		Composite noteControl = super.createNote(control);
		
		Label contentLabel = new Label(noteControl, SWT.None);
		contentLabel.setText(BConfMngtMessages.JobWriterDialog_NOTE_CONTENTS_LABEL);
		
		return control;
	}

}
