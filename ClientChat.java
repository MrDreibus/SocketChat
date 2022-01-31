import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientChat implements Runnable{
	
	public static ArrayList<ClientChat> chats = new ArrayList<>();
	public String nick;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;

	public ClientChat(Socket socket) {
		try {
			this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.nick = br.readLine();

			this.socket = socket;
			
			chats.add(this);
			
			sendMensaje("Servidor: " + nick + " se ha unido al chat");
			
		} catch (IOException e) {
			close(socket, br, bw);
		}
	}

	@Override
	public void run() {
		String mensaje;
		
		while(socket.isConnected()) {
			try {
				mensaje = br.readLine();
				sendMensaje(mensaje);
				
			} catch (IOException e) {
				close(socket, br, bw);
				break;
			}
		}
	}
	
	public void sendMensaje(String mensaje) {
		for(ClientChat c : chats) {
			try {
				if (!c.nick.equals(nick)) {
					c.bw.write(mensaje);
					c.bw.newLine();
					c.bw.flush();
				}
			} catch (Exception e) {
				close(socket, br, bw);
			}
		}
	}
	
	public void eliminarChat() {
		chats.remove(this);
		sendMensaje("Servidor: " + nick + " se ha ido del chat");
	}
	
	public void close(Socket socket, BufferedReader br, BufferedWriter bw) {
		eliminarChat();
		
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
}
