package com.kmangutov.mememaster.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kmangutov.mememaster.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kmangutov on 3/18/15.
 */
public class ListFragment extends Fragment {

    @InjectView(R.id.listViewThreads)
    ListView mListViewThreads;

    private String[] monthsArray = { "JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY",
            "AUG", "SEPT", "OCT", "NOV", "DEC" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);

        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, monthsArray);
        mListViewThreads.setAdapter(adapter);

        return view;
    }
}
