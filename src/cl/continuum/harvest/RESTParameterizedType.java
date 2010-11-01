package cl.continuum.harvest;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;



public class RESTParameterizedType implements ParameterizedType {

	private Type rawType;
	private Type[] actualTypeArguments;

	private RESTParameterizedType(Type rawType, Type[] actualTypeArguments) {
		super();
		this.rawType = rawType;
		this.actualTypeArguments = actualTypeArguments;
	}
	
	public RESTParameterizedType(Type targetType, boolean isList) {
		if (isList) {
			this.rawType = List.class;
			this.actualTypeArguments = new Type[] {new RESTParameterizedType(Map.class, new Type[] {String.class, targetType})};
		} else {
			this.rawType = Map.class;
			this.actualTypeArguments = new Type[] {String.class, targetType};
		}
		
	}


	public boolean isList() {
		return List.class.isAssignableFrom((Class<?>)rawType);
	}

	@Override
	public Type[] getActualTypeArguments() {
		return this.actualTypeArguments;
	}

	@Override
	public Type getOwnerType() {
		return null;
	}

	@Override
	public Type getRawType() {
		return this.rawType;
	}
	
	

}
