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

import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import com.jaspersoft.tamanoir.dto.MetadataItem;
import com.jaspersoft.tamanoir.dto.query.MatchingRule;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;

import java.util.Collection;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class UnifiedTableSubSet implements TableDataSet<UnifiedTableQuery> {
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
        columns = queryClone.getSelect().getColumns();
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
                    current++;
                    pageCurrent++;
                } while (hasNext && !matchRow());
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
    public MetadataItem getMetadata() {
        // to be implemented later
        return null;
    }

    @Override
    public TableDataSet<UnifiedTableQuery> subset(UnifiedTableQuery query) {
        return new UnifiedTableSubSet(this, query);
    }
}
