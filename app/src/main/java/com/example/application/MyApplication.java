package com.example.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.news.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.xutils.common.util.LogUtil;

import cn.bmob.push.BmobPush;
import cn.bmob.sms.*;
import cn.bmob.v3.*;

/**
 * Created by Administrator on 2016/6/5.
 */
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    DisplayImageOptions options = new DisplayImageOptions.Builder()
      .showStubImage(R.drawable.new_logo2)          // 设置图片下载期间显示的图片
      .showImageForEmptyUri(R.drawable.new_logo2)  // 设置图片Uri为空或是错误的时候显示的图片
      .showImageOnFail(R.drawable.new_logo2)       // 设置图片加载或解码过程中发生错误显示的图片
      .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
      .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
      .cacheOnDisk(true)
//       .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
//      .displayer(new FadeInBitmapDisplayer(100))  // 设置图片渐显的时间
      .displayer(new SimpleBitmapDisplayer())  // 设置图片正常显示
      .imageScaleType(ImageScaleType.EXACTLY)//图像将完全按比例缩小的目标大小
      .bitmapConfig(Bitmap.Config.RGB_565)//默认是ARGB_8888，使用RGB_565会比使用ARGB_8888少消耗2倍的内
      .build();
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
      .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
      .threadPoolSize(6) // default  线程池内加载的数量
      .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级

      .memoryCache(new WeakMemoryCache())
      .defaultDisplayImageOptions(options)// .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
      .build(); //开始构建

//        //初始化ImageLoader
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    ImageLoader.getInstance().init(config);


    // 初始化BmobSDK
    //  Bmob.initialize(this, "e388ec3ec9417e1c8aa7c1279beda897");
    // 使用推送服务时的初始化操作
    //   BmobInstallation.getCurrentInstallation(this).save();
    //初始化Bugly
    Bugly.init(getApplicationContext(), "900034562", false);
    Beta.defaultBannerId = R.drawable.news_logo;
    Beta.largeIconId = R.drawable.news_logo;
    super.onCreate();
  }
}
