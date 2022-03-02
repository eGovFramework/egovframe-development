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
package egovframework.boot.dev.imp.ide.wizards.examples;

import java.util.ResourceBundle;
import java.util.StringTokenizer;

import egovframework.boot.dev.imp.ide.common.BootIdeLog;
import egovframework.dev.imp.core.utils.StringUtil;

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
	
	/** CORE 프로젝트 POM 파일 */
    public static String bootCorePomFile;
    /** WEB 프로젝트 POM 파일 */
    public static String bootWebPomFile;
    
    /** CORE 부트 프로젝트 예제 */
    public static String defaultBootCoreExample;
    /** CORE 부트 프로젝트 추가 예제 */
    public static String[] optionalBootCoreExample;
    /** WEB 부트 프로젝트 예제 */
    public static String defaultBootWebExample;
    /** WEB 부트 프로젝트 추가 예제 */
    public static String[] optionalBootWebExample;

    /** SIMPLE SITE 포털 예제*/
    public static String templateSimpleExample;

    /** MSA 예제*/
    public static String templateMSAExample1;
    public static String templateMSAExample2;
    public static String templateMSAExample3;
    public static String templateMSAExample4;
    public static String templateMSAExample5;
    public static String templateMSAExample6;
    public static String templateMSAExample7;
    public static String templateMSAExample8;
    public static String templateMSAExample9;
    
    /**
     * 리소스 번들로 부터 속성 값 읽어오기
     */
    static {
        try {
            ResourceBundle resource = ResourceBundle.getBundle("egovframework.boot.dev.imp.ide.wizards.examples.examples");

            bootCorePomFile = resource.getString("pom.boot.core");
            bootWebPomFile = resource.getString("pom.boot.web");

            defaultBootCoreExample = resource.getString("example.boot.core.default");
            defaultBootWebExample = resource.getString("example.boot.web.default");

            String bootCoreOption = resource.getString("example.boot.core.optional");
            if (!"".equals(StringUtil.nvl(bootCoreOption))) {
                StringTokenizer tokens = new StringTokenizer(bootCoreOption, ",");
                optionalBootCoreExample = new String[tokens.countTokens()];
                for (int i = 0; tokens.hasMoreElements(); i++) {
                	optionalBootCoreExample[i] = tokens.nextToken();
                }
            } else {
            	optionalBootCoreExample = new String[0];
            }

            String bootWebOption = resource.getString("example.boot.web.optional");
            if (!"".equals(StringUtil.nvl(bootWebOption))) {
                StringTokenizer tokens = new StringTokenizer(bootWebOption, ",");
                optionalBootWebExample = new String[tokens.countTokens()];
                for (int i = 0; tokens.hasMoreElements(); i++) {
                	optionalBootWebExample[i] = tokens.nextToken();
                }
            } else {
            	optionalBootWebExample = new String[0];
            }

            templateMSAExample1 = resource.getString("example.template.msa1");
            templateMSAExample2 = resource.getString("example.template.msa2");
            templateMSAExample3 = resource.getString("example.template.msa3");
            templateMSAExample4 = resource.getString("example.template.msa4");
            templateMSAExample5 = resource.getString("example.template.msa5");
            templateMSAExample6 = resource.getString("example.template.msa6");
            templateMSAExample7 = resource.getString("example.template.msa7");
            templateMSAExample8 = resource.getString("example.template.msa8");
            templateMSAExample9 = resource.getString("example.template.msa9");
            templateSimpleExample = resource.getString("example.template.simple");

        } catch (Exception ex) {
            BootIdeLog.logError(ex);
        }

    }
}
