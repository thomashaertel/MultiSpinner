/*
 * Copyright (c) 2015. Thomas Haertel
 *
 * Licensed under MIT License (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://opensource.org/licenses/MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The software is provided "AS IS", Without warranty of any kind, express or
 * implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose and noninfringement, in no event shall the
 * authors or copyright holders be liable for any claim, damages or other
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in
 * the software.
 */
package com.thomashaertel.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MultiSpinner extends TextView implements OnMultiChoiceClickListener {

    private SpinnerAdapter mAdapter;
    private boolean[] mOldSelection;
    private boolean[] mSelected;
    private String mDefaultText;
    private String mAllText;
    private boolean mAllSelected;
    private MultiSpinnerListener mListener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context context, AttributeSet attr) {
        this(context, attr, R.attr.spinnerStyle);
    }

    public MultiSpinner(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mSelected[which] = isChecked;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            String choices[] = new String[mAdapter.getCount()];

            for (int i = 0; i < choices.length; i++) {
                choices[i] = mAdapter.getItem(i).toString();
            }

            for (int i = 0; i < mSelected.length; i++) {
                mOldSelection[i] = mSelected[i];
            }

            builder.setMultiChoiceItems(choices, mSelected, MultiSpinner.this);

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < mSelected.length; i++) {
                        mSelected[i] = mOldSelection[i];
                    }

                    dialog.dismiss();
                }
            });

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    refreshSpinner();
                    mListener.onItemsSelected(mSelected);
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    };

    public SpinnerAdapter getAdapter() {
        return this.mAdapter;
    }

    DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            // all selected by default
            mOldSelection = new boolean[mAdapter.getCount()];
            mSelected = new boolean[mAdapter.getCount()];
            for (int i = 0; i < mSelected.length; i++) {
                mOldSelection[i] = false;
                mSelected[i] = mAllSelected;
            }
        }
    };


    public void setAdapter(SpinnerAdapter adapter, boolean allSelected, MultiSpinnerListener listener) {
        SpinnerAdapter oldAdapter = this.mAdapter;

        setOnClickListener(null);
        
        this.mAdapter = adapter;
        this.mListener = listener;
        this.mAllSelected = allSelected;

        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(dataSetObserver);
        }

        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(dataSetObserver);

            // all selected by default
            mOldSelection = new boolean[mAdapter.getCount()];
            mSelected = new boolean[mAdapter.getCount()];
            for (int i = 0; i < mSelected.length; i++) {
                mOldSelection[i] = false;
                mSelected[i] = allSelected;
            }
            
            setOnClickListener(onClickListener);
        }

        // all text on the spinner
        setText(mAllText);
    }

    public void setOnItemsSelectedListener(MultiSpinnerListener listener) {
        this.mListener = listener;
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }

    public boolean[] getSelected() {
        return this.mSelected;
    }

    public void setSelected(boolean[] selected) {
        if (this.mSelected.length != selected.length)
            return;

        this.mSelected = selected;

        refreshSpinner();
    }

    private void refreshSpinner() {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        boolean allUnselected = true;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mSelected[i]) {
                spinnerBuffer.append(mAdapter.getItem(i).toString());
                spinnerBuffer.append(", ");
                allUnselected = false;
            } else {
                someUnselected = true;
            }
        }

        String spinnerText;

        if (!allUnselected) {
            if (someUnselected && !(mAllText != null && mAllText.length() > 0)) {
                spinnerText = spinnerBuffer.toString();
                if (spinnerText.length() > 2)
                    spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            } else {
                spinnerText = mAllText;
            }
        } else {
            spinnerText = mDefaultText;
        }

        setText(spinnerText);
    }

    public String getDefaultText() {
        return mDefaultText;
    }

    public void setDefaultText(String defaultText) {
        this.mDefaultText = defaultText;
    }

    public String getAllText() {
        return mAllText;
    }

    public void setAllText(String allText) {
        this.mAllText = allText;
    }
}
