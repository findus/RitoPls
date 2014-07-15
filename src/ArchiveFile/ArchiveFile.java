package ArchiveFile;

import LittleEndian.LeWord;
import PathEntry.PathEntryList;
import FileEntry.FileEntry;


/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 09.07.14
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */

/**
 * Diese Klasse legt die Informationen der Patth und PathEntry List zusammen und stellt sie zur weiteren Verarbeitung zur Verfügung
 */
public class ArchiveFile implements Comparable<ArchiveFile> {

    private ArchivFileList parent;
    //FileName
    private String filePath;

    //Raf Dat data
    private LeWord offset;
    private LeWord length;

    //RawCompressedData
    private byte[] data;

    private ArchiveFile() {};

    public ArchiveFile(FileEntry fileEntry, PathEntryList pathList,byte[] rawData) {
        this.offset = fileEntry.getDataOffset();
        this.length = fileEntry.getDataSize();
        this.filePath = pathList.getPathList().get((int)fileEntry.getPathListIndex().getContent()).getPath();
        this.data = rawData;
    }

    public ArchiveFile(FileEntry fileEntry, PathEntryList pathList) {
        this.offset = fileEntry.getDataOffset();
        this.length = fileEntry.getDataSize();
        this.filePath = pathList.getPathList().get((int)fileEntry.getPathListIndex().getContent()).getPath();
    }

    public String getFilePath() {
        return filePath;
    }

    public long getLength() {
        return length.getContent();
    }

    public long getOffset() {
        return offset.getContent();
    }

    public void setOffset(long offset) {this.offset.setContent(offset);
    }

    public void setLength(long length) {
        this.length.setContent(length);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data.clone();
        this.length.setContent(data.length);

    }




    @Override
    public int compareTo(ArchiveFile o) {
        if(this.getOffset() > o.getOffset())
            return 1;
        if(this.getOffset() < o.getOffset())
            return -1;
        return 0;
    }
}
