/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.util;

import java.sql.Connection;
import java.sql.SQLException;

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
 * 
 * DataTools 유틸리티 클래스
 * <p><b>NOTE:</b> DataTools용 유틸리티 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.08.03
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.03  이흥주          최초 생성
 *
 * </pre>
 */
public class DataToolsUtil {
    /**
     * 
     * DatabaseIdentifier 가져오기
     *
     * @param connectionInfo
     * @return
     */
    public static DatabaseIdentifier getDatabaseIdentifier(ISQLEditorConnectionInfo connectionInfo) {
        String profileName = connectionInfo.getConnectionProfileName();
        String dbName = connectionInfo.getDatabaseName();
        return new DatabaseIdentifier(profileName, dbName);

    }

    /**
     * 
     * Connection 가져오기
     *
     * @param databaseIdentifier
     * @return
     * @throws SQLException
     * @throws NoSuchProfileException
     */
    public static Connection getConnection(DatabaseIdentifier databaseIdentifier)
    throws SQLException, NoSuchProfileException {
        SQLDevToolsConfiguration f = SQLToolsFacade.getConfigurationByProfileName(databaseIdentifier.getProfileName());
        ConnectionService conService = f.getConnectionService();
        return conService.createConnection(databaseIdentifier, true);
    }

    /**
     * 
     * Profiles 가져오기
     *
     * @return
     */
    public static IConnectionProfile [] getProfiles() {
        return ProfileUtil.getProfiles();
    }

    /**
     * 
     * ProfileNames 가져오기
     *
     * @return
     */
    public static String[] getProfileNames() {
        IConnectionProfile[] profiles = ProfileUtil.getProfiles();
        String[] profileNames = new String[profiles.length];

        for(int i = 0; i < profiles.length; i++) {
            profileNames[i] = profiles[i].getName();
        }

        return profileNames;
    }

    /**
     * 
     * ConnectionInfo 가져오기
     *
     * @param profileName
     * @return
     */
    public static ISQLEditorConnectionInfo getConnectionInfo(String profileName) {
        return SQLUtility.getConnectionInfo(profileName);
    }
}
