package egovframework.bdev.imp.confmngt.preferences.readwrite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import egovframework.bdev.imp.confmngt.common.BConfMngtMessages;
import egovframework.bdev.imp.confmngt.preferences.com.BatchPreferencePage;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRW;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRWMap;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.BatchTableColumn;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * Batch ReaderWriter 정보를 관리하는 클래스
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
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
public class JobRWPreferencePage extends BatchPreferencePage {
	
	/** Job Reader Preference Store Name */
	final static public String JOB_READER_PREFERENCE_STORE_NAME = "Reader";
	
	/** Job Writer Preference Store Name */
	final static public String JOB_WRITER_PREFERENCE_STORE_NAME = "Writer";
	
	/** ItemReaderWriterPreferencePage의 생성자 */
	public JobRWPreferencePage() {
		setDescription(BConfMngtMessages.JobRWPreferencePage_DESCRIPTION);
	}

	/** Item Reader Tab Page */
	private JobRWTabContents reader;
	
	/** Item Writer Tab Page */
	private JobRWTabContents writer;
	
	@Override
	public void createInnerPart(Composite parent) {
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.heightHint = 500;
		
		final TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayoutData(gData);
		
		BatchTableColumn[] readerColumns = createReaderColumns();
		BatchTableColumn[] writerColumns = createWriterColumns();

		reader = new JobRWTabContents("Reader", new JobReaderInfo(), JOB_READER_PREFERENCE_STORE_NAME, false); //$NON-NLS-1$ //$NON-NLS-2$
		reader.setTableColumnProperty(readerColumns);
		reader.createTabContents(folder);
		
		writer = new JobRWTabContents("Writer", new JobWriterInfo(), JOB_WRITER_PREFERENCE_STORE_NAME, false); //$NON-NLS-1$ //$NON-NLS-2$
		writer.setTableColumnProperty(writerColumns);
		writer.createTabContents(folder);
		
		// 버튼(add, edit, remove) 생성
		createControlButtons(parent);
		
		Set<String> defaultJobReaderNameList = getDefaultJobReaderNameList();
		addJobRWTableListener(reader.getTableViewer(), defaultJobReaderNameList);
		
		Set<String> defaultJobWriterNameList = getDefaultJobWriterNameList();
		addJobRWTableListener(writer.getTableViewer(), defaultJobWriterNameList);
		
		// tab을 바꿀때마다 해당하는 tab에 대한 Listener를 button에게 설정
		folder.addListener(SWT.Selection, new Listener() {
			
			public void handleEvent(Event event) {
				switch(folder.getSelectionIndex()){
				case 0:
					setButtonListener(reader);
					break;
				case 1:
					setButtonListener(writer);
					break;
				}
			}
		});
		
		folder.notifyListeners(SWT.Selection, new Event());
	}
	
	/**
	 * Job Reader TableViewer의 컬럼 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createReaderColumns(){
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_READER_TABLE_COLUMN_NAME, 100));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_READER_TABLE_COLUMN_RESOURCE_TYPE, 90));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_READER_TABLE_COLUMN_READ_TYPE, 130));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_READER_TABLE_COLUMN_CLASS, 130));
		
		return columns.toArray(new BatchTableColumn[0]);
	}
	
	/**
	 * Job Writer TableViewer의 컬럼 생성
	 * 
	 * @return
	 */
	private BatchTableColumn[] createWriterColumns(){
		ArrayList<BatchTableColumn> columns = new ArrayList<BatchTableColumn>();
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_WRITER_TABLE_COLUMN_NAME, 100));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_WRITER_TABLE_COLUMN_RESOURCE_TYPE, 90));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_WRITER_TABLE_COLUMN_WRITER_TYPE, 130));
		columns.add(new BatchTableColumn(BConfMngtMessages.JobRWPreferencePage_WRITER_TABLE_COLUMN_CLASS, 130));
		
		return columns.toArray(new BatchTableColumn[0]);
	}
	
	/**
	 * Job Reader / Writer TableViewer의 data 클릭시 Edit, Remove버튼 활성화 Listener
	 * 
	 * @param tableViewer
	 * @param defaultJobRWName
	 */
	private void addJobRWTableListener(final TableViewer tableViewer, final Set<String> defaultJobRWName){		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent e) {				
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					JobRWInfo jobRWInfo = (JobRWInfo) selection.getFirstElement();
					String jobRWName = jobRWInfo.getName();
					if(isDefaultJobRWName(jobRWName, defaultJobRWName)){
						editButton.setEnabled(false);
						removeButton.setEnabled(false);
					}else{
						editButton.setEnabled(true);
						removeButton.setEnabled(true);
					}
					
				} else {
					editButton.setEnabled(false);
					removeButton.setEnabled(false);
				}
			}
		});
	}
	
	/**
	 * 기본 Job Reader / Writer Name인지 확인
	 * 
	 * @param jobRWName
	 * @param defaultJobRWName
	 * @return
	 */
	private boolean isDefaultJobRWName(String jobRWName, Set<String> defaultJobRWName){
		if(NullUtil.isEmpty(jobRWName) || NullUtil.isNull(defaultJobRWName)){
			return false;
		}else{
			return defaultJobRWName.contains(jobRWName);
		}
	}
	
	/**
	 * 기본 Job Reader 의 Name을 가져온다.
	 * 
	 * @return
	 */
	private Set<String> getDefaultJobReaderNameList(){
		HashSet<String> disableEditButtonJobRWList = new HashSet<String>();
		
		DefaultJobRWMap defaultjobRW = new DefaultJobRWMap();
		defaultjobRW.resetDefaultReadType();
		disableEditButtonJobRWList.addAll(defaultjobRW.getFileTypeList());
		disableEditButtonJobRWList.addAll(defaultjobRW.getDBTypeList());
		
		disableEditButtonJobRWList.remove(DefaultJobRW.CUSTOMIZE_DB_READER);
		disableEditButtonJobRWList.remove(DefaultJobRW.CUSTOMIZE_FILE_READER);
	
		return disableEditButtonJobRWList;
	}
	
	/**
	 * 기본 Job Writer 의 Name을 가져온다.
	 * 
	 * @return
	 */
	private Set<String> getDefaultJobWriterNameList(){
		HashSet<String> disableEditButtonJobRWList = new HashSet<String>();
		
		DefaultJobRWMap defaultjobRW = new DefaultJobRWMap();
		defaultjobRW.resetDefaultWriterType();
		disableEditButtonJobRWList.addAll(defaultjobRW.getFileTypeList());
		disableEditButtonJobRWList.addAll(defaultjobRW.getDBTypeList());
		
		disableEditButtonJobRWList.remove(DefaultJobRW.CUSTOMIZE_DB_WRITER);
		disableEditButtonJobRWList.remove(DefaultJobRW.CUSTOMIZE_FILE_WRITER);
	
		return disableEditButtonJobRWList;
	}
	
	@Override
	public boolean performOk() {

		reader.saveData();
		writer.saveData();
		
		return super.performOk();
	}
}
