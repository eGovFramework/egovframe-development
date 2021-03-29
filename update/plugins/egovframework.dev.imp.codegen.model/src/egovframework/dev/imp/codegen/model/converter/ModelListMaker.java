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
package egovframework.dev.imp.codegen.model.converter;

import java.util.Map;
import java.util.TreeMap;

/**
 * 클래스 모델 목록을 생성하는 클래스
 * <p>
 * <b>NOTE:</b>
 * 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 * 
 * </pre>
 */
public class ModelListMaker implements ListMaker {

	/** 모델 트리맵	 * 
	 */
	@SuppressWarnings("rawtypes")
	private final Map target = new TreeMap();

	/** 상위 패키지 */
	private org.eclipse.uml2.uml.Package package_;

	/* 
	 * 
	 * 대상 객체를 설정한다.
	 * 
	 * 
	 */
	public void setObject(Object obj) {
		if (obj instanceof org.eclipse.uml2.uml.Package) {
			package_ = (org.eclipse.uml2.uml.Package) obj;
		}
	}

	/* 
	 * 
	 * 목록을 생성한다.
	 * 
	 * 
	 */
	public void makeList() {
		makeClassesList("", package_);
	}

	/**
	 * 패키지 하위의 클래스를 목록화한다. 
	 * 
	 * @param parentPackageName
	 * @param package_
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void makeClassesList(String parentPackageName, org.eclipse.uml2.uml.Package package_) {
		String packageName = parentPackageName + "." + package_.getName();
		for (int i = 0; i < package_.getPackagedElements().size(); i++) {
			Object child = package_.getPackagedElements().get(i);
			if (child instanceof org.eclipse.uml2.uml.Class) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.Class) child).getName(), child);
			}
			if (child instanceof org.eclipse.uml2.uml.Interface) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.Interface) child).getName(), child);
			}
			if (!(child instanceof org.eclipse.uml2.uml.PrimitiveType) && child instanceof org.eclipse.uml2.uml.DataType) {
				this.target.put(packageName + "." + ((org.eclipse.uml2.uml.DataType) child).getName(), child);
			}
			if (child instanceof org.eclipse.uml2.uml.Package) {
				makeClassesList(packageName, (org.eclipse.uml2.uml.Package) child);
			}
		}
	}

	/* 
	 * 클래스 목록을 반환한다.
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.ListMaker#getClassesList()
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<?, Object> getClassesList() {
		return target;
	}

}
