package by.black_pearl.cheloc.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.mainActivity.MainActivity;
import by.black_pearl.cheloc.location.service.ChelocService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    public StartFragment() {
    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        v.findViewById(R.id.exitButton).setOnClickListener(this);
        v.findViewById(R.id.toSetPosButton).setOnClickListener(this);
        v.findViewById(R.id.stopMockLocationButton).setOnClickListener(this);
        setGpsListeners(v);
        return v;
    }

    private void setGpsListeners(View v) {
        v.findViewById(R.id.serviceStatusTextView)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                getActivity().finish();
                break;
            case R.id.toSetPosButton:
                ((MainActivity) getActivity()).fragmentChanger(MainActivity.CHANGE_TO_SETPOSFRAGMENT);
                break;
            case R.id.stopMockLocationButton:
                stopMockLocation();
                break;
        }
    }

    private void stopMockLocation() {

        AlertDialog.Builder stopMockDialog = new AlertDialog.Builder(getContext());
        stopMockDialog.setTitle("Включить реальное местоположение?");
        stopMockDialog.setPositiveButton("Да, я не ошибся.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (((MainActivity) getActivity()).getBound()) {
                    ChelocService chelocService = ((MainActivity) getActivity()).getChelocService();
                    chelocService.stopMockLocation();
                } else {
                    Toast.makeText(getContext(), "Не удалось подключиться к сервису ;(",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Нужно перезапустить приложение",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        stopMockDialog.setNegativeButton("Нет, случайно.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Реальное положение НЕ включено.",
                        Toast.LENGTH_LONG).show();
            }
        });
        stopMockDialog.show();
    }

}
