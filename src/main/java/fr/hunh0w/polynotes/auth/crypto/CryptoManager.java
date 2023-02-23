package fr.hunh0w.polynotes.auth.crypto;

import io.vertx.codegen.annotations.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CryptoManager {

    @Nullable
    public static String sha384(String str){
        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-384");
            crypt.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = crypt.digest();
            BigInteger bi = new BigInteger(1, bytes);
            return String.format("%0" + (bytes.length << 1) + "x", bi);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
