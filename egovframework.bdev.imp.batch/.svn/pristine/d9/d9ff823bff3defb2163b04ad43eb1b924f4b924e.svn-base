package egovframework.bdev.imp.batch.wizards.jobcreation.util;

import java.util.Map;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobRWDetailInfoItem;
import egovframework.dev.imp.core.utils.NullUtil;
/**
 * File Job Reader / Writer Detail info의 Validation
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
public class JobRWFileDetailInfoValidationUtil extends JobRWDetailInfoValidationUtil {
	
	/**
	 * JobRWFileDetailInfoValidationUtil 생성자
	 * 
	 * @param detailContext
	 *
	 */
	public JobRWFileDetailInfoValidationUtil(Map<String, String> detailContext) {
		super(detailContext);
	}
	
	/**
	 * FixedReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateFixedReaderItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);		
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String columnLength = detailContext.get(JobRWDetailInfoItem.COLUMN_LENGTH);
		errorMessage = validateColumnLengthAndGetErrorMessage(columnLength);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String voClass = detailContext.get(JobRWDetailInfoItem.VO_CLASS);
		errorMessage = validateVOClassAndGetErrorMessage(voClass);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * DelimitedReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateDelimitedReaderItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String delimiter = detailContext.get(JobRWDetailInfoItem.DELIMITER);
		errorMessage = validateDelimiterAndGetErrorMessage(delimiter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String voClass = detailContext.get(JobRWDetailInfoItem.VO_CLASS);
		errorMessage = validateVOClassAndGetErrorMessage(voClass);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}

		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * DelimitedWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateDelimitedWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String delimiter = detailContext.get(JobRWDetailInfoItem.DELIMITER);
		errorMessage = validateDelimiterAndGetErrorMessage(delimiter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * FormatterFlatFileItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateFormatterFlatFileItemWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldFormat = detailContext.get(JobRWDetailInfoItem.FIELD_FORMAT);
		errorMessage = validateFieldFormatAndGetErrorMessage(fieldFormat);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * EgovFixedFlatFileItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateEgovFixedFlatFileItemWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldRange = detailContext.get(JobRWDetailInfoItem.FIELD_RANGE);
		errorMessage = validateFieldRangeAndGetErrorMessage(fieldRange);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * DelimitedMultiWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateDelimitedMultiWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String delimiter = detailContext.get(JobRWDetailInfoItem.DELIMITER);
		errorMessage = validateDelimiterAndGetErrorMessage(delimiter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String maxCountPerResource = detailContext.get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE);
		errorMessage = validateMaxCountPerResourceAndGetErrorMessage(maxCountPerResource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * FormatterMultiResourceItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateFormatterMultiResourceItemWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldFormat = detailContext.get(JobRWDetailInfoItem.FIELD_FORMAT);
		errorMessage = validateFieldFormatAndGetErrorMessage(fieldFormat);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String maxCountPerResource = detailContext.get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE);
		errorMessage = validateMaxCountPerResourceAndGetErrorMessage(maxCountPerResource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * EgovFixedMultiResourceItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validateEgovFixedMultiResourceItemWriterAndGetErrorMessage(){
		String errorMessage = "";
		
		String resource = detailContext.get(JobRWDetailInfoItem.RESOURCE);
		errorMessage = validateResourceAndGetErrorMessage(resource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}

		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldRange = detailContext.get(JobRWDetailInfoItem.FIELD_RANGE);
		errorMessage = validateFieldRangeAndGetErrorMessage(fieldRange);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String maxCountPerResource = detailContext.get(JobRWDetailInfoItem.MAX_COUNT_PER_RESOURCE);
		errorMessage = validateMaxCountPerResourceAndGetErrorMessage(maxCountPerResource);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * PartitionFixedReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validatePartitionFixedReaderAndGetErrorMessage(){
		String errorMessage = "";
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}

		String columnLength = detailContext.get(JobRWDetailInfoItem.COLUMN_LENGTH);
		errorMessage = validateColumnLengthAndGetErrorMessage(columnLength);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String voClass = detailContext.get(JobRWDetailInfoItem.VO_CLASS);
		errorMessage = validateVOClassAndGetErrorMessage(voClass);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * PartitionDelimitedReader 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validatePartitionDelimitedReaderAndGetErrorMessage(){
		String errorMessage = "";
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String delimiter = detailContext.get(JobRWDetailInfoItem.DELIMITER);
		errorMessage = validateDelimiterAndGetErrorMessage(delimiter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String voClass = detailContext.get(JobRWDetailInfoItem.VO_CLASS);
		errorMessage = validateVOClassAndGetErrorMessage(voClass);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * PartitionDelimitedWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validatePartitionDelimitedWriterAndGetErrorMessage(){
		String errorMessage = "";
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String delimiter = detailContext.get(JobRWDetailInfoItem.DELIMITER);
		errorMessage = validateDelimiterAndGetErrorMessage(delimiter);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * PartitionFormatterFlatFileItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validatePartitionFormatterFlatFileItemWriterAndGetErrorMessage(){
		String errorMessage = "";
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldFormat = detailContext.get(JobRWDetailInfoItem.FIELD_FORMAT);
		errorMessage = validateFieldFormatAndGetErrorMessage(fieldFormat);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * PartitionEgovFixedFlatFileItemWriter 유효성 검사 및 Error Message 생성
	 * 
	 * @return
	 */
	public String validatePartitionEgovFixedFlatFileItemWriterItemAndGetErrorMessage(){
		String errorMessage = "";
		
		String fieldName = detailContext.get(JobRWDetailInfoItem.FIELD_NAME);
		errorMessage = validateFieldNameAndGetErrorMessage(fieldName);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		String fieldRange = detailContext.get(JobRWDetailInfoItem.FIELD_RANGE);
		errorMessage = validateFieldRangeAndGetErrorMessage(fieldRange);
		if(!NullUtil.isEmpty(errorMessage)){
			return errorMessage;
		}
		
		return NO_ERROR_MESSAGE;
	}

}
