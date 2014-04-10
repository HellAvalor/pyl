package com.andreykaraman.pyl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    final static String LOG_TAG = "MainActivity";
    final static int EDIT_ID = 1;
    final static int DELETE_ID = 2;
    final static String PREFERENCE_NAME = "preferencename";

    static ListView skillsListView;
    static SkillListAdapter skillsAdapter;
    ArrayList<SkillModel> skills;
    View footerLayout;
    Button addSkillButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	prefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

	footerLayout = getLayoutInflater().inflate(R.layout.addfuter, null);

	skills = new ArrayList<SkillModel>();

	skillsListView = (ListView) findViewById(R.id.listSkills);
	skillsAdapter = new SkillListAdapter(this, skills);
	skillsListView.setLongClickable(true);
	skillsListView.setAdapter(skillsAdapter);

	// printSkills(skills);

	addSkillButton = (Button) footerLayout
		.findViewById(R.id.buttonAddSkill);
	addSkillButton.setOnClickListener(new Button.OnClickListener() {
	    public void onClick(View v) {
		addNewSkill();
	    }
	});
	skillsListView.addFooterView(footerLayout, null, false);
	skillsAdapter.notifyDataSetChanged();
	registerForContextMenu(skillsListView);

    }

    @Override
    public void onPause() {
	// Log.d(LOG_TAG,
	// "---------------------------- pausing/saving -------------------------");
	// printSkills(skills);
	saveArray(skills, this);
	super.onPause();
    }

    public void onResume() {
	// Log.d(LOG_TAG, "----------------- Resuming --------------------");
	loadArray(this);
	// printSkills(skills);
	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	getMenuInflater().inflate(R.menu.main, menu);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {

	case R.id.action_add_skill:
	    addNewSkill();
	    return true;

	case R.id.action_settings:
	    Toast.makeText(this, "There are no settings yet!",
		    Toast.LENGTH_SHORT).show();
	    return true;

	    // Generic catch all for all the other menu resources
	default:
	    // Don't toast text when a submenu is clicked

	    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
	    return true;

	}
    }

    public void addNewSkill() {
	LinearLayout popUp = (LinearLayout) getLayoutInflater().inflate(
		R.layout.alert_dialog_text_entry, null);
	final EditText getText = (EditText) popUp
		.findViewById(R.id.skill_name_edit);

	new AlertDialog.Builder(this)
		.setTitle(R.string.action_add_skill)
		.setView(popUp)
		.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {

				if (getText.getText().toString() != null) {
				    final String newSkillName = getText
					    .getText().toString();
				    skills.add(new SkillModel(newSkillName));

				    skillsAdapter.notifyDataSetChanged();
				    Toast.makeText(
					    getBaseContext(),
					    "New skill " + newSkillName
						    + " was added",
					    Toast.LENGTH_SHORT).show();

				    // Log.d(LOG_TAG,
				    // "----------------- add new skill --------------------");
				    // printSkills(skills);
				}

			    }
			})
		.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {
				// ignore
			    }
			}).show();
    }

    public void editSkill(final int pos) {
	LinearLayout popUp = (LinearLayout) getLayoutInflater().inflate(
		R.layout.alert_dialog_text_entry, null);
	final EditText getText = (EditText) popUp
		.findViewById(R.id.skill_name_edit);
	getText.setText(skills.get(pos).getSkillName());

	new AlertDialog.Builder(this)
		.setTitle(R.string.action_edit_skill)
		.setView(popUp)
		.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {
				String newSkillName = getText.getText()
					.toString();
				if (newSkillName != skills.get(pos)
					.getSkillName()) {

				    skills.get(pos).setSkillName(
					    getText.getText().toString());
				    skillsAdapter.notifyDataSetChanged();
				    Toast.makeText(
					    getBaseContext(),
					    "Skill name was changed to "
						    + newSkillName,
					    Toast.LENGTH_SHORT).show();

				    // Log.d(LOG_TAG,
				    // "----------------- edit skill --------------------");
				    // printSkills(skills);
				}
			    }
			})
		.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {
				// ignore
			    }
			}).show();

    }

    public void deleteSkill(final int pos) {

	new AlertDialog.Builder(this)
		.setTitle(R.string.action_delete_skill)
		.setMessage("Delete " + skills.get(pos).getSkillName() + " ?")
		.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {
				Toast.makeText(
					getBaseContext(),
					"Skill "
						+ skills.get(pos)
							.getSkillName()
						+ " was deleted.",
					Toast.LENGTH_SHORT).show();
				skills.remove(pos);
				skillsAdapter.notifyDataSetChanged();
				// Log.d(LOG_TAG,
				// "----------------- delete skill --------------------");
				// printSkills(skills);
			    }
			})
		.setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {

			    public void onClick(DialogInterface dialog,
				    int which) {
				// ignore
			    }
			}).show();

    }

    public boolean saveArray(ArrayList<SkillModel> array, Context mContext) {
	Editor editor = prefs.edit();
	long date1 = System.currentTimeMillis();

	editor.putString("SkillSave", "save");
	editor.putLong("SaveDate", date1);
	editor.putInt("SkillArray" + "_size", array.size());
	Log.d(LOG_TAG, "Putting size " + array.size());
	for (int i = 0; i < array.size(); i++) {
	    editor.putString("SkillArray" + "_name_" + i, array.get(i)
		    .getSkillName());
	    editor.putFloat("SkillArray" + "_cur_exp_" + i, array.get(i)
		    .getCurExp());
	    editor.putInt("SkillArray" + "_level_" + i, array.get(i).getLevel());
	    editor.putInt("SkillArray" + "_maxLE_" + i, array.get(i)
		    .getMaxLevelUpExp());

	}

	// printSkills(skills);
	return editor.commit();
    }

    public void loadArray(Context mContext) {

	String name = prefs.getString("SkillSave", "");

	if (name != "") {
	    int size = prefs.getInt("SkillArray" + "_size", 0);

	    long loadTime = prefs.getLong("SaveDate", 0);
	    long currantTime = System.currentTimeMillis();
	    int days = (int) ((currantTime - loadTime) / (24 * 60 * 60 * 1000));
	    Log.d(LOG_TAG, "Load " + loadTime + " Current " + currantTime
		    + " diff " + days);
	    Log.d(LOG_TAG, "Getting size " + size);
	    skills.clear();
	    float flashExp = 0;
	    for (int i = 0; i < size; i++) {
		SkillModel tempSkill = new SkillModel();
		tempSkill.setSkillName(prefs.getString("SkillArray" + "_name_"
			+ i, null));
		tempSkill.setCurExp(prefs.getFloat("SkillArray" + "_cur_exp_"
			+ i, 0));
		tempSkill.setLevel(prefs
			.getInt("SkillArray" + "_level_" + i, 0));
		tempSkill.setMaxLevelUpExp(prefs.getInt("SkillArray"
			+ "_maxLE_" + i, 0));

		skills.add(tempSkill);
		// printSkill(tempSkill);
		if (days > 0) {
		    flashExp += skills.get(i).dayExpFlash(days);
		}
		// printSkill(tempSkill);

	    }
	    if (days > 0) {
		Toast.makeText(
			getBaseContext(),
			"You lost " + (int) flashExp + " exp for " + days
				+ " day(s)", Toast.LENGTH_SHORT).show();
	    }
	    // Log.d(LOG_TAG,
	    // "----------------- current skills --------------------");
	    // printSkills(skills);
	    skillsAdapter.notifyDataSetChanged();

	}
    }

    // debug tests
    private void printSkills(ArrayList<SkillModel> skil) {
	Log.d(LOG_TAG, "----------------------------------------------");
	for (SkillModel sm : skil) {
	    Log.d(LOG_TAG, "Name " + sm.getSkillName());
	    Log.d(LOG_TAG, "CurExp " + sm.getCurExp());
	    Log.d(LOG_TAG, "Level " + sm.getLevel());
	    Log.d(LOG_TAG, "MaxExp " + sm.getMaxLevelUpExp());
	}
    }

    private void printSkill(SkillModel skil) {
	Log.d(LOG_TAG, "----------------------------------------------");

	Log.d(LOG_TAG, "Name " + skil.getSkillName());
	Log.d(LOG_TAG, "CurExp " + skil.getCurExp());
	Log.d(LOG_TAG, "Level " + skil.getLevel());
	Log.d(LOG_TAG, "MaxExp " + skil.getMaxLevelUpExp());

    }

    public void onCreateContextMenu(ContextMenu menu, View view,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, view, menuInfo);
	switch (view.getId()) {
	case R.id.listSkills:
	    menu.add(0, EDIT_ID, 0, "Редактировать");
	    menu.add(0, DELETE_ID, 0, "Удалить");
	    break;

	}
    }

    public boolean onContextItemSelected(MenuItem item) {
	final AdapterContextMenuInfo adapterContextMenu = (AdapterContextMenuInfo) item
		.getMenuInfo();

	if (item.getItemId() == EDIT_ID) {
	    editSkill(adapterContextMenu.position);
	}
	if (item.getItemId() == DELETE_ID) {
	    deleteSkill(adapterContextMenu.position);
	}

	return super.onContextItemSelected(item);
    }

}
