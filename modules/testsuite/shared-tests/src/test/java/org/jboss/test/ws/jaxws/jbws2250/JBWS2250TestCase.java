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
package org.jboss.test.ws.jaxws.jbws2250;

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
 * [JBWS-2250] Test Case.
 * 
 * The user of JAXBElement causes a NullPointerException on deployment
 * where JBossWS generates a new WSDL.
 * 
 * @author darran.lofthouse@jboss.com
 * @since 7th July 2008
 */
@ExtendWith(ArquillianExtension.class)
public class JBWS2250TestCase extends JBossWSTest
{
   @ArquillianResource
   private URL baseURL;

   @Deployment(testable = false)
   public static WebArchive createDeployments() {
      WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxws-jbws2250.war");
         archive
               .addManifest()
               .addClass(org.jboss.test.ws.jaxws.jbws2250.Endpoint.class)
               .addClass(org.jboss.test.ws.jaxws.jbws2250.EndpointImpl.class)
               .addClass(org.jboss.test.ws.jaxws.jbws2250.Id.class)
               .addClass(org.jboss.test.ws.jaxws.jbws2250.Message.class)
               .addClass(org.jboss.test.ws.jaxws.jbws2250.ObjectFactory.class)
               .addAsWebInfResource(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/jbws2250/WEB-INF/jboss-web.xml"), "jboss-web.xml")
               .setWebXML(new File(JBossWSTestHelper.getTestResourcesDir() + "/jaxws/jbws2250/WEB-INF/web.xml"));
      return archive;
   }

   @Test
   @RunAsClient
   public void testPortAccess() throws Exception
   {
      URL wsdlURL = new URL(baseURL + "?wsdl");
      QName serviceName = new QName("http://ws.jboss.org/jbws2250", "EndpointService");
      Service service = Service.create(wsdlURL, serviceName);
      Endpoint port = service.getPort(Endpoint.class);

      ObjectFactory of = new ObjectFactory();
      Id id = new Id();
      id.setId("003");

      Message message = new Message();
      message.setMessage("Hello");
      message.setMyId(of.createLayoutPerformanceId(id));

      Message retMessage = port.echo(message);
      assertEquals(message.getMessage(), retMessage.getMessage());
      assertEquals(id.getId(), retMessage.getMyId().getValue().getId());
   }

}
