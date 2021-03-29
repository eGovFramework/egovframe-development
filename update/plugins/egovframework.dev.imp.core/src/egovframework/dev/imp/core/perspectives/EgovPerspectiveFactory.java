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
package egovframework.dev.imp.core.perspectives;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import egovframework.dev.imp.ui.EgovCorePlugin;

/**
 * eGovFramework 퍼스펙티브 팩토리 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 *   2011.06.13  조윤정           수정
 * 
 * 
 * </pre>
 */
public class EgovPerspectiveFactory implements IPerspectiveFactory,
        EgovPerspective {

    private IPageLayout layout = null;

    /**
     * 초기 레이아웃 생성
     */
    public void createInitialLayout(IPageLayout layout) {
        this.layout = layout;
        addViews();
        addNewWizardShortcuts();
        addShowViewShortcuts();
        addPerspectiveShortcuts();
        addActionSets();
        addFastView();
    }
    

    /**
     * 레이아웃 설정
     * @param layout
     */
    private void addViews() {
    	
        String editorArea = layout.getEditorArea();
        
        IFolderLayout leftFolder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea); 
        leftFolder.addView(JavaUI.ID_PACKAGES);
        leftFolder.addView("org.eclipse.datatools.connectivity.DataSourceExplorerNavigator"); 
        // org.eclipse.datatools.connectivity.ui.dse
        // org.eclipse.datatools.connectivity.DataSourceExplorerNavigator

        IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea); 
        bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottomFolder.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
        bottomFolder.addView(IPageLayout.ID_TASK_LIST);
        bottomFolder.addView(IPageLayout.ID_PROP_SHEET);
        bottomFolder.addView("org.eclipse.wst.server.ui.ServersView"); 
        bottomFolder.addView("com.mountainminds.eclemma.ui.CoverageView"); 
        bottomFolder.addView(IPageLayout.ID_BOOKMARKS);
        bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        bottomFolder.addView(JavaUI.ID_JAVADOC_VIEW);
        bottomFolder.addView("org.eclipse.team.svn.ui.repository.RepositoriesView");
        
        // ResultView
        bottomFolder.addView("org.eclipse.datatools.sqltools.result.resultView"); 

        // org.eclipse.datatools.sqltools.plan
        // Execution Plan
        bottomFolder.addView("org.eclipse.datatools.sqltools.plan.planView"); 		

        IFolderLayout rightFolder = layout.createFolder("right", IPageLayout.RIGHT, (float) 0.75, editorArea); 
        rightFolder.addView(IPageLayout.ID_OUTLINE);
        rightFolder.addView("org.eclipse.wst.common.snippets.internal.ui.SnippetsView"); 

    }

    private void addActionSets() {
        layout.addActionSet("org.eclipse.jdt.ui.JavaElementCreationActionSet"); 
        layout.addActionSet("org.eclipse.jdt.ui.JavaActionSet"); 
//        layout.addActionSet("com.mountainminds.eclemma.ui.CoverageActionSet"); 

        layout.addActionSet(EgovCorePlugin.ID_ACTION_SET);
    }

    /**
     * 신규 마법사 단축메뉴 추가
     */
    private void addNewWizardShortcuts() {
//        layout.addNewWizardShortcut(EgovCorePlugin.ID_CORE_PROJECT_WIZARD);
//        layout.addNewWizardShortcut(EgovCorePlugin.ID_WEB_PROJECT_WIZARD);
//        layout.addNewWizardShortcut(EgovCorePlugin.ID_TEMPLATE_PROJECT_WIZARD);
//        layout.addNewWizardShortcut(EgovCorePlugin.ID_WEB_MOBILE_PROJECT_WIZARD);
//        layout.addNewWizardShortcut(EgovCorePlugin.ID_MOBILE_TEMPLATE_PROJECT_WIZARD);
        
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard"); 
        layout.addNewWizardShortcut("org.eclipse.jst.servlet.ui.project.facet.WebProjectWizard"); 
        layout.addNewWizardShortcut("org.maven.ide.eclipse.wizards.Maven2ProjectWizard"); 
 
        // org.eclipse.jdt.ui
        // common component
//        layout.addNewWizardShortcut("egovframework.dev.imp.commngt.wizards.newEgovCommngt");
//        layout.addNewWizardShortcut("egovframework.mdev.imp.commngt.wizards.AddMobileCommngtWizard");
        
        // Package
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); 
        // Class
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); 
        // Interface
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); 
        // Enum
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard"); 
        // Annotation
        layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard"); 

        // org.eclipse.ui.ide
        // Folder
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder"); 
        // File
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file"); 

        // org.eclipse.wst.css.ui
        layout.addNewWizardShortcut("org.eclipse.wst.css.ui.internal.wizard.NewCSSWizard"); 
        // org.eclipse.jst.jsp.ui
        layout.addNewWizardShortcut("org.eclipse.jst.jsp.ui.internal.wizard.NewJSPWizard"); 
        // org.eclipse.wst.html.ui
        layout.addNewWizardShortcut("org.eclipse.wst.html.ui.internal.wizard.NewHTMLWizard"); 

        // net.java.amateras.db
        // ER Diagram
        layout.addNewWizardShortcut("net.java.amateras.db.newDiagramWizard"); 

        // net.java.amateras.umleditor
        // Class Diagram
        layout.addNewWizardShortcut("net.java.amateras.umleditor.wizard1"); 

        // org.springframework.ide.eclipse.beans.ui
        // Spring Bean Definition
        layout.addNewWizardShortcut("org.springframework.ide.eclipse.beans.ui.wizards.newBeansConfig"); 

        // org.springframework.ide.eclipse.webflow.ui
        // Spring Web Flow Definition
        layout.addNewWizardShortcut("org.springframework.ide.eclipse.webflow.ui.wizard.newWebflowConfigWizard"); 

        // org.eclipse.jdt.junit
        layout.addNewWizardShortcut("org.eclipse.jdt.junit.wizards.NewTestCaseCreationWizard"); 
        layout.addNewWizardShortcut("org.eclipse.jdt.junit.wizards.NewTestSuiteCreationWizard"); 

    }

    /**
     * 뷰 보기 단축메뉴 추가
     * @param layout
     */
    private void addShowViewShortcuts() {

        layout.addShowViewShortcut(JavaUI.ID_PACKAGES);

        layout.addShowViewShortcut("org.eclipse.datatools.sqltools.result.resultView"); 
        layout.addShowViewShortcut("org.eclipse.datatools.sqltools.plan.planView"); 		
        layout.addShowViewShortcut("org.eclipse.datatools.connectivity.DataSourceExplorerNavigator"); 		

        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut("org.eclipse.wst.server.ui.ServersView"); 
        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
//        layout.addShowViewShortcut("com.mountainminds.eclemma.ui.CoverageView"); 
        layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);

        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut("org.eclipse.wst.common.snippets.internal.ui.SnippetsView"); 

        // org.springframework.ide.eclipse.aop.ui
        // Spring Explorer
        layout.addShowViewShortcut("org.springframework.ide.eclipse.ui.navigator.springExplorer"); 
        // org.springframework.ide.eclipse.aop.ui
        // Spring AOP Event Trace
        layout.addShowViewShortcut("org.springframework.ide.eclipse.aop.ui.tracing.eventTraceView"); 

//        layout.addShowViewShortcut("egovframework.dev.imp.dbio.view.queryResult");
//        layout.addShowViewShortcut("egovframework.dev.imp.dbio.view.dbioSearch");
//        layout.addShowViewShortcut("egovframework.dev.tst.tcgenerator.views.TestCaseTemplateListView");
    }

    /**
     * 빠른보기 추가
     * @param layout
     */
    private void addFastView() {
        // layout.addFastView(IConsoleConstants.ID_CONSOLE_VIEW);
    }

    /**
     * 퍼스펙티브 단축메뉴 추가
     * @param layout
     */
    private void addPerspectiveShortcuts() {
        layout.addPerspectiveShortcut(JavaUI.ID_PERSPECTIVE);
        layout.addPerspectiveShortcut(IDebugUIConstants.ID_DEBUG_PERSPECTIVE);
        layout.addPerspectiveShortcut("org.eclipse.jst.j2ee.J2EEPerspective"); 
        layout.addPerspectiveShortcut("org.eclipse.wst.web.ui.webDevPerspective"); 

        // org.eclipse.team.svn.ui
        // SVN Repository Exploring
        layout.addPerspectiveShortcut("org.eclipse.team.svn.ui.repository.RepositoryPerspective"); 

        // org.eclipse.datatools.sqltools.sqleditor
        // Database Development
        layout.addPerspectiveShortcut("org.eclipse.datatools.sqltools.sqleditor.perspectives.EditorPerspective"); 
    }
}
