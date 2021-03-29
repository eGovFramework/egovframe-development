/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.rte.rdt.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 플러그인의 life cycle을 통제하는 activator 클래스 
 * @author 이영진
 */
public class Activator extends AbstractUIPlugin {

	/** plug-in ID */
	public static final String PLUGIN_ID = "egovframework.rte.rdt"; 

	/** 전역 플러그인 객체 */
	private static Activator plugin;
	
	/**
	 * Activator 기본 생성자
	 */
	public Activator() {
	}

	/**
	 * 프로그램이 시작될때 호출되는 메소드
	 * @param context
	 * @throws Exception
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}


	/**
	 * 프로그램이 종료될때 호출되는 메소드
	 * @param context
	 * @throws Exception
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * 전역 플러그인 객체를 리턴한다
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
