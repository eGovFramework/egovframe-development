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
package egovframework.dev.imp.dbio;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.dev.imp.dbio.common.DbioLog;

/**
 * eGovFramework DBIO 플러그인의 라이프 사이클을 관리하는 Activator 클래스
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class DBIOPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "egovframework.dev.imp.dbio"; //$NON-NLS-1$

	// The shared instance
	private static DBIOPlugin plugin;
	
	private static final String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public static final String IMG_SQL_MAP_WIZ_BANNER = "sql_map_wiz_banner"; //$NON-NLS-1$
	public static final String IMG_SQL_MAP_CONFIG_WIZ_BANNER = "sql_map_config_wiz_banner"; //$NON-NLS-1$
	public static final String IMG_MAPPER_WIZ_BANNER = "mapper_wiz_banner"; //$NON-NLS-1$
	public static final String IMG_MAPPER_CONFIGURATION_WIZ_BANNER = "mapper_configuration_wiz_banner"; //$NON-NLS-1$	
	public static final String IMG_PROPERTY = "property"; //$NON-NLS-1$
	
	
	/**
	 * 생성자
	 */
	public DBIOPlugin() {
	}

	/** 
	 * 플러그인 시작
	 * @param context
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * 클러그인 종료 
	 * 
	 * @parm context
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * DBIOPlugin 공유 인스턴스 반환
	 *
	 * @return DBIOPlugin 공유 인스턴스
	 */
	public static DBIOPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * 이미지 등록
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
			DbioLog.logError(e);
		}
		return;
	}
	
	/**
	 * ImageRegistry 초기화
	 * 
	 * @param registry
	 */
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		registerImage(registry, IMG_SQL_MAP_WIZ_BANNER,	"wizards/sqlmap_wiz.png"); //$NON-NLS-1$
		registerImage(registry, IMG_SQL_MAP_CONFIG_WIZ_BANNER, "wizards/sqlmapconfig_wiz.png"); //$NON-NLS-1$		
		registerImage(registry, IMG_MAPPER_WIZ_BANNER,	"wizards/mapper_wiz.png"); //$NON-NLS-1$
		registerImage(registry, IMG_MAPPER_CONFIGURATION_WIZ_BANNER, "wizards/mapperconfiguration_wiz.png"); //$NON-NLS-1$				
		registerImage(registry, IMG_PROPERTY, "editors/property.gif"); //$NON-NLS-1$		
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
