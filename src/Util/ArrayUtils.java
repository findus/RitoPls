package Util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: philipp.hentschel
 * Date: 16.07.14
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public class ArrayUtils {

    public static byte[] objectArrayToByteArray(Object[] inputArray){
        byte[] retArray=new byte[inputArray.length];
        for(int i=0;i<inputArray.length;i++){
            retArray[i]=((Byte)inputArray[i]);
        }
        return retArray;
    }

    public static String encodeByteArrayToString(byte[] inputArray) {
        StringBuffer strb = new StringBuffer();

        for (int i = 0; i < inputArray.length; i++) {
            strb.append((char) ((inputArray[i]) & 0xFF));
        }

        return strb.toString().trim();
    }

    public static void addArrayToList(List<String> list,String[]input)
    {
        list.clear();
        for(String val : input)
        {
            list.add(val);
        }
    }
}
