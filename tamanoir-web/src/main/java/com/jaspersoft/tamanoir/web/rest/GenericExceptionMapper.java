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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    private final static Log log = LogFactory.getLog(GenericExceptionMapper.class);
    @Override
    public Response toResponse(Exception e) {
        if(e instanceof WebApplicationException){
            return ((WebApplicationException) e).getResponse();
        }
        log.error("Unexpected error occur", e);
        return Response.serverError().entity(new ConnectionException(e).getErrorDescriptor()).build();
    }
}
