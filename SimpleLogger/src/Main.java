import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

interface MyLogger{
	void log(int level, String message);
}
class ConsoleLogger implements MyLogger{
	public void log(int level, String message){
		TimeZone tz = TimeZone.getTimeZone("GMT+2");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	    df.setTimeZone(tz);
	    String loggedMsg ="";
	    switch (level) {
		case 1:
			loggedMsg = "INFO";
			break;
		case 2:
			loggedMsg = "WARNING";
			break;
		case 3:
			loggedMsg = "PLSCHECKFFS";
			break;
		default:
			break;
		}
	    loggedMsg += "::"+df.format(new Date());
		System.out.println( loggedMsg+"::"+message);
	}
}
class FileLogger implements MyLogger{
	public void log(int level, String message){
		TimeZone tz = TimeZone.getTimeZone("GMT+2");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	    df.setTimeZone(tz);
	    String loggedMsg ="";
	    switch (level) {
		case 1:
			loggedMsg = "INFO";
			break;
		case 2:
			loggedMsg = "WARNING";
			break;
		case 3:
			loggedMsg = "PLSCHECKFFS";
			break;
		default:
			break;
		}
	    loggedMsg += "::"+df.format(new Date())+message;
	    try
	    {
	        String filename= "log.txt";
	        FileWriter fw = new FileWriter(filename,true);
	        fw.write(loggedMsg+'\n');
	        fw.close();
	        System.out.println("Successfully logged into file.");
	    }
	    catch(IOException ioe)
	    {
	        System.err.println("IOException: " + ioe.getMessage());
	    }
	}
}
class HTTPLogger implements MyLogger{
	public void log(int level, String message){
		TimeZone tz = TimeZone.getTimeZone("GMT+2");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	    df.setTimeZone(tz);
	    String loggedMsg ="";
	    switch (level) {
		case 1:
			loggedMsg = "INFO";
			break;
		case 2:
			loggedMsg = "WARNING";
			break;
		case 3:
			loggedMsg = "PLSCHECKFFS";
			break;
		default:
			break;
		}
	    loggedMsg += "::"+df.format(new Date())+message;

		try {
			sendPost(loggedMsg, "http://kristiyanboyanov.com/post-test.php");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void sendPost(String params, String url) throws MalformedURLException, IOException{
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String query = String.format("log=%s", URLEncoder.encode(params, charset));
		
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true); // POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		System.out.println("POST sent!");
		try (OutputStream output = connection.getOutputStream()) {
			System.out.println("HTTP response:");
		    output.write(query.getBytes(charset));
		}

		InputStream response = connection.getInputStream();
		printResult(response);
	}
	
	public void printResult(java.io.InputStream response) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        System.out.println(out.toString());
        reader.close();
	}
 
}
public class Main {
	public static void main(String[] args){
		ConsoleLogger loggerConsole = new ConsoleLogger();
		FileLogger loggerFile = new FileLogger();
		HTTPLogger loggerHTTP = new HTTPLogger();
		
		loggerConsole.log(1, "Just-log-that-message");
		loggerHTTP.log(1, "Send Post");
		loggerFile.log(2, "write that log down");
		
	}
}
