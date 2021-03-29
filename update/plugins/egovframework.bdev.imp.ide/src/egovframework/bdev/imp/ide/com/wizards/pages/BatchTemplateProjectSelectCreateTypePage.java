package egovframework.bdev.imp.ide.com.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.ide.EgovBatchIdePlugin;
import egovframework.bdev.imp.ide.common.BatchIdeMessages;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;

/**
 * 배치 프로젝트 생성 타입 선택 마법사 페이지 클래스
 * @author 조용현
 * @since 2012.07.24
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
public class BatchTemplateProjectSelectCreateTypePage extends WizardPage {

	/** 배치 프로젝트 Context */
	private final NewBatchProjectContext context;

	/** DB 선택 Radio Button */
	private Button radioSelectDB;

	/** File 선택 Radio Button */
	private Button radioSelectFile;

	/**
	 * BatchTemplateProjectSelectCreateTypePage의 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public BatchTemplateProjectSelectCreateTypePage(String pageName, NewBatchProjectContext context) {
		super(pageName);
		this.context = context;
		setTitle(BatchIdeMessages.wizardPageBatchJobTemplatePage0);
		setDescription(BatchIdeMessages.BatchTemplateProjectSelectCreateTypePage_DESCRIPTION);
	}

	public void createControl(Composite parent) {

		setPageComplete(false);

		Composite container = new Composite(parent, SWT.NONE);

		GridLayout firstLayout = new GridLayout();
		firstLayout.numColumns = 2;

		container.setLayout(firstLayout);

		createFileTemplate(container);
		createDBTemplate(container);

		radioSelectFile.notifyListeners(SWT.MouseDown, new Event());

		setPageComplete(true);
		setControl(container);
		// 현재 페이지에 대해서 접근성 제공

	}

	/** DB선택 Composite 생성 */
	private void createDBTemplate(Composite parent) {

		GridData gd1 = new GridData();

		GridData empty = new GridData();
		empty.heightHint = 10;
		GridData empty2 = new GridData();
		empty2.widthHint = 350;

		radioSelectDB = new Button(parent, SWT.RADIO);
		radioSelectDB.setText(BatchIdeMessages.BatchTemplateProjectSelectCreateTypePage_DB_RADIO_BUTTON);
		radioSelectDB.setLayoutData(gd1);

		createSeparator(parent);

		Label label1 = new Label(parent, SWT.NONE);
		label1.setImage(EgovBatchIdePlugin.getDefault().getImage(EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_DB));

		Text dbText = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		dbText.setText(BatchIdeMessages.BatchTemplateProjectSelectCreateTypePage_DB_EXPLANATION);

		dbText.setLayoutData(empty2);

		radioSelectDB.addListener(SWT.MouseDown, listener);

	}

	/** File선택 Composite 생성 */
	private void createFileTemplate(Composite parent) {

		GridData gd1 = new GridData();

		GridData empty = new GridData();
		empty.heightHint = 10;
		GridData empty2 = new GridData();
		empty2.widthHint = 350;

		radioSelectFile = new Button(parent, SWT.RADIO);
		radioSelectFile.setText(BatchIdeMessages.BatchTemplateProjectSelectCreateTypePage_FILE_SAM_RADIO_BUTTON);
		radioSelectFile.setLayoutData(gd1);
		radioSelectFile.setSelection(true);

		createSeparator(parent);

		Label label1 = new Label(parent, SWT.NONE);
		label1.setImage(EgovBatchIdePlugin.getDefault().getImage(EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_SAM));

		Text fileText = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);

		fileText.setText(BatchIdeMessages.BatchTemplateProjectSelectCreateTypePage_FILE_SAM_EXPLANATION);

		fileText.setLayoutData(empty2);

		radioSelectFile.addListener(SWT.MouseDown, listener);

	}

	/** 구분선 생성 */
	private void createSeparator(Composite parent) {

		GridData sepa = new GridData(GridData.FILL_HORIZONTAL);

		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(sepa);
	}

	/** DB / File Radio Button 클릭시 Listener */
	Listener listener = new Listener() {

		public void handleEvent(Event event) {
			Button button = (Button) event.widget;

			if (button.equals(radioSelectDB)) {
				context.setCreationType(BatchIdeMessages.wizardPageBatchJobTemplatePage5);
			} else if (button.equals(radioSelectFile)) {
				context.setCreationType(BatchIdeMessages.wizardPageBatchJobTemplatePage3);
			}
		}

	};

	public void setVisible(boolean visible) {
		getShell().setSize(533, 571);
		getShell().setLocation(550, 150);
		super.setVisible(visible);
	};
}
