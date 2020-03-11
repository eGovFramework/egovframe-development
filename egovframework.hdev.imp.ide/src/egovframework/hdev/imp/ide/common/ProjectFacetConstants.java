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
package egovframework.hdev.imp.ide.common;

/**  
 * 프로젝트 패싯 및 nartue, feature Id
 * @Class Name : ProjectFacetConstants
 * @Description : ProjectFacetConstants Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 22.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 22.
 * @version 1.0
 * @see
 * 
 */
public class ProjectFacetConstants {
    /** 기본 자바 버젼 */
    public static final String DEFAULT_JAVA_VERSION = "1.8";  //Project Facet - Java : 1.3 , 1.4 , 1.5 , 1.6 , 1.7 , 1.8 , 9 , 10
    /** 기본 Android 버젼 */
    public static final String ANDROID_JAVA_VERSION = "6.0"; 
    /** 기본 서블릿 버젼 */
    public static final String DEFAULT_SERVLET_VERSION = "2.5"; 
    /** 자바 패싯 아이디 */
    public static final String JAVA_FACET_ID = "jst.java"; 
    /** 웹 패싯 아이디 */
    public static final String WEB_FACET_ID = "jst.web"; 

    /** 메이블 클래스패스엔트리 컨테이너 아이디 */
    public static final String MAVEN2_CLASSPATH_CONTAINER_ID = "org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER"; 
    public static final String MAVEN3_CLASSPATH_CONTAINER_ID = "org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"; 
    
    /** 메이블 네이처 아이디 */
    public static final String MAVEN2_NATURE_ID = "org.maven.ide.eclipse.maven2Nature"; 
    public static final String MAVEN3_NATURE_ID = "org.eclipse.m2e.core.maven2Nature";
    
    /** 메이븐 feature id */
    public static final String MAVEN2_FEATURE_ID = "org.maven.ide.eclipse.feature.feature.group";
    public static final String MAVEN3_FEATURE_ID = "org.eclipse.m2e.feature.feature.group";
    
    /** 안드로이드 네이처 아이디 */
    public static final String ANDROID_NATURE_ID = "com.android.ide.eclipse.adt.AndroidNature"; 
    
    /** 안드로이드 feature 아이디 */
    public static final String ANDROID_FEATURE_ID = "com.android.ide.eclipse.adt.feature.feature.group";
    
    /** 안드로이드를 위한 M2E 설정 feature 아이디*/
    public static final String M2E_ANDROID_FEATURE_ID = "me.gladwell.eclipse.m2e.android.feature.feature.group"; 

}
