package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todolist.databinding.ActivityAddTodoBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding : ActivityAddTodoBinding
    lateinit var db : AppDatabase       // ToDoDao 불러오기
    lateinit var todoDao : ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!        // ToDoDao 불러오기
        todoDao = db.getTodoDao()

        binding.btnCompletion.setOnClickListener {
            insertTodo()
        }
    }

    // 할 일 추가 함수

    private fun insertTodo() {
        val todoTitle = binding.edtTitle.text.toString()        // 할 일 제목
        var todoImportance = binding.radioGroup.checkedRadioButtonId        // 할 일 중요도

        when(todoImportance) {
            R.id.btn_high -> {
                todoImportance = 1
            }
            R.id.btn_middle -> {
                todoImportance = 2
            }
            R.id.btn_low -> {
                todoImportance = 3
            }
            else -> {
                todoImportance = -1
            }
        }

        if(todoImportance == -1 || todoTitle.isBlank()) {
            Toast.makeText(this,"모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show()
        } else {
            // 백그라운드 스레드 실행행
            Thread {
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImportance))
                runOnUiThread {
                    Toast.makeText(this,"추가되었습니다",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }
}