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
import egovframework.bdev.imp.ide.com.wizards.EgovNewBatchTemplateProjectWizard;
import egovframework.bdev.imp.ide.common.BatchIdeMessages;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;
import egovframework.bdev.imp.ide.job.wizards.operation.BatchWebProjectCreationOperation;
import egovframework.bdev.imp.ide.scheduler.wizards.operation.CoreProjectCreationOperation;

/**
 * 배치 프로젝트 실행 타입 선택 마법사 페이지 클래스
 * 
 * @author 조용현
 * @since 2012.07.24
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
public class BatchTemplateProjectSelectExecuteTypePage extends WizardPage {

	/** 배치 프로젝트 Context */
	private NewBatchProjectContext context;

	/** Command Line 선택 Radio Button */
	private Button radioSelectCommandLine;

	/** Web 선택 Radio Button */
	private Button radioSelectWeb;

	/** Scheduler 선택 Radio Button */
	private Button radioSelectScheduler;

	/**
	 * BatchTemplateProjectSelectExecuteTypePage의 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public BatchTemplateProjectSelectExecuteTypePage(String pageName, NewBatchProjectContext context) {
		super(pageName);
		this.context = context;
		setTitle(BatchIdeMessages.wizardPageBatchJobTemplatePage0);
		setDescription(BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_DESCRIPTION);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		radioSelectScheduler = createSelectControl(container,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_SCHEDULER_RADIO_BUTTON,
				EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_SCHEDULER,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_SCHEDULER_EXPLANATION);

		radioSelectCommandLine = createSelectControl(container,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_COMMAND_LINE_RADIO_BUTTON,
				EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_COMMANDLINE,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_COMMAND_LINE_EXPLANATION);

		radioSelectWeb = createSelectControl(container,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_WEB_RADIO_BUTTON,
				EgovBatchIdePlugin.IMG_BATCH_TMP_PROJ_WIZ_WEB,
				BatchIdeMessages.BatchTemplateProjectSelectExecuteTypePage_WEB_EXPLANATION);

		radioSelectScheduler.setSelection(true);

		setControl(container);
	}

	/** Scheduler, CommandLine, Web 선택 Radio Button의 Control 생성 */
	private Button createSelectControl(Composite parent, String buttonName, String imageAddr, String explanation) {
		GridData gd1 = new GridData();

		GridData empty = new GridData();
		empty.heightHint = 10;
		GridData empty2 = new GridData();
		empty2.widthHint = 500;

		Button radioButton = new Button(parent, SWT.RADIO);
		radioButton.setText(buttonName);
		radioButton.setLayoutData(gd1);

		createSeparator(parent);

		Label label1 = new Label(parent, SWT.NONE);
		label1.setImage(EgovBatchIdePlugin.getDefault().getImage(imageAddr));

		Text text = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		text.setText(explanation);
		text.setLayoutData(empty2);

		radioButton.addListener(SWT.Selection, selectTypeListener);

		return radioButton;
	}

	/** 구분선 생성 */
	private void createSeparator(Composite parent) {

		GridData sepa = new GridData(GridData.FILL_HORIZONTAL);

		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(sepa);
	}

	/** Radio Button 클릭시 Listener */
	Listener selectTypeListener = new Listener() {

		public void handleEvent(Event event) {
			if (context.getCreationType().equals(BatchIdeMessages.wizardPageBatchJobTemplatePage5)) {
				if (radioSelectCommandLine.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage9);
					settingProjectValue(BatchIdeMessages.DBCommandLineDefaultsrc, BatchIdeMessages.CommandLinePomFile,
							BatchIdeMessages.wizardPageBatchJobDBCommandLineDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new CoreProjectCreationOperation(context));
				} else if (radioSelectWeb.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage11);
					settingProjectValue(BatchIdeMessages.DBWebDefaultsrc, BatchIdeMessages.WebPomFile,
							BatchIdeMessages.wizardPageBatchJobDBWebDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new BatchWebProjectCreationOperation(context));
				} else if (radioSelectScheduler.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage7);
					settingProjectValue(BatchIdeMessages.DBSchedulerDefaultsrc, BatchIdeMessages.SchedulerPomFile,
							BatchIdeMessages.wizardPageBatchJobDBSchedulerDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new CoreProjectCreationOperation(context));
				} else {

				}

			} else if (context.getCreationType().equals(BatchIdeMessages.wizardPageBatchJobTemplatePage3)) {
				if (radioSelectCommandLine.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage9);
					settingProjectValue(BatchIdeMessages.SamCommandLineDefaultsrc, BatchIdeMessages.CommandLinePomFile,
							BatchIdeMessages.wizardPageBatchJobFileCommandLineDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new CoreProjectCreationOperation(context));
				} else if (radioSelectWeb.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage11);
					settingProjectValue(BatchIdeMessages.SamWebDefaultsrc, BatchIdeMessages.WebPomFile,
							BatchIdeMessages.wizardPageBatchJobFileWebDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new BatchWebProjectCreationOperation(context));
				} else if (radioSelectScheduler.getSelection()) {
					context.setExecutionType(BatchIdeMessages.wizardPageBatchJobTemplatePage7);
					settingProjectValue(BatchIdeMessages.SamSchedulerDefaultsrc, BatchIdeMessages.SchedulerPomFile,
							BatchIdeMessages.wizardPageBatchJobFileSchedulerDescription);
					((EgovNewBatchTemplateProjectWizard) getWizard())
							.setOperation(new CoreProjectCreationOperation(context));
				} else {

				}
			}
			setPageComplete(true);

		}
	};

	/**
	 * Page의 입력 정보에 따라 Context에 Example File, Pom File Name과 다음 Page의 Description 설정
	 */
	public void settingProjectValue(String defaultsrc, String pomFile, String templateProjectTitle) {

		context.setDefaultExampleFile(defaultsrc);
		context.setPomFileName(pomFile);
		context.setTemplateProjectDescription(templateProjectTitle);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			selectTypeListener.handleEvent(new Event());
		}
		super.setVisible(visible);
	}
}
