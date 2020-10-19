
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int n = scanner.nextInt();
        int k = scanner.nextInt();

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = a; i <= b; i++) {

            Random random = new Random(i);
            List<Integer> temp = new ArrayList<>();

            for (int j = 1; j <= n; j++) {
                temp.add(random.nextInt(k));
            }

            Integer max = Collections.max(temp);

            map.put(i, max);

        }

        Integer key = Collections.min(map.entrySet(), Map.Entry.comparingByValue()).getKey();
        Integer val = Collections.min(map.entrySet(), Map.Entry.comparingByValue()).getValue();

        System.out.println(key);
        System.out.println(val);
    }
}