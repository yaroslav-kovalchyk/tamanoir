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
package org.jaspersoft.tamanoir.domain;

import com.jaspersoft.tamanoir.dto.QueryConnectionDescriptor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@Entity
public class Domain extends QueryConnectionDescriptor<Domain> {
    private String name;
    private Long id;

    public Domain(){super();}
    public Domain(Domain source){
        super(source);
        name = source.getName();
        id = source.getId();
    }
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="connection_properties", joinColumns=@JoinColumn(name="connection_id"))
    @Override
    public Map<String, String> getProperties() {
        return super.getProperties();
    }


    @Column
    @Override
    public String getNativeQuery() {
        return super.getNativeQuery();
    }

    @Column
    @Override
    public String getType() {
        return super.getType();
    }

    @Column
    @Override
    public String getUrl() {
        return super.getUrl();
    }

    public Domain setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Domain setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Domain)) return false;
        if (!super.equals(o)) return false;

        Domain domain = (Domain) o;

        if (id != null ? !id.equals(domain.id) : domain.id != null) return false;
        if (name != null ? !name.equals(domain.name) : domain.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                "} " + super.toString();
    }
}
