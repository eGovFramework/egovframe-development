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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
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
	
	/**
	 * SqlMapFile 확인
	 * 
	 * @param file
	 * @return SqlMapFile 여부 
	 */
	public static boolean isSqlMapFile(IFile file) {
		try {
			IContentDescription contentDescription = file.getContentDescription();
			if (contentDescription == null) return false;
			IContentType contentType = contentDescription.getContentType();
			//return matchContentType(contentType, "egovframework.dev.imp.dbio.sqlMap"); //$NON-NLS-1$
			return matchContentType(contentType, "net.harawata.mybatipse.mapper"); //$NON-NLS-1$
		} catch (CoreException e) {
			return false;
		}
	}
	
	/**
	 * MapperFile 확인
	 * 
	 * @param file
	 * @return MapperFile 여부 
	 */
	public static boolean isMapperFile(IFile file) {
		try {
			IContentDescription contentDescription = file.getContentDescription();
			if (contentDescription == null) return false;
			IContentType contentType = contentDescription.getContentType();
			return matchContentType(contentType, "egovframework.dev.imp.dbio.mapper"); //$NON-NLS-1$			
		} catch (CoreException e) {
			return false;
		}
	}
	
	/**
	 * isEGovSqlMapperFile 확인
	 * @param file
	 * @return
	 */
	public static boolean isEGovSqlMapperFile(IFile file) {
		try {
			IContentDescription contentDescription = file.getContentDescription();
			if (contentDescription == null) return false;
			IContentType contentType = contentDescription.getContentType();
			
			ArrayList<String> matchContents = new ArrayList<>();
			matchContents.add("net.harawata.mybatipse.mapper");
			matchContents.add("egovframework.dev.imp.dbio.mapper");
			matchContents.add("egovframework.dev.imp.dbio.sqlMap");
			
			Iterator<String> it = matchContents.iterator();
			while(it.hasNext()) {
				if(matchContentType(contentType, it.next()))
					return true;
			}
			return false;
			
			//return matchContentType(contentType, "egovframework.dev.imp.dbio.mapper"); //$NON-NLS-1$			
		} catch (CoreException e) {
			return false;
		}
	}
	
	/**
	 * ContentType  일치여부 반환
	 * @param contentType
	 * @param id
	 * @return ContentType  일치여부
	 */
	private static boolean matchContentType(IContentType contentType, String id) {
		if (contentType != null && id.equals(contentType.getId())) {
			return true;
		} else {
			IContentType baseType;
			if(contentType != null) {
				baseType = contentType.getBaseType();
				if (baseType != null) {
					return false;
				} else {
					return matchContentType(baseType, id);
				}
			} else {
				return false;
			}
		}
	}

}
