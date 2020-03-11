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
package egovframework.dev.imp.codegen.template.impl;

import net.sf.abstractplugin.core.EclipseProjectUtils;

import org.eclipse.eclipsework.core.impl.EclipseUtils;
import org.eclipse.eclipsework.core.impl.StaticSettings;
import org.eclipse.eclipsework.core.impl.WorkspaceSettings;
import org.eclipse.eclipsework.core.impl.interfaces.resources.Project;
import org.eclipse.eclipsework.core.interfaces.IComponentFactory;
import org.eclipse.eclipsework.core.interfaces.IConnectionProvider;
import org.eclipse.eclipsework.core.interfaces.IEWFactory;
import org.eclipse.eclipsework.core.interfaces.IEWUtils;
import org.eclipse.eclipsework.core.interfaces.IStaticSettings;
import org.eclipse.eclipsework.core.interfaces.IWorkspaceSettings;
import org.eclipse.eclipsework.core.interfaces.resources.IProject;
import org.eclipse.eclipsework.core.interfaces.resources.IResourceManager;

/**
 * 
 * 코드젠 팩토리 클래스
 * <p><b>NOTE:</b> CodeGen에서 사용되어질 각종 구현체 생성 팩토리
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
public class CodeGenFactory implements IEWFactory {

    /** 팩토리 인스턴스 */
    private static CodeGenFactory instance = null;

    /**
     * 
     * 팩토리 싱글톤 인스턴스 생성
     *
     * @return
     */
    public static  CodeGenFactory getInstance() {
        if(instance == null) {
            instance = new CodeGenFactory();
        }
        return instance;
    }

    /**
     * 
     * 컴포넌트 팩토리 반환
     *
     * @return IComponentFactory 컴포넌트 제공자
     */
    public IComponentFactory getComponentFactory() {
        return new CodeGenComponentFactory();
    }

    /**
     * 
     * Database 연결 제공자 반환
     *
     * @return IConnectionProvider 
     */
    public IConnectionProvider getConnectionProvider() {
        return new ConnectionProvider();
    }

    /**
     * 
     * 프로젝트 반환
     *
     * @return IProject 프로젝트
     */
    public IProject getProject() {
        return new Project(EclipseProjectUtils.getSelectedProject());
    }

    /**
     * 
     * 리소스관리자 반환
     *
     * @return IResourceManager 리소스관리자
     */
    public IResourceManager getResourceManager() {
        return new CodeGenResourceManager();
    }

    /**
     * 
     * 정적설정 반환
     *
     * @return IStaticSettings
     */
    public IStaticSettings getStaticSettings() {
        return new StaticSettings();
    }

    /**
     * 
     * 유틸리티 반환
     *
     * @return IEWUtils
     */
    public IEWUtils getUtils() {
        return new EclipseUtils();
    }
    
    /**
     * 
     * 워크스페이스설정 반환
     *
     * @return IWorkspaceSettings
     */
    public IWorkspaceSettings getWorkspaceSettings() {
        return new WorkspaceSettings();
    }
}
