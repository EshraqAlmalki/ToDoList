package com.example.todolist.ToDoListFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat.format
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ToDoFragment.ToDoFragment
import com.example.todolist.dateBase.ToDo
import org.w3c.dom.Text
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val KYE_ADD = "add task"
//const val KYE_UPDATE = "update task"
const val KYE_ID = "myid"
const val FORMAT_KYE ="dd/MM/yyyy"
class ToDoListFragment :Fragment(){


    private lateinit var todoRecyclerView: RecyclerView

    val toDoListViewModel by lazy { ViewModelProvider(this).get(ToDoListViewModel::class.java) }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_list_menu,menu)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId){
        R.id.new_todo ->{ val fragment = ToDoFragment()
            activity?.let {
                     it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()}
            true }else->super.onContextItemSelected(item)
    }
    }







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list,container,false)

        todoRecyclerView=view.findViewById(R.id.todo_recycler_view)
        val linearLayoutManager= LinearLayoutManager(context)
        todoRecyclerView.layoutManager=linearLayoutManager




    return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toDoListViewModel.LiveDataTodo.observe(
            viewLifecycleOwner, Observer {
                updateIU(it)

            }
        )


    }

    private fun updateIU(todo:List<ToDo>){
        val todoAdapter =TodoAdapter(todo)
        todoRecyclerView.adapter=todoAdapter

    }



    private inner class TodoHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener {
        private lateinit var todo: ToDo
        private val titleTextView: TextView = itemView.findViewById(R.id.task_title_item)
        private val dateTextView: TextView = itemView.findViewById(R.id.task_date_item)


        val delBtn: Button = view.findViewById(R.id.del_btn)


        init {
            itemView.setOnClickListener(this)
            delBtn.setOnClickListener(this)


        }

        @SuppressLint("SimpleDateFormat")
        fun bind(todo: ToDo) {
            this.todo = todo
                titleTextView.text = todo.title

            if (todo.duoDate != null ) {

                dateTextView.text = todo.duoDate.toString()
                dateTextView.text= DateFormat.getDateInstance().format(todo.duoDate)

            }else{

                dateTextView.text=""
            }

            var currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
             currentDate = dateFormat.parse(dateFormat.format(currentDate))



            if (todo.duoDate != null) {
                if (currentDate.after(todo.duoDate)) {

                  dateTextView.setTextColor(Color.RED)


                }
            }




            when(todo.category){
                "work" -> {
                    titleTextView.setTextColor(resources.getColor(toDoListViewModel.brown))
                }
                "Home" -> {
                    titleTextView.setTextColor(resources.getColor(toDoListViewModel.blue))
                }
                "sport" -> {
                    titleTextView.setTextColor(resources.getColor(toDoListViewModel.yellow))
                }
            }

            when(todo.done){

                true -> titleTextView.setTextColor(Color.GRAY)
            }



        }


        override fun onClick(v: View?) {
            if (v==itemView){
                val args = Bundle()
                args.putSerializable(KYE_ID,todo.id)
                args.putString(KYE_ADD,"add task")
 //               args.putString(KYE_UPDATE,"update")

                val fragment=ToDoFragment()
                fragment.arguments=args
                activity?.let {

                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

            if(v==delBtn){
                val builder = context?.let { it -> AlertDialog.Builder(it)

                }
                builder?.let {
                    it.setMessage("Are you sure you want to remove this task?")
                    it.setCancelable(false)
                    it.setPositiveButton("Yes, I'm"){dialog, id ->
                        toDoListViewModel.delTodo(todo)}
                    it.setNegativeButton("No"){
                        dialog,id-> dialog.dismiss()
                    }

                    val alert = builder.create()
                    alert.show()

                }

            }
        }
    }


    private inner class TodoAdapter(val todo :List<ToDo>):
            RecyclerView.Adapter<TodoHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {

            val view = layoutInflater.inflate(R.layout.list_item_todo,parent,false)

            return TodoHolder(view)
        }

        override fun onBindViewHolder(holder: TodoHolder, position: Int) {

            val toDo = todo[position]
            holder.bind(toDo)
        }

        override fun getItemCount(): Int = todo.size
        }



    }