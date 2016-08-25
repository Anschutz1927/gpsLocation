package by.black_pearl.cheloc.location.service;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Print info messages at whole application lifecycle.
 */
public class InfoMessages {
    private Context context;
    private boolean isStop = false;
    private Handler infoMessagesHandler = new Handler();
    private Runnable startRunnable;

    public InfoMessages(Context context) {
        this.context = context;
    }

    public void startingMessages() {
        startRunnable = new Runnable() {
            int n = 9;
            @Override
            public void run() {
                Toast.makeText(context, "Установка через: " + n, Toast.LENGTH_SHORT).show();
                n--;
                n--;
                if(n >= 1) {
                    infoMessagesHandler.postDelayed(this, 2000);
                }
                else {
                    Toast.makeText(context, "Установлено.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        infoMessagesHandler.postDelayed(startRunnable, 1000);
    }

    public void changeMessage() {
        infoMessagesHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Изменено.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void stoppingMessage() {
        if(startRunnable != null) {
            infoMessagesHandler.removeCallbacks(startRunnable);
            infoMessagesHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Отменено пользователем.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
