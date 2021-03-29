package egovframework.bdev.tst.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;

import egovframework.bdev.tst.common.BatchTestLog;
import egovframework.bdev.tst.wizards.model.BatchJobTestContext;
import egovframework.dev.imp.core.utils.BTextSearchUtil;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Job 테스트를 위해 프로젝트내 파일을 찾는 클래스 
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.07.26
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.07.26  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class FindFilesinProjectUtil {

	
	/** 선택한 프로젝트 정보*/
	private static Object selected;
	
	/**
	 * selection에서 프로젝트 정보를 가져와 해당 프로젝트 내에 조건에 따라 XML파일 가져오기
	 *  
	 * @param context
	 * @param selection
	 * @param type 
	 */
	public static void findXMLFiles(BatchJobTestContext context, IStructuredSelection selection, String type) {

//		if(selected != selection.getFirstElement()){
//			foundJobXMLfiles.clear();
//			foundJobExecXMLfiles.clear();
//		}
		/** 선택한 프로젝트 내 Job XML파일을 검색한 리스트*/
		Map<String, IFile> foundJobXMLfiles = new HashMap<String, IFile>();
		/** 선택한 프로젝트 내 Job Execution XML파일을 검색한 리스트*/
		Map<String, IFile> foundJobExecXMLfiles = new HashMap<String, IFile>();
		
		IJavaProject javaProject = null;
		Object selected = selection.getFirstElement();

		if(selected != null){
			
			if (selected instanceof IResource) {
				if(((IResource) selected).getProject() != null){
					javaProject = JavaCore.create( ((IResource) selected).getProject() );
				}
			} else if(selected instanceof IJavaElement){
				if(((IJavaElement) selected).getJavaProject().getProject() != null){
					javaProject = ((IJavaElement) selected).getJavaProject(); 
				}
			}
			
			
			IProgressMonitor monitor = new NullProgressMonitor();
			List<?> foundList = null;
			IFile actualFile = null;
			List<IPackageFragmentRoot> foundFolders = null;
			try {
				foundFolders = EgovJavaElementUtil.getSourceFolders(javaProject);
			} catch (CoreException e) {
				BatchTestLog.logError(e);
			} 
			if(foundFolders.size() > 0) {
				if(!NullUtil.isNull(foundFolders.get(0).getResource().getParent()) && !NullUtil.isNull(foundFolders.get(0).getResource().getParent().getParent()) && !NullUtil.isNull(foundFolders.get(0).getResource().getParent().getParent().getParent())){
				try {
						foundList = BTextSearchUtil.findFiles(foundFolders.get(0).getResource().getParent().getParent().getParent().members(), type, "*.xml", false, monitor);
					
				} catch (CoreException e) {
					BatchTestLog.logError(e);
				}
				for (int j = 0; j < foundList.size(); j++) {

					Map<?, ?> map = (Map<?, ?>) foundList.get(j);
					if(map != null) {
						Object f = map.get(BTextSearchUtil.K_FILE);
	
						if (f instanceof IFile) {
							actualFile = (IFile) f; 
							if(!(actualFile.toString().contains("target") || actualFile.toString().contains("eGovBase")))
							{
								if(type == "<job"){
									foundJobXMLfiles.put(j+actualFile.toString(), actualFile);
								} else {
									foundJobExecXMLfiles.put(j+actualFile.toString(), actualFile);
								}
							}
						}
					}
				}
			}
			if(type == "<job"){
				context.setFoundJobXMLfiles(foundJobXMLfiles);
			} else {
				context.setFoundJobExecXMLfiles(foundJobExecXMLfiles);
				selected = null;
			}
			}
		}
	}
	
	
	/**
	 * 선택한 프로젝트내의 특정확장자내 String을 찾아 파일들 다 가져오기
	 * 
	 * @param context
	 * @param includedText 파일내 찾는 단어
	 * @param qualifiedFileName 찾는 파일들의 확장자
	 * @return foundXMLfiles
	 */
	public static Map<String, IFile> findXMLFiles(BatchJobTestContext context, String includedText, String qualifiedFileName, Boolean isXML){
		
		LinkedHashMap<String, IFile> foundXMLfiles = new LinkedHashMap<String, IFile>();
		
		IJavaProject javaProject = null;
		selected = context.getSelection().getFirstElement();

		if(selected != null){

			if (selected instanceof IResource) {
				if(((IResource) selected).getProject() != null){
					javaProject = JavaCore.create( ((IResource) selected).getProject() );
				}
			} else if(selected instanceof IJavaElement){
				if(((IJavaElement) selected).getJavaProject().getProject() != null){
					javaProject = ((IJavaElement) selected).getJavaProject(); 
				}
			}

		

			IProgressMonitor monitor = new NullProgressMonitor();
			List<?> foundList = null;
			IFile actualFile = null;
			List<IPackageFragmentRoot> foundFolders = null;
			try {
				foundFolders = EgovJavaElementUtil.getSourceFolders(javaProject);
			} catch (CoreException e) {
				BatchTestLog.logError(e);
			} 
			
			if(foundFolders.size() > 0) {
				try {
					foundList = BTextSearchUtil.findFiles(foundFolders.get(0).getResource().getParent().getParent().getParent().members(), includedText, qualifiedFileName, false, monitor);
				} catch (CoreException e) {
					BatchTestLog.logError(e);
				}

				for (int j = 0; j < foundList.size(); j++) {

					Map<?, ?> map = (Map<?, ?>) foundList.get(j);
					Object f = map.get(BTextSearchUtil.K_FILE);

					if (f instanceof IFile) {
						actualFile = (IFile) f; 

						if(isXML){
							if(!actualFile.toString().contains("pom.xml")){
								if(!actualFile.toString().contains("target")){
									foundXMLfiles.put(actualFile.toString(), actualFile);
								}
							}
						} else {
							if(!actualFile.toString().contains("target")){
								foundXMLfiles.put(actualFile.toString(), actualFile);
							}
						}
					}
				}
			}
		}
		return foundXMLfiles;
	}
}
