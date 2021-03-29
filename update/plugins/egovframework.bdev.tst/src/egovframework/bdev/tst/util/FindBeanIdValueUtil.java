package egovframework.bdev.tst.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.bdev.tst.common.BatchTestLog;
import egovframework.dev.imp.core.utils.XmlUtil;

/**
 * XML 파일에서 지정한 조건으로 XML파일 검색해오기
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.09.17
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.09.17  최서윤          최초 생성
 * 
 * 
 * </pre>
 */
public class FindBeanIdValueUtil {

	/**
	 * XML 파일들에서 beans하위의 bean에 지정한 Attribute와 beanID로 XML파일 검색해오기 
	 * 
	 * @param foundXMLfiles
	 * @param findingNode
	 * @param findingBeanID
	 * @param findingBeanAttr
	 * @return foundBeanIDFile
	 */
	public static Map<String, IFile> findBeanIDList(Map<String, IFile> foundXMLfiles, String findingNode, String findingBeanID, String findingBeanAttr){
		
		Node rootNode = null;
		NodeList nodeList = null;
		LinkedHashMap<String, IFile> linkedMap = new LinkedHashMap<String, IFile>();
		LinkedHashMap<String, IFile> foundBeanIDFile = new LinkedHashMap<String, IFile>();
		
		for (int i = 0; i < foundXMLfiles.size(); i++) {

			Set<String> xmlFileKey= foundXMLfiles.keySet();
			Object[] keyArray = xmlFileKey.toArray();

			String key = (String)keyArray[i];
			try {
				rootNode = XmlUtil.getRootNode(foundXMLfiles.get(key).getRawLocation().toOSString());

				if (XmlUtil.existNode(rootNode, findingNode)) {
					nodeList = XmlUtil.getNodeList(rootNode, findingNode);

					if(nodeList != null){
						for (int inx = 0; inx < nodeList.getLength(); inx++) {
							Node targetBean = nodeList.item(inx);
							if(targetBean.getAttributes().getNamedItem(findingBeanAttr) != null){
								String tempID = targetBean.getAttributes().getNamedItem(findingBeanAttr).getNodeValue();
								linkedMap.put(tempID, foundXMLfiles.get(key));
							}
						}
					}
				}
			} catch (Exception e) {
				BatchTestLog.logError(e);
			}
		}

		Iterator<String> keyLinked = linkedMap.keySet().iterator();
		String beanID;
		while(keyLinked.hasNext()){
			beanID = (String) keyLinked.next();
			if(findingBeanID != null && beanID.equals(findingBeanID)){
				foundBeanIDFile.put(beanID, linkedMap.get(beanID));
				return foundBeanIDFile;
			} else if(findingBeanID == null){
				foundBeanIDFile.put(beanID, linkedMap.get(beanID));
				return foundBeanIDFile;
			}
		}
		
		return null;
	}

	/**
	 *  XML 파일에서 beans하위의 bean에 지정한 Attribute와 beanID로 XML파일 검색해오기
	 *  
	 * @param foundXMLfiles
	 * @param findingNode
	 * @param findingBeanID
	 * @param findingBeanAttr
	 * @return foundBeanIDFile
	 */

	public static Map<String, IFile> findBeanIDList(IFile foundXMLfiles, String findingNode, String findingBeanID, String findingBeanAttr){

		Node rootNode = null;
		NodeList nodeList = null;
		LinkedHashMap<String, IFile> linkedMap = new LinkedHashMap<String, IFile>();
		LinkedHashMap<String, IFile> foundBeanIDFile = new LinkedHashMap<String, IFile>();

		try {
			rootNode = XmlUtil.getRootNode(foundXMLfiles.getRawLocation().toOSString());
			
			if (XmlUtil.existNode(rootNode, findingNode)) {
				nodeList = XmlUtil.getNodeList(rootNode, findingNode);
				
				if(nodeList != null){
					for (int inx = 0; inx < nodeList.getLength(); inx++) {
						Node targetBean = nodeList.item(inx);
						if(targetBean.getAttributes().getNamedItem(findingBeanAttr) != null){
							String tempID = targetBean.getAttributes().getNamedItem(findingBeanAttr).getNodeValue();
							linkedMap.put(tempID, foundXMLfiles);
						}
					}
				}
			}
		} catch (Exception e) {
			BatchTestLog.logError(e);
		}


		Iterator<String> keyLinked = linkedMap.keySet().iterator();
		String beanID;
		while(keyLinked.hasNext()){
			beanID = (String) keyLinked.next();
			if(findingBeanID != null && beanID.equals(findingBeanID)){
				foundBeanIDFile.put(beanID, linkedMap.get(beanID));
				return foundBeanIDFile;
			} else if(findingBeanID == null){
				foundBeanIDFile.put(beanID, linkedMap.get(beanID));
				return foundBeanIDFile;
			}
		}

		return null;
	}

	/**
	 * XML 파일에서 원하는 node하위의 value 값과 같은 위치에 있는 Attr의 값 가져오기
	 * 
	 * @param foundXMLfiles
	 * @param findingNode
	 * @param findingAttrValue
	 * @param findingBeanAttr
	 * @param getBeanIDOfThisAttr
	 * @return foundBeanIDFile
	 */

	public static Map<String, IFile> findBeanIDList(IFile foundXMLfiles, String findingNode, String findingAttrValue, String findingBeanAttr, String getBeanIDOfThisAttr){

		Node rootNode = null;
		NodeList nodeList = null;
		LinkedHashMap<String, IFile> linkedMap = new LinkedHashMap<String, IFile>();
		LinkedHashMap<String, IFile> foundBeanIDFile = new LinkedHashMap<String, IFile>();

		try {
			rootNode = XmlUtil.getRootNode(foundXMLfiles.getRawLocation().toOSString());
			
			if (XmlUtil.existNode(rootNode, findingNode)) {
				nodeList = XmlUtil.getNodeList(rootNode, findingNode);
				
				if(nodeList != null){
					for (int inx = 0; inx < nodeList.getLength(); inx++) {
						Node targetBean = nodeList.item(inx);
						if(targetBean.getAttributes().getNamedItem(findingBeanAttr) != null){
							if(targetBean.getAttributes().getNamedItem(findingBeanAttr).getNodeValue().contains(findingAttrValue)){
								String tempID = targetBean.getAttributes().getNamedItem(getBeanIDOfThisAttr).getNodeValue();
								linkedMap.put(tempID, foundXMLfiles);
							}							
						}
					}
				}
			}
		} catch (Exception e) {
			BatchTestLog.logError(e);
		}


		Iterator<String> keyLinked = linkedMap.keySet().iterator();
		String beanID;
		while(keyLinked.hasNext()){
			beanID = (String) keyLinked.next();
			foundBeanIDFile.put(beanID, linkedMap.get(beanID));
			return foundBeanIDFile;
		}

		return null;
	}
}
