package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;

import tuhh.nme.mp.R;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.ui.controls.adapters.HistoryFileAdapter;


/**
 * Presents all available MPDF files stored.
 */
public class HistoryBrowserFragment extends Fragment
{
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

        // TODO: Add item click listener for ListView that starts the preview Activity.

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
