package com.singam.jgrapht;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.io.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * This is class is created by singam
 * ===================================
 *
 * This class finds the closest common ancestor of two people.
 *
 **/
public class Application {

	/**
	 * This is the main class that gets the arguments and generates graph and prints the output
	 *
	 * This takes three inputs as parameters
	 *          *filename ( have to be in the src/main/resources)
	 *          *name of the first person
	 *          *name of the second person
	 * Prints the common ancestor to the console
	 * @param args
	 * @throws IOException
	 * @throws ImportException
	 */
	public static void main(String[] args) throws IOException, ImportException {

		if(args.length!=3){     //arg check
			System.out.println("please enter the 3 arguments as per the instruction in the readme file ");
			System.exit(1);
		}
		String filename=args[0];
		String name1 =args[1];
		String name2=args[2];
		VertexProvider<String> vp = (label, attrs) -> label;
		EdgeProvider<String, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
		ComponentUpdater<String> cu = (v, attrs) -> {
		};
		DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);
		DirectedMultigraph<String, DefaultEdge> result =
				new DirectedMultigraph<>(DefaultEdge.class);
		importer.importGraph(result,new FileInputStream(filename)); //imports dot file to a graph
		if((!result.vertexSet().contains(name1)) || (!result.vertexSet().contains(name2))){        //vaidates the names
			System.out.println("Atleast one of the names in the arguments is not in the graph");
			System.exit(1);
		}
		Map<Integer,Set<String>> achester1 =getAnchestors(name1,result);
		Map<Integer,Set<String>> achester2=getAnchestors(name2,result);
		System.out.println("The List of Common Anchestors are : "+findParent(achester1,achester2));
	}

	/**
	 * This will find all the anchestors of a person
	 * @param v
	 * @param g
	 * @return
	 */
	public static Map<Integer,Set<String>> getAnchestors(String v,DirectedMultigraph g) {

		Set<String> parents = new HashSet<>();
		parents.add(v);
		Map<Integer,Set<String>> anchestors = new HashMap<>();
		int level=1;
		while(!parents.isEmpty()){                                  //fins the parents of parents from the previous iteration
			Set<String> levelParents=new HashSet<>();
			first:
			for(String parent:parents){
				Set<DefaultEdge> edges=g.incomingEdgesOf(parent);
				second:
				for(DefaultEdge e:edges){                                       //parents are found by the incomming edge
					Set<DefaultEdge> testSet=g.outgoingEdgesOf(parent);
					third:
					for (DefaultEdge ed :testSet){                                  //tests for the marriage relationship
						if(g.getEdgeTarget(ed).equals(g.getEdgeSource(e))) {
							break second;
						}
					}
					levelParents.add((String) g.getEdgeSource(e));                  //adds to the parents list for this level
				}
			}
			if(!levelParents.isEmpty()) anchestors.put(level,levelParents);
			level++;
			parents=levelParents;

		}


		return anchestors;
	}

	/**
	 * This method will find the list of closest ancestors when the list of ancestors are given
	 * @param person1
	 * @param person2
	 * @return
	 */
	public static List<String> findParent(Map<Integer,Set<String>> person1, Map<Integer,Set<String>> person2){
		int noAnchester1=person1.size();
		int noAnchester2=person2.size();
		boolean status=false;
		List<String> anchesters=new ArrayList<>();
		for(int tot=2;tot<noAnchester1+noAnchester2+1;tot++){                   //checks the total level from the minimum
			for(int temp=1;temp<tot;temp++){                                    //find the combinations for the total levels
				if(temp<noAnchester1+1 && (tot-temp)<noAnchester2+1) {
					for(String parent1:person1.get(temp)){
						if(person2.get(tot - temp).contains(parent1)){
							status=true;                                        // maintains a status when the first closest anchestor id found
							anchesters.add(parent1);
						}
					}
				}
			}
			if(status){
				return anchesters;
			}
		}

		return null;
	}
}
