package org.accessibility.mo.haokan;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 作者 create by moziqi on 2018/9/27
 * 邮箱 709847739@qq.com
 * 说明
 **/
public class HaokanAccessibilityService extends AccessibilityService {

    private final static String TAG = HaokanAccessibilityService.class.getSimpleName();


    private boolean isComment = false;

    /**
     * 当启动服务的时候就会被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventText = "";
        //Log.i(TAG, "onAccessibilityEvent = " + event.toString());
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                if (App.type == 1) {
                    share(event);
                } else if (App.type == 2) {
                    comment(event);
                }
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            default:
                if (eventType == 4) {
                    //点击下一个视频
                    List<AccessibilityNodeInfo> nextNodesById = findNodesById(event, "com.baidu.haokan:id/next_btn");
                    performClick(nextNodesById, false);
                    if (isComment) {
                        isComment = false;
                    }
                }
                eventText = "TYPE_" + eventType;
        }
        Log.i(TAG, eventText);
    }

    private void share(AccessibilityEvent event) {
        List<AccessibilityNodeInfo> enterBtn = findNodesById(event, "com.baidu.haokan:id/bottom_enter_btn");
//                boolean enterBtnClick = performClick(enterBtn, true);
//                Log.i(TAG, "enterBtnClick = " + enterBtnClick);
        //开宝箱得金币
        List<AccessibilityNodeInfo> kbx = findNodesById(event, "com.baidu.haokan:id/go-shitu");
//                kbxClick = performClick(kbx, false);
//                Log.i(TAG, "kbxClick = " + kbxClick);

        //分享图片
        List<AccessibilityNodeInfo> shareNodesById = findNodesById(event, "com.baidu.haokan:id/share_img");
        performClick(shareNodesById, false);
        //点击微信分享
        List<AccessibilityNodeInfo> weixinNodesById = findNodesById(event, "com.baidu.haokan:id/weixin_container");
        performClick(weixinNodesById, false);
    }

    private void comment(AccessibilityEvent event) {
        if (isComment) {
            return;
        }
        //点击底部评论
        List<AccessibilityNodeInfo> commentNodesById = findNodesById(event, "com.baidu.haokan:id/comment_btn");
        performClick(commentNodesById, false);
        //打开说点什么
        List<AccessibilityNodeInfo> addCommentNodesById = findNodesById(event, "com.baidu.haokan:id/iv_emotion");
        performClick(addCommentNodesById, false);
        //文本编辑器
        List<AccessibilityNodeInfo> edittextNodesById = findNodesById(event, "com.baidu.haokan:id/detail_add_comment_edittext");
        performClick(edittextNodesById, false);
        //使用剪切板
        ClipboardManager clipboard = (ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", App.comment[new Random().nextInt(App.comment.length)]);
        clipboard.setPrimaryClip(clip);
        //实现复制
        performPaste(edittextNodesById, false);
        //发布评论
        List<AccessibilityNodeInfo> publishNodesById = findNodesById(event, "com.baidu.haokan:id/publish_btn");
        performClick(publishNodesById, false);
        //标致过评论了
        isComment = true;
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     */
    public List<AccessibilityNodeInfo> findNodesById(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        }
        return null;
    }

    public List<AccessibilityNodeInfo> findNodesById(AccessibilityEvent event, String viewId) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        }
        return null;
    }

    public List<AccessibilityNodeInfo> findNodesByText(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(viewId);
        }
        return null;
    }

    public List<AccessibilityNodeInfo> findNodesByText(AccessibilityEvent event, String viewId) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(viewId);
        }
        return null;
    }

    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos, boolean isClickParent) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                Log.i(TAG, "View类型：" + node.getClassName() + "  isEnabled= " + node.isEnabled());
                // 进行模拟点击
                if (node.isEnabled()) {
                    //1.因为imageview给百度设置click false，拿到framelayou还是可以点击到，或者本来就这样设计
                    if (isClickParent) {
                        return node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
        return false;
    }

    private boolean performPaste(List<AccessibilityNodeInfo> nodeInfos, boolean isClickParent) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                Log.i(TAG, "View类型：" + node.getClassName() + "  isEnabled= " + node.isEnabled());
                // 进行模拟点击
                if (node.isEnabled()) {
                    if (isClickParent) {
                        node.getParent().performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                        return node.getParent().performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    } else {
                        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                        return node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    }
                }
            }
        }
        return false;
    }
}
