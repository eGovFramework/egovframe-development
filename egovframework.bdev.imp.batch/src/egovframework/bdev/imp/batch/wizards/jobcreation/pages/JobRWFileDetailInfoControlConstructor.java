package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobRWDetailInfoItem;
import egovframework.bdev.imp.batch.wizards.jobcreation.util.JobRWFileDetailInfoValidationUtil;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRW;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;

/**
 * <pre>
 * File Job Reader / Writer Detail Info를 생성
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see
 * 
 * <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.10.10	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class JobRWFileDetailInfoControlConstructor extends
		JobRWDetailInfoControlConstructor {

	/** 유효성을 검사하는 객체 */
	JobRWFileDetailInfoValidationUtil validation = null;

	/** Normal, Partition Type을 구분 */
	final private boolean isNormalType;

	private JobRWInfo jobRWInfo = null;

	/**
	 * JobRWFileDetailInfoControlConstructor 생성자
	 * 
	 * @param detailInfoControl
	 * @param errorSettingLabel
	 * @param isPartitionType
	 * @param context
	 * @param preSavedDetailContext
	 * 
	 */
	protected JobRWFileDetailInfoControlConstructor(
			ScrolledComposite detailInfoControl, Label errorSettingLabel,
			boolean isPartitionType, BatchJobCreationContext context,
			Map<String, String> preSavedDetailContext,
			boolean isPreviousSelectedJobRW) {
		super(detailInfoControl, errorSettingLabel, context,
				preSavedDetailContext, isPreviousSelectedJobRW);

		validation = new JobRWFileDetailInfoValidationUtil(detailContext);
		isNormalType = !isPartitionType;
	}

	@Override
	protected void createDetailInfoContents(Composite control,
			JobRWInfo jobRWInfo) {
		this.jobRWInfo = jobRWInfo;

		String resourceDetailType = jobRWInfo.getResourceDetailType();

		if (DefaultJobRW.FIXED_READER_TYPE.equals(resourceDetailType)) {
			if (isNormalType) {
				createFixedReaderControl(control);
			} else {
				createPartitionFixedReaderControl(control);
			}

		} else if (DefaultJobRW.DELIMITED_READER_TYPE
				.equals(resourceDetailType)) {
			if (isNormalType) {
				createDelimitedReaderControl(control);
			} else {
				createPartitionDelimitedReaderControl(control);
			}

		} else if (DefaultJobRW.DELIMITED_WRITER_TYPE
				.equals(resourceDetailType)) {
			if (isNormalType) {
				createDelimitedWriterControl(control);
			} else {
				createPartitionDelimitedWriterControl(control);
			}

		} else if (DefaultJobRW.FORMATTER_FLAT_FILE_ITEM_WRITER_TYPE
				.equals(resourceDetailType)) {
			if (isNormalType) {
				createFormatterFlatFileItemWriterControl(control);
			} else {
				createPartitionFormatterFlatFileItemWriterControl(control);
			}

		} else if (DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_WRITER_TYPE
				.equals(resourceDetailType)) {
			if (isNormalType) {
				createEgovFixedFlatFileItemWriterControl(control);
			} else {
				createPartitionEgovFixedFlatFileItemWriterControl(control);
			}

		} else if (DefaultJobRW.DELIMITED_MULTI_WRITER_TYPE
				.equals(resourceDetailType)) {
			createDelimitedMultiWriterControl(control);

		} else if (DefaultJobRW.FORMATTER_MULTI_RESOURCE_ITEM_WRITER_TYPE
				.equals(resourceDetailType)) {
			createFormatterMultiResourceItemWriterControl(control);

		} else if (DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER_TYPE
				.equals(resourceDetailType)) {
			createEgovFixedMultiResourceItemWriterControl(control);

		} else if (DefaultJobRW.CUSTOMIZE_FILE_TYPE.equals(resourceDetailType)) {
			createCustomizeFileControl();
		}

	}

	/**
	 * <pre>
	 * Type 1 
	 * FixedReader의 Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createFixedReaderControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addFixedReaderItemListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addFixedReaderItemListener(fieldNameText);

		Text columnLengthText = createColumnLengthControl(control);
		addFixedReaderItemListener(columnLengthText);

		Text voClassText = createVOClassControl(control);
		addFixedReaderItemListener(voClassText);

		setMessage(validation.validateFixedReaderItemAndGetErrorMessage());
	}

	/**
	 * Text에 FixedReaderItem의 Listener 추가
	 * 
	 * @param text
	 */
	private void addFixedReaderItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateFixedReaderItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 2
	 * DeDelimitedReader의 Control 생성
	 * 
	 * @param control
	 */
	private void createDelimitedReaderControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addDelimitedReaderListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addDelimitedReaderListener(fieldNameText);

		Text delimiterText = createDelimiterControl(control);
		addDelimitedReaderListener(delimiterText);

		Text voClassText = createVOClassControl(control);
		addDelimitedReaderListener(voClassText);

		setMessage(validation.validateDelimitedReaderItemAndGetErrorMessage());
	}

	/**
	 * Text에 DelimitedReader Listener 추가
	 * 
	 * @param text
	 */
	private void addDelimitedReaderListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateDelimitedReaderItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * Type3 DelimitedWriter의 Control 생성
	 * 
	 * @param control
	 */
	private void createDelimitedWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addDelimitedWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addDelimitedWriterListener(fieldNameText);

		Text delimiterText = createDelimiterControl(control);
		addDelimitedWriterListener(delimiterText);

		setMessage(validation.validateDelimitedWriterItemAndGetErrorMessage());
	}

	/**
	 * Text에 DelimitedWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addDelimitedWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateDelimitedWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 4-1
	 * FormatterFlatFileItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createFormatterFlatFileItemWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addFormatterFlatFileItemWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addFormatterFlatFileItemWriterListener(fieldNameText);

		Text fieldFormatText = createFieldFormatControl(control);
		addFormatterFlatFileItemWriterListener(fieldFormatText);

		setMessage(validation
				.validateFormatterFlatFileItemWriterItemAndGetErrorMessage());
	}

	/**
	 * Text에 FormatterFlatFileItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addFormatterFlatFileItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateFormatterFlatFileItemWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 4-2
	 * EgovFixedFlatFileItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createEgovFixedFlatFileItemWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addEgovFixedFlatFileItemWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addEgovFixedFlatFileItemWriterListener(fieldNameText);

		Text fieldRangeText = createFieldRangeControl(control);
		addEgovFixedFlatFileItemWriterListener(fieldRangeText);

		setMessage(validation
				.validateEgovFixedFlatFileItemWriterItemAndGetErrorMessage());
	}

	/**
	 * Text에 EgovFixedFlatFileItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addEgovFixedFlatFileItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateEgovFixedFlatFileItemWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 5
	 * DelimitedMultiWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createDelimitedMultiWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addDelimitedMultiWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addDelimitedMultiWriterListener(fieldNameText);

		Text delimiterText = createDelimiterControl(control);
		addDelimitedMultiWriterListener(delimiterText);

		Text maxCountPerResourceText = createMaxCountPerResourceControl(control);
		addDelimitedMultiWriterListener(maxCountPerResourceText);

		setMessage(validation
				.validateDelimitedMultiWriterItemAndGetErrorMessage());
	}

	/**
	 * Text에 DelimitedMultiWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addDelimitedMultiWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateDelimitedMultiWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 6-1
	 * FormatterMultiResourceItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createFormatterMultiResourceItemWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addFormatterMultiResourceItemWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addFormatterMultiResourceItemWriterListener(fieldNameText);

		Text fieldFormatText = createFieldFormatControl(control);
		addFormatterMultiResourceItemWriterListener(fieldFormatText);

		Text maxCountPerResource = createMaxCountPerResourceControl(control);
		addFormatterMultiResourceItemWriterListener(maxCountPerResource);

		setMessage(validation
				.validateFormatterMultiResourceItemWriterItemAndGetErrorMessage());
	}

	/**
	 * FormatterMultiResourceItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addFormatterMultiResourceItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateFormatterMultiResourceItemWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 6-2
	 * EgovFixedMultiResourceItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createEgovFixedMultiResourceItemWriterControl(Composite control) {
		Text resourceText = createResourceControl(control);
		addEgovFixedMultiResourceItemWriterListener(resourceText);

		createResouceExampleLabel(control);

		Text fieldNameText = createFieldNameControl(control);
		addEgovFixedMultiResourceItemWriterListener(fieldNameText);

		Text fieldRangeText = createFieldRangeControl(control);
		addEgovFixedMultiResourceItemWriterListener(fieldRangeText);

		Text maxCountPerResource = createMaxCountPerResourceControl(control);
		addEgovFixedMultiResourceItemWriterListener(maxCountPerResource);

		setMessage(validation
				.validateEgovFixedMultiResourceItemWriterAndGetErrorMessage());
	}

	/**
	 * Text에 EgovFixedMultiResourceItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addEgovFixedMultiResourceItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validateEgovFixedMultiResourceItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 7
	 * CustomizeFile Control 생성
	 * </pre>
	 */
	private void createCustomizeFileControl() {
		setMessageOK();
	}

	/**
	 * <pre>
	 * Type 8
	 * Partition FixedReader Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createPartitionFixedReaderControl(Composite control) {
		Text fieldNameText = createFieldNameControl(control);
		addPartitionFixedReaderListener(fieldNameText);

		Text columnLengthText = createColumnLengthControl(control);
		addPartitionFixedReaderListener(columnLengthText);

		Text voClassText = createVOClassControl(control);
		addPartitionFixedReaderListener(voClassText);

		setMessage(validation.validatePartitionFixedReaderAndGetErrorMessage());
	}

	/**
	 * Text에 PartitionFixedReader Listener 추가
	 * 
	 * @param text
	 */
	private void addPartitionFixedReaderListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validatePartitionFixedReaderAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 9
	 * Partition DelimitedReader Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createPartitionDelimitedReaderControl(Composite control) {
		Text fieldNameText = createFieldNameControl(control);
		addPartitionDelimitedReaderListener(fieldNameText);

		Text delimiter = createDelimiterControl(control);
		addPartitionDelimitedReaderListener(delimiter);

		Text voClassText = createVOClassControl(control);
		addPartitionDelimitedReaderListener(voClassText);

		setMessage(validation
				.validatePartitionDelimitedReaderAndGetErrorMessage());
	}

	/**
	 * Text에 Partition DelimitedReader Listener 추가
	 * 
	 * @param text
	 */
	private void addPartitionDelimitedReaderListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validatePartitionDelimitedReaderAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 10
	 * Partition DelimitedWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createPartitionDelimitedWriterControl(Composite control) {
		Text fieldNameText = createFieldNameControl(control);
		addPartitionDelimitedWriterListener(fieldNameText);

		Text delimiter = createDelimiterControl(control);
		addPartitionDelimitedWriterListener(delimiter);

		setMessage(validation
				.validatePartitionDelimitedWriterAndGetErrorMessage());
	}

	/**
	 * Text에 PartitionDelimitedWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addPartitionDelimitedWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validatePartitionDelimitedWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 11-1
	 * Partition FormatterFlatFileItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createPartitionFormatterFlatFileItemWriterControl(
			Composite control) {
		Text fieldNameText = createFieldNameControl(control);
		addPartitionFormatterFlatFileItemWriterListener(fieldNameText);

		Text fieldFormatText = createFieldFormatControl(control);
		addPartitionFormatterFlatFileItemWriterListener(fieldFormatText);

		setMessage(validation
				.validatePartitionFormatterFlatFileItemWriterAndGetErrorMessage());
	}

	/**
	 * Text에 PartitionFormatterFlatFileItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addPartitionFormatterFlatFileItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validatePartitionFormatterFlatFileItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * <pre>
	 * Type 11-2
	 * Partition EgovFixedFlatFileItemWriter Control 생성
	 * </pre>
	 * 
	 * @param control
	 */
	private void createPartitionEgovFixedFlatFileItemWriterControl(
			Composite control) {
		Text fieldNameText = createFieldNameControl(control);
		addPartitionEgovFixedFlatFileItemWriterListener(fieldNameText);

		Text fieldRangeText = createFieldRangeControl(control);
		addPartitionEgovFixedFlatFileItemWriterListener(fieldRangeText);

		setMessage(validation
				.validatePartitionEgovFixedFlatFileItemWriterItemAndGetErrorMessage());
	}

	/**
	 * Text에 PartitionEgovFixedFlatFileItemWriter Listener 추가
	 * 
	 * @param text
	 */
	private void addPartitionEgovFixedFlatFileItemWriterListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation
						.validatePartitionEgovFixedFlatFileItemWriterItemAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/**
	 * ResourceControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createResourceControl(final Composite control) {
		final Text resourceText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_RESOURCE_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.RESOURCE,
				resourceText);
		setInitialValueToText(JobRWDetailInfoItem.RESOURCE, resourceText);

		Button button = createBrowseButton(control);

		String itemType = jobRWInfo.getItemType();

		if (isFixedItemReader(itemType) || isFixedItemWriter(itemType) || isFormatterItemWriter(itemType)) {
			button.addListener(SWT.Selection,
					getResourceButtonTXTDefaultListeners(control, resourceText));
		} else {
			button.addListener(SWT.Selection,
					getResourceCSVDefaultButtonListener(control, resourceText));
		}

		return resourceText;
	}

	/**
	 * .csv가 기본값으로 되어있는 FileDialog 생성 Listener 반환
	 * 
	 * @param control
	 * @param text
	 * @return
	 */
	protected Listener getResourceCSVDefaultButtonListener(final Composite control,
			final Text text) {
		String[] filterExt = { "*.csv", "*.txt" };

		return getFileDialogButtonListener(control, text, filterExt);
	}

	/**
	 * .txt가 기본값으로 되어있는 FileDialog 생성 Listener 반환
	 * 
	 * @param control
	 * @param text
	 * @return
	 */
	protected Listener getResourceButtonTXTDefaultListeners(
			final Composite control, final Text text) {
		String[] filterExt = { "*.txt", "*.csv" };

		return getFileDialogButtonListener(control, text, filterExt);
	}

	/**
	 * 각 유형에 맞는 Resource Example Label 생성
	 * 
	 * @param control
	 */
	private void createResouceExampleLabel(Composite control) {
		String itemType = jobRWInfo.getItemType();
		String example = "ex) batchEgovPrj/src/main/resources/example";

		if (isMultiResourceReader(itemType)) {
			example = example.concat("*");
		}

		if (isFixedItemReader(itemType) || isFixedItemWriter(itemType) || isFormatterItemWriter(itemType)) {
			example = example.concat(".txt");
		} else {
			example = example.concat(".csv");
		}

		createResouceExampleLabel(control, example);
	}

	/**
	 * MultiResourceReader인지 확인
	 * 
	 * @param itemType
	 * @return
	 */
	private boolean isMultiResourceReader(String itemType) {
		String pattern = ".*MultiResource.*Reader"; //$NON-NLS-1$

		return Pattern.matches(pattern, itemType);
	}

	/**
	 * FixedReader인지 확인
	 * 
	 * @param itemType
	 * @return
	 */
	private boolean isFixedItemReader(String itemType) {
		String pattern = ".*Fixed.*ItemReader";

		return Pattern.matches(pattern, itemType);
	}

	/**
	 * FixedWriter인지 확인
	 * 
	 * @param itemType
	 * @return
	 */
	private boolean isFixedItemWriter(String itemType) {
		String pattern = ".*Fixed.*ItemWriter"; //$NON-NLS-1$

		return Pattern.matches(pattern, itemType);
	}
	
	/**
	 * FormatterItemWriter인지 확인
	 * 
	 * @param itemType
	 * @return
	 */
	private boolean isFormatterItemWriter(String itemType){
		String pattern = "Formatter.*ItemWriter"; //$NON-NLS-1$

		return Pattern.matches(pattern, itemType);
	}
	
	/**
	 * ResourceExampleLabel 생성
	 * 
	 * @param control
	 */
	private void createResouceExampleLabel(Composite control, String labelString) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 3;

		Label exampleLabel = new Label(control, SWT.None);
		exampleLabel.setText(labelString);
		exampleLabel.setLayoutData(gData);
	}

	/**
	 * FieldNameControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createFieldNameControl(Composite control) {
		Text fieldNameText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_NAME_LABEL);
		fieldNameText
				.setText(BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_NAME_EXAMPLE);
		setTextHorizontalFullAndSpan(fieldNameText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.FIELD_NAME,
				fieldNameText);
		setInitialValueToText(JobRWDetailInfoItem.FIELD_NAME, fieldNameText);

		return fieldNameText;
	}

	/**
	 * ColumnLengthControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createColumnLengthControl(Composite control) {
		Text columnLength = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_COLUMN_LENGTH_LABEL);
		columnLength
				.setText(BatchMessages.JobRWFileDetailInfoControlConstructor_COLUMN_LENGTH_EXAMPLE);
		setTextHorizontalFullAndSpan(columnLength, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.COLUMN_LENGTH,
				columnLength);
		setInitialValueToText(JobRWDetailInfoItem.COLUMN_LENGTH, columnLength);

		return columnLength;
	}

	/**
	 * DelimiterControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createDelimiterControl(Composite control) {
		Text delimiterText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_DELIMITER_LABEL);
		delimiterText
				.setText(BatchMessages.JobRWFileDetailInfoControlConstructor_DELIMITER_EXAMPLE);
		setTextHorizontalFullAndSpan(delimiterText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.DELIMITER,
				delimiterText);
		setInitialValueToText(JobRWDetailInfoItem.DELIMITER, delimiterText);

		return delimiterText;
	}

	/**
	 * VOClassControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createVOClassControl(Composite control) {
		Text voClassText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_VO_CLASS_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.VO_CLASS, voClassText);
		setInitialValueToText(JobRWDetailInfoItem.VO_CLASS, voClassText);

		Button button = createBrowseButton(control);
		button.addListener(SWT.Selection,
				getClassSearchBrowseButtonListener(voClassText));

		return voClassText;
	}

	/**
	 * FieldFormatControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createFieldFormatControl(Composite control) {
		Text fieldFormatText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_FORMAT_LABEL);
		fieldFormatText
				.setText(BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_FORMAT_EXAMPLE);
		setTextHorizontalFullAndSpan(fieldFormatText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.FIELD_FORMAT,
				fieldFormatText);
		setInitialValueToText(JobRWDetailInfoItem.FIELD_FORMAT, fieldFormatText);

		return fieldFormatText;
	}

	/**
	 * FieldRangeControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createFieldRangeControl(Composite control) {
		Text fieldRangeText = createLabelText(
				control,
				BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_RANGE_LABEL);
		fieldRangeText
				.setText(BatchMessages.JobRWFileDetailInfoControlConstructor_FIELD_RANGE_EXAMPLE);
		setTextHorizontalFullAndSpan(fieldRangeText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.FIELD_RANGE,
				fieldRangeText);
		setInitialValueToText(JobRWDetailInfoItem.FIELD_RANGE, fieldRangeText);

		return fieldRangeText;
	}

	/**
	 * MaxCountPerResourceControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createMaxCountPerResourceControl(Composite control) {
		GridLayout gLayout = new GridLayout(2, false);
		gLayout.marginHeight = 0;
		gLayout.marginWidth = 0;
		gLayout.horizontalSpacing = 0;
		gLayout.verticalSpacing = 0;

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 3;

		Composite childControl = new Composite(control, SWT.None);
		childControl.setLayout(gLayout);
		childControl.setLayoutData(gData);

		Text maxCountPerResource = createLabelText(
				childControl,
				BatchMessages.JobRWFileDetailInfoControlConstructor_MAX_COUNT_PER_RESOURCE_LABEL);
		addSaveValueToContextListener(
				JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE, maxCountPerResource);
		setInitialValueToText(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE,
				maxCountPerResource);

		return maxCountPerResource;
	}
}
