package cl.continuum.harvest.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugServer {
	
	public static final String SHUTDOWN_TOKEN = "/shutdown";
	public static final String SHUTDOWN_REGEX = "GET (.*) HTTP";
	
	
    public static void copy(InputStream in, OutputStream out, boolean closeAll) throws IOException {
    	try {
//    		String endMark = new String(new byte[] {13,10,13,10, 0});
    		String endMark = new String(new byte[] {0, 0, 0});
    		Pattern p = Pattern.compile(endMark);
            while (true) {
            	byte[] buffer = new byte[1024];
            	
               int amountRead = in.read(buffer);
               out.write(buffer, 0, amountRead);
               Matcher m = p.matcher(new String(buffer));
               if (m.find())
            	   break;
            }
		} catch (Exception e) {
			throw new IOException("Problemas al copiar los datos", e);
		} finally {
	        if (closeAll) {
	        	in.close();
	        	out.close();
	        }
		}
    }
    
	
	public static void start(int port, File outputFile) throws IOException {
		System.out.println("Iniciando server ...");
		System.out.println("PUERTO: " + port);
		System.out.println("SHUTDOWN: http://localhost:" + port + SHUTDOWN_TOKEN);
		Pattern pattern = Pattern.compile(SHUTDOWN_REGEX);
		ServerSocket server = new ServerSocket(port);
		while (true) {
			Socket socket = server.accept();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			System.out.println("DATOS RECIBIDOS!");
			copy(socket.getInputStream(), bos, false);
			socket.getOutputStream().write("HTTP/1.1 404 Not Found\n\r\n\r".getBytes());
			socket.close();
			
			byte[] outputBytes = bos.toByteArray();
			bos.close();
			
			ByteArrayInputStream bis = new ByteArrayInputStream(outputBytes);
			copy(bis, new FileOutputStream(outputFile), true);
			
			String outputString = new String(outputBytes);
			System.out.println("RECIBIENDO:\n" + outputString);
			Matcher matcher = pattern.matcher(outputString);
			if (matcher.find() && SHUTDOWN_TOKEN.equalsIgnoreCase(matcher.group(1)))
				break;
		}
		System.out.println("Cerrando server ...");
		server.close();
	}
	
	public static void main(String[] args) throws IOException {
		start(80, new File("d:/output.dat"));
	}

}
