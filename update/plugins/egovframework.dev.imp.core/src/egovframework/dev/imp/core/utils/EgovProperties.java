package egovframework.dev.imp.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.search.ui.text.FileTextSearchScope;

//파일 존재여부 리턴하는 메소드 추가(공통 )

public class EgovProperties{
	
	public static final String ERR_CODE =" EXCEPTION OCCURRED";				//프로퍼티값 로드시 에러발생하면 반환되는 에러문자열
	public static final String ERR_CODE_FNFE =" EXCEPTION(FNFE) OCCURRED";	//프로퍼티값 로드시 에러발생하면 반환되는 에러문자열
	public static final String ERR_CODE_IOE =" EXCEPTION(IOE) OCCURRED";		//프로퍼티값 로드시 에러발생하면 반환되는 에러문자열
	
	/** 프로젝트 하위 경로 파일들 */
	public static List<?> foundFiles = null;
	/** 실제 properties 파일 */
	public static File actualFile = null;
	
	//파일구분자
    static final char FILE_SEPARATOR     = File.separatorChar;
    

//    public static void setPropertyFile(IJavaProject project) {
//    	actualFile = getPropertiesFile(project);
//	}
    
	/**
	 * 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다(Globals.java 전용)
	 * @param keyName String
	 * @return String
	 */
	public static String getProperty(String keyName){
		String value = ERR_CODE;
		value="99";
		debug(actualFile + " : " + keyName);
		FileInputStream fis = null;
		try{
			Properties props = new Properties();
			fis  = new FileInputStream(actualFile);
			props.load(new java.io.BufferedInputStream(fis));
			value = props.getProperty(keyName).trim();
		}catch(FileNotFoundException fne){
			debug(fne);
		}catch(IOException ioe){
			debug(ioe);
		}catch(Exception e){
			debug(e);
		}finally{
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		return value;
	}
	
	/**
	 * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다
	 * @param fileName String
	 * @param key String
	 * @return String
	 */
	public static String getProperty(String fileName, String key){
		FileInputStream fis = null;
		try{
			java.util.Properties props = new java.util.Properties();
			fis  = new FileInputStream(fileName);
			props.load(new java.io.BufferedInputStream(fis));
			fis.close();

			String value = props.getProperty(key);
			return value;
		}catch(java.io.FileNotFoundException fne){
			return ERR_CODE_FNFE;
		}catch(java.io.IOException ioe){
			return ERR_CODE_IOE;
		}finally{
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 주어진 프로파일의 내용을 파싱하여 (key-value) 형태의 구조체 배열을 반환한다.
	 * @param property String
	 * @return ArrayList
	 */
	@SuppressWarnings("unused")
	public static ArrayList<Map<String, String>> loadPropertyFile(String property){

		// key - value 형태로 된 배열 결과
		ArrayList<Map<String, String>> keyList = new ArrayList<Map<String, String>>();
		
		String src = property.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		FileInputStream fis = null;
		try
		{   
			
			File srcFile = new File(src);
			if (srcFile.exists()) {
				
				java.util.Properties props = new java.util.Properties();
				fis  = new FileInputStream(src);
				props.load(new java.io.BufferedInputStream(fis));
				fis.close();
				
				int i = 0;
				Enumeration<?> plist = props.propertyNames();
				if (plist != null) {
					while (plist.hasMoreElements()) {
						Map<String, String> map = new HashMap<String, String>();
						String key = (String)plist.nextElement();
						map.put(key, props.getProperty(key));
						keyList.add(map);
					}
				}
			}
		} catch (Exception ex){
		    ex.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return keyList;
	}
	
	/**
	 * properties 파일을 가져온다.
	 * 
	 * */
	public static File getPropertiesFile(IJavaProject project){ 
		
		//우클릭한 프로젝트에서 src하위의 폴더 및 파일들에 대한 정보를 가져옵니다.
		//대리님은 기존의 IProject에서 JavaCore.create(IProject형태) 이걸로 캐스팅을 해야합니다.
		/* 그리고 이런식으로 선택한 프로젝트의 로컬 경로를 가져와야 합니다. 
		 * IPath firstPath = context.getJavaProject().getProject().getLocation(); //=>> C:/eGovRCP/runtime-EclipseApplication(1)/001
		
			String[] realPath = firstPath.toString().split(context.getJavaProject().getProject().getName()); //=>> C:/eGovRCP/runtime-EclipseApplication(1)/
		
			String path =  realPath[0]+ EgovProperties.getPropertiesFile(context.getJavaProject());
		 * 
		 * */
		
		
		IProgressMonitor monitor = new NullProgressMonitor();
		
		try {
			List<IPackageFragmentRoot> realList = null;
			
			realList = EgovJavaElementUtil.getSourceFolders(project);
			actualFile = null;
			
			
			for (int i = 0; i < realList.size(); i++) {

				foundFiles = findFiles(realList.get(i).getResource()
						.getParent().members(), "", "globals.properties", true, //하위에서 globals.properties를 찾아온다
						monitor);
				for (int j = 0; j < foundFiles.size(); j++) {

					Map<?, ?> map = (Map<?, ?>) foundFiles.get(j);
					Object f = map.get(BTextSearchUtil.K_FILE);

					if (f instanceof IFile) {
						actualFile = ((IFile) f).getFullPath().toFile(); 
						// map형태의 list안에 반환된 파일을 끄집어 내서 file형태(path형태로 나옴)로 만들어 준다
					}
				}

			}

		} catch (JavaModelException e) {
			// e.printStackTrace();
			debug(e);
		} catch (CoreException e) {
			// e.printStackTrace();
			debug(e);
		}
		return actualFile;
		
		
		
	}
	
	/**
	 * 파일 찾아서 가져오는 기능
	 *  
	 */
	public static List<?> findFiles(IResource[] roots, String includedText,
			String qualifiedFileName, boolean isRegexSearch,
			IProgressMonitor monitor) {
		List<?> result = null;
		String[] fileNamePattern = new String[] { qualifiedFileName };

		String includedText2 = includedText;
		
		if (includedText == null)
			includedText2 = "";
		

		FileTextSearchScope scope = null;
		if (roots == null)
			scope = FileTextSearchScope
					.newWorkspaceScope(fileNamePattern, true);
		else
			scope = FileTextSearchScope.newSearchScope(roots, fileNamePattern,
					true);

		Pattern pattern = BTextSearchUtil.createPattern(includedText2, false,
				isRegexSearch);
		boolean isFileSearchOnly = (pattern.pattern().length() == 0);

		BTextSearchRequestor collector = new BTextSearchRequestor(
				isFileSearchOnly, qualifiedFileName);

		result = BTextSearchUtil.findFiles(scope, pattern, collector, monitor);
		return result;
	}
	
	/**
	 * 시스템 로그를 출력한다.
	 * @param obj Object
	 */	
	private static void debug(Object obj) {
		if (obj instanceof java.lang.Exception) {
			((Exception)obj).printStackTrace();
		}
	}
	
	
	
}

