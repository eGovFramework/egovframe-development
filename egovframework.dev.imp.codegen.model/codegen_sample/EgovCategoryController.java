package egovframework.rte.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import egovframework.rte.sample.service.EgovCategoryService;
import egovframework.rte.sample.vo.SearchVO;
import egovframework.rte.sample.model.Category;

/**
 * @version 1.0
 * @created 05-8-2009 오후 3:07:51
 */
@Controller
public class EgovCategoryController {

    @Resource(name = "egovCategoryService")
    private EgovCategoryService egovCategoryService;

	/**
	 * 
	 * @param category
	 */
	public void deleteCategory(Category category){

	}

	/**
	 * 
	 * @param category
	 */
	public void insertCategory(Category category){

	}

	/**
	 * 
	 * @param searchVO
	 */
	public ModelMap listCategory(SearchVO searchVO){
		return null;
	}

	/**
	 * 
	 * @param category
	 */
	public ModelMap selectCategory(Category category){
		return null;
	}

	/**
	 * 
	 * @param category
	 */
	public void updateCategory(Category category){

	}

}