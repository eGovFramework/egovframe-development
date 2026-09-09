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
}
