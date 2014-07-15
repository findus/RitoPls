package PathEntry;

import LittleEndian.LeWord;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: philipp.hentschel Date: 03.07.14 Time: 11:03 To change this template use File |
 * Settings | File Templates.
 */

/**
 * Die PathEntry Klasse speichert Offset, Länge und Inhalt eines PathStrings im Filearchiv
 */
public class PathEntry {

    LeWord pathOffset;

    LeWord pathLength;

    String path;

    public PathEntry(LeWord pathOffset, LeWord pathLentgh, ArrayList<Byte> filebyteList, long byteoffsetPathList) {
        this.pathLength = pathLentgh;
        this.pathOffset = pathOffset;

        StringBuffer strb = new StringBuffer();

        for (int i = 0; i < this.pathLength.getContent(); i++) {
            strb.append((char) ((int) (filebyteList.get((int) (i + this.pathOffset.getContent() + byteoffsetPathList - 9))) & 0xFF));
        }

        path = strb.toString().trim();


    }

    public LeWord getPathLength() {
        return pathLength;
    }

    public LeWord getPathOffset() {
        return pathOffset;
    }

    public String getPath() {
        return this.path;
    }

}
