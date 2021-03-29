package egovframework.dev.imp.commngt.wizards.operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import egovframework.dev.imp.commngt.EgovCommngtPlugin;
import egovframework.dev.imp.commngt.common.CommngtLog;
import egovframework.dev.imp.commngt.util.ComResourceUtilVO;
import egovframework.dev.imp.commngt.util.ComResourceUtils;
import egovframework.dev.imp.commngt.wizards.model.IComponentElement;
import egovframework.dev.imp.commngt.wizards.model.NewEgovCommngtContext;
import egovframework.dev.imp.commngt.wizards.pages.CustomizeTableCreationPage;
import egovframework.dev.imp.ide.common.Policy;
import egovframework.dev.imp.ide.common.ResourceUtils;
/**
 * 공통컴포넌트 생성 오퍼레이션  클래스
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
public class ComponentAssembleOperation implements IRunnableWithProgress{
	
	protected NewEgovCommngtContext context;
	CustomizeTableCreationPage datasourcePage;
	
	/*
	 * 생성자
	 */
	public ComponentAssembleOperation( NewEgovCommngtContext context) {
		this.context = context;
	}
	
	/*
	 * 공통컴포넌트 추가
	 */
	@SuppressWarnings("unchecked")
	public void addComponent(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		
		try {
			
			IFile file = null;
			
			List<IComponentElement> components = context.getComponent();
			
			//프로젝트를 가져온다.
			IProject project = context.getJavaProject().getProject();
			
			//공통컴포넌트 생성정보 파일에 남길 내용 (2011.10.25)
			String egovComponentInfo = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			Date dDate = new Date();
			String time = sdf.format(dDate);
			
			for(int i=0 ; components.size() > i ; i++) {
				
				monitor.beginTask("", components.size()+3);
				
				int work=0;
				
				monitor.worked(++work);
				monitor.worked(++work);
				monitor.worked(++work);
				
				// get File(in Plug-in)
				String fileName = "examples/" + components.get(i).getFileName();
				URL url = FileLocator.toFileURL(EgovCommngtPlugin.getDefault().getBundle().getEntry(fileName));
				URL resolvedUrl = FileLocator.resolve(url);
				
				ZipFile zipFile = new ZipFile(new File(resolvedUrl.getFile()), "UTF-8");
				Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();
				
				ZipEntry entry = null;
				
				//프로젝트에서 사용하는 web.xml을 가져와 dispatcher-servlet, context파일이 위치한 경로를 읽어 ComResourceUtilVO에 담는다.
				ComResourceUtilVO comVo = ComResourceUtils.getDispatcherAndContextLocation(context.getJavaProject());
				
				ComResourceUtilVO prefixVo = ComResourceUtils.getDispatcherPrefixLocation(context.getJavaProject(), comVo);
				
				String type = null;
				String name = null;
				
				while (enumeration.hasMoreElements()) {

					entry = (ZipEntry) enumeration.nextElement();
					
					name = entry.getName();
					
					monitor.subTask("Install Common Component : "+ name);
					
					if(name != null && name.indexOf("/web.xml")>-1){
						type = "web.xml";
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							// 파일을 생성한다.
							ComResourceUtils.copyWebXmlFile(context, project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1), type);
						}
						
					} else if(name != null && name.indexOf("pom.xml")>-1){
						type = "pom.xml";
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							// 파일을 생성한다.
							ComResourceUtils.modifyPom(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
						}
						
					} else if(name != null && name.indexOf("context-common.xml")>-1){

						if(!entry.isDirectory()){
							
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							
							// 같은 경로에 context-common.xml파일이 있을 경우에는 컴포넌트를 재설치 하는것으로 간주하고 덮어씌운다.
//							if(file.isAccessible() && "egovframework.com.cmm.Wizard.2.0.0.zip".equals(components.get(i).getFileName())){
//								ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
//								
//							} else {
								// 경로가 틀릴경우 xml 내부의 messageSource 노드를 확인, 수정하는 작업을 수행한다.
								// 파일을 수정한다.
							System.out.println(name);
							System.out.println(entry.getName());
								boolean newComp = ComResourceUtils.modifyMessageSource(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
								
					    		// xml 내부의 leavertrace 노드를 확인하여 수정하는 작업을 수행한다.
					    		boolean newLeavea = ComResourceUtils.modifyLeaverTraceSource(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1), newComp);
					    		
					    		// 공통컴포넌트(zip)에 파일이 존재하고, AS-IS 시스템에 파일이 존재 하지 않을경우 파일 복사를 수행한다.
					    		if(!newComp && !newLeavea) {
					    			ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
					    		}
//							}
						}
						
					} else if(name != null && name.indexOf("context-validator.xml")>-1){
						
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							
							// 같은 경로에 egov-com-servlet.xml파일이 있을 경우에는 컴포넌트를 재설치 하는것으로 간주하고 덮어씌운다.
//							if(file.isAccessible()){
//								ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
//								
//							} else {
							
								// 파일을 생성한다.
								boolean fileExist = ComResourceUtils.copyValidator(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1), comVo);
								
								if(!fileExist) {
									ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
								}
							
//							}
							
						}
						
					} else if(name != null && name.indexOf("egov-com-servlet.xml")>-1){
						
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							
							// 같은 경로에 egov-com-servlet.xml파일이 있을 경우에는 컴포넌트를 재설치 하는것으로 간주하고 덮어씌운다.
							if(file.isAccessible()){
								ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
								
							} else {
							
							// 파일을 생성한다.
							ComResourceUtils.copyDispatcherServletFile(context, project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1), comVo);
							
							}
							
						}
						
					} else if(name != null && name.endsWith(".jsp")){
						
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							// 파일을 생성한다.
							if(prefixVo == null || prefixVo.getPrefixPattern() == null) {
								ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
							}else {
								ResourceUtils.createFolderFile(project, entry.getName().replace("/WEB-INF/jsp/", prefixVo.getPrefixPattern()), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
							}
						}
						
					} else {
						if(!entry.isDirectory()){
							// 파일을 생성하기 위한 파일명 및 경로를 구성한다.
							file = project.getFile(entry.getName());
							// 파일을 생성한다.
							ResourceUtils.createFolderFile(project, entry.getName(), zipFile.getInputStream(entry), Policy.subMonitorFor(monitor, 1));
						}
					}
					
					type = "";

				}
				//공통컴포넌트 생성정보 파일에 쓸 내용 - (현재시간 \t 설치 컴포넌트 패키지 명 \t 설치 컴포넌트 명) (2011.10.25)
				egovComponentInfo = egovComponentInfo + "\n[" + time + "] Installed component:\t" + components.get(i).getPackageName() + "\t" + components.get(i).getName();
				
				if(monitor.isCanceled()){
					throw new InterruptedException();
				} else {
					monitor.worked(++work);
				}
				
			}
			
			//공통컴포넌트 생성정보 생성로직 추가 (2011.10.25)
			String egovComponentInfoFile = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()+"/.metadata/.egovComponentInfo";
			try{
				File f = new File(egovComponentInfoFile);
				FileWriter fw = new FileWriter(f, true);
				fw.write(egovComponentInfo);
				fw.close();
			} catch (Exception ee) {
			}
			
			monitor.done();
			
	    } catch (IOException ie) {
	    	CommngtLog.logError(ie);
	    	throw new InvocationTargetException(ie);
	    } catch (CoreException ce) {
	    	CommngtLog.logCoreError(ce);
	    	throw new InvocationTargetException(ce);
		}
	    
	}
	
	/*
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		addComponent(monitor);
		
		if(context.getInstallType().indexOf("basic") > -1){
			datasourcePage = new CustomizeTableCreationPage("insertDatasource", context);
			datasourcePage.performOk(monitor);
		}
		
		if(context.getInstallType().indexOf("user") > -1){
			datasourcePage = new CustomizeTableCreationPage("insertDatasource", context);
			datasourcePage.performOk(monitor);
		}
	}
}
