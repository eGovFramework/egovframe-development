package pkg.service;

/**
 * @Class Name : Sample2VO.java
 * @Description : Sample2 VO class
 * @Modification Information
 *
 * @author 홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class Sample2VO extends Sample2DefaultVO{
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
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUseYn() {
        return this.useYn;
    }
    
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
    
    public String getRegUser() {
        return this.regUser;
    }
    
    public void setRegUser(String regUser) {
        this.regUser = regUser;
    }
    
}