import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Привіт. Це - програма, з якою можна поговорити про погоду.");
        System.out.println("Звертайтеся до неї українською мовою.");
        System.out.println("Щоб закінчити сеанс, введіть quit");
        Scanner scanner = new Scanner(System.in);
        IntelligentMeteorologist intelligentMeteorologist = new IntelligentMeteorologist();
        while (true){
            String question = scanner.nextLine();
            if(question.equalsIgnoreCase("quit")) break;
            try {
                String answer = intelligentMeteorologist.generateResponse(question);
                System.out.println(answer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
