import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client 
{
	/*
	 * Requirements:
	 * 1. Accept one of the following cmd args, and performs the stated task:
	 * 		1.1: list - outputs all files on server/serverFiles
	 * 		1.2: put <fname> - upload file fname to the server to be added to serverFiles
	 * 				or return error to say file already exists
	 * 		1.3: Exit after completing each command
	 */
	public static void main( String[] args )
	{
		String cmd;
		if (args.length > 0 && args[0] != null){
			cmd = args[0].toLowerCase();
			System.out.println(cmd);
		}
		else{
			System.out.println("Usage: [cmd] [arg1]");
			return;
		}

		String serverAddress = "localhost"; // Replace with the actual server address
		int serverPort = getPort(); // Call the getPort() method to receive the port number

		try {
			Socket socket = new Socket(serverAddress, serverPort);
			// Connection successful, perform further operations with the server
		} catch (UnknownHostException e) {
			System.out.println("Error: Unknown host");
		} catch (IOException e) {
			System.out.println("Error: Cannot establish connection to server");
		}

		switch(cmd){
			case "list":
				// Process the response from the server
				try {
					Socket socket = new Socket(serverAddress, serverPort);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String response = in.readLine();
					System.out.println("Server response: " + response);
				} catch (IOException e) {
					System.out.println("Error reading server response: " + e.getMessage());
				}
				
				break;
			
			case "put":
				// Get filename from argument 
				if(args[1] != null){
					String filename = args[1]; // no lowercase correction
				}
				else{
					System.out.println("Usage: 'put <filename>'");
					return; 
				}

			
				// Upload file
				break;
			default:
				System.out.println("Command " + cmd + " unrecognised");
		}
		
		
	}


	static int getPort(){
		
			
		return 1; 
	}
}