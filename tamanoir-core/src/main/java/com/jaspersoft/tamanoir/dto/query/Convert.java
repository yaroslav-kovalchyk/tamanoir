package com.jaspersoft.tamanoir.dto.query;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author Yaroslav.Kovalchyk
 */
public class Convert {
    private List<CastConversionRule> rules;

    public Convert(){}

    public Convert(Convert source){
        if(source.getRules() != null) {
            this.rules = new ArrayList<CastConversionRule>(source.getRules());
        }
    }

    public List<CastConversionRule> getRules() {
        return rules;
    }

    public Convert setRules(List<CastConversionRule> rules) {
        this.rules = rules;
        return this;
    }

    public Convert addRule(CastConversionRule rule){
        if(rules == null)rules = new ArrayList<CastConversionRule>();
        rules.add(rule);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Convert)) return false;

        Convert that = (Convert) o;

        if (rules != null ? !rules.equals(that.rules) : that.rules != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return rules != null ? rules.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Convert{" +
                "rules=" + rules +
                '}';
    }
}
