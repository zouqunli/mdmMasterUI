package cn.mdm.masterui.wiget.nettv;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import cn.mdm.masterui.R;
import cn.mdm.masterui.databinding.ActivityNettvBinding;

public class NetTvActivity extends AppCompatActivity {


    private ActivityNettvBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nettv);
        mBinding.netTv.setHtmlText(getHtml(),true);
    }



    private String getHtml(){
        return "<h1 class = \"postTitle\">\n" +
                "                \n" +
                "<a id=\"cb_post_title_url\" class=\"postTitle2 vertical-middle\" href=\"https://www.cnblogs.com/woaixingxing/p/7418688.html\">\n" +
                "    <span>windows svchost.exe 引起的出现的莫名其妙的窗口失去焦点</span>\n" +
                "    \n" +
                "\n" +
                "\n" +
                "\n" +
                "</a>\n" +
                "\n" +
                "            </h1>\n" +
                "            <div class=\"clear\"></div>\n" +
                "            <div class=\"postBody\">\n" +
                "                <div id=\"cnblogs_post_body\" class=\"blogpost-body blogpost-body-html\">\n" +
                "<p>我不知道你们遇到没，反正我是遇到了，现在我就把解决方法给你们，当然都是从网上整理下来的</p>\n" +
                "<p>这个失去焦点可以分为两种，一种是病毒，一种是系统自带的问题</p>\n" +
                "<p>首先你得知道自己的窗口被什么给挤掉了焦点</p>\n" +
                "<p>&nbsp;先看看这篇文章：&nbsp;<span style=\"color: rgba(51, 102, 255, 1)\"><a href=\"http://blog.csdn.net/wuruiaoxue/article/details/46774229\"><span style=\"color: rgba(51, 102, 255, 1)\">电脑无故失去焦点，罪魁祸首是谁？终极解决办法</span></a></span></p>\n" +
                "<p><span class=\"link_title\">里面有两个工具组合使用可以查看你的窗口被什么进程给顶掉了焦点，具体怎么使用里面有说明我就不赘述了。</span></p>\n" +
                "<h3><span class=\"link_title\">我这里是一开始查询到到底是什么进程不停的抢我焦点。</span></h3>\n" +
                "<p><span class=\"link_title\"><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823150249808-615130515.png\" alt=\"\"></span></p>\n" +
                "<p>开始看着着标题名字很耐人寻味，不是有人在耍我吧~，结果我就放入了ViewWizard (这个是上面博客文中的提供的工具)</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823150635308-591811805.png\" alt=\"\"></p>\n" +
                "<p>结果：what？怎么啥都没有？</p>\n" +
                "<p>后来我又试了下监听窗口失去焦点，结果发现妈的，这家伙的句柄一直在变，变换的间隔时间是你切换窗口后到失去焦点的间隔时间，所以我又监听到失去焦点后立马把句柄信息输入了ViewWizard中就如下图</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823151141449-326371286.png\" alt=\"\"></p>\n" +
                "<p>结果：终于找到了是谁在抢我焦点，结果是svchost.exe进程，看了路劲大概分析下，这不是系统的文件嘛，然后吐槽了下微软，谈定了打开了百度。</p>\n" +
                "<p>查到了大概意思如下：</p>\n" +
                "<p><strong>syswow64是什么文件夹？</strong><br>sysWoW64 (Windows-on-Windows 64-bit)是一个Windows操作系统的子系统, 能够运行32-bit 应用&nbsp; windows操作系统程序, 并且在所有的64-bit 版本的windows上都存在，包括：Windows 2000 Limited Edition Windows XP Professional x64 Edition, and IA-64 64-bit版本的Windows Server 2003 64-bit版本的Windows www.xitonghe.com&nbsp;Vista 64-bit版本的Windows Server 2008 64-bit版本的Windows 7在Windows server 2008 R2上, 这是一个可选组件. WoW64被设计用来处理许多在32-bit Windows 和64-bit Windows www.xitonghe.com 之间的不同, 尤其是在Windows自身的结构变化上的不同。可以负责任的说<strong>syswow64</strong>是一个很重要的文件夹，你的 64位 win7旗舰版能运行32位的软件全靠它。<br><br><strong>syswow64文件夹可以删除吗？</strong><br>看到上面的解释相信你已经不敢去删除了，没错删除后你的系统就会崩溃，提示缺少各种文件。</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><br>好吧，不能删除，那就找其他办法吧。</p>\n" +
                "<p>终于找到了下面的方法，下面的是win7的方法，win10也能用只是有些地方要注意下</p>\n" +
                "<p>1.确定是svchost.exe抢占了程序焦点,导致打字或游戏时自动跳出桌面! &nbsp; &nbsp; ------ &gt; <span style=\"color: rgba(255, 102, 0, 1)\">&nbsp;<strong>这个是前提条件哈</strong></span></p>\n" +
                "<p><br>2.如果不是上述所说的问题请搜索\"Win7失去焦点\"根据修改注册表或停用windows up等解决.</p>\n" +
                "<p><br>3.解决svchost.exe抢占很简单,打开任务管理器,吧这个对勾去掉! &nbsp;(<strong><span style=\"color: rgba(255, 102, 0, 1)\">这个win10我是没看到这个勾选，所以win10不用进行这一步</span></strong>)<br><img src=\"https://img2018.cnblogs.com/blog/677455/201902/677455-20190218164347007-1686321052.png\" alt=\"\"></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><br><br>4.找到svchost.exe进程,右键\"打开文件位置\"确定文件在SysWOW64中;(<strong><span style=\"color: rgba(255, 102, 0, 1)\">这里我要提下，这里是重点，不能关闭System32文件下的svchost.exe</span></strong>)</p>\n" +
                "<p><br>5.一般会有7个svchost.exe进程都在SysWOW64中,如果是在System32中的不要结束; &nbsp; (<strong><span style=\"color: rgba(255, 102, 0, 1)\">一般7个我看了我的，发现竟然有十几个...</span><span style=\"color: rgba(255, 102, 0, 1)\">.数字不重要哈</span></strong>)</p>\n" +
                "<p><br>6.结束完进程后将SysWOW64中的svchost.exe改名,我改成svchost.exe.bak,重启就不会再跳焦点了!!!!<br><img class=\"BDE_Image\" src=\"http://imgsrc.baidu.com/forum/w%3D580/sign=c501a28df51fbe091c5ec31c5b620c30/0b3c8c8ba61ea8d343d5e9ff900a304e271f5898.jpg\" alt=\"\" width=\"560\" height=\"199\"></p>\n" +
                "<p>7.这里你会发现你如果以前没有修改权限的话，是不能修改文件名称的，要么提示你需要获取管理员权限要不提示你获取其他某某某的权限。</p>\n" +
                "<p>win10的话应该是要求获取Trustedinstaller权限，我的话这边由于是已经修改过了那么就是会提示这样的，这里我已经成功的重命名了，所以这边的文件名称是修改过的，为了给你们演示，只是告诉你们会有个这样的提示。</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823153351527-1231009398.png\" alt=\"\"></p>\n" +
                "<p>那么这时候我们该怎么做呢？</p>\n" +
                "<p>你们可以先这样获取管理员权限的重命名，如果可以就不用往下看了，如果不可以继续向下看吧。</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823153857199-1511301370.png\" alt=\"\"></p>\n" +
                "<p>不行的话请看看这里 &nbsp;选中文件，右键属性&nbsp;--&gt; 安全</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154211949-1038970321.png\" alt=\"\"></p>\n" +
                "<p>会发现管理员都没有这个写入，修改，权限，那么就算你是管理员你也改不了。</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>接下来我们这么办</p>\n" +
                "<p>首先选中文件，右键属性 --&gt; 安全 --&gt; 高级</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154552464-756536312.png\" alt=\"\"></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154623714-1570731121.png\" alt=\"\"></p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154657308-1715111670.png\" alt=\"\"></p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154848527-1737845260.png\" alt=\"\"></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;<img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823154946433-1546054237.png\" alt=\"\"></p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823155449230-813807278.png\" alt=\"\"></p>\n" +
                "<p>点击确定（说明下为什么要把所有者改了？因为首先我们没有权限，我们要添加权限，但添加权限的话，又需要你是所有者，那么就得先获取所有者）</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823155632074-1872218503.png\" alt=\"\"></p>\n" +
                "<p>你看到管理员只有读取和执行的权限 ，所以需要我们自己添加权限，当我们获取了所有者就可以添加了</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823160223011-1337713162.png\" alt=\"\"></p>\n" +
                "<p>再次选中文件，右键</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823153857199-1511301370.png\" alt=\"\"></p>\n" +
                "<p>&nbsp;你会发现可以修改了。</p>\n" +
                "<p><img src=\"https://images2017.cnblogs.com/blog/677455/201708/677455-20170823160520105-1574034122.png\" alt=\"\"></p>\n" +
                "<p>如果你怕文件会被其他非法软件轻易修改的话，就把修改权限去除，只不过你再次修改的时候就必须添加上了。</p>\n" +
                "<p>&nbsp;这时你会发现你的焦点不会失去了。大功告成。完美.</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>有其他原因可以参考:</p>";
    }
}