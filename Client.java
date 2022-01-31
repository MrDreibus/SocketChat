import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public String nick;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	
	public Client(Socket socket, String nick) {
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			this.socket = socket;
			this.nick = nick;
			
		} catch (IOException e) {
			close(socket, br, bw);
		}
	}
	
	public void sendMensaje() {
		try {
			bw.write(nick);
			bw.newLine();
			bw.flush();
			
			Scanner sc = new Scanner(System.in);
			
			while (socket.isConnected()) {
				String mensaje = sc.nextLine();
				bw.write("[ " + nick + " ]: " + mensaje);
				bw.newLine();
				bw.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void esperaMensaje() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String mensaje;
				
				while (socket.isConnected()) {
					try {
						mensaje = br.readLine();
						System.out.println(mensaje);
						
					} catch (IOException e) {
						close(socket, br, bw);
						
					}
				}
			}
			
		}).start();
	}
	
	public void close(Socket socket, BufferedReader br, BufferedWriter bw) {
		try {
			if (br != null) {
				br.close();
			}
			
			if (bw != null) {
				bw.close();
			}
			
			if (socket != null) {
				socket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.print("Inserta el nick que quieres usar: ");
			
			String nickname = sc.nextLine();
			Socket socket;
				socket = new Socket("localhost", 2880);
			
			
			Client client = new Client(socket, nickname);
			client.esperaMensaje();
			client.sendMensaje();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
