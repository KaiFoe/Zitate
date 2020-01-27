package foerstermann.kai.zitate;

import android.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    MainActivity mainActivity;
    QuotesArrayAdapter quoteArrayAdapter;

    private static final String LISTVIEW_DATA = "Zitatdaten";
    public List<Quote> quoteList = new ArrayList<>();

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        //createQuotesList();
        bindAdapterToListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        int duration = Toast.LENGTH_LONG;
//        String quoteAuthor = quoteList.get(position).getQuoteAuthor();
//        String message = "Ein Zitat von " + quoteAuthor + " wurde angeklickt.";
//        Toast toast = Toast.makeText(mainActivity.getApplicationContext(), message, duration);
//        toast.show();

        //Anfordern des zur angeklickten Position gehörenden Datenobjekts
        Quote clickedQuote = quoteList.get(position);
        String quoteAuthor = clickedQuote.getQuoteAuthor();
        String quoteText = clickedQuote.getQuoteText();

        //Erzeugen des expliziten Intents mit den zugehörigen Daten
        Intent explizit = new Intent(mainActivity, DetailActivity.class);
        explizit.putExtra(DetailActivity.EXTRA_QUOTE_TEXT, quoteText);
        explizit.putExtra(DetailActivity.EXTRA_QUOTE_AUTHOR, quoteAuthor);
        mainActivity.startActivity(explizit);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        String author = quoteList.get(position).getQuoteAuthor();

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(author);
        builder.setMessage(quoteList.get(position).getQuoteText());
        builder.setPositiveButton("Schließen", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        ;
        return true;
    }


    private void createQuotesList() {

        File quotesDataFile = mainActivity.getFileStreamPath(Utility.FILENAME_QUOTE_DATA);

        if (quotesDataFile.exists()) {
            quoteList = Utility.restoreQuoteListFromFile(mainActivity);
        } else {
            String[] sampleQuotes = mainActivity.getResources().getStringArray(R.array.sample_quotes);
            String[] sampleQuotesAuthor = mainActivity.getResources().getStringArray(R.array.quote_authors);

            for (int i = 0; i < sampleQuotes.length; i++) {
                Quote sampleQuote = new Quote(sampleQuotes[i], sampleQuotesAuthor[i], String.valueOf(i));
                quoteList.add(sampleQuote);
            }
        }
    }

    private void bindAdapterToListView() {
        quoteArrayAdapter = new QuotesArrayAdapter(mainActivity, quoteList);
        mainActivity.lvQuotes.setAdapter(quoteArrayAdapter);
    }

    void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (quoteList.size() > 0) {
            String jsonString = Utility.createJSONStringFromQuoteList(quoteList);
            outState.putString(LISTVIEW_DATA, jsonString);
        } else {
            Log.v("SecondActivityListener:", "--> Zitateliste ist leer. Zustand wurden nicht gespeichert");
        }
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
        String jsonString = savedInstanceState.getString(LISTVIEW_DATA);

        if (jsonString != null) {
            List<Quote> restoreQuoteList = Utility.createQuotesFromJSONString(jsonString);
            quoteList.clear();
            quoteList.addAll(restoreQuoteList);
            quoteArrayAdapter.notifyDataSetChanged();
        } else {
            Log.v("SecondActivityListener:", "<-- Es sind keine Zitatdaten im Bundle-Objekt vorhanden.");
            Log.v("SecondActivityListener:", "<-- Der Zustand konnte nicht wiederhergestellt werden.");
        }
    }

    void onStop() {
        if (quoteList.size() > 0) {
            Utility.saveQuoteListInFile(mainActivity, quoteList);
            Log.v("SecondActivityListener:", "--> Zitatdaten in Datei gespeichert");
        } else {
            Log.v("SecondActivityListener:", "--> Zitateliste leer. Es wurden keine Daten gespeichert.");
        }
    }

    public void refreshListView() {
        int quoteCount = 8;
        int parsingMethod = Utility.JSON_PARSING_METHOD;

        //Auslesen der ausgewählten Einstellung aus der SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String prefXmlModeKey = "preference_xmlmode_key";
        boolean isXmlModeOn = sharedPreferences.getBoolean(prefXmlModeKey, false);
        if (isXmlModeOn)
            parsingMethod = Utility.XML_PARSING_METHOD;

        String prefCountKey = "preference_quotecount_key";
        quoteCount = Integer.parseInt(sharedPreferences.getString(prefCountKey, "10"));
        //Instanzieren des TaskObjektes und Starten des Tasks
        //der dafür sorgt, dass die Zitate eingelesen werden
        RequestQuoteTask requestQuoteTask = new RequestQuoteTask(mainActivity, this);
        requestQuoteTask.execute(quoteCount, parsingMethod);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivity.getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_data:
                refreshListView();
                break;
            case R.id.action_settings:
                Intent intentSettings = new Intent(mainActivity, SettingsActivity.class);
                mainActivity.startActivity(intentSettings);
                break;
        }
        return true;
    }
}
