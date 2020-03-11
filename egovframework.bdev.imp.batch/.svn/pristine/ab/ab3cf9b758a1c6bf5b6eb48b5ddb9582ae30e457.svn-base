package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.DecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.NextVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.views.NextTableViewerProvider;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * BatchJobCreationCustomizePage에서 Decision Info 화면을 그림
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
public class DecisionInfoContentsConstructor extends InfoContentsConstructor {

	/** DecisionInfo 화면의 입력값을 가지고 다니는 임시 DecisionVo */
	private DecisionVo tempDecisionVo = null;

	/**
	 * <pre>
	 * [Decision Info]
	 * Next On, To의 입력 여부를 설정하는 CheckBox
	 * </pre>
	 */
	private Button addNextOnTo = null;

	/**
	 * <pre>
	 * [Decision Info]
	 * End On, To의 입력 여부를 설정하는 CheckBox
	 * </pre>
	 */
	private Button addEndOnToButton = null;

	/**
	 * <pre>
	 * [Decision Info]
	 * Stop On, To의 입력 여부를 설정하는 CheckBox
	 * </pre>
	 */
	private Button addStopOnToButton = null;

	/**
	 * <pre>
	 * [Decision Info]
	 * Fail On, To의 입력 여부를 설정하는 CheckBox
	 * </pre>
	 */
	private Button addFailOnToButton = null;

	private String[] stepAndDecisionNameList = null;

	/**
	 * DecisionInfoContentsConstructor 생성자
	 * 
	 * @param currentPage
	 * @param infoControl
	 * @param applyButton
	 * @param batchXMLFileBeanIDList
	 * @param projectBeanIDList
	 *
	 */
	public DecisionInfoContentsConstructor(WizardPage currentPage,
			Composite infoControl, Button applyButton,
			BatchXMLFileBeanIDList batchXMLFileBeanIDList,
			List<String> projectBeanIDList) {
		this.currentPage = currentPage;
		this.infoControl = infoControl;
		this.applyButton = applyButton;
		this.batchXMLFileBeanIDList = batchXMLFileBeanIDList;
		this.projectBeanIDList = projectBeanIDList;
	}

	/**
	 * Decision info 생성
	 * 
	 * @param decisionVo
	 */
	public void createDecisionInfoContents(JobVo jobVo, DecisionVo decisionVo) {
		tempDecisionVo = new DecisionVo();
		tempDecisionVo.copyValues(decisionVo);
		tempDecisionVo.setJobName(jobVo.getJobName());

		invalidBatchXMLFileBeanIDList = getInvalidBeanList(tempDecisionVo);

		GridData gData = new GridData();
		gData.widthHint = 138;

		stepAndDecisionNameList = getStepAndDecisionNameList(jobVo, decisionVo);

		Text jobIDField = createJobIDField(infoControl, jobVo);
		jobIDField.setEnabled(false);

		createDecisionIDField(infoControl);

		createNextGroup(infoControl, decisionVo);

		createEndControl(infoControl, gData);

		createFailControl(infoControl, gData);

		createStopControl(infoControl, jobVo, gData);
		
		if(!NullUtil.isNull(decisionVo)){
			validation().handleEvent(null);
		}
	}

	/**
	 * 중복검사를 위해 현재 Step Info page의 BeanID list를 제외한 나머지 BeanID list를 가져온다.
	 * 
	 * @param sdVo
	 * @return
	 */
	public BatchXMLFileBeanIDList getInvalidBeanList(StepAndDecisionVo sdVo) {
		BatchXMLFileBeanIDList invalidList = batchXMLFileBeanIDList.clone();
		BatchJobBeanIDList jobBeanIDList = invalidList.getJobBeanIDList(sdVo
				.getJobName());

		if (!NullUtil.isNull(jobBeanIDList)) {
			jobBeanIDList.removeStepDecision(sdVo.getName());
		}

		return invalidList;
	}

	/**
	 * jobVo가 가지는 Step/Decision들의 이름 Array를 return
	 * 
	 * @return stepAndDecisionNameList
	 */
	public String[] getStepAndDecisionNameList(JobVo jobVo,
			StepAndDecisionVo sdVo) {
		StepAndDecisionVo[] data = jobVo.getStepAndDecisionVoList();
		String[] stepAndDecisionNameList = new String[] {};
		String sdVoName = ""; //$NON-NLS-1$

		if (!NullUtil.isEmpty(data)) {
			if (NullUtil.isNull(sdVo)) {
				stepAndDecisionNameList = new String[data.length];
				sdVoName = ""; //$NON-NLS-1$
			} else {
				stepAndDecisionNameList = new String[data.length - 1];
				sdVoName = sdVo.getName();
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

	/**
	 * Job ID Control 생성
	 * 
	 * @param control
	 * @param jobVo
	 */
	private Text createJobIDField(Composite control, JobVo jobVo) {
		Composite jobIdControl = new Composite(control, SWT.NONE);
		jobIdControl.setLayout(new GridLayout(2, false));
		jobIdControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label jobId = new Label(jobIdControl, SWT.NONE);
		jobId.setText(BatchMessages.DecisionInfoContentsConstructor_JOB_ID_LABEL);

		String jobName = jobVo.getJobName();

		Text jobIDField = new Text(jobIdControl, SWT.BORDER);
		jobIDField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobIDField.setText(StringUtil.returnEmptyStringIfNull(jobName));

		return jobIDField;
	}

	/**
	 * Decision ID Control 생성
	 * 
	 * @param control
	 */
	private void createDecisionIDField(Composite control) {

		Composite innerContents = new Composite(control, SWT.None);
		innerContents.setLayout(new GridLayout(2, false));
		innerContents.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label decisionId = new Label(innerContents, SWT.None);
		decisionId
				.setText(BatchMessages.DecisionInfoContentsConstructor_DECISION_ID_LABEL);

		String decisionID = tempDecisionVo.getName();

		final Text decisionIdField = new Text(innerContents, SWT.BORDER);
		decisionIdField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		decisionIdField.setText(StringUtil.returnEmptyStringIfNull(decisionID));
		decisionIdField.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setName(decisionIdField.getText());
			}
		});
		decisionIdField.addListener(SWT.Modify, validation());

		decisionIdField.forceFocus();
	}

	/**
	 * NextVo의 TableViewer 및 Button 생성, Listener 설정
	 * 
	 * @param control
	 */
	private void createNextGroup(final Composite control,
			final DecisionVo decisionVo) {

		final Group nextGroup = new Group(control, SWT.None);
		nextGroup.setLayout(new GridLayout(2, false));
		nextGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		nextGroup
				.setText(BatchMessages.DecisionInfoContentsConstructor_NEXT_GROUP_TITLE);

		addNextOnTo = createOnToCheckBoxButton(
				nextGroup,
				BatchMessages.DecisionInfoContentsConstructor_ADD_NEXT_ON_TO_BUTTON);

		final TableViewer nextTableViewer = createNextTableViewer(nextGroup,
				decisionVo);

		Composite buttonControl = new Composite(nextGroup, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData());

		final Button addButton = createNextTableAddButton(buttonControl,
				nextTableViewer);

		final Button editButton = createNextTableEditButton(buttonControl,
				nextTableViewer);

		final Button removeButton = createNextTableRemoveButton(buttonControl,
				nextTableViewer);

		removeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = getSelection(nextTableViewer);

				if (!NullUtil.isNull(selection)) {
					nextTableViewer.remove(selection.toArray());
					editButton.setEnabled(false);
					removeButton.setEnabled(false);

					tempDecisionVo
							.setNextVo(getAllNextVoFromTable(nextTableViewer));

					validation().handleEvent(null);
				}
			}
		});

		nextTableViewer.getTable().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) nextTableViewer
						.getSelection();

				if (!selection.isEmpty()) {
					editButton.setEnabled(true);
					removeButton.setEnabled(true);
				}

			}
		});

		addNextOnTo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean selection = addNextOnTo.getSelection();

				if (selection) {
					nextTableViewer.getTable().setEnabled(true);
					addButton.setEnabled(true);

					NextVo[] nextTableInput = null;
					if (!NullUtil.isNull(decisionVo)
							&& !NullUtil.isEmpty(decisionVo.getNextVo())) {
						nextTableInput = decisionVo.getNextVo();
					}

					nextTableViewer.setInput(nextTableInput);
					tempDecisionVo.setNextVo(nextTableInput);
				} else {
					nextTableViewer.getTable().setEnabled(false);
					addButton.setEnabled(false);

					nextTableViewer.setInput(null);
					tempDecisionVo.setNextVo(null);
				}

				editButton.setEnabled(false);
				removeButton.setEnabled(false);

				nextTableViewer.setSelection(new StructuredSelection());
			}
		});

		addNextOnTo.notifyListeners(SWT.Selection, null);

		addNextOnTo.addListener(SWT.Selection, validation());
	}

	/**
	 * Next TableViewer 생성
	 * 
	 * @param control
	 * @param decisionVo
	 * @return
	 */
	private TableViewer createNextTableViewer(Composite control,
			DecisionVo decisionVo) {
		TableViewer nextTableViewer = new TableViewer(control, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		nextTableViewer.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));
		nextTableViewer.setContentProvider(new ArrayContentProvider());
		nextTableViewer.setLabelProvider(new NextTableViewerProvider());

		final Table table = nextTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		BatchTableColumn[] columns = createNextTableColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].setColumnToTable(table);
		}

		if (!NullUtil.isNull(decisionVo)
				&& !NullUtil.isEmpty(decisionVo.getNextVo())) {
			addNextOnTo.setSelection(true);
			nextTableViewer.setInput(decisionVo.getNextVo());
		}

		return nextTableViewer;
	}

	/**
	 * Next Table의 Add Button 생성
	 * 
	 * @param control
	 * @param nextTableViewer
	 * @return
	 */
	private Button createNextTableAddButton(Composite control,
			TableViewer nextTableViewer) {
		Button addButton = new Button(control, SWT.PUSH);
		addButton
				.setText(BatchMessages.DecisionInfoContentsConstructor_ADD_NEXT_BUTTON);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addListener(SWT.Selection,
				getNextTableAddButtonListener(nextTableViewer));

		return addButton;
	}

	/**
	 * Next Table의 Edit Button 생성
	 * 
	 * @param control
	 * @param nextTableViewer
	 * @return
	 */
	private Button createNextTableEditButton(Composite control,
			TableViewer nextTableViewer) {
		Button editButton = new Button(control, SWT.PUSH);
		editButton
				.setText(BatchMessages.DecisionInfoContentsConstructor_EDIT_NEXT_BUTTON);
		editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editButton.setEnabled(false);
		editButton.addListener(SWT.Selection,
				getNextTableEditButtonListener(nextTableViewer));

		return editButton;
	}

	/**
	 * Next Table의 Remove Button 생성
	 * 
	 * @param control
	 * @param nextTableViewer
	 * @return
	 */
	private Button createNextTableRemoveButton(Composite control,
			final TableViewer nextTableViewer) {
		Button removeButton = new Button(control, SWT.PUSH);
		removeButton
				.setText(BatchMessages.DecisionInfoContentsConstructor_REMOVE_NEXT_BUTTON);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.setEnabled(false);

		return removeButton;
	}

	/**
	 * Next Table의 Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createNextTableColumns() {
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(
				BatchMessages.DecisionInfoContentsConstructor_NEXT_TABLE_COLUMN_NEXT_ON,
				170, SWT.LEFT));
		columns.add(new BatchTableColumn(
				BatchMessages.DecisionInfoContentsConstructor_NEXT_TABLE_COLUMN_NEXT_TO,
				170, SWT.LEFT));

		return columns.toArray(new BatchTableColumn[0]);
	}

	/**
	 * NextTable의 Add Button Listener
	 * 
	 * @param nextTableViewer
	 * @param stepAndDecisionNameList
	 * @return
	 */
	private Listener getNextTableAddButtonListener(
			final TableViewer nextTableViewer) {
		return new Listener() {
			public void handleEvent(Event event) {
				NextVo[] availableNextList = getAvailableNextVoFromTable(
						nextTableViewer, null);

				NextDialog dialog = new NextDialog(infoControl.getShell(),
						null, stepAndDecisionNameList, availableNextList);
				if (dialog.open() == Window.OK) {
					NextVo data = new NextVo();
					data.setNextOn(dialog.getNextOn());
					data.setNextTo(dialog.getNextTo());

					nextTableViewer.add(data);

					tempDecisionVo
							.setNextVo(getAllNextVoFromTable(nextTableViewer));

					validation().handleEvent(event);
				}
			}
		};
	}

	/**
	 * NextTable의 Edit Button Listener
	 * 
	 * @param nextTableViewer
	 * @param stepAndDecisionNameList
	 * @return
	 */
	private Listener getNextTableEditButtonListener(
			final TableViewer nextTableViewer) {
		return new Listener() {

			public void handleEvent(Event event) {

				IStructuredSelection selection = getSelection(nextTableViewer);

				NextVo nextVo = (NextVo) selection.getFirstElement();

				NextVo[] availableNextList = getAvailableNextVoFromTable(
						nextTableViewer, nextVo);

				NextDialog dialog = new NextDialog(infoControl.getShell(),
						nextVo, stepAndDecisionNameList, availableNextList);
				if (dialog.open() == Window.OK) {

					nextVo.setNextOn(dialog.getNextOn());
					nextVo.setNextTo(dialog.getNextTo());
					nextTableViewer.update(nextVo, null);

					tempDecisionVo
							.setNextVo(getAllNextVoFromTable(nextTableViewer));
				}
			}
		};
	}

	/** 해당 TableViewer의 Selection을 가져온다. */
	private IStructuredSelection getSelection(TableViewer tableViewer) {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		if (selection.isEmpty()) {
			return null;
		} else {
			return selection;
		}
	}

	/**
	 * Next Table에 있는 모든 item을 가져온다
	 * 
	 * @param nextTableViewer
	 * @return
	 */
	private NextVo[] getAllNextVoFromTable(TableViewer nextTableViewer) {
		return getAvailableNextVoFromTable(nextTableViewer, null);
	}

	/**
	 * NextVo의 TableViewer에서 자신을 제외한 NextVO 목록을 가져온다.
	 * 
	 * @return
	 */
	private NextVo[] getAvailableNextVoFromTable(TableViewer nextTableViewer,
			NextVo selectedNextVo) {
		TableItem[] items = nextTableViewer.getTable().getItems();
		NextVo[] nextVos = null;
		boolean isAdd = NullUtil.isNull(selectedNextVo);

		if (isAdd) {
			nextVos = new NextVo[items.length];
		} else {
			nextVos = new NextVo[items.length - 1];
		}

		if (items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				NextVo nextVo = (NextVo) items[i].getData();
				if (!isAdd && selectedNextVo.compare(nextVo)) {
					continue;
				} else {
					nextVos[i] = (NextVo) items[i].getData();
				}
			}
		}
		return nextVos;
	}

	/**
	 * End On, Exit-Code의 Control 생성
	 * 
	 * @param control
	 */
	private void createEndControl(Composite control, GridData gData) {

		Group endOnControl = new Group(control, SWT.None);
		endOnControl.setLayout(new GridLayout(4, false));
		endOnControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		endOnControl
				.setText(BatchMessages.DecisionInfoContentsConstructor_END_GROUP_TITLE);

		addEndOnToButton = createOnToCheckBoxButton(
				endOnControl,
				BatchMessages.DecisionInfoContentsConstructor_ADD_END_ON_TO_BUTTON);

		Combo endOnCombo = createEndOnControl(endOnControl);

		Combo endExitCodeCombo = createEndExitCodeControl(endOnControl);

		setInitialState(addEndOnToButton, endOnCombo, endExitCodeCombo,
				tempDecisionVo.getEndOn(), tempDecisionVo.getEndExitCode());

		addOnToCheckBoxListener(addEndOnToButton, endOnCombo, endExitCodeCombo);
	}

	/**
	 * End On Control 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createEndOnControl(Composite control) {
		String[] endOnComboItems = new String[] {
				"COMPLETED",
				"*",
				BatchMessages.DecisionInfoContentsConstructor_INPUT_VALUE_BY_SELF };

		final Combo endOnCombo = createOnLabelAndCombo(control,
				BatchMessages.DecisionInfoContentsConstructor_END_ON_LABEL,
				endOnComboItems);

		endOnCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setEndOn(endOnCombo.getText());
			}
		});
		endOnCombo.addListener(SWT.Modify, validation());

		return endOnCombo;
	}

	/**
	 * End Exit-Code의 Control 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createEndExitCodeControl(Composite control) {
		String[] endExitComboItems = new String[] {
				"COMPLETED",
				BatchMessages.DecisionInfoContentsConstructor_INPUT_VALUE_BY_SELF };

		final Combo endExitCodeCombo = createToLabelAndCombo(control,
				BatchMessages.DecisionInfoContentsConstructor_EXIT_CODE_LABEL,
				endExitComboItems);

		endExitCodeCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setEndExitCode(endExitCodeCombo.getText());
			}
		});
		endExitCodeCombo.addListener(SWT.Modify, validation());

		return endExitCodeCombo;
	}

	/**
	 * End CheckBox의 Control 생성
	 * 
	 * @param control
	 * @param buttonString
	 * @return
	 */
	private Button createOnToCheckBoxButton(Composite control,
			String buttonString) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 4;

		Button onToButton = new Button(control, SWT.CHECK);
		onToButton.setText(buttonString);
		onToButton.setLayoutData(gData);

		return onToButton;
	}

	/**
	 * On의 Lable 및 Combo 생성
	 * 
	 * @param control
	 * @param lableString
	 * @param comboItems
	 * @return
	 */
	private Combo createOnLabelAndCombo(Composite control, String lableString,
			String[] comboItems) {
		return createOnToLabelAndCombo(control, 50, lableString, comboItems);
	}

	/**
	 * To의 Label 및 Combo 생성
	 * 
	 * @param control
	 * @param lableString
	 * @param comboItems
	 * @return
	 */
	private Combo createToLabelAndCombo(Composite control, String lableString,
			String[] comboItems) {
		return createOnToLabelAndCombo(control, 60, lableString, comboItems);
	}

	/**
	 * On, To의 Label, Combo 생성
	 * 
	 * @param control
	 * @param width
	 * @param lableString
	 * @param comboItems
	 * @return
	 */
	private Combo createOnToLabelAndCombo(Composite control, int width,
			String lableString, String[] comboItems) {
		GridData labelGData = new GridData();
		labelGData.widthHint = width;

		Label endOnLabel = new Label(control, SWT.RIGHT);
		endOnLabel.setText(lableString);
		endOnLabel.setLayoutData(labelGData);

		GridData comboGData = new GridData(GridData.FILL_HORIZONTAL);
		comboGData.grabExcessHorizontalSpace = true;
		comboGData.minimumWidth = 130;

		Combo onToCombo = new Combo(control, SWT.BORDER);
		onToCombo.setItems(comboItems);
		onToCombo.setLayoutData(comboGData);

		return onToCombo;
	}

	/**
	 * 화면 최초 생성시 상태 설정
	 * 
	 * @param ontToButton
	 * @param onCombo
	 * @param toCombo
	 * @param on
	 * @param to
	 */
	private void setInitialState(Button ontToButton, Combo onCombo,
			Combo toCombo, String on, String to) {
		ontToButton.setSelection(false);
		onCombo.setEnabled(false);
		toCombo.setEnabled(false);

		if (!NullUtil.isEmpty(on)) {
			ontToButton.setSelection(true);

			onCombo.setText(on);
			onCombo.setEnabled(true);
			toCombo.setEnabled(true);

			if (!NullUtil.isEmpty(to)) {
				toCombo.setText(to);
			}
		}
	}

	/**
	 * OnTo의 CheckBox Listener 추가
	 * 
	 * @param onToCheckBox
	 * @param onCombo
	 * @param toCombo
	 */
	private void addOnToCheckBoxListener(final Button onToCheckBox,
			final Combo onCombo, final Combo toCombo) {
		onToCheckBox.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean setEnable = onToCheckBox.getSelection();
				onCombo.setEnabled(setEnable);
				toCombo.setEnabled(setEnable);
				validation().handleEvent(new Event());
				if (setEnable) {
					onCombo.select(0);
				} else {
					onCombo.deselectAll();
					toCombo.deselectAll();
				}
			}
		});
	}

	/**
	 * Fail On, Exit-Code의 Control 생성
	 * 
	 * @param control
	 */
	private void createFailControl(Composite control, GridData gData) {

		Group failControl = new Group(control, SWT.None);
		failControl.setLayout(new GridLayout(4, false));
		failControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		failControl
				.setText(BatchMessages.DecisionInfoContentsConstructor_FAIL_GROUP_TITLE);

		addFailOnToButton = createOnToCheckBoxButton(
				failControl,
				BatchMessages.DecisionInfoContentsConstructor_ADD_FAIL_ON_TO_BUTTON);

		Combo failOnCombo = createFailOnControl(failControl);

		Combo failExitCodeCombo = createFailExitCodeControl(failControl);

		setInitialState(addFailOnToButton, failOnCombo, failExitCodeCombo,
				tempDecisionVo.getFailOn(), tempDecisionVo.getFailExitCode());

		addOnToCheckBoxListener(addFailOnToButton, failOnCombo,
				failExitCodeCombo);
	}

	/**
	 * Fail On의 Control 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createFailOnControl(Composite control) {
		String[] failOnComboItems = new String[] {
				"FAILED",
				"*",
				BatchMessages.DecisionInfoContentsConstructor_INPUT_VALUE_BY_SELF };

		final Combo failOnCombo = createOnLabelAndCombo(control,
				BatchMessages.DecisionInfoContentsConstructor_FAIL_ON_LABEL,
				failOnComboItems);

		failOnCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setFailOn(failOnCombo.getText());
			}
		});
		failOnCombo.addListener(SWT.Modify, validation());

		return failOnCombo;
	}

	/**
	 * Fail Exit-Code의 Control 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createFailExitCodeControl(Composite control) {
		String[] failExitComboItems = new String[] {
				"FAILED",
				BatchMessages.DecisionInfoContentsConstructor_INPUT_VALUE_BY_SELF };

		final Combo failExitCodeCombo = createToLabelAndCombo(
				control,
				BatchMessages.DecisionInfoContentsConstructor_FAIL_EXIT_CODE_BUTTON,
				failExitComboItems);

		failExitCodeCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setFailExitCode(failExitCodeCombo.getText());
			}
		});

		failExitCodeCombo.addListener(SWT.Modify, validation());

		return failExitCodeCombo;
	}

	/**
	 * Stop On, Exit-Code의 Control 생성
	 * 
	 * @param control
	 */
	private void createStopControl(Composite control, JobVo jobVo,
			GridData gData) {
		Group stopControl = new Group(control, SWT.None);
		stopControl.setLayout(new GridLayout(4, false));
		stopControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		stopControl
				.setText(BatchMessages.DecisionInfoContentsConstructor_STOP_GROUP_TITLE);

		addStopOnToButton = createOnToCheckBoxButton(
				stopControl,
				BatchMessages.DecisionInfoContentsConstructor_ADD_STOP_ON_TO_BUTTON);

		Combo stopOnCombo = createStopOnControl(stopControl);

		Combo stopRestartCombo = createStopRestartControl(stopControl, jobVo);

		setInitialState(addStopOnToButton, stopOnCombo, stopRestartCombo,
				tempDecisionVo.getStopOn(), tempDecisionVo.getStopRestart());

		addOnToCheckBoxListener(addStopOnToButton, stopOnCombo,
				stopRestartCombo);
	}

	/**
	 * Stop On의 Control 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createStopOnControl(Composite control) {
		String[] stopOnComboItems = new String[] {
				"COMPLETED",
				"FAILED",
				"*",
				BatchMessages.DecisionInfoContentsConstructor_INPUT_VALUE_BY_SELF };

		final Combo stopOnCombo = createOnLabelAndCombo(control,
				BatchMessages.DecisionInfoContentsConstructor_STOP_ON_LABEL,
				stopOnComboItems);

		stopOnCombo.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setStopOn(stopOnCombo.getText());
			}
		});

		stopOnCombo.addListener(SWT.Modify, validation());

		return stopOnCombo;
	}

	/**
	 * Stop Restart의 Control 생성
	 * 
	 * @param control
	 * @param jobVo
	 * @return
	 */
	private Combo createStopRestartControl(Composite control, JobVo jobVo) {
		String[] stopRestartComboItems = getStepNameList(jobVo);

		final Combo stopRestartCombo = createStopRestartLabelAndCombo(
				control,
				BatchMessages.DecisionInfoContentsConstructor_EXIT_CODE_RESTART_LABEL,
				stopRestartComboItems);

		stopRestartCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				tempDecisionVo.setStopRestart(stopRestartCombo.getText());
			}
		});

		stopRestartCombo.addListener(SWT.Selection, validation());

		return stopRestartCombo;
	}

	/**
	 * Stop Restart의 Label, Combo 생성
	 * 
	 * @param control
	 * @param lableString
	 * @param comboItems
	 * @return
	 */
	private Combo createStopRestartLabelAndCombo(Composite control,
			String lableString, String[] comboItems) {
		GridData labelGData = new GridData();
		labelGData.widthHint = 61;

		Label endOnLabel = new Label(control, SWT.RIGHT);
		endOnLabel.setText(lableString);
		endOnLabel.setLayoutData(labelGData);

		GridData comboGData = new GridData(GridData.FILL_HORIZONTAL);
		comboGData.grabExcessHorizontalSpace = true;
		comboGData.minimumWidth = 130;

		Combo restartCombo = new Combo(control, SWT.BORDER | SWT.READ_ONLY);
		restartCombo.setItems(comboItems);
		restartCombo.setLayoutData(comboGData);

		return restartCombo;
	}

	/**
	 * jobVo에 속한 StepVo의 이름만 가져온다.
	 * 
	 * @param jobVo
	 * @return
	 */
	public String[] getStepNameList(JobVo jobVo) {
		StepAndDecisionVo[] data = jobVo.getStepAndDecisionVoList();
		ArrayList<String> stepNameList = new ArrayList<String>();
		if (!NullUtil.isEmpty(data)) {
			for (StepAndDecisionVo vo : data) {
				if (vo instanceof StepVo) {
					stepNameList.add(vo.getName());
				}
			}
		}

		String[] result = new String[stepNameList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = stepNameList.get(i);
		}
		return result;
	}

	@Override
	protected void validateItems() throws InfoValidationException {
		validateDecisionID();

		validateOnTos();
	}

	/**
	 * Decision ID 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateDecisionID() throws InfoValidationException {
		String decisionID = tempDecisionVo.getName();

		if (NullUtil.isEmpty(decisionID)) {
			throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_DECISION_ID_ERROR_MESSAGE);

		}

		if (!StringUtil.isBatchJobBeanIDAvailable(decisionID)) {
			throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_DECISION_ID_ERROR_MESSAGE);

		}

		if (isBeanIDDuplicate(invalidBatchXMLFileBeanIDList, decisionID)) {
			throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_DUPLICATE_BEAN_ID_ERROR_MESSAGE);

		}

		if (!NullUtil.isEmpty(stepAndDecisionNameList)) {
			for (String stepAndDecisionName : stepAndDecisionNameList) {
				if (decisionID.equals(stepAndDecisionName)) {
					throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_DUPLICATE_STEP_OR_DECISION_ID_ERROR_MESSAGE);

				}
			}
		}

	}

	/**
	 * Next, End, Fail, Stop의 On, To 유효성 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateOnTos() throws InfoValidationException {
		validateOnValuesDuplication();

		boolean isNextOnToComplete = validateNextOnTo();

		boolean isEndOnToComplete = validateEndOnTo();

		boolean isFailOnToComplete = validateFailOnTo();

		boolean isStopOnToComplete = validateStopOnTo();

		boolean atLeastOne = isNextOnToComplete || isEndOnToComplete
				|| isFailOnToComplete || isStopOnToComplete;

		if (!atLeastOne) {
			throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_NEXT_END_FAIL_STOP_ERROR_MESSAGE);
		}
	}

	/**
	 * On값 끼리의 중복 검사
	 * 
	 * @throws InfoValidationException
	 */
	private void validateOnValuesDuplication()
			throws InfoValidationException {
		ArrayList<String> onList = new ArrayList<String>();

		NextVo[] nextVos = tempDecisionVo.getNextVo();
		if (!NullUtil.isEmpty(nextVos)) {
			for (NextVo nextVo : nextVos) {
				onList.add(nextVo.getNextOn());
			}
		}

		String[] onValues = new String[] { tempDecisionVo.getEndOn(),
				tempDecisionVo.getFailOn(), tempDecisionVo.getStopOn() };

		for (String onValue : onValues) {
			if (isStringItemDuplicateInArrayList(onList, onValue)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_DUPLICATE_ON_VALUE_ERROR_MESSAGE);

			} else if (!NullUtil.isEmpty(onValue)) {
				onList.add(onValue);

			}
		}
	}

	/**
	 * Next on, to 값 유효성 검사
	 * 
	 * @return
	 * @throws InfoValidationException
	 */
	private boolean validateNextOnTo() throws InfoValidationException {
		boolean atLeastOneOn = false;

		if (addNextOnTo.getSelection()) {
			if (NullUtil.isEmpty(tempDecisionVo.getNextVo())) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_NEXT_ITEM_ERROR_MESSAGE);

			} else {
				atLeastOneOn = true;

			}
		}

		return atLeastOneOn;
	}

	/**
	 * End On, To 값 유효성 검사
	 * 
	 * @return
	 * @throws InfoValidationException
	 */
	private boolean validateEndOnTo() throws InfoValidationException {
		boolean atLeastOneOn = false;

		if (!addEndOnToButton.isDisposed() && addEndOnToButton.getSelection()) {
			String endOnString = tempDecisionVo.getEndOn();
			String endExitCodeString = tempDecisionVo.getEndExitCode();

			if (NullUtil.isEmpty(endOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_EXIT_ON_ERROR_MESSAGE);

			} else if (!isDecisionElementNameAvailable(endOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_EXIT_ON_ERROR_MESSAGE);

			} else if (!NullUtil.isEmpty(endExitCodeString)
					&& !isDecisionElementNameAvailable(endExitCodeString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_END_EXIT_CODE_ERROR_MESSAGE);

			} else {
				atLeastOneOn = true;

			}
		}

		return atLeastOneOn;
	}

	/**
	 * Fail on, to 값 유효성 검사
	 * 
	 * @return
	 * @throws InfoValidationException
	 */
	private boolean validateFailOnTo() throws InfoValidationException {
		boolean atLeastOneOn = false;

		if (!addFailOnToButton.isDisposed() && addFailOnToButton.getSelection()) {
			String failOnString = tempDecisionVo.getFailOn();
			String failExitCodeString = tempDecisionVo.getFailExitCode();

			if (NullUtil.isEmpty(failOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_FAIL_ON_ERROR_MESSAGE);

			} else if (!isDecisionElementNameAvailable(failOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_FAIL_ON_ERROR_MESSAGE);

			} else if (!NullUtil.isEmpty(failExitCodeString)
					&& !isDecisionElementNameAvailable(failExitCodeString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_FAIL_EXIT_CODE_ERROR_MESSAGE);

			} else {
				atLeastOneOn = true;
			}

		}

		return atLeastOneOn;
	}

	/**
	 * Stop on, to 값 유효성 검사
	 * 
	 * @return
	 * @throws InfoValidationException
	 */
	private boolean validateStopOnTo() throws InfoValidationException {
		boolean atLeastOneOn = false;

		if (!addStopOnToButton.isDisposed() && addStopOnToButton.getSelection()) {
			String stopOnString = tempDecisionVo.getStopOn();
			String stopRestartString = tempDecisionVo.getStopRestart();

			if (NullUtil.isEmpty(stopOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_STOP_ON_ERROR_MESSAGE);

			} else if (!isDecisionElementNameAvailable(stopOnString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_INVALID_EMPTY_ON_ERROR_MESSAGE);

			} else if (NullUtil.isEmpty(stopRestartString)) {
				throwInfoValidationException(BatchMessages.DecisionInfoContentsConstructor_EMPTY_STOP_RESTART_ERROR_MESSAGE);

			} else {
				atLeastOneOn = true;
			}

		}

		return atLeastOneOn;
	}

	/**
	 * Decision Vo Item 값들의 String 유효성 검사
	 * 
	 * @param name
	 * @return
	 */
	private boolean isDecisionElementNameAvailable(String name) {
		String invalidSignals = "<>" + "\"";

		if (StringUtil.hasKorean(name)
				|| StringUtil.hasEmptySpace(name)
				|| StringUtil.doesStringHasSignalsAtLeastOneCharacter(
						invalidSignals, name)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * On 값 끼리 중복되는지 검사
	 * 
	 * @param arrayList
	 * @param string
	 * @return
	 */
	private boolean isStringItemDuplicateInArrayList(
			ArrayList<String> arrayList, String string) {
		if (NullUtil.isNull(arrayList) || NullUtil.isNull(string)) {
			return false;
		}

		for (String item : arrayList) {
			if (string.equals(item)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 * Decision info에 입력된 정보를 임시 DecisionVo에 저장한다.
	 * Validation은 실시하지 않는다.
	 * </pre>
	 * 
	 * @return decisionVo
	 */
	public DecisionVo getDecisionVo() {
		return tempDecisionVo;
	}
}
