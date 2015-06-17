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

import com.jaspersoft.tamanoir.connection.DataSet;
import com.jaspersoft.tamanoir.connection.TableDataSet;
import com.jaspersoft.tamanoir.connection.storage.ConnectionContainer;
import com.jaspersoft.tamanoir.connection.storage.ConnectionsStorage;
import com.jaspersoft.tamanoir.dto.QueryConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.query.UnifiedTableQuery;

import java.util.UUID;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class ConnectionsService {
    private final ConnectionsStorage storage;
    private final ConnectionsManager connectionsManager;
    public ConnectionsService(ConnectionsStorage storage){
        this.storage = storage;
        connectionsManager = new ConnectionsManager();
    }

    public UUID saveConnectionDescriptor(QueryConnectionDescriptor connectionDescriptor){
        connectionsManager.testConnection(connectionDescriptor);
        return storage.storeConnection(new ConnectionContainer().setConnectionDescriptor(connectionDescriptor));
    }

    public <T extends DataSet> T getDataSet(UUID connectionUuid){
        final ConnectionContainer connectionContainer = storage.getConnection(connectionUuid);
        if(connectionContainer.getDataSet() == null){
            synchronized (connectionContainer){
                if(connectionContainer.getDataSet() == null){
                    final T dataSet = connectionsManager.prepareDataSet(connectionContainer.getConnectionDescriptor());
                    connectionContainer.setDataSet(dataSet);
                }
            }
        }
        return (T) connectionContainer.getDataSet();
    }

    public Object executeUnifiedQuery(UUID connectionUuid, UnifiedTableQuery query){
        final UnifiedTableDataSet dataSet = storage.getConnection(connectionUuid).getDataSet();
        final TableDataSet<UnifiedTableQuery> subset = dataSet.subset(query);
        return subset.getData();
    }

}
