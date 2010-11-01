package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;

public class Request extends AbstractSerializer {
	
	private String notes;
	private float hours;
	private int project_id;
	private int task_id;
	private String spent_at;
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int projectId) {
		project_id = projectId;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int taskId) {
		task_id = taskId;
	}
	public String getSpent_at() {
		return spent_at;
	}
	public void setSpent_at(String spentAt) {
		spent_at = spentAt;
	}
	
	public DayEntry add() throws ClientProtocolException, IOException {
		String uri = "/daily/add";
		InputStream content = HarvestCore.post(uri, toJson(false, this));
		if (content != null)
			return HarvestCore.mapper.readValue(content, DayEntry.class);
		System.out.println("Problems to save ");
		return null;
	}
	
	public Request update(int day_entry_id) throws ClientProtocolException, IOException {
		return (Request) add("/daily/update/" + day_entry_id);
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Request request = new Request();
		request.setProject_id(876359);
		request.setTask_id(655857);
		request.setNotes("Esta es la primera nota asignada");
		request.setSpent_at("Mon, 18 Oct 2010");
		request.setHours(0);
		request.add();
	}

	
	
	

}
