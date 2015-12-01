package info.devexchanges.androidmenusample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> countries;
    private ArrayAdapter<String> adapter;
    private TextView listViewHeader;
    private Toolbar toolbar;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listViewHeader = (TextView) findViewById(R.id.header_list);
        listViewHeader.setOnClickListener(onShowPopupMenu());

        addingDataToList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries);
        listView.setAdapter(adapter);

        setSupportActionBar(toolbar);

        //register contextMenu with TextView
        registerForContextMenu(listView);
    }

    private void addingDataToList() {
        countries = new ArrayList<>();
        countries.add("Vietnam");
        countries.add("Japan");
        countries.add("China");
        countries.add("Singapore");
        countries.add("USA");
        countries.add("Russia");
        countries.add("Korea");
        countries.add("Thailand");
        countries.add("India");
        countries.add("Malaysia");
        countries.add("Philippines");
        countries.add("Germany");
        countries.add("France");
        countries.add("Netherlands");
    }

    private View.OnClickListener onShowPopupMenu() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(onPopupMenuClickListener());
                popupMenu.inflate(R.menu.menu_pop_up);
                popupMenu.show();
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener onPopupMenuClickListener() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.hide:
                        if (listView.getVisibility() == View.VISIBLE) {
                            //hiding listview
                            listView.setVisibility(View.GONE);
                        } else {
                            //showing listview
                            listView.setVisibility(View.VISIBLE);
                        }
                        return true;
                    case R.id.change_color:
                        listViewHeader.setBackgroundColor(Color.GREEN);
                        return true;
                    case R.id.others:
                        Toast.makeText(MainActivity.this, "Other option, please define later!", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.a_z) {
            sortByName(countries);
            return true;

        } else if (item.getItemId() == R.id.z_a) {
            reverseByName(countries);
            return true;

        } else if (item.getItemId() == R.id.add) {
            showAddingDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_main, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(countries.get(info.position));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //find out which menu item was pressed
        switch (item.getItemId()) {
            case R.id.delete:
                countries.remove(info.position);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.go_wiki:
                String listItemName = countries.get(info.position);
                openBrowser("https://en.wikipedia.org/wiki/" + listItemName);

                return true;
            default:
                return false;
        }
    }

    /**
     * sort countries by name A - Z
     */
    private void sortByName(ArrayList<String> countries) {
        Collections.sort(countries, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareTo(v2);
            }
        });
    }

    /**
     * sort countries by name Z - A
     */
    private void reverseByName(ArrayList<String> countries) {
        Collections.sort(countries, Collections.reverseOrder(new Comparator<String>() {

            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        }));
    }

    private void showAddingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_add_dialog, null, false);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);

        dialogBuilder.setTitle("Adding new Country");
        dialogBuilder.setMessage("Input a Country name");

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Please input some texts!", Toast.LENGTH_SHORT).show();
                } else {
                    //add new country name to List
                    countries.add(editText.getText().toString().trim());

                    //update ListView adapter
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Adding sucessful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing, just close this dialog
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        startActivity(i);
    }
}
