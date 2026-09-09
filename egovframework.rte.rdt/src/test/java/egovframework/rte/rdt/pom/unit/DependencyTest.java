package egovframework.rte.rdt.pom.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.xml.SecureSAXBuilder;

/**
 * dependencyManagement 나 BOM 으로 버전을 공급받아 &lt;version&gt; 이 없는 dependency 를
 * 파싱·출력·삽입할 때 NullPointerException 이 나지 않는지 검증한다.
 */
public class DependencyTest {

	private static final String POM = "<project><modelVersion>4.0.0</modelVersion><artifactId>app</artifactId><version>1.0.0</version>"
			+ "<dependencies>\n  <dependency>\n    <groupId>org.springframework</groupId>\n    <artifactId>spring-core</artifactId>\n    <version>4.3.0</version>\n  </dependency>\n"
			+ "  <dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter</artifactId>\n  </dependency>\n</dependencies></project>";

	private static Element element(String name, String text) {
		Element e = new Element(name);
		e.setText(text);
		return e;
	}

	private static Element dependencyElement(String groupId, String artifactId, String version) {
		Element e = new Element("dependency");
		e.addContent(element("groupId", groupId));
		e.addContent(element("artifactId", artifactId));
		if (version != null) {
			e.addContent(element("version", version));
		}
		return e;
	}

	private static PomObject parse(String xml) throws Exception {
		Document doc = new SecureSAXBuilder().build(new StringReader(xml));
		return (PomObject) PomParser.generatePomObjectFromXml(doc);
	}

	@Test
	public void dependencyWithoutVersionHasNullVersion() {
		Dependency d = new Dependency(dependencyElement("g", "a", null));
		assertNull(d.getVersion());
		assertEquals("g.a", d.getId());
	}

	@Test
	public void toStringOfDependencyWithoutVersionDoesNotThrow() {
		Dependency d = new Dependency(dependencyElement("g", "a", null));
		assertTrue(d.toString().contains("version=null"));
	}

	@Test
	public void pomWithVersionlessDependencyIsParsed() throws Exception {
		PomObject pom = parse(POM);
		Dependency managed = pom.getDependencies().get("org.springframework.boot.spring-boot-starter");
		assertNotNull(managed);
		assertNull(managed.getVersion());
		assertEquals("4.3.0", pom.getDependencies().get("org.springframework.spring-core").getVersion().getRealVersion());
	}

	@Test
	public void insertDependencyWithoutVersionOmitsVersionElement() throws Exception {
		PomObject pom = parse(POM);
		pom.insertDependency(new Dependency(dependencyElement("g", "managed", null)));
		Element inserted = pom.getDependencies().get("g.managed").getElement();
		assertEquals("managed", inserted.getChildText("artifactId"));
		assertNull(inserted.getChild("version"));
	}

	@Test
	public void insertDependencyWithVersionWritesVersionElement() throws Exception {
		PomObject pom = parse(POM);
		pom.insertDependency(new Dependency(dependencyElement("g", "lib", "2.0.0")));
		Element inserted = pom.getDependencies().get("g.lib").getElement();
		assertEquals("2.0.0", inserted.getChildText("version"));
	}
}
