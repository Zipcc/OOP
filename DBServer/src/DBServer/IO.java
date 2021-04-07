package DBServer;

import java.io.*;
import java.util.*;

public class IO {

    private static String currentFolder;

    public boolean createFolder(String folderName) {

        File fileToOpen = new File("." + File.separator + folderName);
        if (fileToOpen.exists()) {
            return false;
        } else return fileToOpen.mkdir();
    }

    public boolean createFile(String fileName) {

        File fileToOpen = new File(currentFolder + File.separator + fileName);
        if (fileToOpen.exists()) {
            return false;
        } else {
            try {
                if(fileToOpen.createNewFile()) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean openFolder(String folderName) {

        File fileToOpen = new File("." + File.separator + folderName);
        if (fileToOpen.exists()) {
            currentFolder = fileToOpen.getPath();
            return true;
        } else {
            return false;
        }
    }

    // If folder not exist, return false.
    public boolean deleteFolder(String folderName) {

        File folderToDelete = new File("." + File.separator + folderName);
        if (!folderToDelete.exists()) {
            return false;
        }
        if(folderToDelete.list() == null){
            if(!folderToDelete.delete()){
                return false;
            }
            System.out.println("Delete ---> "+ folderToDelete);
            return true;
        }
        File[] files = folderToDelete.listFiles();
        if (files != null) {
            for(File file: files){
                if(!file.delete()){
                    return false;
                }
                System.out.println("Delete ---> "+ file.getName());
            }
        }
        if(!folderToDelete.delete()){
            return false;
        }
        System.out.println("Delete ---> "+ folderToDelete.getName());
        return true;
    }

    // If file not exist, return false.
    public boolean deleteFile(String fileName) {

        File fileToDelete = new File(currentFolder + File.separator + fileName);
        if (!fileToDelete.exists()) {
            return false;
        }
        if(fileToDelete.delete()){
            System.out.println("Delete ---> "+ fileToDelete);
            return true;
        }
        return false;
    }

    // Return a table read from file.
    public Table inputFile(String fileName) {

        String bufferLine;
        ArrayList<String> arrayList;
        File fileToOpen = new File(currentFolder + File.separator + fileName);

        if(!fileToOpen.exists()){
            return null;
        }
        System.out.println("inputFile:" + fileName + " successfully opened.");

        try(FileReader reader = new FileReader(fileToOpen);
            BufferedReader br = new BufferedReader(reader)){
            if((bufferLine = br.readLine()) != null){
                Table table = new Table(fileName);
                arrayList = new ArrayList<>();
                Collections.addAll(arrayList,bufferLine.split("\t"));
                arrayList.remove(0);
                // Insert attributes into row No.0 (columnNames)
                // Ignore attribute "id" input from file as new table has one itself.
                System.out.println("Row input from file --> " + bufferLine);
                table.insertColumns(arrayList);
                // Value rows...
                while((bufferLine = br.readLine()) != null){
                    System.out.println("Row input from file --> " + bufferLine);
                    arrayList.clear();
                    Collections.addAll(arrayList, bufferLine.split("\t"));
                    table.inputRow(arrayList);
                }
                return table;
            }else{
                return null;
            }
        } catch (IOException e) {
            System.out.println("Failed to read file.");
            return null;
        } finally {
            System.out.println(fileName + " closed.");
        }
    }

    // Return number of rows successfully written into file.
    public int outputFile(String fileName, Table table) {

        String bufferLine;
        Row row;
        int i=0;// Number of rows written into file.
        File fileToOpen = new File(currentFolder + File.separator + fileName);

        if (fileToOpen.exists()) {
            System.out.println("outputFile:" + fileName + " successfully opened.");
        }else{
            System.out.println("outputFile:" + fileName + " not exist, open failed.");
            return 0;
        }

        try(FileWriter writer = new FileWriter(fileToOpen);
            BufferedWriter bw = new BufferedWriter(writer)) {
            // Write into file.
            while((row = table.getRow(i)) != null){
                bufferLine = row.toString();
                bw.write(bufferLine);
                bw.flush();
                System.out.println(bufferLine + "--> written into file.");
                i++;
            }
            return i;
        } catch (IOException e){
            System.out.println("Failed to write file.");
            return 0;
        } finally {
            System.out.println(fileName + " closed.");
        }
    }
}
