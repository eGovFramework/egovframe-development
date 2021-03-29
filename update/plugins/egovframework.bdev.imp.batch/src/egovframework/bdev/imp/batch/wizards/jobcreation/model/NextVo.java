package egovframework.bdev.imp.batch.wizards.jobcreation.model;
/**
 * Next On, Next To의 정보를 저장하는 VO
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
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
public class NextVo {
	/** Decision의 Next On */
	private String nextOn = null;
	
	/** Decision의 Next To */
	private String nextTo = null;
	
	/** NextVo의 생성자 */
	public NextVo() { }

	/**
	 *  NextVo의 생성자
	 * @param nextOn
	 * @param nextTo
	 *
	 */
	public NextVo(String nextOn, String nextTo) {
		super();
		this.nextOn = nextOn;
		this.nextTo = nextTo;
	}

	/**
	 * nextOn의 값을 가져온다
	 *
	 * @return the nextOn
	 */
	public String getNextOn() {
		return nextOn;
	}

	/**
	 * nextOn의 값을 설정한다.
	 *
	 * @param nextOn
	 */
	public void setNextOn(String nextOn) {
		this.nextOn = nextOn;
	}

	/**
	 * nextTo의 값을 가져온다
	 *
	 * @return the nextTo
	 */
	public String getNextTo() {
		return nextTo;
	}

	/**
	 * nextTo의 값을 설정한다.
	 *
	 * @param nextTo
	 */
	public void setNextTo(String nextTo) {
		this.nextTo = nextTo;
	}
	
	/**
	 * <pre>
	 *  넘어온 NextVo와 비교해서 
	 *  next on, to가 같으면 TRUE 다르면 FALSE를 return
	 * </pre>
	 * 
	 * @param compareNextVo
	 * @return result
	 */
	public boolean compare(NextVo compareNextVo){		
		if(getNextOn().equals(compareNextVo.getNextOn()) && getNextTo().equals(compareNextVo.getNextTo())){
			return true;
		}else{
			return false;
		}
	}

	/** 현재 NextVo와 똑같은 값을 가지는 Vo를 새로 생성한다. */
	public NextVo clone(){
		NextVo clone = new NextVo();
		
		clone.setNextOn(nextOn);
		clone.setNextTo(nextTo);
		
		return clone;
	}
}
