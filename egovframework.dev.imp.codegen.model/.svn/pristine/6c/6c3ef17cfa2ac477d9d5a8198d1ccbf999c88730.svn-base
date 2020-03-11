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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
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

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;

/**
 *  XMI 파일을 EXPORT 하는 위저드 페이지 클래스 
 * <p><b>NOTE:</b> XMI EXPORT를 위한 경로 설정 처리. 
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
public class XMIExportWizardPage extends WizardPage {

	/** 트리 뷰어 */
    private CheckboxTreeViewer viewer;
    /** EXPORT 파일 필드 */
    private Text exportFileField;
    
    /**
     * 생성자
     * 
     * @param pageName
     */
    public XMIExportWizardPage(String pageName) {
            super(pageName);
            setDescription(EgovModelCodeGenPlugin.getDefault().getResourceString(
                            "wizard.resource.description"));
    }

	/* 
	 * 페이지 화면 구성
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
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
//		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		label.setText("Export File Name:");
		exportFileField = new Text(container, SWT.BORDER);
//		exportFileField.setEditable(false);
		exportFileField.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				updatePageComplete();
				
			}
		});
		exportFileField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				browseForExportFile();
			}
		
		});
		button.setText("Browse...");
		
		final Label label2 = new Label(container, SWT.NONE);
		final GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData2.horizontalSpan = 3;
		label2.setLayoutData(gridData2);
	}
	
	/**
	 * 파일 경로 선택창 팝업하기
	 * 
	 * 
	 */
	protected void browseForExportFile() {
		IPath path = browse(getExportPath(), false);
		if (path==null)
			return;
		IPath rootLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		if (rootLoc.isPrefixOf(path))
			path = path.setDevice(null).removeFirstSegments(rootLoc.segmentCount());
		exportFileField.setText(path.toString());
		
	}

	/**
	 * 페이지 완성 여부 업데이트하기
	 * 
	 * 
	 */
	protected void updatePageComplete() {
		setPageComplete(false);
		IPath sourceLoc = getExportPath();
		 
		if (sourceLoc == null || sourceLoc.isEmpty()){
			setMessage(null);
			setErrorMessage("Invalid File Name");
			return;
		}
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}



	/**
	 * 선택된 모델 목록 가져오기
	 * 
	 * @return
	 * 
	 */
	public Object[] getSelectedModel() {
		return viewer.getCheckedElements();
	}
	
	/**
	 * 파일 경로 창 
	 * 
	 * @param path
	 * @param mustExist
	 * @return
	 * 
	 */
	private IPath browse(IPath path, boolean mustExist){
		FileDialog dialog = new FileDialog(getShell(), mustExist? SWT.OPEN:SWT.SAVE);
		if (path!=null){
			if (path.segmentCount() >1)
				dialog.setFilterPath(path.removeLastSegments(1).toOSString());
			if (path.segmentCount() >0)
				dialog.setFileName(path.lastSegment());
		}
		String result = dialog.open();
		if (result == null){
			return null;
		}
		return new Path(result);
		
	}
	
	/**
	 * EXPORT 경로 가져오기 
	 * 
	 * @return
	 * 
	 */
	public IPath getExportPath() {
		String text = exportFileField.getText().trim();
		if (text.length()==0)
			return null;
		IPath path = new Path(text);
		if (!path.isAbsolute())
			path = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(path);
		return path;
	}


}
