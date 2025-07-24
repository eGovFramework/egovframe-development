package test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.incava.util.diff.Diff;
import org.incava.util.diff.Difference;
import org.junit.Before;
import org.junit.Test;

import egovframework.dev.imp.codegen.template.model.Attribute;
import egovframework.dev.imp.codegen.template.model.DataModelContext;
import egovframework.dev.imp.codegen.template.model.Entity;
import operation.CrudCodeGen;
import operation.CrudCodeGen.CrudWizard;

public class CrudCodeGenTest {
	private CrudCodeGen crudCodeGen;
	private DataModelContext dataModel;
	private CrudWizard crudWizard;

	@Before
	public void setUp() {
		crudCodeGen = new CrudCodeGen();

		dataModel = new DataModelContext();

//		dataModel.setPackageName("pkg");
//		dataModel.setAuthor("홍길동");
//		dataModel.setTeam("실행환경 개발팀");
//		dataModel.setCreateDate("2009.02.01");

		crudWizard = new CrudWizard();
		crudWizard.setAuthor("홍길동");
		crudWizard.setCreateDate("실행환경 개발팀");
		crudWizard.setMapperFolder("2009.02.01");
		crudWizard.setDaoPackage("pkg.service.impl");
		crudWizard.setMapperPackage(crudWizard.getDaoPackage());
		crudWizard.setVoPackage("pkg.service");
		crudWizard.setServicePackage(crudWizard.getVoPackage());
		crudWizard.setImplPackage(crudWizard.getDaoPackage());
		crudWizard.setControllerPackage("pkg.web");
		crudWizard.setJspFolder("WEB-INF/jsp/pkg");

		dataModel.setVender("MySql");// HSQLDB, Oracle, MySql, postgres

		Entity entity = new Entity("SAMPLE2");

		dataModel.setEntity(entity);

		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Attribute> primaryKeys = new ArrayList<Attribute>();

		Attribute attr = new Attribute("ID");
		attr.setJavaType("String");
		attributes.add(attr);
		primaryKeys.add(attr);

		attr = new Attribute("NAME");
		attr.setJavaType("String");
		attributes.add(attr);
//		primaryKeys.add(attr);

		attr = new Attribute("DESCRIPTION");
		attr.setJavaType("String");
		attributes.add(attr);

		attr = new Attribute("USE_YN");
		attr.setJavaType("String");
		attributes.add(attr);

		attr = new Attribute("REG_USER");
		attr.setJavaType("String");
		attributes.add(attr);

		dataModel.setAttributes(attributes);
		dataModel.setPrimaryKeys(primaryKeys);

	}

	private void genAndDiff(String templateFile, String targetFile) {
		String result = crudCodeGen.generate(dataModel, templateFile, crudWizard);
		String[] targetLines = readResource(targetFile);
		String[] sourceLines = readString(result);

		List<Difference> diffs = (new Diff<String>(sourceLines, targetLines)).diff();

		for (Difference diff : diffs) {
			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int addStart = diff.getAddedStart();
			int addEnd = diff.getAddedEnd();
			String from = toString(delStart, delEnd);
			String to = toString(addStart, addEnd);
			String type = delEnd != Difference.NONE && addEnd != Difference.NONE ? "c"
					: (delEnd == Difference.NONE ? "a" : "d");

			System.out.println(from + type + to);

			if (delEnd != Difference.NONE) {
				printLines(delStart, delEnd, "<", sourceLines);
				if (addEnd != Difference.NONE) {
					System.out.println("---");
				}
			}
			if (addEnd != Difference.NONE) {
				printLines(addStart, addEnd, ">", targetLines);
			}
		}

		assertEquals(diffs.size(), 0);
	}

	@Test
	public void testMapper() {
		String templateFile = "eGovFrameTemplates/crud/resource/pkg/EgovSample_Sample2_MAPPER.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/resource/pkg/EgovSample_Sample2_MAPPER.xml";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testSQLMap() {
		String templateFile = "eGovFrameTemplates/crud/resource/pkg/EgovSample_Sample2_SQL_OLD.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/resource/pkg/EgovSample_Sample2_SQL_OLD.xml";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testService() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/service/EgovSample2Service.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/service/EgovSample2Service.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testDefaultVO() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/service/Sample2DefaultVO.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/service/Sample2DefaultVO.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testVO() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/service/Sample2VO.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/service/Sample2VO.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testServiceImpl() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/service/impl/EgovSample2ServiceImpl.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/service/impl/EgovSample2ServiceImpl.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testDAO() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/service/impl/Sample2DAO.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/service/impl/Sample2DAO.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testController() {
		String templateFile = "eGovFrameTemplates/crud/java/pkg/web/EgovSample2Controller.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/java/pkg/web/EgovSample2Controller.jav";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testListView() {
		String templateFile = "eGovFrameTemplates/crud/jsp/pkg/egovSample2List.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/jsp/pkg/egovSample2List.jsp";

		genAndDiff(templateFile, targetFile);
	}

	@Test
	public void testRegisterView() {
		String templateFile = "eGovFrameTemplates/crud/jsp/pkg/egovSample2Register.vm";
		String targetFile = "eGovFrameTemplatesResult/crud/jsp/pkg/egovSample2Register.jsp";

		genAndDiff(templateFile, targetFile);
	}

	protected void printLines(int start, int end, String ind, String[] lines) {
		for (int lnum = start; lnum <= end; ++lnum) {
			System.out.println(ind + " " + lines[lnum]);
		}
	}

	protected String toString(int start, int end) {
		// adjusted, because file lines are one-indexed, not zero.

		StringBuffer buf = new StringBuffer();

		// match the line numbering from diff(1):
		buf.append(end == Difference.NONE ? start : (1 + start));

		if (end != Difference.NONE && start != end) {
			buf.append(",").append(1 + end);
		}
		return buf.toString();
	}

	protected String[] readResource(String fileName) {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
			InputStreamReader sr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(sr);
			List<String> contents = new ArrayList<String>();
			String in;
			while ((in = br.readLine()) != null) {
				contents.add(in);
			}
			return contents.toArray(new String[] {});
		} catch (Exception e) {
			System.err.println("error reading " + fileName + ": " + e);
			System.exit(1);
			return null;
		}
	}

	protected String[] readString(String data) {
		try {
			StringReader sr = new StringReader(data);
			BufferedReader br = new BufferedReader(sr);
			List<String> contents = new ArrayList<String>();
			String in;
			while ((in = br.readLine()) != null) {
				contents.add(in);
			}
			return contents.toArray(new String[] {});
		} catch (Exception e) {
			System.err.println("error reading " + e);
			System.exit(1);
			return null;
		}
	}
}
