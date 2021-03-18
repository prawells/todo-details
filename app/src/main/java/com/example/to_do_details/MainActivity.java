package com.example.to_do_details;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private static final int IS_SUCCESS = 0;
    private String[] mTodos;
    private int mTodoIndex = 0;

    public static final String TAG = "TodoActivity";

    /* map or name, value pair to be returned in an intent */
    private static final String IS_TODO_COMPLETE = "com.example.isTodoComplete";

    private static final String TODO_INDEX = "com.example.todoIndex";


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TODO_INDEX, mTodoIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mTodoIndex = savedInstanceState.getInt(TODO_INDEX, 0);
        }

        Resources res = getResources();
        mTodos = res.getStringArray(R.array.todo);

        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        setTextViewComplete("");

        textViewTodo.setText(mTodos[mTodoIndex]);

        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        Button buttonPrevious = (Button) findViewById(R.id.buttonPrev);

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTodoIndex == 0){
                    mTodoIndex = mTodos.length - 1;
                    textViewTodo.setText(mTodos[mTodoIndex]);
                }
                else{
                    mTodoIndex = (mTodoIndex - 1) % mTodos.length;
                    textViewTodo.setText(mTodos[mTodoIndex]);
                }
                setTextViewComplete("");

            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTodoIndex = (mTodoIndex + 1) % mTodos.length;
                textViewTodo.setText(mTodos[mTodoIndex]);
                setTextViewComplete("");
            }
        });

        Button buttonTodoDetail = (Button) findViewById(R.id.buttonTodoDetail);
        buttonTodoDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = com.example.todo_detail_app.TodoDetail.newIntent(MainActivity.this, mTodoIndex);

                startActivityForResult(intent, IS_SUCCESS);

            }
        });

    }

    /*
        requestCode is the integer request code originally supplied to startActivityForResult
        resultCode is the integer result code returned by the child activity through its setResult()
        intent date attached with intent "extras"
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == IS_SUCCESS) {
            if (intent != null) {
                // data in intent from child activity
                boolean isTodoComplete = intent.getBooleanExtra(IS_TODO_COMPLETE, false);
                updateTodoComplete(isTodoComplete);
            } else {
                Toast.makeText(this, R.string.back_button_pressed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.request_code_mismatch,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateTodoComplete(boolean is_todo_complete) {

        final TextView textViewTodo;
        textViewTodo = (TextView) findViewById(R.id.textViewTodo);

        if (is_todo_complete) {
            textViewTodo.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.backgroundSuccess));
            textViewTodo.setTextColor(
                    ContextCompat.getColor(this, R.color.colorSuccess));

            setTextViewComplete("\u2713");
        }

    }

    private void setTextViewComplete( String message ){
        final TextView textViewComplete;
        textViewComplete = (TextView) findViewById(R.id.textViewComplete);

        textViewComplete.setText(message);
    }

}