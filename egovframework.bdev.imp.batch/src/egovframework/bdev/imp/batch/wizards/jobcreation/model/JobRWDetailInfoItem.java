package egovframework.bdev.imp.batch.wizards.jobcreation.model;
/**
 * Job Reader / Writer의 Context Map key
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
public class JobRWDetailInfoItem {

	final static public String RESOURCE = "Resource";
	final static public String FIELD_NAME = "FieldName";
	final static public String COLUMN_LENGTH = "ColumnLength";
	final static public String DELIMITER = "Delimiter";
	final static public String VO_CLASS = "VOClass";
	final static public String FIELD_FORMAT = "FieldFormat";
	final static public String FIELD_RANGE = "FieldRange";
	final static public String MAX_COUNT_PER_RESOURCE = "MaxCountperResource";
	

	final static public String IBATIS_STATEMENT = "iBatisStatement";
	final static public String CONFIGURATION_FILE = "ConfigurationFile";
	final static public String SQL = "Sql";
	final static public String ROW_MAPPER = "RowMapper";
	final static public String PAGE_SIZE = "PageSize";
	final static public String SQL_SORT_COLUMN = "Sql-SortColumn";
	final static public String SQL_SELECT = "Sql-SELECT";
	final static public String SQL_FROM = "Sql-FROM";
	final static public String SQL_WHERE = "Sql-WHERE";
	final static public String SQL_UPDATE = "Sql-Update";
	final static public String SQL_INSERT = "Sql-Insert";
	final static public String PARAMS = "Params";
	final static public String ROW_SETTER = "RowSetter";
	final static public String DATABASE_TYPE = "DatabaseType";
	final static public String DATASOURCE_BEAN_ID = "DatasourceBeanID";
}
