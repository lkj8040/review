import org.junit.Test;
import redis.clients.jedis.Jedis;

public class jedis {
    @Test
    public void testPing(){
        String host = "hadoop101";
        int port = 6379;
        int timeout = 6000;
        Jedis jedis = new Jedis(host, port, timeout);
        String pong = jedis.ping();
        System.out.println(pong);
        jedis.close();
    }
}
