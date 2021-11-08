package com.example.todolist.ToDoListFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ToDoFragment.ToDoFragment
import com.example.todolist.dateBase.ToDo
import com.example.todolist.dateBase.ToDoDao
import com.example.todolist.dateBase.ToDoRepository
import java.util.*

const val KYE_ID = "myid"
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
        R.id.new_todo ->{
        val todo = ToDo()
            toDoListViewModel.addTodo(todo)
            val args =Bundle()
            args.putSerializable(KYE_ID,todo.id)

            val fragment = ToDoFragment()
            fragment.arguments =args
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()
            }
            Log.d("Eshraq","hi from here")
            true
        }else->super.onContextItemSelected(item)
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

         val sportBtn: Button = view.findViewById(R.id.sport_cat)
         val workBtn: Button = view.findViewById(R.id.work_cat)
         val homeBtn: Button = view.findViewById(R.id.home_cat)


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
            delBtn.setOnClickListener{
                toDoListViewModel.delTodo(todo)
            }


        }

        fun bind(todo: ToDo) {
            this.todo = todo
            titleTextView.text = todo.title
            dateTextView.text = todo.date.toString()




        }

        override fun onClick(v: View?) {
            if (v==itemView){
                val args = Bundle()
                args.putSerializable(KYE_ID,todo.id)

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