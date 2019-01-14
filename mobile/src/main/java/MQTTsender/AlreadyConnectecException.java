package MQTTsender;

public class AlreadyConnectecException extends Exception{
    public AlreadyConnectecException(){
        System.err.println("MQTT already connected");
    }
}
