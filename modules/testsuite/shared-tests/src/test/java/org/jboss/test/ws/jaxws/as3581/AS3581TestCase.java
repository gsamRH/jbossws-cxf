/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jboss.test.ws.jaxws.as3581;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * [AS7-3581] Tests manual JNDI lookup in @Oneway annotated method.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
@ExtendWith(ArquillianExtension.class)
public class AS3581TestCase extends JBossWSTest
{
   @ArquillianResource
   private URL baseURL;

   @Deployment(testable = false)
   public static WebArchive createDeployments() {
      WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxws-as3581.war");
         archive
               .addManifest()
               .addClass(org.jboss.test.ws.jaxws.as3581.EndpointIface.class)
               .addClass(org.jboss.test.ws.jaxws.as3581.EndpointIface2.class)
               .addClass(org.jboss.test.ws.jaxws.as3581.EndpointImpl.class)
               .addClass(org.jboss.test.ws.jaxws.as3581.EndpointImpl2.class)
               .setWebXML(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/as3581/WEB-INF/web.xml"));
      return archive;
   }

   @Test
   @RunAsClient
   public void testEndpoint() throws Exception
   {
      // test one-way scenario
      final QName serviceName = new QName("org.jboss.test.ws.jaxws.as3581", "SimpleService");
      final URL wsdlURL = new URL(baseURL + "/SimpleService?wsdl");
      final Service service = Service.create(wsdlURL, serviceName);
      final EndpointIface port = service.getPort(EndpointIface.class);
      port.doit();
      // test req-resp scenario
      final QName serviceName2 = new QName("org.jboss.test.ws.jaxws.as3581", "SimpleService2");
      final URL wsdlURL2 = new URL(baseURL + "/SimpleService2?wsdl");
      final Service service2 = Service.create(wsdlURL2, serviceName2);
      final EndpointIface2 port2 = service2.getPort(EndpointIface2.class);
      final String oneWayLookupString = port2.getString();
      assertEquals("Ahoj", oneWayLookupString);
   }

}
