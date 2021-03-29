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
package egovframework.dev.imp.core.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * UI 유틸리티 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class IdeUIUtil {

    /** 기본 버젼 설정 값 */
    private static final String DEFAULT_VERSION = "1.0.0"; 

    /**
     * 텍스트 필드 생성
     * @param parent
     * @param labelText
     * @return
     */
    public static Text createTextField(Composite parent, String labelText) {
        Label groupIdlabel = new Label(parent, SWT.NONE);
        groupIdlabel.setText(labelText);

        Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);
        text.setFont(parent.getFont());
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        text.setLayoutData(gd);
        return text;
    }

    /**
     * 컴보 필드 생성
     * @param parent
     * @param labelText
     * @return
     */
    public static Combo createComboField(Composite parent, String labelText) {
        Label groupIdlabel = new Label(parent, SWT.NONE);
        groupIdlabel.setText(labelText);

        Combo combo = new Combo(parent, SWT.BORDER);
        GridData gd_versionCombo =
            new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gd_versionCombo.widthHint = 150;
        combo.setLayoutData(gd_versionCombo);
        combo.setText(DEFAULT_VERSION);
        return combo;
    }

}
