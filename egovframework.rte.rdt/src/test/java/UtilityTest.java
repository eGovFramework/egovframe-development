import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import egovframework.rte.rdt.pom.unit.Version;
import egovframework.rte.rdt.pom.util.StringHelper;

public class UtilityTest {

	@Test
	public void propertyTest() {
		assertTrue(StringHelper.isPropertyString("${abc}"));
		assertTrue(StringHelper.isPropertyString("${}"));
		assertTrue(StringHelper.isPropertyString("${a}"));
		assertFalse(StringHelper.isPropertyString("${a"));
		assertFalse(StringHelper.isPropertyString("absdf"));
		assertFalse(StringHelper.isPropertyString("${a}."));

		assertEquals(StringHelper.getProperty("${}"), "");
		assertEquals(StringHelper.getProperty("${abc}"), "abc");
		assertEquals(StringHelper.getProperty("${abc}."), "");
		assertEquals(StringHelper.getProperty("${asda.sad}"), "asda.sad");

	}

	@Test
	public void versionTest() {
		Version[] versions = new Version[12];

		versions[0] = new Version("3.0.0");
		versions[1] = new Version("1.0.1-beta");
		versions[2] = new Version("1.0.1-alpha");
		versions[3] = new Version("1.1.0-RELEASE");
		versions[4] = new Version("1.1.0-SNAPSHOT");
		versions[5] = new Version("2.3.0b");
		versions[6] = new Version("2.3.0a");
		versions[7] = new Version("3.1.0-alpha-10");
		versions[8] = new Version("3.1.0-alpha-2");
		versions[9] = new Version("3.0.5.RELEASE");
		versions[10] = new Version("3.0.5.RELEASE.BETA.2");
		versions[11] = new Version("1.8.3.ALPHA.2010-m2");
		/*
		assertTrue(versions[0].isValidVersion());
		assertTrue(versions[1].isValidVersion());
		assertTrue(versions[7].isValidVersion());
		assertTrue(versions[8].isValidVersion());
		assertTrue(versions[5].isValidVersion());
		assertTrue(versions[6].isValidVersion());
		assertTrue(versions[9].isValidVersion());
		*/

		// �� ���� �� �ֽ����� �˻��Ѵ�.
		assertTrue(new Version("1.1.1-beta").compareTo(new Version("1.1.1-alpha")) > 0);
		assertTrue(new Version("2.0.0").compareTo(new Version("10.9.1")) > 0);
		//assertTrue(new Version("2.1.5-alpha-11").compareTo(new Version("2.1.5-alpha-8")) > 0);
		assertTrue(new Version("4.2.1a").compareTo(new Version("4.2.1")) > 0);
		assertTrue(new Version("4.2.1b").compareTo(new Version("4.2.1a")) > 0);

		System.out.println("before...");
		for (Version v : versions) {
			System.out.println(v.toString());
		}

		Arrays.sort(versions);

		System.out.println("after...");
		for (Version v : versions) {
			System.out.println(v.toString());
		}
	}

}
