package com.diwakar.surveytool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diwakar.surveytool.Data.Survey;

import java.util.List;

/**
 * Created by Diwakar on 4/23/2016.
 */
public class SurveyRecylerAdapter extends RecyclerView.Adapter<SurveyRecylerAdapter.SurveyViewHolder> {

    private ButtonsClickedListener buttonsClickedListener;

    private LayoutInflater inflater;
    private List<Survey> data;

    private boolean currentClicked = false;
    private int clickedIndex;

    SurveyRecylerAdapter(Context context, List<Survey> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setButtonsClickedListener(ButtonsClickedListener buttonsClickedListener) {
        this.buttonsClickedListener = buttonsClickedListener;
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_survey_layout, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SurveyViewHolder holder, int position) {
        holder.surveyTitle.setText(data.get(position).getSurveyName());
        holder.offlineData.setText("Offline data =" + data.get(position).getTotalOfflineData() + "");

//        TODO: handle the button presses for upload data and take survey here
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SurveyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {

        TextView surveyTitle;
        TextView offlineData;
        TextView onlineData;
        Button uploadDataButton;
        Button takeSurveyButton;
        Button showLocationButton;
        TextView viewMoreInfo;

        LinearLayout buttonGroup;

        public SurveyViewHolder(View itemView) {
            super(itemView);

            surveyTitle = (TextView) itemView.findViewById(R.id.single_survey_textview_survey_name);
            offlineData = (TextView) itemView.findViewById(R.id.single_survey_textview_offline_data);

            uploadDataButton = (Button) itemView.findViewById(R.id.single_survey_button_upload_data);
            takeSurveyButton = (Button) itemView.findViewById(R.id.single_survey_button_take_survey);
            showLocationButton = (Button) itemView.findViewById(R.id.show_location);

            viewMoreInfo = (TextView) itemView.findViewById(R.id.single_survey_textview_more);

            buttonGroup = (LinearLayout) itemView
                    .findViewById(R.id.single_survey_linear_layout_button_groups);

            itemView.setOnClickListener(this);
            itemView.setOnFocusChangeListener(this);


            viewMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsClickedListener.moreClicked(v, getAdapterPosition());
                }
            });

            uploadDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsClickedListener.moreClicked(v, getAdapterPosition());
                }
            });

            takeSurveyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsClickedListener.moreClicked(v, getAdapterPosition());
                }
            });

            showLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonsClickedListener.moreClicked(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            buttonGroup.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                Log.d("Adapter", "focus lost on " + getAdapterPosition());
            }
        }
    }

    public interface ButtonsClickedListener {
        public void moreClicked(View view, int position);
    }

}
