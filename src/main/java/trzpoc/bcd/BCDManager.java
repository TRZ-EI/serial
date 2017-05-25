package trzpoc.bcd;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 21/03/17
 * Time: 14.18
 */
public class BCDManager {

    public static BCDManager getNewInstance() {
        return new BCDManager();
    }

    public byte[] DecimalToBCD(long num) {
        int digits = 0;

        long temp = num;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }

        int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;

        byte bcd[] = new byte[byteLen];

        for (int i = 0; i < digits; i++) {
            byte tmp = (byte) (num % 10);

            if (i % 2 == 0) {
                bcd[i / 2] = tmp;
            } else {
                bcd[i / 2] |= (byte) (tmp << 4);
            }

            num /= 10;
        }

        for (int i = 0; i < byteLen / 2; i++) {
            byte tmp = bcd[i];
            bcd[i] = bcd[byteLen - i - 1];
            bcd[byteLen - i - 1] = tmp;
        }

        return bcd;
    }

    public long BCDToDecimal(byte[] bcd) {

        return Long.valueOf(this.ConvertBCDByteArraytoString(bcd));
    }


    public String ConvertBCDByteArraytoString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(this.ConvertBCDByteToString(bcd[i]));
        }

        return sb.toString();
    }
    private String ConvertBCDByteToString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }
    private void testForValue(long val, String expected) {
        String binaryString = this.byteArrayToBinaryString(this.DecimalToBCD(val));
        System.out.print(String.format("Testing: %10d -> %30s %4s\n", val, binaryString, binaryString.equals(expected) ? "[OK]" : "[FAIL]"));
    }

    private void testForValue(long val) {
        long newVal = this.BCDToDecimal(this.DecimalToBCD(val));

        System.out.print(String.format("Testing: %10d -> %30d %4s\n", val, newVal, newVal == val ? "[OK]" : "[FAIL]"));
    }

    private String byteArrayToBinaryString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte i : bytes) {
            String byteInBinary = String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
            sb.append(byteInBinary);
        }
        return sb.toString();
    }

}