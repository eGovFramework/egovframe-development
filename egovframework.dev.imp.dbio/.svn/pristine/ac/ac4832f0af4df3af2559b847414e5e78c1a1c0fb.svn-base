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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.dev.imp.dbio.common.DbioLog;
import egovframework.dev.imp.dbio.util.DBUtil;

/**
 * 테스트 엔진
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
public class QueryTester {
	private final static String TEST_SQL_ID = "testQueryId";
	ConfigFileManager cfm = null;
	SqlMapClient sqlMapClient = null;

	public QueryTester(String sql, String sqlType) throws SQLException {
		try {
			cfm = ConfigFileManager.getConfigFileManagerInst();
			sqlMapClient = cfm.getSqlMapClientInst(sql, sqlType);
			sqlMapClient.setUserConnection(DBUtil.getConnection());
		} catch(SQLException se) {
			//se.printStackTrace();
			throw se;
		} catch(Exception e) {
			DbioLog.logError(e);
		}
	}

	public List<?> select(Map<?, ?> param) throws SQLException {
		List<?> result = null;

		try {
			result = sqlMapClient.queryForList(TEST_SQL_ID, param, 0, DBUtil.getMaxRow());
		} catch (SQLException se) {
			//se.printStackTrace();
			throw se;
		}
		return result;
	}	

	public Object insert(Map<?, ?> param) throws SQLException {
		Object result = null;

		try {
			result = sqlMapClient.insert(TEST_SQL_ID, param);
		} catch (SQLException se) {
			//se.printStackTrace();
			throw se;
		}
		return result;
	}	

	public int update(Map<?, ?> param) throws SQLException {
		int result = 0;

		try {
			result = sqlMapClient.update(TEST_SQL_ID, param);
		} catch (SQLException se) {
			//se.printStackTrace();
			throw se;
		}
		return result;
	}	

	public int delete(Map<?, ?> param) throws SQLException {
		int result = 0;

		try {
			result = sqlMapClient.delete(TEST_SQL_ID, param);
		} catch (SQLException se) {
			//se.printStackTrace();
			throw se;
		}
		return result;
	}
	
	public Object procedureCall(Map<?, ?> param) throws SQLException {
		try {
			return sqlMapClient.queryForObject(TEST_SQL_ID, param); 
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public Object runStatement(Map<?, ?> param) throws SQLException {
		try {
			return sqlMapClient.queryForObject(TEST_SQL_ID, param);
		} catch (SQLException e) {
			throw e;
		}
	}
}
