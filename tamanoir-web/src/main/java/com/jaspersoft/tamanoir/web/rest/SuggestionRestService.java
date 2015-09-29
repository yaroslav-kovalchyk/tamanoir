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

import com.jaspersoft.tamanoir.ConnectionException;
import com.jaspersoft.tamanoir.ConnectionsService;
import com.jaspersoft.tamanoir.SuggestionsService;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import com.jaspersoft.tamanoir.dto.Suggestion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by serhii.blazhyievskyi on 9/22/2015.
 */
@Api(value = "suggestions")
@Path("/suggestions")
public class SuggestionRestService {
    @Context
    ServletContext context;
    @Context
    private HttpServletRequest request;

    @POST
    @ApiOperation(value = "Return list of suggestions by connection descriptor",
            response = Suggestion.class,
            responseContainer = "Set")
    @Produces("application/json")
    public Response readSuggestion(ConnectionDescriptor connectionDescriptor, @Context final HttpServletRequest request){
        Map<String, String[]> options = new HashMap<String, String[]>();
        options.putAll(request.getParameterMap());
        options.put("recursive", new String[]{"root"});
        return Response.ok(getSuggestionsService().getSuggestion(connectionDescriptor, options)).build();
    }

    protected SuggestionsService getSuggestionsService(){
        SuggestionsService service = (SuggestionsService) context.getAttribute(SuggestionsService.class.getName());
        if(service == null) {
            throw new ConnectionException(new ErrorDescriptor().setCode("Suggestions.service.not.initialized")
                    .setMessage("Suggestions service isn't initialized"));
        }
        return service;
    }

    protected ConnectionsService getConnectionService(){
        final ConnectionsService connectionsService = (ConnectionsService) context.getAttribute(ConnectionsService.class.getName());
        if(connectionsService == null){
            throw new ConnectionException(new ErrorDescriptor().setCode("connections.service.not.initialized")
                    .setMessage("Connections service isn't initialized"));
        }
        return connectionsService;
    }

}
