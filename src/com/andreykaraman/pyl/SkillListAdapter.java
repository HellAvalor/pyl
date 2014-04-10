package com.andreykaraman.pyl;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SkillListAdapter extends ArrayAdapter<SkillModel> {

    final static String LOG_TAG = "skillsAdapter";

    private ArrayList<SkillModel> skillsList;
    private final Activity context;

    public SkillListAdapter(Activity context, ArrayList<SkillModel> skills) {
	super(context, R.layout.item_for_list, skills);

	this.skillsList = skills;
	this.context = context;
	notifyDataSetChanged();
    }

    static class ViewHolder {

	protected TextView mSkillName;
	protected TextView mCurrentExp;
	protected TextView mLevel;
	protected ProgressBar mLevelProgress;
	protected Button mAddExp;
	protected Button mAddMileStone;
    }

    public int getCount() {

	return this.skillsList.size();
    }

    public View getView(final int position, View view, ViewGroup parent) {

	View tempView = null;
	final ViewHolder viewHolder;
	if (view == null) {

	    LayoutInflater inflater = context.getLayoutInflater();
	    tempView = inflater.inflate(R.layout.item_for_list, null);
	    viewHolder = new ViewHolder();
	    viewHolder.mSkillName = (TextView) tempView
		    .findViewById(R.id.textSkillName);
	    viewHolder.mCurrentExp = (TextView) tempView
		    .findViewById(R.id.TextExpToLevelUp);
	    viewHolder.mLevel = (TextView) tempView
		    .findViewById(R.id.textCurrentLevel);
	    viewHolder.mLevelProgress = (ProgressBar) tempView
		    .findViewById(R.id.progressBar1);

	    viewHolder.mAddExp = (Button) tempView
		    .findViewById(R.id.buttonAddPoint);
	    viewHolder.mAddMileStone = (Button) tempView
		    .findViewById(R.id.ButtonAddMileStone);

	    tempView.setTag(viewHolder);

	} else {
	    tempView = view;

	    viewHolder = (ViewHolder) tempView.getTag();

	}

	ViewHolder holder = (ViewHolder) tempView.getTag();
	holder.mAddExp.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		int tLevel = skillsList.get(position).getLevel();
		int temp = skillsList.get(position).addExp();

		notifyDataSetChanged();
		Log.d(LOG_TAG, "add exp: " + temp + " position " + position);
		Toast.makeText(getContext(), "You receive " + temp + " exp",
			Toast.LENGTH_SHORT).show();
		toastLevelUp(tLevel, skillsList.get(position).getLevel(),
			skillsList.get(position).getSkillName());
	    }

	});
	holder.mAddMileStone.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		int tLevel = skillsList.get(position).getLevel();
		int temp = (int) skillsList.get(position).addMilestone();
		Log.d(LOG_TAG, "add exp: " + temp + " position " + position);

		notifyDataSetChanged();
		Toast.makeText(getContext(),
			"You receive " + temp + " exp for milestone",
			Toast.LENGTH_SHORT).show();

		toastLevelUp(tLevel, skillsList.get(position).getLevel(),
			skillsList.get(position).getSkillName());
	    }
	});

	holder.mSkillName.setText(skillsList.get(position).getSkillName());
	holder.mCurrentExp.setText("Exp:"
		+ (int) skillsList.get(position).getCurExp() + "/"
		+ (int) skillsList.get(position).getMaxLevelUpExp());
	holder.mLevel.setText("Level:"
		+ (int) skillsList.get(position).getLevel() + " ");

	holder.mLevelProgress.setProgress(skillsList.get(position)
		.getLevelProgress());

	return tempView;
    }

    private void toastLevelUp(int lastLevel, int newLevel, String skillName) {
	if (lastLevel < newLevel) {
	    Toast.makeText(getContext(),
		    "Your new level: " + newLevel + " in " + skillName,
		    Toast.LENGTH_SHORT).show();
	}
    }

}
