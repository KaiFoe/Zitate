package foerstermann.kai.zitate;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QuotesArrayAdapter extends ArrayAdapter<Quote> {


    private Context context;
    private List<Quote> quoteList;
    private LayoutInflater layoutInflater;

    private Resources resources;
    private String packageName;

    public QuotesArrayAdapter(Context context, List<Quote> quoteList) {
        super(context, R.layout.listview_row, quoteList);

        this.context = context;
        this.quoteList = quoteList;
        layoutInflater = LayoutInflater.from(context);

        this.resources = context.getResources();
        this.packageName = context.getPackageName();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Erzeugen der View-Hierarchie auf Grundlage des Zeilenlayouts
        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.listview_row, parent, false);
        } else {
            rowView = convertView;
        }

        //Anfordern des zur Listenposition gehörenden Datenobjektes
        Quote currentQuote = quoteList.get(position);

        //Finden der einzelnen View-Objekte
        TextView txtvQuoteText = (TextView) rowView.findViewById(R.id.txtvQuoteText);
        TextView txtvQuoteAutor = (TextView) rowView.findViewById(R.id.txtvQuoteAuthor);

        //Füllen der View-Objekte mit den passenden Inhalten des Datenobjekts
        txtvQuoteText.setText("\"" + currentQuote.getQuoteText() + "\"");
        txtvQuoteAutor.setText(currentQuote.getQuoteAuthor());

        //Rückgabe der befüllten View-Hierarchie an die aufrufende AdapterView
        return rowView;
    }


}
