/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atomfeed.connector.server;

import org.apache.abdera.protocol.server.Provider;
import org.apache.abdera.protocol.server.impl.DefaultProvider;
import org.apache.abdera.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera.protocol.server.servlet.AbderaServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class App {

    public static void main(String... args) throws Exception {
        int port = 9002;
        try {
            port = args.length > 0 ? Integer.parseInt(args[0]) : 9002;
        } catch (Exception e) {
        }
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        ServletHolder servletHolder = new ServletHolder(new EmployeeProviderServlet());
        context.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }

    public static final class EmployeeProviderServlet extends AbderaServlet {
        @Override
        protected Provider createProvider() {
            EmployeeCollectionAdapter ca = new EmployeeCollectionAdapter();
            ca.setHref("FeedConnector");

            SimpleWorkspaceInfo wi = new SimpleWorkspaceInfo();
            wi.setTitle("Feed Update");
            wi.addCollection(ca);

            DefaultProvider provider = new DefaultProvider("/");
            provider.addWorkspace(wi);

            provider.init(getAbdera(), null);
            return provider;
        }

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            super.service(request, response);
        }

    }
}
