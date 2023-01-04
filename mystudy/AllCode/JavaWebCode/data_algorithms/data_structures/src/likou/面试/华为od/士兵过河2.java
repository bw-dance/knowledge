package likou.面试.华为od;

import java.util.Scanner;

public class 士兵过河2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] colors = parseColors(scanner.nextLine());
        int timeWindow = scanner.nextInt();

        int maxCount = 0;
        for (int i = 0; i < colors.length - timeWindow + 1; i++) {
            int count = 0;
            for (int j = i; j < i + timeWindow; j++) {
                count += colors[j];
            }
            maxCount = Math.max(maxCount, count);
        }

        System.out.println(maxCount - 2);
    }

    private static int[] parseColors(String input) {
        String[] parts = input.split(" ");
        int[] colors = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            colors[i] = Integer.parseInt(parts[i]);
        }
        return colors;
    }
}
