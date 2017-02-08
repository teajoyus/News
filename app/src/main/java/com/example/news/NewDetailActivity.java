package com.example.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.CommentAdapter;
import com.example.entry.CommentItem;
import com.example.entry.NewDetail;
import com.example.entry.NewItem;
import com.example.iface.INewsMessage;
import com.example.iface.OnResultListener;
import com.example.model.NewsMessageModel;
import com.example.myview.dialog.SpotsDialog;
import com.example.runtime.RunTime;
import com.example.util.NewsUtils;
import com.example.myview.NewAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林妙鸿 on 2016/5/27.
 */

public class NewDetailActivity extends Activity implements OnResultListener {
    private Handler handler;
    private String html;
    private TextView tv, tv_comment_num_detail, tv_bar_detail;
    private ImageView back, back_comment, collect, share_detail;
    private EditText et_bottom, et_comment;
    private LinearLayout ll_bottom,ll_most_detail;
    private RelativeLayout rl_bottom, rl_comment_lv_detail, rl_back_detail;
    private ScrollView sl_conent;
    private ImageButton iv_comment_bottom, iv_share_bottom_detail;
    private ListView lv_comment;
    private CommentAdapter commentAdapter;
    private List<CommentItem> commentItemList;
    private View head, bottom;
    private NewDetail newDetail;
    private INewsMessage newsMessage;
    private Button bt_confirm_detail;
    private  TextView relate1,relate2,relate3;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_detail);
        initDatas();
        initView();
        initHandler();
        initListener();
    }

    private void initListener() {
        rl_back_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDetailActivity.this.finish();
            }
        });
        tv_bar_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl_conent.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        et_bottom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ll_bottom.setVisibility(View.GONE);
                    rl_bottom.setVisibility(View.VISIBLE);
                    et_comment.setFocusable(true);
                    et_comment.setFocusableInTouchMode(true);
                    et_comment.requestFocus();
                    InputMethodManager inputManager =
                            (InputMethodManager) et_comment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(et_comment, 0);
                }
            }
        });
        back_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_bottom.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
                ll_bottom.setFocusable(true);
                ll_bottom.setFocusableInTouchMode(true);
                ll_bottom.requestFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) ll_bottom.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(ll_bottom.getWindowToken(), 0);
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = RunTime.getRunTimeUser().getId();
                if (id == null) {
                    NewAlertDialog.alertLogin(NewDetailActivity.this);
                    return;
                }
                if (newDetail.isLove()) {
                    newsMessage.userClickLove(newDetail.getNewId(), id, false);
                    Toast.makeText(NewDetailActivity.this, "已经取消喜欢该新闻", Toast.LENGTH_SHORT).show();
                    collect.setBackgroundResource(R.drawable.love_unselected);
                    newDetail.setLove(false);
                } else {
                    newsMessage.userClickLove(newDetail.getNewId(), id, true);
                    collect.setBackgroundResource(R.drawable.love_selected);
                    newDetail.setLove(true);
                    Toast.makeText(NewDetailActivity.this, "已经标记喜欢该新闻", Toast.LENGTH_SHORT).show();
                }
            }
        });
        share_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, newDetail.getNewUrl());
                startActivity(Intent.createChooser(intent, "转发该条新闻到"));
            }
        });
        iv_share_bottom_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("1234", "url:" + newDetail.getNewUrl());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, newDetail.getNewUrl());
                startActivity(Intent.createChooser(intent, "转发该条新闻到"));
            }
        });
        iv_comment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //滑倒评论
                        sl_conent.fullScroll(ScrollView.FOCUS_DOWN);

                    }
                });
            }
        });
        bt_confirm_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_comment.getText().toString().trim();
                String userId = RunTime.getRunTimeUser().getId();
                if (userId == null) {
                    NewAlertDialog.showAlert(NewDetailActivity.this, "没有权限", "对不起，您还没有登录，不能对评论点赞。您要登录吗", "登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NewDetailActivity.this, LoginActivity.class);
                            NewDetailActivity.this.startActivity(intent);
                            NewDetailActivity.this.finish();
                        }
                    });
                    return;
                }
//                else if(newDetail.isComment()==true){
//                    NewAlertDialog. showAlert(NewDetailActivity.this, "错误", "对不起，您已经评论过了", "确定", null);
//                    return;
//
//                }return
                if (content.length() < 2) {
                    Toast.makeText(NewDetailActivity.this, "输入的评论太少了", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    INewsMessage inews = new NewsMessageModel();
                    inews.setListener(new OnResultListener() {
                        @Override
                        public void onStartDoing() {

                        }

                        @Override
                        public void onSuccess(Object o) {
                            newDetail.setComment(true);//把新闻设置为已经评论过
                            ((CommentItem) o).setNum("0");
                            commentItemList.add((CommentItem) o);
                            commentAdapter.notifyDataSetChanged();
                            Toast.makeText(NewDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            //添加评论后动态添加listview的高度
                            setListViewHeightBasedOnChildren(lv_comment);
                            //滑倒评论
                            sl_conent.fullScroll(ScrollView.FOCUS_DOWN);
                        }

                        @Override
                        public void onFaild(Object o) {

                        }
                    });
                    //满足评论的条件
                    inews.newComment(userId, newDetail.getNewId(), content);

                    //将布局影藏掉退回新闻页面
                    {
                        ll_bottom.setVisibility(View.VISIBLE);
                        rl_bottom.setVisibility(View.GONE);
                        ll_bottom.setFocusable(true);
                        ll_bottom.setFocusableInTouchMode(true);
                        ll_bottom.requestFocus();
                        InputMethodManager inputManager =
                                (InputMethodManager) ll_bottom.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(ll_bottom.getWindowToken(), 0);

                    }

                }
            }
        });
        relate1.setOnClickListener(new RelateOnClickListener());
        relate2.setOnClickListener(new RelateOnClickListener());
        relate3.setOnClickListener(new RelateOnClickListener());

    }

    /**
     * 初始化新闻内容的请求、以及新闻评论列表
     */
    private void initDatas() {
       /*
       CommentItem需要有新闻id，userid
        */
        newsMessage = new NewsMessageModel();
        newsMessage.setListener(this);
        newDetail = new NewDetail();
        String newId = getIntent().getExtras().getString("newId");
        newDetail.setNewId(newId);
        commentItemList = new ArrayList<CommentItem>();
//        CommentItem item = new CommentItem();
//        item.setContent("这个新闻太好了太好了 这个是评论 评论 用来测试的行了吧");
//        item.setTime("5月28号 12:30:30");
//        item.setName("用户15767****97");
//        item.setZan(false);
//        for (int i=0;i<15;i++){
//            item.setNum(i+"");
//            commentItemList.add(item);
//        }
//        commentAdapter = new CommentAdapter(NewDetailActivity.this,commentItemList);
        // 网上找的html数据
        html = "<h1 id=\"main_title\">综艺后期神人:B站大神、《围城》铁粉出没</h1><div class=\"content\" data-sudaclick=\"blk_content\" id=\"artibody\">\n" +
                "<div class=\"img_wrapper\">\n" +
                "<img alt=\"大话综艺\" src=\"http://n.sinaimg.cn/ent/transform/20160527/0hr3-fxsqxxs7764424.jpg\"/>\n" +
                "<span class=\"img_descr\">大话综艺</span>\n" +
                "</div><p>　　Ran/文</p>\n" +
                "<p>　　随着综艺节目近两年呈现井喷式的增长，制作团队对节目的水准要求越来越高，后期已经成为鉴别一档节目成功与否的关键环节。在后期上如何剪辑编排、如何通过花字、旁白等方式巧触观众High点就显得更为重要了。目前越来越多综艺节目浑身散发着一股逗比的气质。比如《极限挑战》蜜汁花字、时尚最时尚的BGM，《非凡搭档》极尽吐槽之能事的《非凡旁白君》等等。不少观众已经对后期大大们燃起高涨兴趣，成堆的鸡腿等着往他们的盘子里加。在此，小浪再次发挥人脉优势，找到三档后期特色极具鲜明的节目幕后大神，包括《极限挑战》、《非凡搭档》、《了不起的挑战》。本期“大话综艺”，大家来听他们爆料那些脑洞大开、画风清奇的“有毒”后期到底是如何发想、制作出来的！</p>\n" +
                "<p>　　【<strong>一、#极限挑战●极限细节#】</strong>　　</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"张艺兴与后期团队的合影\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/etLf-fxsrkwk3101376.jpg\"/><span class=\"img_descr\">张艺兴与后期团队的合影</span></div>\n" +
                "<p>　　“给跪！已被张艺兴Rap神曲洗脑，鸡条后期是混B站鬼畜区的吧！”“天了噜！刚刚鸡条的BGM居然放了我本命的新歌！后期也是本命粉吗！”“纳尼？我听到了‘世初’主题曲，后期藏着资深腐女！”</p>\n" +
                "<p>　　眼下《极限挑战》第二季持续热播中，因节目后期各种鬼畜剪辑、花字吐槽、时髦BGM十分契合网友心理，故引发不少人激动表白“‘鸡条’后期太业良”，猜测后期团队里一定藏着“自己人”。看到这些评论，《极挑》总导演任静鬼马一笑：“哈哈，网友老说后期有自己人，我默默想说……我们团队确实有你们‘自己人’啊！”</p>\n" +
                "<p>　　<strong>从群众中来反造福群众——</strong></p>\n" +
                "<p><strong>　　“鸡条”大大都是“老司机”！</strong></p>\n" +
                "<p>　　任静承认，其实在做第一季节目时，自己并没有那么了解二次元的世界，也仅仅是听说过“B站”这类的存在。但架不住自己后期团队里90后年轻姑娘小伙儿大多是混“二次元”来的，加之当时有同事建议可以把节目上传到一些互动性强的网站上，以便听取更多网友反馈，导演这才算是打开了一扇新世界的大门，找到了一个更深入了解观众所爱所需的途径。</p>\n" +
                "<p>　　其实“鸡条”后期很多经典梗都是来源于众网友的智慧呢！甚至就连极具代表性的“极限三精”、“极限三傻”名号，都是从颜王孙红雷<a class=\"wt_article_link\" href=\"http://weibo.com/1650450024?zw=ent\" onmouseover=\"WeiboCard.show(1650450024, 'ent' , this)\" target=\"_blank\">[微博]</a>的微博留言和B站上来的！任静透露：“记得当初第一季节目大概播到第二第三集的时候，有网友在孙红雷的微博下留言提到了‘极限三傻’这么一个名字，每当他们出糗，B站上也会有人这么叫他们，我们的花字同学看到了觉得很有趣，就说在节目中把红雷、艺兴、王迅三人正式命名为‘三傻’吧，相应的，‘三精’也有了。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"三傻&amp;三精\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/-aYH-fxsrkwk3101488.jpg\"/><span class=\"img_descr\">三傻&amp;三精</span></div>\n" +
                "<p>　　又比如，在第二季节目中极具好评的张艺兴“魔性Rap”也是受到了网友启发。从一季开始，就有不少喜爱“鸡条”的网友会把节目中有趣的梗重新整合，做成许多鬼畜向的小视频。而后期大大们注意到大家的兴趣点后，决定索性做一些官方鬼畜来玩一玩，艺兴牌“洗脑Rap”应运而生！</p>\n" +
                "<p>　　当然啦！除了常常潜入群众内部汲取新鲜灵感，我们贴心的后期大大也是很懂得回报观众、反发福利的！</p>\n" +
                "<p>　　譬如MC们在“三亚站”客串舞团群演时，张艺兴有一段美人鱼似的地板舞表演，为了凸显“小绵羊”的好舞技，后期团队将其之前在个人生日会上表演的一段酷炫舞蹈也串剪了进来，并且另外配上了艺兴的一首自作曲。就是这么几十秒的串剪，后期剪辑师也是花了很大心思的：“因为艺兴舞蹈很好，有很多之前演出的相关视频可选择，剪辑师专门找来《I’M LAY》这首歌和他在生日会上的表演。因为他们懂得这一段对很多艺兴的粉丝来说更有特殊意义。在素材的选择上我们真的会考虑很多。”</p>\n" +
                "<p>　　另外。不少观众发现这季节目增加了更多艺兴、小猪自己的歌做BGM（《一个人》、《约定》/《爱转角》等等），也是因为后期大大看到粉丝们说想多听到他们的歌出现在节目中，所以才默默地发派福利哦！</p>\n" +
                "<p>　　<strong>资深二次元、B站大神、某团真爱粉</strong></p>\n" +
                "<p><strong>　　后期团队确实有“自己人”！</strong></p>\n" +
                "<p>　　就如任静反复强调的，“鸡条”后期团队有许多脑洞很大的90后年轻人——传说中的“大神”是也！就说“花字”小组吧，组员们天天热衷刷各种二次元网站，有什么新词、段子从哪儿衍生出来，这帮人都是最早get到的！严敏调侃：“和他们对话，他们讲的很多词儿我都搞不清楚是什么鬼（笑）！”之前在制作某期节目时，严敏本来兴致勃勃地想用“绅士”一词形容一下六位MC，结果被花字君们及时拦下：“领导！我们（网络）世界里只有日本变态大叔才被叫做‘绅士’呀！”任静叹了一口气：“艾玛！幸亏被提醒了，不然我这不就露怯了么！”</p>\n" +
                "<p>　　记得之前“友谊的小船”梗刚出来，还未流传太广的时候，“鸡条”花字君们已然热情地跟导演科普起了这个说法，且麻利儿地转手就把它运用到了第一集黄磊<a class=\"wt_article_link\" href=\"http://weibo.com/1831216671?zw=ent\" onmouseover=\"WeiboCard.show(1831216671, 'ent' , this)\" target=\"_blank\">[微博]</a>、黄渤<a class=\"wt_article_link\" href=\"http://weibo.com/1263498570?zw=ent\" onmouseover=\"WeiboCard.show(1263498570, 'ent' , this)\" target=\"_blank\">[微博]</a>、罗志祥<a class=\"wt_article_link\" href=\"http://weibo.com/1784537661?zw=ent\" onmouseover=\"WeiboCard.show(1784537661, 'ent' , this)\" target=\"_blank\">[微博]</a>在老码头相遇的画面上。等到节目正式播出，也正是这句话大范围流行开来之时，不少网友就此找到了共鸣。</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"友谊的小船\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/6uvu-fxsqxyc1522593.jpg\"/><span class=\"img_descr\">友谊的小船</span></div>\n" +
                "<p>　　不止热衷网络文化，花字君们的精神世界可是十分多元的！再举简单一例，那个被王迅本人激动认证的“大松鼠”定位，其实是花字君在看完《冰河世界》电影后，从片中那只永远找松果的松鼠那里捕获到的灵感。嗯，我们的花字君们也都是电影咖哦！</p>\n" +
                "<p>　　至于听到各种极具“深意”的BGM后，纷纷猜测后期团队藏着“自己人”的网友，小浪在此也正式回复你们：“‘鸡条’里确实有你们的人！”导演爆料啦：“我们有两三个腐女剪辑师，在后期配乐配字这方面有‘奇才’！有一个还属于突然就请个假说我要去韩国看某某某的演唱会！然后带了灯牌就走的那种……”</p>\n" +
                "<p>　　<strong>哥玩太High！“逼疯”后期团</strong></p>\n" +
                "<p>　　在我们“大话综艺”第一期中，小浪已经跟大家爆了不少关于六位M（老）C（狐狸）如何“欺负”现场导演组的猛料，这一次幕后人员继续爆料：“第一季刚开始时六位MC还不算太熟，有些束手束脚。到了第二季，他们越玩越开，你没听到艺兴和黄磊在节目中的名言吗？‘结果不重要，出戏才重要！’他们经常不顾规则地在现场玩出许多新剧情来。这种情况下，后期重新架构就特别重要了。”</p>\n" +
                "<p>　　大家还记得六位MC在台湾站分别执导“偶像剧”那集吗？当时节目播出后，无论是那六段最终整合出来的完整文艺片，还是穿插在正片中的漫画版“教艺兴谈恋爱”都备受好评。其实这些都是被六位“老狐狸”逼出来的！</p>\n" +
                "<p>　　任静透露：“当时给了六位哥哥权力，让他们自导偶像剧，所以现场录的时候他们真的是乱七八糟地瞎玩呀！各种穿帮！我们在后期整理素材时，发现存在很多剧情上的漏洞。所以就和后期商量要不要做个漫画版小片+旁白的形式穿插其中，把残漏的部分串起来？又因平时六位哥哥真的像一家人一样，会在各方面不断教艺兴成长。所以我们想说这个漫画的线索就是让其他五个人以前辈的姿态去引导小艺兴，让他懂得什么是爱。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"漫画脚本对照图\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/mmv9-fxsqxxs7690746.jpg\"/><span class=\"img_descr\">漫画脚本对照图</span></div>\n" +
                "<p>　　补救方案定好后，漫画具体要怎么画呢？那就轮到我们后期手绘师大大们出场了！“很多观众都知道我们节目有很多典型场景，我们后期先把其中一些经典场景搜出来，当做原版参照，以三幅画面为一组，做成分镜头版本，再让漫画师根据分镜头手绘。仅一幅画就要画上六个小时！”最后，任静还不忘神吐槽：“你看看，就因为现场各种不受控，都把我们‘逼’出来一个专门画插画的手绘团队了！”</p>\n" +
                "<p>　　大大们还爆料：“六个人会给我们提很细致的意见。之前三亚那集，本来我们剪到正片里有一段是小猪和黄渤在车内讲双簧，结果小猪自己跑来要求我们换成他在车里放屁的场景！我们说你是偶像你确定吗？他很认真地说你们相信我，我做综艺时间很长，这样更有戏。于是我们很‘无语’地又换了一遍素材（笑）。”</p>\n" +
                "<p>　<strong>　极致的细节你确定不是处女座？</strong></p>\n" +
                "<p><strong>　　为了两秒镜头抠抠抠、为画面干净PPP</strong></p>\n" +
                "<p>　　而被“逼疯”的后期团还做过什么惊人之举呢？篇幅原因，小浪在此就快速罗列一些啦，大家感受、感受就好。</p>\n" +
                "<p>　　*第四期“百元的幸福”。片头出来时，有一堆金币从片名几个字后面冒出来，虽然只出现了两三秒时间，但仔细观察便会发现，每个金币居然都有细节！正面是六个哥哥的头像，背面是《极挑》“6”的LOGO，按特效师的说法：“我们《极限挑战》的金币怎么能随意嘛，要做就做极限金币！而按照任静的说法：“为了两秒钟的画面他们竟然一个一个去抠图，大概是疯了吧（笑）！”</p>\n" +
                "<p><span style=\"line-height: 22.96px;\">　　*“花样男子”那期。王迅饰演的“累”在天台上有一场倒立戏，因为“大松鼠”并不会倒立，所以拍摄当下是由艺兴、孙红雷等人扶着拍摄完成的。但为啥在之后的正片中却呈现出了“大松鼠”独立倒立的镜头？后期微笑回答：“因为我们把‘多余’的人P没了！”</span></p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"倒立效果是P出来的\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/TeXF-fxsqxxs7691853.jpg\"/><span class=\"img_descr\">倒立效果是P出来的</span></div>\n" +
                "<p>　　“有很多观众看到我们最终呈现的画面很唯美，都以为我们是重拍了一遍，其实不是。做后期的时候我们只能P，把穿帮的人事物统统P掉！不止倒立戏，后来孙红雷被外星人吸走那场戏，我们也有把乱入画面的小猪、王迅和颜王的狐狸尾巴都P掉了哦~”</p>\n" +
                "<p>　　眼下，《极挑》第二季最后一期的录制业已结束，六十多人的后期团队也进入到了这季最后的制作冲刺阶段，每做一期都要花费三到四周的时间连轴转，十分辛苦。</p>\n" +
                "<p>　　【二、<strong>#非凡搭档●非凡旁白#】</strong></p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"《非凡搭档》录制现场\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/FSen-fxsqxyc1526917.jpg\"/><span class=\"img_descr\">《非凡搭档》录制现场</span></div>\n" +
                "<p>　　除了如每档综艺节目一般用花字做笑果外，《非凡搭档》一个很大的亮点在于它的“旁白君”。在这档节目中，每当嘉宾遇到各种好笑或棘手的状况，总会有一个男声以极其冷静的声调犀利评点现状，“一本正经地腹黑”，“一本正经地打脸嘉宾”，因为极具反差萌感，又吐槽的十分到位，节目播出后，“旁白君”迅速走红了。而这一切，其实都在总制片人易骅的掌握、预料之中……</p>\n" +
                "<p>　　<strong>冷艳的旁白君他来自何方？</strong></p>\n" +
                "<p><strong>　　不是V脸社长 灵感来自电视剧《围城》</strong></p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"拍摄现场\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160527/Vsfa-fxsqxxu4509414.jpg\"/><span class=\"img_descr\">拍摄现场</span></div>\n" +
                "<p>　　提到《非凡搭档》，观众最感兴趣的便是江湖人称“乃爷”的腹黑“旁白君”。制片人易骅首先辟谣：“很多网友以为我们的旁白君是‘V脸社长’，不是的，其实就连‘乃爷’这个名字也只是个临时代号。Ta只是一个声音，可以是任何人来说这些旁白，重点在于我们编剧团队的内容设计。”</p>\n" +
                "<p>　　后期团队又是怎么想到“旁白君”这个梗的呢？易骅透露：“第一次剪辑节目时，我们并没有旁白君的设计，但发现整期节目的节奏太快了，嘉宾们很多可爱的面向无法充分突出体现，我就提议需要个旁白提示下看点，让看点更清晰，试验了好几种，发现这种带点新闻联播冷淡腔的说书人感觉最好，不站在任何一方立场上，以围观者的调调去强调当下看点，同时又承上启下为观众提示下节笑点。至于这种论调，其实我们是参照了以前陈道明演的电视剧《围城》，《围城》里的旁白也有点这种围观者的调调，我印象一直很深刻的。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"黎明被吐槽\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/LRWh-fxsqxyc1528856.jpg\"/><span class=\"img_descr\">黎明被吐槽</span></div>\n" +
                "<p>　　声音确定后，接下来就是为“旁白君”设计讲话内容，非凡后期团队坚持的一个重点就是：“语言一定要时髦，不能土！必须槽点十足，符合互联网年轻人喜欢的风格！”于是编剧们便开始脑洞大开，一本正经地设计起了吐槽金句。譬如吐槽踢球的李小鹏——“自摆乌龙？你已经达到了中国足球队的水平了。”譬如吐槽内急的“厕所之神”黎明<a class=\"wt_article_link\" href=\"http://weibo.com/2201594645?zw=ent\" onmouseover=\"WeiboCard.show(2201594645, 'ent' , this)\" target=\"_blank\">[微博]</a>——“和（黎明）这一别可能就是沧海桑田。”句句“打脸”嘉宾，笑果足。不过虽然走的是犀利吐槽风，但后期团队也强调：“我们有个原则，不能真的在人格上语言上对明星造成伤害。就算调侃他的缺点也要打情骂俏式的，比如陈楚河<a class=\"wt_article_link\" href=\"http://weibo.com/1874608417?zw=ent\" onmouseover=\"WeiboCard.show(1874608417, 'ent' , this)\" target=\"_blank\">[微博]</a>，他说自己腿太长玩不好舞狮环节，我们旁白君就要用娇嗔的口吻说他的腿真应该和他那张嘴一起剁了，这就很好玩。但如果这话说的很严肃，说你就吹牛吧，能力不行还怪腿？妈呀，那就太伤人啦！请大家用心感受一下我们旁白君冷面背后的娇嗔哦……”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"陈楚河朱珠的“三世情缘”\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/V5R5-fxsqxyc1523563.jpg\"/><span class=\"img_descr\">陈楚河朱珠的“三世情缘”</span></div>\n" +
                "<p>　<strong>　十二个人怎么都好玩？</strong></p>\n" +
                "<p><strong>　　没有那么多黄渤和邓超<a class=\"wt_article_link\" href=\"http://weibo.com/5187664653?zw=ent\" onmouseover=\"WeiboCard.show(5187664653, 'ent' , this)\" target=\"_blank\">[微博]</a>，只能靠搭配</strong></p>\n" +
                "<p>　　其实很多观众初看到《非凡》的嘉宾阵容时，并没有那么看好这档节目。但节目播出之后，大家却惊喜发现这几对嘉宾之间的互动火花蛮妙的，朱珠<a class=\"wt_article_link\" href=\"http://weibo.com/1500894097?zw=ent\" onmouseover=\"WeiboCard.show(1500894097, 'ent' , this)\" target=\"_blank\">[微博]</a>&amp;陈楚河，黎明&amp;王濛……相爱相杀，戏剧效果太足！这都要归功面谈了上百位明星，精心调配嘉宾阵容的非凡团队。易骅坦言：“真人秀，尤其我们这种美式真人秀最重要的三点，第一是选角，第二是选角，第三还是选角。一档真人秀的成功一定是选角的成功、角色的成功。坦白讲，最初开始选角时，我们有的小朋友也很羡慕别的节目有孙红雷黄渤，有邓超angelababy<a class=\"wt_article_link\" href=\"http://weibo.com/1642351362?zw=ent\" onmouseover=\"WeiboCard.show(1642351362, 'ent' , this)\" target=\"_blank\">[微博]</a>，但那个是已经做得很好的了。对于我们新节目而言，12位明星怎么组组合？每个人个性鲜明，不装不作，气场互补，能认真对待节目就可以了。所以我们在前期选角时非常谨慎，我们在面谈每一位艺人时，首先要清楚他是不是认真对待这件事情，他想不想做这件事情，有的时候是公司安排的，没有办法，你能感受出来。现在这十二个嘉宾都是特别愿意来做这件事的，而且我们特意挑了不同年代不同背景不同性格的人，都是精心配过的，在上百个接触过的艺人中，这十二个人匹配度最高。事实证明，他们的搭配也是精彩的。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"《非凡》明星嘉宾与工作人员合影\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/4TYp-fxsqxxs7701588.JPG\"/><span class=\"img_descr\">《非凡》明星嘉宾与工作人员合影</span></div>\n" +
                "<p>　<strong>　体育竞技节目最重要的是动力！</strong></p>\n" +
                "<p><strong>　　拿高分换搭档增加游戏感</strong></p>\n" +
                "<p>　　脱离《极速前进》的旧模式做《非凡搭档》，团队首先提出了两个新鲜概念：一，作为一档竞技类节目，《非凡》并不安排淘汰制，而是用积分制的方式推动。二，搭档可换。易骅解释：“比如《极速前进》，每期最重要的就是去冲第一名，当游戏进行到一半，嘉宾知道自己第一名无望了的时候，可能就没有往前冲的动力了，反正最后一名、倒数第二名都没差。但是《非凡》采用倒计时积分制，就算你是最后一名，但你完成任务的速度快一些，卡到的那个倒计时时间可能就是七十分，八十分档的，和第一名分数差距不会太大，但如果你完全放弃，四五十分，那么就会非常影响你个人在排行榜上的整体分数，这是可以让他们出效果的。</p>\n" +
                "<p>　　节目的第二个驱动力就是换搭档，你分高的话就可以换自己愿意的搭档，像郑元畅<a class=\"wt_article_link\" href=\"http://weibo.com/1797007051?zw=ent\" onmouseover=\"WeiboCard.show(1797007051, 'ent' , this)\" target=\"_blank\">[微博]</a>分高就可以挑林依晨<a class=\"wt_article_link\" href=\"http://weibo.com/1785075474?zw=ent\" onmouseover=\"WeiboCard.show(1785075474, 'ent' , this)\" target=\"_blank\">[微博]</a>，陈楚河每次落后没得挑就要被挑，只能和朱珠相爱相杀，大家就觉得很好玩，有游戏感。而每次选择搭档都要根据排名分数来选，大家就觉得这个东西能决定我的命运，所以玩的更拼了，很多趣味点自然而然就出来了。”之后再加上后期字幕组、配乐组大大们的用心制作，哦，还有我们最最特别的“旁白君”的强势吐槽，节目自然就更好看啦！</p>\n" +
                "<p>　　<strong>【三、#了不起的挑战●了不起的鸡汤#】</strong></p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"《了不起的挑战》节目组自称“老母鸡”\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/TthU-fxsqxxs7696116.jpg\"/><span class=\"img_descr\">《了不起的挑战》节目组自称“老母鸡”</span></div>\n" +
                "<p>　　去年底，一档《了不起的挑战》彻底扭转了观众对于“央视”的既定印象。大家一边被节目画风逗得不能自已、狂敲弹幕，一边不敢置信地念叨：“没想到有朝一日我竟在B站刷起了央视节目？！太清奇！”嗯哼，想知道那些让央视爸爸画风大变的幕后“黑手”是谁吗？跟小浪走！</p>\n" +
                "<p>　　<strong>中韩联手树立正确价值观</strong></p>\n" +
                "<p><strong>　　央视大大给的自由度很大</strong></p>\n" +
                "<p>　　咳！小浪知道这小标题起的是有点儿太“正”了……《了不起的挑战》后期制作部分主要由灿星团队和做过原版《无挑》的MBC团队合作完成。一期节目的拍摄素材拿到手后，中方会先把所有素材都速记翻译成韩文，通过时间码标注出来，然后由韩方MBC团队负责第一周的剪辑工作，把控节目关键走向和笑点，剪到六个小时素材的样子，再交由灿星方扩充中国元素，细化表述方式，剪成最终播放的一个半小时时长。</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"后期团队制作中\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/Rasc-fxsqxxs7701397.JPG\"/><span class=\"img_descr\">后期团队制作中</span></div>\n" +
                "<p>　　虽然MBC携手灿星制作的综艺节目确有品质保障，但毕竟这也是一档“央视综艺”，不少观众总觉得央视节目比较常规、趣味性不强，无论灿星还是MBC在和央视合作过程中势必会受到许多限制？灿星某位后期大大可是要辟谣一下的哦：“其实央视给的自由度蛮大的，只不过毕竟是主流平台，有一点肯定要注意，就是要呈现正确价值观、正能量。但这个和《无限挑战》原版本身也不冲突，原版就是一档关注韩国民生的综艺节目，很多内容都是和时事热点相关。所以在最初挑选引进模式的时候，我们已从根本上达成一致啦。”</p>\n" +
                "<p>　　<strong>“污”？哼，泥萌心里有段子，看啥都是段子！</strong></p>\n" +
                "<p>　　这档深谙综艺观众心理的《了不起的挑战》播出后，众人被其清奇画风吓die的同时也有爱表白：“天了噜！我们央视爸爸‘不正经’起来连自己都害怕！”甚有网友就着节目细节总结、再加工出了不少只可意会不可言传的“污”段子，譬如嘉宾阮经天<a class=\"wt_article_link\" href=\"http://weibo.com/2506966302?zw=ent\" onmouseover=\"WeiboCard.show(2506966302, 'ent' , this)\" target=\"_blank\">[微博]</a>和华少在泥水里一起挖硬硬的藕……譬如二人以某种“特殊姿势”结束挖藕……众人大叹后期小编简直就是“污”力全开的“老司机”！后期妹子听到这个说法很是委（傲）屈（娇）：“我们后期团队20多个人大多都是90后，没经验哦！至于特别污……泥萌心里有段子看到什么都是段子，心里没有段子看什么都看不出来嘛（笑）！”。</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"华少阮经天泥潭挖藕\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160526/fno4-fxsqxxs7693099.jpg\"/><span class=\"img_descr\">华少阮经天泥潭挖藕</span></div>\n" +
                "<p>　　<strong>“后期有毒”？哈，毕竟我们90后是在二次元文化中浸大的！</strong></p>\n" +
                "<p>　　除了所谓的“污”，《了不起》还有很多能引起观众兴奋的点，譬如不时被当做BGM出现的银魂、日和、海贼王、EXO的音乐，惊掉众人下巴的二次元鬼畜画风，热门话题的即时穿插， 众人叹：“嘤嘤！这节目的后期‘有毒’啊！”后期妹子解释：“因为我们整个制作团队的年轻人本身就是在那种文化下长大的，用什么音乐、花字，一下子想到的就是那些二次元东西。用了一两次，发现网上反响非常好，就增加力度，更灵活运用起来。”</p>\n" +
                "<p>　　妹子举例：“记得我们节目播出那段时间，在B站上长期排名第一，但每到某一天我们就会掉落到第二——嗯，就是日剧《朝九晚五》每周更新的时候。那时候我也在追，就想说我们也要偷偷埋点梗！到了“交换人生”岳云鹏<a class=\"wt_article_link\" href=\"http://weibo.com/1751675285?zw=ent\" onmouseover=\"WeiboCard.show(1751675285, 'ent' , this)\" target=\"_blank\">[微博]</a>去泰国当老师那期，岳岳最后有一段关于怎么当好一个老师的独白，比较燃，我就把《朝九晚五》里面的背景音乐就铺上去了，果然在B站就炸了！到了这段，弹幕整个爆了，网友一片粉红各种叫老公老公（笑）。还有比如我们在做环保特辑时候，正好《星战》上映，我们就在节目片头用了星战的包装致敬。“了不起的消防”，我们就用了漫威的包装，映射嘉宾们是超级英雄的感觉，是有意向二次元靠拢啦，观众易懂，画面也好看。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"工作人员后期制作中\" data-link=\"\" src=\"http://n.sinaimg.cn/ent/transform/20160527/mGEd-fxsqxxu4509343.JPG\"/><span class=\"img_descr\">工作人员后期制作中</span></div>\n" +
                "<p>　<strong>　“鸡汤”了不起？唉，这只“母鸡”不好养！</strong></p>\n" +
                "<p>　　《了不起》的最大特色之一无疑就是“鸡汤风”了。适时又不过分矫情传递正能量的方式备受观众认可，那么，后期同学们到底是如何脑洞大开“孵化”出一只“母鸡”来为大家“灌鸡汤”呢？揭秘：这只 “网红母鸡”其实是《了不起》的导演一拍脑袋想出来的！</p>\n" +
                "<p>　　“因为我们节目里有很多职业体验设置，但看电视的观众其实无法那么感同身受地了解这些职业的辛苦之处，我们又不想把这些大道理说的太生硬化，就想出了用VCR表达的方式。其实‘鸡汤风’分两个部分，由科普VCR和嘉宾自己的解释独白组成的，每个VCR做了特别设计，有插画设计，动画面设计……走轻松搞笑风。第一期VCR推出后，有观众就说我们是在灌正能量鸡汤，调侃‘这碗鸡汤我不喝’，这碗鸡汤我干了’，我们就发现这每段VCR真的就是一个鸡汤般的存在嘛，那索性就放碗鸡汤放上去吧！怎么弄一碗汤表示鸡汤？导演就决定搞出一只母鸡放在汤里面。不过每次出现的母鸡都一样也听没意思的，我们就又进一步地不停变换和当期主题相符的母鸡，呈现出不一样的画风。”</p>\n" +
                "<div class=\"img_wrapper\"><img alt=\"节目中时不时在右上角会有“鸡汤”图出现\" data-link=\"\" src=\"http://ww2.sinaimg.cn/large/85cccab3gw1etdrc0dt2lg20dw06yqux.jpg\"/><span class=\"img_descr\">节目中时不时在右上角会有“鸡汤”图出现</span></div>\n" +
                "<p>　　某后期妹子在此爆料“抗议”：“我们的总导演真的是个很想成为网红的人哎！很乐于和网友互动，比如我们的打工特辑分上下两集，我个人本来想偷个懒啦，在下集就不换母鸡了，可导演非得上网问一下这期换什么母鸡呀？网友就说了要怎样怎样，他就马上截图给我，说网友要这样画！”虽然当下有点儿崩溃，但后期妹子还是羞涩对网友们“表白”：“做节目的时候多亏了大家的弹幕互动，我们节目中的很多花字都是从大家弹幕中截取下来的，多谢多谢啦！”</p>\n" +
                "<p>　　不过后期大大心里，还是存在一些遗憾：“每一期节目从素材录入到制作完成，最短只有两周半的时间，我们这个节目组还涉及大量的韩文翻译，时间真的太赶了。大量的梗是要通过反复剪辑强调，插入大量辅助素材铺垫才能体现出来的，但就是因为时间少，很多难做的笑点我们只好忍痛抛弃掉，所以有时候我们也看到大家说某处后期太烂给差评。我们也好苦，素材来不及看完来不及做，抱歉了！”</p><div class=\"show_author\">(责编：浅乐)</div>\n" +
                "<div class=\"show_statement\">　　声明：新浪网独家稿件，转载请注明出处。</div>\n" +
                "</div>";


    }

    private void initView() {
        bt_confirm_detail = (Button) findViewById(R.id.bt_confirm_detail);//发表评论的按钮
        back = (ImageView) findViewById(R.id.iv_back_detail);//页面返回按钮
        tv = (TextView) this.findViewById(R.id.tv_content_detail);//新闻内容
        tv_comment_num_detail = (TextView) this.findViewById(R.id.tv_comment_num_detail);//评论数量
        tv_bar_detail = (TextView) this.findViewById(R.id.tv_bar_detail);//顶部标题
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
        et_bottom = (EditText) findViewById(R.id.et_bottom_detail);//评论
        et_comment = (EditText) findViewById(R.id.et_comment_detail);//写评论时候的编辑框
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom_detail);
        ll_most_detail = (LinearLayout) findViewById(R.id.ll_most_detail);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_comment_detail);
        rl_back_detail = (RelativeLayout) findViewById(R.id.rl_back_detail);
        rl_comment_lv_detail = (RelativeLayout) findViewById(R.id.rl_comment_lv_detail);//评论列表布局
        back_comment = (ImageView) findViewById(R.id.iv_comment_back_detail);//评论页面返回
        collect = (ImageView) findViewById(R.id.iv_collect_bottom_detail);//喜欢
        share_detail = (ImageView) findViewById(R.id.iv_share_detail);//分享
        sl_conent = (ScrollView) findViewById(R.id.sl_conent_detail);//新闻内容滚动布局
        iv_comment_bottom = (ImageButton) findViewById(R.id.iv_comment_bottom_detail);//底部布局
        iv_share_bottom_detail = (ImageButton) findViewById(R.id.iv_share_bottom_detail);//底部分享
        lv_comment = (ListView) findViewById(R.id.lv_comment_detail);
        head = findViewById(R.id.rl_bar_detail);
        bottom = findViewById(R.id.ll_bottom_detail);
        relate1 = (TextView) findViewById(R.id.tv_relate1_detail);
        relate2 = (TextView) findViewById(R.id.tv_relate2_detail);
        relate3 = (TextView) findViewById(R.id.tv_relate3_detail);
        dialog = new SpotsDialog(NewDetailActivity.this,"正在获取新闻内容...");

    }

    private void initHandler() {
        dialog.show();//一开始页面就显示加载框
        String userId = RunTime.getRunTimeUser().getId();
        newsMessage.newDtail(userId, newDetail.getNewId());
        lv_comment.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(lv_comment);
        // tv.setText(Html.fromHtml(html));
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                if (msg.what == 0x101) {
                    dialog.dismiss();
                    tv.setText((CharSequence) msg.obj);
                }
                super.handleMessage(msg);
            }
        };
        rl_comment_lv_detail.setVisibility(View.GONE);//一进来先把评论的布局影藏

    }



    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = NewsUtils.getListViewHeight(NewDetailActivity.this,listView);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onStartDoing() {
        dialog.show();
    }

    @Override
    public void onSuccess(Object o) {
        dialog.dismiss();
        NewDetail n = (NewDetail) o;
        commentItemList = n.getCommentItems();
        newDetail.setContent(n.getContent());
        newDetail.setLove(n.isLove());
        newDetail.setComment(n.isComment());
        newDetail.setNewUrl(n.getNewUrl());
        if (newDetail.isLove()) {
            collect.setBackgroundResource(R.drawable.love_selected);
        } else {
            collect.setBackgroundResource(R.drawable.love_unselected);

        }
        int num = commentItemList.size();
        tv_comment_num_detail.setText(num + "");
        System.out.println("新闻内容长度:" + newDetail.getContent().length());
        tv.setText(Html.fromHtml(newDetail.getContent()));//新闻内容

        List<NewItem> ralateList = n.getRelateItems();
        if(ralateList!=null&&ralateList.size()>0){
            relate1.setText(ralateList.get(0).getTitle());
            relate1.setTag(ralateList.get(0).getNewId());
            if(ralateList.size()>1) {
                relate2.setText(ralateList.get(1).getTitle());
                relate2.setTag(ralateList.get(1).getNewId());
            }
            if(ralateList.size()>2) {
                relate3.setText(ralateList.get(2).getTitle());
                relate3.setTag(ralateList.get(2).getNewId());
            }
        }else{
            ll_most_detail.setVisibility(View.GONE);
        }

        tv_comment_num_detail.setVisibility(View.VISIBLE);//设置显示评论数量

        // tv.setText(newDetail.getContent().length()+"");
        commentAdapter = new CommentAdapter(NewDetailActivity.this, commentItemList);
        rl_comment_lv_detail.setVisibility(View.VISIBLE);//将评论布局可视化
        lv_comment.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(lv_comment);
        requestHtmlImage(newDetail.getContent());
      
    }

    @Override
    public void onFaild(Object o) {
        dialog.dismiss();
    }

    private void requestHtmlImage(final String content) {
        WindowManager wm = this.getWindowManager();

        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Log.i("123", width + "--" + height);
        // 因为从网上下载图片是耗时操作 所以要开启新线程
        Thread t = new Thread(new Runnable() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                // TODO Auto-generated method stub

                /**
                 * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned
                 * fromHtml (String source, Html.ImageGetterimageGetter,
                 * Html.TagHandler
                 * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
                 * (String source)方法中返回图片的Drawable对象才可以。
                 */
                Html.ImageGetter imageGetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        // TODO Auto-generated method stub
                        URL url;
                        Drawable drawable = null;
                        try {
                            Log.i("123", source);
                           // url = new URL(source);
                            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(source);

//                            drawable = Drawable.createFromStream(
//                                    url.openStream(), null);
                            drawable = new BitmapDrawable(bitmap);
                            if (drawable.getIntrinsicWidth() < 96 && drawable.getIntrinsicHeight() < 96) {
                                drawable.setBounds(0, 0,
                                        drawable.getIntrinsicWidth(),
                                        drawable.getIntrinsicHeight());
                            } else

                            if (drawable.getIntrinsicWidth() < (width / 2)) {
                                int scale1000 = width * 1000 / drawable.getIntrinsicWidth();
                                drawable.setBounds(0, 0,
                                        width,
                                        drawable.getIntrinsicHeight() * scale1000 / 1000);
                            }

                            Log.i("123", drawable.getIntrinsicWidth() + "--" + drawable.getIntrinsicHeight());
                        }  catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                        catch (MalformedURLException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
                        return drawable;
                    }
                };

                CharSequence test = Html.fromHtml(content, imageGetter, null);
                msg.what = 0x101;
                msg.obj = test;
                handler.sendMessage(msg);
            }
        });
        //  progressBar.setVisibility(View.VISIBLE);
        t.start();
    }
    private class RelateOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            String newId = (String) v.getTag();
            if(newId==null)return;
            Intent intent = new Intent(NewDetailActivity.this,NewDetailActivity.class);
            intent.putExtra("newId", newId);
            startActivity(intent);
//            NewDetailActivity.this.finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
