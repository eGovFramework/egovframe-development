package egovframework.bdev.imp.confmngt.preferences.listeners;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.com.BatchPreferencePage;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ChunkListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.JobListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.StepListenerInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;

/**
 * Batch Listener 정보를 관리하는 클래스
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class ListenerPreferencePage extends BatchPreferencePage {

	/** Job Listener Preference Store Name */
	final public static String JOB_LISTENER_PREFERENCE_STORE_NAME = "Job";

	/** Step Listener Preference Store Name */
	final public static String STEP_LISTENER_PREFERENCE_STORE_NAME = "Step";

	/** Chunk Listener Preference Store Name */
	final public static String CHUNK_LISTENER_PREFERENCE_STORE_NAME = "Chunk";

	/** Job Listener Preference Name */
	final public static String JOB = "Job";
	
	/** Step Listener Preference Name */
	final public static String STEP = "Step";
	
	/** Chunk Listener Preference Name */
	final public static String CHUNK = "Chunk";

	/** Job Listener Tab Page */
	private ListenerTabContents job;

	/** Step Listener Tab Page */
	private ListenerTabContents step;

	/** Chunk Listener Tab Page */
	private ListenerTabContents chunk;

	/** ListenerPreferencePage의 생성자 */
	public ListenerPreferencePage() {
		setDescription(BConfMngtMessages.ListenerPreferencePage_DESCRIPTION);
	}

	@Override
	public void createInnerPart(Composite parent) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 500;

		final TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayoutData(gData);

		BatchTableColumn[] columns = createColumns();

		job = new ListenerTabContents(JOB, new JobListenerInfo(),
				JOB_LISTENER_PREFERENCE_STORE_NAME, false);
		step = new ListenerTabContents(STEP, new StepListenerInfo(),
				STEP_LISTENER_PREFERENCE_STORE_NAME, false);
		chunk = new ListenerTabContents(CHUNK, new ChunkListenerInfo(),
				CHUNK_LISTENER_PREFERENCE_STORE_NAME, false);

		job.setTableColumnProperty(columns);
		step.setTableColumnProperty(columns);
		chunk.setTableColumnProperty(columns);

		job.createTabContents(folder);
		step.createTabContents(folder);
		chunk.createTabContents(folder);

		createControlButtons(parent);

		setButtonListener(job);

		addEditAndRemoveButtonListener(job.getTableViewer());
		addEditAndRemoveButtonListener(step.getTableViewer());
		addEditAndRemoveButtonListener(chunk.getTableViewer());

		folder.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				switch (folder.getSelectionIndex()) {
				case 0:
					setButtonListener(job);
					getControl().getParent().layout(true, true);
					break;
				case 1:
					setButtonListener(step);
					getControl().getParent().layout(true, true);
					break;
				case 2:
					setButtonListener(chunk);
					getControl().getParent().layout(true, true);
					break;
				}
			}
		});

		folder.notifyListeners(SWT.Selection, new Event());
	}

	/**
	 * Listener Table의 Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns() {
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(
				BConfMngtMessages.ListenerPreferencePage_TABLE_COLUMN_NAME, 175));
		columns.add(new BatchTableColumn(
				BConfMngtMessages.ListenerPreferencePage_TABLE_COLUMN_CLASS,
				150, SWT.LEFT));
		columns.add(new BatchTableColumn(
				BConfMngtMessages.ListenerPreferencePage_TABLE_COLUMN_LISTENER_TYPE,
				122));

		return columns.toArray(new BatchTableColumn[0]);
	}

	@Override
	public boolean performOk() {

		job.saveData();
		step.saveData();
		chunk.saveData();

		return super.performOk();
	}
}
