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
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import org.jaspersoft.tamanoir.domain.Domain;
import org.jaspersoft.tamanoir.domain.DomainsService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@Path("/domains")
public class DomainsRestService {
    @Context
    ServletContext context;
    @Context
    private HttpServletRequest request;

    @POST
    @Consumes("application/json")
    public Response createDomain(Domain domain) throws URISyntaxException {
        return Response.created(new URI(request.getRequestURL().append("/").append(getDomainsService().saveDomain(domain)).toString())).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Domain readDomain(@PathParam("id")Long id){
        return getDomainsService().readDomain(id);
    }

    @GET
    @Produces("application/json")
    public List<Domain> readAllDomains(){
        return getDomainsService().readAllDomains();
    }


    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Domain updateDomain(@PathParam("id")Long id, Domain domain){
        return getDomainsService().updateDomain(domain.setId(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Domain deleteDomain(@PathParam("id")Long id){
        return getDomainsService().deleteDomain(id);
    }

    protected DomainsService getDomainsService(){
        DomainsService service = (DomainsService) context.getAttribute(DomainsService.class.getName());
        if(service == null) {
            throw new ConnectionException(new ErrorDescriptor().setCode("domains.service.not.initialized")
                    .setMessage("Domains service isn't initialized"));
        }
        return service;
    }
}
