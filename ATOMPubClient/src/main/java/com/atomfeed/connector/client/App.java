/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package com.atomfeed.connector.client;

import java.util.Date;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;

public class App {

    public static void main(String[] args) throws Exception {

        Abdera abdera = new Abdera();
        AbderaClient abderaClient = new AbderaClient(abdera);
        Factory factory = abdera.getFactory();

        Entry entry = factory.newEntry();
        entry.setId("tag:example.org,2011:foo");
        entry.setTitle("This is the title");
        entry.setUpdated(new Date());
        entry.addAuthor("Chad");
        entry.setContent("Hello World");
        report("The Entry to Post", entry.toString());

        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");

        ClientResponse resp = abderaClient.post("http://localhost:9002/FeedConnector", entry, opts);
        report("HTTP STATUS TEXT", resp.getStatusText());
    }

    private static void report(String title, String message) {
        System.out.println("== " + title + " ==");
        if (message != null)
            System.out.println(message);
        System.out.println();
    }
}