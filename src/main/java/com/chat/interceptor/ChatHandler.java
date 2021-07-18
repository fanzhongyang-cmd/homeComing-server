package com.chat.interceptor;
import com.jerry.entity.Message;
import com.jerry.entity.User;
import com.jerry.mapper.MessageMapper;
import com.jerry.mapper.TaskMapper;
import com.jerry.mapper.UserMapper;

import org.json.JSONObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Controller
@Service
//@Component
@MapperScan("com.jerry.mapper")
public class ChatHandler implements WebSocketHandler {
    private static MessageMapper messageMapper;
    private static TaskMapper taskMapper;
    private static UserMapper userMapper;
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ChatHandler.applicationContext = applicationContext;
    }
    //    @Autowired
//    public static void setMessageMapper(MessageMapper messageMapper) {
//        ChatHandler.messageMapper = messageMapper;
//    }
//    @Autowired
//    public static void setTaskMapper(TaskMapper taskMapper) {
//        ChatHandler.taskMapper = taskMapper;
//    }
//    @Autowired
//    public static void setUserMapper(UserMapper userMapper) {
//        ChatHandler.userMapper = userMapper;
//    }


    private static final Map<Integer, WebSocketSession> userMap = new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立连接");
        String INFO = session.getUri().getPath().split("INFO=")[1];
        System.out.println("INFO:"+INFO);
//        System.out.println("session:"+session);
        if (INFO != null && INFO.length() > 0) {
            //org.json.JSONObject jsonObject = new org.json.JSONObject(INFO);
//            int task_id=Integer.parseInt(jsonObject.optString("task_id"));
            int user_id=Integer.parseInt(INFO);
            if(userMap.get(user_id)==null){
                userMap.put(user_id,session);
                System.out.println("新socket：user_id:"+user_id+"session:"+session);
            }else{
                userMap.replace(user_id,session);
                System.out.println("更新socket：user_id:"+user_id+"session:"+session);
            }


        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        try {
            //消息格式：{"to_id": 10000001,"from_id":20210001,"message":message,"type":}
            messageMapper=applicationContext.getBean(MessageMapper.class);
            System.out.println("websocketMessage:"+webSocketMessage.toString());
            JSONObject jsonobject = new JSONObject(webSocketMessage.getPayload().toString());
            System.out.println("jsonobject:"+jsonobject);
            Message message = new Message(jsonobject.toString());
            message.setMessage_id(messageMapper.getId());
            System.out.println("发送消息："+message.toString());

            if(message.getPattern()==Message.Pattern_Group){

                System.out.println("attribute"+webSocketSession.getAttributes());
                System.out.print("群聊消息：");
                System.out.println(message.toString());
                sendMessageToGroupUsers(message.getTo_id(),message);
            }else {
                System.out.print("私人消息：");
                System.out.println(message.toString());
                sendMessageToUser(message.getTo_id(),message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param to_id
     * @param message
     * @return
     */
    private  boolean sendMessageToUser(int to_id, Message message) {
        System.out.println("private message :"+message);
        WebSocketSession session = userMap.get(to_id);
        WebSocketSession mySession =userMap.get(message.getFrom_id());

        messageMapper=applicationContext.getBean(MessageMapper.class);
        net.sf.json.JSONObject jsonMessage=messageToJson(message);
        messageMapper.insertMessage(message);
        try {
            if (session.isOpen()){
                session.sendMessage(new TextMessage(jsonMessage.toString()));
                mySession.sendMessage(new TextMessage(jsonMessage.toString()));

            }else {

                messageMapper.insertUnreadMessage(message.getMessage_id(),message.getTo_id());
                System.out.println(message+"保存数据库");

            }

        }catch(NullPointerException ne){
//            ne.printStackTrace();
            messageMapper.insertUnreadMessage(message.getMessage_id(),message.getTo_id());
            return false;
        }catch (IOException e) {
//            e.printStackTrace();


        }
        return true;
    }
    public  boolean sendOfflineMessageToUser(int to_id, Message message) {
        WebSocketSession session = userMap.get(to_id);
        try {
            if (session.isOpen()){
                session.sendMessage(new TextMessage(message.toString()));
                messageMapper.deleteUnreadMessage(message.getMessage_id(),message.getTo_id());
            }else {
               return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     *
     * @param to_id
     * @param message
     * @return
     */
    private boolean sendMessageToGroupUsers(int to_id, Message message) {
        taskMapper=applicationContext.getBean(TaskMapper.class);
        messageMapper=applicationContext.getBean(MessageMapper.class);
        System.out.println("pubic message:"+message);
        boolean allSendSuccess = true;
        String strMessage=message.toString();
        List<Integer> users=taskMapper.getGroupUserId(to_id);
        messageMapper.insertMessage(message);
        net.sf.json.JSONObject jsonMessage=messageToJson(message);
        for(int i=0;i<users.size();i++){
            int user_id=users.get(i);
            WebSocketSession session=userMap.get(user_id);
            try{
                if(session.isOpen()){
                    session.sendMessage(new TextMessage(jsonMessage.toString()));
                }else {

                    messageMapper.insertUnreadMessage(message.getMessage_id(),user_id);
                }
            }catch (NullPointerException ne){
                ne.printStackTrace();
                messageMapper.insertUnreadMessage(message.getMessage_id(),user_id);
            }
            catch (Exception e){
                e.printStackTrace();
                allSendSuccess=false;
            }

        }


        return allSendSuccess;
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("连接出错"+"user_id"+getUserIdFromSession(webSocketSession));
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        userMap.remove(getUserIdFromSession(webSocketSession));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        System.out.println("连接已关闭：" + closeStatus+"user_id"+getUserIdFromSession(webSocketSession));

        userMap.remove(getUserIdFromSession(webSocketSession));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 获取用户名称
     *
     * @param session
     * @return
     */
    private int getUserIdFromSession(WebSocketSession session) {
        try {
            int user_id = (int)session.getAttributes().get("user_id");
            return user_id;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取房间号
     *
     * @param session
     * @return
     */
    private String getRoomIdFromSession(WebSocketSession session) {
        try {
            String roomId = (String) session.getAttributes().get("task_id");
            return roomId;
        } catch (Exception e) {
            return null;
        }
    }
    private net.sf.json.JSONObject messageToJson(Message message){
        userMapper=applicationContext.getBean(UserMapper.class);
        net.sf.json.JSONObject jsonMessage=new net.sf.json.JSONObject();
        jsonMessage.put("from_id",message.getFrom_id());
        jsonMessage.put("to_id",message.getTo_id());
        jsonMessage.put("time",message.getTime());
        jsonMessage.put("type",message.getType());
        jsonMessage.put("pattern",message.getPattern());
        jsonMessage.put("message",message.getMessage());
        jsonMessage.put("from_name",userMapper.getName(message.getFrom_id()));
        jsonMessage.put("from_face",userMapper.getIcon(message.getFrom_id()));
        return jsonMessage;
    }

}

