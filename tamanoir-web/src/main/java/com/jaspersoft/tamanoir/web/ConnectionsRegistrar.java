/*
* Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights  reserved.
* http://www.jaspersoft.com.
*
* Unless you have purchased  a commercial license agreement from Jaspersoft,
* the following license terms  apply:
*
* This program is free software: you can redistribute it and/or  modify
* it under the terms of the GNU Affero General Public License  as
* published by the Free Software Foundation, either version 3 of  the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero  General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public  License
* along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
*/
package com.jaspersoft.tamanoir.web;

import com.jaspersoft.tamanoir.ConnectionsManager;
import com.jaspersoft.tamanoir.ConnectionsService;
import com.jaspersoft.tamanoir.csv.CsvConnectionProcessorFactory;
import com.jaspersoft.tamanoir.jdbc.JdbcConnectionProcessorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jaspersoft.tamanoir.domain.DomainsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class ConnectionsRegistrar implements ServletContextListener {
    private final static Log log = LogFactory.getLog(ConnectionsRegistrar.class);

    private EhCacheConnectionStorage storage;
    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionsManager.registerConnection("csv", new CsvConnectionProcessorFactory());
        ConnectionsManager.registerConnection("jdbc", new JdbcConnectionProcessorFactory());
        try {
            storage = new EhCacheConnectionStorage();
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            log.error("Unexpected error occur", e);
        }
        sce.getServletContext().setAttribute(ConnectionsService.class.getName(), new ConnectionsService(storage));
        sce.getServletContext().setAttribute(DomainsService.class.getName(), new DomainsService(sessionFactory));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        storage.shutdown();
        sessionFactory.close();
    }
}
