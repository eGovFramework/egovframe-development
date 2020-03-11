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

import org.eclipse.osgi.util.NLS;

/**
 * 다국어 처리를 위한 메시지 클래스
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class DeviceAPIMessages extends NLS {

    /** 번들명 */
    private static final String BUNDLE_NAME = "egovframework.hdev.imp.ide.common.messages"; 

    public static String CUSTOMIZE_TABLE_CREATION_PAGE_TITLE;
    
    public static String CUSTOMIZE_TABLE_CREATION_PAGE_DESCRIPTION;
    
    public static String SELECT_DB_LABEL;
    
    public static String DATASOURCE_GROUP_TEXT;
    
    public static String MYSQL_DBTYPE;
    
    public static String ORACLE_DBTYPE;
    
    public static String ALTIBASE_DBTYPE;
    
    public static String TIBERO_DBTYPE;
    
    public static String DRIVER_CLASS_NAME_LABEL;
    
    public static String DBTYPE_LABEL;
    
    public static String URL_LABEL;
   
    public static String USER_NAME_LABEL;
    
    public static String PASSWORD_LABEL;
    
    public static String TEST_BUTTON_TEXT;
    
    public static String CREATE_TABLE_BUTTON_TEXT;
    
    public static String CREATE_TABLE_GROUP_TEXT;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_TABLENAME;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_DESCRIPTION;
    
    public static String ERROR_CANNOT_CONNECT_DB;
    
    public static String SUCCESS_CONNECT_DB;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_ALREADY_EXIST;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_SUCCESS;
    
    public static String CREATE_TABLE_TABLEVIEWER_COLUMN_RESULT_FAILED;
    
    public static String PROJECT_CREATION_PROJECT_NAME_LABEL;
    
    public static String PROJECT_CREATION_CONTENTS_GROUP_TEXT;
    
    public static String PROJECT_CREATION_USE_DEFAULT_WORKSPACE_LOCATION_BUTTON_TEXT;
    
    public static String PROJECT_CREATION_LOCATION_LABEL;
    
    public static String PROJECT_CREATION_LOCATION_BROWSE_BUTTON_TEXT;
    
    public static String PROJECT_CREATION_LOCATION_BROWSE_DIALOG_TITLE;
    
    public static String PROJECT_CREATION_ERROR_PROJECT_NAME_EMPTY;
    
    public static String PROJECT_CREATION_ERROR_PROJECT_NAME_INVALID;
    
    public static String PROJECT_CREATION_ERROR_PROJECT_NAME_TOOLONG;
    
    public static String PROJECT_CREATION_ERROR_PROJECT_NAME_ALREADY_EXIST;
    
    public static String PROJECT_CREATION_ERROR_WORKSPACE_LOCATION_NOT_VALID;
    
    public static String PROJECT_CREATION_PAGE_TITLE;
    
    public static String PROJECT_CREATION_PAGE_DESCRIPTION;
    
    public static String PROJECT_CREATION_GROUP_ID_LABEL;
    
    public static String PROJECT_CREATION_ARTIFACT_ID_LABEL;
    
    public static String PROJECT_CREATION_VERSION_LABEL;
    
    public static String PROJECT_CREATION_MAVEN_GROUP_TEXT;
    
    public static String PROJECT_CREATION_ERROR_GROUP_ID_EMPTY;
    
    public static String PROJECT_CREATION_ERROR_GROUP_ID_INVALID;
    
    public static String PROJECT_CREATION_ERROR_ARTIFACT_ID_EMPTY;
    
    public static String PROJECT_CREATION_ERROR_ARTIFACT_ID_INVALID;
    
    public static String PROJECT_CREATION_ERROR_VERSION_EMPTY;
    
    public static String PROJECT_CREATION_ERROR_VERSION_INVALID;
    
    public static String WEB_PROJECT_TITLE;
    
    public static String WEB_PROJECT_DESCRIPTION;
    
    public static String WEB_PROJECT_SERVER_TARGET_GROUP_TEXT;
    
    public static String WEB_PROJECT_NEW_SERVER_TARGET_BUTTON_TEXT;
    
    public static String WEB_PROJECT_WEB_MODULE_VERSION_GROUP_LABEL;
    
    public static String WEB_PROJECT_TABLE_CREATE_GROUP_TEXT;
    
    public static String WEB_PROJECT_TABLE_CREATE_DEFAULT_BUTTON_TEXT;
    
    public static String WEB_PROJECT_TABLE_CREATE_SELECT_BUTTON_TEXT;
    
    public static String WEB_PROJECT_DESCRIPTION_GROUP_NAME;
    
    public static String WEB_PROJECT_DESCRIPTION_GROUP_TEXT1;
    
    public static String WEB_PROJECT_DESCRIPTION_GROUP_TEXT2;
    
    public static String WEB_PROJECT_ERROR_EQUAL_WITH_DEVICE_API_PJT;
    
    public static String WEB_PROJECT_ERROR_REQUIRE_JAVA_VERSION;
    
    public static String GENERATE_TEMPLATE_WIZARD_PAGE_TITLE;
    
    public static String GENERATE_TEMPLATE_WIZARD_PAGE_DESCRIPTION;
    
    public static String FILES_TABLE_VIEWER_CONTROL_CHECKBOX;
    
    public static String FILES_TABLE_VIEWER_GROUP_TEXT;
    
    public static String FILES_TABLE_VIEWER_SOURCE_NAME_COLUMN;
    
    public static String FILES_TABLE_VIEWER_SOURCE_DESCRIPTION_COLUMN;
    
    public static String CREATE_WEB_PROJECT_CHECKBOX_BUTTON_TEXT;
    
    public static String URL_GROUP_TEXT;
    
    public static String URL_TEXT_CONTROL_CHECKBOX;
    
    public static String URL_LABEL_TEXT;
    
    public static String GENERATE_TEMPLATE_WIZARD_PAGE_ERROR_EMPTY_URL;
    
    public static String GENERATE_TEMPLATE_WIZARD_PAGE_ERROR_INVALID_URL;
    
    public static String SELECT_PROJECT_PAGE_TITLE;
    
    public static String SELECT_PROJECT_PAGE_DESCRIPTION;
    
    public static String SELECT_PROJECT_PAGE_CONTAINER_NAME_LABEL_TEXT;
    
    public static String SELECT_PROJECT_PAGE_NOTE;
    
    public static String SELECT_PROJECT_PAGE_NOTE_CONTENTS;
    
    public static String SELECT_PROJECT_PAGE_ERROR_INVALID_PROJECT_NAME;
    
    public static String COFIRM_PROJECT_DELETE;
    
    public static String WIZARD_NEW_PROJECT_TITLE;
    
    public static String WIZARD_ADD_PROJECT_TITLE;
    
    public static String SELECT_TEMPLATE_NOTE_WEB_CONTENTS;
    
    public static String SELECT_TEMPLATE_NOTE_CONTENTS;
    
    /** 리소스 번들 초기화 */
    static {
        NLS.initializeMessages(BUNDLE_NAME, DeviceAPIMessages.class);
    }

}
