package LittleEndian; /**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 11:08 To change this template use File |
 * Settings | File Templates.
 */

import java.util.Collections;
import java.util.List;

/**
 * Little Endian Word ( ͡° ͜ʖ ͡°), grundlegendse Dateneinheit in dem Filearchiv, die in die Entsprechenden Listen einsortiert werden muss
 */
public class LeWord {

    private byte[] rawContent;

    public LeWord(byte word[]) {
        this.rawContent = word.clone();
    }

    public LeWord(List<Byte> filebyteList,int offset)
    {
        rawContent = new byte[]{filebyteList.get(offset),filebyteList.get(offset+1),filebyteList.get(offset+2),filebyteList.get(offset+3)};
    }

    public long getContent() {
        return byteArrayToLong(rawContent,0,4);
    }

    public byte[] getRawContent() { return rawContent;}

    public void setContent(long content) {
        this.rawContent = longToByteArray(content);
    }

    private long byteArrayToLong(byte[] array, int start, int laenge) {
        long value = 0;
        int starter = 0;
        for (int i = start; i < start + laenge; i++, starter++) {
            // Durch 0xFF werden die Werte so umgewandelt, das sie die richtigen Werte anzeigen, so als währen sie
            // unsigned, den Bitshift benötigt man, damit die Daten in der richtigen Reihenfolge angezeigt werden
            value += (array[i] & 0xFFL) << (8 * starter);
        }
        return value;
    }

    private byte[] longToByteArray(long input)
    {
        byte[] retVal = new byte[4];
        retVal[0] = (byte)(input & 0xFF);
        retVal[1] = (byte)((input >> 8) & 0xFF);
        retVal[2] = (byte)((input >> 16) & 0xFF);
        retVal[3] = (byte)((input >> 24) & 0xFF);

        return retVal;
    }

    public static void main(String[]args)
    {
        LeWord word = new LeWord(new byte[]{1,2,3,4});
        long test = word.getContent();
        word.setContent(test);
    }
}
