import java.io.File;
import java.util.Set;

import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;


public class SWRLParser {
	
	private static final String DOC_URL = "/Users/kk0/kuldeep/ORNL/Projects/SemQuery/Ontology_v3/SpatialRules/direction/north.owl";
	 //private static final String DOC_URL = "http://acrab.ics.muni.cz/ontologies/swrl_tutorial_ind.owl"; 
	static boolean filterFlag=false;
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	public static String parseRuleHeadBody(String ruleBodyAtom){
		
		String ruleAtomType = ruleBodyAtom.substring(0, ruleBodyAtom.indexOf("(<")); //System.out.println(ruleAtomType);
    	String restOfRuleBody = ruleBodyAtom.substring(ruleBodyAtom.indexOf("(<")+1, ruleBodyAtom.length()-1); //System.out.println(restOfRuleBody);
    	String restRuleElements[]=restOfRuleBody.split(" ");
    	
    	String predicate = restRuleElements[0];
    	String returnStmt="";
    	if(ruleAtomType.equalsIgnoreCase("BuiltInAtom")){
    		filterFlag=true;
    		String operator=getSWRLBuiltInOp(predicate);
    		//returnStmt="FILTER( "+parseRuleHeadBodyVariable(restRuleElements[1])+" "+operator+" "+parseRuleHeadBodyVariable(restRuleElements[2])+")"; 
    		returnStmt=parseRuleHeadBodyVariable(restRuleElements[1])+" "+operator+" "+parseRuleHeadBodyVariable(restRuleElements[2]);
    	}
    	else{
    	//return(ruleAtomType+" "+restRuleElements[0]+" "+parseRuleBodyVariable(restRuleElements[1])+" "+parseRuleBodyVariable(restRuleElements[2]));
    		returnStmt=parseRuleHeadBodyVariable(restRuleElements[1])+" "+predicate+" "+parseRuleHeadBodyVariable(restRuleElements[2]);
    	}
    	
    	return(returnStmt);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	public static String parseRuleHeadBodyVariable(String variableBody){
		
		String varBody = variableBody;
		String varBodySecondPart=varBody.split("#")[1];
		return("?"+varBodySecondPart.substring(0, varBodySecondPart.indexOf('>')));
		
		
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	public static String getSWRLBuiltInOp(String predicate){
		String op="";
		String opString=predicate.substring(predicate.indexOf('<')+1,predicate.lastIndexOf('>')).split("#")[1];
		
		if(opString.equalsIgnoreCase("equal")){
			op="=";
		}else if(opString.equalsIgnoreCase("lessThan")){
			op="<";
		}else if(opString.equalsIgnoreCase("greaterThan")){
			op=">";
		}
		
		return op;
	}
    
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(String args[]) throws OWLOntologyCreationException{
		//initialize ontology and reasoner 
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(DOC_URL)); 
        
        //OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
        for (SWRLRule rule : ontology.getAxioms(AxiomType.SWRL_RULE)) { 
            //System.out.println(rule.getBody().toArray()[0]);
        	//System.out.println(rule.getBody().toArray()[0].toString().indexOf("(<"));
        	//String ruleTempBody = rule.getBody().toArray()[0].toString();
        	System.out.println(rule.getBody()+"\n"+rule.getHead());
        	int ruleHeadAtoms = rule.getHead().toArray().length;
        	int i=0;
        	while(i<ruleHeadAtoms){
        	String formattedHeadAtom=parseRuleHeadBody(rule.getHead().toArray()[i].toString()); // iterate through all Atoms 
        	System.out.println(formattedHeadAtom);
        	i++;
        	}
        	
        	
        	int ruleBodyAtoms = rule.getBody().toArray().length;
        	int j=0;
        	String formattedBodyAtom="";
        	String formattedBodyAtomFilter="";
        	while(j<ruleBodyAtoms){
        	
        	String ruleBody = parseRuleHeadBody(rule.getBody().toArray()[j].toString());
        		
        	if(!filterFlag){
        	   //System.out.println(formattedBodyAtom);
        		formattedBodyAtom=formattedBodyAtom.concat(ruleBody)+" . "; // iterate through all Atoms
        	}
        	else{
        		//here filter 
        		formattedBodyAtomFilter=formattedBodyAtomFilter.concat(ruleBody);
        	}
        	j++;
        	}
        	filterFlag=false;
        	System.out.println(formattedBodyAtom);
        	System.out.println(formattedBodyAtomFilter+"\n");
        	
            
        } 
        
        
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
}
