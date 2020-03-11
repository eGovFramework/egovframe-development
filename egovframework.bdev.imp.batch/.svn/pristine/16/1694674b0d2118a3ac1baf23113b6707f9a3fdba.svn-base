package egovframework.bdev.imp.batch.wizards.joblauncher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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

import egovframework.bdev.imp.batch.EgovBatchPlugin;
import egovframework.bdev.imp.batch.common.BatchLog;
import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.FindXMLFileBeanIdValueUtil;
import egovframework.bdev.imp.batch.wizards.com.HandlingFileOperation;
import egovframework.bdev.imp.batch.wizards.joblauncher.model.BatchJobLauncherContext;
import egovframework.bdev.imp.batch.wizards.joblauncher.operation.CreateBatchJobLauncherXMLFileOperation;
import egovframework.bdev.imp.batch.wizards.joblauncher.pages.BatchJobLauncherCustomizePage;
import egovframework.bdev.imp.batch.wizards.joblauncher.pages.BatchJoblauncherSelectProjectPage;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Batch Job Execution 생성 Wizard
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
public class EgovNewBatchJobLauncherWizard extends Wizard implements
		INewWizard {

	/** Project Explorer에서 선택한 프로젝트 */
	private ISelection selection;
	
	/** EgovNewBatchJobLauncherWizard의 Context  */
	private BatchJobLauncherContext context = new BatchJobLauncherContext();

	/** Job Launcher 정보 입력 Page */
	private BatchJobLauncherCustomizePage customizePage;

	/** EgovNewBatchJobExecutionWizard의 생성자 */
	public EgovNewBatchJobLauncherWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle(BatchMessages.EgovNewBatchJobLauncherWizard_TITLE);
		setDefaultPageImageDescriptor(EgovBatchPlugin.getDefault()
				.getImageDescriptor(
						EgovBatchPlugin.IMG_BATCH_JOB_LAUNCHER_WIZ_BANNER));
	}

	/** Adding the page to the wizard. */
	public void addPages() {

		BatchJoblauncherSelectProjectPage selectProjectPage = new BatchJoblauncherSelectProjectPage(
				BatchMessages.EgovNewBatchJobLauncherWizard_1, context, selection);
		addPage(selectProjectPage);

		customizePage = new BatchJobLauncherCustomizePage(BatchMessages.EgovNewBatchJobLauncherWizard_2,
				context);
		addPage(customizePage);

		if (!NullUtil.isNull(getContainer())) {
			getContainer().getShell().setLocation(550, 100);
		}
	}

	public boolean performFinish() {

		String duplicateBeanID = getDuplicateBeanID();

		if(!NullUtil.isEmpty(duplicateBeanID)){
			customizePage.setErrorMessage(BatchMessages.EgovNewBatchJobLauncherWizard_DUPLICAT_BEAN_ID_1+duplicateBeanID+BatchMessages.EgovNewBatchJobLauncherWizard_DUPLICAT_BEAN_ID_2);
			return false;
		}else {
			checkDuplicateDBBeanID();
			checkDuplicateJobRepositoryBeanID();
		}
		
		boolean result = true;

		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

					monitor.beginTask(BatchMessages.EgovNewBatchJobLauncherWizard_CREATING_JOB_LAUNCHER, 3);

					IFile newXMLFile = HandlingFileOperation
							.createFile(context);
					monitor.subTask(BatchMessages.EgovNewBatchJobLauncherWizard_CREATE_JOB_LAUNCHER_FILE);
					monitor.worked(1);

					CreateBatchJobLauncherXMLFileOperation jobExecutionXML = new CreateBatchJobLauncherXMLFileOperation();
					jobExecutionXML.setContext(context);
					monitor.subTask(BatchMessages.EgovNewBatchJobLauncherWizard_SETTING_JOB_LAUNCHER_INFORMATION);
					monitor.worked(1);

					jobExecutionXML.createJobExecutionXMLFile(newXMLFile);
					monitor.subTask(BatchMessages.EgovNewBatchJobLauncherWizard_APPENDING_JOB_LAUNCHER_FILE);
					monitor.worked(1);

					if (monitor.isCanceled()) {
						HandlingFileOperation
								.deleteFile(HandlingFileOperation.newXMLFile);
						throw new InterruptedException();
					}

					monitor.done();

				}
			});
			
			openCreatedFile(HandlingFileOperation.newXMLFile);
			
		} catch (InvocationTargetException e) {
			HandlingFileOperation.deleteFile(HandlingFileOperation.newXMLFile);
			BatchLog.logError(e);
			result = false;
		} catch (InterruptedException e) {
			HandlingFileOperation.deleteFile(HandlingFileOperation.newXMLFile);
			BatchLog.logError(e);
			result = false;
		}
		return result;

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

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage().equals(customizePage)) {
			return super.canFinish();
		} else {
			return false;
		}
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * 중복된 BeanID를 가져온다.
	 * 없을경우 null을 return
	 * 
	 * @return
	 */
	private String getDuplicateBeanID() {
		List<String> findingNode = new ArrayList<String>();
		findingNode.add("/beans/bean"); //$NON-NLS-1$
		
		List<String> beanList = FindXMLFileBeanIdValueUtil.findXMLFiles(context, findingNode, "id", 1); //$NON-NLS-1$
		context.setBeanList(beanList);
		HashMap<String, String> jobExecutorIDs = getJobExecutorIDs();

		for (int i = 0; i < beanList.size(); i++) {
			String duplicateBeanID = returnDuplicateBeanIdIfExist(jobExecutorIDs, beanList.get(i));
			if(!NullUtil.isNull(duplicateBeanID)){
				return duplicateBeanID;
			}
		}
		
		return null;
	}
	
	/**
	 * 중복된 Bean ID를 가져온다
	 * 
	 * @param jobExecutorIDs
	 * @param preExistBeanID
	 * @return
	 */
	private String returnDuplicateBeanIdIfExist(HashMap<String,String> jobExecutorIDs, String preExistBeanID){
		String duplicateBeanID = jobExecutorIDs.get(preExistBeanID);
		
		if(!NullUtil.isNull(duplicateBeanID)){
			return duplicateBeanID;
		}else{
			return null;
		}
	}
	
	/**
	 * Page의 입력 정보를 모두 가져온다.
	 * 
	 * @return
	 */
	private HashMap<String, String> getJobExecutorIDs(){
		HashMap<String, String> iDs = new HashMap<String, String>();
		
		iDs.put(context.getJobLauncerId(), "Launcher ID"); //$NON-NLS-1$
		iDs.put(context.getOperatorId(), "Operator ID"); //$NON-NLS-1$
		iDs.put(context.getExplorerId(), "Explorer ID"); //$NON-NLS-1$
		iDs.put(context.getRegisterId(), "Register ID"); //$NON-NLS-1$
		
		String repositoryType = context.getRepositoryType();
		if("Memory".equals(repositoryType)){ //$NON-NLS-1$
			iDs.put(context.getDatasourceBeanID(), "Datasource Bean ID"); //$NON-NLS-1$
		}
		
		return iDs;
	}
	

	/**
	 * db 관련 bean id 중복 체크
	 * 
	 */
	private void checkDuplicateDBBeanID() {
		
		List<String> beanList = context.getBeanList();

		for (int i = 0; i < beanList.size(); i++) {
			String beanID = beanList.get(i);
			if("transactionManager".equals(beanID)){ //$NON-NLS-1$
				context.setIsTransactionManagerExist(true);
			}
			if("lobHandler".equals(beanID)){ //$NON-NLS-1$
				context.setIsLobHandlerExist(true);
			}
			if("jdbcTemplate".equals(beanID)){ //$NON-NLS-1$
				context.setIsJdbcTemplateExist(true);
			}
		}
	}
	
	/**
	 * jobRepository  bean id 중복 체크
	 * 
	 */
	private void checkDuplicateJobRepositoryBeanID() {
		
		List<String> beanList = context.getBeanList();

		for (int i = 0; i < beanList.size(); i++) {
			String beanID = beanList.get(i);
			if("jobRepository".equals(beanID)){ //$NON-NLS-1$
				context.setIsJobRepositoryExist(true);
			}
		}
	}
}
