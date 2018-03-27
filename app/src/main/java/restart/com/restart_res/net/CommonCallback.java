package restart.com.restart_res.net;

import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import restart.com.restart_res.utils.GsonUtil;

/**
 * Created by Administrator on 2018/3/27.
 */

public abstract class CommonCallback<T> extends StringCallback {
    Type mType;

    public CommonCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T response);

    @Override
    public void onResponse(String response, int id) {
        try {
            Log.e("zhy", "response :" + response);

            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");
            if (resultCode == 1) {
                onSuccess((T) GsonUtil.getGson().fromJson(resp.getString("data"), mType));
            } else {
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }
}
