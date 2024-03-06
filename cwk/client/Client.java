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
		if (args.length < 1){
			System.out.println("Usage: java Client <command>");
			System.exit(1);
		}

		String cmd = String.join(" ", args);
		
		
		try {
			// Set up client socket

			// i think i should read lowportscanner on how to listen to a range of ports
			int port = 9100;
			Socket socket = new Socket("localhost", port);
			
			// Set up input and output streams
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Send command to server
			out.println(cmd);

			
			
			

			
			
			
			
			// Process the server response based on the command
			switch(cmd){
				case "list":
					// Handle server response
					String response;
					while ((response = in.readLine()) != null){
						System.out.println(response);
					}

					break;
				case "put":
					if (args.length < 2){
						System.out.println("Usage: java Client put <filename>");
						System.exit(1);
					}
					// Process the response for uploading a file
					break;
				default:
					System.out.println("Error: Invalid command");
					System.exit(1);
			}
			// Close the socket
			socket.close();

		} catch (UnknownHostException e) {
			System.err.println("Error: Unknown host");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error: I/O exception");
			System.exit(1);
		}
		
		

		


	}

}