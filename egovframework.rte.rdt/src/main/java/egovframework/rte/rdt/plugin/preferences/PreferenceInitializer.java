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
package egovframework.rte.rdt.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import egovframework.rte.rdt.plugin.Activator;

/**
 * 환경설정에서 사용되는 설정들의 기본값을 정의하는 클래스
 * @author 이영진
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * 환경설정에서 사용되는 값들을 기본값으로 초기화한다.
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(RdtPreferencePage.DEFAULT_PROPERTY, false);
		store.setDefault(RdtPreferencePage.MASTER_FILEPATH_PROPERTY, "");
		store.setDefault(RdtPreferencePage.SERVICE_FILEPATH_PROPERTY, "");
		store.setDefault(RdtPreferencePage.MVN_PROPERTY, "");
	}

}
