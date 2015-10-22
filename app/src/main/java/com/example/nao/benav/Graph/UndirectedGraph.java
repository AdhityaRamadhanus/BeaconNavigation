package com.example.nao.benav.Graph;

import android.util.Log;

import com.example.nao.benav.Graph.GraphEdge;
import com.example.nao.benav.Graph.GraphEdgeComparator;
import com.example.nao.benav.Graph.GraphNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by nao on 8/27/15.
 */
public class UndirectedGraph {
    ArrayList<GraphNode> Nodes;
    ArrayList<GraphEdge> Edges;
    public UndirectedGraph(int NodesSize,ArrayList<GraphEdge> ListEdges){
        Nodes = new ArrayList<GraphNode>();
        for(int i=0;i<NodesSize+1;i++) {
            Nodes.add(new GraphNode(i));
        }
        for(int i=0;i<ListEdges.size();i++){
            GraphEdge edge = ListEdges.get(i);
            Nodes.get(edge.getSrc()).addEdge(edge);
            Nodes.get(edge.getDst()).addEdge(new GraphEdge(edge.getDst(),edge.getSrc(),edge.getWeight()));
        }
    }
    public void printGraph(){
        for(int i=1;i<Nodes.size();i++){
            Log.d("UndirectedGraph", "Node Id : " + Nodes.get(i).getId());
            ArrayList<GraphEdge> NodeEdges = Nodes.get(i).getEdges();
            for(int j=0;j<NodeEdges.size();j++){
                GraphEdge edge = NodeEdges.get(j);
                Log.d("UndirectedGraph","Edge , "+ edge.getSrc()+"->"+edge.getDst()+" , "+edge.getWeight());
            }
        }
    }
    public void Djikstra(int source, int dst){
        Log.d("Dijkstra","Computing distance from "+source+" to "+dst);
        Double[] Distances = new Double[Nodes.size()];
        Distances[source]=0.0;
        int[] Path = new int[Nodes.size()];
        Path[source]=source;
        boolean visited[] = new boolean[Nodes.size()];
        for(int i=0;i<Nodes.size();i++){
            visited[i]=false;
            if (i!=source){
                Distances[i]= Double.MAX_VALUE;
            }
        }
        int idx=source;
        Comparator<GraphEdge> comparator = new GraphEdgeComparator();
        PriorityQueue<GraphEdge> queue =   new PriorityQueue<GraphEdge>(10, comparator);
        while(visited[dst]==false){
            Log.d("Dijkstra","Iterating Node  "+idx);
            ArrayList<GraphEdge> edges = Nodes.get(idx).getEdges();
            for(int j=0;j<edges.size();j++){
                int destidx = edges.get(j).getDst();
                double Weight = edges.get(j).getWeight();
                if (!visited[destidx]){
                    queue.add(edges.get(j));
                }
                if (Distances[destidx] > (Distances[idx]+Weight)){
                    Distances[destidx] = Distances[idx]+Weight;
                    Path[destidx] = idx;
                }
            }

            visited[idx]=true;
            if (idx==dst) break;
            idx = queue.poll().getDst();
            while(visited[idx]==true){
                idx=queue.poll().getDst();
            }
        }
        Log.d("Dijkstra","Distance from "+source+" to "+dst+" is "+Distances[dst]);
        String Paths = ""+dst;
        while(Path[dst]!=dst){
            Paths+="->"+Path[dst];
            dst = Path[dst];
        }
        Log.d("Dijkstra","Path from "+source+" to "+dst+" is "+Paths);
        //Log.d("Dijkstra",""+source);
    }
}
