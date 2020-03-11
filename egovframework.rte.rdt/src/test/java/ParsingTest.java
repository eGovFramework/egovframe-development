import java.io.File;
import java.util.List;

import org.junit.Test;

import egovframework.rte.rdt.pom.exception.PomException;
import egovframework.rte.rdt.pom.parser.PomParser;
import egovframework.rte.rdt.pom.unit.Dependency;
import egovframework.rte.rdt.pom.unit.Pom;
import egovframework.rte.rdt.pom.unit.Version;

public class ParsingTest {

	@Test
	public void generatePomModel() {
		try {
			File input = new File("./src/test/resources/pom.xml");
			Pom pom = PomParser.parse(input);
			List<Dependency> dependencyList = pom.listDependencies();
			for (Dependency d : dependencyList) {
				System.out.println(d);
			}
			Dependency d = new Dependency();
			d.setGroupId("sadsdafjiafsjisafjioalsfjsailf");
			d.setArtifactId("artifactTest");
			d.setScope("asdsasad");
			d.setVersion(new Version("1321313.131"));
			pom.insertDependency(d);
			
//			d = new Dependency();
			
			d.setGroupId("sadsdafjiafsjisafjioalsfjsailff");
			d.setArtifactId("artifactTestf");
			d.setScope("asdsasadf");
			d.setVersion(new Version("1321313.131f"));
			
			pom.insertDependency(d);
			
			pom.removeDependency("commons-dbcp.commons-dbcp");
			System.out.println("=====================");
			dependencyList = pom.listDependencies();
			for (Dependency d2 : dependencyList) {
				System.out.println(d2);
			}
			
			pom.commit();
			
			//PomParser.writeDocument((DetailPom) pom);
		} catch (PomException pe) {
			System.out.println(pe.getErrorCode());
			System.out.println(pe.getErrorDetail());
			pe.printStackTrace();
		}

	}

}
