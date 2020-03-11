/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.rte.rdt.pom.util;

import java.io.File;

/**
 * 문자열의 계산 및 점검을 담당하는 유틸리티 클래스
 */
public class StringHelper {
	/**
	 * 유틸리티 클래스는 생성할 수 없다.
	 */
	private StringHelper() {

	}
	
	/**
	 * 주어진 문자열이 프로퍼티 문자열인지 여부를 점검한다.
	 * @param s 점검할 문자열
	 * @return 프로퍼티 문자열 여부
	 */
	public static boolean isPropertyString(String s) {
		return s.startsWith("${") && s.endsWith("}");
	}
	
	/**
	 * 주어진 문자열에서 실제 프로퍼티 키 값을 추출한다. 프로퍼티 문자열이 아닌 경우 빈 문자열을 반환한다.
	 * @param s 프로퍼티 문자열
	 * @return 프로퍼티 키 값
	 */
	public static String getProperty(String s) {
		if (isPropertyString(s)) {
			return s.substring(2, s.length()-1);
		} else {
			return "";
		}
	}
	
	/**
	 * 버전간의 신구 비교를 수행한다.
	 * @param s1 비교할 버전 (기준버전)
	 * @param s2 비교할 버전 (비교버전)
	 * @return s1과 s2간의 신구 비교 결과
	 */
	public static int compareVersionString(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		
		if (s1.equals(s2)) {
			return 0;
		}
		
		if (s1.equals("release") && s2.equals("snapshot")) {
			return 1;
		}
		
		if (s1.equals("snapshot") && s2.equals("release")) {
			return -1;
		}
		
		return s1.compareTo(s2);
		
		
	}

	/**
	 * 인자로 전달된 문자열들을 전달된 구분자로 합친다.
	 * @param delim 구분자
	 * @param names 합칠 문자열 (가변인자)
	 * @return 합쳐진 문자열
	 */
	public static String concatNames(char delim, String... names) {
		StringBuffer sb = new StringBuffer();
		for (String name : names) {
			sb.append(delim + name);
		}

		return sb.toString().substring(1);
	}

	/**
	 * 인자로 전달된 문자열들을 콤마로 합친다.
	 * @param names 합칠 문자열 (가변인자)
	 * @return 합쳐진 문자열
	 */
	public static String concatNameWithComma(String... names) {
		return concatNames(',', names);
	}

	/**
	 * 인자로 전달된 문자열들을 점으로 합친다.
	 * @param names 합칠 문자열 (가변인자)
	 * @return 합쳐진 문자열
	 */
	public static String concatNameWithDot(String... names) {
		return concatNames('.', names);
	}

	/**
	 * 문자열의 첫 글자를 소문자로 변경한다.
	 * @param str 첫 글자를 변경할 문자열
	 * @return 첫 글자가 소문자화 된 문자열
	 */
	public static String lowerFirstChar(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 주어진 파일의 확장자를 구한다.
	 * @param f 파일 인스턴스
	 * @return 확장자
	 */
	public static String getExtension(final File f) {
		return getExtension(f.getName());
	}

	/**
	 * 주어진 파일명의 확장자를 구한다.
	 * @param s 파일명
	 * @return 확장자
	 */
	public static String getExtension(final String s) {
		int lastDot = s.lastIndexOf(".");
		return (lastDot == -1 || lastDot == 0) ? "" : s.substring(lastDot + 1);
	}
}
