package pkg.web;

import java.util.List;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pkg.service.Sample2Service;
import pkg.service.Sample2VO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * @Class Name : Sample2Controller.java
 * @Description : Sample2 Controller class
 * @Modification Information
 *
 * @author 홍길동
 * @since 실행환경 개발팀
 * @version 1.0
 * @see
 *
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Sample2Controller {

	/** Sample2Service */
	@Resource(name = "sample2Service")
	private Sample2Service sample2Service;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	/**
	 * SAMPLE2 목록을 조회한다. (pageing)
	 * @param sample2VO - 조회할 정보가 담긴 Sample2VO
	 * @param model
	 * @return "sample2/sample2List"
	 * @exception Exception
	 */
	@GetMapping("/sample2/sample2List.do")
	public String selectSample2List(@ModelAttribute("sample2VO") Sample2VO sample2VO, ModelMap model) throws Exception {

		/** EgovPropertyService.sample */
		sample2VO.setPageUnit(propertiesService.getInt("pageUnit"));
		sample2VO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing setting */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(sample2VO.getPageIndex());
		paginationInfo.setRecordCountPerPage(sample2VO.getPageUnit());
		paginationInfo.setPageSize(sample2VO.getPageSize());

		sample2VO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		sample2VO.setLastIndex(paginationInfo.getLastRecordIndex());
		sample2VO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		/** List */
		List<?> sample2List = sample2Service.selectSample2List(sample2VO);
		model.addAttribute("resultList", sample2List);

		/** Count */
		int totCnt = sample2Service.selectSample2ListTotCnt(sample2VO);
		paginationInfo.setTotalRecordCount(totCnt);

		/** Pagination */
		model.addAttribute("paginationInfo", paginationInfo);

		return "sample2/sample2List";
	}

	/**
	 * SAMPLE2 등록 화면을 조회한다.
	 * @param sample2VO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "sample2/sample2Register"
	 * @exception Exception
	 */
	@PostMapping("/sample2/addSample2View.do")
	public String addSample2View(@ModelAttribute("sample2VO") Sample2VO sample2VO, Model model) throws Exception {
	
		model.addAttribute("sample2VO", sample2VO);
		
		return "sample2/sample2Register";
	}

	/**
	 * SAMPLE2을 등록한다.
	 * @param sample2VO - 등록할 정보가 담긴 VO
	 * @param bindingResult
	 * @param model
	 * @param status
	 * @return "redirect:/sample2/sample2List.do"
	 * @exception Exception
	 */
	@PostMapping("/sample2/addSample2.do")
	public String addSample2(@Valid @ModelAttribute("sample2VO") Sample2VO sample2VO, BindingResult bindingResult, Model model, SessionStatus status) throws Exception {

		if (bindingResult.hasErrors()) {
			model.addAttribute("sample2VO", sample2VO);
			return "sample2/sample2Register";
		}

		sample2Service.insertSample2(sample2VO);
		status.setComplete();

		return "redirect:/sample2/sample2List.do";
	}

	/**
	 * SAMPLE2 수정화면을 조회한다.
	 * @param id - 수정할 SAMPLE2 id
	 * @param model
	 * @return "sample2/sample2Register"
	 * @exception Exception
	 */
	@PostMapping("/sample2/updateSample2View.do")
	public String updateSample2View(@ModelAttribute("sample2VO") Sample2VO sample2VO, Model model) throws Exception {
	
		Sample2VO detail = sample2Service.selectSample2(sample2VO);
		detail.setSearchCondition(sample2VO.getSearchCondition());
		detail.setSearchKeyword(sample2VO.getSearchKeyword());
		detail.setPageIndex(sample2VO.getPageIndex());
		
		model.addAttribute("sample2VO", detail);
		
		return "sample2/sample2Register";
	}

	/**
	 * SAMPLE2을 수정한다.
	 * @param sample2VO - 수정할 정보가 담긴 VO
	 * @param bindingResult
	 * @param model
	 * @param status
	 * @return "redirect:/sample2/sample2List.do"
	 * @exception Exception
	 */
	@PostMapping("/sample2/updateSample2.do")
	public String updateSample2(@Valid @ModelAttribute("sample2VO") Sample2VO sample2VO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, SessionStatus status) throws Exception {

		if (bindingResult.hasErrors()) {
			model.addAttribute("sample2VO", sample2VO);
			return "sample2/sample2Register";
		}

		sample2Service.updateSample2(sample2VO);
		status.setComplete();

		redirectAttributes.addAttribute("searchCondition", sample2VO.getSearchCondition());
		redirectAttributes.addAttribute("searchKeyword", sample2VO.getSearchKeyword());
		redirectAttributes.addAttribute("pageIndex", sample2VO.getPageIndex());

		return "redirect:/sample2/sample2List.do";
	}

	/**
	 * SAMPLE2을 삭제한다.
	 * @param sample2VO - 삭제할 정보가 담긴 VO
	 * @param model
	 * @param status
	 * @return "redirect:/sample2/sample2List.do"
	 * @exception Exception
	 */
	@PostMapping("/sample2/deleteSample2.do")
	public String deleteSample2(@ModelAttribute("sample2VO") Sample2VO sample2VO, RedirectAttributes redirectAttributes, SessionStatus status) throws Exception {
		sample2Service.deleteSample2(sample2VO);
		status.setComplete();

		redirectAttributes.addAttribute("searchCondition", sample2VO.getSearchCondition());
		redirectAttributes.addAttribute("searchKeyword", sample2VO.getSearchKeyword());
		redirectAttributes.addAttribute("pageIndex", sample2VO.getPageIndex());

		return "redirect:/sample2/sample2List.do";
	}

}
