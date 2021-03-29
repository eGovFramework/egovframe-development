package egovframework.rte.sample.vo;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:52
 */
public class SearchVO implements Serializable {

	private int pageIndex;
	private String searchCondition;
	private String searchKeyword;
	private String searchUseYn;
	
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchUseYn() {
		return searchUseYn;
	}

	public void setSearchUseYn(String searchUseYn) {
		this.searchUseYn = searchUseYn;
	}

}