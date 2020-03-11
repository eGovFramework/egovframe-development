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
package egovframework.dev.imp.codegen.template.views;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * 템플릿 목록 뷰어 클래스
 * <p><b>NOTE:</b> 템플릿 플러그인의 템플릿 목록을 TreeViewer로 표시하는 클래스
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
public class TemplatesView extends ViewPart {


    /** 마법사 열기 액션 */
    private Action openWizardAction;
    /** 트리구조를 모두 펼치게 하는 Eclipse 액션처리 */
    private Action expandAllAction;

    /** 템플릿의 트리구조 처리를 위한 TreeView */
    TreeViewer treeViewer;	

    /** 로거 */
    Logger log = Logger.getLogger(TemplatesView.class);


    /**
     * 
     * 생성자
     *
     */
    public TemplatesView() {

    }

    /**
     * 
     * 컨트롤 생성
     *
     * @param parent
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        
        
        
        createTree(parent);

        openWizardAction = createOpenWizardAction();
        
        hookContextMenu();
        makeActions();
        contributeToActionBars();
    }

    /**
     * 
     * setFocus
     *
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
    }

    /**
     * 
     * 트리 생성
     *
     * @param parent
     */
    private void createTree(Composite parent) {

        treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        treeViewer.setContentProvider(new ViewContentProvider());
        treeViewer.setLabelProvider(new ViewLabelProvider());
        //		treeViewer.setSorter(new NameSorter());
        treeViewer.setInput(ResourcesPlugin.getWorkspace());

        treeViewer.addDoubleClickListener(new IDoubleClickListener(){

            public void doubleClick(DoubleClickEvent event) {
                openWizardAction.run();
            }

        });
        
        
    }

    /**
     * 
     * 마법사열기액션 생성
     *
     * @return
     */
    private Action createOpenWizardAction(){
        return new OpenWizardAction(this);
    }

    /**
     * Eclipse 플러그인 액션 생성.
     * @return void
    */
    private void makeActions() {
        expandAllAction = new Action()
        {
                public void run()
                {
                        treeViewer.expandAll();
                }
        };
        expandAllAction
                        .setText("Expand All");
        expandAllAction
                        .setToolTipText("Expand All");
        expandAllAction
                        .setImageDescriptor(net.sf.abstractplugin.ImageUtil
                                        .getImageDescriptor("expandAll.gif"));        
    }
    
    /**
     * Eclipse 기반 플러그인의 툴바 지원.
     * @return void
    */
    private void contributeToActionBars()
    {
            IActionBars bars = getViewSite().getActionBars();
            fillToolBar(bars.getToolBarManager());
    }  
    
    /**
     * Eclipse 툴바 구성/채우기.
     * @param  manager - IToolBarManager 구현체
     * @return void
    */
    private void fillToolBar(IToolBarManager manager)
    {
            manager.add(new Separator());
            manager.add(expandAllAction);
    }  
    
    /**
     * 컨텍스트 메뉴 후킹.
     * @return void
    */
    private void hookContextMenu()
    {
            MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
            menuMgr.setRemoveAllWhenShown(true);

            Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
            treeViewer.getControl().setMenu(menu);
            getSite().registerContextMenu(menuMgr, treeViewer);
    }    

}
