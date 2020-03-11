package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.TypeCSqlKeyValueVo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * SqlPagingQueryJdbcItemReader의 Context에 저장되는 Key, Value를 입력하는 dialog
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.10.10	조용현	최초생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class SqlKeyValueTableDialog extends StatusDialog {

	/** Key 값 */
	private Text keyText = null;
	
	/** Value 값*/
	private Text valueText = null;
	
	/** 기존 vo 및 리턴한 vo를 저장하는 Vo*/
	private TypeCSqlKeyValueVo vo = null;
	
	/**
	 * SqlKeyValueTableDialog 생성자
	 * 
	 * @param parent
	 *
	 */
	public SqlKeyValueTableDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(2, false));
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label keyLabel = new Label(control, SWT.None);
		keyLabel.setText(BatchMessages.SqlKeyValueTableDialog_KEY_LABEL);
		
		keyText = new Text(control, SWT.BORDER);
		keyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		keyText.addListener(SWT.Modify, validation);
		
		Label valueLabel = new Label(control, SWT.None);
		valueLabel.setText(BatchMessages.SqlKeyValueTableDialog_KEY_VALUE);
		
		valueText = new Text(control, SWT.BORDER);
		valueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		valueText.addListener(SWT.Modify, validation);
		
		validation.handleEvent(null);
		
		return control;
	}

	@Override
	protected void okPressed() {
		vo = new TypeCSqlKeyValueVo(keyText.getText(), valueText.getText());
		
		super.okPressed();
	}
	
	/**
	 * 입력한 TypeCSqlKeyValueVo를 가져온다.
	 * 
	 * @return
	 */
	public TypeCSqlKeyValueVo getVo(){
		return vo;
	}
	
	/** Key, Value의 유효성 검사 */
	Listener validation = new Listener() {
		
		public void handleEvent(Event event) {			
			if(!validateKey(keyText.getText())){
				return;
				
			}else if(!validateValue(valueText.getText())){
				return;
				
			}
			
			StatusInfo status = new StatusInfo();
			status.setOK();
			updateStatus(status);
		}
		
		/** 
		 * Key의 유효성 검사
		 * 
		 * @param key
		 * @return
		 */
		protected boolean validateKey(String key){
			StatusInfo status = new StatusInfo();
			
			if (NullUtil.isEmpty(key)) {
				status.setError("Key를 입력해 주십시오.");
				updateStatus(status);
				return false;
			}
			
			if(!isSqlTableKeyValueValid(key)){
				status.setError("Key값이 유효하지 않습니다.");
				updateStatus(status);
				return false;
			}
			
			return true;
		}
		
		/**
		 * Value의 유효성 검사
		 * 
		 * @param value
		 * @return
		 */
		protected boolean validateValue(String value){
			StatusInfo status = new StatusInfo();
			
			if (NullUtil.isEmpty(value)) {
				status.setError("Value를 입력해 주십시오.");
				updateStatus(status);
				return false;
			}
			
			if(!isSqlTableKeyValueValid(value)){
				status.setError("Value값이 유효하지 않습니다.");
				updateStatus(status);
				return false;
			}
			
			return true;
		}
		
		/**
		 * Key, Value의 String 유효성 검사
		 * 
		 * @param string
		 * @return
		 */
		private boolean isSqlTableKeyValueValid(String string){
			char invalidChar = '\'';
			String pattern = StringUtil.KOR_PATTERN + invalidChar;
			
			if(StringUtil.doesCharacterOfStringBelongToPatternAtleastOne(pattern, string)){
				return false;
			}else{
				return true;
			}
		}
	};

}
