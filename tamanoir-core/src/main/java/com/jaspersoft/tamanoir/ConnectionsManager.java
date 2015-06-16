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

import com.jaspersoft.tamanoir.connection.ConnectionProcessorFactory;
import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.connection.MetadataBuilder;
import com.jaspersoft.tamanoir.connection.QueryExecutor;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import com.jaspersoft.tamanoir.dto.QueryConnectionDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class ConnectionsManager {
    private static Map<String, ConnectionProcessorFactory> factories = new HashMap<String, ConnectionProcessorFactory>();

    public static void registerConnection(String connectionType, ConnectionProcessorFactory factory) {
        factories.put(connectionType, factory);
    }

    public void testConnection(ConnectionDescriptor connectionDescriptor) {
        getProcessor(connectionDescriptor, Connector.class).testConnection(connectionDescriptor);
    }

    public Object buildConnectionMetadata(final ConnectionDescriptor connectionDescriptor, final Map<String, String[]> options) {
        return operateConnection(connectionDescriptor, new ConnectionOperator<Object>() {
            @Override
            public Object operate(Object connection) {
                return getProcessor(connectionDescriptor, MetadataBuilder.class).build(connection, options);
            }
        });
    }

    public Object executeQuery(final QueryConnectionDescriptor queryConnectionDescriptor) {

        return operateConnection(queryConnectionDescriptor, new ConnectionOperator<Object>() {
            @Override
            public Object operate(Object connection) {
                return getProcessor(queryConnectionDescriptor, QueryExecutor.class).executeQuery(connection, queryConnectionDescriptor.getNativeQuery());
            }
        });
    }

    protected <T> T getProcessor(ConnectionDescriptor connectionDescriptor, Class<T> processorClass) {
        final ConnectionProcessorFactory connectionProcessorFactory = factories.get(connectionDescriptor.getType());
        if (connectionProcessorFactory == null) {
            throw new ConnectionException(new ErrorDescriptor().setCode("unsupported.processor")
                    .setMessage("Processor type '" + processorClass.getName() + "' is not supported")
                    .setParameters(processorClass.getName()));
        }
        return connectionProcessorFactory.getProcessor(processorClass);
    }


    protected <R> R operateConnection(ConnectionDescriptor connectionDescriptor, ConnectionOperator<R> operator) {
        final Connector connector = getProcessor(connectionDescriptor, Connector.class);
        R result;
        Object connection = null;
        try {
            connection = connector.openConnection(connectionDescriptor);
            result = (R) ((ConnectionOperator) operator).operate(connection);
        } finally {
            if(connection != null) {
                connector.closeConnection(connection);
            }
        }
        return result;
    }

    private interface ConnectionOperator<R> {
        R operate(Object connection);
    }

}
