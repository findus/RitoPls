package FileEntry;

import LittleEndian.LeWord;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 11:53 To change this template use File |
 * Settings | File Templates.
 */

/**
 * Diese Klasse hält alle 4 Bytes langen Words in einer Liste bereit
 *
 */
public class FileEntryList {

    private long start;

    private long length;

    private ArrayList<FileEntry> fileList = new ArrayList<FileEntry>();

    private ArrayList<LeWord> worldList;

    public FileEntryList(ArrayList<LeWord> wordList) {
        //System.out.println("Initialisiere FileList");

        LeWord beinhaltetZeigerZuEinemOffset = wordList.get(3);
        long zeigerAufOffset = beinhaltetZeigerZuEinemOffset.getContent();
        int offset = (int) (zeigerAufOffset / 4);
        this.length = wordList.get(offset).getContent();

        this.start = offset + 1;
        this.worldList = wordList;

        for (int i = (int) this.start; i < ((this.length * 4) + this.start); i += 4) {
            fileList.add(new FileEntry(worldList.get(i), worldList.get(i + 1), worldList.get(i + 2), worldList.get(i + 3)));
        }

       // System.out.println("FileListe initialisiert(" + fileList.size() + " FileEntry-Elemente)");
    }

    public ArrayList<FileEntry> getFileList() {
        return fileList;
    }

    public long getLength() {
        return length;
    }
}
