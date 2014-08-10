<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<openmrs:portlet
    url="globalProperties"
    parameters="title=${title}|propertyPrefix=IHEInteroperability.|excludePrefix=IHEInteroperability.started|hidePrefix=false|readOnly=false"/>


<%@ include file="/WEB-INF/template/footer.jsp"%>