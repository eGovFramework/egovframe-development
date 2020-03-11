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
package egovframework.dev.imp.dbio.editor.pages;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.editor.SqlMapEditor;
import egovframework.dev.imp.dbio.editor.parts.SqlMapMasterDetailsBlock;
import egovframework.dev.imp.dbio.util.JdtUtil;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * SqlMapPage 구성
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class SqlMapPage extends SimpleFormPage implements Listener {
	
	private SqlMapMasterDetailsBlock mdBlock;
	private final SqlMapEditor editor;
	private List<?> queryTestResult;
	private boolean voCreationStatus = false;
	
	public SqlMapPage(SqlMapEditor editor) {
		super(editor, SqlMapPage.class.getName(), "SQL Map");
		this.setMdBlock();
		this.editor = editor;
	}
	
	private void setMdBlock() {
		this.mdBlock = new SqlMapMasterDetailsBlock(this);
	}
	
	/**
	 * SqlMapPage 화면 생성
	 */
	@Override
	protected void createContents(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();

		Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
		mdBlock.createContent(managedForm);
	}

	/**
	 * VO 생성 시작을 알림.
	 */
	public void startVoCreation(){
		voCreationStatus = true;
	}
	
	/**
	 * VO 생성 종료를 알림
	 */
	public void endVoCreation() {
		voCreationStatus = false;
	}
	
	/**
	 * VO 생성상태를 반환
	 * @return VO 생성요청 여부
	 */
	public boolean getVoCreationStatus() {
		return voCreationStatus;
	}
	/**
	 * 작성된 Query 의 테스트결과를 가진다.(VO 객체 생성에 필요한 필드정보를 추출하기 위해 사용) 
	 * @param queryResult
	 */
	public void setQueryResult(List<?> queryResult) {
		queryTestResult = queryResult;
	}
	
	public void handleEvent(Event event) {
		// DbioLog.logInfo("sham");
	}
	
	/**
	 * 모델 객체 설정
	 * @param model
	 */
	public void setModel(IStructuredModel model) {
		mdBlock.setModel(model);
	}
	
//	private void refreshConnectionInfo() {
//		if (connInfo != null && !initialized) {
//			connInfo.init();
//			connInfo.refreshConnectionStatus();
//			initialized = true;
//		}
//	}
	
	@Override
	public void setActive(boolean active) {
		if (mdBlock != null) {
			mdBlock.setActive(active);
		}
		super.setActive(active);
	}
	
	public String openOrCreateNewJavaClass(String className) {
		
		//Vo 생성의 경우 테스트를 먼저 하도록 유도
		if(getVoCreationStatus() && queryTestResult == null) {
			MessageDialog.openInformation(this.getSite().getShell(),
					"Create VO", DbioMessages.sqlmap_info_doQueryTest);
			return null;
		}
		
		IFile file = getCurrentFile();
		if (file == null) return null;
		
		IJavaProject jproject = (IJavaProject) file.getProject().getAdapter(IJavaElement.class);
		if (jproject == null) return null;
//		if (className != null && !"".equals(className.trim())) {
//		//if (className != null && className.trim().length() > 0) {
//			try {
//				IType type = jproject.findType(className);
//				JavaUI.openInEditor(type);
//				return null;
//			} catch (CoreException e) {
//				DBIOPlugin.getDefault().getLog().log(e.getStatus());
//			}
//		}
		
		OpenNewClassWizardAction action = new OpenNewClassWizardAction();
		//VO생성의 경우는후처리 후 편집창에 소스를 띄운다.
		if(!getVoCreationStatus())action.setOpenEditorOnFinish(true);
		action.setSelection(new StructuredSelection(file));
		action.run();
		IJavaElement element = action.getCreatedElement();

		if (element instanceof IType) {
			//VO생성의 경우 필드와 메소드를 추가한 후 편집창에 띄운다.
			if (getVoCreationStatus()) {
				try{
					attachSourceBody((IType)element);
					JavaUI.openInEditor(element);
					queryTestResult = null;
				} catch (CoreException e) {
					DBIOPlugin.getDefault().getLog().log(e.getStatus());
				}
			}
			return ((IType) element).getFullyQualifiedName('$');
		} else {
			return null;
		}
	}
	
	/**
	 * 새로 생성된 VO객체에 Field 와 Method 를 생성한다.
	 * @param iType
	 */
	@SuppressWarnings("unused")
	private void attachSourceBody(IType iType) {
		Object obj = null;
		String name = null;
		String Name = null;
		String type = "String";
		
		String [] colNames = getColName(queryTestResult);
//		Map resultData = (queryTestResult.size() > 0) ? (Map)queryTestResult.get(0) : null;
//		String[] types = new String[colNames.length];
		
//		for(int i=0; i < colNames.length; i++) {
//			obj = resultData.get(colNames[i]);
//			
//			if(obj instanceof Integer) types[i] = "int";
//			else if(obj instanceof BigDecimal) types[i] = "BigDecimal";
//			else if(obj instanceof Date) types[i] = "Date";
//			else types[i] = "String";
//		}
		
		try {
			//필드 생성
			for(int i=0; i < colNames.length; i++) { 
				name = StringUtil.convertCamelNotation(colNames[i]);
				colNames[i] = name;

				iType.createField(new StringBuffer("private ").append(type).
						append(" ").append(name).append(";").toString(), null, false, null);
			}
			
			//메소드 생성
			for(int i=0; i < colNames.length; i++) {
				name = colNames[i];
				Name = StringUtil.toUpperCaseForFirstChar(name);
				
				//iType.createField("\n", null, false, null);
				iType.createMethod(new StringBuffer("public void set").append(Name).append("(")
						.append(type).append(" ").append(name).append(") {\n\tthis.")
						.append(name).append(" = ").append(name).append("; \n}").toString(), null, false, null);

				//iType.createField("\n", null, false, null);
				iType.createMethod(new StringBuffer("public ").append(type)
						.append(" get").append(Name).append("() {\n\treturn ").append(name)
						.append("; \n}").toString(), null, false, null);
			}
		} catch(Exception e) {
			MessageDialog.openInformation(this.getSite().getShell(),
					"New Class", e.getMessage());
		}
	}
	
	private String[] getColName(List<?> queryResult) {
		String[] colNames = new String[0];
		Iterator<?> itr = queryResult.iterator();
		
		if (itr.hasNext()) {
			Map<?, ?> row = (Map<?, ?>) itr.next();
			Object [] rs = row.keySet().toArray();
			colNames = new String[rs.length];
			
			for (int i =0 ; i<rs.length; i++) {
				colNames[i] = (String)rs[i];
			}
		}
		return colNames;
	}

	
	public String selectClass() {
		IFile file = getCurrentFile();
		if (file == null) return null;
		
		IJavaElement jelement = (IJavaElement) file.getProject().getAdapter(IJavaElement.class);
		if (jelement == null) return null;
		
		try {
			return JdtUtil.selectClass(file.getProject(), "");
		} catch (CoreException e) {
			DBIOPlugin.getDefault().getLog().log(e.getStatus());
			return null;
		}
	}
	
	protected IFile getCurrentFile() {
		return ResourceUtil.getFile(getEditorInput());
	}

  	/**
	 * MasterDetailsBlock 객체를 리턴함. 
	 */
	public SqlMapMasterDetailsBlock getMdBlock() {
		return this.mdBlock;
	}
  	/**
	 * SqlMapEditor 객체를 리턴함 
	 */
	public SqlMapEditor getEditor() {
		return this.editor;
	}

	/**
	 * class 에 대한 field 값 가져옴
	 * @param className
	 * @return
	 */
	public String[] getSelectedClassProperty(String className) {
		String fullyQualifiedName = null;
		IFile file = getCurrentFile();
		if (file == null) return null;

		if (getMdBlock().getMasterPart().isInitialTypes(className)){
			fullyQualifiedName = getMdBlock().getMasterPart().getAliasClass(className);
			if (fullyQualifiedName==null)
				return null;
			else
				return getSelectedClassProperty(fullyQualifiedName);
		}else{
			fullyQualifiedName = className;
		}
			
		IJavaProject jproject = (IJavaProject) file.getProject().getAdapter(IJavaElement.class);
		if (jproject == null) return null;
		try {
			 IType tmp = jproject.findType(fullyQualifiedName);
			 IJavaElement temp = (IJavaElement)tmp;
			 return JdtUtil.getProperty(temp);
//			return JdtUtil.getSelectedClassProperty(file.getProject(), fullyQualifiedName);
		} catch (Exception e) {
			return null;
		}
	}
}
