package egovframework.rte.rdt.pom.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.xml.SecureSAXBuilder;

/**
 * Version 이 프로퍼티 참조를 해석한 실제 버전으로 비교하고,
 * version 엘레멘트가 없거나 프로퍼티를 해석할 수 없는 경우를 안전하게 다루는지 검증한다.
 */
public class VersionTest {

	private static Element element(String name, String text) {
		Element e = new Element(name);
		e.setText(text);
		return e;
	}

	private static PomMap properties(String... keyValues) {
		Element props = new Element("properties");
		for (int i = 0; i < keyValues.length; i += 2) {
			props.addContent(element(keyValues[i], keyValues[i + 1]));
		}
		return new PomMap(props);
	}

	// --- <properties> 는 있으나 <version> 이 없는 pom (부모 상속 자식 모듈)

	@Test
	public void nullElementWithPropertiesDoesNotThrow() {
		Version v = new Version((Element) null, properties("spring.version", "4.3.0"));
		assertNull(v.getContent());
		assertNull(v.getRealVersion());
		assertFalse(v.isPropertyVersion());
	}

	@Test
	public void pomWithPropertiesButWithoutVersionIsParsed() throws Exception {
		String xml = "<project><modelVersion>4.0.0</modelVersion><artifactId>child</artifactId>"
				+ "<properties><spring.version>4.3.0</spring.version></properties></project>";
		Document doc = new SecureSAXBuilder().build(new StringReader(xml));
		DetailPom pom = PomParser.generatePomObjectFromXml(doc);
		assertNull(pom.getVersion().getContent());
	}

	// --- 프로퍼티 해석 상태

	@Test
	public void literalVersionFromStringHasRealVersion() {
		Version v = new Version("4.2.0");
		assertEquals("4.2.0", v.getRealVersion());
		assertFalse(v.isPropertyVersion());
		assertFalse(v.isUnresolvedProperty());
	}

	@Test
	public void propertyVersionIsResolvedThroughProperties() {
		Version v = new Version(element("version", "${spring.version}"), properties("spring.version", "4.3.0"));
		assertEquals("${spring.version}", v.getContent());
		assertEquals("4.3.0", v.getRealVersion());
		assertTrue(v.isPropertyVersion());
		assertFalse(v.isUnresolvedProperty());
	}

	@Test
	public void unknownPropertyKeyIsUnresolved() {
		Version v = new Version(element("version", "${project.version}"), properties("spring.version", "4.3.0"));
		assertFalse(v.isPropertyVersion());
		assertTrue(v.isUnresolvedProperty());
	}

	@Test
	public void propertyVersionWithoutPropertyMapIsUnresolved() {
		Version v = new Version(element("version", "${spring.version}"));
		assertFalse(v.isPropertyVersion());
		assertTrue(v.isUnresolvedProperty());
	}

	@Test
	public void stringConstructorWithPropertiesResolvesProperty() {
		Version v = new Version("${spring.version}", properties("spring.version", "4.3.0"));
		assertEquals("${spring.version}", v.getContent());
		assertEquals("4.3.0", v.getRealVersion());
		assertTrue(v.isPropertyVersion());
		assertFalse(v.isUnresolvedProperty());
	}

	@Test
	public void stringConstructorWithPropertiesKeepsLiteral() {
		Version v = new Version("4.2.0", properties("spring.version", "4.3.0"));
		assertEquals("4.2.0", v.getRealVersion());
		assertFalse(v.isPropertyVersion());
	}

	// --- 프로퍼티 연쇄 참조

	@Test
	public void chainedPropertyIsResolvedToFinalValue() {
		Version v = new Version(element("version", "${lib.version}"),
				properties("lib.version", "${spring.version}", "spring.version", "4.3.0"));
		assertEquals("${lib.version}", v.getContent());
		assertEquals("4.3.0", v.getRealVersion());
		assertTrue(v.isPropertyVersion());
		assertFalse(v.isUnresolvedProperty());
		assertTrue(v.isOlderThan(new Version("4.4.0")));
		assertFalse(v.isOlderThan(new Version("4.2.0")));
	}

	@Test
	public void chainEndingInUnknownPropertyIsUnresolved() {
		Version v = new Version(element("version", "${lib.version}"), properties("lib.version", "${spring.version}"));
		assertFalse(v.isPropertyVersion());
		assertTrue(v.isUnresolvedProperty());
		assertFalse(v.isOlderThan(new Version("9.9.9")));
	}

	@Test
	public void cyclicPropertyReferenceIsUnresolved() {
		Version v = new Version(element("version", "${a}"), properties("a", "${b}", "b", "${a}"));
		assertFalse(v.isPropertyVersion());
		assertTrue(v.isUnresolvedProperty());
		assertFalse(v.isOlderThan(new Version("9.9.9")));
	}

	// --- 비교

	@Test
	public void compareToUsesResolvedVersion() {
		Version installed = new Version(element("version", "${spring.version}"), properties("spring.version", "4.3.0"));
		Version master = new Version(element("version", "4.2.0"), properties());
		assertTrue(installed.compareTo(master) > 0);
		assertTrue(master.compareTo(installed) < 0);
	}

	@Test
	public void compareToOrdersLiteralVersions() {
		assertTrue(new Version("4.2.0").compareTo(new Version("4.3.0")) < 0);
		assertEquals(0, new Version("4.3.0").compareTo(new Version("4.3.0")));
	}

	@Test
	public void isOlderThanComparesResolvedVersions() {
		assertTrue(new Version("4.2.0").isOlderThan(new Version("4.3.0")));
		assertFalse(new Version("4.3.0").isOlderThan(new Version("4.2.0")));
		assertFalse(new Version("4.3.0").isOlderThan(new Version("4.3.0")));

		Version installed = new Version(element("version", "${spring.version}"), properties("spring.version", "4.3.0"));
		assertFalse(installed.isOlderThan(new Version("4.2.0")));
		assertTrue(installed.isOlderThan(new Version("4.4.0")));
	}

	// --- 자리별 수 비교

	@Test
	public void compareToComparesNumericSegmentsAsNumbers() {
		assertTrue(new Version("6.2.9").compareTo(new Version("6.2.11")) < 0);
		assertTrue(new Version("6.2.11").compareTo(new Version("6.2.9")) > 0);
		assertTrue(new Version("3.9.0").compareTo(new Version("3.10.0")) < 0);
		assertTrue(new Version("1.9").compareTo(new Version("1.10")) < 0);
	}

	@Test
	public void isOlderThanDetectsUpdateOfTwoDigitSegment() {
		assertTrue(new Version("6.2.9").isOlderThan(new Version("6.2.11")));
		assertFalse(new Version("6.2.11").isOlderThan(new Version("6.2.9")));
	}

	@Test
	public void trailingZeroAndReleaseQualifierDoNotChangeVersion() {
		assertEquals(0, new Version("4.3").compareTo(new Version("4.3.0")));
		assertEquals(0, new Version("5.6.15.Final").compareTo(new Version("5.6.15")));
		assertEquals(0, new Version("4.3.0").compareTo(new Version("4.3.0.GA")));
	}

	@Test
	public void snapshotIsOlderThanSameRelease() {
		assertTrue(new Version("4.3.0-SNAPSHOT").isOlderThan(new Version("4.3.0")));
		assertFalse(new Version("4.3.0").isOlderThan(new Version("4.3.0-SNAPSHOT")));
	}

	@Test
	public void isOlderThanIsFalseWhenEitherSideIsUnresolved() {
		Version unresolved = new Version(element("version", "${project.version}"), properties());
		Version literal = new Version("4.2.0");
		assertFalse(unresolved.isOlderThan(literal));
		assertFalse(literal.isOlderThan(unresolved));
	}
}
