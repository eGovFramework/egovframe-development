/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.rte.rdt.plugin.model;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import egovframework.rte.rdt.service.unit.Service;

/**
 * viewer에 표시될 내용을 관리하는 DependencyContentProvider 클래스
 * @author 이영진
 */
public class DependencyContentProvider implements IStructuredContentProvider {

	/**
	 * Elements를 제공한다.
	 * @param inputElement
	 * @return Object[] Viewer에 보여질 Object 배열
 	 */
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {

		return ((ArrayList<Service>)inputElement).toArray();
	}
	
	/** dispose 메소드 */
	public void dispose() {
	}

	/** inputChanged 메소드 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
