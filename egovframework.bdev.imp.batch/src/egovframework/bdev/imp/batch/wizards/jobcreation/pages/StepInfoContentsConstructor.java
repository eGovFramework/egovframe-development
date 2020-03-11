package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.SharedValueVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.views.BatchListenerLabelProvider;
import egovframework.bdev.imp.batch.wizards.jobcreation.views.SharedValuesTableProvider;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ChunkListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.StepListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * BatchJobCreationCustomizePage에서 Step Info 화면을 그림
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.09.18
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
public class StepInfoContentsConstructor extends InfoContentsConstructor {

	/** StepInfo 화면의 입력값을 가지고 다니는 임시 StepVo */
	private StepVo tempStepVo = null;

	/** grid-size 입력값을 String 상태로 입력 */
	private String gridSizeString = null;

	/** commit-interval 입력값을 String 상태로 입력 */
	private String commitIntervalString = null;

	/**
	 * <pre>
	 * [Step Info]
	 * Step info의 partition Type의 Control
	 * 
	 * </pre>
	 */
	private Composite partitionTypeControl = null;

	/** Next(Step)을 Enable/Disable하게 설정하는 CheckBox Button */
	private Button activeNext = null;

	/** Job Reader Text */
	private Text readerText = null;

	/** Job Writer Text */
	private Text writerText = null;

	/** Wizard의 Context */
	private BatchJobCreationContext context = null;

	/** SharedValueVo를 보여주는 TableViewer */
	private TableViewer sharedValueTableViewer = null;

	/** Step/Decision List에 있는 VO Name 목록 */
	String[] stepAndDecisionNameList = null;

	/**
	 * StepInfoContentsConstructor 생성자
	 * 
	 * @param currentPage
	 * @param infoControl
	 * @param applyButton
	 * @param batchXMLFileBeanIDList
	 * @param projectBeanIDList
	 * @param context
	 * 
	 */
	public StepInfoContentsConstructor(WizardPage currentPage, Composite infoControl, Button applyButton, BatchXMLFileBeanIDList batchXMLFileBeanIDList,
			List<String> projectBeanIDList, BatchJobCreationContext context) {
		this.currentPage = currentPage;
		this.infoControl = infoControl;
		this.applyButton = applyButton;
		this.batchXMLFileBeanIDList = batchXMLFileBeanIDList;
		this.selectedProject = context.getProject();
		this.projectBeanIDList = projectBeanIDList;
		this.context = context;
	}

	/**
	 * Step info 생성
	 * 
	 * @param stepVo
	 */
	public void createStepInfoContents(JobVo jobVo, StepVo stepVo) {

		tempStepVo = new StepVo();
		tempStepVo.copyValues(stepVo);
		tempStepVo.setJobName(jobVo.getJobName());

		gridSizeString = ConvertIntegerToString(tempStepVo.getGridSize());
		commitIntervalString = ConvertIntegerToString(tempStepVo.getCommitInterval());

		invalidBatchXMLFileBeanIDList = getBatchJobsInvalidBeanList();
		stepAndDecisionNameList = getStepAndDecisionNameListWithoutThisStepVoName(jobVo);

		createJobIDField(jobVo.getJobName());

		createStepTypeControl();

		createStepIDAndNext();

		createStepTypeOptionControl();

		createJobRWAndCommitIntervalGroup();

		createAdvancedControl(jobVo);

		if (!NullUtil.isNull(stepVo)) {
			validation().handleEvent(null);
		};
	}

	/**
	 * Integer 타입을 String 타입으로 변환
	 * 
	 * @param integer
	 * @return
	 */
	private String ConvertIntegerToString(Integer integer) {
		if (NullUtil.isNull(integer)) {
			return null;
		} else {
			return integer.toString();
		}
	}

	/**
	 * Item Reader / Writer, Commit-Interval의 Control 생성
	 * 
	 * @param control
	 */
	private void createJobRWAndCommitIntervalGroup() {
		Group itemModeGroup = new Group(infoControl, SWT.NONE);
		itemModeGroup.setLayout(new GridLayout());
		itemModeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		itemModeGroup.setText(BatchMessages.StepInfoContentsConstructor_ITEM_CONFIGURATION_GROUP);
		removeMarginOfGridLayout(itemModeGroup);

		createJobRWControl(itemModeGroup);

		creatCommitIntervalControl(itemModeGroup);
	}

	/**
	 * Job Reader / Writer Control 생성
	 * 
	 * @param control
	 */
	private void createJobRWControl(Composite control) {
		Composite readerWriterControl = new Composite(control, SWT.None);
		readerWriterControl.setLayout(new GridLayout(3, false));
		readerWriterControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(readerWriterControl);

		Label readerLabel = new Label(readerWriterControl, SWT.None);
		readerLabel.setText(BatchMessages.StepInfoContentsConstructor_READER_LABEL);

		readerText = createJobReaderText(readerWriterControl);
		readerText.addListener(SWT.Modify, validation());

		Label writerLabel = new Label(readerWriterControl, SWT.None);
		writerLabel.setText(BatchMessages.StepInfoContentsConstructor_WRITER_LABEL);

		writerText = createJobWriterText(readerWriterControl);
		writerText.addListener(SWT.Modify, validation());
	}

	/**
	 * Job Reader의 Text와 Button 생성
	 * 
	 * @param subControl
	 * @return
	 */
	private Text createJobReaderText(Composite subControl) {
		final Text text = new Text(subControl, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setEnabled(false);

		String jobReaderFieldText = getJobReaderFullName(tempStepVo);
		text.setText(returnJobRWTextDefaultStringIfNull(jobReaderFieldText));

		Button button = new Button(subControl, SWT.PUSH);
		button.setText(BatchMessages.StepInfoContentsConstructor_ADD_BUTTON);
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setStepPageBeanID(pageBeanIDs);
				pageBeanIDs.removeBeanID(text.getText());

				JobRWDialog dialog = new JobReaderDialog(infoControl.getShell(), invalidBatchXMLFileBeanIDList, pageBeanIDs, tempStepVo, context);
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					pageBeanIDs.removeBeanID(text.getText());
					JobReaderInfo info = (JobReaderInfo) dialog.getInfo();
					tempStepVo.setJobReaderInfo(info);
					text.setText(getJobReaderFullName(tempStepVo));
					pageBeanIDs.addBeanID(info.getName());
					tempStepVo.setJobReaderContext(dialog.getDetailContext());

					tempStepVo.setJobReadersqlKeyValueVo(dialog.getSqlKeyValueVo());
				}
			}
		});

		return text;
	}

	/**
	 * Item Writer의 Text와 Button 생성
	 * 
	 * @param subControl
	 * @return
	 */
	private Text createJobWriterText(Composite subControl) {
		final Text text = new Text(subControl, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setEnabled(false);

		String jobWriterFieldText = getJobWriterFullName(tempStepVo);
		text.setText(returnJobRWTextDefaultStringIfNull(jobWriterFieldText));

		Button button = new Button(subControl, SWT.PUSH);
		button.setText(BatchMessages.StepInfoContentsConstructor_ADD_BUTTON);
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setStepPageBeanID(pageBeanIDs);
				pageBeanIDs.removeBeanID(text.getText());

				JobRWDialog dialog = new JobWriterDialog(infoControl.getShell(), invalidBatchXMLFileBeanIDList, pageBeanIDs, tempStepVo, context);
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					pageBeanIDs.removeBeanID(text.getText());
					JobWriterInfo info = (JobWriterInfo) dialog.getInfo();
					tempStepVo.setJobWriterInfo(info);
					text.setText(getJobWriterFullName(tempStepVo));

					tempStepVo.setJobWriterContext(dialog.getDetailContext());
				}
			}
		});

		return text;
	}

	/**
	 * Job RW에 입력값이 없으면 기본 String을 리턴
	 * 
	 * @param string
	 * @return
	 */
	private String returnJobRWTextDefaultStringIfNull(String string) {
		if (NullUtil.isEmpty(string)) {
			return BatchMessages.StepInfoContentsConstructor_NO_JOB_RW_MESSAGE;
		} else {
			return string;
		}
	}

	/**
	 * Job Reader의 Full Name 생성
	 * 
	 * @param stepVo
	 * @return
	 */
	private String getJobReaderFullName(StepVo stepVo) {
		return getJobRWFullName(stepVo.getJobName(), stepVo.getName(), stepVo.getJobReaderInfo());
	}

	/**
	 * Job Writer의 Full Name 생성
	 * 
	 * @param stepVo
	 * @return
	 */
	private String getJobWriterFullName(StepVo stepVo) {
		return getJobRWFullName(stepVo.getJobName(), stepVo.getName(), stepVo.getJobWriterInfo());
	}

	/**
	 * Job Reader / Writer의 Full Name Sting 생성
	 * 
	 * @param jobID
	 * @param stepID
	 * @param jobRW
	 * @return
	 */
	private String getJobRWFullName(String jobID, String stepID, JobRWInfo jobRW) {
		if (!NullUtil.isNull(jobRW)) {
			return jobID + "." + stepID + "." + jobRW.getName(); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return null;
		}
	}

	/**
	 * Commit Interval Control 생성
	 * 
	 * @param control
	 */
	private void creatCommitIntervalControl(Composite control) {
		Composite commitControl = new Composite(control, SWT.None);
		commitControl.setLayout(new GridLayout(2, false));
		commitControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(commitControl);

		Label commitIntervalLabel = new Label(commitControl, SWT.None);
		commitIntervalLabel.setText(BatchMessages.StepInfoContentsConstructor_COMMIT_INTERVAL_LABEL);

		Integer commitinterval = tempStepVo.getCommitInterval();

		final Text commitIntervalField = new Text(commitControl, SWT.BORDER);
		commitIntervalField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		commitIntervalField.setText(returnEmptyStringIfNullOrConvertToString(commitinterval));
		commitIntervalField.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					commitIntervalString = commitIntervalField.getText();
					Integer value = Integer.parseInt(commitIntervalString);
					tempStepVo.setCommitInterval(value);
				} catch (Exception e) {
				}
			}
		});
		commitIntervalField.addListener(SWT.Modify, validation());
	}

	/**
	 * Integer가 Null 이면 "" 을, Null 이 아니면 String으로 변환해서 리턴
	 * 
	 * @param integer
	 * @return
	 */
	public String returnEmptyStringIfNullOrConvertToString(Integer integer) {
		if (NullUtil.isNull(integer)) {
			return ""; //$NON-NLS-1$
		} else {
			return integer.toString();
		}
	}

	/**
	 * Advanced Button 클릭시 보여지는 Control(Step, Chunk, Shared Values TableViewer)
	 * 생성
	 * 
	 * @param control
	 */
	private void createAdvancedControl(JobVo jobVo) {
		final Button advanced = new Button(infoControl, SWT.PUSH);
		advanced.setText(BatchMessages.StepInfoContentsConstructor_DESELECT_ADVANCED_BUTTON);

		final TabFolder listenerAndSV = new TabFolder(infoControl, SWT.None);
		listenerAndSV.setLayout(new GridLayout());
		listenerAndSV.setLayoutData(new GridData(GridData.FILL_BOTH));
		listenerAndSV.setVisible(false);
		removeMarginOfGridLayout(listenerAndSV);

		createStepListenerTabItem(listenerAndSV);

		createChunkListenerTabItem(listenerAndSV);

		createSharedValuesTabItem(listenerAndSV, jobVo);

		advanced.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (!listenerAndSV.isVisible()) {
					advanced.setText(BatchMessages.StepInfoContentsConstructor_SELECT_ADVANCED_BUTTON);
					listenerAndSV.setVisible(true);
				} else {
					advanced.setText(BatchMessages.StepInfoContentsConstructor_DESELECT_ADVANCED_BUTTON);
					listenerAndSV.setVisible(false);
				}
			}
		});
	}

	/**
	 * Step Listener Tab 생성
	 * 
	 * @param subControl
	 * @return
	 */
	private TableViewer createStepListenerTabItem(TabFolder subControl) {
		String tabItemText = BatchMessages.StepInfoContentsConstructor_STEP_LISTENER_TAB;
		ListenerInfo[] listenerInput = tempStepVo.getStepListenerInfoList();

		TabItem tabItem = new TabItem(subControl, SWT.None);
		tabItem.setText(tabItemText);

		Composite listenerControl = new Composite(subControl, SWT.None);
		listenerControl.setLayout(new GridLayout(2, false));
		listenerControl.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableViewer tableViewer = createListenerTableViewer(listenerControl, listenerInput);

		createStepListenerTableButtons(listenerControl, tableViewer);

		tabItem.setControl(listenerControl);

		return tableViewer;
	}

	/**
	 * Chunk Listener Tab 생성
	 * 
	 * @param subControl
	 * @return
	 */
	private TableViewer createChunkListenerTabItem(TabFolder subControl) {

		String tabItemText = BatchMessages.StepInfoContentsConstructor_CHUNK_LISTENER_TAB;
		ListenerInfo[] listenerInput = tempStepVo.getChunkListenerInfoList();

		TabItem tabItem = new TabItem(subControl, SWT.None);
		tabItem.setText(tabItemText);

		Composite listenerControl = new Composite(subControl, SWT.None);
		listenerControl.setLayout(new GridLayout(2, false));
		listenerControl.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableViewer tableViewer = createListenerTableViewer(listenerControl, listenerInput);

		createChunkListenerTableButtons(listenerControl, tableViewer);

		tabItem.setControl(listenerControl);

		return tableViewer;
	}

	/**
	 * Step, Chunk의 Listener Table 생성
	 * 
	 * @param control
	 * @param listenerInput
	 * @return
	 */
	private TableViewer createListenerTableViewer(Composite control, ListenerInfo[] listenerInput) {
		final TableViewer tableViewer = new TableViewer(control, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = tableViewer.getTable();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		BatchTableColumn[] columns = createListenerTableColumns();

		for (int i = 0; i < columns.length; i++) {
			columns[i].setColumnToTable(table);
		}

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new BatchListenerLabelProvider());
		tableViewer.setInput(listenerInput);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		return tableViewer;
	}

	/**
	 * Listener Table의 컬럼 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createListenerTableColumns() {
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BatchMessages.StepInfoContentsConstructor_LISTENER_TABLE_COLUMN_NAME, 180, SWT.LEFT));
		columns.add(new BatchTableColumn(BatchMessages.StepInfoContentsConstructor_LISTENER_TABLE_COLUMN_CLASS, 180, SWT.LEFT));

		return columns.toArray(new BatchTableColumn[0]);
	}

	/**
	 * Chunk Listener Table의 Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createChunkListenerTableButtons(Composite control, TableViewer tableViewer) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData());

		Button addButton = new Button(buttonControl, SWT.PUSH);
		addButton.setText(BatchMessages.StepInfoContentsConstructor_ADD_BUTTON);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Button removeButton = new Button(buttonControl, SWT.PUSH);
		removeButton.setText(BatchMessages.StepInfoContentsConstructor_REMOVE_BUTTON);
		removeButton.setEnabled(false);

		tableViewer.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				removeButton.setEnabled(true);
			}
		});

		addButton.addListener(SWT.Selection, chunkListenerTableListener(tableViewer, removeButton));
		removeButton.addListener(SWT.Selection, chunkListenerRemoveButtonListener(tableViewer, removeButton));
	}

	/**
	 * Step Listener Table의 Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createStepListenerTableButtons(Composite control, TableViewer tableViewer) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData());

		Button addButton = new Button(buttonControl, SWT.PUSH);
		addButton.setText(BatchMessages.StepInfoContentsConstructor_ADD_BUTTON);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Button removeButton = new Button(buttonControl, SWT.PUSH);
		removeButton.setText(BatchMessages.StepInfoContentsConstructor_REMOVE_BUTTON);
		removeButton.setEnabled(false);
		removeButton.addListener(SWT.Selection, stepListenerRemoveButtonListener(tableViewer, removeButton));

		tableViewer.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				removeButton.setEnabled(true);
			}
		});

		addButton.addListener(SWT.Selection, stepListenerTableListener(tableViewer, removeButton));
	}

	/**
	 * Step Listener Table의 RemoveButton Listener 생성
	 * 
	 * @param tableViewer
	 * @param removeButton
	 * @return
	 */
	private Listener stepListenerRemoveButtonListener(final TableViewer tableViewer, final Button removeButton) {
		return new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = getSelection(tableViewer);

				if (!NullUtil.isNull(selection)) {
					Object[] selections = selection.toArray();

					tableViewer.remove(selections);
					removeButton.setEnabled(false);

					TableItem[] items = tableViewer.getTable().getItems();

					StepListenerInfo[] stepData = new StepListenerInfo[items.length];
					if (!NullUtil.isEmpty(items)) {
						for (int i = 0; i < items.length; i++) {
							stepData[i] = (StepListenerInfo) items[i].getData();
						}
					}
					tempStepVo.setStepListenerInfoList(stepData);
				}
			}
		};
	}

	/**
	 * Chunk Listener Table의 Remove Button Listener 생성
	 * 
	 * @param tableViewer
	 * @param removeButton
	 * @return
	 */
	private Listener chunkListenerRemoveButtonListener(final TableViewer tableViewer, final Button removeButton) {
		return new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = getSelection(tableViewer);

				if (!NullUtil.isNull(selection)) {
					Object[] selections = selection.toArray();

					tableViewer.remove(selections);
					removeButton.setEnabled(false);

					TableItem[] items = tableViewer.getTable().getItems();

					ChunkListenerInfo[] chunkData = new ChunkListenerInfo[items.length];
					if (!NullUtil.isEmpty(items)) {
						for (int i = 0; i < items.length; i++) {
							chunkData[i] = (ChunkListenerInfo) items[i].getData();
						}
					}
					tempStepVo.setChunkListenerInfoList(chunkData);
				}
			}
		};
	}

	/**
	 * 해당 TableViewer의 Selection을 가져온다.
	 * 
	 * @param tableViewer
	 * @return
	 */
	private IStructuredSelection getSelection(TableViewer tableViewer) {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return null;
		} else {
			return selection;
		}
	}

	/**
	 * Step Listener Table의 Listener 생성
	 * 
	 * @param tableviewer
	 * @param removeButton
	 * @return
	 */
	private Listener stepListenerTableListener(final TableViewer tableviewer, final Button removeButton) {
		return new Listener() {
			public void handleEvent(Event event) {
				TableItem[] items = tableviewer.getTable().getItems();
				String[] listenerNames = getListenerNameFromTableItem(items);

				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setStepPageBeanID(pageBeanIDs);
				pageBeanIDs.removeBeanID(listenerNames);

				ListenerDialog dialog = new StepListenerDialog(infoControl.getShell(), listenerNames, invalidBatchXMLFileBeanIDList, pageBeanIDs);
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					ListenerInfo[] data = dialog.getInfo();
					tableviewer.setInput(data);

					StepListenerInfo[] stepData = new StepListenerInfo[data.length];
					for (int i = 0; i < data.length; i++) {
						stepData[i] = (StepListenerInfo) data[i];
					}
					tempStepVo.setStepListenerInfoList(stepData);

					removeButton.setEnabled(false);
				}
			}
		};
	}

	/**
	 * Chunk Listener Table의 Listener 생성
	 * 
	 * @param tableviewer
	 * @param removeButton
	 * @return
	 */
	private Listener chunkListenerTableListener(final TableViewer tableviewer, final Button removeButton) {
		return new Listener() {
			public void handleEvent(Event event) {
				TableItem[] items = tableviewer.getTable().getItems();
				String[] listenerNames = getListenerNameFromTableItem(items);

				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setStepPageBeanID(pageBeanIDs);
				pageBeanIDs.removeBeanID(listenerNames);

				ListenerDialog dialog = new ChunkListenerDialog(infoControl.getShell(), listenerNames, invalidBatchXMLFileBeanIDList, pageBeanIDs);
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					ListenerInfo[] data = dialog.getInfo();
					tableviewer.setInput(data);

					ChunkListenerInfo[] chunkData = new ChunkListenerInfo[data.length];
					for (int i = 0; i < data.length; i++) {
						chunkData[i] = (ChunkListenerInfo) data[i];
					}
					tempStepVo.setChunkListenerInfoList(chunkData);

					removeButton.setEnabled(false);
				}
			}
		};
	}

	/**
	 * <pre>
	 * Job, Step, Chunk TableViewer의 TableItem Array를
	 * ListenerInfo 타입의 Array로 변환
	 * </pre>
	 * 
	 * @param items
	 * @return
	 */
	private String[] getListenerNameFromTableItem(TableItem[] items) {
		String[] result = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			result[i] = ((ListenerInfo) items[i].getData()).getName();
		}

		return result;
	}

	/**
	 * Advanced Control의 Shared Values TableViewer생성
	 * 
	 * @param subControl
	 */
	private void createSharedValuesTabItem(TabFolder subControl, JobVo jobVo) {

		TabItem tabItem = new TabItem(subControl, SWT.None);
		tabItem.setText(BatchMessages.StepInfoContentsConstructor_SHARED_VALUES_TAB);

		Composite sharedValuesControl = new Composite(subControl, SWT.None);
		sharedValuesControl.setLayout(new GridLayout(2, false));
		sharedValuesControl.setLayoutData(new GridData(GridData.FILL_BOTH));

		sharedValueTableViewer = createSharedValueTableViewer(sharedValuesControl, jobVo);

		tabItem.setControl(sharedValuesControl);

		createSharedValueTableViewerButtons(sharedValuesControl);
	}

	/**
	 * SharedValue TableViewer 생성
	 * 
	 * @param control
	 * @param jobVo
	 * @return
	 */
	private TableViewer createSharedValueTableViewer(Composite control, JobVo jobVo) {
		sharedValueTableViewer = new TableViewer(control, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table sharedValueTable = sharedValueTableViewer.getTable();

		sharedValueTable.setHeaderVisible(true);
		sharedValueTable.setLinesVisible(true);

		BatchTableColumn[] columns = createSharedValueTableColumns();

		for (int i = 0; i < columns.length; i++) {
			columns[i].setColumnToTable(sharedValueTable);
		}

		sharedValueTableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		sharedValueTableViewer.setLabelProvider(new SharedValuesTableProvider());
		sharedValueTableViewer.setContentProvider(new ArrayContentProvider());
		sharedValueTableViewer.setInput(jobVo.getSharedValues());

		return sharedValueTableViewer;
	}

	/**
	 * SharedValue TableViewer의 컬럼 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createSharedValueTableColumns() {
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BatchMessages.StepInfoContentsConstructor_SHARED_VALUES_TABLE_COLUMN_KEY, 250, SWT.LEFT));
		columns.add(new BatchTableColumn(BatchMessages.StepInfoContentsConstructor_SHARED_VALUES_TABLE_COLUMN_STEP_ID, 120, SWT.LEFT));

		return columns.toArray(new BatchTableColumn[0]);
	}

	/**
	 * SharedValue TableViewer의 버튼 생성
	 * 
	 * @param control
	 */
	private void createSharedValueTableViewerButtons(Composite control) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData());

		Button addButton = new Button(buttonControl, SWT.PUSH);
		addButton.setText(BatchMessages.StepInfoContentsConstructor_SHARED_VALUES_TABLE_COLUMN_);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				BatchPageBeanIDList pageBeanIDs = new BatchPageBeanIDList();
				setStepPageBeanID(pageBeanIDs);

				SharedValueVo[] list = getSharedValueVoFromTableViewer(sharedValueTableViewer);
				SharedValueDialog dialog = new SharedValueDialog(infoControl.getShell(), list, tempStepVo, invalidBatchXMLFileBeanIDList, pageBeanIDs);
				if (dialog.open() == Window.OK) {
					sharedValueTableViewer.setInput(dialog.getResult());
				}
			}
		});

		final Button removeButton = new Button(buttonControl, SWT.PUSH);
		removeButton.setText(BatchMessages.StepInfoContentsConstructor_REMOVE_BUTTON);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.setEnabled(false);
		removeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = getSelection(sharedValueTableViewer);

				if (!NullUtil.isNull(selection)) {
					sharedValueTableViewer.remove(selection.toArray());
					removeButton.setEnabled(false);
				}
				removeButton.setEnabled(false);
			}
		});

		sharedValueTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				removeButton.setEnabled(true);
			}
		});

	}

	/**
	 * Shared Values의 TableViewer에서 SharedValueVo 목록을 가져온다.
	 * 
	 * @param tableviewer
	 * @return
	 */
	private SharedValueVo[] getSharedValueVoFromTableViewer(TableViewer tableviewer) {
		TableItem[] items = tableviewer.getTable().getItems();
		SharedValueVo[] data = new SharedValueVo[items.length];

		if (items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				data[i] = (SharedValueVo) items[i].getData();
			}
		}
		return data;
	}

	/**
	 * Step ID와 Next Step의 Control 생성
	 * 
	 * @param control
	 * @param stepVo
	 */
	private void createStepIDAndNext() {
		Composite idControl = new Composite(infoControl, SWT.NONE);
		idControl.setLayout(new GridLayout(4, false));
		idControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(idControl);

		Label stepId = new Label(idControl, SWT.NONE);
		stepId.setText(BatchMessages.StepInfoContentsConstructor_STEP_ID_LABEL);

		GridData stepIdFieldGD = new GridData();
		stepIdFieldGD.widthHint = 145;

		final Text stepIdField = new Text(idControl, SWT.BORDER);
		stepIdField.setLayoutData(stepIdFieldGD);
		stepIdField.setText(StringUtil.returnEmptyStringIfNull(tempStepVo.getName()));
		stepIdField.forceFocus();
		stepIdField.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempStepVo.setName(stepIdField.getText());

				String readerFieldText = getJobReaderFullName(tempStepVo);
				if (!NullUtil.isEmpty(readerFieldText)) {
					readerText.setText(readerFieldText);
				}

				String writerFieldText = getJobWriterFullName(tempStepVo);
				if (!NullUtil.isEmpty(writerFieldText)) {
					writerText.setText(writerFieldText);
				}
			}
		});
		stepIdField.addListener(SWT.Modify, validation());

		activeNext = new Button(idControl, SWT.CHECK);
		activeNext.setText(BatchMessages.StepInfoContentsConstructor_NEXT_CHECK_BOX);
		activeNext.setSelection(false);
		activeNext.setEnabled(false);

		final Combo nextStep = new Combo(idControl, SWT.BORDER | SWT.READ_ONLY);
		nextStep.setLayoutData(stepIdFieldGD);
		nextStep.removeAll();
		nextStep.setItems(stepAndDecisionNameList);
		nextStep.setEnabled(false);
		nextStep.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempStepVo.setNextStep(nextStep.getText());
			}
		});

		activeNext.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (activeNext.getSelection()) {
					nextStep.setEnabled(true);
					nextStep.select(0);
				} else {
					nextStep.setEnabled(false);
					nextStep.deselectAll();
				}
			}
		});

		if (!NullUtil.isEmpty(tempStepVo.getNextStep())) {
			activeNext.setEnabled(true);
			activeNext.setSelection(true);
			nextStep.setEnabled(true);
			nextStep.setText(tempStepVo.getNextStep());
		}

		if (stepAndDecisionNameList.length > 0) {
			activeNext.setEnabled(true);
		}
	}

	/**
	 * 넘어온 Control의 marginHeight를 0으로 설정
	 * 
	 * @param control
	 */
	public void removeMarginOfGridLayout(Composite control) {
		((GridLayout) control.getLayout()).marginHeight = 0;
	}

	/**
	 * 입력 할 수 없는 Bean ID List 생성
	 * 
	 * @return
	 */
	public BatchXMLFileBeanIDList getBatchJobsInvalidBeanList() {
		BatchXMLFileBeanIDList invalidList = batchXMLFileBeanIDList.clone();
		BatchJobBeanIDList jobBeanIDList = invalidList.getJobBeanIDList(tempStepVo.getJobName());

		if (!NullUtil.isNull(jobBeanIDList)) {
			jobBeanIDList.removeStepDecision(tempStepVo.getName());
		}

		return invalidList;
	}

	/**
	 * jobVo가 가지는 Step/Decision들의 이름 Array를 return
	 * 
	 * @return stepAndDecisionNameList
	 */
	public String[] getStepAndDecisionNameListWithoutThisStepVoName(JobVo jobVo) {
		StepAndDecisionVo[] data = jobVo.getStepAndDecisionVoList();
		String[] stepAndDecisionNameList = new String[] {};
		String sdVoName = StringUtil.returnEmptyStringIfNull(tempStepVo.getName());

		if (!NullUtil.isEmpty(data)) {
			if (NullUtil.isEmpty(sdVoName)) {
				stepAndDecisionNameList = new String[data.length];
			} else {
				stepAndDecisionNameList = new String[data.length - 1];
			}

			int j = 0;

			for (int i = 0; i < data.length; i++) {
				String dataName = data[i].getName();

				if (sdVoName.equals(dataName)) {
					continue;
				} else {
					stepAndDecisionNameList[j] = dataName;
					j++;
				}
			}
		}
		return stepAndDecisionNameList;
	}

	/** Step Type선택에 따른 Option Control 생성 */
	private void createStepTypeOptionControl() {
		partitionTypeControl = new Composite(infoControl, SWT.None);
		partitionTypeControl.setLayout(new GridLayout());
		partitionTypeControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeMarginOfGridLayout(partitionTypeControl);

		if (tempStepVo.isPartitionMode()) {
			createPartitionTypeOption();
		} else {
			createNormalTypeOption();
		}
	}

	/**
	 * Job ID Control 생성
	 * 
	 * @param control
	 * @param jobVo
	 */
	private void createJobIDField(String jobName) {
		Composite jobIdControl = new Composite(infoControl, SWT.NONE);
		jobIdControl.setLayout(new GridLayout(2, false));
		jobIdControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label jobId = new Label(jobIdControl, SWT.NONE);
		jobId.setText(BatchMessages.StepInfoContentsConstructor_JOB_ID_LABEL);

		Text jobIDField = new Text(jobIdControl, SWT.BORDER);
		jobIDField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobIDField.setText(StringUtil.returnEmptyStringIfNull(jobName));
		jobIDField.setEnabled(false);
	}

	/** Step Type Control 생성 */
	private void createStepTypeControl() {
		final Group stepTypeGroup = new Group(infoControl, SWT.None);
		stepTypeGroup.setLayout(new GridLayout(2, false));
		stepTypeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		stepTypeGroup.setText(BatchMessages.StepInfoContentsConstructor_STEP_TYPE_GROUP);

		Button stepTypeNormal = new Button(stepTypeGroup, SWT.RADIO);
		stepTypeNormal.setLayoutData(new GridData());
		stepTypeNormal.setText(BatchMessages.StepInfoContentsConstructor_NORMAL_RADIO_BUTTON);
		stepTypeNormal.addListener(SWT.Selection, selectNormalTypeListener);
		stepTypeNormal.addListener(SWT.Selection, validation());

		Button stepTypePartition = new Button(stepTypeGroup, SWT.RADIO);
		stepTypePartition.setText(BatchMessages.StepInfoContentsConstructor_PARTITION_RADIO_BUTTON);
		stepTypePartition.addListener(SWT.Selection, selectPartitionerTypeListener);
		stepTypePartition.addListener(SWT.Selection, validation());

		if (tempStepVo.isPartitionMode()) {
			stepTypePartition.setSelection(true);
		} else {
			stepTypeNormal.setSelection(true);
		}
	}

	/** Step Normal Type Option Control 생성 */
	private void createNormalTypeOption() {
		cleanComposite(partitionTypeControl);

		// Advanced의 Table이 짤리는 현상을 막기 위해
		infoControl.layout(true, true);

		@SuppressWarnings("unused")
		Label emptyLabel = new Label(partitionTypeControl, SWT.None);
	}

	/** Step Normal Type Radio Button Listener 생성 */
	Listener selectNormalTypeListener = new Listener() {
		public void handleEvent(Event event) {
			tempStepVo.setPartitionMode(false);
			tempStepVo.setPartitionerType("ColumnRange Partitioner");

			createNormalTypeOption();

			if (!NullUtil.isEmpty(stepAndDecisionNameList)) {
				activeNext.setEnabled(true);
			}

			infoControl.layout(true, true);
		}
	};

	/**
	 * <pre>
	 * 넘어온 control의 child를 모두 dispose한다.
	 * 여기서는 Advanced Button 클릭시 Control 제거에 사용된다.
	 * </pre>
	 * 
	 * @param control
	 */
	private void cleanComposite(Composite control) {
		Control[] child = control.getChildren();

		if (child != null && child.length > 0) {
			for (Control data : child) {
				data.dispose();
			}
		}
	}

	/** Step Type에서 Partition Type 설정시 Option Control 생성 */
	Listener selectPartitionerTypeListener = new Listener() {

		public void handleEvent(Event event) {
			tempStepVo.setPartitionMode(true);
			tempStepVo.setPartitionerType("MultiResource Partitioner"); //$NON-NLS-1$

			cleanComposite(partitionTypeControl);

			// Advanced의 Table이 짤리는 현상을 막기 위해
			infoControl.layout(true, true);

			createPartitionTypeOption();

			activeNext.setSelection(false);
			activeNext.setEnabled(false);
			activeNext.notifyListeners(SWT.Selection, null);

			infoControl.layout(true, true);
		}
	};

	/** Step Partition Type Option Control 생성 */
	private void createPartitionTypeOption() {

		Composite partitionTypeOptionControl = new Composite(partitionTypeControl, SWT.None);
		partitionTypeOptionControl.setLayout(new GridLayout(4, false));
		partitionTypeOptionControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createSubstepControl(partitionTypeOptionControl);

		createGridSizeControl(partitionTypeOptionControl);

		createResourceControl(partitionTypeOptionControl);
	}

	/**
	 * SubStep Control 생성
	 * 
	 * @param control
	 */
	private void createSubstepControl(Composite control) {
		Label subStepIdLabel = new Label(control, SWT.None);
		subStepIdLabel.setText(BatchMessages.StepInfoContentsConstructor_SUB_STEP_ID_LABEL);

		final Text subStepIdText = new Text(control, SWT.BORDER);
		subStepIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		subStepIdText.setText(tempStepVo.getSubStepID());
		subStepIdText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempStepVo.setSubStepID(subStepIdText.getText());
			}
		});
		subStepIdText.addListener(SWT.Modify, validation());
	}

	/**
	 * GridSize Control 생성
	 * 
	 * @param control
	 */
	private void createGridSizeControl(Composite control) {
		Label gridSizeLabel = new Label(control, SWT.RIGHT);
		gridSizeLabel.setText(BatchMessages.StepInfoContentsConstructor_GRID_SIZE_LABEL);

		GridData gridSizeTextGData = new GridData();
		gridSizeTextGData.widthHint = 80;

		final Text gridSizeText = new Text(control, SWT.BORDER);
		gridSizeText.setLayoutData(gridSizeTextGData);
		gridSizeText.setText(returnEmptyStringIfNullOrConvertToString(tempStepVo.getGridSize()));
		gridSizeText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					gridSizeString = gridSizeText.getText();
					Integer value = Integer.parseInt(gridSizeString);
					tempStepVo.setGridSize(value);
				} catch (Exception e) {
				}
			}
		});
		gridSizeText.addListener(SWT.Modify, validation());
	}

	/**
	 * Resource Control 생성
	 * 
	 * @param control
	 */
	private void createResourceControl(Composite control) {
		Label ResourceLabel = new Label(control, SWT.None);
		ResourceLabel.setText(BatchMessages.StepInfoContentsConstructor_RESOURCE_LABEL);

		GridData resourceTextGData = new GridData(GridData.FILL_HORIZONTAL);
		resourceTextGData.horizontalSpan = 2;

		final Text resourceText = new Text(control, SWT.BORDER);
		resourceText.setLayoutData(resourceTextGData);
		resourceText.setText(StringUtil.returnEmptyStringIfNull(tempStepVo.getResource()));
		resourceText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempStepVo.setResource(resourceText.getText());
			}
		});
		resourceText.addListener(SWT.Modify, validation());

		GridData resourceGData = new GridData();
		resourceGData.widthHint = 90;

		Button resourceButton = new Button(control, SWT.PUSH);
		resourceButton.setText(BatchMessages.StepInfoContentsConstructor_BROWSE_BUTTON);
		resourceButton.setLayoutData(resourceGData);
		resourceButton.addListener(SWT.Selection, getResourceButtonListener(resourceText));

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 4;

		Label exampleLabel = new Label(control, SWT.None);
		exampleLabel.setText(BatchMessages.StepInfoContentsConstructor_RESOURCE_EXAMPLE);
		exampleLabel.setLayoutData(gData);
	}

	/**
	 * Resource Button 생성
	 * 
	 * @param resourceText
	 * @return
	 */
	private Listener getResourceButtonListener(final Text resourceText) {
		return new Listener() {

			@SuppressWarnings("restriction")
			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(partitionTypeControl.getShell(), SWT.OPEN);
				fileDialog.setText(BatchMessages.StepInfoContentsConstructor_RESOURCE_BUTTON_DIALOG_TITLE);
				fileDialog.setFilterPath(context.getProject().getLocation().toOSString());
				String[] filterExt = { "*.csv", "*.txt" };
				fileDialog.setFilterExtensions(filterExt);

				String selectedResource = fileDialog.open();
				if (NullUtil.isEmpty(selectedResource)) {
					selectedResource = StringUtil.returnEmptyStringIfNull(resourceText.getText());
				} else {
					String[] extensions = getExtensions(filterExt);

					selectedResource = selectedResource.replace("\\", "/");

					if (!hasExtension(selectedResource, extensions)) {
						int extensionIndex = fileDialog.getFilterIndex();

						String extension = extensions[extensionIndex];

						selectedResource += "." + extension;
					}
				}

				resourceText.setText(selectedResource);
			}
		};
	}

	/**
	 * 확장자 목록 생성
	 * 
	 * @param filteredExtensions
	 * @return
	 */
	private String[] getExtensions(String[] filteredExtensions) {
		ArrayList<String> extensions = new ArrayList<String>();

		if (!NullUtil.isEmpty(filteredExtensions)) {
			for (String filteredExtension : filteredExtensions) {
				int dotIndex = filteredExtension.lastIndexOf(".");

				// . 이후의 확장자를 가져오기 위해 1을 더한다.
				String extension = filteredExtension.substring(dotIndex + 1);

				extensions.add(extension);
			}
		}

		return extensions.toArray(new String[0]);
	}

	/**
	 * 해당 확장자를 가지고 있는지 여부 확인
	 * 
	 * @param resourceName
	 * @param extensions
	 * @return
	 */
	private boolean hasExtension(String resourceName, String[] extensions) {
		if (!NullUtil.isEmpty(extensions)) {

			for (String extension : extensions) {
				if (resourceName.endsWith(extension)) {
					return true;
				}

			}
		}

		return false;
	}

	@Override
	protected void validateItems() throws InfoValidationException {
		validateDuplicationBeanID();

		validateStepID();

		if (tempStepVo.isPartitionMode()) {
			validateSubStepID();

			validateGridSize();

			validateResource();
		}

		validateJobReader();

		validateJobWriter();

		validateCommitInterval();
	}

	/**
	 * Bean ID 중복 여부 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateDuplicationBeanID() throws InfoValidationException {
		if (!validateStepPageBeanID()) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_BEAN_ID);
		}
	}

	/**
	 * Step ID 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateStepID() throws InfoValidationException {
		String stepID = tempStepVo.getName();

		if (NullUtil.isEmpty(stepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_STEP_ID);
		}

		if (!NullUtil.isEmpty(stepAndDecisionNameList)) {
			for (int i = 0; i < stepAndDecisionNameList.length; i++) {
				if (stepID.equals(stepAndDecisionNameList[i])) {
					throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_STEP_DECISION_ID);

				}
			}
		}

		if (isBeanIDDuplicate(invalidBatchXMLFileBeanIDList, stepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_BEAN_ID);

		}

		if (!StringUtil.isBatchJobBeanIDAvailable(stepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_STEP_ID);

		}

	}

	/**
	 * SubStep ID 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateSubStepID() throws InfoValidationException {
		String subStepID = tempStepVo.getSubStepID();

		if (NullUtil.isEmpty(subStepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_SUB_STEP_ID);

		}

		if (!StringUtil.isBatchJobBeanIDAvailable(subStepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_SUB_STEP_ID);

		}

		String stepID = tempStepVo.getName();

		if (subStepID.equals(stepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_SUB_STEP_ID_WITH_STEP_DECISION_ID);

		}

		if (!NullUtil.isEmpty(stepAndDecisionNameList)) {
			for (String stepAndDecisionName : stepAndDecisionNameList) {
				if ((subStepID.equals(stepAndDecisionName))) {
					throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_SUB_STEP_ID_WITH_STEP_DECISION_ID);

				}
			}
		}

		if (isBeanIDDuplicate(invalidBatchXMLFileBeanIDList, subStepID)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_DUPLICATE_BEAN_ID);

		}

	}

	/**
	 * GridSize 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateGridSize() throws InfoValidationException {
		if (NullUtil.isEmpty(gridSizeString)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_GRID_SIZE);

		}

		try {
			if (Integer.parseInt(gridSizeString) < 1) {
				throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_GRID_SIZE);

			}

		} catch (Exception e) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_GRID_SIZE);

		}

	}

	/**
	 * Resource 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateResource() throws InfoValidationException {
		if (NullUtil.isEmpty(tempStepVo.getResource())) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_RESOURCE);

		}
	}

	/**
	 * Job Reader 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateJobReader() throws InfoValidationException {
		if (NullUtil.isNull(tempStepVo.getJobReaderInfo())) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_JOB_READER);

		}
	}

	/**
	 * Job Writer 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateJobWriter() throws InfoValidationException {
		if (NullUtil.isNull(tempStepVo.getJobWriterInfo())) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_JOB_WRITER);

		}
	}

	/**
	 * Commit-Interval 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateCommitInterval() throws InfoValidationException {
		if (NullUtil.isEmpty(commitIntervalString)) {
			throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_EMPTY_COMMIT_INTERVAL);

		} else {
			try {
				if (Integer.parseInt(commitIntervalString) < 1) {
					throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_COMMIT_INTERVAL);

				}

			} catch (Exception e) {
				throwInfoValidationException(BatchMessages.StepInfoContentsConstructor_INVALID_COMMIT_INTERVAL);

			}
		}
	}

	/**
	 * 현재 Step Info Page에 중복되는 Bean ID가 있는지 확인한다.
	 * 
	 * @return
	 */
	private boolean validateStepPageBeanID() {
		return setStepPageBeanID(new BatchPageBeanIDList());
	}

	/**
	 * 입력 중인 Step Info Page에 있는 BeanID를 모두 모아 Array에 저장한다.
	 * 
	 * @param pageBeanIDs
	 * @return
	 */
	private boolean setStepPageBeanID(BatchPageBeanIDList pageBeanIDs) {
		return pageBeanIDs.setStepInfoPageBeanIDs(tempStepVo);
	}

	/**
	 * <pre>
	 * Step info에 입력된 정보를 임시 StepVo에 저장한다.
	 * Validation은 실시하지 않는다.
	 * </pre>
	 * 
	 * @return stepVo
	 */
	public StepVo getStepVo() {
		tempStepVo.setJobReaderFullName(getJobReaderFullName(tempStepVo));
		tempStepVo.setJobWriterFullName(getJobWriterFullName(tempStepVo));

		return tempStepVo;
	}

	/**
	 * 신규 StepID의 경우 SharedValueVo의 StepID 적용
	 * 
	 * @param stepID
	 * @return
	 */
	public SharedValueVo[] applyStepIDToSharedVoAndGetList(String stepID) {
		return updateStepIDToSharedValueVoAndGetList(null, stepID);
	}

	/**
	 * 기존 StepID를 수정한 경우 SharedValueVo의 StepID 적용
	 * 
	 * @param preStepID
	 * @param postStepID
	 * @return
	 */
	public SharedValueVo[] updateStepIDToSharedValueVoAndGetList(String preStepID, String postStepID) {
		TableItem[] items = sharedValueTableViewer.getTable().getItems();
		SharedValueVo[] data = new SharedValueVo[items.length];

		if (!NullUtil.isEmpty(items)) {
			for (int i = 0; i < items.length; i++) {
				SharedValueVo svVo = (SharedValueVo) items[i].getData();
				String stepID = svVo.getStepId();

				if (NullUtil.isEmpty(stepID) || stepID.equals(preStepID)) {
					svVo.setStepId(postStepID);
				}

				data[i] = svVo;
			}
		}

		return data;
	}
}
