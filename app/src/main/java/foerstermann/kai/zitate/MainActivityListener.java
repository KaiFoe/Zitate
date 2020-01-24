package foerstermann.kai.zitate;

import android.app.AlertDialog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    MainActivity mainActivity;
    QuotesArrayAdapter quoteArrayAdapter;

    private static final String LISTVIEW_DATA = "Zitatdaten";
    private List<Quote> quoteList = new ArrayList<>();

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        createQuotesList();
        bindAdapterToListView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int duration = Toast.LENGTH_LONG;
        String quoteAuthor = quoteList.get(position).getQuoteAuthor();
        String message = "Ein Zitat von " + quoteAuthor + " wurde angeklickt.";
        Toast toast = Toast.makeText(mainActivity.getApplicationContext(), message, duration);
        toast.show();
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

}
