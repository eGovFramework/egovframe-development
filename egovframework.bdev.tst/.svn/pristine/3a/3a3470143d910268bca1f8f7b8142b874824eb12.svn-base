package egovframework.bdev.tst.wizards;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import egovframework.bdev.imp.ide.com.natures.EgovBatchNature;
import egovframework.bdev.tst.EgovBatchTestPlugin;
import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.bdev.tst.wizards.model.BatchJobTestContext;
import egovframework.bdev.tst.wizards.pages.BatchJobTestCustomizePage;
import egovframework.bdev.tst.wizards.pages.BatchJobTestSelectProjectPage;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * 배치 테스트 마법사 클래스
 * @author 조용현
 * @since 2012.07.24
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class EgovBatchJobTestWizard extends Wizard implements INewWizard {
	
	/** Batch Job Test 프로젝트 Context */
	private BatchJobTestContext context = new BatchJobTestContext();
	
	/** Test할 Project 선택 Wizard Page */
	private BatchJobTestSelectProjectPage selectionPage;
	
	/** 선택한 porject를 Test하는 Wizard Page */
	private BatchJobTestCustomizePage customizePage;
	
	/** Selection */
	private IStructuredSelection selection;

	/** EgovBatchJobTestWizard 생성자 */
	public EgovBatchJobTestWizard() {
		super();
		setNeedsProgressMonitor(true);
		setDefaultPageImageDescriptor(EgovBatchTestPlugin.getDefault().getImageDescriptor(EgovBatchTestPlugin.IMG_BATCH_TST_WIZ_BANNER));
		setWindowTitle(BatchTestMessages.EgovBatchJobTestWizard_TITLE);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		
		if(isEgovProjectElement()){
			IProject selectProject = getSelectProject();
			context.setSelection(new StructuredSelection(selectProject));
			context.setProjectName(selectProject.getName());
		}else{
			selectionPage = new BatchJobTestSelectProjectPage("SelectProject", context, selection); //$NON-NLS-1$
			addPage(selectionPage);
		}
		
		customizePage = new BatchJobTestCustomizePage("Customize", context); //$NON-NLS-1$
		addPage(customizePage);
	}
	
	@Override
	public boolean canFinish() {
		if(getContainer().getCurrentPage().equals(customizePage)){
			return super.canFinish();
		}else{
			return false;
		}
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		if(!NullUtil.isEmpty(context.getJobTestFile())){
			Path path = new Path(context.getJobTestFile());
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			File file = (File)resource;
			
			openCreatedFile(file);
		}
		return true;
	}
	
	private void openCreatedFile(IFile file){
		
		IEditorInput editorInput = new FileEditorInput(file);
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		
		try {
			page.openEditor(editorInput, desc.getId());
		} catch (PartInitException e) {	}
		
	}
	
	/**
	 * 선택한 Project가 Egov Project인지 확인
	 * 
	 * @return
	 */
	private boolean isEgovProjectElement(){
		if(isProjectElement()){
			IProject project = getSelectProject();
			
			try {
				if (EgovJavaElementUtil.isJavaProject(project)
						&& (EgovBatchNature.isEgovNatureEnabled(project))) {
					return true;
				}
			} catch (CoreException e) { 
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * 선택한 항목이 Project인지 확인
	 * 
	 * @return
	 */
	private boolean isProjectElement(){
		if(selection.isEmpty()){
			return false;
		}else{
			IProject selectProject = getSelectProject();
			
			if(NullUtil.isNull(selectProject)){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**
	 * 선택한 Element가 속한 Project를 가져온다
	 * 
	 * @return
	 */
	private IProject getSelectProject(){
		Object object = selection.getFirstElement();
		IProject selectProject = null;
		
		if(object instanceof IResource){
			IResource selectResource = (IResource)object; 
			selectProject = selectResource.getProject();
		}else if(object instanceof IJavaElement){
			IJavaElement selectElement = (IJavaElement)object;
			selectProject = selectElement.getJavaProject().getProject();
		}

		return selectProject;
	}
}