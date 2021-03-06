package org.vosie.wikicards.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
  protected static NetworkUtils instance;

  public static NetworkUtils get() {
    synchronized (NetworkUtils.class) {
      if (null == instance) {
        instance = new NetworkUtils();
      }
    }

    return instance;
  }

  /**
   * The constructor of this class is only used by protected scope.
   */
  protected NetworkUtils() {

  }

  public boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager =
            (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  /**
   * add network state change listener.
   *
   * @param ctx
   * @param connectedRunnable
   *          called when network is connected.
   * @param lostRunnable
   *          called when network is lost.
   * @return the receiver object.
   */
  public BroadcastReceiver notifyInternetState(Context ctx,
          final Runnable connectedRunnable, final Runnable lostRunnable) {

    BroadcastReceiver receiver = new BroadcastReceiver() {

      @Override
      public void onReceive(Context context, Intent intent) {
        if (!intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
          connectedRunnable.run();
        } else {
          lostRunnable.run();
        }
      }
    };
    ctx.registerReceiver(receiver,
            new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    return receiver;
  }

  /**
   * remove the listener
   *
   * @param ctx
   * @param receiver
   */
  public void removeInternetStateNotifier(Context ctx,
          BroadcastReceiver receiver) {
    if (null == receiver) {
      return;
    }
    ctx.unregisterReceiver(receiver);
  }
}
