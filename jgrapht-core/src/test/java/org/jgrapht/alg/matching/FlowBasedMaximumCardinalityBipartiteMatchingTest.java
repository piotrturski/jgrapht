/*
 * (C) Copyright 2017-2017, by Joris Kinable and Contributors.
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
package org.jgrapht.alg.matching;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.util.Collection;

/**
 * Unit test for the FlowBasedMaximumCardinalityBipartiteMatching class
 * 
 * @author Joris Kinable
 *
 */
public class FlowBasedMaximumCardinalityBipartiteMatchingTest
    extends MaximumCardinalityBipartiteMatchingTest
{

    @Override
    public MatchingAlgorithm<Integer, DefaultEdge> getMatchingAlgorithm(Graph<Integer, DefaultEdge> graph, Collection<Integer> partition1, Collection<Integer> partition2) {
        return new FlowBasedMaximumCardinalityBipartiteMatching<>(graph, partition1, partition2);
    }
}
