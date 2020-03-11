package egovframework.bdev.imp.confmngt.preferences.readwrite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRWMap;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * <pre>
 * Job Reader, Writer Info를 각각 생성하는 
 * JobReaderDialog, JobWriterDialog의 부모 클래스
 * </pre>
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
@SuppressWarnings("restriction")
public class JobRWDialog extends StatusDialog {
	final private String DEFAULT = "선택";
	final private String FILE = "File"; //$NON-NLS-1$
	final private String DB = "DB"; //$NON-NLS-1$

	/** 기본 Job Reader / Writer List */
	private DefaultJobRWMap defaultJobType = null;

	/** Add Job RW 여부 */
	final private boolean isAddButton;

	/** 기존에 존재하는 Job RW ID 리스트 */
	final List<String> existingIdList;

	/** 선택한 Job RW */
	private JobRWInfo jobRWInfo = null;

	/** Name을 입력받는 Text */
	private Text nameText = null;

	/** Item Type을 선택하는 Combo */
	private Combo itemTypeCombo = null;

	/** Resource Type을 선택하는 Combo */
	private Combo resourceTypeCombo = null;

	/** Class를 입력받는 Text */
	private Text classText = null;
	
	/** Browse Button */
	private Button browseSelectButton = null;

	/** itemTypeField Text 설정 및 Dialog type 구분 */
	private String description;
	
	/**
	 * description의 값을 설정한다.
	 *
	 * @param description the description to set
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ItemReaderWriterDialog의 생성자
	 * 
	 * @param parent
	 * @param isAddButton
	 * @param name
	 * @param itemType
	 * @param classValue
	 * @param existingIdList
	 * @param description
	 */
	public JobRWDialog(Shell parent, boolean isAddButton, JobRWInfo jobRWInfo, List<String> existingIdList) {
		super(parent);
		
		this.jobRWInfo = jobRWInfo;

		this.isAddButton = isAddButton;
		this.existingIdList = existingIdList;
		
		defaultJobType = getDefaultJobType();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite inner = new Composite(parent, SWT.NONE);
		inner.setLayout(new GridLayout(3, false));
		inner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createNameField(inner);

		createResourceTypeField(inner);

		createItemTypeField(inner);

		createClassField(inner);

		createNote(inner);
		
		setValues();

		return inner;
	}
	
	/** 
	 * 기본 Job Reader 또는 Writer 목록 생성
	 * 
	 * @return
	 */
	protected DefaultJobRWMap getDefaultJobType(){
		return null;
	}
	
	/** 기본 값 설정 */
	private void setValues(){
		nameText.setText(StringUtil.returnEmptyStringIfNull(jobRWInfo.getName()));
		
		String resourceType = jobRWInfo.getResourceType();
		if(NullUtil.isEmpty(resourceType)){
			resourceTypeCombo.setText(DEFAULT);
		}else{
			resourceTypeCombo.setText(resourceType);
		}
		resourceTypeCombo.notifyListeners(SWT.Selection, null);
		
		String itemType = jobRWInfo.getItemType();
		if(!NullUtil.isEmpty(itemType)){
			itemTypeCombo.setText(itemType);
		}
		itemTypeCombo.notifyListeners(SWT.Selection, null);
		
		classText.setText(StringUtil.returnEmptyStringIfNull(jobRWInfo.getClassValue()));
	}

	/**
	 * Name Field 생성
	 * 
	 * @param control
	 */
	private void createNameField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label nameLabel = new Label(control, SWT.None);
		nameLabel.setText(BConfMngtMessages.JobRWDialog_NAME_LABEL);

		nameText = new Text(control, SWT.BORDER);
		nameText.setLayoutData(horizontalSpanTwo);

		if (!isAddButton) {
			nameText.setEnabled(false);
		}
		
		nameText.addListener(SWT.Modify, validation);
	}

	/**
	 * ResourceType Field 생성
	 * 
	 * @param control
	 */
	private void createResourceTypeField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label resourceTypeLabel = new Label(control, SWT.None);
		resourceTypeLabel.setText(BConfMngtMessages.JobRWDialog_RESOURCE_TYPE_LABEL);

		String[] resourceItems = new String[] { DEFAULT, FILE, DB };

		resourceTypeCombo = new Combo(control, SWT.DROP_DOWN | SWT.READ_ONLY);
		resourceTypeCombo.setItems(resourceItems);
		
		resourceTypeCombo.setLayoutData(horizontalSpanTwo);
		
		resourceTypeCombo.addListener(SWT.Selection, validation);
		resourceTypeCombo.addListener(SWT.Selection, ResourceTypeComboListener);
		
	}

	/**
	 * ItemType Field 생성
	 * 
	 * @param control
	 */
	private void createItemTypeField(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Label itemTypeLabel = new Label(control, SWT.None);
		itemTypeLabel.setText(description + BConfMngtMessages.JobRWDialog_ITEM_TYPE_LABEL);

		itemTypeCombo = new Combo(control, SWT.DROP_DOWN | SWT.READ_ONLY);
		itemTypeCombo.setLayoutData(horizontalSpanTwo);

		itemTypeCombo.addListener(SWT.Selection, ItemTypeComboListener);
		itemTypeCombo.addListener(SWT.Selection, validation);
	}

	/**
	 * Class Field 생성
	 * 
	 * @param control
	 */
	private void createClassField(Composite control) {
		Label classLabel = new Label(control, SWT.None);
		classLabel.setText(BConfMngtMessages.JobRWDialog_CLASS_LABEL);

		classText = new Text(control, SWT.BORDER);
		classText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		classText.setEnabled(false);

		browseSelectButton = new Button(control, SWT.PUSH);
		browseSelectButton.setText(BConfMngtMessages.JobRWDialog_BROWSE_BUTTON);
		browseSelectButton.addListener(SWT.Selection, browseListener);
		browseSelectButton.setEnabled(false);
		
		classText.addListener(SWT.Modify, validation);
	}

	/**
	 * Note 생성
	 * 
	 * @param control
	 * @return
	 */
	protected Composite createNote(Composite control) {
		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 3;

		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(horizontalSpanTwo);

		Label noteLabel = new Label(noteControl, SWT.None);
		noteLabel.setText(BConfMngtMessages.JobRWDialog_NOTE_LABEL);
		StringUtil.setLabelStringBold(noteLabel);
		
		return noteControl;
	}

	/** 클래스 정보 search기능 */
	Listener browseListener = new Listener() {

		public void handleEvent(Event event) {

			FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(
					getShell(), false, PlatformUI.getWorkbench()
							.getProgressService(),
					SearchEngine.createWorkspaceScope(),
					IJavaSearchConstants.CLASS);
			dialog.setTitle(BConfMngtMessages.JobRWDialog_BROWSE_BUTTON_DIALOG_TITLE);
			dialog.setMessage(BConfMngtMessages.JobRWDialog_BROWSE_BUTTON_DIALOG_DESCRIPTION);
			dialog.setInitialPattern(""); //$NON-NLS-1$

			if (dialog.open() == Window.OK) {
				Object type = dialog.getFirstResult();

				if (!NullUtil.isNull(type)) {
					if (type instanceof BinaryType) {
						classText.setText(((BinaryType) type)
								.getFullyQualifiedName());
					} else if (type instanceof SourceType) {
						classText.setText(((SourceType) type)
								.getFullyQualifiedName());
					}
				}
				return;
			}

		}
	};
	
	/** ResourceType Combo의 Listener */
	Listener ResourceTypeComboListener = new Listener() {
	
		public void handleEvent(Event event) {
			String type = resourceTypeCombo.getText();
			ArrayList<String> itemTypeList = new ArrayList<String>();
			itemTypeList.add(DEFAULT);
			
			if(DEFAULT.equals(type)){				
				classText.setText("");
				classText.setEnabled(false);
				browseSelectButton.setEnabled(false);
			}else if(FILE.equals(type)){
				itemTypeList.addAll(defaultJobType.getFileTypeList());
			}else if(DB.equals(type)){
				itemTypeList.addAll(defaultJobType.getDBTypeList());
			}
			
			itemTypeCombo.setItems(itemTypeList.toArray(new String[0]));
			itemTypeCombo.setText(DEFAULT);
		}
	};
	
	/** ItemType Combo의 Listener */
	Listener ItemTypeComboListener = new Listener() {
		
		public void handleEvent(Event event) {
			String type = itemTypeCombo.getText();
			
			String classValue = defaultJobType.getClassValue(type);
			
			if(DEFAULT.equals(type)){
				classText.setText("");
				classText.setEnabled(false);
				browseSelectButton.setEnabled(false);
			}else if(isCustomizeType(classValue)){
				classText.setText("");
				classText.setEnabled(true);
				browseSelectButton.setEnabled(true);
			}else{
				classText.setText(classValue);
				classText.setEnabled(false);
				browseSelectButton.setEnabled(false);
			}
		}
	};
	
	/** CustomizeType 여부 확인 */
	private boolean isCustomizeType(String name){
		if(NullUtil.isEmpty(name)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void okPressed() {
		jobRWInfo.setName(nameText.getText());
		jobRWInfo.setResourceType(resourceTypeCombo.getText());
		jobRWInfo.setItemType(itemTypeCombo.getText());
		jobRWInfo.setClassValue(classText.getText());
		jobRWInfo.setResourceDetailType(defaultJobType.getDetailType(itemTypeCombo.getText()));

		super.okPressed();
	}

	/** 유효성 검사 */
	Listener validation = new Listener(){
		
		public void handleEvent(Event event) {
			StatusInfo status = new StatusInfo();
			status = new StatusInfo();
	
			if (isAddButton) {
				String id = nameText.getText();
				if (id.length() == 0) {
					status.setError(BConfMngtMessages.JobRWDialog_EMPTY_NAME);
					updateStatus(status);
					return;
				} else {
					if (!StringUtil.isBatchJobBeanIDAvailable(id)) {
						status.setError(BConfMngtMessages.JobRWDialog_INVALID_NAME);
						updateStatus(status);
						return;
					}
					if (existingIdList.contains(id)) {
						status.setError(BConfMngtMessages.JobRWDialog_DUPLICATE_NAME);
						updateStatus(status);
						return;
					}
				}
			}
			
			String resourceType = resourceTypeCombo.getText();
			if(DEFAULT.equals(resourceType)){
				status.setError(BConfMngtMessages.JobRWDialog_EMPTY_RESOURCE_TYPE);
				updateStatus(status);
				return;
			}
			
			String itemType = itemTypeCombo.getText();
			if(DEFAULT.equals(itemType)){
				status.setError(description+BConfMngtMessages.JobRWDialog_EMPTY_TYPE);
				updateStatus(status);
				return;
			}
	
			if (classText != null) {
				String classValueString = classText.getText();
				if (classValueString.length() < 1) {
					status.setError(BConfMngtMessages.JobRWDialog_EMPTY_CLASS);
					updateStatus(status);
					return;
				} else if (!isClassNameOfBatchPreferenceAvailable(classValueString)) {
					status.setError(BConfMngtMessages.JobRWDialog_INVALID_CLASS_VALUE);
					updateStatus(status);
					return;
				}
			}
	
			status.setOK();
			updateStatus(status);
		}
	};

	/**
	 * Class명의 유효성 검사
	 * 
	 * @param className
	 * @return
	 */
	protected boolean isClassNameOfBatchPreferenceAvailable(String className) {
		if (StringUtil.hasKorean(className)
				|| StringUtil.hasInvalidClassFileSignal(className)
				|| StringUtil.hasEmptySpace(className)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * jobRWInfo의 값을 가져온다
	 * 
	 * @return the jobRWInfo
	 */
	public JobRWInfo getJobRWInfo() {
		return jobRWInfo;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(380, 250);
	}

}
