import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
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

		
		try {
			// Set up client socket

			// i think i should read lowportscanner on how to listen to a range of ports
			int port = 9105;
			Socket socket = new Socket("localhost", port);
			
			// Set up input and output streams
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// Send command to server
			
			String response;
			// Process the server response based on the command
			switch(args[0]){
				case "list":
					// Send list command to server
					out.println(args[0]);
					// Handle server response
					while ((response = in.readLine()) != null){
						String[] lines = response.split("\0");
						for(String line : lines){
							System.out.println(line);
						}
					}
					break;
				case "put":
					if (args.length < 2){
						System.out.println("Usage: java Client put <filename>");
						System.exit(1);
					}

					String filename = args[1];
					// Check if the file exists in the directory
					File file = new File(filename);
					if (!file.exists()) {
						System.out.println("Client error: Cannot locate file in directory.");
						System.exit(1);
					}

					// Check if file type is .txt
					if (!file.getName().endsWith(".txt")) {
						System.out.println("Client error: File " + filename + " is not a text file.");
						System.exit(1);
					}

					// Send put command and file name to server
					out.println("put " + filename);
					
					// Open the file
					try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
						String line;
						// Read the file line by line and send each line to the server
						while ((line = fileReader.readLine()) != null) {
							out.println(line);
						}
						// Send "EOF" to signal the end of the file
						out.println("EOF");
					} catch (IOException e) {
						System.out.println("Error occurred while sending the file: " + e.getMessage());
					}
					response = in.readLine();
					System.out.println(response); 
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
			System.err.println("Error: Unable to connect to server, may be offline.");
			System.exit(1);
		}
		


	}

}