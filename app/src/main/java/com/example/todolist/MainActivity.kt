package com.example.todolist

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity

class MainActivity : AppCompatActivity(), OnItemLongClickListener {     // OnItemLongClickListener 구현

    private lateinit var binding: ActivityMainBinding       // 뷰바인딩 설정

    // 데이터베이스 관련 변수 선언
    private lateinit var db : AppDatabase
    private lateinit var todoDao : ToDoDao

    // 할 일 리스트 담아둘 변수 선언
    private lateinit var todoList: ArrayList<ToDoEntity>

    private lateinit var adapter: ToDoRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰바인딩 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인텐트를 사용해 다음 액티비티로 넘어감
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }

        // AppDatabase 객체를 생성해서 DB 작업을 할 수 있는 DAO를 가져옴
        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        getAllTodoList()
    }

    // 백그라운드 스레드에서 DB 관련 작업
    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAll())
            setRecyclerView()
        }.start()
    }

    // 리사이클러뷰 설정
    private fun setRecyclerView() {
        runOnUiThread {
            adapter = ToDoRecyclerViewAdapter(todoList, this)     // 어댑터 객체 할당
            binding.recyclerView.adapter = adapter      // 리사이클러뷰 어댑터로 위에서 만든 어댑터 설정
            binding.recyclerView.layoutManager = LinearLayoutManager(this)     // 레이아웃 매니저 설정
        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllTodoList()
    }

    override fun onLongClick(positon: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        // 사용자에게 의사를 물어볼 때 사용

        builder.setTitle("할 일 삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("네",
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTodo(positon)
                }
            }
        )
        builder.show()
    }

    private fun deleteTodo(position: Int) {
        Thread {
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            runOnUiThread{
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}