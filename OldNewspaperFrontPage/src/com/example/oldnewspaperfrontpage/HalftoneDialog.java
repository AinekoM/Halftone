package com.example.oldnewspaperfrontpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class HalftoneDialog extends DialogFragment {
	HalftoneOptionsView v;
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        builder.setTitle(R.string.halftone_dialog)
	        	   .setView(inflater.inflate(R.layout.sample_halftone_options_view, null))
	               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                   
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       
	                   }
	               });
	   
	        return builder.create();
	    }
	

}
