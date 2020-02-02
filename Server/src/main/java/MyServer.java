import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer  {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("我是服务端");
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String command = reader.readLine();
            Runtime.getRuntime().exec(command);
            reader.close();
            inputStream.close();
            socket.close();
            break;
        }
        serverSocket.close();
    }
}
