package foerstermann.kai.zitate;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        //Erzeugen der App-Einstellungen
        addPreferencesFromResource(R.xml.preferences);

        //Die Listener registrieren
        Preference xml_preference = findPreference("preference_xmlmode_key");
        Preference quoteCount_preference = findPreference("preference_quotecount_key");
        xml_preference.setOnPreferenceChangeListener(this);
        quoteCount_preference.setOnPreferenceChangeListener(this);

        //Auslesen der Einstellungswerte und Auslösen des Listeners
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean preference_xml_Value = sharedPreferences.getBoolean(xml_preference.getKey(), false);
        onPreferenceChange(xml_preference, preference_xml_Value);

        String preference_count_Value = sharedPreferences.getString(quoteCount_preference.getKey(), "10");
        onPreferenceChange(quoteCount_preference, preference_count_Value);


    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String preferenceKey = preference.getKey();


        if (preferenceKey.equals("preference_xmlmode_key")) {
            boolean isXMLModeOn = (boolean) newValue;
            if(isXMLModeOn)
                preference.setSummary("Der XML-Datenmodus ist aktiviert");
            else
                preference.setSummary("Der XML-Datenmodus ist deaktiviert");
        }

        if (preferenceKey.equals("preference_quotecount_key")) {
            String quoteCount = (String) newValue;
            preference.setSummary("Gewählte Anzahl: " + quoteCount);
        }
        return true;
    }
}
