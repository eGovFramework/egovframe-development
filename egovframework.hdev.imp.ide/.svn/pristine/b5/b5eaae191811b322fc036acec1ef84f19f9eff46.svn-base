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
package egovframework.hdev.imp.ide;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;

/**  
 * 플러그인 라이프 싸이클 클래스
 * @Class Name : EgovDeviceAPIIdePlugin
 * @Description : EgovDeviceAPIIdePlugin Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 9. 17.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 9. 17.
 * @version 1.0
 * @see
 * 
 */
public class EgovDeviceAPIIdePlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.hdev.imp.ide"; //$NON-NLS-1$
	
	/** 네이처 아이디 */
    public static final String ID_NATURE = PLUGIN_ID + ".natures.egovnature"; 
    
    /** 항목 이미지 */
    public static final String IMG_ITEM = "item"; 

	/** 플러그인 인스턴스 */
	private static EgovDeviceAPIIdePlugin plugin;
	
	/** 기본 프로젝트 마법사 배너 이미지 */
    public static final String IMG_CORE_PROJ_WIZ_BANNER = "core_proj_wiz_banner"; 
	
	/** 아이콘 최상위 경로 */
    private static final String ICONS_PATH = "icons/";
	
	/**
	 * 생성자
	 */
	public EgovDeviceAPIIdePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * 플러그인 인스턴스 가져오기
	 * @return Plugin
	 */
	public static EgovDeviceAPIIdePlugin getDefault() {
		return plugin;
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
     * 플러그인 설치 경로 확인
     * @return 플러그인 설치 경로
     */
    @SuppressWarnings("deprecation")
    public String getInstalledPath() {
        String path = ""; 
        try {
        	//example경로 오류 수정(장성호 2018-06-25)
            //URL url = Platform.resolve(getDefault().getDescriptor().getInstallURL());
            URL url = Platform.asLocalURL(plugin.getBundle().getEntry("/"));
            DeviceAPIIdeLog.logInfo(url.toString());
            path = (new File(url.getPath())).getAbsolutePath();
            DeviceAPIIdeLog.logInfo(path.toString());
        } catch (Exception e) {

        	DeviceAPIIdeLog.logError(e);
        }
        return path;
    }
    
    /**
     * 이미지 레스트스리 최기화
     * @param registry
     */
    protected void initializeImageRegistry(ImageRegistry registry) {
        super.initializeImageRegistry(registry);

        registerImage(registry, IMG_CORE_PROJ_WIZ_BANNER, "newjprj_wiz_c.png"); 
        return;
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
            DeviceAPIIdeLog.logError(e);
        }
        return;
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
}
