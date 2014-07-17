package Releasemanifest;

import LittleEndian.LeWord;
import Util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public ReleaseManifestStringList(ReleasemanifestFile file,byte[] content,long offset)
    {
        this.relFile = file;
        //UWOTM8
        this.content = content;
        this.stringCount = new LeWord(content,(int)offset).getContent();
        this.sizeOfData = new LeWord(content,(int)offset+4).getContent();

        byte[] stringBytes = Arrays.copyOfRange(this.content,(int)offset+8,(int)offset+8+(int)sizeOfData);
        String allStrigns = ArrayUtils.encodeByteArrayToString(stringBytes);
        ArrayUtils.addArrayToList(stringList,allStrigns.split("\0"));
    }


    public List<String> getStringList() {
        return stringList;
    }




    public byte[] getBytes()
    {
       List<Long> result = new ArrayList<Long>();
        result.add(stringCount);
        result.add(sizeOfData);
        int sd4 = (int)sizeOfData/4;
        byte[] resultOutput = new byte[(result.size()*4)+(int)sizeOfData];
        for(int i = 0; i < result.size();i++)
        {
           byte[] temp = ArrayUtils.longToByteArray(result.get(i));
           ArrayUtils.insertArrayInAnotherArray(resultOutput,temp,i*4,4);
        }

        String[] temp = stringList.toArray(new String[stringList.size()]);
        String fullString ="\0"+String.join("\0",temp)+"\0";
        byte[] stringBytes =  fullString.getBytes();
        ArrayUtils.insertArrayInAnotherArray(resultOutput,stringBytes,result.size()*4,4);
        return resultOutput;
    }



}
