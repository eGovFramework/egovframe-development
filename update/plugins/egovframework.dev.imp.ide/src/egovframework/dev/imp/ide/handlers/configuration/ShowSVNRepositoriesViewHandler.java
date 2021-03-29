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
package egovframework.dev.imp.ide.handlers.configuration;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import egovframework.dev.imp.ide.common.IdeLog;
import egovframework.dev.imp.ide.handlers.EgovMenu;

/**
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.27
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.27  조윤정          최초 생성
 * 
 * 
 * </pre>
 */
public class ShowSVNRepositoriesViewHandler extends AbstractHandler implements EgovMenu {
    /**
     * SVN Repository Viewer를 실행하는 메소드
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("org.eclipse.team.svn.ui.repository.RepositoriesView");
        } catch (Exception e) {
        	IdeLog.logError(e);
        }
        return null;
    }

}
