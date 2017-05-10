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

private static ArrayList<String> suggestedEntry=new ArrayList<String>();

private static ArrayList<String> HpOboDiseaseIdAdd=new ArrayList<String>();

private static ResultsLists possibleDiseases=new  ResultsLists();
private static ResultsLists possibleOriginOfSideEffect=new  ResultsLists();
private static ResultsLists usefulMedecines=new  ResultsLists();
static Scanner sc=  new Scanner(System.in);
public static String userInput;
private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	public GlobalClass(){
		
	}

	

	public static void doSearch(String userInput){


		Long startTime=(long) (System.nanoTime());
		try {

			/*********inialize BD***********/

			//AccesSider.InitSider();
			ReadHpoAnnotations.connect();

					/***********/



			Long initListeDebut= (System.nanoTime());


			/*first we start with bases which link with the entry*/

			/* cui from sider not very useful i am going to explain it to you at the next meeting
			 * We can not find diseaese which have their cui in sider because  the only only link which was
			 * cui with omim_onto cn not be exploited (cause of MTHU).
			 */
			//ArrayList<String> cuiFromSider=AccesSider.cuiToCure(userInput);



			long initListeFin= System.nanoTime();
       		double initListe = (-initListeDebut + initListeFin)/Math.pow(10,9);
       		System.out.println("temps initialisation listes "+initListe);


			/*we create a thread which will do this task*/
			//ExecutorService executorService1 = Executors.newFixedThreadPool(NUM_CORES);
			final ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);


			ArrayList<String> idOriginOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);


			List<Future<?>> first_futures  = new LinkedList<Future<?>>();
		        if (idOriginOfSideEffectFromSider !=null){
						// ExecutorService executorService1 = Executors.newFixedThreadPool(NUM_CORES);
							    for(final String s : idOriginOfSideEffectFromSider){
									Future future11 = executorService.submit(new Runnable() {
						 				public void run() {
						 					String originOfSideEffectName=ReadStitch.getATCNameByStitchID(s);
											possibleOriginOfSideEffect.add(originOfSideEffectName,"Sider");
						 				 }});
						 				first_futures.add(future11);
	
								}
				}
			  


		final ArrayList<String> stitchIdFromSider=AccesSider.getStitchIDByConceptName(userInput);
		final ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name",userInput);


	       		Future future2 = executorService.submit(new Runnable() {
				    public void run() {
				    	if (stitchIdFromSider !=null){
							for (String s:stitchIdFromSider){
								String drugName=ReadStitch.getATCNameByStitchID(s);
								usefulMedecines.add(drugName,"Sider");

							}
				    	}
				    	

						


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
				
				    







			/*we take cui from Omim_onto , take them to sider in order to find
			 * medine
			 * and  sideEffect
			 */


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

			     long firstTime = System.nanoTime();


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
	             long secondTimeT = System.nanoTime();
		       	double durationT = (firstTime - secondTimeT)/Math.pow(10,9);
	       		System.out.println("duration T "+durationT);

				final ExecutorService executorService4 = Executors.newFixedThreadPool(NUM_CORES);
				 List<Future<?>> futures4 = new LinkedList<Future<?>>();

	       	 if (diseaseIdFromOrpha != null){
	 				for (final String s:diseaseIdFromOrpha ){
	 					Future future11 = executorService.submit(new Runnable() {
							public void run() {
								try {
									if(/*!HpOboDiseaseIdAdd.contains(s)*/!possibleDiseases.alreadySave(s)){
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
										
										/*the disease have already been add.so we updte his origin list*/
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


	             /*we maybe can also let this one down.
	              * In fact when we find a diseaseIdFromOmim we in the same time find a  diseaseNameFromOmim.
	              * This diseaseNameFromOmim is added to Disease List.
	              * Here we add the name link to the diseaseIdFromOmim in HpAnnotations but this one might be the same as
	              * diseaseNameFromOmim.
	              * We have to check!
	              */
	       	
			ArrayList<String> diseaseIdFromOmim=ReadOmim.getNO("CS",userInput);

	             if (diseaseIdFromOmim!=null){
	            	 for (String s :diseaseIdFromOmim){
	            		 /*we test if this disease has already be add in HP Annotation or not*/
	            		 	if (/*!HpOboDiseaseIdAdd.contains(s)*/!possibleDiseases.alreadySave(s)){
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
	
	 			long attenteDebutfirst = System.nanoTime();


			for (Future<?> f : first_futures)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
			long attenteFinfirst = System.nanoTime();
       		double durFirst = (attenteDebutfirst- attenteFinfirst)/Math.pow(10,9);
       		System.out.println("durFirst "+durFirst);

			long attenteDebutBis = System.nanoTime();
			for (Future<?> f : futures3Bis)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }

			long attenteFinBis = System.nanoTime();
       		double durBis = (attenteDebutBis- attenteFinBis)/Math.pow(10,9);

			long attenteDebut = System.nanoTime();
			for (Future<?> f : futures3)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }

			long attenteFin = System.nanoTime();
       		double dur = (attenteDebut - attenteFin)/Math.pow(10,9);
       		System.out.println("dur future 3 Bis "+durBis);
       		System.out.println("dur future 3 "+dur);
       		long attentefuture2 = System.nanoTime();
       		future2.get();
       		long attentefuture2Fin = System.nanoTime();
       		double durFuture2 = (attentefuture2 - attentefuture2Fin)/Math.pow(10,9);

       		long attentefuture4 = System.nanoTime();
       		for (Future<?> f : futures4)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
       		long attentefuture4Fin = System.nanoTime();
       		
       		long attentefuture2Bis = System.nanoTime();
       		for (Future<?> f : futures_2Bis)
		    {
		        try   { f.get(); }
		        catch (InterruptedException e) { }
		        catch (ExecutionException   e) { }
		    }
       		long attentefuture2BisFin = System.nanoTime();
       		double durFuture2Bis = (attentefuture2Bis- attentefuture2BisFin)/Math.pow(10,9);

       		double durFuture4 = (attentefuture4 - attentefuture4Fin)/Math.pow(10,9);

       		System.out.println("future 2 :"+durFuture2);
       		System.out.println("future 4 :"+durFuture4);
       		System.out.println("future 2Bis :"+durFuture2Bis);

       		System.out.println("attente fin totale : "+(attentefuture2BisFin-attenteDebutfirst)/Math.pow(10,9));

	executorService.shutdown();
	executorService2Bis.shutdown();
	executorService4.shutdown();





	//AccesSider.closeSider();
	ReadHpoAnnotations.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void showResult(){


		System.out.println("*****useful Medecines****");
		System.out.println(possibleDiseases.toString());

		

		System.out.println("*****useful Medecines****");
		System.out.println(usefulMedecines.toString());

		
		System.out.println("*****origin of possible SideEffect****");
		System.out.println(possibleOriginOfSideEffect.toString());


		if(suggestedEntry.size()!=0){
			System.out.println("*****suggestions for entry****");
			for(String s:suggestedEntry){
				System.out.println(s);
			}
			System.out.println("\n");

		}


	}

	public static void clearList(){
		 possibleDiseases.clear();
		 possibleOriginOfSideEffect.clear();
		 usefulMedecines.clear();
		  suggestedEntry.clear();
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

