package chattings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Main {
	private static final String databaseURL = "https://chat-35f14.firebaseio.com";
	private static final String adminSDKPath = "/Users/imseongbin/chat-35f14-firebase-adminsdk-wk1y0-265d57edbd.json";
	public static final String chatRoomCollectionPath = "chatting_rooms";

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		initFirebase();

	}

	private static void printChattingRooms() throws InterruptedException, ExecutionException {
		List<QueryDocumentSnapshot> children = geyQueryDocuments(chatRoomCollectionPath);

		for (QueryDocumentSnapshot qsh : children) {
//			DocumentSnapshot sh = qsh.getReference().get().get();
//			System.out.println("sh: " + sh.getId());F

			ChattingRoom room = new ChattingRoom(qsh);
			System.out.println(room);
		}

		System.out.println("------------");
	}

	private static List<QueryDocumentSnapshot> geyQueryDocuments(String collectionPath)
			throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		List<QueryDocumentSnapshot> children = db.collection(chatRoomCollectionPath).get().get().getDocuments();
		return children;
	}

	public static void initFirebase() throws IOException {

		FileInputStream serviceAccount = new FileInputStream(adminSDKPath);

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl(databaseURL).build();

		FirebaseApp.initializeApp(options);

	}

}
