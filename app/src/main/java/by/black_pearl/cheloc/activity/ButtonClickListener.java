package by.black_pearl.cheloc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.Coordinates;
import by.black_pearl.cheloc.location.service.ChelocService;

public class ButtonClickListener implements View.OnClickListener{
    private MainActivity mainActivity;
    private final String LOG_TAG = "ButtonClickListener";

    public  ButtonClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private int getSpeedMode() {
        switch (
                ((RadioGroup)(mainActivity.findViewById(R.id.setSpeedModeRadioGroup)))
                .getCheckedRadioButtonId()
                ) {
            case R.id.walkSpeedModeRadioButton:
                return 1;
            case R.id.driveSpeedModeRadioButton:
                return 2;
        }
        return 0;
    }

    private boolean randPosition() {
        return ((CheckBox) mainActivity.findViewById(R.id.randPosCheckBox)).isChecked();
    }

    private Coordinates getCoordinates() {
        Log.i(LOG_TAG, "getCoordinates");
        return new Coordinates(
                Double.valueOf(((EditText) (mainActivity.findViewById(R.id.latEditText)))
                        .getText().toString()),
                Double.valueOf(((EditText) (mainActivity.findViewById(R.id.longEditText)))
                        .getText().toString()),
                Double.valueOf(((EditText) (mainActivity.findViewById(R.id.altEditText)))
                        .getText().toString()),
                0.0f,
                getSpeedMode(),
                ((CheckBox) mainActivity.findViewById(R.id.randPosCheckBox)).isChecked()
        );
    }

    private boolean hasMistakes() {
        Log.i(LOG_TAG, ((EditText) mainActivity.findViewById(R.id.latEditText)).getText().toString());
        return ((EditText) mainActivity.findViewById(R.id.latEditText)).getText().toString().equalsIgnoreCase("") ||
                ((EditText) mainActivity.findViewById(R.id.longEditText)).getText().toString().equalsIgnoreCase("") ||
                ((EditText) mainActivity.findViewById(R.id.altEditText)).getText().toString().equals("");
    }

    private void setLocationToSetPosScrollLayout() {
        if(((TextView)mainActivity.findViewById(R.id.latGpsLocationTextView)).getText()
                != mainActivity.getString(R.string.noValue)) {
            ((EditText)mainActivity.findViewById(R.id.latEditText)).setText(
                    ((TextView) mainActivity.findViewById(R.id.latGpsLocationTextView)).getText()
            );
            ((EditText)mainActivity.findViewById(R.id.longEditText)).setText(
                    ((TextView) mainActivity.findViewById(R.id.longGpsLocationTextView)).getText()
            );
            ((EditText)mainActivity.findViewById(R.id.altEditText)).setText(
                    ((TextView) mainActivity.findViewById(R.id.altGpsLocationTextView)).getText()
            );

        }
    }

    private void stopMockLocation() {
        AlertDialog.Builder stopMockDialog = new AlertDialog.Builder(mainActivity);
        stopMockDialog.setTitle("Включить реальное местоположение?");
        stopMockDialog.setPositiveButton("Да, я не ошибся.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    chelocService.stopMockLocation();
                }
                else {
                    Toast.makeText(mainActivity, "Не удалось подключиться к сервису ;(",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(mainActivity, "Нужно перезапустить приложение",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        stopMockDialog.setNegativeButton("Нет, случайно.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mainActivity, "Реальное положение НЕ включено.",
                        Toast.LENGTH_LONG).show();
            }
        });
        stopMockDialog.show();
    }

    private void closeSetPosLayout() {
        mainActivity.findViewById(R.id.setPosScrollLayout).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.startLayout).setVisibility(View.VISIBLE);
    }

    private void toastReloadApp() {
        Toast.makeText(mainActivity, "Не задано. Перезапустите приложение...",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                mainActivity.finish();
                break;
            case R.id.toSetPosButton:
                setLocationToSetPosScrollLayout();
                mainActivity.findViewById(R.id.startLayout).setVisibility(View.GONE);
                mainActivity.findViewById(R.id.setPosScrollLayout).setVisibility(View.VISIBLE);
                break;
            case R.id.stopMockLocationButton:
                stopMockLocation();
                mainActivity.setServiceStatusOnTextView();
                break;
            case R.id.setPosWithoutUpdatesButton:
                if(hasMistakes()) {
                    return;
                }
                if(mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    if(!chelocService.isWork()) {
                        chelocService.setMockLocation(getCoordinates(), false);
                    }
                    else {
                        chelocService.changeMockLocation(getCoordinates(), false);
                    }
                    closeSetPosLayout();
                }
                else {
                    closeSetPosLayout();
                }
                mainActivity.setServiceStatusOnTextView();
                break;
            case R.id.setPosWithUpdatesButton:
                if(hasMistakes()) {
                    return;
                }
                if(mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    if(!chelocService.isWork()) {
                        Coordinates coordinates = getCoordinates();
                        Intent intentService = new Intent(mainActivity, ChelocService.class);
                        intentService.putExtra("lat", coordinates.getSettedLat());
                        intentService.putExtra("lon", coordinates.getSettedLon());
                        intentService.putExtra("alt", coordinates.getSettedAlt());
                        intentService.putExtra("speedMode", getSpeedMode());
                        intentService.putExtra("randPos", randPosition());
                        intentService.putExtra("bearing", Double.valueOf(0));
                        mainActivity.startService(intentService);
                    }
                    else {
                        chelocService.changeMockLocation(getCoordinates(), true);
                    }
                    closeSetPosLayout();
                }
                else {
                    toastReloadApp();
                }
                mainActivity.setServiceStatusOnTextView();
                break;
            case R.id.cancelSetLocationButton:
                closeSetPosLayout();
                break;
        }
    }
}
