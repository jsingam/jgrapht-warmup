import com.singam.jgrapht.Application;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.io.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This is class is created by singam
 **/
public class ApplicationTest {

	/**
	 * This creates the graph for the test cases
	 * @return
	 * @throws FileNotFoundException
	 * @throws ImportException
	 */
	public DirectedMultigraph<String, DefaultEdge> createGraph() throws FileNotFoundException, ImportException {
		VertexProvider<String> vp = (label, attrs) -> label;
		EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
		ComponentUpdater<String> cu = (v, attrs) -> {
		};
		DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);
		DirectedMultigraph<String, DefaultEdge> graph =
				new DirectedMultigraph<>(DefaultEdge.class);
		importer.importGraph(graph,new FileInputStream("src/test/resources/sample.dot"));
		return graph;
	}

	/**
	 * This is a test case for getAnchestors method
	 * @throws FileNotFoundException
	 * @throws ImportException
	 */
	@Test
	public void testGetAnchestors() throws FileNotFoundException, ImportException {
		DirectedMultigraph<String, DefaultEdge> g=createGraph();
		Map<Integer,Set<String>> map=new HashMap<>();
		map.put(1,new HashSet<>(Arrays.asList("Eddard", "Rhaegar", "Lyanna")));
		map.put(2, new HashSet<>(Arrays.asList("Rickard", "Aerys_II")));
		map.put(3,new HashSet<>(Arrays.asList("Jaehaerys_II")));
		map.put(4,new HashSet<>(Arrays.asList("Aegon_V")));
		map.put(5,new HashSet<>(Arrays.asList("Maekar_I")));
		Assert.assertEquals(Application.getAnchestors("Jon",g),map);
	}

	/**
	 * This is the test case for findParentMethod
	 * @throws FileNotFoundException
	 * @throws ImportException
	 */
	@Test
	public void testFindParent() throws FileNotFoundException, ImportException {
		DirectedMultigraph<String, DefaultEdge> g=createGraph();
		Map<Integer,Set<String>> map=new HashMap<>();
		Assert.assertEquals(Application.findParent(Application.getAnchestors("Jon",g),Application.getAnchestors("Tommen",g)),Arrays.asList("Aegon_V"));
	}

}
