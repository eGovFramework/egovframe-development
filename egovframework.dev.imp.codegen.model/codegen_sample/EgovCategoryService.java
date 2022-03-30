package org.egovframe.rte.sample.service;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:53
 */
public interface EgovCategoryService {

	/**
	 * 
	 * @param category
	 */
	public void deleteCategory(Category category) throws Exception;

	/**
	 * 
	 * @param category
	 */
	public void insertCategory(Category category) throws Exception;

	/**
	 * 
	 * @param str
	 */
	public Category selectCategory(String str) throws Exception;

	/**
	 * 
	 * @param searchVo
	 */
	public Page selectCategoryList(SearchVO searchVo) throws Exception;

	/**
	 * 
	 * @param category
	 */
	public void updateCategory(Category category) throws Exception;

}