import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class GlobalClass {

static HashMap<String,Integer> provokedDiseases=new HashMap<String,Integer>();	
static ArrayList<String> originOfSideEffect=new ArrayList<String>();	
static HashMap<String,Integer>  usefulMedecine=new HashMap<String,Integer>();	
static ArrayList<String> suggestedEntry=new ArrayList<String>();


	static Scanner sc=  new Scanner(System.in);;

	public static void doSearch(String userInput){
		try {
			
			/*first we start with bases which link with the entry*/
			ArrayList<String> stitchIdFromSider=AccesSider.getStitchIDByConceptName(userInput);
			ArrayList<String> cuiFromSider=AccesSider.cuiToCure(userInput);
			ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);
			ArrayList<String> diseaseFromOmim=ReadOmim.getTI("CS",userInput);
			ArrayList<String> symptomIdFromHpObo=ReadHpObo.getId("name",userInput);
			ArrayList<String> diseaseNameFromOrpha=AccesOrphaDataBase.GetDeseaseByClinicalSign(userInput);
			ArrayList<String> idOriginOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);
			
			ArrayList<String> cuiMedecineOmimOnto=ReadOmimOnto.SymptomToCUI(userInput);
			
			

			/*test if we find the each list is empty or not
			 * 
			 */
			if (cuiFromSider==null && diseaseIdFromOrpha==null && diseaseFromOmim==null && symptomIdFromHpObo==null){
				/*make a suggestion*/
			}
			
			
			
			if (stitchIdFromSider !=null){
				for (String s:stitchIdFromSider){
					String Atc_id=ReadStitch.stitchCompoundIDToATCID(s);
					String drugName=ReadATC.getLabel(Atc_id);
					addMedecine(drugName);
				}
			}
			
			/*we take cui from Omim_onto , take them to sider in order to find 
			 * medine 
			 * and later sideEffect
			 */
			if (cuiMedecineOmimOnto!=null){
				for (String s:cuiMedecineOmimOnto){
					ArrayList<String> stitchId=AccesSider.getStitchIdByCUI(s, "name");
					if (stitchId!=null){
						for (String s1:stitchId){
							String label=ReadStitch.getATCNameByStitchID(s1);
							addMedecine(label);
						}
					}
					
				}
			}

			
			if (cuiFromSider!= null){
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
			}

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
			
			if (idOriginOfSideEffectFromSider !=null){
				for(String s : idOriginOfSideEffectFromSider){
					String Atc_id=ReadStitch.stitchCompoundIDToATCID(s);
					String originOfSideEffectName=ReadATC.getLabel(Atc_id);
					originOfSideEffect.add(originOfSideEffectName);
					/*ADD TO ORIGIN OF SIDE EFFECT LIST*/
				}
			}
			
			if (diseaseFromOmim!=null){
				for(String s:diseaseFromOmim){
					addDisease(s);
				}
			}
			
			
			
	
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
			for(String s:originOfSideEffect){
				System.out.println(s);
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
	
	public static void addDisease(String s){
		if (!provokedDiseases.containsKey(s)){
			provokedDiseases.put(s,1);
		}
		else{
			int oldNum=provokedDiseases.get(s);
			provokedDiseases.put(s,oldNum+1);

		}
		
	}
	
	public static void addMedecine(String s){
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
	public static void main(String[] args){
		String userInput="";
		
		 //String rep = "";
		
			System.out.println("Entrer votre requette" +"|"+" q pour sortir");
	        while (!"q".equals(userInput)) {
	            
	                System.out.print(":> ");
	                userInput = sc.nextLine();
	                try{
	                	doSearch(userInput);
		                showResult();
		               clearList();
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
