package cl.continuum.harvest.console;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import cl.continuum.harvest.Daily;
import cl.continuum.harvest.DayEntry;
import cl.continuum.harvest.Project;
import cl.continuum.harvest.Request;
import cl.continuum.harvest.Task;

/**
 * Main console testing
 * You can execute this class without arguments and follow the menu options. 
 * Also you can use classic flags as arguments, example:
 * 
 * java -jar harvest-plugin.jar -a
 *  To assign selected task to actual project.
 *  
 *  The flags used are the same options than menu.
 * 
 * @author Mauricio Offermann <Mauricio.offermann@continuum.cl>
 *
 */
public class Main {
	
	private static final Map<String, Option> options = new HashMap<String, Option>();
	
	private static Project project = null;
	private static Task task = null;
	private static DayEntry dayEntry = null;
	
	
	static {
		Main.project = readObject(Project.class);
		Main.task = readObject(Task.class);
		Main.dayEntry = readObject(DayEntry.class);
		
		options.put("a", new Option('a', "Assign task to project", "assignTask"));
		options.put("p", new Option('p', "Select project", "selectProject"));
		options.put("t", new Option('t', "Select task from project", "selectTask"));
		options.put("k", new Option('k', "List all tasks", "listTasks"));
		options.put("s", new Option('s', "Stop/Start timer from actual dayentry", "toggleTimer"));
		options.put("l", new Option('l', "List day entries and selected project", "show"));
		options.put("n", new Option('n', "Add day entry", "addDayEntry"));
		options.put("q", new Option('q', "Quit", "exit"));
	}
	
	public static void exit() {
		System.out.println("Good bye");
		System.exit(0);
	}
	
	public static void selectProject() throws IOException, ClassNotFoundException {
		Main.project = Project.select();
		writeObject(Main.project);
	}
	
	public static void listTasks() throws IOException, ClassNotFoundException {
		Main.task = Task.select();
		writeObject(Main.task);
	}
	
	public static void assignTask() throws IOException, ClassNotFoundException {
		if (Main.project == null)
			selectProject();
		if (Main.task == null)
			listTasks();
		Main.task.assign((int) Main.project.getId());
	}
	
	public static void show() throws ClientProtocolException, IOException, ClassNotFoundException {
		Main.dayEntry = Daily.get().select();
		writeObject(Main.dayEntry);
	}
	
	public static void addDayEntry() throws IOException, ClassNotFoundException {
		if (Main.project == null)
			selectProject();
		if (Main.task == null)
			listTasks();
		Scanner scan = new Scanner(System.in);
		System.out.print("Notes: ");
		String notes = scan.nextLine();
		Request request = new Request();
		request.setNotes(notes);
		request.setProject_id((int) Main.project.getId());
		request.setTask_id((int) Main.task.getId());
		Main.dayEntry =	request.add();
		writeObject(Main.dayEntry);
	}
	
	public static void toggleTimer() throws Exception {
		if (Main.project == null)
			selectProject();
		if (Main.task == null)
			listTasks();
		if (Main.dayEntry == null)
			Main.dayEntry = Daily.get().select();
		Main.dayEntry =	Main.dayEntry.togleTimer();
		writeObject(Main.dayEntry);
	}
	
	public static void selectTask() throws IOException, ClassNotFoundException {
			Daily daily = Daily.get();
			if (Main.project == null)
				selectProject();
			if (Main.project == null)
				return;
			for (Project project : daily.getProjects()) {
				if (Main.project.getId() == project.getId()) {
					Main.task =	Task.select(project.getTasks());
					writeObject(Main.task);
					break;
				}
					
			}
		
	}
	
	
	private static void printMenu() {
		for (Option option : options.values())
			System.out.println("\t" + option);
		System.out.println("");
		if (Main.project != null)
			System.out.println("\nACTUAL PROJECT: " + Main.project.getName());
		if (Main.task != null)
			System.out.println("\nACTUAL TASK: " + Main.task.getName());
		if (Main.dayEntry != null)
			System.out.println("\nACTUAL DAY ENTRY: " + Main.dayEntry.getNotes());
		System.out.print("\nChoose an option:");
	}
	
	
	private static void menu() throws Exception {
		System.out.println("------ HARVEST CONSOLE ------\n");
		Scanner scan = new Scanner(System.in);
		while (true) {
			printMenu();
			String o =scan.next().toLowerCase();
			if (!options.containsKey(o)) {
			System.out.println("Unknown option");
				continue;
			}
			Option option = options.get(o);
			option.execute();
		}
	}
	
	private static void close(Closeable c) {
		try {
			if (c != null)
				c.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public static void writeObject(Object object) {
		if (object == null)
			return;
		ObjectOutputStream os = null;
		try {
			File dir = new File("data");
			dir.mkdirs();
			File file = new File(dir, object.getClass().getSimpleName());
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(os);
		}

	}
	
	public static <T> T readObject(Class<T> objectClass) {
		ObjectInputStream is = null;
		try {
			File file = new File("data", objectClass.getSimpleName());
			if (!file.exists())
				return null;
			is = new ObjectInputStream(new FileInputStream(file));
			return objectClass.cast(is.readObject());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {
		if (args.length > 0){
			for (String argument : args) {
				if (!Pattern.compile("^-[a-z]$").matcher(argument).matches())
					throw new Exception("The argument '" + argument + "' is invalid.");
				String option = argument.substring(1);
				if (!options.containsKey(option))
					throw new Exception("The argument '" + argument + "' is invalid.");
				options.get(option).execute();
			}
		}else{
			menu();
		}
	}

}
;