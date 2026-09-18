<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
  /**
  * @Class Name : sample2Register.jsp
  * @Description : Sample2 Register 화면
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  실행환경 개발팀  홍길동          최초 생성
  *
  * author 홍길동
  * since 실행환경 개발팀
  *
  * Copyright (C) All right reserved.
  */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <c:set var="registerFlag" value="${empty sample2VO.id ? 'create' : 'modify'}"/>
    <title>Sample2 <c:if test="${registerFlag == 'create'}">Regist</c:if>
                  <c:if test="${registerFlag == 'modify'}">Update</c:if>
    </title>

    <!-- KRDS CSS -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/component/output.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/egovframe.css'/>" />
    <script type="text/javascript" src="<c:url value='/js/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/component/ui-script.js'/>" defer></script>
    <script type="text/javascript" src="<c:url value='/js/egovframework/common.js'/>" defer></script>

    <!--For Custom Validation-->
    <!-- egovframe-Todo: Validator 적용시 아래 주석 코드 참고 -->
    <!-- <script type="text/javascript" src="<c:url value='/js/egovframework/EgovValidation.js'/>" defer></script> -->

    <script type="text/javascript" defer>

    function fn_egov_list() {
    	document.detailForm.action = "<c:url value='/sample2/sample2List.do'/>";
       	document.detailForm.method = 'get';
       	document.detailForm.submit();
    }

    function fn_egov_add() {
        if (confirm('등록하시겠습니까?')) {
            let frm = document.detailForm;
        
            // egovframe-Todo: Validator 적용시 아래 주석 코드 참고
            // EgovValidation.js 파일에 validateSample2VO 함수 추가 필요
            // if (!validateSample2VO(frm)) {
            //     return;
            // }
            
            frm.action = "<c:url value='/sample2/addSample2.do'/>";
            frm.submit();
        }
    }

    function fn_egov_update() {
        if (confirm('수정하시겠습니까?')) {
            let frm = document.detailForm;
            
            // egovframe-Todo: Validator 적용시 아래 주석 코드 참고
            // EgovValidation.js 파일에 validateSample2VO 함수 추가 필요
            // if (!validateSample2VO(frm)) {
            //     return;
            // }
            
            frm.action = "<c:url value='/sample2/updateSample2.do'/>";
            frm.submit();
        }
    }

    function fn_egov_delete() {
        if (confirm('삭제하시겠습니까?')) {
        	document.detailForm.action = "<c:url value='/sample2/deleteSample2.do'/>";
           	document.detailForm.submit();
        }
    }

    function fn_egov_reset() {
        $('form').each(function() {
            this.reset();
        });
    }
    </script>
</head>

<body>
<div id="container" class="inner">

	<!-- Page Title -->
	<h2 class="heading-large">
		<c:if test="${registerFlag == 'create'}">Regist</c:if>
		<c:if test="${registerFlag == 'modify'}">Update</c:if>
	</h2>

	<form:form id="detailForm" name="detailForm" modelAttribute="sample2VO">
	<input type="hidden" id="searchCondition" name="searchCondition" value="${sample2VO.searchCondition}" />
	<input type="hidden" id="searchKeyword" name="searchKeyword" value="${sample2VO.searchKeyword}" />
	<input type="hidden" id="pageIndex" name="pageIndex" value="${sample2VO.pageIndex}" />

	<spring:message code="confirm.required.name" var="placeholderName"/>
	<spring:message code="confirm.required.description" var="placeholderDescription"/>
	<spring:message code="confirm.required.user" var="placeholderUser"/>

		<div class="conts-wrap">
			<div class="fieldset input-form">
			
                <div class="form-group">
                    <div class="form-tit">
                        <label for="id">Id</label>
                    </div>
                    <div class="form-conts">
                        <form:input path="id" cssClass="krds-input"/>
                        <form:errors path="id" cssClass="error-message" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-tit">
                        <label for="name">Name</label>
                    </div>
                    <div class="form-conts">
                        <form:input path="name" cssClass="krds-input"/>
                        <form:errors path="name" cssClass="error-message" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-tit">
                        <label for="description">Description</label>
                    </div>
                    <div class="form-conts">
                        <form:input path="description" cssClass="krds-input"/>
                        <form:errors path="description" cssClass="error-message" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-tit">
                        <label for="useYn">UseYn</label>
                    </div>
                    <div class="form-conts">
                        <form:input path="useYn" cssClass="krds-input"/>
                        <form:errors path="useYn" cssClass="error-message" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="form-tit">
                        <label for="regUser">RegUser</label>
                    </div>
                    <div class="form-conts">
                        <form:input path="regUser" cssClass="krds-input"/>
                        <form:errors path="regUser" cssClass="error-message" />
                    </div>
                </div>

            </div>

			<!-- Action Buttons -->
			<div class="page-btn-wrap">
                <div class="btn-wrap">
	                <button type="button" class="krds-btn medium secondary" onclick="fn_egov_list()">List</button>
	                <button type="button" class="krds-btn medium tertiary" onclick="fn_egov_reset()">Reset</button>
                </div>
				<div class="btn-wrap">
					<c:if test="${registerFlag == 'modify'}">
						<button type="button" class="krds-btn medium" onclick="fn_egov_update()">Update</button>
						<button type="button" class="krds-btn medium danger" onclick="fn_egov_delete()">Delete</button>
					</c:if>
					<c:if test="${registerFlag == 'create'}">
						<button type="button" class="krds-btn medium" onclick="fn_egov_add()">Regist</button>
					</c:if>
				</div>
	        </div>
		</div>

	</form:form>

</div>

</body>
</html>

