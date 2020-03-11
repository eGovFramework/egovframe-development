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
package egovframework.dev.imp.codegen.model.converter;

import java.util.Map;

/**
 * 모델 목록을 생성하는 인터페이스
 * <p>
 * <b>NOTE:</b> 모델 목록 생성시에 필요한 필수 메소드를 정의한다.
 * 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 * 
 * </pre>
 */
public interface ListMaker {
	/**
	 * 모델을 설정한다. 
	 * 
	 * @param obj
	 * 
	 */
	public void setObject(Object obj);

	/**
	 * 모델 목록을 생성한다.
	 * 
	 * 
	 */
	public void makeList();

	/**
	 * 클래스 목록을 반환한다.
	 * 
	 * @return
	 * 
	 */
	public Map<?, ?> getClassesList();
}
