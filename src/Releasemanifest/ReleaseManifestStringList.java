package Releasemanifest;

import LittleEndian.LeWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Util.ArrayUtils;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 16.07.14
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseManifestStringList {
    private ReleasemanifestFile relFile;
    private byte[] content;
    private long stringCount;
    private long sizeOfData;
    private List<String> stringList = new ArrayList<String>();

    public ReleaseManifestStringList(ReleasemanifestFile file, List<Byte> content,long offset)
    {
        this.relFile = file;
        //UWOTM8
        this.content =  ArrayUtils.objectArrayToByteArray(content.toArray());
        this.stringCount = new LeWord(content,(int)offset).getContent();
        this.sizeOfData = new LeWord(content,(int)offset+4).getContent();

        byte[] stringBytes = Arrays.copyOfRange(this.content,(int)offset+8,(int)offset+8+(int)sizeOfData);
        String allStrigns = ArrayUtils.encodeByteArrayToString(stringBytes);
        ArrayUtils.addArrayToList(stringList,allStrigns.split("\0"));
    }



    public void getBytes()
    {
        //TODO REINCOEDN
    }



}
