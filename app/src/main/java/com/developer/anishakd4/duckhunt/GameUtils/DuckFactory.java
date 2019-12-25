package com.developer.anishakd4.duckhunt.GameUtils;

import android.content.Context;

import com.developer.anishakd4.duckhunt.GamingObjects.Duck;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.Utilities;

import java.util.Random;

public class DuckFactory {

    private Context context;

    public DuckFactory(Context context) {
        this.context = context;
    }

    public Duck makeGreenDuck() {
        int x = Utilities.getRandomNumberInRange((int)(0.1 * Utilities.SCREEN_WIDTH), (int)(0.9 * Utilities.SCREEN_WIDTH));
        int y = (int)(0.6 * Utilities.SCREEN_HEIGHT);
        Duck duck = new Duck(x, y, "green", new BasicPhysicsComponent(), context);
        return duck;
    }

    public Duck makeRedDuck() {
        int x = Utilities.getRandomNumberInRange((int)(0.1 * Utilities.SCREEN_WIDTH), (int)(0.9 * Utilities.SCREEN_WIDTH));
        int y = (int)(0.6 * Utilities.SCREEN_HEIGHT);
        Duck duck = new Duck(x, y, "red", new BasicPhysicsComponent(), context);
        return duck;
    }

    public Duck makePinkDuck() {
        int x = Utilities.getRandomNumberInRange((int)(0.1 * Utilities.SCREEN_WIDTH), (int)(0.9 * Utilities.SCREEN_WIDTH));
        int y = (int)(0.6 * Utilities.SCREEN_HEIGHT);
        Duck duck = new Duck(x, y, "pink", new BasicPhysicsComponent(), context);
        return duck;
    }

    public Duck makeRandomDuck() {
        Duck duck;
        int duck_type = new Random().nextInt(GameConstants.NUMBER_OF_DUCK_TYPES);
        switch (duck_type) {
            case GameConstants.GREENDUCK:
                duck = makeGreenDuck();
                break;
            case GameConstants.REDDUCK:
                duck = makeRedDuck();
                break;
            case GameConstants.PINKDUCK:
                duck = makePinkDuck();
                break;
            default:
                duck = makeGreenDuck();
                break;
        }
        return duck;
    }
}
