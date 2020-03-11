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
package egovframework.hdev.imp.ide.common;

import java.util.regex.Pattern;

/**
 * 문자열 처리 유틸리티 클래스
 * 
 * @since 2012.07.24
 * @author 디바이스 API 개발환경 팀 조용현
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class StringUtil {
	
	
	/**
	 * 프로젝트 요소 Validation
	 * @param elementName
	 * @return boolean
	 */
	public static boolean isProjectElementNameAvailable(String elementName){
		if(!Pattern.matches("^[a-zA-Z0-9-._]*$", elementName) || elementName.indexOf(" ") > -1) {
        	return false;
        }else{
        	return true;
        }
	}
	
}
