package com.andreykaraman.pyl;

import android.util.Log;

public class SkillModel {
    final static String LOG_TAG = "MainActivity";
    private String skillName;
    private float curExp;
    private int level;
    private int maxLevelUpExp;
    final static LevelUpModel lTable = new LevelUpModel();

    /**
     * @param skillName
     * @param curExp
     * @param level
     * @param maxLevelUpExp
     */
    public SkillModel(String skillName, float curExp, int level,
	    int maxLevelUpExp) {
	super();
	this.skillName = skillName;
	this.curExp = curExp;
	this.level = level;
	this.maxLevelUpExp = maxLevelUpExp;
    }

    public SkillModel() {
	super();
	this.skillName = "Test";
	this.curExp = 0;
	this.level = 1;
	this.maxLevelUpExp = 10;
    }

    public SkillModel(String name) {
	super();
	this.skillName = name;
	this.curExp = 0;
	this.level = 1;
	this.maxLevelUpExp = 10;
    }

    public String getSkillName() {
	return skillName;
    }

    public void setSkillName(String skillName) {
	this.skillName = skillName;
    }

    public float getCurExp() {
	return curExp;
    }

    public void setCurExp(float curExp) {
	this.curExp = curExp;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    public int getMaxLevelUpExp() {
	return maxLevelUpExp;
    }

    public void setMaxLevelUpExp(int maxLevelUpExp) {
	this.maxLevelUpExp = maxLevelUpExp;
    }

    public int addExp() {
	this.curExp += LevelUpModel.ADD_ACTION;
	checkLevelUp();
	return LevelUpModel.ADD_ACTION;
    }

    public float addMilestone() {

	float addexp = (lTable.expTable[this.level] - lTable.expTable[this.level - 1])
		* LevelUpModel.MILE_STONE_EXP;

	this.curExp += addexp;
	checkLevelUp();
	return addexp;
    }

    public float dayExpFlash(int days) {

	float newExp = this.curExp - LevelUpModel.DAY_FLASH * days;

	if (newExp < 0) {
	    newExp = this.curExp;
	    checkLevelDown();
	    this.curExp = 0;

	    return newExp;
	} else {
	    this.curExp -= LevelUpModel.DAY_FLASH * days;
	    checkLevelDown();
	    return LevelUpModel.DAY_FLASH * days;
	}

    }

    public void checkLevelUp() {

	if (this.curExp >= this.maxLevelUpExp) {
	    this.level++;

	    this.maxLevelUpExp = lTable.expTable[this.level];
	    Log.d(LOG_TAG, "Level : " + this.level + " max exp "
		    + this.maxLevelUpExp);
	    checkLevelUp();
	}

    }

    public void checkLevelDown() {
	if (this.level > 0) {
	    if (this.curExp < lTable.expTable[this.level - 1]) {
		this.level--;

		this.maxLevelUpExp = lTable.expTable[this.level];
		Log.d(LOG_TAG, "Level : " + this.level + " max exp "
			+ this.maxLevelUpExp);
		checkLevelDown();
	    }
	}
    }

    public int getLevelProgress() {

	return (int) ((this.curExp - lTable.expTable[this.level - 1])
		/ (lTable.expTable[this.level] - lTable.expTable[this.level - 1]) * 100);
    }

}
