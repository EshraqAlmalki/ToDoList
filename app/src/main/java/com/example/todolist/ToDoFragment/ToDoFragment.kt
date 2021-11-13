package com.example.todolist.ToDoFragment

import android.graphics.Color
import android.graphics.Color.BLUE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.DatePickerDialogFragment
import com.example.todolist.R
import com.example.todolist.ToDoListFragment.FORMAT_KYE
import com.example.todolist.ToDoListFragment.KYE_ADD
import com.example.todolist.ToDoListFragment.KYE_ID
import com.example.todolist.ToDoListFragment.ToDoListFragment
import com.example.todolist.dateBase.ToDo
import java.text.DateFormat
import java.util.*

const val TODO_DATE_KYE="todo-date"
const val FORMAT_KYE ="dd/MM/yyyy"
class ToDoFragment : Fragment() , DatePickerDialogFragment.datePickerCallBack{

    private lateinit var titleEditText:EditText
    private lateinit var decEditText: EditText
    private lateinit var dateBtn:Button
    private lateinit var doneCheckBox: CheckBox
    private lateinit var saveBtn:Button
    private lateinit var updateBtn:Button
    private lateinit var sportcat:RadioButton
    private lateinit var workcat:RadioButton
    private lateinit var homecat:RadioButton
    private lateinit var radioGroup: RadioGroup

    private lateinit var todo:ToDo

    private val fragmentViewModel by lazy { ViewModelProvider(this).get(ToDoFragmentViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(
            R.layout.fragment_to_do,
            container,
            false)

        titleEditText=view.findViewById(R.id.to_do_title)
        decEditText=view.findViewById(R.id.to_do_des)
        dateBtn=view.findViewById(R.id.data_btn)
        doneCheckBox=view.findViewById(R.id.done_check)
        saveBtn=view.findViewById(R.id.save_btn)
        updateBtn=view.findViewById(R.id.update_btn)
        sportcat=view.findViewById(R.id.radio_btn_sport)
        workcat=view.findViewById(R.id.radio_btn_work)
        homecat=view.findViewById(R.id.radio_btn_home)
        radioGroup=view.findViewById(R.id.radio_group)



        dateBtn.apply {
            text = todo.duoDate.toString()
        }

        return view

    }

    override fun onStart() {
        super.onStart()

        dateBtn.setOnClickListener {
            val args= Bundle()
            args.putSerializable(TODO_DATE_KYE,todo.duoDate)

            val datePicker=  DatePickerDialogFragment()
            datePicker.arguments=args
            datePicker.setTargetFragment(this,0)
            datePicker.show(this.parentFragmentManager,"date picker")

        }

        saveBtn.setOnClickListener {
            todo.title = titleEditText.text.toString()
            todo.description = decEditText.text.toString()

            if (todo.title==""){
                Toast.makeText(context,"Try to add Task!", Toast.LENGTH_SHORT).show()
            }else{
                fragmentViewModel.addTodo(todo)
                val fragment = ToDoListFragment()
                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        updateBtn.setOnClickListener {
            todo.title = titleEditText.text.toString()
            todo.description = decEditText.text.toString()

            if (todo.title== ""){
                Toast.makeText(context,"You change nothing.", Toast.LENGTH_SHORT).show()
            }else{
                fragmentViewModel.saveUpdate(todo)
                val fragment = ToDoListFragment()
                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }




      radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when(radioGroup.checkedRadioButtonId){
                R.id.radio_btn_home -> {
                    todo.category = "Home"

                }
                R.id.radio_btn_work -> {
                    todo.category = "work"
                }
                R.id.radio_btn_sport -> {
                    todo.category = "sport"
                }
            }
      }





        val textWatcher = object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    todo.title = s.toString()



            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        val textWatcher1 = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                todo.description = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        titleEditText.addTextChangedListener(textWatcher)
        decEditText.addTextChangedListener(textWatcher1)

        doneCheckBox.setOnCheckedChangeListener { _ , isChecked ->
            todo.done=isChecked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            todo= ToDo()

            val todoId = arguments?.getSerializable(KYE_ID) as? UUID
        if (todoId != null) {
            fragmentViewModel.loadToDo(todoId)
       }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sender = arguments?.getString(KYE_ADD)


        if (todo.duoDate != null ) {
            dateBtn.text = todo.duoDate.toString()
        }else{
            dateBtn.text = "add due date"
        }


        if ( sender == "add task"){
            saveBtn.isEnabled = false
        }else  {
            updateBtn.isEnabled = false
        }


        fragmentViewModel.todoLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    todo = it
                    titleEditText.setText(it.title)
                    dateBtn.text=it.duoDate.toString()
                    decEditText.setText(it.description)
                }

                if(todo.duoDate==null){
                    dateBtn.text="add due date"
                }else {
                    dateBtn.text = DateFormat.getDateInstance().format(todo.duoDate)
                }

                if (todo.done==true){
                    doneCheckBox.isChecked=true
                }

                if(todo.category=="sport"){
                    sportcat.isChecked=true
                }

                if (todo.category=="Home"){
                    homecat.isChecked=true
                }

                if (todo.category=="work"){
                    workcat.isChecked=true
                }

            }

        )

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDateSelected(date: Date) {
        todo.duoDate=date

        dateBtn.text= DateFormat.getDateInstance().format(todo.duoDate)
    }
}