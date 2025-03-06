package egovframework.bdev.imp.batch.wizards.joblauncher.operation;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import egovframework.bdev.imp.batch.wizards.com.BatchJobContext;
import egovframework.bdev.imp.batch.wizards.com.HandlingFileOperation;
import egovframework.bdev.imp.batch.wizards.joblauncher.model.BatchJobLauncherContext;

/**
 * Job 실행모듈 XML 파일을 생성 하는 클래스
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.07.26
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *  수정일        수정자        수정내용
 *  ----------  --------    ---------------------------
 *  2012.07.26  최서윤        최초 생성
 *  2012.07.26  신용호        표준프레임워크 v4.0 패키지 적용
 * 
 * 
 * </pre>
 */

public class CreateBatchJobLauncherXMLFileOperation {

	
	/** JobExecution default 항목*/
	private HashMap<String, String> jobExecutionDefault = new HashMap<String, String>();
	/** JobExecution other 항목*/
	private HashMap<String, String> jobExecutionOption = new HashMap<String, String>();
	/** repository type이 DB일 경우 database 항목*/
	private HashMap<String, String> databaseInfo = new HashMap<String, String>();
	/** 사용자 지정 작업 실행 context 정보*/
	private BatchJobLauncherContext jobExecutorContext = new BatchJobLauncherContext();
	/** lobHandler의 prefix로 사용할 JobLauncherId*/
	private String jobLauncherId = "";
	
	/**
	 * context 정보들 가져와서 셋팅
	 * 
	 * @param context
	 * 
	 * */
	public void setContext(BatchJobContext context){
		
		if(context != null){
			
			if(context instanceof BatchJobLauncherContext){
				jobExecutorContext = (BatchJobLauncherContext)context;
				
				jobExecutionDefault.put("jobLauncher", jobExecutorContext.getJobLauncerId());
				jobLauncherId = jobExecutorContext.getJobLauncerId();
				if(!jobExecutorContext.isSyncMode()){
					jobExecutionDefault.put("syncMode", "async"+"<"+"org.springframework.core.task.SimpleAsyncTaskExecutor");
				}
				
				// Repository Type null check
				if(jobExecutorContext.getRepositoryType() != null ) {
					// Repository Type 항목: "DB(Reference)", "DB(New)", "Memory"
					if(! jobExecutorContext.getRepositoryType().equals("Memory")){
						// DB(Reference/New) Repository Type case
						jobExecutionDefault.put("datasourceBeanID", jobExecutorContext.getDatasourceBeanID());

						// jobRepository bean 중복 생성 방지
						if(!jobExecutorContext.getIsJobRepositoryExist()) {
							jobExecutionDefault.put("jobRepository", "jobRepository<org.springframework.batch.core.repository.support.JobRepositoryFactoryBean");
						}
						jobExecutionDefault.put("jobExplorer", jobExecutorContext.getExplorerId()+"<"+"org.springframework.batch.core.explore.support.JobExplorerFactoryBean");

						// transactionManager bean 중복 생성 가능하도록 수정
						jobExecutionOption.put("transactionManager", "org.springframework.jdbc.datasource.DataSourceTransactionManager");
						
						// Repository Type이 DB(New) 일때만 databaseInfo 설정
						if(jobExecutorContext.getRepositoryType().equals("DB(New)")) {
							databaseInfo.put("driverClassName", "driverClassName" + "<" + jobExecutorContext.getDriverClassName());
							databaseInfo.put("url", "url" + "<" + jobExecutorContext.getUrl());
							databaseInfo.put("username", "username" + "<" + jobExecutorContext.getUserName());
							databaseInfo.put("password", "password" + "<" + jobExecutorContext.getPasswd());
						}
						// jdbcTemplate bean 중복 생성 방지
						if(!jobExecutorContext.getIsJdbcTemplateExist()) {
							jobExecutionOption.put("jdbcTemplate", "org.springframework.jdbc.core.JdbcTemplate");
						}
						// lobHandler bean 중복 생성 가능하도록 수정
						jobExecutionOption.put("lobHandler", "org.springframework.jdbc.support.lob.DefaultLobHandler");
					} else {
						jobExecutionDefault.put("jobRepository", "jobRepository<org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean");
						jobExecutionDefault.put("jobExplorer", jobExecutorContext.getExplorerId()+"<"+"org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean");

						// transactionManager bean 중복 생성 가능하도록 수정
						jobExecutionOption.put("transactionManager", "org.springframework.batch.support.transaction.ResourcelessTransactionManager");
				
					}
				} else {
					// 기존 Repository Type을 사용하는 경우 jobRepositoryType이 null
					if(! jobExecutorContext.getJobRepositoryClass().contains("Map") ) {
						// DB type
						jobExecutionDefault.put("jobExplorer", jobExecutorContext.getExplorerId()+"<"+"org.springframework.batch.core.explore.support.JobExplorerFactoryBean");
					} else {
						// Memory type
						jobExecutionDefault.put("jobExplorer", jobExecutorContext.getExplorerId()+"<"+"org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean");
					}
				}
				jobExecutionDefault.put("jobOperator", jobExecutorContext.getOperatorId());
				jobExecutionDefault.put("jobRegistry", jobExecutorContext.getRegisterId());
			}
		}
	}
	
	/**
	 * Job XML 파일 구성
	 * 
	 * @param newXMLFile
	 * 
	 * */
	public void createJobExecutionXMLFile(IFile newXMLFile) {

		Element beansElement = appendBeansElement();
		
		Document doc = new Document(beansElement);
		
		appendDefaultBeans(beansElement);
		appendOtherBeans(beansElement);

		HandlingFileOperation.transformFiletoXMLFile(doc);
		
	}
	
	/**
	 * XML에 Beans Element생성
	 * @param doc 
	 * */
	private Element appendBeansElement(){
		
		// root node(beans)
		Element beansElement = new Element("beans");
		
		// XML Namespace: beans, aop, tx, p, context
		beansElement.setNamespace(Namespace.getNamespace("http://www.springframework.org/schema/beans"));
		beansElement.addNamespaceDeclaration(Namespace.getNamespace("aop", "http://www.springframework.org/schema/aop"));
		beansElement.addNamespaceDeclaration(Namespace.getNamespace("tx", "http://www.springframework.org/schema/tx"));
		beansElement.addNamespaceDeclaration(Namespace.getNamespace("p", "http://www.springframework.org/schema/p"));
		beansElement.addNamespaceDeclaration(Namespace.getNamespace("context", "http://www.springframework.org/schema/context"));
		
		// XML Namespace SchemaLocations
		Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		String xsiSchemaLocation = "http://www.springframework.org/schema/beans \n" +
		"\t http://www.springframework.org/schema/beans/spring-beans.xsd \n" +
		"\t http://www.springframework.org/schema/aop \n" +
		"\t http://www.springframework.org/schema/aop/spring-aop.xsd \n" +
		"\t http://www.springframework.org/schema/tx \n" +
		"\t http://www.springframework.org/schema/tx/spring-tx.xsd \n" + 
		"\t http://www.springframework.org/schema/context \n" +
		"\t http://www.springframework.org/schema/context/spring-context.xsd";
		beansElement.addNamespaceDeclaration(xsi);
		beansElement.setAttribute("schemaLocation", xsiSchemaLocation, xsi);
		
		return beansElement;
	}
	
	/**
	 * XML에 Default Beans Element생성
	 * @param beansElement 
	 * @param doc 
	 * @param doc 
	 * */
	private void appendDefaultBeans(Element beansElement){

		Set<String> jobExecutionDefaultKey= jobExecutionDefault.keySet();
		Object[] keyArray = jobExecutionDefaultKey.toArray();
		
		Namespace propertyNameSpace = Namespace.getNamespace("p", "http://www.springframework.org/schema/p");
		Element eGovBatchRunnerBean = new Element("bean");
		beansElement.addContent(eGovBatchRunnerBean);
		
		// unique egovBatchRunner id 생성
		eGovBatchRunnerBean.setAttribute(new Attribute("id", jobExecutorContext.getFileName().split("\\.")[0] + ".egovBatchRunner"));
		// 표준프레임워크 v4.0 패키지명 적용
		eGovBatchRunnerBean.setAttribute(new Attribute("class", "org.egovframe.rte.bat.core.launch.support.EgovBatchRunner"));
		
		// egovBatchRunner에 JobRepository constructor-arg 추가
		Element constructorBeanJobRepository = new Element("constructor-arg");
		eGovBatchRunnerBean.addContent(constructorBeanJobRepository);
		constructorBeanJobRepository.setAttribute("ref", "jobRepository");
		
		// JobRepository를 제외한 JobOperator, JobExplorer를 constructor-arg로 추가
		for(int i = 0; i < jobExecutionDefault.size(); i++){

			String key = (String)keyArray[i];
			if(key.equals("jobOperator") || key.equals("jobExplorer")){
				Element constructorBean = new Element("constructor-arg");
				eGovBatchRunnerBean.addContent(constructorBean);
				
				Attribute attrRef = null;
				if(!key.equals("jobOperator")){
					attrRef = new Attribute("ref", jobExecutionDefault.get(key).split("<")[0]);
				}else{
					attrRef = new Attribute("ref", jobExecutionDefault.get(key));
				}

				constructorBean.setAttribute(attrRef);
			}
		}
		
		for(int i = 0; i < jobExecutionDefault.size(); i++){

			String key = (String)keyArray[i];
			
			if(key.equals("jobLauncher")){
				Element jobLauncherBean = new Element("bean");
				jobLauncherBean.removeAttribute("xmlns");
				beansElement.addContent(jobLauncherBean);
				
				jobLauncherBean.setAttribute("id", jobExecutionDefault.get(key));
				jobLauncherBean.setAttribute("class", "org.springframework.batch.core.launch.support.SimpleJobLauncher");
				
				Element propertyBean = new Element("property");
				jobLauncherBean.addContent(propertyBean);
				
				// JobRepository ID: "jobRepository"
				propertyBean.setAttribute("name", "jobRepository");
				propertyBean.setAttribute("ref", "jobRepository");
				
				// asyncTaskExecutor
				if(!jobExecutorContext.isSyncMode()){
					
					Element asyncPropertyBean = new Element("property");
					jobLauncherBean.addContent(asyncPropertyBean);
					
					asyncPropertyBean.setAttribute("name", "taskExecutor");
					
					Element asyncClassBean = new Element("bean");
					asyncPropertyBean.addContent(asyncClassBean);
					
					asyncClassBean.setAttribute("class", "org.springframework.core.task.SimpleAsyncTaskExecutor");
				}
			} 
			else if(key.equals("jobRepository")){
				
				String[] value = jobExecutionDefault.get(key).split("<");
				
				Element jobRepositoryBean = new Element("bean");
				beansElement.addContent(jobRepositoryBean);
				
				jobRepositoryBean.setAttribute("id", value[0]);
				jobRepositoryBean.setAttribute("class", value[1]);
				
				if(!value[1].contains("Map")){
					
					// JobRepository Type: DB(Reference / New)
					jobRepositoryBean.setAttribute("dataSource-ref", jobExecutorContext.getDatasourceBeanID(), propertyNameSpace);					
					jobRepositoryBean.setAttribute("transactionManager-ref", "transactionManager", propertyNameSpace);					
					jobRepositoryBean.setAttribute("lobHandler-ref", jobLauncherId+".lobHandler", propertyNameSpace);		
					
				} else {
					
					// JobRepository Type: Memory
					jobRepositoryBean.setAttribute("transactionManager-ref", "transactionManager", propertyNameSpace);					
					
				}
				
			} else if(key.equals("jobOperator")){
				
				Element jobOperatorBean = new Element("bean");
				beansElement.addContent(jobOperatorBean);
				
				jobOperatorBean.setAttribute("id", jobExecutionDefault.get(key));
				jobOperatorBean.setAttribute("class", "org.springframework.batch.core.launch.support.SimpleJobOperator");
				
				jobOperatorBean.setAttribute("jobLauncher-ref", jobExecutorContext.getJobLauncerId(), propertyNameSpace);					
				jobOperatorBean.setAttribute("jobExplorer-ref", jobExecutorContext.getExplorerId(), propertyNameSpace);					
				jobOperatorBean.setAttribute("jobRepository-ref", "jobRepository", propertyNameSpace);					
				jobOperatorBean.setAttribute("jobRegistry-ref", jobExecutorContext.getRegisterId(), propertyNameSpace);					
				
			} else if(key.equals("jobExplorer")){
				
				String[] value = jobExecutionDefault.get(key).split("<");
				
				Element jobExplorerBean = new Element("bean");
				beansElement.addContent(jobExplorerBean);
				
				jobExplorerBean.setAttribute("id", value[0]);
				jobExplorerBean.setAttribute("class", value[1]);
								
				if(value[1].contains("Map")){				
					jobExplorerBean.setAttribute("repositoryFactory-ref", "&amp;jobRepository", propertyNameSpace);					
				} else {	
					
					//jobRepository가 있을 때, jobRepository의 datasource-ref 값을 세팅
					if(jobExecutorContext.getDatasourceBeanID() != null) {
						jobExplorerBean.setAttribute("dataSource-ref", jobExecutorContext.getDatasourceBeanID() , propertyNameSpace);
					} else {
						jobExplorerBean.setAttribute("dataSource-ref", jobExecutorContext.getDataSourceRef() , propertyNameSpace);
					}
					
					if(jobExecutorContext.getDatasourceBeanID() != null) {
						//// dataSourceBean
						if(jobExecutorContext.getRepositoryType().equals("DB(New)")){
							
							// JobRepository Type: DB(New)
							Element dataSourceBean = new Element("bean");
							beansElement.addContent(dataSourceBean);
	
							dataSourceBean.setAttribute("id", jobExecutorContext.getDatasourceBeanID());
							dataSourceBean.setAttribute("class", "org.apache.commons.dbcp.BasicDataSource");
	
							Set<String> databaseInfoKey= databaseInfo.keySet();
							Object[] databaseInfokeyArray = databaseInfoKey.toArray();
							for(int j = 0; j < databaseInfo.size(); j++){
	
								String databaseKey = (String)databaseInfokeyArray[j];
	
								String[] databaseValue = databaseInfo.get(databaseKey).split("<");
	
								if(databaseValue[0].contains("password") && databaseValue.length < 2){
								} else {
									Element dataSourceProperty = new Element("property");
									dataSourceBean.addContent(dataSourceProperty);
	
									dataSourceProperty.setAttribute("name", databaseValue[0]);
									dataSourceProperty.setAttribute("value", databaseValue[1]);
								}
							}
						}
					}
				}
			} else if(key.equals("jobRegistry")){
				
				Element jobRegistryBean = new Element("bean");
				beansElement.addContent(jobRegistryBean);
				
				jobRegistryBean.setAttribute("id", jobExecutionDefault.get(key));
				jobRegistryBean.setAttribute("class", "org.springframework.batch.core.configuration.support.MapJobRegistry");
				
				Element jobRegistryPostProcessorBean = new Element("bean");
				beansElement.addContent(jobRegistryPostProcessorBean);
				
				jobRegistryPostProcessorBean.setAttribute("class", "org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor");

				Element jobRegistryPostProcessorProperty = new Element("property");
				jobRegistryPostProcessorBean.addContent(jobRegistryPostProcessorProperty);
				
				jobRegistryPostProcessorProperty.setAttribute("name", "jobRegistry");
				jobRegistryPostProcessorProperty.setAttribute("ref", jobExecutionDefault.get(key));

			} 
		}
	}
	
	/**
	 * XML에 Option, Datasource, Transaction Beans Element생성
	 * @param beansElement 
	 * @param doc 
	 * @param doc 
	 * */
	private void appendOtherBeans(Element beansElement){

		Set<String> jobExecutionOptionKey= jobExecutionOption.keySet();
		Object[] keyArray = jobExecutionOptionKey.toArray();

		for(int i = 0; i < jobExecutionOption.size(); i++){

			String key = (String)keyArray[i];
			Element jobLauncherOtherBean = new Element("bean");
			beansElement.addContent(jobLauncherOtherBean);
			
			if(key.contains("transactionManager")){
				
				jobLauncherOtherBean.setAttribute("id", key);
				jobLauncherOtherBean.setAttribute("class", jobExecutionOption.get(key));

				if(!jobExecutorContext.getRepositoryType().equals("Memory")){

					jobLauncherOtherBean.setAttribute("lazy-init", "true");

					Element datasourceProperty = new Element("property");
					jobLauncherOtherBean.addContent(datasourceProperty);

					datasourceProperty.setAttribute("name", "dataSource");
					datasourceProperty.setAttribute("ref", jobExecutorContext.getDatasourceBeanID());
				}
				
				
			} else if (key.contains("lobHandler")){
				
				jobLauncherOtherBean.setAttribute("id", jobLauncherId+"."+key);

				if(jobExecutorContext.getDriverClassName() != null && jobExecutorContext.getDriverClassName().contains("oracle")){
					jobLauncherOtherBean.setAttribute("class", "org.springframework.jdbc.support.lob.OracleLobHandler");
				} else {
					jobLauncherOtherBean.setAttribute("class", jobExecutionOption.get(key));
				}
								
			} else if (key.contains("jdbcTemplate") ){
				
				jobLauncherOtherBean.setAttribute("id", key);
				jobLauncherOtherBean.setAttribute("class", jobExecutionOption.get(key));

				Element datasourceProperty = new Element("property");
				jobLauncherOtherBean.addContent(datasourceProperty);

				datasourceProperty.setAttribute("name", "dataSource");
				datasourceProperty.setAttribute("ref", jobExecutorContext.getDatasourceBeanID());
				
			} else {
				
				jobLauncherOtherBean.setAttribute("id", key);
				jobLauncherOtherBean.setAttribute("class", jobExecutionOption.get(key));
				
			}
		}

	}

}