package com.example.nao.benav.Graph;

import java.util.Comparator;
/**
 * Created by nao on 8/27/15.
 */
public class GraphEdgeComparator implements Comparator<GraphEdge>{
    @Override
    public int compare(GraphEdge lhs, GraphEdge rhs) {
        if (lhs.getWeight()<rhs.getWeight()){
            return 1;
        }
        else if (lhs.getWeight()>rhs.getWeight()){
            return -1;
        }
        else{
            return 0;
        }
    }
}
