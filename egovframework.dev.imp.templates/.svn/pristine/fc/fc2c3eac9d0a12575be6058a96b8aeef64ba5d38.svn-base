<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
 /**
  * @Class Name : egovSample2Register.java
  * @Description : Sample2 Register 화면
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
<c:set var="registerFlag" value="${empty sample2VO.id ? '등록' : '수정'}"/>
<title>Sample2 <c:out value="${registerFlag}"/> </title>
<script type="text/javaScript" language="javascript" defer="defer" src="<c:url value='/js/egovframework/cmmn/CommonScript.js'/>"></script>
<script type="text/javaScript" language="javascript" defer="defer">
<!--
function fncListSample2() {
   	document.detailForm.action = "<c:url value='/sample2/egovSample2List.do'/>";
   	document.detailForm.submit();		
}

function fncDeleteSample2() {
   	document.detailForm.action = "<c:url value='/sample2/deleteSample2.do'/>";
   	document.detailForm.submit();		
}
-->
</script>
</head>
<body>
<form name="detailForm" method="post"
	action="<c:url value="${registerFlag == '등록' ? '/sample2/addSample2.do' : '/sample2/updateSample2.do'}"/>" 
	onsubmit="javascript:return FormValidation(document.detailForm);" >
	<fieldset>
		<legend>Sample2 <c:out value="${registerFlag}"/></legend>
		<dl>
			<c:if test="${registerFlag == '수정'}">
				<dt><label for="id">Id</label> :</dt>
				<dd><input name="id" id="id" type="text" readonly="readonly" value="<c:out value='${sample2VO.id}'/>"/></dd>
			</c:if>
			<dt><label for="name">Name</label> :</dt>
			<dd><input name="name" id="name" type="text" value="<c:out value='${sample2VO.name}'/>" 
						required="true" fieldTitle="Name" maxLength="50" char="s" /></dd>
			<dt><label for="useYn">UseYn</label> :</dt>
			<dd>
				<select name="useYn" id="useYn" required="true" fieldTitle="UseYn">
					<option value="Y" <c:if test="${sample2VO.useYn == 'Y'}">selected="selected"</c:if> >Yes</option>
					<option value="N" <c:if test="${sample2VO.useYn == 'N'}">selected="selected"</c:if> >No</option>				
				</select>
			</dd>
		</dl>
		<div>
			<dl>
				<dt><label for="description">Description</label> :</dt>
				<dd>
					<div id="plainText">
						<textarea name="description" id="description" rows="" cols="20" title="description"
								fieldTitle="Description" maxLength="100" char="s"><c:out value='${sample2VO.description}'/></textarea>
					</div>
				</dd>
			</dl>
		</div>
		<dl>
			<dt><label for="regUser">RegUser</label> :</dt>
			<dd><input name="regUser" id="regUser" type="text" <c:if test="${registerFlag == '수정'}">readonly="readonly"</c:if> value="<c:out value='${sample2VO.regUser}'/>"
					required="true" fieldTitle="RegUser" maxLength="10" char="s" /></dd>
		</dl>
		<div>
			<span><button type="button" onclick="javascript:fncListSample2();">List</button></span>
			<span><input type="submit" value="<c:out value='${registerFlag}'/>"/></span>
			<c:if test="${registerFlag == '수정'}"><span><button type="button" onclick="javascript:fncDeleteSample2();">삭제</button></span></c:if>
			<span><button type="button" onclick="javascript:document.detailForm.reset();">Cancel</button></span>
		</div>
	</fieldset>
<!-- 검색조건 유지 -->
<input type="hidden" name="searchCondition" value="<c:out value='${searchVO.searchCondition}'/>"/>
<input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
<input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
</form>
</body>
</html>

