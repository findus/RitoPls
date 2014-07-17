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

    public static byte[] longToByteArray(long input)
    {
        byte[] retVal = new byte[4];
        retVal[0] = (byte)(input & 0xFF);
        retVal[1] = (byte)((input >> 8) & 0xFF);
        retVal[2] = (byte)((input >> 16) & 0xFF);
        retVal[3] = (byte)((input >> 24) & 0xFF);

        return retVal;
    }

    public static void insertArrayInAnotherArray(byte[] zielArray, byte[] input, int position, int laenge)
    {
        int counter = 0;
        for(int i = position; i < position+laenge;i++)
        {
            zielArray[i] = input[counter];
            counter++;
        }
    }

}
