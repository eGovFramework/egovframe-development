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
package egovframework.dev.imp.codegen.template.wizards;

/**
 * 
 * 마법사 서비스 인터페이스
 * <p><b>NOTE:</b> 마법사를 통한 코드 제너레이션 서비스 인터페이스
 * 코드 생성하기 위하여 사용자는 Template 목록에서 Template 을 선택하고 
 * Template 에 필요한 항목을 입력하게 되는 데 
 * 각각의 Template 에 필요한 입력항목들을 표시하여 사용자가 입력하고 코드를 생성할 수 있도록 도와준다. 
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
public interface Wizard {
    /**
     * 
     * 마법사 시작
     *
     */
    public void start();
}
