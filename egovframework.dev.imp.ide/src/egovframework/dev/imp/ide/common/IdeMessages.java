
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

import org.eclipse.osgi.util.NLS;

/**
 * 다국어 처리를 위한 메시지 클래스
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
public class IdeMessages extends NLS {

    /** 번들명 */
    private static final String BUNDLE_NAME = "egovframework.dev.imp.ide.common.messages"; 
    public static String baseProjectCreationPage0;
	public static String baseProjectCreationPage1;
	public static String baseProjectCreationPage2;
	public static String projectCreationPage1;
	public static String projectCreationPage2;
	public static String projectCreationPage3;
	public static String projectCreationPage7;
	/** 프로젝트 생성시 프로젝트 이름이 길경우 에러메세지 */
	public static String baseProjectCreationPage5;
	public static String baseProjectCreationPage7;
	public static String templateProjectCreationPage0;
	public static String TemplateProjectSelectPage1;
	public static String TemplateProjectSelectPage4;
	public static String TemplateProjectSelectPage6;
	public static String TemplateProjectSelectPage7;
	;
	public static String webProjectCreationPage5;
	/** 신규 기본 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewCoreProjectWizardTITLE;
    /** 신규 웹 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewWebProjectWizardTITLE;
    /** 신규 템플릿 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewTemplateProjectWizardTITLE;
    /** 기본 프로젝트 마법사 제목 */
    public static String wizardspagesCoreProjectCreationPage0;
    /** 기본 프로젝트 마법사 설명 */
    public static String wizardspagesCoreProjectCreationPage1;
    /** 그룹아이디 미 입력시 메시지 */
    public static String wizardspagesProjectCreationPage11;
    /** 아티팩트아이디 미 입력시 메시지 */
    public static String wizardspagesProjectCreationPage13;
    /** 메이븐 설정 */
    public static String wizardspagesProjectCreationPage14;
    /** 그룹 아이디 */
    public static String wizardspagesProjectCreationPage15;
    /** 아티팩트 아이디 */
    public static String wizardspagesProjectCreationPage16;
    /** 버젼 */
    public static String wizardspagesProjectCreationPage17;
    
    public static String wizardspagesProjectCreationPage18;
    /** 예제 생성 마법사 제목 */
    public static String wizardspagesSelectSamplePage0;
    /** 예제 생성 마법사 설명 */
    public static String wizardspagesSelectSamplePage1;
    /** 예제 생성 여부 확인 버튼 텍스트 */
    public static String wizardspagesSelectSamplePage2;
    /** 예제 파일 목록 리스트 그룹 명 */
    public static String wizardspagesSelectSamplePage3;
    /** eGovFramework 웹 프로젝트 마법사 제목 */
    public static String wizardspagesWebProjectCreationPage0;
    /** eGovFramework 웹 프로젝트 마법사 설명 */
    public static String wizardspagesWebProjectCreationPage1;
    /** Target Runtime */
    public static String wizardspagesWebProjectCreationPage2;
    /** 동적 웹 모듈 버젼 */
    public static String wizardspagesWebProjectCreationPage3;
    /** 신규 버튼 텍스트 */
    public static String wizardspagesWebProjectCreationPage4;
    /** 템플릿 프로젝트 생성 메세지 */
    public static String wizardspagesTemplateProjectCreationPage0;
    /** 템플릿 프로젝트 생성 설명 */
    public static String wizardspagesTemplateProjectCreationPage1;
    /** 템플릿 프로젝트 선택 타이틀 */
    public static String wizardspagesTemplateProjectCreationPage2;
    /** 템플릿 프로젝트 개요 타이틀 */
    public static String wizardspagesTemplateProjectCreationPage3;
    /** 템플릿 프로젝트 심플 홈페이지 제목 */
    public static String wizardspagesTemplateProjectCreationPage4;
    /** 템플릿 프로젝트 포털 사이트 제목 */
    public static String wizardspagesTemplateProjectCreationPage5;
    /** 템플릿 프로젝트 엔터프라이즈 사이트 제목 */
    public static String wizardspagesTemplateProjectCreationPage6;
    /** 템플릿 프로젝트 심플 홈페이지 내용 */
    public static String wizardspagesTemplateProjectCreationPage7;
    /** 템플릿 프로젝트 포털 사이트 내용 */
    public static String wizardspagesTemplateProjectCreationPage8;
    /** 템플릿 프로젝트 엔터프라이즈 사이트 내용 */
    public static String wizardspagesTemplateProjectCreationPage9;
    /** 템플릿 생성 프로젝트 타이틀 */
    public static String wizardspagesTemplateProjectCreationPage10;
    /** 공통컴포넌트 all-in-one 프로젝트 타이틀 */
    public static String wizardspagesTemplateProjectCreationPage11;
    /** 공통컴포넌트 all-in-one 프로젝트 내용 */
    public static String wizardspagesTemplateProjectCreationPage12;
    
    /** 리소스 번들 초기화 */
    static {
        NLS.initializeMessages(BUNDLE_NAME, IdeMessages.class);
    }

}
