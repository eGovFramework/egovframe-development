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
package egovframework.rte.rdt.pom.parser;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.pom.unit.DetailPom;
import egovframework.rte.rdt.pom.unit.Pom;
import egovframework.rte.rdt.pom.unit.PomObject;

/**
 * pom.xml 파일을 파싱하여 Pom 인스턴스를 만든다
 */
public class PomParser {

	/**
	 * pom.xml 파일을 파싱하여 Pom 인스턴스를 만들어서 반환한다. 정확한 pom.xml이 아닐경우 PomException을 발생시킨다.
	 * @param file pom.xml 파일
	 * @return Pom 인스턴스
	 */
	public static Pom parse(File file) throws PomException {
		DetailPom pom = null;

		SAXBuilder builder = new SAXBuilder();

		try {
			Document doc = builder.build(file);
			pom = generatePomObjectFromXml(doc);
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
		} catch (IllegalArgumentException iae) { // file�� null�϶� �߻�
			PomException pe = new PomException(iae);
			pe.setErrorCode(PomException.FILE_NOT_FOUND_ERROR);
			pe.setErrorDetail(file.toString());
			throw pe;
		}

		pom.setPomFile(file);

		return pom;
	}

	/**
	 * Pom 인스턴스을 화면에 출력한다.
	 * @param pom Pom 인스턴스
	 */
	public static void writeDocument(DetailPom pom) {
		XMLOutputter outputter = new XMLOutputter();

		try {
			outputter.output(pom.getDocument(), System.out);
		} catch (Exception e) {
		}
	}

	/**
	 * Pom 인스턴스를 주어진 OutputStream으로 출력한다.
	 * @param pom Pom 인스턴스
	 * @param os 출력할 스트림
	 */
	public static void writeDocument(DetailPom pom, OutputStream os) {
		XMLOutputter outputter = new XMLOutputter();

		try {
			outputter.output(pom.getDocument(), os);
		} catch (IOException ie) {
			PomException pe = new PomException(ie);
			pe.setErrorCode(PomException.FILE_IO_ERROR);
			pe.setErrorDetail(os.toString());
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 주어진 Document를 주어진 OutputStream으로 출력한다.
	 * @param doc 출력할 XML Document
	 * @param os 출력할 스트림
	 */
	public static void writeDocument(Document doc, OutputStream os) {
		XMLOutputter outputter = new XMLOutputter();
		try {
			outputter.output(doc, os);
			os.close();
		} catch (IOException ie) {
			PomException pe = new PomException(ie);
			pe.setErrorCode(PomException.FILE_IO_ERROR);
			pe.setErrorDetail(os.toString());
		}
	}

	/**
	 * JDOM으로 파싱된 XML 오브젝트를 이용하여 Pom 객체의 인스턴스를 만들어낸다.
	 * @param doc XML Document 인스턴스
	 * @return Pom 인스턴스
	 */
	public static DetailPom generatePomObjectFromXml(Document doc) {
		DetailPom pom = new PomObject();

		Element root = doc.getRootElement();
		Namespace ns = root.getNamespace();

		pom.setDocument(doc);
		pom.setNamespace(ns);
		pom.setModelVersion(root.getChild("modelVersion", ns));
		pom.setGroupId(root.getChild("groupId", ns));
		pom.setArtifactId(root.getChild("artifactId", ns));
		pom.setName(root.getChild("name", ns));
		pom.setPackaging(root.getChild("packaging", ns));
		pom.setUrl(root.getChild("url", ns));
		if (root.getChild("properties", ns) != null)
			pom.setProperties(root.getChild("properties", ns));
		pom.setVersion(root.getChild("version", ns));
		if (root.getChild("dependencies", ns) != null)
			pom.setDependencies(root.getChild("dependencies", ns));

		return pom;
	}

}