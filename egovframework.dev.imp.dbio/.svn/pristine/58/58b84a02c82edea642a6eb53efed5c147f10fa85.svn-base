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
package egovframework.dev.imp.dbio.editor.parts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.ResourceUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.dialog.MapperSelectionDialog;
import egovframework.dev.imp.dbio.wizard.OpenNewMapperWizard;

/**
 * MapperConfiguration 에디터의 MapperSection 화면
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperConfigurationMapperSection extends ListViewerPart {

	private Button addButton;
	private Button removeButton;
	private Button newButton;
	
	private Element root;
	private FormEditor editor;

	/**
	 * 생성자
	 * 
	 * @param editor
	 */
	public MapperConfigurationMapperSection(FormEditor editor) {
		super("Mapper", null, new ArrayContentProvider(), new MapperElementLabelProvider());
		this.setEditor(editor);
	}
	
	private void setEditor(FormEditor editor) {
		this.editor = editor;
	}

	/**
	 * 섹션 구성
	 */
	@Override
	public Section createContents(IManagedForm managedForm, Composite parent) {
		Section ret = super.createContents(managedForm, parent);
		getViewer().addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				openMapper(event.getSelection());
			}
		});
		return ret;
	}
	
	protected void openMapper(ISelection selection) {
		if (selection.isEmpty()) return;
		IStructuredSelection sselection = (IStructuredSelection) selection;
		Element element = (Element) sselection.getFirstElement();
		String resource = element.getAttribute("resource");
		if (resource != null && resource.length() == 0) return;
		
		IPath resourcePath = new Path(resource);
		IProject project = getProject();
		IFile file = project.getFile(resourcePath);
		if (file.exists()) {
			try {
				IDE.openEditor(editor.getSite().getPage(), file);
				return;
			} catch (CoreException e) {
				DBIOPlugin.getDefault().getLog().log(e.getStatus());
			}
		}
		IJavaProject jproject = (IJavaProject) project.getAdapter(IJavaElement.class);
		if (jproject != null) {
			try {
				for (IPackageFragmentRoot root : jproject.getPackageFragmentRoots()) {
					if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
						IResource rootResource = root.getResource();
						if (rootResource instanceof IContainer) {
							file = ((IContainer) rootResource).getFile(resourcePath);
							if (file.exists()) {
								IDE.openEditor(editor.getSite().getPage(), file);
								return;
							}
						}
					}
				}
			} catch (CoreException e) {
				DBIOPlugin.getDefault().getLog().log(e.getStatus());
			}
		}
		
		MessageDialog.openInformation(editor.getSite().getShell(),
				"Open mapper", "Cannot find " + resource);
	}

	@Override
	protected void createButtons(FormToolkit toolkit, Composite parent) {
		
		addButton = toolkit.createButton(parent, "Add", SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addMapper();
			}
		});

		removeButton = toolkit.createButton(parent, "Remove", SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeMapper();
			}
		});
		
		
		newButton = toolkit.createButton(parent, "New", SWT.NONE);
		newButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newMapper();
			}
		});
	}
	protected void newMapper() {		

		IFile newMapperFile = newMapperFile();
		if (newMapperFile != null) {
			List<Element> newElements = new LinkedList<Element>();
			
			IPath path = makeResourcePath(newMapperFile);
			Element element = root.getOwnerDocument().createElement("mapper"); //$NON-NLS-1$
			element.setAttribute("resource", path.toString()); //$NON-NLS-1$
			root.appendChild(element);
			newElements.add(element);

			getViewer().setInput(getMappers());
			getViewer().setSelection(new StructuredSelection(newElements));
		}
		
	}
	protected void addMapper() {
		IProject project = getProject();
		if (project == null) return;
		
		MapperSelectionDialog dialog = new MapperSelectionDialog(editor.getSite().getShell(), project);
		if (dialog.open() == Window.OK) {
			
			List<Element> newElements = new LinkedList<Element>();
			for (Object result : dialog.getResult()) {
				IFile file = ResourceUtil.getFile(result);
				IPath path = makeResourcePath(file);
				Element element = root.getOwnerDocument().createElement("mapper"); //$NON-NLS-1$
				element.setAttribute("resource", path.toString()); //$NON-NLS-1$
				root.appendChild(element);
				newElements.add(element);
			}
			if (newElements.size() > 0) {
				getViewer().setInput(getMappers());
				getViewer().setSelection(new StructuredSelection(newElements));
			}
		}
	}
	
	private IPath makeResourcePath(IFile file) {
		IJavaElement jelement = (IJavaElement) file.getParent().getAdapter(IJavaElement.class);
		if (jelement != null) {
			if (jelement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
				IPackageFragment pf = (IPackageFragment) jelement;
				return new Path(pf.getElementName().replace('.', '/')).append(file.getName());
			} else if (jelement.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT) {
				return new Path(file.getName());
			} 
		} else {
			String path = file.getProjectRelativePath().toOSString();
			if(path != null && path.indexOf("src\\main\\resources\\")>-1){
				path = path.replace("src\\main\\resources\\", "");
			}
			return new Path(path);
		}
		return file.getProjectRelativePath();
	}

	protected void removeMapper() {
		IStructuredSelection selection = getSelection();
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.getParentNode().removeChild(element);
		}
		getViewer().setInput(getMappers());
	}
	
	/**
	 * Mapper 정보 설정
	 * @param root
	 */
	public void setElement(Element root) {
		this.root = root;
		setInput(getMappers());
		toggleButtons();
	}

	private List<Element> getMappers() {
		List<Element> ret = new LinkedList<Element>();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "mapper".equals(((Element) child).getTagName())) {
				ret.add((Element) child);
			}
		}
		return ret;
	}

	private IProject getProject() {
		IFile file = ResourceUtil.getFile(editor.getEditorInput());
		return file == null ? null : file.getProject();
	}
	
	@Override
	protected void selectionChanged(SelectionChangedEvent event) {
		toggleButtons();
	}
	
	private void toggleButtons() {
		addButton.setEnabled(root != null);
		removeButton.setEnabled(!getSelection().isEmpty());
	}

	private static class MapperElementLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof Element) {
				return ((Element) element).getAttribute("resource"); //$NON-NLS-1$
			}
			return super.getText(element);
		}
	}
	
	private IFile getCurrentFile() {
		return ResourceUtil.getFile(editor.getEditorInput());
	}
	private IFile newMapperFile() {
		IFile file = getCurrentFile();
		if (file == null) return null;
						
		OpenNewMapperWizard action = new OpenNewMapperWizard();
		action.setOpenEditorOnFinish(true);
		action.setSelection(new StructuredSelection(file));
		action.run();
		

		return action.getCreatedFile();
		
	}	
}
