import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import egovframework.rte.rdt.plugin.model.DependencyList;
import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.pom.unit.Pom;
import egovframework.rte.rdt.pom.unit.Version;
import egovframework.rte.rdt.service.parser.ServicesParser;
import egovframework.rte.rdt.service.unit.Service;

public class TableListTest {

	DependencyList installedList = null;
	DependencyList notInstalledList = null;

	//@Test
	public void TestInsertList() throws PomException {

		File input = new File("./src/test/resources/pom11");
		Pom pom = PomParser.parse(input);
		
//		Dependency d = new Dependency();
//		d.setGroupId("aaaaaaaaaa");
//		d.setArtifactId("bbbb");
//		d.setScope("ccc");
//		d.setVersion(new Version("59900.0.1"));
//		pom.insertDependency(d);
//		pom.commit();
//		System.out.println("-**********************************************************");
	
		
//		List<Dependency> dependencyList = pom.listDependencies();
		
//		for (Dependency d2 : dependencyList) {
//			System.out.println(d2);
//		}

//		System.out.println("****");
//		Dependency dependencyTochange = dependencyList.get(0);
		//System.out.println(dependencyTochange);
		pom.changeVersion("aaaaaaaaaa.bbbb", new Version("777.7"));
		
		File f = new File("./src/test/resources/pom11.xml");
		pom.commit(f);
//		
//		Object[] array = installedList.getDependencyList()
//		.values().toArray();
//		
//		 for (int i=0;i<array.length;i++) {
//			 Dependency dd = (Dependency)array[i];
//			 if(dd.getArtifactId().equals("junit")){
//			 }
//			 System.out.println(dd);
//			 }
//		 
		
		// //notInstalledList 출력


//		notInstalledList = new DependencyList(
//				"./src/test/resources/pom_master.xml");
//
//		Object[] currentPomDependencyArray = installedList.getDependencyList()
//				.values().toArray();
//		Object[] notInsArray = notInstalledList.getDependencyList().values()
//				.toArray();
//
//		for (int i = 0; i < notInsArray.length; i++) {
//			Dependency notInsDependency = (Dependency) notInsArray[i];
//			for (int j = 0; j < currentPomDependencyArray.length; j++) {
//				Dependency insDependency = (Dependency) currentPomDependencyArray[j];
//				if (notInsDependency.getArtifactId().equals(insDependency.getArtifactId())) {
//					((Dependency) notInsArray[i]).setIsinstalled(true);
//				}
//			}
//
//		}
//
//		for (int i = 0; i < notInsArray.length; i++) {
//			Dependency d = (Dependency) notInsArray[i];
//			if (d.isIsinstalled())
//				System.out.println(d);
//		}
	}
	
	//@Test
	@SuppressWarnings({ "null", "unused" })
	public void serviceTest(){
		Map<String, Service> serviceMap = null;
		
		/** excel */
		Service s = new Service("Apache POI", "String Util");
		s.setName("Excel");
		serviceMap.put(s.getName(), s);
		
		s = new Service();
		s.setName("Apache POI");
		serviceMap.put(s.getName(), s);

		s = new Service();
		s.setName("String Util");
		serviceMap.put(s.getName(), s);

		s = new Service();
		s.setName("Commons FileUpload");
		serviceMap.put(s.getName(), s);
		
		//설치할 목록
		ArrayList<String> installList = new ArrayList<String>();
		
		//자기자신 설치
		
		//for - 의존성 하나씩
		//설치여부검사
		//설치안된것의 의존성 검사
//		System.out.println(s.getDependency().toString());
		System.out.println(serviceMap.toString());
		
	}
	
	//@Test
	public void serviceTest2() throws PomException{
		File file = new File("c:/services.xml");
	ArrayList<Service> serviceList = ServicesParser.parse(file);
	System.out.println(serviceList.toString());
	}
	
//	@Test
	public void systemTest() throws IOException{

		Properties properties = System.getProperties();

		for(Entry<?, ?> entry : properties.entrySet()) {

		System.out.println(entry.getKey()+"="+entry.getValue());

		}
		
		System.out.println(	System.getProperty("user.dir"));
//		String szLine; 

//		Runtime.getRuntime().exec("mvn.bat");
//		 Process process = Runtime.getRuntime().exec("%maven_home%\\bin\\mvn.bat");		
//		 Process process = Runtime.getRuntime().exec("mvn.bat");		
//		
//		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        // 응용 프로그램의 입력 스트림에서 한 라인씩 읽어서 응용 프로그램의 표준 출력으로 보낸다        
//        while ((szLine = br.readLine()) != null)
//             System.out.println(szLine);

	}
	
//	@Test
//	public void debugToolTest() throws CoreException {
//		  ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
//		
//		  ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(org.eclipse.ui.externaltools.internal.model.IExternalToolConstants.ID_PROGRAM_LAUNCH_CONFIGURATION_TYPE);		  
//		  
////		 ILaunchConfigurationType launchConfigurationType =
////             launchManager
////                 .getLaunchConfigurationType("org.anyframe.ide.eclipse.core.Maven2LaunchConfigurationType");
//		 
//		 ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
//		 
//		 ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "rdt:install");
//		 workingCopy.setAttribute(org.eclipse.ui.externaltools.internal.model.IExternalToolConstants.ATTR_LOCATION, "mvn.bat");
//         workingCopy.setAttribute(org.eclipse.ui.externaltools.internal.model.IExternalToolConstants.ATTR_TOOL_ARGUMENTS, "install");
//         workingCopy.setAttribute(org.eclipse.ui.externaltools.internal.model.IExternalToolConstants.ATTR_WORKING_DIRECTORY, "c:\\pjt2011\\workspace_temp\\test1");
//		 
//		 
//         ILaunchConfiguration configuration = workingCopy.doSave();
//         DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
//	}
	
}
