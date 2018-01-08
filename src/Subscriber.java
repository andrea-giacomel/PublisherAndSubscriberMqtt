// Subscriber.java

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Subscriber {
	
	public static void main(String[] args) {
		/* Impostazioni Mqtt */
		String topic = "#";						//mi sottoscrivo a qualsiasi topic
		//String topic = "Sistemi Immersi";		//mi sottoscrivo ad un topic specifico
		int _QoS = 1;							//qualità del servizio
        String broker = "tcp://localhost:1883";	//indirizzo del broker
        String clientId = "ISCRITTO";			//Identificativo del client
        //String username = "";					//Username broker
        //String password = "";					//Password broker
        MemoryPersistence persistence = new MemoryPersistence();
        
		try {
			//creo un Mqtt Client
			MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions modalita_connessione = new MqttConnectOptions();
            modalita_connessione.setCleanSession(true);
            //modalita_connessione.setPassword(password.toCharArray());
            //modalita_connessione.setUserName(username);
            
            //mi connetto al broker
            System.out.println("Connessione al broker: "+broker);
            client.connect(modalita_connessione);
            System.out.println("Subscriber-> Connesso");
            
            //mi iscrivo al topic con la qualità impostata
            client.subscribe(topic, _QoS);
            
            //Callback: classe interna per ricevere messaggi
            client.setCallback(new MqttCallback() {

				@Override //fa l'overload di alcuni operatori 
				public void connectionLost(Throwable arg0) {
				}
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
				}
				@Override
				public void messageArrived(String topic, MqttMessage messaggio) throws Exception {
					
					System.out.println("[Topic: " + topic + "]  Messaggio: " + messaggio);
					
				}
			});		
            
		}	
		catch(MqttException me) {
            System.out.println("Ragione disconnessisone: " + me.getReasonCode());
            System.out.println("Messaggio: " + me.getMessage());
            System.out.println("Locale: " + me.getLocalizedMessage());
            System.out.println("Causa: " + me.getCause());
            System.out.println("Eccezione: "+ me);
            me.printStackTrace();
        }			
	}
}	

// Subscriber