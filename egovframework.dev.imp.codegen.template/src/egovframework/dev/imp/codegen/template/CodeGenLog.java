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
package egovframework.dev.imp.codegen.template;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * 
 * 로깅 처리 클래스
 * <p><b>NOTE:</b> 플러그인에서의 로그 기록을 위한 클래스이다.
 * 기록된 로그는 Error Log 뷰를 통해서 확인할 수 있다. 
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
public class CodeGenLog {

    /**
     * 로깅할 상태 생성
     * @param severity
     * @param code
     * @param message
     * @param exception
     * @return
     */
    private static IStatus createStatus(int severity, int code, String message,
            Throwable exception) {
        IStatus status =
            new Status(severity, EgovCodeGenPlugin.PLUGIN_ID, code, message,
                exception);
        return status;
    }

    /**
     * 로깅 처리
     * @param status
     */
    private static void log(IStatus status) {
        EgovCodeGenPlugin.getDefault().getLog().log(status);
        return;
    }

    /**
     * 정보레벨 로깅처리
     * @param message
     */
    public static void logInfo(String message) {
        log(IStatus.INFO, IStatus.OK, message, null);
        return;
    }

    /**
     * 에러레벨 로깅 처리
     * @param exception
     */
    public static void logError(Throwable exception) {
        logError("Unexpected Exception", exception); //$NON-NLS-1$
        return;
    }

    /**
     * 에러레벨 로깅 처리
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
}
