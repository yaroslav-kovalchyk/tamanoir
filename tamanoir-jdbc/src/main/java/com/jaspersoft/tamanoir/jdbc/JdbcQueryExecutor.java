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
import com.jaspersoft.tamanoir.UnifiedTableDataSet;
import com.jaspersoft.tamanoir.connection.QueryExecutor;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.MetadataGroupItem;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

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
public class JdbcQueryExecutor implements QueryExecutor<Connection, TableDataSet> {
    @Override
    public Object executeQuery(Connection connection, String query) {
        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {

            getResultSet(connection, query, new ResultSetCallback() {
                @Override
                public void resultSet(ResultSet resultSet) throws Exception {
                    final ArrayList<String> columnNames = new ArrayList<String>();
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                        columnNames.add(metaData.getColumnName(i));
                    }
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<String, Object>();
                        for (String columnName : columnNames) {
                            row.put(columnName, resultSet.getString(columnName));
                        }
                        result.add(row);
                    }
                }
            });


        } catch (Exception e ) {
            throw new ConnectionException(e);
        }
        return result;
    }

    protected ResultSet getResultSet(Connection connection, String query, ResultSetCallback callback){
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            if(callback != null){
                callback.resultSet(resultSet);
            }
        } catch (Exception e ) {
            throw new ConnectionException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new ConnectionException(e);
                }
            }
        }
        return resultSet;

    }

    @Override
    public TableDataSet prepareDataSet(Connection connection, String query) {
        final JRResultSetDataSource jrDataSource = new JRResultSetDataSource(getResultSet(connection, query, null));
        final TableDataSet<UnifiedTableQuery> originalDataSet = new UnifiedTableDataSet(jrDataSource,
                (MetadataGroupItem) new JdbcMetadataBuilder().build(connection, null));
        return new UnifiedTableDataSet(new JRMapCollectionDataSource(originalDataSet.getTableData()),
                (MetadataGroupItem) originalDataSet.getMetadata());
    }

    private interface ResultSetCallback{
        void resultSet(ResultSet resultSet) throws Exception;
    }
}
