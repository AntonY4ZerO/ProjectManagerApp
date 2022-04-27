package com.antapp.projectstasksapp.ui.tasklist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.antapp.projectstasksapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.antapp.projectstasksapp.database.AppDatabase;
import com.antapp.projectstasksapp.database.DatabaseHelper;
import com.antapp.projectstasksapp.database.TaskEntity;
import com.antapp.projectstasksapp.ui.taskedit.TaskEditActivity;

public class TaskListActivity extends AppCompatActivity {

    private TextView tvNoTasks;
    private TaskListAdapter tasksListAdapter;
    private FloatingActionButton fabAddTask;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        database = DatabaseHelper.getDatabase(this);

        tvNoTasks = findViewById(R.id.tvNoTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                intent.putExtra("PROJECT_ID_TAG", getProjectId());
                startActivity(intent);
            }
        });

        tasksListAdapter = new TaskListAdapter(this);
        RecyclerView rvTasksList = findViewById(R.id.rvTasksList);
        rvTasksList.setAdapter(tasksListAdapter);

        tasksListAdapter.setOnEditClickListener(new TaskListAdapter.ItemTaskClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                intent.putExtra("ID_TAG", task.getId());
                intent.putExtra("PROJECT_ID_TAG", getProjectId());
                startActivity(intent);
            }
        });

        tasksListAdapter.setOnDeleteClickListener(new TaskListAdapter.ItemTaskClickListener() {
            @Override
            public void onItemClick(Task task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
                builder.setTitle(R.string.attention);  // заголовок
                builder.setMessage(R.string.delete_task_question); // сообщение
                // кнопка подтверждения
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // удалить задачу из базы
                        database.taskDao().deleteById(task.getId());
                        // обновим список отображаемый на экране
                        showTasks();
                        dialog.cancel();
                    }
                });
                // кнопка отмены
                builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                // возможность закрыть диалог нажатием вне его границ
                builder.setCancelable(true);
                builder.create();
                builder.show();
            }
        });

        // нажатием на задачу меняем её статус выполения
        tasksListAdapter.setOnTaskClickListener(new TaskListAdapter.ItemTaskClickListener() {
            @Override
            public void onItemClick(Task task) {
                // получаем задачу из базы по её id
                TaskEntity taskEntity = database.taskDao().getById(task.getId());
                // меняем её статус выполения на противоположный
                taskEntity.setCompleted(!taskEntity.isCompleted());
                // записываем обновленную taskEntity
                database.taskDao().update(taskEntity);
                // обновляем список задач, чтобы изменения отобразились
                showTasks();
            }
        });

        setTitle(getIntent().getStringExtra("TITLE_TAG"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showTasks();
    }

    // достаёт из интента projectId - идентификатор проекта, к которому относятся данные задачи
    private int getProjectId() {
        return getIntent().getIntExtra("PROJECT_ID_TAG", -1);
    }

    // метод запрашивает задачи из базы, преобразует их в данные необходимые для отображения и отображает их с помощью адаптера
    private void showTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (getProjectId() < 0) {
            finish();
        }

        // получение всех задач проекта из базы
        List<TaskEntity> taskEntities = database.taskDao().getByProjectId(getProjectId());

        for (TaskEntity taskEntity : taskEntities) {
            // формируем объект с данными для отображения
            Task task = new Task(
                    taskEntity.getId(),
                    taskEntity.getTitle(),
                    taskEntity.getDeadline(),
                    taskEntity.getDescription(),
                    taskEntity.isCompleted()
            );
            // добавляем его в результирующий список
            tasks.add(task);
        }

        // передаём список адаптеру, который их отобразит
        tasksListAdapter.setTasks(tasks);

        // если список проектов пуст показывает сообщение об этом пользователю
        if (tasks.isEmpty()) {
            tvNoTasks.setVisibility(View.VISIBLE);
        } else {
            tvNoTasks.setVisibility(View.GONE);
        }
    }
}