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


import java.util.LinkedList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.misc.StringMatcher;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.actions.MapperAddAliasAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddCacheModelAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddParameterMapAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQueryDeleteAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQueryInsertAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQueryProcedureAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQuerySelectAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQueryStatementAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddQueryUpdateAction;
import egovframework.dev.imp.dbio.editor.actions.MapperAddResultMapAction;
import egovframework.dev.imp.dbio.editor.actions.MapperDeleteElementAction;
import egovframework.dev.imp.dbio.editor.model.DOMElementProxy;
import egovframework.dev.imp.dbio.editor.model.MapperAliasElement;
import egovframework.dev.imp.dbio.editor.model.MapperAliasGroupElement;
import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.editor.model.MapperElement;
import egovframework.dev.imp.dbio.editor.model.MapperGroupElement;
import egovframework.dev.imp.dbio.editor.model.MapperParameterMapElement;
import egovframework.dev.imp.dbio.editor.model.MapperParameterMapGroupElement;
import egovframework.dev.imp.dbio.editor.model.MapperQueryGroupElement;
import egovframework.dev.imp.dbio.editor.model.MapperResultMapElement;
import egovframework.dev.imp.dbio.editor.model.MapperResultMapGroupElement;
import egovframework.dev.imp.dbio.editor.model.MapperSelectElement;
import egovframework.dev.imp.dbio.editor.pages.MapperPage;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * MapperMasterPart (Mapper Tree 화면)
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
@SuppressWarnings("restriction")
public class MapperMasterPart{
/*
	public static final int QUERY_GROUP = 0;
	public static final int ALIAS_GROUP = 1;
	public static final int PARAMETER_GROUP = 2;
	public static final int RESULT_GROUP =3;
	public static final int CACHE_MODEL_GROUP = 4;
*/
	public static final int QUERY_GROUP = 0;
	public static final int RESULT_GROUP = 1;
	public static final int ALIAS_GROUP = 2; //unused
	public static final int PARAMETER_GROUP = 3; //unused
	public static final int CACHE_MODEL_GROUP = 4; //unused
	
	private TextField namespace;
	private Element element;
	private LinkedList<String> loadedTypes;
		
	private MapperPage page;
	private Text filterText;

	private TreeViewer viewer;
	//private Filter filter = new Filter();
	private Filter filter;
	private MapperAddQuerySelectAction addQuerySelectAction;
	private MapperAddQueryInsertAction addQueryInsertAction;
	private MapperAddQueryUpdateAction addQueryUpdateAction;
	private MapperAddQueryDeleteAction addQueryDeleteAction;
	private MapperAddAliasAction addAliasAction;
	private MapperAddParameterMapAction addParamaeterMapAction;
	private MapperAddResultMapAction addResultMapAction;
	private MapperAddQueryStatementAction addQueryStatementAction;
	private MapperAddQueryProcedureAction addQueryProcedureAction;
	private MapperAddCacheModelAction addCacheModelAction;
	private MapperDeleteElementAction deleteAction;
	
	private Section section;
	
	public MapperMasterPart(MapperPage page) {
		this.setPage(page);
		this.setNameSpace();
		setFilter();
	}
	
	private void setNameSpace() {
		this.namespace = new TextField("Namespace:");
	}

	/**
	 * 필터 초기화
	 */
	private void setFilter() {
		this.filter = new Filter();
	}
	
	/**
	 * 페이지 설정
	 * 
	 * @param page
	 */
	private void setPage(MapperPage page) {
		this.page = page;
	}

	/**
	 * Mapper tree 화면 구성
	 * @param managedForm
	 * @param parent
	 */
	public void createContents(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();

		section = toolkit.createSection(parent, Section.TITLE_BAR);
		section.marginWidth = 10;
		section.marginHeight = 5;
		section.setText("Mapper");

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);

		//-------------------
		namespace.create(toolkit, client, 2);
		namespace.addFieldListener(new IFieldListener() {
			public void eventOccured(FieldEvent event) {
				if (event.getType() == FieldEvent.Type.TextChanged && element != null) {
					element.setAttribute("namespace", namespace.getText()); //$NON-NLS-1$
				}
			}
		});

		toolkit.createLabel(client, "Filter:");
		filterText = toolkit.createText(client, ""); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		filterText.setLayoutData(gd);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filterChanged();
			}
		});
		//------------
				
		Tree tree = toolkit.createTree(client, SWT.MULTI | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.widthHint = 20;
		gd.heightHint = 20;
		tree.setLayoutData(gd);
		
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		toolkit.paintBordersFor(client);

		viewer = new TreeViewer(tree);
		viewer.setContentProvider(new MapperContentProvider());
		viewer.setLabelProvider(new MapperLabelProvider());
		viewer.addFilter(filter);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL
						&& deleteAction.isEnabled()) {
					//deleteAction.run();
					chkIdValidation();
				}
			}
		});			
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				refreshContextMenu(event.getSelection()); // 수정 
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		
		toolkit.paintBordersFor(parent);
		
		makeActions();
		createContextMenu();
	}
	
	/**
	 * RootElement 설정
	 * @param element
	 */
	private void setRootElement(Element element) {
		this.element = element;
		if (element == null) {
			namespace.setText(""); //$NON-NLS-1$
		} else {
			namespace.setText(StringUtil.nvl(element.getAttribute("namespace"))); //$NON-NLS-1$
			loadTypes(element);
		}
	}

	private void loadTypes(Element root) {
		loadedTypes = new LinkedList<String>();
		loadedTypes.add("byte");
		loadedTypes.add("int");
		loadedTypes.add("short");
		loadedTypes.add("long");
		loadedTypes.add("float");
		loadedTypes.add("double");
		loadedTypes.add("byte[]");
		loadedTypes.add("char");
		loadedTypes.add("java.lang.String");
		loadedTypes.add("java.lang.BigDecimal");
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "typeAlias".equals(((Element) child).getTagName())) {
				String alias = ((Element) child).getAttribute("alias"); //$NON-NLS-1$
				if (alias != null && !"".equals(alias.trim())) {
				//if (alias != null && alias.trim().length() > 0) {
					loadedTypes.add(alias);
				}
			}
		}
	}

	public String[] getInitialTypes() {
		if (loadedTypes == null) {
			return new String[0];
		} else {
			return loadedTypes.toArray(new String[loadedTypes.size()]);
		}
	}
	
	/**
	 * InitialTypes 인지 검사 
	 * @return
	 */
	public boolean isInitialTypes(String className) {
		return loadedTypes.contains(className);
	}

	private void makeActions() {
		///*
		addQuerySelectAction = new MapperAddQuerySelectAction(page, viewer);
		addQueryInsertAction = new MapperAddQueryInsertAction(page, viewer);
		addQueryUpdateAction = new MapperAddQueryUpdateAction(page, viewer);
		//addQueryProcedureAction = new MapperAddQueryProcedureAction(page, viewer);
		//addQueryStatementAction = new MapperAddQueryStatementAction(page, viewer);
		addQueryDeleteAction = new MapperAddQueryDeleteAction(page, viewer);
		addAliasAction = new MapperAddAliasAction(page, viewer);
		addParamaeterMapAction = new MapperAddParameterMapAction(page, viewer);
		addResultMapAction = new MapperAddResultMapAction(page, viewer);
		//addCacheModelAction = new MapperAddCacheModelAction(page, viewer);
		
		deleteAction = new MapperDeleteElementAction(page, viewer);
		//*/
		viewer.addSelectionChangedListener(addQuerySelectAction);
		viewer.addSelectionChangedListener(addQueryInsertAction);
		viewer.addSelectionChangedListener(addQueryUpdateAction);
		viewer.addSelectionChangedListener(addQueryDeleteAction);
		//viewer.addSelectionChangedListener(addQueryStatementAction);
		viewer.addSelectionChangedListener(addParamaeterMapAction);
		viewer.addSelectionChangedListener(addAliasAction);
		viewer.addSelectionChangedListener(addResultMapAction);
		//viewer.addSelectionChangedListener(addCacheModelAction);
		viewer.addSelectionChangedListener(deleteAction);
	}
	
	private void createContextMenu() {
		MenuManager manager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		Menu menu = manager.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		
		manager.add(addQuerySelectAction);
		manager.add(addQueryInsertAction);
		manager.add(addQueryUpdateAction);
		manager.add(addQueryDeleteAction);
		//manager.add(addQueryStatementAction);
		//manager.add(addQueryProcedureAction);
		manager.add(addAliasAction);
		manager.add(addParamaeterMapAction);
		manager.add(addResultMapAction);
		//manager.add(addCacheModelAction);
		manager.add(deleteAction);
	}

	/**
	 * 그룹/멤버별 context 메뉴를 조정함. 
	 * @param selection
	 */
	private void refreshContextMenu(ISelection selection) {
		MenuManager manager = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		Menu menu = manager.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		if (selection instanceof TreeSelection){
			TreeSelection treeSelection = (TreeSelection) selection;
			if (treeSelection.size()==0){
				createContextMenu();
			}
			else if (treeSelection.size()>1){
				manager.add(deleteAction);				
			}else {
				if (treeSelection.getFirstElement() instanceof MapperSelectElement
						|| ((TreeSelection) selection).getFirstElement() instanceof MapperCRUDElement
						|| ((TreeSelection) selection).getFirstElement() instanceof MapperQueryGroupElement){
					manager.add(addQuerySelectAction);
					manager.add(addQueryInsertAction);
					manager.add(addQueryUpdateAction);
					manager.add(addQueryDeleteAction);
					//manager.add(addQueryStatementAction);
					//manager.add(addQueryProcedureAction);
				}else if (treeSelection.getFirstElement() instanceof MapperAliasElement
					|| ((TreeSelection) selection).getFirstElement() instanceof MapperAliasGroupElement){
					manager.add(addAliasAction);
				}else if (treeSelection.getFirstElement() instanceof MapperParameterMapElement
					|| ((TreeSelection) selection).getFirstElement() instanceof MapperParameterMapGroupElement){
					manager.add(addParamaeterMapAction);
				}else if (treeSelection.getFirstElement() instanceof MapperResultMapElement
					|| ((TreeSelection) selection).getFirstElement() instanceof MapperResultMapGroupElement){
					manager.add(addResultMapAction);
				}/*else if (treeSelection.getFirstElement() instanceof MapperCacheModelElement
						|| ((TreeSelection) selection).getFirstElement() instanceof MapperCacheModelGroupElement){
					manager.add(addCacheModelAction);
				}*/
				manager.add(deleteAction);
			}		
		}	
	}
	
	protected void filterChanged() {
		filter.setFilter(filterText.getText());
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				viewer.refresh();
				alertOnTreeItem(); // 추가 				
			}
		});
	}
	
	/**
	 * 화면 갱신
	 * @param element
	 */
	public void refreshViewer(Object element) {
		viewer.refresh(element, true);
		//viewer.expandAll();
		viewer.expandToLevel(element, TreeViewer.ALL_LEVELS);
		page.getEditor().refreshOutlinePage();		
		if (element instanceof MapperElement)
			chkIdValidation();  	
		
	}

	/**
	 * 화면 갱신
	 */
	public void refreshViewer() {
		viewer.refresh();
		//viewer.expandAll();
		viewer.expandAll();
		page.getEditor().refreshOutlinePage();				
	}

	/**
	 * Mapper 페이지 반환
	 * @return Mapper 페이지
	 */
	public MapperPage getPage() {
		return page;
	}

	public void setInput(Object input) {
		if (viewer != null) {
			ISelection selection = viewer.getSelection();
			viewer.setInput(input);
			viewer.setSelection(selection);
		}
		setRootElement((Element)input);
		chkIdValidation();	
	}
	
	private static class MapperContentProvider implements ITreeContentProvider {
		private Object[] groups;

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Element) {
				return getGroups((Element) parentElement);
			} else if (parentElement instanceof MapperGroupElement) {
				return ((MapperGroupElement) parentElement).getChildren();
			} else {
				return new Object[0];
			}
		}
		
		private Object[] getGroups(Element element) {
			if (!"mapper".equals(element.getNodeName())) return new Object[0]; //$NON-NLS-1$
			
			if (groups == null) {
				/*
				groups = new MapperGroupElement[5];
				groups[QUERY_GROUP] = new MapperQueryGroupElement(element);
				groups[ALIAS_GROUP] = new MapperAliasGroupElement(element);
				groups[PARAMETER_GROUP] = new MapperParameterMapGroupElement(element);
				groups[RESULT_GROUP] = new MapperResultMapGroupElement(element);
				groups[CACHE_MODEL_GROUP] = new MapperCacheModelGroupElement(element);
				*/
				groups = new MapperGroupElement[2];
				groups[QUERY_GROUP] = new MapperQueryGroupElement(element);
				groups[RESULT_GROUP] = new MapperResultMapGroupElement(element);			
			}
			return groups;
		}

		public Object getParent(Object element) {
			if (element instanceof DOMElementProxy) {
				return ((DOMElementProxy) element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof MapperGroupElement) {
				return ((MapperGroupElement) element).getChildren().length > 0;
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
			this.groups = null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.groups = null;
		}
	}
	
	private static class MapperLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		
		@Override
		public String getText(Object element) {
			if (element instanceof MapperGroupElement) {
				return ((MapperGroupElement) element).getName();
			} else if (element instanceof MapperElement) {
				if (element instanceof MapperAliasElement) {
					return ((MapperElement) element).getAlias();
				} 
				else {
					return ((MapperElement) element).getId();
				}
			} else {
				return super.getText(element);
			}
		}
	}
	
	private class Filter extends ViewerFilter {
		private StringMatcher matchPattern;
		
		public void setFilter(String namePattern) {
			matchPattern = new StringMatcher("*" + namePattern + "*", true, false); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof MapperGroupElement) return true;
			
			ILabelProvider labelProvider = (ILabelProvider) MapperMasterPart.this.viewer.getLabelProvider();
			if (matchPattern != null) {
				return matchPattern.match(labelProvider.getText(element));
			}
			return true;
		}
	}
	
	
	/**
	 * element 에 대한 validation 체크 
	 * @param element
	 * @return
	 */	
	public void chkIdValidation(){ 
		alertOnTreeItem();
		Form form = page.getManagedForm().getForm().getForm();		
		initErrorMessage(form);
		
		//시스템 오류 방지를 위해 try~catch구문 추가 (sh.jang - 20170628)
		try{
			if (element.hasChildNodes()){
				if (chkIdValidation(element,"select"))
					displayErrorMessage(form,DbioMessages.mapper_err_QueryId_invalid);
				if (chkIdValidation(element,"insert"))
					displayErrorMessage(form,DbioMessages.mapper_err_QueryId_invalid);
				if (chkIdValidation(element,"delete"))
					displayErrorMessage(form,DbioMessages.mapper_err_QueryId_invalid);
				if (chkIdValidation(element,"update"))
					displayErrorMessage(form,DbioMessages.mapper_err_QueryId_invalid);
				if (chkIdValidation(element,"parameterMap"))
					displayErrorMessage(form,DbioMessages.mapper_err_ParameterMapId_invalid);
				if (chkIdValidation(element,"resultMap"))
					displayErrorMessage(form,DbioMessages.mapper_err_ResultMapId_invalid);
				if (chkIdValidation(element,"typeAlias"))
					displayErrorMessage(form,DbioMessages.mapper_err_AliasName_invalid);
			}
			
			chkDuplicate();
		
		}catch (Exception ne) {
			// TODO: handle exception
		}
			
	}
	
	/**
	 * element tag 에 대한 id validation 체크.. 현재는 공란만 체크함.   
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkIdValidation(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		Node nodeI = null;
		for (int i=0;i<temp.getLength();i++){
			if (TagName.equals("typeAlias")){
				nodeI = temp.item(i).getAttributes().getNamedItem("alias");
			}else{
				nodeI = temp.item(i).getAttributes().getNamedItem("id");
			}			
			if (nodeI!=null && nodeI.getNodeValue().equals("")){
				return true;
			}
		}
		return false;		
	}
	
	/**
	 * element 에 대한 id 중복 체크함 
	 * @param element
	 * @return
	 */	
	public void chkDuplicate(){ 
		Form form = page.getManagedForm().getForm().getForm();
		if (element.hasChildNodes()){
			if (chkDuplicate(element,"select"))
				displayErrorMessage(form,DbioMessages.mapper_err_QueryId_duplication);
			if (chkDuplicate(element,"insert"))
				displayErrorMessage(form,DbioMessages.mapper_err_QueryId_duplication);
			if (chkDuplicate(element,"update"))
				displayErrorMessage(form,DbioMessages.mapper_err_QueryId_duplication);
			if (chkDuplicate(element,"delete"))
				displayErrorMessage(form,DbioMessages.mapper_err_QueryId_duplication);
			if (chkDuplicate(element,"parameterMap"))
				displayErrorMessage(form,DbioMessages.mapper_err_ParameterMapId_duplication);
			if (chkDuplicate(element,"resultMap"))
				displayErrorMessage(form,DbioMessages.mapper_err_ResultMapId_duplication);
			if (chkDuplicate(element,"typeAlias"))
				displayErrorMessage(form,DbioMessages.mapper_err_AliasName_duplication);
		}
		
	}
	
	/**
	 * element 에 대한 id 중복 체크함 
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkDuplicate(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		Node nodeI = null;
		Node nodeJ = null;
		
		for (int i=0;i<temp.getLength();i++){
			for (int j=i;j<temp.getLength();j++){
				if (TagName.equals("typeAlias")){
					 nodeI = temp.item(i).getAttributes().getNamedItem("alias");
					 nodeJ = temp.item(j).getAttributes().getNamedItem("alias");
				}else{
					 nodeI = temp.item(i).getAttributes().getNamedItem("id");
					 nodeJ = temp.item(j).getAttributes().getNamedItem("id");
				}
				
				if (nodeI!=null && nodeJ!=null && nodeI.getNodeValue().equals(
						nodeJ.getNodeValue())&& i!=j
				&& !nodeI.getNodeValue().equals("")
				){
					return true;
				}
			}
		}
		return false;		
	}
	/**
	 * sql map 트리에 중복 item을 빨간색으로 표시. 
	 */
	public void alertOnTreeItem(){
		Color alertColor = new Color(Display.getCurrent(),255,0,0); 
		Color generalColor = new Color(Display.getCurrent(),0,0,0); 
		for (int k=0;k<viewer.getTree().getItemCount();k++){
			TreeItem items = viewer.getTree().getItem(k);
			for(int i=0;i<items.getItemCount();i++)
				items.getItem(i).setForeground(generalColor);			
			for(int i=0;i<items.getItemCount();i++){
				for (int j=i;j<items.getItemCount();j++){
				
					if (items.getItem(i).getText().equals(items.getItem(j).getText())&& i!=j){
						items.getItem(i).setForeground(alertColor);
						items.getItem(j).setForeground(alertColor);
					}
				}
			}
		}
	}
	
	/**
	 * displayErrorMessage 중복에러메시지를 초기화함. 
	 * @param managedForm
	 * @param parent
	 */
	public void initErrorMessage(Form form) { 
		form.getToolBarManager().update(true);
		String message = null;
		message = form.getMessage();
		if (message!=null){
			message = message.replaceAll(DbioMessages.mapper_err_QueryId_duplication,"");
			message = message.replaceAll(DbioMessages.mapper_err_ParameterMapId_duplication,"");
			message = message.replaceAll(DbioMessages.mapper_err_ResultMapId_duplication,"");
			message = message.replaceAll(DbioMessages.mapper_err_QueryId_invalid,"");
			message = message.replaceAll(DbioMessages.mapper_err_ParameterMapId_invalid,"");
			message = message.replaceAll(DbioMessages.mapper_err_ResultMapId_invalid,"");
			message = message.replaceAll(DbioMessages.mapper_err_AliasName_invalid,"");
			message = message.replaceAll(DbioMessages.mapper_err_AliasName_duplication,"");
			if (message.length()==0)
				form.setMessage(message, IMessageProvider.NONE);
			else
				form.setMessage(message,IMessageProvider.ERROR);	// 기존에 에러메시지가 표시되어 있으면 삭제한다.  
		}
	}

	/**
	 * displayErrorMessage 에러메시지를 타이틀에 표시함. 
	 * @param managedForm
	 * @param parent
	 */
	public void displayErrorMessage(Form form, String message) { 
		form.getToolBarManager().update(true);
		
		if (form.getMessage()==null)
			form.setMessage(message,IMessageProvider.ERROR);	// NEW LINE
		else
			if (form.getMessage().indexOf(message)<0)
				form.setMessage(form.getMessage()+message,IMessageProvider.ERROR);	// 기존에 에러메시지가 표시되어 있지 않으면 표시한다. 
	}

	/**
	 * outline 페이지에서 select 하면 해당 mapper element object 에 대한 편집화면이 보이도록 하기 위함.   
	 * @param object 
	 */
	public void setTreeSelection(Object object){
		ISelection selection = new StructuredSelection(object); 
		viewer.setSelection(selection, true);
	}
	/**
	 * alias된 클래스명 가져옴.  
	 */
	public String getAliasClass(String aliasName){
		NodeList temp = element.getElementsByTagName("typeAlias");
		
		for (int i=0;i<temp.getLength();i++){
			if (temp.item(i).getAttributes().getNamedItem("alias").getNodeValue().equals(aliasName))
				return temp.item(i).getAttributes().getNamedItem("type").getNodeValue();
		}		
		return null;
	}
	/**
	 * get the masterPart's section
	 * @return
	 */
	public Section getSection(){
		return section;
	}

	/**
	 * detail page refresh 
	 */
	public void detailRefresh(){
		ISelection selection = viewer.getSelection();
		if (!selection.isEmpty()) {
			Object tmp = ((IStructuredSelection) selection).getFirstElement();
			Object tmp2 = viewer.getTree().getItems()[1];
			setTreeSelection(tmp2);
			setTreeSelection(tmp);
		}
	}
}
