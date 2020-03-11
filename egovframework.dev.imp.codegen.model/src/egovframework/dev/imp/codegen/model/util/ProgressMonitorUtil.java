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
package egovframework.dev.imp.codegen.model.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * 모델 기반 코드젠 프로세스를 모니터하는 유틸 클래스
 * <p><b>NOTE:</b> 코드젠 프로세스에 대한 진행 프로세스 처리. 
 * @author 개발환경 개발팀 김형조
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  김형조          최초 생성
 *
 * </pre>
 */
public class ProgressMonitorUtil {
	/**
	 * 모니터 초기화
	 * 
	 * @param progressMonitor
	 * @return
	 * 
	 */
	public static IProgressMonitor initMonitor(IProgressMonitor progressMonitor)	{
		final IProgressMonitor monitor;

		if ( progressMonitor == null ) {
			monitor = new NullProgressMonitor();
		} else {
			monitor = progressMonitor;
		}
		
		return monitor;
	}

}
