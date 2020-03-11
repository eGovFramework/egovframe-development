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
package egovframework.rte.rdt.pom.exception;

/**
 * pom.xml 파일을 파싱하면서 발생할 수 있는 예외들을 처리하는 클래스
 */
public class PomException extends Throwable {
	/**
	 * 객체 직렬화를 위한 난수 생성된 ID값
	 */
	private static final long serialVersionUID = 19831021L;
	
	/**
	 * 알수 없는 에러
	 */
	public static final int UNKNOWN_ERROR = 0;
	/**
	 * 파일 입출력 에러
	 */
	public static final int FILE_IO_ERROR = 1;
	/**
	 * XML 파싱 에러
	 */
	public static final int PARSING_XML_ERROR = 2;
	/**
	 * POM 문법 에러
	 */
	public static final int POM_SYNTAX_ERROR = 3;
	/**
	 * 전달 인자 에러
	 */
	public static final int ARGUMENT_ERROR = 4;
	/**
	 * 파일 미존재 에러
	 */
	public static final int FILE_NOT_FOUND_ERROR = 5;
	
	/**
	 * 프로퍼티 없음
	 */
	public static final int PROPERTY_NOT_FOUND_ERROR = 6;
	/**
	 * 중복된 프로퍼티 정의
	 */
	public static final int DUPLICATED_PROPERTIES_ERROR = 7;
	/**
	 * 중복된 디펜던시 정의
	 */
	public static final int DUPLICATED_DEPENDENCY_ERROR = 8;
	/**
	 * 중복된 저장소 정의
	 */
	public static final int DUPLICATED_REPOSITORY_ERROR = 9;
	
	/**
	 * 에러 코드
	 */
	protected int errorCode;
	/**
	 * 에러 메세지
	 */
	protected String errorDetail;
	
	/**
	 * POM 예외 처리 클래스의 인스턴스를 생성한다.
	 */
	public PomException() {

	}
	
	/**
	 * POM 예외 처리 클래스의 인스턴스를 생성한다.
	 * @param e Exception 인스턴스
	 */
	public PomException(Exception e) {
		setStackTrace(e.getStackTrace());
	}
	
	/**
	 * POM 예외 처리 클래스의 인스턴스를 생성한다.
	 * @param t Throwable 인스턴스
	 */
	public PomException(Throwable t) {
		super(t);
	}

	/**
	 * 에러 코드를 가져온다.
	 * @return 에러 코드
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * 에러 코드를 설정한다.
	 * @param errorCode 에러 코드
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 에러 메세지를 가져온다.
	 * @return 에러 메세지
	 */
	public String getErrorDetail() {
		return errorDetail;
	}

	/**
	 * 에러 메세지를 설정한다.
	 * @param errorDetail 에러 메세지
	 */
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
}