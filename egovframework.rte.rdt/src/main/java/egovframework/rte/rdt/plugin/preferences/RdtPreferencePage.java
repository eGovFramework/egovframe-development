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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import egovframework.rte.rdt.plugin.Activator;

/**
 * 환경설정 페이지에 관한 클래스
 * @author 이영진
 */
public class RdtPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** 파일직접선택 모드 라벨 */
	public static final String DEFAULT_TITLE = "&Use local meta file";
	/** 파일직접선택 모드 이름*/
	public static final String DEFAULT_PROPERTY = "RDT_DEFAULT_MODE";
	/** Master pom 파일위치에 대한 라벨 */
	public static final String MASTER_FILEPATH_TITLE = "&Master pom file location :";
	/** Master pom 파일위치에 대한 이름 */
	public static final String MASTER_FILEPATH_PROPERTY = "RDT_MASTER_FILEPATH";
	/** Servies 파일위치에 대한 라벨 */
	public static final String SERVICE_FILEPATH_TITLE = "&Service file location :";
	/** Servies 파일위치에 대한 이름 */
	public static final String SERVICE_FILEPATH_PROPERTY = "RDT_SERVICE_FILEPATH";
	/** maven 디렉토리설정에 대한 라벨 */
	public static final String MVN_HOME_TITLE = "Ma&ven directory :";
	/** maven 디렉토리설정에 대한 이름 */
	public static final String MVN_PROPERTY = "RDT_MAVEN";
	/** 파일선택모드를 입력받는다 */
	private BooleanFieldEditor mode;
	/** Master pom 파일위치를 입력받는다 */
	private FileFieldEditor masterFilePath;
	/** Servies 파일위치를 입력받는다 */
	private FileFieldEditor serviceFilePath;

	/**
	 * RdtPreferencePage 생성자
	 */
	public RdtPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("RTE Distribution Tool General Preferences");
	}

	/**
	 * 환경설정페이지의 FieldEditor들을 생성한다.
	 */
	public void createFieldEditors() {

		/**
		 * default mode 가 false 일 경우는 resources/meta 폴더 안의 pom_master.xml 파일과
		 * services.xml을 사용한다.
		 * 
		 * true 일 경우는 각각 masterFileFieldEditor, serviceFileFieldEditor 에 입력된
		 * 파일위치를 사용한다.
		 */

		mode = new BooleanFieldEditor(DEFAULT_PROPERTY, DEFAULT_TITLE, getFieldEditorParent());
		addField(mode);

		masterFilePath = new FileFieldEditor(MASTER_FILEPATH_PROPERTY, MASTER_FILEPATH_TITLE, getFieldEditorParent());
		masterFilePath.setFileExtensions(new String[] { "*.xml" });
		addField(masterFilePath);

		serviceFilePath = new FileFieldEditor(SERVICE_FILEPATH_PROPERTY, SERVICE_FILEPATH_TITLE, getFieldEditorParent());
		serviceFilePath.setFileExtensions(new String[] { "*.xml" });
		serviceFilePath.setChangeButtonText("B&rowse...");
		addField(serviceFilePath);

		DirectoryFieldEditor mavenPath = new DirectoryFieldEditor(MVN_PROPERTY, MVN_HOME_TITLE, getFieldEditorParent());
		mavenPath.setChangeButtonText("Br&owse...");
		addField(mavenPath);

		//파일직접선택 모드가 false면 텍스트박스 비활성화
		if (getPreferenceStore().getString(DEFAULT_PROPERTY).equals("false"))
			changeVisibleFieldEditor();
	}

	/**
	 * 초기화 할 내용을 정의한다.
	 * @param workbench
	 */
	public void init(IWorkbench workbench) {

	}

	/**
	 * 파일을 직접선택하는 mode가 변경되면 실행할 내용을 지정한다.
	 * @param event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE)) {
			if (event.getSource() == mode) {
				changeVisibleFieldEditor();
			}
		}
	}

	/**
	 * masterFilePath와 serviceFilePath의 활성화, 비활성화상태로 변경한다.
	 */
	public void changeVisibleFieldEditor() {
		masterFilePath.setEnabled(mode.getBooleanValue(), getFieldEditorParent());
		serviceFilePath.setEnabled(mode.getBooleanValue(), getFieldEditorParent());
	}
}