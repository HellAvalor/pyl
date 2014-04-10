package com.andreykaraman.pyl;

public class LevelUpModel {

    final static String LOG_TAG = "LevelUpModel";
    public static int ADD_ACTION = 1;
    public static float MILE_STONE_EXP = 0.25f;
    public static int LEVEL_MULTIPLIER = 10;
    public static int MAX_LEVEL = 100;
    public static float DAY_FLASH = 0.5f;
    public int expTable[] = new int[MAX_LEVEL];

    public LevelUpModel() {

	expTable[0] = 0;
	expTable[1] = 10;
	for (int i = 2; i < MAX_LEVEL - 1; i++) {
	    expTable[i] = 2 * expTable[i - 1] - expTable[i - 2]
		    + LEVEL_MULTIPLIER;
	    // Log.d(LOG_TAG, "Level : " + i + " max exp " + expTable[i]);
	}

    }
}
