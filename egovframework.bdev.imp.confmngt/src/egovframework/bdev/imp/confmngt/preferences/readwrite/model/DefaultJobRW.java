package egovframework.bdev.imp.confmngt.preferences.readwrite.model;
/**
 * 기본 Job Reader / Writer의 이름, Class, Type 
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.10.10
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.10.10	조용현	최초생성
 *
 * 
 * </pre>
 */

public class DefaultJobRW {
	// Default Job Reader / Writer의 이름
	final static public String FIXED_FLAT_FILE_ITEM_READER = "FixedFlatFileItemReader";
	final static public String DELIMITED_FLAT_FILE_ITEM_READER = "DelimitedFlatFileItemReader";
	final static public String EGOV_FIXED_FLAT_FILE_ITEM_READER = "EgovFixedFlatFileItemReader";
	final static public String EGOV_DELIMITED_FLAT_FILE_ITEM_READER = "EgovDelimitedFlatFileItemReader";
	final static public String FIXED_MULTI_RESOURCE_ITEM_READER = "FixedMultiResourceItemReader";
	final static public String DELIMITED_MULTI_RESOURCE_ITEM_READER = "DelimitedMultiResourceItemReader";
	final static public String EGOV_FIXED_MULTI_RESOURCE_ITEM_READER = "EgovFixedMultiResourceItemReader";
	final static public String EGOV_DELIMITED_MULTI_RESOURCE_ITEM_READER = "EgovDelimitedMultiResourceItemReader";
	final static public String CUSTOMIZE_FILE_READER = "CustomizeFileReader";
	final static public String IBATIS_PAGING_ITEM_READER = "IbatisPagingItemReader";
	final static public String CUSTOMIZED_JDBC_CURSOR_ITEM_READER = "CustomizedJdbcCursorItemReader";
	final static public String SQL_PAGING_QUERY_JDBC_ITEM_READER = "SqlPagingQueryJdbcItemReader";
	final static public String CUSTOMIZE_DB_READER = "CustomizeDBReader";
	
	final static public String DELIMITED_FLAT_FILE_ITEM_WRITER = "DelimitedFlatFileItemWriter";
	final static public String FORMATTER_FLAT_FILE_ITEM_WRITER = "FormatterFlatFileItemWriter";
	final static public String EGOV_DELIMITED_FLAT_FILE_ITEM_WRITER = "EgovDelimitedFlatFileItemWriter";
	final static public String EGOV_FIXED_FLAT_FILE_ITEM_WRITER = "EgovFixedFlatFileItemWriter";
	final static public String DELIMITED_MULTI_RESOURCE_ITEM_WRITER = "DelimitedMultiResourceItemWriter";
	final static public String FORMATTER_MULTI_RESOURCE_ITEM_WRITER = "FormatterMultiResourceItemWriter";
	final static public String EGOV_DELIMITED_MULTI_RESOURCE_ITEM_WRITER = "EgovDelimitedMultiResourceItemWriter";
	final static public String EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER = "EgovFixedMultiResourceItemWriter";
	final static public String CUSTOMIZE_FILE_WRITER = "CustomizeFileWriter";
	final static public String IBATIS_BATCH_ITEM_WRITER = "IbatisBatchItemWriter";
	final static public String SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER = "SqlParameterJdbcBatchItemWriter";
	final static public String ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER = "ItemPreparedStatementJdbcBatchItemWriter";
	final static public String EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER = "EgovItemPreparedStatementJdbcBatchItemWriter";
	final static public String EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER = "EgovCustomizedJdbcBatchItemWriter";
	final static public String CUSTOMIZE_DB_WRITER = "CustomizeDBWriter";
	
	
	// 실행환경 배치 코어(egovframe-rte-bat-core)의 패키지. Job XML 에 기록하는 실행환경 클래스명은 모두 이 값에서 만든다.
	final static public String EGOV_BAT_CORE_PACKAGE = "org.egovframe.rte.bat.core";

	// Default Job Reader / Writer의 Class
	final static public String FLAT_FILE_ITEM_READER_CLASS = "org.springframework.batch.item.file.FlatFileItemReader";
	final static public String MULTI_RESOURCE_ITEM_READER_CLASS = "org.springframework.batch.item.file.MultiResourceItemReader";
	final static public String IBATIS_PAGING_ITEM_READER_CLASS = "org.springframework.batch.item.database.IbatisPagingItemReader";
	final static public String JDBC_CURSOR_ITEM_READER_CLASS = "org.springframework.batch.item.database.JdbcCursorItemReader";
	final static public String JDBC_PAGING_ITEM_READER_CLASS = "org.springframework.batch.item.database.JdbcPagingItemReader";
	final static public String FLAT_FILE_ITEM_WRITER_CLASS = "org.springframework.batch.item.file.FlatFileItemWriter";
	final static public String IBATIS_BATCH_ITEM_WRITER_CLASS = "org.springframework.batch.item.database.IbatisBatchItemWriter";
	final static public String JDBC_BATCH_ITEM_WRITER_CLASS = "org.springframework.batch.item.database.JdbcBatchItemWriter";
	final static public String EGOV_JDBC_BATCH_ITEM_WRITER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.database.EgovJdbcBatchItemWriter";
	final static public String MULTI_RESOURCE_ITEM_WRITER_CLASS = "org.springframework.batch.item.file.MultiResourceItemWriter";

	// Job XML 의 Reader / Writer 하위 bean, Listener, Decider 에 기록하는 실행환경 Class
	final static public String EGOV_DEFAULT_LINE_MAPPER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.mapping.EgovDefaultLineMapper";
	final static public String EGOV_OBJECT_MAPPER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.mapping.EgovObjectMapper";
	final static public String EGOV_FIXED_LENGTH_TOKENIZER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.transform.EgovFixedLengthTokenizer";
	final static public String EGOV_DELIMITED_LINE_TOKENIZER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.transform.EgovDelimitedLineTokenizer";
	final static public String EGOV_FIELD_EXTRACTOR_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.transform.EgovFieldExtractor";
	final static public String EGOV_FIXED_LENGTH_LINE_AGGREGATOR_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.file.transform.EgovFixedLengthLineAggregator";
	final static public String EGOV_METHOD_MAP_ITEM_PREPARED_STATEMENT_SETTER_CLASS = EGOV_BAT_CORE_PACKAGE + ".item.database.support.EgovMethodMapItemPreparedStatementSetter";
	final static public String EGOV_OUTPUT_FILE_LISTENER_CLASS = EGOV_BAT_CORE_PACKAGE + ".listener.EgovOutputFileListener";
	final static public String EGOV_DECIDER_CLASS = EGOV_BAT_CORE_PACKAGE + ".job.flow.EgovDecider";

	
	// Default Job Reader / Writer의 Detail Info Type
	final static public String FIXED_READER_TYPE = "FixedReader";
	final static public String DELIMITED_READER_TYPE = "DelimitedReader";
	final static public String DELIMITED_WRITER_TYPE = "DelimitedWriter";
	final static public String FORMATTER_FLAT_FILE_ITEM_WRITER_TYPE = "FormatterFlatFileItemWriter";
	final static public String EGOV_FIXED_FLAT_FILE_ITEM_WRITER_TYPE = "EgoFixedFlatFileItemWriter";
	final static public String DELIMITED_MULTI_WRITER_TYPE = "DelimitedMultiWriter";
	final static public String FORMATTER_MULTI_RESOURCE_ITEM_WRITER_TYPE = "FormatterMultiResourceItemWriter";
	final static public String EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER_TYPE = "EgovFixedMultiResourceItemWriter";
	final static public String CUSTOMIZE_FILE_TYPE = "CustomizeFile";
	
	final static public String IBATIS_RW_TYPE = "IbatisRW";
	final static public String CUSTOMIZED_JDBC_CURSOR_ITEM_READER_TYPE = "CustomizedJdbcCursorItemReader";
	final static public String SQL_PAGING_QUERY_JDBC_ITEM_READER_TYPE = "SqlPagingQueryJdbcItemReader";
	final static public String SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER_TYPE = "SqlParameterJdbcBatchItemWriter";
	final static public String ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE = "ItemPreparedStatementJdbcBatchItemWriter";
	final static public String EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE = "EgovItemPreparedStatementJdbcBatchItemWriter";
	final static public String EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER_TYPE = "EgovCustomizedJdbcBatchItemWriter";
	final static public String CUSTOMIZE_DB_TYPE = "CustomizeDB";
}