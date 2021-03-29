package egovframework.bdev.imp.confmngt.preferences.readwrite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.confmngt.preferences.com.BatchContents;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWProvider;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * 배치 ReaderWriter 생성 컨택스트 클래스
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
public class JobRWTabContents extends BatchContents {

	/**
	 * ItemReaderWriterTabContents의 생성자
	 * 
	 * @param description
	 * @param jobRWInfo
	 * @param preferenceStoreName
	 * @param isCheckBoxTableViewer
	 */
	public JobRWTabContents(String description, JobRWInfo jobRWInfo, String preferenceStoreName, boolean isCheckBoxTableViewer) {
		super(description, jobRWInfo, preferenceStoreName, new JobRWProvider(), isCheckBoxTableViewer);
	}

	public Listener addButtonListener(final Shell parent) {
		
		return new Listener() {

			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();

				final List<String> idList = new ArrayList<String>();

				if (itemCnt > 0)
					for (int i = 0; i < itemCnt; i++) {
						JobRWInfo rwVoInfo = (JobRWInfo) tableViewer
								.getTable().getItem(i).getData();
						idList.add(rwVoInfo.getName());
					}
				
				JobRWInfo voInfo = (JobRWInfo) getInfoClass();
				
				JobRWDialog dialog = null;
				
				if(voInfo instanceof JobReaderInfo){
					dialog = new JobReaderDialog(parent, true, voInfo, idList);
				}else if(voInfo instanceof JobWriterInfo){
					dialog = new JobWriterDialog(parent, true, voInfo, idList);
				}

				if (dialog.open() == Window.OK) {
					if(!NullUtil.isNull(dialog.getJobRWInfo())){
						tableViewer.add(dialog.getJobRWInfo());
					}
				}
			}
		};
	}

	@Override
	public Listener editButtonListener(final Shell parent) {
		return new Listener() {
	
			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();
	
				final List<String> idList = new ArrayList<String>();
	
				if (itemCnt > 0)
					for (int i = 0; i < itemCnt; i++) {
						JobRWInfo rwVoInfo = (JobRWInfo) tableViewer
								.getTable().getItem(i).getData();
						idList.add(rwVoInfo.getName());
					}
	
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
	
				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					Object object = selection.getFirstElement();
					if (object instanceof JobRWInfo) {
						JobRWInfo rwVoInfo = (JobRWInfo) object;
						
						JobRWDialog dialog = null;
						
						if(rwVoInfo instanceof JobReaderInfo){
							dialog = new JobReaderDialog(parent, false, rwVoInfo, idList);
						}else if(rwVoInfo instanceof JobWriterInfo){
							dialog = new JobWriterDialog(parent, false, rwVoInfo, idList);
						}
	
						if (dialog.open() == Window.OK) {	
							if(!NullUtil.isNull(dialog.getJobRWInfo())){
								tableViewer.update(dialog.getJobRWInfo(), null);
							}
						}
					}
				}
			}
		};
	}

}
