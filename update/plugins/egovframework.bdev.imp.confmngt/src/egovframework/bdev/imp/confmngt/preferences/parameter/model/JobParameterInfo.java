package egovframework.bdev.imp.confmngt.preferences.parameter.model;

import egovframework.bdev.imp.confmngt.preferences.com.model.Info;

/**
 * JobParameterContents에 사용되는 VO객체
 * 
 * @since 2012.07.24
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class JobParameterInfo extends Info {	
	/** Job Parameter의 Paramaeter Name*/
	private String parameterName;
	
	/** Job Parameter의 Value */
	private String value;
	
	/** Job Parameter의 Data Type */
	private String dataType;
	
	/** Job Parameter의 Date Format */
	private String dateFormat = null;

	/**
	 * parameterName의 값을 가져온다
	 *
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * parameterName의 값을 설정한다.
	 *
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * value의 값을 가져온다
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * value의 값을 설정한다.
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * dataType의 값을 가져온다
	 *
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * dataType의 값을 설정한다.
	 *
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * dateFormat의 값을 가져온다
	 *
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * dateFormat의 값을 설정한다.
	 *
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * JobParameterInfo의 생성자
	 * 
	 * @param parameterName
	 * @param value
	 * @param dataType
	 */
	public JobParameterInfo(String parameterName, String value, String dataType){
		this.parameterName = parameterName;
		this.value = value;
		this.dataType = dataType;
		
	}
	
	/** JobParameterInfo의 생성자 */
	public JobParameterInfo(){
	}
	
	/**
	 * Parameter로 넘어온 JobParameterInfo의 값들을 복사해 온다.
	 * 
	 * @param source
	 */
	public void copyValues(JobParameterInfo source){
		parameterName = source.getParameterName();
		value = source.getValue();
		dataType = source.getDataType();
		dateFormat = source.getDateFormat();
	}
}
