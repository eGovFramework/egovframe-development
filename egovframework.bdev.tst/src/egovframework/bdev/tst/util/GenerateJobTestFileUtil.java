package egovframework.bdev.tst.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import egovframework.bdev.tst.common.BatchTestLog;
import egovframework.bdev.tst.wizards.model.BatchJobTestContext;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Job Test시 사용할 파일 생성
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.07.18
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.07.18  최서윤          최초 생성
 * 
 * 
 * </pre>
 */
public class GenerateJobTestFileUtil {

	/**
	 * 사용자 설정 정보에 따라 테스트 파일 구성
	 * 
	 * @param context
	 * @param jobIDExistFileList 
	 * @return isFileCreateSucccess
	 */
	public static Boolean GenerateJobTestFile(BatchJobTestContext context, LinkedHashMap<String, String> jobIDExistFileList){

		Boolean isFileCreateSucccess = false;

		String fileName = context.getFileName().split(".java")[0];
		//Path path = new Path("4TEST_ParamNo.1/src/test/java/MakeTestFile.java");
		Path path = new Path(context.getFolderPath()+"/"+context.getFileName());

		String jobFilePath = null;
		String jobExecFilePath = null;
		
		jobFilePath = getJobFilePath(jobIDExistFileList, context, jobFilePath);
		jobExecFilePath = getJobLauncherFilePath(context, jobExecFilePath);
		
		String jobRepositoryFilePath = null;
		String datasourceFilePath = null;
		String transactionManagerFilePath = null;

		String[] jobRepoAndDate = getJobRepositoryAndDatasourceFilePath(jobRepositoryFilePath, datasourceFilePath, context).split(">");
		if(jobRepoAndDate != null){
			jobRepositoryFilePath = jobRepoAndDate[0];
			if(jobRepoAndDate.length == 2){
				datasourceFilePath = jobRepoAndDate[1];
			}
			
			if(NullUtil.isNull(jobRepositoryFilePath)){
				jobRepositoryFilePath = "";
			}
			if(NullUtil.isNull(datasourceFilePath)){
				datasourceFilePath = "";
			}
		}
		
		String transactionManagerFoundPath = getTransactionManagerFilePath(transactionManagerFilePath, context);
		if(!NullUtil.isNull(transactionManagerFoundPath)){
			transactionManagerFilePath = transactionManagerFoundPath;
		} else {
			transactionManagerFilePath = "";
		}

		//contextConfig에 들어가는 파일들의 중복 여부 체크
		if(jobFilePath.equals(jobRepositoryFilePath)){
			jobRepositoryFilePath = "";
		} 
		if(jobFilePath.equals(datasourceFilePath)){
			datasourceFilePath = "";
		} 
		if(jobExecFilePath.equals(jobRepositoryFilePath)){
			jobRepositoryFilePath = "";
		} 
		if(jobExecFilePath.equals(datasourceFilePath)){
			datasourceFilePath = "";
		}
		if(jobRepositoryFilePath.equals(datasourceFilePath)){
			datasourceFilePath = "";
		}
		
		if(jobFilePath.equals(transactionManagerFilePath)){
			transactionManagerFilePath = "";
		} 
		if(jobExecFilePath.equals(transactionManagerFilePath)){
			transactionManagerFilePath = "";
		} 
		if(jobRepositoryFilePath.equals(transactionManagerFilePath)){
			transactionManagerFilePath = "";
		}
		if(datasourceFilePath.equals(transactionManagerFilePath)){
			transactionManagerFilePath = "";
		}
		
		
		String packageName = "";
		IFile newJunitTestFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		packageName = newJunitTestFile.toString();
		packageName = packageName.substring(packageName.indexOf("java")+ 4, packageName.lastIndexOf("/"));
		
		String egovBatchRunnerID = getEgovBatchRunnerID(context, jobExecFilePath);
		
		
		String partOne = "";
		if(packageName.length() > 0) { 
			 packageName = packageName.substring(1, packageName.length());
			 packageName = packageName.replaceAll("/", ".");
			 partOne = 
					"package " + packageName + ";\n" + 
					"import static org.junit.Assert.assertEquals;\n" +
					"import java.util.Date;\n";
		} else {
			partOne = 
				"import static org.junit.Assert.assertEquals;\n" +
				"import java.util.Date;\n";
		}
		   
		
		String simpleDateFormat = "";
		if(context.getJobParameterList() != null){
			for(int i = 0; i < context.getJobParameterList().length; i++){
				if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("date")){
					simpleDateFormat = "import java.text.SimpleDateFormat;\n";
				}
			}
		}
		
		String partTwo =
			"import org.junit.Test;\n" +
			"import org.junit.runner.RunWith;\n" +
			"import org.springframework.batch.core.BatchStatus;\n" +
			"import org.springframework.batch.core.JobParametersBuilder;\n" +
			"import org.springframework.beans.factory.annotation.Autowired;\n" +
			"import org.springframework.beans.factory.annotation.Qualifier;\n" +
			"import org.springframework.test.context.ContextConfiguration;\n" +
			"import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n" +
			"import org.egovframe.rte.bat.core.launch.support.EgovBatchRunner;\n" +
			"@RunWith(SpringJUnit4ClassRunner.class)\n\n";
		
		String annotation = "/** \n * Test File Information \n"+
			" * Job:: " + jobFilePath + "\n" +
			" * Job Launcher:: " + jobExecFilePath + "\n" +
			" * job Parameters:: " +jobParametersContent(context)+ "\n" +
			" */ \n\n";
		
		String contextConfigAndFileName = 
			"@ContextConfiguration(locations = { \"" + jobExecFilePath + "\", \"" + jobFilePath + "\"";
		
			if(jobRepositoryFilePath != "" && !jobRepositoryFilePath.equalsIgnoreCase("null") && jobRepositoryFilePath != null){
				contextConfigAndFileName += ", \"" + jobRepositoryFilePath +  "\"";
			}
		
			if(datasourceFilePath != "" && !datasourceFilePath.equalsIgnoreCase("null") && datasourceFilePath != null){
				contextConfigAndFileName += ", \"" + datasourceFilePath + "\"";
			}
			
			if(transactionManagerFilePath != "" && !transactionManagerFilePath.equalsIgnoreCase("null") && transactionManagerFilePath != null){
				contextConfigAndFileName += ", \"" + transactionManagerFilePath + "\"";
			}
			contextConfigAndFileName +=	
				" })\n" + "public class "+ fileName +"{\n\n\t";
			
		String autowired = "@Autowired\n\t";
		
		String egovBatchRunnerQualifier = "";
		
		if(egovBatchRunnerID != "" || egovBatchRunnerID.length() > 0){
			egovBatchRunnerQualifier = "@Qualifier(\"" + egovBatchRunnerID + "\")\n\t" +
			"private EgovBatchRunner egovBatchRunner;\n\n\t";
		} else {
			egovBatchRunnerQualifier = "private EgovBatchRunner egovBatchRunner;\n\n\t";
		}
		
		String dateFormat = "";
		if(context.getJobParameterList() != null){
			for(int i = 0; i < context.getJobParameterList().length; i++){
				if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("date")){
					dateFormat += "private SimpleDateFormat "+ context.getJobParameterList()[i].getParameterName()+ "= new SimpleDateFormat(\"" + context.getJobParameterList()[i].getDateFormat() + "\");\n\n\t";
				}
			}
		}
			
		String partThree =
			"@Test\n\tpublic void testJobRun() throws Exception {\n\n\t\t" +
			"String jobName = \"" + context.getJobVo().getJobName() + "\";\n\n\t\t" +
			"JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();\n\t\t";

		String timeStamp = "";
			timeStamp = "jobParametersBuilder.addLong(\"timestamp\", new Date().getTime());\n\t\t";

		String partFour = 
			//param Size 만큼 돌면서 jobParametersBuilder에 ADD
			AddJobParametersBuilder(context) +
			"\n\n\t\t" +
			"String jobParameters = egovBatchRunner.convertJobParametersToString(jobParametersBuilder.toJobParameters());\n\n\t\t" +
			"long executionId = egovBatchRunner.start(jobName, jobParameters);\n\n\t\t";
		
		String threadSleep = "";
		if(context.getIsAsync()){
			threadSleep = "if(egovBatchRunner.getJobExecution(executionId).getExitStatus().toString().contains(\"UNKNOWN\")){\n\t\t\t" +
			"Thread.sleep(10000);\n\t\t" +
			"}\n\n\t\t";
		}		
		
		String partFive = 
			"assertEquals(BatchStatus.COMPLETED, egovBatchRunner.getJobExecution(executionId).getStatus());\n\n\t" +
			"}\n" +
			"}";

		String content = partOne + simpleDateFormat + partTwo + annotation + contextConfigAndFileName + autowired + egovBatchRunnerQualifier + dateFormat + partThree + timeStamp + partFour + threadSleep + partFive;

		isFileCreateSucccess = createTestFile(content, newJunitTestFile, context, isFileCreateSucccess);

		return isFileCreateSucccess;
	}

	/**
	 * 테스트를 진행할 job이 있는 파일경로
	 * 
	 * @param jobIDExistFileList
	 * @param context
	 * @param jobFilePath
	 * @return jobFilePath
	 */
	private static String getJobFilePath(LinkedHashMap<String, String> jobIDExistFileList, BatchJobTestContext context, String jobFilePath){
		
		//위저드에 표현되는 JobIDList keySet
		Set<String> foundJobkey = jobIDExistFileList.keySet();
		Object[] jobkeyArray = foundJobkey.toArray();
		//프로젝트내에 JobID 형태가 있는 파일목록의 keySet
		Set<String> foundJobXMLKey = context.getFoundJobXMLfiles().keySet();
		Object[] jobXMLKeyArray = foundJobXMLKey.toArray();
		//contextConfig경로로 내려줄 JOB
		for(int i = 0; i < jobIDExistFileList.size(); i++){
			String key = (String)jobkeyArray[i];
			if(key.equals(context.getJobVo().getJobName())){
				if(jobIDExistFileList.get(key).contains("main/resources")||jobIDExistFileList.get(key).contains("test/resources")){
					jobFilePath = jobIDExistFileList.get(key).split("resources")[1];
					break;
				} else {
					IFile jobXmlFile = null;
					for(int j = 0; j < context.getFoundJobXMLfiles().size(); j++){
						String jobKey = (String)jobXMLKeyArray[j];
						if(jobKey.contains(jobIDExistFileList.get(key))){
							jobXmlFile = context.getFoundJobXMLfiles().get(jobKey);
							break;
						}
					}
					if(!NullUtil.isNull(jobXmlFile.getLocation())){
						jobFilePath = "file:" + jobXmlFile.getLocation();
					}
					break;
				}
			}
		}
		return jobFilePath;
	}

	/**
	 * 테스트를 진행할 jobLauncher 파일의 경로
	 * 
	 * @param context
	 * @param jobExecFilePath
	 * @return jobExecFilePath
	 */
	private static String getJobLauncherFilePath(BatchJobTestContext context, String jobExecFilePath){
		
		//위저드에 표현되는 JobExecList keySet
		Set<String> foundJobExeckey = context.getFoundJobExecXMLfiles().keySet();
		Object[] jobExeckeyArray = foundJobExeckey.toArray();
		//프로젝트내에 JobExec 파일목록의 keySet
		Set<String> foundJobExecXMLKey = context.getFoundJobExecXMLfiles().keySet();
		Object[] jobExecXMLKeyArray = foundJobExecXMLKey.toArray();
		//contextConfig경로로 내려줄 JOBExec
		for(int i = 0; i < context.getFoundJobExecXMLfiles().size(); i++){
			String key = (String)jobExeckeyArray[i];
			if(key.contains(context.getJobExecName())){
				if(key.contains("main/resources")||key.contains("test/resources")){
					jobExecFilePath = key.split("resources")[1];
					break;
				} else {
					IFile jobExecXmlFile = null;
					for(int j = 0; j < context.getFoundJobExecXMLfiles().size(); j++){
						String jobExecKey = (String)jobExecXMLKeyArray[j];
						if(jobExecKey.contains(context.getJobExecName())){
							jobExecXmlFile = context.getFoundJobExecXMLfiles().get(jobExecKey);
							break;
						}
					}
					if(!NullUtil.isNull(jobExecXmlFile.getLocation())){
						jobExecFilePath = "file:" + jobExecXmlFile.getLocation();
					}
					break;
				}
			}
		}
		return jobExecFilePath;
	}
	
	/**
	 * jobRepository와 datasource를 지닌 파일의 경로
	 * 
	 * @param jobRepositoryFilePath
	 * @param datasourceFilePath
	 * @param context
	 * @return jobRepoAndDate
	 */
	private static String getJobRepositoryAndDatasourceFilePath(String jobRepositoryFilePath, String datasourceFilePath, BatchJobTestContext context){
		
		String jobRepoAndDate = null;

		if(context.getJobRepositoryXMLFile() != null){
			
			if(context.getJobRepositoryXMLFile().getRawLocation().toString().contains("main/resources")||
					context.getJobRepositoryXMLFile().getRawLocation().toString().contains("test/resources")){
				jobRepositoryFilePath = context.getJobRepositoryXMLFile().getRawLocation().toString().split("resources")[1];
			} else {
				jobRepositoryFilePath = "file:" + context.getJobRepositoryXMLFile().getRawLocation().toString();
			}
			
			if(context.getDatasourceXMLFile() != null){
				
				if(context.getDatasourceXMLFile().getRawLocation().toString().contains("main/resources")||
						context.getDatasourceXMLFile().getRawLocation().toString().contains("test/resources")){
					datasourceFilePath = context.getDatasourceXMLFile().getRawLocation().toString().split("resources")[1];
				} else {
					datasourceFilePath = "file:" + context.getDatasourceXMLFile().getRawLocation().toString();
				}
			}
		}
		
		jobRepoAndDate = jobRepositoryFilePath + ">" + datasourceFilePath;
		
		return jobRepoAndDate;
	}
	
	/**
	 * jobRepository와 datasource를 지닌 파일의 경로
	 * 
	 * @param jobRepositoryFilePath
	 * @param datasourceFilePath
	 * @param context
	 * @return jobRepoAndDate
	 */
	private static String getTransactionManagerFilePath(String transactionManagerFilePath, BatchJobTestContext context){
		
		String transactionManagerFoundFilePath = null;

		if(context.getTransactionManagerXMLFile() != null){
			
			if(context.getTransactionManagerXMLFile().getRawLocation().toString().contains("main/resources")||
					context.getTransactionManagerXMLFile().getRawLocation().toString().contains("test/resources")){
				transactionManagerFoundFilePath = context.getTransactionManagerXMLFile().getRawLocation().toString().split("resources")[1];
			} else {
				transactionManagerFoundFilePath = "file:" + context.getTransactionManagerXMLFile().getRawLocation().toString();
			}
			
		}
		
		transactionManagerFilePath = transactionManagerFoundFilePath;
		
		return transactionManagerFilePath;
	}
	
	/**
	 * param Size 만큼 돌면서 jobParameters주석 정보 구성
	 * 
	 * @param context
	 * @return testFileParamString
	 */
	private static String jobParametersContent(BatchJobTestContext context) {

		String testFileParamString = "";
		String stringContent = "String_";
		String longContent = "Long_";
		String doubleContent = "Double_";
		String dateContent = "Date_";
		
		if(context.getJobParameterList() != null){
			for(int i = 0; i < context.getJobParameterList().length; i ++){
				if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("string")){
					stringContent += context.getJobParameterList()[i].getValue() + ",";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("long")) {
					longContent += context.getJobParameterList()[i].getValue() + ",";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("double")) {
					doubleContent += context.getJobParameterList()[i].getValue() + ",";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("date")) {
					dateContent += context.getJobParameterList()[i].getValue() + ",";
				}
			}
			
			if(!stringContent.equals("String_")){
				testFileParamString = stringContent + " ";
			}
			if(!stringContent.equals("Long_")){
				testFileParamString += longContent + " ";
			}
			if(!stringContent.equals("Double_")){
				testFileParamString += doubleContent + " ";
			}
			if(!stringContent.equals("Date_")){
				testFileParamString += dateContent + " ";
			}
			testFileParamString = testFileParamString.substring(0, testFileParamString.length() - 2);
		} else {
			testFileParamString = "Date_Default Timestamp";
		}
		return testFileParamString;
	}
	
	/**
	 * 선택한 런쳐파일에서 EgovBatchRunner의 ID를 가져온다
	 * @param context 
	 * @param jobExecFilePath 
	 * @return egovBatchRunnerID
	 */
	private static String getEgovBatchRunnerID(BatchJobTestContext context, String jobExecFilePath) {
		String egovBatchRunnerID = "";
		IFile usingLauncherFile = null;
		for(int i = 0; i < context.getFoundJobExecXMLfiles().keySet().toArray().length; i++){
			if(context.getFoundJobExecXMLfiles().keySet().toArray()[i].toString().contains(jobExecFilePath)){
				usingLauncherFile = context.getFoundJobExecXMLfiles().get(context.getFoundJobExecXMLfiles().keySet().toArray()[i]);
			}
			if(NullUtil.isNull(usingLauncherFile)){
				if(jobExecFilePath.contains(context.getFoundJobExecXMLfiles().keySet().toArray()[i].toString().substring(2))){
					usingLauncherFile = context.getFoundJobExecXMLfiles().get(context.getFoundJobExecXMLfiles().keySet().toArray()[i]);
				}
			}
			if(!NullUtil.isNull(usingLauncherFile)){
				break;
			}
		}
		Map<String, IFile> beanID = new LinkedHashMap<String, IFile>();
		beanID = FindBeanIdValueUtil.findBeanIDList(usingLauncherFile, "/beans/bean", "EgovBatchRunner", "class", "id");
		if(!NullUtil.isNull(beanID)){
			egovBatchRunnerID = beanID.keySet().toArray()[0].toString();
			
		}
		
		return egovBatchRunnerID;
	}
	
	/**
	 * param Size 만큼 돌면서 jobParametersBuilder에 ADD
	 * 
	 * @param context
	 * @return testFileParamString
	 */
	private static String AddJobParametersBuilder(BatchJobTestContext context) {

		String testFileParamString = "";
		if(context.getJobParameterList() != null){
			for(int i = 0; i < context.getJobParameterList().length; i ++){
				if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("string")){
					testFileParamString += "jobParametersBuilder.addString(\""+ context.getJobParameterList()[i].getParameterName()+"\", \"" + context.getJobParameterList()[i].getValue() +"\");\n\t\t";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("long")) {
					testFileParamString += "jobParametersBuilder.addLong(\""+ context.getJobParameterList()[i].getParameterName()+"\", Long.parseLong(\"" + context.getJobParameterList()[i].getValue() +"\"));\n\t\t";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("double")) {
					testFileParamString += "jobParametersBuilder.addDouble(\""+ context.getJobParameterList()[i].getParameterName()+"\", Double.parseDouble(\"" + context.getJobParameterList()[i].getValue() +"\"));\n\t\t";
				} else if(context.getJobParameterList()[i].getDataType().equalsIgnoreCase("date")) {
					testFileParamString += "jobParametersBuilder.addDate(\""+ context.getJobParameterList()[i].getParameterName()+"\", "+ context.getJobParameterList()[i].getParameterName() +".parse(\"" + context.getJobParameterList()[i].getValue() +"\"));\n\t\t";
				}
			}
		}
		return testFileParamString;
	}
	
	/**
	 * 사용자 설정 정보에 따라 테스트 파일 생성
	 * 
	 * @param content
	 * @param isFileCreateSucccess 
	 * @param context 
	 * @param newJunitTestFile 
	 * @return isFileCreateSucccess
	 */
	private static Boolean createTestFile(String content, IFile newJunitTestFile, BatchJobTestContext context, Boolean isFileCreateSucccess){

		try {
			InputStream inStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

			newJunitTestFile.create(inStream, true, null);
			if(newJunitTestFile.isAccessible()){
				isFileCreateSucccess = true;
				context.setJobTestFile(newJunitTestFile.getFullPath().toOSString());
			} else {
				return isFileCreateSucccess;
			}


		} catch (UnsupportedEncodingException e) {
			BatchTestLog.logError(e);
		} catch (CoreException e) {
			BatchTestLog.logError(e);
		}

		return isFileCreateSucccess;
	}
}
