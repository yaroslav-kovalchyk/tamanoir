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

import com.jaspersoft.datadiscovery.dto.MetadataGroupItem;
import com.jaspersoft.datadiscovery.dto.MetadataItem;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

import java.util.Collection;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class UnifiedTableDataSet extends AbstractTableDataSet {
    private final JRDataSource jrDataSource;
    private final MetadataGroupItem metadata;
    public UnifiedTableDataSet(JRDataSource jrDataSource, MetadataGroupItem metadata){
        this.jrDataSource = jrDataSource;
        this.metadata = metadata;
    }
    @Override
    public boolean next() {
        try {
            return jrDataSource.next();
        } catch (JRException e) {
           throw new ConnectionException(e);
        }
    }

    @Override
    public <T> T getValue(String name, Class<T> valueClass) {

        JRDesignField field = new JRDesignField();
        field.setName(name);
        if(valueClass != null && Object.class != valueClass){
            field.setValueClass(valueClass);
        }
        try {
            return (T) jrDataSource.getFieldValue(field);
        } catch (JRException e) {
            throw new ConnectionException(e);
        }
    }

    @Override
    public Object getValue(String name) {
        return getValue(name, String.class);
    }

    @Override
    public MetadataItem getMetadata() {
        return metadata;
    }

    @Override
    public Collection<Map<String, ?>> getTableData() {
        Collection<Map<String, ?>> result;
        if(jrDataSource instanceof JRMapCollectionDataSource){
            result = ((JRMapCollectionDataSource) jrDataSource).getData();
        } else {
            result = super.getTableData();
        }
        return result;
    }

    public TableDataSet<UnifiedTableQuery> subset(UnifiedTableQuery query) {
        return new UnifiedTableSubSet(new UnifiedTableDataSet(new JRMapCollectionDataSource(getTableData()), metadata), query);
    }
}
