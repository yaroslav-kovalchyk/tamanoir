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
package com.jaspersoft.tamanoir.psql;

import com.jaspersoft.tamanoir.connection.QueryExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class PsqlQueryExecutor implements QueryExecutor<Connection> {
    @Override
    public Object executeQuery(Connection connection, String query) {
        Statement stmt = null;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            final ResultSetMetaData metaData = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<String>();
            for(int i = 1; i < metaData.getColumnCount(); i++){
                columnNames.add(metaData.getColumnName(i));
            }
            while (rs.next()) {
                Map<String, Object> row = new HashMap<String, Object>();
                for(String columnName : columnNames){
                    row.put(columnName, rs.getString(columnName));
                }
                result.add(row);
            }
        } catch (Exception e ) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}
