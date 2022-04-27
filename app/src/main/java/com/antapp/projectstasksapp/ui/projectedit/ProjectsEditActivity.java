package com.antapp.projectstasksapp.ui.projectedit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.antapp.projectstasksapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.antapp.projectstasksapp.database.AppDatabase;
import com.antapp.projectstasksapp.database.DatabaseHelper;
import com.antapp.projectstasksapp.database.ProjectEntity;

public class ProjectsEditActivity extends AppCompatActivity {

    private final String DATE_TAG = "DATE_TAG";

    private TextView tvDeadline;
    private Button btnSave;
    private ImageView ivCalendar;
    private EditText etTitle, etDescription;
    private AppDatabase database;

    // Объект для хранения даты
    private final Calendar calendar = Calendar.getInstance();
    // Объект для форматирования даты в строку
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        database = DatabaseHelper.getDatabase(this);

        // на всякий случай проверяем что верхняя панель не null
        if (getSupportActionBar() != null) {
            // отображаем стрелку назад в верхней панели
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvDeadline = findViewById(R.id.tvDeadline);
        btnSave = findViewById(R.id.btnSave);
        ivCalendar = findViewById(R.id.ivCalendar);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProject();
            }
        });

        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvDeadline.setText(format.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog(ProjectsEditActivity.this, dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        setTitle(getString(R.string.project_editor));

        // если id > -1 значит это редактирование проекта, значит нужно заполнить поля данными этого проекта
        if (getId() > -1) {
            setProjectInfo(getId());
        }

        // проверяем что savedInstanceState не равен null,
        // это условие выполнится только после поворота экрана, при первом запуске onCreate savedInstanceState равен null
        if (savedInstanceState != null) {
            long millis = savedInstanceState.getLong(DATE_TAG, 0);
            // перезаписываем значение внутри calendar тем что было сохранено ранее
            if (millis > 0) {
                calendar.setTimeInMillis(millis);
            }
        }
    }

    // возвращает на предыдующий экран при нажатии на стрелку назад
    @Override
    public boolean onSupportNavigateUp() {
        // завершает текущую активность
        finish();
        return true;
    }

    // достаёт из интента переданный ему id, в случае если мы создаём новый объект а не редактируем старый вернёт -1
    private int getId() {
        return getIntent().getIntExtra("ID_TAG", -1);
    }

    // достаёт проект из базы данных и устанавливает его текущие значения в поля экрана,
    // чтобы пользователь имел возможность редактировать ранее заданные значения, а не писал их заново
    private void setProjectInfo(int id) {
        // получает из бд проект по id
        ProjectEntity projectEntity = database.projectDao().getById(id);
        etTitle.setText(projectEntity.getTitle());
        tvDeadline.setText(projectEntity.getDeadline());
        etDescription.setText(projectEntity.getDescription());
    }

    // выполняет сохранение нового либо обновление существующего проекта в базе данных
    private void saveProject() {
        // проверка того что заголовок в поле ввода не пуст
        String title = etTitle.getText().toString();
        if (title.isEmpty()) {
            // нужно чтобы при нажатии на кнопку Сохранить и пустом поле заголовка корректно отобразилась ошибка под полем для ввода заголовка
            etTitle.requestFocus();
            etTitle.setError("Заголовок не может быть пустым");
            return;
        }

        // создаёт объект проекта для базы данных, заполняет его данными из полей
        ProjectEntity projectEntity = new ProjectEntity(
                title, tvDeadline.getText().toString(), etDescription.getText().toString()
        );

        // если id проекта был передан с предыдущего экрана, значит экран выполняет редактирование проекта
        if (getId() > -1) {
            // установим этот id ранее созданному projectEntity
            projectEntity.setId(getId());
            // и выполним метод update, который перезапишет в базе проект с этим id
            database.projectDao().update(projectEntity);
        } else {
            // сохранит ранее созданный projectEntity, сгенерировав автоматически для него новый уникальный id
            database.projectDao().insert(projectEntity);
        }
        // закрывает текущий экран
        finish();
    }

    @Override
    // метод вызывается перед переворотом экрана и любой другой сменой конфигурации
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // в специальный объект outState записываем занчение calendar в миллисекундах с ключем DATE_TAG
        outState.putLong(DATE_TAG, calendar.getTimeInMillis());
    }
}