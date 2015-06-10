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
import com.jaspersoft.tamanoir.connection.QueryExecutor;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.QueryConnectionDescriptor;

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

    public Object buildConnectionMetadata(final ConnectionDescriptor connectionDescriptor, final Map<String, String[]> options){
        return operateConnection(connectionDescriptor, new ConnectionOperator<Object>() {
            @Override
            public Object operate(Object connection) {
                return getProcessor(connectionDescriptor, MetadataBuilder.class).build(connection, options);
            }
        });
    }

    public Object executeQuery(final QueryConnectionDescriptor queryConnectionDescriptor){

        return operateConnection(queryConnectionDescriptor, new ConnectionOperator<Object>() {
            @Override
            public Object operate(Object connection) {
                return getProcessor(queryConnectionDescriptor, QueryExecutor.class).executeQuery(connection, queryConnectionDescriptor.getNativeQuery());
            }
        });
    }

    protected <T> T getProcessor(ConnectionDescriptor connectionDescriptor, Class<T> processorClass){
        T result = null;
        try {
            // let's hardcode classes for now. Factory should be here in future
            if(processorClass == Connector.class) {
                result = (T) Class.forName("com.jaspersoft.tamanoir.psql.PsqlConnector").newInstance();
            } else if(processorClass == MetadataBuilder.class){
                result = (T) Class.forName("com.jaspersoft.tamanoir.psql.PsqlMetadataBuilder").newInstance();
            } else if(processorClass == QueryExecutor.class){
                result = (T) Class.forName("com.jaspersoft.tamanoir.psql.PsqlQueryExecutor").newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }



    protected <R> R operateConnection(ConnectionDescriptor connectionDescriptor, ConnectionOperator<R> operator){
        final Connector connector = getProcessor(connectionDescriptor, Connector.class);
        R result;
        Object connection = null;
        try {
            connection = connector.openConnection(connectionDescriptor);
            result = (R) ((ConnectionOperator)operator).operate(connection);
        }finally {
            connector.closeConnection(connection);
        }
        return result;
    }

    private interface ConnectionOperator<R>{
        R operate(Object connection);
    }

}
