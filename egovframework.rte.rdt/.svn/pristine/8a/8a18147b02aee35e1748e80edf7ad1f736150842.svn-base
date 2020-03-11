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
package egovframework.rte.rdt.plugin.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import egovframework.rte.rdt.plugin.Activator;
import egovframework.rte.rdt.plugin.editor.RdtEditor;
import egovframework.rte.rdt.plugin.message.Messages;
import egovframework.rte.rdt.plugin.preferences.RdtPreferencePage;
import egovframework.rte.rdt.plugin.util.ProjectUtil;
import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.pom.unit.Dependency;
import egovframework.rte.rdt.pom.unit.Pom;
import egovframework.rte.rdt.pom.unit.PomObject;
import egovframework.rte.rdt.pom.unit.Version;
import egovframework.rte.rdt.pom.util.StringHelper;
import egovframework.rte.rdt.service.parser.ServicesParser;
import egovframework.rte.rdt.service.unit.Service;

/**
 * 설치된 서비스와 미설치된 서비스를 분류하여 저장해놓는다.
 * @author 이영진
 */
public class TableList {

	/** 부모 Composite을 저장 */
	private RdtEditor instance;
	/** 설치된 서비스 목록 */
	private ArrayList<Service> installedList;
	/** 미설치된 서비스 목록 */
	private ArrayList<Service> notInstalledList;
	/** 참조할때 사용될 모든 라이브러리 목록 */
	private static Map<String, Dependency> allDpendencyMap;
	/** 설치된 라이브러리 목록 */
	private static Map<String, Dependency> insDMap; // installed Dependency Map
	/** 리소스내의 마스터파일이름(필요한 모든 라이브러리가 정의되어있다) */
	private String MASTER_FILENAME = "src/main/resources/meta/pom_master.xml";
	/** 리소스내의 서비스파일이름(서비스의 의존성이 정의되어 있다) */
	private String SERVICES_FILENAME = "src/main/resources/meta/services.xml";
	/** 해당 프로젝트의 pom 파일이름(보통 프로젝트 root의 pom.xml) */
	private String POM_FILENAME = "pom.xml";
	/** 모든 라이브러리 목록이 정의된 메타파일 */
	private String masterFile;
	/** 모든 서비스 목록이 정의된 메타파일 */
	private String servicesFile;
	/** Preference의 정보를 가져오기 위한 Store */
	private IPreferenceStore store;

	/**
	 * TableList의 생성자
	 * use local meta file 체크박스가 false이면 메타파일의 위치를 저장해 놓는다.
	 * @param instance
	 */
	public TableList(RdtEditor instance) {
		store = Activator.getDefault().getPreferenceStore();

		/**
		 * default mode 가 false 일 경우는 프로그램에 내장된 
		 * resources/meta 폴더 안의 pom_master.xml 파일과 services.xml을 사용한다.
		 */
		if (!store.getBoolean(RdtPreferencePage.DEFAULT_PROPERTY)) {
			// resources의 마스터파일과 서비스목록의 위치를 저장
			URL masterUrl = FileLocator.find(Activator.getDefault().getBundle(), new Path(MASTER_FILENAME), null);
			URL servicesUrl = FileLocator.find(Activator.getDefault().getBundle(), new Path(SERVICES_FILENAME), null);
			try {
				masterFile = FileLocator.toFileURL(masterUrl).getFile();
				servicesFile = FileLocator.toFileURL(servicesUrl).getFile();
			} catch (IOException e) {
				// 마스터파일과 서비스목록 가져오기 실패
				e.printStackTrace();
			}
		}

		/**
		 * true 일 경우는 각각 MASTER_FILEPATH_PROPERTY, SERVICE_FILEPATH_PROPERTY 에 
		 * 입력된 파일위치를 사용한다. 
		 */
		else {
			masterFile = store.getString(RdtPreferencePage.MASTER_FILEPATH_PROPERTY);
			servicesFile = store.getString(RdtPreferencePage.SERVICE_FILEPATH_PROPERTY);
		}

		this.instance = instance;
		installedList = null;
		notInstalledList = null;
		allDpendencyMap = null;
		insDMap = null;
		insertList();

	}

	/**
	 * 설치된 서비스들을 조회한다.
	 * @return ArrayList<Service> 설치된 서비스 목록
	 */
	public ArrayList<Service> getInstalledList() {
		return installedList;
	}

	/**
	 * 설치되지 않은 서비스들을 조회한다.
	 * @return ArrayList<Service> 미설치된 서비스 목록
	 */
	public ArrayList<Service> getNotInstalledList() {
		return notInstalledList;
	}

	/**
	 * 라이브러리 목록에서 특정 라이브러리를 검색한다.
	 * @param dependencyId 찾으려는 라이브러리의 dependencyId
	 * @return Dependency 검색된 라이브러리
	 */
	static public Dependency searchDependency(String dependencyId) {
		return allDpendencyMap.get(dependencyId);
	}

	/**
	 * 버전을 조회한다. 
	 * @param dependencyId 버전을 조회할 dependencyId
	 * @return String 조회된 버전
	 */
	static public String getVersion(String dependencyId) {
		Dependency d = searchDependency(dependencyId);
		if (d != null) {
			return d.getVersion().toString();
		} else {
			return null;
		}
	}

	/**
	 * 업데이트 가능여부를 조회한다.
	 * @param service 업데이트가 가능여부를 판단할 service
	 * @return boolean 업데이트 가능여부
	 */
	static public boolean isUpdate(Service service) {
		for (String s : service.getDependency()) {
			if (insDMap.get(s) != null && allDpendencyMap.get(s) != null) {
				if (insDMap.get(s).getVersion().compareTo(allDpendencyMap.get(s).getVersion()) < 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Services 파일과 Master pom파일, 프로젝트의 pom파일을 읽어서 저장한다.
	 */
	public void insertList() {

		try {
			// master pom파일내 모든 dependency를 읽어놓는다
			allDpendencyMap = getDependencyList(masterFile);
			if (allDpendencyMap.size() == 0) {
				PomException pe = new PomException();
				pe.setErrorCode(PomException.FILE_IO_ERROR);
				throw pe;
			}
			// pom파일의 설치된 dependency를 읽어놓는다
			insDMap = getDependencyList(POM_FILENAME);

		} catch (PomException pe) {
			String errorMessage = null;
			switch (pe.getErrorCode()) {
				case PomException.FILE_NOT_FOUND_ERROR:
					errorMessage = Messages.TableList_3;
					break;
				case PomException.FILE_IO_ERROR:
					errorMessage = Messages.TableList_4;
					break;
				case PomException.PARSING_XML_ERROR:
					errorMessage = Messages.TableList_5;
					break;
				default:
					errorMessage = Messages.TableList_6;
					break;
			}

			ProjectUtil.errorBox(instance.getShell(), errorMessage + ProjectUtil.ENTER + pe.getErrorDetail());
			return;
		}

		// 비교해서 설치된 서비스는 installedList에 넣는다.
		notInstalledList = new ArrayList<Service>();
		installedList = new ArrayList<Service>();

		ArrayList<Service> svcList;
		try {
			svcList = getServiceList(servicesFile);
		} catch (PomException pe) {
			String errorMessage = null;
			switch (pe.getErrorCode()) {
				case PomException.FILE_NOT_FOUND_ERROR:
					errorMessage = Messages.TableList_7;
					break;
				case PomException.FILE_IO_ERROR:
					errorMessage = Messages.TableList_8;
					break;
				case PomException.PARSING_XML_ERROR:
					errorMessage = Messages.TableList_9;
					break;
				default:
					errorMessage = Messages.TableList_10;
					break;
			}

			ProjectUtil.errorBox(instance.getShell(), errorMessage + ProjectUtil.ENTER + pe.getErrorDetail());
			return;

		}

		if (svcList != null) {
			for (Service service : svcList) {
				if (isInstalled(service)) {
					installedList.add(service);
				} else {
					notInstalledList.add(service);
				}
			}
		}
	}

	/**
	 * 설치 가능여부를 조회한다.
	 * @param service
	 * @return boolean 설치 가능여부
	 */
	public boolean isInstalled(Service service) {

		for (String s : service.getDependency()) { // s는 각서비스 마다 필요한 dependency들의 Id
			if (insDMap.get(s) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Services 파일을 읽어서 저장한다.
	 * @param fileName
	 * @return ArrayList<Service> 읽어들인 서비스목록
	 * @throws PomException
	 */
	public ArrayList<Service> getServiceList(String fileName) throws PomException {
		ArrayList<Service> svsList = null;

		File file;

		//프로젝트내의 services.xml 읽어들이는 경우
		if (SERVICES_FILENAME.equals(fileName)) {
			IFile ifile = instance.getCurrentProject().getFile(fileName);
			file = new File(ifile.getLocationURI());
		}
		//사용자가 입력한 위치의 services.xml을 읽을 경우
		else {
			file = new File(fileName);
			if (!file.exists()) {
				PomException pe = new PomException();
				pe.setErrorCode(PomException.FILE_NOT_FOUND_ERROR);
				pe.setErrorDetail(fileName);
				throw pe;
			}
		}

		try {
			svsList = ServicesParser.parse(file);
		} catch (Exception e) {
			ProjectUtil.errorBox(instance.getShell(), Messages.TableList_11);
			e.printStackTrace();
		}
		return svsList;
	}

	/**
	 * xml파일을 읽어들여 dependency부분을 저장한다.
	 * @param fileName
	 * @return Map<String, Dependency> 읽어들인 dependency 리스트
	 * @throws PomException
	 */
	public Map<String, Dependency> getDependencyList(String fileName) throws PomException {

		File file;

		//프로젝트내의 pom.xml을 읽어들이는 경우
		if (POM_FILENAME.equals(fileName)) {
			IFile ifile = instance.getCurrentProject().getFile(fileName);
			file = new File(ifile.getLocationURI());
		}
		//플러그인 메타정보의 pom_master.xml 파일을 읽을 경우
		else {
			file = new File(fileName);
			if (!file.exists()) {
				PomException pe = new PomException();
				pe.setErrorCode(PomException.FILE_NOT_FOUND_ERROR);
				pe.setErrorDetail(fileName);
				throw pe;
			}
		}

		PomObject pom = (PomObject) PomParser.parse(file);
		return pom.getDependencies();
	}

	/**
	 * 설치될 서비스 리스트를 분석하여 각 서비스마다
	 * 필요한 dependency들을 찾아 pom파일에 기록한다.
	 * @param InstallList 기록될 서비스들의 리스트
	 */
	public void InstallDependency(Object[] InstallList) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());

			Pom pom = PomParser.parse(file);
			for (Object o : InstallList) {
				Service service = (Service) o;
				for (String s : service.getDependency()) { // s는 각서비스 마다 필요한 dependency들의 Id
					if (!isInstalled(s)) {
						pom.insertDependency(allDpendencyMap.get(s)); // 설치
					} else {
						if (insDMap.get(s).getVersion().compareTo(allDpendencyMap.get(s).getVersion()) < 0) { // 버전이 낮을때
							// 버전수정
							pom.changeVersion(insDMap.get(s).getId(), allDpendencyMap.get(s).getVersion());
						}
					}
				}// end for
			}
			pom.commit(file);

		} catch (PomException pe) {
			ProjectUtil.errorBox(instance.getShell(), Messages.TableList_12);
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		} catch (NullPointerException e) {
			ProjectUtil.errorBox(instance.getShell(), Messages.TableList_12);
			System.out.println(Messages.TableList_14);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 특정 라이브러리의 설치여부를 조회한다.
	 * @param dependencyId
	 * @return boolean 설치여부
	 */
	public boolean isInstalled(String dependencyId) {
		boolean b = false;
		if (insDMap.get(dependencyId) != null)
			b = true;
		return b;
	}

	/**
	 * 삭제될 서비스 리스트를 분석하여 각 서비스마다
	 * 삭제될 dependency들을 찾아 pom파일에서 삭제한다.
	 * @param UninstallList 삭제될 서비스들의 리스트
	 */
	public void UninstallDependency(Object[] UninstallList) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());

			Pom pom = PomParser.parse(file);

			for (Object o : UninstallList) {
				Service service = (Service) o;
				for (String s : service.getDependency()) { // s는 각서비스 마다 필요한 dependency들의 Id
					if (isInstalled(s)) {
						pom.removeDependency(s); // pom파일 삭제
						insDMap.remove(s); // 설치된 dependency목록에서 삭제
					}
				}
			}

			pom.commit(file);
		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
	}

	/**
	 * property를 수정한다.
	 * @param version 변경될 버전
	 */
	public void changeProperty(Version version) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());
			PomObject pom = (PomObject) PomParser.parse(file);

			pom.changeProperty(StringHelper.getProperty(version.toString()), version.getRealVersion().toString());
			pom.commit(file);
		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
	}
}
