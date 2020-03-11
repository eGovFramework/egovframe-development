package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.BinaryType;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.TypeCSqlKeyValueVo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * <pre>
 * Job Reader / Writer Detail Info를 생성
 * [추상 클래스]
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.10.10	조용현	최초생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
abstract public class JobRWDetailInfoControlConstructor {
	
	/** 상세 정보 입력 Control 이 생성되는 Composite */
	final protected ScrolledComposite detailInfoControl;
	
	/** Error 정보가 Dialog로 넘겨주는 Label*/
	final protected Label errorSettingLabel;
	
	/** Wizard의 BatchJobCreationContext */
	protected BatchJobCreationContext context = null;
	
	private Map<String, String> preSavedDetailContext = null;

	/** 상세 정보가 저장되는 Context*/
	protected Map<String, String> detailContext = new HashMap<String, String>();

	private boolean isPreviousSelectedJobRW;
	
	/**
	 * JobRWDetailInfoControlConstructor의 생성자
	 * 
	 * @param detailInfoControl
	 * @param errorSettingLabel
	 * @param context
	 * @param detailContext
	 *
	 */
	public JobRWDetailInfoControlConstructor(ScrolledComposite detailInfoControl,
			Label errorSettingLabel, BatchJobCreationContext context, Map<String, String> detailContext, boolean isPreviousSelectedJobRW) {
		this.detailInfoControl = detailInfoControl;
		this.errorSettingLabel = errorSettingLabel;
		this.context = context;
		this.preSavedDetailContext = detailContext;
		this.isPreviousSelectedJobRW = isPreviousSelectedJobRW;
	}

	/**
	 * Detail Info Control을 생성한다.
	 * 
	 * @param jobRWInfo
	 */
	@SuppressWarnings("unused")
	public void createDetailInfoControl(JobRWInfo jobRWInfo) {
		String resourceDetailType = jobRWInfo.getResourceDetailType();

		clearDetailInfoControl();
		
		Composite control = createColumnControl();

		createDetailInfoContents(control, jobRWInfo);
		
		detailInfoControl.setMinSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		detailInfoControl.getParent().layout(true, true);
	}

	/**
	 * 실제 항목을 생성한다.
	 * 
	 * @param resourceDetailType
	 */
	abstract protected void createDetailInfoContents(Composite control, JobRWInfo jobRWInfo);

	/**
	 * 기본 Composite을 생성한다.
	 * 
	 * @param columnNumber
	 * @return
	 */
	private Composite createColumnControl() {		
		Composite control = new Composite(detailInfoControl, SWT.None);
		control.setLayout(new GridLayout(3, false));
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		control.setSize(440, SWT.DEFAULT);
		
		detailInfoControl.setContent(control);

		return control;
	}

	/**
	 * Detail Info Control을 초기화(아무 항목도 없는 상태)한다.
	 * 
	 */
	protected void clearDetailInfoControl() {
		Control[] children = detailInfoControl.getChildren();

		if (!NullUtil.isEmpty(children)) {
			for (Control child : children) {
				child.dispose();
			}
		}
	}

	/**
	 * Browse Button을 생성하고 Text를 입력한다.
	 * 
	 * @param control
	 * @return
	 */
	protected Button createBrowseButton(Composite control) {
		Button button = new Button(control, SWT.PUSH);
		button.setText(BatchMessages.JobRWDetailInfoControlConstructor_BROWSE_BUTTON);

		return button;
	}

	/**
	 * <pre>
	 * Label과 Text를 생성하여 Text를 리턴한다.
	 * Label에는 넘겨진 String 값으로 설정한다.
	 * </pre>
	 * @param control
	 * @param labelContent
	 * @return
	 */
	protected Text createLabelText(Composite control, String labelContent) {
		Label label = new Label(control, SWT.None);
		label.setText(labelContent);

		Text text = new Text(control, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return text;
	}

	/**
	 * <pre>
	 * Text의 GridData를 FILL_HORIZONTAL 로 설정하고
	 * 가로로 넘겨진 숫자만큼 합친(span)다.
	 * </pre>
	 * 
	 * @param text
	 * @param spanNumber
	 */
	protected void setTextHorizontalFullAndSpan(Text text, int spanNumber) {
		if (!NullUtil.isNull(text)) {
			GridData gData = new GridData(GridData.FILL_HORIZONTAL);
			gData.horizontalSpan = spanNumber;

			text.setLayoutData(gData);
		}
	}

	/**
	 * <pre>
	 * Text에 입력된 값을 넘겨진 String을 키로 설정하여
	 * context(Map)에 입력하는 Listener를 추가한다.
	 * </pre>
	 * 
	 * @param item
	 * @param text
	 */
	protected void addSaveValueToContextListener(final String item,
			final Text text) {
		text.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				saveValueToContext(item, text.getText());
			}
		});
	}
	
	
	/**
	 * <pre>
	 * Text에 입력된 값을 넘겨진 String을 키로 설정하여
	 * context(Map)에 입력한다.
	 * </pre>
	 * 
	 * @param item
	 * @param text
	 */
	private void saveValueToContext(String item, String value) {
		detailContext.put(item, value);
	}
	
	/**
	 * <pre>
	 * Combo에 입력된 값을 넘겨진 String을 키로 설정하여
	 * context(Map)에 입력하는 Listener를 추가한다.
	 * </pre>
	 * 
	 * @param item
	 * @param combo
	 */
	protected void addSaveValueToContextListener(final String item,
			final Combo combo) {
		combo.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				saveValueToContext(item, combo.getText());
			}
		});
	}
	
	/**
	 * Context(Map)에 입력된 값을 Text에 입력한다.
	 * 
	 * @param item
	 * @param text
	 */
	protected void setInitialValueToText(String item, Text text){
		if(isPreviousSelectedJobRW){
			String value = preSavedDetailContext.get(item);
			value = StringUtil.returnEmptyStringIfNull(value);
			
			text.setText(value);
		}
	}

	/**
	 * Context(Map)에 입력된 값을 Combo에 입력한다.
	 * 
	 * @param item
	 * @param combo
	 */
	protected void setInitialValueToCombo(String item, Combo combo){
		if(isPreviousSelectedJobRW){
			String value = preSavedDetailContext.get(item);
			value = StringUtil.returnEmptyStringIfNull(value);
			
			combo.setText(value);
		}
	}

	/**
	 * Error 메세지를 설정한다.
	 * 
	 * @param message
	 */
	protected void setMessage(String message) {
		String errorMessage = StringUtil.returnEmptyStringIfNull(message);

		errorSettingLabel.setText(errorMessage);
		errorSettingLabel.notifyListeners(SWT.Modify, null);
	}
	
	/**
	 * errorSettingLabel에 null값을 넘겨 에러가 없음을 표시한다.
	 */
	protected void setMessageOK(){
		setMessage(null);
	}

	/**
	 * 클래스 정보 search기능
	 * 
	 */
	protected Listener getClassSearchBrowseButtonListener(final Text text) {
		return new Listener() {

			public void handleEvent(Event event) {

				FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getShell(), false, PlatformUI.getWorkbench()
								.getProgressService(),
						SearchEngine.createWorkspaceScope(),
						IJavaSearchConstants.CLASS);
				dialog.setTitle(BatchMessages.JobRWDetailInfoControlConstructor_BROWSE_DIALOG_TITLE);
				dialog.setMessage(BatchMessages.JobRWDetailInfoControlConstructor_BROWSE_DIALOG_DESCRIPTION);
				dialog.setInitialPattern("");

				if (dialog.open() == Window.OK) {
					Object type = dialog.getFirstResult();

					if (!NullUtil.isNull(type)) {
						if (type instanceof BinaryType) {
							BinaryType binaryType = (BinaryType) type;
							text.setText(binaryType.getFullyQualifiedName());
						} else if (type instanceof SourceType) {
							SourceType sourceType = (SourceType) type;
							text.setText(sourceType.getFullyQualifiedName());
						}
					}
				}

			}
		};
	}

	/**
	 * Detail info의 Context값을 가져온다.
	 * 
	 * @return
	 */
	public Map<String, String> getDetailContext() {
		return detailContext;
	}

	/**
	 * SQL Table의 Key, Value값을 가져온다.
	 * 
	 * @return
	 */
	public List<TypeCSqlKeyValueVo> getSqlKeyValueList() {
		return new ArrayList<TypeCSqlKeyValueVo>();
	}

	/**
	 * FileDilog Browse Button에 추가한 Listener를 생성한다.ㄴ
	 * 
	 * @param control
	 * @param text
	 * @return
	 */
	protected Listener getFileDialogButtonListener(final Composite control, final Text text, final String[] filterExt) {
		return new Listener() {

			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(
						control.getShell(), SWT.OPEN);
				fileDialog
						.setText(BatchMessages.StepInfoContentsConstructor_RESOURCE_BUTTON_DIALOG_TITLE);
				fileDialog.setFilterPath(context.getProject().getLocation().toOSString());
				fileDialog.setFilterExtensions(filterExt);

				String selectedResource = fileDialog.open();
				if (NullUtil.isEmpty(selectedResource)) {
					selectedResource = StringUtil
							.returnEmptyStringIfNull(text.getText());
				} else {
					String[] extensions = getExtensions(filterExt);

					selectedResource = selectedResource.replace("\\", "/");

					if (!hasExtension(selectedResource, extensions)) {
						int extensionIndex = fileDialog.getFilterIndex();
						
						String extension = extensions[extensionIndex];

						selectedResource += "." + extension;
					}
				}

				text.setText(selectedResource);
			}
			
			/**
			 * File Dialog에 넘겨준 Filter Extension에서 dot(.)이후의 String을 가져온다.
			 * 
			 * @param filteredExtensions
			 * @return
			 */
			private String[] getExtensions(String[] filteredExtensions) {
				ArrayList<String> extensions = new ArrayList<String>();

				if (!NullUtil.isEmpty(filteredExtensions)) {
					for (String filteredExtension : filteredExtensions) {
						int dotIndex = filteredExtension.lastIndexOf(".");

						// . 이후의 확장자를 가져오기 위해 1을 더한다.
						String extension = filteredExtension.substring(dotIndex + 1);

						extensions.add(extension);
					}
				}

				return extensions.toArray(new String[0]);
			}
			
			/**
			 * 해당 확장자를 가지고 있는지 확인한다.
			 * 
			 * @param resourceName
			 * @param extensions
			 * @return
			 */
			private boolean hasExtension(String resourceName, String[] extensions) {
				if (!NullUtil.isEmpty(extensions)) {

					for (String extension : extensions) {
						if (resourceName.endsWith(extension)) {
							return true;
						}

					}
				}

				return false;
			}
		};
	}

}
