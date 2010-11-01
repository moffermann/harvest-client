package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class Daily extends AbstractSerializer {
	
	private String for_day;
	private List<DayEntry> day_entries;
	private List<Project> projects;
	public String getFor_day() {
		return for_day;
	}
	public void setFor_day(String forDay) {
		for_day = forDay;
	}
	public List<DayEntry> getDay_entries() {
		return day_entries;
	}
	public void setDay_entries(List<DayEntry> dayEntries) {
		day_entries = dayEntries;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	
	public static Daily get() throws ClientProtocolException, IOException {
		String uri = "/daily";
		InputStream content = HarvestCore.get(uri);
		HarvestCore.mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return HarvestCore.mapper.readValue(content, Daily.class);

	}
	
	public DayEntry select() throws IOException, ClassNotFoundException {
		System.out.println("Select an entry:");
		System.out.println("=======================");
		System.out.println("\tq: Quit");
		System.out.println("\t0: None");
		for (int i=0; i < day_entries.size(); i++) {
			DayEntry day_entry = day_entries.get(i);
			System.out.println("\t" + (i+1) + ": " + day_entry.getNotes());
		}
		Scanner scan = new Scanner(System.in);
		String option = "";
		while (true) {
			System.out.print("\nChoose an entry:");
			option = scan.next();
			if (option.equalsIgnoreCase("q")) {
				System.out.println("Good bye!");
				System.exit(0);
			}
			if (!Pattern.matches("[0-9]+", option)) {
				System.out.println("You must to enter a number");
				continue;
			}
			int index = Integer.parseInt(option);
			if (index < 0 || index > day_entries.size()) {
				System.out.println("Option out of range");
				continue;
			}
			if (index == 0)
				return null;
			return day_entries.get(index - 1);
		}
	}
	
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		Daily daily = get();
		for (Project project : daily.getProjects()) {
			System.out.println(project.getName() + "\t" + project.getId());
			System.out.println("===========");
			for (Task task : project.getTasks()) 
				System.out.println("\t" + task.getName() + "\t" + task.getId());
		}
		
	}

}
