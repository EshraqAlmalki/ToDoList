package com.example.todolist.ToDoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.DatePickerDialogFragment
import com.example.todolist.R
import com.example.todolist.dateBase.ToDo
import java.util.*

const val TODO_DATE_KYE="todo-date"
class ToDoFragment : Fragment() , DatePickerDialogFragment.datePickerCallBack{

    private lateinit var titleEditText:EditText
    private lateinit var decEditText: EditText
    private lateinit var dateBtn:Button
    private lateinit var doneCheckBox: CheckBox

    private var todo = ToDo()

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



        dateBtn.apply {
            text= todo.date.toString()
        }

        return view

    }

    override fun onStart() {
        super.onStart()

        dateBtn.setOnClickListener {
            val args= Bundle()
            args.putSerializable(TODO_DATE_KYE,todo.date)

            val datePicker=  DatePickerDialogFragment()
            datePicker.arguments=args
            datePicker.setTargetFragment(this,0)
            datePicker.show(this.parentFragmentManager,"date picker")

        }

        doneCheckBox.setOnCheckedChangeListener { _ , isChecked ->
            todo.done=isChecked
        }
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel.saveUpdate(todo)
    }

    override fun onDateSelected(date: Date) {
        todo.date=date
        dateBtn.text=date.toString()

    }


}