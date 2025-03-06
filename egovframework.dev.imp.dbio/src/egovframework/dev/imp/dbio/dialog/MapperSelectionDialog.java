/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.dev.imp.dbio.dialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.util.FileUtil;
import egovframework.dev.imp.dbio.util.JdtUtil;

/**
 * Mapper Selection Dialog
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일         수정자        수정내용
 *  ----------   --------    ---------------------------
 *  2019.02.25   김효수        MyBatis DBIO
 *  2024.08.13   신용호        isEGovSqlMapperFile 적용
 * 
 * </pre>
 */
public class MapperSelectionDialog extends ElementTreeSelectionDialog {

	/**
	 * 생성자
	 * @param parent
	 * @param project
	 */
	public MapperSelectionDialog(Shell parent, IProject project) {
		super(parent, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		setTitle("Select Mapper file");
		setInput(project);
		setValidator(new ISelectionStatusValidator() {
			public IStatus validate(Object[] selection) {
				if (selection == null || selection.length == 0) {
					return new Status(IStatus.ERROR, DBIOPlugin.PLUGIN_ID, "Selection is empty");
				}
				for (Object selected : selection) {
					IResource resource = ResourceUtil.getResource(selected);
					if (resource == null || resource.getType() != IResource.FILE) {
						return new Status(IStatus.ERROR, DBIOPlugin.PLUGIN_ID, "Selected item is not a file");
					}
				}
				return new Status(IStatus.OK, DBIOPlugin.PLUGIN_ID, "");
			}
		});
		addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				IResource resource = ResourceUtil.getResource(element);
				if (resource == null)
					return false;

				if (resource.getType() == IResource.FILE) {
					return FileUtil.isEGovSqlMapperFile((IFile) resource);
				} else if (resource.getType() == IResource.FOLDER && JdtUtil.isOutputFolder((IFolder) resource)) {
					return false;
				}
				return true;
			}
		});
	}
}
