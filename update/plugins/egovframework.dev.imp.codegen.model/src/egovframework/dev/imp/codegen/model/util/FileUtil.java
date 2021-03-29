/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.model.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

/**
 * 파일 유틸 클래스
 * <p><b>NOTE:</b>  
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
@SuppressWarnings("restriction")
public class FileUtil {
	
	/**
	 * XMI 파일 여부를 판단 
	 *
	 * @param file
	 * @return boolean
	*/
	public static boolean isXMIFile(IFile file) {
		try {
			IContentDescription contentDescription = file.getContentDescription();
			if (contentDescription == null) return false;
			IContentType contentType = contentDescription.getContentType();
			return isXMIFile(contentType);
		} catch (CoreException e) {
			return false;
		}
	}
	
	/** XMI 파일 확장자를 가지고 있는 지 확인한다. 
	 * 
	 * @param file
	 * @return
	 * 
	 */
	public static boolean hasXMIExtension(IFile file){
		String extension_name = file.getFileExtension();
		if (extension_name.equals("xmi") || extension_name.equals("xml") 
				|| extension_name.equals("XMI") || extension_name.equals("XML")
				|| extension_name.equals("uml") || extension_name.equals("UML"))
			return true;
		return false;
	}
	/**
	 * XMI 파일 여부를 판단 
	 *
	 * @param file
	 * @return boolean
	*/
	public static boolean isXMIFile(IContentType contentType) {
		return (matchContentType(contentType,"org.eclipse.emf.ecore.xmi")
				|| matchContentType(contentType,"egovframework.dev.imp.codegen.model.xmi")
				|| matchContentType(contentType,"org.eclipse.wst.xml.core.xmisource")
				|| matchContentType(contentType,"org.eclipse.uml2.uml_2_1_0")
				);
	}

	/**
	 * ContentType  일치여부 반환
	 * 
	 * @param contentType
	 * @param id
	 * @return ContentType  일치여부
	 * 
	 */
	private static boolean matchContentType(IContentType contentType, String id) {
		if (id.equals(contentType.getId())) {
			return true;
		} else {
			IContentType baseType = contentType.getBaseType();
			if (baseType != null) {
				return false;
			} else {
				return matchContentType(baseType, id);
			}
		}
	}
	
	/**
	 * ContentType  유사 여부 반환
	 * 
	 * @param contentType
	 * @param id
	 * @return ContentType  일치여부
	 * 
	 */
	public static boolean alikeXMIContentType(IContentType contentType) {
		if (contentType.getId().indexOf("xmi")>=0 || contentType.getId().indexOf("uml")>=0) {
			return true;
		} else {
			IContentType baseType = contentType.getBaseType();
			if (baseType != null) {
				return false;
			} else {
				return alikeXMIContentType(baseType);
			}
		}
	}
	
	/**
	 * IStructuredModel 을 반환한다.
	 * 
	 * @param file
	 * @return IStructuredModel 인서턴스
	 * 
	 */
	public static IStructuredModel getModelForRead(IFile file) {
		return StructuredModelManager.getModelManager().getExistingModelForRead(file);
	}

}
