This example is based on the one posted by Edwin Biemond at: http://biemond.blogspot.be/2012/08/generating-ejb-sdo-service-interface.html

To purpose of this example was to expose the EJB based SDO example from above also through Soap.

To achieve this I had to do the following:

1) Add a WAR deployment profile to the ModelSDO project in JDeveloper:

Right click on the 'ModelSDO' project, choose 'Project Properties', go to 'Deployment' and create a new Deployment Profile by clicking on 'New...'.  In the Create Deployment Profile choose 'WAR File' as Achive Type and name it for example 'SdoEjbService', then click 'OK' to confirm.

2) Before we continue with the web archive part, we will first alter the EAR deployment profile so that the 'ModelSDO' EJB and WAR gets well deployed.

To achieve this click on the context menu right of the 'EjbSdoService' application and select 'Application Properties...'.
There you'll see a Deployment Profile named 'ejbsdoservice (EAR File)', select it and click on 'Edit...'.
Under 'Application Assembly', make sure 'hrSdoEjbService' and 'SdoEjbService' is selected and click on 'OK'.  This will allow the assembling of the EJB and WAR within the EAR.
Also click on 'OK' when you are returned to the 'Application Properties' popup.

3) Now that the ModelSDO is WAR aware, we need to add a web deployment descriptor (e.g. web.xml).  To achieve this do the following:

Right click on the 'ModelSDO' project, select 'New...' from the context menu.  In the tab 'Current Project Technologies', make sure the tree node 'Categories' is set to 'General' and 'Deployment Descriptors'.
There you need to select the item labeled 'Java EE Deployment Descriptor' and click on 'OK'. In the dialog 'Create Java EE Deployment Descriptor', make sure you select 'web.xml' in step 1 and click on 'Next >'.
Set the deployment descriptor version to 2.5 and hit 'Next >'. In the last step just click on 'Finish'.

Now you'll notice that the 'ModelSDO' project has a third directory named 'Web Content' containing a 'WEB-INF' directory with 2 files, namely 'weblogic.xml' and the just created deployment descriptor named 'web.xml'.

3) Now click on the source mode of the web.xml file and copy/paste the following content in it:

<web-app xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
     version="2.5">
    <!-- https://forums.oracle.com/forums/thread.jspa?threadID=987232 -->
    <servlet>
        <display-name>nl.amis.sdo.jpa.services.HrSessionEJBBeanWSForward</display-name>
        <servlet-name>nl.amis.sdo.jpa.services.HrSessionEJBBeanWSForward</servlet-name><!-- name of the servlet mapping to HrSessionEJBBean WebService -->
        <servlet-class>oracle.j2ee.ws.server.WSForwardServlet</servlet-class>
        <init-param>
            <param-name>to</param-name>
            <param-value>/HrSessionEJBBeanWS</param-value><!-- servlet path mapping to HrSessionEJBBean WebService -->
        </init-param>
    </servlet>
    <servlet>
        <servlet-name>nl.amis.sdo.jpa.services.HrSessionEJBBean</servlet-name><!-- name of the servlet mapping to HrSessionEJBBean WebService -->
        <servlet-class>oracle.j2ee.ws.server.provider.ProviderServlet</servlet-class>
        <init-param>
            <param-name>Oracle.JAX-WS.EjbLink</param-name>
            <param-value>ejb/HrSessionEJB</param-value><!-- link to the ejb HrSessionEJB -->
        </init-param>
        <init-param>
            <param-name>Oracle.JAX-WS.ServiceEndpointBean</param-name>
            <param-value>nl.amis.sdo.jpa.services.HrSessionEJBBean</param-value><!-- fully qualified Java name of the HrSessionEJB implementation -->
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>nl.amis.sdo.jpa.services.HrSessionEJBBean</servlet-name>
        <url-pattern>/HrSessionEJBBeanWS</url-pattern><!-- servlet path mapping to HrSessionEJBBean WebService -->
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>nl.amis.sdo.jpa.services.HrSessionEJBBeanWSForward</servlet-name>
        <url-pattern>/HrSessionEJBBeanWS/secure</url-pattern><!-- secured servlet path mapping to HrSessionEJBBean WebService -->
    </servlet-mapping>
    <login-config>
        <auth-method>BASIC</auth-method>
    </login-config>
    <security-role>
        <role-name>Admin</role-name>
    </security-role>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>nl.amis.sdo.jpa.services.HrSessionEJBBeanWS</web-resource-name>
            <url-pattern>/HrSessionEJBBeanWS/secure</url-pattern><!-- secured servlet path mapping to HrSessionEJBBean WebService -->
        </web-resource-collection>
        <auth-constraint>
            <role-name>Admin</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <ejb-ref><!-- define EJB reference in WAR for use above in ProviderServlet definition -->
        <ejb-ref-name>ejb/HrSessionEJB</ejb-ref-name>
        <ejb-ref-type>Session</ejb-ref-type>
        <remote>nl.amis.sdo.jpa.services.HrSessionEJB</remote>
        <ejb-link>HrSessionEJB</ejb-link>
    </ejb-ref>
</web-app>

If you're wondering how I defined the above web.xml file, I just took the content of a web.xml from an ADF-BC SDO generated WAR and modified it so that it matched my web service definition.

4) Then from that same generated ADF-BC SDO WAR file, I also copied the following files to the ModelSDO project directory under ‘public_html\WEB-INF’ (shown as Web Content/WEB-INF in JDeveloper):

	- oracle-webservices.xml
	- standard-webservices.xml
	
5) I also modified the content of both files so that they matched the definitions of my web service.

	For oracle-webservices.xml:
	
	<?xml version="1.0" encoding="UTF-8"?>
<oracle-webservices xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:noNamespaceSchemaLocation="http://xmlns.oracle.com/oracleas/schema/11/oracle-webservices-11_1.xsd"
                    schema-major-version="11" schema-minor-version="1">
  <context-root>EjbSdoService-ModelSDO-context-root</context-root><!-- context root of the WAR -->
  <webservice-description name="{/nl.amis.sdo.jpa.services/}HrSessionEJBBeanWS"><!-- fully qualified WebService name -->
    <expose-wsdl>true</expose-wsdl>
    <expose-testpage>true</expose-testpage>
    <schema-file-mappings>
      <schema-file-mapping advertisedName="/nl/amis/sdo/jpa/entities/EmployeesSDO.xsd"
                           path="/nl/amis/sdo/jpa/entities/EmployeesSDO.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the xsd defintion (mind entities) -->
      <schema-file-mapping advertisedName="/nl/amis/sdo/jpa/entities/DepartmentsSDO.xsd"
                           path="/nl/amis/sdo/jpa/entities/DepartmentsSDO.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the xsd defintion (mind entities) -->
      <schema-file-mapping advertisedName="/nl/amis/sdo/jpa/services/HrSessionEJBBeanWS.xsd"
                           path="/nl/amis/sdo/jpa/services/HrSessionEJBBeanWS.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the xsd defintion (mind services) -->
      <schema-file-mapping advertisedName="/xml/datagraph.xsd"
                           path="/xml/datagraph.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the commonj SDO xsd defintions -->
      <schema-file-mapping advertisedName="/xml/sdoModel.xsd"
                           path="/xml/sdoModel.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the commonj SDO xsd defintions -->
      <schema-file-mapping advertisedName="/xml/sdoJava.xsd"
                           path="/xml/sdoJava.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the commonj SDO xsd defintions -->
      <schema-file-mapping advertisedName="/xml/sdoXML.xsd"
                           path="/xml/sdoXML.xsd"/><!-- full path (starting form WEB-INF/wsdl) to the commonj SDO xsd defintions -->
    </schema-file-mappings>
    <port-component name="HrSessionEJBBeanWS" enabled="true"
                    schemaValidateInput="false">
      <mtom-support threshold="0">false</mtom-support>
      <endpoint-address-uri>/HrSessionEJBBeanWS</endpoint-address-uri><!-- servlet path mapping to HrSessionEJBBean WebService -->
    </port-component>
    <enable-mex>true</enable-mex>
  </webservice-description>
</oracle-webservices>

	Mind the content-root element (jbSdoService-ModelSDO-context-root), it the same as the one proposed by default when we created the WAR deployment profile.
	
	For standard-webservices.xml:
	
	<?xml version="1.0" encoding="UTF-8"?>
<webservices xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/javaee_web_services_1_2.xsd" version="1.2">
  <webservice-description>
    <webservice-description-name>{/nl.amis.sdo.jpa.services/}HrSessionEJBBeanWS</webservice-description-name><!-- fully qualified WebService name -->
    <wsdl-file>WEB-INF/wsdl/nl/amis/sdo/jpa/services/HrSessionEJBBeanWS.wsdl</wsdl-file><!-- location of the WSDL file in our WAR project -->
    <jaxrpc-mapping-file/>
    <port-component>
      <port-component-name>HrSessionEJBBeanWS</port-component-name><!-- corresponds to the name defintion found in HrSessionEJBBeanWS.wsdl --> 
      <wsdl-service xmlns:ns0="/nl.amis.sdo.jpa.services/">ns0:HrSessionEJBBeanWS</wsdl-service><!-- namespace ns0 corresponds to the targetNamespace defintion found in HrSessionEJBBeanWS.wsdl --> 
      <wsdl-port xmlns:ns1="/nl.amis.sdo.jpa.services/">ns1:HrSessionEJBBeanWSSoapHttpPort</wsdl-port><!-- namespace ns1 corresponds to the targetNamespace defintion found in HrSessionEJBBeanWS.wsdl --> 
      <enable-mtom>false</enable-mtom>
      <service-endpoint-interface>nl.amis.sdo.jpa.services.HrSessionEJB</service-endpoint-interface><!-- corresponds to the Java remote EJB interface -->
      <service-impl-bean>
        <servlet-link>nl.amis.sdo.jpa.services.HrSessionEJBBean</servlet-link><!-- corresponds to the Java EJB implementation name -->
      </service-impl-bean>
    </port-component>
  </webservice-description>
</webservices>

	Mind that the element 'wsdl-file' points to the 'WEB-INF/wsdl'-directory, which doesn't exist yet.  This will be done in the next step.
	
6) First we need to copy the WSDL and XSD-files to the WEB-INF/wsdl directory, to do this we create a directory 'wsdl' under WEB-INF within JDeveloper.  Once the directory create we'll copy the WSDL and XSD-files from the 'src' directory of the ModelSDO project to the 'WEB-INF/wsdl' directory,
make sure the directory structure keeps intact. It should look like the following screenshot.

7) I also copied the SDO xsd specifications (origins from the commonj sdo library) from the generated ADF-BC SDO WAR file located under WEB-INF/wsdl/xml into our project.

Now the project should look like the following screenshot.  I didn't copy the ADF-BC xsd definitions as we don't use ADF-BC in our project.

8) Now the best part is the deployment of our EjbSdoService application and see if it is also exposed as a SOAP WebService so that we can invoke it from SoapUI.

If at runtime you receive the following weird exception (EclipseLink-45010):

Exception Description: A type could not be found for interface class [nl.amis.sdo.jpa.entities.EmployeesSDO]. Please make sure that the type has been defined.  In addition,  the interface classloader should be a member of the helper context classloader hierarchy:  it appears as if this is [false].

Then please go to the following url and do as explained there: https://forums.oracle.com/forums/thread.jspa?threadID=987232
