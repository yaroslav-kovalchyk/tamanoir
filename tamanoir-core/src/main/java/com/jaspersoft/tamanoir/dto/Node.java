package com.jaspersoft.tamanoir.dto;

/**
 * Created by serhii.blazhyievskyi on 9/22/2015.
 */
public class Node {
    public static final String DELIMITER = ".";

    private String fullPath;
    private String columnName;
    private boolean identifier = false;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!fullPath.equals(node.fullPath)) return false;
        return columnName.equals(node.columnName);

    }

    @Override
    public int hashCode() {
        int result = fullPath.hashCode();
        result = 31 * result + columnName.hashCode();
        return result;
    }
}
