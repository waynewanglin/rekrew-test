package com.finaltest.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.github.underscore.lodash.U;
import com.finaltest.myapplication.databinding.FragmentMainBinding;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    TextView statusTextView;
    Button convertToJsonButton;
    Button convertToXmlButton;
    JSONObject jsonObject = null;
    String xmlString = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusTextView = getActivity().findViewById(R.id.textview_status);
        convertToJsonButton = getActivity().findViewById(R.id.button_convertToJson);
        convertToXmlButton = getActivity().findViewById(R.id.button_convertToXml);

        statusTextView.setText(R.string.initialStatus);
        convertToJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xmlString = readXML();
                XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
                jsonObject = xmlToJson.toJson();
                System.out.println(jsonObject);
                if (jsonObject != null) { // converted successfully
                    statusTextView.setText(R.string.convertedToJson);
                    convertToXmlButton.setVisibility(View.VISIBLE);
                }
            }
        });

        convertToXmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmlString = U.jsonToXml(jsonObject.toString());
                if (xmlString != null) { // converted successfully
                    statusTextView.setText(R.string.convertedToXml);
                }
            }
        });
    }

    public String readXML() {
        String line;
        StringBuilder total = new StringBuilder();

        try {
            InputStream is = getActivity().getAssets().open("ab.xml");
            BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            total = new StringBuilder();

            while ((line = r.readLine()) != null) {
                total.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}