package egovframework.bdev.imp.batchnature.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.prefs.BackingStoreException;

import egovframework.bdev.imp.batchnature.EgovBatchNature;
import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;
import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.bdev.imp.batchnature.util.GetProjectInfoUtil;
import egovframework.bdev.imp.batchnature.util.HandlePomXMLFileUtil;


/**
 * 배치 Nature 핸들링용 Menu 서비스 인터페이스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2012.07.02	배치개발환경 개발팀    최초 생성
 *
 * 
 * </pre>
 */
public class EgovBatchNatureAddBatchNatureHandler  extends AbstractHandler {

    /**
     * 메뉴 실행
     * @param event
     * @return
     * @throws ExecutionException
     */
	
	 public Object execute(ExecutionEvent event) throws ExecutionException{
	        ISelection selection = HandlerUtil.getCurrentSelection(event);
	        IProject project = GetProjectInfoUtil.getSelectedProject(selection);
//	        WizardDialog dialog = new WizardDialog(GetProjectInfoUtil.getActiveShell(), new EgovBatchNatureWizard(project));
//	        dialog.setPageSize(400, 300);
//	        dialog.open();
	        
	        //Wizard 없앰 위저드 필요할 경우 위의 주석 풀고 아래 try문 주석화
	        try {
	            IProjectDescription description = project.getDescription();
	            String natures[] = description.getNatureIds();
	            String newNatures[] = new String[natures.length + 1];
	            System.arraycopy(natures, 0, newNatures, 0, natures.length);
	            newNatures[natures.length] = EgovBatchNature.Nature_ID;
	            description.setNatureIds(newNatures);
	            project.setDescription(description, null);
	            new ProjectScope(project).getNode(EgovBatchNaturePlugin.PLUGIN_ID).flush();
	            
	            //eGovBatchNature를 추가하면서 pom.xml 파일 핸들링
	            HandlePomXMLFileUtil.isPomFileExist(project);
	            
	            return true;
	        } catch (BackingStoreException e) {
	        	BatchNatureLog.logError(e);
	            // fail store property for nature because of locking.. or something bad.
	        } catch (Exception e) {
	        	BatchNatureLog.logError(e);
	        }
	        
	        return null;
	    }
	
	
}