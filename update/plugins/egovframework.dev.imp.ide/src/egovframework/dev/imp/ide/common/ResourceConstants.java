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
package egovframework.dev.imp.ide.common;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * 리소스 관련 상수 클래스
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
public class ResourceConstants {

    /** 제외 패스 */
    public static final IPath EXCLUDING_PATH = new Path("**"); 
    /** 소스 폴더 */
    public static final IPath SOURCE_FOLDER = new Path("src/main/java"); 
    /** 리소스 폴더 */
    public static final IPath RESOURCE_FOLDER = new Path("src/main/resources"); 

    /** 테스트 소스 폴더 */
    public static final IPath TEST_SOURCE_FOLDER = new Path("src/test/java"); 
    /** 테스트 리소스 폴더 */
    public static final IPath TEST_RESOURCE_FOLDER =
        new Path("src/test/resources"); 

    /** META-INF 경로 */
    public static final IPath METAINF_PATH =
        new Path("src/main/webapp/META-INF"); 
    /** WEB-INF 경로 */
    public static final IPath WEBINF_PATH =
        new Path("src/main/webapp/WEB-INF/"); 
    /** WEB-INF/lib 경로 */
    public static final IPath WEBINF_LIB_PATH =
        new Path("src/main/webapp/WEB-INF/lib"); 
    /** WEB-INF/classes 경로 */
    public static final IPath WEBINF_CLASSES_PATH =
        new Path("src/main/webapp/WEB-INF/classes"); 

    /** 기본 output 폴더 */
    public static final IPath DEFAULT_OUTPUT_FOLDER =
        new Path("target/classes"); 
    /** 테스트 output 폴더 */
    public static final IPath TEST_OUTPUT_FOLDER =
        new Path("target/test-classes"); 

    /** 웹 루트 */
    public static final String WEB_ROOT = "src/main/webapp"; 

    /** POM 파일명 */
    public static final String POM_FILENAME = "pom.xml"; 
    /** POM 예제 파일 경로 */
    public static final String POM_EXAMPLE_PATH =
        "egovframework/dev/imp/ide/wizards/examples/"; 
    /** 예제 파일 경로 */
    public static final String EXAMPLES_PATH = "examples/";

    /** 시스템 폴더들 */
    public static final IPath[] SYSTEM_FOLDERS =
        {SOURCE_FOLDER, RESOURCE_FOLDER, TEST_SOURCE_FOLDER,
            TEST_RESOURCE_FOLDER, DEFAULT_OUTPUT_FOLDER, TEST_OUTPUT_FOLDER };
    /** 웹 어플리케이션 시스템 폴더들 */
    public static final IPath[] WEBAPP_SYSTEM_FOLDERS =
        {METAINF_PATH, WEBINF_PATH, WEBINF_LIB_PATH, WEBINF_CLASSES_PATH };
}
