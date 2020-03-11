package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchPageBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.SharedValueVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * SharedValueVo TableViewer의 Add 버튼 클릭시 실행되는 Dialog
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
@SuppressWarnings("restriction")
public class SharedValueDialog extends StatusDialog {
	
	/** Apply한 JobVo 및 Step, Decision Vo들의 Bean ID list */
	private BatchXMLFileBeanIDList batchJobListBeanIDList = null;
	
	/** Info 페이지에서 입력된 Bean ID를 저장하는 Bean ID list*/
	private BatchPageBeanIDList pageBeanIDs = null;
	
	/** 현재 StepVo의 ID */
	private String stepID = null;
	
	/** StepVo에서 설정한 Item Reader */
	private JobReaderInfo readerVo = null;
	
	/** StepVo에서 설정한 Item Writer */
	private JobWriterInfo writerVo = null;
	
	/** SharedValueTableViewer의 Item들 */
	private SharedValueVo[] sharedValueList = null;
	
	/** Dialog 실행 결과로 나온 SharedValueVo의 목록 */
	private ArrayList<SharedValueVo> result = null;
	
	/** 해당 Step에서 이전에 체크된 항목 목록 */
	private ArrayList<SharedValueVo> checkedList = null;
	
	/** 해당 Step에서  체크되지 않은 항목 목록 */
	private ArrayList<SharedValueVo> elseList = null;

	/** ReadCount의 String*/
	private String readCountString = null;
	
	/** ReadCount의 CheckBox Button */
	private Button readCount = null;

	/** WrittenCount의 String*/
	private String writtenString = null;
	
	/** WrittenCount의 CheckBox Button */
	private Button written = null;

	/** CurrentCount의 String*/
	private String currentCountString = null;
	
	/** CurrentCount의 CheckBox Button */
	private Button currentCount = null;

	/** 사용자가 입력하는 input Text의 CheckBox Button */
	private Button inputButton = null;
	
	/** 사용자가 입력하는 input Text */
	private Text inputText = null;

	/**
	 * SharedValueDialog의 생성자
	 * 
	 * @param parent
	 * @param sharedValueList
	 * @param stepVo
	 * @param batchJobListBeanIDList
	 * @param pageBeanIDs
	 *
	 */
	public SharedValueDialog(Shell parent, SharedValueVo[] sharedValueList, StepVo stepVo, BatchXMLFileBeanIDList batchJobListBeanIDList, BatchPageBeanIDList pageBeanIDs) {
		super(parent);

		stepID = stepVo.getName();

		this.readerVo = stepVo.getJobReaderInfo();
		this.writerVo = stepVo.getJobWriterInfo();

		setButtonString();

		this.sharedValueList = sharedValueList;

		this.batchJobListBeanIDList = batchJobListBeanIDList;
		this.pageBeanIDs = pageBeanIDs;
		
		setTitle(BatchMessages.SharedValueDialog_TITLE);
	}
	

	@Override
	protected Point getInitialSize() {
		return new Point(350, 200);
	}
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createSelectOptionControl(control);

		createInputOptionControl(control);

		setButtonEnable();

		getCheckedItem(sharedValueList);

		return control;
	}

	/**
	 * 사용자가 선택하는 CheckBox Control 생성
	 * 
	 * @param control
	 */
	private void createSelectOptionControl(Composite control) {

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 2;

		readCount = new Button(control, SWT.CHECK);
		readCount.setLayoutData(gData);
		readCount.setText(readCountString);

		written = new Button(control, SWT.CHECK);
		written.setLayoutData(gData);
		written.setText(writtenString);

		currentCount = new Button(control, SWT.CHECK);
		currentCount.setLayoutData(gData);
		currentCount.setText(currentCountString);

		if (NullUtil.isNull(readerVo)) {
			readCount.setEnabled(false);
		}
		if (NullUtil.isNull(writerVo)) {
			written.setEnabled(false);
			currentCount.setEnabled(false);
		}
	}

	/**
	 * 사용자가 입력하는 Text밑 CheckBox control 생성
	 * 
	 * @param control
	 */
	private void createInputOptionControl(Composite control) {

		inputButton = new Button(control, SWT.CHECK);
		inputButton.setText(BatchMessages.SharedValueDialog_USER_INPUT_CHECK_BOX);

		inputText = new Text(control, SWT.BORDER);
		inputText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inputText.setEnabled(false);
		inputText.addListener(SWT.Modify, inputTextValidation);

		inputButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (inputButton.getSelection()) {
					inputText.setEnabled(true);
					inputText.forceFocus();
					inputTextValidation.handleEvent(new Event());
				} else {
					inputText.setEnabled(false);
					inputText.setText(""); //$NON-NLS-1$
				}
			}
		});
	}

	/**
	 * 각 CheckBox Button에 입력되는 String 생성
	 * 
	 */
	private void setButtonString() {		
		readCountString = createReadCountString(readerVo);
		writtenString = getWrittenString(writerVo);
		currentCountString = getCurrentCountString(writerVo);
	}
	
	/**
	 * ReadCount String 생성
	 * 
	 * @param readerVO
	 * @return
	 */
	private String createReadCountString(JobReaderInfo readerVO){
		return getSimpleJobRWClassName(readerVO)+BatchMessages.SharedValueDialog_READ_COUNT_CHECK_BOX;
	}
	
	/**
	 * Written String 생성
	 * 
	 * @param writerVo
	 * @return
	 */
	private String getWrittenString(JobWriterInfo writerVo){
		return getSimpleJobRWClassName(writerVo) + BatchMessages.SharedValueDialog_WRITTEN_COUNT_CHECK_BOX;
	}
	
	/**
	 * Currentcount String 생성
	 * 
	 * @param writerVo
	 * @return
	 */
	private String getCurrentCountString(JobWriterInfo writerVo){
		return getSimpleJobRWClassName(writerVo) + BatchMessages.SharedValueDialog_CURRENT_COUNT__CHECK_BOX;
	}
	
	/**
	 * CheckBox String에 사용한 Job Reader / Writer의 Simple Name을 가져온다
	 * 
	 * @param jobRWInfo
	 * @return
	 */
	protected String getSimpleJobRWClassName(JobRWInfo jobRWInfo){
		if(!NullUtil.isNull(jobRWInfo)){
			String[] splitClassValue = jobRWInfo.getClassValue().split("[.]"); //$NON-NLS-1$
			
			return splitClassValue[splitClassValue.length-1];
		}else{
			return BatchMessages.SharedValueDialog_EMPTY_JOB_RW_STRING;
		}
	}

	/**
	 * 기존 sharedValueTableViewer Item중 이미 입력된 data와 나머지 data를 분리, 저장한다.
	 * 
	 * @param sharedValueList
	 */
	private void getCheckedItem(SharedValueVo[] sharedValueList) {
		checkedList = new ArrayList<SharedValueVo>();
		elseList = new ArrayList<SharedValueVo>();

		if (!NullUtil.isEmpty(sharedValueList)) {
			for (SharedValueVo vo : sharedValueList) {
				if (NullUtil.isEmpty(vo.getStepId())
						|| vo.getStepId().equals(stepID)) {
					if (vo.getKey().equals(readCountString)) {
						checkedList.add(vo);
						readCount.setSelection(true);
					} else if (vo.getKey().equals(writtenString)) {
						checkedList.add(vo);
						written.setSelection(true);
					} else if (vo.getKey().equals(currentCountString)) {
						checkedList.add(vo);
						currentCount.setSelection(true);
					} else {
						elseList.add(vo);
					}
				} else {
					elseList.add(vo);
					
					if (vo.getKey().equals(readCountString)) {
						readCount.setEnabled(false);
					} else if (vo.getKey().equals(writtenString)) {
						written.setEnabled(false);
					} else if (vo.getKey().equals(currentCountString)) {
						currentCount.setEnabled(false);
					}
				}
			}
		}
	}

	/**
	 * 버튼의 Enable / Disable을 설정한다.
	 * 
	 */
	private void setButtonEnable() {
		if (NullUtil.isNull(readerVo)) {
			readCount.setEnabled(false);
		}
		if (NullUtil.isNull(writerVo)) {
			written.setEnabled(false);
			currentCount.setEnabled(false);
		}
	}

	@Override
	protected void okPressed() {
		result = addCheckedList(elseList);

		super.okPressed();
	}

	/**
	 * Check된 항목들을 기존의 나머지 항목에 더한다.
	 * 
	 * @param sharedValueList
	 * @return
	 */
	private ArrayList<SharedValueVo> addCheckedList(
			ArrayList<SharedValueVo> sharedValueList) {
		
		if (readCount.getSelection()) {
			sharedValueList.add(new SharedValueVo(readCountString, stepID));
		}
		if (written.getSelection()) {
			sharedValueList.add(new SharedValueVo(writtenString, stepID));
		}
		if (currentCount.getSelection()) {
			sharedValueList.add(new SharedValueVo(currentCountString, stepID));
		}
		if (inputButton.getSelection()) {
			sharedValueList.add(new SharedValueVo(inputText.getText(), stepID));
		}

		return sharedValueList;
	}

	/**
	 * result의 값을 가져온다
	 * 
	 * @return the result
	 */
	public ArrayList<SharedValueVo> getResult() {
		return result;
	}
	
	/**
	 * bean ID의 유효성을 검사한다.
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
	 * SharedValue 명의 유효성을 검사한다.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isSharedValueNameAvailable(String name){
		String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN + "._-"; //$NON-NLS-1$
		if(StringUtil.doesStringMatchWithPatten(pattern, name) && !StringUtil.hasEmptySpace(name)){
			return true;
		}else{
			return false;
		}
	}
	
	/** inputText의 validation Listener */
	Listener inputTextValidation = new Listener() {

		public void handleEvent(Event event) {
			StatusInfo status = new StatusInfo();

			// String validation(* ! ? 같은것 ) 처리할 것.
			if (inputButton.getSelection()) {
				if (NullUtil.isEmpty(inputText)) {
					status.setError(BatchMessages.SharedValueDialog_EMPTY_KEY);
					updateStatus(status);
					return;
				} else {
					String inputKey = inputText.getText();
					
					if(!isSharedValueNameAvailable(inputKey)){
						status.setError(BatchMessages.SharedValueDialog_INVALID_KEY);
						updateStatus(status);
						return;
					}
					
					if (!NullUtil.isEmpty(elseList)) {
						for (SharedValueVo vo : elseList) {
							if (inputKey.equals(vo.getKey())) {
								status.setError(BatchMessages.SharedValueDialog_EMPTY_VALUE);
								updateStatus(status);
								return;
							}
						}
					}
					
					if(!validateBeanID(inputKey)){
						status.setError(BatchMessages.SharedValueDialog_DUPLICATE_BEAN_ID);
						updateStatus(status);
						return;
					}
				}
			}

			status.setOK();
			updateStatus(status);
		}
	};

}
