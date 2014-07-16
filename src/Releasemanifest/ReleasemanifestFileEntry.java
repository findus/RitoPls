package Releasemanifest;

import LittleEndian.LeWord;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 16.07.14
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public class ReleasemanifestFileEntry {

    private byte[] content;
    private long offsetEntry;
    private ReleasemanifestFile relFile;
    private long nameIndex;
    private long version;
    private String checkSum;
    private long[] checkArray;

    private long unKnkownData;
    private long uncompressedFilesize;
    private long compressedFilesize;
    private long unknownData2;
    private long unknownData3;
    private int fileindex;
    private String name;
    private ReleaseManifestDirectoryEntry directoryEntry;

    public ReleasemanifestFileEntry(ReleasemanifestFile file,byte[] content, long offset,int fileIndex)
    {
        this.relFile = file;
        this.content = content;
        this.offsetEntry = offset;
        this.fileindex = fileIndex;

         //Raep
        this.nameIndex = new LeWord(content, (int) offsetEntry,4).getContent();
        this.name = relFile.getStringList().getStringList().get((int)nameIndex-1);
        this.version = new LeWord(content,(int) offsetEntry+4,4).getContent();
        this.checkSum = new LeWord(content,(int) offsetEntry+4,16).getString();

        this.checkArray = new long[4];
        this.checkArray[0] = new LeWord(content,(int)offsetEntry + 8,4).getContent();
        this.checkArray[1] = new LeWord(content,(int)offsetEntry + 12,4).getContent();
        this.checkArray[2] = new LeWord(content,(int)offsetEntry + 16,4).getContent();
        this.checkArray[3] = new LeWord(content,(int)offsetEntry + 20,4).getContent();

        this.unKnkownData =   new LeWord(content,(int)offsetEntry + 24,4).getContent();
        this.uncompressedFilesize =   new LeWord(content,(int)offsetEntry + 28,4).getContent();
        this.compressedFilesize =   new LeWord(content,(int)offsetEntry + 32,4).getContent();
        this.unknownData2 =   new LeWord(content,(int)offsetEntry + 34,4).getContent();
        this.unknownData3 =   new LeWord(content,(int)offsetEntry + 38,4).getContent();
    }

    public String getPathAndName()
    {
        String path = "";
        if(this.directoryEntry != null)
        {
            path = this.directoryEntry.getFullPath();
        }
        return path + "/"+ name;
    }
}
