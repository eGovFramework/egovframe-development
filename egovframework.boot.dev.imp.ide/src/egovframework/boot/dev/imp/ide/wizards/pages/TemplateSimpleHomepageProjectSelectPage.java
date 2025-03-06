package egovframework.boot.dev.imp.ide.wizards.pages;


import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.boot.dev.imp.ide.EgovBootIdePlugin;
import egovframework.boot.dev.imp.ide.common.BootIdeMessages;
import egovframework.boot.dev.imp.ide.wizards.examples.ExampleInfo;
import egovframework.boot.dev.imp.ide.wizards.model.NewProjectContext;

public class TemplateSimpleHomepageProjectSelectPage extends WizardPage{
	
	private final NewProjectContext context;
	
	public TemplateSimpleHomepageProjectSelectPage(String pageName, NewProjectContext context){
		super(pageName);
		this.context = context;
		context.setCreateExample(true);
		setTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage0);
		setDescription(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage2);
	}
	
	
	private Button radioSimpleHomepage = null;
	private Text textPreview = null;
	private ImageData imageData = null;
	private Label label = null;
	private Image image = null;
	
	
	/**
	 * 페이지를 구성한다.
	 */
	public void createControl(Composite parent) {
		
		Composite container = new Composite (parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		container.setLayout(gridLayout);
		createSimpleTemplate(container);
		setControl(container);
	}
	
	
	/**
	 * simple homepage view를 구성한다.
	 * @param parent
	 */
	private void createSimpleTemplate(Composite parent) {
		
		GridData simple = new GridData();
		
		GridData empty = new GridData();
		empty.heightHint = 10;
		GridData empty2 = new GridData();
		empty2.widthHint = 350;
		GridData empty3 = new GridData();
		
		radioSimpleHomepage = new Button(parent, SWT.RADIO);
		radioSimpleHomepage.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage9);
		radioSimpleHomepage.setLayoutData(simple);
		radioSimpleHomepage.setSelection(true);
		
		createSeparator(parent);
		
		setImage(parent, empty3, "icons/big_boothomepage.png"); //$NON-NLS-1$
		
		textPreview = new Text(parent, SWT.MULTI | SWT.WRAP| SWT.READ_ONLY);
		textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage10);
		textPreview.setLayoutData(empty2);
		
		
		radioSimpleHomepage.addListener(SWT.MouseDown, listener);
		
		/**
		 * Default setting : simple homepage site
		 */
		context.setDefaultExampleFile(ExampleInfo.templateSimpleExample);
		//context.setPomFileName(ExampleInfo.simplePomFile);
		context.setTemplateProjectTitle(BootIdeMessages.bootTemplateSimpleHomeProjectSelectPage);
		context.setTemplateProjectTopTitle(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage0);
		
	}
		
	/**
	 * 이미지를 해당 그리드데이터에 삽입한다.
	 * @param parent 부모 컴포지트
	 * @param data 삽입하려는 위치의 그리드데이터
	 * @param path 이미지 경로
	 */
	public void setImage(Composite parent, GridData data, String path) {
		
		label = new Label(parent, SWT.NONE);
		
		try {
			URL url = FileLocator.toFileURL(EgovBootIdePlugin.getDefault().getBundle().getEntry(path));
			URL resolvedUrl = FileLocator.resolve(url);
			imageData = new ImageData(new FileInputStream(new File(resolvedUrl.getFile())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		image = new Image(Display.getCurrent(), imageData);
		label.setImage(image);
		label.pack();
		label.setLayoutData(data);
	}
	
	
	/**
	 * 세퍼레이트를 생성한다.
	 * @param parent
	 */
	private void createSeparator(Composite parent) {
		
		GridData sepa = new GridData(GridData.FILL_HORIZONTAL);
		
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(sepa);
		
	}
	
	
	/**
	 * 템플릿 선택시 발생하는 이벤트 처리를 한다.
	 */
	Listener listener = new Listener() {
		
		public void handleEvent(Event event) {
			Button button = (Button) event.widget;

			if(radioSimpleHomepage.equals(button)){
				//textPreview.setText(BootIdeMessages.wizardspagesBootTemplateProjectCreationPage5);
				context.setDefaultExampleFile(ExampleInfo.templateSimpleExample);
				//context.setPomFileName(ExampleInfo.simplePomFile);
				context.setTemplateProjectTitle(BootIdeMessages.bootTemplateSimpleHomeProjectSelectPage); //$NON-NLS-1$
			}
		}
	};

}
