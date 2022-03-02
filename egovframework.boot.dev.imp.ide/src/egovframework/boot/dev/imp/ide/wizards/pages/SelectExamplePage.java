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
package egovframework.boot.dev.imp.ide.wizards.pages;

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

import egovframework.boot.dev.imp.ide.EgovBootIdePlugin;
import egovframework.boot.dev.imp.ide.common.BootIdeLog;
import egovframework.boot.dev.imp.ide.common.BootIdeMessages;
import egovframework.boot.dev.imp.ide.wizards.model.NewProjectContext;

/**
 * 예제 탬플릿 추가 여부 선택 마법사 페이지 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class SelectExamplePage extends WizardPage {

    private final NewProjectContext context;

    /** 위젯 */
    private Button createExampleButton;
    private TableViewer sourceListTableViewer;
    private Table listTable;
    private boolean fillExampleFileList = false;

    /**
     * 생성자
     * @param pageName
     * @param context
     */
    public SelectExamplePage(String pageName, NewProjectContext context) {
        super(pageName);
        this.context = context;
        setTitle(BootIdeMessages.wizardspagesSelectSamplePage0);
        setDescription(BootIdeMessages.wizardspagesSelectSamplePage1);
    }

    /**
     * 선택 버튼 파트 생성
     * @param parent
     */
    private void createSelection(Composite parent) {

        createExampleButton = new Button(parent, SWT.CHECK);
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        createExampleButton.setLayoutData(gridData);
        createExampleButton
            .setText(BootIdeMessages.wizardspagesSelectSamplePage2);
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
        group.setText(BootIdeMessages.wizardspagesSelectSamplePage3);
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
    private void fillList(TableViewer tableViewer) {
        try {
            Path path = new Path(EgovBootIdePlugin.getDefault().getInstalledPath());
            String zipFileName =
                path.append("examples/" + context.getDefaultExampleFile())
                    .toOSString();
            ZipFile zipFile = new ZipFile(zipFileName, "UTF-8");
            Enumeration<?> enumeration = zipFile.getEntries();
            ZipEntry entry;
            ArrayList<SourceItem> items = new ArrayList<SourceItem>();
            while (enumeration.hasMoreElements()) {
                entry = (ZipEntry) enumeration.nextElement();
                String entryName = entry.getName();
                if (!entry.isDirectory()) {
                    if (entryName.lastIndexOf("/") < 0) { 
                        SourceItem item = new SourceItem(entryName, ""); 
                        items.add(item);
                    } else {
                        SourceItem item =
                            new SourceItem(entryName.substring(entryName
                                .lastIndexOf("/") + 1) 
                                , entryName.substring(0, entryName
                                    .lastIndexOf("/"))); 
                        items.add(item);
                    }
                }
            }
            tableViewer.setLabelProvider(new SourceItemLabelProvider());
            tableViewer.setContentProvider(new ArrayContentProvider());
            tableViewer.setInput(items.toArray());

        } catch (Exception ex) {
        	BootIdeLog.logError(ex);
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
                return EgovBootIdePlugin.getDefault().getImage(
                    EgovBootIdePlugin.IMG_ITEM);
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
