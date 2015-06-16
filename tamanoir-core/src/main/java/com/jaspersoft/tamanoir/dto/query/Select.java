package com.jaspersoft.tamanoir.dto.query;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@XmlRootElement(name = "select")
public class Select {
    private List<String> columns;

    public Select(){}
    public Select(Select source){
        final List<String> sourceColumns = source.getColumns();
        if(sourceColumns != null){
            columns = new ArrayList<String>(sourceColumns);
        }
    }

    public List<String> getColumns() {
        return columns;
    }

    public Select setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public Select addColumn(String... columns){
        if(columns != null && columns.length > 0) {
            if (this.columns == null) {
                this.columns = new ArrayList<String>();
            }
            Collections.addAll(this.columns, columns);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Select)) return false;

        Select select = (Select) o;

        if (columns != null ? !columns.equals(select.columns) : select.columns != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return columns != null ? columns.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Select{" +
                "columns=" + columns +
                '}';
    }
}
