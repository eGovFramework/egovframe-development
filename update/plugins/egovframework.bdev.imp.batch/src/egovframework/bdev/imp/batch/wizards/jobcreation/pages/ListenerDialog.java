package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.confmngt.preferences.listeners.ListenerTabContents;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * <pre>
 * BatchJobCreationCustomizePage에서
 * Job, Step, Chunk Listener TableViewer에 추가, 수정할때 여는 Dialog
 * 
 * type 1: Job Listener 
 * type 2: Step Listener 
 * type 3: Chunk Listener
 * </pre>
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
@SuppressWarnings("restriction")
public class ListenerDialog extends StatusDialog {

	/** 선택된 Job / Step / Chunk Listener를 가지는 Vo의 ArrayList */
	private ListenerInfo[] info = null;

	/** Job / Step / Chunk Listener TableViewer를 생성 */
	private ListenerTabContents contents = null;

	/** 기존에 선택한 Job / Step / Chunk Listener 목록 */
	private String[] items = null;
	
	/** Apply한 JobVo 및 Step, Decision Vo들의 Bean ID list */
	private BatchXMLFileBeanIDList batchJobListBeanIDList = null;
	
	/** Info 페이지에서 입력된 Bean ID를 저장하는 Bean ID list*/
	private BatchPageBeanIDList pageBeanIDs = null;
	
	/** Description 1 */
	private String descriptionOneString = null;
	
	/** Description 2 */
	private String descriptionTwoString = null;

	/**
	 * contents의 값을 설정한다.
	 *
	 * @param contents
	 */
	public void setContents(ListenerTabContents contents) {
		this.contents = contents;
	}

	/**
	 * descriptionOneString의 값을 설정한다.
	 *
	 * @param descriptionOneString
	 */
	public void setDescriptionOneString(String descriptionOneString) {
		this.descriptionOneString = descriptionOneString;
	}

	/**
	 * descriptionTwoString의 값을 설정한다.
	 *
	 * @param descriptionTwoString
	 */
	public void setDescriptionTwoString(String descriptionTwoString) {
		this.descriptionTwoString = descriptionTwoString;
	}

	/**
	 * info의 값을 가져온다
	 * 
	 * @return the info
	 */
	public ListenerInfo[] getInfo() {
		return info;
	}

	/**
	 * <pre>
	 * ListenerInfoToWizardDialog 생성자
	 * type 1: Job Listener
	 * type 2: Step Listener
	 * type 3: Chunk Listener
	 * </pre
	 * 
	 * ?
	 * 
	 * @param parent
	 * @param type
	 */
	public ListenerDialog(Shell parent, String[] items,
			BatchXMLFileBeanIDList batchJobListBeanIDList,
			BatchPageBeanIDList pageBeanIDs) {
		super(parent);
		this.items = items;
		this.batchJobListBeanIDList = batchJobListBeanIDList;
		this.pageBeanIDs = pageBeanIDs;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridData tableGData = new GridData(GridData.FILL_HORIZONTAL);
		tableGData.heightHint = 400;
		
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(tableGData);

		Label descriptionOne = new Label(control, SWT.None);
		descriptionOne.setText(descriptionOneString);
		StringUtil.setLabelStringBold(descriptionOne);

		Label descriptionTwo = new Label(control, SWT.None);
		descriptionTwo.setText(descriptionTwoString);

		Link link = new Link(control, SWT.RIGHT);
		link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		link.setText(BatchMessages.ListenerDialog_LINK);
		link.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				PreferencesUtil
						.createPreferenceDialogOn(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell(),
								"egovframework.bdev.imp.confmngt.preferences.listenerpreperencepage", //$NON-NLS-1$
								new String[] { "egovframework.bdev.imp.confmngt.preferences.listenerpreperencepage" }, //$NON-NLS-1$
								null).open();
				contents.refreshInputData();
				
				checkPreExistItems();
			}
		});
		
		BatchTableColumn[] columns = createColumns();
		
		contents.setTableColumnProperty(columns);
		contents.createTableViewerContents(control);
		contents.getTableViewer().addSelectionChangedListener(validation);
		contents.getTableViewer().getTable().addListener(SWT.Selection, setCheckedItem);
		

		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout(2, true));
		buttonControl.setLayoutData(new GridData());

		Button selectAll = new Button(buttonControl, SWT.PUSH);
		selectAll.setText(BatchMessages.ListenerDialog_SELECT_ALL_BUTTON);
		selectAll.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectAll.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				CheckboxTableViewer contentsTableViewer = (CheckboxTableViewer) contents.getTableViewer();
				contentsTableViewer.setAllChecked(true);
				validation.selectionChanged(null);
			}
		});

		Button deselectAll = new Button(buttonControl, SWT.PUSH);
		deselectAll.setText(BatchMessages.ListenerDialog_DESELECT_ALL_BUTTON);
		deselectAll.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		deselectAll.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				CheckboxTableViewer contentsTableViewer = (CheckboxTableViewer) contents.getTableViewer();
				contentsTableViewer.setAllChecked(false);
				validation.selectionChanged(null);
			}
		});

		// 기존 선택된 항목은 table에서 체크된 상태로 있도록!
		checkPreExistItems();

		return control;
	}
	
	/**
	 * Column 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createColumns(){
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BatchMessages.ListenerDialog_COLUMN_NAME, 175));
		columns.add(new BatchTableColumn(BatchMessages.ListenerDialog_COLUMN_CLASS, 150, SWT.LEFT));
		columns.add(new BatchTableColumn(BatchMessages.ListenerDialog_COLUMN_LISTENER_TYPE, 122));
		
		return columns.toArray(new BatchTableColumn[0]);
	}
	
	/** 기존에 선택된 항목을 Link를 통해서 수정하고 난 뒤에도 계속 체크된 상태를 유지하도록 설정 */
	private void checkPreExistItems(){
		if (!NullUtil.isEmpty(items)) {
			CheckboxTableViewer tableViewer = (CheckboxTableViewer) contents
					.getTableViewer();
			Item[] preferenceItems = tableViewer.getTable().getItems();

			if (!NullUtil.isEmpty(preferenceItems)) {
				for (int i = 0; i < items.length; i++) {
					for (int j = 0; j < preferenceItems.length; j++) {
						ListenerInfo info = (ListenerInfo) preferenceItems[j]
								.getData();
						if (items[i].equals(info.getName())) {
							tableViewer.setChecked(info, true);
							break;
						}
					}
				}
			}
		}
	}
	
	/** Dialog의 validation Listener */
	ISelectionChangedListener validation = new ISelectionChangedListener() {

		public void selectionChanged(SelectionChangedEvent event) {
			StatusInfo status = new StatusInfo();
			
			CheckboxTableViewer chTableViewer = (CheckboxTableViewer) contents
			.getTableViewer();
			Object[] object = chTableViewer.getCheckedElements();
			for (int i = 0; i < object.length; i++) {
				ListenerInfo info = (ListenerInfo)object[i];
				if(!validateBeanID(info.getName())){
					status.setError(info.getName()+BatchMessages.ListenerDialog_DUPLICATE_BEAN_ID_ERROR);
					updateStatus(status);
					return;
				}
			}
			
			status.setOK();
			updateStatus(status);
		}
	};
	
	
	/** Check한 Item 목록 생성하는 Listener */
	Listener setCheckedItem = new Listener() {
		public void handleEvent(Event event) {
			ListenerInfo[] checkedItems = getCheckedItem();
			String[] checkedItemsName = new String[checkedItems.length];
			
			for(int i =0; i < checkedItems.length; i++){
				checkedItemsName[i] = checkedItems[i].getName();
			}
			
			items = checkedItemsName;
		}
	};

	@Override
	protected void okPressed() {
		info = getCheckedItem();

		super.okPressed();
	}
	
	/**
	 * Bean ID의 validation 
	 * 
	 * @param beanID
	 * @return
	 */
	private boolean validateBeanID(String beanID){
		if(pageBeanIDs.isBeanIDExist(beanID)){
			return false;
		}
		
		if(batchJobListBeanIDList.isBeanIDExistIncludeJobRWBeanList(beanID)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check한 Item 목록 생성
	 * 
	 * @return
	 */
	private ListenerInfo[] getCheckedItem(){
		CheckboxTableViewer chTableViewer = (CheckboxTableViewer) contents.getTableViewer();
		
		Object[] object = chTableViewer.getCheckedElements();
		ListenerInfo[] infos = new ListenerInfo[object.length];
		for (int i = 0; i < object.length; i++) {
			infos[i] = (ListenerInfo) object[i];
		}
		
		return infos;
	}

}
