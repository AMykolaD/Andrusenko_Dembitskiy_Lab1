import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class IntelligentMeteorologist {
    String previousQuestion = "";
    IntelligentMeteorologist(){

    }
    //length-1: загальні
    //length: повторки
    public String generateResponse(String input) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\responses.txt"));
        if(previousQuestion.length()!=0&& previousQuestion.equals(input)){
            String line = getLine(br, br.lines().count()-1);
            StringTokenizer stringTokenizer = new StringTokenizer(line, "_");
            br.close();
            return getRandomResponse(getValidResponses(stringTokenizer, input));
        }
        previousQuestion = input;
        long lines = br.lines().count();
        String currentLine, inputToLower = input.toLowerCase();
        String currentWord;
        StringTokenizer spaceTokenizer;
        int maxValue = 0;
        long maxLine = 0;
        br = resetBufferedReader(br);
        for(long i = 0; i < lines - 2; i++) {
            currentLine = br.readLine().split("_")[0];
            double value = 0;
            spaceTokenizer = new StringTokenizer(currentLine, " ");
            while (spaceTokenizer.hasMoreTokens()) {
                currentWord = spaceTokenizer.nextToken();
                if(currentWord.contains("+")){
                    currentWord = currentWord.replace("+", "");
                    if(inputToLower.contains(currentWord)) value+=0.5;
                }
                else if (inputToLower.contains(currentWord)) value+=1;
            }
            if (value >= 1) {
                if(value>maxValue) {
                    maxValue = (int) value;
                    maxLine = i;
                }
            }
        }
        if(maxValue>=1){
            br.close();
            currentLine = getLine(br, maxLine);
            currentLine = currentLine.substring(currentLine.indexOf("_")+1);
            return getRandomResponse(getValidResponses(new StringTokenizer(currentLine, "_"), input));
        }
        else{
            br = resetBufferedReader(br);
            String line = getLine(br, br.lines().count()-2);
            StringTokenizer stringTokenizer = new StringTokenizer(line, "_");
            br.close();
            return getRandomResponse(getValidResponses(stringTokenizer, input));
        }
    }

    private BufferedReader resetBufferedReader(BufferedReader bufferedReader) throws IOException {
        bufferedReader.close();
        return new BufferedReader(new FileReader("src\\main\\resources\\responses.txt"));
    }

    private String getLine(BufferedReader br, long line) throws IOException {
        br = resetBufferedReader(br);
        String result = null;
        for (long i = 0; i <= line; i++){
            result = br.readLine();
            if(result==null) throw new IOException("File is too short to read line number "+line);
        }
        return result;
    }

    private boolean isDay(){
        int h = LocalTime.now().getHour();
        return h < 20 && h > 5;
    }

    private boolean isQuestion(String str){
        return str.charAt(str.length()-1)=='?';
    }

    private boolean isValidResponse(String s, String input){
        if(!s.contains("*")) return true;
        boolean isDay = isDay();
        boolean isQuestion = isQuestion(s);
        if(isDay&&s.contains("*IFNIGHT*")) return false;
        if(!isDay&&s.contains("*IFDAY*")) return false;
        if(!isQuestion&&s.contains("*IFQUESTION")) return false;
        if(isQuestion&&s.contains("*IFNOTQUESTION")) return false;
        return true;
    }

    private String removePatterns(String s){
        if(s.contains("*")) {
            s = s.replace("*IFDAY*", "");
            s = s.replace("*IFNIGHT*", "");
            s = s.replace("*IFQUESTION*", "");
            s = s.replace("*IFNOTQUESTION*", "");
        }
       return s;
    }

    private ArrayList<String> getValidResponses(StringTokenizer stringTokenizer, String input){
        ArrayList<String> arrayList = new ArrayList<>();
        String response;
        while(stringTokenizer.hasMoreTokens()){
            response = stringTokenizer.nextToken();
            if(isValidResponse(response, input)){
                arrayList.add(removePatterns(response));
            }
        }
        return arrayList;
    }

    private String getRandomResponse(ArrayList<String> arrayList){
        Random random = new Random();
        return arrayList.get((int)(random.nextDouble()*arrayList.size()));
    }
}
