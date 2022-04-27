package com.antapp.projectstasksapp.ui.projectslist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.antapp.projectstasksapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.antapp.projectstasksapp.database.AppDatabase;
import com.antapp.projectstasksapp.database.DatabaseHelper;
import com.antapp.projectstasksapp.database.ProjectEntity;
import com.antapp.projectstasksapp.database.TaskEntity;
import com.antapp.projectstasksapp.ui.projectedit.ProjectsEditActivity;
import com.antapp.projectstasksapp.ui.tasklist.TaskListActivity;

public class ProjectsListActivity extends AppCompatActivity {

    private TextView tvNoProjects;
    private FloatingActionButton fabAddProject;
    private ProjectListAdapter projectListAdapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_list);
        database = DatabaseHelper.getDatabase(this);

        tvNoProjects = findViewById(R.id.tvNoProjects);
        fabAddProject = findViewById(R.id.fabAddProject);
        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // переход на экран редактора проекта
                startActivity(new Intent(ProjectsListActivity.this, ProjectsEditActivity.class));
            }
        });

        projectListAdapter = new ProjectListAdapter(this);
        RecyclerView rvProjectsList = findViewById(R.id.rvProjectsList);
        rvProjectsList.setAdapter(projectListAdapter);

        projectListAdapter.setOnProjectClickListener(new ProjectListAdapter.ItemProjectClickListener() {
            @Override
            public void onItemClick(Project project) {
                // ProjectsListActivity.this значит что мы переходим со списка проектов
                // TaskListActivity.class значит что мы переходим на список задач
                Intent intent = new Intent(ProjectsListActivity.this, TaskListActivity.class);
                // передаётся id проекта чтобы на экране деталей проекта загрузить из базы список задач по этому id
                intent.putExtra("PROJECT_ID_TAG", project.getId());
                // передаётся название проекта чтобы на экране деталей проекта отобразить его в верхней панели
                intent.putExtra("TITLE_TAG", project.getTitle());
                startActivity(intent);
            }
        });

        projectListAdapter.setOnEditClickListener(new ProjectListAdapter.ItemProjectClickListener() {
            @Override
            public void onItemClick(Project project) {
                Intent intent = new Intent(ProjectsListActivity.this, ProjectsEditActivity.class);
                intent.putExtra("ID_TAG", project.getId());
                startActivity(intent);
            }
        });

        projectListAdapter.setOnDeleteClickListener(new ProjectListAdapter.ItemProjectClickListener() {
            @Override
            public void onItemClick(Project project) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectsListActivity.this);
                builder.setTitle(R.string.attention);  // заголовок
                builder.setMessage(R.string.delete_project_question); // сообщение
                // кнопка подтверждения
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // удалить сам проект из базы
                        database.projectDao().deleteById(project.getId());
                        // удалить задачи этого проекта из базы
                        database.taskDao().deleteByProjectId(project.getId());
                        // обновим список отображаемый на экране
                        showProjects();
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
    }

    // метод onStart вызывается после метода onCreate при первичном показе экрана,
    // а при возврате на этот экран вызывается только onStart, onCreate не вызывается
    @Override
    protected void onStart() {
        super.onStart();
        // после возврата на этот экран с экрана редактирования проекта отображает новые актуальные данные из базы
        showProjects();
    }

    // метод запрашивает проекты из базы, преобразует их в данные необходимые для отображения и отображает их с помощью адаптера
    private void showProjects() {
        ArrayList<Project> projects = new ArrayList<>();

        // получение всех проектов из базы
        List<ProjectEntity> projectEntities = database.projectDao().getAll();

        for (ProjectEntity projectEntity : projectEntities) {
            // для каждого проекта запрашиваем его задачи
            List<TaskEntity> taskEntities = database.taskDao().getByProjectId(projectEntity.getId());
            // формируем объект с данными для отображения
            Project project = new Project(
                    projectEntity.getId(),
                    projectEntity.getTitle(),
                    projectEntity.getDeadline(),
                    projectEntity.getDescription(),
                    taskEntities.size()
            );
            // добавляем его в результирующий список
            projects.add(project);
        }

        // передаём список адаптеру, который их отобразит
        projectListAdapter.setProjects(projects);

        // если список проектов пуст показывает сообщение об этом пользователю
        if (projects.isEmpty()) {
            tvNoProjects.setVisibility(View.VISIBLE);
        } else {
            tvNoProjects.setVisibility(View.GONE);
        }
    }
}