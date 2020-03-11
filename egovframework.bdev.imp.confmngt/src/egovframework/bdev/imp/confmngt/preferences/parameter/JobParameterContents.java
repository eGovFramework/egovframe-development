package egovframework.bdev.imp.confmngt.preferences.parameter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.confmngt.preferences.com.BatchContents;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterInfo;
import egovframework.bdev.imp.confmngt.preferences.parameter.model.JobParameterLabelProvider;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * 배치 JobParameter 생성 컨택스트 클래스
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
public class JobParameterContents extends BatchContents {

	/**
	 * JobParameterContents의 생성자
	 * 
	 * @param description
	 * @param preferenceStoreName
	 * @param isCheckBoxTableViewer
	 */
	public JobParameterContents(String description, String preferenceStoreName, boolean isCheckBoxTableViewer) {
		super(description, new JobParameterInfo(), preferenceStoreName, new JobParameterLabelProvider(), isCheckBoxTableViewer);
	}
	
	public Listener addButtonListener(final Shell parent) {
		
		return new Listener() {

			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();

				final List<String> idList = new ArrayList<String>();

				for (int i = 0; i < itemCnt; i++) {
					JobParameterInfo jobVoInfo = (JobParameterInfo) tableViewer
							.getTable().getItem(i).getData();
					idList.add(jobVoInfo.getParameterName());
				}

				JobParameterDialog dialog = new JobParameterDialog(parent, true, idList, null);

				if (dialog.open() == Window.OK) {
					tableViewer.add(dialog.getJobParameter());
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
	
				for (int i = 0; i < itemCnt; i++) {
					JobParameterInfo jobVoInfo = (JobParameterInfo) tableViewer
							.getTable().getItem(i).getData();
					idList.add(jobVoInfo.getParameterName());
				}
	
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
	
				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					Object object = selection.getFirstElement();
					if (object instanceof JobParameterInfo) {
						JobParameterInfo jobVoInfo = (JobParameterInfo) object;
						JobParameterDialog dialog = new JobParameterDialog(parent, false, idList, jobVoInfo);
	
						if (dialog.open() == Window.OK) {
							jobVoInfo.copyValues(dialog.getJobParameter());
							tableViewer.update(jobVoInfo, null);
						}
					}
				}
			}
		};
	}

}
