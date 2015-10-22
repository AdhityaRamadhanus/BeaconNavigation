package com.example.nao.benav.Graph;

/**
 * Created by nao on 8/27/15.
 */
public class GraphEdge {
    private int Src,Dst;
    private double Weight;
    public GraphEdge(int Source,int Destination,double Weight){
        Src = Source;
        Dst = Destination;
        this.Weight = Weight;
    }
    public int getSrc(){
        return Src;
    }
    public int getDst(){
        return Dst;
    }
    public double getWeight(){
        return Weight;
    }
}
