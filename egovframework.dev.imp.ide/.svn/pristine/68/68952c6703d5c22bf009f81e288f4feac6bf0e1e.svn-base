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
package egovframework.dev.imp.ide.common;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import egovframework.dev.imp.ide.EgovIdePlugin;

/**
 * 로깅 처리 클래스
 * 
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
public class IdeLog {

	/**
	 * 로깅할 상태 생성
	 * 
	 * @param severity
	 * @param code
	 * @param message
	 * @param exception
	 * @return
	 */
	private static IStatus createStatus(int severity, int code, String message,
			Throwable exception) {
		IStatus status = new Status(severity, EgovIdePlugin.PLUGIN_ID, code,
				message, exception);
		return status;
	}

	/**
	 * 로깅 처리
	 * 
	 * @param status
	 */
	private static void log(IStatus status) {
		EgovIdePlugin.getDefault().getLog().log(status);
		return;
	}

	/**
	 * 정보레벨 로깅처리
	 * 
	 * @param message
	 */
	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
		return;
	}

	/**
	 * 에러레벨 로깅 처리
	 * 
	 * @param exception
	 */
	public static void logError(Throwable exception) {
		logError("Unexpected Exception", exception); 
		return;
	}

	/**
	 * 에러레벨 로깅 처리
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logError(String message, Throwable exception) {
		exception.printStackTrace();
		log(IStatus.ERROR, IStatus.OK, message, exception);
		return;
	}

	/**
	 * 대표 로깅 처리
	 * 
	 * @param severity
	 * @param code
	 * @param message
	 * @param exception
	 */
	public static void log(int severity, int code, String message,
			Throwable exception) {
		log(createStatus(severity, code, message, exception));
		return;
	}
	
    /**
     * 로깅 처리 및  Dialog로 로그메세지 출력      
     * @param exception
     */
    public static void setDialogMessage(Throwable exception) {
		//Dialog로 로그메세지 출력과 함께 로그메세지를 저장할 경우 사용
		StatusManager.getManager().handle(createStatus(IStatus.ERROR, IStatus.OK,
				exception.getMessage(), exception), StatusManager.LOG|StatusManager.BLOCK);
	}

    
    /**
     * 플러그인 내부 에러 발생할 경우 로깅 처리
     * @param exception
     */
    public static void logCoreError(CoreException exception) {
    	log(exception.getStatus());
    }
}
