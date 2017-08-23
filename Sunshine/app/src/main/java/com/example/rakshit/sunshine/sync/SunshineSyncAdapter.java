package com.example.rakshit.sunshine.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.rakshit.sunshine.DataLoader;
import com.example.rakshit.sunshine.R;
import com.example.rakshit.sunshine.Utility;

public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter
{
    public final String LOG_TAG = SunshineSyncAdapter.class.getSimpleName();
    private static final int SYNC_INTERVAL = 60*180;
    private static final int FLEX_TIME = SYNC_INTERVAL/10;

    public SunshineSyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.d(LOG_TAG, "onPerformSync Called.");
        Utility utility = new Utility(getContext());
        new DataLoader(getContext(), utility.getURL()).loadInBackground();
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context)
    {
        Log.d("syncService", "onPerformSync Called.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context)
    {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (accountManager.getPassword(newAccount) == null)
        {
            //Add the account and account type, no password or user data
            // If successful, return the Account object, otherwise report an error.
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
            {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime)
    {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        // we can enable inexact timers in our periodic sync
        SyncRequest request = new SyncRequest.Builder().
                syncPeriodic(syncInterval, flexTime).
                setSyncAdapter(account, authority).
                setExtras(new Bundle()).build();
        ContentResolver.requestSync(request);
    }

    private static void onAccountCreated(Account newAccount, Context context)
    {
        //Since we've created an account
        SunshineSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, FLEX_TIME);

        //Without calling setSyncAutomatically, our periodic sync will not be enabled.
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        //Finally, let's do a sync to get things started
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context)
    {
        getSyncAccount(context);
    }
}