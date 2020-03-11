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
package egovframework.rte.rdt.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

/**
 * 플러그인내부에서 사용될 유틸성 메소드를 정의한 클래스
 * @author 이영진
 */
public class ProjectUtil {
	
	/** 시스템에서 정의된 슬래쉬 */
	public final static String SLASH = System.getProperty("file.separator");
	/** 시스템에서 정의된 개행문자 */
	public final static String ENTER = System.getProperty("line.separator");

	/**
	 * 선택된 리소스를 반환한다.
	 * @param selection
	 * @return IResource
	 */
	public static IResource getSelectedResource(ISelection selection) {
		ArrayList<Object> resources = null;
		if (!selection.isEmpty()) {
			resources = new ArrayList<Object>();
			if (selection instanceof IStructuredSelection) {
				Iterator<?> elements = ((IStructuredSelection) selection)
						.iterator();
				while (elements.hasNext()) {
					Object next = elements.next();
					if (next instanceof IResource) {
						resources.add(next);
						continue;
					}
					if (next instanceof IAdaptable) {
						IAdaptable adaptable = (IAdaptable) next;
						Object adapter = adaptable.getAdapter(IResource.class);
						if (adapter instanceof IResource) {
							resources.add(adapter);
							continue;
						}
					}
				}
			}
		}

		if (resources != null && !resources.isEmpty()) {
			IResource[] result = new IResource[resources.size()];
			resources.toArray(result);
			if (result.length >= 1)
				return result[0];
		}
		return null;
	}
	
    /**
     * 파일의 존재여부를 검사한다.
     * @param loc
     * @return boolean 파일 존재여부
     */
    public static boolean existPath(String loc) {
        File path = new File(loc);
        return path.exists();
    }
    
	/**
	 * 입력받은 에러메세지를 MessageBox 형태로 표시한다.
	 * @param parent
	 * @param errorMassage
	 */
	public static void errorBox(Composite parent, String errorMassage) {
		MessageBox mb = new MessageBox(parent.getShell());
		mb.setText("Error");
		mb.setMessage(errorMassage);
		mb.open();
	}

}
