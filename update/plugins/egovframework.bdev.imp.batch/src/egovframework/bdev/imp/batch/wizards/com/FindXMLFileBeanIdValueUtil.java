package egovframework.bdev.imp.batch.wizards.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.bdev.imp.batch.common.BatchLog;
import egovframework.dev.imp.core.utils.BTextSearchUtil;
import egovframework.dev.imp.core.utils.XmlUtil;


/**
 * 프로젝트내 Bean ID 검색 오퍼레이션
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.08.05
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.08.05  최서윤          최초 생성
 * 
 * 
 * </pre>
 */
public class FindXMLFileBeanIdValueUtil {

	/** 선택한 프로젝트 내 Job XML파일을 검색한 리스트*/
	private static Map<String, IFile> foundXMLfiles = new HashMap<String, IFile>();
		
	/**
	 * 선택한 프로젝트내의 xml파일 다 가져오기
	 * 
	 * @param context
	 * @param findingAttr 
	 * @param findingNode 
	 * @param isBeanList 
	 */
	@SuppressWarnings("restriction")
	public static List<String> findXMLFiles(BatchJobContext context, List<String> findingNode, String findingAttr, int findingNodeCount){
		
		IProgressMonitor monitor = new NullProgressMonitor();
		List<?> foundList = null;
		IFile actualFile = null;

		foundXMLfiles.clear();
		
		try {
			foundList = BTextSearchUtil.findFiles(context.getProject().members(), "bean", "*.xml", false, monitor);
		} catch (CoreException e) {
			BatchLog.logError(e);
		}
		
		if(foundList != null) {
			for (int j = 0; j < foundList.size(); j++) {


				Map<?, ?> map = (Map<?, ?>) foundList.get(j);
				Object f = map.get(BTextSearchUtil.K_FILE);

				if (f instanceof IFile) {
					actualFile = (IFile) f; 

					if(!actualFile.toString().contains("pom.xml")){
						if(!actualFile.toString().contains("target")){
							foundXMLfiles.put(actualFile.toString(), actualFile);
						}
					}
				}
			}
		}
		
		return findBeanIDList(foundXMLfiles, findingNode, findingAttr, findingNodeCount);
	}
	
	
	/**
	 * 파일내의 findingNode 하위 findingAttr의 value 값 가져오기
	 * 
	 * @param foundXMLfiles
	 * @param findingAttr 
	 * @param findingNode 
	 * */
	private static List<String> findBeanIDList(Map<String, IFile> foundXMLfiles, List<String> findingNode, String findingAttr, int findingNodeCount){
		
		Node rootNode = null;
		NodeList nodeList = null;
		List<String> beanIDList = new ArrayList<String>();
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < foundXMLfiles.size(); i++) {

			Set<String> xmlFileKey= foundXMLfiles.keySet();
			Object[] keyArray = xmlFileKey.toArray();

			String key = (String)keyArray[i];
			try {
				rootNode = XmlUtil.getRootNode(foundXMLfiles.get(key).getRawLocation().toOSString());

				for(int j = 0; j < findingNodeCount; j++){
					if (XmlUtil.existNode(rootNode, findingNode.get(j))) {
						nodeList = XmlUtil.getNodeList(rootNode, findingNode.get(j));

						if(nodeList != null){
							for (int inx = 0; inx < nodeList.getLength(); inx++) {
								Node targetBean = nodeList.item(inx);
								if(targetBean.getAttributes().getNamedItem(findingAttr) != null){
									String tempID = targetBean.getAttributes().getNamedItem(findingAttr).getNodeValue();
									linkedMap.put(tempID, tempID);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				BatchLog.logError(e);
			}
		}

		Iterator<String> keyLinked = linkedMap.keySet().iterator();
		String beanID;
		while(keyLinked.hasNext()){
			beanID = (String) keyLinked.next();
			beanIDList.add(beanID);
		}
		
		return beanIDList;
	}
	
	/**
	 * 원하는 Bean에 관계된 value의 존재여부 검색 
	 * 
	 * @param context
	 * @param findingNode xml파일내 찾고 있는 node의 주소 ex) beans하위 bean = /beans/bean
	 * @param findingAttr 찾고 있는 node내의 attribute ex) id, class 등
	 * @param findingValue node내 attribute의 value ex) id의 value = jobRepository
	 * @param valueOfFoundAttrRel 검색한 항목(attribute)존재시 그 node의 다른 attribute ex) id가 jobRepository인 node에서 "class" value 
	 * @param isParent 동일 노드레벨의 value를 찾을경우 False
	 * @return linkedMap (찾은 노드의 value, 해당 노드가 있는 파일의 path)
	 */
	@SuppressWarnings("restriction")
	public static LinkedHashMap<String, String> findingWantedBeanInXMLFiles(BatchJobContext context, String findingNode, String findingAttr, String findingValue, String valueOfFoundAttrRel, boolean isParent){
		
		IProgressMonitor monitor = new NullProgressMonitor();
		List<?> foundList = null;
		IFile actualFile = null;
		Node rootNode = null;
		NodeList nodeList = null;
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap<String, String>();

		foundXMLfiles.clear();
		
		try {
			foundList = BTextSearchUtil.findFiles(context.getProject().members(), "bean", "*.xml", false, monitor);
		} catch (CoreException e) {
			BatchLog.logError(e);
		}
		
		if(foundList != null){
			for (int j = 0; j < foundList.size(); j++) {


				Map<?, ?> map = (Map<?, ?>) foundList.get(j);
				Object f = map.get(BTextSearchUtil.K_FILE);

				if (f instanceof IFile) {
					actualFile = (IFile) f; 

					if(!actualFile.toString().contains("pom.xml")){
						if(!actualFile.toString().contains("target")){
							foundXMLfiles.put(actualFile.toString(), actualFile);
						}
					}
				}
			}
		}
		
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
							if(targetBean.getAttributes().getNamedItem(findingAttr) != null){
								String tempID = targetBean.getAttributes().getNamedItem(findingAttr).getNodeValue();
								if(isParent){
									if(tempID.equals(findingValue)){
										linkedMap.put(targetBean.getParentNode().getAttributes().getNamedItem(valueOfFoundAttrRel).getNodeValue().toString(), foundXMLfiles.get(key).getProjectRelativePath().toString());
									}
								} else {
									if(tempID.equals(findingValue)){
										linkedMap.put(targetBean.getAttributes().getNamedItem(valueOfFoundAttrRel).getNodeValue().toString(), foundXMLfiles.get(key).getProjectRelativePath().toString());
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				BatchLog.logError(e);
			}
		}
		return linkedMap;
	}

}
