import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;

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
			int port = 9105;
			Socket socket = new Socket("localhost", port);
			
			// Set up input and output streams
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			

			// Process the server response based on the command
			switch(args[0]){
				case "list":

					// Send command to server
					out.println(cmd);
					// Handle server response
					String list_response = in.readLine();
					
					// Split into newlines using provided null character
					String[] lines = list_response.split("\0");
					for (String line : lines) {
						System.out.println(line);
					}
	
					break;
				case "put":
					if (args.length < 2){
						System.out.println("Usage: java Client put <filename>");
						System.exit(1);
					}

					// Open the file and send each line to the server
					try (BufferedReader fileReader = new BufferedReader(new FileReader(args[1]))) {
						String line;
						while ((line = fileReader.readLine()) != null) {
							out.println(line);
						}
					}
					
					out.println("EOF");

					String put_response = in.readLine(); 
					System.out.println(put_response);
					
					// Process the response for uploading a file
					break;
				// unrecognised command
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