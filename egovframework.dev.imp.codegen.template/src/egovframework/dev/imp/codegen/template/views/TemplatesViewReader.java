/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.views;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.abstractplugin.EclipseErrorLogUtil;

import org.apache.commons.lang.StringUtils;

import egovframework.dev.imp.codegen.template.CodeGenLog;
import egovframework.dev.imp.codegen.template.model.WizardsData;

/**
 * 
 * 템플릿 목록 파일 Reader 클래스
 * <p><b>NOTE:</b> 템플릿 목록 파일(wizards.xml)을 InputStream으로 읽어오는 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
public class TemplatesViewReader {

    /** 컨텐트 프로바이더 */
    private final ViewContentProvider view;

    /**
     * 
     * 생성자
     *
     * @param view
     */
    public TemplatesViewReader(ViewContentProvider view) {
        this.view = view;
    }

    /**
     * 
     * 초기화 하기
     *
     */
    @SuppressWarnings("unused")
	public void initialize() {

        String wizardConfigFile = "";
        WizardsData[] wizardsDatas = WizardsData.getTemplateLists();

        for(WizardsData wizardsData : wizardsDatas){
            initializeFromFilePath(wizardsData.getName(), wizardsData.getWizardsFile());
        }


    }


    /**
     * 
     * wizards.xml 파일 위치 확인
     *
     * @param rootName
     * @param wizardConfigFile
     */
    private void initializeFromFilePath(String rootName, String wizardConfigFile) {
        InputStream in = getInputStream(wizardConfigFile);
        File file = new File(wizardConfigFile);
        if(in != null){
            initFromInputStream(rootName, in, file);
        }

    }

    /**
     * 
     * InputStream으로부터 Tree 초기화
     *
     * @param rootName
     * @param in
     * @param file
     */
    private void initFromInputStream(String rootName, InputStream in, File file) {
        try {

            if(in != null) {
                view.showWizards(rootName, in, file);
            }
        }
        catch (Exception ex) {
            CodeGenLog.logError(ex);
            EclipseErrorLogUtil.logStackTrace(ex);
            view.log.error(ex);
            view.createErrorTree("Error reading templates: " + ex.getMessage());
        }
        finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    view.log.error(e);
                }
            }
        }
    }

    /**
     * 
     * getInputStream
     *
     * @param wizardConfigFile
     * @return
     */
    private InputStream getInputStream(String wizardConfigFile) {
        if(StringUtils.isEmpty(wizardConfigFile)) {
            view.createErrorTree("Configure the wizards.xml file in preferences->eclipsework");
            return null;
        }

        File file = new File(wizardConfigFile);
        if(!file.exists()) {
            view.createErrorTree("File " + file + " not found.");
            return null;
        }

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            view.log.error(ex);
            view.createErrorTree(ex.getMessage());
        }
        return null;
    }
}