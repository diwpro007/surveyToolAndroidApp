package com.diwakar.surveytool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.diwakar.surveytool.Data.Answers;
import com.diwakar.surveytool.Data.Option;
import com.diwakar.surveytool.Data.Question;
import com.diwakar.surveytool.Data.Survey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    //Shared pref value
    public static final String SHARED_PREF_TOP_KEY = "MYPREF";
    public static final String ALL_ANS_OF_A_SURVEY = "ANS_SUR_";

    SharedPreferences savedAnswer;
    List<List<Answers>> totalAnswer = new ArrayList<>();
    Gson gson;
    String jsonedAns;


    public static final String TAG = MainActivity.class.getSimpleName();

    List<Survey> data = new ArrayList<>();

    public static final String ID_LEADINGPART = "com.diwakar.surveytool:id/";
    public static final String MORE_BUTTON = ID_LEADINGPART + "single_survey_textview_more";
    public static final String UPLOAD_BUTTON = ID_LEADINGPART + "single_survey_button_upload_data";
    public static final String TAKE_SURVEY_BUTTON = ID_LEADINGPART + "single_survey_button_take_survey";
    public static final String FIND_LOCATION_BUTTON = ID_LEADINGPART + "show_location";

    private List<Survey> data2 = new ArrayList<>();

    //TODO: replace placeholder data
    String surveyTitles[] = {"Survey1", "Survey2", "Survey3"};
    int offlineData[] = {12, 2, 50};

    RecyclerView surveyRecylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        savedAnswer = getSharedPreferences(SHARED_PREF_TOP_KEY, MODE_PRIVATE);
        final SharedPreferences.Editor editor = savedAnswer.edit();

//        if (savedAnswer.contains(ALL_ANS_OF_A_SURVEY + currentSurvey.getSurveyID())) {
////            Log.d(TAG, "Does exist");
//            String jsonedAns = savedAnswer.getString(ALL_ANS_OF_A_SURVEY + currentSurvey.getSurveyID(), "");
//            Type type = new TypeToken<List<List<Answers>>>() {
//            }.getType();
//            totalAnswer = gson.fromJson(jsonedAns, type);
//        }

        //setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);


//        try {
//            getData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //setup the recylerview



        surveyRecylerView = (RecyclerView) findViewById(R.id.activity_main_recylerview);
//        SurveyRecylerAdapter adapter = new SurveyRecylerAdapter(this, data);
//        updateUI();
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData() throws JSONException {

        String url = "http://192.168.0.101/SurveyProject/postSurveyQuestions.php";


        if (isNetworkAvailable()) {
            //toggle loading
            //TODO insert loading screen here

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //TODO: handle error
                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String jsonSurveyData = null;
                    if (response.isSuccessful()) {
                        jsonSurveyData = response.body().string();
                        try {
                            data2 = parseJSONData(jsonSurveyData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUI();
//                                    Toast.makeText(MainActivity.this, "apple", Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (JSONException e) {
                            Log.e(TAG, e + "");
                        }
                        Log.d(TAG, jsonSurveyData);
                    } else {
                        Log.d(TAG, "error");
                    }

                }
            });
        }

//        data = parseJSONData(json);
//        updateUI();
    }

    private List<Survey> parseJSONData(String jsonData) throws JSONException {
        //        Log.d(TAG, jsonData);
        JSONObject jsonObject = new JSONObject(jsonData);
        List<Survey> surveys = new ArrayList<>();

//        Log.d(TAG, jsonObject.getJSONArray("Survey").toString());

//        Log.d(TAG, jsonObject.getJSONArray("Survey").length() + "");

        JSONArray surveysArray = jsonObject.getJSONArray("Survey");

        for (int i = 0; i < surveysArray.length(); i++) {
            JSONObject surveyObject = surveysArray.getJSONObject(i);
            Survey tempSurvey = new Survey();
            tempSurvey.setSurveyName(surveyObject.getString("Name"));

//            TODO Put actual id instead of place holder
//            tempSurvey.setSurveyID(1);
            tempSurvey.setSurveyID(surveyObject.getInt("ID"));



            if (savedAnswer.contains(ALL_ANS_OF_A_SURVEY + tempSurvey.getSurveyID())) {
//            Log.d(TAG, "Does exist");
                jsonedAns = savedAnswer.getString(ALL_ANS_OF_A_SURVEY + tempSurvey.getSurveyID(), "");
                Log.d(TAG, jsonedAns);
                Type type = new TypeToken<List<List<Answers>>>() {
                }.getType();
                totalAnswer = gson.fromJson(jsonedAns, type);
                Log.d(TAG, jsonedAns);
            }




//            Log.d(TAG, tempSurvey.getSurveyName());
            List<Question> questions = new ArrayList<>();
            JSONArray questionsArray = surveyObject.getJSONArray("Questions");
            for (int j = 0; j < questionsArray.length(); j++) {
                JSONObject questionObject = questionsArray.getJSONObject(j);
                Question tempQuestion = new Question();
                tempQuestion.setQuestionName(questionObject.getString("Question_Name"));
                tempQuestion.setQuestionID(Integer.parseInt(questionObject.getString("Question_ID")));
                tempQuestion.setAnswerType(questionObject.getString("Answer_Type"));
                tempQuestion.setAnswerIPType(questionObject.getString("Answer_IP_Type"));

                if (questionObject.getString("Mandatory").toLowerCase().equals("y")) {
//                    Log.d(TAG, "true");
                    tempQuestion.setMandatory(true);
                } else {
//                    Log.d(TAG, "false");
                    tempQuestion.setMandatory(false);
                }
                if (tempQuestion.getAnswerIPType().toLowerCase().equals("m")) {
                    List<Option> options = new ArrayList<>();
                    JSONArray optionsArray = questionObject.getJSONArray("Options");
                    for (int k = 0; k < optionsArray.length(); k++) {
                        Option tempOption = new Option();
                        tempOption.setOptionName(optionsArray.getString(k));
                        options.add(tempOption);
                    }
                    Option optionFinalArray[] = new Option[options.size()];
                    for (int x = 0; x < options.size(); x++) {
                        optionFinalArray[x] = options.get(x);
                    }

                    tempQuestion.setOptions(optionFinalArray);
                } else {
                    //TODO When option empty
                }
                questions.add(tempQuestion);
            }

            Question questionFinalArray[] = new Question[questions.size()];
            for (int x = 0; x < questions.size(); x++) {
                questionFinalArray[x] = questions.get(x);
            }

            tempSurvey.setQuestions(questionFinalArray);
            surveys.add(tempSurvey);
        }

        return surveys;
    }


    private void updateUI() {
        SurveyRecylerAdapter adapter = new SurveyRecylerAdapter(this, data2);
        surveyRecylerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setButtonsClickedListener(new SurveyRecylerAdapter.ButtonsClickedListener() {
            @Override
            public void moreClicked(View view, int position) {
                Log.d(TAG, "" + position + view.getResources().getResourceName(view.getId()));

//                More button pressed
                if (view.getResources().getResourceName(view.getId()).equals(MORE_BUTTON)) {
                    Log.d(TAG, "MORE pressed");
                }

//                Upload button pressed
                if (view.getResources().getResourceName(view.getId()).equals(UPLOAD_BUTTON)) {
                    Log.d(TAG, "UPLOAD pressed");
//                    postData();
                    new Thread() {
                        @Override
                        public void run() {
                            //your code here
                            postAnswers();
                        }
                    }.start();


                }
                if (view.getResources().getResourceName(view.getId()).equals(FIND_LOCATION_BUTTON)) {
                    Log.d(TAG, "Show pressed");
                }

//                Take survey button pressed
                if (view.getResources().getResourceName(view.getId()).equals(TAKE_SURVEY_BUTTON)) {
                    Log.d(TAG, "TAKE pressed");

                    Intent intent = new Intent(MainActivity.this, TakeSurveyActivity.class);

//                    myIntent.putExtra("key", value); //Optional parameters
//                    CurrentActivity.this.startActivity(myIntent);
//                  TODO put some const key
                    intent.putExtra("KEY", data2.get(position));
                    startActivity(intent);
                }
            }
        });
        surveyRecylerView.setAdapter(adapter);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        } else {
            Toast.makeText(this, R.string.network_unavailable_message
                    , Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }

//    public void postData(){
////
////        if (isNetworkAvailable()) {
////            OkHttpClient postClient = new OkHttpClient();
////            RequestBody formBody = new FormBody.Builder()
////                    //TODO form POST key and json
////                    .add("response", jsonedAns)
////                    .build();
////
////            Request request = new Request.Builder()
////                    .url("http://192.168.0.100/SurveyProject/writeAnswerToDatabase.php")
////                    .post(formBody)
////                    .build();
////
//////            Call call = postClient.newCall(request);
////
//////            call.enqueue(new Callback() {
//////                @Override
//////                public void onFailure(Call call, IOException e) {
//////                    Log.d(TAG, "Failed " + e.toString());
//////
//////                }
//////
//////                @Override
//////                public void onResponse(Call call, Response response) throws IOException {
//////
//////                    String jsonSurveyData = null;
//////                    if (response.isSuccessful()) {
//////                        jsonSurveyData = response.body().string();
//////                        Log.d(TAG, "This is the response " + jsonSurveyData);
//////                    }
//////                }
//////            });
////
////            Response response = null;
////            try {
////                response = postClient.newCall(request).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            if (!response.isSuccessful()) try {
////                throw new IOException("Unexpected code " + response);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//
////            http://192.168.0.101/SurveyProject/
//        }
//
//
//    }

    protected void postAnswers()
    {

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = "";
        String parameters = "response=" +jsonedAns;

        try
        {
            url = new URL("http://192.168.0.101/SurveyProject/writeAnswerToDatabase.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
//            Toast.makeText(this,"Message from Server: \n"+ response, 0).show();
            Log.d(TAG,"Message from Server: \n"+ response);
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }

    private void deleteOfflineData(int surveyID) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_TOP_KEY, 0);
        preferences.edit().remove(ALL_ANS_OF_A_SURVEY + surveyID).commit();
    }


}


