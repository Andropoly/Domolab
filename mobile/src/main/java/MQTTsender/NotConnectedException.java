package MQTTsender;

public class NotConnectedException extends Exception{
    public NotConnectedException(){
        System.err.println("MQTT Not connected");
    }
}
