/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.ws.jaxws.samples.wsse.policy.trust.bearer;

import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.jboss.test.ws.jaxws.samples.wsse.policy.trust.service.ServiceIface;

import javax.jws.WebService;

@WebService
(
   portName = "BearerServicePort",
   serviceName = "BearerService",
   wsdlLocation = "WEB-INF/wsdl/BearerService.wsdl",
   targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/bearerwssecuritypolicy",
   endpointInterface = "org.jboss.test.ws.jaxws.samples.wsse.policy.trust.bearer.BearerIface"
)
@EndpointProperties(value = {
      @EndpointProperty(key = "ws-security.signature.username", value = "myclientkey" ),  // "myservicekey"
      @EndpointProperty(key = "ws-security.signature.properties", value = "xclientKeystore.properties"), // rls test serviceKeystore.properties
      @EndpointProperty(key = "ws-security.encryption.properties", value = "xclientKeystore.properties"), // rls test serviceKeystore.properties
      @EndpointProperty(key = "ws-security.callback-handler", value = "org.jboss.test.ws.jaxws.samples.wsse.policy.trust.bearer.BearerCallbackHandler")
})
public class BearerImpl implements BearerIface
{
   public String sayHello()
   {
      return "Bearer WS-Trust Hello World!";
   }
}
