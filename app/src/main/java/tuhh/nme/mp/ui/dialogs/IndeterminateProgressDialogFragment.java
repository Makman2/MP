package tuhh.nme.mp.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * A dialog that displays an indeterminate progress.
 */
public class IndeterminateProgressDialogFragment extends DialogFragment
{
    /**
     * Instantiates a new IndeterminateProgressDialogFragment.
     */
    public IndeterminateProgressDialogFragment()
    {
        super();

        setArguments(new Bundle());
        m_OnCancelListener = null;
        m_OnDismissListener = null;
    }

    // Inherited documentation.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        dialog.setIndeterminate(true);
        dialog.setCancelable(getArguments().getBoolean(BUNDLE_KEY_CANCELABLE, true));

        if (getArguments().containsKey(BUNDLE_KEY_MESSAGE))
        {
            dialog.setMessage(getArguments().getString(BUNDLE_KEY_MESSAGE));
        }
        else if (getArguments().containsKey(BUNDLE_KEY_MESSAGE_RESOURCE_ID))
        {
            dialog.setMessage(getActivity().getResources().getString(
                getArguments().getInt(BUNDLE_KEY_MESSAGE_RESOURCE_ID)));
        }
        else
        {
            dialog.setMessage("");
        }

        return dialog;
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
     * Returns whether this dialog is cancelable or not.
     *
     * @return Whether the user can cancel this dialog or not.
     */
    public boolean isCancelable()
    {
        return getArguments().getBoolean(BUNDLE_KEY_CANCELABLE, true);
    }

    /**
     * Sets whether this dialog is cancelable by user or not.
     *
     * @param cancelable Whether the user can cancel this dialog or not.
     */
    public void setCancelable(boolean cancelable)
    {
        getArguments().putBoolean(BUNDLE_KEY_CANCELABLE, cancelable);
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
     * @param listener The listener. Pass null to register no listener.
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
     * @param listener The listener. Pass null to register no listener.
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        m_OnDismissListener = listener;
    }

    /**
     * The listener that listens for dialog cancellation.
     */
    private DialogInterface.OnCancelListener m_OnCancelListener;
    /**
     * The listener that listens for dialog dismiss.
     */
    private DialogInterface.OnDismissListener m_OnDismissListener;

    private final String BUNDLE_KEY_CANCELABLE = "cancelable";
    private final String BUNDLE_KEY_MESSAGE = "message";
    private final String BUNDLE_KEY_MESSAGE_RESOURCE_ID = "message_resource_id";
}
