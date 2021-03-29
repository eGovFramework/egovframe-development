package egovframework.bdev.imp.batchnature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.bdev.imp.batchnature.natures.EgovBatchNatureBuilder;

public class EgovBatchNature implements IProjectNature {

	public static String Nature_ID = "egovframework.bdev.imp.ide.natures.egovnature";

	private IProject project;

	public EgovBatchNature() {
		project = null;
	}

	public void configure() throws CoreException {
		addBuilderToProject(project);
		new Job("Adun Builder") {

			protected IStatus run(IProgressMonitor monitor) {
				try {
					project.build(EgovBatchNatureBuilder.INCREMENTAL_BUILD, EgovBatchNatureBuilder.BUILDER_ID, null, monitor);
				} catch (CoreException e) {
					BatchNatureLog.logError(e);
					return e.getStatus();
				}
				return Status.OK_STATUS;
			}
		};
	}

	/**
	 * Remove the nature-specific information here.
	 */
	public void deconfigure() throws CoreException {
		removeBuilderFromProject(project);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject value) {
		project = value;
	}

	private void addBuilderToProject(IProject project) throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] cmds = desc.getBuildSpec();

		for (int idx = 0; cmds != null && idx < cmds.length; idx++) {
			if (EgovBatchNatureBuilder.BUILDER_ID.equals(cmds[idx].getBuilderName())) {
				return;
			}
		}
		ICommand cmd = desc.newCommand();
		cmd.setBuilderName(EgovBatchNatureBuilder.BUILDER_ID);
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(cmd);
		desc.setBuildSpec((ICommand[]) newCmds.toArray(new ICommand[newCmds.size()]));
		project.setDescription(desc, null);
	}

	private void removeBuilderFromProject(IProject project) throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] cmds = desc.getBuildSpec();

		List<ICommand> newCmds = new ArrayList<ICommand>();
		for (int idx = 0; cmds != null && idx < cmds.length; idx++) {
			if (!EgovBatchNatureBuilder.BUILDER_ID.equals(cmds[idx].getBuilderName())) {
				newCmds.add(cmds[idx]);
			}
		}

		desc.setBuildSpec((ICommand[]) newCmds.toArray(new ICommand[newCmds.size()]));

		project.setDescription(desc, null);
	}

}
