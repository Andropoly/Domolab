package MQTTsender;

class FailToConnectMQTTException extends Throwable {
    public FailToConnectMQTTException(){
        System.err.println("MQTT already connected");
    }
}
