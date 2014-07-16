package Releasemanifest;

import LittleEndian.LeWord;
import Util.ArrayUtils;

import java.util.List;

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

    public ReleasemanifestFileEntry(ReleasemanifestFile file,List<Byte> content, long offset,int fileIndex)
    {
          this.relFile = file;
           this.content = ArrayUtils.objectArrayToByteArray(content.toArray());
        this.offsetEntry = offset;
        this.fileindex = fileIndex;

         //Raep
        this.nameIndex = new LeWord(content, (int) offsetEntry).getContent();
        this.name = relFile.getStringList().getStringList().get((int)offsetEntry-1);
        this.version = new LeWord(content,(int) offsetEntry+4).getContent();
        this.checkSum = "Später Implementieren";

        this.checkArray = new long[4];
        this.checkArray[0] = new LeWord(content,(int)offsetEntry + 8).getContent();
        this.checkArray[1] = new LeWord(content,(int)offsetEntry + 12).getContent();
        this.checkArray[2] = new LeWord(content,(int)offsetEntry + 16).getContent();
        this.checkArray[3] = new LeWord(content,(int)offsetEntry + 20).getContent();

        this.unKnkownData =   new LeWord(content,(int)offsetEntry + 24).getContent();
        this.uncompressedFilesize =   new LeWord(content,(int)offsetEntry + 28).getContent();
        this.compressedFilesize =   new LeWord(content,(int)offsetEntry + 32).getContent();
        this.unknownData2 =   new LeWord(content,(int)offsetEntry + 34).getContent();
        this.unknownData3 =   new LeWord(content,(int)offsetEntry + 38).getContent();
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
