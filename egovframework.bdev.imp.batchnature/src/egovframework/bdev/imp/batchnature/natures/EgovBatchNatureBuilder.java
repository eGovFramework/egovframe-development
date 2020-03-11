package egovframework.bdev.imp.batchnature.natures;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;

public class EgovBatchNatureBuilder extends IncrementalProjectBuilder {

    public static final String BUILDER_ID = EgovBatchNaturePlugin.PLUGIN_ID + ".builder";

    @Override
    protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException{
        //do something here
        return null;
    }

    @Override
    protected void clean(IProgressMonitor monitor) throws CoreException{
        //do something here
    }

}
