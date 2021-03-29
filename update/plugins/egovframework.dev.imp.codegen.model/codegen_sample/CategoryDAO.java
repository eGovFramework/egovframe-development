package egovframework.rte.sample.dao;

import egovframework.rte.sample.model.Category;
import egovframework.rte.sample.Page;
import egovframework.rte.sample.vo.SearchVO;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

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