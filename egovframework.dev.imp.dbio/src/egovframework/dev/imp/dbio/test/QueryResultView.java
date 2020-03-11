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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

/**
 * QueryResultView 화면
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
public class QueryResultView extends ViewPart implements ResultView {

	public static final String ID = "egovframework.dev.imp.dbio.view.queryResult";
	private TableViewer resultViewer;
	private List<Map<String, String>> queryResult = null;
	private String [] colNames = {"Empty1", "Empty2"};
	
	/**
	 * 화면 구성
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		
		//데이타 초기화
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("", "");
		queryResult = new ArrayList<Map<String, String>>(1);
		queryResult.add(map);
		setQueryResultData(queryResult);

		
		resultViewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		createQueryResultPart();
	}
	
	public void clearQueryResultPart() {
		Table table = resultViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.clearAll();
		removeTableColumn(table);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = colNames.length;
		table.setLayoutData(gd);		
	}
	public void createQueryResultPart() {
		Table table = resultViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.clearAll();
		removeTableColumn(table);
						
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = colNames.length;
		table.setLayoutData(gd);
		
		TableColumn col = null;		
		for(int i=0; i < colNames.length;i++) {
			col = new TableColumn(table, SWT.NONE);
			col.setText(colNames[i]);
			col.setWidth(150);
		}
		
		resultViewer.setLabelProvider(new ResultTableLabelProvider());
		//데이타 넣기
		Map<?, ?> map = null;
		for(int i=0; i < queryResult.size(); i++) {
			map = (Map<?, ?>) queryResult.get(i);
			resultViewer.add(map);
		}

	}
	
	private void removeTableColumn(Table table) {
		int columnCount = table.getColumnCount();
		for (int i = 0; i < columnCount; i++){
			table.getColumn(0).dispose();
		}
	}

	/**
	 * 테스트 결과값을 설정한다.
	 * 주로 EditPart에서 호출
	 * @param queryResult
	 */
	public void setQueryResultData(List<Map<String, String>> queryResult) {
		this.queryResult = queryResult;
		Iterator<Map<String, String>> itr = queryResult.iterator();
		
		if (itr.hasNext()) {
			Map<?, ?> row = (Map<?, ?>) itr.next();
			Object [] rs = row.keySet().toArray();
			colNames = new String[rs.length];
			
			for (int i =0 ; i<rs.length; i++) {
				colNames[i] = (String)rs[i];
			}
		}
	}
	
	@Override
	public void setFocus() {

	}
	
	private class ResultTableLabelProvider extends LabelProvider implements
	ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Map<?, ?> row = (Map<?, ?>) element;
			Object colValue = row.get(colNames[columnIndex]);
			
			if (colValue == null) {
				return "";
			} else {
				return colValue.toString();
			}
		}
	}

}
