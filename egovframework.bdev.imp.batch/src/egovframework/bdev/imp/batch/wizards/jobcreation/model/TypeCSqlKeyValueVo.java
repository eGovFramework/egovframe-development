package egovframework.bdev.imp.batch.wizards.jobcreation.model;
/**
 * SqlPagingQueryJdbcItemReader의 Context에 저장되는 Key, Value를 가지는 VO
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
public class TypeCSqlKeyValueVo {

	/** Key 값*/
	public String key = null;
	
	/** Value 값 */
	public String value = null;
	
	/**
	 * TypeCSqlKeyValueVo 생성자
	 * 
	 * @param key
	 * @param value
	 *
	 */
	public TypeCSqlKeyValueVo(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Parameter로 넘어온TypeCSqlKeyValueVo의 값들을 복사해 온다.
	 * 
	 * @param originalVo
	 */
	public void copyValues(TypeCSqlKeyValueVo originalVo){
		this.key = originalVo.key;
		this.value = originalVo.value;
	}
	
	/** 현재 TypeCSqlKeyValueVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public TypeCSqlKeyValueVo clone(){
		TypeCSqlKeyValueVo cloneVo = new TypeCSqlKeyValueVo(this.key, this.value);
		
		return cloneVo;
	}

}
