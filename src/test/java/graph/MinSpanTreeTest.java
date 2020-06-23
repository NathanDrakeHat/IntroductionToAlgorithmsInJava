package graph;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MinSpanTreeTest {
    @Test
    public void algorithmOfKruskalTest(){
        var G = buildKruskalExample();
        var t = MinSpanTree.algorithmOfKruskal(G);
        assertTrue(t.equals(buildKruskalAnswer1()) || t.equals(buildKruskalAnswer2()));
        int i = 0;
        for(var e : t){
            i += e.getWeight();
        }
        assertEquals(37,i );
    }
    static Graph<MinSpanTree.KruskalVertex<String>> buildKruskalExample(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.KruskalVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i, new MinSpanTree.KruskalVertex<>(names[i])); }
        Graph<MinSpanTree.KruskalVertex<String>> res = new Graph<>(vertices, Graph.Direction.NON_DIRECTED);
        int[] indexes1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indexes2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        double[] weights =  new double[]{4,8,7,9,10,2,1,8,11,2,7,6,4,14};
        int len_ = indexes1.length;
        for(int i = 0; i < len_; i++){
            res.setNeighbor(vertices.get(indexes1[i]), vertices.get(indexes2[i]), weights[i]);
        }
        return res;
    }
    static Set<Graph.Edge<MinSpanTree.KruskalVertex<String>>> buildKruskalAnswer1(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.KruskalVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i,new MinSpanTree.KruskalVertex<>(names[i])); }
        int[] indexes1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indexes2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        double[] weights =  new double[]{4,8,7,9,10,2,1,8,11,2,7,6,4,14};
        Set<Graph.Edge<MinSpanTree.KruskalVertex<String>>> res = new HashSet<>();
        int[] answers = new int[]{0, 2, 3, 5, 6, 7, 9, 12};
        for(var i : answers)
            res.add(new Graph.Edge<>(vertices.get(indexes1[i]), vertices.get(indexes2[i]), weights[i], Graph.Direction.NON_DIRECTED));
        return res;
    }
    static Set<Graph.Edge<MinSpanTree.KruskalVertex<String>>> buildKruskalAnswer2(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.KruskalVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i,new MinSpanTree.KruskalVertex<>(names[i])); }
        int[] indexes1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indexes2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        double[] weights =  new double[]{4,8,7,9,10,2,1,8,11,2,7,6,4,14};
        Set<Graph.Edge<MinSpanTree.KruskalVertex<String>>> res = new HashSet<>();
        int[] answers = new int[]{0, 1, 2, 3, 5, 6, 9, 12};
        for(var i : answers)
            res.add(new Graph.Edge<>(vertices.get(indexes1[i]), vertices.get(indexes2[i]), weights[i], Graph.Direction.NON_DIRECTED));
        return res;
    }


    @Test
    public void algorithmOfPrimTestWithFibonacciHeap(){
        var graph = buildPrimExample();
        MinSpanTree.algorithmOfPrimWithFibonacciHeap(graph, new MinSpanTree.PrimVertex<>("a"));
        var vertices = graph.getAllVertices();
        Set<Set<MinSpanTree.PrimVertex<String>>> res = new HashSet<>();
        for(var vertex : vertices){
            if(vertex.parent != null){
                Set<MinSpanTree.PrimVertex<String>> t = new HashSet<>();
                t.add(vertex);
                t.add(vertex.parent);
                res.add(t);
            }
        }
        assertTrue(res.equals(buildPrimAnswer1()) || res.equals(buildPrimAnswer2()));
    }

    @Test
    public void algorithmOfPrimTestWithMinHeap(){
        var graph = buildPrimExample();
        MinSpanTree.algorithmOfPrimWithMinHeap(graph, new MinSpanTree.PrimVertex<>("a"));
        var vertices = graph.getAllVertices();
        Set<Set<MinSpanTree.PrimVertex<String>>> res = new HashSet<>();
        for(var vertex : vertices){
            if(vertex.parent != null){
                Set<MinSpanTree.PrimVertex<String>> t = new HashSet<>();
                t.add(vertex);
                t.add(vertex.parent);
                res.add(t);
            }
        }
        assertTrue(res.equals(buildPrimAnswer1()) || res.equals(buildPrimAnswer2()));
    }
    static Graph<MinSpanTree.PrimVertex<String>> buildPrimExample(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.PrimVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i,new MinSpanTree.PrimVertex<>(names[i])); }
        Graph<MinSpanTree.PrimVertex<String>> res = new Graph<>(vertices, Graph.Direction.NON_DIRECTED);
        int[] indices1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indices2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        double[] weights =  new double[]{4,8,7,9,10,2,1,8,11,2,7,6,4,14};
        int len_ = indices1.length;
        for(int i = 0; i < len_; i++){
            res.setNeighbor(vertices.get(indices1[i]), vertices.get(indices2[i]), weights[i]);
        }
        return res;
    }
    static Set<Set<MinSpanTree.PrimVertex<String>>> buildPrimAnswer1(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.PrimVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i,new MinSpanTree.PrimVertex<>(names[i])); }
        int[] indexes1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indexes2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        Set<Set<MinSpanTree.PrimVertex<String>>> res = new HashSet<>();
        int[] answers = new int[]{0, 1, 2, 3, 5, 6, 9, 12};
        for (int answer : answers) {
            Set<MinSpanTree.PrimVertex<String>> t = new HashSet<>();
            t.add(vertices.get(indexes1[answer]));
            t.add(vertices.get(indexes2[answer]));
            res.add(t);
        }
        return res;
    }
    static Set<Set<MinSpanTree.PrimVertex<String>>> buildPrimAnswer2(){
        String n = "a,b,c,d,e,f,g,h,i";
        String[] names = n.split(",");
        int len = names.length;
        var vertices = new ArrayList<MinSpanTree.PrimVertex<String>>(len);
        for(int i = 0; i < len; i++){ vertices.add(i,new MinSpanTree.PrimVertex<>(names[i])); }
        int[] indexes1 = new int[]{0,1,2,3,4, 5,6,7,1, 2,8,8,2,3};
        int[] indexes2 = new int[]{1,2,3,4,5, 6,7,0,7, 8,7,6,5,5};
        Set<Set<MinSpanTree.PrimVertex<String>>> res = new HashSet<>();
        int[] answers = new int[]{0, 7, 2, 3, 5, 6, 9, 12};
        for (int answer : answers) {
            Set<MinSpanTree.PrimVertex<String>> t = new HashSet<>();
            t.add(vertices.get(indexes1[answer]));
            t.add(vertices.get(indexes2[answer]));
            res.add(t);
        }
        return res;
    }

}