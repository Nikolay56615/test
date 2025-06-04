package ru.nsu.lebedev.graph;

/**
 * Default realization of VertexReader interface.
 */
public class DefaultVertexReader implements VertexReader<String> {
    @Override
    public void readVertex(Graph<String> graph, String line) {
        var edgeData = line.split(" ");
        graph.addEdge(edgeData[0], edgeData[1], Double.parseDouble(edgeData[2]));
    }
}
