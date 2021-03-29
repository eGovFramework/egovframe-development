package egovframework.rte.sample.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

import egovframework.rte.sample.service.EgovCategoryService;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:53
 */
@Service("egovCategoryService")
public class EgovCategoryServiceImpl extends AbstractServiceImpl implements EgovCategoryService {

    @Resource(name="categoryDAO")
	public CategoryDAO categoryDAO;

	/**
	 * 
	 * @param category
	 */
	public void deleteCategory(Category category) throws Exception{

	}

	/**
	 * 
	 * @param category
	 */
	public void insertCategory(Category category) throws Exception{
	
	}

	/**
	 * 
	 * @param str
	 */
	public Category selectCategory(String str) throws Exception{
		return null;
	}

	/**
	 * 
	 * @param searchVO
	 */
	public Page selectCategoryList(SearchVO searchVO) throws Exception{
		return null;
	}

	/**
	 * 
	 * @param category
	 */
	public void updateCategory(Category category) throws Exception{

	}

}