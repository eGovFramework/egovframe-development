package egovframework.bdev.imp.confmngt.preferences.parameter;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterComboString;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * Batch Job Info를 등록하는 다이어로그 클래스
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see
 * 
 *      <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 *      </pre>
 */
@SuppressWarnings("restriction")
public class JobParameterDialog extends StatusDialog {

	/** Add button에서 Open했는지 여부 */
	protected final boolean isAddButton;

	/** 중복 검사를 위한 기존 TableViewer의 item 목록 */
	protected final List<String> existingIdList;

	/** 선택한 JobParameter */
	private JobParameterInfo jobParameter = null;

	/** ParameterName 입력 Field */
	private Text parameterNameField;

	/** Value 입력 Field */
	private Text valueField;

	/** DataType 선택 Field */
	private Combo dataTypeField;

	/** DateFormat 선택 Field */
	private Combo dateFormatField;

	/** Date Format List */

	/**
	 * JobDialog의 생성자
	 * 
	 * @param shell
	 * @param isAddButton
	 * @param existingIdList
	 * @param jobParameter
	 */
	public JobParameterDialog(Shell shell, boolean isAddButton, List<String> existingIdList,
			JobParameterInfo jobParameter) {
		super(shell);

		this.jobParameter = new JobParameterInfo();

		this.isAddButton = isAddButton;
		this.existingIdList = existingIdList;

		if (!NullUtil.isNull(jobParameter)) {
			this.jobParameter.copyValues(jobParameter);
		}

		if (isAddButton) {
			setTitle(BConfMngtMessages.JobDialog_NEW_JOB_PARAMETER_TITLE);
		} else {
			setTitle(BConfMngtMessages.JobDialog_EDIT_JOB_PARAMETER_TITLE);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite inner = new Composite(parent, SWT.NONE);
		inner.setLayout(new GridLayout(2, false));
		inner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createNameField(inner);

		createValueField(inner);

		createDataTypeField(inner);

		createFormatField(inner);

		createNote(inner);

		return inner;
	}

	/**
	 * Name Field 생성
	 * 
	 * @param control
	 */
	private void createNameField(Composite control) {
		Label parameterNameLabel = new Label(control, SWT.None);
		parameterNameLabel.setText(BConfMngtMessages.JobDialog_PARAMETER_NAME_LABEL);

		parameterNameField = new Text(control, SWT.BORDER);
		parameterNameField.setText(StringUtil.returnEmptyStringIfNull(jobParameter.getParameterName()));
		parameterNameField.addListener(SWT.Modify, validation);
		parameterNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (!isAddButton) {
			parameterNameField.setEnabled(false);
		} else {
			parameterNameField.forceFocus();
		}
	}

	/**
	 * Value Field 생성
	 * 
	 * @param control
	 */
	private void createValueField(Composite control) {
		Label valueLabel = new Label(control, SWT.None);
		valueLabel.setText(BConfMngtMessages.JobDialog_VALUE_LABEL);

		valueField = new Text(control, SWT.BORDER);
		valueField.setText(StringUtil.returnEmptyStringIfNull(jobParameter.getValue()));
		valueField.addListener(SWT.Modify, validation);
		valueField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/**
	 * DataType Field 생성
	 * 
	 * @param control
	 */
	private void createDataTypeField(Composite control) {
		String[] list = new String[] { JobParameterComboString.DEFAULT, JobParameterComboString.STRING,
				JobParameterComboString.LONG, JobParameterComboString.DATE, JobParameterComboString.DOUBLE }; // $NON-NLS-1$
																												// //$NON-NLS-2$
																												// //$NON-NLS-3$
																												// //$NON-NLS-4$

		Label dataTypeLabel = new Label(control, SWT.None);
		dataTypeLabel.setText(BConfMngtMessages.JobDialog_DATA_TYPE_LABEL);

		dataTypeField = new Combo(control, SWT.DROP_DOWN | SWT.READ_ONLY);
		dataTypeField.setItems(list);
		dataTypeField.addListener(SWT.Selection, dataTypeListener);
		dataTypeField.addListener(SWT.Selection, validation);
		dataTypeField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		String dataType = jobParameter.getDataType();
		if (NullUtil.isEmpty(dataType)) {
			dataType = JobParameterComboString.DEFAULT;
		}
		dataTypeField.setText(dataType);
	}

	/**
	 * Format Field 생성
	 * 
	 * @param control
	 */
	private void createFormatField(Composite control) {
		Label dateforLabel = new Label(control, SWT.None);
		dateforLabel.setText(BConfMngtMessages.JobDialog_FORMAT_LABEL);

		dateFormatField = new Combo(control, SWT.DROP_DOWN);
		dateFormatField.setItems(getFormatList().keySet().toArray(new String[] {}));
		dateFormatField.addListener(SWT.Modify, validation);
		dateFormatField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (NullUtil.isEmpty(jobParameter.getDateFormat())) {
			dateFormatField.setText(""); //$NON-NLS-1$
			dateFormatField.setEnabled(false);
		} else {
			dateFormatField.setText(jobParameter.getDateFormat());
			dateFormatField.setEnabled(true);
		}
	}

	/**
	 * Note 생성
	 * 
	 * @param control
	 */
	private void createNote(Composite control) {

		GridData horizontalSpanTwo = new GridData(GridData.FILL_HORIZONTAL);
		horizontalSpanTwo.horizontalSpan = 2;

		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(horizontalSpanTwo);

		Label noteLabel = new Label(noteControl, SWT.None);
		noteLabel.setText(BConfMngtMessages.JobDialog_NOTE);
		StringUtil.setLabelStringBold(noteLabel);

		Label contentLabel = new Label(noteControl, SWT.None);
		if (isAddButton) {
			contentLabel.setText(BConfMngtMessages.JobDialog_ADD_NOTE_CONTENTS);
		} else {
			contentLabel.setText(BConfMngtMessages.JobDialog_EDIT_NOTE_CONTENTS);
		}
	}

	/**
	 * ParameterName 유효성 검사
	 * 
	 * @param name
	 * @return
	 */
	private boolean isJobParameterNameAvailable(String name) {
		if ("timestamp".equals(name)) { //$NON-NLS-1$
			return false;

		} else {
			String dataType = dataTypeField.getText();

			if (JobParameterComboString.DATE.equals(dataType)) {
				return isParameterNameAvailableWhenDateType(name);

			} else {
				return StringUtil.isBatchJobBeanIDAvailable(name);

			}

		}
	}

	/**
	 * DateType인 경우 ParameterName의 유효성 검사
	 * 
	 * @param name
	 * @return
	 */
	private boolean isParameterNameAvailableWhenDateType(String name) {
		char validChar = '_';
		String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN + validChar;

		if (StringUtil.doesStringMatchWithPatten(pattern, name)) {
			if (!StringUtil.isStringStartWithNumber(name)) {
				return true;
			}
		}

		return false;
	}

	/** Dialog의 validation */
	Listener validation = new Listener() {
		public void handleEvent(org.eclipse.swt.widgets.Event event) {
			StatusInfo status = new StatusInfo();
			String parameterName = null;
			String value = null;

			parameterName = parameterNameField.getText();
			if (isAddButton) {
				if (parameterName.length() == 0) {
					status.setError(BConfMngtMessages.JobDialog_EMPTY_PARAMETER_NAME);
					updateStatus(status);
					return;
				} else {
					if (!isJobParameterNameAvailable(parameterName)) {
						status.setError(BConfMngtMessages.JobDialog_INALID_PARAMETER_NAME);
						updateStatus(status);
						return;
					}
					if (existingIdList.contains(parameterName)) {
						status.setError(BConfMngtMessages.JobDialog_DUPLICATE_PARAMETER_NAME);
						updateStatus(status);
						return;
					}
				}
			}

			if (JobParameterComboString.DEFAULT.equals(dataTypeField.getText())) {
				status.setError(BConfMngtMessages.JobParameterDialog_EMPTY_DATA_TYPE);
				updateStatus(status);
				return;
			}

			if (JobParameterComboString.DATE.equals(dataTypeField.getText())
					&& NullUtil.isEmpty(dateFormatField.getText())) {
				status.setError(BConfMngtMessages.JobDialog_EMPTY_FORMAT);
				updateStatus(status);
				return;
			}

			if (!NullUtil.isNull(valueField)) {
				value = valueField.getText();

				if (value.length() < 1) {
					status.setError(BConfMngtMessages.JobDialog_EMPTY_VALUE);
					updateStatus(status);
					return;
				}

				if (!valueValidation(value)) {
					status.setError(BConfMngtMessages.JobDialog_VALUE_IS_NOT_MATCH_WITH_FORMAT);
					updateStatus(status);
					return;
				}

			}

			status.setOK();
			updateStatus(status);
		}
	};

	@Override
	protected void okPressed() {
		jobParameter.setParameterName(parameterNameField.getText());
		jobParameter.setValue(valueField.getText());
		jobParameter.setDataType(dataTypeField.getText());
		jobParameter.setDateFormat(dateFormatField.getText());

		super.okPressed();
	};

	/**
	 * Value값의 validation
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private boolean valueValidation(String value) {
		String dataTypeString = dataTypeField.getText();

		if (JobParameterComboString.STRING.equals(dataTypeString)) {
			if (!(value.indexOf("\\") > -1) && !(value.indexOf("\"") > -1) && !StringUtil.hasKorean(value) //$NON-NLS-1$ //$NON-NLS-2$
					&& !StringUtil.hasEmptySpace(value)) {
				return true;

			}
		} else if (JobParameterComboString.LONG.equals(dataTypeString)) {
			try {
				Long.parseLong(value);
				return true;

			} catch (Exception e) {
			}

		} else if (JobParameterComboString.DATE.equals(dataTypeString)) {
			return validateDateFormat(value);

		} else if (JobParameterComboString.DOUBLE.equals(dataTypeString)) {
			try {
				Double.parseDouble(value);
				return true;

			} catch (Exception e) {
			}

		}

		return false;
	}

	/** Date Format list 설정 */
	private HashMap<String, String> getFormatList() {
		HashMap<String, String> formatList = new HashMap<String, String>();

		formatList.put(JobParameterComboString.DATE_FORMAT_YYYY_dash_MM_dash_DD,
				JobParameterComboString.DATE_FORMAT_REGEX_DASH);
		formatList.put(JobParameterComboString.DATE_FORMAT_YYYY_slash_MM_slash_DD,
				JobParameterComboString.DATE_FORMAT_REGEX_SLASH);
		formatList.put(JobParameterComboString.DATE_FORMAT_YYYY_dot_MM_dot_DD,
				JobParameterComboString.DATE_FORMAT_REGEX_DOT);
		formatList.put(JobParameterComboString.DATE_FORMAT_YY_dash_MM_dash_DD,
				JobParameterComboString.DATE_FORMAT_REGEX_DASH);
		formatList.put(JobParameterComboString.DATE_FORMAT_MM_dash_DD, JobParameterComboString.DATE_FORMAT_REGEX_DASH);
		formatList.put(JobParameterComboString.DATE_FORMAT_DD, JobParameterComboString.DATE_FORMAT_REGEX_NO_REGEX);

		return formatList;
	}

	/**
	 * <pre>
	 * dataType이 Date인 경우
	 * Value에 대한 validation
	 * </pre>
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private boolean validateDateFormat(String value) {
		boolean result = true;

		String format = dateFormatField.getText();
		String regex = getFormatList().get(format);

		String[] splitedDate = null;
		String[] splitedDateFormat = null;

		if (!NullUtil.isEmpty(regex) && !JobParameterComboString.DATE_FORMAT_REGEX_NO_REGEX.equals(regex)) {

			splitedDate = value.split(regex);
			splitedDateFormat = format.split(regex);

			for (int i = 0; i < splitedDate.length; i++) {
				Integer.parseInt(splitedDate[i]);

				if (splitedDate[i].length() != splitedDateFormat[i].length()) {
					result = false;
					break;
				}
			}

			if (splitedDateFormat.length != splitedDate.length) {
				result = false;
			}
		} else if (JobParameterComboString.DATE_FORMAT_REGEX_NO_REGEX.equals(regex)) {
			Integer.parseInt(value);

			if (value.length() != format.length()) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * jobParameter의 값을 가져온다
	 *
	 * @return the jobParameter
	 */
	public JobParameterInfo getJobParameter() {
		return jobParameter;
	}

	/** DataTypeCombo의 Listener */
	Listener dataTypeListener = new Listener() {
		public void handleEvent(Event event) {
			if (JobParameterComboString.DATE.equals(dataTypeField.getText())) {
				dateFormatField.setEnabled(true);
			} else {
				dateFormatField.setEnabled(false);
				dateFormatField.deselectAll();
			}
		}
	};

	@Override
	protected Point getInitialSize() {
		return new Point(380, 265);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
