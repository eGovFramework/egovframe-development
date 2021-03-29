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
package egovframework.dev.imp.ide.wizards.examples;

import java.util.ResourceBundle;
import java.util.StringTokenizer;

import egovframework.dev.imp.core.utils.StringUtil;
import egovframework.dev.imp.ide.common.IdeLog;

/**
 * 예제 정보 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  이흥주          최초 생성
 * 
 * 
 * </pre>
 */
public class ExampleInfo {
    /** 부모 POM파일 */
    //public static String parentPomFile;
    /** 기본프로젝트 POM 파일 */
    public static String corePomFile;
    /** 웹 프로젝트 POM 파일 */
    public static String webPomFile;
    /** 포털 프로젝트 POM 파일 */
    //public static String portalPomFile;
    /** 업무 프로젝트 POM 파일 */
    //public static String enterprisePomFile;
    /** 단순홈페이지 프로젝트 POM 파일 */
    //public static String simplePomFile;
    /** 공통컴포넌트 all-in-one POM 파일 */
    //public static String commonPomFile;
    
    /** 기본 프로젝트 예제 */
    public static String defaultCoreExample;
    /** 웹 프로젝트 예제 */
    public static String defaultWebExample;
    /** 기본 프로젝트 추가 예제 */
    public static String[] optionalCoreExample;
    /** 웹 프로젝트 추가 예제 */
    public static String[] optionalWebExample;
    /** 템플릿 포털 예제*/
    public static String templatePortalExample;
    /** 엔터프라이즈 시스템 포털 예제*/
    public static String templateEnterpriseExample;
    /** SIMPLE SITE 포털 예제*/
    public static String templateSimpleExample;
    /** 공통컴포넌트 all-in-one 예제*/
    public static String templateCommonAllInOneExample;
    
    /**
     * 리소스 번들로 부터 속성 값 읽어오기
     */
    static {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("egovframework.dev.imp.ide.wizards.examples.examples");
            
            //parentPomFile = resource.getString("pom");
            corePomFile = resource.getString("pom.core");
            webPomFile = resource.getString("pom.web");
            //portalPomFile = resource.getString("pom.portal");
            //enterprisePomFile = resource.getString("pom.enterprise");
            //simplePomFile = resource.getString("pom.simple");
            //commonPomFile = resource.getString("pom.common");
                        
            defaultCoreExample = resource.getString("example.core.default");
            defaultWebExample = resource.getString("example.web.default");
            templatePortalExample = resource.getString("example.template.portal");
            templateEnterpriseExample = resource.getString("example.template.enterprise");
            templateSimpleExample = resource.getString("example.template.simple");
            templateCommonAllInOneExample = resource.getString("example.template.common");            

            String coreOption = resource.getString("example.core.optional");
            if (!"".equals(StringUtil.nvl(coreOption))) {
                StringTokenizer tokens = new StringTokenizer(coreOption, ",");
                optionalCoreExample = new String[tokens.countTokens()];
                for (int i = 0; tokens.hasMoreElements(); i++) {
                    optionalCoreExample[i] = tokens.nextToken();
                }
            } else {
                optionalCoreExample = new String[0];
            }

            String webOption = resource.getString("example.web.optional");
            if (!"".equals(StringUtil.nvl(webOption))) {
                StringTokenizer tokens = new StringTokenizer(webOption, ",");
                optionalWebExample = new String[tokens.countTokens()];
                for (int i = 0; tokens.hasMoreElements(); i++) {
                    optionalWebExample[i] = tokens.nextToken();
                }
            } else {
                optionalWebExample = new String[0];
            }
        } catch (Exception ex) {
            IdeLog.logError(ex);
        }

    }
}
