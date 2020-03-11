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

/**
 * 공통적으로 쓰이는 일반 유틸 클래스
 * <p><b>NOTE:</b>  
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class CommonUtil {

	/** 
	 * 문자열이 숫자로 구성되어 있는 지 확인 
	 * 
	 * @param s
	 * @return
	 * 
	 */
	public static boolean isNumber(String s)
	  {
	    String validChars = "0123456789";
	    boolean isNumber = true;
	 
	    for (int i = 0; i < s.length() && isNumber; i++) 
	    { 
	      char c = s.charAt(i); 
	      if (validChars.indexOf(c) == -1) 
	      {
	        isNumber = false;
	      }
	      else
	      {
	        isNumber = true;
	      }
	    }
	    return isNumber;
	  }
	
	/**
	 *  Camel Notation으로 변환 
	 * 
	 * @param word
	 * @return
	 * 
	 */
	public static String firstCharToLowerCase(String word){
		return word.substring(0,1).toLowerCase() + word.substring(1);
	}

}
