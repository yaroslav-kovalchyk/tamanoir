package com.jaspersoft.tamanoir.dto;

/**
 * Created by serhii.blazhyievskyi on 9/22/2015.
 */
public class Suggestion {
    private Node relationFrom;
    private Node relationTo;

    public Node getRelationFrom() {
        return relationFrom;
    }

    public Suggestion setRelationFrom(Node relationFrom) {
        this.relationFrom = relationFrom;
        return this;
    }

    public Node getRelationTo() {
        return relationTo;
    }

    public Suggestion setRelationTo(Node relationTo) {
        this.relationTo = relationTo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Suggestion that = (Suggestion) o;

        if (!relationFrom.equals(that.relationFrom) && !relationFrom.equals(that.relationTo)) return false;
        return (relationTo.equals(that.relationTo) || relationTo.equals(that.relationFrom));
    }

    @Override
    public int hashCode() {
        int result = relationFrom.hashCode();
        result = 31 * result + relationTo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "relationFrom=" + relationFrom +
                ", relationTo=" + relationTo +
                '}';
    }
}
