import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SsqJudge {

    public static void main(String[] args) {

        int[] todayNumber = new int[]{6, 9, 17, 22, 24, 26, 16};

        List<int[]> myNumberList = Arrays.asList(
                new int[]{1, 2, 3, 4, 5, 6, 7}
        );

        for (int i = 0; i < myNumberList.size(); i++) {
            Prize prize = judge(todayNumber, myNumberList.get(i));
            if (prize != null) {
                System.out.println(Arrays.toString(myNumberList.get(i)) + ": Level " + prize.level + " (" + prize.bonus + ")");
            }
        }


    }

    private static Prize judge(int[] todayNumber, int[] myNumber) {

        Set<Integer> todayRedNumber = new HashSet<>();
        Set<Integer> myRedNumber = new HashSet<>();

        for (int i = 0; i < 6; i++) {
            todayRedNumber.add(todayNumber[i]);
            myRedNumber.add(myNumber[i]);
        }

        todayRedNumber.retainAll(myRedNumber);
        int redHit = todayRedNumber.size();
        int blueHit = todayNumber[6] == myNumber[6] ? 1 : 0;

        switch (redHit) {
            case 6:
                return blueHit == 1 ? FIRST_PRIZE : SECOND_PRIZE;
            case 5:
                return blueHit == 1 ? THIRD_PRIZE : FOURTH_PRIZE;
            case 4:
                return blueHit == 1 ? FOURTH_PRIZE : FIFTH_PRIZE;
            case 3:
                return blueHit == 1 ? FIFTH_PRIZE : null;
            case 2:
                return blueHit == 1 ? SIXTH_PRIZE : null;
            case 1:
                return blueHit == 1 ? SIXTH_PRIZE : null;
            case 0:
                return blueHit == 1 ? SIXTH_PRIZE : null;
        }
        return null;
    }

    public static class Prize {

        private int level;
        private String rule;
        private int bonus;

        public Prize(int level, String rule, int bonus) {
            this.level = level;
            this.rule = rule;
            this.bonus = bonus;
        }

    }

    public static final Prize FIRST_PRIZE = new Prize(1, "6 red + 1 blue", 500_0000);
    public static final Prize SECOND_PRIZE = new Prize(2, "6 red + 0 blue", 500_0000);
    public static final Prize THIRD_PRIZE = new Prize(3, "5 red + 1 blue", 3000);
    public static final Prize FOURTH_PRIZE = new Prize(4, "5 red + 0 blue / 4 red + 1 blue", 200);
    public static final Prize FIFTH_PRIZE = new Prize(5, "4 red + 0 blue / 3 red + 1 blue", 10);
    public static final Prize SIXTH_PRIZE = new Prize(6, "2 red + 1 blue / 1 red + 1 blue / 0 red + 1 blue", 5);


}
