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
package egovframework.dev.imp.dbio.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.IDE;

import egovframework.dev.imp.dbio.DBIOPlugin;

/**
 * Eclipse 의 구성 인스턴스를 획득하기 위한 도구
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
public class EclipseUtil {
	/**
	 * 해당 아이디의 에디터를 연다.
	 * @param editorInput
	 * @param editorID
	 * @return 에디터
	 * @throws Exception
	 */
	public static IEditorPart openEditor( IEditorInput editorInput, String editorID ) throws Exception {

		IWorkbenchPage activePage = getActivePage();
		if ( activePage == null ) {
			return null;
		}
		
	
		IEditorPart editor = activePage.findEditor( editorInput );
		
		if ( editor == null ) {
			editor = IDE.openEditor( activePage, editorInput, editorID );
		} else {
			activePage.activate( editor );
		}
		return editor;
	}
	
	/**
	 * 활성화 페이지를 반환한다.
	 * @return 활성 페이지
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = getActiveWorkbench().getActiveWorkbenchWindow();
		if ( window == null ) {
			return null;
		}
		return window.getActivePage();
	}
	
	/**
	 * 활성 워크벤치 반환
	 * @return 활성 워크벤치
	 */
	public static IWorkbench getActiveWorkbench() {
		return DBIOPlugin.getDefault().getWorkbench();
	}
	
	/**
	 * viewID 해당하는 객체를 반환 
	 * @param viewID
	 * @return IViewPart
	 */
	public static IViewPart findView(final String viewID) {
				
		final IViewPart[] retContainer = new IViewPart[1];
		
		syncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = getActivePage();
				if (null==page) return ;
				retContainer[0] = page.findView(viewID);
			}});
		
		return retContainer[0];
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Object syncExec(Runnable runnable) {
		
		final Object[] obj = new Object[1];
		getDisplay().syncExec(runnable);
		
		return obj;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Display getDisplay() {
		
		Display dp = Display.getCurrent();
		
		if (dp == null) 
			dp = Display.getDefault();
		
		return dp;
	}

}
