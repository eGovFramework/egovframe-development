/**
 * 
 */
package egovframework.dev.imp.codegen.model.wizard;

import java.util.Iterator;
import java.util.Map;

import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.model.RootModel;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;

import egovframework.dev.imp.codegen.model.EgovModelCodeGenPlugin;
import egovframework.dev.imp.codegen.model.converter.UML2XMIFileImporter;
import egovframework.dev.imp.codegen.model.util.LogUtil;

/**
 *  XMI 파일로 EXPORT 하는 객체  
 * <p><b>NOTE:</b> EA 구현도구에서 작성한 클래스 다이어그램 XMI 파일을 IMPORT 하여 
 *  전자정부프레임워크 모델에서 클래스다이어그램을 확인할 수 있도록 함.
 * @author 운영환경1팀 김연수
 * @since 2010.06.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.06.10  김연수          최초 생성
 *
 * </pre>
 */
public class XMIImportPopupWizard extends Wizard {
    /** 로거 */
    protected final Logger log = Logger.getLogger(XMIImportPopupWizard.class);
    
    /** 소스 모델 */
	private RootModel rootModel;

	/** XMI 파일을 선택하는 페이지 */
	private UML2SelectResourcePage resourcePage;

	/** XMI 파일의 클래스를 확인하는 페이지 */
	private UML2ModelBasedCodeGenWizardPage modelPage;
	
    /** 타겟 프로젝트 */
//    private IJavaProject project;
    
    /** 소스 모델 */
    private Model egovModel;
    
	private CommandStack stack;
    /** 스테레오 타입 클래스 목록 */
//    private Map stereotypeClassList;
    
	/**
	 * 생성자
	 * 
	 * 
	 */
	public XMIImportPopupWizard(RootModel rootModel, CommandStack stack) {
		super();

		this.stack = stack;
		setWindowTitle(EgovModelCodeGenPlugin.getDefault().getResourceString(
				"wizard.window.title"));
		this.rootModel = rootModel;

		// 김연수 수정
		resourcePage = new UML2SelectResourcePage(EgovModelCodeGenPlugin.getDefault()
				.getResourceString("wizard.resource.pagename"));

		// 김연수 수정
		modelPage = new UML2ModelBasedCodeGenWizardPage(EgovModelCodeGenPlugin.getDefault()
				.getResourceString("wizard.model.pagename"));

	}

	/* 
	 * 페이지 추가하기
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 * 
	 */
	public void addPages() {
		super.addPages();
		addPage(resourcePage);
		addPage(modelPage);
	}

	/* 
	 * 위저드 종료 이벤트 처리시 EXPORT 수행
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 * 
	 */
	public boolean performFinish() {
		LogUtil.consoleClear();
		log.info("====================================================================================");
        log.info("eGovFrame XMI ClassDiagrm Converter ...");
		log.info("====================================================================================");
		final UML2XMIFileImporter convertor = new UML2XMIFileImporter();
		egovModel = resourcePage.getModel();
        final EList<Element> objects = egovModel.allOwnedElements();
        Job job = new Job(EgovModelCodeGenPlugin.getDefault().getResourceString("import.job.name")) {

			protected IStatus run(IProgressMonitor monitor) {
		        
				monitor.beginTask(EgovModelCodeGenPlugin.getDefault().getResourceString("import.job.task.name"), objects.size() * 2 + 1);

		        Map<?, ?> stereoName = modelPage.getStereotypeClassList();
		       /* Iterator keys = stereoName.keySet().iterator();

		        while( keys.hasNext() ){
		            String key = (String)keys.next();
		            log.info("===22==stedsfsfsfsdfsfsfs:"+key);
		        }*/
		        /*for( Object key : stereoName.keySet() ){
		        	log.info("=================="+key.toString());
		          }
		        for( Object val : stereoName.values() ){
		        	log.info("=================="+val.toString());
		        	}*/
		        
				for (int i = 0; i < objects.size(); i++) {
					Object object = objects.get(i);

					if (object instanceof NamedElement) {
						// 스테레오 타입 가져오기
						String stName = (String) stereoName.get(object);
						NamedElement elem = (NamedElement) object;
						monitor.subTask(EgovModelCodeGenPlugin.getDefault().getResourceString("import.job.task.convert.node") + elem.getName());
						convertor.convertNodes(elem, stName);
						monitor.worked(1);
					}
				}

				for (int i = 0; i < objects.size(); i++) {
					Object object = objects.get(i);
					if (object instanceof NamedElement) {
						// 스테레오 타입 가져오기

						NamedElement elem = (NamedElement) object;
						String stName = (String) stereoName.get(object);

						monitor.subTask(EgovModelCodeGenPlugin.getDefault().getResourceString("import.job.task.convert.link") + elem.getName());
						convertor.convertLinks(elem, stName);
						monitor.worked(1);

					}
				}

				// 김연수 수정

				// Generalization 구현
				for (int i = 0; i < objects.size(); i++) {
					Object object = objects.get(i);

					if (object instanceof Generalization) {
						// 스테레오 타입 가져오기
						String stName = (String) stereoName.get(object);

						Generalization elem = (Generalization) object;
						monitor.subTask(EgovModelCodeGenPlugin.getDefault().getResourceString("import.job.task.convert.link") + elem.getOwner());
						convertor.convertLinks(elem, stName);
						monitor.worked(1);
					}
				}

				log.info("====================================================================================");
		        log.info("XMI Convter has finished.");
		        log.info("You need to Check above logs");
				log.info("====================================================================================");
				
				monitor.subTask(EgovModelCodeGenPlugin.getDefault().getResourceString(
				"import.job.task.convert.end"));
				Display.getDefault().syncExec(new Runnable() {
		
					public void run() {
						for (Iterator<?> iterator = convertor.getConvertedModel().iterator(); iterator.hasNext();) {
							AbstractUMLModel model = (AbstractUMLModel) iterator.next();
							ImportCommand command = new ImportCommand();
							command.setModel(model);
							XMIImportPopupWizard.this.stack.execute(command);
						}
					}
		
				});
		
				return Status.OK_STATUS;
			}
        };	
		job.setUser(true);
		job.schedule();
		return true;
		
	}
	private class ImportCommand extends Command {

		private AbstractUMLModel model;

		public void setModel(AbstractUMLModel model) {
			this.model = model;
		}

		public void execute() {
			XMIImportPopupWizard.this.rootModel.copyPresentation(model);
			XMIImportPopupWizard.this.rootModel.addChild(model);
		}

		public void undo() {
			XMIImportPopupWizard.this.rootModel.removeChild(model);
		}
	}

}
