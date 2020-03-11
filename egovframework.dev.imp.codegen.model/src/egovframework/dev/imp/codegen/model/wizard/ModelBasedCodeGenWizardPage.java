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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.FolderSelectionDialog;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.uml2.uml.DataType;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.util.ModelConvertUtil;

/**
 * 모델 기반 코드젠 위저드 페이지 클래스 
 * <p><b>NOTE:</b> 변환할 클래스 항목과 소스를 생성할 위치를 입력받음.  
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
@SuppressWarnings("restriction")
public class ModelBasedCodeGenWizardPage extends WizardPage {

	/** 
	 * 생성자 
	 * 
	 * @param pageName
	 * 
	 */
	protected ModelBasedCodeGenWizardPage(String pageName) {
		super(pageName);
	}

	/** 프로젝트 인스턴스 */
	private IJavaProject project;
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


	/**
	 * 생성자 
	 * 
	 * 
	 */
	public ModelBasedCodeGenWizardPage() { // IJavaProject project, Map classes
		super("Output Folder");
		setTitle(EgovModelCodeGenPlugin.getDefault().getResourceString(
				"wizard.modelcodegen.dialog.title"));
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
				if (items[i].getText(1).equals("")
						|| items[i].getText(1).equals("(default)"))
					selection.add(items[i].getText());
				else
					selection.add(items[i].getText(1) + "."
							+ items[i].getText());
			} else {
				if (items[i].getText(1).equals("(default)"))
					unchecked.add(items[i].getText());
				else
					unchecked.add(items[i].getText(1) + "."
							+ items[i].getText());
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

	/**
	 * 스테레오타입 지원 여부 가져오기
	 * 
	 * @param obj
	 * @return
	 * 
	 */
	private boolean getStereotypeCodeGenAble(Object obj) {
		String applicableStereotype = EgovModelCodeGenPlugin.getDefault().getResourceString("stereotype.support.template");
		String stereotype = getStereotype(obj);
		if (!stereotype.equals("") && applicableStereotype.indexOf(stereotype)>=0)
			return true;
		return false;
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
	public void createControl(Composite parent) {
		EgovModelCodeGenPlugin plugin = EgovModelCodeGenPlugin.getDefault();
		// getShell().setText(plugin.getResourceString("generate.dialog.title"));

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(composite, SWT.NULL);
		label.setText(plugin
				.getResourceString("wizard.modelcodegen.dialog.srcdir"));
		targetDir = new Text(composite, SWT.BORDER);
		targetDir.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		targetDir.setEditable(false);
		Color color = new Color(null,255,255,255);
		targetDir.setBackground(color);
		targetDir.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updatePageComplete();

			}
		});

		Button button = new Button(composite, SWT.PUSH);
		button.setText("...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectFolder();
			}
		});

		label = new Label(composite, SWT.NULL);
		label.setText(plugin
				.getResourceString("wizard.modelcodegen.dialog.types"));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		list = new Table(composite, SWT.V_SCROLL | SWT.BORDER | SWT.CHECK);
		list.setHeaderVisible(true);
		list.setLinesVisible(true);
		TableColumn column1 = new TableColumn(list, SWT.NULL);
		column1.setText("Class");
		column1.setWidth(180);
		// column1.pack();
		TableColumn column2 = new TableColumn(list, SWT.NULL);
		column2.setText("Package");
		column2.setWidth(120);
		// column2.pack();

		TableColumn column3 = new TableColumn(list, SWT.NULL);
		column3.setText("Stereotype");
		column3.setWidth(100);
		// column3.pack();

		TableColumn column4 = new TableColumn(list, SWT.NULL);
		column4.setText("Stereotype Support");
		column4.setWidth(150);
		// column4.pack();

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		list.setLayoutData(gd);
		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		
		Button selectButton = new Button(composite, SWT.PUSH);
		selectButton.setText("Select All");
		selectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectAll();
			}
		});
		selectButton.setLayoutData(gd);
		Button deselectButton = new Button(composite, SWT.PUSH);
		deselectButton.setText("Deselect All");
		deselectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				deselectAll();
			}
		});
		deselectButton.setLayoutData(gd);
		setModelList();

		setControl(composite);
		updatePageComplete();
	}

	/**
	 * 전체 선택해제하기
	 * 
	 * 
	 */
	protected void deselectAll() {
		for (int i=0;i<list.getItemCount();i++){
			list.getItem(i).setChecked(false);
		}
		
	}

	/**
	 * 전체 선택하기 
	 * 
	 * 
	 */
	protected void selectAll() {
		for (int i=0;i<list.getItemCount();i++){
			list.getItem(i).setChecked(true);
		}
	}

	/**
	 * 
	 * 클래스 목록 세팅하기 
	 * 
	 * 
	 */
	protected void setModelList() {
		list.removeAll();
		if (classes != null) {
			String key = null;
			Object clazz = null;
			String className = null;
			TableItem item = null;
			String[] str = null;
			for (Iterator<?> iter = classes.keySet().iterator(); iter.hasNext();) {
				key = (String) iter.next();
				clazz = classes.get(key);
				if (!(clazz instanceof DataType)) {
					className = key.substring(key.lastIndexOf(".") + 1);
					item = new TableItem(list, SWT.NULL);
					str = new String[] { className, ModelConvertUtil.getPackage(clazz), getStereotype(clazz).equals("") ? "" : getStereotype(clazz),
							getStereotype(clazz).equals("") ? "" : getStereotypeCodeGenAble(clazz) ? "supported" : "not supported" };
					item.setText(str);
					if (clazz instanceof DataType)
						unchecked.add(str);
					if (!unchecked.contains(str)) {
						item.setChecked(true);
					}
				}
			}
		} else {
			setMessage(null);
			setErrorMessage("Class Model Not Found");
			setPageComplete(false);
		}
	}

	/**
	 * 페이지 완료 여부 업데이트하기
	 * 
	 * 
	 */
	protected void updatePageComplete() {
		setPageComplete(false);
		
		IPath targetLoc = getTargetLocation();
		if (targetLoc == null) {
			setMessage(null);
			setErrorMessage("Invalid Output Folder");
			setPageComplete(false);
			return;
		}
		IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
		this.project = JavaCore.create(wsroot.getProject(targetLoc.segment(0)));
		if (project == null || !project.exists()) {
			// current project is not a Java project
			setMessage(null);
			setErrorMessage("Invalid Project");
			setPageComplete(false);
			return;			
		}
		setMessage(null);
		setErrorMessage(null);
		if (list.getItemCount()>0){
			setPageComplete(true);
		}else{
			setErrorMessage("Class Model Not Found");
		}
	}

	/**
	 * 타겟 폴더 가져오기
	 * 
	 * @return
	 * 
	 */
	public IPath getTargetLocation() {
		String text = targetDir.getText().trim();
		if (text.length() == 0)
			return null;
		IPath path = new Path(text);
		if (!path.isAbsolute())
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(path);
		return path;
	}

	/**
     * 타겟 폴더 선택하기
     * 
     */
	@SuppressWarnings("rawtypes")
	private void selectFolder() {
		try {
			Class[] acceptedClasses = new Class[] { IProject.class, IFolder.class };
			ISelectionStatusValidator validator = new TypedElementSelectionValidator(acceptedClasses, false);

			IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();

			ViewerFilter filter = new TypedViewerFilter(acceptedClasses); // ,
																			// rejectedElements.toArray());

			FolderSelectionDialog dialog = new FolderSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());

			dialog.setTitle("Select output folder");
			dialog.setMessage("Select output folder:");

			dialog.setInput(wsroot);
			dialog.setValidator(validator);
			dialog.addFilter(filter);
			if (dialog.open() == Window.OK) {
				targetDir.setText(getFolderName(dialog.getFirstResult()));
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 폴더명 가져오기
	 * 
	 * @param result
	 * @return
	 * @throws CoreException
	 * 
	 */
	private String getFolderName(Object result) throws CoreException {
		if (result instanceof IFolder) {
			IFolder folder = (IFolder) result;
			return folder.getFullPath().toString();
		}
		return "";
	}

	/**
	 * 타겟 프로젝트 세팅하기
	 * 
	 * @param project2
	 * 
	 */
	public void setProject(IJavaProject project2) {
		this.project = project2;

	}

	/**
	 * 타겟 프로젝트 가져오기
	 * 
	 * @return
	 * 
	 */
	public IJavaProject getProject() {
		return this.project;
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

}
