package cl.continuum.harvest;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class Timer extends AbstractSerializer {
	
	private DayEntry day_entry;
	private float hours_for_previously_running_timer;
	public DayEntry getDay_entry() {
		return day_entry;
	}
	public void setDay_entry(DayEntry dayEntry) {
		day_entry = dayEntry;
	}
	public float getHours_for_previously_running_timer() {
		return hours_for_previously_running_timer;
	}
	public void setHours_for_previously_running_timer(
			float hoursForPreviouslyRunningTimer) {
		hours_for_previously_running_timer = hoursForPreviouslyRunningTimer;
	}
	
	public static Timer get(int day_entry_id) throws JsonParseException, JsonMappingException, IOException {
		String uri = "/daily/show/" + day_entry_id;
		InputStream content = HarvestCore.get(uri);
		return HarvestCore.mapper.readValue(content, Timer.class);
	}
	
	

}
