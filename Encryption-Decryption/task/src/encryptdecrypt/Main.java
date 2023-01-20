package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String dataToWork;
        List<String> lst = consoleParse(args);
        String type = lst.get(0);
        int key = Integer.parseInt(lst.get(1));
        String data = lst.get(2);
        String fileIn = lst.get(3);
        String fileOut = lst.get(4);
        String alg = lst.get(5);
        String out = null;
        if (!"".equals(data) && !Objects.equals(fileIn, null)) {
            dataToWork = data;
        } else if (!Objects.equals(fileIn, null)) {
            dataToWork = String.valueOf(fileParse(fileIn));
        } else {
            dataToWork = data;
        }
        if (Objects.equals(alg, null)) {
            alg = "shift";
        }
        if (type.equalsIgnoreCase("enc")) {
            if ("shift".equalsIgnoreCase(alg)) {
                out = shiftEncrypt(dataToWork, key,true);
            } else {
                out = unicodeEncrypt(dataToWork, key,true);
            }
        } else if (type.equalsIgnoreCase("dec")) {
            if ("shift".equalsIgnoreCase(alg)) {
                out = shiftEncrypt(dataToWork, key, false);
            } else {
                out = unicodeEncrypt(dataToWork, key, false);
            }
        }
        if (Objects.equals(fileOut, null)) {
            System.out.println(out);
        } else {
            fileOut(out, fileOut);
        }
    }


    // parse the file and save into string type list
    static StringBuilder fileParse(String f) {
        File file = new File(f);
        StringBuilder str = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
              str.append(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return str;
    }

    // output to file the text read
    static void fileOut(String data, String f) {
        try (FileWriter write = new FileWriter(f)) {
            for (String l : data.split("\\n")) {
                write.write(l);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // parse the  console parameters
    static List<String> consoleParse(String[] args) {
        String type = "enc";
        String data = "";
        String key = "0";
        String in = null;
        String out = null;
        String alg = null;
        List<String> lst = new ArrayList<>();
        for (int i = 0; i < args.length ; i++) {
            if (args[i].matches("-mode")) {
                type = args[i + 1].toLowerCase();
            } else if (args[i].matches("-key")) {
                key = args[i + 1];
            } else if (args[i].matches("-data")) {
                data = args[i + 1];
            } else if (args[i].matches("-in")) {
                in = args[i + 1];
            } else if (args[i].matches("-out")) {
                out = args[i + 1];
            } else if (args[i].matches("-alg")) {
                alg = args[i + 1];
            }
        }
        lst.add(type);
        lst.add(key);
        lst.add(data);
        lst.add(in);
        lst.add(out);
        lst.add(alg);
        return lst;
    }

    // encryption by shifting position
    static String shiftEncrypt(String str, int number, boolean type) {

        List<Character> lst = shiftEncodeReplace(str, number, 97, 122, type);
        StringBuilder strB = new StringBuilder();
        StringBuilder strF = new StringBuilder();
        for (Character s : lst) {
            strB.append(s);
        }
        lst = shiftEncodeReplace(strB.toString(), number, 65, 90, type);
        for (Character s : lst) {
            strF.append(s);
        }
        return strF.toString();
    }

    static List<Character> shiftEncodeReplace(String str, int number, int fplace, int lplace, boolean type) {
        List<Character> lst = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            lst.add(str.charAt(i));
        }
        for (int j = 0; j < str.length(); j++) {
            for (int i = fplace; i <= lplace; i++) {
                if (str.charAt(j) == (char) i) {
                    if (type) {
                        int pos = i + number;
                        if (pos > lplace) {
                           int dif = pos - lplace - 1;
                            pos = fplace + dif;
                        }
                        lst.set(j, (char) pos);
                    } else {
                        int pos = i - number;
                        if (pos < fplace) {
                            int dif = fplace - pos;
                            pos = lplace - dif + 1;
                        }
                        lst.set(j, (char) pos);
                    }
                }
            }
        }
        return lst;
    }

    static String unicodeEncrypt(String str, int number, boolean type) {
        int temp = type ? 126 : 21;
        StringBuilder newStr = new StringBuilder();
        int position = 0;
        for (int c = 0; c < str.length(); c++) {
            char strC = str.charAt(c);
            if (type) {
                position = (int) strC + number;
                if (position > temp) {
                    position = position - temp;
                }
            } else {
                position = (int) strC - number;
                if (position < temp) {
                    int dif = temp - position;
                    position = 126 - dif;
                }
            }
            char newC = (char) position;
            newStr.append(newC);
        }
        return newStr.toString();
    }
}
