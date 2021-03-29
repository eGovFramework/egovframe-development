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


/**
 * Nexus Info의 모델을 관리하는 클래스
 * 
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

public class NexusInfo {

	private String id;
	
	/**
	 * Nexus ID
	 */
	private String nexusId;
	
	/**
	 * Nexus URL
	 */
	private String nexusUrl;
	/**
	 * Realease 선택 여부
	 */
	private boolean isRealeaseSelected;
	/**
	 * Snapshots 선택 여부
	 */
	private boolean isSnapshotsSelected;

	/**
	 * Nexus Info 생성자
	 */
	public NexusInfo() {

	}

	/**
	 * Nexus Info의 생성자
	 * 
	 * @param id
	 * @param url
	 * @param isRealeaseSelected
	 * @param isSnapshotsSelected
	 */
	public NexusInfo(String id, String url, boolean isRealeaseSelected,
			boolean isSnapshotsSelected) {
		this.nexusId = id;
		this.nexusUrl = url;
		this.isRealeaseSelected = isRealeaseSelected;
		this.isSnapshotsSelected = isSnapshotsSelected;
	}

	/**
	 * Nexus ID를 get하는 메소드
	 * 
	 * @return
	 */
	public String getNexusId() {
		return nexusId;
	}

	/**
	 * Nexus ID를 set하는 메소드
	 * 
	 * @return
	 */
	public void setNexusId(String id) {
		this.nexusId = id;
	}

	/**
	 * Nexus URL get하는 메소드
	 * 
	 * @return
	 */
	public String getNexusUrl() {
		return nexusUrl;
	}

	/**
	 * Nexus URL set하는 메소드
	 * 
	 * @return
	 */
	public void setNexusUrl(String url) {
		this.nexusUrl = url;
	}

	/**
	 * Nexus Realease 여부를 get하는 메소드
	 * 
	 * @return
	 */
	public boolean getIsRealeaseSelected() {
		return isRealeaseSelected;
	}

	/**
	 * Nexus Realease 여부를 set하는 메소드
	 * 
	 * @return
	 */

	public boolean setIsRealeaseSelected(boolean isRealeaseSelected) {
		return this.isRealeaseSelected = isRealeaseSelected;
	}

	/**
	 * Nexus Snapshots 여부를 get하는 메소드
	 * 
	 * @return
	 */

	public boolean getIsSnapshotsSelected() {
		return isSnapshotsSelected;
	}

	/**
	 * Nexus Snapshots 여부를 set하는 메소드
	 * 
	 * @return
	 */

	public boolean setIsSnapshotsSelected(boolean isSnapshotsSelected) {
		return this.isSnapshotsSelected = isSnapshotsSelected;
	}

	
	public String getId() {
		return this.id;
	}

	
	public void setId(String id111) {
		this.id = getClass().getName()+"_"+id111;
	}

}
