
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
package egovframework.boot.dev.imp.ide.common;

import org.eclipse.osgi.util.NLS;

/**
 * 다국어 처리를 위한 메시지 클래스
 * @author 개발환경 개발팀 이흥주
 * @since 2022.02.14
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *  수정일        수정자        수정내용
 *  ----------  --------    ---------------------------
 *  2022.02.14  이정은        최초 생성
 *  2024.07.22  신용호        영문 메시지 오기 수정
 * 
 * 
 * </pre>
 */
public class BootIdeMessages extends NLS {

    /** 번들명 */
    private static final String BUNDLE_NAME = "egovframework.boot.dev.imp.ide.common.messages"; 
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
	
	/** 다이나믹 웹모듈 에러메세지 **/
	public static String bootProjectCreationPage5;
	
	/** 신규 부트 웹 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewBootWebProjectWizardTITLE;
    /** 신규 부트 템플릿 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewBootTemplateProjectWizardTITLE;
    /** 신규 MSA 템플릿 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewMsaTemplateProjectWizardTITLE;
    
    /** 그룹아이디 미 입력 시 메시지 */
    public static String wizardspagesProjectCreationPage11;
    /** 아티팩트아이디 미 입력 시 메시지 */
    public static String wizardspagesProjectCreationPage13;
    /** 메이븐 설정 */
    public static String wizardspagesProjectCreationPage14;
    /** 그룹 아이디 */
    public static String wizardspagesProjectCreationPage15;
    /** 아티팩트 아이디 */
    public static String wizardspagesProjectCreationPage16;
    /** 버젼 */
    public static String wizardspagesProjectCreationPage17;
    /** 버젼 미 입력 시 메시지 */
    public static String wizardspagesProjectCreationPage18;
    
    /** 예제 생성 마법사 제목 */
    public static String wizardspagesSelectSamplePage0;
    /** 예제 생성 마법사 설명 */
    public static String wizardspagesSelectSamplePage1;
    /** 예제 생성 여부 확인 버튼 텍스트 */
    public static String wizardspagesSelectSamplePage2;
    /** 예제 파일 목록 리스트 그룹 명 */
    public static String wizardspagesSelectSamplePage3;
    
    /** eGovFramework 부트 웹 프로젝트 마법사 제목 */
    public static String wizardspagesBootWebProjectCreationPage0;
    /** eGovFramework 부트 웹 프로젝트 마법사 설명 */
    public static String wizardspagesBootWebProjectCreationPage1;
    /** Target Runtime */
    public static String wizardspagesBootWebProjectCreationPage2;
    /** 동적 웹 모듈 버젼 */
    public static String wizardspagesBootWebProjectCreationPage3;
    /** 신규 버튼 텍스트 */
    public static String wizardspagesBootWebProjectCreationPage4;
    
    /** 부트 템플릿 프로젝트 생성 메세지 */
    public  static String wizardspagesBootTemplateProjectCreationPage0;
    /** MSA 템플릿 프로젝트 생성 메세지 */
    public  static String wizardspagesBootTemplateProjectCreationPage1;
    /** 부트 템플릿 프로젝트 생성 설명 */
    public static String wizardspagesBootTemplateProjectCreationPage2;
    /** MSA 템플릿 프로젝트 생성 설명 */
    public static String wizardspagesBootTemplateProjectCreationPage3;
    /** 부트 템플릿 프로젝트 선택 타이틀 */
    public static String wizardspagesBootTemplateProjectCreationPage4;
    /** 부트 템플릿 프로젝트 개요 타이틀 */
    public static String wizardspagesBootTemplateProjectCreationPage5;
    
    /** 부트 템플릿 프로젝트 생성 메세지 */
    public static String boottemplateProjectCreationPage0;
    /** 심플 템플릿 프로젝트 생성 메세지 */
    public static String bootTemplateSimpleHomeProjectSelectPage;
    /** MSA 템플릿 프로젝트 생성 메세지 */
    public static String bootTemplateMSAProjectSelectPage;
    
    public static String bootTemplateMSAProjectSelectPageDiscovery;
    public static String bootTemplateMSAProjectSelectPageConfig;
    public static String bootTemplateMSAProjectSelectPageGateway;
    public static String bootTemplateMSAProjectSelectPageBoard;
    public static String bootTemplateMSAProjectSelectPagePortal;
    public static String bootTemplateMSAProjectSelectPageUser;
    public static String bootTemplateMSAProjectSelectPageReserveCheck;
    public static String bootTemplateMSAProjectSelectPageReserveItem;
    public static String bootTemplateMSAProjectSelectPageReserveRequest;
    
    /** MSA 템플릿 프로젝트 생성화면 목록 & description */
    public static String wizardspagesBootTemplateProjectCreationPage7;
    public static String wizardspagesBootTemplateProjectCreationPage8;
    /** 부트 템플릿 생성 프로젝트 타이틀 및 설명 */
    public static String wizardspagesBootTemplateProjectCreationPage9;
	public static String wizardspagesBootTemplateProjectCreationPage10;
	/** MSA 템플릿 생성 프로젝트 타이틀 및 설명 */
	public static String wizardspagesBootTemplateMSAProjectCreationPageDiscoveryTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageDiscoveryDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageConfigTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageConfigDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageGatewayTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageGatewayDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageBoardTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageBoardDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPagePortalTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPagePortalDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageUserTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageUserDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveCheckTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveCheckDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveItemTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveItemDesc;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveRequestTitle;
	public static String wizardspagesBootTemplateMSAProjectCreationPageReserveRequestDesc;
    
    
    /** 리소스 번들 초기화 */
    static {
        NLS.initializeMessages(BUNDLE_NAME, BootIdeMessages.class);
    }

}
