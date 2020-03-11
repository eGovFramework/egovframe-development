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
package egovframework.dev.imp.codegen.template;

import net.sf.abstractplugin.ImageUtil;

import org.eclipse.eclipsework.EclipseWorkPlugin;
import org.eclipse.eclipsework.core.EclipseWorkFactoryManager;
import org.osgi.framework.BundleContext;

import egovframework.dev.imp.codegen.template.impl.CodeGenFactory;

/**
 * 
 * 템플릿 기반 코드젠 플로그인 라이프 사이클을 관리하는 Activator 클래스  
 * <p><b>NOTE:</b> 플러그인별 환경설정 또는 기타 상태 정보에 접근해 초기화하기 위한 메소드 제공 
 * 플러그인 로딩, 종료 이벤트 처리.
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
public class EgovCodeGenPlugin extends EclipseWorkPlugin implements ConfigGeneration{

    /** 플러그인 아이디 */
    public static final String PLUGIN_ID = "egovframework.dev.imp.codegen.template";

    /** 플러그인 인스턴스 */
    private static EgovCodeGenPlugin plugin;

    /**
     * 생성자
     */
    public EgovCodeGenPlugin() {
        super();
        EclipseWorkFactoryManager.registerConductorFactory(CodeGenFactory.getInstance());
        //CodeGenLog.logInfo("file.encoding:" + System.getProperty("file.encoding"));
        try
        {
                ImageUtil.init(PLUGIN_ID);
        } catch (Throwable e)
        {
                CodeGenLog.logError(e);
        }
    }

    /**
     * 플로그인 시작
     * @param context
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        return;
    }

    /**
     * 플러그인 종료
     * @param context
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        return;
    }

    /**
     * 플러그인 인스턴스 가져오기
     *
     * @return 플러그인 인스턴스
     */
    public static EgovCodeGenPlugin getDefault() {
        return plugin;
    }

}
