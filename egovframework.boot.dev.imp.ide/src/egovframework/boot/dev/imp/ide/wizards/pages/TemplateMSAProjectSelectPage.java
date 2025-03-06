package egovframework.boot.dev.imp.ide.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.boot.dev.imp.ide.common.BootIdeMessages;
import egovframework.boot.dev.imp.ide.wizards.examples.ExampleInfo;
import egovframework.boot.dev.imp.ide.wizards.model.NewProjectContext;

public class TemplateMSAProjectSelectPage extends WizardPage{
	
	private final NewProjectContext context;
	
	public TemplateMSAProjectSelectPage(String pageName, NewProjectContext context){
		super(pageName);
		this.context = context;
		context.setCreateExample(true);
		setTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage1);
		setDescription(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage3);
	}

	private Button radioMSA1 = null;
	private Button radioMSA2 = null;
	private Button radioMSA3 = null;
	private Button radioMSA4 = null;
	private Button radioMSA5 = null;
	private Button radioMSA6 = null;
	private Button radioMSA7 = null;
	private Button radioMSA8 = null;
	private Button radioMSA9 = null;
	private Text textPreview = null;

	
	
	/**
	 * 페이지를 구성한다.
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite (parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		container.setLayout(gridLayout);

		createSelectTemplateControls(container);
		templateDescription(container);
		
		setControl(container);
	}
	
	/**
	 * Template Project를 구성한다.
	 * @param parent
	 */
	private void createSelectTemplateControls(Composite parent) {
		
		
		/* 템플릿 프로젝트 생성 선택*/
		GridLayout lay1 = new GridLayout();
		lay1.numColumns = 2;
		lay1.makeColumnsEqualWidth = true;
		lay1.verticalSpacing = 12;
		
		GridData data = new GridData(GridData.FILL_BOTH);
		GridData radio = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		
		Group templateGroup = new Group(parent, SWT.NONE);
		templateGroup.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage7);
		templateGroup.setLayoutData(data);
		templateGroup.setLayout(lay1);
		
		context.setTemplateProjectTopTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage1);
		
		/*Spring Cloud Service Discovery*/
		radioMSA1 = new Button(templateGroup, SWT.RADIO);
		radioMSA1.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageDiscoveryTitle);
		radioMSA1.setLayoutData(radio);
		radioMSA1.addListener(SWT.MouseDown, listener);
		radioMSA1.setSelection(true);
		
		/* Default setting : Spring Cloud Service Discovery*/
		context.setDefaultExampleFile(ExampleInfo.templateMSAExampleDiscovery);
		context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageDiscovery);
		
		/*Spring Cloud Config*/
		radioMSA2 = new Button(templateGroup, SWT.RADIO);
		radioMSA2.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageConfigTitle);
		radioMSA2.setLayoutData(radio);
		radioMSA2.addListener(SWT.MouseDown, listener);
		
		/*Spring Cloud Gateway을 생성한다*/
		radioMSA3 = new Button(templateGroup, SWT.RADIO );
		radioMSA3.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageGatewayTitle);
		radioMSA3.setLayoutData(radio);
		radioMSA3.addListener(SWT.MouseDown, listener);
		
		/*Board Service*/
		radioMSA4 = new Button(templateGroup, SWT.RADIO);
		radioMSA4.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageBoardTitle);
		radioMSA4.setLayoutData(radio);
		radioMSA4.addListener(SWT.MouseDown, listener);
		
		/*Portal Service*/
		radioMSA5 = new Button(templateGroup, SWT.RADIO);
		radioMSA5.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPagePortalTitle);
		radioMSA5.setLayoutData(radio);
		radioMSA5.addListener(SWT.MouseDown, listener);
		
		/*User Service*/
		radioMSA6 = new Button(templateGroup, SWT.RADIO);
		radioMSA6.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageUserTitle);
		radioMSA6.setLayoutData(radio);
		radioMSA6.addListener(SWT.MouseDown, listener);
		
		/*Reserve Check Service*/
		radioMSA7 = new Button(templateGroup, SWT.RADIO);
		radioMSA7.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveCheckTitle);
		radioMSA7.setLayoutData(radio);
		radioMSA7.addListener(SWT.MouseDown, listener);
		
		/*Reserve Item Service*/
		radioMSA8 = new Button(templateGroup, SWT.RADIO);
		radioMSA8.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveItemTitle);
		radioMSA8.setLayoutData(radio);
		radioMSA8.addListener(SWT.MouseDown, listener);
		
		/*Reserve Request Service*/
		radioMSA9 = new Button(templateGroup, SWT.RADIO);
		radioMSA9.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveRequestTitle);
		radioMSA9.setLayoutData(radio);
		radioMSA9.addListener(SWT.MouseDown, listener);
		
	}
	

	/**
	 * 템플릿 설명을 구성한다.
	 * @param parent
	 */
	private void templateDescription(Composite parent) {

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		gridData.heightHint = 100;

		GridData gridData2 = new GridData(GridData.FILL_BOTH);

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setBackground(parent.getBackground());
		sashForm.setLayoutData(gridData);

		Group descriptionGroup = new Group(sashForm, SWT.NONE);
		descriptionGroup.setLayout(new GridLayout());
		descriptionGroup.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage8);

		textPreview = new Text(descriptionGroup, SWT.BOLD | SWT.READ_ONLY | SWT.MULTI);
		//textPreview.setBackground(parent.getBackground());
		textPreview.setLayoutData(gridData2);
		/* Default setting : Spring Cloud Service Discovery*/
		textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageDiscoveryDesc);
		
	}
	

	/**
	 * 템플릿 선택시 발생하는 이벤트 처리를 한다.
	 */
	Listener listener = new Listener() {
		
		public void handleEvent(Event event) {
			Button button = (Button) event.widget;
			
			if(radioMSA1.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageDiscoveryDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleDiscovery);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageDiscovery); //$NON-NLS-1$
				//setTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage2);
			} else if(radioMSA2.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageConfigDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleConfig);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageConfig); //$NON-NLS-1$
			} else if(radioMSA3.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageGatewayDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleGateway);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageGateway); //$NON-NLS-1$
			} else if(radioMSA4.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageBoardDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleBoardService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageBoard); //$NON-NLS-1$
			} else if(radioMSA5.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPagePortalDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExamplePortalService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPagePortal); //$NON-NLS-1$
			} else if(radioMSA6.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageUserDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleUserService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageUser); //$NON-NLS-1$
			} else if(radioMSA7.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveCheckDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleReserveCheckService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageReserveCheck); //$NON-NLS-1$
			} else if(radioMSA8.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveItemDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleReserveItemService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageReserveItem); //$NON-NLS-1$
			} else if(radioMSA9.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateMSAProjectCreationPageReserveRequestDesc);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExampleReserveRequestService);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateMSAProjectSelectPageReserveRequest); //$NON-NLS-1$
			}
		}
	};

}
