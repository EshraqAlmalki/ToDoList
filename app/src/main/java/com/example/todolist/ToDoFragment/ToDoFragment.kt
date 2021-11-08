package com.example.todolist.ToDoFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.todolist.ToDoListFragment.KYE_ID
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

        val textWatcher = object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                todo.title=s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        titleEditText.addTextChangedListener(textWatcher)
        decEditText.addTextChangedListener(textWatcher)

        doneCheckBox.setOnCheckedChangeListener { _ , isChecked ->
            todo.done=isChecked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo= ToDo()

        val todoId = arguments?.getSerializable(KYE_ID) as UUID
        fragmentViewModel.loadToDo(todoId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel.todoLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    todo = it
                    titleEditText.setText(it.title)
                    dateBtn.text=it.date.toString()
                    decEditText.setText(it.description)

                }
            }
        )






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