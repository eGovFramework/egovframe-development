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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.components.TableComboBoxCellEditor;
import egovframework.dev.imp.dbio.components.TableTextCellEditor;
import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboButtonField;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.util.StringUtil;


/**
 * MapperConfiguration 에디터의 TransactionManagerSection
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
public class MapperConfigurationTypeAliasesSection extends SectionFactory {

	private ComboField typeCombo;
	private ComboField typeAliasTypeCombo;
	private TableViewer typeAliasViewer;
	//private TableComboBoxCellEditor typeAliasNameEditor;
	private TableTextCellEditor typeAliasNameEditor;
	private Element root;
	private Element tmElement;
	private Element typeAliasElement;
	private Button addTypeAliasButton;
	private Button removeTypeAliasButton;
	private Button clearTypeAliasButton;
	private Form form;
	
	private static final String[] JDBC_SIMPLE_PROPERTIES = new String[] {
		"JDBC.Driver", "JDBC.ConnectionURL", "JDBC.Usename", "JDBC.Password"
	};
	private static final String[] JDBC_SIMPLE_COMBO_PROPERTIES = new String[] {
		"JDBC.Driver", "JDBC.ConnectionURL", "JDBC.Usename", "JDBC.Password"
		,"JDBC.DefaultAutoCommit"
		,"Pool.MaximumActiveConnections"
		,"Pool.MaximumIdleConnections"
		,"Pool.MaximumCheckoutTime"
		,"Pool.TimeToWait"
		,"Pool.PingQuery"
		,"Pool.PingEnabled"
		,"Pool.PingConnectionsOlderThan"
		,"Pool.PingConnectionsNotUsedFor"
	};
	private static final String[] JDBC_JNDI_PROPERTIES = new String[] {
		"DataSource"
	};
	private static final String[] JTA_JNDI_PROPERTIES = new String[] {
		"DataSource", "UserTransaction"
	};

	public MapperConfigurationTypeAliasesSection() {
//		super("Transaction Manager", Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		super("TypeAlias", Section.TITLE_BAR | Section.TWISTIE);
	}

	/**
	 * TransactionManagerSection 화면 구성
	 */
	@Override
	protected void createContents(Composite parent, FormToolkit toolkit) {
		
		parent.setLayout(new GridLayout(3, false));
		/*
		typeCombo = new ComboField("Type:", SWT.READ_ONLY);
		typeCombo.create(toolkit, parent, 3);
		typeCombo.setItems(new String[] {"JDBC", "JTA"});
		typeCombo.addFieldListener(new IFieldListener() {
			public void eventOccured(FieldEvent event) {
				if (event.getType() == FieldEvent.Type.TextChanged) {
					typeChanged();
				}
			}
		});
		typeAliasTypeCombo = new ComboField("DataSource Type:", SWT.READ_ONLY);
		typeAliasTypeCombo.create(toolkit, parent, 3);
		typeAliasTypeCombo.addFieldListener(new IFieldListener() {
			public void eventOccured(FieldEvent event) {
				if (event.getType() == FieldEvent.Type.TextChanged) {
					typeAliasTypeChanged();
				}
			}
		});
		*/
		/*
		final Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		toolkit.adapt(label, true, true);
		label.setText("TypeAliases:");
		*/		
		Table table = toolkit.createTable(parent, SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.heightHint = 103;
		table.setLayoutData(gd);
		toolkit.adapt(table, true, true);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//TableLayout tl = new TableLayout();
		//table.setLayout(tl);
		
		//tl.addColumnData(new ColumnWeightData(150, true));
		TableColumn column;
		
		column = new TableColumn(table, SWT.LEFT, 0);
		column.setText("");
		column.setWidth(30);
		
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Alias");
		column.setWidth(200);
		
		//tl.addColumnData(new ColumnWeightData(150, true));
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Type");
		column.setWidth(500);
		
		typeAliasViewer = new TableViewer(table);
		typeAliasViewer.setColumnProperties(new String[] {"", "alias", "type"});
		typeAliasViewer.setContentProvider(new ArrayContentProvider());
		typeAliasViewer.setLabelProvider(new TypeAliasLabelProvider(typeAliasViewer));
		//typeAliasNameEditor = new TableComboBoxCellEditor(typeAliasViewer, 1);
		typeAliasNameEditor = new TableTextCellEditor(typeAliasViewer, 1);
		typeAliasViewer.setCellEditors(new CellEditor[] {
				null,
				typeAliasNameEditor,
				new TableTextCellEditor(typeAliasViewer, 2)
		});	
		typeAliasViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String typeAlias) {
				return (element instanceof Element) && "typeAlias".equals(((Element) element).getTagName());
			}

			public Object getValue(Object element, String typeAlias) {
				if (element instanceof Element) {
					return StringUtil.nvl(((Element) element).getAttribute(typeAlias));
				} else {
					return null;
				}
			}

			public void modify(Object element, String typeAlias, Object value) {
				if (element instanceof Element) {
					((Element) element).setAttribute(typeAlias, (String) value);
					typeAliasViewer.refresh(element, true);
					chkValidation();
				}
			}
		});
		typeAliasViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//updateWidgetStatus();
			}
		});

		//버튼 컴포지트
		Composite buttonComposite = toolkit.createComposite(parent, SWT.NONE);
		gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		buttonComposite.setLayoutData(gd);		
		GridLayout blayout = new GridLayout();
		blayout.marginHeight = 0;
		blayout.horizontalSpacing = 0;
		blayout.marginWidth = 0;		
		buttonComposite.setLayout(blayout);
		toolkit.adapt(buttonComposite);
		
		addTypeAliasButton = toolkit.createButton(buttonComposite, "Add", SWT.NONE);
		addTypeAliasButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewTypeAlias();
				chkValidation();				
			}
		});
		removeTypeAliasButton = toolkit.createButton(buttonComposite, "Remove", SWT.NONE);
		removeTypeAliasButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeTypeAliases();
				chkValidation();				
			}
		});
		
		clearTypeAliasButton = toolkit.createButton(buttonComposite, "Init", SWT.NONE);
		clearTypeAliasButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearTypeAliases();
				initTypeAliases();
				chkValidation();				
			}
		});
		
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		addTypeAliasButton.setLayoutData(gd);
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		removeTypeAliasButton.setLayoutData(gd);
		
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		clearTypeAliasButton.setLayoutData(gd);
	}
	
	protected void addNewTypeAlias() {
		Element element =  getTypeAliasElement();
		Element newTypeAlias = element.getOwnerDocument().createElement("typeAlias");
		newTypeAlias.setAttribute("alias", "");
		newTypeAlias.setAttribute("type", "");
		element.appendChild(newTypeAlias);
		typeAliasViewer.setInput(getTypeAliases());
		typeAliasViewer.setSelection(new StructuredSelection(newTypeAlias));
	}
	protected void addNewTypeAlias(String name) {
		Element element =  getTypeAliasElement();
		Element newTypeAlias = element.getOwnerDocument().createElement("typeAlias");
		newTypeAlias.setAttribute("alias", name);
		newTypeAlias.setAttribute("type", "");
		element.appendChild(newTypeAlias);
		typeAliasViewer.setInput(getTypeAliases());
		typeAliasViewer.setSelection(new StructuredSelection(newTypeAlias));
	}	
	
	protected void removeTypeAliases() {
		IStructuredSelection selection = (IStructuredSelection) typeAliasViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.getParentNode().removeChild(element);
		}
		typeAliasViewer.setInput(getTypeAliases());
	}
	
	protected void clearTypeAliases() {
		
		Iterator<?> iterator = getTypeAliases().iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.getParentNode().removeChild(element);
		}
		typeAliasViewer.setInput(getTypeAliases());
	}
	
	protected void typeAliasTypeChanged() {
		getTypeAliasElement().setAttribute("type", typeAliasTypeCombo.getText());
		updateWidgetStatus();
		
		initTypeAliases();
	}
	
	private void initTypeAliases(){
		//속성 초기화
		if (typeAliasViewer.getSelection().isEmpty()){
			String type = typeCombo.getText();
			String typeAliasType = typeAliasTypeCombo.getText();
			if ((!"".equals(StringUtil.nvl(type)))
					&& (!"".equals(StringUtil.nvl(typeAliasType)))
					) {
				String[] typeAliases = new String[0];
				if ("JDBC".equals(type)) {
					if ("SIMPLE".equals(typeAliasType)){
						typeAliases = JDBC_SIMPLE_PROPERTIES;
					}
					else if ("JNDI".equals(typeAliasType)){
						typeAliases = JDBC_JNDI_PROPERTIES;
					}
				}
				else if ("JTA".equals(type)) {
					if ("JNDI".equals(typeAliasType)){
						typeAliases = JTA_JNDI_PROPERTIES;
					}
				}
				
				for (int i = 0; i < typeAliases.length; i++){
					addNewTypeAlias(typeAliases[i]);
				}
			}
			
		}
	}

	protected void typeChanged() {
		if (getTypeAliasElement() == null) createTypeAliasElement();
		//getTmElement().setAttribute("type", typeCombo.getText());
		updateWidgetStatus();
	}
	
	private void updateWidgetStatus() {
		/*
		String type = typeCombo.getText();
		String old = typeAliasTypeCombo.getText();
		if ("JDBC".equals(type)) {
			typeAliasTypeCombo.setItems(new String[] {"SIMPLE", "JNDI"});
			typeAliasTypeCombo.setText(old);
			typeAliasTypeCombo.setEnabled(true);
		} else if ("JTA".equals(type)) {
			typeAliasTypeCombo.setItems(new String[] {"JNDI"});
			typeAliasTypeCombo.setText(old);
			typeAliasTypeCombo.setEnabled(true);
		} else {
			typeAliasTypeCombo.setItems(new String[0]);
			typeAliasTypeCombo.setEnabled(false);
		}
		*/
		/*
		String dsType = typeAliasTypeCombo.getText();
		if ("SIMPLE".equals(dsType)) {
			typeAliasNameEditor.setItems(JDBC_SIMPLE_COMBO_PROPERTIES);
		} else if ("JNDI".equals(dsType)) {
			if ("JTA".equals(type)) {
				typeAliasNameEditor.setItems(JTA_JNDI_PROPERTIES);
			} else {
				typeAliasNameEditor.setItems(JDBC_JNDI_PROPERTIES);
			}
		} else {
			typeAliasNameEditor.setItems(new String[0]);
		}
		*/
		addTypeAliasButton.setEnabled(true); //addTypeAliasButton.setEnabled(typeAliasElement != null);
		removeTypeAliasButton.setEnabled(true); //removeTypeAliasButton.setEnabled(!typeAliasViewer.getSelection().isEmpty());
	}
	
	private Element getTypeAliasElement() {
		if (typeAliasElement == null) {
			Element element = root; //getTmElement();
			typeAliasElement = element.getOwnerDocument().createElement("typeAliases"); //$NON-NLS-1$
			/*
			element.appendChild(typeAliasElement);
			element.appendChild(element.getOwnerDocument().createTextNode("\n"));
			*/
			Element sibling = null;

			NodeList children = element.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& ( "typeHandlers".equals(((Element) child).getTagName())
						     || "mappers".equals(((Element) child).getTagName()) )
					) { //$NON-NLS-1$
					sibling = (Element) child;
					break;
				}
			}
			element.insertBefore(typeAliasElement, sibling);
			element.insertBefore(element.getOwnerDocument().createTextNode("\n"), sibling);
		}
		return typeAliasElement;
	}

	private Element getTmElement() {
//		if (tmElement == null) {
//			tmElement = root.getOwnerDocument().createElement("transactionManager"); //$NON-NLS-1$
//			Element sibling = null;
//			
//			NodeList children = root.getChildNodes();
//			for (int i = 0; i < children.getLength(); i++) {
//				Node child = children.item(i);
//				if (child instanceof Element
//						&& "mapper".equals(((Element) child).getTagName())) { //$NON-NLS-1$
//					sibling = (Element) child;
//					break;
//				}
//			}
//			root.insertBefore(tmElement, sibling);
//			root.insertBefore(root.getOwnerDocument().createTextNode("\n"), sibling);
//		}
		return tmElement;
	}

	private void createTypeAliasElement() {
		if (typeAliasElement == null) {
			typeAliasElement = root.getOwnerDocument().createElement("typeAliases"); //$NON-NLS-1$
			Element sibling = null;

			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& ( "typeHandlers".equals(((Element) child).getTagName()) 
							  || "mappers".equals(((Element) child).getTagName()) )
					) { //$NON-NLS-1$
					sibling = (Element) child;
					break;
				}
			}
			root.insertBefore(typeAliasElement, sibling);
			root.insertBefore(root.getOwnerDocument().createTextNode("\n"), sibling);
		}
	}
	
	private void createTmElement() {
		if (tmElement == null) {
			tmElement = root.getOwnerDocument().createElement("typeAliases"); //$NON-NLS-1$
			Element sibling = null;

			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "mappers".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					sibling = (Element) child;
					break;
				}
			}
			root.insertBefore(tmElement, sibling);
			root.insertBefore(root.getOwnerDocument().createTextNode("\n"), sibling);
		}
	}
	private List<Element> getTypeAliases() {
		List<Element> ret = new LinkedList<Element>();
		if (typeAliasElement != null) {
			NodeList children = typeAliasElement.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "typeAlias".equals(((Element) child).getTagName())) {
					ret.add((Element) child);
				}
			}
		}
		return ret;
	}
	
	public void setElement(Element root) {
		this.root = root;
		this.tmElement = null;
		this.typeAliasElement = null;
		/*
		if (root != null) {
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "typeAliases".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					this.tmElement = (Element) child;
				}
			}
		}
		*/
		if (root != null) {
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element
						&& "typeAliases".equals(((Element) child).getTagName())) { //$NON-NLS-1$
					this.typeAliasElement = (Element) child;
				}
			}
		}
		setInitialData();
	}

	private void setInitialData() {
		/*
		if (tmElement != null) {
			typeCombo.setText(StringUtil.nvl(tmElement.getAttribute("type"))); //$NON-NLS-1$
		} else {
			typeCombo.setText("");//$NON-NLS-1$
		}
		*/
		/*
		if (typeAliasElement != null) {
			typeAliasTypeCombo.setText(StringUtil.nvl(typeAliasElement.getAttribute("type"))); //$NON-NLS-1$
		} else {
			typeAliasTypeCombo.setText(""); //$NON-NLS-1$
		}
		*/
		typeAliasViewer.setInput(getTypeAliases());
		updateWidgetStatus();
		chkValidation();
	}

	private static class TypeAliasLabelProvider extends LabelProvider
	implements ITableLabelProvider {

		private TableViewer viewer;

		public TypeAliasLabelProvider(
				TableViewer viewer) {
			//this.viewer = viewer;
			this.setViewer(viewer);
		}
		
		/**
		 * viewer 설정
		 * 
		 * @param viewer
		 */
		private void setViewer(TableViewer viewer) {
			this.viewer = viewer;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0){
				return DBIOPlugin.getDefault().getImageDescriptor(DBIOPlugin.IMG_PROPERTY).createImage();
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Element
					&& "typeAlias".equals(((Element) element).getTagName())) { //$NON-NLS-1$
				return ((Element) element).getAttribute((String) viewer.getColumnProperties()[columnIndex]);
			}
			return null;
		}

	}

	/**
	 * 화면 구성
	 * 
	 * @param managedForm
	 * @param parent
	 * @return
	 */
	public Section createContents(IManagedForm managedForm, Composite parent) {

		Form form = managedForm.getForm().getForm();
		this.form = form;
		initErrorMessage(form);
		return super.createContents(managedForm,parent);
	}
	
	/**
	 * TmElement 에 대한 validation 체크 
	 * @param element
	 * @return
	 */	
	public void chkValidation(){ 
		initErrorMessage(form);
		if (getTypeAliasElement() != null && getTypeAliasElement().hasChildNodes()){
			if (chkTypeAliasNameValidation( getTypeAliasElement(),"typeAlias"))
				displayErrorMessage(form,DbioMessages.mapperconfig_err_TypeAliasAlias_invalid);
			if (chkTypeAliasValueEmpty(getTypeAliasElement(),"typeAlias"))
				displayErrorMessage(form,DbioMessages.mapperconfig_err_TypeAliasType_empty);
			if (chkTypeAliasDuplicate(getTypeAliasElement(),"typeAlias"))
				displayErrorMessage(form,DbioMessages.mapperconfig_err_TypeAliasAlias_duplication);
		}
	}

	/**
	 * element tag 에 대한 name validation 체크.. 현재는 공란만 체크함.   
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkTypeAliasNameValidation(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			if (temp.item(i).getAttributes().getNamedItem("alias").getNodeValue().equals("")){
				return true;
			}
		}
		return false;		
	}
	/**
	 * typeAlias Value Empty 체크
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkTypeAliasValueEmpty(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			if (temp.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("")){
				return true;
			}
		}
		return false;		
	}	
	
	/**
	 * typeAlias 에 대한 name 중복 체크함 
	 * @param element
	 * @param TagName
	 * @return
	 */
	public boolean chkTypeAliasDuplicate(Element element, String TagName){ 
		NodeList temp = element.getElementsByTagName(TagName);
		for (int i=0;i<temp.getLength();i++){
			for (int j=i;j<temp.getLength();j++){
				if (temp.item(i).getAttributes().getNamedItem("alias").getNodeValue().equals(
				temp.item(j).getAttributes().getNamedItem("alias").getNodeValue())&& i!=j
				&& !temp.item(i).getAttributes().getNamedItem("alias").getNodeValue().equals("")
				){
					return true;
				}
			}
		}
		return false;		
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
			message = message.replaceAll(DbioMessages.mapperconfig_err_TypeAliasAlias_duplication,"");
			message = message.replaceAll(DbioMessages.mapperconfig_err_TypeAliasAlias_invalid,"");
			message = message.replaceAll(DbioMessages.mapperconfig_err_TypeAliasType_empty,"");
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

}
