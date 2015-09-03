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

import com.jaspersoft.datadiscovery.dto.MetadataElementItem;
import com.jaspersoft.datadiscovery.dto.MetadataGroupItem;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public abstract class AbstractTableDataSet implements TableDataSet<UnifiedTableQuery> {
    @Override
    public Object getData() {
        return getTableData();
    }

    @Override
    public Collection<Map<String, ?>> getTableData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        MetadataGroupItem metadata = (MetadataGroupItem) getMetadata();
        final List<MetadataElementItem> items = (List)metadata.getItems();
        while (next()) {
            Map<String, Object> row = new HashMap<String, Object>(items.size());
            for (MetadataElementItem item : items) {
                try {
                    final String name = item.getName();
                    final Object value = getValue(name, Class.forName(item.getType()));
                    row.put(name, value);
                } catch (ClassNotFoundException e) {
                    throw new ConnectionException(e);
                }
            }
            data.add(row);
        }
        return (Collection)data;
    }
}
