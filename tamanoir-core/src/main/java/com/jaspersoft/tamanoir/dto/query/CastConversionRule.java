package com.jaspersoft.tamanoir.dto.query;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class CastConversionRule {
    private String column;
    private String type;

    public CastConversionRule(){
    }

    public CastConversionRule(CastConversionRule source){
        this.column = source.getColumn();
        this.type = source.getType();
    }

    public String getColumn() {
        return column;
    }

    public CastConversionRule setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getType() {
        return type;
    }

    public CastConversionRule setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CastConversionRule)) return false;

        CastConversionRule that = (CastConversionRule) o;

        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column != null ? column.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CastConversionRule{" +
                "column='" + column + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
