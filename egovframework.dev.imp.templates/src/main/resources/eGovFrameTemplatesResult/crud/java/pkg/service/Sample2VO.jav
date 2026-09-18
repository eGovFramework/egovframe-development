package pkg.service;

/**
 * Sample2VO VO class
 *
 * @author 홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see
 *
 *      <pre>
 *  == 개정이력(Modification Information) ==
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   실행환경 개발팀  홍길동          최초 생성
 *
 *      </pre>
 */
public class Sample2VO extends Sample2DefaultVO {

	private static final long serialVersionUID = 1L;

	/** ID */
	private String id;

	/** NAME */
	private String name;

	/** DESCRIPTION */
	private String description;

	/** USE_YN */
	private String useYn;

	/** REG_USER */
	private String regUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getRegUser() {
		return regUser;
	}

	public void setRegUser(String regUser) {
		this.regUser = regUser;
	}

}
