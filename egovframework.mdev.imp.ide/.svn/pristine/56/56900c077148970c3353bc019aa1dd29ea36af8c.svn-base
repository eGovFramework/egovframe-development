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
package egovframework.mdev.imp.ide.wizards.pages;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;
import egovframework.mdev.imp.ide.common.MoblieIdeMessages;
import egovframework.mdev.imp.ide.wizards.model.NewMobileProjectContext;

/**
 * Moblie eGovFramework 예제 탬플릿 추가 여부 선택 마법사 페이지 클래스
 * @author 이종대
 * @since 2011.07.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2011.07.13  	이종대          최초 생성
 *
 * 
 * </pre>
 */
public class SelectMobileExamplePage extends WizardPage {

    private final NewMobileProjectContext context;			// 컨텍스트

    private Button createExampleButton;						// 위젯 버튼
    private TableViewer sourceListTableViewer;	// 위젯 테이블뷰어
    private Table listTable;					// 위젯 테이블리스트
    private boolean fillExampleFileList = false; // 위젯 예제 파일 리스트

    /**
     * 생성자
     * @param pageName
     * @param context
     */
    public SelectMobileExamplePage(String pageName, NewMobileProjectContext context) {
        super(pageName);
        this.context = context;
        setTitle(MoblieIdeMessages.wizardsPagesSelectSamplePage0);
        setDescription(MoblieIdeMessages.wizardsPagesSelectSamplePage1);
    }

    /**
     * 선택 버튼 파트 생성
     * @param parent
     */
    private void createSelection(Composite parent) {

        createExampleButton = new Button(parent, SWT.CHECK);
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        createExampleButton.setLayoutData(gridData);
        createExampleButton.setText(MoblieIdeMessages.wizardsPagesSelectSamplePage2);
        createExampleButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                boolean isCreateExample = isCreateExample();
                context.setCreateExample(isCreateExample);
                if (isCreateExample && !fillExampleFileList) {
                    fillList(sourceListTableViewer);
                    fillExampleFileList = true;
                }
                listTable.setEnabled(isCreateExample);
            }
        });
    }

    /**
     * 예제 파일 리스트 위젯 생성
     * @param parent
     */
    private void createEntryLIst(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(MoblieIdeMessages.wizardsPagesSelectSamplePage3);
        group.setSize(0, 400);
        GridLayout dirLayout = new GridLayout();
        group.setLayout(dirLayout);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));

        sourceListTableViewer =
            new TableViewer(group, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI
                | SWT.FULL_SELECTION | SWT.BORDER | SWT.FILL);

        listTable = sourceListTableViewer.getTable();
        listTable.setHeaderVisible(true);
        listTable.setLinesVisible(false);
        listTable.setLayoutData(new GridData(GridData.FILL_BOTH));

        TableColumn typeColumn = new TableColumn(listTable, SWT.LEFT);
        typeColumn.setText(""); 
        typeColumn.setWidth(20);

        TableColumn nameColumn = new TableColumn(listTable, SWT.LEFT);
        nameColumn.setText(""); 
        nameColumn.setWidth(200);

        TableColumn pathColumn = new TableColumn(listTable, SWT.LEFT);
        pathColumn.setText(""); 
        pathColumn.setWidth(200);

        listTable.setEnabled(false);
    }

    /**
     * 예제 코드 생성여부 확인
     * @return
     */
    private boolean isCreateExample() {
        return createExampleButton.getSelection();
    }

    /**
     * 예제 파일 목록 데이터 설정
     * @param tableViewer
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void fillList(TableViewer tableViewer) {
        try {
            Path path = new Path(EgovMobileIdePlugin.getDefault().getInstalledPath());
            String zipFileName =
                path.append("examples/" + context.getDefaultExampleFile())
                    .toOSString();
            ZipFile zipFile = new ZipFile(zipFileName, "UTF-8");
            Enumeration enumeration = zipFile.getEntries();
            ZipEntry entry;
            ArrayList items = new ArrayList();
            while (enumeration.hasMoreElements()) {
                entry = (ZipEntry) enumeration.nextElement();
                String entryName = entry.getName();
                if (!entry.isDirectory()) {
                    if (entryName.lastIndexOf("/") < 0) { 
                        SourceItem item = new SourceItem(entryName, ""); 
                        items.add(item);
                    } else {
                        SourceItem item =
                            new SourceItem(entryName.substring(entryName.lastIndexOf("/") + 1) 
                            			 , entryName.substring(0, entryName.lastIndexOf("/"))); 
                        items.add(item);
                    }
                }
            }
            tableViewer.setLabelProvider(new SourceItemLabelProvider());
            tableViewer.setContentProvider(new ArrayContentProvider());
            tableViewer.setInput(items.toArray());

        } catch (Exception ex) {
            MoblieIdeLog.logError(ex);
        }
    }

    /**
     * UI 컨트롤 생성
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSelection(composite);
        createEntryLIst(composite);

        setControl(composite);
        setPageComplete(true);
    }

    /**
     * 라벨 제공자 클래스
     * @author 개발 환경 개발팀 이흥주
     */
    class SourceItemLabelProvider extends LabelProvider implements
            ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0)
                return EgovMobileIdePlugin.getDefault().getImage(EgovMobileIdePlugin.IMG_ITEM);
            else
                return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            SourceItem item = (SourceItem) element;
            switch (columnIndex) {
            case 0:
                return ""; 
            case 1:
                return item.getName();
            case 2:
                return item.getPath();
            default:
                return ""; 
            }
        }

    }

    /**
     * 소스 아이템
     * @author 개발 환경 개발팀 이흥주
     */
    class SourceItem {
        public SourceItem(String name, String path) {
            this.name = name;
            this.path = path;
        }

        private String name;
        private String path;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
