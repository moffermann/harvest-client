package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class TaskAssignment extends AbstractSerializer {
	
	private int project_id;
	private int task_id;
	private boolean billable;
	private boolean deactivated;
	private int budget;
	private int hourly_rate;
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
	public boolean isBillable() {
		return billable;
	}
	public void setBillable(boolean billable) {
		this.billable = billable;
	}
	public boolean isDeactivated() {
		return deactivated;
	}
	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	public int getHourly_rate() {
		return hourly_rate;
	}
	public void setHourly_rate(int hourlyRate) {
		hourly_rate = hourlyRate;
	}
	
	
	
	public AbstractSerializer add() throws ClientProtocolException, IOException {
		String plural = getClass().getSimpleName().toLowerCase()  + "s";
		InputStream content = HarvestCore.post("/" + plural, toJson());
		return fromJson(content, getClass());
	}
	
	public static List<TaskAssignment> list(int project_id) throws ClientProtocolException, IOException, ClassNotFoundException {
		String uri = "/projects/" + project_id + "/task_assignments";
		InputStream content = HarvestCore.get(uri);
		return fromJsonList(content, TaskAssignment.class);
	}
	
	public static TaskAssignment get(int project_id, long id) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		String uri = "/projects/" + project_id + "/task_assignments/" + id; 
		InputStream content = HarvestCore.get(uri);
		return fromJson(content, TaskAssignment.class);
	}
	
	public void delete() throws ClientProtocolException, IOException {
		String uri = "/projects/" + project_id + "/task_assignments/" + getId(); 
		HarvestCore.delete(uri);
	}
	
	public TaskAssignment update() throws JsonGenerationException, JsonMappingException, ClientProtocolException, IOException {
		String uri = "/projects/" + project_id + "/task_assignments/" + getId();
		InputStream content = HarvestCore.put(uri, toJson());
		return fromJson(content, TaskAssignment.class);
	}
	

}
