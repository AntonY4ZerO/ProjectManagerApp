package com.antapp.projectstasksapp.ui.projectslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antapp.projectstasksapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер, который используется в RecyclerView, должен наследоваться от абстрактного класса RecyclerView.Adapter.
 *
 * Описывает каким образом из списка проектов (ArrayList<Project> projects) будут созданы конкретные виджеты(ProjectListHolder)
 * и как они будут переиспользоваться при скролле(метод bind)
 *
 * Сам по себе адаптер должен отвечать только за отображение виджетов.
 * Но так как виджетам нужно задать поведение по нажатии на сам виджет и на его иконки,
 * то снаружи(из активити) необходимо передать в адаптер эти коллбэки, чтобы адаптер прокинул их в конкретные виджеты при их создании
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectListHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Project> projects = new ArrayList<>();

    // обработчики передаются снаружи чтобы логика работы с навигацией и базой данных
    // не лежала в адаптере(адаптер должен отвечать только за отображение)
    // обработка нажатия на проект
    private ItemProjectClickListener onProjectClickListener;
    // обработка нажатия на удаление проекта
    private ItemProjectClickListener onDeleteClickListener;
    // обработка нажатия на изменение проекта
    private ItemProjectClickListener onEditClickListener;

    public ProjectListAdapter(@NonNull Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    // возвращает объект ViewHolder, который будет хранить данные по одному объекту Project.
    public ProjectListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_project, parent, false);
        return new ProjectListHolder(view);
    }

    @Override
    // выполняет привязку объекта ViewHolder к объекту Project по определенной позиции.
    public void onBindViewHolder(@NonNull ProjectListHolder holder, int position) {
        holder.bind(projects.get(position));
    }

    @Override
    // возвращает количество объектов в списке
    public int getItemCount() {
        return projects.size();
    }

    // метод заполнения списка данными
    public void setProjects(List<Project> newProjects) {
        // очистка текущего списка
        projects.clear();
        // заполнение новыми проектами
        projects.addAll(newProjects);
        // удаление пустых значений в конце списка, которые ArrayList создаёт автоматически, это нужно для оптимизации памяти
        projects.trimToSize();
        // команда адаптеру перерисовать себя, автоматически он этого не делает
        notifyDataSetChanged();
    }

    public void setOnProjectClickListener(ItemProjectClickListener onProjectClickListener) {
        this.onProjectClickListener = onProjectClickListener;
    }

    public void setOnDeleteClickListener(ItemProjectClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnEditClickListener(ItemProjectClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    //Для хранения данных о виджетах в классе адаптера определен класс ViewHolder, который использует виджеты из item_project.xml.
    class ProjectListHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDeadline, tvTasksCount, tvDescription;
        private final ImageView ivDelete, ivEdit;

        ProjectListHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvTasksCount = itemView.findViewById(R.id.tvTasksCount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }

        // заполняет виджеты значениями из конкретного проекта
        public void bind(Project project) {
            tvTitle.setText(project.getTitle());
            tvDeadline.setText(project.getDeadline());
            tvTasksCount.setText(String.valueOf(project.getTasksCount()));
            tvDescription.setText(project.getDescription());

            // setOnClickListener задаёт обработчик нажатия на конкретный виджет
            // внутри него вызывается коллбэк, который приходит внутрь адаптера снаружи
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditClickListener != null) {
                        // открывает экран редактирования(передаётся снаружи)
                        onEditClickListener.onItemClick(project);
                    }
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        // удаление проекта(передаётся снаружи)
                        onDeleteClickListener.onItemClick(project);
                    }
                }
            });

            // itemView - это виджет-контейнер проекта, в котором лежат все остальные описанные выше виджеты
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProjectClickListener != null) {
                        // открывает экран просмотра задач(передаётся снаружи)
                        onProjectClickListener.onItemClick(project);
                    }
                }
            });
        }
    }

    // интерфейс описывает действие, которое необходимо выполнить при нажатии на виджет
    public interface ItemProjectClickListener {
        void onItemClick(Project project);
    }
}
