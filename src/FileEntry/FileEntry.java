package FileEntry; /**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 11:02 To change this template use File |
 * Settings | File Templates.
 */


import LittleEndian.LeWord;

/**
 * Der Fileentry beinhaltet Informationen zur Größe und Lage einer Ressource in Riots Filearchiv
 */
public class FileEntry {

    private LeWord PathHash;

    private LeWord DataOffset;

    private LeWord DataSize;

    private LeWord PathListIndex;

    public FileEntry(LeWord PathHash, LeWord DataOffset, LeWord DataSize, LeWord PathListIndex) {
        this.PathHash = PathHash;
        this.DataOffset = DataOffset;
        this.DataSize = DataSize;
        this.PathListIndex = PathListIndex;
    }

    public LeWord getPathListIndex() {
        return PathListIndex;
    }

    public LeWord getPathHash() {
        return PathHash;
    }

    public LeWord getDataOffset() {
        return DataOffset;
    }

    public LeWord getDataSize() {
        return DataSize;
    }

}
