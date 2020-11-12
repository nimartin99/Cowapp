package de.hhn.frontend.provider;

/**
 * Class for holding the unique key for the user.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-21
 */
public class Key {
    private static String key;

    public static String getKey() {
        return key;
    }

    public static void setKey(String value) {
        key = value;
    }

    /**
     * Increases the given Key according to our conditions.
     * @param key the current key
     * @return the increased key
     */
    public static String increaseKey(String key) {
        String newKey = key.replace("-", "");   //removes the -
        char[] chars = newKey.toCharArray();
        int i = (chars.length - 1);
        chars = inceaseChars(chars, i);
        String result = new String(chars);
        result = addChar(result, '-', 4);
        result = addChar(result, '-', 9);
        result = addChar(result, '-', 14);
        return result;
    }

    private static String addChar(String str, char ch, int position) {
        int len = str.length();
        char[]updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position]= ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }

    private static char[] inceaseChars(char[] chars, int postion) {
        if (postion == -1) return chars;
        char[] charot = chars;
        if (getNextChar(charot[postion]) == '0') {
            charot[postion] = '0';
            charot = inceaseChars(charot, postion-1);
        } else {
            charot[postion] = getNextChar(charot[postion]);
        }
        return charot;
    }

    private static char getNextChar(char chi) {
        switch (chi) {
            case('0'): return '1';
            case('1'): return '2';
            case('2'): return '3';
            case('3'): return '4';
            case('4'): return '5';
            case('5'): return '6';
            case('6'): return '7';
            case('7'): return '8';
            case('8'): return '9';
            case('9'): return 'A';
            case('A'): return 'B';
            case('B'): return 'C';
            case('C'): return 'D';
            case('D'): return 'E';
            case('E'): return 'F';
            default: return '0';
        }
    }
}
