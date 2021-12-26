import java.util.Comparator;

public class NodeComparator {
   public static class smallerG implements Comparator<Node> {
       @Override
       /*
        public int compare(Node first, Node second) {
            if (first.g < second.g) {
                return 1;
            }
            if (first.g > second.g) {
                return -1;
            }
            return 0;
        }
        */

        public int compare(Node s1, Node s2){
	        if(s1.f == s2.f && s1.g == s2.g){
	            return (Math.random() >= 0.5) ? -1 : 1;
	        		//return 0;
	        }
	        if(s1.f != s2.f) {
	        	return s1.f - s2.f;
	        }
	        
	       if(s1.f == s2.f) {
	    	   	return s1.g - s2.g;
	       }

	       return (Math.random() >= 0.5) ? -1 : 1;
	       //return 0;
	    }
   }

   public static class higherG implements Comparator<Node> { 
       @Override
       /*
        public int compare(Node first, Node second) {
            if (first.g > second.g) {
                return 1;
            }
            if (first.g < second.g) {
                return -1;
            }
            return 0;
        }
        */
        
        public int compare(Node s1, Node s2){
	        if(s1.f == s2.f && s1.g == s2.g){
	            return (Math.random() >= 0.5) ? -1 : 1;
	        		//return 0;
	        }

	        if(s1.f != s2.f) {
        			return s1.f - s2.f;
	        }	
	       
	       if(s1.f == s2.f) {
	    	   		return s2.g - s1.g;
	       }

	       return (Math.random() >= 0.5) ? -1 : 1;
	       //return 0;
	    }
        
   }
}
