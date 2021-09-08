package apps.attendancemanagementsystem.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AppVersion extends AsyncTask<String, Void, String> {

    @Override
    public String doInBackground(String[] params) {
        // do above Server call here
        String result = "";
        result = getPlayStoreAppVersion("https://play.google.com/store/apps/details?id=apps.attendancemanagementsystem");
        //result = getPlayStoreAppVersion("https://play.google.com/store/apps/internaltest/4701393277777419568");
        return result;
    }

    @Override
    protected void onPostExecute(String message) {
        //Log.e("VERSION Meditech", " APP VERSION " + message);
    }

    private String getPlayStoreAppVersion(String appUrlString) {
        final String currentVersion_PatternSeq = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>";
        final String appVersion_PatternSeq = "htlgb\">([^<]*)</s";
        String playStoreAppVersion = null;

        BufferedReader inReader = null;
        URLConnection uc = null;
        StringBuilder urlData = new StringBuilder();

        URL url = null;
        try {
            url = new URL(appUrlString);
            uc = url.openConnection();

            if (uc == null) {
                return null;
            }
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            inReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String str = "";
            while ((str = inReader.readLine()) != null) {
                urlData.append(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the current version pattern sequence
        String versionString = getAppVersion(currentVersion_PatternSeq, urlData.toString());
        if (null == versionString) {
            return null;
        } else {
            // get version from "htlgb">X.X.X</span>
            playStoreAppVersion = getAppVersion(appVersion_PatternSeq, versionString);
        }
        return playStoreAppVersion;
    }

    private String getAppVersion(String patternString, String inputString) {
        try {
            //Create a pattern
            Pattern pattern = Pattern.compile(patternString);
            if (null == pattern) {
                return null;
            }

            //Match the pattern string in provided string
            Matcher matcher = pattern.matcher(inputString);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}