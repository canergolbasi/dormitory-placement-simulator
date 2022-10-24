import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class placementApp {

	public static void main(String[] args) {
		PriorityQueue<Student> wStudent = new PriorityQueue<Student>(10, new StudentIdComparator());     //Student information will always be transferred between these two PriorityQueue 
		PriorityQueue<Student> wStudent2 = new PriorityQueue<Student>(10, new StudentIdComparator());  //and student info will be published at the end of the 8th semester
		PriorityQueue<House> myHouse = new PriorityQueue<House>(10, new HouseIdComparator());            // for sorting house informations
		ArrayList<House> myHouse2 = new ArrayList<House>();												// for keeping house informations
		
		
		ArrayList<String> txtArray = new ArrayList<String>();    //put informations from input.txt
		String inputFileName=args[0];
		String outputFileName=args[1];
		
		Scanner sc2 = null;	   
		try {
	        sc2 = new Scanner(new File(inputFileName));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }	
	    while (sc2.hasNextLine()) {                     //copy input.txt to txtArray
	    	Scanner s2 = new Scanner(sc2.nextLine());
	    	String ss=s2.nextLine();
	        txtArray.add(ss) ;           
	    }
	    

	    for(int i=0;i<txtArray.size();i++) {
	    	String string1=txtArray.get(i);
	    	char firstinitial=string1.charAt(0);
	    	if(firstinitial=='s') {             // add student values to student1 PriorityQueue
	    		Scanner sc3=null;
	    		sc3=new Scanner(string1);
	    		String firstletter=sc3.next();
	    		int valid=Integer.parseInt(sc3.next());
	    		String valname=sc3.next();
	    		int valduration=Integer.parseInt(sc3.next());
	    		double valrating=Double.parseDouble(sc3.next());
	    		Student student1 = new Student(valid, valname,valduration,valrating);
	    		wStudent.add(student1); 
	    		sc3.close();
	    		
	    	}else {                            // add house values to house1 PriorityQueue
	    		Scanner sc3=null;
	    		sc3=new Scanner(string1);
	    		String firstletter=sc3.next();
	    		int valid=Integer.parseInt(sc3.next());
	    		int valduration=Integer.parseInt(sc3.next());
	    		double valrating=Double.parseDouble(sc3.next());
	    		House h1 = new House(valid ,valduration, valrating);
	    		myHouse.add(h1); 
	    		sc3.close();
	    	}
	    }

		while(!myHouse.isEmpty()) {               // send myHouse priorityqueu values to myHouse2 arraylist<House>
			myHouse2.add(myHouse.poll());
		}
		
		
		for(int r=0;r<4;r++) {
		//semester1   || semester 3 in second loop    || semester5 in third loop || semester7 in fourth loop
		while(!wStudent.isEmpty()) {			
			while(wStudent.peek().duration==0 ) {  					//If the duration of the student is 0, send directly to the other and delete from there
				int nid=wStudent.peek().id;                         //take id
				String nname=wStudent.peek().name;                 //take name
				int nduration=(wStudent.peek().duration);         //take duration
				double nrating=wStudent.peek().rating;            //take rating
				wStudent.remove(); // en usttekini sildik
				Student nstudent=new Student(nid,nname,nduration, nrating ); //we created the student with new values
				wStudent2.add(nstudent);
				if(wStudent.isEmpty()) {
					break;
				}				
			}			
			if(!wStudent.isEmpty()) {
			boolean state=true;		
			int a=0;				//number of house, 1. house 2. house... int the myHouse2
			int counter2=0;       // use for checking does it scanned them all for house
			while(state && a<myHouse2.size()) {
				if(myHouse2.get(a).duration==0 && (myHouse2.get(a).rating >= wStudent.peek().rating )) {
					int nduration1=(wStudent.peek().duration);                    // copy student duration 
					int nid1=myHouse2.get(a).id;                               //same old id
					double nrating1=myHouse2.get(a).rating;                   //same old rating
					House nhouse1=new House(nid1, nduration1, nrating1);     //	update the duration of the house where the student is residing				
					myHouse2.set(a, nhouse1);                                // update the duration value of house
					wStudent.remove();	
					state=false;									
				}else if(counter2==(myHouse2.size()-1) ) {	        //if all houses were tried				
						int nid=wStudent.peek().id;      // Decrease the top student duration by 1. Posted it to 2.priorityqueue and deleted it from 1.priorityqueue.
						String nname=wStudent.peek().name;
						int nduration;
						if(wStudent.peek().duration==0) { //If the student duration is 0, so that it does not decrease further.
						 nduration=(wStudent.peek().duration);	
						}else {
						 nduration=(wStudent.peek().duration-1);
						}
						double nrating=wStudent.peek().rating;
						wStudent.remove();                                           // we remove the top element
						Student nstudent=new Student(nid,nname,nduration, nrating ); //we created the student with new values
						wStudent2.add(nstudent);										//add to other priorityqueue
						state=false;					
					}
			a++;
			counter2++;
				}		
			}
			//1 semester is over. Decrease(-1) the duration of houses with duration greater than 0.   			
		}	
		for(int d=0; d<myHouse2.size() ; d++) {   
			  if(myHouse2.get(d).duration>0) {                  //so that the duration is not less than 0
				int nduration2=((myHouse2.get(d).duration)-1);             //  decrease house duration 1       
				int nid2=myHouse2.get(d).id;                               //same old id
				double nrating2=myHouse2.get(d).rating;                   //same old rating
				House nhouse2=new House(nid2, nduration2, nrating2);      //we created the house with new values				
				myHouse2.set(d, nhouse2);                                // aim is update the duration value of all house
				}			
			}

		//semester2   || semester4 in second loop    || semester6 in third loop  || semester8 in fourth loop
		while(!wStudent2.isEmpty()) {
			while(wStudent2.peek().duration==0 ) {  //If the duration of the student is 0, send directly to the other and delete from there
				int nid=wStudent2.peek().id;
				String nname=wStudent2.peek().name;                              //take name
				int nduration=(wStudent2.peek().duration);                      // take student duration 
				double nrating=wStudent2.peek().rating;                         //take student rating
				wStudent2.remove();                                            // remove top element
				Student nstudent=new Student(nid,nname,nduration, nrating ); //we created the student with new values
				wStudent.add(nstudent);                                      //add to other priorityqueue
				if(wStudent2.isEmpty()) {
					break;
				}
			}	
			if(!wStudent2.isEmpty()) {
			boolean state=true;		
			int a=0;     					//number of house, 1. house 2. house... int the myHouse2 
			int counter2=0;  											// use for checking does it scanned them all for house
			while(state && a<myHouse2.size()) {
				if(myHouse2.get(a).duration==0 && (myHouse2.get(a).rating >= wStudent2.peek().rating )) {
					int nduration1=(wStudent2.peek().duration);                    // copy student duration
					int nid1=myHouse2.get(a).id;                               //same old id
					double nrating1=myHouse2.get(a).rating;                   //same old rating
					House nhouse1=new House(nid1, nduration1, nrating1);     //	update the duration of the house where the student is residing					
					myHouse2.set(a, nhouse1);                                // update the duration value of house
					wStudent2.remove();	
					state=false;									
				}else if(counter2==(myHouse2.size()-1) ) {		//if all houses were tried			
						int nid=wStudent2.peek().id;             // Decrease the top student duration by 1. Posted it to 2.priorityqueue and deleted it from 1.priorityqueue. 
						String nname=wStudent2.peek().name;
						int nduration;
						if(wStudent2.peek().duration==0) {       //If the student duration is 0, so that it does not decrease further.
						 nduration=(wStudent2.peek().duration);	
						}else {
						 nduration=(wStudent2.peek().duration-1);
						}
						double nrating=wStudent2.peek().rating;
						wStudent2.remove();                                           // we remove the top element
						Student nstudent=new Student(nid,nname,nduration, nrating ); //we created the student with new values
						wStudent.add(nstudent);
						state=false;					
					}
			a++;
			counter2++;
				}
			}
			//1 semester is over. Decrease(-1) the duration of houses with duration greater than 0. 			
		}	
		for(int d=0; d<myHouse2.size() ; d++) {   
			  if(myHouse2.get(d).duration>0) {                  //so that the duration is not less than 0
				int nduration2=((myHouse2.get(d).duration)-1);             //  subtract house duration 1       
				int nid2=myHouse2.get(d).id;                               //same old id
				double nrating2=myHouse2.get(d).rating;                   //same old rating
				House nhouse2=new House(nid2, nduration2, nrating2);      //we created the house with new values				
				myHouse2.set(d, nhouse2);                                // aim is update the duration value of all house
				}			
			}
		}
		
				
		
				
		      //create output folder from wStudent2 priorityqueue student names
		      FileWriter myWriter = null;
			try {
				myWriter = new FileWriter(outputFileName);
			} catch (IOException e) {				
				e.printStackTrace();
			}
		      
		      while(!wStudent.isEmpty()) {
		    	  try {
					myWriter.write(wStudent.poll().name);
				} catch (IOException e) {					
					e.printStackTrace();
				}
		    	  try {
					myWriter.write("\n");
				} catch (IOException e) {					
					e.printStackTrace();
				}
		      }
		      
		      try {
				myWriter.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		      

	}

}



class StudentIdComparator implements Comparator<Student>{    //same with priorityqueue ps lecture. I changed only the arrow directions.
    
    public int compare(Student s1, Student s2) {
        if (s1.id > s2.id)
            return 1;
        else if (s1.id < s2.id)
            return -1;
        
        return 0;
    }
}


class HouseIdComparator implements Comparator<House>{
    
    public int compare(House s1, House s2) {
        if (s1.id > s2.id)
            return 1;
        else if (s1.id < s2.id)
            return -1;
        
        return 0;
    }
    


	
}