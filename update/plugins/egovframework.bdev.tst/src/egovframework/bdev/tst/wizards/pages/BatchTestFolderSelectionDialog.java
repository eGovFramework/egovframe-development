package egovframework.bdev.tst.wizards.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.eclipse.ui.views.navigator.ResourceComparator;

import egovframework.bdev.tst.common.BatchTestMessages;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * Generate Batch Test File Dialog에서 프로젝트 내 Resource 선택 Dialog
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
public class BatchTestFolderSelectionDialog extends ElementTreeSelectionDialog implements ISelectionChangedListener {

	/** New Folder Button */
	private Button fNewFolderButton;
	
	private IContainer fSelectedContainer;

	/**
	 * BatchTestFolderSelectionDialog 생성자
	 * 
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public BatchTestFolderSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
		super(parent, labelProvider, contentProvider);
		setComparator(new ResourceComparator(ResourceComparator.NAME));
	}

	protected Control createDialogArea(Composite parent) {
		Composite result= (Composite)super.createDialogArea(parent);

		getTreeViewer().addSelectionChangedListener(this);

		Button button = new Button(result, SWT.PUSH);
		button.setText(BatchTestMessages.BatchTestFolderSelectionDialog_NEW_FOLDER_BUTTON);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				newFolderButtonPressed();
			}
		});
		button.setFont(parent.getFont());
		fNewFolderButton= button;

		applyDialogFont(result);
		
		return result;
	}

	/** New Folder Button 상태를 갱신 */
	private void updateNewFolderButtonState() {
		IStructuredSelection selection= (IStructuredSelection) getTreeViewer().getSelection();
		fSelectedContainer= null;
		if (selection.size() == 1) {
			Object first= selection.getFirstElement();
			if (first instanceof IContainer) {
				fSelectedContainer= (IContainer) first;
			}
		}
		
		if(isPathIncludeResourceFolder(fSelectedContainer)){
			fNewFolderButton.setEnabled(true);
		}else{
			fNewFolderButton.setEnabled(false);
		}
		
	}
	
	/**
	 * 해당 Path가 resouce folder를 포함하는지 여부 확인
	 * 
	 * @param container
	 * @return
	 */
	private boolean isPathIncludeResourceFolder(IContainer container){	
		String[] splitedFolderPaths = getFoldersListFromPath(container);
		
		if(!NullUtil.isEmpty(splitedFolderPaths)){
			for(String folder : splitedFolderPaths){
				if("java".equals(folder)){ //$NON-NLS-1$
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Path에서 폴더 리스트를 가져온다.
	 * 
	 * @param container
	 * @return
	 */
	private String[] getFoldersListFromPath(IContainer container){
		
		if(NullUtil.isNull(container)){
			return new String[0];
		}
		
		String selectedPath = container.getFullPath().makeRelative().toString();
		String[] splitedPath = selectedPath.split("/",2); //$NON-NLS-1$
		
		if(splitedPath.length > 1){
			String folderPath = splitedPath[1];			
			return folderPath.split("/"); //$NON-NLS-1$
		}else{
			return new String[0];
		}
	}

	/** New Folder Button 이 선택되었을 때 동작 */
	protected void newFolderButtonPressed() {
		NewFolderDialog dialog= new NewFolderDialog(getShell(), fSelectedContainer);
		if (dialog.open() == Window.OK) {
			TreeViewer treeViewer= getTreeViewer();
			treeViewer.refresh(fSelectedContainer);
			Object createdFolder= dialog.getResult()[0];
			treeViewer.reveal(createdFolder);
			treeViewer.setSelection(new StructuredSelection(createdFolder));
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {
		updateNewFolderButtonState();
	}

}
