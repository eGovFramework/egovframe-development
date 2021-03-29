package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.JobRWPreferencePage;
import egovframework.bdev.imp.confmngt.preferences.readwrite.JobRWTabContents;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;

/**
 * <pre>
 * BatchJobCreationCustomizePage에서 
 * Job Reader TableViewer에 추가, 수정할때 여는 Dialog
 * </pre>
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.10.10	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class JobReaderDialog extends JobRWDialog {

	/**
	 * JobReaderDialog 생성자
	 * 
	 * @param parent
	 * @param type
	 * @param batchJobListBeanIDList
	 * @param pageBeanIDs
	 * @param info
	 * 
	 */
	public JobReaderDialog(Shell parent,
			BatchXMLFileBeanIDList batchJobListBeanIDList,
			BatchPageBeanIDList pageBeanIDs, StepVo stepVo,
			BatchJobCreationContext context) {
		super(parent, batchJobListBeanIDList, pageBeanIDs, stepVo
				.getJobReaderInfo(), stepVo.isPartitionMode(), context, stepVo
				.getJobReaderContext(), stepVo.getJobReadersqlKeyValueVo());

		setTitle(BatchMessages.JobReaderDialog_TITLE);

		setSemiTitle(BatchMessages.JobReaderDialog_SEMI_TITLE);
		setsemiDescription(BatchMessages.JobReaderDialog_SEMI_DESCRIPTION);

		setNoteContent(BatchMessages.JobReaderDialog_NOTE_CONTENT);
	}

	@Override
	protected JobRWTabContents createJobRWContents() {
		BatchTableColumn[] columns = createColumns();

		JobRWTabContents content = new JobRWTabContents(
				"Reader", new JobReaderInfo(), JobRWPreferencePage.JOB_READER_PREFERENCE_STORE_NAME, false); //$NON-NLS-1$ //$NON-NLS-2$
		content.setTableColumnProperty(columns);

		return content;
	}

	private BatchTableColumn[] createColumns() {
		List<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(
				BatchMessages.JobReaderDialog_TABLE_COLUMN_NAME, 100));
		columns.add(new BatchTableColumn(
				BatchMessages.JobReaderDialog_TABLE_COLUMN_RESOURCE_TYPE, 90));
		columns.add(new BatchTableColumn(
				BatchMessages.JobReaderDialog_TABLE_COLUMN_READ_TYPE, 130));
		columns.add(new BatchTableColumn(
				BatchMessages.JobReaderDialog_TABLE_COLUMN_CLASS, 130));

		return columns.toArray(new BatchTableColumn[0]);
	}

	@Override
	protected String getEmptyErrorMessage() {
		return BatchMessages.JobReaderDialog_EMPTY_READER_ERROR_MESSAGE;
	}

}
