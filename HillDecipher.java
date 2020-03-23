import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

class HillDecipher {

    //params 
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("you must give five arguments, <radix> <blocksize> <keyfile> <plainfile> <cipherfile>");
            return;
        }

        try {
            int radix = Integer.parseInt(args[0]);

            if (radix != 26 && radix != 128 && radix != 256) {
                System.out.println("You need to give a valid radix");
                return;
            }

            int blockSize = Integer.parseInt(args[1]);
            File keyFile = new File(args[2]);
            File plainFile = new File(args[3]);
            File cipherFile = new File(args[4]);

            if (!keyFile.exists()) {
                throw new FileNotFoundException(keyFile.getName() + " does not exist");
            }

            if (!plainFile.exists()) {
                throw new FileNotFoundException(plainFile.getName() + " does not exist");
            }
    
            cipherFile.createNewFile();
            String[] sanitize = readKeyFile(plainFile.getName().trim()).split("\\s");

            if ((sanitize.length % blockSize) != 0) {
                throw new ArithmeticException("the matrices are incompatible");
            }

            ArrayList<ArrayList<Integer>> k = parseFileInput(keyFile.getPath().trim(), blockSize);

            if (k.size() != blockSize) {
                throw new ArithmeticException("the matrices are incompatible");
            }

            ArrayList<ArrayList<Integer>> p = parseFileInput(plainFile.getPath().trim(), blockSize);
            ArrayList<ArrayList<Integer>> l = transpose(p);
            String c = decrypt(l, k, radix);

            writeToFile(c, cipherFile.getPath());
        } catch (FileNotFoundException f) {
            System.err.println(f.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException n) {
            System.err.println(n.getMessage().replaceFirst(".*For input string: ", "Either the key file or the message file does not contain valid matrices. Could not convert "));
        } catch (ArithmeticException a) {
            System.err.println(a.getMessage());
        }
    }
    
    public static ArrayList<ArrayList<Integer>> transpose(ArrayList<ArrayList<Integer>> p) {
        ArrayList<ArrayList<Integer>> k = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < p.get(0).size(); i++) {
            k.add(new ArrayList<Integer>());
            for (int j = 0; j < p.size(); j++) {
                k.get(i).add(p.get(j).get(i));
            }
        }

        return k;
    }

    public static void writeToFile(String content, String filePath) throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(filePath);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static ArrayList<ArrayList<Integer>> parseFileInput(String keyFile, int blockSize) throws NumberFormatException {
            String key = readKeyFile(keyFile);
            String[] keyAsArray = key.split("\\s+");
            
            ArrayList<ArrayList<Integer>> k = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> m = new ArrayList<Integer>();

            for (int i = 0; i < keyAsArray.length; i++) {

                m.add(Integer.parseInt(keyAsArray[i]));
                
                if (m.size() >= blockSize) {
                    k.add(m);
                    m = new ArrayList<Integer>();
                }
            }

            return k;
    }

    public static String readKeyFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
    
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) 
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static String decrypt(ArrayList<ArrayList<Integer>> message, ArrayList<ArrayList<Integer>> k, int radix) {
        ArrayList<ArrayList<Integer>> dec = matrixMultiply(k, message, radix);
        String decrypted = matrixToString(dec);
        
        return decrypted;
    }

    public static String matrixToString(ArrayList<ArrayList<Integer>> matrix) {
        String parts = "";
        for (int k = 0; k < matrix.get(0).size(); k++) {
            String part = "";
            for (int i = 0; i < matrix.size(); i++) {
                part += (char)(matrix.get(i).get(k) + 65);
            }
            parts += part;
        }

        return parts;
    }

    public static ArrayList<ArrayList<Integer>> stringToMatrix(String key) {
    
        ArrayList<ArrayList<Integer>> keyMatrix = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> parts = new ArrayList<String>();
        String substr = "";
        for (int i = 0; i < key.length(); i++) {
            substr += key.charAt(i);
            if (substr.length() >= 3) {
                parts.add(substr);
                substr = "";
            }
        }

        for (int i = 0; i < 3; i++) {
            keyMatrix.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < parts.size(); i++) {
            for (int j = 0; j < 3; j++) {
                keyMatrix.get(j).add((int) parts.get(i).charAt(j) - 65);
            }
        }

        return keyMatrix;
    }

    public static ArrayList<ArrayList<Integer>> matrixMultiply(ArrayList<ArrayList<Integer>> key, ArrayList<ArrayList<Integer>> msg, int radix) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        int row = 0;
        while (row < key.get(0).size()) {
            int kl = 0;
            ArrayList<Integer> arr = new ArrayList<>();

            while (kl <= msg.get(0).size()) {
                ArrayList<Integer> columns = new ArrayList<Integer>();

                for (int k = 0; k < msg.size(); k++) {
                    columns.add(msg.get(k).get(kl));
                }

                int sum = 0;
                for (int i = 0; i < key.size(); i++) {
                    sum += (key.get(row).get(i) * columns.get(i));
                }

                arr.add(sum % radix);
                kl++;
            }

            result.add(arr);
            row++;
        }

        return result;

    }
}