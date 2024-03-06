
import java.lang.System;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Date;
public class Server 
{
	/*
	Requirements:
	
	1. Run continously.
	2. Use an 'Executor' to manage a fixed thread pool with 20 connections
	3. Following a request by a client, query serverFiles and return a list of files found there to the same client
	4. Receive a request from a client to upload new file to serverFiles
			if name exists - return error, otherwise transfer file from client and save to serverFiles
	5. Create file 'log.txt' on server directory and log every valid client request, one line per request in format:
			date|time|client IP|request - (request is one of list or put, no need to log filename for get operations)

	
	Listen to port number in range 9100 to 9999
	Client / server run on same host (localhost)
	 */ 
	/**
	 * The main method is the entry point of the server application.
	 * It sets up a server socket on a specified port and listens for client connections.
	 * When a client connects, a new thread is created to handle the client request.
	 * The server can handle commands such as "list" to list files on the server,
	 * and "put" to transfer and save files on the server.
	 * The server also logs each client request to a log file.
	 *
	 * @param args The command line arguments passed to the application.
	 */
	public static void main( String[] args )
	{
		
		int port = 9100;
		boolean running = true;
		try {

			
			ServerSocket serverSocket = new ServerSocket(port);
			ExecutorService executorService = Executors.newFixedThreadPool(20);

			// create log file 
			File logFile = new File("serverFiles/log.txt");

			
			// run continuously
			while(running){
				try {
					Socket clientSocket = serverSocket.accept();
					
					// 
					executorService.submit(() -> {
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
							
							// Read the client request
							String input = reader.readLine();

							// Tokenising the input
							String[] cmds = input.split(" ");

							// Command processing
							switch(cmds[0]){
								case "list":
									File folder = new File("serverFiles");
									File[] files = folder.listFiles();

									StringBuilder sb = new StringBuilder();
									
									if (files.length > 0){
										// Return files to the client
										sb.append("Listing " + files.length + " file(s):\n");
										for (File file : files) {
											sb.append(file.getName()).append('\n');
										}
										// Remove last terminator
										sb.setLength(sb.length() - 1);
										
									}
									else {
										sb.append("No files present on server.\n");
									}

									System.out.println(sb.toString());

									writer.println(sb.toString());

									//log(logFile, cmds, clientSocket);

									
									
									
									
									
									break;

								case "put":
									if (cmds.length < 2){
										writer.println("Usage: java Client put <filename>");
										break;
									}
									File file = new File("serverFiles/" + cmds[1]);
									
									// Check for filename existence
									if (file.exists()) {
										writer.println("Error: File " + cmds[1] + " already exists.");
										break;
									}

									writer.println("Uploaded file " + cmds[1] + ".");
									
									
									// Continue with file transfer and saving
									
									break;
									
							}


							
							writer.println(input);
		
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


	static void log(File logFile, String cmds[], Socket clientSocket){
	// get time 

	// log the request
	String str_log = String.format("%s|%s|%s|%s",
									new Date(), 
									clientSocket.getInetAddress().getHostAddress(), 
									cmds[0], 
									cmds.length > 1 ? cmds[1] : "");

	try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
        out.println(str_log);
    } catch (IOException e) {
        System.out.println("Error occurred while writing to the log file: " + e.getMessage());
    } 

		
	}
	

}