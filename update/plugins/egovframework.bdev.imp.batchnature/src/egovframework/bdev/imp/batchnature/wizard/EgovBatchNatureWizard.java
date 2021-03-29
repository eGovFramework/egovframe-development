package egovframework.bdev.imp.batchnature.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.service.prefs.BackingStoreException;

import egovframework.bdev.imp.batchnature.EgovBatchNature;
import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;
import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.bdev.imp.batchnature.util.HandlePomXMLFileUtil;
import egovframework.dev.imp.core.utils.NullUtil;

public class EgovBatchNatureWizard extends Wizard {

    public static final String TITLE = "Add eGovFrame Batch Nature";

    private final IProject project;

    private EgovBatchNatureWizardPage page;

    public EgovBatchNatureWizard(IProject project) {
        super();
        this.project = project;
        setWindowTitle(TITLE);
        setHelpAvailable(false);

    }

    @Override
    public void addPages(){
        try {
            page = new EgovBatchNatureWizardPage(project, TITLE);
            addPage(page);
            
            if (!NullUtil.isNull(getContainer())) {
    			getContainer().getShell().setLocation(550, 100);
    		}
            
        } catch (Exception t) {
            BatchNatureLog.logError(t.getMessage(), t);
        }
    }

    @Override
    public boolean performFinish(){

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
        return false;
    }
}
