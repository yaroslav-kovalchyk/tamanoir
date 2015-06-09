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
package com.jaspersoft.tamanoir;

import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.connection.MetadataBuilder;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;

import java.sql.Connection;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class ConnectionsManager {
    public void testConnection(ConnectionDescriptor connectionDescriptor){
        getProcessor(connectionDescriptor, Connector.class).testConnection(connectionDescriptor);
    }

    public Object buildConnectionMetadata(ConnectionDescriptor connectionDescriptor, Map<String, String[]> options){
        return getProcessor(connectionDescriptor, MetadataBuilder.class).build(getProcessor(connectionDescriptor, Connector.class).openConnection(connectionDescriptor), options);
    }

    protected Class<?> getConnectionClass(ConnectionDescriptor connectionDescriptor){
        return Connection.class;
    }

    protected <T> T getProcessor(ConnectionDescriptor connectionDescriptor, Class<T> processorClass){
        try {
            // let's hardcode connector class for now. Factory should be here in future
            return (T) Class.forName("com.jaspersoft.tamanoir.psql.PsqlConnector").newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
