package by.black_pearl.cheloc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * provider to get exit dialog.
 */

public class ExitDialog {

    public static void showExitDialog(final MainActivity activity) {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(activity);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Таки да!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing;
            }
        });

        quitDialog.show();
    }
}
