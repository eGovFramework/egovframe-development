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
package egovframework.dev.imp.dbio.editor.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;

import egovframework.dev.imp.dbio.editor.XMLFormEditor;
import egovframework.dev.imp.dbio.editor.model.SqlMapAliasElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapGroupElement;

/**
 * SQL Map 에디터의 아웃라인 페이지 클래스
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
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
@SuppressWarnings("restriction")
public class SqlMapOutlinePage extends ContentOutlinePage {
		private final XMLFormEditor editor;

		
		public SqlMapOutlinePage(XMLFormEditor xmleditor){
			editor = xmleditor;
		}
		
	    public void createControl(Composite gParent) {
	        super.createControl(gParent);
	        
	        TreeViewer viewer = super.getTreeViewer();
	        viewer.addSelectionChangedListener(this);
			viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);	        
	        viewer.setContentProvider(new SqlMapOutlineContentProvider());
	        viewer.setLabelProvider(new SqlMapOutlineLabelProvider());
	        viewer.setInput(editor.getModel());
	      }

//		/**
//		 * @see org.eclipse.jface.viewers.ISelectionProvider#selectionChanged(SelectionChangedEvent)
//		 */
		public void selectionChanged(SelectionChangedEvent anEvent) {
			super.selectionChanged(anEvent);
			ISelection selection = anEvent.getSelection();
			if (!selection.isEmpty()) {
				Object tmp = ((IStructuredSelection) selection).getFirstElement();
					
				if (!new SqlMapOutlineContentProvider().hasChildren(tmp)){
					editor.setActivePage("egovframework.dev.imp.dbio.editor.pages.SqlMapPage");
					SqlMapPage sqlmappage = (SqlMapPage) editor.getActivePageInstance();
					sqlmappage.getMdBlock().getMasterPart().setTreeSelection(tmp);

				}
//				EclipseUtil.getActivePage()
			}	
		}


		/**
		 * Updates the outline page.
		 */
		public void update() {
	        TreeViewer viewer = super.getTreeViewer();
			if (viewer != null) {
				viewer.setInput(editor.getModel());
			}
		}

		/**
		 * @see org.eclipse.ui.part.Page#dispose()
		 */
		public void dispose() {
			super.dispose();
		}

		private static class SqlMapOutlineLabelProvider extends LabelProvider {
			/* (non-Javadoc)
			 * 각 노드에 대한 이미지가 있을 경우 이미지를 리턴함. 
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			@Override
			public Image getImage(Object element) {
				return super.getImage(element);
			}
			
			/* (non-Javadoc)
			 * 각 노드에 대한 이름을 리턴함. 
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof SqlMapGroupElement) {
					return ((SqlMapGroupElement) element).getName();
				} else if (element instanceof SqlMapElement) {
					if (element instanceof SqlMapAliasElement) {
						return ((SqlMapElement) element).getAlias();
					} 
					else {
						return ((SqlMapElement) element).getId();
					}
				} else {
					return super.getText(element);
				}
			}
	        /**
	         * node 에 대한 adapter 를 리턴함. 
	         * @param adaptable
	         *            java.lang.Object The object to get the adapter for
	         */
	        @SuppressWarnings("unused")
			protected IJFaceNodeAdapter getAdapter(Object adaptable) {
	                if (adaptable instanceof INodeNotifier) {
	                        INodeAdapter adapter = ((INodeNotifier) adaptable).getAdapterFor(IJFaceNodeAdapter.class);
	                        if (adapter instanceof IJFaceNodeAdapter)
	                                return (IJFaceNodeAdapter) adapter;
	                }
	                return null;
	        }


		}
		
	}
