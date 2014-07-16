package Releasemanifest;

import LittleEndian.LeWord;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private List<Byte> filebyteList = new ArrayList<Byte>();
    private LeWord direcotryCunt;

    private ReleaseManifestStringList stringList;
    private ReleaseManifestDirectoryList dirList;
    private ReleasemanifestFileList fileList;



    ArrayList<ManifestEntry> entries;

    public ReleasemanifestFile(String releasemanifestFile) throws IOException {
        FileInputStream reader = new FileInputStream(releasemanifestFile);
        byte[] sanic = new byte[1];
        int offset = 0;
        while (reader.read(sanic, 0, 1) != -1) {
            //wordList.add(new LeWord((sanic)));
            filebyteList.add(sanic[0]);
            offset += 1;
        }

        this.magic = new LeWord(filebyteList,0);
        this.fileType = new LeWord(filebyteList,4);
        this.numberItems = new LeWord(filebyteList,8);
        this.version = new LeWord(filebyteList,12);
        this.direcotryCunt = new LeWord(filebyteList,16);


        long fileheaderlocation = 16 + 4 + (this.direcotryCunt.getContent()*20);
        LeWord fileCount = new LeWord(filebyteList,(int)fileheaderlocation);
        long stringheaderlocation = fileheaderlocation +4 + (fileCount.getContent()*44);

        this.stringList = new ReleaseManifestStringList(this,filebyteList,stringheaderlocation);
        this.fileList = new ReleasemanifestFileList(this,filebyteList,stringheaderlocation);
        this.dirList = new ReleaseManifestDirectoryList(this,filebyteList,fileheaderlocation);
    }

    public ReleaseManifestStringList getStringList() {
        return stringList;
    }

    public void createHeader()
    {

    }

    public static void main(String[] args) throws IOException {
        new ReleasemanifestFile("releasemanifest");
    }

}

