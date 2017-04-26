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

public static HashMap<String,Integer> provokedDiseases=new HashMap<String,Integer>();
public static HashMap<String,Integer> originOfSideEffect=new  HashMap<String,Integer>();
public static HashMap<String,Integer>  usefulMedecine=new HashMap<String,Integer>();
public static ArrayList<String> suggestedEntry=new ArrayList<String>();
public static ResultsLists possibleDiseases=new  ResultsLists();
public static ResultsLists possibleOriginOfSideEffect=new  ResultsLists();
public static ResultsLists usefulMedecines=new  ResultsLists();




	static Scanner sc=  new Scanner(System.in);
	public static String userInput;
	private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	public static void doSearch(String userInput){
	ArrayList<String> a=new ArrayList<String>();


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
			ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);


			ArrayList<String> idOriginOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);


			List<Future<?>> first_futures  = new LinkedList<Future<?>>();
		//Future future1 = executorService.submit(new Runnable() {
			  //  public void run() {
			     //   System.out.println("Asynchronous task 2");
			        if (idOriginOfSideEffectFromSider !=null){
							// ExecutorService executorService1 = Executors.newFixedThreadPool(NUM_CORES);
								    for(String s : idOriginOfSideEffectFromSider){
										Future future11 = executorService.submit(new Runnable() {
							 				public void run() {
							 					String originOfSideEffectName=ReadStitch.getATCNameByStitchID(s);
												addOriginSideEffect(originOfSideEffectName);
												possibleOriginOfSideEffect.add(originOfSideEffectName,"Sider");
							 				 }});
							 				first_futures.add(future11);

									}
					}
			   // }
			//});



					ArrayList<String> stitchIdFromSider=AccesSider.getStitchIDByConceptName(userInput);
					ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name",userInput);
					ArrayList<String> diseaseNameFromOrpha=AccesOrphaDataBase.GetDeseaseByClinicalSign(userInput);


	       	//	ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);
	       		Future future2 = executorService.submit(new Runnable() {
				    public void run() {
				    	if (stitchIdFromSider !=null){
							for (String s:stitchIdFromSider){
								String drugName=ReadStitch.getATCNameByStitchID(s);
								addMedecine(drugName);
								//usefulMedecines.add(drugName,"Sider");

							}
				    	}
				    	if (symptomIdFromHpObo !=null){
							for (String s : symptomIdFromHpObo){
								ArrayList<String> diseaseLabel;
								try {
									diseaseLabel = ReadHpoAnnotations.getDiseaseLabelBySignId(s);
									for(String s1:diseaseLabel){
										addDisease(s1);
										possibleDiseases.add(s1, "Hpo Annotation");
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}


						if (diseaseNameFromOrpha !=null){
							for (String s : diseaseNameFromOrpha){
								addDisease(s);
								possibleDiseases.add(s, "OrphaData");

							}

						}


				    }
				});






			/*we take cui from Omim_onto , take them to sider in order to find
			 * medine
			 * and  sideEffect
			 */

       		/*ExecutorService executorService3 = Executors.newFixedThreadPool(NUM_CORES);;
       		executorService3.submit(new Runnable() {
			  public void run() {*/

				ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI(userInput);

			   //  ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);
				 List<Future<?>> futures3  = new LinkedList<Future<?>>();
	             if (cuiMedecineOmimOnto!=null){
						for (String s:cuiMedecineOmimOnto){
							Future future11 = executorService.submit(new Runnable() {
							public void run() {
									try{
										ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");;
										if (stitchId!=null){
											 ExecutorService executorService111 = Executors.newFixedThreadPool(1);

											for (String s1:stitchId){

												Future future111 = executorService111.submit(new Runnable() {
													public void run() {
														String label=ReadStitch.getATCNameByStitchID(s1);
														//addMedecine(label);
														usefulMedecines.add(label,"Omim_onto + Sider");

												}});
												//futures3.add(future111);
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



	       		//System.out.println("duration T "+durationT1);
	             }

			    //}
			//});
	 			ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);

			     long firstTime = System.nanoTime();

			    // ExecutorService executorService3Bis = Executors.newFixedThreadPool(NUM_CORES);

				 List<Future<?>> futures3Bis  = new LinkedList<Future<?>>();
	             if (cuiMedecineOmimOnto!=null){
	 				for (String s:cuiMedecineOmimOnto){
	 					Future future11 = executorService.submit(new Runnable() {
							public void run() {
									try{
										ArrayList<String> sideEffectStitchID=AccesSider.idMedocCauseEffetSecondaire(s);
					 		             if (sideEffectStitchID !=null){
					 						for (String s1:sideEffectStitchID){
					 							Future future111 = executorService.submit(new Runnable() {
					 								public void run() {

					 									String label=ReadStitch.getATCNameByStitchID(s1);
					 									addOriginSideEffect(label);
					 									possibleOriginOfSideEffect.add(label,"Omim_Onto + sider");
					 									System.out.println("hilyigjhkkhlguy"+possibleOriginOfSideEffect);
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


				 List<Future<?>> futures4 = new LinkedList<Future<?>>();

	       	 if (diseaseIdFromOrpha != null){
	 				for (String s:diseaseIdFromOrpha ){
	 					Future future11 = executorService.submit(new Runnable() {
							public void run() {
								try {
									ArrayList<String> diseaseLabelFromHpoAnnotation = ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
									for (String s2 : diseaseLabelFromHpoAnnotation){
				 						addDisease(s2);
										possibleDiseases.add(s2, "OrphaData + Hpo Annotation");

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
							ArrayList<String> diseaseLabelFromHpoAnnot=ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
							for(String s1: diseaseLabelFromHpoAnnot){
								addDisease(s1);
								possibleDiseases.add(s1, "Omim + Hpo Annotations");
							}
	            	 }
	             }

	 	       	ArrayList<String> diseaseFromOmim=ReadOmim.getTI("CS",userInput);


	             if (diseaseFromOmim!=null){
	 				for(String s:diseaseFromOmim){
	 					addDisease(s);
	 					possibleDiseases.add(s, "Omim");
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
       		double durFuture4 = (attentefuture4 - attentefuture4Fin)/Math.pow(10,9);

       		System.out.println("future 2 :"+durFuture2);
       		System.out.println("future 4 :"+durFuture4);
       		System.out.println("attente fin totale : "+(attentefuture4Fin-attenteDebutfirst)/Math.pow(10,9));
	//while ( !future2.isDone()   || !future4.isDone() || !future1.isDone()){};

	executorService.shutdown();

	//executorService1.shutdown();
	/*executorService2.shutdown();
	executorService3.shutdown(); */
	//executorService4.shutdown();



	//AccesSider.closeSider();
	ReadHpoAnnotations.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void showResult(){


		/*if (provokedDiseases.size()!=0){
			System.out.println("*****provoked diseases****");
			for(String s:provokedDiseases.keySet()){
				System.out.println(s +" : "+provokedDiseases.get(s));
			}
			System.out.println("\n");
		}*/

		System.out.println("*****useful Medecines****");
		System.out.println(possibleDiseases.toString());

		/*if(usefulMedecine.size()!=0){
			System.out.println("*****useful Medecines****");
			for(String s:usefulMedecine.keySet()){
				System.out.println(s +" : "+usefulMedecine.get(s));

			}
			System.out.println("\n");
		}*/

		System.out.println("*****useful Medecines****");
		System.out.println(usefulMedecines.toString());

		/*if (originOfSideEffect.size()!=0){
			System.out.println("*****origin of possible SideEffect****");
			for(String s:originOfSideEffect.keySet()){
				System.out.println(s +" : "+originOfSideEffect.get(s));
			}
			System.out.println("\n");

		}*/
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