package egovframework.rte.rdt.xml;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.SAXParserFactory;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * SecureSAXBuilder 가 외부 엔티티·외부 DTD 를 읽지 않으면서
 * 내부 엔티티 치환은 기본 SAXBuilder 와 동일하게 유지하는지 검증한다.
 */
public class SecureSAXBuilderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	/**
	 * 외부 엔티티가 가리킬 파일을 임시 폴더에 만들고 그 URI 를 돌려준다.
	 * JUnit 4.7 은 @Before 보다 @Rule 을 늦게 준비하므로 @Before 가 아니라 테스트 안에서 부른다.
	 */
	private String createSecretFile() throws IOException {
		File secret = folder.newFile("secret.txt");
		Writer w = new FileWriter(secret);
		w.write("secret-file-content");
		w.close();
		return secret.toURI().toString();
	}

	private static String parseVersion(SAXBuilder builder, String xml) throws Exception {
		Document doc = builder.build(new StringReader(xml));
		return doc.getRootElement().getChildText("version");
	}

	@Test
	public void plainDocumentParsesUnchanged() throws Exception {
		String xml = "<project><version>0.0.1</version></project>";
		assertEquals("0.0.1", parseVersion(new SecureSAXBuilder(), xml));
	}

	@Test
	public void internalEntityIsStillExpanded() throws Exception {
		String xml = "<!DOCTYPE project [<!ENTITY v \"9.9.9-ENT\">]>"
				+ "<project><version>&v;</version></project>";
		assertEquals("9.9.9-ENT", parseVersion(new SecureSAXBuilder(), xml));
	}

	@Test
	public void externalGeneralEntityIsReplacedWithEmptyText() throws Exception {
		String secretUri = createSecretFile();
		String xml = "<!DOCTYPE project [<!ENTITY xxe SYSTEM \"" + secretUri + "\">]>"
				+ "<project><version>&xxe;</version></project>";
		assertEquals("", parseVersion(new SecureSAXBuilder(), xml));
	}

	@Test
	public void externalParameterEntityIsIgnored() throws Exception {
		String secretUri = createSecretFile();
		String xml = "<!DOCTYPE project [<!ENTITY % p SYSTEM \"" + secretUri + "\"> %p;]>"
				+ "<project><version>1</version></project>";
		assertEquals("1", parseVersion(new SecureSAXBuilder(), xml));
	}

	@Test
	public void externalDtdIsNotLoaded() throws Exception {
		String secretUri = createSecretFile();
		// 기본 SAXBuilder 라면 DTD 로 읽은 secret.txt 가 잘못된 마크업이라 파싱에 실패한다.
		String xml = "<!DOCTYPE project SYSTEM \"" + secretUri + "\">"
				+ "<project><version>1</version></project>";
		assertEquals("1", parseVersion(new SecureSAXBuilder(), xml));
	}

	@Test
	public void unrecognizedFeatureDoesNotBreakParsing() throws Exception {
		// Apache 전용 feature 를 모르는 SAX 드라이버에서도 정상 문서는 파싱되어야 한다.
		String xml = "<project><version>0.0.1</version></project>";
		SAXBuilder builder = new SecureSAXBuilder(FeatureRejectingReader.class.getName());
		assertEquals("0.0.1", parseVersion(builder, xml));
	}

	@Test
	public void externalGeneralEntityIsStillBlockedWhenFeatureIsUnrecognized() throws Exception {
		String secretUri = createSecretFile();
		// 일부 feature 가 거부되어도 EntityResolver 에 의한 차단은 유지되어야 한다.
		String xml = "<!DOCTYPE project [<!ENTITY xxe SYSTEM \"" + secretUri + "\">]>"
				+ "<project><version>&xxe;</version></project>";
		SAXBuilder builder = new SecureSAXBuilder(FeatureRejectingReader.class.getName());
		assertEquals("", parseVersion(builder, xml));
	}

	/**
	 * 기본 XMLReader 를 감싸되 Apache 전용 feature 는 인식하지 못하는 척하는 드라이버.
	 * JDOM 은 SAX 드라이버 클래스명을 받으면 그 클래스를 직접 인스턴스화한다.
	 */
	public static class FeatureRejectingReader extends XMLFilterImpl {
		public FeatureRejectingReader() throws Exception {
			super(SAXParserFactory.newInstance().newSAXParser().getXMLReader());
		}

		@Override
		public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
			if (name.startsWith("http://apache.org/")) {
				throw new SAXNotRecognizedException(name);
			}
			super.setFeature(name, value);
		}

		@Override
		public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
			if (name.startsWith("http://apache.org/")) {
				throw new SAXNotRecognizedException(name);
			}
			return super.getFeature(name);
		}
	}
}
