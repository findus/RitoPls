package RafProcessing.RAF;

import RafProcessing.ArchiveFile.ArchiveFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philipp on 11.07.2014.
 */

/**
 * Diese Klasse managed das ziemlich randomhafte Auftreten der Filearchive im LoL Ordner
 */
public class RAFFileList {
    Map<String,ArrayList<RAFFile>> archives;
    private final String BASEFOLDER = "RADS\\projects\\lol_game_client\\filearchives";
    private final String LOLDIRECTORY;
    private final File LOLFOLDER;

    public RAFFileList(String LeagueOfLegendsPath) throws IOException {
        archives = new HashMap<String, ArrayList<RAFFile>>();
        LOLDIRECTORY = LeagueOfLegendsPath;
        LOLFOLDER = new File(LOLDIRECTORY+BASEFOLDER);

        System.out.println("Scan all Archives...");
        scanArchives();

        System.out.println("Done");
    }

    private void scanArchives() throws IOException {
        for(File temp : LOLFOLDER.listFiles())
        {
            ArrayList<RAFFile> list = new ArrayList<RAFFile>();
            for(File file : temp.listFiles())
            {
                if(file.getName().endsWith(".raf"))
                {
                    list.add(new RAFFile(file.getAbsolutePath()));
                }
            }
            archives.put(temp.getName(),list);
        }
    }


    public RAFFile  replaceRessource(String input) throws IOException {
        RAFFile retVal = null;
        outtaloop:
        for(Map.Entry<String,ArrayList<RAFFile>> file : archives.entrySet())
        {
            for(RAFFile rafFile : file.getValue())
            {
                if(rafFile.searchForRessource(input) != null)
                {
                    System.out.println("Ressource location found in Archive: " + rafFile.getRafArchiv());
                    rafFile.repack(input);
                    retVal = rafFile;
                    break outtaloop;
                }
            }
        }
        return retVal;
    }

    public RAFFile findRessource(String input) throws IOException {
        RAFFile retVal = null;
        outtaloop:
        for(Map.Entry<String,ArrayList<RAFFile>> file : archives.entrySet())
        {
            for(RAFFile rafFile : file.getValue())
            {
                if(rafFile.searchForRessource(input) != null)
                {
                    System.out.println("Ressource location found in Archive: " + rafFile.getRafArchiv());
                    retVal = rafFile;
                    break outtaloop;
                }
            }
        }
        return retVal;
    }

    public void extractArchive(RAFFile file) throws IOException {
        file.extractEverything();
    }

    public void dumpStrings(String filter) throws FileNotFoundException {
        System.out.println("Dumping Strings");

        PrintWriter writer = new PrintWriter("Ressources.txt");
        for(Map.Entry<String,ArrayList<RAFFile>> file : archives.entrySet())
        {
            for(RAFFile rafFile : file.getValue()) {
                for(ArchiveFile archiveFile : rafFile.getArchiveFiles().getArchiveFiles())
                {
                    String filepath = archiveFile.getFilePath().toLowerCase();
                    String ttest = filter.toLowerCase();
                    if(filepath.contains(ttest))
                    {
                        writer.write(archiveFile.getFilePath()+"\n");
                        writer.flush();
                    }
                }
            }

        }
        writer.close();
    }
}
