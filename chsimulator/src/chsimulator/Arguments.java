package chsimulator;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Arguments {
    private Map<Character, Integer> flagChars;
    private Map<Character, Boolean> boolArgs;
    private Map<Character, Integer> intArgs;
    private Map<Character, String> stringArgs;

    public Arguments(String format, String[] args) {
        flagChars = new HashMap<Character, Integer>();
        boolArgs = new HashMap<Character, Boolean>();
        intArgs = new HashMap<Character, Integer>();
        stringArgs = new HashMap<Character, String>();
        parseFormat(format);
        parseArguments(args);
    }

    private void parseFormat(String format) {
        String[] argsFormat = format.split(",");
        for(String arg : argsFormat) {
            Character chr = arg.toLowerCase().charAt(0);
            if(flagChars.keySet().contains(chr)) return;
            if(arg.length() == 1) {
                flagChars.put(chr, 0);
                boolArgs.put(chr, false);
            } else {
                if(arg.charAt(1) == '#') {
                    flagChars.put(chr, 1);
                    intArgs.put(chr, null);
                }
                if(arg.charAt(1) == '*') {
                    flagChars.put(chr, 2);
                    boolArgs.put(chr, null);
                }
            }
        }
    }

    private void parseArguments(String[] args) {
        for(int i=0; i<args.length; i++) {
            if(args[i].charAt(0) != '-') continue;
            int j = 1;
            Character flag = args[i].charAt(j);
            switch(flagChars.get(flag)) {
                case 0:
                    do {
                        boolArgs.put(flag, true);
                        j++;
                        try {
                            flag = args[i].charAt(j);
                        } catch(IndexOutOfBoundsException e) {
                            break;
                        }
                    } while(flagChars.get(flag) == 0);
                    if(flagChars.get(flag) > 0) {
                        System.out.println("ERROR: Only boolean arguments can be chained");
                    }
                    break;
                case 1:
                    intArgs.put(flag, Integer.parseInt(args[++i]));
                    break;
                case 2:
                    stringArgs.put(flag, args[++i]);
                    break;
            }
        }
    }

    public Object get(Character flag) {
        Object res = null;
        switch(flagChars.get(flag)) {
            case 0:
                res = boolArgs.get(flag);
                break;
            case 1:
                res = intArgs.get(flag);
                break;
            case 2:
                res = stringArgs.get(flag);
                break;
        }
        return res;
    }

    public boolean getBool(Character flag) { return boolArgs.get(flag); }

    public int getInt(Character flag) { return intArgs.get(flag); }

    public String getString(Character flag) { return stringArgs.get(flag); }

    public Set<Character> getFlags() { return flagChars.keySet(); }

    public Map<Character, Boolean> getBoolArgs() {
        Map<Character, Boolean> res = new HashMap<Character, Boolean>(boolArgs);
        return res;
    }

    public Map<Character, Integer> getIntArgs() {
        Map<Character, Integer> res = new HashMap<Character, Integer>(intArgs);
        return res;
    }

    public Map<Character, String> getStringArgs() {
        Map<Character, String> res = new HashMap<Character, String>(stringArgs);
        return res;
    }
}