package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.NextVo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * <pre>
 * BatchJobCreationCustomizePage에서
 * NextVo의 TableViewer에 추가, 수정할때 여는 Dialog
 * </pre>
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
public class NextDialog extends StatusDialog {
	
	/** Add Button에서 open했는지 Edit Button에서 open했는지 구분 */
	private boolean isAddButton;
	
	/** Edit Button이 open한 경우 기존 선택한 NextVo */
	private NextVo nextVo = null;
	
	/** NextVo TableViewer에 있는 NextVo의 목록 */
	private NextVo[] nextVos = null;
	
	/** Next On을 선택하는 ComboBox */
	private Combo nextOnField = null;
	
	/** Next To을 선택하는 ComboBox */
	private Combo nextToField = null;
	
	/** 입력한 Next On 값 */
	private String nextOn = null;
	
	/** 입력한 Next To 값 */
	private String nextTo = null;
	
	/** Next To에 입력할 수 있는 Step/Decision 목록 */
	private String[] stepAndDecisionNameList = null;

	/**
	 * NextDialog 생성자
	 * 
	 * @param parent
	 * @param nextVo
	 * @param stepAndDecisionNameList
	 * @param nextVos
	 *
	 */
	public NextDialog(Shell parent, NextVo nextVo, String[] stepAndDecisionNameList, NextVo[] nextVos) {
		super(parent);
		this.stepAndDecisionNameList = stepAndDecisionNameList;
		this.nextVo = nextVo;
		this.nextVos = nextVos;
		
		isAddButton = (nextVo == null);
		if(isAddButton){
			setTitle(BatchMessages.NextDialog_NEW_TITLE);
		}else{
			setTitle(BatchMessages.NextDialog_EDIT_TITLE);
		}
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label nextOnLabel = new Label(control, SWT.None);
		nextOnLabel.setText(BatchMessages.NextDialog_NEXT_ON_LABEL);
		
		nextOnField = new Combo(control, SWT.BORDER);
		nextOnField.setItems(new String[]{"COMPLETED", "FAILED", "*", BatchMessages.NextDialog_INPUT_VALUE_BY_SELF}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		nextOnField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nextOnField.select(0);
		nextOnField.addListener(SWT.Modify, validation);
		
		Label nextTo = new Label(control, SWT.None);
		nextTo.setText(BatchMessages.NextDialog_NEXT_TO_LABEL);
		
		nextToField = new Combo(control, SWT.BORDER | SWT.READ_ONLY);
		nextToField.setItems(stepAndDecisionNameList);
		nextToField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nextToField.select(0);
		nextToField.addListener(SWT.Modify, validation);
		
		if(nextVo != null){
			nextOnField.setText(nextVo.getNextOn());
			nextToField.setText(nextVo.getNextTo());
		}
		
		validation.handleEvent(new Event());
		
		return control;
	}
	
	@Override
	protected void okPressed() {
		nextOn = nextOnField.getText();
		nextTo = nextToField.getText();		
		super.okPressed();
	}

	
	/**
	 * nextOn의 값을 가져온다
	 *
	 * @return the nextOn
	 */
	public String getNextOn() {
		return nextOn;
	}

	/**
	 * nextTo의 값을 가져온다
	 *
	 * @return the nextTo
	 */
	public String getNextTo() {
		return nextTo;
	}
	
	/**
	 * NextOn String의 유효성 검사
	 * 
	 * @param string
	 * @return
	 */
	private boolean isNextOnStringAvailable(String string){
		if(StringUtil.hasKorean(string) || StringUtil.hasEmptySpace(string)){
			return false;
		}else{
			return true;
		}
	}

	/** Next On, Next To에 대한 Validation */
	Listener validation = new Listener() {
		public void handleEvent(Event event) {
			StatusInfo status = new StatusInfo();
			
			if(NullUtil.isEmpty(nextOnField)){
				status.setError(BatchMessages.NextDialog_EMPTY_NEXT_ON_ERROR_MESSAGE);
				updateStatus(status);
				return;
			}else{
				if(!isNextOnStringAvailable(nextOnField.getText())){
					status.setError(BatchMessages.NextDialog_INVALID_NEXT_ON_ERROR_MESSAGE);
					updateStatus(status);
					return;
				}
				for(NextVo nextVo : nextVos){
					if(nextVo.getNextOn().equals(nextOnField.getText())){
						status.setError(BatchMessages.NextDialog_DUPLICATE_NEXT_ON_ERROR_MESSAGE);
						updateStatus(status);
						return;
					}
				}
			}
			
			if(NullUtil.isEmpty(nextToField)){
				status.setError(BatchMessages.NextDialog_EMPTY_NEXT_TO_ERROR_MESSAGE);
				updateStatus(status);
				return;
			}
			
			status.setOK();
			updateStatus(status);
			
		}
	};
}
