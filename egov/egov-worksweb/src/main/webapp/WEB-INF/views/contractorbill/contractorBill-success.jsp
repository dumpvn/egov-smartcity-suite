<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="page-container" id="page-container">
<input type="hidden" id="id" value="${contractorBillRegister.id }" />
		<div class="panel panel-primary" data-collapsed="0">
			<div class="alert text-center" style="color:green;">
				<c:if test="${contractorBillRegister.getId() != null}">
					<c:out value="${message }" />
				</c:if>
			</div>
			<div class="alert text-center" style="color:red;">
				<c:out value="${errorMessage }" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 text-center">
				<c:if test="${contractorBillRegister.billstatus == 'APPROVED' }">
					<a href="javascript:void(0)" class="btn btn-primary" onclick="renderPDF()" ><spring:message code="lbl.view.contractorbillpdf" /></a>
				</c:if>
				<input type="submit" name="closeButton"	id="closeButton" value="Close" Class="btn btn-default" onclick="window.close();" />
			</div>
		</div>
</div>
<script src="<c:url value='/resources/js/searchcontractorbill.js?rnd=${app_release_no}'/>"></script>