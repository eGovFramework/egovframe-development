package egovframework.mdev.imp.ide.wizards.pages;


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

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeMessages;
import egovframework.mdev.imp.ide.wizards.examples.MobileExampleInfo;
import egovframework.mdev.imp.ide.wizards.model.NewMobileProjectContext;

/**
 * Moblie eGovFramework 템플릿 프로젝트 선택 마법사 페이지 클래스
 * @author 이종대
 * @since 2011.07.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2011.07.13  	이종대          최초 생성
 *	2013.12.20  	이기하          mobile all-in-one 템플릿 프로젝트 추가(기존 템플릿 삭제)
 *
 * 
 * </pre>
 */
public class MobileTemplateProjectSelectPage extends WizardPage{
	
	// 모바일 프로젝트 컨텍스트
	private final NewMobileProjectContext context;
	
	
	// 모바일 템플릿 프로젝트 선택 페이지
	public MobileTemplateProjectSelectPage(String pageName, NewMobileProjectContext context){
		super(pageName);
		this.context = context;
		context.setCreateExample(true);
		setTitle(MoblieIdeMessages.wizardsPagesTemplateMProjectCreationPage0);
		setDescription(MoblieIdeMessages.wizardsPagesTemplateMProjectCreationPage1);
	}
	
	private Button radioAllInOne = null;		// 위젯 버튼
	private Text textPreview = null;			// 위젯 텍스트
	
	private ImageData imageData = null;			// 위젯 이미지 데이터
	private Label label = null;					// 위젯 레이블
	private Image image = null;					// 위젯 이미지

	/**
	 * 템플릿 페이지를 구성한다.
	 */
	public void createControl(Composite parent) {
		
		Composite container = new Composite (parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		
		container.setLayout(gridLayout);
		allInOneTemplate(container);
		setControl(container);
	}
	
	/**
	 * all-in-one 뷰를 생성한다.
	 * @param parent
	 */
	private void allInOneTemplate(Composite parent) {
		
		GridData simple = new GridData();
		
		GridData empty = new GridData();
		empty.heightHint = 10;
		GridData empty2 = new GridData();
		empty2.widthHint = 350;
		GridData empty3 = new GridData();
		
		radioAllInOne = new Button(parent, SWT.RADIO);
		radioAllInOne.setText(MoblieIdeMessages.wizardsPagesTemplateMProjectCreationPage11);
		radioAllInOne.setLayoutData(simple);
		radioAllInOne.setSelection(true);
		
		createSeparator(parent);
		
		setImage(parent, empty3, "icons/big_usersupport.png"); //$NON-NLS-1$
		
		textPreview = new Text(parent, SWT.MULTI | SWT.WRAP| SWT.READ_ONLY);
		textPreview.setText(MoblieIdeMessages.wizardsPagesTemplateMProjectCreationPage12);
		textPreview.setLayoutData(empty2);
		
		radioAllInOne.addListener(SWT.Selection, listener);
		
		/**
		 * Default setting : all-in-one template
		 */
		context.setDefaultExampleFile(MobileExampleInfo.templateAllInOneExample);
		//context.setPomFileName(MobileExampleInfo.allInOnePomFile);
		context.setTemplateProjectTitle(MoblieIdeMessages.MobileTemplateProjectSelectPage4); //$NON-NLS-1$
		
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
			URL url = FileLocator.toFileURL(EgovMobileIdePlugin.getDefault().getBundle().getEntry(path));
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
	 * 세퍼레이터를 생성한다.
	 * @param parent
	 */
	private void createSeparator(Composite parent) {
		
		GridData sepa = new GridData(GridData.FILL_HORIZONTAL);
		
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(sepa);
		
	}
	
	
	/**
	 * 리스너 클래스
	 */
	Listener listener = new Listener() {
		
		public void handleEvent(Event event) {
			context.setDefaultExampleFile(MobileExampleInfo.templateAllInOneExample);
			//context.setPomFileName(MobileExampleInfo.allInOnePomFile);
			context.setTemplateProjectTitle(MoblieIdeMessages.MobileTemplateProjectSelectPage4); //$NON-NLS-1$
		}
	};

}
