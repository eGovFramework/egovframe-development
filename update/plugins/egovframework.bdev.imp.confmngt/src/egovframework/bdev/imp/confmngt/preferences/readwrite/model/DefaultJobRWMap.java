package egovframework.bdev.imp.confmngt.preferences.readwrite.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import egovframework.dev.imp.core.utils.StringUtil;
/**
 * 기본 Job Reader / Writer를 담는 Map
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
public class DefaultJobRWMap {
	/** 기본 Job RW의 File Type List */
	private HashMap<String, String> fileType = new HashMap<String, String>();
	
	/** 기본 Job RW의 DB Type List  */
	private HashMap<String, String> dbType = new HashMap<String, String>();
	
	/** Job RW의 Type 정보 List */
	private HashMap<String, String> detailType = new HashMap<String, String>();
	
	/** DefaultJobRWMap의 생성자 */
	public DefaultJobRWMap() { }
	
	/**
	 * FileType List 반환
	 * 
	 * @return
	 */
	public Set<String> getFileTypeList(){		
		return fileType.keySet();
	}
	
	/**
	 * DBType List 반환
	 * 
	 * @return
	 */
	public Set<String> getDBTypeList(){
		return dbType.keySet();
	}
	
	/**
	 * Item Type에 해당하는 Class Value 반환
	 * 
	 * @param itemType
	 * @return
	 */
	public String getClassValue(String itemType){
		String classValue = null;
		
		if(fileType.containsKey(itemType)){
			classValue = fileType.get(itemType);
		}else if(dbType.containsKey(itemType)){
			classValue = dbType.get(itemType);
		}

		return StringUtil.returnEmptyStringIfNull(classValue);
	}
	
	/**
	 * Item Type에 해당하는 Type 반환
	 * 
	 * @param itemType
	 * @return
	 */
	public String getDetailType(String itemType){
		String classValue = detailType.get(itemType);
		
		return StringUtil.returnEmptyStringIfNull(classValue);
	}
	
	/** 기본 값을 ReadType에 맞게 설정 */
	public void resetDefaultReadType(){
		fileType = getReadTypeFileMap();
		dbType = getReadTypeDBMap();
		
		detailType = getReadDetailType();
	}
	
	/** 기본 값을 WriterType에 맞게 설정 */
	public void resetDefaultWriterType(){
		fileType = getWriteTypeFileMap();
		dbType = getWriteTypeDBMap();
		
		detailType = getWriteDetailType();
	}
	
	/**
	 * File Type인지 확인
	 * 
	 * @param itemType
	 * @return
	 */
	public boolean isFileType(String itemType) {
		return fileType.containsKey(itemType);
	}
	
	/**
	 * ReadType File List 생성
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getReadTypeFileMap(){		
		LinkedHashMap<String, String> readTypeFileMap = new LinkedHashMap<String, String>();
		readTypeFileMap.put(DefaultJobRW.FIXED_FLAT_FILE_ITEM_READER, DefaultJobRW.FLAT_FILE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_READER, DefaultJobRW.FLAT_FILE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_READER, DefaultJobRW.FLAT_FILE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_READER, DefaultJobRW.FLAT_FILE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.FIXED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.MULTI_RESOURCE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.MULTI_RESOURCE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.MULTI_RESOURCE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.MULTI_RESOURCE_ITEM_READER_CLASS);
		readTypeFileMap.put(DefaultJobRW.CUSTOMIZE_FILE_READER, "");
		
		return readTypeFileMap;
	}
	
	/**
	 * ReadType DB List 생성
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getReadTypeDBMap(){
		LinkedHashMap<String, String> readTypeDBMap = new LinkedHashMap<String, String>();
		readTypeDBMap.put(DefaultJobRW.IBATIS_PAGING_ITEM_READER, DefaultJobRW.IBATIS_PAGING_ITEM_READER_CLASS);
		readTypeDBMap.put(DefaultJobRW.CUSTOMIZED_JDBC_CURSOR_ITEM_READER, DefaultJobRW.JDBC_CURSOR_ITEM_READER_CLASS);
		readTypeDBMap.put(DefaultJobRW.SQL_PAGING_QUERY_JDBC_ITEM_READER, DefaultJobRW.JDBC_PAGING_ITEM_READER_CLASS);
		readTypeDBMap.put(DefaultJobRW.CUSTOMIZE_DB_READER, "");
		
		return readTypeDBMap;
	}
	
	/**
	 * ReadType Detail Type 생성
	 * 
	 * @return
	 */
	private HashMap<String, String> getReadDetailType(){
		HashMap<String, String> readDetailType = new HashMap<String, String>();
		readDetailType.put(DefaultJobRW.FIXED_FLAT_FILE_ITEM_READER, DefaultJobRW.FIXED_READER_TYPE);
		readDetailType.put(DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_READER, DefaultJobRW.DELIMITED_READER_TYPE);
		readDetailType.put(DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_READER, DefaultJobRW.FIXED_READER_TYPE);
		readDetailType.put(DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_READER, DefaultJobRW.DELIMITED_READER_TYPE);
		readDetailType.put(DefaultJobRW.FIXED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.FIXED_READER_TYPE);
		readDetailType.put(DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.DELIMITED_READER_TYPE);
		readDetailType.put(DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.FIXED_READER_TYPE);
		readDetailType.put(DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_READER, DefaultJobRW.DELIMITED_READER_TYPE);
		readDetailType.put(DefaultJobRW.CUSTOMIZE_FILE_READER, DefaultJobRW.CUSTOMIZE_FILE_TYPE);
		
		readDetailType.put(DefaultJobRW.IBATIS_PAGING_ITEM_READER, DefaultJobRW.IBATIS_RW_TYPE);
		readDetailType.put(DefaultJobRW.CUSTOMIZED_JDBC_CURSOR_ITEM_READER, DefaultJobRW.CUSTOMIZED_JDBC_CURSOR_ITEM_READER_TYPE);
		readDetailType.put(DefaultJobRW.SQL_PAGING_QUERY_JDBC_ITEM_READER, DefaultJobRW.SQL_PAGING_QUERY_JDBC_ITEM_READER_TYPE);
		readDetailType.put(DefaultJobRW.CUSTOMIZE_DB_READER, DefaultJobRW.CUSTOMIZE_DB_TYPE);

		return readDetailType;
	}
	
	/**
	 * WriterType File List 생성
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getWriteTypeFileMap(){
		LinkedHashMap<String, String> writeTypeFileMap = new LinkedHashMap<String, String>();
		writeTypeFileMap.put(DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.FLAT_FILE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.FORMATTER_FLAT_FILE_ITEM_WRITER, DefaultJobRW.FLAT_FILE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.FLAT_FILE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.FLAT_FILE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.MULTI_RESOURCE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.FORMATTER_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.MULTI_RESOURCE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.MULTI_RESOURCE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.MULTI_RESOURCE_ITEM_WRITER_CLASS);
		writeTypeFileMap.put(DefaultJobRW.CUSTOMIZE_FILE_WRITER, "");
		
		return writeTypeFileMap;
	}
	
	/**
	 * WriterType DB List 생성
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getWriteTypeDBMap(){
		LinkedHashMap<String, String> writeTypeDBMap = new LinkedHashMap<String, String>();
		writeTypeDBMap.put(DefaultJobRW.IBATIS_BATCH_ITEM_WRITER, DefaultJobRW.IBATIS_BATCH_ITEM_WRITER_CLASS);
		writeTypeDBMap.put(DefaultJobRW.SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.JDBC_BATCH_ITEM_WRITER_CLASS);
		writeTypeDBMap.put(DefaultJobRW.ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.JDBC_BATCH_ITEM_WRITER_CLASS);
		writeTypeDBMap.put(DefaultJobRW.EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.EGOV_JDBC_BATCH_ITEM_WRITER_CLASS);
		writeTypeDBMap.put(DefaultJobRW.EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.EGOV_JDBC_BATCH_ITEM_WRITER_CLASS);
		writeTypeDBMap.put(DefaultJobRW.CUSTOMIZE_DB_WRITER, "");
		
		return writeTypeDBMap;
	}
	
	/**
	 * WriterType Detail Type List 생성
	 * 
	 * @return
	 */
	private HashMap<String, String> getWriteDetailType(){
		HashMap<String, String> writeDetailType = new HashMap<String, String>();
		
		writeDetailType.put(DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.DELIMITED_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.FORMATTER_FLAT_FILE_ITEM_WRITER, DefaultJobRW.FORMATTER_FLAT_FILE_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.DELIMITED_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_WRITER, DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.DELIMITED_MULTI_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.FORMATTER_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.FORMATTER_MULTI_RESOURCE_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.DELIMITED_MULTI_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER, DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.CUSTOMIZE_FILE_WRITER, DefaultJobRW.CUSTOMIZE_FILE_TYPE);
		
		writeDetailType.put(DefaultJobRW.IBATIS_BATCH_ITEM_WRITER, DefaultJobRW.IBATIS_RW_TYPE);
		writeDetailType.put(DefaultJobRW.SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER, DefaultJobRW.EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER_TYPE);
		writeDetailType.put(DefaultJobRW.CUSTOMIZE_DB_WRITER, DefaultJobRW.CUSTOMIZE_DB_TYPE);
		
		return writeDetailType;
	}
}
