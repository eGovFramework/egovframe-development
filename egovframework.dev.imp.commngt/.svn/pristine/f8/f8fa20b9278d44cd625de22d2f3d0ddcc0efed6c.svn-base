package egovframework.dev.imp.commngt.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.core.utils.XmlUtil;
import egovframework.dev.imp.ide.common.ResourceUtils;
/**
 * 공통컴포넌트 파일복사 유틸  클래스
 * @author 개발환경 개발팀 이종대
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  이종대          최초 생성
 * 
 * 
 * </pre>
 */
public class ComResourceUtils{
	
    /**
     * 특정 폴더 하위에 파일을 생성한다.
     * 하위 폴더를 모두 생성한다.
     * 공통컴포넌트에 특화되어 파일을 복사한 후 새로운 파일을 넣는다.
     * @param container   파일이 생성될 폴더
     * @param fname       파일명
     * @param content     파일내용
     * @param monitor
     * @throws CoreException
     */
    public static IFile copyFolderFile(IContainer container, String fname, InputStream content, IProgressMonitor monitor, String type) throws CoreException {
    	IPath p = new Path(fname.replace('\\', '/'));
    	IContainer fo = null;
    	
    	if ( p.segmentCount() > 1) { // include path
    		IPath pt;
    		for(int i=p.segmentCount()-1 ; i >  0 ; --i) {
    			pt = p.removeLastSegments(i);
    			fo = container.getFolder(pt);
    			if ( !fo.isAccessible()) ((IFolder)fo).create(true, true, monitor);
    		}
    	}else {
    		fo = container;
    	}
    	
    	IFile originf = fo.getFile(new Path(type));
    	int i = 0;
    	
    	if (originf.isAccessible()) {
    		IFile copyf = fo.getFile(new Path(type + getCurrentTime()+i));
    		
    		//백업파일 생성시 기존의 파일이 있으면, 파일 카운드를 +1 한 후 새로운 파일을 생성한다.
    		while(copyf.isAccessible()){
    			copyf = fo.getFile(new Path(type + getCurrentTime()+i));
    			i++;
    		}
    		copyf.create(originf.getContents(), true, monitor);
    		originf.setContents(content, true, false, monitor);
    		
    	} else {
    		originf.create(content, true, monitor);
    	}
    	return originf;
    }
    
    /**
     * web.xml에 대하여 기존파일을 백업하고 내용을 추가하여 새로운 파일을 생성한다.
     * @param container
     * @param fname
     * @param content
     * @param monitor
     * @param type
     * @return
     * @throws CoreException
     */
    public static IFile copyWebXmlFile(NewEgovCommngtContext context, IContainer container, String fname, InputStream content, IProgressMonitor monitor, String type) throws CoreException {
    	IPath p = new Path(fname.replace('\\', '/'));
    	IContainer fo = null;
    	
    	if ( p.segmentCount() > 1) { // include path
    		IPath pt;
    		for(int i=p.segmentCount()-1 ; i >  0 ; --i) {
    			pt = p.removeLastSegments(i);
    			fo = container.getFolder(pt);
    			if (!fo.isAccessible()) ((IFolder)fo).create(true, true, monitor);
    		}
    	}else {
    		fo = container;
    	}
    	
    	IFile originf = fo.getFile(new Path(type));
    	int i = 0;
    	
    	if (originf.isAccessible()) {
    		
    		IFile copyf = fo.getFile(new Path(type + getCurrentTime()+i));
    		
    		//백업파일 생성시 기존의 파일이 있으면, 파일 카운드를 +1 한 후 새로운 파일을 생성한다.
    		while(copyf.isAccessible()){
    			copyf = fo.getFile(new Path(type + getCurrentTime()+i));
    			i++;
    		}
    		/*
    		// 기존파일에 servlet, servlet-mapping이 없을경우 web.xml의 값이 초기값이거나, 없는 것으로 간주되므로 새로운 파일로 대체한다.
    		boolean fileExist = checkOldWebXml(container, originf.getContents(), monitor);
    		*/
    		InputStream newContent = content;
    		/*
    		if(fileExist) {
    			// 새로운 파일을 생성하기 위한 데이터를 만든다.
    			newContent = modifyWebXml(container, fname, content, originf.getContents(), monitor);
    			
    		}
    		*/
    		if(context.isWebModify()){
    			
    			// 기존 파일을 백업한다.
    			copyf.create(originf.getContents(), true, monitor);
    			
    			// web.xml 파일을 생성한다.
    			originf.setContents(newContent, true, false, monitor);
    		}else {
    			// 새로생성한 파일을 bak파일로 구성한다.
    			copyf.create(newContent, true, monitor);
    		}
    		
    	} else {
    		originf.create(content, true, monitor);
    	}
    	return originf;
    }
    
    /**
     * web.xml에 servlet, servlet-mapping 노드가 있는지 체크한다.
     * @param container
     * @param oriCon
     * @param monitor
     * @return
     */
    public static boolean checkOldWebXml(IContainer container, InputStream oriCon, IProgressMonitor monitor){
    	
    	boolean check = false;
    	
    	try {

    		DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    		
    		// AS-IS 시스템의 document
    		Document oriDoc = docBuilder.parse(new InputSource(oriCon));
    		Node oriRootNode = oriDoc.getDocumentElement();
    		
    		//기존시스템에 servlet과 servlet-mapping이 있는지 확인한다.
    		boolean mappingExist = checkServletMapping(oriRootNode);
    		
    		if(mappingExist) {
				check = true;
    		}
    		
    	}catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	return check;
    }
    
    /**
     * inputStream을 입력받아 document를 리턴한다.
     * dtd, namespace등의 정보는 무시한다.
     * @param xmlSource
     * @return
     */
    public static Document makeDocument(InputStream xmlSource){
    	Document xmlDoc = null;
    	try {
    		
    		DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    		
    		docBF.setValidating(false);
    		docBF.setNamespaceAware(false);
    		docBF.setFeature("http://xml.org/sax/features/validation", false);
    		docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    		docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    		docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    		docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    		
    		DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    		
    		xmlDoc = docBuilder.parse(new InputSource(xmlSource));
			
		} catch (Exception e) {
			CommngtLog.logError(e);
		}
    	return xmlDoc;
    }
    
    /**
     * 변경될 web.xml파일을 생성할 컨텐츠를 구성한다.
     * @param container
     * @param fname
     * @param newCon 입력받은 컨텐츠
     * @param oriCon AS-IS 시스템의 컨텐츠
     * @param monitor
     * @return
     */
    public static InputStream modifyWebXml(IContainer container, String fname, InputStream newCon, InputStream oriCon, IProgressMonitor monitor){
    	
    	Node oriRootNode = null;

    	try {

    		DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    		
    		// AS-IS 시스템의 document
    		Document oriDoc = docBuilder.parse(new InputSource(oriCon));
    		oriRootNode = oriDoc.getDocumentElement();
    		
    		// 새로운 컨텐츠의 document
    		Document newDoc = docBuilder.parse(new InputSource(newCon));
    		Node newRootNode = newDoc.getDocumentElement();
    		
    		// 새로운 컨텐츠에 servlet과 servlet-mapping이 있는지 확인한다.
    		boolean mappingExist = checkServletMapping(newRootNode);
    		
    		if(mappingExist) {
    			
    			String urlDoPattern = "*.do";
    			
    			// 새로운 컨텐츠에서 *.do에 대한 url servlet 및 context-param작업을 수행한다.
    			// *.do url패턴에 대하여 해당 노드가 존재할 경우 *.do가 가리키는 servlet 노드를 가져온다.(새로운 컨텐츠)
    			Node doNode = getServlet(newRootNode, urlDoPattern);
    			
    			// *.do url패턴에 대하여 해당 노드가 존재할 경우 *.do가 가리키는 servlet 노드를 가져온다.(기존)
    			Node doOldNode = getServlet(oriRootNode, urlDoPattern);
    			
    			// servlet node를 이용하여 init-param/param-value를 수정한다.
    			if(doOldNode != null) {
    				// 기존에 *.do가 있다면 병합, 없다면 새로운 servlet 노드를 추가한다.
    				mergeServlet(doOldNode, doNode);
    			} else {
    				//기존에 *.do 노드가 없다면 최초의 servlet과 새로운 servlet의 병합을 실시한다.
    				NodeList servletList = XmlUtil.getNodeList(oriRootNode, "/web-app/servlet");
    				if(servletList.getLength()>0) {
    					mergeServlet(servletList.item(0), doNode);
    					//기존에 *.do노드가 없다면 새로운 servlet-mapping(*.do)를 추가한다. 추가시 기존의 servletName을 적용한다.
    					XmlUtil.addNode(oriRootNode, "/web-app", nodeToString(getServletMapping(newRootNode, urlDoPattern, XmlUtil.getNode(servletList.item(0), "./servlet-name").getFirstChild().getNodeValue())), "\t", "\n");
    				}
    			}
    			
    			// *.do 패턴에 대한 context를 가져온다.
    			if(doNode != null) {
    				Node doOldContextParam = getContextParam(oriRootNode, doOldNode);	//기존
    				Node doContextParam = getContextParam(newRootNode, doNode);			//새로운 컨텐츠
    				
    				//context node를 이용하여 param-value를 수정한다.
    				mergeContext(doOldContextParam, doContextParam);
    			}
    			
    			
    			/* 중복체크를 통한 filter, filter-mapping, listener노드를 추가한다. */
    			
    			// 기존 파일에서 filter 노드 리스트를 가져와 filter/filter-name노드 명을 가져온다.
    			NodeList oriFilterNameList = XmlUtil.getNodeList(oriRootNode, "/web-app/filter/filter-name");
    			String filterNameList = "";
    			if(oriFilterNameList.getLength() > 0) {
    				for(int i=0 ; oriFilterNameList.getLength() > i ; i++) {
    					filterNameList += ((Node)oriFilterNameList.item(i)).getFirstChild().getNodeValue() + ";;;";
    				}
    			}
    			
    			// filter 노드를 가져온다.
    			NodeList filterList = XmlUtil.getNodeList(newRootNode, "/web-app/filter");
    			//노드를 추가한다.
    			if(filterList.getLength() > 0) {
    				for(int i=0 ; filterList.getLength() > i ; i++) {
    					if(!(filterNameList.indexOf(XmlUtil.getNode(filterList.item(i), "./filter-name").getFirstChild().getNodeValue()) > -1)) {
    						XmlUtil.addNode(oriRootNode, "/web-app", nodeToString(filterList.item(i)), "\t", "\n");
    						filterNameList += XmlUtil.getNode(filterList.item(i), "./filter-name").getFirstChild().getNodeValue() + ";;;";
    					}
    				}
    			}
    			
    			// 기존 파일에서 filter 노드 리스트를 가져와 filter/filter-name노드 명을 가져온다.
    			NodeList oriFilterMappingNameList = XmlUtil.getNodeList(oriRootNode, "/web-app/filter-mapping/filter-name");
    			String filterMappingNameList = "";
    			if(oriFilterMappingNameList.getLength() > 0) {
    				for(int i=0 ; oriFilterMappingNameList.getLength() > i ; i++) {
    					filterMappingNameList += ((Node)oriFilterMappingNameList.item(i)).getFirstChild().getNodeValue() + ";;;";
    				}
    			}
    			
    			// filter-mapping 노드를 가져온다.
    			NodeList filterMappingList = XmlUtil.getNodeList(newRootNode, "/web-app/filter-mapping");
    			//노드를 추가한다.
    			if(filterMappingList.getLength() > 0) {
    				for(int i=0 ; filterMappingList.getLength() > i ; i++) {
    					if(!(filterMappingNameList.indexOf(XmlUtil.getNode(filterMappingList.item(i), "./filter-name").getFirstChild().getNodeValue()) > -1)) {
    						XmlUtil.addNode(oriRootNode, "/web-app", nodeToString(filterMappingList.item(i)), "\t", "\n");
    						filterMappingNameList += XmlUtil.getNode(filterMappingList.item(i), "./filter-name").getFirstChild().getNodeValue() + ";;;";
    					}
    				}
    			}
    			
    			//기존파일에서 리스너 클래스 리스트를 가져온다.
    			NodeList oriListenerClassList = XmlUtil.getNodeList(oriRootNode, "/web-app/listener/listener-class");
    			String listenerClassNameList = "";
    			
    			if(oriListenerClassList.getLength() > 0) {
    				for(int i=0 ; oriListenerClassList.getLength() > i ; i++) {
    					listenerClassNameList += ((Node)oriListenerClassList.item(i)).getFirstChild().getNodeValue() + ";;;";
    				}
    			}
    			
    			// listener 노드를 가져온다.
    			NodeList listenerList = XmlUtil.getNodeList(newRootNode, "/web-app/listener");
    			//노드를 추가한다.
    			if(listenerList.getLength() > 0) {
    				for(int i=0 ; listenerList.getLength() > i ; i++) {
    					if(!(listenerClassNameList.indexOf(XmlUtil.getNode(listenerList.item(i), "./listener-class").getFirstChild().getNodeValue()) > -1)) {
    						XmlUtil.addNode(oriRootNode, "/web-app", nodeToString(listenerList.item(i)), "\t", "\n");
    						listenerClassNameList += XmlUtil.getNode(listenerList.item(i), "./listener-class").getFirstChild().getNodeValue() + ";;;";
    					}
    				}
    			}
    		}
    		
    	}catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	
    	//Document를 inputStream으로 변환하여 return 한다.
    	return nodeToStream(oriRootNode);
    }
    
    /**
     * 노드를 inputStream형태로 바꾸어 리턴한다.
     * @param root
     * @return
     */
    public static InputStream nodeToStream(Node root) {
    	
    	InputStream is = null;
    	
    	try {
    		
    		String xmlStr = XmlUtil.getXmlString(root, "/");
    		is = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
    		
		} catch (Exception e) {
			CommngtLog.logError(e);
		}
		
		return is;
    }
    
    /**
     * AS-IS 서블릿과 새로운 서블릿 노드를 입력받아
     * servlet 설정 부분을 병합한다.
     * 변화가 없을 경우 기존의 노드를 return 한다.
     * @param oldNode : 기존 시스템의 노드
     * @param newNode : 새로운 파일의 노드
     * @return
     */
    public static void mergeServlet(Node oldNode, Node newNode) {
    	try {
    		if(null != oldNode ) {
    			//새로운 노드가 null일 경우 기존의 노드를 리턴한다.
    			if(null != newNode){
    				//init-param 노드가 있을경우 병합, 없을경우 새로운 노드의 값을 넣는다.
    				if(XmlUtil.existNode(oldNode, "./init-param") && XmlUtil.existNode(oldNode, "./init-param/param-value")) {
    					//병합한다.
    					String oldValue = XmlUtil.getNode(oldNode, "./init-param/param-value").getFirstChild().getNodeValue();
    					String newValue = XmlUtil.getNode(newNode, "./init-param/param-value").getFirstChild().getNodeValue();
    					
    					if(null == oldValue) {
    						XmlUtil.getNode(oldNode, "./init-param/param-value").getFirstChild().setNodeValue(newValue);
    					} else if (oldValue != null && newValue != null) {
    						if(!(oldValue.indexOf(trimString(newValue))>-1)) {
    							oldValue += "," + newValue.replace("\n", "") + "\n";
    							XmlUtil.getNode(oldNode, "./init-param/param-value").getFirstChild().setNodeValue(oldValue);
    						}
    					}
    				
    				}
    			}
    		}
    		
    	}catch (Exception e) {
    		CommngtLog.logError(e);
		}
    }
    
    /**
     * String object의 tab, space, enter를 없애준다.
     * @param oriString
     * @return
     */
    public static String trimString(String oriString) {
    	String newString = "";
    	if(null != oriString) {
    		newString = oriString.replace("\n", "").replace("\t", "").replace(" ", "");
    	}
    	
    	return newString;
    }
    /**
     * AS-IS 컨텍스트와 새로운 컨텍스트 노드를 입력받아
     * context 설정 부분을 병합한다.
     * 변화가 없을 경우 기존의 노드를 return 한다.
     * @param oldNode
     * @param newNode
     * @return
     */
    public static void mergeContext(Node oldNode, Node newNode) {
    	try {
    		if(null != oldNode ) {
    			//새로운 노드가 null일 경우 기존의 노드를 리턴한다.
    			if(null != newNode){
    				//init-param 노드가 있을경우 병합, 없을경우 새로운 노드의 값을 넣는다.
    				if(XmlUtil.existNode(oldNode, "./param-value")) {
    					//병합한다.
    					String oldValue = XmlUtil.getNode(oldNode, "./param-value").getFirstChild().getNodeValue();
    					String newValue = XmlUtil.getNode(newNode, "./param-value").getFirstChild().getNodeValue();
    					
    					if(null == oldValue) {
    						XmlUtil.getNode(oldNode, "./param-value").getFirstChild().setNodeValue(newValue);
    					} else if (oldValue != null && newValue != null) {
    						if(!(oldValue.indexOf(trimString(newValue))>-1)) {
    							oldValue += "," + newValue.replace("\n", "") + "\n";
    							XmlUtil.getNode(oldNode, "./param-value").getFirstChild().setNodeValue(oldValue);
    						}
    					}
    				}
    			}
    		}
    		
    	}catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    }
    
    /**
     * url패턴을 가지는 servlet-mapping과 관계되어있는 servlet node를 가져온다.
     * 없을경우 null을 return 한다.
     * @param root
     * @param pattern
     * @return
     */
    public static Node getServlet(Node rootNode, String pattern) {
    	try {
    		NodeList servletMappingList = XmlUtil.getNodeList(rootNode, "/web-app/servlet-mapping");
    		if(servletMappingList != null){
    			for(int i=0 ; servletMappingList.getLength() > i ; i++) {
    				
    				// servlet-mapping node를 가져온다.
    				Node servletMappingNode = servletMappingList.item(i);
    				// url 패턴을 가져온다.
    				Node urlPatternNode = XmlUtil.getNode(servletMappingNode, "./url-pattern");
    				if(urlPatternNode != null && pattern != null) {
    					if(pattern.indexOf(urlPatternNode.getFirstChild().getNodeValue()) > -1) {
    						
    						// 서블릿 명을 가져온다.
    						String servletName = XmlUtil.getNode(servletMappingNode, "./servlet-name").getFirstChild().getNodeValue();
    						
    						//서블릿 노드를 가져온다.
    						NodeList servletNodeList = XmlUtil.getNodeList(rootNode, "/web-app/servlet");
    						
    						for(int j=0 ; servletNodeList.getLength() > j ; j++) {
    							Node servletNode = servletNodeList.item(j);
    							Node servletN = XmlUtil.getNode(servletNode, "./servlet-name");
    							//서블릿 노드명과 매핑된 서블릿을 비교하여 같은 이름의 서블릿을 리턴한다.
    							if(servletName != null){
    								if(servletName.equals(servletN.getFirstChild().getNodeValue())) return servletNode; 
    							}
    						}
    						
    					}
    				}
    			}
    		}
    	} catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	return null;
    }
    
    /**
     * url 패턴을 가지는 servlet-mapping과 관계되어있는 servlet mapping node를 가져온다.
     * 없을 경우 null값을 return 한다.
     * @param rootNode
     * @param pattern
     * @return
     */
    public static Node getServletMapping(Node rootNode, String pattern){
    	try {
    		NodeList servletMappingList = XmlUtil.getNodeList(rootNode, "/web-app/servlet-mapping");
    		if(servletMappingList != null){
    			for(int i=0 ; servletMappingList.getLength() > i ; i++) {
    				
    				// servlet-mapping node를 가져온다.
    				Node servletMappingNode = servletMappingList.item(i);
    				
    				// url 패턴을 가져온다.
    				Node urlPatternNode = XmlUtil.getNode(servletMappingNode, "./url-pattern");
    				
    				if(urlPatternNode != null && pattern != null) {
    					if(pattern.indexOf(urlPatternNode.getFirstChild().getNodeValue()) > -1) {
    						return servletMappingNode;
    					}
    				}
    			}
    		}
    	} catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	return null;
    }
    
    /**
     * url 패턴을 가지는 servlet-mapping과 관계되어있는 servlet mapping node를 가져온다.
     * servletName을 입력받을경우, servlet Name을 수정한다.
     * 없을 경우 null값을 return 한다.
     * @param rootNode
     * @param pattern
     * @return
     */
    public static Node getServletMapping(Node rootNode, String pattern, String servletName){
    	try {
    		NodeList servletMappingList = XmlUtil.getNodeList(rootNode, "/web-app/servlet-mapping");
    		if(servletMappingList != null){
    			for(int i=0 ; servletMappingList.getLength() > i ; i++) {
    				
    				// servlet-mapping node를 가져온다.
    				Node servletMappingNode = servletMappingList.item(i);
    				
    				// url 패턴을 가져온다.
    				Node urlPatternNode = XmlUtil.getNode(servletMappingNode, "./url-pattern");
    				
    				if(urlPatternNode != null && pattern != null) {
    					if(pattern.indexOf(urlPatternNode.getFirstChild().getNodeValue()) > -1) {
    						XmlUtil.getNode(servletMappingNode, "./servlet-name").getFirstChild().setNodeValue(servletName);
    						return servletMappingNode;
    					}
    				}
    			}
    		}
    	} catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    	return null;
    }
    
    /**
     * servlet node를 입력받아 init-param/param-name의 context-param을 읽어 context-param 노드를 리턴한다.
     * init-param 노드가 없을 경우 null을 리턴한다.
     * context-param이 없을 경우 null을 리턴한다.
     */
    public static Node getContextParam(Node rootNode, Node servletNode) {
    	
    	try {
    		if(null != servletNode ){
    			
    			String paramName = "";
    			/**
    			 *  servletNode 하위에 init-param 및 param-name 노드가 있는지 확인한다.
    			 *  존재한다면 param-name을 가져온다.
    			 */
    			if(XmlUtil.existNode(servletNode, "./init-param") && XmlUtil.existNode(servletNode, "./init-param/param-name")){
    				paramName = XmlUtil.getNode(servletNode, "./init-param/param-name").getFirstChild().getNodeValue();
    			}
    			
    			if(!"".equals(paramName)){
    				
    				// param name을 이용하여 context-param을 가져온다.
    				NodeList contextParamList = XmlUtil.getNodeList(rootNode, "/web-app/context-param");
    				if(contextParamList != null){
    					for(int i=0 ; contextParamList.getLength() > i ; i++) {
    						Node context = contextParamList.item(i);
    						if(paramName.equals(XmlUtil.getNode(context, "./param-name").getFirstChild().getNodeValue())) return context;
    					}
    				}
    			}
    		}
    		
    	} catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    	return null;
    }
    
    /**
     * pom.xml에 대한 append 및 수정을 수행한다.
     * @param container
     * @param fname
     * @param content
     * @param monitor
     * @return
     */
    public static void modifyPom(IContainer container, String fname, InputStream content, IProgressMonitor monitor){
    	
    	try {
	    	//AS-IS 시스템의 pom.xml 파일을 읽어 노드 리스트를 구성한다.
			Node rootNode = XmlUtil.getRootNode(new File(container.getProject().getFile("pom.xml").getLocation().toOSString()));
			NodeList oriNodes = XmlUtil.getNodeList(rootNode, "/project/dependencies/dependency");
	    	
			File file = new File("pom.xml");
			OutputStream os = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len=0;
			
			while((len = content.read(buf))>0){
				os.write(buf, 0, len);
			}
			
			os.close();
			content.close();
			
			//컴포넌트의 pom.xml 파일을 읽어 노드 리스트를 구성한다.
			Node targetNode = XmlUtil.getRootNode(file);
			NodeList targetNodes = XmlUtil.getNodeList(targetNode, "/project/dependencies/dependency");
			String comment = "";
			
			if(targetNodes != null){
				
				for(int i=0 ; targetNodes.getLength() > i ; i++) {
					
					//컴포넌트의 아티펙트 아이디와 버전을 가져온다.
					Node targetDependency = targetNodes.item(i);
					Node targetArtifactId = XmlUtil.getNode(targetDependency, "./artifactId");
					Node targetVersion = XmlUtil.getNode(targetDependency, "./version");
					boolean noExist = true;	// 아티펙트 존재 여부 파악
					
					//컴포넌트와 AS-IS 시스템의 아티팩트 및 버전을 확인한다.
					for(int j=0 ; oriNodes.getLength() > j ; j++){
						Node oriDependency = oriNodes.item(j);
						Node oriArtifactId = XmlUtil.getNode(oriDependency, "./artifactId");	// AS-IS 아티펙트의 ID
						
						if(oriArtifactId != null && targetArtifactId != null && oriArtifactId.getFirstChild().getNodeValue().equals(targetArtifactId.getFirstChild().getNodeValue())) {
							noExist = false;		// 아티펙트 존재 확인
							Node oriVersion = XmlUtil.getNode(oriDependency, "./version");		// AS-IS 아티펙트의 버전
							
							if(oriVersion != null && targetVersion != null && !oriVersion.getFirstChild().getNodeValue().equals(targetVersion.getFirstChild().getNodeValue())){
								//아티펙트 버전이 틀릴경우 주석처리 후 추가한다.
								
								StringWriter sw = new StringWriter();
								TransformerFactory tfFac = TransformerFactory.newInstance();
								Transformer tf = tfFac.newTransformer();
								tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
								tf.transform(new DOMSource(targetDependency), new StreamResult(sw));
								comment += "\n" + sw.toString() + "\n\n";
								
								//주석을 추가한다.
								XmlUtil.addComment(rootNode, "/project", comment, "\n\t", "\n\t");
								
								//추가된 comment를 초기화 한다.
								comment = "";
								
//							}else {	/*아티펙트 버전이 같으므로 아무런 작업을 하지 않는다.*/	
							}
						}
					}
					
					if(noExist) {
						// 노드를 추가한다.
						StringWriter sw = new StringWriter();
						TransformerFactory tfFac = TransformerFactory.newInstance();
						Transformer tf = tfFac.newTransformer();
						tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						tf.transform(new DOMSource(targetDependency), new StreamResult(sw));
						String newNode = "\n\n" + sw.toString() + "\n\n";
						
						// 새 노드를 추가한다.
						XmlUtil.addNode(rootNode, "/project/dependencies", newNode, "\n\t", "\n\t");
					}
					
				}
				
				// 파일을 생성한다.
				IFile originf = container.getFile(new Path("pom.xml"));
				String xmlStr = XmlUtil.getXmlString(rootNode, "/");
				InputStream inStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
				originf.setContents(inStream, true, false, monitor);
			}
			
		} catch (Exception e) {
			CommngtLog.logError(e);
		}

    }
    
    /**
     * Node를 String으로 바꾸어 리턴한다.
     * @param node
     * @return
     */
    public static String nodeToString(Node node) {
    	String newNode = "";

    	try {
    		StringWriter sw = new StringWriter();
    		TransformerFactory tfFac = TransformerFactory.newInstance();
    		Transformer tf = tfFac.newTransformer();
    		tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    		tf.transform(new DOMSource(node), new StreamResult(sw));
    		newNode += "\n\n" + sw.toString() + "\n\n";
    	} catch (TransformerException e) {
    		CommngtLog.logError(e);
    	}
    	
    	return newNode;
    }
    
    /**
     * xml파일에 messageSource라는 id를 가지는 bean이 있는지 체크한다.
     * @param xmlFile
     * @return
     */
    public static boolean checkMessageSource(InputStream xmlFile) {
    	
    	try {
    		Document doc = makeDocument(xmlFile);
    		Node rootNode = doc.getDocumentElement();
    		NodeList beans = XmlUtil.getNodeList(rootNode, "/beans/bean");
    		if(beans.getLength()>0) {
    			for(int i=0 ; beans.getLength() > i ; i++) {
    				if( XmlUtil.existNode(beans.item(i), "./@id") && "messageSource".equals(XmlUtil.getNode(beans.item(i), "./@id").getNodeValue())) return true;
    			}
    		}
			
		} catch (Exception e) {
			CommngtLog.logError(e);
		}
    	
    	return false;
    }
    
    // 기존 시스템에 common-context.xml에 들어가는 내용이 있는지 없는지 확인하는 값.
	// 기존 시스템에 messageSource 노드가 없을경우 해당 common-context.xml의 내용이 없는것으로 판단한다.
    static boolean asIsCommonExist = false;
    
    /**
     * context-common.xml에 대해 messageSource에 한 수정을 수행한다.
     * @param container	project
     * @param fname		입력받은 xml파일의 위치
     * @param content	입력받은 xml파일
     * @param monitor
     * @throws CoreException
     */
    public static boolean modifyMessageSource(final IContainer container, final String fname, InputStream content, final IProgressMonitor monitor) throws CoreException {
    	
    	// 기존 시스템의 파일 존재 유무를 defalut값으로 바꾼다.
    	asIsCommonExist = false;
    	
    	// 1. 새롭게 입력받은 파일의 형태를 확인한다.
    	// 1.1 파일에 messageSource bean의 존재유무를 판단한다.
    	boolean beanExist = false;
    	final Document doc = makeDocument(content);
    	
    	try {
    		Node rootNode = doc.getDocumentElement();
    		NodeList beans = XmlUtil.getNodeList(rootNode, "/beans/bean");
    		if(beans.getLength()>0) {
    			for(int i=0 ; beans.getLength() > i ; i++) {
    				if( XmlUtil.existNode(beans.item(i), "./@id") && "messageSource".equals(XmlUtil.getNode(beans.item(i), "./@id").getNodeValue())) beanExist = true;
    			}
    		}
			
		} catch (Exception e) {
			CommngtLog.logError(e);
		}
    	
    	// 1.2 존재 할 경우 AS-IS 시스템의 파일을 읽어 액션을 취한다.
    	if(beanExist){
    		
    		// 2. AS-IS 시스템의 파일을 찾아 원하는 로직을 수행한다.
    		final IContainer realContainer = container.getProject().getParent();
    		
    		container.getProject().accept(new IResourceVisitor() {
    			
    			// 프로젝트를 순환하면서 *.xml인 파일을 찾는다. sql 파일 제외
    			@SuppressWarnings("unused")
				public boolean visit(IResource resource)   throws CoreException {
    				
    				boolean nextWork = true;
    				if(resource.getType() == IResource.FILE && resource.getName().endsWith(".xml") && resource.getName().toUpperCase().indexOf("SQL")==-1 && resource.getFullPath().toOSString().indexOf("\\classes\\")==-1 ){
    					try {
    						DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    						
    						docBF.setValidating(false);
    						docBF.setNamespaceAware(false);
    						docBF.setFeature("http://xml.org/sax/features/validation", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    						docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    						docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    						
    						DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    						
    						// AS-IS 대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
    						IFile file = realContainer.getFile(resource.getFullPath());
    						
    						Node messageNode = null;
    						
    						if(file.isAccessible()) {
    							//AS-IS 시스템내 파일의 document
    							Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
    							Node targetRootNode = targetDoc.getDocumentElement();
    							
    							Document newDoc = doc;
    							
    							// 새로운 파일의 rootNode
    							Node newRootNode = newDoc.getDocumentElement();
    							
    							// 파일에 /beans/bean이 있을경우 노드 리스트를 가져온다.
    							if(XmlUtil.existNode(targetRootNode, "/beans/bean")) {
    								
    								//bean list를 가져온다.
    								NodeList beanList = XmlUtil.getNodeList(targetRootNode, "/beans/bean");
    								if(beanList.getLength() > 0) {
    									messageNode = findNodeById(beanList, "messageSource");
    									//노드가 있을경우 - 노드를 수정한다.
    									if(messageNode != null) {
    										
    										//기존의 시스템에 context내용이 있다.
    										asIsCommonExist = true;
    										
    										//list/value가 있을경우
    										if(XmlUtil.existNode(messageNode, "./property[@name='basenames']/list/value")) {
    											NodeList values = XmlUtil.getNodeList(messageNode, "./property[@name='basenames']/list/value");
    											if(values != null && values.getLength()>0) {
    												String valuesToString = "";
    												for(int v=0 ; values.getLength() > v ; v++) {
    													valuesToString += values.item(v).getFirstChild().getNodeValue() + ";;;";
    												}
    												NodeList newNodes = XmlUtil.getNodeList(newRootNode, "/beans/bean[@id='messageSource']/property[@name='basenames']/list/value");
    												if(newNodes != null && newNodes.getLength()>0) {
    													for(int n=0 ; newNodes.getLength() > n ; n++){
    														
    														//노드의 중복을 체크한다.
    														if(valuesToString.indexOf(newNodes.item(n).getFirstChild().getNodeValue()) == -1) {
    															//노드를 추가한다.
    															XmlUtil.addNode(messageNode, "./property[@name='basenames']/list", "<value>" + newNodes.item(n).getFirstChild().getNodeValue() + "</value>", "\t\t\t\t", "\n");
     															
    															//valuesToString값에 추가된 노드 값을 추가한다.
    															valuesToString += newNodes.item(n).getFirstChild().getNodeValue() + ";;;";
    														}
    													}
    												}
    											}
    											
    											XmlUtil.removeNode(newRootNode, "/beans/bean[@id='messageSource']");
    											
											// value가 있을경우 - list없음(단일값)
    										} else if(XmlUtil.existNode(messageNode, "./property[@name='basename']/value")){
    											String value = XmlUtil.getNode(messageNode, "./property[@name='basename']/value").getFirstChild().getNodeValue();
    											if(!"".equals(value)) {
    												String newValues = XmlUtil.getXmlString(newRootNode, "/beans/bean[@id='messageSource']/property[@name='basenames']");
    												if(newValues.indexOf(value)>-1){
    													// AS-IS 시스템에 새롭게 추가하는 값이 포함되어있으므로, 새로운 노드를 추가한다.
    													// 기존의 property[@name='basename'] 노드를 삭제하고 property[@name='basenames'] 노드를 추가한다.
    													XmlUtil.removeNode(targetRootNode, "/beans/bean[@id='messageSource']/property[@name='basename']");
    													XmlUtil.addNode(targetRootNode, "/beans/bean[@id='messageSource']", newValues, "\t\t\t\t", "\n");
    												} else {
    													//AS-IS 시스템에 노드값이 없으므로 새로운 노드를 추가할때 value노드를 추가한다.
    													XmlUtil.removeNode(targetRootNode, "/beans/bean[@id='messageSource']/property[@name='basename']");
    													XmlUtil.addNode(targetRootNode, "/beans/bean[@id='messageSource']", newValues, "\t\t\t\t", "\n");
    													XmlUtil.addNode(targetRootNode, "/beans/bean[@id='messageSource']/property[@name='basenames']/list", "<value>" + value + "</value>", "\t\t\t\t", "\n");
    												}
    												
    												// 새로운 파일의 messageSoruce bean을 삭제한다.
    												XmlUtil.removeNode(newRootNode, "/beans/bean[@id='messageSource']");
    											}
    										}
    										
    										String filePath = file.getFullPath().toOSString();
    										
    										//AS-IS 시스템의 파일을 수정한다.
    		    							String xmlStr = XmlUtil.getXmlString(targetRootNode, "/");
    		    							InputStream inStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
    		    							file.setContents(inStream, true, false, monitor);
    		    							
    		    							if(!(resource.getFullPath().toString().indexOf(fname)>-1)){
    		    								// 새로운 파일을 복사한다.
    		    								String newXmlStr = XmlUtil.getXmlString(newRootNode, "/");
    		    								InputStream newInStream = new ByteArrayInputStream(newXmlStr.getBytes("UTF-8"));
    		    								ResourceUtils.createFolderFile(container, fname, newInStream, monitor);
    		    							}
    		    							
    		    							//더이상 작업을 수행하지 않는다.
    		    							nextWork = false;
    										
    									//노드가 없을경우 - 노드를 추가한다.
//    									} else {
    										
    									}
    								}
    								
    							}
    							
    							
    						}
    					} catch (Exception e) {
    						CommngtLog.logError(e);
    					}
    				}
    				return nextWork;
    			}
    			
    		});
    		
    	}
    	
    	return asIsCommonExist;
    	
    }
    
    
    // 기존 시스템에 common-context.xml에 들어가는 내용이 있는지 없는지 확인하는 값.
	// 기존 시스템에 leaverTrace 노드가 없을경우 해당 common-context.xml의 내용이 없는것으로 판단한다.
    static boolean leaverTraceExist = false;
    /**
     * context-common.xml에 대해 LeaverTrace에 한 수정을 수행한다.
     * @param container	project
     * @param fname		입력받은 xml파일의 위치
     * @param content	입력받은 xml파일
     * @param monitor
     * @throws CoreException
     */
    public static boolean modifyLeaverTraceSource(final IContainer container, final String fname, InputStream content, final IProgressMonitor monitor, final boolean newComp) throws CoreException {
    	
    	// 기존 시스템의 파일 존재 유무를 defalut값으로 바꾼다.
    	leaverTraceExist = false;
    	
    	// 1. 새롭게 입력받은 파일의 형태를 확인한다.
    	// 1.1 파일에 messageSource bean의 존재유무를 판단한다.
    	boolean beanExist = false;
    	final Document doc = makeDocument(content);
    	
    	try {
    		Node rootNode = doc.getDocumentElement();
    		NodeList beans = XmlUtil.getNodeList(rootNode, "/beans/bean");
    		if(beans.getLength()>0) {
    			for(int i=0 ; beans.getLength() > i ; i++) {
    				if( XmlUtil.existNode(beans.item(i), "./@id") && "leaveaTrace".equals(XmlUtil.getNode(beans.item(i), "./@id").getNodeValue())) beanExist = true;
    			}
    		}
    		
    	} catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    	
    	// 1.2 존재 할 경우 AS-IS 시스템의 파일을 읽어 액션을 취한다.
    	if(beanExist){
    		
    		// 2. AS-IS 시스템의 파일을 찾아 원하는 로직을 수행한다.
    		final IContainer realContainer = container.getProject().getParent();
    		
    		container.getProject().accept(new IResourceVisitor() {
    			
    			// 프로젝트를 순환하면서 *.xml인 파일을 찾는다. sql 파일 제외
    			public boolean visit(IResource resource)   throws CoreException {
    				
    				boolean nextWork = true;
    				if(resource.getType() == IResource.FILE && resource.getName().endsWith(".xml") && resource.getName().toUpperCase().indexOf("SQL")==-1 && resource.getFullPath().toOSString().indexOf("\\classes\\")==-1){
    					try {
    						DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    						
    						docBF.setValidating(false);
    						docBF.setNamespaceAware(false);
    						docBF.setFeature("http://xml.org/sax/features/validation", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    						docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    						docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    						
    						DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    						
    						// AS-IS 대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
    						IFile file = realContainer.getFile(resource.getFullPath());
    						
    						Node leaveaNode = null;
    						
    						if(file.isAccessible()) {
    							//AS-IS 시스템내 파일의 document
    							Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
    							Node targetRootNode = targetDoc.getDocumentElement();
    							
    							Document newDoc = doc;
    							
    							// 새로운 파일의 rootNode
    							Node newRootNode = newDoc.getDocumentElement();
    							
    							// 파일에 /beans/bean이 있을경우 노드 리스트를 가져온다.
    							if(XmlUtil.existNode(targetRootNode, "/beans/bean")) {
    								
    								//bean list를 가져온다.
    								NodeList beanList = XmlUtil.getNodeList(targetRootNode, "/beans/bean");
    								if(beanList.getLength() > 0) {
    									leaveaNode = findNodeById(beanList, "leaveaTrace");
    									//노드가 있을경우 - 노드를 수정한다.
    									if(leaveaNode != null) {
    										
    										//기존의 시스템에 context내용이 있다.
    										leaverTraceExist = true;
    										
    										//property/list/ref 노드가 있을경우 XmlUtil.getNode(beans.item(i), "./@id").getNodeValue()
    										if(XmlUtil.existNode(targetRootNode, "/beans/bean[@id='leaveaTrace']/property/list/ref")) {
    											NodeList values = XmlUtil.getNodeList(targetRootNode, "/beans/bean[@id='leaveaTrace']/property/list/ref");
    											if(values != null && values.getLength()>0) {
    												String valuesToString = "";
    												for(int v=0 ; values.getLength() > v ; v++) {
    													valuesToString += XmlUtil.getNode(values.item(v), "./@bean").getNodeValue() + ";;;";
    												}
    												NodeList newNodes = XmlUtil.getNodeList(newRootNode, "/beans/bean[@id='leaveaTrace']/property/list/ref");
    												if(newNodes != null && newNodes.getLength()>0) {
    													for(int n=0 ; newNodes.getLength() > n ; n++){
    														
    														//노드의 중복을 체크한다.
    														if(valuesToString.indexOf(XmlUtil.getNode(newNodes.item(n), "./@bean").getNodeValue()) == -1) {
    															//노드를 추가한다.
    															XmlUtil.addNode(targetRootNode, "/beans/bean[@id='leaveaTrace']/property/list", "<ref bean=\"" + XmlUtil.getNode(newNodes.item(n), "./@bean").getNodeValue() +"\"/>", "\t\t\t\t", "\n");
    															
    															//valuesToString값에 추가된 노드 값을 추가한다.
    															valuesToString += XmlUtil.getNode(newNodes.item(n), "./@bean").getNodeValue() + ";;;";
    														}
    													}
    												}
    											}
    											
    											XmlUtil.removeNode(newRootNode, "/beans/bean[@id='leaveaTrace']");
    											
    										}
    										
    										//기존에 컴포넌트가 있으므로 messageSource를 삭제한다.(선수 작업이 진행됨.)
    										if(newComp){
    											XmlUtil.removeNode(newRootNode, "/beans/bean[@id='messageSource']");
    										}
    										
    										//AS-IS 시스템의 파일을 수정한다.
    										String xmlStr = XmlUtil.getXmlString(targetRootNode, "/");
    										InputStream inStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
    										file.setContents(inStream, true, false, monitor);
    										
    										if(!(resource.getFullPath().toString().indexOf(fname)>-1)){
    											// 새로운 파일을 복사한다.
    											String newXmlStr = XmlUtil.getXmlString(newRootNode, "/");
    											InputStream newInStream = new ByteArrayInputStream(newXmlStr.getBytes("UTF-8"));
    											ResourceUtils.createFolderFile(container, fname, newInStream, monitor);
    										}
    										
    										//더이상 작업을 수행하지 않는다.
    										nextWork = false;
    										
    									}
    								}
    								
    							}
    							
    							
    						}
    					} catch (Exception e) {
    						CommngtLog.logError(e);
    					}
    				}
    				return nextWork;
    			}
    			
    		});
    		
    	}
    	
    	return leaverTraceExist;
    	
    }
    
    
    // 기존 시스템에 common-validator.xml를 수정했는지 확인하는 값
	// 기존 시스템에 수정하지 않았다면 기존에 validator가 없는 것으로 판단, 기존 파일을 복사하게 된다.
    static boolean modifyValidator = false;
    
    /**
     * context경로내의 파일을 확인하여 validator 노드를 수정한다.
     * @param container
     * @param fname
     * @param content
     * @param monitor
     * @param newComp
     * @return
     * @throws CoreException
     */
    public static boolean copyValidator(final IContainer container, final String fname, InputStream content, final IProgressMonitor monitor, ComResourceUtilVO comVo) throws CoreException {
    	
    	// 1. 새롭게 입력받은 파일의 형태를 확인한다.
    	// 1.1 파일에 validatorFactory bean의 존재유무를 판단한다.
    	boolean beanExist = false;
    	final Document doc = makeDocument(content);
    	
    	try {
    		Node rootNode = doc.getDocumentElement();
    		NodeList beans = XmlUtil.getNodeList(rootNode, "/beans/bean");
    		if(beans.getLength()>0) {
    			for(int i=0 ; beans.getLength() > i ; i++) {
    				if( XmlUtil.existNode(beans.item(i), "./@id") && "validatorFactory".equals(XmlUtil.getNode(beans.item(i), "./@id").getNodeValue())) beanExist = true;
    			}
    		}
    		
    	} catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    	
    	final ComResourceUtilVO vo = comVo;
    	
    	// 1.2 존재 할 경우 AS-IS 시스템의 파일을 읽어 액션을 취한다.
    	if(beanExist){
    		
    		// 2. AS-IS 시스템의 파일을 찾아 원하는 로직을 수행한다.
    		final IContainer realContainer = container.getProject().getParent();
    		
    		container.getProject().accept(new IResourceVisitor() {
    			
    			// 프로젝트를 순환하면서 *.xml인 파일을 찾는다. sql 파일 제외
    			public boolean visit(IResource resource)   throws CoreException {
    				
    				boolean nextWork = true;
    				
    				String contextLocation = vo.getContextUrlLocation();
    				
    				boolean isLocation = false;
    				
    				if(contextLocation != null) {
    					
    					//space를 제거한다.
    					contextLocation = contextLocation.replaceAll(" ", "");
    					//*를 제거한다.
    					contextLocation = contextLocation.replaceAll("\\*", "");
    					//.xml을 제거한다.
    					contextLocation = contextLocation.replaceAll(".xml", "");
    					//tab을 제거한다.
    					contextLocation = contextLocation.replaceAll("\t", "");
    					//enter를 제거한다.
    					contextLocation = contextLocation.replaceAll("\n", "");
    					// /를 \\로 치환한다.
    					contextLocation = contextLocation.replaceAll("/", "\\\\");
    					// classpath를 제거한다.
    					contextLocation = contextLocation.replaceAll("classpath:", "");
    					
    					String context[] = contextLocation.split(",");
    					for(int i=0 ; context.length > i ; i++) {
    						if(resource.getFullPath().toOSString().indexOf(context[i])>-1){
    							isLocation = true;
    						}
    					}
    					
    				}
    				
    				if(resource.getType() == IResource.FILE && resource.getName().endsWith(".xml") && resource.getName().toUpperCase().indexOf("SQL")==-1 && resource.getFullPath().toOSString().indexOf("\\classes\\")==-1 && isLocation){
    					try {
    						DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    						
    						docBF.setValidating(false);
    						docBF.setNamespaceAware(false);
    						docBF.setFeature("http://xml.org/sax/features/validation", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    						docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    						docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    						docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    						
    						DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    						
    						// AS-IS 대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
    						IFile file = realContainer.getFile(resource.getFullPath());
    						
    						Node leaveaNode = null;
    						
    						if(file.isAccessible()) {
    							//AS-IS 시스템내 파일의 document
    							Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
    							Node targetRootNode = targetDoc.getDocumentElement();
    							
    							Document newDoc = doc;
    							
    							// 새로운 파일의 rootNode
    							Node newRootNode = newDoc.getDocumentElement();
    							
    							// 파일에 /beans/bean이 있을경우 노드 리스트를 가져온다.
    							if(XmlUtil.existNode(targetRootNode, "/beans/bean")) {
    								
    								//bean list를 가져온다.
    								NodeList beanList = XmlUtil.getNodeList(targetRootNode, "/beans/bean");
    								if(beanList.getLength() > 0) {
    									leaveaNode = findNodeById(beanList, "validatorFactory");
    									//노드가 있을경우 - 노드를 수정한다.
    									if(leaveaNode != null) {
    										
    										//property/list/ref 노드가 있을경우 XmlUtil.getNode(beans.item(i), "./@id").getNodeValue()
    										if(XmlUtil.existNode(targetRootNode, "/beans/bean[@id='validatorFactory']/property/list/value")) {
    											NodeList values = XmlUtil.getNodeList(targetRootNode, "/beans/bean[@id='validatorFactory']/property/list/value");
    											if(values != null && values.getLength()>0) {
    												String valuesToString = "";
    												for(int v=0 ; values.getLength() > v ; v++) {
    													valuesToString += values.item(v).getFirstChild().getNodeValue() + ";;;";
    												}
    												NodeList newNodes = XmlUtil.getNodeList(newRootNode, "/beans/bean[@id='validatorFactory']/property/list/value");
    												if(newNodes != null && newNodes.getLength()>0) {
    													for(int n=0 ; newNodes.getLength() > n ; n++){
    														
    														//노드의 중복을 체크한다.
    														if(valuesToString.indexOf(newNodes.item(n).getFirstChild().getNodeValue()) == -1) {
    															//노드를 추가한다.
    															XmlUtil.addNode(targetRootNode, "/beans/bean[@id='validatorFactory']/property/list", "<value>"+newNodes.item(n).getFirstChild().getNodeValue()+"</value>", "\t\t\t\t", "\n");
    															
    															//valuesToString값에 추가된 노드 값을 추가한다.
    															valuesToString += newNodes.item(n).getFirstChild().getNodeValue() + ";;;";
    														}
    													}
    												}
    											}
    											
    											XmlUtil.removeNode(newRootNode, "/beans/bean[@id='validatorFactory']");
    											XmlUtil.removeNode(newRootNode, "/beans/bean[@id='beanValidator']");
    											
    										}
    										
    										modifyValidator = true;
    										
    										//AS-IS 시스템의 파일을 수정한다.
    										String xmlStr = XmlUtil.getXmlString(targetRootNode, "/");
    										InputStream inStream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
    										file.setContents(inStream, true, false, monitor);
    										
    										if(!(resource.getFullPath().toString().indexOf(fname)>-1)){
    											// 새로운 파일을 복사한다.
    											String newXmlStr = XmlUtil.getXmlString(newRootNode, "/");
    											InputStream newInStream = new ByteArrayInputStream(newXmlStr.getBytes("UTF-8"));
    											ResourceUtils.createFolderFile(container, fname, newInStream, monitor);
    										}
    										
    										//더이상 작업을 수행하지 않는다.
    										nextWork = false;
    										
    									}
    								}
    								
    							}
    							
    							
    						}
    					} catch (Exception e) {
    						CommngtLog.logError(e);
    					}
    				}
    				return nextWork;
    			}
    			
    		});
    		
    	}
    	
    	return modifyValidator;
    	
    }
    
    /**
     * Nodelist에서 특정 아이디를 가지고 있는 노드를 찾아온다.
     * 없을경우 null을 리턴한다.
     * @param nodeList
     * @param id
     * @return
     */
    public static Node findNodeById(NodeList nodeList, String id) throws Exception {
    	Node node = null;
    	if(nodeList.getLength() > 0) {
    		for(int i=0 ; nodeList.getLength() > i ; i++) {
    			node = nodeList.item(i);
    			if(XmlUtil.existNode(node, "./@id") && id.equals(XmlUtil.getNode(node, "./@id").getNodeValue())){
    				return node;
    			}
    		}
    	}
    	return null;
    }

    /**
     * web-app/servlet, web-app/servlet-mapping 노드가 있는지 확인한다.
     * @param rootNode
     * @return
     */
    public static boolean checkServletMapping(Node rootNode) {
    	boolean servletExist = false;
    	try {
    		if(XmlUtil.existNode(rootNode, "/web-app")) {
    			if(XmlUtil.existNode(rootNode, "/web-app/servlet") && XmlUtil.existNode(rootNode, "/web-app/servlet-mapping")){
    				servletExist = true;
    			}
    		}
    		
    	} catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	return servletExist;
    }
    
    /**
     * 현재 년도, 월, 일을 return 하는 메소드
     * @return
     */
    public static String getCurrentTime(){
    	
    	String curT = "";
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", new Locale(System.getProperty("user.language")));
    	
    	Calendar cal = Calendar.getInstance();
    	
    	curT = format.format(cal.getTime());
    	    	
    	return curT;
    }
    
    /**
     * servlet의 servlet name이 있을경우 새로운 이름으로 바꾼다.
     * @param rootNode
     * @param servletNode
     * @return
     */
    public static Node changeDuplicateServletNode(Node rootNode, Node servletNode) {
    	
    	try {
    		
    		String existedServlet = "";
    		
    		NodeList servletList = XmlUtil.getNodeList(rootNode, "/web-app/servlet/servlet-name");
    		
    		// 기 설치된 서블릿 리스트를 구성한다.
    		if(servletList.getLength() > 0) {
    			for(int i=0 ; servletList.getLength() > i ; i++){
    				existedServlet += ((Node)servletList.item(i)).getFirstChild().getNodeValue() + ";;;";
    			}
    		}
    		
    		String servletName = (XmlUtil.getNode(servletNode, "./servlet-name")).getFirstChild().getNodeValue();
    		
    		// 새로운 servletName을 생성한다.
    		boolean servletExist = false;
    		while (!servletExist) {
				
    			if(existedServlet != null && existedServlet.indexOf(servletName) > -1){
    				servletName += "_1";
    			} else {
    				(XmlUtil.getNode(servletNode, "./servlet-name")).getFirstChild().setNodeValue(servletName);
    				servletExist = true;
    			}
				
			}
    	
    	} catch (Exception e) {
    		CommngtLog.logError(e);
		}
    	
    	
    	return servletNode;
    }
    
    
    /**
     * servlet-mapping에서 servlet name이 있을경우 새로운 이름으로 바꾼다.
     * @param rootNode
     * @param servletNode
     * @return
     */
    public static Node changeDuplicateServletMappingNode(Node rootNode, Node servletMappingNode) {
    	
    	try {
    		
    		String existedServlet = "";
    		
    		NodeList servletList = XmlUtil.getNodeList(rootNode, "/web-app/servlet-mapping/servlet-name");
    		
    		// 기 설치된 서블릿 리스트를 구성한다.
    		if(servletList.getLength() > 0) {
    			for(int i=0 ; servletList.getLength() > i ; i++){
    				existedServlet += ((Node)servletList.item(i)).getFirstChild().getNodeValue() + ";;;";
    			}
    		}
    		
    		String servletName = (XmlUtil.getNode(servletMappingNode, "./servlet-name")).getFirstChild().getNodeValue();
    		
    		// 새로운 servletName을 생성한다.
    		boolean servletExist = false;
    		while (!servletExist) {
    			
    			if(existedServlet != null && existedServlet.indexOf(servletName) > -1){
    				servletName += "_1";
    			} else {
    				(XmlUtil.getNode(servletMappingNode, "./servlet-name")).getFirstChild().setNodeValue(servletName);
    				servletExist = true;
    			}
    			
    		}
    		
    	} catch (Exception e) {
    		CommngtLog.logError(e);
    	}
    	
    	
    	return servletMappingNode;
    }

    /**
     * web.xml을 찾아 해당 web.xml이 사용하는 dispatcher-servlet 파일의 위치 및 context의 경로를 가져온다.
     * @param project : 해당 프로젝트
     * @return
     */
    public static ComResourceUtilVO getDispatcherAndContextLocation(IJavaProject project){
    	
    	final IContainer realContainer = project.getProject().getParent();
    	
    	final ComResourceUtilVO comVo = new ComResourceUtilVO();
    		
    	try {
			project.getProject().accept(new IResourceVisitor() {
				
				// 프로젝트를 순환하면서 web.xml인 파일을 찾는다.
				public boolean visit(IResource resource)   throws CoreException {
					
					boolean nextWork = true;
					if(resource.getType() == IResource.FILE && resource.getName().equals("web.xml")){
						
						try {
							DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
							
							docBF.setValidating(false);
							docBF.setNamespaceAware(false);
							docBF.setFeature("http://xml.org/sax/features/validation", false);
							docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
							docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
							docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
							docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
							
							DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
							
							// 대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
							IFile file = realContainer.getFile(resource.getFullPath());
							
							if(file.isAccessible()) {
								//시스템내 파일의 document
								Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
								Node targetRootNode = targetDoc.getDocumentElement();
								
								//web.xml의 *.do패턴의 servlet 노드를 가져온다.
								String doPattern = "*.do";
								Node servletNode = getServlet(targetRootNode, doPattern);
								
								if(servletNode != null) {
									//servlet노드를 이용하여 dispatcherServlet 설정 정보가 담기 파일의 패턴을 가져온다.
									String dispaterUrlPattern = XmlUtil.getNode(servletNode, "./init-param/param-value").getFirstChild().getNodeValue();
									
									//vo에 담는다.
									comVo.setUrlLocation(dispaterUrlPattern);
								}
								
								/** Context의 경로를 가져온다. */
								String contextUrlPattern = null;
								//servlet노드를 이용하여 dispatcherServlet 설정 정보가 담기 파일의 패턴을 가져온다.
    							if(XmlUtil.existNode(targetRootNode, "/web-app/context-param/param-value")){
    								contextUrlPattern = XmlUtil.getNode(targetRootNode, "/web-app/context-param/param-value").getFirstChild().getNodeValue();
    							}
								
								//vo에 담는다.
								comVo.setContextUrlLocation(contextUrlPattern);
								
								//더이상 작업을 수행하지 않는다.
    							nextWork = false;
								
							}
						} catch (Exception e) {
							CommngtLog.logError(e);
						}
					}
					return nextWork;
				}
				
			});
			
		} catch (CoreException e) {
			CommngtLog.logError(e);
		}
    			
    	return comVo;
    }
    
    
    /**
     * dispatcher 경로에서 viewResolver에 있는 prefix를 찾는다. 
     * @param project
     * @param vo
     * @return
     */
    public static ComResourceUtilVO getDispatcherPrefixLocation(IJavaProject project, ComResourceUtilVO vo){
    	
    	final IContainer realContainer = project.getProject().getParent();
    	
    	final ComResourceUtilVO comVo = new ComResourceUtilVO();
    	
    	String dispatcherLocation = null;
    	
    	if(vo != null && vo.getUrlLocation() != null){
    		
    		//space를 제거한다.
    		dispatcherLocation = vo.getUrlLocation().replaceAll(" ", "");
    		
    		//*를 제거한다.
    		dispatcherLocation = dispatcherLocation.replaceAll("\\*", "");
    		
    		//.xml을 제거한다.
    		dispatcherLocation = dispatcherLocation.replaceAll(".xml", "");
    		
    		//tab을 제거한다.
    		dispatcherLocation = dispatcherLocation.replaceAll("\t", "");
    		
    		//enter를 제거한다.
    		dispatcherLocation = dispatcherLocation.replaceAll("\n", "");
    		
    		// /를 \\로 치환한다.
    		dispatcherLocation = dispatcherLocation.replaceAll("/", "\\\\");
    	
    	
    		comVo.setLocationPattern(dispatcherLocation.split(","));
    	
    		try {
    			project.getProject().accept(new IResourceVisitor() {
	    			
    				//프로젝트를 순환하면서 web.xml인 파일을 찾는다.
    				public boolean visit(IResource resource)   throws CoreException {
	    				
    					boolean nextWork = true;
    					
    					String[] dispatcherLocationPattern = comVo.getLocationPattern();
    					 
    					if(resource.getType() == IResource.FILE && resource.getName().endsWith(".xml")){
    						
    						/**
    						 * DispatcherServlet의 경로에 있는 파일인지 파악한다.
    						 */
    						boolean isDiapatcher = false;
    						
    						for(int i=0 ; dispatcherLocationPattern.length > i ; i ++){
    							if(resource.getFullPath().toOSString().indexOf(dispatcherLocationPattern[i])>-1){
    								isDiapatcher = true;
    							}
    						}
    						
    						
    						/**
    						 * DispatcherServlet Location 패턴이 일치 할 경우 prefix값을 찾는다.
    						 */
    						if(isDiapatcher){
    							
    							try {
    								DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    								
    								docBF.setValidating(false);
    								docBF.setNamespaceAware(false);
    								docBF.setFeature("http://xml.org/sax/features/validation", false);
    								docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    								docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    								docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    								docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    								
    								DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    								
    								//대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
    								IFile file = realContainer.getFile(resource.getFullPath());
    								
    								if(file.isAccessible()) {
    									//시스템내 파일의 document
    									Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
    									Node targetRootNode = targetDoc.getDocumentElement();
    									
    									//bean list를 가져와 p:prefix 속성값을 가진 녀석을 찾아 그 값을 가져온다.
    									String propertyPattern = "prefix";
    									NodeList beanNodes = XmlUtil.getNodeList(targetRootNode, "/beans/bean");
    									if(beanNodes != null && beanNodes.getLength()>0) {
    										
    										for(int j=0 ; beanNodes.getLength() > j ; j++){
    											if(XmlUtil.existNode(beanNodes.item(j), "./@"+propertyPattern)){
    												//vo에 prefix를 담는다.
    												comVo.setPrefixPattern(XmlUtil.getNode(beanNodes.item(j), "./@"+propertyPattern).getNodeValue());
    												
    												//더이상 작업을 수행하지 않는다.
    												nextWork = false;
    											}
    										}
    									}
    									
    								}
    							} catch (Exception e) {
    								CommngtLog.logError(e);
    							}
    							
    						}else {
    							return nextWork;
    						}
	    					
	    				}
	    				return nextWork;
	    			}
	    			
	    		});
	    		
	    	} catch (CoreException e) {
	    		CommngtLog.logError(e);
	    	}
    	}
    	return comVo;
    }
    
    /**
     * dispatcher 경로에서 xpathNode와 일치하는 bean이 있는지 확인한다. 
     * @param project
     * @param vo
     * @return
     */
    public static ComResourceUtilVO checkExistBean(IJavaProject project, ComResourceUtilVO vo){
    	
    	final IContainer realContainer = project.getProject().getParent();
    	
    	final ComResourceUtilVO comVo = vo;
    	
    	comVo.setExistBean(false);//노드의 초기값
    	
    	String urlLocation = null;
    	
    	if(vo != null && vo.getUrlLocation() != null){
    		
    		//정규표현식써서 수정요망..
    		
    		//space를 제거한다.
    		urlLocation = vo.getUrlLocation().replaceAll(" ", "");
    		
    		//*를 제거한다.
    		urlLocation = urlLocation.replaceAll("\\*", "");
    		
    		//.xml을 제거한다.
    		urlLocation = urlLocation.replaceAll(".xml", "");
    		
    		//tab을 제거한다.
    		urlLocation = urlLocation.replaceAll("\t", "");
    		
    		//enter를 제거한다.
    		urlLocation = urlLocation.replaceAll("\n", "");
    		
    		// /를 \\로 치환한다.
    		urlLocation = urlLocation.replaceAll("/", "\\\\");
    		
    		// classpath를 제거한다.
    		urlLocation = urlLocation.replaceAll("classpath:", "");
    		
    		
    		comVo.setLocationPattern(urlLocation.split(","));
    		
    		try {
    			project.getProject().accept(new IResourceVisitor() {
    				
    				//프로젝트를 순환하면서 web.xml인 파일을 찾는다.
    				public boolean visit(IResource resource)   throws CoreException {
    					
    					boolean nextWork = true;
    					
    					String[] dispatcherLocationPattern = comVo.getLocationPattern();
    					
    					
    					if(resource.getType() == IResource.FILE && resource.getName().endsWith(".xml")){
    						
    						/**
    						 * 경로에 있는 파일인지 파악한다.
    						 */
    						boolean isLocation = false;
    						
    						for(int i=0 ; dispatcherLocationPattern.length > i ; i ++){
    							if(resource.getFullPath().toOSString().indexOf(dispatcherLocationPattern[i])>-1){
    								isLocation = true;
    							}
    						}
    						
    						
    						/**
    						 * DispatcherServlet Location 패턴이 일치 할 경우 exception handle bean을 찾는다.
    						 */
    						if(isLocation){
    							
    							try {
    								DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
    								
    								docBF.setValidating(false);
    								docBF.setNamespaceAware(false);
    								docBF.setFeature("http://xml.org/sax/features/validation", false);
    								docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    								docBF.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    								docBF.setFeature("http://xml.org/sax/features/external-general-entities", false);
    								docBF.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    								
    								DocumentBuilder docBuilder = (DocumentBuilder) docBF.newDocumentBuilder();
    								
    								//대상 파일이 접근 가능할 경우, 대상파일의 document를 가져온다.
    								IFile file = realContainer.getFile(resource.getFullPath());
    								
    								if(file.isAccessible()) {
    									//시스템내 파일의 document
    									Document targetDoc = docBuilder.parse(new InputSource(file.getContents()));
    									Node targetRootNode = targetDoc.getDocumentElement();
    									
    									//bean list를 가져와 nodeXpath와 맞는 노드를 찾는다.
    									if(XmlUtil.existNode(targetRootNode, comVo.getNodeXpath())){
    										comVo.setExistBean(true);
    										comVo.setAsIsRootNode(targetRootNode);
    										nextWork = false;
    										
    									}
    									
    								}
    							} catch (Exception e) {
    								CommngtLog.logError(e);
    							}
    							
    						}else {
    							return nextWork;
    						}
    						
    					}
    					return nextWork;
    				}
    				
    			});
    			
    		} catch (CoreException e) {
    			CommngtLog.logError(e);
    		}
    	}
    	return comVo;
    }
    

    /**
     * DispatcherServlet에서 Exception처리를 위한 bean을 추가하거나 삭제한다.
     * logic
     *   1. 기존에 처리를하는 bean이 있을경우 새롭게 추가되는 파일의 bean을 삭제한다.
     *   2. 기존에 처리하는 bean이 없을경우 새롭게 추가되는 파일을 복사한다.
     * @param context
     * @param container
     * @param fname
     * @param content
     * @param monitor
     * @param comVo dipatcher의 경로를 가지고 있는 VO
     * @throws CoreException
     */
    public static void copyDispatcherServletFile(NewEgovCommngtContext context, IContainer container, String fname, InputStream content, IProgressMonitor monitor, ComResourceUtilVO comVo) throws CoreException {
    	
    	try {
    		
    		// 새로운 파일의 노드 리스트를 구성한다.
    		Document doc = makeDocument(content);
    		Node rootNode = doc.getDocumentElement();
    		
    		/** 기존 파일 시스템에 bean이 존재하는지 확인한다. */
    		// 1. exceptionMappings
    		comVo.setNodeXpath("/beans/bean/property[@name='exceptionMappings']");
        	ComResourceUtilVO vo = checkExistBean(context.getJavaProject(), comVo);
        	NodeList nodeList = XmlUtil.getNodeList(rootNode, "/beans/bean");
        	
        	if(vo.isExistBean()){
        		/** 존재할 경우 새롭게 추가되는 파일의 bean값을 삭제한다. */
        		if(nodeList.getLength() > 0){
        			for(int i=0 ; nodeList.getLength() > i ; i++){
        				Node node = nodeList.item(i);
        				if(XmlUtil.existNode(node, "./property[@name='exceptionMappings']")){
        					XmlUtil.removeNode(node, ".");
        				}
        			}
        		}
        	}
        	
        	//2.webBindingInitializer
        	comVo.setNodeXpath("/beans/bean/property[@name='webBindingInitializer']");
        	vo = checkExistBean(context.getJavaProject(), comVo);
        	//node가 계속 수정되므로 refresh한다.
        	nodeList = XmlUtil.getNodeList(rootNode, "/beans/bean");
        	
        	if(vo.isExistBean()){
        		/** 존재할 경우 새롭게 추가되는 파일의 bean값을 삭제한다. */
        		if(nodeList.getLength() > 0){
        			for(int i=0 ; nodeList.getLength() > i ; i++){
        				Node node = nodeList.item(i);
        				if(XmlUtil.existNode(node, "./property[@name='webBindingInitializer']")){
        					XmlUtil.removeNode(node, ".");
        				}
        			}
        		}
        	}
        	
        	//2.prefix
        	comVo.setNodeXpath("/beans/bean/@prefix");
        	vo = checkExistBean(context.getJavaProject(), comVo);
        	//node가 계속 수정되므로 refresh한다.
        	nodeList = XmlUtil.getNodeList(rootNode, "/beans/bean");
        	
        	if(vo.isExistBean()){
        		/** 존재할 경우 새롭게 추가되는 파일의 bean값을 삭제한다. */
        		if(nodeList.getLength() > 0){
        			for(int i=0 ; nodeList.getLength() > i ; i++){
        				Node node = nodeList.item(i);
        				if(XmlUtil.existNode(node, "./@prefix")){
        					XmlUtil.removeNode(node, ".");
        				}
        			}
        		}
        	}
        	
    		String newXmlStr = XmlUtil.getXmlString(rootNode, "/");
    		InputStream newInStream = new ByteArrayInputStream(newXmlStr.getBytes("UTF-8"));
    		ResourceUtils.createFolderFile(container, fname, newInStream, monitor);
    		
    	}catch(Exception e) {
			CommngtLog.logError(e);
		}
    	
    }

}
