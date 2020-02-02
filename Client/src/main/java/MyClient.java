import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 9999);
        System.out.println("我是客户端:");
        OutputStream outputStream = socket.getOutputStream();
        String command = "CMD /c notepad";
        PrintWriter writer = new PrintWriter(new PrintStream(outputStream));
        writer.println(command);
        writer.flush();
        writer.close();
    }
}
