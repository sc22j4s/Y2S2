import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server 
{
	public static void main( String[] args )
	{
		int port = 9757; // Assign port number within desired range
		boolean running = true;

		try {

			// Set up server socket
			ServerSocket serverSocket = new ServerSocket(port);
			ExecutorService executorService = Executors.newFixedThreadPool(20);

			// Create log file, if it doesn't exist
			File logFile = new File("log.txt");
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// Broadcast server is running
			System.out.println("Server listening on port " + port + "...");

			// Run continuously, accepting client connections
			while(running){
				try {
					// Accept connection
					Socket clientSocket = serverSocket.accept();

					executorService.submit(() -> {
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
							
							// Read the client request
							String input = reader.readLine();

							// Tokenising the input
							String[] tokens = input.split(" ");

							// Identify command
							switch(tokens[0]){

								case "list":
									File folder = new File("serverFiles");
									File[] files = folder.listFiles();

									StringBuilder sb = new StringBuilder();
									
									// Check if files are present
									if (files.length > 0){
										// Return files to the client
										sb.append("Listing " + files.length + " file(s):\0");
										for (File file : files) {
											// Text file assertion
											if (file.getName().endsWith(".txt")) {
												sb.append(file.getName()).append('\0');
											}
											
										}
										// Remove last terminator
										sb.setLength(sb.length() - 1);
										
									}
									else { // No files present in directory
										sb.append("No files present on server.");
									}

						
									// Send the list of files to the client as a string
									writer.println(sb.toString());
									log(logFile, tokens[0], clientSocket); // Log the request
									
									break;

								case "put":
									// No filename provided (serverside validation)
									if (tokens.length < 2){
										writer.println("Usage: java Client put <filename>");
										break;
									}

									String filename = tokens[1];
									File file = new File("serverFiles/" + filename);
									
									// Check for filename presence in directory
									if (file.exists()) {
										writer.println("Server error: File " + filename + " already exists on server.");
										break;
									}

									// Continue with file transfer and saving
									try (FileWriter fileWriter = new FileWriter(file)) {
										String line;
										// Read in lines from client until EOF is reached
										while ((line = reader.readLine()) != null && !line.equals("EOF")) {
											fileWriter.write(line);
											fileWriter.write(System.lineSeparator());
										}		
									} catch (IOException e) {
										writer.println("Server error: An error occurred while receiving the file.");
										break;
									}

									writer.println("File " + filename + " successfully uploaded.");
									log(logFile, tokens[0], clientSocket); // Log the request
									break;
							}
						} catch (IOException e) {
							System.out.println("Error occurred while handling client request: " + e.getMessage());
						}
					});
				} catch (IOException e) {
					System.out.println("Error occurred while accepting client connection: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Error occurred while setting up the server socket: " + e.getMessage());
		}
	}

	// Function that logs a valid client request, including date, time, client IP and request type
	static void log(File logFile, String command, Socket clientSocket){
		// Get date and time
		String currentDate = new SimpleDateFormat("dd-MM-yy").format(new Date());
		String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

		// Log the request
		String str_log = String.format("%s|%s|%s|%s",
										currentDate, // date
										currentTime, // time
										clientSocket.getInetAddress().getHostAddress(), // client IP
										command); // command

		// Write to log.txt
		try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
			out.println(str_log);
		} catch (IOException e) {
			System.out.println("Error occurred while writing to the log file: " + e.getMessage());
		} 
	}
}