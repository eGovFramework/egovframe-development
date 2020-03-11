package egovframework.dev.imp.commngt.wizards.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import egovframework.dev.imp.commngt.EgovCommngtPlugin;
import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.common.EgovComponentsInfo;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;

/**
 * 컴포넌트 정보 생성 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public final class ComponentElementFactory {
	/** 공통컴포넌트 팩토리 */
	private List<IComponentElement> factory ;
	/** 공통컴포넌트 팩토리 인스턴스*/
	private static ComponentElementFactory instance;


	/** 
	 * 생성자
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * */
	private ComponentElementFactory() {
		try {
			initializeFactory();
		} catch (SecurityException e) {
			CommngtLog.logError(e);
		} catch (NoSuchFieldException e) {
			CommngtLog.logError(e);
		
		} catch (MalformedURLException e) {
			CommngtLog.logError(e);
		} catch (JDOMException e) {
			CommngtLog.logError(e);
		} catch (IOException e) {
			CommngtLog.logError(e);
		} catch (InstantiationException e) {
			CommngtLog.logError(e);
		} catch (IllegalAccessException e) {
			CommngtLog.logError(e);
		}
	}
	
	/** 인스턴스 생성 */
	public static ComponentElementFactory getInstance(){
		if ( instance == null) instance = new ComponentElementFactory();
		return instance;
	}

	/** 
	 * 초기화
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * */
	private void initializeFactory() throws MalformedURLException, JDOMException, IOException, SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {
		SAXBuilder builder = new SAXBuilder();
//		Document doc = builder.build(EgovPluginLocationFinder.getAbsoluteFile(EgovCommngtPlugin.getDefault(), EgovCommngtMessages.componentElementFactoryPath ));
		Document doc = builder.build( FileLocator.toFileURL(EgovCommngtPlugin.getDefault().getBundle().getEntry(ComMngtMessages.componentElementFactoryPath)) );

		Element components = doc.getRootElement();
		List<?> clist = components.getChildren("category"); //$NON-NLS-1$
		Class<EgovComponentsInfo> cls = EgovComponentsInfo.class;
		
		if ( clist !=null){
			factory  = new ArrayList<IComponentElement>();
			List<Component> totalComponent = new ArrayList<Component>();
			for ( int idx=0; idx < clist.size(); idx++ ){
				Object curr = clist.get(idx);
				
				if ( curr instanceof Element){
					Category cCategory = new Category();
					Element category = (Element) curr;
					
					//cls.getField((category.getAttributeValue("name")).replace("#", "").toString());
					//cls.getField((category.getAttributeValue("description")).replace("#", "").toString());
					//EgovComponentsInfo.Category1_name

					cCategory.setName( getFieldValue(cls, category, "name", "attribute") );
					
					//cCategory.setDesc(EgovComponentsInfo.Category1_description);
					//EgovComponentsInfo.getMessage("EgovComponentsInfo.Category1_description", null, null);

					cCategory.setDesc( getFieldValue(cls, category, "description", "attribute") ); 
					cCategory.setChildren( getComponent(cls, category, totalComponent, getFieldValue(cls, category, "name", "attribute")));
				
					factory.add(cCategory);
				}
			}
		}		
	}
	/** 필드 값 읽기 */
	private String getFieldValue(Class<EgovComponentsInfo> c, Element e, String a, String flag){
		
		try {
			Field f = getFieldInfo(c, e, a, flag);
			if(f != null) return f.get( f ).toString();
		} catch (IllegalArgumentException e1) {
			CommngtLog.logError(e1);
		} catch (IllegalAccessException e1) {
			CommngtLog.logError(e1);
		}
		return "";
	}
	
	/** 필드 정보 얻기 */
	private Field getFieldInfo(Class<EgovComponentsInfo> c, Element e, String a, String flag){
		try {
			if(flag.equals("attribute")){
				if(e.getAttributeValue(a) != null && e.getAttributeValue(a) !="")
				return c.getField((e.getAttributeValue(a)).replace("#", "").toString());
			}
			else{
				if(e.getChildText(a) != null && e.getChildText(a) !="")
				return c.getField((e.getChildText(a)).replace("#", "").toString());
			}
		} catch (SecurityException e1) {
			CommngtLog.logError(e1);
		} catch (NoSuchFieldException e1) {
			CommngtLog.logError(e1);
		}
		return null;
	}
	/** 필드 값 문자열 얻기 */
	private String getFieldValueString(Class<EgovComponentsInfo> c, String a){
		try {
			if(a != null && a != "")
				return c.getField(a.replace("#", "").toString()).get( c.getField(a.replace("#", "").toString()) ).toString();
	
		} catch (IllegalArgumentException e1) {
			CommngtLog.logError(e1);
		} catch (IllegalAccessException e1) {
			CommngtLog.logError(e1);
		
		} catch (SecurityException e1) {
			CommngtLog.logError(e1);
		} catch (NoSuchFieldException e1) {
			CommngtLog.logError(e1);
		}
		return "";
	}
	
	/** 컴포넌트 정보 얻기 */
	protected List<IComponentElement> getComponent(Class<EgovComponentsInfo> cls, Element category, List<Component> totalComponent, String catName) {
		List<IComponentElement> components = new ArrayList<IComponentElement>();
		
		List<?> children = category.getChildren("component"); 

		if (children == null) return null;

		for ( int idx=0; idx < children.size(); idx++){
			Element child = null;
			if ( children.get(idx) instanceof Element){
				child = (Element) children.get(idx);
				Component component = new Component();
				//component.setPackageName(child.getChildText("packageName")); 
				component.setPackageName( getFieldValue(cls, child, "packageName", "childText") ); 
				
				if ( totalComponent.contains(component)){
					component = totalComponent.get(totalComponent.indexOf(component));
				}else{
					totalComponent.add(component);
				}

				component.setName( getFieldValue(cls, child, "name", "attribute")  ); 
				component.setDesc( getFieldValue(cls, child, "description", "childText") ); 
				component.setUseTable( getFieldValue(cls, child, "useTable", "childText") ); 
				component.setDependencyPackage(computeDependency(cls, child, totalComponent));
				component.setVersion( getFieldValue(cls, child, "version", "childText") ); 
				component.setParentName(catName);
				component.setWebExist( getFieldValue(cls, child, "webexist", "childText").equals("true") ? true : false); 
				component.setAddedOptions( getFieldValue(cls, child, "addedOptions", "childText").equals("true") ? true : false);
				//파일명 = 패키지명-버전.zip
				component.setFileName( getFieldValue(cls, child, "packageName", "childText") +"."+ getFieldValue(cls, child, "version", "childText") +".zip"); 

				components.add(component);
			}
		}
		
		return components;
	}
	
	/** 컴포넌트 디펜던시 정보 얻기 */
	private List<Component> computeDependency(Class<EgovComponentsInfo> cls, Element element, List<Component> totalComponent){
		List<Component> dependencyList = new ArrayList<Component>();
		List<?> dependencyinfo = element.getChildren("dependencyPackage"); //$NON-NLS-1$
		
		if ( dependencyinfo == null) return null;
		
		for (Object o : dependencyinfo){
			if ( o instanceof Element){
				Element dependency = (Element)o;
				
				//String componentName = dependency.getText();
				String componentName = getFieldValueString(cls, dependency.getText());

				Component temp = new Component();
				temp.setPackageName(componentName);
				
				if ( totalComponent.contains(temp) ){
					temp = totalComponent.get(totalComponent.indexOf(temp));
				}
				totalComponent.add(temp);
				
				dependencyList.add(temp);
			}
		}
		return dependencyList;
	}

	/** 
	 * 컴포넌트 설치여부
	 * @param javaProject 
	 * */
	public boolean isCreatedComponent(IJavaProject javaProject, String locationPath){
		//final IContainer realContainer = javaProject.getProject().getParent();
		try {	
//			locationPath= (locationPath.startsWith("/")? locationPath : "/" + locationPath);
			
			IPackageFragment compackage = EgovJavaElementUtil.findPackage(javaProject, null, locationPath.replace('.', '/'));

			if (compackage == null || !compackage.exists()){
				return false;
			}else{
				//boolean rv = true;
				//System.out.println(locationPath);
				if(locationPath.contains("com.cmm")){
					//System.out.println(">>>>>>>>>>>>>>>"+locationPath.contains("com.cmm"));
					//IFile file =  realContainer.getFile(new Path("src/main/resources/egovframework/spring/com/context-common.xml"));
					IFile file =  javaProject.getProject().getFile(new Path("src/main/resources/egovframework/spring/com/context-common.xml"));
					//System.out.println(file.isAccessible());
					if(!file.isAccessible()){
						return false;
					}
				}
			}
		} catch (JavaModelException e) {
			CommngtLog.logError(e);
		} catch (CoreException e) {
			CommngtLog.logCoreError(e);
		}

		return true;
	}
	/** 엘리먼트 추가 */
	public void addElement(IComponentElement element){
		factory.add(element);
	}
	/** 컴포넌트 엘리먼트 구함 */
	public List<IComponentElement> getComponentElements(){
		return factory;
	}
	/** 컴포넌트 포함 여부 */
	public void containsComponent(IJavaProject javaProject) {
		if ( factory == null) return;
		
		for ( IComponentElement element : factory){
			if ( element instanceof Category){
				List<IComponentElement> components = ((Category) element).getChildren();
				
				if (components == null) continue;
				
				for( IComponentElement component : components){
					if ( !(component instanceof Component)) continue;
					
					((Component)component).setCreatedComponent( isCreatedComponent(javaProject,component.getPackageName()) );
				}
			}
		}
	}
	
	
}