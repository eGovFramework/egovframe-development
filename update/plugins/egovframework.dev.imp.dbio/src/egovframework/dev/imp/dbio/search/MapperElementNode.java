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
import org.eclipse.jface.viewers.TreeNode;

import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;

/**
 * MapperElementNode
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
public class MapperElementNode extends TreeNode implements IFileTreeNode{

	public MapperElementNode(FileTreeNode parent, MapperCRUDElement element) {
		super(element);
		setParent(parent);
	}
	
	/** 
	 * Mapper 모델 반환
	 * @return Mapper 모델
	 */
	public MapperCRUDElement getElement() {
		return (MapperCRUDElement) getValue();
	}
	
	public IFile getFile() {
		return ((FileTreeNode) getParent()).getFile();
	}
}
