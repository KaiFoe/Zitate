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

        //Auslesen der Einstellungswerte und Auslösen des Listeners
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean preferenceValue = sharedPreferences.getBoolean(xml_preference.getKey(), false);
        onPreferenceChange(xml_preference, preferenceValue);


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
        return true;
    }
}
