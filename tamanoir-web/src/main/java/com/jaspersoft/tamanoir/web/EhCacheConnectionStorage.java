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
package com.jaspersoft.tamanoir.web;

import com.jaspersoft.tamanoir.connection.storage.ConnectionContainer;
import com.jaspersoft.tamanoir.connection.storage.ConnectionsStorage;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.UUID;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class EhCacheConnectionStorage implements ConnectionsStorage {
    public static final String CONNECTIONS_CACHE_NAME = "connections";
    private final Cache cache;


    public EhCacheConnectionStorage(){
        CacheManager cacheManager = CacheManager.getInstance();
        cache = cacheManager.getCache(CONNECTIONS_CACHE_NAME);
    }

    @Override
    public UUID storeConnection(ConnectionContainer connectionContainer) {
        final UUID uuid = UUID.randomUUID();
        cache.put(new Element(uuid, connectionContainer.setUuid(uuid)));
        return uuid;
    }

    @Override
    public ConnectionContainer getConnection(UUID uuid) {
        final Element element = cache.get(uuid);
        return element != null ? (ConnectionContainer) element.getObjectValue() : null;
    }

    public void shutdown(){
        CacheManager.getInstance().shutdown();
    }
}
