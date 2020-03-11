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
package egovframework.dev.imp.core.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.sqltools.core.DatabaseIdentifier;
import org.eclipse.datatools.sqltools.core.SQLDevToolsConfiguration;
import org.eclipse.datatools.sqltools.core.SQLToolsFacade;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;
import org.eclipse.datatools.sqltools.core.profile.ProfileUtil;
import org.eclipse.datatools.sqltools.core.services.ConnectionService;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.internal.sqlscrapbook.util.SQLUtility;

/**
 * Data Source Explorer OpenSource를 사용하기 위한 툴
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
 *	 2011.06.13  최서윤        getProperty 추가
 * 
 * </pre>
 */
public final class DataToolsPlatformUtil {

	private DataToolsPlatformUtil() {}
	
	public static DatabaseIdentifier getDatabaseIdentifier(ISQLEditorConnectionInfo connectionInfo) {
		String profileName = connectionInfo.getConnectionProfileName();
		String dbName = connectionInfo.getDatabaseName();
		return new DatabaseIdentifier(profileName, dbName);

	}

	public static Connection getConnection(DatabaseIdentifier databaseIdentifier)
	throws SQLException, NoSuchProfileException {
		SQLDevToolsConfiguration f = SQLToolsFacade.getConfigurationByProfileName(databaseIdentifier.getProfileName());
		ConnectionService conService = f.getConnectionService();
		return conService.createConnection(databaseIdentifier, true);
	}
	
	public static IConnectionProfile [] getProfiles() {
		return ProfileUtil.getProfiles();
	}
	
	public static String[] getProfileNames() {
		IConnectionProfile[] profiles = ProfileUtil.getProfiles();
		String[] profileNames = new String[profiles.length];

		for(int i = 0; i < profiles.length; i++) {
			profileNames[i] = profiles[i].getName();
		}
		
		return profileNames;
	}
	
	public static ISQLEditorConnectionInfo getConnectionInfo(String profileName) {
		return SQLUtility.getConnectionInfo(profileName);
	}
	
	public static Map<String, String> getProperty(String dbType) {
		Map<String, String> property =  new HashMap<String, String>();

		IConnectionProfile p = getConnectionInfo(dbType).getConnectionProfile();
		
		property.put("username", p.getBaseProperties().getProperty(
		"org.eclipse.datatools.connectivity.db.username"));

		property.put("password", p.getBaseProperties().getProperty(
		"org.eclipse.datatools.connectivity.db.password"));

		property.put("driverClass", p.getBaseProperties().getProperty(
		"org.eclipse.datatools.connectivity.db.driverClass"));
		
		property.put("url", p.getBaseProperties().getProperty(
		"org.eclipse.datatools.connectivity.db.URL"));
		
		return property;
	}
	
}
