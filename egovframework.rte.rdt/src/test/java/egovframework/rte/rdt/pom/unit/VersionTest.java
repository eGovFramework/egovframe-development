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
	public void propertyKeyIsLastKeyOfChain() {
		PomMap properties = properties("lib.version", "${spring.version}", "spring.version", "4.3.0");
		assertEquals("spring.version", new Version(element("version", "${lib.version}"), properties).getPropertyKey());
		assertEquals("spring.version", new Version(element("version", "${spring.version}"), properties).getPropertyKey());
		assertNull(new Version(element("version", "4.3.0"), properties).getPropertyKey());
		assertNull(new Version(element("version", "${unknown.version}"), properties).getPropertyKey());
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

	// --- 한정자 비교

	@Test
	public void qualifierNumberIsComparedAsNumber() {
		assertTrue(new Version("2.0.0-RC9").isOlderThan(new Version("2.0.0-RC10")));
		assertFalse(new Version("2.0.0-RC10").isOlderThan(new Version("2.0.0-RC9")));
		assertTrue(new Version("2.0.0-M2").compareTo(new Version("2.0.0-M10")) < 0);
		assertTrue(new Version("1.0.0.beta2").compareTo(new Version("1.0.0.BETA12")) < 0);
		assertEquals(0, new Version("2.0.0-RC01").compareTo(new Version("2.0.0-rc1")));
		assertTrue(new Version("2.0.0-RC").compareTo(new Version("2.0.0-RC1")) < 0);
	}

	@Test
	public void preReleaseQualifiersKeepTheirOrder() {
		String[] ascending = { "1.0.0-alpha", "1.0.0-beta", "1.0.0-M1", "1.0.0-RC1", "1.0.0-SNAPSHOT", "1.0.0" };
		assertAscending(ascending);
	}

	@Test
	public void servicePackIsNewerThanSameRelease() {
		assertFalse(new Version("1.0.0-SP1").isOlderThan(new Version("1.0.0")));
		assertTrue(new Version("1.0.0").isOlderThan(new Version("1.0.0-SP1")));
		assertTrue(new Version("1.0.0.Final").isOlderThan(new Version("1.0.0.SP1")));
		assertTrue(new Version("1.0.0-SP1").isOlderThan(new Version("1.0.0-sp2")));
		assertTrue(new Version("1.0.0-SP9").isOlderThan(new Version("1.0.0-SP10")));
		assertTrue(new Version("1.0.0-SP1").isOlderThan(new Version("1.0.1")));
		// sp 로 시작할 뿐 서비스 팩이 아닌 한정자는 릴리스 이전으로 남는다.
		assertTrue(new Version("1.0.0-special").isOlderThan(new Version("1.0.0")));
	}

	@Test
	public void compareToIsAntisymmetricAndTransitive() {
		String[] ascending = { "1.0.0-alpha", "1.0.0-RC2", "1.0.0-RC10", "1.0.0-SNAPSHOT", "1.0.0", "1.0.0-SP1",
				"1.0.0-SP2", "1.0.1", "1.0.9", "1.0.10", "1.9.0", "1.10.0", "2.0.0-M2", "2.0.0" };
		assertAscending(ascending);
	}

	@Test
	public void compareToHandlesSegmentsBeyondLongRange() {
		assertTrue(new Version("1.0.99999999999999999999").compareTo(new Version("1.0.100000000000000000000")) < 0);
		assertEquals(0, new Version("1.0.007").compareTo(new Version("1.0.7")));
	}

	/**
	 * 오름차순으로 나열한 버전들의 모든 쌍이 양방향으로 그 순서대로 비교되는지 단언한다.
	 */
	private static void assertAscending(String[] ascending) {
		for (int i = 0; i < ascending.length; i++) {
			for (int j = 0; j < ascending.length; j++) {
				int expected = i < j ? -1 : (i == j ? 0 : 1);
				int actual = new Version(ascending[i]).compareTo(new Version(ascending[j]));
				assertEquals(ascending[i] + " vs " + ascending[j], expected, Integer.signum(actual));
			}
		}
	}

	@Test
	public void isOlderThanIsFalseWhenEitherSideIsUnresolved() {
		Version unresolved = new Version(element("version", "${project.version}"), properties());
		Version literal = new Version("4.2.0");
		assertFalse(unresolved.isOlderThan(literal));
		assertFalse(literal.isOlderThan(unresolved));
	}
}
