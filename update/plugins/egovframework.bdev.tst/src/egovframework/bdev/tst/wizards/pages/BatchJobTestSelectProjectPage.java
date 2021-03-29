package egovframework.bdev.tst.wizards.pages;

import java.util.ArrayList;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.ide.com.natures.EgovBatchNature;
import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.bdev.tst.wizards.model.BatchJobTestContext;
import egovframework.bdev.tst.wizards.views.ClassTableViewerLabelProvider;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * 배치 테스트 프로젯트 선택 마법사 페이지 클래스
 * @author 조용현
 * @since 2012.07.24
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class BatchJobTestSelectProjectPage extends WizardPage {

	/** Batch Job Test Context */
	BatchJobTestContext context;
	
	/** Batch Nature를 가진 선택 가능한 Project의 ListViewer*/
	TableViewer projectTableViewer;
	
	/** Project 이름을 입력하는 Text */
	private Text containerNameField;
	
	/** containerNameField 옆의 설명 Label */
	private Label containerNameLabel;
	
	/** "Note : " String을 가지는 Label */
	private Label noteName;
	
	/** Note의 내용을 담는 Label */
	private Label noteContent;
	
	/** Batch Nature를 가진 선택 가능한 Project의 목록 */
	private ArrayList<IProject> eGovProjects;
	
	/** Project Explorer에서 선택한 항목 */
	private IStructuredSelection selection = null;

	/**
	 * BatchJobTestSelectProjectPage 생성자
	 * 
	 * @param pageName
	 * @param context
	 * @param selection
	 */
	public BatchJobTestSelectProjectPage(String pageName, BatchJobTestContext context, IStructuredSelection selection) {
		super(pageName);

		this.context = (BatchJobTestContext) context;
		this.selection = selection;
		
		setTitle(BatchTestMessages.BatchJobTestSelectProjectPage_TITLE);
		setDescription(BatchTestMessages.BatchJobTestSelectProjectPage_DESCRIPTION);
		eGovProjects = getBatchNatureProject();
	}

	public void createControl(Composite parent) {

		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL));

		containerNameLabel = new Label(control, SWT.NONE);
		containerNameLabel.setText(BatchTestMessages.BatchJobTestSelectProjectPage_CONTAINER_NAME_LABEL);

		containerNameField = new Text(control, SWT.BORDER);
		containerNameField
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		containerNameField.addListener(SWT.Modify, validation);

		createTreeTable(control);

		createNoteControl(control);

		setPageComplete(false);
		setControl(control);
	}

	/**
	 * 선택 가능한 Project의 목록을 보여주는 ListTable 생성
	 * 
	 * @param control
	 */
	private void createTreeTable(Composite control) {
		GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
		spec.widthHint = 320;
		spec.heightHint = 300;
		
		projectTableViewer = new TableViewer(control, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		projectTableViewer.getControl().setLayoutData(spec);
		projectTableViewer.setContentProvider(new ArrayContentProvider());
		projectTableViewer.setLabelProvider(new ClassTableViewerLabelProvider());
		projectTableViewer.setInput(eGovProjects);		
		projectTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)projectTableViewer.getSelection();
				
				if(selection.isEmpty()){
					return;
				}
				
				IProject selectProject = (IProject)selection.getFirstElement();
				containerNameField.setText(selectProject.getName());
				
				context.setSelection(selection);
			}
		});
	}
	
	/**
	 * Note Control 생성
	 * 
	 * @param control
	 */
	private void createNoteControl(Composite control){
		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(new GridData());

		noteName = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteName.setText(BatchTestMessages.BatchJobTestSelectProjectPage_NOTE);
		StringUtil.setLabelStringBold(noteName);

		noteContent = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteContent.setText(BatchTestMessages.BatchJobTestSelectProjectPage_NOTE_CONTENTS);
	}
	
	/** Batch Nature를 가진 Project 목록 추출 */
	private ArrayList<IProject> getBatchNatureProject(){
		ArrayList<IProject> result = new ArrayList<IProject>();

		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot()
		.getProjects();
		for (IProject project : allProjects) {
			try {
				if (EgovJavaElementUtil.isJavaProject(project)
						&& (EgovBatchNature.isEgovNatureEnabled(project))) {
					result.add(project);
				}
			} catch (CoreException e) {	}
		}
		return result;
	}
	
	/** containerNameField의 validation Listener */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {
			
			IProject project = null;
			boolean canFlipToNextPage = false;
			String containerClassName = containerNameField.getText();
			
			if(!NullUtil.isEmpty(containerClassName)){
				for (int i = 0; i < eGovProjects.size(); i++) {
					project = eGovProjects.get(i);
	
					// class명이 같으면 true
					if (project.getName().equals(containerClassName)) {
						canFlipToNextPage = true;
						break;
					}
				}
			}

			setPageComplete(canFlipToNextPage);
			
			if(canFlipToNextPage){
				setErrorMessage(null);
				context.setProjectName(containerClassName);
			}else{
				setErrorMessage(BatchTestMessages.BatchJobTestSelectProjectPage_INVALID_PROJECT_NAME);
			}
		}
	};
	
	/**
	 * <pre>
	 * EgovBatchNature를 가진 Project 또는 그 하위 디렉토리 선택시
	 * Project와 Path정보를 context에 저장하고 project 선택 Page는 넘어간다.
	 * <pre>
	 * @return boolean
	 */
	private IProject getElementFromPackageExplorer() {		
		if (!selection.isEmpty()) {

			IProject selectedProject = null;
			Object object = ((IStructuredSelection) selection)
					.getFirstElement();

			if (object instanceof IJavaElement) {
				IJavaElement element = (IJavaElement) object;
				selectedProject = element.getJavaProject().getProject();
			} else if (object instanceof Folder) {
				Folder element = (Folder) object;
				selectedProject = element.getProject();
			} else {
				return null;
			}
			
			return selectedProject;
		}

		return null;
	}
	
	/**
	 * TreeViewer에서 넘어온 Project를 Select 함
	 * 
	 * @param selectedProject
	 */
	private void selectItemFromTreeViewer(IProject selectedProject){
		
		if(!NullUtil.isNull(selectedProject)){
		
			TableItem[] items = projectTableViewer.getTable().getItems();
		
			if(items.length > 0){
				
				for(TableItem item : items){
					IProject project = (IProject)item.getData();
					String projectName = project.getName();
					if(selectedProject.getName().equals(projectName)){
						projectTableViewer.setSelection(new StructuredSelection(selectedProject));
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible){
			resetContextWithoutSelectionAndProjectName();
			
			IProject selectedProject = getElementFromPackageExplorer();
			
			if(!NullUtil.isNull(selectedProject)){
				selectItemFromTreeViewer(selectedProject);
			}
		}
		super.setVisible(visible);
	}
	
	/**
	 * Context의 값을 Project 명, Selection을 제외하고 초기화 시킴
	 * 
	 */
	private void resetContextWithoutSelectionAndProjectName(){
		IStructuredSelection selection = context.getSelection();
		String projectName = context.getProjectName();
		context.clearValues();
		context.setSelection(selection);
		context.setProjectName(projectName);
	}
}
