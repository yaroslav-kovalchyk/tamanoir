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

import com.jaspersoft.datadiscovery.dto.ResourceSingleElement;
import com.jaspersoft.datadiscovery.dto.ResourceGroupElement;
import com.jaspersoft.datadiscovery.dto.SchemaElement;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import com.jaspersoft.tamanoir.dto.query.MatchingRule;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class UnifiedTableSubSet extends AbstractTableDataSet {
    private final TableDataSet<UnifiedTableQuery> parent;
    private Integer current = -1;
    private Integer pageCurrent = -1;
    private boolean hasNext = true;
    private final Integer limit;
    private final Integer offset;
    private final Collection<String> columns;
    private final List<MatchingRule> matchingRules;
    public UnifiedTableSubSet(TableDataSet<UnifiedTableQuery> parent, UnifiedTableQuery query){
        this.parent = parent;
        // let's use a clone to avoid any modifications by reference
        UnifiedTableQuery queryClone = new UnifiedTableQuery(query);

        this.limit = queryClone.getLimit() != null ? queryClone.getLimit() : Integer.MAX_VALUE;
        this.offset = queryClone.getOffset() != null ? queryClone.getOffset() : 0;
        columns = queryClone.getSelect() != null ? queryClone.getSelect().getColumns() : null;
        matchingRules = queryClone.getWhere() != null ? query.getWhere().getMatchingRules() : null;
    }
    @Override
    public boolean next() {
        hasNext = hasNext && pageCurrent < limit - 1;
        if(hasNext) {
            if (current < offset) {
                while (hasNext && current < offset) {
                    hasNext = parent.next();
                    if (matchRow()) {
                        current++;
                    }
                }
                pageCurrent = 0;
            } else {
                do {
                    hasNext = parent.next();
                } while (hasNext && !matchRow());
                current++;
                pageCurrent++;
            }
        }
        return hasNext;
    }

    protected boolean matchRow(){
        boolean result = true;
        if(matchingRules != null){
            for(MatchingRule rule : matchingRules){
                final String value = parent.getValue(rule.getColumn(), String.class);
                if(!rule.match(value)){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public <T> T getValue(String name, Class<T> valueClass) {
        if(columns != null && !columns.isEmpty() && !columns.contains(name)){
            throw new ConnectionException(new ErrorDescriptor().setCode("no.such.column")
                    .setMessage("No column '" + name + "' in data set"));
        }
        return parent.getValue(name, valueClass);
    }

    @Override
    public Object getValue(String name) {
        return getValue(name, String.class);
    }

    @Override
    public SchemaElement getMetadata() {
        final ResourceGroupElement metadata = new ResourceGroupElement ((ResourceGroupElement) parent.getMetadata());
        final List<ResourceSingleElement> columnsMetadata = (List)metadata.getElements();
        final Iterator<ResourceSingleElement> iterator = columnsMetadata.iterator();
        if(columns != null && !columns.isEmpty()) {
            for (; iterator.hasNext(); ) {
                final ResourceSingleElement column = iterator.next();
                if (!columns.contains(column.getName())) {
                    iterator.remove();
                }
            }
        }
        return metadata;
    }

    public TableDataSet<UnifiedTableQuery> subset(UnifiedTableQuery query) {
        return new UnifiedTableSubSet(this, query);
    }
}
