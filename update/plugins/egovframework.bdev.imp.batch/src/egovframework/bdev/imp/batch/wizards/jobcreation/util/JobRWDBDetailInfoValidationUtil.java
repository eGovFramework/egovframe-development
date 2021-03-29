package egovframework.bdev.imp.batch.wizards.jobcreation.util;

import java.util.Map;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobRWDetailInfoItem;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * DB Job Reader / Writer Detail info의 Validation
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
public class JobRWDBDetailInfoValidationUtil extends JobRWDetailInfoValidationUtil {

	/**
	 * JobRWDBDetailInfoValidationUtil 생성자
	 * 
	 * @param detailContext
	 *
	 */
	public JobRWDBDetailInfoValidationUtil(Map<String, String> detailContext) {
		super(detailContext);
	}
	
	/**
	 * IbatisRW 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateIbatisRWAndGetErrorMessage(){
		String errorMessage = "";
		
		String iBatisStatement = detailContext.get(JobRWDetailInfoItem.IBATIS_STATEMENT);
		errorMessage = validateIBatisStatementAndGetErrorMessage(iBatisStatement);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String configurationFile = detailContext.get(JobRWDetailInfoItem.CONFIGURATION_FILE);
		errorMessage = validateConfigurationFileAndGetErrorMessage(configurationFile);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * CustomizedJdbcCursorItemReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateCustomizedJdbcCursorItemReaderAndGetErrorMessage(){
		String errorMessage = ""; //$NON-NLS-1$
		
		String sql = detailContext.get(JobRWDetailInfoItem.SQL);
		if(NullUtil.isEmpty(sql)){
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL;
		}else if(!validateSqlRelateValue(sql)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL;
		}

		String rowMapper = detailContext.get(JobRWDetailInfoItem.ROW_MAPPER);
		errorMessage = validateRowMapperAndGetErrorMessage(rowMapper);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * SqlPagingQueryJdbcItemReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateSqlPagingQueryJdbcItemReaderAndGetErrorMessage(){
		String errorMessage = ""; //$NON-NLS-1$
		
		String rowMapper = detailContext.get(JobRWDetailInfoItem.ROW_MAPPER);
		errorMessage = validateRowMapperAndGetErrorMessage(rowMapper);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String pageSize = detailContext.get(JobRWDetailInfoItem.PAGE_SIZE);
		errorMessage = validatePageSizeAndGetErrorMessage(pageSize);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String sqlSortColumn = detailContext.get(JobRWDetailInfoItem.SQL_SORT_COLUMN);
		if(NullUtil.isEmpty(sqlSortColumn)){
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL_SORT_COLUMN;
			
		}else if(!validateSqlRelateValue(sqlSortColumn)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_SORT_COLUMN;
			
		}
		
		String sqlSelectText = detailContext.get(JobRWDetailInfoItem.SQL_SELECT);
		if(NullUtil.isEmpty(sqlSelectText)){
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL_SELECT;
			
		}else if(!validateSqlRelateValue(sqlSelectText)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_SELECT;
			
		}
		
		String sqlFromText = detailContext.get(JobRWDetailInfoItem.SQL_FROM);
		if(NullUtil.isEmpty(sqlFromText)){
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL_FROM;
			
		}else if(!validateSqlRelateValue(sqlFromText)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_FROM;
			
		}
		
		String sqlWhereText = detailContext.get(JobRWDetailInfoItem.SQL_WHERE);
		if(!NullUtil.isEmpty(sqlWhereText) && !validateSqlRelateValue(sqlWhereText)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_WHERE;
			
		}

		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * SqlParameterJdbcBatchItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateSqlParameterJdbcBatchItemWriterAndGetErrorMessage(){
		
		String sqlUpdate = detailContext.get(JobRWDetailInfoItem.SQL_UPDATE);
		
		return validateSqlUpdate(sqlUpdate);
	}
	
	/**
	 * ItemPreparedStatementJdbcBatchItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage(){
		String errorMessage = "";
		
		String sqlInsert = detailContext.get(JobRWDetailInfoItem.SQL_INSERT);
		errorMessage = validateSqlInsert(sqlInsert);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String rowSetter = detailContext.get(JobRWDetailInfoItem.ROW_SETTER);
		errorMessage = validateRowSetterAndGetErrorMessage(rowSetter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * EgovItemPreparedStatementJdbcBatchItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateEgovItemPreparedStatementJdbcBatchItemWriterAndGetErrorMessage(){
		String errorMessage = ""; //$NON-NLS-1$
		
		String sqlInsert = detailContext.get(JobRWDetailInfoItem.SQL_INSERT);
		errorMessage = validateSqlInsert(sqlInsert);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String params = detailContext.get(JobRWDetailInfoItem.PARAMS);
		errorMessage = validateParamsAndGetErrorMessage(params);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * EgovCustomizedJdbcBatchItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateEgovCustomizedJdbcBatchItemWriterAndGetErrorMessage(){
		String errorMessage = ""; //$NON-NLS-1$
		
		String sqlInsert = detailContext.get(JobRWDetailInfoItem.SQL_INSERT);
		errorMessage = validateSqlInsert(sqlInsert);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String rowSetter = detailContext.get(JobRWDetailInfoItem.ROW_SETTER);
		errorMessage = validateRowSetterAndGetErrorMessage(rowSetter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}

}
