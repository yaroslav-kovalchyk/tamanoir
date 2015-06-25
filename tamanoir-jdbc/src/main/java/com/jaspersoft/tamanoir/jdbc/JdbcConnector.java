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
package com.jaspersoft.tamanoir.jdbc;

import com.jaspersoft.tamanoir.ConnectionException;
import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class JdbcConnector implements Connector<JdbcConnectionContainer> {
    public static final String DRIVER_CLASS_PROPERTY = "driverClass";


    @Override
    public JdbcConnectionContainer openConnection(ConnectionDescriptor descriptor) {
        final Properties properties = new Properties();
        final Connection connection;
        final Map<String, String> descriptorProperties = descriptor.getProperties();
        String driverClassName = descriptorProperties != null && descriptorProperties.get(DRIVER_CLASS_PROPERTY) != null
                ? descriptorProperties.get(DRIVER_CLASS_PROPERTY) : "org.postgresql.Driver";
        if (descriptorProperties != null) {
            properties.putAll(descriptorProperties);
        }
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(descriptor.getUrl(), properties);
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
        return new JdbcConnectionContainer(connection);
    }

    @Override
    public void closeConnection(JdbcConnectionContainer connection) {
        connection.close();
    }

    @Override
    public void testConnection(ConnectionDescriptor connectionDescriptor) {
        closeConnection(openConnection(connectionDescriptor));
    }


}
