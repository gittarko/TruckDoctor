package com.juma.truckdoctor.js.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

/**
 * Created by hedong on 16/8/5.
 */

public class AlertDialogFragment extends DialogFragment {
    private static final String TAG = AlertDialogFragment.class.getSimpleName();
    private AlertDialog dialog;
    private Activity activity;

    public void show() {
        show(activity.getFragmentManager(), TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return dialog;
    }

    public static class Builder extends AlertDialog.Builder {

        private Activity activity;

        public Builder(Context context) {
            super(context);
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
        }

        public Builder(Context context, int themeResId) {
            super(context, themeResId);
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
        }

        @Override
        public Builder setTitle(int titleId) {
            super.setTitle(titleId);
            return this;
        }

        @Override
        public Builder setTitle(CharSequence title) {
            super.setTitle(title);
            return this;
        }

        @Override
        public Builder setCustomTitle(View customTitleView) {
            super.setCustomTitle(customTitleView);
            return this;
        }

        @Override
        public Builder setMessage(int messageId) {
            super.setMessage(messageId);
            return this;
        }

        @Override
        public Builder setMessage(CharSequence message) {
            super.setMessage(message);
            return this;
        }

        @Override
        public Builder setIcon(int iconId) {
            super.setIcon(iconId);
            return this;
        }

        @Override
        public Builder setIcon(Drawable icon) {
            super.setIcon(icon);
            return this;
        }

        @Override
        public Builder setIconAttribute(int attrId) {
            super.setIconAttribute(attrId);
            return this;
        }

        @Override
        public Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
            super.setPositiveButton(textId, listener);
            return this;
        }

        @Override
        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            super.setPositiveButton(text, listener);
            return this;
        }

        @Override
        public Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            super.setNegativeButton(textId, listener);
            return this;
        }

        @Override
        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            super.setNegativeButton(text, listener);
            return this;
        }

        @Override
        public Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
            super.setNeutralButton(textId, listener);
            return this;
        }

        @Override
        public Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            super.setNeutralButton(text, listener);
            return this;
        }

        @Override
        public Builder setCancelable(boolean cancelable) {
            super.setCancelable(cancelable);
            return this;
        }

        @Override
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            super.setOnCancelListener(onCancelListener);
            return this;
        }

        @Override
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            super.setOnDismissListener(onDismissListener);
            return this;
        }

        @Override
        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            super.setOnKeyListener(onKeyListener);
            return this;
        }

        @Override
        public Builder setItems(int itemsId, DialogInterface.OnClickListener listener) {
            super.setItems(itemsId, listener);
            return this;
        }

        @Override
        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            super.setItems(items, listener);
            return this;
        }

        @Override
        public Builder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            super.setAdapter(adapter, listener);
            return this;
        }

        @Override
        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            super.setCursor(cursor, listener, labelColumn);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            super.setMultiChoiceItems(itemsId, checkedItems, listener);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            super.setMultiChoiceItems(items, checkedItems, listener);
            return this;
        }

        @Override
        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            super.setSingleChoiceItems(itemsId, checkedItem, listener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            super.setSingleChoiceItems(items, checkedItem, listener);
            return this;
        }

        @Override
        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            super.setSingleChoiceItems(adapter, checkedItem, listener);
            return this;
        }

        @Override
        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            super.setOnItemSelectedListener(listener);
            return this;
        }

        @Override
        public Builder setView(int layoutResId) {
            super.setView(layoutResId);
            return this;
        }

        @Override
        public Builder setView(View view) {
            super.setView(view);
            return this;
        }

        public AlertDialogFragment createDialog() {
            AlertDialog dialog = create();
            AlertDialogFragment dialogFragment = new AlertDialogFragment();
            dialogFragment.dialog = dialog;
            dialogFragment.activity = activity;
            return dialogFragment;
        }
    }

    public static void showDemoDialog(Context context) {
        new AlertDialogFragment.Builder(context)
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .createDialog()
                .show();
    }
}
