package egovframework.bdev.tst.wizards.pages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.bdev.tst.wizards.views.GenerateTestFileContentProvider;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * Generate Batch Test File Dialog
 * @author 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.10.10	조용현	최초생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class GenerateFileDialog extends StatusDialog {

	/** 기본 Folder Path */
	final private String defaultfolderPath;
	
	/** 선택한 Project */
	private IProject selectProject = null;

	/** 폴더 Path */
	private String folderPath = null;
	
	/** 파일명 */
	private String fileName = null;

	/** 폴더 Path가 입력되는 Text */
	private Text folderPathText = null;
	
	/** File명이 입력되는 Text */
	private Text fileNameText = null;

	/**
	 * folderPath의 값을 가져온다
	 * 
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * fileName의 값을 가져온다
	 * 
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * GenerateFileDialog 생성자
	 * 
	 * @param parent
	 * @param folderPath
	 * @param filePath
	 * @param selectProject
	 */
	public GenerateFileDialog(Shell parent, String folderPath, String filePath,
			IProject selectProject) {
		super(parent);
		setTitle(BatchTestMessages.GenerateFileDialog_TITLE);
		this.folderPath = folderPath;
		this.fileName = filePath;
		this.selectProject = selectProject;
		defaultfolderPath = selectProject.getProject().getName() + "/src/test/java"; //$NON-NLS-1$
	}

	@Override
	protected Point getInitialSize() {
		return new Point(370, 206);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(3, false));
		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		createSelectFolderControl(control);

		createDescriptionControl(control);

		return control;
	}

	/**
	 * Description Control 생성
	 * 
	 * @param control
	 */
	private void createDescriptionControl(Composite control) {

		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalSpan = 3;

		Composite labelControl = new Composite(control, SWT.None);
		labelControl.setLayout(new GridLayout(2, false));
		labelControl.setLayoutData(gData);

		Label descriptionOne = new Label(labelControl, SWT.None);
		descriptionOne.setText(BatchTestMessages.GenerateFileDialog_NOTE);
		StringUtil.setLabelStringBold(descriptionOne);

		Label descriptionTwo = new Label(labelControl, SWT.None);
		descriptionTwo.setText(BatchTestMessages.GenerateFileDialog_NOTE_CONTENTS);
	}

	/**
	 * Select Folder Control 생성
	 * 
	 * @param control
	 */
	private void createSelectFolderControl(Composite control) {
		Label selectFolderLabel = new Label(control, SWT.None);
		selectFolderLabel.setText(BatchTestMessages.GenerateFileDialog_SELECT_FOLDER_LABEL);

		folderPathText = new Text(control, SWT.BORDER);
		folderPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		folderPathText.setText(NullUtil.isEmpty(folderPath) ? defaultfolderPath
				: folderPath);
		folderPathText.addListener(SWT.Modify, validate);

		Button browseSelectButton = new Button(control, SWT.PUSH);
		browseSelectButton.setText(BatchTestMessages.GenerateFileDialog_BROWSE_BUTTON);
		browseSelectButton.addListener(SWT.Selection, browseListener);

		Label fileNameLabel = new Label(control, SWT.None);
		fileNameLabel.setText(BatchTestMessages.GenerateFileDialog_FILE_NAME_LABEL);

		fileNameText = new Text(control, SWT.BORDER);
		fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fileNameText.setText(StringUtil.returnEmptyStringIfNull(fileName));
		fileNameText.addListener(SWT.Modify, validate);
		fileNameText.forceFocus();

		Label emptyLabel = new Label(control, SWT.None);
		emptyLabel.setText(""); //$NON-NLS-1$
	}

	/**
	 * 넘어온 Path에 해당하는 IResouce 를 반환
	 * 
	 * @param folderPath
	 * @return
	 */
	private IResource getSelectResource(String folderPath) {
		IResource resource = null;

		if (!NullUtil.isEmpty(folderPath)) {
			String[] splitedFolderPath = folderPath.split("/", 2); //$NON-NLS-1$

			if (splitedFolderPath.length < 2) {
				if (splitedFolderPath[0].equals(selectProject.getProject().getName())) {
					resource = selectProject;
				}
			} else {
				resource = selectProject.findMember(splitedFolderPath[1]);
			}
		}

		return resource;
	}

	/** Browse 버튼의 Listener */
	Listener browseListener = new Listener() {

		public void handleEvent(Event event) {
			ILabelProvider labelProvider = WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider();
			ITreeContentProvider contentProvider = new GenerateTestFileContentProvider();
			
			BatchTestFolderSelectionDialog dialog = new BatchTestFolderSelectionDialog(getShell(), labelProvider, contentProvider);

			dialog.setComparator(new ViewerComparator());

			dialog.setInput(new IProject[]{selectProject});
			dialog.setTitle(BatchTestMessages.GenerateFileDialog_BROWSE_BUTTON_DIALOG_TITLE);

			String selectFolderPathString = folderPathText.getText();
			IResource selectedElement = getSelectResource(selectFolderPathString);

			if (!NullUtil.isNull(selectedElement)) {
				dialog.setInitialSelection(selectedElement);
			}

			if (dialog.open() == Window.OK) {
				Object object = dialog.getFirstResult();

				if (!NullUtil.isNull(object)) {
					IResource selection = (IResource) object;
					IPath selectionPath = selection.getFullPath();

					folderPathText.setText(selectionPath.makeRelative().toString());
				}
			}
		}
	};

	@Override
	protected void okPressed() {
		folderPath = folderPathText.getText();
		super.okPressed();
	}

	/**
	 * File 명의 유효성 확인
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isFileNameAvailable(String fileName) {
		if (isFileNameHasInvalidCharacter(fileName)
				|| isInvalidFileName(fileName) || StringUtil.isStringStartWithNumber(fileName)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * File명이 사용할 수 없는 String인지 확인
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isInvalidFileName(String fileName) {
		if ("Autowired".equals(fileName) //$NON-NLS-1$
				|| "ContextConfiguration".equals(fileName) //$NON-NLS-1$
				|| "RunWith".equals(fileName) || "Test".equals(fileName)) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		} else {
			return false;
		}
	}

	/**
	 * File명이 사용할 수 없는 문자를 포함하는지 확인
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isFileNameHasInvalidCharacter(String fileName) {
		if (StringUtil.hasInvalidClassFileSignal(fileName)
				|| StringUtil.hasKorean(fileName)
				|| StringUtil.doesStringHasSignal('.', fileName)
				|| StringUtil.hasEmptySpace(fileName)) {
			return true;
		} else {
			return false;
		}
	}

	/** 유효성 검사 */
	Listener validate = new Listener() {

		public void handleEvent(Event event) {
			StatusInfo status = new StatusInfo();

			String folderPath = folderPathText.getText();

			if (NullUtil.isEmpty(folderPath)) {
				status.setError(BatchTestMessages.GenerateFileDialog_EMPTY_FOLDER_PATH);
				updateStatus(status);
				return;
			} else {
				IResource selectedResource = getSelectResource(folderPath);
				if (NullUtil.isNull(selectedResource)) {
					status.setError(BatchTestMessages.GenerateFileDialog_NOT_EXIST_FOLDER);
					updateStatus(status);
					return;
				}
				
				if(!isFolderPathIncludeJavaFolder(folderPath)){
					status.setError(BatchTestMessages.GenerateFileDialog_FOLDER_PATH_MUST_CONTAIN_JAVA);
					updateStatus(status);
					return;
				}
			}

			String inputFileName = fileNameText.getText();
			if (NullUtil.isEmpty(inputFileName)) {
				status.setError(BatchTestMessages.GenerateFileDialog_EMPTY_FILE_NAME);
				updateStatus(status);
				return;
			} else {
				String fileNameWithoutDotJava = getFileNameWithoutDotJava(inputFileName);

				if (!isFileNameAvailable(fileNameWithoutDotJava)) {
					status.setError(BatchTestMessages.GenerateFileDialog_INVALID_FILE_NAME);
					updateStatus(status);
					return;
				} else {
					fileName = fileNameWithoutDotJava + ".java"; //$NON-NLS-1$

					status.setOK();
					updateStatus(status);
					return;
				}
			}
		}

		/** 
		 * FileName에서 .Java 확장자를 제외한 이름만 가져온다.
		 * 
		 * @param fileName
		 * @return
		 */
		private String getFileNameWithoutDotJava(String fileName) {
			String dotJava = ".java"; //$NON-NLS-1$
			int lastIndexOfDotJava = fileName.lastIndexOf(dotJava);

			if (lastIndexOfDotJava > 0) {
				if (lastIndexOfDotJava + dotJava.length() == fileName.length()) {
					return fileName.substring(0, lastIndexOfDotJava);
				}
			}
			return fileName;
		}
		
		/**
		 * FolderPath에서 .Java확장자가 있는지 확인
		 * 
		 * @param folderPath
		 * @return
		 */
		private boolean isFolderPathIncludeJavaFolder(String folderPath){
			String[] folders = folderPath.split("/"); //$NON-NLS-1$
			
			if(!NullUtil.isEmpty(folders)){
				for(String folder : folders){
					if("java".equals(folder)){ //$NON-NLS-1$
						return true;
					}
				}
			}
			
			return false;
		}
	};

}
