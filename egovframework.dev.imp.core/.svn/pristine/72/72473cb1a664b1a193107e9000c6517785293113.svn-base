package egovframework.dev.imp.core.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class BatchTableColumn {
	String columnName = null;
	int columnWidth;
	int columnAlignment;

	public BatchTableColumn(String columnName, int columnWidth, int columnAlignment){
		this.columnName = columnName;
		this.columnAlignment = columnAlignment;
		this.columnWidth = columnWidth;
	}
	
	public BatchTableColumn(String columnName, int columnWidth){
		this(columnName, columnWidth, SWT.CENTER);
	}
	
	/**
	 * columnName의 값을 가져온다
	 *
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * columnName의 값을 설정한다.
	 *
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * columnWidth의 값을 가져온다
	 *
	 * @return the columnWidth
	 */
	public int getColumnWidth() {
		return columnWidth;
	}
	/**
	 * columnWidth의 값을 설정한다.
	 *
	 * @param columnWidth
	 */
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
	/**
	 * columnAlignment의 값을 가져온다
	 *
	 * @return the columnAlignment
	 */
	public int getColumnAlignment() {
		return columnAlignment;
	}
	/**
	 * columnAlignment의 값을 설정한다.
	 *
	 * @param columnAlignment
	 */
	public void setColumnAlignment(int columnAlignment) {
		this.columnAlignment = columnAlignment;
	}
	
	public void setColumnToTable(Table table){
		TableColumn column = new TableColumn(table, columnAlignment);
		column.setText(columnName);
		column.setWidth(columnWidth);
	}

}
