package com.dhammalab.satipatthna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by anthony.lipscomb on 10/5/2014.
 */
public class CreditsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_credits, null));
        Dialog dialog = builder.create();
        dialog.show();

        String lhtext = "Visit ";
        String rhtext = " for more information.";

        TextView linkTextView = (TextView) dialog.findViewById(R.id.credits_link_text);
        linkTextView.setText(Html.fromHtml(lhtext + "<a href=\"http://www.dhammalab.com\">www.dhammalab.com</a>" + rhtext));
        linkTextView.setClickable(true);
        linkTextView.setMovementMethod (LinkMovementMethod.getInstance());
        return dialog;
    }
}
