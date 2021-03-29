package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobRWDetailInfoItem;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.TypeCSqlKeyValueVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.util.JobRWDBDetailInfoValidationUtil;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRW;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * <pre>
 * DB Job Reader / Writer Detail Info를 생성
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
public class JobRWDBDetailInfoControlConstructor extends JobRWDetailInfoControlConstructor {

	/** SqlPagingQueryJdbcItemReader에서 입력한 Key, Value값 목록 */
	private List<TypeCSqlKeyValueVo> sqlKeyValueList = new ArrayList<TypeCSqlKeyValueVo>();

	private List<TypeCSqlKeyValueVo> preSavedSqlKeyValueList = null;

	/** 유효성을 검사하는 객체 */
	private JobRWDBDetailInfoValidationUtil validation = null;

	/**
	 * JobRWDBDetailInfoControlConstructor 생성자
	 * 
	 * @param detailInfoControl
	 * @param errorSettingLabel
	 * @param context
	 * @param preSavedDetailContext
	 * @param preSavedSqlKeyValueList
	 * 
	 */
	public JobRWDBDetailInfoControlConstructor(ScrolledComposite detailInfoControl, Label errorSettingLabel, BatchJobCreationContext context,
			Map<String, String> preSavedDetailContext, List<TypeCSqlKeyValueVo> preSavedSqlKeyValueList, boolean isPreviousSelectedJobRW) {
		super(detailInfoControl, errorSettingLabel, context, preSavedDetailContext, isPreviousSelectedJobRW);
		validation = new JobRWDBDetailInfoValidationUtil(detailContext);
		this.preSavedSqlKeyValueList = preSavedSqlKeyValueList;
		this.sqlKeyValueList.addAll(preSavedSqlKeyValueList);
	}

	@Override
	protected void createDetailInfoContents(Composite control, JobRWInfo jobRWInfo) {
		String resourceDetailType = jobRWInfo.getResourceDetailType();

		if (DefaultJobRW.IBATIS_RW_TYPE.equals(resourceDetailType)) {
			createIbatisRWControl(control);

		} else if (DefaultJobRW.CUSTOMIZED_JDBC_CURSOR_ITEM_READER_TYPE.equals(resourceDetailType)) {
			createCustomizedJdbcCursorItemReaderControl(control);

		} else if (DefaultJobRW.SQL_PAGING_QUERY_JDBC_ITEM_READER_TYPE.equals(resourceDetailType)) {
			createSqlPagingQueryJdbcItemReaderControl(control);

		} else if (DefaultJobRW.SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER_TYPE.equals(resourceDetailType)) {
			createSqlParameterJdbcBatchItemWriterControl(control);

		} else if (DefaultJobRW.ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE.equals(resourceDetailType)) {
			createItemPreparedStatementJdbcBatchItemWriterControl(control);

		} else if (DefaultJobRW.EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE.equals(resourceDetailType)) {
			createEgovItemPreparedStatementJdbcBatchItemWriterControl(control);

		} else if (DefaultJobRW.EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER_TYPE.equals(resourceDetailType)) {
			createEgovCustomizedJdbcBatchItemWriterControl(control);

		} else if (DefaultJobRW.CUSTOMIZE_DB_TYPE.equals(resourceDetailType)) {
			createCustomizeDBControl();

		}
	}

	/** IbatisRW Control 생성 */
	@SuppressWarnings("unused")
	private void createIbatisRWControl(Composite control) {
		Text iBatisStatementText = createIBatisStatementControl(control);
		addIbatisRWItemListener(iBatisStatementText);

		Text configurationFile = createConfigurationFileControl(control);
		addIbatisRWItemListener(configurationFile);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateIbatisRWAndGetErrorMessage());
	}

	/**
	 * IbatisRWItem 유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addIbatisRWItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateIbatisRWAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** CustomizedJdbcCursorItemReader Control 생성 */
	@SuppressWarnings("unused")
	private void createCustomizedJdbcCursorItemReaderControl(Composite control) {
		Text sqlText = createSqlControl(control);
		addCustomizedJdbcCursorItemReaderItemListener(sqlText);

		Text rowMapperText = createRowMapperControl(control);
		addCustomizedJdbcCursorItemReaderItemListener(rowMapperText);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateCustomizedJdbcCursorItemReaderAndGetErrorMessage());
	}

	/**
	 * CustomizedJdbcCursorItemReader 유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addCustomizedJdbcCursorItemReaderItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateCustomizedJdbcCursorItemReaderAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** SqlPagingQueryJdbcItemReader Control 생성 */
	@SuppressWarnings("unused")
	private void createSqlPagingQueryJdbcItemReaderControl(Composite control) {
		resizeToFitScrollVisibleControl(control);

		Text rowMapperText = createRowMapperControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(rowMapperText);

		Text pageSizeText = createPageSizeControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(pageSizeText);

		Text sqlSortColumn = createSqlSortColumnControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(sqlSortColumn);

		Text sqlSelectText = createSqlSelectControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(sqlSelectText);

		Text sqlFromText = createSqlFromControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(sqlFromText);

		Text sqlWhereText = createSqlWhereControl(control);
		addSqlPagingQueryJdbcItemReaderItemListener(sqlWhereText);

		Combo databaseTypeCombo = createDatabaseType(control);
		databaseTypeCombo.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateSqlPagingQueryJdbcItemReaderAndGetErrorMessage();
				setMessage(message);
			}
		});

		Text datasourceBeanIDText = createDatasourceBeanIDText(control);
		addSqlPagingQueryJdbcItemReaderItemListener(datasourceBeanIDText);

		TableViewer sqlTableViewer = createSqlTableViewerControl(control);

		setMessage(validation.validateSqlPagingQueryJdbcItemReaderAndGetErrorMessage());
	}

	/**
	 * Scroll이 보이는 경우 ScrolledComposite 내부 Composite의 크기를 수정 한다.
	 * 
	 * @param control
	 */
	private void resizeToFitScrollVisibleControl(Composite control) {
		Point prePoint = control.getSize();
		int newX = prePoint.x - 13;
		int newY = prePoint.y;
		control.setSize(newX, newY);
	}

	/**
	 * SqlPagingQueryJdbcItemReader 유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addSqlPagingQueryJdbcItemReaderItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateSqlPagingQueryJdbcItemReaderAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** SqlParameterJdbcBatchItemWriter Control 생성 */
	@SuppressWarnings("unused")
	private void createSqlParameterJdbcBatchItemWriterControl(Composite control) {
		Text sqlUpdateText = createSqlUpdateControl(control);
		addSqlParameterJdbcBatchItemWriterItemListener(sqlUpdateText);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateSqlParameterJdbcBatchItemWriterAndGetErrorMessage());
	}

	/**
	 * SqlParameterJdbcBatchItemWriter유효성 검사
	 * 
	 * @param text
	 */
	private void addSqlParameterJdbcBatchItemWriterItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateSqlParameterJdbcBatchItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** ItemPreparedStatementJdbcBatchItemWriter Control 생성 */
	@SuppressWarnings("unused")
	private void createItemPreparedStatementJdbcBatchItemWriterControl(Composite control) {
		Text sqlInsertText = createSqlInsertControl(control);
		addSqlItemPreparedStatementJdbcBatchItemWriterItemListener(sqlInsertText);

		Text rowSetterText = createRowSetterControl(control);
		addSqlItemPreparedStatementJdbcBatchItemWriterItemListener(rowSetterText);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage());
	}

	/**
	 * SqlItemPreparedStatementJdbcBatchItemWriterItem 유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addSqlItemPreparedStatementJdbcBatchItemWriterItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** EgovItemPreparedStatementJdbcBatchItemWriter Control 생성 */
	@SuppressWarnings("unused")
	private void createEgovItemPreparedStatementJdbcBatchItemWriterControl(Composite control) {
		Text sqlInsertText = createSqlInsertControl(control);
		addEgovItemPreparedStatementJdbcBatchItemWriterItemListener(sqlInsertText);

		Text paramsText = createParamsControl(control);
		addEgovItemPreparedStatementJdbcBatchItemWriterItemListener(paramsText);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateEgovItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage());
	}

	/**
	 * EgovItemPreparedStatementJdbcBatchItemWriterItem 유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addEgovItemPreparedStatementJdbcBatchItemWriterItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateEgovItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** EgovCustomizedJdbcBatchItemWriter Control 생성 */
	@SuppressWarnings("unused")
	private void createEgovCustomizedJdbcBatchItemWriterControl(Composite control) {
		Text sqlInsertText = createSqlInsertControl(control);
		addEgovCustomizedJdbcBatchItemWriterItemListener(sqlInsertText);

		Text rowSetterText = createRowSetterControl(control);
		addEgovCustomizedJdbcBatchItemWriterItemListener(rowSetterText);

		Text databaseBeanIDText = createDatasourceBeanIDText(control);

		setMessage(validation.validateEgovCustomizedJdbcBatchItemWriterAndGetErrorMessage());
	}

	/**
	 * EgovCustomizedJdbcBatchItemWriterItem유효성 검사 Listener
	 * 
	 * @param text
	 */
	private void addEgovCustomizedJdbcBatchItemWriterItemListener(Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				String message = validation.validateEgovCustomizedJdbcBatchItemWriterAndGetErrorMessage();
				setMessage(message);
			}
		});
	}

	/** CustomizeDB Control 생성 */
	private void createCustomizeDBControl() {
		setMessageOK();
	}

	/**
	 * IBatisStatementControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createIBatisStatementControl(Composite control) {
		Text iBatisStatementText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_IBATIS_STATEMENT_LABEL);
		setTextHorizontalFullAndSpan(iBatisStatementText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.IBATIS_STATEMENT, iBatisStatementText);
		setInitialValueToText(JobRWDetailInfoItem.IBATIS_STATEMENT, iBatisStatementText);

		return iBatisStatementText;
	}

	/**
	 * ConfigurationFileControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createConfigurationFileControl(Composite control) {
		Text configurationFileText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_CONFIGURATION_FILE_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.CONFIGURATION_FILE, configurationFileText);
		setInitialValueToText(JobRWDetailInfoItem.CONFIGURATION_FILE, configurationFileText);

		Button browseButton = createBrowseButton(control);
		browseButton.addListener(SWT.Selection, getConfigurationButtonListener(control, configurationFileText));

		return configurationFileText;
	}

	private Listener getConfigurationButtonListener(Composite control, Text text) {
		String[] filterExt = { "*.xml" };

		return getFileDialogButtonListener(control, text, filterExt);
	}

	/**
	 * SqlControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlControl(Composite control) {
		Text sqlText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_LABEL);
		sqlText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL, sqlText);
		setInitialValueToText(JobRWDetailInfoItem.SQL, sqlText);

		return sqlText;
	}

	/**
	 * RowMapperControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createRowMapperControl(Composite control) {
		Text rowMapperText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_ROW_MAPPER_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.ROW_MAPPER, rowMapperText);
		setInitialValueToText(JobRWDetailInfoItem.ROW_MAPPER, rowMapperText);

		Button button = createBrowseButton(control);
		button.addListener(SWT.Selection, getClassSearchBrowseButtonListener(rowMapperText));

		return rowMapperText;
	}

	/**
	 * PageSizeControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createPageSizeControl(Composite control) {
		Text pageSizeText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_PAGE_SIZE_LABEL);
		setTextHorizontalFullAndSpan(pageSizeText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.PAGE_SIZE, pageSizeText);
		setInitialValueToText(JobRWDetailInfoItem.PAGE_SIZE, pageSizeText);

		return pageSizeText;
	}

	/**
	 * SqlSortColumnControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlSortColumnControl(Composite control) {
		Text sqlSortColumn = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_SORT_COLUMN_LABEL);
		sqlSortColumn.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_SORT_COLUMN_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlSortColumn, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_SORT_COLUMN, sqlSortColumn);
		setInitialValueToText(JobRWDetailInfoItem.SQL_SORT_COLUMN, sqlSortColumn);

		return sqlSortColumn;
	}

	/**
	 * SqlSelectControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlSelectControl(Composite control) {
		Text sqlSelectText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_SELECT_LABEL);
		sqlSelectText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_SELECT_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlSelectText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_SELECT, sqlSelectText);
		setInitialValueToText(JobRWDetailInfoItem.SQL_SELECT, sqlSelectText);

		return sqlSelectText;
	}

	/**
	 * SqlFromControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlFromControl(Composite control) {
		Text sqlFromText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_FROM_LABEL);
		sqlFromText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_FROM_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlFromText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_FROM, sqlFromText);
		setInitialValueToText(JobRWDetailInfoItem.SQL_FROM, sqlFromText);

		return sqlFromText;
	}

	/**
	 * SqlWhereControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlWhereControl(Composite control) {
		Text sqlWhereText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_WHERE_LABEL);
		sqlWhereText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_WHERE_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlWhereText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_WHERE, sqlWhereText);
		setInitialValueToText(JobRWDetailInfoItem.SQL_WHERE, sqlWhereText);

		return sqlWhereText;
	}

	/**
	 * SqlUpdateControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlUpdateControl(Composite control) {
		Text sqlUpdateText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_LABEL);
		sqlUpdateText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_UPDATE_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlUpdateText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_UPDATE, sqlUpdateText);
		setInitialValueToText(JobRWDetailInfoItem.SQL_UPDATE, sqlUpdateText);

		return sqlUpdateText;
	}

	/**
	 * SqlInsertControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createSqlInsertControl(Composite control) {
		Text sqlInsertText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_LABEL);
		sqlInsertText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_INSERT_EXAMPLE);
		setTextHorizontalFullAndSpan(sqlInsertText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.SQL_INSERT, sqlInsertText);
		setInitialValueToText(JobRWDetailInfoItem.SQL_INSERT, sqlInsertText);

		return sqlInsertText;
	}

	/**
	 * ParamsControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createParamsControl(Composite control) {
		Text paramsText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_PARAMS_LABEL);
		paramsText.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_PARAMS_EXAMPLE);
		setTextHorizontalFullAndSpan(paramsText, 2);
		addSaveValueToContextListener(JobRWDetailInfoItem.PARAMS, paramsText);
		setInitialValueToText(JobRWDetailInfoItem.PARAMS, paramsText);

		return paramsText;
	}

	/**
	 * RowSetterControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createRowSetterControl(Composite control) {
		Text rowSetterText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_ROW_SETTER_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.ROW_SETTER, rowSetterText);
		setInitialValueToText(JobRWDetailInfoItem.ROW_SETTER, rowSetterText);

		Button button = createBrowseButton(control);
		button.addListener(SWT.Selection, getClassSearchBrowseButtonListener(rowSetterText));

		return rowSetterText;
	}

	/**
	 * SqlTableViewerControl 생성
	 * 
	 * @param control
	 * @return
	 */
	private TableViewer createSqlTableViewerControl(Composite control) {
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalSpan = 3;

		Composite TableViewerControl = new Composite(control, SWT.None);
		TableViewerControl.setLayout(new GridLayout(2, false));
		TableViewerControl.setLayoutData(gData);

		TableViewer tableViewer = createTableViewer(TableViewerControl);

		createTableViewerButtonControl(TableViewerControl, tableViewer);

		return tableViewer;
	}

	/**
	 * SqlPagingQueryJdbcItemReader의 TableViewer 생성
	 * 
	 * @param control
	 * @return
	 */
	private TableViewer createTableViewer(Composite control) {
		TableViewer tableViewer = new TableViewer(control, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		tableViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new KeyValueTableLabelProvider());

		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		BatchTableColumn keyColumn = new BatchTableColumn(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_TABLE_COLUMN_KEY, 150);
		BatchTableColumn valueColumn = new BatchTableColumn(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_TABLE_COLUMN_VALUE, 180);

		keyColumn.setColumnToTable(table);
		valueColumn.setColumnToTable(table);

		tableViewer.setInput(preSavedSqlKeyValueList);

		return tableViewer;
	}

	/**
	 * SqlPagingQueryJdbcItemReader의 TableViewer Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createTableViewerButtonControl(Composite control, TableViewer tableViewer) {
		Composite buttonControl = new Composite(control, SWT.None);
		buttonControl.setLayout(new GridLayout());
		buttonControl.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		createAddButton(buttonControl, tableViewer);

		createRemoveButton(buttonControl, tableViewer);
	}

	/**
	 * SqlPagingQueryJdbcItemReader의 TableViewer Add Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createAddButton(final Composite control, final TableViewer tableViewer) {
		Button addButton = new Button(control, SWT.PUSH);
		addButton.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_ADD_BUTTON);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				SqlKeyValueTableDialog dialog = new SqlKeyValueTableDialog(control.getShell());
				dialog.setTitle(BatchMessages.JobRWDBDetailInfoControlConstructor_SQL_TABLE_ADD_BUTTON_DIALOG_TITLE);
				if (dialog.open() == Window.OK) {
					TypeCSqlKeyValueVo vo = dialog.getVo();
					tableViewer.add(vo);

					sqlKeyValueList.add(vo);
				}
			}
		});
	}

	/**
	 * SqlPagingQueryJdbcItemReader의 TableViewer Remove Button 생성
	 * 
	 * @param control
	 * @param tableViewer
	 */
	private void createRemoveButton(Composite control, final TableViewer tableViewer) {
		Button removeButton = new Button(control, SWT.PUSH);
		removeButton.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_REMOVE_BUTTON);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (!selection.isEmpty()) {
					TypeCSqlKeyValueVo vo = (TypeCSqlKeyValueVo) selection.getFirstElement();
					tableViewer.remove(vo);
					removeItemFromSqlKeyValueList(vo);
				}
			}
		});
	}

	/**
	 * sqlKeyValueList에서 해당 TypeCSqlKeyValueVo 제거
	 * 
	 * @param toRemoveVo
	 * @return
	 */
	private boolean removeItemFromSqlKeyValueList(TypeCSqlKeyValueVo toRemoveVo) {
		if (!NullUtil.isEmpty(sqlKeyValueList)) {
			for (TypeCSqlKeyValueVo vo : sqlKeyValueList) {
				if (vo.key.equals(toRemoveVo.key)) {
					return sqlKeyValueList.remove(vo);
				}
			}
		}

		return false;
	}

	/** sqlKeyValueList를 가져온다 */
	public List<TypeCSqlKeyValueVo> getSqlKeyValueList() {
		return sqlKeyValueList;
	}

	/**
	 * databaseTypeCombo 생성
	 * 
	 * @param control
	 * @return
	 */
	private Combo createDatabaseType(Composite control) {
		Label databaseTypeLabel = new Label(control, SWT.None);
		databaseTypeLabel.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_DATABASE_TYPE_LABEL);

		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 2;

		final Combo databaseTypeCombo = new Combo(control, SWT.BORDER | SWT.READ_ONLY);

		// 나중에 이 STRING이 어디서 사용되나 확인하고 어디에 static public 으로 정의해야 하는지 확인
		databaseTypeCombo.setItems(new String[] { "Oracle", "Hsql", "mySql", "Altibase", "Tibero" });
		databaseTypeCombo.setLayoutData(gData);
		addSaveValueToContextListener(JobRWDetailInfoItem.DATABASE_TYPE, databaseTypeCombo);
		setInitialValueToCombo(JobRWDetailInfoItem.DATABASE_TYPE, databaseTypeCombo);

		return databaseTypeCombo;
	}

	/**
	 * DatasourceBeanIDText 생성
	 * 
	 * @param control
	 * @return
	 */
	private Text createDatasourceBeanIDText(final Composite control) {
		final Text datasourceBeanIDText = createLabelText(control, BatchMessages.JobRWDBDetailInfoControlConstructor_DATABASE_BEAN_ID_LABEL);
		addSaveValueToContextListener(JobRWDetailInfoItem.DATASOURCE_BEAN_ID, datasourceBeanIDText);
		setInitialValueToText(JobRWDetailInfoItem.DATASOURCE_BEAN_ID, datasourceBeanIDText);

		Button datasourceBrowseButton = new Button(control, SWT.PUSH);
		datasourceBrowseButton.setText(BatchMessages.JobRWDBDetailInfoControlConstructor_BROWSE_BUTTON);
		datasourceBrowseButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				String selectedBeanID = "";
				GetDatasourceBeanIDDialog dialog = new GetDatasourceBeanIDDialog(control.getShell(), context, selectedBeanID);
				dialog.setTitle(BatchMessages.JobRWDBDetailInfoControlConstructor_DATASOURCE_BEAN_ID_BROWSE_BUTTON_DIALOG_TITLE);
				if (dialog.open() == Window.OK) {

					selectedBeanID = (String) dialog.getRefDatasourceBeanID();
					datasourceBeanIDText.setText(selectedBeanID);
				}
			}
		});
		return datasourceBeanIDText;
	}
}

/**
 * SqlPagingQueryJdbcItemReader의 TableViewer에 적용되는 LabelProvider
 * 
 * @author SDS
 * 
 */
class KeyValueTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof TypeCSqlKeyValueVo) {
			TypeCSqlKeyValueVo vo = (TypeCSqlKeyValueVo) element;

			switch (columnIndex) {
				case 0:
					return vo.key;
				case 1:
					return vo.value;
			}
		}

		return null;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}
