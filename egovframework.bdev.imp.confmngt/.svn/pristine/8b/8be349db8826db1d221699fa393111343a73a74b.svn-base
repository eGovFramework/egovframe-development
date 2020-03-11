package egovframework.bdev.imp.confmngt.preferences.parameter;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.com.BatchPreferencePage;
import egovframework.dev.imp.core.utils.BatchTableColumn;
/**
 * Batch JobParameter 정보를 관리하는 클래스
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
public class JobParameterPreferencePage extends BatchPreferencePage {
	
	/** Job Parameter Preference Store Name */
	final static public String JOB_PARAMETER_PREFERENCE_STORE_NAME = "JobParameter";
	
	/** Job Parameter Page*/
	private JobParameterContents jobPage;

	/** JobParameterPreferencePage의 생성자 */
	public JobParameterPreferencePage() {
		setDescription(BConfMngtMessages.JobParameterPreferencePage_DESCRIPTION);
	}
	
	public void createInnerPart(Composite parent) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 500;
		
		Composite tableViewerControl = new Composite(parent, SWT.None);
		tableViewerControl.setLayout(new GridLayout());
		tableViewerControl.setLayoutData(gData);
		
		BatchTableColumn[] columns = createColumns();
		
		jobPage = new JobParameterContents("Job Parameter", JOB_PARAMETER_PREFERENCE_STORE_NAME, false); //$NON-NLS-1$ //$NON-NLS-2$
		jobPage.setTableColumnProperty(columns);
		jobPage.createTableViewerContents(tableViewerControl);
		
		createControlButtons(parent);
		
		setButtonListener(jobPage);
		addEditAndRemoveButtonListener(jobPage.getTableViewer());
	}
	
	/**
	 * Job Parameter TableViewer의 Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns(){
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BConfMngtMessages.JobParameterPreferencePage_TABLE_COLUMN_PARAMETER_NAME, 184));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobParameterPreferencePage_TABLE_COLUMN_VALUE, 130));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobParameterPreferencePage_TABLE_COLUMN_DATA_TYPE, 151));
		
		return columns.toArray(new BatchTableColumn[0]);
	}
	
	@Override
	public boolean performOk() {
		
		jobPage.saveData();
		
		return super.performOk();
	}
}
