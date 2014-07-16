import RafProcessing.RAF.RAFFile;
import RafProcessing.RAF.RAFFileList;

import java.io.IOException;
import java.util.Date;

/**
 * Created by philipp on 05.07.2014.
 */
public class main {
    public static void main(String[] args) throws IOException, InterruptedException {


        Date date = new Date();
        // wu t de fk ac is en deterbaes!?!?
        //RAFFile file = new RAFFile("Archive_1.raf");
        //file.repack("AkaliLoadScreen_1.DDS");
        //file.extractEverything();

        RAFFileList list = new RAFFileList("C:\\Users\\philipp.hentschel\\Documents\\Java\\Workspace\\raftest\\");
        RAFFile e = list.findRessource("LuxLoadScreen.dds");
        e.repack("LuxLoadScreen.dds");
        e.extractEverything();
        Date date2 = new Date();

        long diff = date2.getTime() - date.getTime();
        long diffSeconds = diff / 1000 % 60;

        System.out.println("Time: " + diffSeconds +" Seconds");

    }
}
