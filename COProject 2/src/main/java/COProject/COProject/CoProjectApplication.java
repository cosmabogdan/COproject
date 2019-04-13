package COProject.COProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.cloud.FirestoreClient;

import COProject.COProject.cpu.*;

@SpringBootApplication
public class CoProjectApplication {
    
    private static CoProjectApplication getScore = new CoProjectApplication();
    private static long totalTimeForCpuTests;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CoProjectApplication.class, args);
		//System.out.println("Hello!");
        
        /* Link with firebase account */
        
        System.out.println("B");
        FileInputStream serviceAccount = new FileInputStream("ServiceAccountKey.json");
        System.out.println("A");
        FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();
        
        /* Initialize app */
        
        FirebaseApp.initializeApp(options);
        
        /* Create db */
        
        Firestore db = FirestoreClient.getFirestore();
        
        
        
        
        /* Start the actual testing ... */
        
        
        /* Test how many cores */
        
        System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        
        /* Start time for calculating digits of Pi program (first test) */
        
        long start1 = System.currentTimeMillis();
        Pi.pi(100000);
        long end1 = System.currentTimeMillis();
        long timeForFirstCpuTest = end1 - start1;
        System.out.println("Calculating 1000000 digits of pi takes : " + timeForFirstCpuTest + "ms");
        
        /* Start time for calculating Thread Eater (second test) */
        
        long start2 = System.currentTimeMillis();
        ThreadEater eater = new ThreadEater();
        
        try{
            eater.startEating();
        } catch (InterruptedException e) {
            System.out.println("Exception caught!");
        }
        
        long end2 = System.currentTimeMillis();
        long timeForSecondCpuTest = end2 - start2;
        System.out.println("Calculating Thread Eater takes : " + timeForSecondCpuTest + "ms");
        
        
        /* Calculate total time for cpu testing */
        
        totalTimeForCpuTests = timeForFirstCpuTest + timeForSecondCpuTest;
        System.out.println("Total time for CPU test: " + totalTimeForCpuTests + "ms");
        
        
        /*  End of testing cpu */
        
        
        
        /* Initialize the HashMap containing the calculated score in order to update the db with this */
        
        HashMap<String, String> score = getScore.getScoreFromHTTP();
        
        /* Create the db with the given fields */
        
        ApiFuture<WriteResult> future = db.collection("PC").document("cpu")
        .set(score);
        
        try {
            System.out.println("Successfully updated at: " + future.get().getUpdateTime());
        } catch (Exception e) {
            System.out.println("Interrupted exception! Verify your internet maybe!");
        }
	}
    
    
    public String getTotalCpuTime() {
        return totalTimeForCpuTests + "";
    }
    
    /* !!! THE FOLLOWING FUNCTION NEEDS A REVIEW !!! */
    
    public HashMap<String, String> getScoreFromHTTP() throws IOException {
        
        String score = "score";
        String value = getTotalCpuTime();
        HashMap<String, String> theThings = new HashMap<>();
        //do things to get the Map built
        theThings.put(score, value); //or something similar
        
        System.out.println("Hashmap : " + theThings);
        
        return theThings;
        
    }
}
