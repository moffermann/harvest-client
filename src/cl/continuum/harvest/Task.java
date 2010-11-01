package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Project clas to admin projects requests
 * 
 * @author Mauricio Offermann Palma <Mauricio.offermann@continuum.cl>
 *
 */
public class Task extends AbstractSerializer implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4381249959658450952L;
	private boolean billable_by_default;
	private float default_hourly_rate;
	private boolean is_default;
	private String name;
	private boolean deactivated;

	
	private static final Log log = LogFactory.getLog(Task.class);

	
	public boolean isBillable_by_default() {
		return billable_by_default;
	}

	public void setBillable_by_default(boolean billableByDefault) {
		billable_by_default = billableByDefault;
	}

	public float getDefault_hourly_rate() {
		return default_hourly_rate;
	}

	public void setDefault_hourly_rate(float defaultHourlyRate) {
		default_hourly_rate = defaultHourlyRate;
	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean isDefault) {
		is_default = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}




	public static Log getLog() {
		return log;
	}



	public boolean isDeactivated() {
		return deactivated;
	}


	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}

	public static List<Task> list() throws ClientProtocolException, IOException, ClassNotFoundException {
		return list(Task.class);
	}

	public static Task get(int id) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		return get(id, Task.class);
	}

	public TaskAssignment assign(int project_id) throws JsonGenerationException, JsonMappingException, ClientProtocolException, IOException {
		String uri = "/projects/" + project_id + "/task_assignments/add_with_create_new_task";
		InputStream content = HarvestCore.post(uri, toJson());
		if (content == null)
			return null;
		return fromJson(content, TaskAssignment.class);
	}
	
	public static Task select() throws IOException, ClassNotFoundException {
		List<Task> tasks = list();
		return select(tasks);
	}
	
	
	public static Task select(List<Task> tasks) throws IOException, ClassNotFoundException {
		System.out.println("Task selection");
		System.out.println("=======================");
		System.out.println("\tq: Quit");
		System.out.println("\t0: None");
		for (int i=0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			System.out.println("\t" + (i+1) + ": " + task.getName());
		}
		Scanner scan = new Scanner(System.in);
		String option = "";
		while (true) {
			System.out.print("\nChoose task number:");
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
			if (index < 0 || index > tasks.size()) {
				System.out.println("Opcion fuera de rango");
				continue;
			}
			if (index == 0)
				return null;
			return tasks.get(index - 1);
		}
	}
	
	

	public static void main(String[] args) throws ClientProtocolException, IOException, ClassNotFoundException {
		Task task = select();
		System.out.println(task.getName());
	}
}
