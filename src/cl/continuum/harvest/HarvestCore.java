package cl.continuum.harvest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This class provides methods to send request to harvest.
 * TODO: Probably is necesary to control more error codes.  
 * @author Mauricio Offermann <Mauricio.offermann@continuum.cl>
 *
 */
public class HarvestCore {
	
    private static String USERNAME;
    private static String PASSWORD;
    private static String HOST;
    public static final ObjectMapper mapper = new ObjectMapper();
    public static String credentials;
    
    static {
    	try{
    		Properties property = new Properties();
    		property.load(new FileReader("authentication.properties"));
    		USERNAME = property.getProperty("username");
    		PASSWORD = property.getProperty("password");
    		HOST	 = property.getProperty("host");
    		credentials = USERNAME + ":" + PASSWORD;
    	} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Can't find authentication.properties");
		}

        mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    
    public static void copy(InputStream in, OutputStream out) throws IOException {
    	byte[] buffer = new byte[1024];
    	while (true) {
    		int amountRead = in.read(buffer);
    		if (amountRead == -1)
    			break;
    		out.write(buffer, 0, amountRead);
    	}
    }
    
    public static HttpClient openConnection() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(HOST, 443), 
                new UsernamePasswordCredentials(USERNAME, PASSWORD));
        
        httpclient.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Java");
        return httpclient;
    }
    
    
    public static InputStream send(HttpClient httpclient, HttpUriRequest request, String parameters) throws ClientProtocolException, IOException {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Basic " + Base64.encodeBase64String(credentials.getBytes()).trim());
        if (parameters != null) {
            StringEntity reqentity = new StringEntity(parameters);
            if (request instanceof HttpEntityEnclosingRequestBase)
            	((HttpEntityEnclosingRequestBase)request).setEntity(reqentity);
            else
            	throw new ClientProtocolException("Method not allowed. Don't send parameters or use POST or PUT");
                    	
        }
        HttpResponse response = httpclient.execute(request);
        System.out.println(response.getStatusLine());
        if (response.getStatusLine().getStatusCode() == 404) {
        	System.out.println("Service not found");
        	return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HttpEntity entity = response.getEntity();
        if (entity != null)
        	entity.writeTo(bos);
        Header[] locations = response.getHeaders("Location");
        if (locations == null || locations.length == 0)
        	return new ByteArrayInputStream(bos.toByteArray());
        String uri = "https://" + HOST;
        uri += locations[0].getValue();
        HttpGet get = new HttpGet(uri);
    	return send(httpclient, get, null);
    }
    
    public static InputStream send(Class<? extends HttpUriRequest> methodClass, String uri, String parameters) throws ClientProtocolException, IOException {
    	try {
        	uri = "https://" + HOST + uri;
        	Constructor<? extends HttpUriRequest> constructor = methodClass.getConstructor(String.class);
        	HttpUriRequest request = constructor.newInstance(uri);
        	HttpClient httpclient = openConnection();
        	InputStream content = send(httpclient, request, parameters);
        	httpclient.getConnectionManager().shutdown();
        	return content;
		} catch (Exception e) {
			throw new ClientProtocolException("Problems to send", e);
		}
    }
    
    public static InputStream send(Class<? extends HttpUriRequest> methodClass, String uri) throws ClientProtocolException, IOException {
    	return send(methodClass, uri, null);
    }
    
    public static InputStream get(String uri) throws ClientProtocolException, IOException {
    	return send(HttpGet.class, uri);
    }
    
    public static InputStream post(String uri, final String parameters) throws ClientProtocolException, IOException {
    	return send(HttpPost.class, uri, parameters);
    }
    
    public static InputStream delete(String uri) throws ClientProtocolException, IOException {
    	return send(HttpDelete.class, uri);
    }
    
    public static InputStream put(String uri, final String parameters) throws ClientProtocolException, IOException {
    	return send(HttpPut.class, uri, parameters);
    }
}
