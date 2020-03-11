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
package egovframework.dev.imp.ide;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.dev.imp.ide.common.IdeLog;

/**
 * eGovFramework IDE 플러그인의 라이프 사이클을 관리하는 Activator 클래스
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
public class EgovIdePlugin extends AbstractUIPlugin {

    /** 플러그인 아이디 */
    public static final String PLUGIN_ID = "egovframework.dev.imp.ide"; 
    /** 퍼스펙트 아이디 */
    public static final String ID_PERSPECTIVE = "egovframework.dev.imp.core.perspectives.egovperspective";
//        PLUGIN_ID + ".perspectives.egovperspective"; 
    /** 네이처 아이디 */
    public static final String ID_NATURE = PLUGIN_ID + ".natures.egovnature"; 

    /** 기본 프로젝트 마법사 배너 이미지 */
    public static final String IMG_CORE_PROJ_WIZ_BANNER = "core_proj_wiz_banner";
    
    /** 웹 프로젝트 마법사 배너 이미지 */
    public static final String IMG_WEB_PROJ_WIZ_BANNER = "web_proj_wiz_banner";
    
    /** 템플릿 프로젝트 마법사 배너 이미지 */
    public static final String IMG_TMP_PROJ_WIZ_BANNER = "tmp_proj_wiz_banner"; 
    /** 항목 이미지 */
    public static final String IMG_ITEM = "item"; 
    /** 하이콘 최상위 경로 */
    private static final String ICONS_PATH = "icons/"; 

    /** 플러그인 공유 인스턴스 */
    private static EgovIdePlugin plugin;

    /**
     * 생성자
     */
    public EgovIdePlugin() {

    }

    /**
     * 이미지를 레지스트리에 등록
     * @param registry
     * @param key
     * @param fileName
     */
    @SuppressWarnings("deprecation")
    private void registerImage(ImageRegistry registry, String key,
            String fileName) {
        try {
            IPath path = new Path(ICONS_PATH + fileName);
            URL url = find(path);
            if (url != null) {
                ImageDescriptor desc = ImageDescriptor.createFromURL(url);
                registry.put(key, desc);
            }
        } catch (Exception e) {
            IdeLog.logError(e);
        }
        return;
    }

    /**
     * 이미지 레스트스리 최기화
     * @param registry
     */
    protected void initializeImageRegistry(ImageRegistry registry) {
        super.initializeImageRegistry(registry);

        registerImage(registry, IMG_CORE_PROJ_WIZ_BANNER, "newjprj_wiz_c.png"); 
        registerImage(registry, IMG_WEB_PROJ_WIZ_BANNER, "newjprj_wiz_w.png"); 
        registerImage(registry, IMG_TMP_PROJ_WIZ_BANNER, "big_template.png"); 
        registerImage(registry, IMG_ITEM, "templateprop_co.gif"); 
        return;
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
     * 공유 플러그인 인스턴스를 반환
     * @return 공유 플러그인 인스턴스
     */
    public static EgovIdePlugin getDefault() {
        return plugin;
    }

    /**
     * 워크스페이스 가져오기
     * @return Workspace
     */
    public static IWorkspace getWorkspace() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace;
    }

    /**
     * 활성화된 워크벤치 윈도우 가져오기
     * @return WorkbenchWindow
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbenchWindow workbenchWindow =
            getDefault().getWorkbench().getActiveWorkbenchWindow();
        return workbenchWindow;
    }

    /**
     * 활성화된 워크벤치 쉘 가져오기
     * @return Shell
     */
    public static Shell getActiveWorkbenchShell() {
        Shell shell = getActiveWorkbenchWindow().getShell();
        return shell;
    }

    /**
     * 활성화된 워크벤치 페이지 가져오기
     * @return WorkbenchPage
     */
    public static IWorkbenchPage getActiveWorkbenchPage() {
        IWorkbenchPage workbenchPage =
            getActiveWorkbenchWindow().getActivePage();
        return workbenchPage;
    }

    /**
     * 이미지 가져오기
     * @param key
     * @return Image
     */
    public Image getImage(String key) {
        Image image = getImageRegistry().get(key);
        return image;
    }

    /**
     * ImageDescriptor 가져오기
     * @param key
     * @return ImageDescriptor
     */
    public ImageDescriptor getImageDescriptor(String key) {
        ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
        return imageDescriptor;
    }

    /**
     * 플러그인 설치 경로 확인
     * @return 플러그인 설치 경로
     */
    @SuppressWarnings("deprecation")
    public String getInstalledPath() {
        String path = ""; 
        try {
        	
        	//기존 플러그인 설치 경로 확인 오류
            //URL url = Platform.resolve(getDefault().getDescriptor().getInstallURL());
            
            //대체 기능 (경로 취득 로직변경)2017.6.14
        	URL url = Platform.asLocalURL(plugin.getBundle().getEntry("/"));
                    	
            path = (new File(url.getPath())).getAbsolutePath();
        } catch (Exception e) {
            IdeLog.logError(e);
        }
        return path;
    }
}
