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
package egovframework.mdev.imp.ide.wizards.examples;

import java.util.ResourceBundle;
import java.util.StringTokenizer;

import egovframework.dev.imp.core.utils.StringUtil;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;

/**
 * Moblie eGovFramework 예제 정보 클래스
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
 * </pre>
 */
public class MobileExampleInfo {
    /** 부모 POM파일 */
    public static String parentPomFile;
    /** 웹 프로젝트 POM 파일 */
    public static String webPomFile;
    /** all-in-one POM 파일 */
    public static String allInOnePomFile;
    
    
    /** 기본 프로젝트 예제 */
    public static String defaultCoreExample;
    
    /** 웹 프로젝트 예제 */
    public static String defaultWebExample;
    /** 기본 프로젝트 추가 예제 */
    public static String[] optionalCoreExample;
    /** 웹 프로젝트 추가 예제 */
    public static String[] optionalWebExample;
    /** all-in-one  템플릿 */
    public static String templateAllInOneExample;

    /**
     * 리소스 번들로 부터 속성 값 읽어오기
     */
    static {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("egovframework.mdev.imp.ide.wizards.examples.examples");
            
            parentPomFile = resource.getString("pom");
            webPomFile = resource.getString("pom.web");
            allInOnePomFile = resource.getString("pom.all-in-one");

            defaultCoreExample = resource.getString("example.core.default");
            defaultWebExample = resource.getString("example.web.default");
            templateAllInOneExample = resource.getString("example.allinone");

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
            MoblieIdeLog.logError(ex);
        }

    }
}
