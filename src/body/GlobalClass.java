package body;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class GlobalClass {

//private static ArrayList<String> suggestedEntry = new ArrayList<String>(); //list of suggestions
private static ResultsLists possibleDiseases = new ResultsLists();
private static ResultsLists possibleOriginSideEffect = new ResultsLists();
private static ResultsLists usefulMedicines = new ResultsLists();
private static Scanner sc = new Scanner(System.in);
public static String userInput;
private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	public GlobalClass() {
		
	}

	public static void doSearch(String userInput) {
		
		try {
			/***********************************************************************************/
			/** inialize BD **/
			
			ReadHpoAnnotations.connect();
			
			/***********************************************************************************/
			/** we enter in Sider to search the origin of the side effects **/
			
			ArrayList<String> idMedicineSideEffect = AccesSider.idMedicineSideEffect(userInput);
			
			final ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);
			List<Future<?>> listfutures = new LinkedList<Future<?>>();
			
			if (idMedicineSideEffect != null) {
				for (String s : idMedicineSideEffect) {
					Future<?> future = executorService.submit(
							new Runnable() {
								public void run() {
									String nameMedecineSideEffect = ReadStitch.getATCNameByStitchID(s);
									possibleOriginSideEffect.add(nameMedecineSideEffect,"Sider + Stitch + ATC");
								}
							});
					listfutures.add(future);
				}
			}

			/***********************************************************************************/
			/** we enter in Sider to search medicine */
			
			final ArrayList<String> idStitchFromSider = AccesSider.getStitchIDByConceptName(userInput);
			final ArrayList<String> idSymptomHpObo = ReadHpObo.getId("name",userInput);
			
			Future<?> future2 = executorService.submit(
				new Runnable() {
					public void run() {
						if (idStitchFromSider != null) {
							for (String s : idStitchFromSider){
								String drugName = ReadStitch.getATCNameByStitchID(s);
								usefulMedicines.add(drugName,"Sider");
							}
				    	}
					}
				});	    	

			/***********************************************************************************/			
			/**  we enter in hpo.obo*/
	       		
			final ExecutorService executorService2 = Executors.newFixedThreadPool(NUM_CORES);
			List<Future<?>> listfutures2 = new LinkedList<Future<?>>();
			
			if (idSymptomHpObo != null) {
				for (final String s : idSymptomHpObo){
					Future<?> future3 = executorService2.submit(
							new Runnable() {
								public void run() {
									ArrayList<String> diseaseLabel;
									try {
										diseaseLabel = ReadHpoAnnotations.getDiseaseLabelBySignId(s);
										for (String s : diseaseLabel) {
											String label = ReadHpoAnnotations.getDiseaseIdByLabel(s);
											possibleDiseases.saveId(label, s);
											possibleDiseases.add(s, "Hp.obo + Hpo Annotation");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							listfutures2.add(future3);
			       		}
				    }
			
			/***********************************************************************************/
			/** we take CUI from Omim_onto and give them to Sider in order to find medicine and side effect **/
			
			ArrayList<String> cuiMedicineOmimOnto = ReadOmimOnto.SymptomToCUI(userInput);
			List<Future<?>> listfutures3 = new LinkedList<Future<?>>();
			
			if (cuiMedicineOmimOnto != null) {
				for (String s : cuiMedicineOmimOnto) {
					Future<?> future4 = executorService.submit(
						new Runnable() {
							public void run() {
								try {
									ArrayList<String> stitchId = AccesSider.getStitchIdByCUI(s, "name");
									if (stitchId != null) {
										ExecutorService executorService111 = Executors.newFixedThreadPool(1);
										for (final String s : stitchId) {
											Future<?> future5 = executorService111.submit(
												new Runnable() {
													public void run() {
														String label = ReadStitch.getATCNameByStitchID(s);
														usefulMedicines.add(label,"Omim_onto + Sider");
													}
												});
										}
									}
								} catch(Exception ex) {
									System.err.println("autre erreur");
									while (ex != null) {
										System.err.println("Error msg: "+ex.getMessage());
										ex.printStackTrace();
									}
								}
							}
						});
					listfutures3.add(future4);
				}
			}

			/***********************************************************************************/
			/** we enter in Omim_onto **/
			
			ArrayList<String> diseaseIdOrpha = AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);
			final List<Future<?>> listfutures4 = new LinkedList<Future<?>>();
			
	        if (cuiMedicineOmimOnto != null) {
	 			for (String s : cuiMedicineOmimOnto) {
	 				Future<?> future6 = executorService.submit(
	 					new Runnable() {
	 						public void run() {
	 							try {
	 								ArrayList<String> sideEffectStitchID = AccesSider.idMedicineSideEffect(s);
	 								if (sideEffectStitchID != null) {
	 									for (final String s : sideEffectStitchID) {
	 										Future<?> future7 = executorService.submit(
	 											new Runnable() {
	 												public void run() {
	 													String label=ReadStitch.getATCNameByStitchID(s);
	 													possibleOriginSideEffect.add(label,"Omim_Onto + sider");
	 												}
	 											});
	 									listfutures4.add(future7);
	 									}
	 								}
	 							} catch(Exception ex) {
	 								System.err.println("Another error");
	 								while (ex != null) {
		 								System.err.println("Error msg: "+ex.getMessage());
		 								ex.printStackTrace();
	 								}
	 							}
	 						}
	 					}
	 				);
	 			}
	        }
	          
	        /***********************************************************************************/
	        /** we enter in OrphaData **/
	        
	        final ExecutorService executorService3 = Executors.newFixedThreadPool(NUM_CORES);
	        List<Future<?>> listfutures5 = new LinkedList<Future<?>>();

	        if (diseaseIdOrpha != null) {
	 			for (final String s : diseaseIdOrpha) {
	 				Future<?> future8 = executorService.submit(
	 					new Runnable() {
	 						public void run() {
	 							try {
	 								if (!possibleDiseases.alreadySave(s)) {
	 									
	 									/* the disease have not still been add in disease list (by Hpo.annotations) */
	 									
	 									String diseaseLabelHpoAnnotation = ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
	 									if (diseaseLabelHpoAnnotation != null) {
											possibleDiseases.add(diseaseLabelHpoAnnotation, "OrphaData + Hpo Annotation");
	 									} else {
	 										for (String s : AccesOrphaDataBase.GetDeseaseByDiseaseId(Integer.parseInt(s))) {
	 											possibleDiseases.add(s, "OrphaData");
	 										}
	 									}
	 								} else {
	 									
	 									/* the disease have already been add.so we update his origin list */
	 									
	 									possibleDiseases.addOriginOnly(s,"OrphaData");
	 								}	
	 							} catch (Exception e) {
	 								e.printStackTrace();
	 							}
	 						}
	 					});
	 				listfutures5.add(future8);
	 			}
	       	 }
	
	        /***********************************************************************************/	           
	       	/** we enter in Omim */
	       	 
			ArrayList<String> diseaseIdOmim = ReadOmim.getNO("CS",userInput);
			if (diseaseIdOmim != null) {
	            for (String s : diseaseIdOmim) {
	            		 
	            	/* we test if this disease has already be add in HP Annotation or not */
	            		 
	            	if (!possibleDiseases.alreadySave(s)) {
	            		String diseaseLabelFromHpoAnnot = ReadHpoAnnotations.getDiseaseLabelByDiseaseId(s);
	            		if (diseaseLabelFromHpoAnnot!= null) {
	            				 
	            			/* we found the disease in HPObo.Annotation so we add it there */
	            				 
	            			possibleDiseases.add(diseaseLabelFromHpoAnnot, "Omim + Hpo Annotations");
	            		} else {
	            				 
	            			/* we don't find the disease in Hpo Annotations so we add it in Omim directly */
	            				 
	            			ArrayList<String> diseaseFromOmim = ReadOmim.getTI("NO",s);
	            			if (diseaseFromOmim != null) {
	            				for (String s1:diseaseFromOmim) {
	            					possibleDiseases.add(s1, "Omim");
	            				}
	            			}
	            		}
	            	} else {
	            			 
	            		/* the disease has already been add to disease list so we just refresh his origin list */
	            			 
	            		possibleDiseases.addOriginOnly(s, "OMIM");
	            	}		
	            }
			}
	
			/***********************************************************************************/
			/** start waiting then ending threads **/
	 			
			for (Future<?> f : listfutures) {
				try { f.get(); }
				catch (InterruptedException e) { }
				catch (ExecutionException e) { }
			}
			
			for (Future<?> f : listfutures2) {
				try { f.get(); }
				catch (InterruptedException e) { }
				catch (ExecutionException   e) { }
			}
			
			for (Future<?> f : listfutures3) {
				try { f.get(); }
				catch (InterruptedException e) { }
				catch (ExecutionException   e) { }
			}
			future2.get();
			
			for (Future<?> f : listfutures4) {
				try { f.get(); }
				catch (InterruptedException e) { }
				catch (ExecutionException   e) { }
			}

			for (Future<?> f : listfutures5) {
				try { f.get(); }
				catch (InterruptedException e) { }
				catch (ExecutionException   e) { }
			}
       		
			executorService.shutdown();
			executorService2.shutdown();
			executorService3.shutdown();

			/***********************************************************************************/
			/** closing HpoAnnotations **/
	             
			ReadHpoAnnotations.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***********************************************************************************/
	/** to show results in console **/
	
	public static void showResult() {
		System.out.println("*****useful Medicines****");
		System.out.println(possibleDiseases.toString());

		System.out.println("*****useful Medicines****");
		System.out.println(usefulMedicines.toString());
		
		System.out.println("*****origin of possible SideEffect****");
		System.out.println(possibleOriginSideEffect.toString());
	}

	public static void clearList() {
		 possibleDiseases.clear();
		 possibleOriginSideEffect.clear();
		 usefulMedicines.clear();
	}

	public ResultsLists getPossibleDiseases() {
		return possibleDiseases;
	}
	
	public ResultsLists getpossibleOriginSideEffect() {
		return possibleOriginSideEffect;
	}
	
	public ResultsLists getUsefulMedecines() {
		return usefulMedicines;
	}
	
	public static void main(String[] args) {
		String userInput = "";
		System.out.println("Entrer votre requette" +"|"+" q pour sortir");
		while (!"q".equals(userInput)) {
			System.out.print(":> ");
			userInput = sc.nextLine();
			try {
				long startTime = System.nanoTime();
				doSearch(userInput);
				showResult();
				clearList();
				long endTime = System.nanoTime();
				double duration = (endTime - startTime)/Math.pow(10,9);
				System.out.println("\n Time needed : "+duration);
				System.out.println("Entrer votre requette" +"|"+" q pour sortir");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
}
