// Publisher.java

/* librerie I/O Paho */
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/* librerie I/O per l'input da tastiera */
import java.io.*;

public class Publisher {
	
	public static void main(String[] args) throws IOException {
		/* Impostazioni Mqtt */
        String topic = "Sistemi Immersi";		//pubblico in un topic specifico
        int _QoS = 1;							//qualità del servizio
        String broker = "tcp://localhost:1883";	//indirizzo del broker
        String clientId = "EDITORE";			//Identificativo del client
        //String username = "";					//Username broker
        //String password = "";					//Password broker
        MemoryPersistence persistence = new MemoryPersistence();
        
        /* Inizializzo il Buffer/Input da tastiera */
        BufferedReader stdIn= null;

        try {
        	//creo un Mqtt Client
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions modalita_connessione = new MqttConnectOptions();
            modalita_connessione.setCleanSession(true);
            //modalita_connessione.setPassword(password.toCharArray());
            //modalita_connessione.setUserName(username);
            
            //mi connetto al broker
            System.out.println("Connessione al broker: " + broker);
            client.connect(modalita_connessione);
            System.out.println("Publisher-> Connesso");
            
            // creazione stream (canale) di input da tastiera
 			stdIn = new BufferedReader(new InputStreamReader(System.in));
 			String userInput;
 			
 			System.out.println("Publisher-> Canale di I/O creati.");
			System.out.println("Publisher-> E' ora possibile scrivere i messaggi per il broker.\n");
            
			/* Pubblicazione messaggi */
			
			// ciclo di lettura da tastiera, invio al broker
			while (true){
				System.out.print("Messaggio: ");
				
				//leggo una stringa da tastiera
				userInput = stdIn.readLine();
				
				//se la stringa da tastiera vale "END"
				if (userInput.equals("END"))	//se la stringa da tastiera è "END"
					break;						//interrompo il ciclo di comunicazione
				
				//creo un messaggio Mqtt
                MqttMessage messaggio = new MqttMessage(userInput.getBytes());
                
                //imposto la qualità del messaggio
                messaggio.setQos(_QoS);
                
                //pubblico il messaggio
                client.publish(topic, messaggio);
                System.out.println("Publisher-> Il messaggio e' stato pubblicato\n");
			}
	        
	        // terminata la pubblicazione, posso disconnettermi
            client.disconnect();
            System.out.println("Publisher-> Disconnesso");
            //stdIn.close();
            //System.exit(0);
            
        } 
        catch(MqttException me) {
            System.out.println("Ragione disconnessisone: " + me.getReasonCode());
            System.out.println("Messaggio: " + me.getMessage());
            System.out.println("Locale: " + me.getLocalizedMessage());
            System.out.println("Causa: " + me.getCause());
            System.out.println("Eccezione: "+ me);
            me.printStackTrace();
        }
        
        // se il ciclo di comunicazione è stato interrotto,
     	// eseguo la chiusura dello stream
        stdIn.close();
        System.out.println("Publisher-> Arresto completato");
    }
}

// Publisher
