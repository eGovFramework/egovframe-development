package egovframework.dev.imp.commngt.wizards.pages;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.ide.natures.EgovNature;


/**
 * 프로젝트 선택 마법사페이지 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public class SelectTargetProjectPage extends WizardPage implements ModifyListener, Listener{

	/** 선택프로젝트명 */
	private Text containerText = null;
	/** 공통컴포넌트 컨텍스트 */
	private final NewEgovCommngtContext context;
	/** 프로젝트 목록 정보 */
//	private String[] projectList = null;
	/** 프로젝트 목록 */
	private ListViewer projectListViewer = null;
	/** 선택 프로젝트정보 */
	private java.util.List<IProject> jpj = null;
	/** 선택프로젝트 정보 */
	private final IJavaProject selectionProject;
	
    /**
     * 생성자
     * @param pageName
     * @param context
     */
	public SelectTargetProjectPage(String pageName, IJavaProject selectionProject, NewEgovCommngtContext context) {
		super(pageName);
		this.context = context;
		setTitle(ComMngtMessages.selectProjectPagepageTitle);
		setDescription(ComMngtMessages.selectProjectPagepageDescription);
		
		this.selectionProject = selectionProject;
		
		//this.currentSelection = selection;
		
		
	}

	/** 페이지 완료 여부 */
	public boolean isPageComplete() {
		if(projectListViewer.getList().getSelectionIndex() >= 0){
			//setPageComplete(true);
			return true;
		}
		else{
			//setPageComplete(false);
			return false;
		}
		//return containerText.getText().length() != 0;
		//return context.getJavaProject() != null;
	}
	
	/** 텍스트 편집 */
	public void modifyText(ModifyEvent e) {
		getWizard().getContainer().updateButtons();
	}
	
    /**
     * 화면단 컨트롤 생성
     */
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		
		layout.numColumns = 2;
		layout.verticalSpacing = 6;
		
		createSelectProjectControls(container);
		
		containerText.addModifyListener(this);
		
		setControl(container);
		
		updatePageComplete();

	}

    /**
     * 프로젝트 선택 컨트롤 생성
     */
	public void createSelectProjectControls(Composite container) {
		
		GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
		gridData1.horizontalSpan = 2;
		Label label = new Label(container, SWT.NULL);
		label.setText(ComMngtMessages.selectProjectPage0);
		label.setLayoutData(gridData1);
		
		/*선택된 프로젝트 명*/
		GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.horizontalSpan = 2;
		containerText = new Text(container, SWT.BORDER);
		containerText.setLayoutData(gridData2);
		containerText.addListener(SWT.Modify, projectNameModifyListener);
		
		/*프로젝트 정보 보여줄 리스트*/
		GridData gridData3 = new GridData(GridData.FILL_HORIZONTAL);
		gridData3.horizontalSpan = 2;
		gridData3.heightHint = 180;
		
		projectListViewer = new ListViewer(container, SWT.FILL | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE );
		//pList = projectListViewer.getList();
		//pList.setLayoutData(new GridData(GridData.FILL_BOTH/*, GridData.FILL_VERTICAL, true, true, 1, 1)*/));
		projectListViewer.setContentProvider(new projectContentProvider());
		projectListViewer.setLabelProvider(new projectLabelProvider());
		projectListViewer.addSelectionChangedListener(new projectSelectionChangedListener());
		Object o = getProjectList();
		

		Label note = new Label(container, SWT.BOLD|SWT.TOP);
		note.setAlignment(SWT.TOP);
		note.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		GridData gd = new GridData();
		note.setText("Note:\n ");
		note.setLayoutData(gd);
		
		Label label2 = new Label(container, SWT.NONE|SWT.TOP);
		label2.setAlignment(SWT.TOP);
		if(o == null ){
			label2.setText(ComMngtMessages.selectProjectPage1);
    		setPageComplete(false);
			setErrorMessage(ComMngtMessages.selectProjectPageerror1);
		}else{
			projectListViewer.setInput(o);
			//setCurrentSelection();
			setCurrentSelection(selectionProject);
			label2.setText(ComMngtMessages.selectProjectPage2);
		}
		

		projectListViewer.getList().setLayoutData(gridData3);
		GridData gd2 = new GridData();
		label2.setLayoutData(gd2);

		
	}
	
    /**
     * 프로젝트명 변경 이벤트 리스너
     */
    private final Listener projectNameModifyListener = new Listener() {
        public void handleEvent(Event e) {
        	int i = projectListViewer.getList().indexOf(containerText.getText());
        	if(i>=0){
        		projectListViewer.getList().setSelection(i);
        		//setMessage(EgovCommngtMessages.SelectProjectPage_pageDescription);
        		setPageComplete(true);
                setErrorMessage(null);
                setMessage(null);
        	}
        	else{
        		projectListViewer.getList().deselectAll();
        		setPageComplete(false);
        		setErrorMessage(ComMngtMessages.selectProjectPageerror2);
        	}
        }
    };
	
	/**
     * 생성된 Java 프로젝트, egov 프로젝트 목록
     */
    private Object getProjectList() {
    	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();

		jpj = new ArrayList<IProject>();
		for (int i = 0; i < projects.length; i++) {
			
			try {
				if ( EgovJavaElementUtil.isJavaProject(projects[i]) &&
						EgovNature.isEgovNatureEnabled(projects[i]) )
					jpj.add(projects[i].getProject());
			} catch (CoreException e) {
				CommngtLog.logCoreError(e);
			}
		}
		if(jpj.size()>0) return jpj.toArray();
		else return null;
	}
    
	/**
     * 프로젝트 정보 Content Provider
     */
	private class projectContentProvider implements IStructuredContentProvider{
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// inputChanged
		}
		
		public void dispose() {
			//dispose()
		}
		
		public Object[] getElements(Object inputElement) {
			//if ( inputElement instanceof List){
			//	return ((java.util.List)inputElement).toArray();
			//}
			return (Object[]) inputElement;
			
		}
	}
    
	/**
     * 프로젝트 정보 Label Provider
     */
	private class projectLabelProvider extends LabelProvider{
		public String getText(Object element) {
			
			if ( element instanceof IProject){
				return ((IProject)element).getName();
			}
			return null;
		}
	}

	/**
     * 프로젝트 정보 Selection Changed Listener
     */
	private class projectSelectionChangedListener implements ISelectionChangedListener{
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object selected = selection.getFirstElement();
			if(selected != null){
				IProject pj = (IProject)selected;
				context.setJavaProject(JavaCore.create(pj.getProject()));
				containerText.setText(pj.getName());
				if(containerText.getText() != null || !"".equals(containerText.getText())){
					setPageComplete(true);
			        setErrorMessage(null);
			        setMessage(null);
				}
			}


		}
	}	

	/**
     * 선택된 프로젝트 정보
     */
	public void setCurrentSelection(IJavaProject selectionProject){
		ISelection sel = null;
		if(selectionProject != null){
			for(IProject project : jpj){
				if(project.equals(selectionProject.getProject())){
					sel = new StructuredSelection(selectionProject.getProject());
					projectListViewer.setSelection(sel);
				}
			}
		}
		
//		if(currentSelection.size()>0){
//			
//			Object object = currentSelection.getFirstElement();
//			IJavaProject javaProject = null;
//			ISelection sel = null;
//			if (object instanceof IResource) {
//				javaProject = JavaCore.create( ((IResource) object).getProject() );
//			} else if(object instanceof IJavaElement){
//				javaProject = ((IJavaElement) object).getJavaProject(); 
//			}
//			
//			if(javaProject != null){
//				for(IProject project : jpj){
//					if(project.equals(javaProject.getProject())){
//						sel = new StructuredSelection(javaProject.getProject());
//						projectListViewer.setSelection(sel);
//					}
//				}
//			}
//			
//		}
			
		
	}
	
	/** 페이지 완결성 체크 */
	private void updatePageComplete()
	{
		setPageComplete(false);
		// 페이지의 완결성 체크
		if (isPageComplete())
			return;
		// 페이지의 완결성 체크를 건너 뛰었다면 페이지를 완료상태로 변경
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}
	
	/** Handle Event */
	public void handleEvent(Event event) {
		//handleEvent
	}
	

}
