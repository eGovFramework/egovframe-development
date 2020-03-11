/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.dev.imp.dbio.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioLog;

/**
 * 테스트를 위한 SqlMapConfig File 과 SqlMap File 을 관리하는 관리자 객체
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
public class ConfigFileManager {
//	private static final String TDRIVER = "TDRIVER";
//	private static final String TURL = "TURL";
//	private static final String TUSER = "TUSER";
//	private static final String TPASSWD = "TPASSWD";
	private static final String configContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n<!DOCTYPE sqlMapConfig\n    PUBLIC \"-//ibatis.apache.org//DTD SQL Map Config 2.0//EN\"\n    \"http://ibatis.apache.org/dtd/sql-map-config-2.dtd\">\n\n<sqlMapConfig>\n  <sqlMap url=\"file:///"+ getMapFilePath().toString() +"\" />\n</sqlMapConfig>\n";
	private static final String resultAttribute = "resultClass=\"hashmap\"";

	private static ConfigFileManager cfm = null;
	//--//
	private SqlMapClient sqlMap;

	private ConfigFileManager() {}

	/**
	 * ConfigFileManager 인스턴스 반환
	 * @return ConfigFileManager 인스턴스
	 */
	public static ConfigFileManager getConfigFileManagerInst() {
		if (cfm == null) cfm = new ConfigFileManager();
		if (!ConfigFileManager.checkConfigFile()) cfm.makeConfigFile(null, null, null, null);
		return cfm;
	}

	/**
	 * SqlMapClient 인스턴스 반환
	 * @param sql
	 * @return SqlMapClient 인스턴
	 */
	@SuppressWarnings("static-access")
	public SqlMapClient getSqlMapClientInst(String sql, String sqlType) throws RuntimeException{	//getSqlMapInstance
		cfm.makeMapFile(sql, sqlType);
		
		try {
			String resource =  cfm.getConfigFilePath().toString();			
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(new FileInputStream(new File(resource)));
		} catch (Exception e) {
			DbioLog.logError("Error initializing ConfigManager class. Cause:" + e.getMessage(), e);
			
		}

		return sqlMap;
	}
	//--//

	/**
	 * 파일 존재여부 확인
	 */
	public static boolean checkConfigFile() {
		String resource = ConfigFileManager.getConfigFilePath().toString();
		File file = new File(resource);
		return file.exists();
	}

	public void makeConfigFile(String dbDriver, String dbUrl, String dbUser, String dbPasswd) {
//		if (dbDriver == null || "".equals(dbDriver))dbDriver = "oracle.jdbc.driver.OracleDriver";
//		if (dbUrl == null || "".equals(dbUrl)) dbUrl = "jdbc:oracle:thin:@192.168.100.14:1521:XE";
//		if (dbUser == null || "".equals(dbUser)) dbUser = "hrr";
//		if (dbPasswd == null || "".equals(dbPasswd)) dbPasswd = "hrr";

//		String tmpCfgCntn = configContent;
//		tmpCfgCntn = tmpCfgCntn.replaceAll(TDRIVER, dbDriver);
//		tmpCfgCntn = tmpCfgCntn.replaceAll(TURL, dbUrl);
//		tmpCfgCntn = tmpCfgCntn.replaceAll(TUSER, dbUser);
//		tmpCfgCntn = tmpCfgCntn.replaceAll(TPASSWD, dbPasswd);

		String filePath = getConfigFilePath().toString();
		writeFile(filePath, configContent);
//		writeFile(filePath, tmpCfgCntn);
	}

	/**
	 * SqlMap 파일 생성
	 * @param sql
	 * @param type : select, insert, procedure 등을 구분
	 */
	public void makeMapFile(String sql, String sqlType) {
		StringBuffer tmpMapCntn = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\"\n    \"http://ibatis.apache.org/dtd/sql-map-2.dtd\">\n\n<sqlMap namespace=\"tester\">\n  <")
		.append(sqlType.toLowerCase()).append(" id=\"testQueryId\" ")
		.append(" parameterClass=\"hashmap\" ")
		.append(("STATEMENT".equals(sqlType) || "SELECT".equals(sqlType) || "PROCEDURE".equals(sqlType)) ? resultAttribute : "")
		.append(">\n")
		.append(sql.trim()).append("\n  </").append(sqlType.toLowerCase()).append(">\n\n </sqlMap>\n");

		String filePath = getMapFilePath().toString();
		writeFile(filePath, tmpMapCntn.toString());
	}

	private void writeFile(String filePath, String cntn) {
		FileWriter fw = null;
		BufferedWriter bf = null;

		try {
			fw = new FileWriter(filePath);
			bf = new BufferedWriter(fw);
			bf.write(cntn);
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			if (bf != null) try { bf.close(); } catch (IOException ie){
				ie.printStackTrace();
			}
			if (fw != null) try {fw.close(); } catch (IOException ie) {
				ie.printStackTrace();
			}
		}

	}

	public static IPath getConfigFilePath() {
		return DBIOPlugin.getDefault().getStateLocation().append("SqlMapConfig.xml");
	}

	public static IPath getMapFilePath() {
		return DBIOPlugin.getDefault().getStateLocation().append("SqlMap.xml");
	}
}
