package egovframework.dev.imp.commngt.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import egovframework.dev.imp.commngt.EgovCommngtPlugin;
import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.wizards.model.IComponentElement;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.commngt.wizards.operation.ComponentAssembleOperation;
import egovframework.dev.imp.commngt.wizards.pages.CustomizeTableCreationPage;
import egovframework.dev.imp.commngt.wizards.pages.SelectCommonComponentPage;
import egovframework.dev.imp.commngt.wizards.pages.SelectTableCreationTypePage;
import egovframework.dev.imp.commngt.wizards.pages.SelectTargetProjectPage;
import egovframework.dev.imp.ide.natures.EgovNature;

//public abstract class AddEgovCcmWizard extends Wizard implements
//INewWizard, IExecutableExtension{
/**
 * 공통컴포넌트 생성 마법사  클래스
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
public class NewEgovCommngtWizard extends BasicNewResourceWizard implements INewWizard, IExecutableExtension {

	/** 공통컴포넌트 마법사 컨텍스트 */
	private NewEgovCommngtContext context;
	/** 프로젝트 선택 페이지 */
	private SelectTargetProjectPage projectPage;
	/** 컴포넌트 선택 페이지 */
	private SelectCommonComponentPage componentPage;
	/** 인스톨타입 선택 페이지 */
	private SelectTableCreationTypePage installTypePage;
	/** 데이터소스 선택 페이지 */
	private CustomizeTableCreationPage datasoucePage;
	/** 워크벤치 */
	private IWorkbench workbench;
	/** 선택리소스 */
	private IStructuredSelection selection;


    public IWorkbench getWorkbench() {
		return workbench;
	}

	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	/**
     * 생성자
     */
	public NewEgovCommngtWizard() {
        //setContext(new NewEgovCommngtContext());
		setWindowTitle(ComMngtMessages.newEgovCommngtWizard0);
        setDefaultPageImageDescriptor(EgovCommngtPlugin.getDefault()
                .getImageDescriptor(EgovCommngtPlugin.IMG_COMMNGT_WIZ_BANNER));
		setNeedsProgressMonitor(true);
	}

    /**
     * 페이지 컨트롤 생성
     * @param pageContainer
     */
	@Override
	public void createPageControls(Composite pageContainer){
		super.createPageControls(pageContainer);
		//getShell().setSize(535, 640);
		Rectangle rect = Display.getCurrent().getClientArea();
		getShell().setBounds(rect.width/2-265, rect.height/2-320, 535, 640);
	}
	
    /**
     * 컨텍스트 설정
     * @param context
     */
    protected void setContext(NewEgovCommngtContext context) {
        this.context = context;
    }
    
    /**
     * 컨텍스트 가져오기
     * @return
     */
    protected NewEgovCommngtContext getContext() {
        return this.context;
    }
    
    /**
     * 마법사 페이지 추가
     */
    public void addPages() {
		IStructuredSelection selection = (IStructuredSelection)getSelection();
		Object selected = selection.getFirstElement(); 
		IJavaProject javaProject = null;
		boolean projectPageView = true;
		if(selected != null){
			try {
				if (selected instanceof IResource) {
					if(EgovNature.isEgovNatureEnabled(((IResource) selected).getProject())){
						javaProject = JavaCore.create( ((IResource) selected).getProject() );
						projectPageView = false;
					}
				} else if(selected instanceof IJavaElement){
					if(EgovNature.isEgovNatureEnabled(((IJavaElement) selected).getJavaProject().getProject())){
						javaProject = ((IJavaElement) selected).getJavaProject(); 
						projectPageView = false;
					}
				}
				

			} catch (CoreException e) {
				CommngtLog.logCoreError(e);
			}
		}
		if(projectPageView){
	    	projectPage = new SelectTargetProjectPage(ComMngtMessages.newEgovCommngtWizard1, javaProject, getContext());
	    	addPage(projectPage);
		}else{
			context.setJavaProject(javaProject);	
		}
		
    	componentPage = new SelectCommonComponentPage(ComMngtMessages.newEgovCommngtWizard2, getContext());
    	addPage(componentPage);
    	installTypePage = new SelectTableCreationTypePage(ComMngtMessages.newEgovCommngtWizard3, getContext());
    	addPage(installTypePage);
    	datasoucePage = new CustomizeTableCreationPage(ComMngtMessages.newEgovCommngtWizard4, getContext());
    	addPage(datasoucePage);    	

    }

    /**
     * 종료시 실행
     */
	@Override
	public boolean performFinish(){
		
		boolean result = true;
		boolean webExist = false;
		try {
			for(IComponentElement component : context.getComponent()){
				if(component.isWebExist()) webExist = true;
			}
			
			String webFile = "dontAction";
			System.out.println("********** webExist : "+ webExist);
			if(webExist) {
				/*
				MessageDialog dg = new MessageDialog ( getShell(), ComMngtMessages.newEgovCommngtWizard5, null, ComMngtMessages.newEgovCommngtWizard6, MessageDialog.QUESTION_WITH_CANCEL, new String[]{ "Yes", "No"}, 0 );
				
				switch(dg.open()) {
				case 0: 
					//yes
					context.setWebModify(true);
					webFile = "Action";
					System.out.println("********** 0 : "+ webFile + " = " + context.isWebModify());
					break;
				case 1:
					//no
					context.setWebModify(false);
					webFile = "Action";
					System.out.println("********** 1 : "+ webFile + " = " + context.isWebModify());
					break;
				default:
//					break;
				}
				
				result = false;
				*/
				if (!MessageDialog.openConfirm(getShell(), ComMngtMessages.newEgovCommngtWizard5, ComMngtMessages.newEgovCommngtWizard6)) {
					return false;
				}else {
					context.setWebModify(true);
					webFile = "Action";					
					result = false;
				}
			} else {
				webFile = "Action";
				System.out.println("********** webFile : "+ webFile);
			}
			
			if(!"dontAction".equals(webFile)) {
				result = true;
				
				ComponentAssembleOperation ao = new ComponentAssembleOperation(context);
				IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(ao);
				getContainer().run(true, true, op);
			}
	        
		}catch (InvocationTargetException e) {
			CommngtLog.logError(e);
            result = false;
        } catch (InterruptedException e) {
			CommngtLog.logError(e);
            result = false;
        }
//		datasoucePage.performOk(); //프로퍼티파일에 설정

		return result;
   }
		
    /**
     * 취소시 실행
     */
	public boolean performCancel()
	{
		return true;
	}

    /**
     * 종료 가능 조건
     */
	@Override
	public boolean canFinish() {
		
		boolean successFinish = false;
		if( (context.getJavaProject()!=null || projectPage.isPageComplete()) && componentPage.isPageComplete() && installTypePage.isPageComplete() && installTypePage.isSelectInstallTypePage != null){ //default, recommand
			successFinish = true;
//			return true;//finish 활성화
			
		}else{ //customize			
			if( (context.getJavaProject()!=null || projectPage.isPageComplete()) && componentPage.isPageComplete() && datasoucePage.isPageComplete()  && datasoucePage.checkLastPage != null){
				successFinish = true;
//				return true;
			}
//			return false; //finish 비활성화
		}
//		if((context.getJavaProject()!=null || projectPage.isPageComplete()) && componentPage.isPageComplete() && installTypePage.isPageComplete() && installTypePage.isSelectInstallTypePage != null && installTypePage.isCheckedComponentNumChanged != null){
//			successFinish = true;
//			
//		}else{
//			successFinish = false;
//		}
		return successFinish;
	}
	
	/** 초기화 */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        setContext(new NewEgovCommngtContext());
        this.workbench = workbench;
        this.selection = currentSelection;

    }
    
    /** 선택리소스 얻기 */
    public IStructuredSelection getSelection() {
        return selection;
    }
    /** 초기데이터 셋팅 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// setInitializationData
	}
	
}
