import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class classTest {

	public static void main(String[] args){
		ArrayList<String> idOriginOfSideEffectFromSider;
		ArrayList<String> list=new ArrayList<String>();;
		long startTime = System.nanoTime();
		

	/*	ExecutorService executorService11 = Executors.newSingleThreadExecutor();
        List<Future<?>> futures  = new LinkedList<Future<?>>();
			    For{
					Future future11 = executorService11.submit(new Runnable() {
		 				public void run() {
		 					......
		 				}
		 				futures.add(future11);

				}
				for (Future<?> f : futures)
					    {
					        try   { f.get(); }
					        catch (InterruptedException e) { } 
					        catch (ExecutionException   e) { }         
					    }
						
						
						executorService11.shutdown();    
	
		*/
		try {
			AccesSider.InitSider();
			idOriginOfSideEffectFromSider = AccesSider.idMedocCauseEffetSecondaire("Failure to thrive");
			ExecutorService executorService1 = Executors.newSingleThreadExecutor();
			Future future1 = executorService1.submit(new Runnable() {
			    public void run() {
			        System.out.println("Asynchronous task 2");
			        if (idOriginOfSideEffectFromSider !=null){
			        	
						ExecutorService executorService11 = Executors.newSingleThreadExecutor();
			            List<Future<?>> futures  = new LinkedList<Future<?>>();

						for(String s : idOriginOfSideEffectFromSider){
							Future future11 = executorService11.submit(new Runnable() {
							    public void run() {
							//String Atc_id=ReadStitch.stitchCompoundIDToATCID(s);
							String originOfSideEffectName=ReadStitch.getATCNameByStitchID(s);
							System.out.println(originOfSideEffectName);
							    }});
							futures.add(future11);
						}
							
						for (Future<?> f : futures)
					    {
					        try   { f.get(); }
					        catch (InterruptedException e) { } 
					        catch (ExecutionException   e) { }         
					    }
						
						
						executorService11.shutdown();    
						
						
					}
			    }
			});
			while(!future1.isDone()){
				

			}
			executorService1.shutdown();
			long endTime = System.nanoTime();
	   		double duration = (endTime - startTime)/Math.pow(10,9);
	   		System.out.println(duration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
