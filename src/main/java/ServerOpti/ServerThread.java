package ServerOpti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable{
    private final Socket clientSocket;
    private final int id;
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.id = id;
    }

    // Méthode utilisée dans chaque thread pour gérer la requête d'un client
    public void run() {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            System.out.println("Client "+id+" started at " + (System.currentTimeMillis()-ServerLaunch.start) );
            int cpt = 0;
            String client = in.readLine();
            String request = in.readLine();

            /*if (request.equals("Exit")) {
                System.out.println("Client #" + client + " sends exit...");
                out.println("Exit");
                this.clientSocket.close();
                System.out.println("Closed");
                break;
            }*/

            // On process la requête et on affiche la réponse chez le client
            String answer = ServerLaunch.protocol.processInput(request);

            long start = System.currentTimeMillis();
            //System.out.println("time before print the "+answer.length() + " char answer");

            out.println(answer);
            System.out.println("-> "+ ++cpt +" : Client #" +client+" served | time used to print : "+(System.currentTimeMillis() - start));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + ServerLaunch.port + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
