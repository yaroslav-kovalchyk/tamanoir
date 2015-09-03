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
import java.sql.SQLException;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class JdbcConnector implements Connector<Connection> {
    private JdbcDataSource jdbcDataSource = new JdbcDataSource();

    @Override
    public Connection openConnection(ConnectionDescriptor connectionDescriptor) {
        final Connection connection;
        try {
            connection = jdbcDataSource.getInstance(connectionDescriptor).getConnection();
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
        return connection;
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch(SQLException e) {
            throw new ConnectionException(e);
        }
    }

    @Override
    public void testConnection(ConnectionDescriptor connectionDescriptor) {
        closeConnection(openConnection(connectionDescriptor));
    }


}
