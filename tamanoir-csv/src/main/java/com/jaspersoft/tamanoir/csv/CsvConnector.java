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
package com.jaspersoft.tamanoir.csv;

import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import net.sf.jasperreports.engine.data.JRCsvDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class CsvConnector implements Connector<JRCsvDataSource> {
    private static final String USE_FIRST_ROW_AS_HEADER = "useFirstRowAsHeader";

    @Override
    public JRCsvDataSource openConnection(ConnectionDescriptor descriptor) {
        JRCsvDataSource dataSource;
        try {
            dataSource = new JRCsvDataSource(openStream(descriptor));
            dataSource.setUseFirstRowAsHeader(true);
            dataSource.next();
            final Map<String, Integer> columnNames = dataSource.getColumnNames();
            final String[] columnNamesArray = new String[columnNames.size()];
            dataSource.close();
            dataSource = new JRCsvDataSource(openStream(descriptor));
            if (descriptor.getProperties() != null && descriptor.getProperties().containsKey(USE_FIRST_ROW_AS_HEADER)
                    && "true".equalsIgnoreCase(descriptor.getProperties().get(USE_FIRST_ROW_AS_HEADER))) {
                dataSource.setUseFirstRowAsHeader(true);
            } else {
                for(int i = 0; i < columnNames.size(); i++){
                    columnNamesArray[i] = "COLUMN_" + i;
                }
                dataSource.setColumnNames(columnNamesArray);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dataSource;
    }

    protected InputStream openStream(ConnectionDescriptor connectionDescriptor) throws IOException {
        return new URL(connectionDescriptor.getUrl().substring("csv:".length())).openStream();
    }

    @Override
    public void closeConnection(JRCsvDataSource connection) {
        connection.close();
    }

    @Override
    public void testConnection(ConnectionDescriptor connectionDescriptor) {
        openConnection(connectionDescriptor).close();
    }
}
