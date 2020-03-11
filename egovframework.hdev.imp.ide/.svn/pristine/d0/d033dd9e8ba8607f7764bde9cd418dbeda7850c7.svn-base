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
package egovframework.hdev.imp.ide.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;

import egovframework.dev.imp.core.common.DataToolsPlatformUtil;
import egovframework.hdev.imp.ide.EgovDeviceAPIIdePlugin;
import egovframework.hdev.imp.ide.pages.CustomizeTableCreationPage;

/**  
 * 데이터베이스 관련 유틸리티 클래스
 * @Class Name : DatabaseUtil
 * @Description : DatabaseUtil Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 27.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 27.
 * @version 1.0
 * @see
 * 
 */
public class DatabaseUtil {

	@SuppressWarnings("unused")
	private Connection conn = null;
	
	/**
	 * Zip 파일에서 선택한 컴포넌트의 table script를 반환한다
	 * @param fileName
	 * @param dbType
	 * @return table 정보가 담긴 script
	 * @throws IOException 
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getScriptFileinZip(String fileName, String dbType) throws IOException {

		InputStream inputStream = null;
		
		HashMap<String, String> hashMap = new HashMap<String, String>();

		URL insetUrl = EgovDeviceAPIIdePlugin.getDefault().getBundle().getEntry(fileName);
		try {
			URL url = FileLocator.toFileURL(insetUrl);
			URL resolvedUrl = FileLocator.resolve(url);

			File inputZipFile = new File(resolvedUrl.getFile());
			ZipFile zipFile = new ZipFile(inputZipFile);

			Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();

			while (enumeration.hasMoreElements()) {

				ZipEntry entry = enumeration.nextElement();

				String name = entry.getName();
				if (!entry.isDirectory()) {
					
					if (name.indexOf("_create_") > -1 && name.endsWith(".sql")
							&& name.indexOf(dbType) > -1) {

						inputStream = zipFile
								.getInputStream(entry);
						String createInputStream =  IOUtils.toString(inputStream, "UTF-8"); 
						hashMap.put("create", createInputStream);

					} else if (name.indexOf("_insert_") > -1 && name.endsWith(".sql")
							&& name.indexOf(dbType) > -1) { 

						inputStream = zipFile
								.getInputStream(entry);
						String insertInputStream =  IOUtils.toString(inputStream, "UTF-8"); 
						hashMap.put("insert", insertInputStream);

					}

				}

			}

		} catch (IOException e) {
			DeviceAPIIdeLog.logError(e);
		} finally {
				 
			 if(inputStream != null) {
				 try {
					 inputStream.close();
				 } catch (IOException e) {
					 
					 DeviceAPIIdeLog.logError(e);
				 }
			 }
		}
		
		return hashMap;
	}
	
	
	/**
	 * DDL문 파싱 후, Map 처리
	 * @param fileContent
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, HashMap> getSqlStatementFromDDL(String fileContent) {
		
		String tableName = "";
		String indexName = "";
		String ddlSQL = "";
		
		HashMap<String, HashMap> map = new HashMap<String, HashMap>();
		HashMap<String, String> tableMap = new HashMap<String, String>();
		HashMap<String, String> indexMap = new HashMap<String, String>();
		
		String[] list = fileContent.split(";");
		for (int i = 0; i < list.length; i++) {
			
			if(list[i].toUpperCase().indexOf("CREATE ") > -1 && list[i].toUpperCase().indexOf("TABLE ") > -1 ) {
				
				tableName = list[i].substring(list[i].toUpperCase().indexOf("TABLE") + 5, list[i].indexOf("(")).trim();
				ddlSQL = list[i];
				
				tableMap.put(tableName.toUpperCase(), ddlSQL);
			} else if(list[i].toUpperCase().indexOf("INDEX") > -1) {
				
				indexName = list[i].substring(list[i].toUpperCase().indexOf("INDEX") + 5, list[i].indexOf("(")).trim();
				ddlSQL = list[i];
				
				indexMap.put(indexName.toUpperCase(), ddlSQL);
			}
		}
		
		map.put("TABLE", tableMap);
		map.put("INDEX", indexMap);
		
		return map;
		
	}
	
	/**
	 * DML문 파싱 후, Map 처리
	 * @param fileContent
	 * @param ddlList
	 * @return insert문을 넣은 SqlStatementModel list
	 */
	public static HashMap<String, String> setSqlStatementFromDML(String fileContent) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(fileContent != "") {
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
						
						map.put(insTableName, list[i]);
					
					}
					
				}
			}
		}
		
		return map;
	}
	
	public static boolean isExistTable(String tableName) throws Exception {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM " + tableName;
		
		try {
			
			conn = DataToolsPlatformUtil.getConnection(DataToolsPlatformUtil.getDatabaseIdentifier(DataToolsPlatformUtil.getConnectionInfo(DataToolsPlatformUtil.getProfileNames()[CustomizeTableCreationPage.selectedDatasourceNum])));
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			return true;
			
		} catch (SQLException e) {
			
			return false;
		} catch (NoSuchProfileException e) {
			
			throw new Exception();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					DeviceAPIIdeLog.logError(e);
				}
			}
			if(psmt != null) {
				try {
					psmt.close();
				} catch (SQLException e) {
					DeviceAPIIdeLog.logError(e);
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					DeviceAPIIdeLog.logError(e);
				}
			}
		}
	}
	
	public static boolean excuteSQL(String sql) throws Exception {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		
		try {
			
			conn = DataToolsPlatformUtil.getConnection(DataToolsPlatformUtil.getDatabaseIdentifier(DataToolsPlatformUtil.getConnectionInfo(DataToolsPlatformUtil.getProfileNames()[CustomizeTableCreationPage.selectedDatasourceNum])));
			
			psmt = conn.prepareStatement(sql);
			psmt.executeUpdate();
				
			return true;
		} catch (SQLException e) {
			
			DeviceAPIIdeLog.logError(e);
			
			return false;
		} catch (NoSuchProfileException e) {
			
			DeviceAPIIdeLog.logError(e);
			
			throw new Exception();
		} finally {
			
			if(psmt != null) {
				try {
					psmt.close();
				} catch (SQLException e) {
					DeviceAPIIdeLog.logError(e);
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					DeviceAPIIdeLog.logError(e);
				}
			}
		}
	}
}
