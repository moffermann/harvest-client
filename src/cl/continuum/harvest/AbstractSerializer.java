package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.type.TypeFactory;
/**
 * Base entries class thats provides funtions to serialize to JSON an unserialize 
 * to java objects.
 * Also contains methods to make requests easy from childrens. 
 * IMPORTANT: Not all methods as add,delete, update, get and list have sense from inherit classes
 * due to some entities don't use classic plural to make requests. In this cases you must
 * override this methods passing the uri to methods that can accept it.  
 * 
 * @author Mauricio Offermann Palma <Mauricio.offermann@continuum.cl>
 *
 */
public abstract class AbstractSerializer {
	
	private long id;
	private int company_id;
	private String created_at;
	private String updated_at;
	private long cache_version;
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public int getCompany_id() {
		return company_id;
	}



	public void setCompany_id(int companyId) {
		company_id = companyId;
	}



	public String getCreated_at() {
		return created_at;
	}



	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}



	public String getUpdated_at() {
		return updated_at;
	}



	public void setUpdated_at(String updatedAt) {
		updated_at = updatedAt;
	}



	public long getCache_version() {
		return cache_version;
	}



	public void setCache_version(long cacheVersion) {
		cache_version = cacheVersion;
	}
	
	public static String getObjectName(Class<?> objectClass) {
		return objectClass.getSimpleName().replaceAll("([A-Z])", "_$1").substring(1).toLowerCase();
		
	}



	public static String toJson(boolean mapWrapped, Object target) throws JsonGenerationException, JsonMappingException, IOException {
		if (mapWrapped) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(target.getClass().getSimpleName().toLowerCase(), target);
			return HarvestCore.mapper.writeValueAsString(map);
		}
		return HarvestCore.mapper.writeValueAsString(target);
	}
	public String toJson() throws JsonGenerationException, JsonMappingException, IOException {
		return toJson(true, this);
	}
	
	public static <T> T fromJson(String source, Class<T> objectClass) throws JsonParseException, JsonMappingException, IOException {
		Map<String, T> map =	HarvestCore.mapper.readValue(source, TypeFactory.fromType(new RESTParameterizedType(objectClass, false)));
		return map.get(getObjectName(objectClass));
	}
	
	public static <T> T fromJson(InputStream source, Class<T> objectClass) throws JsonParseException, JsonMappingException, IOException {
		if (source == null)
			throw new IOException("Source is null");  
		Map<String, T> map =	HarvestCore.mapper.readValue(source, TypeFactory.fromType(new RESTParameterizedType(objectClass, false)));
		return map.get(getObjectName(objectClass));
	}
	
	public static <T> List<T> fromJsonList(String source, Class<T> objectClass) throws JsonParseException, JsonMappingException, IOException {
		List<Map<String, T>> list =	HarvestCore.mapper.readValue(source, TypeFactory.fromType(new RESTParameterizedType(objectClass, true)));
		List<T> result = new ArrayList<T>();
		for (Map<String, T> map : list) {
			result.add(map.get(getObjectName(objectClass)));
		}
		return result;
	}
	
	public static <T> List<T> fromJsonList(InputStream source, Class<T> objectClass) throws JsonParseException, JsonMappingException, IOException {
		List<Map<String, T>> list =	HarvestCore.mapper.readValue(source, TypeFactory.fromType(new RESTParameterizedType(objectClass, true)));
		List<T> result = new ArrayList<T>();
		for (Map<String, T> map : list) {
			result.add(map.get(getObjectName(objectClass)));
		}
		return result;
	}
	

	/**
	 * Make a post request serializing this class to json and sending data to specified uri
	 * @param uri
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public AbstractSerializer add(String uri) throws ClientProtocolException, IOException {
		InputStream content = HarvestCore.post(uri, toJson());
		return fromJson(content, getClass());
	}
	
	/**
	 * Make a POST request sending this class as JSON to classic plural uri. For example:
	 * In Projects class project.add() will be send data to /projects. 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public AbstractSerializer add() throws ClientProtocolException, IOException {
		String uri = "/" + getClass().getSimpleName().toLowerCase()  + "s";
		return add(uri);
	}
	
	/**
	 * Make a GET request getting a list of specified Class Objects to specified uri  
	 * @param <T>
	 * @param uri
	 * @param objectClass
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> list(String uri, Class<T> objectClass) throws ClientProtocolException, IOException, ClassNotFoundException {
		InputStream content = HarvestCore.get(uri);
		return fromJsonList(content, objectClass);
	}
	
	/**
	 * Make a GET request to classic plural uri based in the name of specified class object.  
	 * @param <T>
	 * @param objectClass
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> list(Class<T> objectClass) throws ClientProtocolException, IOException, ClassNotFoundException {
		String uri = "/" + objectClass.getSimpleName().toLowerCase() + "s";
		return list(uri, objectClass);
	}
	
	/**
	 * Make a GET request to specified uri
	 * 
	 * @param <T>
	 * @param uri
	 * @param id
	 * @param childClass
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T get(String uri, long id, Class<T> childClass) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		InputStream content = HarvestCore.get(uri);
		return fromJson(content, childClass);
	}
	
	/**
	 *  
	 *Makes a GET request an  
	 * @param <T>
	 * @param id
	 * @param childClass
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T get(long id, Class<T> childClass) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		String uri = "/" + childClass.getSimpleName().toLowerCase() + "s/" +id;
		return get(uri, id, childClass);
	}
	
	public void delete(String uri) throws ClientProtocolException, IOException {
		HarvestCore.delete(uri);
	}
	
	public void delete() throws ClientProtocolException, IOException {
		String uri = "/" + getClass().getSimpleName().toLowerCase()  + "s/" + getId();
		delete(uri);
	}
	
	public AbstractSerializer update(String uri) throws JsonGenerationException, JsonMappingException, ClientProtocolException, IOException {
		InputStream content = HarvestCore.put(uri, toJson());
		return fromJson(content, getClass());
	}
	
	public AbstractSerializer update() throws JsonGenerationException, JsonMappingException, ClientProtocolException, IOException {
		String uri = "/" + getClass().getSimpleName().toLowerCase()  + "s/" + getId();
		return update(uri);
	}
	
	

}
