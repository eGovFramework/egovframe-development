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
package egovframework.dev.imp.ide.wizards.pages;

import org.eclipse.swt.widgets.Composite;

import egovframework.dev.imp.ide.common.IdeMessages;
import egovframework.dev.imp.ide.wizards.examples.ExampleInfo;
import egovframework.dev.imp.ide.wizards.model.NewProjectContext;

/**
 * 기본 프로젝트 생성 마법사 페이지 클래스
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
public class CoreProjectCreationPage extends ProjectCreationPage {

    /**
     * 생성자
     * @param pageName
     * @param context
     */
    public CoreProjectCreationPage(String pageName, NewProjectContext context) {
        super(pageName, context);
        context.setDefaultExampleFile(ExampleInfo.defaultCoreExample);
        context.setPomFileName(ExampleInfo.corePomFile);
        setTitle(IdeMessages.wizardspagesCoreProjectCreationPage0);
        setDescription(IdeMessages.wizardspagesCoreProjectCreationPage1);
    }

    /**
     * UI 컨트롤 생성
     */
    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
    }

}
