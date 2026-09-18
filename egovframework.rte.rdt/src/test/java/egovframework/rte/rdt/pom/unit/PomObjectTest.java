package egovframework.rte.rdt.pom.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import org.jdom.Document;
import org.junit.Test;

import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.xml.SecureSAXBuilder;

/**
 * PomObject 가 프로퍼티를 변경할 때 프로퍼티 섹션이나 키가 없는 pom 을 안전하게 다루는지 검증한다.
 */
public class PomObjectTest {

	private static final String POM_WITH_PROPERTIES = "<project><modelVersion>4.0.0</modelVersion><artifactId>app</artifactId><version>1.0.0</version>"
			+ "<properties><spring.version>4.2.0</spring.version></properties></project>";

	private static final String POM_WITH_PROPERTY_VERSIONS = "<project>\n\t<modelVersion>4.0.0</modelVersion>\n\t<artifactId>app</artifactId>\n\t<version>1.0.0</version>\n"
			+ "\t<properties>\n\t\t<spring.framework.version>6.2.9</spring.framework.version>\n\t</properties>\n"
			+ "\t<dependencies>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>org.springframework</groupId>\n\t\t\t<artifactId>spring-core</artifactId>\n\t\t\t<version>${spring.framework.version}</version>\n\t\t</dependency>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>org.springframework</groupId>\n\t\t\t<artifactId>spring-beans</artifactId>\n\t\t\t<version>${spring.framework.version}</version>\n\t\t</dependency>\n"
			+ "\t</dependencies>\n</project>";

	private static final String POM_WITH_LITERAL_VERSION = "<project>\n\t<modelVersion>4.0.0</modelVersion>\n\t<artifactId>app</artifactId>\n\t<version>1.0.0</version>\n"
			+ "\t<dependencies>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>org.springframework</groupId>\n\t\t\t<artifactId>spring-core</artifactId>\n\t\t\t<version>6.2.9</version>\n\t\t</dependency>\n"
			+ "\t</dependencies>\n</project>";

	private static final String POM_WITH_CHAINED_PROPERTY = "<project>\n\t<modelVersion>4.0.0</modelVersion>\n\t<artifactId>app</artifactId>\n\t<version>1.0.0</version>\n"
			+ "\t<properties>\n\t\t<spring.version>6.2.9</spring.version>\n\t\t<lib.version>${spring.version}</lib.version>\n\t</properties>\n"
			+ "\t<dependencies>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>org.springframework</groupId>\n\t\t\t<artifactId>spring-core</artifactId>\n\t\t\t<version>${lib.version}</version>\n\t\t</dependency>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>org.springframework</groupId>\n\t\t\t<artifactId>spring-beans</artifactId>\n\t\t\t<version>${spring.version}</version>\n\t\t</dependency>\n"
			+ "\t</dependencies>\n</project>";

	private static final String POM_WITH_UNRELATED_LIBS_SHARING_PROPERTY = "<project>\n\t<modelVersion>4.0.0</modelVersion>\n\t<artifactId>app</artifactId>\n\t<version>1.0.0</version>\n"
			+ "\t<properties>\n\t\t<common.version>2.0.0</common.version>\n\t</properties>\n"
			+ "\t<dependencies>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>g</groupId>\n\t\t\t<artifactId>lib-a</artifactId>\n\t\t\t<version>${common.version}</version>\n\t\t</dependency>\n"
			+ "\t\t<dependency>\n\t\t\t<groupId>g</groupId>\n\t\t\t<artifactId>lib-b</artifactId>\n\t\t\t<version>${common.version}</version>\n\t\t</dependency>\n"
			+ "\t</dependencies>\n</project>";

	private static final String POM_WITHOUT_PROPERTIES = "<project><modelVersion>4.0.0</modelVersion><artifactId>app</artifactId><version>1.0.0</version></project>";

	private static PomObject parse(String xml) throws Exception {
		Document doc = new SecureSAXBuilder().build(new StringReader(xml));
		return (PomObject) PomParser.generatePomObjectFromXml(doc);
	}

	@Test
	public void changePropertyUpdatesElementAndMap() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTIES);
		pom.changeProperty("spring.version", "4.3.0");
		assertEquals("4.3.0", pom.getProperties().getValue("spring.version").toString());
		assertEquals("4.3.0", pom.getDocument().getRootElement().getChild("properties").getChildText("spring.version"));
	}

	@Test
	public void changePropertyWithUnknownKeyDoesNothing() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTIES);
		pom.changeProperty("unknown.version", "4.3.0");
		assertNull(pom.getProperties().getValue("unknown.version"));
		assertEquals("4.2.0", pom.getProperties().getValue("spring.version").toString());
	}

	@Test
	public void changePropertyWithoutPropertiesSectionDoesNothing() throws Exception {
		PomObject pom = parse(POM_WITHOUT_PROPERTIES);
		pom.changeProperty("spring.version", "4.3.0");
		assertNull(pom.getProperties());
	}

	// --- 프로퍼티로 지정된 버전의 변경

	@Test
	public void changeVersionOfPropertyVersionUpdatesPropertyNotVersionElement() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"));

		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
		assertEquals("${spring.framework.version}",
				pom.getDependencies().get("org.springframework.spring-core").getElement().getChildText("version"));
	}

	@Test
	public void changeVersionOfPropertyVersionKeepsModulesOnTheSameVersion() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"));

		assertEquals("${spring.framework.version}",
				pom.getDependencies().get("org.springframework.spring-beans").getElement().getChildText("version"));
		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
	}

	@Test
	public void changeVersionOfLiteralVersionUpdatesVersionElement() throws Exception {
		PomObject pom = parse(POM_WITH_LITERAL_VERSION);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"));

		assertEquals("6.2.11",
				pom.getDependencies().get("org.springframework.spring-core").getElement().getChildText("version"));
	}

	// --- 프로퍼티 버전 변경의 경계 조건

	@Test
	public void changeVersionWritesRealVersionWhenNewVersionIsPropertyReference() throws Exception {
		PomObject master = parse(POM_WITH_PROPERTY_VERSIONS.replace("6.2.9", "6.2.11"));
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core",
				master.getDependencies().get("org.springframework.spring-core").getVersion());

		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
	}

	@Test
	public void changeVersionOfChainedPropertyUpdatesLastPropertyInChain() throws Exception {
		PomObject pom = parse(POM_WITH_CHAINED_PROPERTY);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"));

		assertEquals("6.2.11", pom.getProperties().getValue("spring.version").toString());
		assertEquals("${spring.version}", pom.getProperties().getValue("lib.version").toString());
		assertEquals("6.2.11", pom.getDependencies().get("org.springframework.spring-beans").getVersion().getRealVersion());
	}

	@Test
	public void changeVersionDoesNotLowerSharedProperty() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"));
		pom.changeVersion("org.springframework.spring-beans", new Version("6.2.10"));

		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
	}

	@Test
	public void changePropertyRefreshesRealVersionOfDependencies() throws Exception {
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeProperty("spring.framework.version", "6.2.11");

		Version version = pom.getDependencies().get("org.springframework.spring-beans").getVersion();
		assertEquals("6.2.11", version.getRealVersion());
		assertEquals("${spring.framework.version}", version.getContent());
	}

	// --- 마스터 pom 기준으로 검증한 버전 변경

	private static String masterPom(String... idsAndVersions) {
		StringBuilder sb = new StringBuilder("<project>\n\t<modelVersion>4.0.0</modelVersion>\n\t<artifactId>master</artifactId>\n\t<version>1.0.0</version>\n\t<dependencies>\n");
		for (int i = 0; i < idsAndVersions.length; i += 3) {
			sb.append("\t\t<dependency>\n\t\t\t<groupId>").append(idsAndVersions[i]).append("</groupId>\n\t\t\t<artifactId>")
					.append(idsAndVersions[i + 1]).append("</artifactId>\n\t\t\t<version>").append(idsAndVersions[i + 2])
					.append("</version>\n\t\t</dependency>\n");
		}
		return sb.append("\t</dependencies>\n</project>").toString();
	}

	@Test
	public void changeVersionUpdatesPropertyWhenAllSharersHaveSameMasterVersion() throws Exception {
		PomObject master = parse(masterPom("org.springframework", "spring-core", "6.2.11", "org.springframework", "spring-beans", "6.2.11"));
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"), master.getDependencies());

		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
		assertEquals("${spring.framework.version}",
				pom.getDependencies().get("org.springframework.spring-core").getElement().getChildText("version"));
	}

	@Test
	public void changeVersionLeavesPropertyWhenSharerIsNotInMaster() throws Exception {
		PomObject master = parse(masterPom("g", "lib-a", "2.5.0"));
		PomObject pom = parse(POM_WITH_UNRELATED_LIBS_SHARING_PROPERTY);
		pom.changeVersion("g.lib-a", new Version("2.5.0"), master.getDependencies());

		assertEquals("2.0.0", pom.getProperties().getValue("common.version").toString());
		assertEquals("2.5.0", pom.getDependencies().get("g.lib-a").getElement().getChildText("version"));
		assertEquals("2.5.0", pom.getDependencies().get("g.lib-a").getVersion().getRealVersion());
		assertEquals("${common.version}", pom.getDependencies().get("g.lib-b").getElement().getChildText("version"));
		assertEquals("2.0.0", pom.getDependencies().get("g.lib-b").getVersion().getRealVersion());
	}

	@Test
	public void changeVersionSplitsSharersWithDifferentMasterVersions() throws Exception {
		PomObject master = parse(masterPom("g", "lib-a", "2.5.0", "g", "lib-b", "2.1.0"));
		PomObject pom = parse(POM_WITH_UNRELATED_LIBS_SHARING_PROPERTY);
		pom.changeVersion("g.lib-a", new Version("2.5.0"), master.getDependencies());
		pom.changeVersion("g.lib-b", new Version("2.1.0"), master.getDependencies());

		// lib-a 가 리터럴로 떨어져 나간 뒤에는 lib-b 만 프로퍼티를 쓰므로 프로퍼티를 고쳐도 된다.
		assertEquals("2.5.0", pom.getDependencies().get("g.lib-a").getElement().getChildText("version"));
		assertEquals("2.1.0", pom.getProperties().getValue("common.version").toString());
		assertEquals("${common.version}", pom.getDependencies().get("g.lib-b").getElement().getChildText("version"));
		assertEquals("2.1.0", pom.getDependencies().get("g.lib-b").getVersion().getRealVersion());
	}

	@Test
	public void changeVersionTreatsEquivalentMasterVersionsAsSame() throws Exception {
		PomObject master = parse(masterPom("org.springframework", "spring-core", "6.2.11", "org.springframework", "spring-beans", "6.2.11.RELEASE"));
		PomObject pom = parse(POM_WITH_PROPERTY_VERSIONS);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"), master.getDependencies());

		assertEquals("6.2.11", pom.getProperties().getValue("spring.framework.version").toString());
	}

	@Test
	public void changeVersionWithMasterOfLiteralVersionUpdatesVersionElement() throws Exception {
		PomObject master = parse(masterPom("org.springframework", "spring-core", "6.2.11"));
		PomObject pom = parse(POM_WITH_LITERAL_VERSION);
		pom.changeVersion("org.springframework.spring-core", new Version("6.2.11"), master.getDependencies());

		assertEquals("6.2.11",
				pom.getDependencies().get("org.springframework.spring-core").getElement().getChildText("version"));
	}
}
