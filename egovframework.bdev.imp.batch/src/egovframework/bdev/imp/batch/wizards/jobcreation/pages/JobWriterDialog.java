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
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;

/**
 * <pre>
 * BatchJobCreationCustomizePage에서 
 * Job Writer TableViewer에 추가, 수정할때 여는 Dialog
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
public class JobWriterDialog extends JobRWDialog {

	/**
	 * JobWriterDialog 생성자
	 * 
	 * @param parent
	 * @param batchJobListBeanIDList
	 * @param pageBeanIDs
	 * @param info
	 * @param isPartitionType
	 * @param context
	 * @param detailContext
	 * 
	 */
	public JobWriterDialog(Shell parent,
			BatchXMLFileBeanIDList batchJobListBeanIDList,
			BatchPageBeanIDList pageBeanIDs, StepVo stepVo,
			BatchJobCreationContext context) {
		super(parent, batchJobListBeanIDList, pageBeanIDs, stepVo
				.getJobWriterInfo(), stepVo.isPartitionMode(), context, stepVo
				.getJobWriterContext(), stepVo.getJobReadersqlKeyValueVo());

		setTitle(BatchMessages.JobWriterDialog_TITLE);

		setSemiTitle(BatchMessages.JobWriterDialog_SEMI_TITLE);
		setsemiDescription(BatchMessages.JobWriterDialog_DESCRIPTION);

		setNoteContent(BatchMessages.JobWriterDialog_NOTE_CONTENTS);
	}

	@Override
	protected JobRWTabContents createJobRWContents() {
		BatchTableColumn[] columns = createColumns();

		JobRWTabContents content = new JobRWTabContents("Writer",
				new JobWriterInfo(), JobRWPreferencePage.JOB_WRITER_PREFERENCE_STORE_NAME, false);
		content.setTableColumnProperty(columns);

		return content;
	}

	/**
	 * Table Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns() {
		List<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(
				BatchMessages.JobWriterDialog_COLUMN_NAME, 100));
		columns.add(new BatchTableColumn(
				BatchMessages.JobWriterDialog_COLUMN_RESOURCE_TYPE, 90));
		columns.add(new BatchTableColumn(
				BatchMessages.JobWriterDialog_COLUMN_WRITER_TYPE, 130));
		columns.add(new BatchTableColumn(
				BatchMessages.JobWriterDialog_COLUMN_CLASS, 130));

		return columns.toArray(new BatchTableColumn[0]);
	}

	@Override
	protected String getEmptyErrorMessage() {
		return BatchMessages.JobWriterDialog_NO_SELECT_WRITER_ERROR;
	}

}
