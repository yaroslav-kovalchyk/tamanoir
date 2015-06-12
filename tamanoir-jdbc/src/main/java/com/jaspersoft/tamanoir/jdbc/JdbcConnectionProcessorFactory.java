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

import com.jaspersoft.tamanoir.connection.ConnectionProcessorFactory;
import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.connection.MetadataBuilder;
import com.jaspersoft.tamanoir.connection.QueryExecutor;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class JdbcConnectionProcessorFactory implements ConnectionProcessorFactory {
    @Override
    public <T> T getProcessor(Class<T> processorClass) {
        T processor =  null;
        if(Connector.class == processorClass){
            processor = (T) new JdbcConnector();
        } else if (MetadataBuilder.class == processorClass){
            processor = (T) new JdbcMetadataBuilder();
        }else if (QueryExecutor.class == processorClass){
            processor = (T) new JdbcQueryExecutor();
        }
        return processor;
    }
}
