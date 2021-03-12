package DBServer;

import java.io.*;

public class DBServer {

    public void inputFile(String name) {

        File fileToOpen;
        String filename, bufferline;

        String fileName = name;
        try {
            fileToOpen = new File(fileName);
            if (fileToOpen.exists()) {
                System.out.println("File" + fileName + "successfully opened.");
            } else {
                if(fileToOpen.createNewFile()){
                    System.out.println("File doesn't exist, new file" +fileName + "created.");
                }
            }
            FileReader reader = new FileReader(fileToOpen);
            BufferedReader br = new BufferedReader(reader);
            while((bufferline = br.readLine()) != null){
                System.out.println(bufferline);
            }
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("Can't read file.");
        } catch (IOException e) {
            System.out.println("Failed to create new file.");
        }
    }
}
