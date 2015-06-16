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
package com.jaspersoft.tamanoir.web.rest;

import com.jaspersoft.tamanoir.ConnectionsManager;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.QueryConnectionDescriptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@Path("/connections")
public class ConnectionsRestService {
    @Context
    ServletContext context;

    @POST
    @Consumes("application/json")
    public Response createConnection(ConnectionDescriptor connectionDescriptor, @HeaderParam("Accept") String accept, @Context final HttpServletRequest request){
        ConnectionsManager connectionsManager = new ConnectionsManager();
        final Response response;
        if(accept != null && accept.toLowerCase().contains("metadata")){
            response = Response.ok(connectionsManager.buildConnectionMetadata(connectionDescriptor, request.getParameterMap())).build();

        } else {
            connectionsManager.testConnection(connectionDescriptor);
            response = Response.ok(connectionDescriptor).build();
        }
        return response;
    }

    @POST
    @Consumes("application/queryconnection+json")
    public Response executeQuery(QueryConnectionDescriptor queryConnectionDescriptor){
        return Response.ok(new ConnectionsManager().executeQuery(queryConnectionDescriptor)).build();
    }

}
