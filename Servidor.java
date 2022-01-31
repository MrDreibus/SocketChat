import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	private ServerSocket server;
	
	public Servidor(ServerSocket server) {
		this.server = server;
	}
	
	public void iniciar() {
		try {
			while (!server.isClosed()) {
				Socket socket = server.accept();
				
				System.out.println("Un nuevo usuario se ha conectado");
				ClientChat client = new ClientChat(socket);
				
				Thread t1 = new Thread(client);
				t1.start();
				
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage() + "\n\n" + e.getStackTrace());
		}
	}
	
	public void cerrarServer() {
		try {
			if (server != null) {
				server.close();
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage() + "\n\n" + e.getStackTrace());
		}
	}
	
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(2880);
			Servidor s = new Servidor(server);
			s.iniciar();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
