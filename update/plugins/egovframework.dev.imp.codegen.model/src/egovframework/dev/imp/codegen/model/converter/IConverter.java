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

import org.eclipse.uml2.uml.Model;

/**
 * XMI 변환 인터페이스
 * <p>
 * <b>NOTE:</b> XMI 변환에 필요한 필수 메소드를 정의함. 
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
public interface IConverter {
	/**
	 * XMI 파일의 경로를 설정한다. 
	 * 
	 * @param xmiFilePath
	 * 
	 */
	public void setXMIFilePath(String xmiFilePath);

	/**
	 * 변환된 결과모델을 읽어들인다.
	 * 
	 * @return
	 * 
	 */
	public Model getModel();

	/**
	 * XMI 파일로부터 모델을 변환한다. 
	 * 
	 * @throws Exception
	 * 
	 */
	public void convertModel() throws Exception;

	/**
	 * 코드젠 대상 클래스 목록을 반환한다.
	 * 
	 * @return
	 * 
	 */
	public Map<?, ?> getTarget();

	/**
	 * 스테레오타입이 적용된 클래스 목록을 반환한다.
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public Map<?, ?> getStereotypeClasses() throws Exception;
}
