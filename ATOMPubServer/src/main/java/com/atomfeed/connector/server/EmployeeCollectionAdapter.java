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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;


public class EmployeeCollectionAdapter extends AbstractEntityCollectionAdapter<Employee> {
    private static final String ID_PREFIX = "tag:wso2.com,2015,Atom:entry:";

    private AtomicInteger nextId = new AtomicInteger(1000);
    private Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
    private Factory factory = new Abdera().getFactory();

    @Override
    public String getId(RequestContext request) {
        return "tag:wso2.com,2015,Atom:entry:";
    }

    public String getTitle(RequestContext request) {
        return "Atom Connector feeds";
    }

    @Override
    public String getAuthor(RequestContext request) {
        return "WSO2 Inc";
    }

    @Override
    public Iterable<Employee> getEntries(RequestContext request) {
        return employees.values();
    }

    @Override
    public Employee getEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        return employees.get(id);
    }

    private Integer getIdFromResourceName(String resourceName) throws ResponseContextException {
        int idx = resourceName.indexOf("-");
        if (idx == -1) {
            throw new ResponseContextException(404);
        }
        return new Integer(resourceName.substring(0, idx));
    }

    @Override
    public String getName(Employee entry) {
        return entry.getId() + "-" + entry.gettitle().replaceAll(" ", "_");
    }

    @Override
    public String getId(Employee entry) {
        return ID_PREFIX + entry.getId();
    }

    @Override
    public String getTitle(Employee entry) {
        return entry.gettitle();
    }

    @Override
    public Date getUpdated(Employee entry) {
        return entry.getUpdated();
    }


    @Override
    public List<Person> getAuthors(Employee entry, RequestContext request) throws ResponseContextException {
        Person author = request.getAbdera().getFactory().newAuthor();
        author.setName("WOS2 Inc");
        return Arrays.asList(author);
    }

    @Override
    public Object getContent(Employee entry, RequestContext request) {
        Content content = factory.newContent(Content.Type.TEXT);
        content.setText(entry.getContent());
        return content;
    }

    @Override
    public Employee postEntry(String title,
                              IRI id,
                              String summary,
                              Date updated,
                              List<Person> authors,
                              Content content,
                              RequestContext request) throws ResponseContextException {
        Employee employee = new Employee();
        employee.setContent(content.getText().trim());
        employee.setId(nextId.getAndIncrement());
        employee.settitle(title);
        employee.setUpdated(updated);
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public void putEntry(Employee employee,
                         String title,
                         Date updated,
                         List<Person> authors,
                         String summary,
                         Content content,
                         RequestContext request) throws ResponseContextException {
        employee.setContent(content.getText().trim());
        employee.settitle(title);
        employee.setUpdated(updated);
    }

    @Override
    public void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        employees.remove(id);
    }
}
