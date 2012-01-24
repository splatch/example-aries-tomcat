/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.code_house.workshop.aries.war;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class ProvisionActivator implements BundleActivator {

    private final ServletContext servletContext;

    private static transient Logger logger = Logger.getLogger(ProvisionActivator.class.getName());

    public ProvisionActivator(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void start(BundleContext context) throws Exception {
        servletContext.setAttribute(BundleContext.class.getName(), context);

        ArrayList<Bundle> installed = new ArrayList<Bundle>();
        for (URL url : findBundles()) {
            Bundle bundle = context.installBundle(url.getFile(), url.openStream());
            installed.add(bundle);
        }

        for (Bundle bundle : installed) {
            try {
                bundle.start();
            } catch (Exception e) {
                logger.warning("Unable to start bundle " + bundle.getSymbolicName() + ". " + e);
            }
        }
    }

    public void stop(BundleContext context) throws Exception {
    }

    private List<URL> findBundles() throws Exception {
        List<URL> list = new ArrayList<URL>();

        Set paths = this.servletContext.getResourcePaths("/WEB-INF/bundles/");
        logger.info("List of entries in /WEB-INF/bundles/ " + paths);
        for (Object o : paths) {
            String name = (String)o;
            if (name.endsWith(".jar")) {
                URL url = this.servletContext.getResource(name);
                if (url != null) {
                    list.add(url);
                }
            }
        }

        return list;
    }
}