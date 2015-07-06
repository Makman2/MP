package tuhh.nme.mp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import tuhh.nme.mp.IntentExtra;
import tuhh.nme.mp.R;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.ui.controls.adapters.HistoryFileAdapter;


/**
 * Presents all available MPDF files stored.
 */
public class HistoryBrowserFragment extends Fragment
{
    private class OnFileClickListener implements AdapterView.OnItemClickListener
    {
        // Inherited documentation.
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent start_activity_intent = new Intent(getActivity(), HistoryPreviewActivity.class);
            start_activity_intent.putExtra(
                IntentExtra.history_preview_activity_timestamp,
                (HighPrecisionDate)parent.getItemAtPosition(position));
            startActivity(start_activity_intent);
        }
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.history_browser_fragment, container, false);

        ListView list = (ListView)view.findViewById(R.id.HistoryBrowserFragment_data_list);
        View empty_view = inflater.inflate(
            R.layout.history_browser_fragment_data_list_empty, list, false);
        getActivity().addContentView(empty_view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        list.setEmptyView(empty_view);
        m_ListAdapter = new HistoryFileAdapter(
            getActivity(),
            new ArrayList<>(DataFrameFileManager.getStoredFiles()));
        list.setAdapter(m_ListAdapter);
        list.setOnItemClickListener(new OnFileClickListener());

        return view;
    }

    /**
     * Refreshes the file display list.
     */
    public void updateList()
    {
        m_ListAdapter.clear();
        m_ListAdapter.addAll(DataFrameFileManager.getStoredFiles());
        m_ListAdapter.notifyDataSetChanged();
    }

    private HistoryFileAdapter m_ListAdapter;
}
