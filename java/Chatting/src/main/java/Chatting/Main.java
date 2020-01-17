package Chatting;

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

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		initFirebase();

// 0. print existing chtting rooms
//		printChattingRooms();

// 1. new chatting room
		Person sb = new Person("sb");
		ChattingRoom newRoom = sb.makeChatRoom();

		
// 2. person enter the room
		Person sj = new Person("SJ");
		sj.enter(newRoom);
		printChattingRooms();


// 3. person leave the room
		sj.leave(newRoom);
		printChattingRooms();

// 4. person delete the room
		sj.delete(newRoom);
		printChattingRooms();

		sb.delete(newRoom);
		printChattingRooms();
		

		/*
		*/
	}

//	private static ChattingRoom selectOneRoom() throws InterruptedException, ExecutionException {
//		ChattingRoom room =  new ChattingRoom(geyQueryDocuments("chatting_rooms").get(0));
//		return room;
//	}

	private static void printChattingRooms() throws InterruptedException, ExecutionException {
		List<QueryDocumentSnapshot> children = geyQueryDocuments("chatting_rooms");

		for (QueryDocumentSnapshot qsh : children) {
//			DocumentSnapshot sh = qsh.getReference().get().get();
//			System.out.println("sh: " + sh.getId());

			ChattingRoom room = new ChattingRoom(qsh);
			System.out.println(room);
		}

		System.out.println("------------");
	}

	private static List<QueryDocumentSnapshot> geyQueryDocuments(String collectionPath)
			throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		List<QueryDocumentSnapshot> children = db.collection("chatting_rooms").get().get().getDocuments();
		return children;
	}

	private static void initFirebase() throws IOException {

		FileInputStream serviceAccount = new FileInputStream(adminSDKPath);

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl(databaseURL).build();

		FirebaseApp.initializeApp(options);

	}

}
