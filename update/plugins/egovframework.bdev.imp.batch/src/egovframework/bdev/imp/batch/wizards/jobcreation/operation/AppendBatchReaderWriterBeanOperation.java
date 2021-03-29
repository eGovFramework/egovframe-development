package egovframework.bdev.imp.batch.wizards.jobcreation.operation;

import org.jdom2.Element;

import egovframework.dev.imp.core.utils.NullUtil;



/**
 * Job XML 파일을 생성시 Reader/Writer 정보
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
public class AppendBatchReaderWriterBeanOperation {
	
	/**
	 * 선택한 Reader에 따라 설정정보 append 
	 * @param bean
	 * @param beansElement
	 * @param value
	 * @param doc
	 */
	public static void appendReader(Element bean, Element beansElement, String[] value) {
		
		//// File Reader
		
		String readerName = value[0];
		String readerClass = value[1];
		String readerType = value[3];
		
		//// Reader Context(value[5] ~ value[25])
		// file
		String tmpResource = value[5];
		String fieldName = value[6];
		String columnLength = value[7];
		String delimiter = value[8];
		String voClass = value[9];
		       
		// db       
		String ibatisStatement = value[13];
		String tmpConfigurationFile = value[14];
		String sql = value[15];
		String rowMapper = value[16];
		String pageSize = value[17];
		String sqlSortColumn = value[18];
		String sqlSelect = value[19];
		String sqlFrom = value[20];
		String sqlWhere = value[21];
		//String databaseType = value[26];
		String datasourceBeanID = value[27];
		
		// projectName
		String projectName = value[28];
		
		String[] sqlKeyValue = null;
		if(value.length == 30) {
			sqlKeyValue = value[29].split("\\|");
		}
		
		String resource = tmpResource;
		// only file descriptor(file:)
		if(resource.contains(projectName)) {
			resource = resource.substring(resource.indexOf(projectName) + projectName.length());
			if(resource.indexOf("/") == 0) {
				resource = resource.substring(1);
			}
			
			resource = "file:./" + resource;
		} else {
			resource = "file:" + resource;
		}
		
				
		// normal / partition step check
		boolean isPartitionStep = value[4].equals("partition") ? true : false;
		
		// prefix for deligate reader
		String prefix = readerName.split("\\.")[0] + "." + readerName.split("\\.")[1] ;
		
		bean.setAttribute("id", readerName);
		bean.setAttribute("class", readerClass);
		
		if(readerType.equals("FixedFlatFileItemReader")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			
			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineMapper");
			
			Element beanLineMapper = new Element("bean");
			property2.addContent(beanLineMapper);
			beanLineMapper.setAttribute("class", "org.springframework.batch.item.file.mapping.DefaultLineMapper");
			
			Element property3 = new Element("property");
			beanLineMapper.addContent(property3);
			property3.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property3.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "org.springframework.batch.item.file.transform.FixedLengthTokenizer");
			
			Element property4 = new Element("property");
			beanFixedLength.addContent(property4);
			property4.setAttribute("name", "names");
			property4.setAttribute("value", fieldName);
			
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "columns");
			property5.setAttribute("value", columnLength);
			
			Element beanfieldSetMapper = new Element("property");
			beanLineMapper.addContent(beanfieldSetMapper);
			beanfieldSetMapper.setAttribute("name", "fieldSetMapper");
			
			Element beanBeanWrapperFieldSet = new Element("bean");
			beanfieldSetMapper.addContent(beanBeanWrapperFieldSet);
			beanBeanWrapperFieldSet.setAttribute("class", "org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper");
			
			Element beantargetType = new Element("property");
			beanBeanWrapperFieldSet.addContent(beantargetType);
			beantargetType.setAttribute("name", "targetType");
			beantargetType.setAttribute("value", voClass);
			
		} else if(readerType.equals("DelimitedFlatFileItemReader")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[fileName]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineMapper");
			
			Element beanLineMapper = new Element("bean");
			property2.addContent(beanLineMapper);
			beanLineMapper.setAttribute("class", "org.springframework.batch.item.file.mapping.DefaultLineMapper");
			
			Element property3 = new Element("property");
			beanLineMapper.addContent(property3);
			property3.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property3.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineTokenizer");
			
			Element property4 = new Element("property");
			beanFixedLength.addContent(property4);
			property4.setAttribute("name", "delimiter");
			property4.setAttribute("value", delimiter);
			
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "names");
			property5.setAttribute("value", fieldName);
			
			Element beanfieldSetMapper = new Element("property");
			beanLineMapper.addContent(beanfieldSetMapper);
			beanfieldSetMapper.setAttribute("name", "fieldSetMapper");
			
			Element beanBeanWrapperFieldSet = new Element("bean");
			beanfieldSetMapper.addContent(beanBeanWrapperFieldSet);
			beanBeanWrapperFieldSet.setAttribute("class", "org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper");
			
			Element beantargetType = new Element("property");
			beanBeanWrapperFieldSet.addContent(beantargetType);
			beantargetType.setAttribute("name", "targetType");
			beantargetType.setAttribute("value", voClass);
			
		} else if(readerType.equals("EgovFixedFlatFileItemReader")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[fileName]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineMapper");
			
			Element beanLineMapper = new Element("bean");
			property2.addContent(beanLineMapper);
			beanLineMapper.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovDefaultLineMapper");
			
			Element property3 = new Element("property");
			beanLineMapper.addContent(property3);
			property3.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property3.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFixedLengthTokenizer");
			
			Element property4 = new Element("property");
			beanFixedLength.addContent(property4);
			property4.setAttribute("name", "columns");
			property4.setAttribute("value", columnLength);
			
			Element beanfieldSetMapper = new Element("property");
			beanLineMapper.addContent(beanfieldSetMapper);
			beanfieldSetMapper.setAttribute("name", "objectMapper");
			
			Element beanBeanWrapperFieldSet = new Element("bean");
			beanfieldSetMapper.addContent(beanBeanWrapperFieldSet);
			beanBeanWrapperFieldSet.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovObjectMapper");
			
			Element beantargetType = new Element("property");
			beanBeanWrapperFieldSet.addContent(beantargetType);
			beantargetType.setAttribute("name", "type");
			beantargetType.setAttribute("value", voClass);
			
			Element beanEgovObjectMapperproperty = new Element("property");
			beanBeanWrapperFieldSet.addContent(beanEgovObjectMapperproperty);
			beanEgovObjectMapperproperty.setAttribute("name", "names");
			beanEgovObjectMapperproperty.setAttribute("value", fieldName);
			
		}  else if(readerType.equals("EgovDelimitedFlatFileItemReader")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[fileName]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineMapper");
			
			Element beanLineMapper = new Element("bean");
			property2.addContent(beanLineMapper);
			beanLineMapper.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovDefaultLineMapper");
			
			Element property3 = new Element("property");
			beanLineMapper.addContent(property3);
			property3.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property3.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovDelimitedLineTokenizer");
			
			Element property4 = new Element("property");
			beanFixedLength.addContent(property4);
			property4.setAttribute("name", "delimiter");
			property4.setAttribute("value", delimiter);
			
			Element beanfieldSetMapper = new Element("property");
			beanLineMapper.addContent(beanfieldSetMapper);
			beanfieldSetMapper.setAttribute("name", "objectMapper");
			
			Element beanBeanWrapperFieldSet = new Element("bean");
			beanfieldSetMapper.addContent(beanBeanWrapperFieldSet);
			beanBeanWrapperFieldSet.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovObjectMapper");
			
			Element beantargetType = new Element("property");
			beanBeanWrapperFieldSet.addContent(beantargetType);
			beantargetType.setAttribute("name", "type");
			beantargetType.setAttribute("value", voClass);
			
			Element beanEgovObjectMapperproperty = new Element("property");
			beanBeanWrapperFieldSet.addContent(beanEgovObjectMapperproperty);
			beanEgovObjectMapperproperty.setAttribute("name", "names");
			beanEgovObjectMapperproperty.setAttribute("value", fieldName);
			
		}  else if(readerType.equals("FixedMultiResourceItemReader")){
		
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resources");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "delegate");
			property2.setAttribute("ref", prefix + ".delegatingReader");
			
			Element beandelegatingReader = new Element("bean");
			beansElement.addContent(beandelegatingReader);
			beandelegatingReader.setAttribute("id", prefix + ".delegatingReader");
			beandelegatingReader.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemReader");
			
			Element property3 = new Element("property");
			beandelegatingReader.addContent(property3);
			property3.setAttribute("name", "lineMapper");
			
			Element beanDefaultLine = new Element("bean");
			property3.addContent(beanDefaultLine);
			beanDefaultLine.setAttribute("class", "org.springframework.batch.item.file.mapping.DefaultLineMapper");
			
			Element property4 = new Element("property");
			beanDefaultLine.addContent(property4);
			property4.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property4.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "org.springframework.batch.item.file.transform.FixedLengthTokenizer");
			
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "names");
			property5.setAttribute("value", fieldName);
			
			Element property6 = new Element("property");
			beanFixedLength.addContent(property6);
			property6.setAttribute("name", "columns");
			property6.setAttribute("value", columnLength);
			
			Element property7 = new Element("property");
			beanDefaultLine.addContent(property7);
			property7.setAttribute("name", "fieldSetMapper");
			
			Element beanWrapperFieldSet = new Element("bean");
			property7.addContent(beanWrapperFieldSet);
			beanWrapperFieldSet.setAttribute("class", "org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper");
			
			Element property8 = new Element("property");
			beanWrapperFieldSet.addContent(property8);
			property8.setAttribute("name", "targetType");
			property8.setAttribute("value", voClass);
			
		} else if(readerType.equals("DelimitedMultiResourceItemReader")){
		
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resources");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "delegate");
			property2.setAttribute("ref", prefix + ".delegatingReader");
			
			
			Element beandelegatingReader = new Element("bean");
			beansElement.addContent(beandelegatingReader);
			beandelegatingReader.setAttribute("id", prefix + ".delegatingReader");
			beandelegatingReader.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemReader");
			
			Element property3 = new Element("property");
			beandelegatingReader.addContent(property3);
			property3.setAttribute("name", "lineMapper");
			
			Element beanDefaultLine = new Element("bean");
			property3.addContent(beanDefaultLine);
			beanDefaultLine.setAttribute("class", "org.springframework.batch.item.file.mapping.DefaultLineMapper");
			
			Element property4 = new Element("property");
			beanDefaultLine.addContent(property4);
			property4.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property4.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineTokenizer");
			
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "delimiter");
			property5.setAttribute("value", delimiter);
			
			Element property6 = new Element("property");
			beanFixedLength.addContent(property6);
			property6.setAttribute("name", "names");
			property6.setAttribute("value", fieldName);
			
			Element property7 = new Element("property");
			beanDefaultLine.addContent(property7);
			property7.setAttribute("name", "fieldSetMapper");
			
			Element beanWrapperFieldSet = new Element("bean");
			property7.addContent(beanWrapperFieldSet);
			beanWrapperFieldSet.setAttribute("class", "org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper");
			
			Element property8 = new Element("property");
			beanWrapperFieldSet.addContent(property8);
			property8.setAttribute("name", "targetType");
			property8.setAttribute("value", voClass);
			
		} else if(readerType.equals("EgovFixedMultiResourceItemReader")){
		
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resources");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "delegate");
			property2.setAttribute("ref", prefix + ".delegatingReader");
			
			
			Element beandelegatingReader = new Element("bean");
			beansElement.addContent(beandelegatingReader);
			beandelegatingReader.setAttribute("id", prefix + ".delegatingReader");
			beandelegatingReader.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemReader");
			
			Element property3 = new Element("property");
			beandelegatingReader.addContent(property3);
			property3.setAttribute("name", "lineMapper");
			
			Element beanDefaultLine = new Element("bean");
			property3.addContent(beanDefaultLine);
			beanDefaultLine.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovDefaultLineMapper");
			
			Element property4 = new Element("property");
			beanDefaultLine.addContent(property4);
			property4.setAttribute("name", "lineTokenizer");
			
			Element beanFixedLength = new Element("bean");
			property4.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFixedLengthTokenizer");
				
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "columns");
			property5.setAttribute("value", columnLength);
			
			
			Element property7 = new Element("property");
			beanDefaultLine.addContent(property7);
			property7.setAttribute("name", "objectMapper");
			
			Element beanEgovObject = new Element("bean");
			property7.addContent(beanEgovObject);
			beanEgovObject.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovObjectMapper");
			
			Element property8 = new Element("property");
			beanEgovObject.addContent(property8);
			property8.setAttribute("name", "type");
			property8.setAttribute("value", voClass);
			
			Element property9 = new Element("property");
			beanEgovObject.addContent(property9);
			property9.setAttribute("name", "names");
			property9.setAttribute("value", fieldName);
			
		} else if(readerType.equals("EgovDelimitedMultiResourceItemReader")){
		
			bean.setAttribute("scope", "step");
			
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resources");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "delegate");
			property2.setAttribute("ref", prefix + ".delegatingReader");
			
			Element beandelegatingReader = new Element("bean");
			beansElement.addContent(beandelegatingReader);
			beandelegatingReader.setAttribute("id", prefix + ".delegatingReader");
			beandelegatingReader.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemReader");
			
			Element property3 = new Element("property");
			beandelegatingReader.addContent(property3);
			property3.setAttribute("name", "lineMapper");
			
			Element beanDefaultLine = new Element("bean");
			property3.addContent(beanDefaultLine);
			beanDefaultLine.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovDefaultLineMapper");
			
			Element property4 = new Element("property");
			beanDefaultLine.addContent(property4);
			property4.setAttribute("name", "lineTokenizer");
			
			Element beanDelimited = new Element("bean");
			property4.addContent(beanDelimited);
			beanDelimited.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovDelimitedLineTokenizer");
				
			Element property5 = new Element("property");
			beanDelimited.addContent(property5);
			property5.setAttribute("name", "delimiter");
			property5.setAttribute("value", delimiter);
			
			Element property7 = new Element("property");
			beanDefaultLine.addContent(property7);
			property7.setAttribute("name", "objectMapper");
			
			Element beanEgovObject = new Element("bean");
			property7.addContent(beanEgovObject);
			beanEgovObject.setAttribute("class", "egovframework.brte.core.item.file.mapping.EgovObjectMapper");
			
			Element property8 = new Element("property");
			beanEgovObject.addContent(property8);
			property8.setAttribute("name", "type");
			property8.setAttribute("value", voClass);
			
			Element property9 = new Element("property");
			beanEgovObject.addContent(property9);
			property9.setAttribute("name", "names");
			property9.setAttribute("value", fieldName);
			
		}
		//// DB Reader
		else if(readerType.equals("IbatisPagingItemReader")){
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "queryId");
			property.setAttribute("value", ibatisStatement);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sqlMapClient");
			
			Element beandelegatingReader = new Element("bean");
			property2.addContent(beandelegatingReader);
			beandelegatingReader.setAttribute("class", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property3 = new Element("property");
				beandelegatingReader.addContent(property3);
				property3.setAttribute("name", "dataSource");
				property3.setAttribute("ref", datasourceBeanID);
			}
			
			Element property4 = new Element("property");
			beandelegatingReader.addContent(property4);
			property4.setAttribute("name", "configLocation");
			
			String configurationFile = tmpConfigurationFile;
			// only file descriptor(file:)
			if(configurationFile.contains(projectName)) {
				configurationFile = configurationFile.substring(configurationFile.indexOf(projectName) + projectName.length());
				if(configurationFile.indexOf("/") == 0) {
					configurationFile = configurationFile.substring(1);
				}
				
				configurationFile = "file:./" + configurationFile;
			} else {
				configurationFile = "file:" + configurationFile;
			}
			
			property4.setAttribute("value", configurationFile);
			
		} else if(readerType.equals("CustomizedJdbcCursorItemReader")){
			 
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property = new Element("property");
				bean.addContent(property);
				property.setAttribute("name", "dataSource");
				property.setAttribute("ref", datasourceBeanID);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sql");
			property2.setAttribute("value", sql);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "rowMapper");
			
			Element beanCustomized = new Element("bean");
			property3.addContent(beanCustomized);
			beanCustomized.setAttribute("class", rowMapper);
			
		} else if(readerType.equals("SqlPagingQueryJdbcItemReader")){
			
			bean.setAttribute("scope", "step");
			 
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property = new Element("property");
				bean.addContent(property);
				property.setAttribute("name", "dataSource");
				property.setAttribute("ref", datasourceBeanID);
			}
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "rowMapper");
			
			Element beanCustomized = new Element("bean");
			property3.addContent(beanCustomized);
			beanCustomized.setAttribute("class", rowMapper);
			
			Element property9 = new Element("property");
			bean.addContent(property9);
			property9.setAttribute("name", "pageSize");
			property9.setAttribute("value", pageSize);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "queryProvider");
			
			Element beanSqlPagingQueryProvider= new Element("bean");
			property2.addContent(beanSqlPagingQueryProvider);
			beanSqlPagingQueryProvider.setAttribute("class", "org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean");
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property4 = new Element("property");
				beanSqlPagingQueryProvider.addContent(property4);
				property4.setAttribute("name", "dataSource");
				property4.setAttribute("ref", datasourceBeanID);
			}
			
			Element property5 = new Element("property");
			beanSqlPagingQueryProvider.addContent(property5);
			property5.setAttribute("name", "sortKey");
			property5.setAttribute("value", sqlSortColumn);
			
			Element property6 = new Element("property");
			beanSqlPagingQueryProvider.addContent(property6);
			property6.setAttribute("name", "selectClause");
			property6.setAttribute("value", sqlSelect);
			
			Element property7 = new Element("property");
			beanSqlPagingQueryProvider.addContent(property7);
			property7.setAttribute("name", "fromClause");
			property7.setAttribute("value", sqlFrom);
			
			Element property8 = new Element("property");
			beanSqlPagingQueryProvider.addContent(property8);
			property8.setAttribute("name", "whereClause");
			property8.setAttribute("value", sqlWhere);
			
			if(!NullUtil.isNull(sqlKeyValue)){
				Element property10 = new Element("property");
				bean.addContent(property10);
				property10.setAttribute("name", "parameterValues");
				
				Element map = new Element("map");
				property10.addContent(map);
				
					for(int i = 0; i < sqlKeyValue.length; i++) {
						Element entry = new Element("entry");
						map.addContent(entry);
						entry.setAttribute("key", sqlKeyValue[i].split("=")[0]);
						entry.setAttribute("value", sqlKeyValue[i].split("=")[1]);
					}
				}
		} else {
			
			bean.setAttribute("scope", "step");

		}
	}
	
	/**
	 * 선택한 Writer에 따라 설정정보 append 
	 * @param bean
	 * @param beansElement
	 * @param value
	 * @param doc
	 */
	public static void appendWriter(Element bean, Element beansElement, String[] value) {
		//// File Writer
		String writerName = value[0];
		String writerClass = value[1];
		String writerType = value[3];
		
		//// Writer Context(value[5] ~ value[25])
		// file
		String tmpResource = value[5];
		String fieldName = value[6];
		String delimiter = value[8];
		String fieldFormat = value[10];
		String fieldRange = value[11];
		String maxCountPerResource = value[12];
		       
		// db       
		String ibatisStatement = value[13];
		String tmpConfigurationFile = value[14];
		String sqlUpdate = value[22];
		String sqlInsert = value[23];
		String params = value[24];
		String rowSetter = value[25];
		//String databaseType = value[26];
		String datasourceBeanID = value[27];
		
		// projectName
		String projectName = value[28];
		
		String resource = tmpResource;
		// only file descriptor(file:)
		if(resource.contains(projectName)) {
			resource = resource.substring(resource.indexOf(projectName) + projectName.length());
			if(resource.indexOf("/") == 0) {
				resource = resource.substring(1);
			}
			
			resource = "file:./" + resource;
		} else {
			resource = "file:" + resource;
		}
		
		// normal / partition step check
		boolean isPartitionStep = value[4].equals("partition") ? true : false;
		
		// prefix for deligate writer
		String prefix = writerName.split("\\.")[0] + "." + writerName.split("\\.")[1] ;
		
		bean.setAttribute("id", writerName);
		bean.setAttribute("class", writerClass);
		
		if(writerType.equals("DelimitedFlatFileItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			
			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineAggregator");
			
			Element beanDelimitedLine = new Element("bean");
			property2.addContent(beanDelimitedLine);
			beanDelimitedLine.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineAggregator");
			
			Element property3 = new Element("property");
			beanDelimitedLine.addContent(property3);
			property3.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField = new Element("bean");
			property3.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor");
			
			Element property4 = new Element("property");
			beanWrapperField.addContent(property4);
			property4.setAttribute("name", "names");
			property4.setAttribute("value", fieldName);
			
			Element property5 = new Element("property");
			beanDelimitedLine.addContent(property5);
			property5.setAttribute("name", "delimiter");
			property5.setAttribute("value", delimiter);
			
		} else if(writerType.equals("FormatterFlatFileItemWriter")){
			
			bean.setAttribute("scope", "step");
			
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			
			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineAggregator");
			
			Element beanDelimitedLine = new Element("bean");
			property2.addContent(beanDelimitedLine);
			beanDelimitedLine.setAttribute("class", "org.springframework.batch.item.file.transform.FormatterLineAggregator");
			
			Element property3 = new Element("property");
			beanDelimitedLine.addContent(property3);
			property3.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField = new Element("bean");
			property3.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor");
			
			Element property4 = new Element("property");
			beanWrapperField.addContent(property4);
			property4.setAttribute("name", "names");
			property4.setAttribute("value", fieldName);
			
			Element property5 = new Element("property");
			beanDelimitedLine.addContent(property5);
			property5.setAttribute("name", "format");
			property5.setAttribute("value", fieldFormat);
			
		} else if(writerType.equals("EgovDelimitedFlatFileItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineAggregator");
			
			Element beanDelimitedLine = new Element("bean");
			property2.addContent(beanDelimitedLine);
			beanDelimitedLine.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineAggregator");
			
			Element property3 = new Element("property");
			beanDelimitedLine.addContent(property3);
			property3.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField = new Element("bean");
			property3.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFieldExtractor");
			
			Element property4 = new Element("property");
			beanWrapperField.addContent(property4);
			property4.setAttribute("name", "names");
			property4.setAttribute("value", fieldName);
			
			Element property5 = new Element("property");
			beanDelimitedLine.addContent(property5);
			property5.setAttribute("name", "delimiter");
			property5.setAttribute("value", delimiter);
			
		} else if(writerType.equals("EgovFixedFlatFileItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "lineAggregator");
			
			Element beanDelimitedLine = new Element("bean");
			property2.addContent(beanDelimitedLine);
			beanDelimitedLine.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFixedLengthLineAggregator");
			
			Element property3 = new Element("property");
			beanDelimitedLine.addContent(property3);
			property3.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField = new Element("bean");
			property3.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFieldExtractor");
			
			Element property4 = new Element("property");
			beanWrapperField.addContent(property4);
			property4.setAttribute("name", "names");
			property4.setAttribute("value", fieldName);
			
			Element property5 = new Element("property");
			beanDelimitedLine.addContent(property5);
			property5.setAttribute("name", "fieldRanges");
			property5.setAttribute("value", fieldRange);
			
		} else if(writerType.equals("DelimitedMultiResourceItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");

			if(isPartitionStep) {
				property.setAttribute("value", "#{stepExecutionContext[outputFile]}");
			} else {
				property.setAttribute("value", resource);
			}
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "itemCountLimitPerResource");
			property2.setAttribute("value", maxCountPerResource);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "delegate");
			property3.setAttribute("ref", prefix + ".delegateWriter");
			
			Element beanFileItemWriter = new Element("bean");
			beansElement.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("id", prefix + ".delegateWriter");
			beanFileItemWriter.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemWriter");
			beanFileItemWriter.setAttribute("scope", "step");
			
			Element property4 = new Element("property");
			beanFileItemWriter.addContent(property4);
			property4.setAttribute("name", "lineAggregator");
			
			Element beanDelimitedLine = new Element("bean");
			property4.addContent(beanDelimitedLine);
			beanDelimitedLine.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineAggregator");
			
			Element property5 = new Element("property");
			beanDelimitedLine.addContent(property5);
			property5.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField= new Element("bean");
			property5.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor");
			
			Element property6 = new Element("property");
			beanWrapperField.addContent(property6);
			property6.setAttribute("name", "names");
			property6.setAttribute("value", fieldName);
			
			Element property7 = new Element("property");
			beanDelimitedLine.addContent(property7);
			property7.setAttribute("name", "delimiter");
			property7.setAttribute("value", delimiter);
			
		} else if(writerType.equals("FormatterMultiResourceItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "itemCountLimitPerResource");
			property2.setAttribute("value", maxCountPerResource);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "delegate");
			property3.setAttribute("ref", prefix + ".delegateWriter");
			
			Element beanFileItemWriter = new Element("bean");
			beansElement.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("id", prefix + ".delegateWriter");
			beanFileItemWriter.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemWriter");
			beanFileItemWriter.setAttribute("scope", "step");
			
			Element property4 = new Element("property");
			beanFileItemWriter.addContent(property4);
			property4.setAttribute("name", "lineAggregator");
			
			Element beanFormatterLine = new Element("bean");
			property4.addContent(beanFormatterLine);
			beanFormatterLine.setAttribute("class", "org.springframework.batch.item.file.transform.FormatterLineAggregator");
			
			Element property5 = new Element("property");
			beanFormatterLine.addContent(property5);
			property5.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField= new Element("bean");
			property5.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor");
			
			Element property6 = new Element("property");
			beanWrapperField.addContent(property6);
			property6.setAttribute("name", "names");
			property6.setAttribute("value", fieldName);
			
			Element property7 = new Element("property");
			beanFormatterLine.addContent(property7);
			property7.setAttribute("name", "format");
			property7.setAttribute("value", fieldFormat);
			
		} else if(writerType.equals("EgovDelimitedMultiResourceItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "itemCountLimitPerResource");
			property2.setAttribute("value", maxCountPerResource);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "delegate");
			property3.setAttribute("ref", prefix + ".delegateWriter");
			
			Element beanFileItemWriter = new Element("bean");
			beansElement.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("id", prefix + ".delegateWriter");
			beanFileItemWriter.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemWriter");
			beanFileItemWriter.setAttribute("scope", "step");
			
			Element property4 = new Element("property");
			beanFileItemWriter.addContent(property4);
			property4.setAttribute("name", "lineAggregator");
			
			Element beanFormatterLine = new Element("bean");
			property4.addContent(beanFormatterLine);
			beanFormatterLine.setAttribute("class", "org.springframework.batch.item.file.transform.DelimitedLineAggregator");
			
			Element property5 = new Element("property");
			beanFormatterLine.addContent(property5);
			property5.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField= new Element("bean");
			property5.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFieldExtractor");
			
			Element property6 = new Element("property");
			beanWrapperField.addContent(property6);
			property6.setAttribute("name", "names");
			property6.setAttribute("value", fieldName);
			
			Element property7 = new Element("property");
			beanFormatterLine.addContent(property7);
			property7.setAttribute("name", "delimiter");
			property7.setAttribute("value", delimiter);
			
		} else if(writerType.equals("EgovFixedMultiResourceItemWriter")){
			
			bean.setAttribute("scope", "step");
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "resource");
			property.setAttribute("value", resource);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "itemCountLimitPerResource");
			property2.setAttribute("value", maxCountPerResource);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "delegate");
			property3.setAttribute("ref", prefix + ".delegateWriter");
			
			Element beanFileItemWriter = new Element("bean");
			beansElement.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("id", prefix + ".delegateWriter");
			beanFileItemWriter.setAttribute("class", "org.springframework.batch.item.file.FlatFileItemWriter");
			beanFileItemWriter.setAttribute("scope", "step");
			
			Element property4 = new Element("property");
			beanFileItemWriter.addContent(property4);
			property4.setAttribute("name", "lineAggregator");
			
			Element beanFixedLength = new Element("bean");
			property4.addContent(beanFixedLength);
			beanFixedLength.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFixedLengthLineAggregator");
			
			Element property5 = new Element("property");
			beanFixedLength.addContent(property5);
			property5.setAttribute("name", "fieldExtractor");
			
			Element beanWrapperField= new Element("bean");
			property5.addContent(beanWrapperField);
			beanWrapperField.setAttribute("class", "egovframework.brte.core.item.file.transform.EgovFieldExtractor");
			
			Element property6 = new Element("property");
			beanWrapperField.addContent(property6);
			property6.setAttribute("name", "names");
			property6.setAttribute("value", fieldName);
			
			Element property7 = new Element("property");
			beanFixedLength.addContent(property7);
			property7.setAttribute("name", "fieldRanges");
			property7.setAttribute("value", fieldRange);
			
		} 
		//// DB Writer
		else if(writerType.equals("IbatisBatchItemWriter")){
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "statementId");
			property.setAttribute("value", ibatisStatement);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sqlMapClient");
			
			Element beanFileItemWriter = new Element("bean");
			property2.addContent(beanFileItemWriter);
			
			beanFileItemWriter.setAttribute("class", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property4 = new Element("property");
				beanFileItemWriter.addContent(property4);
				property4.setAttribute("name", "dataSource");
				property4.setAttribute("ref", datasourceBeanID);
			}
			
			Element property5 = new Element("property");
			beanFileItemWriter.addContent(property5);
			property5.setAttribute("name", "configLocation");
			
			String configurationFile = tmpConfigurationFile;
			// only file descriptor(file:)
			if(configurationFile.contains(projectName)) {
				configurationFile = configurationFile.substring(configurationFile.indexOf(projectName) + projectName.length());
				if(configurationFile.indexOf("/") == 0) {
					configurationFile = configurationFile.substring(1);
				}
				
				configurationFile = "file:./" + configurationFile;
			} else {
				configurationFile = "file:" + configurationFile;
			}
			
			property5.setAttribute("value", configurationFile);
			
			
			
		} else if(writerType.equals("SqlParameterJdbcBatchItemWriter")){
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "itemSqlParameterSourceProvider");
			
			Element beanFileItemWriter = new Element("bean");
			property.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("class", "org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider");
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sql");
			property2.setAttribute("value", sqlUpdate);
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property3 = new Element("property");
				bean.addContent(property3);
				property3.setAttribute("name", "dataSource");
				property3.setAttribute("ref", datasourceBeanID);
			}
		
		} else if(writerType.equals("ItemPreparedStatementJdbcBatchItemWriter")){
			
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "ItemPreparedStatementSetter");
			
			Element beanFileItemWriter = new Element("bean");
			property.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("class", rowSetter);
						
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sql");
			property2.setAttribute("value", sqlInsert);
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property3 = new Element("property");
				bean.addContent(property3);
				property3.setAttribute("name", "dataSource");
				property3.setAttribute("ref", datasourceBeanID);
			}
			
		} else if(writerType.equals("EgovItemPreparedStatementJdbcBatchItemWriter")){
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "itemPreparedStatementSetter");
			
			Element beanFileItemWriter = new Element("bean");
			property.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("class", "egovframework.brte.core.item.database.support.EgovMethodMapItemPreparedStatementSetter");
						
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sql");
			property2.setAttribute("value", sqlInsert);
			
			Element property3 = new Element("property");
			bean.addContent(property3);
			property3.setAttribute("name", "params");
			property3.setAttribute("value", params);
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property4 = new Element("property");
				bean.addContent(property4);
				property4.setAttribute("name", "dataSource");
				property4.setAttribute("ref", datasourceBeanID);
			}
			
		} else if(writerType.equals("EgovCustomizedJdbcBatchItemWriter")){
			 
			Element property = new Element("property");
			bean.addContent(property);
			property.setAttribute("name", "ItemPreparedStatementSetter");
			
			Element beanFileItemWriter = new Element("bean");
			property.addContent(beanFileItemWriter);
			beanFileItemWriter.setAttribute("class", rowSetter);
			
			Element property2 = new Element("property");
			bean.addContent(property2);
			property2.setAttribute("name", "sql");
			property2.setAttribute("value", sqlInsert);
			
			if(!NullUtil.isNull(datasourceBeanID) && !datasourceBeanID.equalsIgnoreCase("null")){
				Element property3 = new Element("property");
				bean.addContent(property3);
				property3.setAttribute("name", "dataSource");
				property3.setAttribute("ref", datasourceBeanID);
			}
						
		} else {
			
			bean.setAttribute("class", value[1]);
			bean.setAttribute("scope", "step");
			
		 }
	}
}