package egovframework.bdev.imp.batch.wizards.com;

import java.util.ArrayList;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.ide.com.natures.EgovBatchNature;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;

/**
 * Project를 선택하는 wizard page
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see
 * 
 *      <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 *      </pre>
 */

@SuppressWarnings("restriction")
public class BatchJobSelectProjectPage extends WizardPage {

	/** BatchProject의 context */
	BatchJobContext context = null;

	/** BatchProject의 List를 보여주는 TreeViewer */
	TreeViewer treeViewer = null;

	/** BatchProject 또는 디렉토리의 Path를 보여주는 Text */
	private Text containerNameField = null;

	/** ContainerNameField옆에 있는 Label */
	private Label aboveContainerNameLabel = null;

	/** File 이름을 입력하는 Text */
	private Text fileNameField = null;

	/** FileNameField옆에 있는 Label */
	private Label fileNameLabel = null;

	/** Note의 내용을 담는 Label */
	private String noteContent = null;

	/** EgovBatchNature를 가지는 Project를 담는 ArrayList */
	private ArrayList<IProject> eGovProjects = null;

	/** 기본 File 이름 */
	private String defaultFileName = null;

	/** Package Explorer의 Selection */
	private IStructuredSelection selection = null;

	/** 새로운 폴더 생성 버튼 */
	private Button createNewFolderButton = null;

	/**
	 * defaultFileName의 값을 설정한다.
	 * 
	 * @param defaultFileName
	 */
	public void setDefaultFileName(String defaultFileName) {
		this.defaultFileName = defaultFileName;
	}

	/**
	 * noteContent의 값을 설정한다.
	 * 
	 * @param noteContent
	 */
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	/**
	 * context의 값을 가져온다
	 * 
	 * @return the context
	 */
	public BatchJobContext getContext() {
		return context;
	}

	/**
	 * <pre>
	 * BatchJobSelectProjectPage의 생성자
	 * 
	 * Type 1 : Batch Job
	 * Type 2 : Batch Job Executor
	 * </pre>
	 * 
	 * @param pageName
	 * @param context
	 * @param type
	 */
	public BatchJobSelectProjectPage(String pageName, BatchJobContext context, ISelection selection) {
		super(pageName);
		this.selection = (IStructuredSelection) selection;
		this.context = context;

		eGovProjects = getEgovProjectList();
	}

	/**
	 * Egov Nature를 가진 Project의 목록을 가져온다.
	 * 
	 * @return
	 */
	private ArrayList<IProject> getEgovProjectList() {
		ArrayList<IProject> eGovProjects = new ArrayList<IProject>();
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : allProjects) {
			try {
				if (EgovJavaElementUtil.isJavaProject(project) && (EgovBatchNature.isEgovNatureEnabled(project))) {
					eGovProjects.add(project);
				}
			} catch (CoreException e) {
				e.getStackTrace();
			}
		}

		return eGovProjects;
	}

	/**
	 * BatchJobSelectProjectPage의 Control 생성
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		createContainerNameControl(control);

		createTreeTableViewer(control);

		createNewFolderButton(control);

		createFileNameControl(control);

		createNoteControl(control);
	}

	/**
	 * Container Name control을 생성한다.
	 * 
	 * @param control
	 */
	private void createContainerNameControl(Composite control) {
		aboveContainerNameLabel = new Label(control, SWT.NONE);
		aboveContainerNameLabel.setText(BatchMessages.BatchJobSelectProjectPage_ABOVE_CONTAINER_NAME_LABEL);

		containerNameField = new Text(control, SWT.BORDER);
		containerNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		containerNameField.addListener(SWT.Modify, validation);
	}

	/**
	 * EgovBatchNature를 가지는 Project들을 보여주는 TreeTable 생성
	 * 
	 * @param control
	 */
	private void createTreeTableViewer(Composite control) {
		DrillDownComposite drillDown = new DrillDownComposite(control, SWT.BORDER);
		GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
		spec.widthHint = 320;
		spec.heightHint = 300;
		drillDown.setLayoutData(spec);

		ILabelProvider labelProvider = WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider();
		ITreeContentProvider contentsProvider = new SelectProjectTreeTableContentProvider();

		// Create tree viewer inside drill down.
		treeViewer = new TreeViewer(drillDown, SWT.NONE);
		drillDown.setChildTree(treeViewer);
		treeViewer.setContentProvider(contentsProvider);
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setComparator(new ViewerComparator());
		treeViewer.setUseHashlookup(true);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				containerSelectionChanged((IContainer) selection.getFirstElement()); // allow null
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection) selection).getFirstElement();
					if (item == null) {
						return;
					}
					if (treeViewer.getExpandedState(item)) {
						treeViewer.collapseToLevel(item, 1);
					} else {
						treeViewer.expandToLevel(item, 1);
					}
				}
			}
		});
		treeViewer.setInput(eGovProjects.toArray(new IProject[0]));
	}

	/**
	 * 새 폴더 생성 버튼을 생성한다.
	 * 
	 * @param control
	 */
	private void createNewFolderButton(Composite control) {
		createNewFolderButton = new Button(control, SWT.PUSH);
		createNewFolderButton.setText(BatchMessages.BatchJobSelectProjectPage_NEW_FOLDER_BUTTON);
		createNewFolderButton.setEnabled(false);
		createNewFolderButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if (!selection.isEmpty()) {
					IContainer selectResource = (IContainer) selection.getFirstElement();

					NewFolderDialog dialog = new NewFolderDialog(getShell(), selectResource);
					if (dialog.open() == Window.OK) {
						eGovProjects = getEgovProjectList();
						treeViewer.setInput(eGovProjects.toArray(new IProject[0]));

						treeViewer.setSelection(new StructuredSelection(dialog.getFirstResult()));
					}
				}
			}
		});
	}

	/**
	 * File명을 입력하는 control을 생성한다.
	 * 
	 * @param control
	 */
	private void createFileNameControl(Composite control) {
		Composite fileNameControl = new Composite(control, SWT.None);
		fileNameControl.setLayout(new GridLayout(2, false));
		fileNameControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		fileNameLabel = new Label(fileNameControl, SWT.None);
		fileNameLabel.setText(BatchMessages.BatchJobSelectProjectPage_FILE_NAME_LABEL);

		fileNameField = new Text(fileNameControl, SWT.BORDER);
		fileNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fileNameField.addListener(SWT.KeyUp, validation);
		fileNameField.setText(defaultFileName);
		fileNameField.setFocus();
		fileNameField.selectAll();

		fileNameField.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event event) {
				if (fileNameField.getText().length() == 0) {
					fileNameField.setText(defaultFileName);
				}
				context.setFileName(fileNameField.getText());
			}
		});
	}

	/**
	 * Note를 표시하는 Control을 생성한다.
	 * 
	 * @param control
	 */
	private void createNoteControl(Composite control) {
		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(new GridData());

		Label noteNameLabel = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteNameLabel.setText(BatchMessages.BatchJobSelectProjectPage_NOTE_HEAD);
		StringUtil.setLabelStringBold(noteNameLabel);

		Label noteContentLabel = new Label(noteControl, SWT.BOLD | SWT.TOP);
		noteContentLabel.setText(noteContent);

		setPageComplete(false);
		setControl(control);
	}

	/**
	 * Project의 TreeViewer를 클릭했을때의 Action
	 * 
	 * @param container
	 */
	private void containerSelectionChanged(IContainer container) {
		if (container == null) {
			containerNameField.setText("");//$NON-NLS-1$
		} else {
			String text = TextProcessor.process(container.getFullPath().makeRelative().toString());
			containerNameField.setText(text);
			containerNameField.setToolTipText(text);

			if (isPathIncludeResourceFolder(text)) {
				createNewFolderButton.setEnabled(true);
			} else {
				createNewFolderButton.setEnabled(false);
			}
		}
	}

	/**
	 * 선택한 폴더의 경로에 resource가 존재하는지 여부 확인
	 * 
	 * @param selectedPath
	 * @return
	 */
	private boolean isPathIncludeResourceFolder(String selectedPath) {
		String[] splitedFolderPaths = getFoldersListFromPath(selectedPath);

		if (!NullUtil.isEmpty(splitedFolderPaths)) {
			for (String folder : splitedFolderPaths) {
				if ("resources".equals(folder)) { //$NON-NLS-1$
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Path에 존재하는 folder들의 목록을 생성한다.
	 * 
	 * @param selectedPath
	 * @return
	 */
	private String[] getFoldersListFromPath(String selectedPath) {
		String[] splitedPath = selectedPath.split("/", 2); //$NON-NLS-1$

		if (splitedPath.length > 1) {
			String folderPath = splitedPath[1];
			return folderPath.split("/"); //$NON-NLS-1$
		} else {
			return new String[0];
		}
	}

	/** BatchJobSelectProjectPage의 Validation Listener */
	Listener validation = new Listener() {

		public void handleEvent(Event event) {

			IProject project = null;
			boolean canFlipToNextPage = false;
			String[] containerClassName = containerNameField.getText().split("/", 2); //$NON-NLS-1$

			for (int i = 0; i < eGovProjects.size(); i++) {
				project = eGovProjects.get(i);

				// class명이 같으면 true
				if (project.getName().equals(containerClassName[0])) {
					// class 명 뒤에 subfolder명이 있으면( / 로 split)비교후 member을 찾을 수 있으면 true
					if (containerClassName.length == 2) {
						if (!NullUtil.isNull(project.findMember(containerClassName[1]))) {
							canFlipToNextPage = true;
							context.setProject((Project) project);
							context.setFilePath(containerNameField.getText());
							break;
						}
					} else {
						// class명만 존재 하면 바로 true
						canFlipToNextPage = true;
						context.setProject((Project) project);
						context.setFilePath(containerNameField.getText());
						break;
					}
				}
			}

			if (!canFlipToNextPage) {
				setErrorMessage(BatchMessages.BatchJobSelectProjectPage_EMPTY_PROJECT_NAME);
				return;
			}

			if (fileNameField.getText().length() < 1) {
				setErrorMessage(BatchMessages.BatchJobSelectProjectPage_EMPTY_FILE_NAME);
				return;
			} else {
				if (project != null && containerClassName.length == 1) {
					if (project.findMember(fileNameField.getText()) != null) {
						setErrorMessage(BatchMessages.BatchJobSelectProjectPage_DUPLICATE_FILE_NAME);
						canFlipToNextPage = false;
					}
				} else if (project != null && containerClassName.length == 2) {
					if (project.findMember(containerClassName[1] + "/" //$NON-NLS-1$
							+ fileNameField.getText()) != null) {
						setErrorMessage(BatchMessages.BatchJobSelectProjectPage_DUPLICATE_FILE_NAME);
						canFlipToNextPage = false;
					}
				}
			}

			if (!validateFileName(fileNameField.getText())) {
				setErrorMessage(BatchMessages.BatchJobSelectProjectPage_INVALID_FILE_NAME);
				canFlipToNextPage = false;
			}

			setPageComplete(canFlipToNextPage);
			if (canFlipToNextPage) {
				setErrorMessage(null);
			}
		}
	};

	/**
	 * File Name의 유효성을 검사한다.
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean validateFileName(String fileName) {
		if (StringUtil.hasKorean(fileName) || StringUtil.hasInvalidClassFileSignal(fileName)
				|| StringUtil.hasEmptySpace(fileName)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <pre>
	 * EgovBatchNature를 가진 Project 또는 그 하위 디렉토리 선택시 Project와 Path정보를 context에 저장하고
	 * project 선택 Page는 넘어간다.
	 * 
	 * <pre>
	 * 
	 * @return boolean
	 */
	private IPath getPackageExplorerSelection() {
		if (!selection.isEmpty()) {
			IPath selectedPath = null;
			Object object = ((IStructuredSelection) selection).getFirstElement();

			if (object instanceof IJavaElement) {
				IJavaElement element = (IJavaElement) object;
				selectedPath = element.getPath();
			} else if (object instanceof Folder) {
				Folder element = (Folder) object;
				selectedPath = element.getFullPath();
			}

			return selectedPath;
		}

		return null;
	}

	/**
	 * 선택한 Item을 TreeViewer에서 찾아서 Select한다.
	 * 
	 * @param selectedPath
	 */
	private void selectTreeViewerItem(IPath selectedPath) {

		if (!NullUtil.isNull(selectedPath)) {
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(selectedPath);
			treeViewer.setSelection(new StructuredSelection(resource));
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			getShell().setMinimumSize(0, 0);
			getShell().setSize(900, 800);

			if (NullUtil.isEmpty(context.getFilePath())) {
				IPath selectedPath = getPackageExplorerSelection();
				selectTreeViewerItem(selectedPath);
			}
		}
		super.setVisible(visible);
	}
}
