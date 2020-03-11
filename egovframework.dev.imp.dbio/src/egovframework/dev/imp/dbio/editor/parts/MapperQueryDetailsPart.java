/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.dev.imp.dbio.editor.parts;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.common.DbioLog;
import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.components.fields.ComboEditField;
import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboButtonField;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboEditButtonField;
import egovframework.dev.imp.dbio.components.fields.HyperLinkTextButtonField;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.IConnectionInfoEditor;
import egovframework.dev.imp.dbio.editor.SQLSourceViewer;
import egovframework.dev.imp.dbio.editor.actions.OpenSQLBuilderDialogAction;
import egovframework.dev.imp.dbio.editor.model.SqlBindingParm;
import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.test.MapperTestManager;
import egovframework.dev.imp.dbio.test.QueryResultView;
import egovframework.dev.imp.dbio.util.DBUtil;
import egovframework.dev.imp.dbio.util.DTPUtil;
import egovframework.dev.imp.dbio.util.EclipseUtil;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * Mapper 에디터의 Query 상세부 화면
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperQueryDetailsPart extends AbstractDetailsPage implements Listener, IConnectionInfoEditor {

	//private ConnectionInfoComposite2 connInfo;
	protected final static String MAP = "Map";
	protected final static String MAP_DEPRECATED = "Map(deprecated)";
	protected final static String CLASS = "Type";
	protected final static String XML = "Xml";

	public static final int ON_INITIALIZE = 0;
	public static final int QUERY_SECTION_MAXIMIZE = 1;
	public static final int ON_INOUT_SECTION_OPEN = 2;
	public static final int ON_TEST_SECTION_OPEN = 3;
	public static final int ON_INOUT_SECTION_CLOSE = 4;
	public static final int ON_TEST_SECTION_CLOSE = 5;
	private Composite queryComposite;
	private int inOutSectionMaxHeight;
	private int testSectionMaxHeight;

	protected MapperMasterPart masterPart;
	protected MapperCRUDElement currentElement;

	private String selectedProfileName;
	private String[] dbProfileNames;
	private IWorkbenchPartSite site;
	private Section inOutSection;
	private Section querySection;
	private Section testSection;
	private TextField idField;
	private ComboEditField paramClassField; //ComboField -> ComboEditField
	//private ComboField paramMapField; 
	private TextField paramMapField; //(deprecated) ComboField -> TextField
	private Composite parmClassCps;
	private Composite parmMapCps;
	private SQLSourceViewer queryViewer;
	private Button paramRadioClass;
	private Button paramRadioMap;
	private CCombo dbInfoCombo;
	private TableViewer tableViewer;
	private Table bindValTable;
	private TableColumn bindValNameCol;
	private TableColumn bindValValueCol;
	private Text rowLimitText;

	private String oldQuery;
	private boolean refreshing = false;
	private ArrayList<String> outModeBindVars = new ArrayList<String>();

	//private Map testResult;

	private final IFieldListener listener = new IFieldListener() {
		public void eventOccured(FieldEvent event) {
			switch (event.getType()) {
				case ButtonSelected:
					buttonSelected(event.getSource());
					break;
				case HyperLinkActivated:
					linkActivated(event.getSource());
					break;
				case TextChanged:
					textChanged(event.getSource());
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 생성자
	 * 
	 * @param masterPart
	 */
	public MapperQueryDetailsPart(MapperMasterPart masterPart) {
		this.setMasterPart(masterPart);
		this.setIdField();
		this.setParamClassField();
		this.setSaramMapField();
	}

	private void setMasterPart(MapperMasterPart masterPart) {
		this.masterPart = masterPart;
	}

	private void setIdField() {
		this.idField = new TextField("ID*:");
	}

	private void setParamClassField() {
		this.paramClassField = new HyperLinkComboEditButtonField("Type:", "Browse"); //ComboField -> ComboEditField : new : HyperLinkComboButtonField -> HyperLinkComboEditButtonField
	}

	private void setSaramMapField() {
		//this.paramMapField = new ComboField("Map:");
		this.paramMapField = new TextField("Map:"); //(deprecated) ComboField -> TextField
	}

	public ISQLEditorConnectionInfo getConnectionInfo() {
		if (selectedProfileName == null)
			selectedProfileName = "";
		return DTPUtil.getConnectionInfo(this.selectedProfileName);
	}

	protected void createInOutSection(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();

		inOutSection = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE);
		inOutSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				if (e.getState() && querySection.isExpanded()) {
					maximizeQuerySection(ON_INOUT_SECTION_OPEN);
				} else if (!e.getState()) {
					inOutSectionMaxHeight = inOutSection.getClientArea().height;
				}
			}

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (!inOutSection.isExpanded() && querySection.isExpanded()) {
					maximizeQuerySection(ON_INOUT_SECTION_CLOSE);
				}
			}
		});
		inOutSection.setExpanded(true);
		inOutSection.setText("In/Out");
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		inOutSection.setLayoutData(td);

		Composite composite = toolkit.createComposite(inOutSection, SWT.NONE);
		GridLayout gl = new GridLayout(4, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginTop = 0;
		composite.setLayout(gl);
		toolkit.paintBordersFor(composite);

		MouseAdapter paramRadioListener = new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				String parmType = (String) e.widget.getData();
				System.out.println("MouseAdapter paramRadioListener mouseUp parmType :"+parmType);
				if (CLASS.equals(parmType)) {
					//ComboField -> ComboEditField
					paramClassField.setEnabled(true);
					paramMapField.setFocus();
					paramMapField.setText("");
					/*
					//(deprecated) ComboField -> TextField
					paramMapField.setEnabled(false);
                    */
					parmClassCps.setVisible(true);
					parmMapCps.setVisible(false);
				} else if (MAP.equals(parmType)) {
					/*
					//(deprecated) ComboField -> TextField
					paramMapField.setEnabled(true);
					*/
					paramClassField.setFocus();
					paramClassField.setText("");
					//ComboField -> ComboEditField
					paramClassField.setEnabled(false);
					parmClassCps.setVisible(false);
					parmMapCps.setVisible(true);
				} else {
					DbioLog.logInfo("No Selection!");
				}
			}
		};

		//1행
		Label label = toolkit.createLabel(composite, "Parameter");
		GridData gd = new GridData();
		gd.widthHint = 60;
		label.setLayoutData(gd);

		Composite parmBtnCpst = toolkit.createComposite(composite, SWT.NONE);
		gl = new GridLayout(2, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginTop = 0;
		parmBtnCpst.setLayout(gl);
		gd = new GridData();
		gd.horizontalSpan = 3;
		parmBtnCpst.setLayoutData(gd);

		paramRadioClass = toolkit.createButton(parmBtnCpst, CLASS, SWT.RADIO);
		gd = new GridData();
		paramRadioClass.setLayoutData(gd);
		paramRadioClass.setData(CLASS);
		paramRadioClass.addMouseListener(paramRadioListener);

		//(deprecated) ComboField -> TextField
		//paramRadioMap = toolkit.createButton(parmBtnCpst, MAP, SWT.RADIO);
		paramRadioMap = toolkit.createButton(parmBtnCpst, MAP_DEPRECATED, SWT.RADIO);
		gd = new GridData();
		paramRadioMap.setLayoutData(gd);
		paramRadioMap.setData(MAP);
		paramRadioMap.addMouseListener(paramRadioListener);

		createMapContents(toolkit, composite, 3);

		//3행
		//label = toolkit.createLabel(composite, "");
		//gd = new GridData();
		//gd.horizontalSpan = 1;
		//label.setLayoutData(gd);

		inOutSection.setClient(composite);
	}

	protected void createQuerySection(IManagedForm managedForm, Composite parent) {
		//ID:[             ]       DB:[ New Oracle]          [Open Query Builder]
		//입력창
		FormToolkit toolkit = managedForm.getToolkit();
		GridData gd = null;

		querySection = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE);
		querySection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				if (e.getState()) {
					maximizeQuerySection(QUERY_SECTION_MAXIMIZE);
				}
			}

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				maximizeQuerySection(QUERY_SECTION_MAXIMIZE);
			}
		});
		querySection.setExpanded(true);
		querySection.setText("Query");
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		querySection.setLayoutData(td);

		Composite composite = toolkit.createComposite(querySection, SWT.NONE);
		GridLayout gl = new GridLayout(3, false);
		gl.verticalSpacing = 7;
		composite.setLayout(gl);
		toolkit.paintBordersFor(composite);

		//1행 ID 입력
		idField.create(toolkit, composite, 3);
		idField.addFieldListener(listener);

		//2행 Query Editor
		queryComposite = toolkit.createComposite(composite, SWT.FLAT);
		FillLayout flayout = new FillLayout();
		flayout.marginHeight = 2;
		flayout.marginWidth = 0;
		flayout.spacing = 0;
		queryComposite.setLayout(flayout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		gd.minimumHeight = 21;
		gd.grabExcessVerticalSpace = true;
		queryComposite.setLayoutData(gd);
		queryComposite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);

		queryViewer = new SQLSourceViewer(queryComposite, this);
		queryViewer.getTextWidget().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				oldQuery = queryViewer.getQuery().trim();
			}

			public void focusLost(FocusEvent e) {
				if (!queryViewer.getQuery().trim().equals(oldQuery)) {
					textChanged(queryViewer);
				}
			}
		});

		//DB Info
		//3행 DB Info
		Label dbinfo = new Label(composite, SWT.NONE);
		gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.LEFT;
		dbinfo.setText("DB Info*:");
		gd.widthHint = 60;
		dbinfo.setLayoutData(gd);
		dbInfoCombo = new CCombo(composite, SWT.READ_ONLY | SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		dbInfoCombo.setLayoutData(gd);
		dbInfoCombo.setItems(dbProfileNames);

		dbInfoCombo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				dbProfileNames = DTPUtil.getProfileNames();
				dbInfoCombo.setItems(dbProfileNames);
			}

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				// implements
			}
		});
		dbInfoCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				selectedProfileName = dbInfoCombo.getText();
			}
		});
		if (dbProfileNames.length > 0) {
			dbInfoCombo.select(0);
			selectedProfileName = dbInfoCombo.getText();
		}

		dbInfoCombo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(dbInfoCombo);

		//1행 Open Query Builder
		Button openQueryBuilderButton = toolkit.createButton(composite, "Open Query Builder", SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		openQueryBuilderButton.setLayoutData(gd);
		openQueryBuilderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openQueryBuilder();
			}
		});

		querySection.setClient(composite);
	}

	protected void createTestSection(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();

		testSection = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE);
		testSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				if (e.getState() && querySection.isExpanded()) {
					maximizeQuerySection(ON_TEST_SECTION_OPEN);
				}
				if (!e.getState()) {
					testSectionMaxHeight = testSection.getClientArea().height;
				}
			}

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (!testSection.isExpanded() && querySection.isExpanded()) {
					maximizeQuerySection(ON_TEST_SECTION_CLOSE);
				}
			}
		});
		testSection.setExpanded(true);
		testSection.setText("Test");
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.FILL);
		td.grabHorizontal = true;
		testSection.setLayoutData(td);

		Composite composite = toolkit.createComposite(testSection, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		toolkit.paintBordersFor(composite);

		Label label = toolkit.createLabel(composite, "Binding Variable:");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		gd.horizontalAlignment = SWT.LEFT;
		label.setLayoutData(gd);

		//바인딩 변수 테이블 생성
		createBindValTable(composite, toolkit);

		//바인딩 변수 테이블 오른쪽 버튼 모임
		Composite btnComposite = toolkit.createComposite(composite, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.verticalAlignment = SWT.TOP;
		btnComposite.setLayoutData(gd);
		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 5;
		gl.marginHeight = 0;
		gl.marginWidth = 2;
		btnComposite.setLayout(gl);
		toolkit.paintBordersFor(btnComposite);

		Button setBindValButton = toolkit.createButton(btnComposite, "Set Param", SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		setBindValButton.setLayoutData(gd);
		setBindValButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//addNewRow();
				addBindingVariableRows();
			}
		});

		Button testQueryButton = toolkit.createButton(btnComposite, "Query Test", SWT.NONE);
		gd.horizontalAlignment = SWT.LEFT;
		testQueryButton.setLayoutData(gd);
		testQueryButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unused")
			public void widgetSelected(SelectionEvent e) {
				//DB Info 를 추출한다.
				if (DTPUtil.getProfileNames().length == 0) {
					MessageDialog.openInformation(masterPart.getPage().getSite().getShell(), "Alert!", "Set Data Source Explorer");
					return;
				}

				String profileName = getConnectionInfo().getConnectionProfileName();
				if (dbInfoCombo.getText() == null || "".equals(dbInfoCombo.getText())) {
					MessageDialog.openInformation(masterPart.getPage().getSite().getShell(), "Query Tester", "Select \"DB Info\" first!");
					return;
				}

				IConnectionProfile p = getConnectionInfo().getConnectionProfile();
				IStatus status = p.connect();
				IManagedConnection con = p.getManagedConnection(Connection.class.getName());
				DBUtil.setConnection((Connection) con.getConnection().getRawConnection());

				QueryResultView resultView = null;
				try {
					resultView = (QueryResultView) EclipseUtil.getActivePage().showView(QueryResultView.ID);
					resultView.clearQueryResultPart();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				//Test Query 
				List<Map<String, String>> queryResult = testQuery();
				masterPart.getPage().setQueryResult(queryResult);

				//Process Result
				try {
					//					QueryResultView resultView = (QueryResultView) EclipseUtil.getActivePage().showView(QueryResultView.ID);
					//if (dataSet != null && dataSet.getRowCount() > 0)
					resultView.setQueryResultData(queryResult);
					resultView.createQueryResultPart();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		Button createVoButton = toolkit.createButton(btnComposite, "Create VO", SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		createVoButton.setLayoutData(gd);
		createVoButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				masterPart.getPage().startVoCreation();
				masterPart.getPage().openOrCreateNewJavaClass(paramClassField.getText());
				masterPart.getPage().endVoCreation();
			}
		});

		label = toolkit.createLabel(btnComposite, "Row Limit:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = SWT.LEFT;
		label.setLayoutData(gd);

		rowLimitText = toolkit.createText(btnComposite, Integer.toString(DBUtil.getMaxRow()));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		rowLimitText.setLayoutData(gd);
		rowLimitText.setTextLimit(3);
		rowLimitText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (StringUtil.checkStrNum(rowLimitText.getText()))
					DBUtil.setMaxRow(Integer.parseInt(rowLimitText.getText()));
			}
		});

		testSection.setClient(composite);

	}

	/**
	 * 화면 구성
	 */
	@Override
	protected void createPartContents(IManagedForm managedForm, Composite parent) {
		dbProfileNames = DTPUtil.getProfileNames();
		createInOutSection(managedForm, parent);
		createQuerySection(managedForm, parent);
		createTestSection(managedForm, parent);
		querySection.setExpanded(false);
		querySection.setExpanded(true);
		maximizeQuerySection(QUERY_SECTION_MAXIMIZE);
		//initViewer();
	}

	/**
	 * Sql 테스트(테스트 버튼과 연계)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Map<String, String>> testQuery() {
		Map<String, Comparable> parm = new HashMap<String, Comparable>();
		List<Map<String, String>> result = null;
		SqlBindingParm sbp = null;
		String key = currentElement.getTagName();

		StringBuffer sql = new StringBuffer(key).append(StringUtil.SEPARATOR).append(currentElement.getSQLStatement());
		//.append(queryViewer.getQuery().trim());

		TableItem[] tblItm = bindValTable.getItems();
		int dataTypeIdx = 0;

		try {
			for (int i = 0; i < tblItm.length; i++) {
				sbp = (SqlBindingParm) tblItm[i].getData();
				dataTypeIdx = ((Integer) sbp.dataTypeComboBox.getValue()).intValue();

				if ("String".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, sbp.value);
				else if ("Byte".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new Byte(sbp.value));
				else if ("Integer".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new Integer(sbp.value));
				else if ("Long".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new Long(sbp.value));
				else if ("Float".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new Float(sbp.value));
				else if ("Double".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new Double(sbp.value));
				else if ("BigDecimal".equals(SqlBindingParm.dataTypes[dataTypeIdx]))
					parm.put(sbp.parm, new BigDecimal(sbp.value));
			}

			MapperTestManager tm = new MapperTestManager();
			result = tm.runQyery(sql.toString(), parm);

			//procedure의 Out 모드 파라미터 처리
			if ("PROCEDURE".equals(key.toUpperCase())) {
				if (outModeBindVars.size() == 0) {
					Map map = new HashMap(1);
					map.put("Result", "Success!");
					result.add(0, map);
				} else {
					Map oldResultMap = (Map) result.get(0);
					Map newResultMap = new HashMap();

					for (int i = 0; i < outModeBindVars.size(); i++) {
						newResultMap.put(outModeBindVars.get(i), oldResultMap.get(outModeBindVars.get(i)));
					}
					result.remove(0);
					result.add(newResultMap);
				}
			}

		} catch (Exception e) {
			MessageDialog.openInformation(masterPart.getPage().getSite().getShell(), "Query Tester", e.getMessage());
		}
		return result;
	}

	/**
	 * 바인팅 변수의 테스트 값 추가
	 */
	protected void addNewRow() {
		SqlBindingParm newRow = new SqlBindingParm();
		tableViewer.add(newRow);
		CellEditor[] cells = tableViewer.getCellEditors();
		cells[cells.length].setStyle(SWT.COLOR_GRAY);
	}

	protected void addNewRows(String[] variables) {
		tableViewer.getTable().removeAll();

		for (int i = 0; i < variables.length; i++) {
			SqlBindingParm newRow = new SqlBindingParm(variables[i], "", tableViewer.getTable());
			tableViewer.add(newRow);
		}
	}

	public void clearBindingVarTable() {
		tableViewer.getTable().removeAll();
	}

	protected void addBindingVariableRows() {
		String sql = queryViewer.getQuery();
		ArrayList<String> variables = new ArrayList<String>();

		extractBindVariables(variables, sql);
		extractPropertyKey(variables, sql, "property=\"", 0);
		extractPropertyKey(variables, sql, "compareProperty=\"", 0);
		removeDuple(variables);
		processInOutModeVariables(variables);
		addNewRows(variables.toArray(new String[0]));
	}

	protected void removeDuple(ArrayList<String> variables) {
		String key = null;
		for (int i = 0; i < variables.size(); i++) {
			key = variables.get(i);
			for (int j = i + 1; j < variables.size(); j++) {
				if (key.equals(variables.get(j))) {
					variables.remove(j);
					j--;
				}
			}
		}
	}

	private void processInOutModeVariables(ArrayList<String> variables) {
		String key, KEY;

		//기존정보 초기화
		for (int i = 0; i < outModeBindVars.size(); i++)
			outModeBindVars.remove(0);

		for (int i = 0; i < variables.size(); i++) {
			key = variables.get(i);
			KEY = key.toUpperCase();

			if (key.indexOf(",") > 0) {
				key = key.substring(0, key.indexOf(","));
				variables.set(i, key);

				if (KEY.indexOf("OUT") > 0)
					outModeBindVars.add(key);
			}
		}
	}

	protected void extractBindVariables(ArrayList<String> variables, String sql) {
		try {
			String chS = "#{", chD = "${", chE = "}";
			String ch;
			int startS, startD, endS, endD;
			int start = 0, end = 0;

			startS = sql.indexOf(chS);
			startD = sql.indexOf(chD);

			if (startS == -1)
				startS = 9999;
			if (startD == -1)
				startD = 9999;

			if (startS == startD)
				return;

			if (startS < startD)
				ch = chS;
			else
				ch = chD;

			start = sql.indexOf(ch);
			end = sql.indexOf(chE, start + 2);
			variables.add(sql.substring(start + 2, end));
			endS = sql.lastIndexOf(chE);
			endD = sql.lastIndexOf(chE);
			
			if (endS > end || endD > end)
				extractBindVariables(variables, sql.substring(end + 1));
			
		} catch (Exception e) {
			DbioLog.logError(DbioMessages.mapper_err_binding_variables + "\n" + variables, e);
		}
	}

	protected void extractPropertyKey(ArrayList<String> variables, String sql, String extrKey, int beginIdx) {
		try {
			int startIdx = beginIdx, endIdx = 0;
			startIdx = sql.indexOf(extrKey, startIdx);

			if (startIdx > -1) {
				startIdx = startIdx + extrKey.length();
				endIdx = sql.indexOf('"', startIdx);
				variables.add(sql.substring(startIdx, endIdx));
				extractPropertyKey(variables, sql, extrKey, endIdx);
			}

		} catch (Exception e) {
			DbioLog.logError(DbioMessages.mapper_err_binding_variables + "\n" + variables, e);
		}
	}

	protected void createBindValTable(Composite parent, FormToolkit toolkit) {
		Table table = toolkit.createTable(parent, SWT.FULL_SELECTION | SWT.MULTI);

		ResourceBundleListener paintListener = new ResourceBundleListener();
		table.addListener(SWT.MeasureItem, paintListener);
		table.addListener(SWT.PaintItem, paintListener);
		table.addListener(SWT.EraseItem, paintListener);

		tableViewer = new TableViewer(table);
		bindValTable = tableViewer.getTable();
		bindValTable.setSize(100, 100);
		bindValTable.setHeaderVisible(true);
		bindValTable.setLinesVisible(true);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.heightHint = 100;
		bindValTable.setLayoutData(gd);

		bindValNameCol = new TableColumn(bindValTable, SWT.NONE);
		bindValNameCol.setText(SqlBindingParm.PARAMETER);
		bindValNameCol.setWidth(100);

		bindValValueCol = new TableColumn(bindValTable, SWT.NONE);
		bindValValueCol.setText(SqlBindingParm.VALUE);
		bindValValueCol.setWidth(100);

		bindValValueCol = new TableColumn(bindValTable, SWT.NONE);
		bindValValueCol.setText(SqlBindingParm.DATA_TYPE);
		bindValValueCol.setWidth(100);

		tableViewer.setLabelProvider(new SqlBindingTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());

		attachCellEditors(tableViewer, bindValTable);
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return true;
			}

			public Object getValue(Object element, String property) {
				SqlBindingParm sqlBindingParm = null;

				if (element instanceof SqlBindingParm) {
					sqlBindingParm = (SqlBindingParm) element;

					if (SqlBindingParm.PARAMETER.equals(property))
						return StringUtil.nvl(sqlBindingParm.parm);
					else if (SqlBindingParm.VALUE.equals(property))
						return StringUtil.nvl(sqlBindingParm.value);
					else if (SqlBindingParm.DATA_TYPE.equals(property))
						return sqlBindingParm.dataTypeComboBox.getValue();
					//return sqlBindingParm.dataTypeComboBox.getItems()[((Integer)sqlBindingParm.dataTypeComboBox.getValue()).intValue()];
					else
						return element;
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				SqlBindingParm data = (SqlBindingParm) tableItem.getData();

				if (SqlBindingParm.PARAMETER.equals(property))
					data.parm = value.toString();
				else if (SqlBindingParm.VALUE.equals(property))
					data.value = value.toString();
				else
					data.dataTypeComboBox.setValue(value);

				viewer.refresh(data);
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new TextCellEditor(parent),
				new ComboBoxCellEditor(viewer.getTable(), SqlBindingParm.dataTypes, SWT.READ_ONLY) });

		viewer.setColumnProperties(new String[] { SqlBindingParm.PARAMETER, SqlBindingParm.VALUE, SqlBindingParm.DATA_TYPE });

	}

	public class ResourceBundleListener implements org.eclipse.swt.widgets.Listener {

		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.MeasureItem: {
					TableItem item = (TableItem) event.item;
					String text = item.getText(event.index);
					Point size = event.gc.textExtent(text);
					event.width = size.x;
					event.height = 17;

					break;
				}
				case SWT.PaintItem: {
					TableItem item = (TableItem) event.item;

					String text = item.getText(event.index);
					Point size = event.gc.textExtent(text);
					event.width = size.x;
					event.height = 17;

					break;
				}
				case SWT.EraseItem: {
					event.detail &= ~SWT.BACKGROUND;

					break;
				}
				default:
					break;
			}
		}

	}

	private class SqlBindingTableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			SqlBindingParm parm = (SqlBindingParm) element;

			switch (columnIndex) {
				case 0:
					return parm.parm;
				case 1:
					return parm.value;
				case 2:
					return parm.dataTypeComboBox.getItems()[((Integer) parm.dataTypeComboBox.getValue()).intValue()];
				default:
					return "unknown " + columnIndex;
			}
		}
	}

	protected void createMapContents(FormToolkit toolkit, Composite parent, int cols) {
		//2행 3행
		toolkit.createLabel(parent, "");

		Composite parmClsMapCps = toolkit.createComposite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		parmClsMapCps.setLayoutData(gd);

		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 0;
		formLayout.marginHeight = 0;
		formLayout.marginTop = 0;
		formLayout.marginWidth = 0;
		formLayout.marginRight = 0;

		parmClsMapCps.setLayout(formLayout);

		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, 0);
		fd.right = new FormAttachment(100, 0);

		parmClassCps = toolkit.createComposite(parmClsMapCps, SWT.NONE);
		parmClassCps.setLayoutData(fd);
		toolkit.paintBordersFor(parmClassCps);

		fd = new FormData();
		fd.top = new FormAttachment(0, 3);
		fd.left = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, 0);
		fd.right = new FormAttachment(100, 0);
		parmMapCps = toolkit.createComposite(parmClsMapCps, SWT.NONE);
		parmMapCps.setLayoutData(fd);
		toolkit.paintBordersFor(parmMapCps);

		GridLayout gl = new GridLayout(3, false);
		gl.marginBottom = 1;
		gl.marginHeight = 0;
		gl.marginTop = 1;

		parmClassCps.setLayout(gl);
		parmMapCps.setLayout(gl);

		paramClassField.create(toolkit, parmClassCps, cols);
		paramMapField.create(toolkit, parmMapCps, cols);

		paramClassField.addFieldListener(listener);
		paramMapField.addFieldListener(listener);
	}

	/**
	 * 화면 갱신
	 */
	@Override
	public void refresh() {
		refreshing = true;
		try {
			loadParameterMap();
			idField.setText(StringUtil.nvl(currentElement.getId()));
			//ComboField -> ComboEditField
			paramClassField.setItems(masterPart.getInitialTypes());
			String paramClassText = StringUtil.nvl(currentElement.getParameterClass());
			if("".equals(paramClassText)) {
				paramClassText = DbioMessages.mapper_display_Type_guide;
			}
			paramClassField.setText(paramClassText);
			paramMapField.setText(StringUtil.nvl(currentElement.getParameterMap()));
			queryViewer.setQuery(StringUtil.nvl(currentElement.getSQLStatement().trim()));
			
			//2017-06-17 In/Out에서 Paramter 설정에 따른 라디오 버튼(class, map) 설정 수정 (sh.jang)
			if ((currentElement.getParameterClass() == null && "".equals(currentElement.getParameterClass())) || (currentElement.getParameterMap() == null && "".equals(currentElement.getParameterMap()))) {

				paramRadioClass.setSelection(true);
				//ComboField -> ComboEditField
				paramClassField.setEnabled(true);
				parmClassCps.setVisible(true);
				
				paramRadioMap.setSelection(false);
				/*
				//(deprecated) ComboField -> TextField
				paramMapField.setEnabled(false);			
				*/
				parmMapCps.setVisible(false);
			//} else if (currentElement.getParameterClass() == null || "".equals(currentElement.getParameterClass())) {
			} else if (currentElement.getParameterMap() != null && !("".equals(currentElement.getParameterMap()))) {
				paramRadioClass.setSelection(false); 
				//ComboField -> ComboEditField
				paramClassField.setEnabled(false);
				parmClassCps.setVisible(false); 
				
				paramRadioMap.setSelection(true); 
				/*
				//(deprecated) ComboField -> TextField
				paramMapField.setEnabled(true);
				*/
				parmMapCps.setVisible(true); 
							
			} else {

				paramRadioClass.setSelection(true);
				//ComboField -> ComboEditField
				paramClassField.setEnabled(true);
				parmClassCps.setVisible(true);

				paramRadioMap.setSelection(false);
				/*
				//(deprecated) ComboField -> TextField
				paramMapField.setEnabled(false);
				*/
				parmMapCps.setVisible(false);

			}
			
			querySection.setExpanded(false);
			querySection.setExpanded(true);

			clearBindingVarTable();
		} finally {
			refreshing = false;
		}
	}

	private void loadParameterMap() {
		List<String> paramMaps = new LinkedList<String>();
		Element root = (Element) currentElement.getDOMElement().getParentNode();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element && "parameterMap".equals(((Element) child).getTagName())) {
				String id = ((Element) child).getAttribute("id"); //$NON-NLS-1$
				if (id != null && !"".equals(id.trim())) {
					//if (id != null && id.trim().length() > 0) {
					paramMaps.add(id);
				}
			}
		}
		String[] items = paramMaps.toArray(new String[paramMaps.size()]);
		/*
		//(deprecated) ComboField -> TextField
		paramMapField.setItems(items);
		*/
	}

	protected void openQueryBuilder() {
		//String profileName = this.getConnectionInfo().getConnectionProfileName();
		String sql = null;
		if (DTPUtil.getProfileNames().length == 0) {
			MessageDialog.openInformation(masterPart.getPage().getSite().getShell(), "Alert!", "Set Data Source Explorer");
			return;
		}

		if (dbInfoCombo.getText() == null || "".equals(dbInfoCombo.getText())) {
			MessageDialog.openInformation(masterPart.getPage().getSite().getShell(), "Open query builder", "Select \"DB Info\" first!");
		} else {
			OpenSQLBuilderDialogAction action = new OpenSQLBuilderDialogAction(masterPart.getPage().getSite(), this.getConnectionInfo(), queryViewer.getQuery());
			action.run();
			sql = action.getGeneratedSQLStatement();
		}

		if (sql != null) {
			sql = sql.trim();
			queryViewer.setQuery(sql);
			currentElement.setSQLStatement(sql);
		}
	}

	protected void textChanged(Object source) {
		if (!refreshing) {
			if (source == idField) {
				currentElement.setId(idField.getText());
				masterPart.refreshViewer(currentElement);
			} else if (source == paramClassField) {
				currentElement.setParameterClass(paramClassField.getText());
			} else if (source == paramMapField) {
				currentElement.setParameterMap(paramMapField.getText());
			} else if (source == queryViewer) {
				currentElement.setSQLStatement(queryViewer.getQuery());
			}
		}
	}

	protected void linkActivated(Object source) {
		if (source == paramClassField) {
			String newClass = masterPart.getPage().openOrCreateNewJavaClass(paramClassField.getText());
			if (newClass != null && !newClass.equals(paramClassField.getText())) {
				paramClassField.setText(newClass);
				currentElement.setParameterClass(newClass);
			}
		}
	}

	protected void buttonSelected(Object source) {
		if (source == paramClassField) {
			String newClass = masterPart.getPage().selectClass();
			if (newClass != null && !newClass.equals(paramClassField.getText())) {
				paramClassField.setText(newClass);
				currentElement.setParameterClass(newClass);
			}
		}
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof MapperCRUDElement) {
			currentElement = (MapperCRUDElement) element;
			refresh();
		} else {
			currentElement = null;
		}
	}

	/**
	 * getter
	 * @return site
	 */
	public IWorkbenchPartSite getSite() {
		return site;
	}

	/**
	 * setter
	 * 
	 * @param site
	 *            IWorkbenchPartSite object
	 */
	public void setSite(IWorkbenchPartSite site) {
		this.site = site;
	}

	public void handleEvent(Event event) {
		// DbioLog.logInfo("sham");
	}

	/**
	 * querySection을 최대화함. 
	 * @param SectionStateChange
	 */
	private void maximizeQuerySection(int SectionStateChange) {
		int tot_height = masterPart.getPage().getManagedForm().getForm().getBody().getClientArea().height;
		if (masterPart.getPage().getMdBlock().getSashForm().getOrientation() == SWT.VERTICAL) {
			tot_height = tot_height - masterPart.getSection().getClientArea().height;
		}

		int etc_margin_height = 120;
		int inOutSectionHeight = 0;
		int testSectionHeight = 0;
		switch (SectionStateChange) {
			case ON_INITIALIZE:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSection.getClientArea().height;
				break;
			case QUERY_SECTION_MAXIMIZE:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSection.getClientArea().height;
				break;
			case ON_INOUT_SECTION_OPEN:
				inOutSectionHeight = inOutSectionMaxHeight;
				testSectionHeight = testSection.getClientArea().height;
				break;
			case ON_TEST_SECTION_OPEN:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSectionMaxHeight;
				break;
			case ON_INOUT_SECTION_CLOSE:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSection.getClientArea().height;
				break;
			case ON_TEST_SECTION_CLOSE:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSection.getClientArea().height;
				break;
			default:
				inOutSectionHeight = inOutSection.getClientArea().height;
				testSectionHeight = testSection.getClientArea().height;
		}

		GridData gd = (GridData) queryComposite.getLayoutData();
		gd.heightHint = tot_height - (inOutSectionHeight + testSectionHeight + etc_margin_height);

		if (SectionStateChange == ON_INOUT_SECTION_CLOSE || SectionStateChange == ON_TEST_SECTION_CLOSE) {
			masterPart.detailRefresh();
		}

	}

}
