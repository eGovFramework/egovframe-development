package pkg.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.rte.cmmn.SampleDefaultVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import pkg.service.EgovSample2Service;
import pkg.service.Sample2VO;

/**
 * @Class Name : EgovSample2Controller.java
 * @Description : Sample2 Controller class
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2009.02.01    홍길동          최초 생성
 *
 *  @author 실행환경 개발팀 홍길동
 *  @since 2009.02.01
 *  @version 1.0
 *  @see
 *  
 *  Copyright (C) 2009 by MOSPA  All right reserved.
 */

@Controller
@SessionAttributes(types=Sample2VO.class)
public class EgovSample2Controller {

    @Resource(name = "sample2Service")
    private EgovSample2Service sample2Service;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
	
    /**
	 * SAMPLE2 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 SampleDefaultVO
	 * @return "/sample2/egovSample2List"
	 * @exception Exception
	 */
    @RequestMapping(value="/sample2/egovSample2List.do")
    public String selectSample2List(@ModelAttribute("searchVO") SampleDefaultVO searchVO, 
    		ModelMap model)
            throws Exception {
    	
    	/** EgovPropertyService.sample */
    	searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
    	searchVO.setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
        List sample2List = sample2Service.selectSample2List(searchVO);
        model.addAttribute("resultList", sample2List);
        
        int totCnt = sample2Service.selectSample2ListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/sample2/egovSample2List";
    } 
    
    @RequestMapping("/sample2/addSample2View.do")
    public String addSample2View(
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
            throws Exception {
        model.addAttribute("sample2VO", new Sample2VO());
        return "/sample2/egovSample2Register";
    }
    
    @RequestMapping("/sample2/addSample2.do")
    public String addSample2(
            Sample2VO sample2VO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status)
            throws Exception {
        sample2Service.insertSample2(sample2VO);
        status.setComplete();
        return "forward:/sample2/egovSample2List.do";
    }
    
    @RequestMapping("/sample2/updateSample2View.do")
    public String updateSample2View(
            @RequestParam("selectedId") String id ,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, Model model)
            throws Exception {
        Sample2VO sample2VO = new Sample2VO();
        sample2VO.setId(id);
        // 변수명은 CoC 에 따라 sample2VO
        model.addAttribute(selectSample2(sample2VO, searchVO));
        return "/sample2/egovSample2Register";
    }

    @RequestMapping("/sample2/selectSample2.do")
    public @ModelAttribute("sample2VO")
    Sample2VO selectSample2(
            Sample2VO sample2VO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO) throws Exception {
        return sample2Service.selectSample2(sample2VO);
    }

    @RequestMapping("/sample2/updateSample2.do")
    public String updateSample2(
            Sample2VO sample2VO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status)
            throws Exception {
        sample2Service.updateSample2(sample2VO);
        status.setComplete();
        return "forward:/sample2/egovSample2List.do";
    }
    
    @RequestMapping("/sample2/deleteSample2.do")
    public String deleteSample2(
            Sample2VO sample2VO,
            @ModelAttribute("searchVO") SampleDefaultVO searchVO, SessionStatus status)
            throws Exception {
        sample2Service.deleteSample2(sample2VO);
        status.setComplete();
        return "forward:/sample2/egovSample2List.do";
    }

}
