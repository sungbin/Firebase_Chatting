package Chatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
			System.out.println(name + " already entered the room");
			return;
		}

		// DB
		people.add(this.name);
		map.put("people", people);
		map.computeIfPresent("num", (key, val) -> val = ((int) val) + 1);
		room.ref.set(map).get();

		// PC
		room.num++;
		room.people = people;
	}

	@SuppressWarnings("unchecked")
	public void leave(ChattingRoom room) throws InterruptedException, ExecutionException {
		Map<String, Object> map = room.map;
		List<String> people = (List<String>) map.get("people");

		if (!people.contains(name)) {
			System.out.println(name + " cannot leave the room");
			return;
		}

		// DB
		people.remove(this.name);
		map.computeIfPresent("num", (key, val) -> val = ((int) val) - 1);
		room.ref.set(map).get();

		// PC
		room.num--;
		room.people = people;
	}

	public void delete(ChattingRoom room) throws InterruptedException, ExecutionException {
		Map<String, Object> map = room.map;
		String master = (String) map.get("master");

		if (!master.equals(this.name)) {
			System.out.println(name + " is not master of the room");
			return;
		}

		room.ref.delete().get();

	}

	public ChattingRoom makeChatRoom() throws InterruptedException, ExecutionException {

		String[] people = { this.name };
		ChattingRoom newRoom = new ChattingRoom(this.name, "second", 1, Arrays.asList(people));

		return newRoom;
	}
}
