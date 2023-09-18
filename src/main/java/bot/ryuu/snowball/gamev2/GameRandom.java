package bot.ryuu.snowball.gamev2;

import bot.ryuu.snowball.gamev2.power.Power;

import java.util.Random;

public interface GameRandom {
    int PROBABILITY_PACIFIER = 3;
    int PROBABILITY_FORTUNE = 8;
    int PROBABILITY_ENROLMENT = 18;
    int PROBABILITY_SUPER_THROW = 28;
    int PROBABILITY_THIEF = 43;
    int PROBABILITY_BOOST = 63;
    int PROBABILITY_BIG_BAGS = 100;

    int PROBABILITY_SHOT = 60;

    int LEGENDARY = 10;
    int RARE = 30;
    int USUAL = 60;

    int PACIFIER = 10;
    int FORTUNE = 40;
    int ENROLMENT = 50;

    int SUPER_THROW = 40;
    int BOOST = 60;

    int THIEF = 40;
    int BIG_BAGS = 60;

    static boolean randomShot() {
        return Math.ceil(Math.random() * 100) < PROBABILITY_SHOT;
    }

    static Power randomPowerV2() {
        int k1 = (int) Math.ceil(Math.random() * 100);
        int k2 = (int) Math.ceil(Math.random() * 100);

        if (k1 < LEGENDARY) {
            if (k2 < PACIFIER)
                return Power.PACIFIER;
            else if (k2 < PACIFIER + FORTUNE)
                return Power.FORTUNE;
            else if (k2 < PACIFIER + FORTUNE + ENROLMENT)
                return Power.ENROLMENT;
        } else if (k1 < LEGENDARY + RARE) {
            if (k2 < SUPER_THROW)
                return Power.SUPER_THROW;
            else
                return Power.BOOST;
        } else {
            if (k2 < THIEF)
                return Power.THIEF;
            else
                return Power.BIG_BAGS;
        }

        return Power.PACIFIER;
    }

    static Power randomPower() {
        int random = (int) Math.ceil(Math.random() * 100);

        if (random < PROBABILITY_PACIFIER)
            return Power.PACIFIER;
        else if (random < PROBABILITY_FORTUNE)
            return Power.FORTUNE;
        else if (random < PROBABILITY_ENROLMENT)
            return Power.ENROLMENT;
        else if (random < PROBABILITY_SUPER_THROW)
            return Power.SUPER_THROW;
        else if (random < PROBABILITY_THIEF)
            return Power.THIEF;
        else if (random < PROBABILITY_BOOST)
            return Power.BOOST;
        else if (random < PROBABILITY_BIG_BAGS)
            return Power.BIG_BAGS;
        else
            return Power.PACIFIER;
    }
}
