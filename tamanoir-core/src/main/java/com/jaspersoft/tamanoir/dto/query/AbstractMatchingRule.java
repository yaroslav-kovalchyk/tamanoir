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
package com.jaspersoft.tamanoir.dto.query;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public abstract class AbstractMatchingRule<T extends AbstractMatchingRule<T>> implements MatchingRule {
    private String column;

    public AbstractMatchingRule(){}

    public AbstractMatchingRule(AbstractMatchingRule source){
        column = source.getColumn();
    }

    @Override
    public String getColumn() {
        return column;
    }

    public T setColumn(String column) {
        this.column = column;
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractMatchingRule)) return false;

        AbstractMatchingRule that = (AbstractMatchingRule) o;

        if (column != null ? !column.equals(that.column) : that.column != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return column != null ? column.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AbstractMatchingRule{" +
                "column='" + column + '\'' +
                '}';
    }
}
