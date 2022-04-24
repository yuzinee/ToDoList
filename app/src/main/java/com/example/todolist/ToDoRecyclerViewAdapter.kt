package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.ToDoEntity

class ToDoRecyclerViewAdapter(private val todoList : ArrayList<ToDoEntity>,
            private val listener: OnItemLongClickListener)      // 인터페이스 구현체는 메인 액티비티에서 넘겨줌
    : RecyclerView.Adapter<ToDoRecyclerViewAdapter.MyViewHolder>(){

        inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
            val tv_importance = binding.tvImportance
            val tv_title = binding.tvTitle

            // 루트 레이아웃
            val root = binding.root
        }

    // 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTodoBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return MyViewHolder(binding)
    }

    // 받은 데이터를 뷰홀더 객체에 어떻게 넣어줄지 결정
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = todoList[position]

        when (todoData.importance) {
            1 -> {
                holder.tv_importance.setBackgroundResource(R.color.red)
            }
            2 -> {
                holder.tv_importance.setBackgroundResource(R.color.yellow)
            }
            3 -> {
                holder.tv_importance.setBackgroundResource(R.color.green)
            }
        }
        // 중요도에 따라 텍스트 변경
        holder.tv_importance.text = todoData.importance.toString()
        // 할 일의 제목 변경
        holder.tv_title.text = todoData.title
        // 루트 레이아웃(아이템뷰 전체 레이아웃 범위), 할 일이 길게 클릭되었을 때 실행
        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}