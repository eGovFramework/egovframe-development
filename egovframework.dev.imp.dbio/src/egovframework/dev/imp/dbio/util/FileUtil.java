/*
 * Copyright (c) 2009-2025 MOIS (MINISTRY OF THE INTERIOR AND SAFETY).
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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;

/**
 * FileUtil
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
public class FileUtil {
	private FileUtil() {};
	
	/** eGovFrame SQL 매퍼 파일로 취급하는 ContentType id 목록 */
	private static final String[] SQL_MAPPER_CONTENT_TYPE_IDS = {
			"net.harawata.mybatipse.mapper", //$NON-NLS-1$
			"egovframework.dev.imp.dbio.mapper", //$NON-NLS-1$
			"egovframework.dev.imp.dbio.sqlMap" //$NON-NLS-1$
	};
	
	/**
	 * SqlMapFile 확인
	 * 
	 * @param file
	 * @return SqlMapFile 여부 
	 */
	public static boolean isSqlMapFile(IFile file) {
		return matchContentType(contentTypeOf(file), "egovframework.dev.imp.dbio.sqlMap"); //$NON-NLS-1$
	}
	
	/**
	 * isEGovSqlMapperFile 확인
	 * @param file
	 * @return
	 */
	public static boolean isEGovSqlMapperFile(IFile file) {
		IContentType contentType = contentTypeOf(file);
		for (String id : SQL_MAPPER_CONTENT_TYPE_IDS) {
			if (matchContentType(contentType, id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 파일의 ContentType 반환. 설명을 얻을 수 없으면 null
	 * @param file
	 * @return ContentType, 판정 불가 시 null
	 */
	private static IContentType contentTypeOf(IFile file) {
		try {
			IContentDescription contentDescription = file.getContentDescription();
			return contentDescription == null ? null : contentDescription.getContentType();
		} catch (CoreException e) {
			return null;
		}
	}
	
	/**
	 * contentType 이 id 가 가리키는 ContentType 이거나 그 하위 타입인지 반환
	 * @param contentType
	 * @param id
	 * @return ContentType 일치(상속 포함) 여부
	 */
	private static boolean matchContentType(IContentType contentType, String id) {
		if (contentType == null) {
			return false;
		}
		IContentType target = Platform.getContentTypeManager().getContentType(id);
		return target != null && contentType.isKindOf(target);
	}

}
