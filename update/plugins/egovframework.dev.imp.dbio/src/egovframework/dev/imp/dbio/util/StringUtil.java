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
package egovframework.dev.imp.dbio.util;

/**
 * StringUtil
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
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
public class StringUtil {
	public static final String BLANK = " ";
	public static final String NEW_LINE = "\n";
	public static final String CDATA_LEFT = "<![CDATA[";	// <![CDATA[
	public static final String CDATA_RIGHT = "]]>";		// ]]>
	public static final String SEPARATOR = "^";
	//public static boolean CDATA_EXISTENCE_FLAG = false;
	
	private StringUtil() {}
	
	/**
	 * Null 체크
	 * 
	 * @param value
	 * @return
	 */
	public static String nvl(String value) {
		return value == null ? "" : value; //$NON-NLS-1$
	}
	
	/**
	 * Cdata 태그를 삭제
	 * @param sql
	 * @return Cdata 태그 없는 SQL
	 */
	public static String trimCdata(String sql) {
		String tmp = sql;
		if (checkCdata(sql)) {
			tmp = tmp.trim();
			tmp = tmp.substring(CDATA_LEFT.length());
			tmp = tmp.substring(0, tmp.length()- CDATA_RIGHT.length());
		}
		return tmp.trim();
	}

	/**
	 * Cdata 태그 존재여부를 판별
	 * @param sql
	 */
	public static boolean checkCdata(String sql) {
		boolean result = false;
		
		String tmpSql = null;
		
		tmpSql = sql.trim().toUpperCase();

		if (tmpSql.startsWith(CDATA_LEFT)) 
			result = true;
		
		return result;
	}
	
	/**
	 * Query Type 추출
	 * @param sql
	 * @return SELECT, INSERT, UPDATE, DELETE 중 1개
	 */
	public static String getQueryType(String sql) {
		String result = sql.substring(0,sql.indexOf(StringUtil.SEPARATOR));
//		result = removeSpace(result);
//		result = result.replaceAll("\n", "");
//		result = result.replaceAll("\r", "");
//		result = result.replaceAll("\t", "");
//		
//		if (checkCdata(sql)) {
//			result = result.substring(CDATA_LEFT.length(), CDATA_LEFT.length()+6);
//		} else {
//			result = result.substring(0, 6);
//		}
		return result.toUpperCase();		
	}
	
	/**
	 * Cdata 태그를 붙인다.
	 * @param sql
	 * @return Cdata 붙은 SQL
	 */
//	public static String attachCdata(String sql) {
//		String tmp = sql.trim();
//		if (CDATA_EXISTENCE_FLAG) {
//			tmp = CDATA_LEFT +NEW_LINE+ tmp +NEW_LINE+ CDATA_RIGHT;
//		}
//		return tmp;
//	}

//	/**
//	 * Cdata 태그 존재여부를 판별
//	 * @param sql
//	 */
//	public static void setCdataExistenceFlag(String sql) {
//		String tmpSql = null;
//		
//		tmpSql = sql.trim();
//
//		if (tmpSql.startsWith(CDATA_LEFT)) 
//			CDATA_EXISTENCE_FLAG = true;
//		else 
//			CDATA_EXISTENCE_FLAG = false;
//	}
	
	/**
	 * 문장의 공백 삭제
	 * @param value
	 * @return 공백 없는 문장
	 */
	public static String removeSpace(String value) {
		String result = value;
		result = result.replace(" ", "");
		result = result.replace("\n", "");
		return result;
	}
	/**
	 * 숫자 여부를 확인
	 * @param str
	 * @return
	 */
	public static boolean checkStrNum(String str) {
		boolean result = true;
		
		if (str == null || str.equals("")) {
			result = false;
		} else {
			for (int j = 0; j < str.length(); j++) {
				char ch = str.charAt(j);
				
				if (ch < '0' || '9' < ch) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 카멜표기법으로 변경
	 * @param src
	 * @return
	 */
	public static String convertCamelNotation(String src) {
		if(src == null) return "";
		
		int idx = 0;
		String target, replacement;
		String result  = src.toLowerCase();
		
		while(idx >= 0) {
			idx = result.indexOf('_');
			
			if (idx >= 0) {
				target = result.substring(idx, idx+2);
				replacement = result.substring(idx+1, idx+2).toUpperCase();
				result = result.replaceFirst(target, replacement);
			}
		}
		return result;
	}
	
	public static String toUpperCaseForFirstChar(String src) {
		String result = "";
		if (result != null && !"".equals(src)) {
			result = src.replaceFirst(src.substring(0,1), src.substring(0,1).toUpperCase());
		}
		return result;
	}
}
