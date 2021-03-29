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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * Tester 호출 객체
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
public class TestManager implements QueryTest{
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public List runQyery(String sql, Map<?, ?> param) throws SQLException{
		List result = null;
		//Object tmpResult = null;
		String key = null;
		
		ConfigFileManager cfm = ConfigFileManager.getConfigFileManagerInst();
		
		key = StringUtil.getQueryType(sql);
		
		QueryTester tester = new QueryTester(sql.substring(sql.indexOf(StringUtil.SEPARATOR) + 1), key);

		try {
			if ("SELECT".equals(key)) {
				result = tester.select(param);
				if (result.size()==0){
					Map<String, String> map = new HashMap<String, String>(1);
					map.put("Result", DbioMessages.query_result_zero_info);
					result.add(map);
				}
			}
			else if ("INSERT".equals(key)) {
				//결과 형태가 불분명
				Object tmp = tester.insert(param);

				result = new ArrayList(1);
				if (tmp instanceof HashMap) { 
					result.add(tmp);
				} else {
					Map map = new HashMap(1);
					map.put("Result", "Success!");
					result.add(map);
				}
			}
			else if ("UPDATE".equals(key)) {
				Map map = new HashMap(1);
				map.put("Result", tester.update(param) + " rows update!");

				result = new ArrayList(1);
				result.add(map);
			}
			else if ("DELETE".equals(key)) {
				Map map = new HashMap(1);
				map.put("Result", tester.delete(param) + " rows delete!");

				result = new ArrayList(1);
				result.add(map);
			}
			else if ("STATEMENT".equals(key)) {
				result = tester.select(param);
				if (result.size()==0){
					Map map = new HashMap(1);
					map.put("Result", "Success!");
					result.add(map);
				}
			}			
			else if ("PROCEDURE".equals(key)){
				tester.procedureCall(param);
				
				result = new ArrayList(1);
				result.add(param);
			}
			else throw new SQLException("Improper SQL Sentence");
		} catch (SQLException e) {
			throw e;
		}
		
		return result;
	}
}
