package com.mirrorchannelth.internship.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by boss on 4/21/16.
 */
public class WindowsUtil {

    public static void defaultAlertDialog(String title, String message, String label, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        
        builder.setNeutralButton(label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static boolean isRecyclerScrollable(RecyclerView mRecyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (layoutManager == null || adapter == null) return false;

        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1;
    }
}
