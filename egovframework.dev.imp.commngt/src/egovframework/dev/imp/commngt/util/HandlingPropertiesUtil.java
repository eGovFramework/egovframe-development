package egovframework.dev.imp.commngt.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.core.utils.EgovProperties;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.ide.common.ResourceUtils;

/**
 * Globals.Properties파일을 핸들링 하는 클래스
 * 
 * @author 개발환경 개발팀 최서윤
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class HandlingPropertiesUtil {
	
	/** 공통컴포넌트 컨텍스트 */
	private static NewEgovCommngtContext context = null;
	/** 로컬내에 존재하는 property 파일 */
	private static Properties originProps = new Properties();
	
	
	/**
	 * properties 파일을 가져온다
	 * @return globals.properties 파일
	 * 
	 */
	public static File getPropertiesFile() {
		File propertiesFile =  null ;
		
		if(context != null){
			propertiesFile = EgovProperties.getPropertiesFile(context.getJavaProject());
			return propertiesFile;
		}
		
		return null;
	}

	/**
	 * properties 파일 경로를 가져온다
	 * @return globals.properties 경로
	 * 
	 */
	public static String getFilePath() {
		IPath prjPath = null;
		String[] prjPathWithoutName = null;
		String propertiesFilePath = null;
		
		if(context != null){
					
			prjPath = context.getJavaProject().getProject().getLocation();
			prjPathWithoutName = prjPath.toString().split(
					context.getJavaProject().getProject().getName());
				
			if(getPropertiesFile() == null){
				
				createPropertiesFile();
				propertiesFilePath = prjPathWithoutName[0] + getPropertiesFile();
				
			}else{
				propertiesFilePath = prjPathWithoutName[0] + getPropertiesFile();
			}
			
			return propertiesFilePath;
		}
		
		return null;
		
	}

	/**
	 * properties 파일에 저장한다
	 * 
	 */
	public static void storeProperties(NewEgovCommngtContext context) {
		
			
			if(getPropertiesFile() != null){
				setGlobalsProps(getFilePath());
				fileReadAndWrite(getFilePath(), context);
			}
	}
	
	
	/**
	 *  기존 Properties 파일을 읽어와서 변경사항만 반영하는 메소드
	 *  @param filePath
	 * */
	@SuppressWarnings("resource")
	private static void fileReadAndWrite(String filePath, NewEgovCommngtContext context){
		
		try {
			
			String writeString = "";
			
			// 파일 읽어오기
			BufferedReader instrm = new BufferedReader(new FileReader(filePath));
			
			String tmpstr = "";
			while((tmpstr = instrm.readLine()) != null ) {
				if(tmpstr.indexOf("#") < 0 && tmpstr.length() > 0){
					//#이 들어있다면
					String[] getMap = tmpstr.split("=");
					if(getMap.length >= 2 && !getMap[0].equals(" ")){
						for(int i = 0; i < originProps.size(); i++){
							Set<Object> keys = originProps.keySet();
							Iterator<Object> iterator = keys.iterator();
						    while (iterator.hasNext()) {
						      String key = (String) iterator.next();
						      
						      if(key.equals(getMap[0].trim())){
						    	  getMap[1] = originProps.getProperty(key);
						    	  
						      }
						    }
						      
						   }
				
						tmpstr = getMap[0]+"="+getMap[1];
					}else{
						//key는 있지만 value가 null일 경우
						Set<Object> keys = originProps.keySet();
						Iterator<Object> iterator = keys.iterator();
					    while (iterator.hasNext()) {
					    	String key = (String) iterator.next();
					    	
					    	if(key.equals(getMap[0].trim())){
					    		tmpstr = tmpstr + originProps.getProperty(key);
					    	}
					    }
					}
				}
				
				//기존의 파일을 읽어 새로운 파일에 쓰기위한 String을 만든다.
				writeString += tmpstr+"\n";
			}
			
			InputStream inStream = new ByteArrayInputStream(writeString.getBytes("UTF-8"));
			
			String pname = context.getJavaProject().getProject().getName().toString();
			String fname = getPropertiesFile().toString();
			String[] realRoute = fname.split(pname); 
			
			ResourceUtils.createFolderFile(context.getJavaProject().getProject(), realRoute[1], inStream, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 사용자가 입력한 DBtype 판별
	 * @param context
	 * @return DBtype
	 */
	public static String findDbType(NewEgovCommngtContext context){
		
		if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType1) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType1;
			
		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType2) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType2;

		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType3) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType3;

		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType4) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType4;

		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType5) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType5;
			
		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType6) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType6;

		}else if(context.getDBType().toLowerCase().indexOf(ComMngtMessages.customizeTableCreationPagedbType7) > -1){
			return ComMngtMessages.customizeTableCreationPagedbType7;
			
		} else{
			return ""; //$NON-NLS-1$
		}
	}
	
	
	/**
	 * 개발자가 properties 파일을 삭제한 경우 new properties파일 생성
	 * @return 
	 * */
	public static void createPropertiesFile(){
		
		IFile newPropertiesFile = null;
		IFolder folder = null;
		IProgressMonitor nullMonitor = new NullProgressMonitor();
		
		try {
			folder = context.getJavaProject().getProject().getFolder(new Path("/src/main/resources/egovframework")); //$NON-NLS-1$
			if(folder.isAccessible()){
				folder = context.getJavaProject().getProject().getFolder(new Path("/src/main/resources/egovframework/egovProps")); //$NON-NLS-1$
				if(!folder.isAccessible()){ //'/test/src/main/resources/egovframework' does not exist.
					folder.create(true, true, nullMonitor);
				}
			}

		} catch (CoreException e) {
			CommngtLog.logError(e);
		}
		
		folder = context.getJavaProject().getProject().getFolder(new Path("/src/main/resources/egovframework")); //$NON-NLS-1$
		if(folder.isAccessible()){
		
			newPropertiesFile = context.getJavaProject().getProject().getFile("/src/main/resources/egovframework/egovProps/globals.properties"); //$NON-NLS-1$
		
			String contents = " "; //$NON-NLS-1$
			InputStream source = new ByteArrayInputStream(contents.getBytes());
			try {
				newPropertiesFile.create(source, false, null);
			} catch (CoreException e) {
				CommngtLog.logError(e);
			}

		}
	}
	
	/**
	 * 사용자가 선택한 로직에 따라 globals.properties내부의 설정 변경
	 * @param 경로
	 * */
	@SuppressWarnings("unchecked")
	public static void setGlobalsProps(String path){
		
		ArrayList<?> a = EgovProperties.loadPropertyFile(path);
		for(int i = 0; i< a.size(); i++) {
			HashMap<?, ?> m = (HashMap<?, ?>)a.get(i);
			Iterator<?> iterator = m.entrySet().iterator();
		    while (iterator.hasNext()) {
		      Entry<?, String> entry = (Entry<?, String>) iterator.next();
		      
		      if(context.getInstallType().indexOf("user") > -1){ //USER 일 경우		    	  
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage10) && NullUtil.notNone(context.getDBType())){
			    	  entry.setValue(context.getDBType());
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage19) && NullUtil.notNone(context.getUrl())){
			    	  entry.setValue(context.getUrl());
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage24) && NullUtil.notNone(context.getUsername())){
			    	  entry.setValue(context.getUsername());
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage29)){
			    	  entry.setValue(context.getPassword());
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage99) && NullUtil.notNone(findDbType(context))){
			    	  entry.setValue(findDbType(context));
			      }
		      }else{
		    	  //...
		    	  if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage10) && NullUtil.notNone(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage10))){
			    	  entry.setValue(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage10));
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage19) && NullUtil.notNone(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage19))){
			    	  entry.setValue(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage19));
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage24) && NullUtil.notNone(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage24))){
			    	  entry.setValue(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage24));
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage29)){
			    	  entry.setValue(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage29));
			      }
			      if(entry.getKey().equals(ComMngtMessages.customizeTableCreationPage99) && NullUtil.notNone(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage99))){
			    	  entry.setValue(EgovProperties.getProperty(HandlingPropertiesUtil.getFilePath(), ComMngtMessages.customizeTableCreationPage99));
			      }
		      }
		    }
		    originProps.putAll(m);
		}
		
	}
	

	
	/**
	 * NewEgovCommngtContext의 입력값 설정
	 * @param cont
	 * 
	 */
	public static void setContext( NewEgovCommngtContext cont) {
		context = cont;
	}
	
}
