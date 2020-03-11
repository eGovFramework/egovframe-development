package egovframework.bdev.imp.batchnature.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class GetProjectInfoUtil {

    public static IProject getSelectedProject(ISelection selection){
        IProject currentProject = null;
        if (selection != null && (selection instanceof IStructuredSelection)) {
            IStructuredSelection ss = (IStructuredSelection)selection;
            Object obj = ss.getFirstElement();
            if (obj instanceof IProject)
                currentProject = (IProject)obj;
            if (obj instanceof IJavaProject){
            	currentProject = ((IJavaProject)obj).getProject();
            }
            else {
            	obj.getClass();
            }
                
        }
        return currentProject;
    }

    public static Shell getActiveShell(){
        return getActivePage().getActivePart().getSite().getShell();
    }

    public static IWorkbenchPage getActivePage(){
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }
}
