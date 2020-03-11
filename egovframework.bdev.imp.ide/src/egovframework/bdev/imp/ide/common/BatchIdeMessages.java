
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
package egovframework.bdev.imp.ide.common;

import org.eclipse.osgi.util.NLS;

/**
 * 배치 IDE용 다국어 처리를 위한 메시지 클래스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *	수정일      	수정자           수정내용
 *  -------			--------    ---------------------------
 *	2012.07.02	배치개발환경 개발팀    최초 생성
 * 
 * 
 * </pre>
 */
public class BatchIdeMessages extends NLS {

    /** 번들명 */
    private static final String BUNDLE_NAME = "egovframework.bdev.imp.ide.common.messages"; 
    public static String batchBaseProjectCreationPage0;
	public static String batchBaseProjectCreationPage1;
	public static String batchBaseProjectCreationPage3;
	public static String batchBaseProjectCreationPage5;
	public static String batchBaseProjectCreationPage6;
	public static String batchProjectCreationPage1;
	public static String batchProjectCreationPage12;
	public static String batchProjectCreationPage2;
	public static String batchProjectCreationPage9;
	public static String BatchTemplateProjectSelectCreateTypePage_DB_EXPLANATION;
	public static String BatchTemplateProjectSelectCreateTypePage_DB_RADIO_BUTTON;
	public static String BatchTemplateProjectSelectCreateTypePage_DESCRIPTION;
	public static String BatchTemplateProjectSelectCreateTypePage_FILE_SAM_EXPLANATION;
	public static String BatchTemplateProjectSelectCreateTypePage_FILE_SAM_RADIO_BUTTON;
	public static String BatchTemplateProjectSelectExecuteTypePage_COMMAND_LINE_EXPLANATION;
	public static String BatchTemplateProjectSelectExecuteTypePage_COMMAND_LINE_RADIO_BUTTON;
	public static String BatchTemplateProjectSelectExecuteTypePage_DESCRIPTION;
	public static String BatchTemplateProjectSelectExecuteTypePage_SCHEDULER_EXPLANATION;
	public static String BatchTemplateProjectSelectExecuteTypePage_SCHEDULER_RADIO_BUTTON;
	public static String BatchTemplateProjectSelectExecuteTypePage_WEB_EXPLANATION;
	public static String BatchTemplateProjectSelectExecuteTypePage_WEB_RADIO_BUTTON;
	public static String batchWebProjectCreationPage5;
    /** 배치 템플릿 프로젝트 마법사 타이틀 */
    public static String wizardsEgovNewTemplateBProjectWizardTITLE;
    /** 컨텐트 */
    public static String wizardsPagesBaseBProjectCreationPage0;
    /** 워크스페이스 위치를 기본으로 사용 */
    public static String wizardsPagesBaseBProjectCreationPage1;
    /** Location */
    public static String wizardsPagesBaseBProjectCreationPage2;
    /** Browse Button Text */
    public static String wizardsPagesBaseBProjectCreationPage3;
    /** Select Location */
    public static String wizardsPagesBaseBProjectCreationPage4;
    /** 프로젝트 이름 */
    public static String wizardsPagesBaseBProjectCreationPage5;
    /** 기본 프로젝트 마법사 설명 */
    public static String wizardsPagesCoreBProjectCreationPage1;
    /** 그룹아이디 미 입력시 메시지 */
    public static String wizardsPagesBProjectCreationPage11;
    /** 아티팩트아이디 미 입력시 메시지 */
    public static String wizardsPagesBProjectCreationPage13;
    /** 메이븐 설정 */
    public static String wizardsPagesBProjectCreationPage14;
    /** 그룹 아이디 */
    public static String wizardsPagesBProjectCreationPage15;
    /** 아티팩트 아이디 */
    public static String wizardsPagesBProjectCreationPage16;
    /** 버젼 */
    public static String wizardsPagesBProjectCreationPage17;
    
    /** Target Runtime */
    public static String wizardsPagesWebBProjectCreationPage2;
    /** 동적 웹 모듈 버젼 */
    public static String wizardsPagesWebBProjectCreationPage3;
    /** 신규 버튼 텍스트 */
    public static String wizardsPagesWebBProjectCreationPage4;
	
	public static String wizardPageBatchJobTemplatePage0;
	
	public static String wizardPageBatchJobTemplatePage3;
	
	public static String wizardPageBatchJobTemplatePage5;
	
	public static String wizardPageBatchJobTemplatePage7;
	
	public static String wizardPageBatchJobTemplatePage9;
	
	public static String wizardPageBatchJobTemplatePage11;
	
	public static String DBCommandLineDefaultsrc;
	
	public static String SamCommandLineDefaultsrc;
	
	public static String CommandLinePomFile;

	public static String DBWebDefaultsrc;
	
	public static String SamWebDefaultsrc;

	public static String WebPomFile;

	public static String DBSchedulerDefaultsrc;
	
	public static String SamSchedulerDefaultsrc;

	public static String SchedulerPomFile;

	public static String wizardPageBatchJobFileSchedulerDescription;

	public static String wizardPageBatchJobFileCommandLineDescription;

	public static String wizardPageBatchJobFileWebDescription;

	public static String wizardPageBatchJobDBSchedulerDescription;

	public static String wizardPageBatchJobDBCommandLineDescription;

	public static String wizardPageBatchJobDBWebDescription;
	
    /** 리소스 번들 초기화 */
    static {
        NLS.initializeMessages(BUNDLE_NAME, BatchIdeMessages.class);
    }

}
