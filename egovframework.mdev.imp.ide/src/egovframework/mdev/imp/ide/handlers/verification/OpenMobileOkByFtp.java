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
package egovframework.mdev.imp.ide.handlers.verification;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;


/**
 * mobileOk 사이트의 FTP 단위 검증 페이지를 오픈하는  클래스
 * @author 이혜린
 * @since 2011.07.20
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.07.20  이혜린          최초 생성
 * 
 * 
 * </pre>
 */
public class OpenMobileOkByFtp extends AbstractHandler {
	
	/**
	 * The constructor.
	 */
	public OpenMobileOkByFtp() {
	}
	
    /**
     * 이클립스 내부 browser로 mobeliOk 사이트의 FTP 단위 검증 페이지 실행
     */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		

        IWorkbenchWindow window = EgovMobileIdePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();

		IWorkbenchBrowserSupport browserSupport = window.getWorkbench().getBrowserSupport();
		IWebBrowser browser = null;
		try {
			browser = browserSupport.createBrowser("OpenMobileOkByFtp");
			browser.openURL(new URL("http://v.mobileok.kr/index_ftp.jsp"));
			
		} catch (PartInitException e) {
			MoblieIdeLog.logError(e);	
			
		} catch (MalformedURLException e) {
			MoblieIdeLog.logError(e);
			
		} catch (Exception e) {
			MoblieIdeLog.logError(e);		
		}
		
		return null;
	}

}

