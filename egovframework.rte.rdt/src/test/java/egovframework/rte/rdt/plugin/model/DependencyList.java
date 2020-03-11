package egovframework.rte.rdt.plugin.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import egovframework.rte.rdt.plugin.editor.RdtEditor;
import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.pom.unit.Dependency;
import egovframework.rte.rdt.pom.unit.Pom;
import egovframework.rte.rdt.pom.unit.PomObject;
import egovframework.rte.rdt.pom.unit.Version;
import egovframework.rte.rdt.pom.util.StringHelper;
import egovframework.rte.rdt.service.parser.ServicesParser;
import egovframework.rte.rdt.service.unit.Service;

public class DependencyList {

	private RdtEditor instance;

	private ArrayList<Dependency> installedList;
	private ArrayList<Dependency> notInstalledList;

	private final String MASTER_FILENAME = "egov/pom_master.xml";
	private final String POM_FILENAME = "pom.xml";
	@SuppressWarnings("unused")
	private final String SERVICES_FILENAME = "egov/services.xml";

	public DependencyList(RdtEditor instance) {

		this.instance = instance;
		installedList = new ArrayList<Dependency>();
		notInstalledList = new ArrayList<Dependency>();
		insertList();

	}

	public ArrayList<Dependency> getInstalledList() {
		return installedList;
	}

	public ArrayList<Dependency> getNotInstalledList() {
		return notInstalledList;
	}

	/**
	 * 설치된것과 미설치된것을 비교,구분해 리스트를 만들어 놓는다.
	 */
	public void insertList() {
		Object[] currentPomDependencyArray = null; // 현재 pom.xml 읽어서 저장

		/** currentPomDependencyArray와 비교해 설치, 미설치 구분을 위한 정보를 저장 */
		Object[] dependencyInfoArray = null;

		try {
			currentPomDependencyArray = getDependencyList(POM_FILENAME).toArray();
			dependencyInfoArray = getDependencyList(MASTER_FILENAME).toArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < dependencyInfoArray.length; i++) {
			Dependency notInsDependency = (Dependency) dependencyInfoArray[i];
			for (int j = 0; j < currentPomDependencyArray.length; j++) {
				Dependency insDependency = (Dependency) currentPomDependencyArray[j];
				if (notInsDependency.getArtifactId().equals(
						insDependency.getArtifactId())) {
					notInsDependency.setIsinstalled(true);
					// 최신버전 저장
					notInsDependency.setLastestVersion(notInsDependency
							.getVersion());
					// 설치된 버전저장
					notInsDependency.setVersion(insDependency.getVersion());
				}
			}
		}

		for (int i = 0; i < dependencyInfoArray.length; i++) {
			if (((Dependency) dependencyInfoArray[i]).isIsinstalled()) {
				installedList.add((Dependency) dependencyInfoArray[i]);
			} else {
				notInstalledList.add((Dependency) dependencyInfoArray[i]);
			}
		}
	}

	public ArrayList<Service> getServiceList(String fileName) throws PomException {
		ArrayList<Service> svsList = null;
		try {
			IFile ifile = instance.getCurrentProject().getFile(fileName);
			File file = new File(ifile.getLocationURI());

			svsList = ServicesParser.parse(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return svsList;
	}

	public List<Dependency> getDependencyList(String fileName) {

		List<Dependency> list = null;
		try {
			IFile ifile = instance.getCurrentProject().getFile(fileName);
			File file = new File(ifile.getLocationURI());

			Pom pom = PomParser.parse(file);
			list = pom.listDependencies();

		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
		return list;
	}

	public void InstallDependency(Object[] InstallList) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());

			Pom pom = PomParser.parse(file);
			for (int i = 0; i < InstallList.length; i++) {
				pom.insertDependency((Dependency) InstallList[i]);
			}

			pom.commit(file);

		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
	}

	public void UninstallDependency(Object[] UninstallList) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());

			Pom pom = PomParser.parse(file);
			for (int i = 0; i < UninstallList.length; i++) {
				Dependency d = (Dependency) UninstallList[i];
				/** remove할때는 groupId.artifactId의 dependencyId를 이용한다. */
				String dependencyId = StringHelper.concatNameWithDot(
						d.getGroupId(), d.getArtifactId());
				pom.removeDependency(dependencyId);
			}

			pom.commit(file);

		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
	}

	public void UpdateDependency(Object[] updateList) {
		UninstallDependency(updateList);

		for (int i = 0; i < updateList.length; i++) {
			Dependency d = (Dependency) updateList[i];
			if (d.getLastestVersion().isPropertyVersion()) { // property가 있는 경우는
				changeProperty(d.getLastestVersion()); // property를 수정한다.
			}
			d.setVersion(d.getLastestVersion()); // 최신버전을 현재버전에 넣어준다.
		}

		InstallDependency(updateList);

	}

	public void changeProperty(Version version) {
		try {
			IFile ifile = instance.getCurrentProject().getFile(POM_FILENAME);
			File file = new File(ifile.getLocationURI());

			PomObject pom = (PomObject) PomParser.parse(file);

			pom.changeProperty(StringHelper.getProperty(version.toString()),
					version.getRealVersion().toString());
			pom.commit(file);
		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}
	}
}
