package com.jaspersoft.tamanoir.dto.query;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
@XmlRootElement(name = "query")
public class UnifiedTableQuery {
    private Integer offset;
    private Integer limit;
    private Select select;
    private Convert convert;
    private Where where;

    public UnifiedTableQuery(){}

    public UnifiedTableQuery(UnifiedTableQuery source){
        final Select sourceSelect = source.getSelect();
        if(sourceSelect != null){
            select = new Select(sourceSelect);
        }
        if(source.getWhere() != null) {
            where = new Where(source.getWhere());
        }
        offset = source.getOffset();
        limit = source.getLimit();
        if(source.getConvert() != null) {
            convert = new Convert(source.getConvert());
        }
    }

    public Convert getConvert() {
        return convert;
    }

    public UnifiedTableQuery setConvert(Convert convert) {
        this.convert = convert;
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public Select getSelect() {
        return select;
    }

    public UnifiedTableQuery setSelect(Select select) {
        this.select = select;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public UnifiedTableQuery setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public UnifiedTableQuery setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnifiedTableQuery)) return false;

        UnifiedTableQuery that = (UnifiedTableQuery) o;

        if (convert != null ? !convert.equals(that.convert) : that.convert != null) return false;
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        if (offset != null ? !offset.equals(that.offset) : that.offset != null) return false;
        if (select != null ? !select.equals(that.select) : that.select != null) return false;
        if (where != null ? !where.equals(that.where) : that.where != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = offset != null ? offset.hashCode() : 0;
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (select != null ? select.hashCode() : 0);
        result = 31 * result + (convert != null ? convert.hashCode() : 0);
        result = 31 * result + (where != null ? where.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UnifiedTableQuery{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", select=" + select +
                ", convert=" + convert +
                ", where=" + where +
                '}';
    }
}
