package Releasemanifest;

import LittleEndian.LeWord;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
    Logger logger = Logger.getRootLogger();

    public ReleasemanifestFileList(ReleasemanifestFile file,byte[] content,long offset)
    {
        this.relFile  = file;
        this.content = content;
        this.offset = offset;

        this.fileListCount = new LeWord(content,(int)offset).getContent();

        long offsetEntiresStart = offset + 4;
        this.fileentrylist = new ArrayList<ReleasemanifestFileEntry>();
        int entryIndexCounter = 0;

        for(long currentOffset = offsetEntiresStart; currentOffset < offsetEntiresStart + 44 * fileListCount; currentOffset+= 44)
        {
            this.fileentrylist.add(new ReleasemanifestFileEntry(relFile,content,currentOffset,entryIndexCounter++));
        }
    }

    //IN userem Fall true
    public List<ReleasemanifestFileEntry> searchFileEntrys(String partialPath,boolean strict)
    {
        String lowerPath = partialPath.toLowerCase();
        List<ReleasemanifestFileEntry> result = new ArrayList<ReleasemanifestFileEntry>();
        List<ReleasemanifestFileEntry> fileEntryList = this.fileentrylist;
        List<Integer> foundIndexes = new ArrayList<Integer>();

        for (int i = 0; i < fileEntryList.size(); i++)
        {
            String lowerFilename = fileEntryList.get(i).getName().toLowerCase();
            logger.info(lowerFilename);

            if (lowerFilename.endsWith(lowerPath))
            {
                if (!strict || lowerFilename == lowerPath)
                {
                    result.add(fileEntryList.get(i));
                    foundIndexes.add(i);
                }
            }
        }

        for(int i = 0 ; i < fileEntryList.size();i++)
        {
            String lowerFileNaem =  fileEntryList.get(i).getPathAndName().toLowerCase();
            if(lowerFileNaem.endsWith(lowerPath))
            {
                if(!strict || lowerFileNaem == lowerPath || lowerFileNaem == ("/" + lowerPath))
                {
                    if(!foundIndexes.contains(i))
                    {
                        result.add(fileEntryList.get(i));
                    }
                }
            }
        }

        return result;
    }

    public List<Long> getLongArray()
    {
        List<Long> result = new ArrayList<Long>();
        for(ReleasemanifestFileEntry entry : fileentrylist)
        {
            result.addAll(entry.getLongArray());
        }
        return result;
    }





}
