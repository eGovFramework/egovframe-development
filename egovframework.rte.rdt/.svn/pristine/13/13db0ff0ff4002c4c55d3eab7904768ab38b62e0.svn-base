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
package egovframework.rte.rdt.service.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.service.unit.Service;

/**
 * 서비스들의 필요한 라이브러리를 정의해둔 Services 메타파일을 파싱하여 값을 가공해주는 클래스
 * @author 이영진
 */
public class ServicesParser {
	
	/**
	 * 서비스들의 필요한 라이브러리를 정의해둔 Services 메타파일을 파싱하여 ArrayList<Service> 형태로 리턴한다.
	 * @param file
	 * @return ArrayList<Service> 서비스 리스트
	 * @throws PomException
	 */
	public static ArrayList<Service> parse(File file) throws PomException {
		ArrayList<Service> services = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(file);
			services = generateServiceObjectFromXml(doc);
		} catch (JDOMException jde) {
			PomException pe = new PomException(jde);
			pe.setErrorCode(PomException.PARSING_XML_ERROR);
			pe.setErrorDetail(file.toString());
			throw pe;
		} catch (IOException ie) {
			PomException pe = new PomException(ie);
			pe.setErrorCode(PomException.FILE_IO_ERROR);
			pe.setErrorDetail(file.toString());
			throw pe;
		} catch (IllegalArgumentException iae) {	
			PomException pe = new PomException(iae);
			pe.setErrorCode(PomException.FILE_NOT_FOUND_ERROR);
			pe.setErrorDetail(file.toString());
			throw pe;
		}
		return services;
	}
	
	
	/**
	 * 파싱한 결과로 Service 객체에 저장한다. 
	 * @param doc
	 * @return ArrayList<Service> 서비스 리스트
	 */
	public static ArrayList<Service> generateServiceObjectFromXml(Document doc) {
		ArrayList<Service> ServiceList = new ArrayList<Service>();

		Service svc = null;

		Element root = doc.getRootElement();

		List<?> childs = root.getChildren();

		for (Object o : childs) {
			
			if (o instanceof Element) {
				svc = new Service();
				Element serviceElement = (Element) o;
				List<?> serviceChilds = serviceElement.getChildren();
				
				for (Object oo : serviceChilds) {
					Element e = (Element) oo;
					if ("name".equals(e.getName())) {
						svc.setName(e);
					}

					if ("layer".equals(e.getName())) {
						svc.setLayer(e);
					}

					if ("dependencies".equals(e.getName())) {
						svc.setDependency(e);
					}
				}
			} //end if
			ServiceList.add(svc);
		}
		return ServiceList;
	}
}
