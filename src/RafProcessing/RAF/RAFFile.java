package RafProcessing.RAF;


import RafProcessing.ArchiveFile.ArchivFileList;
import RafProcessing.ArchiveFile.ArchiveFile;
import RafProcessing.FileEntry.FileEntryList;
import LittleEndian.LeWord;
import RafProcessing.PathEntry.PathEntryList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.DeflaterInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 08:10 To change this template use File |
 * Settings | File Templates.
 */

/**
 * NSFL
 */
public class RAFFile {

    public String getRafArchiv() {
        return rafArchiv;
    }

    private String rafArchiv;

    private FileEntryList fileList;

    private PathEntryList pathList;

    private ArrayList<LeWord> wordList = new ArrayList<LeWord>();

    private ArrayList<Byte> filebyteList = new ArrayList<Byte>();

    private ArchivFileList archiveFiles = new ArchivFileList();


    public ArchivFileList getArchiveFiles() {
        return archiveFiles;
    }

    /**
     * Die Klasse RAFfile verwaltet die Daten eines Filearchives, sie instantziert eine Liste aus Filearchiven und stellt Methoden zum (Re)Packen des Archives bereit
     * @param rafArchiv
     * @throws IOException
     */
    public RAFFile(String rafArchiv) throws IOException {
        //System.out.println("Initialisiere RAFFile");
        this.rafArchiv = rafArchiv;
        FileInputStream stream = new FileInputStream(rafArchiv);
        int offset = 0;
        byte word[] = new byte[4];
        // Sämtliche Offsets werden eingelesen und als Klasse in eine Liste geschrieben
        while (stream.read(word, 0, 4) != -1) {
            wordList.add(new LeWord((word)));
            filebyteList.add(word[0]);
            filebyteList.add(word[1]);
            filebyteList.add(word[2]);
            filebyteList.add(word[3]);

            offset += 4;
        }
        //System.out.println("Lesen abgeschlossen (" + wordList.size() + " W�rter gelesen)");
        fileList = new FileEntryList(wordList);
        pathList = new PathEntryList(wordList, filebyteList);
        for (int i = 0; i < fileList.getLength(); i++) {
            archiveFiles.getArchiveFiles().add(new ArchiveFile(fileList.getFileList().get(i), pathList));
        }

        Collections.sort(archiveFiles.getArchiveFiles());

        //System.out.println("RAFFile initialisiert");
    }

    /**
     * Lädt Byteweise alle Daten in den Arbeitsspeicher, diese Methode ist eigentlich überflüssig und verbraucht auf Dauer zu viel Ram, eine andere
     * Lösung, die die Dateien einzeln aus dem alten Archiv liest ist evt ressourcenschonender
     * @throws IOException
     */
    @Deprecated
    public void loadDataIntoRam() throws IOException {
        FileInputStream input = new FileInputStream(rafArchiv + ".dat");
        for (ArchiveFile file : archiveFiles.getArchiveFiles()) {
            byte[] buffer = new byte[(int) file.getLength()];
            input.read(buffer, 0, (int) file.getLength());
            file.setData(buffer);
        }
    }

    /**
     * Extrahiert alles aus dem ausgewählten Archiv
     * @throws IOException
     */
    public void extractEverything() throws IOException {
        int threadcount = Runtime.getRuntime().availableProcessors();
        //int threadcount = 1;
        System.out.println(threadcount + " Threads");
        System.out.println("building files");
        System.out.println(" Offset     Length  Path");
        System.out.println("|----------|--------|---------------------------------------");
        ListCounter listCounter = new ListCounter((int) fileList.getLength());

        RafThread[] swag = new RafThread[threadcount];
        for (int i = 0; i < threadcount; i++) {
            swag[i] = new RafThread(listCounter, this);
        }

        for (int i = 0; i < threadcount; i++) {
            while (!swag[i].isFertig()) {
                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println("finished building files");
        System.out.println("lol k im ready!");
    }


    @Deprecated
    //TODO Neu machen
    public void extractSingleFile(String input) throws IOException {

        for (int i = 0; i < fileList.getLength(); i++) {
            if (pathList.getPathList().get((int) fileList.getFileList().get(i).getPathListIndex().getContent()).getPath().contains(input)) {
                extractFile(i);
            }
        }
    }

    public void repack(String filePath) throws IOException {
        loadDataIntoRam();
        FileOutputStream output;
        byte[] compressedFile;
        List<Byte> lyst = new ArrayList<Byte>();
        int counter = 0;

        //System.out.println("Found File, replace it nao");
        DeflaterInputStream toread = new DeflaterInputStream(new FileInputStream(filePath));
        int lol;
        while ((lol = toread.read()) != -1)
            lyst.add((byte) lol);

        compressedFile = convertBytes(lyst);
        ArchiveFile file = searchForRessource(filePath);
        file.setData(compressedFile);
        archiveFiles.readjustOffsets(file);


        //Archivefilelist in Datei schreiben

        output = new FileOutputStream(new File(rafArchiv+".dat"));
        for (ArchiveFile files : archiveFiles.getArchiveFiles()) {
            output.write(files.getData());
        }
        buildIndex();
    }

    /**
     * Erstellt die Indexdatei des Filearchives und überschreibt die alte Datei, ein Backup muss von Hand angelegt werden
     * @throws IOException
     */
    public void buildIndex() throws IOException {
        FileOutputStream stream = new FileOutputStream(rafArchiv);
        for (LeWord word : wordList) {
            stream.write(word.getRawContent());
        }
        stream.close();
        System.out.println("Index build ready");
    }

    /**
     * Bitte die übergeordnete Methode benutzen
     * @param i
     * @throws IOException
     */
    private void extractFile(int i) throws IOException {

        FileOutputStream output;
        FileInputStream input;
        byte[] stuffToWrite;
        String path = archiveFiles.getArchiveFiles().get(i).getFilePath();
        long length = archiveFiles.getArchiveFiles().get(i).getLength();
        long offset = archiveFiles.getArchiveFiles().get(i).getOffset();
        boolean outcome = true;
        stuffToWrite = new byte[(int) length];
        input = new FileInputStream((rafArchiv + ".dat"));

        File fyl = new File("Output/" + path);
        fyl.getParentFile().mkdirs();

        output = new FileOutputStream("Output/" + path);
        input.skip(offset);
        input.read(stuffToWrite, 0, (int) length);


        InflaterInputStream towrite = new InflaterInputStream(new ByteArrayInputStream(stuffToWrite));

        List<Byte> writeDisLyst = new ArrayList<Byte>();
        int tmp;

        try {
            while ((tmp = towrite.read()) != -1) {
                writeDisLyst.add((byte) tmp);
            }
        } catch (ZipException e) {
            outcome = false;
        }
        output.write(convertBytes(writeDisLyst), 0, writeDisLyst.size());

        output.close();
        input.close();

        System.out.format((outcome?"OK   ":"FAIL ") + "|%-9s |%-8s| %s%n", offset, length, path);
    }

    private void printAsHex(long input) {
        System.out.println("Int: " + input + " Hex: " + Long.toHexString(input));
    }

    /**
     * Durchsucht die ArchiveFilelist nach einer Ressource
     * @param filePath
     * @return Archivefile, ansonsten null wenn die Ressource nicht vorhanden ist
     */
    public ArchiveFile searchForRessource(String filePath) {
        for (ArchiveFile file : archiveFiles.getArchiveFiles()) {
            if (file.getFilePath().contains(filePath)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Konvertiert eine Liste in ein Byte array
     * @param bytes
     * @return
     */
    public static byte[] convertBytes(List<Byte> bytes) {
        byte[] ret = new byte[bytes.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = bytes.get(i);
        }
        return ret;
    }

    /**
     * Eine Klasse, die für den ExtrahierThread den Index der Liste managed, damit nichts mehrfach extrahiert werden kann.
     */
    class ListCounter {
        int index = -1;

        private ListCounter() {

        }

        public ListCounter(int amount) {
            this.amount = amount;
        }

        synchronized int getIndex() {
            ++index;
            if (index < amount)
                return index;
            else
                return -1;
        }

        int getAmount() {
            return amount;
        }

        void setAmount(int amount) {
            this.amount = amount;
        }

        int amount;


    }

    /**
     * Der eigentliche Extrahiertrhead, es lassen sich belibig viele Threads starten
     */
    class RafThread extends java.lang.Thread {
        private RAFFile parent;
        private ListCounter counter;
        private int currentIndex;

        boolean isFertig() {
            return fertig;
        }

        boolean fertig = false;

        public RafThread(ListCounter counter, RAFFile parent) {
            super();
            this.parent = parent;
            this.counter = counter;
            this.start();

        }

        public void run() {
            this.currentIndex = counter.getIndex();
            while (this.currentIndex != -1) {
                try {
                    parent.extractFile(this.currentIndex);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                this.currentIndex = counter.getIndex();
            }
            fertig = true;
        }
    }

    }



