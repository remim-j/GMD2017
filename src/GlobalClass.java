import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.queryparser.classic.ParseException;


public class GlobalClass {

static HashMap<String,Integer> provokedDiseases=new HashMap<String,Integer>();	
static HashMap<String,Integer> originOfSideEffect=new  HashMap<String,Integer>();	
static HashMap<String,Integer>  usefulMedecine=new HashMap<String,Integer>();	
static ArrayList<String> suggestedEntry=new ArrayList<String>();

	
	static Scanner sc=  new Scanner(System.in);;
	private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	public static void doSearch(String userInput){
		
		
		Long startTime=(long) (System.nanoTime());
		try {
			
			/*********inialize BD***********/
			
			AccesSider.InitSider();
			ReadHpoAnnotations.connect();
			
					/***********/
			
			
			
			Long startTime1= (System.nanoTime());

			
			/*first we start with bases which link with the entry*/
			 
			ArrayList<String> stitchIdFromSider=AccesSider.getStitchIDByConceptName(userInput);
			ArrayList<String> cuiFromSider=AccesSider.cuiToCure(userInput);
			ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);
			ArrayList<String> diseaseFromOmim=ReadOmim.getTI("CS",userInput);
			ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name",userInput);
			ArrayList<String> diseaseNameFromOrpha=AccesOrphaDataBase.GetDeseaseByClinicalSign(userInput);
			ArrayList<String> idOriginOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);
			
			ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI(userInput);
			
			
			long secondTime = System.nanoTime();
       		double durationT = (startTime1 - secondTime)/Math.pow(10,9);
       		System.out.println("temps a verifier "+durationT);


			/*we create a thread which will do this task*/
			ExecutorService executorService1 = Executors.newFixedThreadPool(NUM_CORES);
			  List<Future<?>> first_futures  = new LinkedList<Future<?>>();

			Future future1 = executorService1.submit(new Runnable() {
			    public void run() {
			     //   System.out.println("Asynchronous task 2");
			        if (idOriginOfSideEffectFromSider !=null){
						//for(String s : idOriginOfSideEffectFromSider){
							
							  ExecutorService executorService1_1 = Executors.newFixedThreadPool(NUM_CORES);
							  //List<Future<?>> first_futures  = new LinkedList<Future<?>>();
								    for(String s : idOriginOfSideEffectFromSider){
										Future future11 = executorService1_1.submit(new Runnable() {
							 				public void run() {
							 					String originOfSideEffectName=ReadStitch.getATCNameByStitchID(s);
												addOriginSideEffect(originOfSideEffectName);
							 				 }});
							 				first_futures.add(future11);

									}
									/*for (Future<?> f : first_futures)
										    {
										        try   { f.get(); }
										        catch (InterruptedException e) { } 
										        catch (ExecutionException   e) { }         
										    }
											
											
									executorService1.shutdown();*/    
						
						
					}
			    }
			});
       		
       		
	       		ExecutorService executorService2 = Executors.newFixedThreadPool(NUM_CORES);
	       		Future future2 = executorService2.submit(new Runnable() {
				    public void run() {
				    	if (stitchIdFromSider !=null){
							for (String s:stitchIdFromSider){
								String drugName=ReadStitch.getATCNameByStitchID(s);
								addMedecine(drugName);
							}
						
				        
				    }
				    }
				});
       		
       		
			
			
			
			 
			/*we take cui from Omim_onto , take them to sider in order to find 
			 * medine 
			 * and  sideEffect
			 */
	       	
       		/*ExecutorService executorService3 = Executors.newFixedThreadPool(NUM_CORES);;
       		Future future3 = executorService3.submit(new Runnable() {
			  public void run() {*/
				  
			    	long firstTime = System.nanoTime();
			    	 ExecutorService executorService3 = Executors.newFixedThreadPool(NUM_CORES);
				       List<Future<?>> futures3  = new LinkedList<Future<?>>();
	
	             if (cuiMedecineOmimOnto!=null){
				//for (String s:cuiMedecineOmimOnto){ 
						//HERE
					// ExecutorService executorService3 = Executors.newFixedThreadPool(NUM_CORES);
				       // List<Future<?>> futures3  = new LinkedList<Future<?>>();
						for (String s:cuiMedecineOmimOnto){ 

							Future future11 = executorService3.submit(new Runnable() {
							public void run() {
									try{
										ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");;
										if (stitchId!=null){
											 ExecutorService executorService111 = Executors.newFixedThreadPool(1);
										     
											for (String s1:stitchId){
												Future future111 = executorService111.submit(new Runnable() {
													public void run() {
														String label=ReadStitch.getATCNameByStitchID(s1);
														addMedecine(label);
											
												}});
												//futures111.add(future111);
											}
											
											
										}
										ArrayList<String> sideEffectStitchID=AccesSider.idMedocCauseEffetSecondaire(s);
										if (sideEffectStitchID !=null){
											 ExecutorService executorService112 = Executors.newFixedThreadPool(1);
											for (String s1:sideEffectStitchID){
												executorService112.submit(new Runnable() {
													public void run() {
														String label=ReadStitch.getATCNameByStitchID(s1);
														addOriginSideEffect(label);
											
												}});
												
											}
									
								}
		 					}catch(Exception ex){
		 						System.err.println("autre erreur");
		 						while (ex!=null){
		 							System.err.println("Error msg: "+ex.getMessage());
		 							ex.printStackTrace();			
		 						}
		 					}
		 					
						
		 				 }});
		 				futures3.add(future11);

				}
						
						  
				long secondTimeT = System.nanoTime();
	       		double durationT1 = (firstTime - secondTime)/Math.pow(10,9);
	       		//System.out.println("duration T "+durationT1);
	             }
	             
			    //}
			//});
       		
      
			
			

       		ExecutorService executorService4 = Executors.newFixedThreadPool(NUM_CORES);
       		Future future4 = executorService4.submit(new Runnable() {
			    public void run() {
			    	if (cuiFromSider!= null){
			    		try {
			    			for (String s: cuiFromSider){
			    				
								String classId=ReadOmimOnto.CUIToClassID(s);
								if (classId !=null){
									ArrayList<String> diseaseLabelFromOmim=ReadOmim.getTI("NO",classId);
									ArrayList<String> diseaseLabelFromHpoAnnot=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(classId);
									
									for (String s1:diseaseLabelFromOmim){
										addDisease(s1);
									}
									for (String s2:diseaseLabelFromHpoAnnot){
										addDisease(s2);
									}
								}
						
							}
			    			
					} catch (IOException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	 catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
			    		
			    }
			    	
		       		
			}});
			
			

			
			if (diseaseIdFromOrpha != null){
				/*We follow the mapping*/
				for (String s:diseaseIdFromOrpha ){
					//System.out.println(s);
					ArrayList<String> diseaseLabelFromHpoAnnotation=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
					for (String s2 : diseaseLabelFromHpoAnnotation){
						addDisease(s2);
					}
				}
			}
			
				
			

			if (symptomIdFromHpObo !=null){				
				for (String s : symptomIdFromHpObo){
					ArrayList<String> diseaseLabel=ReadHpoAnnotations.getDiseaseLabelBySignId(s);
					for(String s1:diseaseLabel){
						addDisease(s1);
					}
				}
			}
			
			
			if (diseaseNameFromOrpha !=null){
				for (String s : diseaseNameFromOrpha){
					addDisease(s);
				}
				
			}
			
			if (diseaseFromOmim!=null){
				for(String s:diseaseFromOmim){
					addDisease(s);
				}
			}
			
			
			
			
			long first = System.nanoTime();

			for (Future<?> f : first_futures)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { } 
		        catch (ExecutionException   e) { }         
		    }

			
       		//System.out.println("duration T "+durationT1);
			for (Future<?> f : futures3)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { } 
		        catch (ExecutionException   e) { }         
		    }
			
			long second = System.nanoTime();
       		double dur = (first - second)/Math.pow(10,9);
       		System.out.println("duration T "+dur);
	while ( !future2.isDone()   || !future4.isDone() || !future1.isDone()){};
	
	
	executorService1.shutdown();  
	executorService2.shutdown(); 
	executorService3.shutdown(); 
	executorService4.shutdown(); 

	
	
	AccesSider.closeSider();
	ReadHpoAnnotations.disconnect();
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	
	public static void showResult(){
		
		if (provokedDiseases.size()!=0){
			System.out.println("*****provoked diseases****");
			for(String s:provokedDiseases.keySet()){
				System.out.println(s +" : "+provokedDiseases.get(s));
			}
			System.out.println("\n");
		}
		
		if(usefulMedecine.size()!=0){
			System.out.println("*****useful Medecines****");
			for(String s:usefulMedecine.keySet()){
				System.out.println(s +" : "+usefulMedecine.get(s));

			}
			System.out.println("\n");
		}
		
		if (originOfSideEffect.size()!=0){
			System.out.println("*****origin of possible SideEffect****");
			for(String s:originOfSideEffect.keySet()){
				System.out.println(s +" : "+originOfSideEffect.get(s));
			}
			System.out.println("\n");

		}
		
		if(suggestedEntry.size()!=0){
			System.out.println("*****suggestions for entry****");
			for(String s:suggestedEntry){
				System.out.println(s);
			}
			System.out.println("\n");

		}
		
		
	}
	
	public static void clearList(){
		 provokedDiseases.clear();
		 originOfSideEffect.clear();
		 usefulMedecine.clear();
		  suggestedEntry.clear();
	}
	
	public static  void addDisease(String s){
		if (!provokedDiseases.containsKey(s)){
			provokedDiseases.put(s,1);
		}
		else{
			int oldNum=provokedDiseases.get(s);
			provokedDiseases.put(s,oldNum+1);

		}
		
	}
	
	public static  void addMedecine(String s){
		if(s!=null){
			if (!usefulMedecine.containsKey(s)){
				usefulMedecine.put(s,1);
			}
			else{
				int oldNum=usefulMedecine.get(s);
				usefulMedecine.put(s,oldNum+1);

			}
		}
		
	}
	
	public static  void addOriginSideEffect(String s){
		if(s!=null){
			if (!originOfSideEffect.containsKey(s)){
				originOfSideEffect.put(s,1);
			}
			else{
				int oldNum=originOfSideEffect.get(s);
				originOfSideEffect.put(s,oldNum+1);

			}
		}
	}
	public static void main(String[] args){
		String userInput="";
		
		 //String rep = "";
		
			System.out.println("Entrer votre requette" +"|"+" q pour sortir");
	        while (!"q".equals(userInput)) {
	            
	                System.out.print(":> ");
	                userInput = sc.nextLine();
	                try{
	                	long startTime = System.nanoTime();
	                	doSearch(userInput);
		                showResult();
		               clearList();
		               long endTime = System.nanoTime();
		       		double duration = (endTime - startTime)/Math.pow(10,9);
		       		System.out.println("\n Time needed : "+duration);
		        		System.out.println("Entrer votre requette" +"|"+" q pour sortir");
	        		}catch (Exception e){
	        			System.out.println(e);
	        		}
	                
	        }

		
			
	}
	
	/* NE PAS SUPPRIMER CE COMMENTAIRE SVP
	public void addDisease(String s){
		if (!provokedDiseases.contains(s)){
			provokedDiseases.add(s);
		}
	}
	
	public void addOriginOfSideEffect(String s){
		if (!originOfSideEffect.contains(s)){
			originOfSideEffect.add(s);
		}
	}
	*/
}
