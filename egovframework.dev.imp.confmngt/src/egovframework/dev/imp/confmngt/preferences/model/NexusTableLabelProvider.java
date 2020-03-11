/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package egovframework.dev.imp.confmngt.preferences.model;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * Nexus Info의 Label Provider를 관리하는 클래스
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 *      개정이력(Modification Information)
 *   
 *   수정일      수정자           수정내용
 *  ------- -------- ---------------------------
 *  2011.06.13  조윤정     최초 생성
 * 
 * 
 * </pre>
 */

public class NexusTableLabelProvider extends LabelProvider implements ITableLabelProvider{
/**
 * Table Viewer의 컬럼에 set할 이미지를 가져옴
 * 
 * @param element
 * @param columnIndex
 * @return
 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * Table Viewer에 보여줄 컬럼을 설정함
	 * 
	 * @param element
	 * @param columnIndex
	 * @return
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		NexusInfo nexusInfo = (NexusInfo)element;
		switch(columnIndex){
		case 0:
			return nexusInfo.getNexusId();
		case 1:
			return nexusInfo.getNexusUrl();
		case 2:
			return nexusInfo.getIsRealeaseSelected()+"";
		case 3:
			return nexusInfo.getIsSnapshotsSelected()+"";

		default:
			return "unknown " + columnIndex;
		}
		
	}

	
}
