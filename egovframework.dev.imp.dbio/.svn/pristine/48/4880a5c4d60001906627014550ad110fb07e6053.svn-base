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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapterFactory;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;

import egovframework.dev.imp.dbio.editor.model.SqlMapAliasGroupElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapGroupElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapParameterMapGroupElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapQueryGroupElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapResultMapGroupElement;

/**
 * SQL Map 에디터의 아웃라인 페이지의 컨텐트 프로바이더 클래스 
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
public class SqlMapOutlineContentProvider implements ITreeContentProvider{
		
		/**
		 * 생성자 
		 */
		public SqlMapOutlineContentProvider() {
			super();
		}

        /**
         * Returns the JFace adapter for the specified object.
         * Object 에 대한 adapter 를 리턴함. 
         * @param adaptable
         *            java.lang.Object The object to get the adapter for
         */
        protected IJFaceNodeAdapter getAdapter(Object adaptable) {
//        	System.out.println("getAdapter()");

        	if (adaptable instanceof INodeNotifier) {
                        INodeAdapter adapter = ((INodeNotifier) adaptable).getAdapterFor(IJFaceNodeAdapter.class);
                        if (adapter instanceof IJFaceNodeAdapter)
                                return (IJFaceNodeAdapter) adapter;
                }
                return null;
        }
        
		/* 
		 * 객체에 속한 객체배열을 리턴함. 
		 */
		@SuppressWarnings("unused")
		public Object[] getElements(Object object) {
			// The root is usually an instance of an XMLStructuredModel in
			// which case we want to extract the document.
			Object topNode = object;
			if (object instanceof IDOMModel) {
				topNode = ((IDOMModel) object).getDocument().getDocumentElement();
			}
            IJFaceNodeAdapter adapter = getAdapter(topNode);
            
			return getChildren(topNode);
		}


		/* (non-Javadoc)
		 * 자식노드가  있는지 여부 검사 
		 */
		@SuppressWarnings("unused")
		public boolean hasChildren(Object element) {
            IJFaceNodeAdapter adapter = getAdapter(element);
			if (element instanceof SqlMapGroupElement) {
				return ((SqlMapGroupElement) element).getChildren().length > 0;
			}
			return false;
		}

		/* (non-Javadoc)
		 * 자식노드를 배열형태로 리턴함.  
		 */
		@SuppressWarnings("unused")
		public Object[] getChildren(Object parentElement) {
        	IJFaceNodeAdapter adapter = getAdapter(parentElement);
        	
			if (parentElement instanceof Element) {
				return getGroups((Element) parentElement);
			} else if (parentElement instanceof SqlMapGroupElement) {
				return ((SqlMapGroupElement) parentElement).getChildren();
			} else {
				return new Object[0];
			}
		}
		
		/**
		 * 그룹을 설정함. 
		 * @param element
		 * @return
		 */
		private Object[] getGroups(Element element) {
			if (!"sqlMap".equals(element.getNodeName())) return new Object[0]; //$NON-NLS-1$
			
				Object[] groups = new SqlMapGroupElement[4];
				groups[0] = new SqlMapQueryGroupElement(element);
				groups[1] = new SqlMapAliasGroupElement(element);				
				groups[2] = new SqlMapParameterMapGroupElement(element);
				groups[3] = new SqlMapResultMapGroupElement(element);
			
				return groups;
		}
		
	/* (non-Javadoc)
	 * 부모객체를 가져옴. 
	 */
	public Object getParent(Object element) {
        IJFaceNodeAdapter adapter = getAdapter(element);	
        if (adapter != null)
            return adapter.getParent(element);

        return null;
        
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// dispose 
	}
		
	/* (non-Javadoc)
	 * input이 변화하였을 경우 LISTENER 를 변경함.  
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if ((oldInput != null) && (oldInput instanceof IStructuredModel)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IStructuredModel) oldInput).getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.removeListener(viewer);
			}
		}
		if ((newInput != null) && (newInput instanceof IStructuredModel)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IStructuredModel) newInput).getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.addListener(viewer);
			}
		}
	}
}


