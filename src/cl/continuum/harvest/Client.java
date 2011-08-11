package cl.continuum.harvest;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 */
public class Client extends AbstractSerializer implements Serializable {

    private static final long serialVersionUID = 8926028143190627264L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Client> list() throws ClientProtocolException, IOException, ClassNotFoundException {
        return list(Client.class);
    }

    public static Client get(int id) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        return get(id, Client.class);
    }

    public static Client select() throws IOException, ClassNotFoundException {
        List<Client> clients = list();
        return select(clients);

    }

    public static Client select(List<Client> clients) throws IOException, ClassNotFoundException {
        System.out.println("Select client");
        System.out.println("=======================");
        System.out.println("\tq: Quit");
        System.out.println("\t0: None");
        for (int i=0; i < clients.size(); i++) {
            Client client = clients.get(i);
            System.out.println("\t" + (i+1) + ": " + client.getName());
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
            if (index < 0 || index > clients.size()) {
                System.out.println("Option out of range");
                continue;
            }
            if (index == 0)
                return null;
            return clients.get(index - 1);
        }
    }

}
