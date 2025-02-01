package com.example.assignment4;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    private static final String ARG_RESULT_TEXT = "result_text";
    private static final String ARG_RESULT_COLOR = "result_color";

    public static ResultFragment newInstance(String resultText, String resultColor) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESULT_TEXT, resultText);
        args.putString(ARG_RESULT_COLOR, resultColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        TextView resultTextView = view.findViewById(R.id.resultTextView);

        registerForContextMenu(resultTextView);

        if (getArguments() != null) {
            String resultText = getArguments().getString(ARG_RESULT_TEXT);
            String resultColor = getArguments().getString(ARG_RESULT_COLOR);
            resultTextView.setText(resultText);
            resultTextView.setTextColor(Color.parseColor(resultColor));
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Choose an action");
        menu.add(0, v.getId(), 0, "Copy Text");
        menu.add(0, v.getId(), 1, "Clear Text");
        menu.add(0, v.getId(), 2, "Change Color");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (getView() == null) return super.onContextItemSelected(item);

        TextView resultTextView = getView().findViewById(R.id.resultTextView);

        switch (item.getTitle().toString()) {
            case "Copy Text":
                String textToCopy = resultTextView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;

            case "Clear Text":
                resultTextView.setText("");
                Toast.makeText(getContext(), "Text cleared", Toast.LENGTH_SHORT).show();
                return true;

            case "Change Color":
                resultTextView.setTextColor(Color.RED);
                Toast.makeText(getContext(), "Text color changed to red", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
