package com.inspection.powerline.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;

import com.amaze.filemanager.database.ExplorerDatabase;
import com.amaze.filemanager.database.UtilitiesDatabase;
import com.amaze.filemanager.database.UtilsHandler;
import com.amaze.filemanager.filesystem.ssh.CustomSshJConfig;
import com.amaze.filemanager.ui.provider.UtilitiesProvider;
import com.amaze.filemanager.utils.LruBitmapCache;
import com.amaze.filemanager.utils.ScreenUtils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.inspection.powerline.utils.CrashHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.ref.WeakReference;




/**
 * 应用入口
 * Created by ccc on 2020/3/19..
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　...　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃   神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃　　　　　　　┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 */


public class BaseApplication extends GlideApplication {


    private static BaseApplication mBaseApplication = null;
    public static final String TAG = BaseApplication.class.getSimpleName();

    private UtilitiesProvider utilsProvider;
    private RequestQueue requestQueue;
    private com.android.volley.toolbox.ImageLoader imageLoader;
    private UtilsHandler utilsHandler;

    private static Handler applicationhandler = new Handler();
    private HandlerThread backgroundHandlerThread;
    private static Handler backgroundHandler;
    private WeakReference<Context> mainActivityContext;
    private static ScreenUtils screenUtils;



    private UtilitiesDatabase utilitiesDatabase;

    private ExplorerDatabase explorerDatabase;

    public UtilitiesProvider getUtilsProvider() {
        return utilsProvider;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(
                true); // selector in srcCompat isn't supported without this
        backgroundHandlerThread = new HandlerThread("app_background");
//        instance = this;

        CustomSshJConfig.init();
        explorerDatabase = ExplorerDatabase.initialize(this);
        utilitiesDatabase = UtilitiesDatabase.initialize(this);

        utilsProvider = new UtilitiesProvider(this);
        utilsHandler = new UtilsHandler(this, utilitiesDatabase);

        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());

        runInBackground(jcifs.Config::registerSmbURLHandler);

        // disabling file exposure method check for api n+
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        CrashHandler.getInstance().init(this);
    }



    public static BaseApplication getIntance() {
        if (null == mBaseApplication) {
            mBaseApplication = new BaseApplication();
        }
        return mBaseApplication;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        backgroundHandlerThread.quit();
    }

    /**
     * Post a runnable to handler. Use this in case we don't have any restriction to execute after
     * this runnable is executed, and {@link #runInBackground(Runnable)} in case we need to execute
     * something after execution in background
     */
    public static void runInBackground(Runnable runnable) {
        synchronized (backgroundHandler) {
            backgroundHandler.post(runnable);
        }
    }

    /**
     * A compact AsyncTask which runs which executes whatever is passed by callbacks. Supports any
     * class that extends an object as param array, and result too.
     */
    public static <Params, Result> void runInParallel(
            final BaseApplication.CustomAsyncCallbacks<Params, Result> customAsyncCallbacks) {

        synchronized (customAsyncCallbacks) {
            new AsyncTask<Params, Void, Result>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    customAsyncCallbacks.onPreExecute();
                }

                @Override
                protected Result doInBackground(Object... params) {
                    return customAsyncCallbacks.doInBackground();
                }

                @Override
                protected void onPostExecute(Result aVoid) {
                    super.onPostExecute(aVoid);
                    customAsyncCallbacks.onPostExecute(aVoid);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, customAsyncCallbacks.parameters);
        }
    }

    /** Interface providing callbacks utilized by {@link #runInBackground(Runnable)} */
    public abstract static class CustomAsyncCallbacks<Params, Result> {
        public final @Nullable
        Params[] parameters;

        public CustomAsyncCallbacks(@Nullable Params[] params) {
            parameters = params;
        }

        public abstract Result doInBackground();

        public void onPostExecute(Result result) {}

        public void onPreExecute() {}
    }

    /**
     * Shows a toast message
     *
     * @param context Any context belonging to this application
     * @param message The message to show
     */
    public static void toast(Context context, @StringRes int message) {
        // this is a static method so it is easier to call,
        // as the context checking and casting is done for you

        if (context == null) return;

        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }

        if (context instanceof Application) {
            final Context c = context;
            final @StringRes int m = message;

            getInstance().runInApplicationThread(() -> Toast.makeText(c, m, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * Shows a toast message
     *
     * @param context Any context belonging to this application
     * @param message The message to show
     */
    public static void toast(Context context, String message) {
        // this is a static method so it is easier to call,
        // as the context checking and casting is done for you

        if (context == null) return;

        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }

        if (context instanceof Application) {
            final Context c = context;
            final String m = message;

            getInstance().runInApplicationThread(() -> Toast.makeText(c, m, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * Run a runnable in the main application thread
     *
     * @param r Runnable to run
     */
    public void runInApplicationThread(Runnable r) {
        applicationhandler.post(r);
    }

    public static synchronized BaseApplication getInstance() {
        return mBaseApplication;
    }

    public com.android.volley.toolbox.ImageLoader getImageLoader() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        if (imageLoader == null) {
            this.imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, new LruBitmapCache());
        }
        return imageLoader;
    }

    public UtilsHandler getUtilsHandler() {
        return utilsHandler;
    }

    public void setMainActivityContext(@NonNull Activity activity) {
        mainActivityContext = new WeakReference<>(activity);
        screenUtils = new ScreenUtils(activity);
    }

    public ScreenUtils getScreenUtils() {
        return screenUtils;
    }

    @Nullable
    public Context getMainActivityContext() {
        return mainActivityContext.get();
    }

    public ExplorerDatabase getExplorerDatabase() {
        return explorerDatabase;
    }

    public UtilitiesDatabase getUtilitiesDatabase() {
        return utilitiesDatabase;
    }

}
