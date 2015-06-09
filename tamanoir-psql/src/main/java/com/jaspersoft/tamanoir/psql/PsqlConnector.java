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

import com.jaspersoft.tamanoir.connection.Connector;
import com.jaspersoft.tamanoir.connection.MetadataBuilder;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.MetadataElementItem;
import com.jaspersoft.tamanoir.dto.MetadataGroupItem;
import com.jaspersoft.tamanoir.dto.MetadataItem;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class PsqlConnector implements Connector<Connection>, MetadataBuilder<Connection>{
    private static final Map<Integer, String> JDBC_TYPES_BY_CODE;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            Field[] fields = Types.class.getFields();
            for (Field f : fields) {
                map.put((Integer)f.get(null), f.getName());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Cannot access java.sql.Types !");
        }
        JDBC_TYPES_BY_CODE = Collections.unmodifiableMap(map);
    }


    @Override
    public Connection openConnection(ConnectionDescriptor descriptor) {
        Properties properties = new Properties();
        Connection connection;
        if(descriptor.getProperties() != null){
            properties.putAll(descriptor.getProperties());
        }
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(descriptor.getUrl(), properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void testConnection(ConnectionDescriptor connectionDescriptor) {
        Connection connection = openConnection(connectionDescriptor);
        closeConnection(connection);
    }

    @Override
    public MetadataItem build(Connection connection, Map<String, String[]> options) {
        final MetadataGroupItem result = new MetadataGroupItem().setName("root").setLabel("root");
        final String[] expands = options.get("expand");
        final Map<String, List<String[]>> expandsMap = new HashMap<String, List<String[]>>();
        if(expands != null){
            for(String expand : expands){
                final String[] tokens = expand.split("\\.");
                List<String[]> tokensList = expandsMap.get(tokens[0]);
                if(tokensList == null){
                    tokensList = new ArrayList<String[]>();
                    expandsMap.put(tokens[0], tokensList);
                }
                if(tokens.length > 1){
                    tokensList.add(Arrays.copyOfRange(tokens, 1, tokens.length));
                }
            }
        }
        try {
            final DatabaseMetaData metaData = connection.getMetaData();
            final ResultSet schemas = metaData.getSchemas();
            while (schemas.next()) {
                String schema = schemas.getString("TABLE_SCHEM");
                final MetadataGroupItem schemaItem = new MetadataGroupItem().setName(schema).setLabel(schema);
                result.addItem(schemaItem);
                if(expandsMap.containsKey(schema)){
                    schemaItem.setItems(expandSchema(schema, expandsMap.get(schema), metaData));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    protected List<MetadataItem> expandSchema(String schema, List<String[]> tokensList, DatabaseMetaData metaData){
        final List<MetadataItem> tableItems = new ArrayList<MetadataItem>();
        final Set<String> tableNamesToExpand = new HashSet<String>();
        if (tokensList != null){
            for(String[] strings : tokensList){
                tableNamesToExpand.add(strings[0]);
            }
        }

        try {
            final ResultSet tables = metaData.getTables(null, schema, null, new String[]{"TABLE", "VIEW", "ALIAS", "SYNONYM"});
            while(tables.next()){
                final String tableName = tables.getString("TABLE_NAME");
                final MetadataGroupItem table = new MetadataGroupItem().setName(tableName);
                tableItems.add(table);
                if(tableNamesToExpand.contains(tableName)){
                    table.setItems(expandTable(schema, tableName, metaData));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableItems;
    }

    protected List<MetadataItem> expandTable(String schema, String table, DatabaseMetaData metaData){
        final ResultSet columns;
        final List<MetadataItem> result = new ArrayList<MetadataItem>();
        try {
            columns = metaData.getColumns(null, schema, table, null);
            while (columns.next()){
                final String columnName = columns.getString("COLUMN_NAME");
                int typeCode = columns.getInt("DATA_TYPE");
                result.add(new MetadataElementItem().setName(columnName).setType(JDBC_TYPES_BY_CODE.get(typeCode)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
