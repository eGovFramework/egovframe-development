package egovframework.mdev.imp.commngt.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import egovframework.mdev.imp.commngt.common.MobileComMngtLog;
import egovframework.mdev.imp.commngt.wizards.model.SqlStatementModel;

/**
 * SCRIPT 파일 유틸 클래스
 * 
 * @author 개발환경 개발팀 박진성
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박진성          최초 생성
 * 
 * 
 * </pre>
 */
public class FileUtil {
	
	/**
	 * 파일을 읽는다
	 * 
	 * @param fileName
	 * @return data
	 */
	public String readFile(String fileName) {
		String data = "";
		int ch = 0;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			do {
				ch = fr.read();
				if (ch != -1) {
					data += ((char) ch);
				}
			} while (ch != -1);
		} catch (IOException e) {
			MobileComMngtLog.logError(e);
		} finally {
			try {
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				MobileComMngtLog.logError(e);
			}
		}


		return data;

	}

	/**
	 * 디렉토리 이름과 DB타입을 입력받아 DB타입에 해당하는 파일명을 리턴받는다.
	 * 
	 * @param dirName
	 * @param dbType
	 * @return 파일명
	 */
	public String getFileName(String dirName, String dbType, String flag) {

		File f = null;
		if (null == dirName || dirName.equals("")) {
			// 전달된 명령행 매개변수가 없으면 현재 디렉토리를 가지고 File 객체를 생성합니다.
			f = new File(".");
		} else {
			// 전달된 명령행 매개변수를 가지고 File 객체를 생성합니다.
			f = new File(dirName);
		}

		String[] fileList = f.list();

		String fileName = "";
		String ext = "";
		for (int i = 0; i < fileList.length; i++) {

			if(fileList[i].lastIndexOf(".") >= 0) {
				ext = fileList[i].substring(fileList[i].lastIndexOf(".") + 1, fileList[i].length());
				if (ext.equals("SCRIPT")) {
					if (fileList[i].indexOf(dbType) > -1 && fileList[i].indexOf(flag) > -1) {
						fileName = fileList[i];
						break;
					}
				}
			}
		}
		return fileName;
	}

	/**
	 * 파일명을 입력받아 해당 파일로 부터 sqlStatement정보를 추출한다.
	 * @param fileName
	 * @return script내의 정보
	 */
	public List <SqlStatementModel> extractSqlStatementFromFile(String fileName) {

		List <SqlStatementModel> aList = new ArrayList<SqlStatementModel>();
		SqlStatementModel ss = null; 
		String fileContent = readFile(fileName).toUpperCase();
		
		String [] list = fileContent.split("CREATE");
		
		for(int i = 0; i < list.length; i++) {
			if(list[i].length() > 0) {
				ss = new SqlStatementModel();
				if(i == list.length - 1) {
					if(list[i].indexOf("INSERT") > -1) {
						String [] insertList = list[i].split("INSERT");
						
						if(insertList[0].indexOf("TABLE") > -1) {
							ss.setStmtName(insertList[0].substring(insertList[0].lastIndexOf("TABLE") + 5, insertList[0].indexOf("(")).trim());
							ss.setStmtType("TABLE");
							ss.setCreateStatement(("CREATE " + insertList[0]).trim());
							
						} else if(insertList[0].indexOf("SEQUENCE") > -1) {
							ss.setStmtName(insertList[0].substring(insertList[0].lastIndexOf("SEQUENCE") + 8, insertList[0].indexOf("START")).trim());
							ss.setStmtType("SEQUENCE");
							ss.setCreateStatement(("CREATE " + insertList[0]).trim());
						} 
						
						for(int j = 1; j < insertList.length; j++) {
							String insTableName = null;
							if(insertList[j].indexOf("VALUES") > insertList[j].indexOf("(")) {
								insTableName = insertList[j].substring(insertList[j].lastIndexOf("INTO") + 4, insertList[j].indexOf("(")).trim();
							} else {
								insTableName = insertList[j].substring(insertList[j].lastIndexOf("INTO") + 4, insertList[j].indexOf("VALUES")).trim();
							}
							
							for(int k = 0; k < aList.size(); k++) {
								SqlStatementModel ssm = (SqlStatementModel)aList.get(k);
								if(insTableName.equals(ssm.getStmtName())) {
									ssm.addInsertStatement("INSERT " + insertList[j]);
								}
							}
						}
					}
				} else {
	 				if(list[i].indexOf("TABLE") > -1) {
						ss.setStmtName(list[i].substring(list[i].lastIndexOf("TABLE") + 5, list[i].indexOf("(")).trim());
						ss.setStmtType("TABLE");
						ss.setCreateStatement(("CREATE " + list[i]).trim());
						
					} else if(list[i].indexOf("SEQUENCE") > -1) {
						ss.setStmtName(list[i].substring(list[i].lastIndexOf("SEQUENCE") + 8, list[i].indexOf("START")).trim());
						ss.setStmtType("SEQUENCE");
						ss.setCreateStatement(("CREATE " + list[i]).trim());
					} 
				}
				aList.add(ss);
			}			
		}
		return aList;
	}
	
	
	/**
	 * DDL문 파싱해서 SqlStatement에 세팅
	 * @param fileName
	 * @return
	 */
	public List <SqlStatementModel> setSqlStatementFromDDL(String fileContent) {

		List <SqlStatementModel> ddlList = new ArrayList<SqlStatementModel>();
		SqlStatementModel ss = null; 
		
		String [] list = fileContent.toUpperCase().split(";");
		String [] newList = new String [list.length];
		for(int i = 0; i < list.length; i++) {
			newList[i] = list[i].trim();
		}
		
		for(int i = 0; i < newList.length; i++) {
			if(newList[i].length() > 0) {
				ss = new SqlStatementModel();
				
				ss.setStmtName(newList[i].substring(newList[i].indexOf("TABLE") + 5, newList[i].indexOf("(")).trim());
				ss.setStmtType("TABLE");
				ss.setCreateStatement(newList[i]);
				ddlList.add(ss);
			}
		}
		
		return ddlList;
	}

	/**
	 * DDL문 파싱해서 SqlStatement에 세팅
	 * @param fileName, ssmList
	 * @return create문을 넣은 SqlStatementModel list
	 */
	public List <SqlStatementModel> setSqlStatementFromDDL(String fileContent, List<SqlStatementModel> ssmList) {

		List <SqlStatementModel> ddlList = new ArrayList<SqlStatementModel>();
		SqlStatementModel ss = null; 
		
		String [] list = fileContent.split(";");
		String [] newList = new String [list.length];
		for(int i = 0; i < list.length; i++) {
			newList[i] = list[i].trim();
		}
		
		for(int i = 0; i < newList.length; i++) {
			if(newList[i].length() > 0) {
				
				if(list[i].toUpperCase().indexOf("CREATE ") > -1 && list[i].toUpperCase().indexOf("TABLE ") > -1) {
					
					ss = new SqlStatementModel();
					ss.setStmtName(newList[i].substring(newList[i].toUpperCase().indexOf("TABLE") + 5, newList[i].indexOf("(")).trim());
					ss.setStmtType("TABLE");
					ss.setCreateStatement(newList[i]);
					
					for(int j= 0; j < ssmList.size(); j++){
						if(ssmList.get(j).getStmtName().equalsIgnoreCase(newList[i].substring(newList[i].toUpperCase().indexOf("TABLE") + 5, newList[i].indexOf("(")).trim())){
							if(ss.getComponent() == null){
								ss.setComponent(ssmList.get(j).getComponent());
							}else{
								ss.setComponent(ss.getComponent()+", "+ssmList.get(j).getComponent());
								
							}
						}
					}
					
				}else if(list[i].toUpperCase().indexOf("VIEW") > -1) {
					
					ss = new SqlStatementModel();
					ss.setStmtName(newList[i].substring(newList[i].toUpperCase().indexOf("VIEW") + 4, newList[i].indexOf("(")).trim());
					ss.setStmtType("VIEW");
					ss.setCreateStatement(newList[i]);
					
					for(int j= 0; j < ssmList.size(); j++){
						if(ssmList.get(j).getStmtName().equalsIgnoreCase(newList[i].substring(newList[i].toUpperCase().indexOf("VIEW") + 4, newList[i].indexOf("(")).trim())){
							if(ss.getComponent() == null){
								ss.setComponent(ssmList.get(j).getComponent());
							}else{
								ss.setComponent(ss.getComponent()+", "+ssmList.get(j).getComponent());
								
							}
						}
					}
					
				}else if(list[i].toUpperCase().indexOf("INDEX") > -1) {
					
					ss = new SqlStatementModel();
					ss.setStmtName(newList[i].substring(newList[i].toUpperCase().indexOf("INDEX") + 5, newList[i].indexOf("(")).trim());
					ss.setStmtType("INDEX");
					ss.setCreateStatement(newList[i]);
					
					for(int j= 0; j < ssmList.size(); j++){
						if(ssmList.get(j).getStmtName().equalsIgnoreCase(newList[i].substring(newList[i].toUpperCase().indexOf("INDEX") + 5, newList[i].indexOf("(")).trim())){
							if(ss.getComponent() == null){
								ss.setComponent(ssmList.get(j).getComponent());
							}else{
								ss.setComponent(ss.getComponent()+", "+ssmList.get(j).getComponent());
								
							}
						}
					}
					
				}else {
					
					ss = new SqlStatementModel();
					ss.setStmtName("OTHER" + i);
					ss.setStmtType("OTHER");
					ss.setCreateStatement(newList[i]);
					
				}
				
				ddlList.add(ss);
			}
		}
		
		return ddlList;
	}
	
	/**
	 * DML문 파싱해서 SqlStatement정보에 세팅
	 * @param fileName
	 * @param ddlList
	 * @return insert문을 넣은 SqlStatementModel list
	 */
	public List <SqlStatementModel> setSqlStatementFromDML(String fileContent, List<SqlStatementModel> ddlList) {
		
		if(fileContent != ""){
		String [] oldList = fileContent.split(";");
		
		String [] list = new String [oldList.length];
		for(int i = 0; i < oldList.length; i++) {
			
			
			
			list[i] = oldList[i].trim();
		}
		
		for(int i = 0; i < list.length; i++) {
			
			if(list[i].trim().length() > 0) {
				String insTableName = null;
				
				if(list[i].toUpperCase().indexOf("INSERT") > -1){
				
					if(list[i].toUpperCase().indexOf("VALUES") > list[i].indexOf("(")) {
						insTableName = list[i].substring(list[i].toUpperCase().indexOf("INTO") + 4, list[i].indexOf("(")).trim();
					} else {
						insTableName = list[i].substring(list[i].toUpperCase().indexOf("INTO") + 4, list[i].toUpperCase().indexOf("VALUES")).trim();
					}
					
					for(int k = 0; k < ddlList.size(); k++) {
						SqlStatementModel ssm = (SqlStatementModel)ddlList.get(k);
						if(insTableName.equals(ssm.getStmtName().trim())) {
							ssm.addInsertStatement(list[i]);
							break;
						}
					}
				
				}
				
				
			}
		}
		
		}
		return ddlList;
	}

}
