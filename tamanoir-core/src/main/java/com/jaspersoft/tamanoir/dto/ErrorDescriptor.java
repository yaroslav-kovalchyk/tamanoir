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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class ErrorDescriptor {
    private String code;
    private String message;
    private List<String> parameters;

    public ErrorDescriptor() {
    }

    public ErrorDescriptor(ErrorDescriptor source) {
        code = source.getCode();
        message = source.getMessage();
        final List<String> sourceParameters = source.getParameters();
        if (sourceParameters != null) {
            parameters = new ArrayList<String>(sourceParameters);
        }
    }

    public String getCode() {
        return code;
    }

    public ErrorDescriptor setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorDescriptor setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public ErrorDescriptor setParameters(List<String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ErrorDescriptor setParameters(String... parameters) {
        return setParameters(parameters != null ? Arrays.asList(parameters) : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDescriptor)) return false;

        ErrorDescriptor that = (ErrorDescriptor) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorDescriptor{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
