/*
 * (C) Copyright 2017-2017, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.GraphTests;
import org.jgrapht.alg.interfaces.TSPAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 * A 2-approximation algorithm for the metric TSP problem.
 * 
 * <p>
 * The travelling salesman problem (TSP) asks the following question: "Given a list of cities and
 * the distances between each pair of cities, what is the shortest possible route that visits each
 * city exactly once and returns to the origin city?". In the metric TSP, the intercity distances
 * satisfy the triangle inequality.
 * 
 * <p>
 * This is an implementation of the folklore algorithm which returns a depth-first ordering of the
 * minimum spanning tree. The algorithm is a 2-approximation assuming that the instance satisfies
 * the triangle inequality. The implementation requires the input graph to be undirected and
 * complete. The running time is O(|V|^2 log|V|).
 * 
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">wikipedia</a> for more
 * details.
 * 
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Dimitrios Michail
 */
public class TwoApproxMetricTSP<V, E>
    implements TSPAlgorithm<V, E>
{
    /**
     * Construct a new instance
     */
    public TwoApproxMetricTSP()
    {
    }

    /**
     * Computes a 2-approximate tour.
     * 
     * @param graph the input graph
     * @return a tour
     * @throws IllegalArgumentException if the graph is not undirected
     * @throws IllegalArgumentException if the graph is not complete
     * @throws IllegalArgumentException if the graph contains no vertices
     */
    @Override
    public GraphPath<V, E> getTour(Graph<V, E> graph)
    {
        if (!graph.getType().isUndirected()) {
            throw new IllegalArgumentException("Graph must be undirected");
        }
        if (!GraphTests.isComplete(graph)) {
            throw new IllegalArgumentException("Graph is not complete");
        }
        if (graph.vertexSet().isEmpty()) {
            throw new IllegalArgumentException("Graph contains no vertices");
        }

        /*
         * Special case singleton vertex
         */
        if (graph.vertexSet().size() == 1) {
            V start = graph.vertexSet().iterator().next();
            return new GraphWalk<>(
                graph, start, start, Collections.singletonList(start), Collections.emptyList(), 0d);
        }

        /*
         * Create MST
         */
        Graph<V, DefaultEdge> mst = new SimpleGraph<>(DefaultEdge.class);
        for (V v : graph.vertexSet()) {
            mst.addVertex(v);
        }
        for (E e : new KruskalMinimumSpanningTree<>(graph).getSpanningTree().getEdges()) {
            mst.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e));
        }

        /*
         * Perform a depth-first-search traversal
         */
        int n = graph.vertexSet().size();
        Set<V> found = new HashSet<>(n);
        List<V> tour = new ArrayList<>(n + 1);
        V start = graph.vertexSet().iterator().next();
        DepthFirstIterator<V, DefaultEdge> dfsIt = new DepthFirstIterator<>(mst, start);
        while (dfsIt.hasNext()) {
            V v = dfsIt.next();
            if (found.add(v)) {
                tour.add(v);
            }
        }
        // repeat the start vertex
        tour.add(start);

        /*
         * Explicitly build the path.
         */
        List<E> tourEdges = new ArrayList<E>(n);
        double tourWeight = 0d;
        Iterator<V> tourIt = tour.iterator();
        V u = tourIt.next();
        while (tourIt.hasNext()) {
            V v = tourIt.next();
            E e = graph.getEdge(u, v);
            tourEdges.add(e);
            tourWeight += graph.getEdgeWeight(e);
            u = v;
        }
        return new GraphWalk<>(graph, start, start, tour, tourEdges, tourWeight);
    }

}
