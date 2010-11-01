package cl.continuum.harvest;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class Project extends AbstractSerializer implements Serializable {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8750681525704516781L;
	private String over_budget_notified_at;
	private String name;
	private boolean show_budget_to_all;
	private String earliest_record_at;
	private boolean billable;
	private String code;
	private boolean notify_when_over_budget;
	private String cost_budget;
	private String latest_record_at;
	private String highrise_deal_id;
	private String fees;
	private boolean cost_budget_include_expenses;
	private String hourly_rate;
	private String client_id;
	private String client;
	private String bill_by;
	private int active_user_assignments_count;
	private float over_budget_notification_percentage;
	private String budget;
	private String budget_by;
	private String basecamp_id;
	private int active_task_assignments_count;
	private boolean active;
	private List<Task> tasks;
	
	
	public String getOver_budget_notified_at() {
		return over_budget_notified_at;
	}


	public void setOver_budget_notified_at(String overBudgetNotifiedAt) {
		over_budget_notified_at = overBudgetNotifiedAt;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isShow_budget_to_all() {
		return show_budget_to_all;
	}


	public void setShow_budget_to_all(boolean showBudgetToAll) {
		show_budget_to_all = showBudgetToAll;
	}


	public String getEarliest_record_at() {
		return earliest_record_at;
	}


	public void setEarliest_record_at(String earliestRecordAt) {
		earliest_record_at = earliestRecordAt;
	}


	public boolean isBillable() {
		return billable;
	}


	public void setBillable(boolean billable) {
		this.billable = billable;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public boolean isNotify_when_over_budget() {
		return notify_when_over_budget;
	}


	public void setNotify_when_over_budget(boolean notifyWhenOverBudget) {
		notify_when_over_budget = notifyWhenOverBudget;
	}


	public String getCost_budget() {
		return cost_budget;
	}


	public void setCost_budget(String costBudget) {
		cost_budget = costBudget;
	}


	public String getLatest_record_at() {
		return latest_record_at;
	}


	public void setLatest_record_at(String latestRecordAt) {
		latest_record_at = latestRecordAt;
	}


	public String getHighrise_deal_id() {
		return highrise_deal_id;
	}


	public void setHighrise_deal_id(String highriseDealId) {
		highrise_deal_id = highriseDealId;
	}


	public String getFees() {
		return fees;
	}


	public void setFees(String fees) {
		this.fees = fees;
	}


	public boolean isCost_budget_include_expenses() {
		return cost_budget_include_expenses;
	}


	public void setCost_budget_include_expenses(boolean costBudgetIncludeExpenses) {
		cost_budget_include_expenses = costBudgetIncludeExpenses;
	}


	public String getHourly_rate() {
		return hourly_rate;
	}


	public void setHourly_rate(String hourlyRate) {
		hourly_rate = hourlyRate;
	}


	public String getClient_id() {
		return client_id;
	}


	public void setClient_id(String clientId) {
		client_id = clientId;
	}


	public String getBill_by() {
		return bill_by;
	}


	public void setBill_by(String billBy) {
		bill_by = billBy;
	}


	public int getActive_user_assignments_count() {
		return active_user_assignments_count;
	}


	public void setActive_user_assignments_count(int activeUserAssignmentsCount) {
		active_user_assignments_count = activeUserAssignmentsCount;
	}


	public float getOver_budget_notification_percentage() {
		return over_budget_notification_percentage;
	}


	public void setOver_budget_notification_percentage(
			float overBudgetNotificationPercentage) {
		over_budget_notification_percentage = overBudgetNotificationPercentage;
	}


	public String getBudget() {
		return budget;
	}


	public void setBudget(String budget) {
		this.budget = budget;
	}


	public String getBudget_by() {
		return budget_by;
	}


	public void setBudget_by(String budgetBy) {
		budget_by = budgetBy;
	}


	public String getBasecamp_id() {
		return basecamp_id;
	}


	public void setBasecamp_id(String basecampId) {
		basecamp_id = basecampId;
	}


	public int getActive_task_assignments_count() {
		return active_task_assignments_count;
	}


	public void setActive_task_assignments_count(int activeTaskAssignmentsCount) {
		active_task_assignments_count = activeTaskAssignmentsCount;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String toString() {
		return "[PROJECT] " + name + " ID: " + getId();
	}


	public static List<Project> list() throws ClientProtocolException, IOException, ClassNotFoundException {
		return list(Project.class);
	}

	public static Project get(int id) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		return get(id, Project.class);
	}
	
	
	
	public List<Task> getTasks() {
		return tasks;
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}


	public String getClient() {
		return client;
	}


	public void setClient(String client) {
		this.client = client;
	}
	
	public static Project select() throws IOException, ClassNotFoundException {
		List<Project> projects = list();
		return select(projects);
		
	}


	public static Project select(List<Project> projects) throws IOException, ClassNotFoundException {
		System.out.println("Select project");
		System.out.println("=======================");
		System.out.println("\tq: Quit");
		System.out.println("\t0: None");
		for (int i=0; i < projects.size(); i++) {
			Project project = projects.get(i);
			System.out.println("\t" + (i+1) + ": " + project.getName());
		}
		Scanner scan = new Scanner(System.in);
		String option = "";
		while (true) {
			System.out.print("\nChoose project number:");
			option = scan.next();
			if (option.equalsIgnoreCase("q")) {
				System.out.println("Good bye!");
				System.exit(0);
			}
			if (!Pattern.matches("[0-9]+", option)) {
				System.out.println("You must enter a number");
				continue;
			}
			int index = Integer.parseInt(option);
			if (index < 0 || index > projects.size()) {
				System.out.println("Option out of range");
				continue;
			}
			if (index == 0)
				return null;
			return projects.get(index - 1);
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
	Project project = select();
	System.out.println(project.getName());
	}
	
}
