package com.embed.util;

import java.security.MessageDigest;
public class HashUtil {
    public static String Hash512Msg(String data) {
        String isValid = "";
        try {

            //LoggingUtil.logDebugInfo("STOKEN::" + data);
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            // ** NOTE all bytes that are retrieved from the data string must be done so using UTF-8 Character Set.
            byte[] hashBytes = data.getBytes("UTF-8");
            //Create the hash bytes from the data
            byte[] messageDigest = digest.digest(hashBytes);
            //Create a HEX string from the hashed data
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                sb.append(h);
            }
            //LoggingUtil.logDebugInfo("SBB-TOKEN:" + sb.toString());
            isValid = sb.toString();
        } catch (Exception ex) {
        }

        return isValid;

    }

    public static String makeToken(String requestId, String sourceCode, String passwd) {
        String s = "";
        try {
            String data = requestId + sourceCode + passwd;
            s = Hash512Msg(data);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }
}
