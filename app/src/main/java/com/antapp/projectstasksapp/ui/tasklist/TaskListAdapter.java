package com.antapp.projectstasksapp.ui.tasklist;

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
 * Описывает каким образом из списка задач (ArrayList<Task> tasks) будут созданы конкретные виджеты(TaskListHolder)
 * и как они будут переиспользоваться при скролле(метод bind)
 *
 * Сам по себе адаптер должен отвечать только за отображение виджетов.
 * Но так как виджетам нужно задать поведение по нажатии на сам виджет и на его иконки,
 * то снаружи(из активити) необходимо передать в адаптер эти коллбэки, чтобы адаптер прокинул их в конкретные виджеты при их создании
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Task> tasks = new ArrayList<>();

    // обработчики передаются снаружи чтобы логика работы с навигацией и базой данных
    // не лежала в адаптере(адаптер должен отвечать только за отображение)
    // обработка нажатия на задачу
    private ItemTaskClickListener onTaskClickListener;
    // обработка нажатия на удаление задачи
    private ItemTaskClickListener onDeleteClickListener;
    // обработка нажатия на изменение задачи
    private ItemTaskClickListener onEditClickListener;

    public TaskListAdapter(@NonNull Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    // возвращает объект ViewHolder, который будет хранить данные по одному объекту Project.
    public TaskListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_task, parent, false);
        return new TaskListHolder(view);
    }

    @Override
    // выполняет привязку объекта ViewHolder к объекту Project по определенной позиции.
    public void onBindViewHolder(@NonNull TaskListHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    // возвращает количество объектов в списке
    public int getItemCount() {
        return tasks.size();
    }

    // метод заполнения списка данными
    public void setTasks(List<Task> newTasks) {
        // очистка текущего списка
        tasks.clear();
        // заполнение новыми задачами
        tasks.addAll(newTasks);
        // удаление пустых значений в конце списка, которые ArrayList создаёт автоматически, это нужно для оптимизации памяти
        tasks.trimToSize();
        // команда адаптеру перерисовать себя
        notifyDataSetChanged();
    }

    public void setOnTaskClickListener(ItemTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    public void setOnDeleteClickListener(ItemTaskClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnEditClickListener(ItemTaskClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    //Для хранения данных о виджетах в классе адаптера определен класс ViewHolder, который использует виджеты из item_task.xml.
    class TaskListHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDeadline, tvDescription;
        private final ImageView ivDelete, ivEdit, ivCompleted;

        TaskListHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivCompleted = itemView.findViewById(R.id.ivCompleted);
        }

        // заполняет виджеты значениями из конкретной задачи
        public void bind(Task task) {
            tvTitle.setText(task.getTitle());
            tvDeadline.setText(task.getDeadline());
            tvDescription.setText(task.getDescription());
            setChecked(task.isCompleted());

            // setOnClickListener задаёт обработчик нажатия на конкретный виджет
            // внутри него вызывается коллбэк, который приходит внутрь адаптера снаружи
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditClickListener != null) {
                        // открывает экран редактирования задачи (передаётся снаружи)
                        onEditClickListener.onItemClick(task);
                    }
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        // удаление задачи (передаётся снаружи)
                        onDeleteClickListener.onItemClick(task);
                    }
                }
            });

            // itemView - это виджет-контейнер задачи, в котором лежат все остальные описанные выше виджеты
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTaskClickListener != null) {
                        // меняет состояние флага на самом элементе списка
                        task.setCompleted(!task.isCompleted());
                        // показывает/скрывает галочку в соответствии с флагом
                        setChecked(task.isCompleted());
                        // меняет статус выполнения задачи (передаётся снаружи)
                        onTaskClickListener.onItemClick(task);
                    }
                }
            });
        }

        private void setChecked(boolean isChecked) {
            if (isChecked) {
                ivCompleted.setVisibility(View.VISIBLE);
            } else {
                ivCompleted.setVisibility(View.GONE);
            }
        }
    }

    // интерфейс описывает действие, которое необходимо выполнить при нажатии на виджет
    public interface ItemTaskClickListener {
        void onItemClick(Task task);
    }
}
