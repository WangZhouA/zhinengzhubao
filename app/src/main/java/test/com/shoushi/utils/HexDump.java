package test.com.shoushi.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Clone of Android's HexDump class, for use in debugging. Cosmetic changes
 * only.十六进制数据操作
 */
public class HexDump {
    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String dumpHexString(byte[] array) {
        return dumpHexString(array, 0, array.length);
    }

    public static String dumpHexString(byte[] array, int offset, int length) {
        StringBuilder result = new StringBuilder();

        byte[] line = new byte[16];
        int lineIndex = 0;

        result.append("\n0x");
        result.append(toHexString(offset));

        for (int i = offset; i < offset + length; i++) {
            if (lineIndex == 16) {
                result.append(" ");

                for (int j = 0; j < 16; j++) {
                    if (line[j] > ' ' && line[j] < '~') {
                        result.append(new String(line, j, 1));
                    } else {
                        result.append(".");
                    }
                }

                result.append("\n0x");
                result.append(toHexString(i));
                lineIndex = 0;
            }

            byte b = array[i];
            result.append(" ");
            result.append(HEX_DIGITS[(b >>> 4) & 0x0F]);
            result.append(HEX_DIGITS[b & 0x0F]);

            line[lineIndex++] = b;
        }

        if (lineIndex != 16) {
            int count = (16 - lineIndex) * 3;
            count++;
            for (int i = 0; i < count; i++) {
                result.append(" ");
            }

            for (int i = 0; i < lineIndex; i++) {
                if (line[i] > ' ' && line[i] < '~') {
                    result.append(new String(line, i, 1));
                } else {
                    result.append(".");
                }
            }
        }

        return result.toString();
    }

    public static byte[] getSameBytes(byte[] byteA, int i) {
        int no = byteA.length - i;
        byte[] b = new byte[i];
        for (int j = 0; j < i; j++) {
            b[j] = byteA[no + j];
        }
        return b;
    }

    public static byte[] gethead(byte[] byteA, int i) {
        byte[] b = new byte[i];
        for (int j = 0; j < i; j++) {
            b[j] = byteA[j];
        }
        return b;
    }


    public static String toHexString(byte b) {
        return toHexString(toByteArray(b));
    }

    //byte转字节16进制字符串
    public static String toHexString(byte[] array) {
        return toHexString(array, 0, array.length);
    }

    public static String toHexString(byte[] array, int offset, int length) {
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];
            buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
            buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
        }

        return new String(buf);
    }

    public static String toHexString(int i) {
        return toHexString(toByteArray(i));
    }

    public static String toHexString(short i) {
        return toHexString(toByteArray(i));
    }

    public static byte[] toByteArray(byte b) {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }

    public static byte[] toByteArray(int i) {
        byte[] array = new byte[4];

        array[3] = (byte) (i & 0xFF);
        array[2] = (byte) ((i >> 8) & 0xFF);
        array[1] = (byte) ((i >> 16) & 0xFF);
        array[0] = (byte) ((i >> 24) & 0xFF);

        return array;
    }

    public static byte[] toByteArray(short i) {
        byte[] array = new byte[2];

        array[1] = (byte) (i & 0xFF);
        array[0] = (byte) ((i >> 8) & 0xFF);

        return array;
    }

    private static int toByte(char c) {
        if (c >= '0' && c <= '9')
            return (c - '0');
        if (c >= 'A' && c <= 'F')
            return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f')
            return (c - 'a' + 10);

        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    public static byte[] hexStringToByteArray(String hexString) {
        Log.e("拼接的值", hexString);
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString
                    .charAt(i + 1)));
        }
        return buffer;
    }

    public static byte[] getMergeBytes(byte[] head, byte[] footer) {
        int numA = head.length;
        int numB = footer.length;
        Log.e("Anumber=", numA + "");
        Log.e("Bnumber=", numB + "");
        byte[] b = new byte[numA + numB];
        for (int i = 0; i < numA; i++) {
            b[i] = head[i];
        }
        for (int i = 0; i < numB; i++) {
            b[numA + i] = footer[i];
        }
        return b;

    }
    public static byte[] getTiHuan(byte[] head, byte[] footer,int start,int end,int cha) {
//        int numA = head.length;
//        int numB = footer.length;
//        Log.e("Anumber=", numA + "");
//        Log.e("Bnumber=", numB + "");
        //  byte[] b = new byte[numA + numB];
        for (int i = start; i <end; i++) {
            head[i] = footer[i-cha];
        }
        return head;

    }
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[16];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    public static byte[] hexStringToBytes(String hexString,int number) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[number];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
//	/**
//	 * 手机号码验证
//	 * @param mobiles
//	 * @return
//	 */
//	public static boolean isMobileNO(String mobiles){
////		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
////		Pattern p = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
//		Pattern p = Pattern.compile("^1[3|4|5|8|7][0-9]\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();
//	}

    /**
     * 手机号码验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles.length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * md5加密方法
     *
     * @param info 密码源
     * @return
     */

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 异或和
     */

    private static final char[] bcdLookup = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final int MESSAGE_DEVICE_NAME = 4;

    public static String XORAnd(byte[] bytes) {
        int str = 0;
        for (int i = 0; i < bytes.length; i++) {
            str ^= Integer.valueOf((bcdLookup[(bytes[i] >>> MESSAGE_DEVICE_NAME) & 15] + "" + bcdLookup[bytes[i] & 15] + ""), 16);
        }
        return Integer.toHexString(str);
    }

    /**
     * 输出0x00:0x01...
     *
     * @param b
     * @return
     */
    public static String BytetohexString(byte[] b) {
        int len = b.length;
        StringBuilder sb = new StringBuilder(b.length * (2 + 1));
        Formatter formatter = new Formatter(sb);

        for (int i = 0; i < len; i++) {
            if (i < len - 1)
                formatter.format("0x%02X:", b[i]);
            else
                formatter.format("0x%02X", b[i]);

        }
        formatter.close();

        return sb.toString();
    }

    /**
     * 输出0x00:0x01...
     *
     * @param adminmac//管理员注册的MAC地址
     * @return
     */
    public static String AdminMac(String adminmac) {
        String data=null;
        if(adminmac.length()<6){
            data="0"+adminmac;
        }else{
            data=adminmac;
        }
        String password = null;
        for (int i = 0; i < data.length(); i++) {
            if (i == 0) {
                password = "0" + data.charAt(i);
            } else {
                password = password + "0" + data.charAt(i);
            }
        }
        return password;
    }
    /**
     * 数字不足位数补0
     * @param str
     * @param strLength
     * @param isLeft    为true时，左补；否则，右补
     * @return
     */
    public static String addZeroForNum(String str, int strLength, Boolean isLeft) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                str = isLeft ? sb.append("0").append(str).toString() : sb
                        .append(str).append("0").toString();
                strLen = str.length();
            }
        }
        return str;
    }

}
