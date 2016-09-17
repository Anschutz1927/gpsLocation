package by.black_pearl.cheloc.activity.mainActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import by.black_pearl.cheloc.DataBaser;
import by.black_pearl.cheloc.activity.scrollActivity.LoadActivity;

/**
 * provider to get exit dialog.
 */

public class Dialogs {

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

    public static void showDeleteAddressDialog(final Context context, final int id, final LoadActivity loadActivity) {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setTitle("Удалить выбранный адрес?");
        deleteDialog.setPositiveButton("Да, верно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaser dataBaser = new DataBaser(context);
                dataBaser.deleteAddress(id);
                loadActivity.reloadLoadActivity();
            }
        });
        deleteDialog.setNegativeButton("Нет, ошибся", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing;
            }
        });
        deleteDialog.show();
    }
}
