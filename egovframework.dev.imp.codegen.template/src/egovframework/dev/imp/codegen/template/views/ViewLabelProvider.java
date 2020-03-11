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
package egovframework.dev.imp.codegen.template.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * 라벨 제공자 클래스
 * <p><b>NOTE:</b> Viewer에 표시될 라벨 제공자 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
class ViewLabelProvider extends LabelProvider {

    /**
     * 
     * 텍스트 가져오기
     *
     * @param object
     * @return
     */
    public String getText(Object object) {
        return ((WizardElement)object).getLabel();	
    }

    /**
     * 
     * 이미지 가져오기
     *
     * @param object
     * @return
     */
    public Image getImage(Object object) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if(object instanceof WizardCategory)
            imageKey = ISharedImages.IMG_OBJ_FOLDER;
        return PlatformUI.getWorkbench().getSharedImages().getImage(
            imageKey);
    }
}	