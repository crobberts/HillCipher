import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class HillKeys {

	public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("You must give three arguments.\nHillKeys <radix> <blocksize> <keyfile>");
            return;
        }

        init(args[0], args[1], args[2]);
	}

    public static void init(String r, String k, String b) {
        int radix = Integer.parseInt(r);
        int blockSize = Integer.parseInt(b);
        try {
            File keyFile = new File(k);
            keyFile.createNewFile();
        } catch (IOException e) {
            System.out.println("could not create file " + k);
        }

        String key = generateRandomKey();
    }

    public static String generateRandomKey() {
        ibyte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
 
        System.out.println(generatedString);
        return generatedString;
    }
}

