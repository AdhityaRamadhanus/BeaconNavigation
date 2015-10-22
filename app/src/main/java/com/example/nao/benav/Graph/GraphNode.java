package com.example.nao.benav.Graph;

import com.example.nao.benav.Graph.GraphEdge;

import java.util.ArrayList;

/**
 * Created by nao on 8/27/15.
 */
public class GraphNode {
    private int Id;
    ArrayList<GraphEdge> Edges;
    public GraphNode(int Id){
        this.Id = Id;
        Edges = new ArrayList<GraphEdge>();
    }
    public void addEdge(GraphEdge edge){
        Edges.add(edge);
    }
    public ArrayList<GraphEdge> getEdges(){
        return Edges;
    }
    public int getId(){
        return Id;
    }
    public void setId(int Id){
        this.Id=Id;
    }
}
