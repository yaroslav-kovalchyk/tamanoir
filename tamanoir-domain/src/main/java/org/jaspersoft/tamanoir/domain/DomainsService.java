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

import com.jaspersoft.tamanoir.ConnectionException;
import com.jaspersoft.tamanoir.dto.ErrorDescriptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class DomainsService {
    private final SessionFactory sessionFactory;

    public DomainsService (SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Long saveDomain(final Domain domain){
        validate(domain);
        return doInTransaction(new SessionCallback<Long>() {
            @Override
            public Long session(Session session) {
                return (Long) session.save(domain);
            }
        });
    }
    public Domain readDomain(final Long id){
        return doInTransaction(new SessionCallback<Domain>() {
            @Override
            public Domain session(Session session) {
                return (Domain) session.get(Domain.class, id);
            }
        });
    }

    public Domain updateDomain(final Domain domain){
        validate(domain);
        return doInTransaction(new SessionCallback<Domain>() {
            @Override
            public Domain session(Session session) {
                session.update(domain);
                return domain;
            }
        });
    }

    public Domain deleteDomain(final Long id){
        return doInTransaction(new SessionCallback<Domain>() {
            @Override
            public Domain session(Session session) {
                final Domain domain = (Domain) session.get(Domain.class, id);
                if(domain != null) {
                    session.delete(domain);
                }
                return domain;
            }
        });
    }

    public Set<Domain> readAllDomains(){
        return doInTransaction(new SessionCallback<Set<Domain>>() {
            @Override
            public Set<Domain> session(Session session) {
                return new HashSet<Domain>(session.createCriteria(Domain.class).list());
            }
        });
    }

    protected <T> T doInTransaction(SessionCallback<T> callback){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        T result = callback.session(session);
        session.getTransaction().commit();
        return result;
    }

    private interface SessionCallback<T>{
        T session(Session session);
    }

    protected void validate(Domain domain){
        if(domain.getName() == null){
            throw new ConnectionException(new ErrorDescriptor().setCode("domain.name.required").setMessage("Domain name is required"));
        }
        if(domain.getType() == null){
            throw new ConnectionException(new ErrorDescriptor().setCode("domain.type.required").setMessage("Domain type is required"));
        }
        if(domain.getUrl() == null){
            throw new ConnectionException(new ErrorDescriptor().setCode("domain.url.required").setMessage("Domain URL is required"));
        }
    }


}
