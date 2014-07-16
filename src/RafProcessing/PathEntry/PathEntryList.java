package RafProcessing.PathEntry;

import LittleEndian.LeWord;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 11:54 To change this template use File |
 * Settings | File Templates.
 */
public class PathEntryList {

    private ArrayList<PathEntry> pathList = new ArrayList<PathEntry>();

    private ArrayList<LeWord> worldList;
    
    private ArrayList<Byte> bytefileList;
    
    private long count;

    private long pathListSize;

    private long start;

    public PathEntryList(ArrayList<LeWord> wordList, ArrayList<Byte> bytefileList) {
       // System.out.println("Initialisiere PathList");

        LeWord BeinhaltetZeigerZurPathList = wordList.get(4);
        long ZeigerAufOffset2 = BeinhaltetZeigerZurPathList.getContent();
        
        int Offset = (int) (ZeigerAufOffset2 / 4);
        this.pathListSize = wordList.get(Offset).getContent();
        this.count = wordList.get(Offset + 1).getContent();

        this.worldList = wordList;
        this.start = (((int) ZeigerAufOffset2 / 4) + 2);
        for (int i = (int) this.start; i < ((this.count * 2) + this.start); i += 2) {
            pathList.add(new PathEntry(worldList.get(i), worldList.get(i + 1),bytefileList,(start*4)));
        }

        //System.out.println("Pathliste initialisiert(" + pathList.size() + " Pathentry-Elemente)");
    }

    public long getCount() {
        return count;
    }

    public ArrayList<PathEntry> getPathList() {
        return pathList;
    }

}
