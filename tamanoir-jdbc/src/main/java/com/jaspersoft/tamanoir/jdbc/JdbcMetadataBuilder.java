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
import com.jaspersoft.tamanoir.connection.MetadataBuilder;
import com.jaspersoft.tamanoir.dto.MetadataElementItem;
import com.jaspersoft.tamanoir.dto.MetadataGroupItem;
import com.jaspersoft.tamanoir.dto.MetadataItem;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
import java.util.Set;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class JdbcMetadataBuilder implements MetadataBuilder<Connection> {
    private static final Map<Integer, String> JDBC_TYPES_BY_CODE;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            Field[] fields = Types.class.getFields();
            for (Field f : fields) {
                map.put((Integer)f.get(null), f.getName());
            }
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
        JDBC_TYPES_BY_CODE = Collections.unmodifiableMap(map);
    }

    @Override
    public MetadataItem build(Connection connection, Map<String, String[]> options) {
        final MetadataGroupItem result = new MetadataGroupItem().setName("root");
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
                final MetadataGroupItem schemaItem = new MetadataGroupItem().setName(schema);
                result.addItem(schemaItem);
                if(expandsMap.containsKey(schema)){
                    schemaItem.setItems(expandSchema(schema, expandsMap.get(schema), metaData));
                }
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
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
            throw new ConnectionException(e);
        }
        return tableItems;
    }

    protected List<MetadataItem> expandTable(String schema, String table, DatabaseMetaData metaData){
        final ResultSet columns;
        final List<MetadataItem> result = new ArrayList<MetadataItem>();
        try {
            columns = metaData.getColumns(null, schema, table, null);
            ResultSet primaryKeySet = metaData.getPrimaryKeys(null, schema, table);
            List<String> primaryKeys = new ArrayList<String>();
            while (primaryKeySet.next()){
                primaryKeys.add(primaryKeySet.getString(4));
            }
            final ResultSet foreignKeysSet = metaData.getImportedKeys(null, schema, table);
            Map<String, String> foreignKeyMap = new HashMap<String, String>();
            while (foreignKeysSet.next()){
                String foreignKeyColumnName = foreignKeysSet.getString("FKCOLUMN_NAME");
                String primaryKeyTableName = foreignKeysSet.getString("PKTABLE_NAME");
                String primaryKeyColumnName = foreignKeysSet.getString("PKCOLUMN_NAME");
                String primaryKeySchemaName = foreignKeysSet.getString("PKTABLE_SCHEM");
                foreignKeyMap.put(foreignKeyColumnName, primaryKeySchemaName + "." + primaryKeyTableName + "." + primaryKeyColumnName);
            }
            while (columns.next()){
                final String columnName = columns.getString("COLUMN_NAME");
                int typeCode = columns.getInt("DATA_TYPE");
                final MetadataElementItem columnItem = new MetadataElementItem().setName(columnName).setType(JDBC_TYPES_BY_CODE.get(typeCode));
                if(primaryKeys.contains(columnName)){
                    columnItem.setIsIdentifier(true);
                }
                if(foreignKeyMap.containsKey(columnName)){
                    columnItem.setReferenceTo(foreignKeyMap.get(columnName));
                }
                result.add(columnItem);
            }
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
        return result;
    }
}
