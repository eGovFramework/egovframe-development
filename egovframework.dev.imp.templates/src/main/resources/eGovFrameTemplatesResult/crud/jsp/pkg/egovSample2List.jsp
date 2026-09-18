<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
/**
* @Class Name : sample2List.jsp
* @Description : Sample2 List 화면
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>List</title>

	<!-- KRDS CSS -->
	<link type="text/css" rel="stylesheet" href="<c:url value='/css/component/output.css'/>" />
	<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframe.css'/>" />
	<script type="text/javaScript" language="javascript" src="<c:url value='/js/jquery.min.js'/>" defer="defer"></script>
	<script type="text/javaScript" language="javascript" src="<c:url value='/js/egovframework/common.js'/>" defer="defer"></script>

	<script type="text/javaScript" language="javascript" defer="defer">
	<!--
		/* SAMPLE2 수정 화면 function */
		function fn_egov_select(id) {
			document.listForm.id.value = id;
			document.listForm.action = "<c:url value='/sample2/updateSample2View.do'/>";
			document.listForm.submit();
		}

		/* SAMPLE2 등록 화면 function */
		function fn_egov_addView() {
			document.listForm.action = "<c:url value='/sample2/addSample2View.do'/>";
			document.listForm.submit();
		}

		/* SAMPLE2 목록 화면 function */
		function fn_egov_selectList() {
			if(document.listForm.searchKeyword.value == '') {
				alert("검색어를 입력해주세요.");
				return false;
			}
			document.listForm.action = "<c:url value='/sample2/sample2List.do'/>";
			document.listForm.method = "get";
			document.listForm.submit();
		}

		/* pagination 페이지 링크 function */
		function fn_egov_link_page(pageNo) {
			document.listForm.pageIndex.value = pageNo;
			document.listForm.action = "<c:url value='/sample2/sample2List.do'/>";
			document.listForm.method = "get";
			document.listForm.submit();
		}
	//-->
	</script>
</head>

<body>
<div id="container" class="inner">

	<!-- 타이틀 -->
	<h2 class="heading-large">list</h2>
	<!-- // 타이틀 -->

	<div id="content_pop">
		<form:form modelAttribute="sample2VO" id="listForm" name="listForm" method="post">
			<input type="hidden" id="id" name="id" />
			<input type="hidden" id="pageIndex" name="pageIndex" value="1" />

			<!-- Search Form -->
			<div class="form-group">
				<div class="search-wrap">
					<div class="search-body">
						<div class="form-conts searchOption">
							<select id="searchCondition" name="searchCondition" class="krds-form-select medium" title="검색어를 선택하세요.">
								<option value="1" <c:if test ="${not empty sample2VO.searchCondition and sample2VO.searchCondition eq 1}">selected="selected"</c:if>>Id</option>
								<option value="0" <c:if test ="${not empty sample2VO.searchCondition and sample2VO.searchCondition eq 0}">selected="selected"</c:if>>Id</option>
							</select>
						</div>
						<div class="form-conts btn-ico-wrap searchKeyword">
							<input type="text" id="searchKeyword" name="searchKeyword" value="${sample2VO.searchKeyword}" class="krds-input medium" placeholder="검색어를 입력하세요.">
							<button type="button" class="krds-btn medium icon" onclick="fn_egov_selectList()">
								<span class="sr-only">검색</span>
								<i class="svg-icon ico-sch"></i>
							</button>
						</div>
					</div>
					<div class="page-btn-wrap">
						<button type="button" class="krds-btn medium" onclick="fn_egov_addView()">등록</button>
					</div>
				</div>
			</div>
		</form:form>

		<!-- List -->
		<div class="krds-table-wrap">
			<table class="tbl col data">
				<colgroup>
					<col style="width: 10%;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
				</colgroup>
				<thead>
					<tr>
						<th scope="col" class="text-center">No</th>
						<th scope="col" class="text-center">Id</th>
						<th scope="col" class="text-center">Name</th>
						<th scope="col" class="text-center">Description</th>
						<th scope="col" class="text-center">UseYn</th>
						<th scope="col" class="text-center">RegUser</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty resultList}">
							<c:forEach var="result" items="${resultList}" varStatus="status">
								<tr>
									<td class="text-center"><c:out value="${paginationInfo.totalRecordCount+1 - ((sample2VO.pageIndex-1) * sample2VO.pageSize + status.count)}"/></td>
									<td class="text-center"><c:out value="${result.id}"/></td>
									<td class="text-center"><c:out value="${result.name}"/></td>
									<td class="text-center"><c:out value="${result.description}"/></td>
									<td class="text-center"><c:out value="${result.useYn}"/></td>
									<td class="text-center"><c:out value="${result.regUser}"/></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td class="text-center" colspan="6">해당 데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<!-- // List -->

		<!-- Pagination -->
		<div id="paging" class="krds-pagination w-page">
			<ui:pagination paginationInfo="${paginationInfo}" type="krds" jsFunction="fn_egov_link_page" />
		</div>

	</div>

</div>

</body>
</html>
