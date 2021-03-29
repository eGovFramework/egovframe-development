/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.mdev.imp.ide.wizards.model;

/**
 * Moblie eGovFramework 신규 웹 프로젝트 컨택스트 클래스
 * @author 이종대
 * @since 2011.07.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2011.07.13  	이종대          최초 생성
 *
 * 
 * </pre>
 */
public class NewMobileWebProjectContext extends NewMobileProjectContext {

    /** 서블릿 버젼 */
    private String servletVersion;
    /** 서버 런타임 명 */
    private String runtimeName;

    // get runtimeName
    public String getRuntimeName() {
        return runtimeName;
    }

    // set runtimeName
    public void setRuntimeName(String runtimeName) {
        this.runtimeName = runtimeName;
    }

    // get servletVersion
    public String getServletVersion() {
        return servletVersion;
    }

    // get servletVersion
    public void setServletVersion(String servletVersion) {
        this.servletVersion = servletVersion;
    }

}
