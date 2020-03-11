<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : egovSample2List.jsp
  * @Description : Sample2 List 화면
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Board List Basic</title>
<script type="text/javaScript" language="javascript" defer="defer">
<!--
function fncCheckAll() {
	var checkField = document.listForm.checkField;
   	if(document.listForm.checkAll.checked) {
   	   	if(checkField) {
   	   	   	if(checkField.length > 1) {
				for(var i=0; i < checkField.length; i++) {
					checkField[i].checked = true;
				}
   	   	   	} else {
   	   	  		checkField.checked = true;
   	   	   	}
   	   	}
   	} else {
   	   	if(checkField) {
   	   	   	if(checkField.length > 1) {
				for(var j=0; j < checkField.length; j++) {
					checkField[j].checked = false;
				}
   	   	   	} else {
   	   	  		checkField.checked = false;
   	   	   	}
   	   	}
   	}
}

function fncManageChecked() {
	var checkField = document.listForm.checkField;
	var id = document.listForm.checkId;
	var checkedIds = "";
	var checkedCount = 0;
	if(checkField) {
		if(checkField.length > 1) {
			for(var i=0; i < checkField.length; i++) {
				if(checkField[i].checked) {
					checkedIds += ((checkedCount==0? "" : ",") + id[i].value);
					checkedCount++;
				}
			}
		} else {
			if(checkField.checked) {
				checkedIds = id.value;
			}
		}
	}	
	if(checkedIds.length > 0) {
		alert(checkedIds);
	}
	return checkedIds;	
}

function fncSelectSample2(id) {
	document.listForm.selectedId.value = id;
   	document.listForm.action = "<c:url value='/sample2/updateSample2View.do'/>";
   	document.listForm.submit();		
}

function fncAddSample2View() {
   	document.listForm.action = "<c:url value='/sample2/addSample2View.do'/>";
   	document.listForm.submit();		
}

function linkPage(pageNo){
	document.listForm.pageIndex.value = pageNo;
	document.listForm.action = "<c:url value='/sample2/egovSample2List.do'/>";
   	document.listForm.submit();
}
-->
</script>
</head>
<body>
<form name="listForm" action="<c:url value='/sample2/egovSample2List.do'/>" method="post">
	<div>
		<div>
			글수 <strong><c:out value="${fn:length(resultList)}"/></strong>
		</div>
		<div>
			<div>
				<select name="searchCondition">
					<option value="0" <c:if test="${searchVO.searchCondition == '0'}">selected="selected"</c:if> >ID</option>
					<option value="1" <c:if test="${empty searchVO.searchCondition || searchVO.searchCondition == '1'}">selected="selected"</c:if> >Name</option>
				</select>
				<input name="searchKeyword" type="text" value="<c:out value="${searchVO.searchKeyword}"/>" title="검색" />
				<span style="vertical-align: top"><input type="submit" value="<spring:message code="button.search" />" /></span>
			</div>
		</div>
	</div>
	
	<br/>

	<fieldset>
		<legend>List Sample2</legend>
		<input name="selectedId" type="hidden" />
		<table border="1" cellspacing="0" summary="List of Sample2">
			<thead>
				<tr>
					<th scope="col">No.</th>
					<th scope="col">
						<input name="checkAll" type="checkbox" title="Check All" onclick="javascript:fncCheckAll();"/>
					</th>
					<th scope="col">Id</th>
					<th scope="col">Name</th>
					<th scope="col">UseYn</th>
					<th scope="col">Description</th>
					<th scope="col">RegUser</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr>
					<td><c:out value="${status.count}"/></td>
					<td>
						<input name="checkField" type="checkbox"/>
						<input name="checkId" type="hidden" value="<c:out value='${result.name}'/>"/>
					</td>
					<td><span onclick="javascript:fncSelectSample2('<c:out value="${result.id}"/>')"><c:out value="${result.id}"/></span></td>
					<td><c:out value="${result.name}"/></td>
					<td><c:out value="${result.useYn}"/></td>
					<td><c:out value="${result.description}"/></td>
					<td><c:out value="${result.regUser}"/></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div>
			<div>
				<span><button type="button" onclick="javascript:fncManageChecked();">Manage Checked</button></span>
				<a href="<c:url value='/sample2/egovSample2List.do'/>"><span>List All</span></a>
			</div>
			<div>
				<span><button type="button" onclick="javascript:fncAddSample2View();">등록</button></span>
			</div>
			<div>
				<ui:pagination paginationInfo = "${paginationInfo}"
						type="image"
						jsFunction="linkPage"
						/>
			</div>
			<div>
				<ui:pagination paginationInfo = "${paginationInfo}"
						type="default"
						jsFunction="linkPage"
						/>
				<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
			</div>
		</div>
	</fieldset>
</form>
</body>
</html>
