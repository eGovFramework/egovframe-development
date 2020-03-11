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

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.internal.ide.StringMatcher;

/**
 * Config 화면의  Mpa File 목록 화면 부분
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class ListViewerPart {

	private String title;
	private String description;
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private Filter filter;
	private Text filterText;
	private TableViewer viewer;

	/**
	 * 생성자
	 * 
	 * @param title
	 * @param description
	 * @param contentProvider
	 * @param labelProvider
	 */
	public ListViewerPart(String title, String description, IContentProvider contentProvider, ILabelProvider labelProvider) {
		this.setTitle(title);
		this.setDescription(description);
		this.setContentProvider(contentProvider);
		this.setLabelProvider(labelProvider);
		this.setFilter();
	}
	
	private void setFilter() {
		filter = new Filter();
	}
	private void setTitle(String title) {
		this.title = title;
	}
	
	private void setDescription(String description) {
		this.description = description;
	}
	
	private void setContentProvider(IContentProvider contentProvider){
		this.contentProvider = contentProvider;
	}
	
	private void setLabelProvider(ILabelProvider labelProvider){
		this.labelProvider = labelProvider;
	}
	/**
	 * 화면 구성
	 * 
	 * @param managedForm
	 * @param parent
	 * @return
	 */
	public Section createContents(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		int style = Section.TITLE_BAR;
		if (description != null) {
			style |= Section.DESCRIPTION;
		}
		Section section = toolkit.createSection(parent, style);
		section.marginWidth = 0;
		section.marginHeight = 0;
		section.setText(title);
		if (description != null) {
			section.setDescription(description);
		}
		
		//client Composite
		Composite client = toolkit.createComposite(section, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		client.setLayout(gridLayout);
		
		section.setClient(client);
		toolkit.paintBordersFor(client);
		
		// 필터 텍스트
		filterText = toolkit.createText(client, null, SWT.NONE); 
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		filterText.setLayoutData(gd);
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filterChanged();
			}
		});		
		
		//테이블
		Table table = toolkit.createTable(client, SWT.MULTI);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 485;
		table.setLayoutData(gd);

		//버튼 컴포지트
		Composite buttonComposite = toolkit.createComposite(client, SWT.NONE);
		gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
		buttonComposite.setLayoutData(gd);		
		GridLayout blayout = new GridLayout();
		blayout.marginHeight = 0;
		blayout.horizontalSpacing = 0;
		blayout.marginWidth = 0;		
		buttonComposite.setLayout(blayout);
		toolkit.adapt(buttonComposite);

		createButtons(toolkit, buttonComposite);
		
		
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);

		viewer = new TableViewer(table);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.addFilter(filter);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
				ListViewerPart.this.selectionChanged(event);
			}
		});
		
		return section;
	}
	
	protected void createButtons(FormToolkit toolkit, Composite parent) {
		// DbioLog.logInfo("sham");
	}
	
	protected IStructuredSelection getSelection() {
		return (IStructuredSelection) viewer.getSelection();
	}
	
	protected StructuredViewer getViewer() {
		return viewer;
	}
	
	protected void filterChanged() {
		filter.setFilter(filterText.getText());
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				viewer.refresh();
			}
		});
	}
	
	protected void selectionChanged(SelectionChangedEvent event) {
		// DbioLog.logInfo("sham");
	}
	
	public void setInput(Object input) {
		if (viewer != null) {
			ISelection selection = viewer.getSelection();
			viewer.setInput(input);
			viewer.setSelection(selection);
		}
	}
	
	public void refreshViewer(ISelection selection) {
		viewer.refresh();
		viewer.setSelection(selection);
	}
	
	public void refreshViewer(Object element) {
		viewer.refresh(element, true);
	}
	
	private class Filter extends ViewerFilter {
		private StringMatcher matchPattern;
		
		public void setFilter(String namePattern) {
			matchPattern = new StringMatcher("*" + namePattern + "*", true, false); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (matchPattern != null) {
				return matchPattern.match(labelProvider.getText(element));
			}
			return true;
		}
	}
	
}
