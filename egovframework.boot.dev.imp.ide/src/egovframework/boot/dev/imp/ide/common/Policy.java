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
package egovframework.boot.dev.imp.ide.common;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * 모니터 관리 클래스
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
public class Policy {

    /**
     * 취소 여부 체크
     * @param monitor
     */
    public static void checkCanceled(IProgressMonitor monitor) {
        if (monitor.isCanceled()) {
            cancelOperation();
        }
    }

    /**
     * 오퍼레이션 취소
     */
    public static void cancelOperation() {
        throw new OperationCanceledException();
    }

    /**
     * 모니터 확인
     * @param monitor
     * @return ProgressMonitor
     */
    public static IProgressMonitor monitorFor(IProgressMonitor monitor) {
        if (monitor == null)
            return new NullProgressMonitor();
        else
            return monitor;
    }

    /**
     * 하위 모니터 생성
     * @param monitor
     * @param ticks
     *        ProgressMonitor
     * @return
     */
    @SuppressWarnings("deprecation")
	public static IProgressMonitor subMonitorFor(IProgressMonitor monitor,
            int ticks) {
        if (monitor == null)
            return new NullProgressMonitor();
        if (monitor instanceof NullProgressMonitor)
            return monitor;
        else
            return new SubProgressMonitor(monitor, ticks);
    }
}
