package vankhulup.test.slideshowdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import vankhulup.test.slideshowdemo.fragments.SettingsFragment;
import vankhulup.test.slideshowdemo.fragments.SlideshowFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SlideshowFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SettingsFragment())
                        .addToBackStack("settings")
                        .commit();
                return true;
        }
    return false;
    }

}
