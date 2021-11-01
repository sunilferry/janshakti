package apps.janshakti.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

public class Utils {

    public static Dialog createSimpleDialog1(Context context, String title, String msg, String btnLabel1, final Method method1) {
        String chars = title;
        SpannableString str = new SpannableString(chars);
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#199D59")), 0,
                chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str);
        builder.setMessage(msg);

        builder.setPositiveButton(btnLabel1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (method1 != null)
                            method1.execute();
                    }
                });

        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        try {
            divider.setBackgroundColor(Color.parseColor("#199D59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public interface Method {
        void execute();
    }
}
