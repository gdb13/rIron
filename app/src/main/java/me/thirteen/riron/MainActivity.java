package me.thirteen.riron;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import android.util.Log;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public final static String EXTRA_MESSAGE = "me.thirteen.riron.MESSAGE";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /** INITIALIZE USER LISTS AND SAMPLE EXERCISES IF FIRST RUN **/
        SharedPreferences ex_lists_pref = getApplicationContext().getSharedPreferences("rIronLists", 0); // 0 - for private mode
        SharedPreferences exercises_pref = getApplicationContext().getSharedPreferences("rIronExercises", 0); // 0 - for private mode
        //here the first parameter is name of your pref file which will hold your data. you can give any name.
        // Note here the 2nd parameter 0 is the default parameter for private access.

        if (!(ex_lists_pref.getInt("first_run", 0) == 1)) {
            Editor lists_editor = ex_lists_pref.edit(); // used for save data
            lists_editor.putInt("first_run", 1);

            Set back_bi = new HashSet();
            Set chest_tri = new HashSet();
            Set legs = new HashSet();
            back_bi.add("Barbell Deadlift");
            back_bi.add("Close Grip Cable Pulldown");
            back_bi.add("Barbell Rows");
            chest_tri.add("Barbell Benchpress");
            chest_tri.add("Preacher Curl");
            chest_tri.add("Dips");
            legs.add("Barbell Squat");
            legs.add("Barbell Stiff-legged Deadlift");
            legs.add("Calf Raises");
            lists_editor.putStringSet("Back and Biceps", back_bi);
            lists_editor.putStringSet("Chest and Triceps", chest_tri);
            lists_editor.putStringSet("Legs", legs);

            lists_editor.commit();

            Editor exercises_editor = exercises_pref.edit(); // used for save data

            exercises_editor.putString("Barbell Deadlift", "0,0");
            exercises_editor.putString("Close Grip Cable Pulldown", "0,0");
            exercises_editor.putString("Barbell Rows", "0,0");
            exercises_editor.putString("Barbell Benchpress", "0,0");
            exercises_editor.putString("Preacher Curl", "0,0");
            exercises_editor.putString("Dips", "0,0");
            exercises_editor.putString("Barbell Squat", "0,0");
            exercises_editor.putString("Barbell Stiff-legged Deadlift", "0,0");
            exercises_editor.putString("Calf Raises", "0,0");

            exercises_editor.commit();

        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /** Called when the user clicks the decrement_reps button */
    public void decrement_reps(View view) {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        TextView reps_textview = (TextView) findViewById(R.id.textView2);
        String reps_value = reps_textview.getText().toString();
        Integer reps_int = Integer.parseInt(reps_value);
        reps_int--;
        reps_textview.setText(Integer.toString(reps_int));
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    /** Called when the user clicks the increment_reps button */
    public void increment_reps(View view) {
        TextView reps_textview = (TextView) findViewById(R.id.textView2);
        String reps_value = reps_textview.getText().toString();
        Integer reps_int = Integer.parseInt(reps_value);
        reps_int++;
        reps_textview.setText(Integer.toString(reps_int));
    }

    /** Called when the user clicks the decrement_weight button */
    public void decrement_weight(View view) {
        TextView weight_textview = (TextView) findViewById(R.id.textView3);
        String weight_value = weight_textview.getText().toString();
        Integer weight_int = Integer.parseInt(weight_value);
        weight_int -= 5;
        weight_textview.setText(Integer.toString(weight_int));
    }

    /** Called when the user clicks the increment_weight button */
    public void increment_weight(View view) {
        TextView weight_textview = (TextView) findViewById(R.id.textView3);
        String weight_value = weight_textview.getText().toString();
        Integer weight_int = Integer.parseInt(weight_value);
        weight_int += 5;
        weight_textview.setText(Integer.toString(weight_int));
    }

    /** Called when the user clicks the Random button */
    public void randomize_exercise(View view) {

        TextView exercise_textview = (TextView) findViewById(R.id.textView);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("list_name", 0); // 0 - for private mode
        //here the first parameter is name of your pref file which will hold your data. you can give any name.
        // Note here the 2nd parameter 0 is the default parameter for private access.

        Map<String,?> keys = pref.getAll();

        int key_idx = 0;
        String [] exerciseName = new String[keys.size()];
        for(Map.Entry<String,?> entry : keys.entrySet()) {
            exerciseName[key_idx] = entry.getKey();
            //Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
            key_idx++;

        }

        Random rand = new Random();
        int intRandExercise = rand.nextInt(keys.size());
        exercise_textview.setText(exerciseName[intRandExercise]);

        String reps_weight = pref.getString(exerciseName[intRandExercise], "");
        /** CODE HERE TO PARSE AND FILL REP & WEIGHT TEXTVIEWS **/

        Log.d("map size", Integer.toString(keys.size()));

    }

    /*** TEMPORARY TEST BUTTONS -- TEST/CODE DATA SAVE AND RECALL -- SharedPreferences ***/

    /** ADD ITEM **/
    /** Called when the user clicks the ADD button -- save string editText to SharedPreferences, toast list*/
    public void add_list(View view) {
        EditText test_text = (EditText) findViewById(R.id.editText);
        String new_exercise = test_text.getText().toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("rIronExercises", 0); // 0 - for private mode
        //here the first parameter is name of your pref file which will hold your data. you can give any name.
        // Note here the 2nd parameter 0 is the default parameter for private access.

        Editor editor = pref.edit(); // used for save data

        // *** put duplicate value check here, execute below code if new value
        if (pref.contains(new_exercise) == false) {
            //int list_length = pref.getInt("list_length", 0);


            //key[new_exercise] = reps,weight

            editor.putString(new_exercise, "0,0");


            //list_length++;

            //editor.putInt("list_length", list_length);

            editor.commit();

            test_text.setText("");

            Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already exists!", Toast.LENGTH_SHORT).show();
        }




        Map<String,?> keys = pref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
        }

        Log.d("map size", Integer.toString(keys.size()));

    }

    /** REMOVE ITEM **/
    /** Called when the user clicks the REMOVE button -- rem string editText from SharedPref, toast list*/
    public void remove_list(View view) {
        EditText test_text = (EditText) findViewById(R.id.editText);
        String remove_exercise = test_text.getText().toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("list_name", 0); // 0 - for private mode
        //here the first parameter is name of your pref file which will hold your data. you can give any name.
        // Note here the 2nd parameter 0 is the default parameter for private access.

        Editor editor = pref.edit(); // used for save data

        if (pref.contains(remove_exercise) == true) {
            editor.remove(remove_exercise);
            editor.commit();
            test_text.setText("");
            Toast.makeText(getApplicationContext(), "Removed!", Toast.LENGTH_SHORT).show();
        }

        Map<String,?> keys = pref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
        }

        Log.d("map size", Integer.toString(keys.size()));

    }

    /** CLEAR LIST **/
    /** Called when the user clicks the CLEAR LIST button -- rem string editText from SharedPref*/
    public void clear_list(View view) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("list_name", 0); // 0 - for private mode
        //here the first parameter is name of your pref file which will hold your data. you can give any name.
        // Note here the 2nd parameter 0 is the default parameter for private access.

        Editor editor = pref.edit(); // used for save data

        editor.clear();

        editor.commit();

        Map<String,?> keys = pref.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
        }

        Log.d("map size", Integer.toString(keys.size()));

    }

}
