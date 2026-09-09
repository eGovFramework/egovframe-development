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
package egovframework.rte.rdt.xml;

import java.io.StringReader;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.input.SAXHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * XXE(XML External Entity Injection, CWE-611) 방어 설정이 적용된 SAXBuilder.
 *
 * 외부 엔티티·외부 DTD 는 읽지 않으며, 내부 엔티티 치환은 기본 SAXBuilder 와 동일하게 동작한다.
 * 사용자 워크스페이스의 pom.xml, services.xml 처럼 신뢰할 수 없는 XML 을 읽는 파서는 이 클래스를 사용한다.
 *
 * JDOM 1.1.1 의 SAXBuilder 는 파서를 구성할 때 external-general-entities feature 를
 * expandEntities 값으로 덮어쓰므로 feature 만으로는 외부 일반 엔티티를 막을 수 없다.
 * 그래서 외부 자원 요청에 빈 스트림을 돌려주는 EntityResolver 로 차단한다.
 */
public class SecureSAXBuilder extends SAXBuilder {

	/** 파서에 false 로 적용할 feature 목록 */
	private static final String[] DISABLED_FEATURES = {
		"http://xml.org/sax/features/external-parameter-entities",
		"http://apache.org/xml/features/nonvalidating/load-external-dtd"
	};

	/** 외부 엔티티·외부 DTD 요청을 빈 내용으로 처리하는 resolver */
	private static final EntityResolver EMPTY_ENTITY_RESOLVER = new EntityResolver() {
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringReader(""));
		}
	};

	public SecureSAXBuilder() {
		super();
		setEntityResolver(EMPTY_ENTITY_RESOLVER);
	}

	/**
	 * @param saxDriverClass 사용할 SAX 드라이버 클래스명
	 */
	public SecureSAXBuilder(String saxDriverClass) {
		super(saxDriverClass);
		setEntityResolver(EMPTY_ENTITY_RESOLVER);
	}

	/**
	 * SAXBuilder.setFeature() 로 등록한 feature 는 파서가 인식하지 못하면 build() 시점에
	 * JDOMException 이 되어 정상 문서까지 파싱에 실패하므로, 파서에 직접 적용하면서
	 * 지원하지 않는 feature 는 건너뛰어 나머지 방어는 그대로 유지한다.
	 */
	@Override
	protected void configureParser(XMLReader parser, SAXHandler contentHandler) throws JDOMException {
		super.configureParser(parser, contentHandler);
		for (String feature : DISABLED_FEATURES) {
			try {
				parser.setFeature(feature, false);
			} catch (SAXNotRecognizedException e) {
				// 파서가 모르는 feature: 무시한다.
			} catch (SAXNotSupportedException e) {
				// 파서가 값 변경을 허용하지 않는 feature: 무시한다.
			}
		}
	}
}
