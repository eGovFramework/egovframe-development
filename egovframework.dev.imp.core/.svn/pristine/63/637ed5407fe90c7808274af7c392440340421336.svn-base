package egovframework.dev.imp.core.utils;

/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {
	public static final int BLOCK_SIZE = 4096;
    protected static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    protected static DocumentBuilderFactory vfactory = DocumentBuilderFactory.newInstance();
    static {
        factory.setValidating(false);
        vfactory.setValidating(true);
    }
	
	public static Node getRootNode(String filePath) throws Exception{
        Document doc = XmlUtil.parsing(filePath, false);
        Node root = doc.getDocumentElement();
        return root;
	}
	
	public static Node getRootNode(InputStream contents) throws Exception{
		Document doc = XmlUtil.parsing(contents, false);
		Node root = doc.getDocumentElement();
		return root;
	}
	
	public static Node getRootNode(File file) throws Exception{
		Document doc = XmlUtil.parsing(file, false);
		Node root = doc.getDocumentElement();
		return root;
	}
	
	/**
	 * 문서의 맨 뒤에 커맨트를 삽입한다.
	 * @param filePath
	 * @param comment
	 * @throws Exception
	 */
	public static void addComment(Node contextNode, String xPath, String commentStr) throws Exception{
		Document doc = contextNode.getOwnerDocument();
		Comment com = doc.createComment(commentStr);
		contextNode.appendChild(com);
	}
	
	/**
	 * 문서의 맨 뒤에 커맨트를 삽입한다.
	 * @param filePath
	 * @param comment
	 * @throws Exception
	 */
	public static void addComment(Node contextNode, String xPath, String commentStr, String beforeText, String afterText) throws Exception{
		Document doc = contextNode.getOwnerDocument();
		Comment com = doc.createComment(commentStr);
		
		Text preValueText = doc.createTextNode(beforeText);
        Text postValueText = doc.createTextNode(afterText);
        contextNode.appendChild(preValueText);
        contextNode.appendChild(com);
        contextNode.appendChild(postValueText);
	}
	
	public static boolean existNode(Node contextNode, String xPath) throws Exception{
        Node searchNode = XmlUtil.getNode(contextNode, xPath);
        if(searchNode == null)
        	return false;
        else
        	return true;
	}
	
	public static boolean existNodeValue(Node contextNode, String xPath, String compareValue) throws Exception{
        Node searchNode = XmlUtil.getNode(contextNode, xPath);
        Node valueNode = searchNode.getFirstChild();
        if(valueNode == null)
        	return false;
        String nodeValue = valueNode.getNodeValue();
        if( compareValue.equals(nodeValue))
        	return true;
        else
        	return false;
	}
	
	public static boolean modifyNodeValue(Node contextNode, String xPath, String value) throws Exception{
        Node searchNode = XmlUtil.getNode(contextNode, xPath);
        Node valueNode = searchNode.getFirstChild();
        if(valueNode == null || valueNode.getNodeType() != Node.TEXT_NODE )
        	return false;
        valueNode.setNodeValue(value);
       	return true;
	}
	
	@Deprecated
	public static void saveXmlFile(String filePath, Node contextNode) throws Exception{
//		String xmlStr = XmlUtil.getXmlString(contextNode, "/");
//        File outFile = new File(filePath);
//		IFile originf = fo.getFile(new Path(type));
        
//        ByteArrayInputStream inStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
//        InputStream is = (InputStream)inStream;
//        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));
//        try {
//            byte[] buffer = new byte[BLOCK_SIZE];
//            int nrOfBytes = -1;
//            while ((nrOfBytes = inStream.read(buffer)) != -1) {
//            	outStream.write(buffer, 0, nrOfBytes);
//            }
//            outStream.flush();
//        } finally {
//            try {
//                if (inStream != null) inStream.close();
//            } catch (IOException ie) {
//            	
//            }
//            try {
//                if (outStream != null) outStream.close();
//            } catch (IOException ie) {
//            	throw new Exception("File IO Error", ie);
//            }
//        }
		
	}
	
	public static Node getNode(Node contextNode, String xPath) throws Exception{
        XObject xobject = XPathAPI.eval(contextNode, xPath);
        NodeList componentList = xobject.nodelist();
        if( componentList == null || componentList.getLength() == 0){
        	return null;
        }
        Node componentNode = componentList.item(0);
		return componentNode;
	}
	
	public static NodeList getNodeList(Node contextNode, String xPath) throws Exception{
        XObject xobject = XPathAPI.eval(contextNode, xPath);
        NodeList componentList = xobject.nodelist();
        if( componentList == null || componentList.getLength() == 0){
        	return null;
        }
		return componentList;
	}
	
	public static void addNode(Node contextNode, String xPath, String xmlStr) throws Exception{
        XObject xobject = XPathAPI.eval(contextNode, xPath);
        NodeList componentList = xobject.nodelist();
        if( componentList == null || componentList.getLength() == 0){
        	return;
        }
        Node parentsNode = componentList.item(0);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
        Node node = doc.getDocumentElement();
        
        Document ownerDoc = parentsNode.getOwnerDocument();
        Node importedNode = ownerDoc.importNode(node, true);
        parentsNode.appendChild(importedNode);
	}
	
	public static void addNode(Node contextNode, String xPath, String xmlStr, String beforeText, String afterText) throws Exception{
        XObject xobject = XPathAPI.eval(contextNode, xPath);
        NodeList componentList = xobject.nodelist();
        if( componentList == null || componentList.getLength() == 0){
        	return;
        }
        Node parentsNode = componentList.item(0);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
        Node node = doc.getDocumentElement();
        
        Document ownerDoc = parentsNode.getOwnerDocument();
        Node importedNode = ownerDoc.importNode(node, true);
        
        Text preValueText = ownerDoc.createTextNode(beforeText);
        Text postValueText = ownerDoc.createTextNode(afterText);
        parentsNode.appendChild(preValueText);
        parentsNode.appendChild(importedNode);
        parentsNode.appendChild(postValueText);
	}
	
	public static void addFirstNode(Node contextNode, String xPath, String xmlStr, String beforeText, String afterText) throws Exception{
	    XObject xobject = XPathAPI.eval(contextNode, xPath);
	    NodeList componentList = xobject.nodelist();
	    if( componentList == null || componentList.getLength() == 0){
	    	return;
	    }
	    Node parentsNode = componentList.item(0);
	    
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
	    Node node = doc.getDocumentElement();
	    
	    Document ownerDoc = parentsNode.getOwnerDocument();
	    Node importedNode = ownerDoc.importNode(node, true);
	    
	    Text preValueText = ownerDoc
	    .createTextNode(beforeText);
	    Text postValueText = ownerDoc.createTextNode(afterText);
	    
	    Node firstNode = parentsNode.getFirstChild();
	    
//	    System.out.println(parentsNode.getTextContent());
//	    System.out.println(firstNode.getTextContent());
//	    System.out.println(parentsNode.getLastChild().getTextContent());;
	    importedNode.getTextContent();
	    
	    parentsNode.insertBefore(preValueText, firstNode);
	    parentsNode.insertBefore(importedNode, firstNode);
	    parentsNode.insertBefore(postValueText, firstNode);
	}


	
	public static boolean removeNode(Node contextNode, String xPath) throws Exception{
        XObject xobject = XPathAPI.eval(contextNode, xPath);
        NodeList componentList = xobject.nodelist();
        if( componentList == null || componentList.getLength() == 0){
        	return false;
        }
        Node componentNode = componentList.item(0);
        componentNode.getTextContent();
        Node parentNode = componentNode.getParentNode();
        Node previousNode = componentNode.getPreviousSibling();
        parentNode.getTextContent();
        parentNode.removeChild(componentNode);
        parentNode.removeChild(previousNode);
        return true;
	}
	
	
	public static String getXmlString(Node rootNode, String xPath) throws Exception{
        XObject xobject = XPathAPI.eval(rootNode, xPath);
        NodeList componentList = xobject.nodelist();
        Node componentNode = componentList.item(0);
		
        StringWriter sw = new StringWriter();
        TransformerFactory tfFac = TransformerFactory.newInstance();
        Transformer tf = tfFac.newTransformer();
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        tf.transform(new DOMSource(componentNode), new StreamResult(sw));
		
		return sw.toString();
	}
	
    /**
     * 지정된 xml파일을 파싱하여 Document를 반환한다.
     *
     * @param filePath String  파싱할 파일의 절대경로
     * @param validating int boolean validation여부
     * @return Document 파싱된 파일의 Document
     * @throws DevonException
     */
	private static Document parsing(String filePath, boolean validating) throws Exception{
        FileInputStream in = null;
        try {
            Document temp;

            File conf_file = new File(filePath);

            in = new FileInputStream(conf_file);
            if (conf_file == null || !conf_file.exists() || !conf_file.canRead()) {
                throw new FileNotFoundException();
            }
            temp = XmlUtil.parse(new java.io.BufferedInputStream(in), validating);
            return temp;
        } catch (FileNotFoundException e) {
        	throw new FileNotFoundException();
        } finally {
            if (in != null) 
                in.close();
        }
    }
	
	private static Document parsing(InputStream contents, boolean validating) throws Exception{
		FileInputStream in = null;
		try {
			Document temp;
			
			in = (FileInputStream)contents;
			if (contents == null) {
				throw new FileNotFoundException();
			}
			temp = XmlUtil.parse(new java.io.BufferedInputStream(in), validating);
			return temp;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} finally {
			if (in != null)
				in.close();
		}
	}
	
	private static Document parsing(File file, boolean validating) throws Exception{
		FileInputStream in = null;
		try {
			Document temp;
			
			 File conf_file = file;

            in = new FileInputStream(conf_file);
            if (conf_file == null || !conf_file.exists() || !conf_file.canRead()) {
                throw new FileNotFoundException();
            }
			temp = XmlUtil.parse(new java.io.BufferedInputStream(in), validating);
			return temp;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} finally {
			if (in != null) 
				in.close();
		}
	}
	
    private static Document parse(InputStream in, boolean validating) throws Exception {
        try {
        	Reader reader = new InputStreamReader(in, "UTF-8");
        	InputSource is = new InputSource(reader);
        	is.setEncoding("UTF-8");
            return createBuilder(validating).parse(is);
        } catch (IOException e) {
        	throw new IOException();
        } catch (SAXException e) {
        	throw new SAXException();
        }
    }
    
    
	private static DocumentBuilder createBuilder(boolean validating) throws Exception {
        try {
            DocumentBuilder builder = null;

            if (validating) builder = vfactory.newDocumentBuilder();
            else builder = factory.newDocumentBuilder();

            builder.setEntityResolver(null);

            return builder;
        } catch (InternalError e) {
        	throw new InternalError();
        } catch (ParserConfigurationException e) {
        	throw new ParserConfigurationException();
        }
    }

}
