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
package egovframework.dev.imp.codegen.model;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 모델 기반 코드젠 플로그인 라이프 사이클을 관리하는 Activator 클래스
 * <p><b>NOTE:</b> 플러그인 로딩, 종료 이벤트 처리. 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class EgovModelCodeGenPlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.dev.imp.codegen.model";

	/** 플러그인 인스턴스 */
	private static EgovModelCodeGenPlugin plugin;
	
	/** 리소스 번들 인스턴스 */
	private ResourceBundle bundle;	
	/**
     * 생성자
     */
	public EgovModelCodeGenPlugin() {
	}

    /**
     * 플로그인 시작
     * 
     * @param context
     */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		try {
			bundle = ResourceBundle.getBundle("egovframework.dev.imp.codegen.model.resource");
		} catch (MissingResourceException x) {
			bundle = null;
		}		
	}

    /**
     * 플러그인 종료
     * 
     * @param context
     */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

    /**
     * 플러그인 인스턴스 가져오기
     *
     * @return 플러그인 인스턴스
     */
	public static EgovModelCodeGenPlugin getDefault() {
		return plugin;
	}

    /**
     * 이미지 디스크립터 가져오기 
     * 
	 * @param path
     * @return 이미지 디스크립터
     */
	public ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * 리소스 메시지 가져오기 
	 * 
	 * @param string
	 * @return
	 */
	public String getResourceString(String string) {
		return bundle.getString(string);
	}
}
