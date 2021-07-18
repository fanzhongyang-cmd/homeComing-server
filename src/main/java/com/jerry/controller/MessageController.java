package com.jerry.controller;

import com.fileserver.FileUploadUtils;
import com.jerry.config.Configuration;
import com.jerry.entity.Message;
import com.jerry.entity.Task;
import com.jerry.entity.User;
import com.jerry.mapper.MessageMapper;
import com.jerry.mapper.TaskMapper;
import com.jerry.mapper.UserMapper;
import com.json.JsonResult;
import net.sf.json.JSONObject;
//import netscape.javascript.JSObject;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.util.*;



@Controller
public class MessageController {

    @Autowired
    public MessageMapper messageMapper;

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public TaskMapper taskMapper;


    @RequestMapping("/message/uploadMessageFile")
    @ResponseBody
    public JsonResult uploadMessageFile(@RequestParam MultipartFile file){
        try {
            System.out.println(file.getOriginalFilename());
//            face.getContentType();
            String extension=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = FileUploadUtils.upload(Configuration.messageFileRepository, file,extension);//将文件保存本地并返回相对路径及文件名

            String completePath = Configuration.messageFileRepository+fileName;
            String IP = InetAddress.getLocalHost().getHostAddress();
            completePath = "http://" + IP + ":" + Configuration.port + "/getFile?path=" + completePath;

            return new JsonResult<>(completePath, Configuration.globalSuccessCode, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "上传失败！");
        }
    }
    @RequestMapping(value = "/message/getMyMessage",produces="Application/json")
    @ResponseBody
    public JsonResult getMyMessage(@RequestParam int user_id){
        List<Message> messages=messageMapper.getUnreadMessage(user_id);
//        System.out.println("message:"+messages);
        List<JSONObject> outLayer = new ArrayList<>();
        Map<Integer,List<Message>> map=new HashMap<>();
        //分类
        for(int i=0;i<messages.size();i++){
            Message IMessage=messages.get(i);
//            System.out.println(IMessage);
            int key=IMessage.getPattern()==Message.Pattern_Group?IMessage.getTo_id():IMessage.getFrom_id();
            if(map.get(key)==null){
                List<Message> subMessages=new ArrayList<>();
                subMessages.add(IMessage);
                map.put(key,subMessages);
            }else {
                map.get(key).add(IMessage);
            }
        }
        System.out.println(map);
        //整合成json
        Iterator<Map.Entry<Integer,List<Message>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, List<Message>> entry = entries.next();
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            JSONObject jsonObject = new JSONObject();
            if (entry.getValue().get(0).getPattern()==Message.Pattern_private){
                User user=userMapper.getUserById(entry.getKey());
                jsonObject.put("id",user.getUser_id());

                jsonObject.put("name",user.getUser_name());
                jsonObject.put("face",user.getUser_face());
                jsonObject.put("type",Message.Pattern_private);
                List<JSONObject> messageList = new ArrayList<>();
                for(Message eachMessage:entry.getValue()){
                    Message message=eachMessage;

                    JSONObject jsonMessage=new JSONObject();
                    jsonMessage.put("user_id",message.getFrom_id());

                    jsonMessage.put("type",message.getType());
                    jsonMessage.put("msgTime",message.getTime());
                    jsonMessage.put("user_face",userMapper.getIcon(message.getFrom_id()));
                    jsonMessage.put("user_name",userMapper.getName(message.getFrom_id()));
                    jsonMessage.put("msg",message.getMessage());
                    messageList.add(jsonMessage);
                }


                jsonObject.put("msg",messageList);
            }else {
                Task task=taskMapper.getTaskById(entry.getKey());
                jsonObject.put("id",task.getTask_id());

                jsonObject.put("name",task.getName());
                jsonObject.put("face",task.getLost_face());
                jsonObject.put("type",entry.getValue().get(0).getPattern());
//                JSONObject message = new JSONObject(entry.getValue().toString());
                List<JSONObject> jsonMessages=new ArrayList<>();
                for(Message message:entry.getValue()){
                    JSONObject jsonMessage = new JSONObject();
                    jsonMessage.put("user_id",message.getFrom_id());

                    jsonMessage.put("type",message.getType());
                    jsonMessage.put("msgTime",message.getTime());
                    jsonMessage.put("user_face",userMapper.getIcon(message.getFrom_id()));
                    jsonMessage.put("user_name",userMapper.getName(message.getFrom_id()));
                    jsonMessage.put("msg",message.getMessage());
                    jsonMessages.add(jsonMessage);
                }
                jsonObject.put("msg",jsonMessages);
            }



            outLayer.add(jsonObject);
        }
        //删除已经推送的未读消息
        for (Message message:messages){
            messageMapper.deleteUnreadMessage(message.getMessage_id(),user_id);
        }


        return new JsonResult(outLayer,Configuration.globalSuccessCode,"操作成功");

    }
}
