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
package egovframework.dev.imp.dbio.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.IModelProvider;

/**
 * StructuredModelUtil
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
public class StructuredModelUtil {

	/**
	 * IStructuredModel 을 반환한다.
	 * @param provider
	 * @param input
	 * @return IStructuredModel 인스턴스
	 */
	public static IStructuredModel getModelForRead(IDocumentProvider provider,
			IEditorInput input) {
		IDocument newDocument = provider.getDocument(input);
		if (provider instanceof IModelProvider) {
			return ((IModelProvider) provider).getModel(input);
		} else {
			return StructuredModelManager.getModelManager().getExistingModelForRead(newDocument);
		}
	}
	
	/**
	 * IStructuredModel 을 반환한다.
	 * @param file
	 * @return IStructuredModel 인서턴스
	 */
	public static IStructuredModel getModelForRead(IFile file) {
		return StructuredModelManager.getModelManager().getExistingModelForRead(file);
	}
}
