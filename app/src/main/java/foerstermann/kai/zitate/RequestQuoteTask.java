package foerstermann.kai.zitate;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RequestQuoteTask extends AsyncTask<Integer, String, List<Quote>> {

    MainActivity mainActivity;
    MainActivityListener mainActivityListener;

    public RequestQuoteTask(MainActivity mainActivity, MainActivityListener mainActivityListener) {
        this.mainActivity = mainActivity;
        this.mainActivityListener = mainActivityListener;
    }

    @Override
    protected List<Quote> doInBackground(Integer... intParams) {
        //wird im Hintergrund-Thead ausgeführt...
        //führt zeitaufwendige Arbeiten durch...
        int quotesCount = intParams[0];
        int parsingMethod = intParams[1];
        List<Quote> newQuoteList = doTimeConsumingCalculation(quotesCount, parsingMethod);

        //Fortschritt durchgeben
        publishProgress("Das Laden der Ztitate ist beendet.");

        //Ergbenis der Berechnung zurückgeben
        return newQuoteList;
    }

    @Override
    protected void onProgressUpdate(String... stringParams) {
        //wird im UI-Thread ausgeführt
        //Ausgabe Statusmeldung
        String message = stringParams[0];
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT ).show();

    }

    @Override
    protected void onPostExecute(List<Quote> receivedQuoteList) {
        mainActivityListener.quoteList.clear();
        mainActivityListener.quoteList.addAll(receivedQuoteList);
        mainActivityListener.quoteArrayAdapter.notifyDataSetChanged();
    }

    private List<Quote> doTimeConsumingCalculation(int quotesCount, int parsingMethod) {
        List<Quote> newQuoteList = new ArrayList<>();

        String quoteString = Utility.requestQuotesFromServer(quotesCount, parsingMethod);

        if (quoteString != null || quoteString.isEmpty() ) {
            switch (parsingMethod) {
                case Utility.JSON_PARSING_METHOD:
                    publishProgress("JSON-Daten werden verwendet");
                    newQuoteList = Utility.createQuotesFromJSONString(quoteString);
                    break;
                case Utility.XML_PARSING_METHOD:
                    publishProgress("XML-Daten werden verwendet");
                    newQuoteList = Utility.createQuotesFromXMLString(quoteString);
                    break;

            }
            //Zeichenkette auslesen
        } else
            publishProgress("Daten konnten nicht gelesen werden");
        return newQuoteList;
    }
}
