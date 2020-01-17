package chattings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class Person {
	public String name;

	public Person(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public void enter(ChattingRoom room) throws InterruptedException, ExecutionException {
		Map<String, Object> map = room.map;
		ArrayList<String> people = new ArrayList<>((List<String>) map.get("people"));

		if (people.contains(name)) {
			System.err.println(name + " already entered the room");
			return;
		}

		// DB
		people.add(this.name);
		map.put("people", people);
		map.computeIfPresent("num", (key, num) -> num = ((int) num) + 1);
		room.ref.set(map).get();

		// PC
		room.num++;
		room.people = people;
	}

	@SuppressWarnings("unchecked")
	public void leave(ChattingRoom room) throws InterruptedException, ExecutionException {
		Map<String, Object> map = room.map;
		Queue<String> people = new LinkedList<String>((List<String>) map.get("people"));

		if (!people.contains(name)) {
			System.err.println(name + " is not in the room");
			return;
		}

		if (room.master.equals(this.name)) {

			if (room.num <= 1) {
				this.delete(room);
				return;
			}

			people.remove(this.name);
			String newMaster = people.peek();
			map.computeIfPresent("num", (key, num) -> num = ((int) num) - 1);
			map.computeIfPresent("master", (key, master) -> master = (String) newMaster);
			room.master = newMaster;

		} else {

			people.remove(this.name);
			map.computeIfPresent("num", (key, num) -> num = ((int) num) - 1);

		}
		// DB
		map.put("people", (List<String>) people);
		room.ref.set(map).get();

		// PC
		room.num--;
		room.people = (List<String>) people;
	}

	public void delete(ChattingRoom room) throws InterruptedException, ExecutionException {
		Map<String, Object> map = room.map;
		String master = (String) map.get("master");

		if (!master.equals(this.name)) {
			System.err.println(name + " is not master of the room");
			return;
		}

		room.ref.delete().get();
		room = null;

	}

	public ChattingRoom makeChatRoom(String roomName) throws InterruptedException, ExecutionException {

		String[] people = { this.name };
		ChattingRoom newRoom = new ChattingRoom(this.name, roomName, 1, Arrays.asList(people));

		return newRoom;
	}
}
