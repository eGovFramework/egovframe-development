package egovframework.bdev.imp.batch.wizards.jobcreation.util;

import java.util.Map;
import java.util.regex.Pattern;

import egovframework.bdev.imp.batch.common.BatchMessages;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.StringUtil;
/**
 * Job Reader / Writer의 Detail info의 기본 Validation 단위
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
public class JobRWDetailInfoValidationUtil {
	/** Error Message가 없는 경우 String*/
	final protected String NO_ERROR_MESSAGE = "";

	/** Job RW Detail Info에서 입력한  */
	protected Map<String, String> detailContext = null;

	/**
	 * JobRWDetailInfoValidationUtil 생성자
	 * 
	 * @param detailContext
	 *
	 */
	protected JobRWDetailInfoValidationUtil(Map<String, String> detailContext) {
		this.detailContext = detailContext;
	}

	/**
	 * Resource 유효성 검사 및 Error Message 생성
	 * 
	 * @param resource
	 * @return
	 */
	protected String validateResourceAndGetErrorMessage(String resource) {
		if (NullUtil.isEmpty(resource)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_RESOURCE;
		}

		final String signals = "\\*" + "\\." + "\\/" + "\\#" + "\\{" + "\\}" + "\\[" + "\\]" + "\\-" + "\\_" + "\\:";
		final String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN
				+ signals;

		if (StringUtil.doesStringMatchWithPatten(pattern, resource)) {
			if (!StringUtil.isSignalsFirstOrLast(signals, resource)) {
				return NO_ERROR_MESSAGE;
			}
		}

		return BatchMessages.JobRWDetailInfoValidation_INVALID_RESOURCE;
	}

	/**
	 * FieldName 유효성 검사 및 Error Message 생성
	 * 
	 * @param fieldName
	 * @return
	 */
	protected String validateFieldNameAndGetErrorMessage(String fieldName) {
		String noEmptySpaceFieldName = getNoEmptySpaceString(fieldName);
		
		if (NullUtil.isEmpty(noEmptySpaceFieldName)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_FIELD_NAME;
		}

		final String signals = "," + "\\-" + "\\_";
		final String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN
				+ signals;

		if (StringUtil.doesStringMatchWithPatten(pattern, noEmptySpaceFieldName)) {
			if (!StringUtil.isSignalsFirstOrLast(signals, noEmptySpaceFieldName)) {
				return NO_ERROR_MESSAGE;
			}
		}

		return BatchMessages.JobRWDetailInfoValidation_INVALID_FIELD_NAME;
	}

	/**
	 * FieldFormat 유효성 검사 및 Error Message 생성
	 * 
	 * @param fieldFormat
	 * @return
	 */
	protected String validateFieldFormatAndGetErrorMessage(String fieldFormat) {
		if (NullUtil.isEmpty(fieldFormat)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_FIELD_FORMAT;
		}

		if (StringUtil.hasKorean(fieldFormat)) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_FIELD_FORMAT;
		} else {
			return NO_ERROR_MESSAGE;
		}
	}

	/**
	 * ColumnLength 유효성 검사 및 Error Message 생성
	 * 
	 * @param columnLength
	 * @return
	 */
	protected String validateColumnLengthAndGetErrorMessage(String columnLength) {
		String noEmptySpaceColumnLength = getNoEmptySpaceString(columnLength);
		
		if (NullUtil.isEmpty(noEmptySpaceColumnLength)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_COLUMN_LENGTH;
		}

		String pattern = "([0-9]+-[0-9]*)(,[0-9]+-[0-9]*)*";
		
		if(Pattern.matches(pattern, noEmptySpaceColumnLength)){
			return NO_ERROR_MESSAGE;
		}else{
			return BatchMessages.JobRWDetailInfoValidation_INVALID_COLUMN_LENGTH;
		}
	}

	/**
	 * VOClass 유효성 검사 및 Error Message 생성
	 * 
	 * @param voClass
	 * @return
	 */
	protected String validateVOClassAndGetErrorMessage(String voClass) {
		if (NullUtil.isEmpty(voClass)) {
			return BatchMessages.JobRWDetailInfoValidation_EMTPY_VO_CLASS;
		}

		if (StringUtil.hasKorean(voClass)
				|| StringUtil.hasInvalidClassFileSignal(voClass)
				|| StringUtil.hasEmptySpace(voClass)) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_VO_CLASS;
		} else {
			return NO_ERROR_MESSAGE;
		}
	}

	/**
	 * Delimiter 유효성 검사 및 Error Message 생성
	 * 
	 * @param delimiter
	 * @return
	 */
	protected String validateDelimiterAndGetErrorMessage(String delimiter) {
		if (NullUtil.isEmpty(delimiter)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_DELIMITER;
		}

		String pattern = StringUtil.KOR_PATTERN + StringUtil.ENG_PATTERN
				+ StringUtil.NUM_PATTERN;

		if (StringUtil.doesCharacterOfStringBelongToPatternAtleastOne(pattern,
				delimiter) || StringUtil.hasEmptySpace(delimiter)) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_DELIMITER;
		} else {
			return NO_ERROR_MESSAGE;
		}
	}

	/**
	 * FieldRange 유효성 검사 및 Error Message 생성
	 * 
	 * @param columnRange
	 * @return
	 */
	protected String validateFieldRangeAndGetErrorMessage(String columnRange) {
		String noEmptySpaceColumnRange = getNoEmptySpaceString(columnRange);
		
		if (NullUtil.isEmpty(noEmptySpaceColumnRange)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_FIELD_RANGE;
		}

		char signal = ',';
		String pattern = StringUtil.NUM_PATTERN + signal;

		if (StringUtil.doesStringMatchWithPatten(pattern, noEmptySpaceColumnRange)) {
			return NO_ERROR_MESSAGE;
		}

		return BatchMessages.JobRWDetailInfoValidation_INVALID_FIELD_RANGE;
	}

	/**
	 * MaxCountPerResource 유효성 검사 및 Error Message 생성
	 * 
	 * @param maxCountPerResource
	 * @return
	 */
	protected String validateMaxCountPerResourceAndGetErrorMessage(
			String maxCountPerResource) {
		if (NullUtil.isEmpty(maxCountPerResource)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_MAX_COUNT_PER_RESOURCE;
		}

		try {
			int maxCountPerResourceInteger = Integer
					.parseInt(maxCountPerResource);

			if (maxCountPerResourceInteger > 0) {
				return NO_ERROR_MESSAGE;
			} else {
				return BatchMessages.JobRWDetailInfoValidation_INVALID_MAX_COUNT_PER_RESOURCE;
			}

		} catch (Exception e) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_MAX_COUNT_PER_RESOURCE;
		}
	}

	/**
	 * IBatisStatement 유효성 검사 및 Error Message 생성
	 * 
	 * @param iBatisStatement
	 * @return
	 */
	protected String validateIBatisStatementAndGetErrorMessage(
			String iBatisStatement) {
		if (NullUtil.isEmpty(iBatisStatement)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_IBATIS_STATEMENT;
		}

		if (StringUtil.isBatchJobBeanIDAvailable(iBatisStatement)) {
			return NO_ERROR_MESSAGE;
		} else {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_IBATIS_STATEMENT;
		}
	}

	/**
	 * ConfigurationFile 유효성 검사 및 Error Message 생성
	 * 
	 * @param configurationFile
	 * @return
	 */
	protected String validateConfigurationFileAndGetErrorMessage(
			String configurationFile) {
		if (NullUtil.isEmpty(configurationFile)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_CONFIGURATION_FILE;
		}

		final String signals = "\\*" + "\\." + "\\/" + "\\#" + "\\{" + "\\}" + "\\[" + "\\]" + "\\-" + "\\_" + "\\:";
		final String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN
				+ signals;

		if (StringUtil.doesStringMatchWithPatten(pattern, configurationFile)) {
			if (!StringUtil.isSignalsFirstOrLast(signals, configurationFile)) {
				return NO_ERROR_MESSAGE;
			}
		}
		
		return BatchMessages.JobRWDetailInfoValidation_INVALID_CONFIGURATION_FILE;

	}

	/**
	 * Sql관련 값 유효성 확인
	 * 
	 * @param sql
	 * @return
	 */
	protected boolean validateSqlRelateValue(String sql) {
		String invalidSignals = "~`@$^{}[];" + "\\";

		if (!StringUtil.doesStringHasSignalsAtLeastOneCharacter(invalidSignals,
				sql)) {
			char firstCharacter = sql.charAt(0);

			int lastIndex = sql.length() - 1;
			char lastCharacter = sql.charAt(lastIndex);

			String pattern = StringUtil.ENG_PATTERN + StringUtil.KOR_PATTERN
					+ StringUtil.NUM_PATTERN;
			if (StringUtil
					.doesCharacterBelongToPattern(pattern, firstCharacter)
					&& StringUtil.doesCharacterBelongToPattern(pattern,
							lastCharacter)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * SqlUpdate 유효성 검사 및 Error Message 생성
	 * 
	 * @param sqlUpdate
	 * @return
	 */
	protected String validateSqlUpdate(String sqlUpdate){
		if (NullUtil.isEmpty(sqlUpdate)) {
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL_UPDATE;
			
		}

		if(!isSqlUpdateAndInsertValid(sqlUpdate)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_UPDATE;
			
		}
		
		return NO_ERROR_MESSAGE;
	}
	
	/**
	 * SqlUpdate 유효성 검사 및 Error Message 생성
	 * 
	 * @param sql
	 * @return
	 */
	private boolean isSqlUpdateAndInsertValid(String sql){
		String invalidSignals = "~`@$^{}[];" + "\\";

		if (StringUtil.doesStringHasSignalsAtLeastOneCharacter(invalidSignals,
				sql)) {
			return false;
		}

		return true;
	}
	
	/**
	 * SqlInsert 유효성 검사 및 Error Message 생성
	 * 
	 * @param sqlInsert
	 * @return
	 */
	protected String validateSqlInsert(String sqlInsert){
		if (NullUtil.isEmpty(sqlInsert)) {
			return BatchMessages.JobRWDBDetailInfoValidation_EMPTY_SQL_INSERT;
			
		}

		if(!isSqlUpdateAndInsertValid(sqlInsert)){
			return BatchMessages.JobRWDBDetailInfoValidation_INVALID_SQL_INSERT;
			
		}
		
		return NO_ERROR_MESSAGE;
	}

	/**
	 * RowMapper 유효성 검사 및 Error Message 생성
	 * 
	 * @param rowMapper
	 * @return
	 */
	protected String validateRowMapperAndGetErrorMessage(String rowMapper) {
		if (NullUtil.isEmpty(rowMapper)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_ROW_MAPPER;
		}

		if (StringUtil.hasInvalidClassFileSignal(rowMapper)
				|| StringUtil.hasKorean(rowMapper)
				|| StringUtil.hasEmptySpace(rowMapper)) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_ROW_MAPPER;
		} else {
			return NO_ERROR_MESSAGE;
		}
	}

	/**
	 * PageSize 유효성 검사 및 Error Message 생성
	 * 
	 * @param pageSize
	 * @return
	 */
	protected String validatePageSizeAndGetErrorMessage(String pageSize) {
		if (NullUtil.isEmpty(pageSize)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_PAGE_SIZE;
		}

		try {
			int maxCountPerResourceInteger = Integer.parseInt(pageSize);

			if (maxCountPerResourceInteger > 0) {
				return NO_ERROR_MESSAGE;
			} else {
				return BatchMessages.JobRWDetailInfoValidation_INVALID_PAGE_SIZE;
			}

		} catch (Exception e) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_PAGE_SIZE;
		}
	}
	
	/**
	 * Params 유효성 검사 및 Error Message 생성
	 * 
	 * @param params
	 * @return
	 */
	protected String validateParamsAndGetErrorMessage(String params) {
		String noEmptySpaceParams = getNoEmptySpaceString(params);
		
		if (NullUtil.isEmpty(noEmptySpaceParams)) {
			return BatchMessages.JobRWDetailInfoValidation_EMTPY_PARAMS;
		}

		final String signals = ","; //$NON-NLS-1$
		final String pattern = StringUtil.ENG_PATTERN + StringUtil.NUM_PATTERN
				+ signals;

		if (StringUtil.doesStringMatchWithPatten(pattern, noEmptySpaceParams)) {
			if (!StringUtil.isSignalsFirstOrLast(signals, noEmptySpaceParams)) {
				return NO_ERROR_MESSAGE;
			}
		}

		return BatchMessages.JobRWDetailInfoValidation_INVALID_PARAMS;
	}

	/**
	 * RowSetter 유효성 검사 및 Error Message 생성
	 * 
	 * @param rowSetter
	 * @return
	 */
	protected String validateRowSetterAndGetErrorMessage(String rowSetter) {
		if (NullUtil.isEmpty(rowSetter)) {
			return BatchMessages.JobRWDetailInfoValidation_EMPTY_ROW_SETTER;
		}

		if (StringUtil.hasInvalidClassFileSignal(rowSetter)
				|| StringUtil.hasKorean(rowSetter)
				|| StringUtil.hasEmptySpace(rowSetter)) {
			return BatchMessages.JobRWDetailInfoValidation_INVALID_ROW_SETTER;
		} else {
			return NO_ERROR_MESSAGE;
		}
	}
	
	/**
	 * String에서 빈칸 모두 제거
	 * 
	 * @param originalString
	 * @return
	 */
	private String getNoEmptySpaceString(String originalString){
		if(NullUtil.isEmpty(originalString)){
			return originalString;
		}else{
			return originalString.replaceAll(" ", ""); //$NON-NLS-2$
		}
	}
	
}
