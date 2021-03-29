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
package egovframework.dev.imp.codegen.model.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.DataType;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.util.ModelConvertUtil;

/**
 *  XMI 파일을 IMPORT 하는 위저드 페이지 클래스 
 * <p><b>NOTE:</b> IMPORT한 XMI파일의 클래스 정보 Validation 체크. 
 * @author 개발환경 개발팀 윤수열
 * @since 2010.06.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.06.10  김연수          최초 생성
 *
 * </pre>
 */
public class UML2ModelBasedCodeGenWizardPage extends WizardPage {

    /** 로거 */
    protected final Logger log = Logger.getLogger(UML2ModelBasedCodeGenWizardPage.class);
    
	/** 
	 * 생성자 
	 * 
	 * @param pageName
	 * 
	 */
	protected UML2ModelBasedCodeGenWizardPage(String pageName) {
		super(pageName);
	}
	
	/** 트리뷰어 */
	private CheckboxTreeViewer viewer;

	/** 프로젝트 인스턴스 */
//	private IJavaProject project;
	/** 클래스 목록 */
	private TreeMap<?, ?> classes;
	/** 테이블 인스턴스 */
	private Table list;
	/** 대상 폴더 */
	private Text targetDir;
	/** 스테레오타입 클래스 목록 */
	private Map<?, ?> stereotypeClassList;

	/** 비선택항목 인스턴스 */
	private static Set<Object> unchecked = new HashSet<Object>();
	
	/** 파일 */
	private IFile file;
	/**
	 * 생성자 
	 * 
	 * 
	 */
	public UML2ModelBasedCodeGenWizardPage() { // IJavaProject project, Map classes
		super("Output Folder");
		setTitle(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.modelcodegen.dialog.title"));
	}

	/**
	 *  대상 폴더 가져옴. 
	 * 
	 * @return 대상폴더명 
	 * 
	 */
	public String getOutputFolder() {
		return targetDir.getText();
	}
	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}
	
	/**
	 * 클래스 목록 가져오기
	 * 
	 * @return
	 * 
	 */
	public String[] getGenerateClasses() {
		ArrayList<String> selection = new ArrayList<String>();
		TableItem[] items = list.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked()) {
				if (items[i].getText(1).equals("") || items[i].getText(1).equals("(default)"))
					selection.add(items[i].getText());
				else
					selection.add(items[i].getText());
				//selection.add(items[i].getText(1) + "."
				//	+ items[i].getText());
			} else {
				if (items[i].getText(1).equals("(default)"))
					unchecked.add(items[i].getText());
				else
					unchecked.add(items[i].getText(1) + "." + items[i].getText());
			}
		}
		return (String[]) selection.toArray(new String[selection.size()]);
	}
	public String[] getGenerateObjects() {
		ArrayList<String> selection = new ArrayList<String>();
		TableItem[] items = list.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked()) {
				if (items[i].getText(1).equals("")
						|| items[i].getText(1).equals("(default)"))
					selection.add(items[i].getText());
				else
					selection.add(items[i].getText());
					//selection.add(items[i].getText(1) + "."
						//	+ items[i].getText());
			} else {
				if (items[i].getText(1).equals("(default)"))
					unchecked.add(items[i].getText());
				else
					unchecked.add(items[i].getText(1) + "."	+ items[i].getText());
			}
		}
		return (String[]) selection.toArray(new String[selection.size()]);
	}
	/**
	 * 스테레오 타입 가져오기
	 * 
	 * @param obj
	 * @return
	 * 
	 */
	private String getStereotype(Object obj) {
		if (stereotypeClassList == null || stereotypeClassList.get(obj) == null)
			return "";
		else
			return (String) stereotypeClassList.get(obj);
	}

	/* 
	 * 페이지가 화면으로 보여질 때 클래스 모델 목록 갱신하기
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 * 
	 */
	public void setVisible(boolean visible) {
		setModelList();
		super.setVisible(visible);
	}

	/*
	 * 페이지 화면 구성 
	 * 
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 * 
	 */
	@SuppressWarnings("unused")
	public void createControl(Composite parent) {
		
		// 김연수 수정

		EgovModelCodeGenPlugin plugin = EgovModelCodeGenPlugin.getDefault();
		
		Composite composite = new Composite(parent, SWT.NULL);
		
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		gd.horizontalSpan = 3;

		list = new Table(composite, SWT.V_SCROLL | SWT.BORDER );

		list.setHeaderVisible(true);
		list.setLinesVisible(true);
		TableColumn column1 = new TableColumn(list, SWT.NULL);
		column1.setText("Class");
		column1.setWidth(220);
		// column1.pack();
		TableColumn column2 = new TableColumn(list, SWT.NULL);
		column2.setText("Package");
		column2.setWidth(160);
		// column2.pack();

		TableColumn column3 = new TableColumn(list, SWT.NULL);
		column3.setText("Stereotype");
		column3.setWidth(120);
		// column3.pack();

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		list.setLayoutData(gd);
		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);

		setModelList();

		setControl(composite);
		setPageComplete(false);
		
	}

	/**
	 * 
	 * 클래스 목록 세팅하기 
	 * 
	 * 
	 */
	protected void setModelList() {
		list.removeAll();

		if (classes != null){

			String key = null;
			Object clazz = null;
			String className = null;
			TableItem item =null;
			String[] str = null;
			
			Iterator<?> iter2 = classes.keySet().iterator();
			if(!iter2.hasNext()){
				setMessage(null);
				setErrorMessage("Class Model Not Found");
				setPageComplete(false);
			}else{
				for (Iterator<?> iter = classes.keySet().iterator(); iter.hasNext();) {
					key = (String) iter.next();

					clazz = classes.get(key);
					if (!(clazz instanceof DataType)){
						className = key.substring(key.lastIndexOf(".") + 1);

						item = new TableItem(list, SWT.NULL);
						str = new String[] {
								className,
								ModelConvertUtil.getPackage(clazz),
								getStereotype(clazz).equals("") ? "" : getStereotype(clazz)};
						item.setText(str);
					}
				}
				setErrorMessage(null);
				setPageComplete(true);
			}
				
			
		}else{
			setMessage(null);
			setErrorMessage("Class Model Not Found");
			setPageComplete(false);
		}
	}

	/**
	 * 스테레오타입 클래스 목록 가져오기
	 * 
	 * @return
	 * 
	 */
	public Map<?, ?> getStereotypeClassList() {
		return this.stereotypeClassList;
	}
	/**
	 * 스테레오타입 클래스 목록 가져오기
	 * 
	 * @return
	 * 
	 */
	public Map<?, ?> extensionElement() {
		return this.extensionElement();
	}
	/**
	 * 타겟 객체 목록 세팅하기
	 * 
	 * @param target
	 * 
	 */
	public void setTarget(Map<?, ?> target) {
		this.classes = (TreeMap<?, ?>) target;

	}

	/**
	 * 스테레오 타입 클래스 목록 세팅하기
	 *  
	 * @param stereotypeClassList
	 * 
	 */
	public void setStereotypeClassList(Map<?, ?> stereotypeClassList) {
		this.stereotypeClassList = stereotypeClassList;
	}
	
	/**
	 * 클래스 목록 세팅하기
	 *  
	 *
	 * 
	 */
	public Object getSelectedModel() {
		//list.getData();
		return viewer.getCheckedElements();
	}
}
