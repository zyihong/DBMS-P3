import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;



//json.simple 1.1
// import org.json.simple.JSONObject;
// import org.json.simple.JSONArray;

// Alternate implementation of JSON modules.
import org.json.JSONObject;
import org.json.JSONArray;

public class GetData{
	
    static String prefix = "jiaqni.";
	
    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;
	
    // You must refer to the following variables for the corresponding 
    // tables in your database

    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;

    // This is the data structure to store all users' information
    // DO NOT change the name
    JSONArray users_info = new JSONArray();		// declare a new JSONArray

	
    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
	super();
	String dataType = u;
	oracleConnection = c;
	// You will use the following tables in your Java code
	cityTableName = prefix+dataType+"_CITIES";
	userTableName = prefix+dataType+"_USERS";
	friendsTableName = prefix+dataType+"_FRIENDS";
	currentCityTableName = prefix+dataType+"_USER_CURRENT_CITY";
	hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITY";
	programTableName = prefix+dataType+"_PROGRAMS";
	educationTableName = prefix+dataType+"_EDUCATION";
	eventTableName = prefix+dataType+"_USER_EVENTS";
	albumTableName = prefix+dataType+"_ALBUMS";
	photoTableName = prefix+dataType+"_PHOTOS";
	tagTableName = prefix+dataType+"_TAGS";
    }
	
	
	
	
    //implement this function

    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException{ 

    	JSONArray users_info = new JSONArray();
		
	// Your implementation goes here....		
    	try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {
			
			ResultSet rst = stmt.executeQuery("SELECT USER_ID,FIRST_NAME,LAST_NAME,YEAR_OF_BIRTH,MONTH_OF_BIRTH,DAY_OF_BIRTH,GENDER FROM "+userTableName);

			while(rst.next()){
				Long user_id = rst.getLong(1);
				String first_name = rst.getString(2);
				String last_name = rst.getString(3);
				String gender = rst.getString(7);
				int yob = rst.getInt(4);
				int mob = rst.getInt(5);
				int dob = rst.getInt(6);

				JSONObject user = new JSONObject();
				user.put("user_id", user_id);
				user.put("first_name", first_name);
				user.put("last_name", last_name);
				user.put("gender", gender);
				user.put("YOB", yob);
				user.put("MOB", mob);
				user.put("DOB", dob);

				Statement stmt2 = oracleConnection.createStatement();
				ResultSet rst2 = stmt2.executeQuery(
					"SELECT USER2_ID FROM " + 
					friendsTableName + " WHERE USER1_ID = " + user_id);

				JSONArray friendlist = new JSONArray();
			
				while(rst2.next()){
					friendlist.put(rst2.getInt(1));
				}

				user.put("friends", friendlist);

				rst2 = stmt2.executeQuery(
					"SELECT C.CITY_NAME, C.STATE_NAME, C.COUNTRY_NAME " + 
					"FROM " + cityTableName + " C, " + currentCityTableName + " UC " + 
					"WHERE C.CITY_ID = UC.CURRENT_CITY_ID AND UC.USER_ID = " + user_id);

				JSONObject current = new JSONObject();

				if(rst2.next()){
					current.put("city", rst2.getString(1));
					current.put("state", rst2.getString(2));
					current.put("country", rst2.getString(3));
				}
				user.put("current", current);

				rst2 = stmt2.executeQuery(
					"SELECT C.CITY_NAME, C.STATE_NAME, C.COUNTRY_NAME " + 
					"FROM " + cityTableName + " C, " + hometownCityTableName + " UH " + 
					"WHERE C.CITY_ID = UH.HOMETOWN_CITY_ID AND UH.USER_ID = " + user_id);

				JSONObject hometown = new JSONObject();
			
				if(rst2.next()){
					hometown.put("city", rst2.getString(1));
					hometown.put("state", rst2.getString(2));
					hometown.put("country", rst2.getString(3));
				}
				user.put("hometown", hometown);

				users_info.put(user);

				rst2.close();
				stmt2.close();
			}

			rst.close();
    		stmt.close();
    	} 
		
		return users_info;
    }

    // This outputs to a file "output.json"
    public void writeJSON(JSONArray users_info) {
	// DO NOT MODIFY this function
	try {
	    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
	    file.write(users_info.toString());
	    file.flush();
	    file.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
		
    }
}
