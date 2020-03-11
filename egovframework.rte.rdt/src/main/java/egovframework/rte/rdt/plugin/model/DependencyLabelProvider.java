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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import egovframework.rte.rdt.plugin.message.Messages;
import egovframework.rte.rdt.service.unit.Service;

/**
 * viewer에 표시될 Label을 관리하는 DependencyLabelProvider 클래스
 * @author 이영진
 */
public class DependencyLabelProvider extends LabelProvider implements
        ITableLabelProvider {


    /**
     * 컬럼의 이미지를 표시한다.
     * @param element
     * @param columnIndex
     * @return Image 표시될 이미지
     */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /**
     * 컬럼별로 표시될 텍스트를 지정한다.
     * @param element
     * @param columnIndex
     * @return String 표시될 텍스트
     */
    public String getColumnText(Object element, int columnIndex) {
        String result = ""; 
        Service service = (Service)element;

        switch (columnIndex) {
        case 0: // checkbox
        	break;
        case 1: // layer
            result = service.getLayer();
            break;
        case 2: // name
            result = service.getName();
            break;
        case 3: // version
        	/** 메타파일에서 읽어온 service들은 첫 dependencyId가 각 서비스를 대표하는
             *  dependency므로 첫번째 dependency의 버전을 service버전으로 사용한다  */
			if (service.getDependency().size() > 0) {
				String serviceDependencyId = service.getDependency().get(0);
				if (TableList.getVersion(serviceDependencyId.trim()) != null)
					result = TableList.getVersion(serviceDependencyId.trim());
				else
					result = "null";
			}
            break;
        case 4: //update 여부
        	result = TableList.isUpdate(service)? Messages.DependencyLabelProvider_1 : Messages.DependencyLabelProvider_2;
        	break;
        default:
            break;
        }

        return result;
    }
}
