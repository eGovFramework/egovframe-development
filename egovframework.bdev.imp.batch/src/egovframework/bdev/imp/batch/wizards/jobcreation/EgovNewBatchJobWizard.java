package egovframework.bdev.imp.batch.wizards.jobcreation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import egovframework.bdev.imp.batch.EgovBatchPlugin;
import egovframework.bdev.imp.batch.common.BatchLog;
import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.HandlingFileOperation;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.operation.CreateBatchJobXMLFileOperation;
import egovframework.bdev.imp.batch.wizards.jobcreation.pages.BatchJobCreationCustomizePage;
import egovframework.bdev.imp.batch.wizards.jobcreation.pages.BatchJobcreationSelectProjectPage;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Batch Job 생성 Wizard
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
public class EgovNewBatchJobWizard extends Wizard implements INewWizard {

	/** Context 할당 */
	BatchJobCreationContext context = new BatchJobCreationContext();

	/** Batch Job 설정 Page */
	BatchJobCreationCustomizePage customizePage = null;

	/** Package Explorer의 Selection */
	private ISelection selection = null;

	/** EgovNewBatchJobWizard의 생성자 */
	public EgovNewBatchJobWizard() {
		super();
		setWindowTitle("New eGovFrame Batch Job File"); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
		setDefaultPageImageDescriptor(EgovBatchPlugin.getDefault().getImageDescriptor(EgovBatchPlugin.IMG_BATCH_JOB_WIZ_BANNER));
	}

	public void addPages() {
		BatchJobcreationSelectProjectPage selectProjectPage = new BatchJobcreationSelectProjectPage("SelectProjectPage", context, selection); //$NON-NLS-1$
		addPage(selectProjectPage);

		customizePage = new BatchJobCreationCustomizePage("CustomizeCreation", //$NON-NLS-1$
				context);
		addPage(customizePage);

		if (!NullUtil.isNull(getContainer())) {
			getContainer().getShell().setLocation(430, 55);
		}
	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage().equals(customizePage)) {
			return super.canFinish();
		} else {
			return false;
		}
	}

	public boolean performFinish() {

		String message = BatchMessages.EgovNewBatchJobWizard_CONFIRM_FINISH_MESSAGE;
		if (!MessageDialog.openConfirm(getShell(), BatchMessages.EgovNewBatchJobWizard_CONFIRM_FINISH_DIALOG_TITLE, message)) {
			return false;
		}

		JobVo[] jobVos = customizePage.getResultBatchJobVos();
		if (jobVos.length > 0) {
			for (JobVo data : jobVos) {
				StepAndDecisionVo[] dsVos = data.getStepAndDecisionVoList();
				if (dsVos.length > 0) {
					for (StepAndDecisionVo dsVo : dsVos) {
						dsVo.setJobName(data.getJobName());
					}
				}
			}
		}
		context.setJobVoList(jobVos);

		boolean result = true;
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask(BatchMessages.EgovNewBatchJobWizard_PROGRESS_MESSAGE_1, 2);

					CreateBatchJobXMLFileOperation jobXML = new CreateBatchJobXMLFileOperation();
					jobXML.setContext(context);
					monitor.subTask(BatchMessages.EgovNewBatchJobWizard_PROGRESS_MESSAGE_2);
					monitor.worked(1);

					jobXML.createJobXMLFile(context);
					monitor.subTask(BatchMessages.EgovNewBatchJobWizard_PROGRESS_MESSAGE_3);
					monitor.worked(1);

					if (monitor.isCanceled()) {
						HandlingFileOperation.deleteFile(HandlingFileOperation.newXMLFile);
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

	private void openCreatedFile(IFile file) {

		IEditorInput editorInput = new FileEditorInput(file);

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());

		try {
			page.openEditor(editorInput, desc.getId());
		} catch (PartInitException e) {
		}

	}

	@SuppressWarnings("unused")
	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {

		// create a sample file
		monitor.beginTask(BatchMessages.EgovNewBatchJobWizard_PROGRESS_MESSAGE_4 + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(BatchMessages.EgovNewBatchJobWizard_PROGRESS_ERROR_MESSAGE_1 + containerName + BatchMessages.EgovNewBatchJobWizard_PROGRESS_ERROR_MESSAGE_2);
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName(BatchMessages.EgovNewBatchJobWizard_PROGRESS_MESSAGE_5);
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream() {
		String contents = BatchMessages.EgovNewBatchJobWizard_OPEN_CONTENT_STREAM_MESSAGE;
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "egovframework.bdev.tst", //$NON-NLS-1$
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}