package egovframework.mdev.imp.commngt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * 컴포넌트 정보가 담긴 엑셀파일을 읽어서
 * 지정된 위치에 위저드가 읽을 수 있는 컴포넌트 정보 파일을 temp 파일로 생성한다.
 * 개발자는 생성된 파일의 내용을 각각의 실제 파일에 덮어쓴다.
 * 
 * 엑셀파일 형식
 *   - 행 : 3행부터 데이터 시작
 *   - 열 :
 *          B) 카테고리 명	
 *          C) 카테고리 설명	
 *          D) 컴포넌트 명	
 *          E) 패키지명	
 *          F) dependency 패키지	(콤마 또는 줄바꿈으로 연결)
 *          G) 컴포넌트 설명	
 *          H) 테이블명 (콤마로 연결)	
 *          I) 버전	
 *          J) web.xml 배포여부	
 *          K) 게시판 추가기능 사용여부 (사용안함)	
 *          L) 카테고리 명_영문	
 *          M) 카테고리 설명_영문	
 *          N) 컴포넌트 명_영문	
 *          O) 컴포넌트 설명_영문
 * 
 * 생성 파일 :  
 * 			  components_ko.properties  (실제 파일경로 : egovframework.dev.imp.commngt\common)
 *            components_en.properties  (실제 파일경로 : egovframework.dev.imp.commngt\common)
 *            components.xml            (실제 파일경로 : egovframework.dev.imp.commngt\example)
 *            MobileComponentsInfo.java (실제 파일경로 : egovframework.dev.imp.commngt\common)
 *
 */

//TODO 모바일 공통컴포넌트 생성
public class CreateComponentInfo{
	
	public static void main(String[] args) throws Exception{
		try{
			//컴포넌트 정보 엑셀 파일 경로			
			String readFilePath = "C:\\\\eclipse-rcp-2022-12-R-win32-x86_64\\workspace\\egovframework.mdev.imp.commngt\\examples\\componentsMobile.xls";
			//컴포넌트 정보 properties, java, xml 파일 생성 경로
			String generateFilePath="C:\\\\eclipse-rcp-2022-12-R-win32-x86_64\\workspace\\egovframework.mdev.imp.commngt\\examples";
			
			//properties 파일
			File propertiesKoFile = File.createTempFile("components_ko", ".properties", new File(generateFilePath));
			FileWriter propertiesKoWriter = new FileWriter(propertiesKoFile);
			//properties 영문 파일
			File propertiesEnFile = File.createTempFile("components_en", ".properties", new File(generateFilePath));
			FileWriter propertiesEnWriter = new FileWriter(propertiesEnFile);			
			//xml 파일
			File xmlFile = File.createTempFile("components", ".xml", new File(generateFilePath));
			FileWriter xmlWriter = new FileWriter(xmlFile);
			xmlWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xmlWriter.write("<components>\n");
			//java 파일
			File javaFile = File.createTempFile("MobileComponentsInfo", ".java", new File(generateFilePath));
			FileWriter javaWriter = new FileWriter(javaFile);
			javaWriter.write("package egovframework.mdev.imp.commngt.common;\n\n");
			javaWriter.write("import org.eclipse.osgi.util.NLS;\n\n");
			javaWriter.write("public class MobileComponentsInfo extends NLS {\n");
			javaWriter.write("\tprivate static final String BUNDLE_NAME2 = \"egovframework.mdev.imp.commngt.common.components\"; //$NON-NLS-1$\n");
			
			InputStream inp = new FileInputStream(readFilePath);
			HSSFWorkbook wbT =  new HSSFWorkbook(inp);
        	HSSFSheet sheetT = wbT.getSheet(wbT.getSheetName(0));
 
        	int catNum = 0;
        	int comNum = 0 ;
        	int depNum = 0 ;
        	String category_nam = "";
			String category_des = "";
        	String category_nam_en = "";
			String category_des_en = "";
			String category_Component_nam = "";
			String category_Component_des = "";
			String category_Component_nam_en = "";
			String category_Component_des_en = "";
			String category_Component_pac = "";
			String category_Component_use = "";
			String category_Component_d01 = "";
			String category_Component_ver = "";
			String category_Component_web = "";
			String category_Component_add = "";
        	
        	for (int i = 2; i <= sheetT.getLastRowNum(); i++) { 
        		
        		HSSFRow rowValue = sheetT.getRow(i);
				HSSFCell cellValue1 = rowValue.getCell(1);
				HSSFCell cellValue2 = rowValue.getCell(2);
				HSSFCell cellValue3 = rowValue.getCell(3);
				HSSFCell cellValue4 = rowValue.getCell(4);
				HSSFCell cellValue5 = rowValue.getCell(5);
				HSSFCell cellValue6 = rowValue.getCell(6);
				HSSFCell cellValue7 = rowValue.getCell(7);
				HSSFCell cellValue8 = rowValue.getCell(8);
				HSSFCell cellValue9 = rowValue.getCell(9);
				HSSFCell cellValue10 = rowValue.getCell(10);
				//영문 부분
				HSSFCell cellValue11 = rowValue.getCell(11); //category_nam_en
				HSSFCell cellValue12 = rowValue.getCell(12); //category_des_en
				HSSFCell cellValue13 = rowValue.getCell(13); //category_Component_nam_en
				HSSFCell cellValue14 = rowValue.getCell(14); //category_Component_des_en
				
				if(!category_nam.equals((cellValue1+"").trim())){
					category_nam           = (cellValue1+"").trim();
					category_des           = (cellValue2+"").trim();
					category_nam_en        = (cellValue11+"").trim();
					category_des_en        = (cellValue12+"").trim();
					catNum++;
					comNum = 0;
					//properties 쓰기
					propertiesKoWriter.write("\ncategory"+catNum+"nam="+category_nam+"\n");
					propertiesKoWriter.write("category"+catNum+"des="+category_des+"\n\n");
					//properties_영문 쓰기
					propertiesEnWriter.write("\ncategory"+catNum+"nam="+category_nam_en+"\n");
					propertiesEnWriter.write("category"+catNum+"des="+category_des_en+"\n\n");
					//xml 쓰기
					if(i>2) xmlWriter.write("\t</category>\n");
					xmlWriter.write("\t<category name=\"#category"+catNum+"nam#\" description=\"#category"+catNum+"des#\" >\n");
					//java 쓰기
					javaWriter.write("\n\tpublic static String category"+catNum+"nam;\n");
					javaWriter.write("\tpublic static String category"+catNum+"des;\n\n");
				}
				
				category_nam           = (cellValue1+"").trim().replace("\n", " "); 
				category_des           = (cellValue2+"").trim().replace("\n", " "); 
				category_Component_nam = (cellValue3+"").trim().replace("\n", " ");
				category_Component_pac = (cellValue4+"").trim().replace("\n", " "); 
				category_Component_d01 = (cellValue5+"").trim().replace("\n", " ");  
				category_Component_des = (cellValue6+"").trim().replace("&", "&#38;")
															   .replace("(", "&#40;")
															   .replace(")", "&#41;")
															   .replace("-", "&#45;")
															   .replace("/", "&#47;")
															   .replace("\n", "<br/>"); 
				category_Component_use = (cellValue7+"").trim().replace("\n", ", "); 
				category_Component_ver = (cellValue8+"").trim().replace("\n", " "); 
				category_Component_web = (cellValue9+"").trim().replace("\n", " "); 
				category_Component_add = (cellValue10+"").trim().replace("\n", " ");
				if(category_Component_use == null || category_Component_use.equals("")) category_Component_use="N/A";
				if(category_Component_web.equals("Y")) category_Component_web="true";
				else if(category_Component_web.equals("N")) category_Component_web="false";
				if(category_Component_add.equals("Y")) category_Component_add="true";
				else if(category_Component_add.equals("N")) category_Component_add="false";
				
				category_nam_en           = (cellValue11+"").trim().replace("\n", " "); 
				category_des_en           = (cellValue12+"").trim().replace("\n", " "); 
				category_Component_nam_en = (cellValue13+"").trim().replace("\n", " ").replace("(", "&#40;").replace(")", "&#41;"); 
				category_Component_des_en = (cellValue14+"").trim().replace("&", "&#38;")
															       .replace("(", "&#40;")
															       .replace(")", "&#41;")
															       .replace("-", "&#45;")
															       .replace("/", "&#47;")
															       .replace("\n", "<br/>"); 
				
				StringTokenizer st = new StringTokenizer(category_Component_d01,","); //dependency 정보
				
				comNum++;
				//properties 쓰기
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"nam="+category_Component_nam+"\n");
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"pac="+category_Component_pac+"\n");
				//properties 영문 쓰기
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"nam="+category_Component_nam_en+"\n");
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"pac="+category_Component_pac   +"\n");
				//xml 쓰기
				xmlWriter.write("\t\t<component name=\"#"+"category"+catNum+"Component"+comNum+"nam#\" >\n");
				xmlWriter.write("\t\t\t<packageName      >#"+"category"+catNum+"Component"+comNum+"pac#</packageName      >\n");
				//java 쓰기
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"nam;\n");
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"pac;\n");
			
				depNum = 0;
				String dep = "";
				while(st.hasMoreTokens()){
					depNum++;
					dep = st.nextToken().trim();
					//properties 쓰기
					propertiesKoWriter.write("category"+catNum+"Component"+comNum+"de"+depNum+"="+dep+"\n");
					//properties 영문 쓰기
					propertiesEnWriter.write("category"+catNum+"Component"+comNum+"de"+depNum+"="+dep+"\n");
					//xml 쓰기
					xmlWriter.write("\t\t\t<dependencyPackage>#"+"category"+catNum+"Component"+comNum+"de"+depNum+"#</dependencyPackage>\n");
					//java 쓰기
					javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"de"+depNum+";\n");
				}
				if(category_Component_d01.length()==0){
					//properties 쓰기
					propertiesKoWriter.write("category"+catNum+"Component"+comNum+"de1=\n");
					//properties 영문 쓰기
					propertiesEnWriter.write("category"+catNum+"Component"+comNum+"de1=\n");
					//xml 쓰기
					xmlWriter.write("\t\t\t<dependencyPackage>#"+"category"+catNum+"Component"+comNum+"de1#</dependencyPackage>\n");
					//java 쓰기
					javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"de1;\n");
				}
				//properties 쓰기
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"des="+category_Component_des+"\n");
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"use="+category_Component_use+"\n");
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"ver=Wizard."+category_Component_ver+"\n");
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"web="+category_Component_web+"\n");
				propertiesKoWriter.write("category"+catNum+"Component"+comNum+"add="+category_Component_add+"\n\n");
				//properties 영문 쓰기
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"des="+category_Component_des_en+"\n");
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"use="+category_Component_use+"\n");
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"ver=Wizard."+category_Component_ver+"\n");
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"web="+category_Component_web+"\n");
				propertiesEnWriter.write("category"+catNum+"Component"+comNum+"add="+category_Component_add+"\n\n");
				//xml 쓰기
				xmlWriter.write("\t\t\t<description      >#"+"category"+catNum+"Component"+comNum+"des#</description      >\n");
				xmlWriter.write("\t\t\t<useTable         >#"+"category"+catNum+"Component"+comNum+"use#</useTable         >\n");
				xmlWriter.write("\t\t\t<version          >#"+"category"+catNum+"Component"+comNum+"ver#</version          >\n");
				xmlWriter.write("\t\t\t<webexist         >#"+"category"+catNum+"Component"+comNum+"web#</webexist         >\n");
				xmlWriter.write("\t\t\t<addedOptions     >#"+"category"+catNum+"Component"+comNum+"add#</addedOptions     >\n");
				xmlWriter.write("\t\t</component>\n");
				//java 쓰기
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"des;\n");
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"use;\n");
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"ver;\n");
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"web;\n");
				javaWriter.write("\tpublic static String category"+catNum+"Component"+comNum+"add;\n\n");
				
        	}
        	//prproperties 닫기
        	propertiesKoWriter.close();
        	//prproperties 영문 닫기
        	propertiesEnWriter.close();
        	//xml 닫기
        	xmlWriter.write("\t</category>\n");
        	xmlWriter.write("</components>");
        	xmlWriter.close();
        	//java 닫기
        	javaWriter.write("\n\tstatic {\n");
        	javaWriter.write("\t\tNLS.initializeMessages(BUNDLE_NAME2, MobileComponentsInfo.class);\n");
        	javaWriter.write("\t}\n");
        	javaWriter.write("\n\tprivate MobileComponentsInfo() {\n\t}\n}");
        	javaWriter.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}