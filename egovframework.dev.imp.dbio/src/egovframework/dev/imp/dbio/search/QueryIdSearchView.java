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
package egovframework.dev.imp.dbio.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapCRUDElement;

/**
 * QueryId SearchView 화면
 * 
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 *      </pre>
 */
public class QueryIdSearchView extends ViewPart implements SearchQueryId {

	private ComboViewer projectViewer;
	private Text queryInput;
	private TreeViewer resultViewer;

	private QueryIdSearchJob job = null;
	private Button searchButton;

	/**
	 * QueryIdSearchView 화면 구성
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);

		createSearchQueryPart(parent);
		createSearchResultPart(parent);
	}

	private void createSearchResultPart(Composite parent) {
		resultViewer = new TreeViewer(parent);
		resultViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		resultViewer.setContentProvider(new TreeNodeContentProvider());
		resultViewer.setLabelProvider(new ISearchResultLabelProvider()); // resultViewer.setLabelProvider(new
																			// IBatisSearchResultLabelProvider());
		resultViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection().isEmpty())
					return;
				openEditor(((IStructuredSelection) event.getSelection()).getFirstElement());
			}
		});
	}

	private void createSearchQueryPart(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(6, false));

		Label label = new Label(composite, SWT.NONE);
		label.setText("Project:");

		GridData gd = new GridData();
		gd.widthHint = 300;

		projectViewer = new ComboViewer(composite, SWT.READ_ONLY | SWT.DROP_DOWN);
		projectViewer.setContentProvider(new WorkbenchContentProvider());
		projectViewer.setLabelProvider(new WorkbenchLabelProvider());
		projectViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		projectViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				toggleSearchButton();
			}
		});

		projectViewer.getCombo().setLayoutData(gd);
		projectViewer.getCombo().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				refreshProjectsList();
			}
		});
		projectViewer.setFilters(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					if (!project.isOpen()) {
						return false;
					}
				}
				return true;
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setText("Query ID:");

		queryInput = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		queryInput.setLayoutData(gd);
		queryInput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				toggleSearchButton();
			}
		});

		searchButton = new Button(composite, SWT.NONE);
		searchButton.setText("Search");
		searchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});

		toggleSearchButton();
	}

	/**
	 * Search 버튼 활성 여부 설정
	 */
	private void toggleSearchButton() {
		if (projectViewer.getSelection().isEmpty()) {
			searchButton.setEnabled(false);
			return;
		}

		if (queryInput.getText().trim().length() == 0) {
			searchButton.setEnabled(false);
			return;
		}

		searchButton.setEnabled(true);
	}

	/**
	 * 프로젝트 리스트 새로고침
	 */
	private void refreshProjectsList() {
		projectViewer.refresh();
	}

	protected void openEditor(Object obj) {
		if (obj instanceof IFileTreeNode) {
			IFile file = ((IFileTreeNode) obj).getFile();
			try {
				IDE.openEditor(getSite().getPage(), file);
			} catch (PartInitException e) {
				DBIOPlugin.getDefault().getLog().log(e.getStatus());
			}
		}
	}

	/**
	 * 검색 실행
	 */
	protected void search() {
		if (job != null) {
			job.cancel();
		} else {
			job = new QueryIdSearchJob(this);
		}

		IStructuredSelection selection = (IStructuredSelection) projectViewer.getSelection();
		if (selection.isEmpty())
			return;

		IResource resource = ResourceUtil.getResource(selection.getFirstElement());

		job.setTaragetProject(resource.getProject());
		job.setQuery(queryInput.getText());
		job.schedule();
	}

	@Override
	public void setFocus() {
		// DbioLog.logInfo("sham");
	}

	@Override
	public void dispose() {
		if (job != null) {
			job.cancel();
		}
		super.dispose();
	}

	public void setResult(Object result) {
		resultViewer.setInput(result);
	}

	/*
	 * private static class IBatisSearchResultLabelProvider extends LabelProvider {
	 * 
	 * @Override public String getText(Object element) { if (element instanceof
	 * FileTreeNode) { return ((FileTreeNode)
	 * element).getFile().getProjectRelativePath().toString(); } else if (element
	 * instanceof SqlMapElementNode) { SqlMapCRUDElement crudElement =
	 * ((SqlMapElementNode) element).getElement(); StringBuffer buf = new
	 * StringBuffer("ID="); buf.append(crudElement.getId()); return buf.toString();
	 * } else { return super.getText(element); } }
	 * 
	 * }
	 */
	private static class ISearchResultLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof FileTreeNode) {
				return ((FileTreeNode) element).getFile().getProjectRelativePath().toString();
			} else if (element instanceof SqlMapElementNode) {
				SqlMapCRUDElement crudElement = ((SqlMapElementNode) element).getElement();
				StringBuffer buf = new StringBuffer("ID=");
				buf.append(crudElement.getId());
				return buf.toString();
			} else if (element instanceof MapperElementNode) {
				MapperCRUDElement crudElement = ((MapperElementNode) element).getElement();
				StringBuffer buf = new StringBuffer("ID=");
				buf.append(crudElement.getId());
				return buf.toString();
			} else {
				return super.getText(element);
			}
		}

	}
}
