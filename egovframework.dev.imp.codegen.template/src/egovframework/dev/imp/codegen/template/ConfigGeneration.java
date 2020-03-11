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

import org.osgi.framework.BundleContext;

/**
 * 
 * Config 파일을 Generation 하기 위한 인터페이스 클래스
 * <p><b>NOTE:</b> Config 파일을 Generation 하기 위한 인터페이스 클래스이다. 
 * Generation할 Config 파일의 종류는 Property, Scheduling, Transaction, Cache, Data Source, Logging, Id Generation 등이 있다.  
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
public interface ConfigGeneration {
    /**
     * 플러그인 시작
     *
     * @param context
     * @throws Exception
     */
    public void start(BundleContext context) throws Exception;
}
