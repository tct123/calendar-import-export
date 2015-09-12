/**
 *  Copyright (C) 2015  Jon Griffiths (jon_p_griffiths@yahoo.com)
 *  Copyright (C) 2010-2011  Lukas Aichbauer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sufficientlysecure.ical.ui.dialogs;

import org.sufficientlysecure.ical.R;
import org.sufficientlysecure.ical.ui.MainActivity;
import org.sufficientlysecure.ical.util.Log;

import android.app.ProgressDialog;

public abstract class RunnableWithProgress {
    private final MainActivity mActivity;
    private ProgressDialog mProgress;

    public RunnableWithProgress(MainActivity activity) {
        mActivity = activity;
        init(ProgressDialog.STYLE_SPINNER);
    }

    protected RunnableWithProgress(MainActivity activity, int style) {
        mActivity = activity;
        init(style);
    }

    private void init(int style) {
        mProgress = new ProgressDialog(mActivity);
        mProgress.setProgressStyle(style);
        mProgress.setCancelable(false);
        mProgress.setMessage("");
        mProgress.setTitle("");
        mProgress.show();
    }

    public void start() {
        new Thread(new Runnable() {
                       public void run() {
                           RunnableWithProgress.this.run();
                           mProgress.cancel();
                       }
                   }).start();
    }

    protected MainActivity getActivity() {
        return mActivity;
    }

    protected void setMax(int max) {
        mProgress.setMax(max);
    }

    protected void setMessage(final int id) {
        mActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mProgress.setMessage(mActivity.getString(id));
                                    }
                                });
    }

    protected void incrementProgressBy(final int amount) {
        mActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mProgress.incrementProgressBy(amount);
                                    }
                                });
    }

    public final void run() {
        try {
            runImpl();
        } catch (Exception e) {
            Log.e("RunnableWithProgress", "An exception occurred", e);
            DialogTools.info(getActivity(), R.string.error, "Error:\n" + e.getMessage());
        }
    }

    protected abstract void runImpl() throws Exception;
}
