import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing .*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class WeatherForcastApp {
	private static JFrame frame;
	private static JTextField locationField;
	private static JTextArea weatherDisplay;
	private static  JButton fetchButton;
	private static String  apiKey ="6d69646d1cfa9633758b22b4a446d284"; //Replaced generated ApiKey  
	

	@SuppressWarnings("deprecation")
	private static String fetchWeatherData(String city) {
		try {
			
			URL url =new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader (new InputStreamReader(connection.getInputStream()));
			String response="";
			String line ;
			while((line= reader.readLine()) !=null) {
				response+=line;
			}
			reader.close();
			
			JSONObject jsonObject =(JSONObject) JSONValue.parse(response.toString());
			JSONObject mainObj =(JSONObject) jsonObject.get("main");
			
			double temperatureKelvin =(double)mainObj.get("temp");
			long humidity=(long)mainObj.get("humidity");
			
			// convert into celcius
			double  temperaturCelcius= temperatureKelvin-273.15;
			
			//Retrieve weather description 
			
			JSONArray weatherArray = (JSONArray)jsonObject.get("weather");
			JSONObject weather = (JSONObject) weatherArray.get(0);
			String description= (String)weather.get("description");
			
			return"Description: " + description+ "\ntemperature:"
			+ temperaturCelcius + "Celsius\nHumidity: "+ humidity+ "%";
			
		} catch (Exception e) {
           return "Failed to fetch weather data. Please check your city and  api key...";
			
		} 
	}

	public static void main(String[] args) {

	    frame=new JFrame("WeatherForcastApp");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,300);
		frame.setLayout(new FlowLayout());
		
		 locationField =new JTextField(15);
		 fetchButton =new JButton("Fetch Wether");
		 weatherDisplay =new JTextArea(10,30);
		weatherDisplay.setEditable(false);
		frame.add(new JLabel("Enter City Name"));
		frame.add(locationField);
		frame.add(fetchButton);
		frame.add(weatherDisplay);
	
		fetchButton.addActionListener(new ActionListener() {
	      @Override
			public void actionPerformed(ActionEvent e) {
				String city  =locationField.getText();
				String weatherInfo = fetchWeatherData(city);
				weatherDisplay.setText(weatherInfo);
	    	  
			}
			
			
		});	
		
		
		frame.setVisible(true);
		
	}

}
