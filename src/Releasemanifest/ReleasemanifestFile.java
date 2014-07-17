package Releasemanifest;

import LittleEndian.LeWord;
import Util.ArrayUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 16.07.14
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class ReleasemanifestFile {

    private LeWord magic;
    private LeWord version;
    private LeWord fileType;
    private LeWord numberItems;
    private byte[] filebyteList;
    private LeWord direcotryCunt;

    private ReleaseManifestStringList stringList;
    private ReleaseManifestDirectoryList dirList;
    private ReleasemanifestFileList fileList;



    ArrayList<ManifestEntry> entries;

    public ReleasemanifestFile(String releasemanifestFile) throws IOException {
        FileInputStream reader = new FileInputStream(releasemanifestFile);
        byte[] sanic = new byte[1];
        int offset = 0;
        filebyteList = Files.readAllBytes(Paths.get(releasemanifestFile));

        this.magic = new LeWord(filebyteList,0);
        this.fileType = new LeWord(filebyteList,4);
        this.numberItems = new LeWord(filebyteList,8);
        this.version = new LeWord(filebyteList,12);
        this.direcotryCunt = new LeWord(filebyteList,16);


        long fileheaderlocation = 16 + 4 + (this.direcotryCunt.getContent()*20);
        LeWord fileCount = new LeWord(filebyteList,(int)fileheaderlocation);
        long stringheaderlocation = fileheaderlocation +4 + (fileCount.getContent()*44);

        this.stringList = new ReleaseManifestStringList(this,filebyteList,stringheaderlocation);
        this.dirList = new ReleaseManifestDirectoryList(this,filebyteList,16);
        this.fileList = new ReleasemanifestFileList(this,filebyteList,fileheaderlocation);
        System.out.println("Fertig");
    }

    public boolean adjustSizeByFile(String fileName,String rafFile) throws IOException {
        return adjustSizeByBytes(Files.readAllBytes(Paths.get(fileName)),rafFile);
    }

    public boolean adjustSizeByBytes(byte[] content,String rafFile) throws IOException {
        int uncompressedsize = content.length;
        ByteOutputStream byteStream = new ByteOutputStream();
        DeflaterOutputStream compressStream = new DeflaterOutputStream(byteStream);
        compressStream.write(content,0,content.length);
        compressStream.close();
        int compressedsize = byteStream.size();
        return adjstSizeByLocation(rafFile,uncompressedsize,compressedsize);

    }

    public boolean adjstSizeByLocation(String rafLocationName,int uncompressedsize,int compressedsize)
    {
        List<ReleasemanifestFileEntry> entries = fileList.searchFileEntrys(rafLocationName,true);
        if(entries.size() > 0)
        {
            ReleasemanifestFileEntry entry = entries.get(0);
            return adjustSize(entry,uncompressedsize,compressedsize);
        }
        return false;
    }

    public boolean adjustSize(ReleasemanifestFileEntry entry, int uncompressedsize, int compressedsize)
    {
        if (entry.getUncompressedFilesize() == entry.getCompressedFilesize())
        {
            entry.setUncompressedFilesize(uncompressedsize);
            entry.setCompressedFilesize(uncompressedsize);
        }
        else
        {
            entry.setUncompressedFilesize(uncompressedsize);
            entry.setCompressedFilesize(compressedsize);
        }
        return true;
    }


    public void saveFile(String filename) throws IOException {
        FileOutputStream stream = new FileOutputStream(filename);
        stream.write(getBytes());
        stream.close();
    }

    private byte[] getBytes()
    {
        List<Long> result = new ArrayList<Long>();
        result.add(magic.getContent());
        result.add(fileType.getContent());
        result.add(numberItems.getContent());
        result.add(version.getContent());

        result.addAll(dirList.getLongArray());
        result.addAll(fileList.getLongArray());
        byte[] stringBytes = stringList.getBytes();

        byte[] resultOutput = new byte[(result.size()*4)+stringBytes.length];
        for(int i = 0; i < result.size(); i++)
        {
            byte[] temp = ArrayUtils.longToByteArray(result.get(i));
            ArrayUtils.insertArrayInAnotherArray(resultOutput,temp,i*4,4);
        }
        ArrayUtils.insertArrayInAnotherArray(resultOutput,stringBytes,result.size()*4,4);
        return resultOutput;

    }

    public ReleaseManifestStringList getStringList() {
        return stringList;
    }

    public void createHeader()
    {

    }

    public static void main(String[] args) throws IOException {
        ReleasemanifestFile lol = new ReleasemanifestFile("releasemanifest");
    }

}

