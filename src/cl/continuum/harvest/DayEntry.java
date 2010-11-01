package cl.continuum.harvest;

import java.io.InputStream;
import java.io.Serializable;

public class DayEntry extends AbstractSerializer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7511143115137074613L;
	private String client;
	private int project_id;
	private String project;
	private int task_id;
	private String task;
	private float hours;
	private String notes;
	private String timer_started_at;
	private float hours_for_previously_running_timer;
	
	
	public String getClient() {
		return client;
	}
	public float getHours_for_previously_running_timer() {
		return hours_for_previously_running_timer;
	}
	public void setHours_for_previously_running_timer(
			float hoursForPreviouslyRunningTimer) {
		hours_for_previously_running_timer = hoursForPreviouslyRunningTimer;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTimer_started_at() {
		return timer_started_at;
	}
	public void setTimer_started_at(String timerStartedAt) {
		timer_started_at = timerStartedAt;
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
	
	
	public DayEntry togleTimer() throws Exception {
		if (getId() == 0)
			return null;
		String uri ="/daily/timer/" + getId();
		InputStream content = HarvestCore.get(uri);
		return HarvestCore.mapper.readValue(content, DayEntry.class);
	}
	
	
	
	
}
