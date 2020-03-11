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

import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Model;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.converter.EclipseModelConverter;
import egovframework.dev.imp.codegen.model.converter.EgovModelConverter;
import egovframework.dev.imp.codegen.model.converter.IConverter;
import egovframework.dev.imp.codegen.model.converter.ModelListMaker;
import egovframework.dev.imp.codegen.model.converter.NonEclipseModelConverter;
import egovframework.dev.imp.codegen.model.util.XMIUtil;
import egovframework.dev.imp.codegen.model.validator.XMIDocumentValidator;

/**
 * XMI 기반 코드 생성을 위한 위저드 페이지 클래스
 * <p><b>NOTE:</b> XMI 리소스 선택 및 모델 변환 처리. 
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
public class XMIBasedCodeGenWizardPage extends WizardPage {

	/** 트리 뷰어 */
	private CheckboxTreeViewer viewer;
	/** 소스 파일 */
	private Text sourceFileField;
	/** 소스 모델 */
	private Model egovModel;
	/** 타겟 객체 맵 */
	private Map<?, ?> target = null;
	/** 스테레오 타입 클래스 목록 */
	private Map<?, ?> stereotypeClassList = null;
	/** 변환 성공 여부 */
	private boolean convertOk = true;

	/**
	 * 생성자
	 * 
	 * @param pageName
	 * 
	 */
	public XMIBasedCodeGenWizardPage(String pageName) {
		super(pageName);
		setTitle(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.resource.title"));
		setDescription(EgovModelCodeGenPlugin.getDefault().getResourceString("wizard.resource.description"));
	}

	/**
	 * 모델 가져오기
	 * 
	 * @return
	 * 
	 */
	public Model getModel() {
		return egovModel;
	}

	/*
	 * 화면 생성
	 * 
	 * (non-Javadoc)
ㄴ	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 * 
	 */
	public void createControl(Composite parent) {
		setPageComplete(false);
		Composite container = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);
		setControl(container);

		final Label label = new Label(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		// gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("XMI File:");
		sourceFileField = new Text(container, SWT.BORDER);
		sourceFileField.setEditable(true);
		sourceFileField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				convertOk = false;
				updatePageComplete();

			}
		});
		sourceFileField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseForSourceFile();
			}

		});
		button.setText("Browse...");

		final Label label2 = new Label(container, SWT.NONE);
		final GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData2.horizontalSpan = 3;
		label2.setLayoutData(gridData2);

		// initContents();
	}

	/**
	 * 모델 변환하기
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	private boolean convertModel() throws Exception {
		// model convert
		convertUmlModelFromXMI(getSourceLocation().makeAbsolute().toString());
		ModelListMaker modelgetter = new ModelListMaker();
		if (egovModel != null) {
			modelgetter.setObject(egovModel);
			modelgetter.makeList();
			target = modelgetter.getClassesList();
			return true;
		}
		return false;
	}
	
	/**
	 * XMI 파일로부터 UML 모델 변환하기
	 * 
	 * @param xmiFilePath
	 * @throws Exception
	 * 
	 */
	public void convertUmlModelFromXMI(String xmiFilePath) throws Exception{
		IConverter converter = null;
		Document doc = XMIUtil.getXMIDocument(xmiFilePath);

		if (XMIUtil.isEclipseUMLModel(doc)){
			NodeList rootnode = doc.getChildNodes();
			if (rootnode!=null
				  && rootnode.getLength()>0
				  && rootnode.item(0).getAttributes().getLength()>0
				  && rootnode.item(0).getAttributes().getNamedItem("xmlns:egov")!=null)
				{
				converter = new EgovModelConverter();
			}else{
				converter = new EclipseModelConverter();
			}
		}else {
			converter = new NonEclipseModelConverter();  	
		}
		converter.setXMIFilePath(xmiFilePath);
		converter.convertModel();
		
		egovModel = converter.getModel();
		stereotypeClassList = converter.getStereotypeClasses();
    }

	/**
	 * 소스 파일 선택 창 팝업하기 
	 * 
	 * 
	 */
	protected void browseForSourceFile() {
		IPath path = browse(getSourceLocation(), true);
		if (path == null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(
					rootLoc.segmentCount());
		sourceFileField.setText(path.toString());

	}

	/**
	 * 페이지 완료 여부 업데이트하기
	 * 
	 * 
	 */
	protected void updatePageComplete() {
		setPageComplete(false);
		IPath sourceLoc = getSourceLocation();
		if (sourceLoc == null || !sourceLoc.toFile().exists()) {
			setMessage(null);
			setErrorMessage("File not exists");
			return;
		}else{
			XMIDocumentValidator validator = new XMIDocumentValidator();
			try {
				if (!validator.validate(sourceLoc.toOSString())){
					setMessage(null);
					setErrorMessage("Not a XMI File.");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				setMessage(null);
				setErrorMessage("File has an Error");
				return;
			}
		}
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}

	/**
	 * 선택된 모델 가져오기
	 * 
	 * @return
	 * 
	 */
	public Object[] getSelectedModel() {
		return viewer.getCheckedElements();
	}

	/**
	 * 소스 파일 선택 창 
	 * 
	 * @param path
	 * @param mustExist
	 * @return
	 * 
	 */
	private IPath browse(IPath path, boolean mustExist) {
		FileDialog dialog = new FileDialog(getShell(), mustExist ? SWT.OPEN
				: SWT.SAVE);
		if (path != null) {
			if (path.segmentCount() > 1)
				dialog.setFilterPath(path.removeLastSegments(1).toOSString());
			if (path.segmentCount() > 0)
				dialog.setFileName(path.lastSegment());
		}
		dialog.setFilterExtensions(new String[]{"*.xmi","*.xml","*.xmi;*.xml","*.*"});
		String result = dialog.open();
		if (result == null) {
			return null;
		}
		return new Path(result);

	}

	/**
	 * 소스 폴더 가져오기
	 * 
	 * @return
	 * 
	 */
	public IPath getSourceLocation() {
		String text = sourceFileField.getText().trim();
		if (text.length() == 0)
			return null;
		IPath path = new Path(text);
		if (!path.isAbsolute())
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
					.append(path);
		return path;
	}

	/* 
	 * 다음페이지로 이동 가능 여부 가져오기
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 * 
	 */
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	/* 
	 * 다음 페이지 가져오기
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 * 
	 */
	public IWizardPage getNextPage() {
        if (!convertOk)
			try {
				convertOk = convertModel();
			} catch (Exception e) {
				e.printStackTrace();
				convertOk = false;
			}

    	ModelBasedCodeGenWizardPage page = (ModelBasedCodeGenWizardPage) super.getNextPage();
        if (convertOk){
        	page.setStereotypeClassList(stereotypeClassList);
        	page.setTarget(target);
        }else{
        	page.setStereotypeClassList(null);
        	page.setTarget(null);
        }
		return page;
	}

}
