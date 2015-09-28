package com.jaspersoft.tamanoir.dto;

/**
 * Created by serhii.blazhyievskyi on 9/22/2015.
 */
public class Suggestion implements Comparable {
    private Node relationFrom;
    private Node relationTo;

    public Node getRelationFrom() {
        return relationFrom;
    }

    public void setRelationFrom(Node relationFrom) {
        this.relationFrom = relationFrom;
    }

    public Node getRelationTo() {
        return relationTo;
    }

    public void setRelationTo(Node relationTo) {
        this.relationTo = relationTo;
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

    public int compareTo(Object o) {
        Suggestion that = (Suggestion) o;
        if(this.equals(that)) {
            return 0;
        }
        if (hashCode() < that.hashCode()) {
            return 1;
        } else {
            return -1;
        }
    }
}
