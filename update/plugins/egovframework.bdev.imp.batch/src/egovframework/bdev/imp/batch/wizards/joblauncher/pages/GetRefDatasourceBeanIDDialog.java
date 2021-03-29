package egovframework.bdev.imp.batch.wizards.joblauncher.pages;

import java.util.LinkedHashMap;

import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.com.FindXMLFileBeanIdValueUtil;
import egovframework.bdev.imp.batch.wizards.joblauncher.model.BatchJobLauncherContext;
import egovframework.bdev.imp.batch.wizards.joblauncher.views.RefDatasourceBeanIDLabelProvider;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Launcher에서 Ref datasource를 사용할 경우 datasourceBeanID 선택
 * 
 * @author 배치개발환경 개발팀
 * @since 2012.09.18
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.09.18	최서윤  최초생성
 *
 * 
 * </pre>
 */
public class GetRefDatasourceBeanIDDialog extends StatusDialog{

	private BatchJobLauncherContext context = null;
	private String refDatasourceBeanID = BatchMessages.GetRefDatasourceBeanIDDialog_0;
	
//	private TableViewer datasourceBeanIDTableViewer;
	
	public String getRefDatasourceBeanID() {
		return refDatasourceBeanID;
	}
	/**
	 * 생성자
	 * 
	 * @param parent
	 * @param context 
	 *
	 */
	public GetRefDatasourceBeanIDDialog(Shell parent, BatchJobLauncherContext context, String refDatasourceBeanID) {
		super(parent);
		
		this.context = context;
		this.refDatasourceBeanID = refDatasourceBeanID;
	}

	/**
	 * dialog 위저드 그리기
	 * 
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(1, false));
		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		createDatasourceBeanID(control);
		
		return control;
	}
	
	/**
	 * dialog 위저드에서 테이블 그리기
	 * 
	 * @param control
	 */
	private void createDatasourceBeanID(Composite control) {
		
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.heightHint = 150;
		gData.widthHint = 220;
		
		final TableViewer datasourceBeanIDTableViewer = new TableViewer(control, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = datasourceBeanIDTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		datasourceBeanIDTableViewer.getControl().setLayoutData(gData);

		String[] columnName = new String[] { BatchMessages.GetRefDatasourceBeanIDDialog_COLUMN_ID, BatchMessages.GetRefDatasourceBeanIDDialog_COLUMN_FILE_NAME };
		int[] columnWidth = new int[] { 130, 200 };
		int[] columnAlignmnet = new int[] { SWT.LEFT, SWT.LEFT };

		for (int i = 0; i < columnName.length; i++) {
			TableColumn column = new TableColumn(table, columnAlignmnet[i]);
			column.setText(columnName[i]);
			column.setWidth(columnWidth[i]);
		}
		datasourceBeanIDTableViewer.setContentProvider(new ArrayContentProvider());
		datasourceBeanIDTableViewer.setLabelProvider(new RefDatasourceBeanIDLabelProvider());
		datasourceBeanIDTableViewer.setInput(getDatasourceBeanID(context));
		datasourceBeanIDTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			
			public void handleEvent(Event event) {
				
				IStructuredSelection selection = (IStructuredSelection) datasourceBeanIDTableViewer.getSelection();
				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					
					refDatasourceBeanID = selection.getFirstElement().toString().split(">")[0]; //$NON-NLS-1$
				}				
			}
		});
		
		
		Composite noteControl = new Composite(control, SWT.None);
		noteControl.setLayout(new GridLayout(2, false));
		noteControl.setLayoutData(new GridData());
		
		Label noteLabel = new Label(noteControl, SWT.None);
		noteLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		noteLabel.setText(BatchMessages.GetRefDatasourceBeanIDDialog_NOTE);
		
		Label contentLabel = new Label(noteControl, SWT.None); 
		contentLabel.setText(BatchMessages.GetRefDatasourceBeanIDDialog_NOTE_CONTENTS);
	}
	
	/**
	 * 테이블 내의 datasourceBeanID와 파일명 정보 구성
	 * @param context 
	 * 
	 * @return listOfDatasourceBeanID
	 */
	private String[] getDatasourceBeanID(BatchJobLauncherContext context){
		
		LinkedHashMap<String, String> beanValueList = FindXMLFileBeanIdValueUtil.findingWantedBeanInXMLFiles(context, "/beans/bean/property", "name", "driverClassName", "id", true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		String[] listOfDatasourceBeanID = new String[beanValueList.keySet().toArray().length];

		for(int i = 0; i < beanValueList.keySet().toArray().length; i++){

			listOfDatasourceBeanID[i] = new String();
			listOfDatasourceBeanID[i] = beanValueList.keySet().toArray()[i]+ ">" + beanValueList.get(beanValueList.keySet().toArray()[i]); //$NON-NLS-1$
		}

		return listOfDatasourceBeanID;
	}
	
}
