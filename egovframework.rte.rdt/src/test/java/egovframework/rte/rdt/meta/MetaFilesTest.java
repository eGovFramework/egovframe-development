package egovframework.rte.rdt.meta;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.junit.Test;

import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.pom.unit.Dependency;
import egovframework.rte.rdt.pom.unit.PomObject;
import egovframework.rte.rdt.service.parser.ServicesParser;
import egovframework.rte.rdt.service.unit.Service;
import egovframework.rte.rdt.xml.SecureSAXBuilder;

/**
 * 플러그인이 배포하는 메타 파일(services.xml, pom_master.xml)이 서로 맞물리는지 검증한다.
 */
public class MetaFilesTest {

	private static Document load(String resource) throws Exception {
		InputStream in = MetaFilesTest.class.getClassLoader().getResourceAsStream(resource);
		try {
			return new SecureSAXBuilder().build(in);
		} finally {
			in.close();
		}
	}

	/**
	 * services.xml 의 dependency ID 는 마스터 pom 의 dependency 맵을 조회하는 키로 그대로 쓰인다.
	 * 마스터에 없는 ID 가 있으면 그 서비스는 설치할 때 NullPointerException 이 나고 업데이트 판정에서도 빠진다.
	 */
	@Test
	public void everyServiceDependencyExistsInMasterPom() throws Exception {
		Map<String, Dependency> master = ((PomObject) PomParser.generatePomObjectFromXml(load("meta/pom_master.xml")))
				.getDependencies();
		List<Service> services = ServicesParser.generateServiceObjectFromXml(load("meta/services.xml"));
		assertFalse(services.isEmpty());

		List<String> missing = new ArrayList<String>();
		for (Service service : services) {
			assertFalse(service.getName() + " 서비스에 dependency 가 없다", service.getDependency().isEmpty());
			for (String dependencyId : service.getDependency()) {
				if (!master.containsKey(dependencyId)) {
					missing.add(service.getName() + " -> " + dependencyId);
				}
			}
		}
		assertTrue("마스터 pom 에 없는 dependency: " + missing, missing.isEmpty());
	}
}
