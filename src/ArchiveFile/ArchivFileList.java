package ArchiveFile;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 09.07.14
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class ArchivFileList {

    public ArrayList<ArchiveFile> getArchiveFiles() {
        return archiveFiles;
    }

    private ArrayList<ArchiveFile> archiveFiles = new ArrayList<ArchiveFile>();



    public void readjustOffsets(ArchiveFile startArchiv)
    {
        ArchiveFile before = null;
        for(ArchiveFile file : archiveFiles)
        {
            if(file.getOffset() > startArchiv.getOffset() && before != null)
            {
                long newOffset = before.getOffset()+before.getLength();
                file.setOffset(newOffset);
            }
            before = file;
        }
    }

}
