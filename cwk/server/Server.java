
import java.lang.System;
import java.net.ServerSocket;
import java.io.File;
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
	public static void main( String[] args )
	{
		int port = 1234;
		ServerSocket socket = new ServerSocket(port);
		System.out.println("Server is listening on port " + port);


		String dirPath = "/serverFiles";

		boolean active = true; 

		
		do{
			
			System.out.println("LISTENGING");
			// listen 4 answer
			

			String cmd = args[0];
			// if exists
			String arg = args[1]; 

			
			switch(cmd){
				case "list":
					String[] filelist = getFiles(dirPath);
					for(int i = 0; i < filelist.length; i++){
						System.out.println(filelist[i]);
					}
					break;
				case "put":
					break;
				
				default:
					System.out.println("Error: Argument(s) unrecognised.")
					
			}
			

		}while(active);

		public String[] getFiles(String path){
			// does it return files submitted by that one user???

			File directory = new File(path);
			return ["sdfs"];
			

		}
	}
}