import java.util.ArrayList;


public class GlobalClass {

static ArrayList<String> provokedDiseases=new ArrayList<String>();	
static ArrayList<String> originOfSideEffect=new ArrayList<String>();	
ArrayList<String> usefulMedecine=new ArrayList<String>();	
ArrayList<String> suggestedEntry=new ArrayList<String>();	

	
	public static void main(String[] args){
		
		String userInput="";
		
		try {
			
			/*first we start with bases wich link with the entry*/

			ArrayList<String> cuiFromSider=AccesSider.cuiToCure(userInput);
			ArrayList<String> diseaseIdFromOrpha=AccesOrphaDataBase.GetDeseaseIdByClinicalSign(userInput);
			//ArrayList<String> diseaseNoFromOmim=fonctionOmim(userInput);
			//ArrayList<String> symptomIdFromHp.obo=fonctionHp.obo(userInput);

			ArrayList<String> diseaseNameFromOrpha=AccesOrphaDataBase.GetDeseaseByClinicalSign(userInput);

			ArrayList<String> originOfSideEffectFromSider=AccesSider.idMedocCauseEffetSecondaire(userInput);
			

			/*test if we find the each list is empty or not
			 * 
			 */

			if (cuiFromSider!= null){
				/*On fait tous le trajet suivant le mapping*/
			}

			if (diseaseIdFromOrpha != null){
				/*On fait tous le trajet suivant le mapping*/

			}
			
			/*if (symptomIdFromHp.obo !=null){
					On fait tous le trajet suivant le mapping

			}*/
			
			if (diseaseNameFromOrpha !=null){
				for (String s : diseaseNameFromOrpha){
					provokedDiseases.add(s);
				}
				/*On fait tous le trajet suivant le mapping*/

			}
			
			if (originOfSideEffectFromSider !=null){
				/*On fait le trajert suivant le mpping pour avoir les noms des medicaments*/

			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
