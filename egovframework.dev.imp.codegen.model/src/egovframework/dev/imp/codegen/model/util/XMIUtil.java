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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XMI 유틸 
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
public class XMIUtil {

    /** XMI ID 속성 */
    protected static final String XMI_ID = "xmi:id";
    
	/**
	 * XMI Root 노드에 대한 필터 클래스
	 * 
	 */
	private static class XmiRootFilter implements NodeFilter {
		/* 허용 노드 설정 
		 * @see org.w3c.dom.traversal.NodeFilter#acceptNode(org.w3c.dom.Node)
		 * 
	 */
		public short acceptNode(Node n) {
			if (n.getNodeName().equals("xmi:XMI")){
				return NodeFilter.FILTER_ACCEPT; //.FILTER_REJECT;
			}else {
				return NodeFilter.FILTER_REJECT;
				
			}
		}
	}

	/**
	 * XMI ID 요소에 대한 필터 클래스
	 * 
	 */
	@SuppressWarnings("unused")
	private static class XmiIdFilter implements NodeFilter {
		/* 허용노드 설정 
		 * @see org.w3c.dom.traversal.NodeFilter#acceptNode(org.w3c.dom.Node)
		 * 
	 */
		public short acceptNode(Node n) {
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (e.getAttributeNode("xmi:id") != null)
					return NodeFilter.FILTER_ACCEPT;
			}
			return NodeFilter.FILTER_REJECT;
		}
	}
	
	/** XMI 문서 가져오기 
	 * 
	 * @param xmiFilePath
	 * @return
	 * 
	 */
	public static Document getXMIDocument(String xmiFilePath){
		FileReader reader= null;
		try {
			reader = new FileReader(new File(xmiFilePath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		InputSource inputSource = new InputSource(reader);
		inputSource.setEncoding("UTF-8");
		DOMParser parser = new DOMParser();
		Document document = null; 

		try {
			parser.parse(inputSource);
			document = parser.getDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	/** 이클립스 UML 모델인지 여부 가져오기 
	 * 
	 * @param document
	 * @return
	 * 
	 */
	public static boolean isEclipseUMLModel(Document document){
		DocumentTraversal dt = null;
		if (document !=null){
			dt = (DocumentTraversal) document;
			NodeIterator it = dt.createNodeIterator(document.getDocumentElement(),
					NodeFilter.SHOW_ALL, new XmiRootFilter(), true);
			Node n = getXMIRootNode(it);
			return isContainEclipseNs(n);
		}
		return false;
	}
	
	/** XMI 루트 노드 가져오기 
	 * 
	 * @param NodeIterator
	 * @return
	 * 
	 */
	public static Node getXMIRootNode(NodeIterator it){
		if (it.getRoot().getNodeName().equals("uml:Model") || it.getRoot().getNodeName().equals("uml:Package") || it.getRoot().getNodeName().equals("xmi:XMI"))
			return it.getRoot();
		Node n = it.nextNode();
		while (n != null) {
			if (n.getNodeName().equals("xmi:XMI") || n.getNodeName().equals("uml:Model")|| n.getNodeName().equals("uml:Package"))
				return n;
			n = it.nextNode();
		}
		return null;
	}
	
	/** 이클립스 UML2 네임스페이스를 가지고 있는 지 여부 반환  
	 * 
	 * @param object
	 * @return
	 * 
	 */
	public static boolean isContainEclipseNs(Node object) {
		NamedNodeMap attribs = null;
		if (object!=null){
			attribs = object.getAttributes();
			for (int j = 0; j < attribs.getLength(); ++j){
				if (attribs.item(j).getNodeValue().indexOf("http://www.eclipse.org/uml2/")>=0){
					return true;
				}
			}
		}	
		return false;
	}
}