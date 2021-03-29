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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.eclipse.core.runtime.IPath;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioLog;

/**
 * 테스트를 위한 SqlMapConfig File 과 SqlMap File 을 관리하는 관리자 객체
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperConfigFileManager {
//	private static final String TDRIVER = "TDRIVER";
//	private static final String TURL = "TURL";
//	private static final String TUSER = "TUSER";
//	private static final String TPASSWD = "TPASSWD";
	private static final String configContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n<!DOCTYPE configuration\n PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\n \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n\n<configuration>\n <mappers>\n<mapper url=\"file:///"+ getMapFilePath().toString() +"\" />\n</mappers>\n</configuration>\n";
	
	private static final String resultAttribute = "resultType=\"hashmap\"";

	private static MapperConfigFileManager cfm = null;
	//--//
	private SqlSessionFactory sqlMapper;

	private MapperConfigFileManager() {}

	/**
	 * ConfigFileManager 인스턴스 반환
	 * @return ConfigFileManager 인스턴스
	 */
	public static MapperConfigFileManager getConfigFileManagerInst() {
		if (cfm == null) cfm = new MapperConfigFileManager();
		if (!MapperConfigFileManager.checkConfigFile()) cfm.makeConfigFile(null, null, null, null);
		return cfm;
	}

	/**
	 * SqlMapClient 인스턴스 반환
	 * @param sql
	 * @return SqlMapClient 인스턴
	 */
	@SuppressWarnings("static-access")
	public SqlSessionFactory getSqlSessionInst(String sql, String sqlType) throws RuntimeException{	//getSqlMapInstance
		cfm.makeMapFile(sql, sqlType);
		
		try {
			String resource =  cfm.getConfigFilePath().toString();
            File file = new File(resource);
			Reader reader = new FileReader(resource); //Resources.getResourceAsReader(resource);
			SqlSessionFactoryBuilder sqlfBuilder = new SqlSessionFactoryBuilder();
			sqlMapper = sqlfBuilder.build(reader);
		} catch (Exception e) {
			DbioLog.logError("Error initializing ConfigManager class. Cause:" + e.getMessage(), e);
			
		}

		return sqlMapper;
	}
	//--//

	/**
	 * 파일 존재여부 확인
	 */
	public static boolean checkConfigFile() {
		String resource = MapperConfigFileManager.getConfigFilePath().toString();
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
		StringBuffer tmpMapCntn = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"tester\">\n  <")
		.append(sqlType.toLowerCase()).append(" id=\"testQueryId\" ")
		.append(" parameterType=\"hashmap\" ")
		.append(("SELECT".equals(sqlType)) ? resultAttribute : "")
		.append(">\n")
		.append(sql.trim()).append("\n  </").append(sqlType.toLowerCase()).append(">\n\n </mapper>\n");

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
		return DBIOPlugin.getDefault().getStateLocation().append("MapperConfig.xml");
	}

	public static IPath getMapFilePath() {
		return DBIOPlugin.getDefault().getStateLocation().append("Mapper.xml");
	}
}
