/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.wizards;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.abstractplugin.EclipseMessageUtils;
import net.sf.abstractplugin.ImageUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.sqm.core.definition.DatabaseDefinition;
import org.eclipse.datatools.connectivity.sqm.core.rte.jdbc.JDBCTable;
import org.eclipse.datatools.modelbase.dbdefinition.PredefinedDataTypeDefinition;
import org.eclipse.datatools.modelbase.sql.query.TableExpression;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn;
import org.eclipse.datatools.modelbase.sql.query.WithTableSpecification;
import org.eclipse.datatools.modelbase.sql.query.helper.TableHelper;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.DatabaseHelper;
import org.eclipse.datatools.sqltools.sqlbuilder.model.OmitSchemaInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.SQLDomainModel;
import org.eclipse.datatools.sqltools.sqlbuilder.provider.rdbschema.AvailableTablesTreeProvider;
import org.eclipse.eclipsework.wizard.EclipseWorkPage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import egovframework.dev.imp.codegen.template.CodeGenLog;
import egovframework.dev.imp.codegen.template.model.Attribute;
import egovframework.dev.imp.codegen.template.model.DataModelContext;
import egovframework.dev.imp.codegen.template.model.Entity;
import egovframework.dev.imp.codegen.template.util.DataToolsUtil;

/**
 * 
 * 테이블 선택 마법사 페이지 클래스
 * <p><b>NOTE:</b> DataTools에 정의된 DB연결정보로 부터 테이블 목록을 표시하고
 * 테이블 목록중 CRUD의 대상이될 하나의 테이블을 선택하는 마법사 페이지이다.
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
public class CodeGenTableWizardPage extends EclipseWorkPage {
    /** 로거 */
    private static Logger log = Logger.getLogger(CodeGenTableWizardPage.class);
    /** 마법사페이지 이름 */
    public static final String PAGE_NAME = "CodeGenTableWizardPage";
    /** 컴포지트 */
    private Composite composite = null;
    // private boolean pageNotInitialized = false;
    /** 데이터 페이스 정의 */
    private DatabaseDefinition databaseDefinition = null;

    /** DBInfo Combo 컴포넌트 */
    private Combo dbInfoCombo = null;
    
    /** DB 벤더 */
    private String vender = "";
    
    /** DB 제품명 */
    private String databaseProductName = "";

    /** 테이블 목록 트리 */
    private Tree tablesTree;
    
    /** 테이블 목록 트리 뷰어 */
    private TreeViewer tablesTreeViewer;   

    /** 테이블 선택 여부 */
    private boolean tableSelected = false;    


    /**
     * 
     * 생성자
     *
     * @param pageName
     */
    public CodeGenTableWizardPage(String pageName) {
        super(pageName);
    }

    /**
     * 
     * 생성자
     *
     * @param description
     * @param required
     * @param image
     */
    public CodeGenTableWizardPage(String description, boolean required,String image) {
        super(PAGE_NAME);

        this.required = required;

        if(StringUtils.isEmpty(description)) {
            setDescription("Select some Table");
        }
        else {
            setDescription(description);
        }

        try {
            if(image != null) {
                setImageDescriptor(ImageUtil.getImageDescriptor(image));
            }

        }
        catch (Exception e) {
            CodeGenLog.logError(e);
            log.error(e, e);
            EclipseMessageUtils.showError(e.getMessage());
            // pageNotInitialized = true;
        }
    }    
    
    /**
     * 
     * 컨트롤 생성
     *
     * @param parent
     */
    public void createControl(Composite parent) {
        setPageComplete(false);
        this.composite = new Composite(parent, SWT.NONE);
        GridLayout grid = new GridLayout(3, false);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        composite.setLayout(grid);

        Label dbInfoLabel = new Label(composite, SWT.NONE);
        dbInfoLabel.setText("DB Info: ");

        dbInfoCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        GridData gd_combo =
            new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_combo.widthHint = 150;
        dbInfoCombo.setLayoutData(gd_combo);
        dbInfoCombo.setItems(DataToolsUtil.getProfileNames());
        dbInfoCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                CodeGenLog.logInfo("Selected DB Info : " + dbInfoCombo.getText().trim());
                showTables();

            }
        });


        Group tablesGroup = new Group(composite, SWT.NONE);
        tablesGroup.setText("Tables");
        GridLayout dirLayout = new GridLayout(1, false);
        tablesGroup.setLayout(dirLayout);
        tablesGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

        tablesTree = new Tree(tablesGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData treeGridData = new GridData(GridData.FILL_BOTH);
        treeGridData.widthHint = 50;
        treeGridData.heightHint = 200;
        tablesTree.setLayoutData(treeGridData);    

        tablesTree.addSelectionListener(new TableSelectionListener());

        tablesTreeViewer = new TreeViewer(tablesTree);        
        setControl(composite);

        if (dbInfoCombo.getItemCount() > 0) {
            dbInfoCombo.select(0);
            showTables();
        }        
    }

    /**
     * 
     * 테이블 목록 보기
     *
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void showTables() {
        try {
            String dbInfoName = dbInfoCombo.getText().trim();

            CodeGenLog.logInfo("DB Info : " + dbInfoName);


            SQLDomainModel domainModel = new SQLDomainModel();
            

            OmitSchemaInfo schemaInfo = new OmitSchemaInfo();
            schemaInfo.initFromPreferences();
            domainModel.setOmitSchemaInfo(schemaInfo);


            ISQLEditorConnectionInfo connInfo = DataToolsUtil.getConnectionInfo(dbInfoName);
            if ( IConnectionProfile.CONNECTED_STATE != connInfo.getConnectionProfile().getConnectionState() ) {
            	setErrorMessage("The Database ["+dbInfoName+"] is not connected!!");
            	return;
            }
            connInfo.getConnectionProfile().connect();

            Connection conn = (Connection)connInfo.getConnectionProfile().getManagedConnection(Connection.class.getName()).getConnection().getRawConnection();
            try {
            	databaseProductName = conn.getMetaData().getDatabaseProductName();
            }catch(SQLException se){
            	setErrorMessage("The Database ["+dbInfoName+"] is not connected!!");
            	return;
            }
            
            domainModel.setConnectionInfo(connInfo);
            domainModel.setDatabase(connInfo.getDatabase());
            
            
            vender = connInfo.getDatabase().getVendor();
            
            this.databaseDefinition = domainModel.getDatabaseDefinition();

            domainModel.setCurrentSchema();

            List<?> schemaList = new ArrayList();
            Database database = domainModel.getDatabase();


            if (database != null){
                schemaList.addAll(DatabaseHelper.getSchemaList(database));
            }

            AvailableTablesTreeProvider provider = new AvailableTablesTreeProvider(schemaList, domainModel);
            tablesTreeViewer.setContentProvider(provider);
            tablesTreeViewer.setLabelProvider(provider);                    
            tablesTreeViewer.setInput(provider);

        } catch (Exception e) {
            CodeGenLog.logError(e);
        }        
    }

    /**
     * 
     * Velocity Context에 값 삽입
     *
     * @param map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void putValuesToVelocityContext(Map map) {
        
        TableExpression tableExpr = null;
        
        List tableList = this.getTablesList();
        Iterator itr = tableList.iterator();
        SQLObject table = null;
        
        DataModelContext dataModel = new DataModelContext();
        dataModel.setVender(vender);
        dataModel.setDatabaseProductName(databaseProductName);
        
        while (itr.hasNext()) {
            table = (SQLObject) itr.next();            
            if (table instanceof Table) {
                Entity entity = new Entity(table.getName());
                dataModel.setEntity(entity);
            
                tableExpr = TableHelper.createTableExpressionForTable((Table) table);
                EList colList= tableExpr.getColumnList();
                Iterator colItr = colList.iterator();
                
                List<Attribute> attributes = new ArrayList<Attribute>();
                List<Attribute> pkAttributes = new ArrayList<Attribute>();
                while(colItr.hasNext()) {
                    ValueExpressionColumn colExpr = (ValueExpressionColumn)colItr.next();
                    Attribute attr = new Attribute(colExpr.getName());
                    attr.setType(colExpr.getDataType().getName());
                    attr.setJavaType(getJavaClassName(colExpr.getDataType().getName()));
                    attributes.add(attr);
                    
                    Column column = TableHelper.getColumnForColumnExpression(tableExpr, colExpr);
                    if ((column != null) && TableHelper.isPrimaryKey(column)) {
                        attr.setPrimaryKey(true);
                        pkAttributes.add(attr);
                    }
                }
                
                /*
                List pkList = TableHelper.getPrimaryKeyColumns((Table)table);
                Iterator pkItr = pkList.iterator();
                while(pkItr.hasNext()) {
                    Column pk = (Column)pkItr.next();
                    System.out.println("PK Column Name: " + pk.getName());
                    //MySqlCatalogColumn column = null;
                }
                */
                
                dataModel.setAttributes(attributes);
                dataModel.setPrimaryKeys(pkAttributes);
            }        
        }
                
        map.put("model", dataModel);
        
    }
    
    /**
     * 
     * 자바 클래스 명 가져오기
     *
     * @param dataType
     * @return
     */
    private String getJavaClassName(String dataType){
        PredefinedDataTypeDefinition dd = databaseDefinition.getPredefinedDataTypeDefinition(dataType);
        return dd.getJavaClassName();
    }

    /**
     * 
     * 값 검증
     *
     * @return
     */
    public StringBuffer validatePage() {
        StringBuffer validate = null;
        if (!tableSelected) {
            validate = new StringBuffer("Select a table!!!");
        }
        return validate;
    }

    /**
     * 
     * 테이블 목록 가져오기
     *
     * @return
     */
    @SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private List getTablesList(){
        TreeItem[] items = tablesTreeViewer.getTree().getSelection();
        boolean aliasIsUnique = true;
        SQLObject tableValue;
        List tablesList;
        tableValue = null;
        tablesList = new ArrayList();

        if (items != null && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                TreeItem treeItem = items[i];
                Object treeItemData = treeItem.getData();
                if (treeItemData instanceof Table) {
                    tableValue = (Table) treeItemData;
                }
                else if (treeItemData instanceof WithTableSpecification) {
                    tableValue = (WithTableSpecification) treeItemData;
                }
                if (tableValue != null) {
                    tablesList.add(tableValue);
                }
            }
        }

        return tablesList;
    }  
    
       
    /**
     * 
     * 테이블 항목 선택 리스너 클래스
     * <p><b>NOTE:</b> 테이블 항목 선택 리스너 클래스
     * 
     * @author 개발환경 개발팀 이흥주
     * @since 2009.08.03
     * @version 1.0
     * @see
     *
     * <pre>
     *  == 개정이력(Modification Information) ==
     *   
     *   수정일      수정자           수정내용
     *  -------    --------    ---------------------------
     *   2009.08.03  이흥주          최초 생성
     *
     * </pre>
     */
    class TableSelectionListener implements SelectionListener {

        /**
         * 
         * 위젯 기본 선택 이벤트
         *
         * @param e
         */
        public void widgetDefaultSelected(SelectionEvent e) {
            //
        }

        /**
         * 
         * 위젯 선택 이벤트
         *
         * @param e
         */
        public void widgetSelected(SelectionEvent e) {
            TreeItem[] items = tablesTreeViewer.getTree().getSelection();


            setPageComplete(false);
        	tableSelected = false;
            
            for (int i = 0; i < items.length; i++) {
                TreeItem treeItem = items[i];
                Object treeItemData = treeItem.getData();
                if (treeItemData instanceof Table || treeItemData instanceof JDBCTable || treeItemData instanceof WithTableSpecification) {
                	setPageComplete(true);
                	tableSelected = true;
                }
            }
        }
    }



}
