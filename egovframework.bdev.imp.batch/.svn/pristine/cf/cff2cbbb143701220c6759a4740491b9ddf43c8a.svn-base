package egovframework.bdev.imp.batch.wizards.jobcreation.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import egovframework.bdev.imp.batch.wizards.com.BatchJobContext;
import egovframework.bdev.imp.batch.wizards.com.HandlingFileOperation;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchJobCreationContext;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.DecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobRWDetailInfoItem;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
import egovframework.dev.imp.core.utils.NullUtil;

/**
 * Job XML 파일을 생성 하는 클래스
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

public class CreateBatchJobXMLFileOperation {

	
	/** 마지막 bean에 추가할 Reader, Writer, Listener, partitioner 등의 정보 저장*/
	private HashMap<String, String> beanMap = new HashMap<String, String>();
	
	/** 마지막 bean에 추가할 정보들을 sorting한 값 저장*/
	private HashMap<String, String> orderedMap = new HashMap<String, String>();
	
	/** jobVOList 인스턴스*/
	private JobVo[] jobVOList = null;
	
	/** stepAndDecisionVoList 인스턴스*/
	private StepAndDecisionVo[] stepAndDecisionVoList = null;
	
	/** stepVOList 정보*/
	private ArrayList<StepVo> stepVOList = new ArrayList<StepVo>();
	
	/** decisionVOList 정보*/
	private ArrayList<DecisionVo> decisionVOList = new ArrayList<DecisionVo>();
	
	/** partitionMode의 Step 정보*/
	private ArrayList<String> partitionModeStep = new ArrayList<String>(); 
	
	/** decisionVOList중 xml파일에 추가한 decision의 수*/
	private int decisionDone = 0;
	
	/** project name **/
	private String projectName = null;
	
	/**
	 * context 정보들 가져와서 셋팅
	 * 
	 * @param context
	 * 
	 * */
	@SuppressWarnings("restriction")
	public void setContext(BatchJobContext context){
		if(context != null){
			BatchJobCreationContext jobContext = new BatchJobCreationContext();
			DecisionVo tempDecisionVo = null;
			StepVo tempStepVo = null;
			
			//job List
			if(context instanceof BatchJobCreationContext){
				jobContext = (BatchJobCreationContext)context;
				jobVOList = jobContext.getJobVoList();
			}
			
			if(jobVOList.length > 0){
				for(int i = 0 ; i < jobVOList.length; i++){
					if(jobVOList[i].getStepAndDecisionVoList().length > 0){
						for(int j = 0; j < jobVOList[i].getStepAndDecisionVoList().length; j++){
							stepAndDecisionVoList = jobVOList[i].getStepAndDecisionVoList();
							
							//decision List
							if(stepAndDecisionVoList[j] instanceof DecisionVo){
								tempDecisionVo = (DecisionVo)stepAndDecisionVoList[j];
								decisionVOList.add(tempDecisionVo);
							}
							//step List
							if(stepAndDecisionVoList[j] instanceof StepVo){
								tempStepVo = (StepVo)stepAndDecisionVoList[j];
								stepVOList.add(tempStepVo);
							}
						}
					}
				}
			}
			
			for(int i = 0; i < stepVOList.size(); i++){
				if(stepVOList.get(i).isPartitionMode()){
					partitionModeStep.add(stepVOList.get(i).getPartitionerType());
				}
			}
			
			//selected project name
			projectName = jobContext.getProject().getName();
		}
	}

	/**
	 * Job XML 파일 구성
	 * @param context 
	 * 
	 * 
	 * */
	public void createJobXMLFile(BatchJobCreationContext context) {

		HandlingFileOperation.createFile(context);
		
		Element beansElement = appendBeansElement();
		
		Document doc = new Document(beansElement);
		
		appendJobElement(beansElement);
		appendBeanElement(beansElement);
		
		HandlingFileOperation.transformFiletoXMLFile(doc);
	}
	
	/**
	 * XML에 Beans Element생성
	 * @param doc 
	 * */
	private Element appendBeansElement(){
		
		// root node(beans)
		Element beansElement = new Element("beans");

		beansElement.setNamespace(Namespace.getNamespace("http://www.springframework.org/schema/beans"));
		beansElement.addNamespaceDeclaration(Namespace.getNamespace("util", "http://www.springframework.org/schema/util"));
		
		// XML Namespace SchemaLocations
		Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		String xsiSchemaLocation = "http://www.springframework.org/schema/batch \n" +
				"\t http://www.springframework.org/schema/batch/spring-batch.xsd \n" +
				"\t http://www.springframework.org/schema/beans \n" +
				"\t http://www.springframework.org/schema/beans/spring-beans.xsd \n" +
				"\t http://www.springframework.org/schema/util \n" +
				"\t http://www.springframework.org/schema/util/spring-util.xsd";
		beansElement.addNamespaceDeclaration(xsi);
		beansElement.setAttribute("schemaLocation", xsiSchemaLocation, xsi);
		
		return beansElement;
	}
	
	/**
	 * XML에 Job Element생성
	 * @param beansElement 
	 * @param doc 
	 * @param context 
	 * 
	 **/

	private void appendJobElement(Element beansElement){
		Element job = null;
		
		if (jobVOList.length > 0) {
			for (int i = 0; i < jobVOList.length; i++) {
				String jobName = null;
				jobName = jobVOList[i].getJobName();
				
				// job
				job = new Element("job");
				beansElement.addContent(job);
				
				job.setAttribute("id", jobVOList[i].getJobName());
				
				if (jobVOList[i].isRestartable()) {
					job.setAttribute("restartable", "true");
				} else {
					job.setAttribute("restartable", "false");
				}
				// job XML namespace
				job.setNamespace(Namespace.getNamespace("http://www.springframework.org/schema/batch"));
				
				for(int j =0; j < stepVOList.size(); j++){
					if(jobName.equals(stepVOList.get(j).getJobName())){
						if(!stepVOList.get(j).isPartitionMode() ){
							appendStepTaskletElement(beansElement, job, jobVOList[i], stepVOList.get(j), jobName);
						} else {
							appendStepPartitionElement(beansElement, job, jobVOList[i], stepVOList.get(j));
						}
					}
				}
				
				appendDecisionElement(beansElement, job, jobName);
				updateNextToAttribute(job);
				findStartingStep(job);
				
				// job
				if (jobVOList[i].getJobListenerInfoList() != null && jobVOList[i].getJobListenerInfoList().length > 0) {
					// job listeners
					Element listeners = new Element("listeners");
					job.addContent(listeners);
					
					Element listener = null;
					for (int j = 0; j < jobVOList[i].getJobListenerInfoList().length; j++) {
						listener = new Element("listener");
						listeners.addContent(listener);
						listener.setAttribute("ref", jobVOList[i].getJobListenerInfoList()[j].getName());
					}
					
					// job listener beans
					if(jobVOList[i].getJobListenerInfoList().length > 0){
						 for(int j = 0; j < jobVOList[i].getJobListenerInfoList().length; j++){
							 if(jobVOList[i].getJobListenerInfoList()[j] != null){
								 String tempName = jobVOList[i].getJobListenerInfoList()[j].getName().toString();
								 String tempClass = jobVOList[i].getJobListenerInfoList()[j].getClassValue().toString();
								 beanMap.put("jobListener" + tempName, tempName + "<" + tempClass);
							 }
						 }
					}
				} //jobVOList[i]getJobListenerInfoList() if
			} //jobVOList.length for
		} //jobVOList if
	}
	
	/**
	 * XML에 Step(Tasklet) Element생성
	 * @param beansElement 
	 * @param doc 
	 * @param jobVO 
	 * @param stepVo 
	 * @param jobName 
	 * 
	 * */
	private void appendStepTaskletElement(Element beansElement, Element job, JobVo jobVO, StepVo stepVo, String jobName) {
		Element step =null;

		if(!stepVo.isPartitionMode()) {
			// step
			step = new Element("step");
			job.addContent(step);
			
			step.setAttribute("id", stepVo.getName());
			
			// step's 'next' attribute
			if (stepVo.getNextStep() != null && stepVo.getNextStep().length() > 0) {
				int size = job.getChildren("decision").size();
				
				for(int i = 0; i < size; i++) {
//					System.out.println(job.getChildren("decision").get(i).getAttribute("id"));
				}
				step.setAttribute("next", stepVo.getNextStep());
			}
			// tasklet
			Element tasklet = new Element("tasklet");
			step.addContent(tasklet);
			
			// chunk
			Element chunk = new Element("chunk");
			tasklet.addContent(chunk);
			
			String jobReaderName = stepVo.getJobReaderFullName();
			String jobReaderResourceType = stepVo.getJobReaderInfo().getResourceType();
			String jobReaderItemType = stepVo.getJobReaderInfo().getItemType();
			String jobReaderClass = stepVo.getJobReaderInfo().getClassValue();
			
			String sqlKeyValue = "";
			//choise temp
			if(!NullUtil.isNull(stepVo.getJobReadersqlKeyValueVo())){
				for(int i = 0; i < stepVo.getJobReadersqlKeyValueVo().size(); i++) {
					if(i != 0) {
						sqlKeyValue += "|";
					}
					sqlKeyValue += stepVo.getJobReadersqlKeyValueVo().get(i).key;
					sqlKeyValue += "=";
					sqlKeyValue += stepVo.getJobReadersqlKeyValueVo().get(i).value;
				}
//				System.out.println(sqlKeyValue);
			}
			
			beanMap.put("jobReader"+jobReaderName, jobReaderName+"<"+jobReaderClass+"<"+jobReaderResourceType+"<"+jobReaderItemType+"<normal"
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.RESOURCE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_NAME)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.COLUMN_LENGTH)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DELIMITER)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.VO_CLASS)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_FORMAT)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_RANGE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.IBATIS_STATEMENT)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.CONFIGURATION_FILE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.ROW_MAPPER)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.PAGE_SIZE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_SORT_COLUMN)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_SELECT)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_FROM)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_WHERE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_UPDATE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_INSERT)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.PARAMS)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.ROW_SETTER)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DATABASE_TYPE)
					+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DATASOURCE_BEAN_ID)
					+"<"+projectName
					+"<"+sqlKeyValue
			);
			
			chunk.setAttribute("reader", jobReaderName);

			String jobWriterName = stepVo.getJobWriterFullName();
			String jobWriterResourceType = stepVo.getJobWriterInfo().getResourceType();
			String jobWriterItemType = stepVo.getJobWriterInfo().getItemType();
			String jobWriterClass = stepVo.getJobWriterInfo().getClassValue();
			beanMap.put("jobWriter"+jobWriterName, jobWriterName+"<"+jobWriterClass+"<"+jobWriterResourceType+"<"+jobWriterItemType+"<normal"
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.RESOURCE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_NAME)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.COLUMN_LENGTH)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DELIMITER)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.VO_CLASS)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_FORMAT)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_RANGE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.IBATIS_STATEMENT)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.CONFIGURATION_FILE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.ROW_MAPPER)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.PAGE_SIZE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_SORT_COLUMN)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_SELECT)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_FROM)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_WHERE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_UPDATE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_INSERT)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.PARAMS)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.ROW_SETTER)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DATABASE_TYPE)
					+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DATASOURCE_BEAN_ID)
					+"<"+projectName
			);
			
			chunk.setAttribute("writer", jobWriterName);
			chunk.setAttribute("commit-interval", stepVo.getCommitInterval().toString());
			
			// chunk listener
			if (stepVo.getChunkListenerInfoList() != null && stepVo.getChunkListenerInfoList().length > 0) {
				Element listeners = new Element("listeners");
				chunk.addContent(listeners);
				
				Element chunkListener = null;
				for (int m = 0; m < stepVo.getChunkListenerInfoList().length; m++) {
					chunkListener = new Element("listener");
					listeners.addContent(chunkListener);
					
					chunkListener.setAttribute("ref", stepVo.getChunkListenerInfoList()[m].getName());
					
					// chunk listener beans
					String tempId = stepVo.getChunkListenerInfoList()[m].getName();
					String tempClass = stepVo.getChunkListenerInfoList()[m].getClassValue();
					beanMap.put("chunkListener" + tempId, tempId + "<" + tempClass);
					
				}
			} 
			
			Element stepListeners = null;
			// step listener
			if (stepVo.getStepListenerInfoList() != null && stepVo.getStepListenerInfoList().length > 0) {
				stepListeners = new Element("listeners");
				step.addContent(stepListeners);
				
				Element stepListener = null;
				for (int m = 0; m < stepVo.getStepListenerInfoList().length; m++) {
					stepListener = new Element("listener");
					stepListeners.addContent(stepListener);

					stepListener.setAttribute("ref", stepVo.getStepListenerInfoList()[m].getName());

					// step listener beans
					String tempId = stepVo.getStepListenerInfoList()[m].getName();
					String tempClass = stepVo.getStepListenerInfoList()[m].getClassValue();
					beanMap.put("stepListener" + tempId, tempId + "<" + tempClass);
				}
			}
			
			if(jobVO.getJobName().equals(stepVo.getJobName())){
				
				if(jobVO.getSharedValues() != null && jobVO.getSharedValues().length > 0) {
					Element sharedValueListener = null;
					Element sharedValuesListBean = null;
					
					for(int i = 0; i < jobVO.getSharedValues().length; i++){
						if(stepVo.getName().equals(jobVO.getSharedValues()[i].getStepId())){
							

							if(stepListeners == null){
								stepListeners = new Element("listeners");
								step.addContent(stepListeners);
							}
							sharedValueListener = new Element("listener");
							stepListeners.addContent(sharedValueListener);

							Element sharedValuesBean = new Element("bean");
							sharedValueListener.addContent(sharedValuesBean);
							
							sharedValuesBean.setAttribute("class", "org.springframework.batch.core.listener.ExecutionContextPromotionListener");
							
							// sharedValue bean XML namespace
							sharedValuesBean.setNamespace(Namespace.getNamespace("http://www.springframework.org/schema/beans"));
							
							Element sharedValuesPropertyBean = new Element("property");
							sharedValuesBean.addContent(sharedValuesPropertyBean);
							
							sharedValuesPropertyBean.setAttribute("name", "keys");
							
							sharedValuesListBean = new Element("list");
							sharedValuesPropertyBean.addContent(sharedValuesListBean);
							
							break;
						}
					}
					
					for(int i = 0; i < jobVO.getSharedValues().length; i++){
						if(stepVo.getName().equals(jobVO.getSharedValues()[i].getStepId())){
							if(sharedValuesListBean != null){
								Element sharedValuesValueBean = new Element("value");
								sharedValuesListBean.addContent(sharedValuesValueBean);
								sharedValuesValueBean.setText(jobVO.getSharedValues()[i].getKey());
							}
						}
					} 
				}
			}
		} 
	}
	
	/**
	 * XML에 Step(Partition) Element생성
	 * @param beansElement 
	 * @param stepVo 
	 * @param step 
	 * 
	 * */
	private void appendStepPartitionElement(Element beansElement, Element job, JobVo jobVo, StepVo stepVo){
		
		String prefix = jobVo.getJobName()+ "." + stepVo.getName();
		
		Element step = new Element("step");
		job.addContent(step);

		step.setAttribute("id", stepVo.getName());
		
		Element partition = new Element("partition");
		step.addContent(partition);
		String partitionName = prefix + "." + "partitioner";

		partition.setAttribute("step", stepVo.getSubStepID());
		String partitionerType = stepVo.getPartitionerType();
		partition.setAttribute("partitioner", partitionName);
		
		String partitionerClass = "org.springframework.batch.core.partition.support.MultiResourcePartitioner";
		
		String resource = stepVo.getResource();
		// only file descripter(file:)
		if(resource.contains(projectName)) {
			resource = resource.substring(resource.indexOf(projectName) + projectName.length());
			if(resource.indexOf("/") == 0) {
				resource = resource.substring(1);
			}
			resource = "file:./" + resource;
		} else {
			resource = "file:" + resource;			
		}
		
		
		
		
		// create multiple partitioner beans
		beanMap.put("partitioner" + partitionerType + prefix, partitionName + "<" + partitionerClass + "<" + resource);
		beanMap.put("fileNameListener" + prefix , prefix + ".fileNameListener" + "<" + "egovframework.brte.core.listener.EgovOutputFileListener" + "<" + resource);

		Element handler = new Element("handler");
		partition.addContent(handler);

		handler.setAttribute("grid-size", stepVo.getGridSize().toString());

		appendStepOfPartitionElement(beansElement, jobVo, stepVo);

	}
	
	/**
	 * XML에 Decision Element생성
	 * @param job 
	 * @param beansElement 
	 * @param doc 
	 * @param jobName 
	 * 
	 * */
	private void appendDecisionElement(Element beansElement, Element job, String jobName){
		if(decisionVOList != null && decisionVOList.size() > 0) {
			int j = 0;
			for (j = 0; j < decisionVOList.size(); j++) {
				j =+ decisionDone;
				if(decisionVOList.size() > j){
					if(jobName.equals(decisionVOList.get(j).getJobName())){
						// decision
						Element decision = new Element("decision");
						job.addContent(decision);

						decision.setAttribute("id", decisionVOList.get(j).getName());						
						decision.setAttribute("decider", jobName + "." + decisionVOList.get(j).getName() + "." +"egovDecider");
						// each decision needs a own decider
						beanMap.put("decider"+j, jobName + "." + decisionVOList.get(j).getName() + "." +"egovDecider" + "<" + "egovframework.brte.core.job.flow.EgovDecider");
						
						// decision next on, to Optional
						if (decisionVOList.get(j).getNextVo() != null && decisionVOList.get(j).getNextVo().length > 0) {
							for(int i = 0; i < decisionVOList.get(j).getNextVo().length; i++){
								Element decisionNext = new Element("next");
								decision.addContent(decisionNext);
								decisionNext.setAttribute("on", decisionVOList.get(j).getNextVo()[i].getNextOn());
								decisionNext.setAttribute("to", decisionVOList.get(j).getNextVo()[i].getNextTo());
							}
						}

						// decision end on Optional
						if (decisionVOList.get(j).getEndOn() != null && decisionVOList.get(j).getEndOn().length() > 0) {
							Element decisionEnd = new Element("end");
							decision.addContent(decisionEnd);

							decisionEnd.setAttribute("on", decisionVOList.get(j).getEndOn());
							
							if (decisionVOList.get(j).getEndExitCode() != null && decisionVOList.get(j).getEndExitCode().length() > 0) {
								decisionEnd.setAttribute("exit-code", decisionVOList.get(j).getEndExitCode());
							}
						}

						// decision fail on Optional
						if (decisionVOList.get(j).getFailOn() != null && decisionVOList.get(j).getFailOn().length() > 0) {
							Element decisionFail = new Element("fail");
							decision.addContent(decisionFail);

							decisionFail.setAttribute("on", decisionVOList.get(j).getFailOn());

							if (decisionVOList.get(j).getFailExitCode() != null && decisionVOList.get(j).getFailExitCode().length() > 0) {
								decisionFail.setAttribute("exit-code", decisionVOList.get(j).getFailExitCode());
							}
						}
						
						// decision stop on Optional
						if (decisionVOList.get(j).getStopOn() != null && decisionVOList.get(j).getStopOn().length() > 0) {
							Element decisionStop = new Element("stop");
							decision.addContent(decisionStop);

							decisionStop.setAttribute("on", decisionVOList.get(j).getStopOn());

							if (decisionVOList.get(j).getStopRestart() != null) {
								decisionStop.setAttribute("restart", decisionVOList.get(j).getStopRestart());
							}
							
						}
						
						decisionDone++;
					}
				}
			}//for-end
		}
	}
	
	/**
	 * XML에 partition의 step 구성
	 * @param beansElement 
	 * @param doc 
	 * @param jobVo 
	 * @param stepVo 
	 * 
	 * */
	private void appendStepOfPartitionElement(Element beansElement, JobVo jobVo, StepVo stepVo){
		
		// fileNameListener's prefix
		String prefix = jobVo.getJobName()+ "." + stepVo.getName();
		
		// step
		Element partitionerStep = new Element("step");
		beansElement.addContent(partitionerStep);

		partitionerStep.setAttribute("id", stepVo.getSubStepID());
		
		partitionerStep.setNamespace(Namespace.getNamespace("http://www.springframework.org/schema/batch"));
		
		// tasklet
		Element tasklet = new Element("tasklet");
		partitionerStep.addContent(tasklet);
		
		// chunk
		Element chunk = new Element("chunk");
		tasklet.addContent(chunk);

		String jobReaderName = stepVo.getJobReaderFullName();
		String jobReaderResourceType = stepVo.getJobReaderInfo().getResourceType();
		String jobReaderItemType = stepVo.getJobReaderInfo().getItemType();
		chunk.setAttribute("reader", jobReaderName);
		beanMap.put("jobReader"+jobReaderName, jobReaderName + "<" 
				+ stepVo.getJobReaderInfo().getClassValue() + "<" 
				+ jobReaderResourceType + "<" + jobReaderItemType + "<partition"
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.RESOURCE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_NAME)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.COLUMN_LENGTH)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DELIMITER)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.VO_CLASS)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_FORMAT)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.FIELD_RANGE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.IBATIS_STATEMENT)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.CONFIGURATION_FILE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.ROW_MAPPER)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.PAGE_SIZE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_SORT_COLUMN)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_SELECT)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_FROM)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_WHERE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_UPDATE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.SQL_INSERT)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.PARAMS)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.ROW_SETTER)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DATABASE_TYPE)
				+"<"+stepVo.getJobReaderContext().get(JobRWDetailInfoItem.DATASOURCE_BEAN_ID)
				+"<"+projectName
		);
		
		String jobWriterName = stepVo.getJobWriterFullName();
		String jobWriterResourceType = stepVo.getJobWriterInfo().getResourceType();
		String jobWriterItemType = stepVo.getJobWriterInfo().getItemType();
		chunk.setAttribute("writer", jobWriterName);
		beanMap.put("jobWriter"+jobWriterName, jobWriterName + "<" + stepVo.getJobWriterInfo().getClassValue() + "<" + jobWriterResourceType + "<" + jobWriterItemType + "<partition"
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.RESOURCE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_NAME)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.COLUMN_LENGTH)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DELIMITER)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.VO_CLASS)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_FORMAT)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.FIELD_RANGE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.IBATIS_STATEMENT)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.CONFIGURATION_FILE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.ROW_MAPPER)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.PAGE_SIZE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_SORT_COLUMN)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_SELECT)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_FROM)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_WHERE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_UPDATE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.SQL_INSERT)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.PARAMS)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.ROW_SETTER)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DATABASE_TYPE)
				+"<"+stepVo.getJobWriterContext().get(JobRWDetailInfoItem.DATASOURCE_BEAN_ID)
				+"<"+projectName
			);
		
		chunk.setAttribute("commit-interval", stepVo.getCommitInterval().toString());
		
		// fileNameListener for partitioner step
		Element listeners = new Element("listeners");
		chunk.addContent(listeners);
		
		Element fileListener = new Element("listener");
		listeners.addContent(fileListener);
		fileListener.setAttribute("ref", prefix + ".fileNameListener");
		
		// chunk listener 
		if (stepVo.getChunkListenerInfoList() != null && stepVo.getChunkListenerInfoList().length > 0) {
			Element chunkListener = null;
			for (int m = 0; m < stepVo.getChunkListenerInfoList().length; m++) {
				chunkListener = new Element("listener");
				listeners.addContent(chunkListener);

				chunkListener.setAttribute("ref", stepVo.getChunkListenerInfoList()[m].getName());
				
				// chunk listener beans
				String tempId = stepVo.getChunkListenerInfoList()[m].getName();
				String tempClass = stepVo.getChunkListenerInfoList()[m].getClassValue();
				beanMap.put("chunkListener" + tempId, tempId + "<" + tempClass);

			}
		} 
	
		Element stepListeners  = null;
		
		// step listener
		if (stepVo.getStepListenerInfoList() != null && stepVo.getStepListenerInfoList().length > 0) {
			stepListeners = new Element("listeners");
			partitionerStep.addContent(stepListeners);
			
			Element stepListener = null;
			for (int m = 0; m < stepVo.getStepListenerInfoList().length; m++) {
				stepListener = new Element("listener");
				stepListeners.addContent(stepListener);

				stepListener.setAttribute("ref", stepVo.getStepListenerInfoList()[m].getName());

				// step listener beans
				String tempId = stepVo.getStepListenerInfoList()[m].getName();
				String tempClass = stepVo.getStepListenerInfoList()[m].getClassValue();
				beanMap.put("stepListener" + tempId, tempId + "<" + tempClass);
			}
		}

		if(jobVo.getJobName().equals(stepVo.getJobName())){
			if(jobVo.getSharedValues().length > 0) {
				Element sharedValueListener = null;
				Element sharedValuesListBean = null;
				
				for(int i = 0; i < jobVo.getSharedValues().length; i++){
					if(stepVo.getName().equals(jobVo.getSharedValues()[i].getStepId())){
						
						if(stepListeners == null){
							stepListeners = new Element("listeners");
							partitionerStep.addContent(stepListeners);
						}
						sharedValueListener = new Element("listener");
						stepListeners.addContent(sharedValueListener);

						Element sharedValuesBean = new Element("bean", Namespace.getNamespace("http://www.springframework.org/schema/beans"));
						sharedValueListener.addContent(sharedValuesBean);
						
						sharedValuesBean.setAttribute("class", "org.springframework.batch.core.listener.ExecutionContextPromotionListener");
					
						Element sharedValuesPropertyBean = new Element("property");
						sharedValuesBean.addContent(sharedValuesPropertyBean);
						
						sharedValuesPropertyBean.setAttribute("name", "keys");
						
						sharedValuesListBean = new Element("list");
						sharedValuesPropertyBean.addContent(sharedValuesListBean);
						
						break;
					}
				}
				
				for(int i = 0; i < jobVo.getSharedValues().length; i++){
					if(stepVo.getName().equals(jobVo.getSharedValues()[i].getStepId())){
						if(sharedValuesListBean != null){
							Element sharedValuesValueBean = new Element("value");
							sharedValuesValueBean.setText(jobVo.getSharedValues()[i].getKey());
						}
					}
				}
			}
		}
	}
	
	/**
	 * XML에 각 Listener, Reader, Writer, Partition, Decision의 bean 구성 
	 * @param beansElement 
	 * @param doc 
	 * 
	 * */
	private void appendBeanElement(Element beansElement){
        
		orderedMap.putAll(beanMap);

		Set<String> orderedMapKey= orderedMap.keySet();
		Object[] keyArray = orderedMapKey.toArray();
		
		for(int i = 0; i < orderedMap.size(); i++){
			String key = (String)keyArray[i];
			
			Element bean = new Element("bean");
			beansElement.addContent(bean);
			
			if(key.contains("jobListener")){
				
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
	
			} else if(key.contains("jobReader")) {
				
				appendJobReader(key, bean, beansElement);
				
			} else if(key.contains("jobWriter")) {
				
				appendJobWriter(key, bean, beansElement);

			} else if(key.contains("partitioner")) {
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
				
				Element partitionerBean = new Element("property");
				bean.addContent(partitionerBean);
				
				if(value[1].contains("Multi")){
					partitionerBean.setAttribute("name", "resources");
					partitionerBean.setAttribute("value", value[2]);
				} else {
					partitionerBean.setAttribute("name", "dataSource");
					partitionerBean.setAttribute("ref", "dataSource");
				}
				
			} else if(key.contains("fileNameListener")) {
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
				bean.setAttribute("scope", "step");
				
				Element fileNameListenerBean = new Element("property");
				bean.addContent(fileNameListenerBean);
				
				fileNameListenerBean.setAttribute("name", "path");
				if(System.getProperty("os.name").contains("Windows")) {
					fileNameListenerBean.setAttribute("value", "file:" + System.getProperty("user.home") + "\\");
				} else {
					fileNameListenerBean.setAttribute("value", "file:" + System.getProperty("user.home") + "/");
				}
			}else if(key.contains("stepListener")){
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
				
			}else if(key.contains("chunkListener")){
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
				
			} else if(key.contains("sharedValueListener")){
				String[] value = orderedMap.get(key).split("<");
				bean.setAttribute("id", "sharedValueListener");
				
				bean.setAttribute("class", "org.springframework.batch.core.listener.ExecutionContextPromotionListener");
				
				Element property = new Element("property");
				bean.addContent(property);
				
				property.setAttribute("name", value[0]);
				
				property.setAttribute("value", value[1]);
				
			} else if(key.contains("decider")){
				String[] value = orderedMap.get(key).split("<");
				
				bean.setAttribute("id", value[0]);
				bean.setAttribute("class", value[1]);
			} 
		}
	}
	
	/**
	 * 선택한 리더에 따라 하위 항목 구성
	 * @param key
	 * @param bean
	 * @param beansElement
	 * @param jobReaderContext
	 * @param doc
	 * @param jobReaderContext 
	 */
	
	private void appendJobReader(String key, Element bean, Element beansElement){
		
		String[] value = orderedMap.get(key).split("<");
		
		AppendBatchReaderWriterBeanOperation.appendReader(bean, beansElement, value);

	}
	
	/**
	 * 선택한 라이터에 따라 하위 항목 구성
	 * @param key
	 * @param bean
	 * @param beansElement 
	 * @param jobWriterContext
	 * @param doc
	 */
	private void appendJobWriter(String key, Element bean, Element beansElement){
		
		String[] value = orderedMap.get(key).split("<");
		
		AppendBatchReaderWriterBeanOperation.appendWriter(bean, beansElement, value);
	}
	
	
	/**
	 * 여러 Step 중, 시작 Step(진입점)태그를 Job 태그의 첫번째 하위 노드로 등록 
	 * @param job
	 */
	private void findStartingStep(Element job) {
		
		// step list
		List<Element> stepList = job.getChildren("step");
		
		// decision list
		List<Element> decisionList = job.getChildren("decision");
		
		// 'next step' list
		List<String> nextList = new ArrayList<String>();
		
		// starting step
		Element startingStep = null;
		
		
		// get 'next step' list
		for(int index = 0; index < stepList.size(); index++) {
			if(stepList.get(index).getAttributeValue("next") != null ) {
				nextList.add(stepList.get(index).getAttributeValue("next"));
			} 
		}
		
		// add decision's next tag to 'next step' list
		for(int index = 0; index < decisionList.size(); index++) {
			List<Element> decisionNextList = decisionList.get(index).getChildren("next");			
			if(decisionNextList != null && decisionNextList.size() > 0) {
				for(int innerIndex = 0; innerIndex < decisionNextList.size(); innerIndex++) {
					nextList.add(decisionNextList.get(innerIndex).getAttributeValue("to"));	
				}
			}	
		}
		
		for(int index = 0; index < stepList.size(); index++) {
			if( ( ! nextList.contains(stepList.get(index).getAttributeValue("id")) )) {
				// find a starting step
				if(stepList.get(index).getAttributeValue("next") != null ||  
						stepList.get(index).getChildren("next").size() > 0)  {
					startingStep = stepList.get(index).detach();
				}
			} 	
		}
		
		// 5. add a starting step to job's first node.
		if(startingStep != null) {
			job.addContent(0, startingStep);
		}
	}
	
	/**
	 * Step의 'next' attribute를 체크하여 decision을 가르킬 경우 'next' attribute를 삭제하고 'next' 태그로 대체 구성 
	 * @param job
	 */
	private void updateNextToAttribute(Element job) {
		
		// step list
		List<Element> stepList = job.getChildren("step");
		
		// decision list
		List<Element> decisionList = job.getChildren("decision");
		
		Element step = null;
		String decisionName = null;
		
		for(int index = 0; index < stepList.size(); index++) {
			step = stepList.get(index);
			
			for(int innerIndex2 = 0; innerIndex2 < decisionList.size(); innerIndex2++) {
				decisionName = decisionList.get(innerIndex2).getAttributeValue("id");
				
				// compare step's 'next' attribute with decision list
				if(step.getAttributeValue("next") != null && decisionName.equals(step.getAttributeValue("next"))){
					// remove a 'next' attribute if an id of decision tag is same to 'next' attribute
					job.getChildren("step").get(index).removeAttribute("next");
					
					// add 'next' tag to step's second node
					Element next = new Element("next");
					job.getChildren("step").get(index).addContent(1, next);
					
					next.setAttribute("on", "*");
					next.setAttribute("to", decisionName);
				}
			}		
		}
	}
}
