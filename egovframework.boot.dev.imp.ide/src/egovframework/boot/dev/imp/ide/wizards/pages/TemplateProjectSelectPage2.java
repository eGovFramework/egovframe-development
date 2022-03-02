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

public class TemplateProjectSelectPage2 extends WizardPage{
	
	private final NewProjectContext context;
	
	public TemplateProjectSelectPage2(String pageName, NewProjectContext context){
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
		
		GridData data = new GridData(GridData.FILL_BOTH);
		GridData radio = new GridData(GridData.FILL_BOTH);
		
		Group templateGroup = new Group(parent, SWT.NONE);
		templateGroup.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage7);
		templateGroup.setLayoutData(data);
		templateGroup.setLayout(lay1);
		
		context.setTemplateProjectTopTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage1);
		
		/*Spring Cloud Service Discovery*/
		radioMSA1 = new Button(templateGroup, SWT.RADIO);
		radioMSA1.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage11);
		radioMSA1.setLayoutData(radio);
		radioMSA1.addListener(SWT.MouseDown, listener);
		radioMSA1.setSelection(true);
		
		/* Default setting : Spring Cloud Service Discovery*/
		context.setDefaultExampleFile(ExampleInfo.templateMSAExample1);
		context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage3);
		
		/*Spring Cloud Config*/
		radioMSA2 = new Button(templateGroup, SWT.RADIO);
		radioMSA2.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage13);
		radioMSA2.setLayoutData(radio);
		radioMSA2.addListener(SWT.MouseDown, listener);
		
		/*Spring Cloud Gateway을 생성한다*/
		radioMSA3 = new Button(templateGroup, SWT.RADIO );
		radioMSA3.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage15);
		radioMSA3.setLayoutData(radio);
		radioMSA3.addListener(SWT.MouseDown, listener);
		
		/*Board Service*/
		radioMSA4 = new Button(templateGroup, SWT.RADIO);
		radioMSA4.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage17);
		radioMSA4.setLayoutData(radio);
		radioMSA4.addListener(SWT.MouseDown, listener);
		
		/*Portal Service*/
		radioMSA5 = new Button(templateGroup, SWT.RADIO);
		radioMSA5.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage19);
		radioMSA5.setLayoutData(radio);
		radioMSA5.addListener(SWT.MouseDown, listener);
		
		/*User Service*/
		radioMSA6 = new Button(templateGroup, SWT.RADIO);
		radioMSA6.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage21);
		radioMSA6.setLayoutData(radio);
		radioMSA6.addListener(SWT.MouseDown, listener);
		
		/*Reserve Check Service*/
		radioMSA7 = new Button(templateGroup, SWT.RADIO);
		radioMSA7.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage23);
		radioMSA7.setLayoutData(radio);
		radioMSA7.addListener(SWT.MouseDown, listener);
		
		/*Reserve Item Service*/
		radioMSA8 = new Button(templateGroup, SWT.RADIO);
		radioMSA8.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage25);
		radioMSA8.setLayoutData(radio);
		radioMSA8.addListener(SWT.MouseDown, listener);
		
		/*Reserve Request Service*/
		radioMSA9 = new Button(templateGroup, SWT.RADIO);
		radioMSA9.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage27);
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
		textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage12);
		
	}
	

	/**
	 * 템플릿 선택시 발생하는 이벤트 처리를 한다.
	 */
	Listener listener = new Listener() {
		
		public void handleEvent(Event event) {
			Button button = (Button) event.widget;
			
			if(radioMSA1.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage12);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample1);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage3); //$NON-NLS-1$
				//setTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage2);
			} else if(radioMSA2.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage14);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample2);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage4); //$NON-NLS-1$
			} else if(radioMSA3.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage16);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample3);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage5); //$NON-NLS-1$
			} else if(radioMSA4.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage18);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample4);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage6); //$NON-NLS-1$
			} else if(radioMSA5.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage20);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample5);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage7); //$NON-NLS-1$
			} else if(radioMSA6.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage22);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample6);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage8); //$NON-NLS-1$
			} else if(radioMSA7.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage24);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample7);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage9); //$NON-NLS-1$
			} else if(radioMSA8.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage26);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample8);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage10); //$NON-NLS-1$
			} else if(radioMSA9.equals(button)){
				textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage28);
				context.setDefaultExampleFile(ExampleInfo.templateMSAExample9);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateProjectSelectPage11); //$NON-NLS-1$
			}
		}
	};

}
