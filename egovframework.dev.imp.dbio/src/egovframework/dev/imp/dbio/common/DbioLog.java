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
package egovframework.dev.imp.dbio.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import egovframework.dev.imp.dbio.DBIOPlugin;

/**
 * DBIO Plugin의 로그 객체
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
 *   2009.02.20  김형조          최초 생성
 *
 * </pre>
 */
public class DbioLog {

    private static final int DEFAULT_CODE = IStatus.OK;

    /**
     * 로깅할 상태 생성
     * 
     * @param severity 로그의 심각도
     * @param code 상태 코드
     * @param message 로그 메시지
     * @param exception 예외 객체 (있는 경우)
     * @return 생성된 IStatus 객체
     */
    private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
        return new Status(severity, DBIOPlugin.PLUGIN_ID, code, message, exception);
    }

    /**
     * 로깅 처리
     * 
     * @param status 로깅할 IStatus 객체
     */
    private static void logStatus(IStatus status) {
        DBIOPlugin.getDefault().getLog().log(status);
    }

    /**
     * 정보레벨 로깅처리
     * 
     * @param message 로그 메시지
     */
    public static void logInfo(String message) {
        log(IStatus.INFO, DEFAULT_CODE, message, null);
    }

    /**
     * 에러레벨 로깅 처리
     * 
     * @param exception 예외 객체
     */
    public static void logError(Throwable exception) {
        logError("Unexpected Exception", exception);
    }

    /**
     * 에러레벨 로깅 처리
     * 
     * @param message 에러 메시지
     * @param exception 예외 객체
     */
    public static void logError(String message, Throwable exception) {
        log(IStatus.ERROR, DEFAULT_CODE, message, exception);
    }

    /**
     * 대표 로깅 처리
     * 
     * @param severity 로그의 심각도
     * @param code 상태 코드
     * @param message 로그 메시지
     * @param exception 예외 객체 (있는 경우)
     */
    public static void log(int severity, int code, String message, Throwable exception) {
        IStatus status = createStatus(severity, code, message, exception);
        logStatus(status);
    }
}
