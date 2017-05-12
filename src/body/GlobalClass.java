package body;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.queryparser.classic.ParseException;


public class GlobalClass {

//list of suggestions
//private static ArrayList<String> suggestedEntry=new ArrayList<String>();


private static ResultsLists possibleDiseases=new  ResultsLists();
private static ResultsLists possibleOriginOfSideEffect=new  ResultsLists();
private static ResultsLists usefulMedecines=new  ResultsLists();
static Scanner sc=  new Scanner(System.in);
public static String userInput;
private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	public GlobalClass(){
		
	}

	

	public static void doSearch(String userInput){


		
		try {

			/*********inialize BD***********/

			ReadHpoAnnotations.connect();

			/**************************************************************************************************/

		/**we enter in sider for searching origin of side effects **/
			
			ArrayList<String> idOriginOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);

			final ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);
			List<Future<?>> first_futures  = new LinkedList<Future<?>>();
		        if (idOriginOfSideEffectFromSider !=null){
							    for(final String s : idOriginOfSideEffectFromSider){
									Future future11 = executorService.submit(new Runnable() {
						 				public void run() {
						 					String originOfSideEffectName=ReadStitch.getATCNameByStitchID(s);
											possibleOriginOfSideEffect.add(originOfSideEffectName,"Sider +Stitch + ATC");
						 				 }});
						 				first_futures.add(future11);
	
								}
				}
			  


		final ArrayList<String> stitchIdFromSider=AccesSider.getStitchIDByConceptName(userInput);
		final ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name",userInput);

		/**************************************************************************************************/
				/**we enter in sider for searching medecine*/
	       		Future future2 = executorService.submit(new Runnable() {
				    public void run() {
				    	if (stitchIdFromSider !=null){
							for (String s:stitchIdFromSider){
								String drugName=ReadStitch.getATCNameByStitchID(s);
								usefulMedecines.add(drugName,"Sider");

							}
				    	}
				    	

			/***********************************************************************************/			
				  /**  we enter in hpo.obo*/

				    }
				});
	       		
				final ExecutorService executorService2Bis = Executors.newFixedThreadPool(NUM_CORES);

				List<Future<?>> futures_2Bis  = new LinkedList<Future<?>>();
				
			       		if (symptomIdFromHpObo !=null){
							for (final String s : symptomIdFromHpObo){
								Future future2Bis = executorService2Bis.submit(new Runnable() {
								    public void run() {
										ArrayList<String> diseaseLabel;
										try {
											diseaseLabel = ReadHpoAnnotations.getDiseaseLabelBySignId(s);
											for(String s1:diseaseLabel){
												//HpOboDiseaseIdAdd.add(ReadHpoAnnotations.getDiseaseIdByLabel(s1));
												String label=ReadHpoAnnotations.getDiseaseIdByLabel(s1);
												possibleDiseases.saveId(label,s1);
												possibleDiseases.add(s1, "Hp.obo + Hpo Annotation");
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		
							}});
								futures_2Bis.add(future2Bis);
			       		}
				    }
	/******************************************************************************************************/
				
           /**we take cui from Omim_onto , take them to sider in order to find medine and  sideEffect  */
			ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI(userInput);

			 List<Future<?>> futures3  = new LinkedList<Future<?>>();
	         if (cuiMedecineOmimOnto!=null){
					for (final String s:cuiMedecineOmimOnto){
						Future future11 = executorService.submit(new Runnable() {
						public void run() {
								try{
									ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");;
									if (stitchId!=null){
										 ExecutorService executorService111 = Executors.newFixedThreadPool(1);
										for (final String s1:stitchId){
											Future future111 = executorService111.submit(new Runnable() {
												public void run() {
													String label=ReadStitch.getATCNameByStitchID(s1);
													usefulMedecines.add(label,"Omim_onto + Sider");
	
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
	         }
	 			

	         ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);
	 /**********************************************************************************************************/
	 			/**Enter in Omim_onto **/
				 final List<Future<?>> futures3Bis  = new LinkedList<Future<?>>();
	             if (cuiMedecineOmimOnto!=null){
	 				for (final String s:cuiMedecineOmimOnto){
	 					Future future11 = executorService.submit(new Runnable() {
							public void run() {
									try{
										ArrayList<String> sideEffectStitchID=AccesSider.idMedocCauseEffetSecondaire(s);
					 		             if (sideEffectStitchID !=null){
					 						for (final String s1:sideEffectStitchID){
					 							Future future111 = executorService.submit(new Runnable() {
					 								public void run() {

					 									String label=ReadStitch.getATCNameByStitchID(s1);
					 									possibleOriginOfSideEffect.add(label,"Omim_Onto + sider");
					 								}});
					 							futures3Bis.add(future111);
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

	 				}
	             }
	          

	/***********************************************************************************************************/  
	      /**Enter in OrphaData **/
				final ExecutorService executorService4 = Executors.newFixedThreadPool(NUM_CORES);
				 List<Future<?>> futures4 = new LinkedList<Future<?>>();

	       	 if (diseaseIdFromOrpha != null){
	 				for (final String s:diseaseIdFromOrpha ){
	 					Future future11 = executorService.submit(new Runnable() {
							public void run() {
								try {
									if(!possibleDiseases.alreadySave(s)){
										/*the disease have not still been add in disease list (by Hpo.annotations*/
										String diseaseLabelFromHpoAnnotation = ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
										if(diseaseLabelFromHpoAnnotation != null){
												possibleDiseases.add(diseaseLabelFromHpoAnnotation, "OrphaData + Hpo Annotation");
										}
										else{
											
											for(String s3: AccesOrphaDataBase.GetDeseaseByDiseaseId(Integer.parseInt(s))){
												possibleDiseases.add(s3, "OrphaData");

											}
										}
										
									}else{
										
										/*the disease have already been add.so we update his origin list*/
										possibleDiseases.addOriginOnly(s,"OrphaData");
									}	
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
	 					});
	 					futures4.add(future11);
	 				}
	       	 }
	
	 /*************************************************************************************************************/	           
	       	/** We enter in OMIM */
	       	 
			ArrayList<String> diseaseIdFromOmim=ReadOmim.getNO("CS",userInput);
	             if (diseaseIdFromOmim!=null){
	            	 for (String s :diseaseIdFromOmim){
	            		 /*we test if this disease has already be add in HP Annotation or not*/
	            		 	if (!possibleDiseases.alreadySave(s)){
	            		 		String diseaseLabelFromHpoAnnot=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
	            		 		if (diseaseLabelFromHpoAnnot!= null){
	            		 		/*We found the disease in HPObo.Annotation so we add it there*/
									possibleDiseases.add(diseaseLabelFromHpoAnnot, "Omim + Hpo Annotations");
	            		 		}
	            		 		else{
	            		 			/*We dont find the disease in Hpo Annotations so we add it in OMIM directly*/
	            		 	       	ArrayList<String> diseaseFromOmim=ReadOmim.getTI("NO",s);
	            		 	       if (diseaseFromOmim!=null){
	            		 				for(String s1:diseaseFromOmim){
	            		 					possibleDiseases.add(s1, "Omim");
	            		 				}
	            		 			}

	            		 		}
								
	            		 	}
	            		 	/*the disease has already been add to disease list so we just refresh his origin list*/
	            		 	else{
	            		 		possibleDiseases.addOriginOnly(s, "OMIM");
								
	            		 	}		
	            	 }
	             }
	

	 /***********************************************************************************************/
	 			/**start waiting  then  ending threads*/
	 			
			for (Future<?> f : first_futures)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
			
			for (Future<?> f : futures3Bis)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }

			for (Future<?> f : futures3)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
       		future2.get();
       		
       		for (Future<?> f : futures4)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
       		
       		for (Future<?> f : futures_2Bis)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
       		

	executorService.shutdown();
	executorService2Bis.shutdown();
	executorService4.shutdown();




							/**closing HpoAnnotations*/
	ReadHpoAnnotations.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		/*************************************************************************************/
							/**for showing results in console**/
	public static void showResult(){


		System.out.println("*****useful Medecines****");
		System.out.println(possibleDiseases.toString());

		System.out.println("*****useful Medecines****");
		System.out.println(usefulMedecines.toString());
		
		System.out.println("*****origin of possible SideEffect****");
		System.out.println(possibleOriginOfSideEffect.toString());

		
	}

	public static void clearList(){
		 possibleDiseases.clear();
		 possibleOriginOfSideEffect.clear();
		 usefulMedecines.clear();
		  
	}

	
		
	public ResultsLists getPossibleDiseases(){
		return possibleDiseases;
	}
	
	public ResultsLists getPossibleOriginOfSideEffect(){
		return possibleOriginOfSideEffect;
	}
	
	public ResultsLists getUsefulMedecines(){
		return usefulMedecines;
	}


	public static void main(String[] args){
		String userInput="";

		 //String rep = "";

			System.out.println("Entrer votre requette" +"|"+" q pour sortir");
	      while (!"q".equals(userInput)) {

	                System.out.print(":> ");
	                userInput = sc.nextLine();
	               // userInput="cancer";
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

	
}

