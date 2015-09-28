package com.jaspersoft.tamanoir;

import com.jaspersoft.datadiscovery.dto.ResourceGroupElement;
import com.jaspersoft.datadiscovery.dto.ResourceMetadataSingleElement;
import com.jaspersoft.datadiscovery.dto.ResourceSingleElement;
import com.jaspersoft.datadiscovery.dto.SchemaElement;
import com.jaspersoft.datadiscovery.exception.DataDiscoveryException;
import com.jaspersoft.tamanoir.dto.ConnectionDescriptor;
import com.jaspersoft.tamanoir.dto.Node;
import com.jaspersoft.tamanoir.dto.Suggestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by serhii.blazhyievskyi on 9/22/2015.
 */
public class SuggestionsService {

    public Set<Suggestion> getSuggestion(ConnectionDescriptor connectionDescriptor, Map<String, String[]> options) {
        ConnectionsManager connectionsManager = new ConnectionsManager();
        final String[] suggestionOptions = options != null ? options.get("suggest") : null;
        SchemaElement metadata = (SchemaElement)connectionsManager.buildConnectionMetadata(connectionDescriptor, options);
        boolean identifiersOnly = false;
        if(suggestionOptions != null && suggestionOptions.length > 0) {
            for(String option : suggestionOptions) {
                if("identifier".equals(option)) {
                    identifiersOnly = true;
                }
            }
        }
        Set<Suggestion> result = new TreeSet<Suggestion>();
        if(metadata != null) {
            Map<String, List<Node>> allColumns = new HashMap<String, List<Node>>();
            fillColumnsMap((ResourceGroupElement) metadata, allColumns);

            for(String type : allColumns.keySet()) {
                List<Node> listA = allColumns.get(type);
                if (listA.size() > 1) {
                    List<Node> listB = allColumns.get(type);
                    for(Node nodeA : listA) {
                        for(Node nodeB : listB) {
                            if(!nodeA.getFullPath().equals(nodeB.getFullPath())) {
                                if (!identifiersOnly || (identifiersOnly && nodeB.isIdentifier())) {
                                    addSuggestion(result, nodeA, nodeB);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void addSuggestion(Set<Suggestion> result, Node nodeA, Node nodeB) {
        if(!nodeA.equals(nodeB)) {
            if(hasRelation(nodeA, nodeB)) {
                Suggestion newSuggestion = new Suggestion();
                newSuggestion.setRelationFrom(nodeA);
                newSuggestion.setRelationTo(nodeB);
                result.add(newSuggestion);
            }
        }
    }

    private boolean hasRelation(Node nodeA, Node nodeB) {
        String nameA = nodeA.getColumnName().toLowerCase();
        if(nameA.endsWith("id")) {
            nameA.substring(0,nameA.length() - 1);
        }
        if(nameA.indexOf("description") > -1) {
            nameA.replace("description","desc");
        }
        String nameB = nodeB.getFullPath().replaceAll(Node.DELIMITER, "") + nodeB.getColumnName();
        return(nameB.toLowerCase().indexOf(nameA) > -1);
    }

    private void fillColumnsMap(ResourceGroupElement metadata, Map<String, List<Node>> allColumns) {
        try {
            List<SchemaElement> schemaList = metadata.getElements();
            if(schemaList != null) {
                for (SchemaElement schema : schemaList) {
                    List<SchemaElement> tables = ((ResourceGroupElement)schema).getElements();
                    if(tables != null) {
                        for (SchemaElement table : tables) {
                            List<SchemaElement> columns = ((ResourceGroupElement)table).getElements();
                            if(columns != null) {
                                for (SchemaElement column : columns) {
                                    String type = ((ResourceSingleElement) column).getType();
                                    List<Node> typedColumns = allColumns.get(type);
                                    if (typedColumns == null) {
                                        typedColumns = new ArrayList<Node>();
                                    }
                                    fillNode(schema, table, column, typedColumns);
                                    allColumns.put(type, typedColumns);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataDiscoveryException(e);
        }
    }

    private void fillNode(SchemaElement schema, SchemaElement table, SchemaElement column, List<Node> typedColumns) {
        Node newNode = new Node();
        newNode.setColumnName(column.getName());
        newNode.setFullPath(schema.getName() + Node.DELIMITER + table.getName());
        newNode.setIdentifier(((ResourceMetadataSingleElement)column).getIsIdentifier() != null
                && ((ResourceMetadataSingleElement)column).getIsIdentifier());
        typedColumns.add(newNode);
    }
}
