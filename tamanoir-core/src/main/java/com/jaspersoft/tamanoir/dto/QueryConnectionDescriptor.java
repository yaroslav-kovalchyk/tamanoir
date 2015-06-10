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
package com.jaspersoft.tamanoir.dto;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class QueryConnectionDescriptor extends ConnectionDescriptor {
    private String nativeQuery;

    public QueryConnectionDescriptor(){super();}
    public QueryConnectionDescriptor(QueryConnectionDescriptor source){
        super(source);
        nativeQuery = source.getNativeQuery();
    }

    public String getNativeQuery() {
        return nativeQuery;
    }

    public void setNativeQuery(String nativeQuery) {
        this.nativeQuery = nativeQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueryConnectionDescriptor)) return false;
        if (!super.equals(o)) return false;

        QueryConnectionDescriptor that = (QueryConnectionDescriptor) o;

        if (nativeQuery != null ? !nativeQuery.equals(that.nativeQuery) : that.nativeQuery != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (nativeQuery != null ? nativeQuery.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QueryConnectionDescriptor{" +
                "nativeQuery='" + nativeQuery + '\'' +
                "} " + super.toString();
    }
}
