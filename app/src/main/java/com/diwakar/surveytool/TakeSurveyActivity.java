package com.diwakar.surveytool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.diwakar.surveytool.Data.Answers;
import com.diwakar.surveytool.Data.Option;
import com.diwakar.surveytool.Data.Question;
import com.diwakar.surveytool.Data.Survey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TakeSurveyActivity extends AppCompatActivity {

    //Shared pref value
    public static final String SHARED_PREF_TOP_KEY = "MYPREF";
    public static final String ALL_ANS_OF_A_SURVEY = "ANS_SUR_";

    public static final String TAG = TakeSurveyActivity.class.getSimpleName();
    Survey currentSurvey;
    Question currentQuestions[];

    TextView questionText;

    int currentQuestionPositon = 0;
    int totalQuestion = 0;
    int currentSurveyID;

    EditText single_option;
    RadioGroup multiple_option;

    Button nextButton;
    Button finalButton;

    List<Answers> answerOfCurrentSurvey = new ArrayList<>();
    List<List<Answers>> totalAnswer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_survey);

        Intent i = getIntent();
        currentSurvey = (Survey) i.getSerializableExtra("KEY");
        currentSurveyID = currentSurvey.getSurveyID();

        SharedPreferences savedAnswer = getSharedPreferences(SHARED_PREF_TOP_KEY, MODE_PRIVATE);
        final SharedPreferences.Editor editor = savedAnswer.edit();



//        get the
        Gson gson = new Gson();
        if (savedAnswer.contains(ALL_ANS_OF_A_SURVEY + currentSurvey.getSurveyID())) {
//            Log.d(TAG, "Does exist");
            String jsonedAns = savedAnswer.getString(ALL_ANS_OF_A_SURVEY + currentSurvey.getSurveyID(), "");
            Type type = new TypeToken<List<List<Answers>>>() {
            }.getType();
            totalAnswer = gson.fromJson(jsonedAns, type);
        } else {

//            Log.d(TAG, "Does not exist");
//            does not exist
        }

        currentQuestions = currentSurvey.getQuestions();

        totalQuestion = currentSurvey.getQuestions().length;

//        Log.d(TAG,  + "");

        //setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.take_survey_activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(currentSurvey.getSurveyName());

        questionText = (TextView) findViewById(R.id.take_survey_activity_question_text);
        nextButton = (Button) findViewById(R.id.take_survey_activity_next_button);
        finalButton = (Button) findViewById(R.id.final_submission);

        //finally save to file
        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Total answers =" + answerOfCurrentSurvey.size());
                totalAnswer.add(answerOfCurrentSurvey);

                //TODO Now add to file
                Gson gson1 = new Gson();
                Log.d(TAG, "Before");
                String jsonFinal = gson1.toJson(totalAnswer);

                editor.putString(ALL_ANS_OF_A_SURVEY + currentSurvey.getSurveyID(), jsonFinal);
                editor.commit();

//                Log.d(TAG, "Total answers =" + answerOfCurrentSurvey.size());
//                Log.d(TAG, jsonFinal);
//                Gson gson1 = new Gson();
//                Log.d(TAG, "Before");
//                String jsonFinal1 = gson1.toJson(answerOfCurrentSurvey);
////                Log.d(TAG, "Total answers =" + answerOfCurrentSurvey.size());
//                Log.d(TAG, totalAnswer.toString());

                Intent intent = new Intent(TakeSurveyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        single_option = (EditText) findViewById(R.id.single_option_edit_text);
        multiple_option = (RadioGroup) findViewById(R.id.multiple_option_radio);

        setQuestion(currentQuestionPositon);

//        for(int y = 0; y < currentQuestions.length; y++){
//            Log.d(TAG, currentQuestions[y].getAnswerType() + "");
//        }

    }

    public void prevQuestion(View view) {
        if (currentQuestionPositon <= 0) {
            Toast.makeText(TakeSurveyActivity.this, "This is the first question", Toast.LENGTH_SHORT).show();
        } else {
            currentQuestionPositon--;
            setQuestion(currentQuestionPositon);
        }

    }

    public void nextQuestion(View view) {
            String type = currentQuestions[currentQuestionPositon].getAnswerIPType().toLowerCase();

            Log.d(TAG, "Type is " + type + " " + currentQuestionPositon);

            if (currentQuestions[currentQuestionPositon].getAnswerIPType().toLowerCase().equals("m")) {
                int selectedID = multiple_option.getCheckedRadioButtonId();

                if (selectedID == -1 && currentQuestions[currentQuestionPositon].isMandatory()) {
                    Toast
                            .makeText(TakeSurveyActivity.this
                                    , "This field is mandatory please fill"
                                    , Toast.LENGTH_SHORT)
                            .show();
                } else {
                    RadioButton selectedRadio = (RadioButton) findViewById(selectedID);
//                    Log.d("TAG", selectedRadio.getText().toString());

                    String answerValue = selectedRadio.getText().toString();

                    multiple_option.removeAllViews();

                    proceedQuestion(answerValue);
                    return;
                }
            }

            if (currentQuestions[currentQuestionPositon].getAnswerIPType().toLowerCase().equals("s")) {
//                Log.d(TAG, single_option.getText().toString().length() + "");
                if (single_option.getText().toString().length() == 0) {
                    Toast
                            .makeText(TakeSurveyActivity.this
                                    , "This field is mandatory please fill"
                                    , Toast.LENGTH_SHORT)
                            .show();
                    //a
                } else {
                    String answerValue = single_option.getText().toString();
//                    Log.d(TAG, answerValue);
                    single_option.setText("");

                    proceedQuestion(answerValue);
                }
            }

//            if (currentQuestions[currentQuestionPositon].getAnswerIPType().toLowerCase().equals("g")) {
//                //TODO: Geo type
//                Log.d(TAG, "Geo");
//                proceedQuestion("12,34");
//            }
    }


    void setQuestion(int position) {
        questionText.setText(currentSurvey.getQuestions()[position].getQuestionName());
        if (currentQuestions[position].getAnswerIPType().toLowerCase().equals("s")) {
            single_option.setVisibility(View.VISIBLE);
            multiple_option.setVisibility(View.GONE);
        }
        if (currentQuestions[position].getAnswerIPType().toLowerCase().equals("m")) {
            single_option.setVisibility(View.GONE);
            multiple_option.setVisibility(View.VISIBLE);

            setUpOptions();
        }
    }

    private void setUpOptions() {
        Option options[] = currentQuestions[currentQuestionPositon].getOptions();
        for (int i = 0; i < options.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options[i].getOptionName());
            multiple_option.addView(radioButton);
        }
    }

    private void proceedQuestion(String answerValue) {
        Log.d(TAG, answerValue);
        Question currentQuestion = currentQuestions[currentQuestionPositon];


        Answers tempAnswer = new Answers();

        tempAnswer.setQuestionID(currentQuestion.getQuestionID());
        tempAnswer.setAnswerValue(answerValue);
        tempAnswer.setIs_single(currentQuestion.getAnswerIPType());

//        answerOfCurrentSurvey.add(tempAnswer);

        if (currentQuestionPositon == (totalQuestion - 1)) {

            Log.d(TAG, "Last reached");
            answerOfCurrentSurvey.add(tempAnswer);
//            Log.d(TAG, "Total answers =" + answerOfCurrentSurvey.size());
//            currentQuestionPositon++;
//            if last question
            //show confirmation button
            finalButton.setVisibility(View.VISIBLE);
            //then add to database

        } else {
            answerOfCurrentSurvey.add(tempAnswer);
            currentQuestionPositon++;
            setQuestion(currentQuestionPositon);
        }

    }
}
