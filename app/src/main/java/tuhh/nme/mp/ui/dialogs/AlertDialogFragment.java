package tuhh.nme.mp.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
        m_OnClickListener = null;
        m_OnCancelListener = null;
        m_OnDismissListener = null;
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

        builder.setPositiveButton(R.string.dialogs_AlertDialogFragment_ok, m_OnClickListener);

        return builder.create();
    }

    // Inherited documentation.
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if (m_OnCancelListener != null)
        {
            m_OnCancelListener.onCancel(dialog);
        }
    }

    // Inherited documentation.
    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if (m_OnDismissListener != null)
        {
            m_OnDismissListener.onDismiss(dialog);
        }
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

    /**
     * Returns the listener that listens for the button click.
     *
     * @return The listener. Returns null if no listener was registered.
     */
    public DialogInterface.OnClickListener getOnClickListener()
    {
        return m_OnClickListener;
    }

    /**
     * Sets the listener that listens for the button click.
     *
     * @param listener The listener. If you want to unregister a listener, you can pass null.
     */
    public void setOnClickListener(DialogInterface.OnClickListener listener)
    {
        m_OnClickListener = listener;
    }

    /**
     * Returns the listener that listens for cancellation.
     *
     * @return The listener. null if none registered.
     */
    public DialogInterface.OnCancelListener getOnCancelListener()
    {
        return m_OnCancelListener;
    }

    /**
     * Sets the listener that listens for cancellation.
     *
     * @param listener The listener. Pass null to unset the listener.
     */
    public void setOnCancelListener(DialogInterface.OnCancelListener listener)
    {
        m_OnCancelListener = listener;
    }

    /**
     * Returns the listener that listens for dialog dismiss.
     *
     * @return The listener. null if none registered.
     */
    public DialogInterface.OnDismissListener getOnDismissListener()
    {
        return m_OnDismissListener;
    }

    /**
     * Sets the listener that listens for dialog dismiss.
     *
     * @param listener The listener. Pass null to unset the listener.
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        m_OnDismissListener = listener;
    }

    /**
     * The listener that listens for the OK-button click.
     */
    private DialogInterface.OnClickListener m_OnClickListener;
    /**
     * The listener that listens for dialog cancellation.
     */
    private DialogInterface.OnCancelListener m_OnCancelListener;
    /**
     * The listener that listens for dialog dismiss.
     */
    private DialogInterface.OnDismissListener m_OnDismissListener;

    private final String BUNDLE_KEY_MESSAGE = "message";
    private final String BUNDLE_KEY_MESSAGE_RESOURCE_ID = "message_resource_id";
}
