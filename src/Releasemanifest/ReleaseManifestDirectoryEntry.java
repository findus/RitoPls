package Releasemanifest;

import java.util.List;

/**
 * Created by philipp on 16.07.2014.
 */
public class ReleaseManifestDirectoryEntry {

    private byte[] directoryFileContent;
    private long offsetEntry;
    private ReleasemanifestFile relFile;
    private long nameIndex;
    private long subDirCount;
    private long subDirFirstIndex;
    private long fileStartOffset;
    private long fileCount;
    private String name;
    private  ReleaseManifestDirectoryEntry relDirecttoryEntry;
    private List<ReleasemanifestFileEntry> relFileEntry;
    private int entryNum;



    public String getFullPath() {
        return null;
    }
}
