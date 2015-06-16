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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@XmlRootElement(name = "where")
public class Where {
    private String column;
    private List<MatchingRule> matchingRules;

    public Where (){}

    public Where (Where source){
        column = source.getColumn();
        final List<MatchingRule> sourceMatchingRules = source.getMatchingRules();
        if(sourceMatchingRules != null){
            matchingRules = new ArrayList<MatchingRule>(sourceMatchingRules.size());
            for(MatchingRule rule : sourceMatchingRules){
                if(rule instanceof LikeMatchingRule){
                    matchingRules.add(new LikeMatchingRule((LikeMatchingRule) rule));
                } else if(rule instanceof EqualsMatchingRule){
                    matchingRules.add(new EqualsMatchingRule((EqualsMatchingRule) rule));
                }
            }
        }
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @XmlElementWrapper(name = "matchingRules")
    @XmlElements({
            @XmlElement(name = "equals", type = EqualsMatchingRule.class),
            @XmlElement(name = "like", type = LikeMatchingRule.class)
    })
    public List<MatchingRule> getMatchingRules() {
        return matchingRules;
    }

    public void setMatchingRules(List<MatchingRule> matchingRules) {
        this.matchingRules = matchingRules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Where)) return false;

        Where where = (Where) o;

        if (column != null ? !column.equals(where.column) : where.column != null) return false;
        if (matchingRules != null ? !matchingRules.equals(where.matchingRules) : where.matchingRules != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column != null ? column.hashCode() : 0;
        result = 31 * result + (matchingRules != null ? matchingRules.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Where{" +
                "column='" + column + '\'' +
                ", matchingRules=" + matchingRules +
                '}';
    }
}
