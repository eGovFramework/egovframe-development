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
package egovframework.dev.imp.codegen.template.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import egovframework.dev.imp.codegen.template.CodeGenLog;
import egovframework.dev.imp.codegen.template.EgovCodeGenPlugin;

/**
 * 
 * Wizard Data 클래스
 * <p><b>NOTE:</b> 템플릿 목록을 정의한 wizards.xml(예)파일의 각 마법사 정보를 담고 있는 클래스 
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
public class WizardsData {

	/** 설정 엘리먼트 */
	private final IConfigurationElement configElement;
	/** 순서 */
	private final int ordinal;
	/** 번들 */
	private final Bundle bundle;

	/** 아이디 */
	private final String id;
	/** 이름 */
	private final String name;
	/** 위자드 파일 */
	private final String wizardsFile;
	/** 설치 경로 */
	private final String installedPath;

	/** 마법사 데이터 캐싱 */
	private static WizardsData[] cachedWizardsData;

	/**
	 * 
	 * 템플릿 리스트 로딩
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static WizardsData[] getTemplateLists() {

		//		if (cachedWizardsData != null)	return cachedWizardsData;
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EgovCodeGenPlugin.PLUGIN_ID, "templateWizards").getExtensions();
		
		List<WizardsData> found = new ArrayList<WizardsData>(20);
		//found.add(UNKNOWN);

		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();

			///////////////////////////////////////////////////////////////////////////////
			// extension된 플러그인 을 찾아 실행 구문 (sh.jang)
			/* eclipse core 3.x 부터 getDeclaringPluginDescriptor 삭제됨 (sh.jang)
			String installedPath = extensions[i].getDeclaringPluginDescriptor().getInstallURL().getPath();

			Bundle bundle = null;

			try {
				
				bundle = extensions[i].getDeclaringPluginDescriptor().getPlugin().getBundle();

			} catch (Exception e) {
				CodeGenLog.logError(e);
			}
			 */
						
			/* 대체 코드  시작 (sh.jang) */
			
			String pluginId = extensions[i].getNamespaceIdentifier();
			//pluginId = "egovframework.dev.imp.codegen.template.templates";
			Bundle bundle = Platform.getBundle(pluginId);
			
			try {
				if( bundle == null )
					throw new RuntimeException("Could not resolve plugin: " + pluginId + "\r\n" +
						"Probably the plugin has not been correctly installed.\r\n" +
						"Running eclipse from shell with -clean option may rectify installation.");
			
			} catch (Exception e) {
				CodeGenLog.logError(e);
			}
			
			/* resolve Bundle::getEntry to local URL */
			URL pluginURL = null;
			try {
				 pluginURL = Platform.resolve(bundle.getEntry("/"));				 
			} catch (IOException e) {
				throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
			}
			String installedPath = pluginURL.getPath().trim();
			if( installedPath.length() == 0 )
				throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
			
			if( Platform.getOS().compareTo(Platform.OS_WIN32) == 0 )
				installedPath = installedPath.substring(1);
			
			/* 코드 대체 끝 */
			///////////////////////////////////////////////////////////////////////////////
			
			
			CodeGenLog.logInfo(extensions[i].getExtensionPointUniqueIdentifier());

			for (int j = 0; j < configElements.length; j++) {
				WizardsData proxy = parseType(bundle, installedPath, configElements[j], found.size());

				if (proxy != null)
					found.add(proxy);
			}
		}
		cachedWizardsData = (WizardsData[]) found.toArray(new WizardsData[found.size()]);
		return cachedWizardsData;
	}
	
	
	/**
	 * ID 가져오기
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 이름 가져오기
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 타입 분석
	 *
	 * @param bundle
	 * @param installedPath
	 * @param configElement
	 * @param ordinal
	 * @return
	 */
	@SuppressWarnings("unused")
	private static WizardsData parseType(Bundle bundle, String installedPath, IConfigurationElement configElement, int ordinal) {
		if (!configElement.getName().equals("wizards"))
			return null;
		try {
			return new WizardsData(bundle, installedPath, configElement, ordinal);
		} catch (Exception e) {
			String name = configElement.getAttribute("name");
			if (name == null)
				name = "[missing name attribute]";
			String msg = "Failed to load itemType named " + name + " in " + configElement.getDeclaringExtension().getNamespaceIdentifier();

			CodeGenLog.logError(e);
			return null;
		}
	}

	/**
	 * 
	 * 생성자
	 *
	 * @param bundle
	 * @param installedPath
	 * @param configElem
	 * @param ordinal
	 */
	@SuppressWarnings("unused")
	public WizardsData(Bundle bundle, String installedPath, IConfigurationElement configElem, int ordinal) {
		this.configElement = configElem;
		this.ordinal = ordinal;
		this.bundle = bundle;

		id = getAttribute(configElem, "id", null);
		name = getAttribute(configElem, "name", id);

		URL url = FileLocator.find(bundle, new Path(getAttribute(configElem, "wizardsFile", id)), null);

		String wizardsFileName = null;
		try {

			Path path = new Path(getAttribute(configElem, "wizardsFile", id));

			wizardsFileName = FileLocator.toFileURL(url).getFile();

			File f = new File(wizardsFileName);

		} catch (Exception e) {
			CodeGenLog.logError(e);
		}

		wizardsFile = wizardsFileName;

		this.installedPath = installedPath;

	}

	/**
	 * 
	 * 속성값 가져오기
	 *
	 * @param configElem
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	private static String getAttribute(IConfigurationElement configElem, String name, String defaultValue) {
		String value = configElem.getAttribute(name);
		if (value != null)
			return value;
		if (defaultValue != null)
			return defaultValue;
		throw new IllegalArgumentException("Missing " + name + " attribute");
	}

	/**
	 * 초기화
	 */
	private WizardsData() {
		this.id = null;
		this.name = null;
		this.wizardsFile = null;
		this.ordinal = 0;
		this.configElement = null;
		this.installedPath = null;
		this.bundle = null;
	}

	/**
	 * 환경설정 요소 가져오기
	 * 
	 * @return
	 */
	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	/**
	 * ordinal 가져오기
	 * 
	 * @return
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * 번들 가져오기 
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * 위저드 목록 파일 가져오기
	 * 
	 * @return
	 */
	public String getWizardsFile() {
		return wizardsFile;
	}

	/**
	 * 설치 경로 가져오기
	 * 
	 * @return
	 */
	public String getInstalledPath() {
		return installedPath;
	}

	/**
	 * 값 없음 표시
	 */
	public static final WizardsData UNKNOWN = new WizardsData() {
		// 값없음 표시 
	};

}
