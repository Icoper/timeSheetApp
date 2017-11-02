package com.example.samik.timesheetappfr.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.samik.timesheetappfr.R;
import com.example.samik.timesheetappfr.basedata.BaseDataHelper;
import com.example.samik.timesheetappfr.basedata.BaseDataMaster;

import java.util.Calendar;
import java.util.HashMap;

public class FragmentExport extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "FragmentExport";
    private static final int DIALOG_DATE = 1;

    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private static final String END = "end";
    private static final String START = "start";
    private String dataKey;
    // Хранит последние данные введенные пользователем в соответсвующие поля
    private String latestEmail;
    private String latestSubject;
    private String latestMessage;

    private Context fContext;

    // Хранит данные выбранного периода отправки пользовательских данных
    private HashMap<String, Integer> userDataMap;
    // View elements
    private EditText emailEt;
    private EditText subjectEt;
    private EditText messageEt;
    private EditText startData;
    private EditText endData;
    private ImageButton startDCalendar;
    private ImageButton endDCalendar;
    private Button sendEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Создем наш фрашмент и инициализируем его
        View view = inflater.inflate(R.layout.screen_email, container, false);
        fContext = view.getContext();
        setupUI(view);
        return view;
    }

    // Настраимваем и инициализируем все view элементы
    private void setupUI(View container) {

        emailEt = (EditText) container.findViewById(R.id.fe_email_et);
        subjectEt = (EditText) container.findViewById(R.id.fe_subject_et);
        messageEt = (EditText) container.findViewById(R.id.fe_message_et5);
        startData = (EditText) container.findViewById(R.id.fe_calendar_startd_et);
        endData = (EditText) container.findViewById(R.id.fe_calendar_endd_et);
        startDCalendar = (ImageButton) container.findViewById(R.id.fe_calendar_startd_ib);
        endDCalendar = (ImageButton) container.findViewById(R.id.fe_calendar_endd_ib);
        sendEmail = (Button) container.findViewById(R.id.fe_send_email_bt);

        startData.setOnClickListener(this);
        endData.setOnClickListener(this);
        startDCalendar.setOnClickListener(this);
        endData.setOnClickListener(this);
        sendEmail.setOnClickListener(this);

        updateUI();
    }


    private void updateUI() {
        BaseDataMaster baseDataMaster = BaseDataMaster.getDataMaster(fContext);
        HashMap<String, String> lastUserEmailData = new HashMap<>();
        // Если пользовватель уже вводил данные, автоматически зополняем поля
        if (!baseDataMaster.getEmailData().isEmpty()) {
            emailEt.setText(lastUserEmailData.get(BaseDataHelper.User.EMAIL));
            subjectEt.setText(lastUserEmailData.get(BaseDataHelper.User.SUBJECT));
            messageEt.setText(lastUserEmailData.get(BaseDataHelper.User.MESSAGE));
        }
    }

    @Override
    public void onClick(View view) {
        // Получаем id вызванной view
        int view_id = view.getId();
        switch (view_id) {
            case R.id.fe_calendar_endd_et:
                openCalendar(view_id);
                break;
            case R.id.fe_calendar_endd_ib:
                openCalendar(view_id);
                break;
            case R.id.fe_calendar_startd_et:
                openCalendar(view_id);
                break;
            case R.id.fe_calendar_startd_ib:
                openCalendar(view_id);
                break;
            case R.id.fe_send_email_bt:
                sendUserData();
                break;
        }
    }

    /**
     * TODO
     */
    private void sendUserData() {
        BaseDataMaster baseDataMaster = BaseDataMaster.getDataMaster(fContext);
        HashMap<String, String> lastUserEmailData = new HashMap<>();

        try {
            if (!baseDataMaster.getEmailData().isEmpty()) {
                lastUserEmailData = baseDataMaster.getEmailData();
                latestEmail = lastUserEmailData.get(BaseDataHelper.User.EMAIL);
                latestSubject = lastUserEmailData.get(BaseDataHelper.User.SUBJECT);
                latestMessage = lastUserEmailData.get(BaseDataHelper.User.MESSAGE);
            } else {
                // Сохраняем данные введенные пользователем в переменные
                latestEmail = emailEt.getText().toString();
                latestSubject = subjectEt.getText().toString();
                latestMessage = messageEt.getText().toString();

                // Если пользователь менял последние сохраненные данные, обновзяем их
                if (!latestEmail.equals(lastUserEmailData.get(BaseDataHelper.User.EMAIL))
                        || !latestSubject.equals(lastUserEmailData.get(BaseDataHelper.User.SUBJECT))
                        || !latestMessage.equals(lastUserEmailData.get(BaseDataHelper.User.MESSAGE))) {

                    // Сохраняем новые данные в локальную базу данных
                    lastUserEmailData.put(BaseDataHelper.User.EMAIL, emailEt.getText().toString());
                    lastUserEmailData.put(BaseDataHelper.User.SUBJECT, subjectEt.getText().toString());
                    lastUserEmailData.put(BaseDataHelper.User.MESSAGE, messageEt.getText().toString());
                    baseDataMaster.insertEmailData(lastUserEmailData);
                }

            }
        } catch (
                NullPointerException e)

        {
            e.printStackTrace();
        }

        // Создаем интент с экшеном на отправку
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        // Заполняем данными: тип текста, адрес, сабж и собственно текст письма
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, latestEmail);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, latestSubject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, latestMessage);
        /* Отправляем на выбор!*/
        fContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));


    }


    /**
     * Говимся к показу виджета календаря польхователю и обрвботки данныъ
     *
     * @param view_id - id view элемента по которому тапнул пользователь.
     *                в зависимости от него обрабатываем информацию по своему
     */
    private void openCalendar(int view_id) {
        // Проверяем userDataMap на null.
        if (userDataMap == null) {
            userDataMap = new HashMap<>();
        }

        DialogFragment dialogFragment = new FragmentExport.DatePicker();
        if (view_id == R.id.fe_calendar_endd_et || view_id == R.id.fe_calendar_endd_ib) {
            dataKey = END;
        } else if (view_id == R.id.fe_calendar_startd_et || view_id == R.id.fe_calendar_startd_ib) {
            dataKey = START;
        }
        dialogFragment.show(getFragmentManager(), "dataPicker");
    }

    /**
     * Этот класс используется для показа пользователю DataPicker dialog
     * В нем реализована логика сохранения даты типа start и end в HashMap
     */
    @SuppressLint("ValidFragment")
    protected class DatePicker extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        // Ключ, отвечает за то, какую дату мы считываем, start data or end data;
        // key может иметь 2 значения : start или end;
        private String key = null;

        /**
         * Показываем виджет календаря пользователю
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // определяем текущую дату
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // создаем DatePickerDialog и возвращаем его
            Dialog picker = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            return picker;
        }

        /**
         * Метод сохраняет данные полученные dataPicker от пользователя
         *
         * @param datePicker - виджет календаря
         * @param year       - год выбранный пользователем
         * @param month      - месяц выбранный пользователем
         * @param day        - день выбранный пользователем
         */
        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year,
                              int month, int day) {
            // Объявляем переменну EditText, которой позже присвоем ссылку на
            // startData || endData (EditText)
            // Это нужно для того, что бы указать выбранную дату в правильном EditText
            EditText varEditText = startData;
            key = dataKey;
            // В зависимости от ключа, присваеваем varEditText нужную нам ссылку
            if (key != null) {
                if (key.equals(START)) {
                    varEditText = startData;
                } else varEditText = endData;
            }
            // Сохраняем нащи данные в HashMap
            userDataMap.put(key + DAY, day);
            // Делепем инкремент ++month потому что
            // счет месяцев в dataPicker наченается с 0 а не с 1
            userDataMap.put(key + MONTH, ++month);
            userDataMap.put(key + YEAR, year);
            // Присваеваем новое значение в EditText
            varEditText.setText(day + " / " + month + " / " + year);
        }
    }

}
