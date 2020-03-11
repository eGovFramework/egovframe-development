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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import egovframework.dev.imp.dbio.common.DbioLog;
import egovframework.dev.imp.dbio.util.DBUtil;

/**
 * 테스트 엔진
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
public class MapperQueryTester {
	private final static String TEST_SQL_ID = "testQueryId";
	MapperConfigFileManager cfm = null;
	SqlSession mapper = null;

	public MapperQueryTester(String sql, String sqlType) throws SQLException {
		try {
			cfm = MapperConfigFileManager.getConfigFileManagerInst();
			SqlSessionFactory sqlSession = cfm.getSqlSessionInst(sql, sqlType);
			Connection conn = DBUtil.getConnection();
			mapper = sqlSession.openSession(conn);
		} catch(Exception e) {
			DbioLog.logError(e);
		}
	}

	public List<?> select(Map<?, ?> param) throws SQLException {
		List<?> result = null;
        RowBounds rowBounds = new RowBounds(0, DBUtil.getMaxRow());
		result = mapper.selectList(TEST_SQL_ID, param, rowBounds);
		return result;
	}	

	public Object insert(Map<?, ?> param) throws SQLException {
		Object result = null;
	    result = mapper.insert(TEST_SQL_ID, param);
		return result;
	}	

	public int update(Map<?, ?> param) throws SQLException {
		int result = 0;
		result = mapper.update(TEST_SQL_ID, param);
		return result;
	}	

	public int delete(Map<?, ?> param) throws SQLException {
		int result = 0;
		result = mapper.delete(TEST_SQL_ID, param);
		return result;
	}
	/*
	public Object procedureCall(Map<?, ?> param) throws SQLException {
		return mapper.selectOne(TEST_SQL_ID, param); 
	}
	
	public Object runStatement(Map<?, ?> param) throws SQLException {
		return mapper.selectOne(TEST_SQL_ID, param);
	}
	*/
}
