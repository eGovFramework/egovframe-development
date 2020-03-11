package egovframework.bdev.imp.confmngt.preferences.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import egovframework.bdev.imp.confmngt.preferences.com.BatchContents;
import egovframework.bdev.imp.confmngt.preferences.com.model.Info;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerInfo;
import egovframework.bdev.imp.confmngt.preferences.listeners.model.ListenerProvider;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * 배치 Listener 생성 컨택스트 클래스
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
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
public class ListenerTabContents extends BatchContents {

	/**
	 * ListenerTabContents의 생성자
	 * 
	 * @param description
	 * @param info
	 * @param preferenceStoreName
	 * @param isCheckBoxTableViewer
	 */
	public ListenerTabContents(String description, Info info,
			String preferenceStoreName, boolean isCheckBoxTableViewer) {
		super(description, info, preferenceStoreName, new ListenerProvider(),
				isCheckBoxTableViewer);
	}

	public Listener addButtonListener(final Shell parent) {

		return new Listener() {

			public void handleEvent(Event event) {
				int itemCnt = tableViewer.getTable().getItemCount();

				final List<String> idList = new ArrayList<String>();

				if (itemCnt > 0)
					for (int i = 0; i < itemCnt; i++) {
						ListenerInfo listenerVoInfo = (ListenerInfo) tableViewer
								.getTable().getItem(i).getData();
						idList.add(listenerVoInfo.getName());
					}

				ListenerInfo voInfo = (ListenerInfo) getInfoClass();
				ListenerDialog dialog = new ListenerDialog(parent, true,
						idList, voInfo, getDescription());

				if (dialog.open() == Window.OK) {
					tableViewer.add(dialog.getListener());
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
						ListenerInfo listenerVoInfo = (ListenerInfo) tableViewer
								.getTable().getItem(i).getData();
						idList.add(listenerVoInfo.getName());
					}

				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();

				if (!NullUtil.isNull(selection) && selection.size() > 0) {
					Object object = selection.getFirstElement();
					if (object instanceof ListenerInfo) {
						ListenerInfo listenerVoInfo = (ListenerInfo) object;
						ListenerDialog dialog = new ListenerDialog(parent,
								false, idList, listenerVoInfo, getDescription());

						if (dialog.open() == Window.OK) {
							tableViewer.update(dialog.getListener(), null);
						}
					}
				}
			}
		};
	}

}
