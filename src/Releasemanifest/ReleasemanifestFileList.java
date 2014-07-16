package Releasemanifest;

import LittleEndian.LeWord;
import Util.ArrayUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 16.07.14
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class ReleasemanifestFileList {

    private ReleasemanifestFile relFile;
    private byte[] content;
    long fileListCount;
    private long offset;
    private List<ReleasemanifestFileEntry> fileentrylist;

    public ReleasemanifestFileList(ReleasemanifestFile file,List<Byte> content,long offset)
    {
        this.relFile  = file;
        this.content = ArrayUtils.objectArrayToByteArray(content.toArray());
        this.offset = offset;

        this.fileListCount = new LeWord(content,(int)offset).getContent();



    }





}
