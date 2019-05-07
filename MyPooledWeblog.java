import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import static java.lang.Integer.parseInt;

/* Commands used to test this code were
javac MyPooledWeblog.java 
java MyPooledWeblog access_log 1/2/3

1 = number of times accessed by each remote host
2 = total bytes transmitted
3 = total bytes by each remotehost

*/

public class MyPooledWeblog {

    private final static int NUM_THREADS = 4;
    // Used to keep track of the bytes sent
    static int byteCounter;
    // Used to identify the user choice from 1, 2 or 3
    static int selection;
    // Delimiter used to seperate the string 
    static String delim = " ";
    // Hashmap used to order the key value pairs of the host:bytes sent/times accessed 
    static HashMap<String, Integer> keyValuePair = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        // Converting the user input from string to int
        selection = parseInt(args[1]);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));) {
            for (String entry = in.readLine(); entry != null; entry = in.readLine()) {
                // Keeping track of the index 
                int index = entry.indexOf(' ');
                String addressCount = entry.substring(0, index);
                String [] tokens = entry.split(delim);

                // Case switch selection depeding on the user's input
                switch(selection){
                    // If the user inputted 1, the program returns the amount of time
                    // The host accessed the site
                    case 1:
                        if(keyValuePair.get(addressCount) == null) {
                            keyValuePair.put(addressCount, 1);
                        } else {
                            Integer counter = keyValuePair.get(addressCount);
                            keyValuePair.put(addressCount, counter + 1);
                        }
                        break;
                    // If the host inputted 2, the program returns the total amount of bytes
                    // Sent by all hosts    
                    case 2:

                        try{
                            // Used to parse only the bytes in the string
                            byteCounter += parseInt(tokens[9]);
                        }catch (Exception e)
                        {
                            // If the number was invalid, a 0 was used in its place
                            byteCounter += 0;
                        }
                        break;
                    // If the user inputted 3, the program returns the amount of bytes sent 
                    // By each host    
                    case 3:

                        try{
                            // Used to parse only the bytes in the string
                            byteCounter = parseInt(tokens[9]);
                        }catch (Exception e)
                        {
                            // Used if there is no valid byte amount from the host
                            byteCounter = 0;
                        }
                        // Used if there was no valid byte amount, a 0 is used instead
                        if(keyValuePair.get(addressCount) == null) {
                            keyValuePair.put(addressCount, byteCounter);
                        } else {
                            // Gets the total amount of bytes sent from the address
                            Integer counter = keyValuePair.get(addressCount);
                            // Adds the current value to the total amount of bytes sent
                            keyValuePair.put(addressCount, counter + byteCounter);
                        }
                        break;
                    // When an invalid number is inputted, the program outputs the error message and ends
                    default:
                        System.out.println("Invalid Input, Only 1, 2 and 3 are valid");
                        System.exit(1);
                }

            }
        }

        // Used for mapping the set
        Set set;
        // Used for iterating through the set
        Iterator iterator;
        switch(selection) {
            case 1:
                // Declares a set variable based on the keyvaluepair hashmap
                set = keyValuePair.entrySet();
                // Sets the iterator to the beginning of the set
                iterator = set.iterator();
                // Iterates as long as the iterator has a next variable
                while(iterator.hasNext())
                {
                    // Sets a variable to start at the beginning of the map and iterates to the end
                    Map.Entry start = (Map.Entry)iterator.next();
                    // Outputs the Host Name and amonut of times it was accessed
                    System.out.println("Remote Host : " + start.getKey());
                    System.out.println("# of times accessed : " + start.getValue());
                }
                break;
            case 2:
                // Outputs the total amount of bytes sent by all hosts 
                System.out.println("Total Bytes Transmitted: " + byteCounter);
                break;
            case 3:
                // Declares a set variable based on the keyvaluepair hashmap
                set = keyValuePair.entrySet();
                // Sets the iterator to the beginning of the set
                iterator = set.iterator();
                // Iterates as long as the iterator has a next variable
                while(iterator.hasNext())
                {
                    // Sets a variable to start at the beginning of the map and iterates to the end
                    Map.Entry start = (Map.Entry)iterator.next();
                    // Outputs the host name and amount of bytes that were transferred
                    System.out.println("Host Name : " + start.getKey()); 
                    System.out.println("# Bytes transmitted : " + start.getValue());
                }
                break;
        }
        
    }

}

