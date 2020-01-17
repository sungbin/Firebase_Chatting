package chattings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

public class ChattingRoom {
	public String master;
	public String name;
	public long num;
	public List<String> people;
	public Map<String, Object> map = null;
	public DocumentReference ref = null;

	@Override
	public String toString() {
		return "ChattingRoom [master=" + master + ", name=" + name + ", num=" + num + ", people=" + people + "]";
	}

	/**
	 * make new chatroom in firestore
	 */
	public ChattingRoom(String master, String name, int num, List<String> people)
			throws InterruptedException, ExecutionException {

		this.map = new HashMap<String, Object>();
		this.master = master;
		this.name = name;
		this.num = num;
		this.people = people;
		map.put("master", master);
		map.put("name", name);
		map.put("num", num);
		map.put("people", people);

		this.ref = FirestoreClient.getFirestore().collection(Main.chatRoomCollectionPath).document();
		this.ref.set(map).get();
	}

	public ChattingRoom(QueryDocumentSnapshot snapshot) throws InterruptedException, ExecutionException {
		this(snapshot.getReference());
	}

	@SuppressWarnings("unchecked")
	public ChattingRoom(DocumentReference ref) throws InterruptedException, ExecutionException {

		Map<String, Object> map = ref.get().get().getData();
		master = (String) map.get("master");
		name = (String) map.get("name");
		num = (long) map.get("num");
		people = (List<String>) map.get("people");
		this.map = map;
		this.ref = ref;
	}
}
