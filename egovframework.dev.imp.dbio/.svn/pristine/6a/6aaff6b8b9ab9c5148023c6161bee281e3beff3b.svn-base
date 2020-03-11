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
import egovframework.dev.imp.dbio.editor.model.MapperAliasElement;
import egovframework.dev.imp.dbio.editor.model.MapperElement;
import egovframework.dev.imp.dbio.editor.model.MapperGroupElement;

/**
 * Mapper 에디터의 아웃라인 페이지 클래스
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
@SuppressWarnings("restriction")
public class MapperOutlinePage extends ContentOutlinePage {
		private final XMLFormEditor editor;

		
		public MapperOutlinePage(XMLFormEditor xmleditor){
			editor = xmleditor;
		}
		
	    public void createControl(Composite gParent) {
	        super.createControl(gParent);
	        
	        TreeViewer viewer = super.getTreeViewer();
	        viewer.addSelectionChangedListener(this);
			viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);	        
	        viewer.setContentProvider(new MapperOutlineContentProvider());
	        viewer.setLabelProvider(new MapperOutlineLabelProvider());
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
					
				if (!new MapperOutlineContentProvider().hasChildren(tmp)){
					editor.setActivePage("egovframework.dev.imp.dbio.editor.pages.MapperPage");
					MapperPage mapperpage = (MapperPage) editor.getActivePageInstance();
					mapperpage.getMdBlock().getMasterPart().setTreeSelection(tmp);

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

		private static class MapperOutlineLabelProvider extends LabelProvider {
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
				if (element instanceof MapperGroupElement) {
					return ((MapperGroupElement) element).getName();
				} else if (element instanceof MapperElement) {
					if (element instanceof MapperAliasElement) {
						return ((MapperElement) element).getAlias();
					} 
					else {
						return ((MapperElement) element).getId();
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
