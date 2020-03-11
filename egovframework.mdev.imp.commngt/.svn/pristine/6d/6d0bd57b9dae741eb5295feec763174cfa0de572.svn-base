package egovframework.mdev.imp.commngt.wizards;


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

import egovframework.mdev.imp.commngt.MobileComMngtPlugin;
import egovframework.mdev.imp.commngt.common.MobileComMngtLog;
import egovframework.mdev.imp.commngt.common.MobileComMngtMessages;
import egovframework.mdev.imp.commngt.wizards.model.IComponentElement;
import egovframework.mdev.imp.commngt.wizards.model.MobileCommngtContext;
import egovframework.mdev.imp.commngt.wizards.operation.ComponentAssembleOperation;
import egovframework.mdev.imp.commngt.wizards.pages.CustomizeTableCreationPage;
import egovframework.mdev.imp.commngt.wizards.pages.SelectCommonComponentPage;
import egovframework.mdev.imp.commngt.wizards.pages.SelectTableCreationTypePage;
import egovframework.mdev.imp.commngt.wizards.pages.SelectTargetProjectPage;
import egovframework.mdev.imp.ide.natures.EgovMobileNature;

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
public class AddMobileCommngtWizard extends BasicNewResourceWizard implements INewWizard, IExecutableExtension {

	/** 공통컴포넌트 마법사 컨텍스트 */
	private MobileCommngtContext context;
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


    /**
     * 생성자
     */
	public AddMobileCommngtWizard() {
        //setContext(new NewEgovCommngtContext());
		setWindowTitle(MobileComMngtMessages.addMobileCommngtWizard0);
        setDefaultPageImageDescriptor(MobileComMngtPlugin.getDefault()
                .getImageDescriptor(MobileComMngtPlugin.imgMCommngtWizBanner));
		setNeedsProgressMonitor(true);
	}
	
    public IWorkbench getWorkbench() {
		return workbench;
	}

	public void setWorkbench(IWorkbench workbench) {
		this.workbench = workbench;
	}

	/**
     * 페이지 컨트롤 생성
     * @param pageContainer
     */
	@Override
	public void createPageControls(Composite pageContainer){
		super.createPageControls(pageContainer);
		Rectangle rect = Display.getCurrent().getClientArea();
		getShell().setBounds(rect.width/2-265, rect.height/2-320, 535, 640);
	}
	
    /**
     * 컨텍스트 설정
     * @param context
     */
    protected void setContext(MobileCommngtContext context) {
        this.context = context;
    }
    
    /**
     * 컨텍스트 가져오기
     * @return
     */
    protected MobileCommngtContext getContext() {
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
					if(EgovMobileNature.isEgovNatureEnabled(((IResource) selected).getProject())){
						javaProject = JavaCore.create( ((IResource) selected).getProject() );
						projectPageView = false;
					}
				} else if(selected instanceof IJavaElement){
					if(EgovMobileNature.isEgovNatureEnabled(((IJavaElement) selected).getJavaProject().getProject())){
						javaProject = ((IJavaElement) selected).getJavaProject(); 
						projectPageView = false;
					}
				}
				

			} catch (CoreException e) {
				MobileComMngtLog.logCoreError(e);
			}
		}
		if(projectPageView){
	    	projectPage = new SelectTargetProjectPage(MobileComMngtMessages.addMobileCommngtWizard1, javaProject, getContext());
	    	addPage(projectPage);
		}else{
			context.setJavaProject(javaProject);	
		}
		
    	componentPage = new SelectCommonComponentPage(MobileComMngtMessages.addMobileCommngtWizard2, getContext());
    	addPage(componentPage);
    	installTypePage = new SelectTableCreationTypePage(MobileComMngtMessages.addMobileCommngtWizard3, getContext());
    	addPage(installTypePage);
    	datasoucePage = new CustomizeTableCreationPage(MobileComMngtMessages.addMobileCommngtWizard4, getContext());
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
			
			if(webExist) {
				MessageDialog dg = new MessageDialog ( getShell(), MobileComMngtMessages.addMobileCommngtWizard5, null, MobileComMngtMessages.addMobileCommngtWizard6, MessageDialog.QUESTION_WITH_CANCEL, new String[]{ "Yes", "No"}, 0 );
				
				switch(dg.open()) {
				case 0: 
					//yes
					context.setWebModify(true);
					webFile = "Action";
					break;
				case 1:
					//no
					context.setWebModify(false);
					webFile = "Action";
					break;
				default:
					//do nothing
				}
				
				result = false;
				
			} else {
				webFile = "Action";
			}
			
			
			if(!"dontAction".equals(webFile)) {
				result = true;
				
				ComponentAssembleOperation ao = new ComponentAssembleOperation(context);
		        IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(ao);
		        getContainer().run(true, true, op);
			}
			
		}catch (InvocationTargetException e) {
			MobileComMngtLog.logError(e);
            result = false;
        } catch (InterruptedException e) {
			MobileComMngtLog.logError(e);
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
			
		}else{ //customize			
			if( (context.getJavaProject()!=null || projectPage.isPageComplete()) && componentPage.isPageComplete() && datasoucePage.isPageComplete()  && datasoucePage.checkLastPage != null){
				successFinish = true;
			}
		}
		return successFinish;
	}
	/** 초기화 */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        setContext(new MobileCommngtContext());
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
		//setInitializationData
	}
	
}
