package com.thomashaertel.widget;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.DataSetObserver;
import android.util.AttributeSet;
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
		this(context, attr,  R.attr.spinnerStyle);
	}

	public MultiSpinner(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			mSelected[which] = isChecked;
	}

	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

		String choices[] = new String[mAdapter.getCount()];

		for (int i = 0; i < choices.length; i++) {
			choices[i] = mAdapter.getItem(i).toString();
		}

		for (int i = 0; i < mSelected.length; i++) {
			mOldSelection[i] = mSelected[i];
		}
		
		builder.setMultiChoiceItems(choices, mSelected, this);

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
		return true;
	}

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
		
		this.mAdapter = adapter;
		this.mListener = listener;
		this.mAllSelected = allSelected;

		if(oldAdapter != null) {
			oldAdapter.unregisterDataSetObserver(dataSetObserver);
		}
		
		if(mAdapter != null) {
			mAdapter.registerDataSetObserver(dataSetObserver);
			
			// all selected by default
			mOldSelection = new boolean[mAdapter.getCount()];
			mSelected = new boolean[mAdapter.getCount()];
			for (int i = 0; i < mSelected.length; i++) {
				mOldSelection[i] = false;
				mSelected[i] = allSelected;
			}
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
