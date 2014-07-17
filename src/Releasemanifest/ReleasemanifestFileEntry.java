package Releasemanifest;

import LittleEndian.LeWord;

import java.util.ArrayList;
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
    private LeWord[] checkArray;

    private long unKnkownData;
    private long uncompressedFilesize;

    public long getCompressedFilesize() {
        return compressedFilesize;
    }

    public long getUncompressedFilesize() {
        return uncompressedFilesize;
    }

    public void setCompressedFilesize(long compressedFilesize) {
        this.compressedFilesize = compressedFilesize;
    }

    public void setUncompressedFilesize(long uncompressedFilesize) {
        this.uncompressedFilesize = uncompressedFilesize;
    }

    private long compressedFilesize;
    private long unknownData2;
    private long unknownData3;
    private int fileindex;

    public String getName() {
        return name;
    }

    private String name;
    private ReleaseManifestDirectoryEntry directoryEntry;

    public ReleasemanifestFileEntry(ReleasemanifestFile file,byte[] content, long offset,int fileIndex)
    {
        this.relFile = file;
        this.content = content;
        this.offsetEntry = offset;
        this.fileindex = fileIndex;

         //Raep
        this.nameIndex = new LeWord(content, (int) offsetEntry).getContent();
        this.name = relFile.getStringList().getStringList().get((int)nameIndex-1);
        this.version = new LeWord(content,(int) offsetEntry+4).getContent();
        //this.checkSum = new LeWord(content,(int) offsetEntry+8,16).getString();

        this.checkArray = new LeWord[4];
        this.checkArray[0] = new LeWord(content,(int)offsetEntry + 8);
        this.checkArray[1] = new LeWord(content,(int)offsetEntry + 12);
        this.checkArray[2] = new LeWord(content,(int)offsetEntry + 16);
        this.checkArray[3] = new LeWord(content,(int)offsetEntry + 20);
        this.checkSum = this.checkArray[0].getHexString() + this.checkArray[1].getHexString() + this.checkArray[2].getHexString() + this.checkArray[3].getHexString();

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

    public List<Long> getLongArray()
    {
        List<Long> result = new ArrayList<Long>();
        result.add(nameIndex);
        result.add(version);
        result.add(checkArray[0].getContent());
        result.add(checkArray[1].getContent());
        result.add(checkArray[2].getContent());
        result.add(checkArray[3].getContent());
        result.add(unKnkownData);
        result.add(uncompressedFilesize);
        result.add(compressedFilesize);
        result.add(unknownData2);
        result.add(unknownData3);
        return result;
    }
}
