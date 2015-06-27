package tuhh.nme.mp.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import tuhh.nme.mp.R;


/**
 * A dialog that just warns the user.
 */
public class AlertDialogFragment extends DialogFragment
{
    /**
     * Instantiates a new AlertDialogFragment.
     */
    public AlertDialogFragment()
    {
        super();

        setArguments(new Bundle());
    }

    // Inherited documentation.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getArguments().containsKey(BUNDLE_KEY_MESSAGE))
        {
            builder.setMessage(getArguments().getString(BUNDLE_KEY_MESSAGE));
        }
        else if (getArguments().containsKey(BUNDLE_KEY_MESSAGE_RESOURCE_ID))
        {
            builder.setMessage(getArguments().getInt(BUNDLE_KEY_MESSAGE_RESOURCE_ID));
        }
        else
        {
            builder.setMessage("");
        }

        builder.setPositiveButton(R.string.dialogs_AlertDialogFragment_ok, null);

        return builder.create();
    }

    /**
     * Sets the message of this dialog.
     *
     * @param message The message string to display.
     */
    public void setMessage(String message)
    {
        getArguments().remove(BUNDLE_KEY_MESSAGE_RESOURCE_ID);
        getArguments().putString(BUNDLE_KEY_MESSAGE, message);
    }

    /**
     * Sets the message of this dialog.
     *
     * @param message_id The message resource ID.
     */
    public void setMessage(int message_id)
    {
        getArguments().remove(BUNDLE_KEY_MESSAGE);
        getArguments().putInt(BUNDLE_KEY_MESSAGE_RESOURCE_ID, message_id);
    }

    private final String BUNDLE_KEY_MESSAGE = "message";
    private final String BUNDLE_KEY_MESSAGE_RESOURCE_ID = "message_resource_id";
}
