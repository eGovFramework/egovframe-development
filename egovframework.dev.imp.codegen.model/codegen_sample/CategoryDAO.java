package org.egovframe.rte.sample.dao;

import org.egovframe.rte.sample.model.Category;
import org.egovframe.rte.sample.Page;
import org.egovframe.rte.sample.vo.SearchVO;

import org.springframework.stereotype.Repository;
import org.egovframe.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:55
 */
@Repository("categoryDAO")
public class CategoryDAO extends EgovAbstractDAO {

	/**
	 * 
	 * @param category
	 */
	public void deleteCategory(Category category) throws Exception {

	}

	/**
	 * 
	 * @param category
	 */
	public String insertCategory(Category category) throws Exception {
		return "";
	}

	/**
	 * 
	 * @param str
	 */
	public Category selectCategory(String str) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param searchVO
	 */
	public Page selectCategoryList(SearchVO searchVO) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param category
	 */
	public void updateCategory(Category category) throws Exception {

	}

}