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

import com.jaspersoft.tamanoir.ConnectionException;
import com.jaspersoft.tamanoir.UnifiedTableDataSet;
import com.jaspersoft.tamanoir.connection.QueryExecutor;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import com.jaspersoft.tamanoir.dto.MetadataElementItem;
import com.jaspersoft.tamanoir.dto.MetadataGroupItem;
import com.jaspersoft.tamanoir.dto.query.Select;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;
import net.sf.jasperreports.engine.data.JRCsvDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class CsvQueryExecutor implements QueryExecutor<JRCsvDataSource, TableDataSet<UnifiedTableQuery>> {
    private final Pattern QUERY_LIMITED_PATTERN = Pattern.compile("select[\\s]*(.+)[\\s]*limit[\\s]*(\\d+)([\\s]*,[\\s]*(\\d+))*");
    private final Pattern QUERY_UNLIMITED_PATTERN = Pattern.compile("select[\\s]*(.+)"
    );

    @Override
    public Object executeQuery(JRCsvDataSource connection, String query) {
        return prepareDataSet(connection, parseQuery(query)).getData();
    }

    protected UnifiedTableQuery parseQuery(String query){
        final Integer size;
        final Integer offset;
        final String columnsString;
        List<String> columns = new ArrayList<String>();
        final Matcher limitedQueryMatcher = QUERY_LIMITED_PATTERN.matcher(query);
        final Matcher unlimitedQueryMatcher = QUERY_UNLIMITED_PATTERN.matcher(query);
        if (limitedQueryMatcher.matches()) {
            columnsString = limitedQueryMatcher.group(1);
            final String offsetString = limitedQueryMatcher.group(2);
            final String sizeString = limitedQueryMatcher.group(4);
            size = sizeString != null ? Integer.valueOf(sizeString) : Integer.valueOf(offsetString);
            offset = sizeString != null ? Integer.valueOf(offsetString) : 0;
        } else if (unlimitedQueryMatcher.matches()) {
            offset = 0;
            size = Integer.MAX_VALUE;
            columnsString = unlimitedQueryMatcher.group(1);
        } else {
            throw new ConnectionException(new ErrorDescriptor().setCode("invalid.query").setMessage("invalid query"));
        }
        if (columnsString.trim().equals("*")) {
            columns = null;
        } else {
            for (String column : columnsString.split(",")) {
                columns.add(column.trim());
            }
        }
        return new UnifiedTableQuery().setOffset(offset).setLimit(size).setSelect(new Select().setColumns(columns));
    }

    protected TableDataSet<UnifiedTableQuery> prepareDataSet(JRCsvDataSource connection, UnifiedTableQuery query){
        MetadataGroupItem group = new MetadataGroupItem().setName("root");
        for (String columnName : connection.getColumnNames().keySet()) {
            group.addItem(new MetadataElementItem().setName(columnName).setType(String.class.getName()));
        }
        return  new UnifiedTableDataSet(connection, group).subset(query);
    }

    @Override
    public TableDataSet<UnifiedTableQuery> prepareDataSet(JRCsvDataSource connection, String query) {
        return prepareDataSet(connection, parseQuery(query));
    }
}
