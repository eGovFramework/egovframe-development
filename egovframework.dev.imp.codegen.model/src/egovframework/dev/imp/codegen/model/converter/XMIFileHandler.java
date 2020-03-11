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

import java.util.Collection;
import java.util.Collections;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XMI 파일을 다루기 위한 기초 클래스 
 * <p><b>NOTE:</b> XMI 모델 요소에 대한 처리. 
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
public class XMIFileHandler extends DefaultHandler implements XMIHandler {

	/** 문자열 버퍼 */
	private StringBuffer buffer;
	/** XMI Documentation 내의 요소인지 여부 */
	private boolean inXMIDocumentation = false;

	/* 
	 * 시작 요소 처리
	 *
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 * 
	 */
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		buffer = new StringBuffer();
		if (qName.equals("xmi:Documentation"))
			inXMIDocumentation = true;
	}

	/* 
	 * 종료 요소 처리 
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		if (inXMIDocumentation) // Ensure these are exporter and
		if (qName.equals("xmi:Documentation"))
			inXMIDocumentation = false;
	}

	/* 
	 * 버퍼에 문자열 추가 
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		buffer.append(ch, start, length);
	}

	/* 
	 * 객체 항목 가져오기 
	 * 
	 * @see egovframework.dev.imp.codegen.model.converter.XMIHandler#getObjects()
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection getObjects() {
		return Collections.EMPTY_LIST;
	}
}