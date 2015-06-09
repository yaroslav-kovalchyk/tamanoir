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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@XmlRootElement(name = "connection")
public class ConnectionDescriptor {
    private String url;
    private Map<String, String> properties;

    public ConnectionDescriptor(){}
    public ConnectionDescriptor(ConnectionDescriptor source){
        url = source.getUrl();
        if(source.getProperties() != null){
            properties = new HashMap<String, String>(source.getProperties());
        }
    }
    public Map<String, String> getProperties() {
        return properties;
    }

    public ConnectionDescriptor setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ConnectionDescriptor setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionDescriptor)) return false;

        ConnectionDescriptor that = (ConnectionDescriptor) o;

        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConnectionDescriptor{" +
                "url='" + url + '\'' +
                ", properties=" + properties +
                '}';
    }
}
