package chattings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.google.firebase.cloud.FirestoreClient;

public class ChattingRoomTest {

	@SuppressWarnings("unchecked")
	@Test(timeout=20*1000) // 20s
	public void test() throws InterruptedException, ExecutionException, IOException {
		Main.initFirebase();

		final int originRoomCount = FirestoreClient.getFirestore().collection(Main.chatRoomCollectionPath).get().get()
				.getDocumentChanges().size();
		Person sb = new Person("SB");
		Person sj = new Person("SJ");

		String testingRoomName = "test";
		ChattingRoom newRoom = sb.makeChatRoom(testingRoomName);
		assertEquals(originRoomCount + 1, roomCount());

		sj.enter(newRoom);
		Map<String, Object> map = newRoom.ref.get().get().getData();
		List<String> people = (List<String>) map.get("people");
		assertTrue(people.contains(sb.name));
		assertTrue(people.contains(sj.name));
		assertEquals(newRoom.num, 2);
		assertEquals(newRoom.num, map.get("num"));
		assertEquals(newRoom.master,sb.name);
		assertEquals(newRoom.master,newRoom.map.get("master"));
		assertEquals(newRoom.name, testingRoomName);
		assertEquals(newRoom.name, map.get("name"));

		sj.delete(newRoom);
		assertEquals(originRoomCount + 1, roomCount()); // cannot delete

		sb.leave(newRoom);
		String newMaster = (String) newRoom.ref.get().get().getData().get("master");
		assertEquals(sj.name, newMaster);
		assertEquals(originRoomCount + 1, roomCount());

		sj.leave(newRoom);
		assertEquals(originRoomCount, roomCount());
	}

	public int roomCount() throws InterruptedException, ExecutionException {
		return FirestoreClient.getFirestore().collection(Main.chatRoomCollectionPath).get().get().getDocuments().size();
	}
}
