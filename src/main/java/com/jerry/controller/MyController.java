package com.jerry.controller;

import com.fileserver.FileUploadUtils;
import com.jerry.config.Configuration;
import com.jerry.entity.Account;
import com.jerry.entity.Location;
import com.jerry.entity.Task;
import com.jerry.entity.User;

import com.jerry.mapper.*;
import com.json.JsonResult;
import com.lzw.face.FaceHelper;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyController {

    @Autowired
    public MessageMapper messageMapper;

    @Autowired
    public UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private AccountMapper accountMapper;



    @RequestMapping("/signUp")
    @ResponseBody
    public JsonResult signUp(@RequestParam long telephone,
                             @RequestParam String password) {
        int id = userMapper.getId();
        System.out.println("注册");
        System.out.println("电话：" + telephone);
        System.out.println("id:" + id);

        try {
            int count = accountMapper.countTel(telephone);
//            System.out.println("count:"+count);
            if (count != 0) {
                return new JsonResult<>(300, "号码已存在");
            } else {

                accountMapper.addAccount(new Account(id, password));
                userMapper.addUser(new User(id, telephone));

                return new JsonResult(id, Configuration.globalSuccessCode, "操作成功");
            }
        } catch (Exception e) {
            accountMapper.deleteAccount(id);
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "操作失败");
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public JsonResult login(@RequestBody Account account) {
        try {
            System.out.println("登录");
            Account account1 = accountMapper.query(account.getUser_id());
            if (account.getUser_id() == account1.getUser_id() && account.getPassword().equals(account1.getPassword())) {

                userMapper.changeUserState(account.getUser_id(), User.ONLINE);
                if(userMapper.getUserById(account.getUser_id()).getUser_state()==User.ONLINE){
                    return new JsonResult(600,"该账号已登录");
                }
                return new JsonResult(userMapper.getUserById(account.getUser_id()), Configuration.globalSuccessCode, "登录成功");
            } else {
                return new JsonResult(Configuration.globalFailureCode, "用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "服务器异常，请稍后再试！");
        }
    }

    @RequestMapping("/telLogin")
    @ResponseBody
    public JsonResult telLogin(@RequestParam long telephone,
                               @RequestParam String password) {
        try {
            System.out.println("手机登录");
            System.out.println(telephone);
            User user = userMapper.getUserByTel(telephone);
            if(user.getUser_state()==User.ONLINE){
                return new JsonResult(600,"该账号已登录");
            }
            Account account = accountMapper.query(user.getUser_id());

            if (password.equals(account.getPassword())) {
                return new JsonResult(user, Configuration.globalSuccessCode, "登录成功");
            }
            return new JsonResult(300, "电话或密码错误");
        } catch (NullPointerException ne) {
            ne.printStackTrace();
            return new JsonResult(400, "Null");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("错误");
            return new JsonResult(Configuration.globalFailureCode, "操作异常");
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public JsonResult logout(@RequestParam int user_id) {
        try {
            System.out.println("退出登录");
            userMapper.changeUserState(user_id, User.OFFLINE);
            locationMapper.deleteUserLocation(user_id);
            return new JsonResult(Configuration.globalSuccessCode, "EXIT!");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "EXIT WITH EXCEPTION");
        }
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public JsonResult updatePassword(@RequestParam int user_id,
//                                     @RequestParam String old_password,
                                     @RequestParam String new_password){
        try{
            accountMapper.updateAccount(new Account(user_id,new_password));
            return new JsonResult(Configuration.globalSuccessCode,"修改密码成功");

        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode,"异常");
        }

    }
    ///
    @RequestMapping("/onlineNotify")
    @ResponseBody
    public JsonResult onlineNotify(@RequestParam int user_id,
                                   @RequestParam double user_longitude,
                                   @RequestParam double user_latitude) {
        try {

            System.out.println(user_id + "上线");
            userMapper.changeUserState(user_id, User.ONLINE);
            userMapper.updateUserLocation(new Location(user_longitude, user_latitude));
            return new JsonResult(Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "异常");
        }
    }

    @RequestMapping("/offlineNotify")
    @ResponseBody
    public JsonResult offlineNotify(@RequestParam int user_id) {
        System.out.println(user_id + "下线");
        userMapper.changeUserState(user_id, User.OFFLINE);

        return new JsonResult(Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/user")
    @ResponseBody
    public JsonResult<List<User>> queryAllUser() {

        List<User> users = userMapper.getAllUser();

        return new JsonResult<>(users, Configuration.globalSuccessCode, "获取成功");
    }

    @PostMapping("/user/addUser")
    @ResponseBody
    public JsonResult addOneUser(@RequestBody User user) {
        try {
            System.out.println(user.toString());
            accountMapper.addAccount(new Account(user.getUser_id(), Account.DefaultPassword));
            userMapper.addUser(user);
            System.out.println("添加新用户：");
            System.out.println(user.toString());
            return new JsonResult(Configuration.globalSuccessCode, "操作成功！");
        } catch (Exception e) {
            accountMapper.deleteAccount(user.getUser_id());
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "操作失败！");
        }
    }

    @RequestMapping("/user/deleteUser")
    @ResponseBody
    public JsonResult deleteUser(@RequestParam int user_id) {
        try {
            userMapper.deleteUser(user_id);
            accountMapper.deleteAccount(user_id);
            System.out.println("id:" + user_id + "的用户已删除");
            return new JsonResult(Configuration.globalSuccessCode, "id:" + user_id + "的用户已删除");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "删除失败");
        }
    }

    @RequestMapping("/user/updateUser")
    @ResponseBody
    public JsonResult updateUser(@RequestBody User user) {
        try {
            userMapper.updateUser(user);
            System.out.println("更新用户：");
            System.out.println(user.toString());
            return new JsonResult(userMapper.getUserById(user.getUser_id()), Configuration.globalSuccessCode, "id:" + user.getUser_id() + "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "更新失败！");
        }
    }

    @RequestMapping("/user/updateUserInfo")
    @ResponseBody
    public JsonResult updateUserInfo(@RequestParam int user_id,
                                     @RequestParam String type,
                                     @RequestParam String value) {
        try {
            System.out.println("更新用户信息");
            userMapper.updateUserInfo(user_id, type, value);
            return new JsonResult(userMapper.getUserById(user_id), Configuration.globalSuccessCode, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "更新失败");
        }
    }

    @RequestMapping("/task")
    @ResponseBody
    public JsonResult<List<Task>> getAllTask() {
        System.out.println("获取全部任务");
        return new JsonResult<>(taskMapper.getAllTask(), Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping(value = "/task/getTask",produces = "Application/json")
    @ResponseBody
    public JsonResult<List<Task>> getTask(@RequestParam int page,
                                          @RequestParam double longitude,
                                          @RequestParam double latitude) {
        try{

            int count=10;
            List<Task> tasks = taskMapper.getAllRunningTask();
            int length=tasks.size();
            System.out.println("获取任务");
            System.out.println("length:"+length);
            System.out.println(tasks.toString());
            Location myLocation=new Location(longitude,latitude);

            double[] distance = new double[tasks.size()];

            for (int i = 0; i < tasks.size(); i++) {
                distance[i] = myLocation.getDistance(tasks.get(i).getLost_longitude(), tasks.get(i).getLost_latitude());
                tasks.get(i).setDistance(((int) distance[i]));
            }//计算
            tasks.sort(Comparator.comparing(Task::getDistance));//排序




            if(length>=count*page+count){
                return new JsonResult<>(tasks.subList(page*count, (page+1)*count), Configuration.globalSuccessCode, "操作成功");
            }else if(length>=page*count&&length<=(page+1)*count){
//                System.out.println(sortedTask.subList(page*count, length));
                return new JsonResult<>(tasks.subList(page*count, length), Configuration.globalSuccessCode, "操作成功");
            }else {
                return new JsonResult<>(400 ,"no more");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult<>(Configuration.globalFailureCode,"获取失败");
        }

    }

    @RequestMapping("/task/getNewestTask")
    @ResponseBody
    public JsonResult getNewestTask(@RequestParam double longitude,
                                    @RequestParam double latitude) {

        try{
            System.out.println("获取最新任务");
            List<Task> tasks = taskMapper.getAllTask();
            List<Task> newTask = new ArrayList<Task>();
            int count = 2;
            Location location=new Location(longitude,latitude);
            int[] addedIndex = new int[count];
            for (int i = 0; i < tasks.size(); i++) {
                double distance = location.getDistance(tasks.get(i).getLost_longitude(), tasks.get(i).getLost_latitude());
                tasks.get(i).setDistance((int)distance);
                if (distance <= 5000) {
                    newTask.add(tasks.get(i));
                    addedIndex[count - 1] = i;
                    count--;
                    if (count == 0)
                        break;
                }
            }
            if (count != 0) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (i != addedIndex[0] || i != addedIndex[1]) {
                        newTask.add(tasks.get(i));
                        count--;
                        if (count == 0)
                            break;
                    }
                }
            }

            return new JsonResult(newTask, Configuration.globalSuccessCode, "操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode,"操作失败");
        }
    }

    @RequestMapping("/task/getMyTask")
    @ResponseBody
    public JsonResult getMyTask(@RequestParam int user_id) {
        try {
            List<Task> tasks = taskMapper.getMyTask(user_id);

            if(tasks.size()==0){
                return new JsonResult(400," ");
            }
            return new JsonResult(tasks, Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "操作失败");
        }
    }
    @RequestMapping("/task/getMyTaskList")
    @ResponseBody
    public JsonResult getMyTaskList(@RequestParam int user_id) {
        try {
            List<Task> tasks = taskMapper.getMyTask(user_id);
            List<Task> publishs=taskMapper.getMyPublish(user_id);
//            tasks.addAll(publishs);
            JSONObject data=new JSONObject();
            data.put("published",publishs);
            data.put("joined",tasks);
//            if(tasks.size()==0){
//                return new JsonResult(400," ");
//            }
            return new JsonResult(data, Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "操作失败");
        }
    }
    @RequestMapping("/task/getMyPublish")
    @ResponseBody
    public JsonResult getMyPublish(@RequestParam int user_id) {
        try {
            List<Task> tasks = taskMapper.getMyPublish(user_id);
            if(tasks.size()==0){
                return new JsonResult(400," ");
            }
            return new JsonResult(tasks, Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "操作失败");
        }
    }
    @RequestMapping("/task/getTaskById")
    @ResponseBody
    public JsonResult<Task> getTaskById(@RequestParam int task_id) {
        return new JsonResult<>(taskMapper.getTaskById(task_id), Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/isInTask")
    @ResponseBody
    public JsonResult isInTask(@RequestParam int user_id,
                              @RequestParam int task_id){
        try{

            return taskMapper.isInTask(user_id,task_id)?new JsonResult(true,Configuration.globalSuccessCode,"你已经在任务中"):
                    new JsonResult(false,Configuration.globalSuccessCode,"你不在任务中");
        }catch (Exception e){
            return new JsonResult(Configuration.globalFailureCode,"操作失败");
        }

    }

    @RequestMapping("/task/joinTask")
    @ResponseBody
    public JsonResult joinTask(@RequestParam int user_id,
                               @RequestParam int task_id){
        try{
            System.out.println(user_id+"加入任务"+task_id);
            taskMapper.joinTask(user_id,task_id,System.currentTimeMillis());
            return new JsonResult(Configuration.globalSuccessCode,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode,"操作失败");
        }
    }
    @RequestMapping("/task/exitTask")
    @ResponseBody
    public JsonResult exitTask(@RequestParam int user_id,
                               @RequestParam int task_id){
        try{
            taskMapper.exitTask(user_id,task_id);
            return new JsonResult(Configuration.globalSuccessCode,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode,"操作失败");
        }
    }
    @RequestMapping("/task/getTaskByInfo")
    @ResponseBody
    public JsonResult<List<Task>> getTaskByInfo(@RequestParam String info) {
        return new JsonResult<>(taskMapper.getTaskByInfo(info), Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/getTaskByName")
    @ResponseBody
    public JsonResult<List<Task>> getTaskByName(@RequestParam String name) {
        return new JsonResult<>(taskMapper.getTaskByName(name), Configuration.globalSuccessCode, "操作成功");
    }


    @RequestMapping("/face/uploadFace")
    @ResponseBody
    public JsonResult uploadFace(@RequestParam int task_id,
                                 @RequestParam MultipartFile face,
                                 @RequestParam int face_width,
                                 @RequestParam int face_height) {
        try {
            System.out.println(face.getOriginalFilename());
//            face.getContentType();
            String fileName = FileUploadUtils.uploadPicture(Configuration.faceRepository + task_id + "/", face);//将文件保存本地并返回相对路径及文件名

            String completePath = Configuration.faceRepository + task_id + "/" + fileName;
            String IP = InetAddress.getLocalHost().getHostAddress();
            completePath = "http://" + IP + ":" + Configuration.port + "/getFile?path=" + completePath+"|[!/*image*/!]|"+face_width+"|[!/*image*/!]|"+face_height;
            taskMapper.insertFace(task_id, completePath);//更新数据库

            Task task=taskMapper.getTaskById(task_id);
            if(taskMapper.getTaskFace(task_id).equals(Task.DefaultEmptyFace)){
                taskMapper.setTaskFace(task_id,completePath);
            }

            return new JsonResult<>(completePath, Configuration.globalSuccessCode, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "上传失败！");
        }
    }
//    @RequestMapping("/file/uploadFile")
//    @ResponseBody
//    public JsonResult uploadFile(@RequestParam String type,
//                                 @RequestParam MultipartFile file) {
//        try {
//
//
//
//
//            return new JsonResult<>( Configuration.globalSuccessCode, "上传成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new JsonResult(Configuration.globalFailureCode, "上传失败！");
//        }
//    }
    @RequestMapping("/face/uploadIcon")
    @ResponseBody
    public JsonResult uploadIcon(@RequestParam int user_id,
                                 @RequestParam MultipartFile icon) {
        try {
            System.out.println("上传头像");
            System.out.println(icon.getOriginalFilename());
//            String fileName=FileUploadUtils.uploadPicture(Configuration.iconRepository+user_id+"/",icon);//将文件保存本地并返回相对路径及文件名
            String extension=icon.getOriginalFilename().substring(icon.getOriginalFilename().lastIndexOf("."));
//            System.out.println(extension);
            File desc = FileUploadUtils.getAbsoluteFile(Configuration.iconRepository, Configuration.iconRepository + user_id + FileUploadUtils.IMAGE_JPG_EXTENSION);
            icon.transferTo(desc);
//            System.out.println(desc.getName());
            String completePath = Configuration.iconRepository + desc.getName();
            String IP = InetAddress.getLocalHost().getHostAddress();
            completePath = "http://" + IP + ":" + Configuration.port + "/getFile?path=" + completePath;
            userMapper.updateUserInfo(user_id, "user_face", completePath);//更新数据库


            return new JsonResult<>(completePath, Configuration.globalSuccessCode, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "上传失败！");
        }
    }

    @RequestMapping("/face/getFacePath")//通过一个路径来显示该路径对应的图片
    @ResponseBody
    public JsonResult<List<String>> getFacePath(@RequestParam int task_id){
        try{
            return new JsonResult<>(taskMapper.getFacePath(task_id),Configuration.globalSuccessCode,"查询成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult<>(Configuration.globalFailureCode,"查询失败");
        }
    }

    @RequestMapping("/getFile")//通过一个路径来显示该路径对应的图片
    @ResponseBody
    public void getFaceImg(@RequestParam String path, final HttpServletResponse response) throws IOException {
//        System.out.println("获取图片:");
//        System.out.println(path);

        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputStream = response.getOutputStream();
        InputStream in = new FileInputStream(path);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf, 0, 1024)) != -1) {
            outputStream.write(buf, 0, len);
        }
        in.close();
        outputStream.close();
    }


    @RequestMapping("/task/insertTask")
    @ResponseBody
    public JsonResult insertTask(@RequestBody Task task) {
        try {
            System.out.println("添加任务");
            int task_id = taskMapper.getId();//自增式id
            task.setTask_id(task_id);
            taskMapper.insertTask(task);
            System.out.println(task.toString());
            return new JsonResult(taskMapper.getTaskById(task_id), Configuration.globalSuccessCode, "成功插入id：" + task_id + " 的任务");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "插入失败");
        }
    }


    @RequestMapping("/task/deleteTask")
    @ResponseBody
    public JsonResult deleteTask(@RequestParam int task_id) {
        try {

            taskMapper.deleteTask(task_id);
            return new JsonResult(Configuration.globalSuccessCode, "成功删除id：" + task_id + " 的任务");
        } catch (Exception e) {
            e.printStackTrace();
            /////删除失败则让后台进行删除
            return new JsonResult(Configuration.globalFailureCode, "删除失败");
        }
    }

    @RequestMapping("/task/updateTask")
    @ResponseBody
    public JsonResult updateTask(@RequestBody Task task) {
        try {


            taskMapper.updateTask(task);
            System.out.println("更新任务！");
            System.out.println(task.toString());
            return new JsonResult(taskMapper.getTaskById(task.getTask_id()), Configuration.globalSuccessCode, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "更新失败");
        }
    }

    @RequestMapping("/task/finishTask")
    @ResponseBody
    public JsonResult finishTask(@RequestParam int task_id) {
        taskMapper.completeTask(task_id, System.currentTimeMillis());
        locationMapper.deleteGroupLocation(task_id);
        taskMapper.completeTask(task_id,System.currentTimeMillis());
        return new JsonResult(Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/cancelTask")
    @ResponseBody
    public JsonResult cancelTask(@RequestParam int task_id) {
        taskMapper.cancelTask(task_id, System.currentTimeMillis());
        locationMapper.deleteGroupLocation(task_id);
        taskMapper.cancelTask(task_id,System.currentTimeMillis());
        return new JsonResult(Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/getTaskLocation")
    @ResponseBody
    public JsonResult<List<Location>> getTaskLocation(@RequestParam int task_id) {
        try {
            List<Integer> users = taskMapper.getGroupUserId(task_id);
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                JSONObject idLocation = new JSONObject();
                List<Location> location = locationMapper.queryLocationById(task_id, users.get(i));
                //System.out.println(location.get(0).getCollect_time());
                //JSONArray array= JSONArray.fromObject(location);
                //System.out.println(array.get(0));
                List<JSONObject> jsonObjects1 = new ArrayList<>();
                for (int j = 0; j < location.size(); j++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("longitude", location.get(j).getLongitude());
                    jsonObject.put("latitude", location.get(j).getLatitude());
                    //jsonObject.put("collect_time", location.get(j).getCollect_time());
                    jsonObjects1.add(jsonObject);
                }
                idLocation.put("id", users.get(i));
                idLocation.put("points", jsonObjects1);
                idLocation.put("color","#"+Integer.toHexString(Integer.parseInt(new String(new  StringBuffer(String.valueOf(users.get(i))).reverse()))%16777215));
                idLocation.put("width",5);
                jsonObjects.add(idLocation);
            }

            return new JsonResult(jsonObjects, Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonResult<>(locationMapper.queryGroupLocation(task_id), Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/uploadLocation")
    @ResponseBody
    public JsonResult uploadLocation(@RequestBody Location location) {
        try {
            locationMapper.uploadLocation(location);
            System.out.println(location.toString());
            return new JsonResult(Configuration.globalSuccessCode, "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalFailureCode, "上传失败");
        }


    }

    @RequestMapping("/task/getAllLocation")
    @ResponseBody
    public JsonResult<List<Location>> getAllLocation() {
        return new JsonResult<>(locationMapper.queryAllLocation(), Configuration.globalSuccessCode, "操作成功");
    }

    @RequestMapping("/task/clearGroupUserLocation")
    @ResponseBody
    public JsonResult clearGroupUserLocation(@RequestParam int task_id,
                                             @RequestParam int user_id) {
        try {
            System.out.println("清除位置： task_id:"+task_id+"  user_id:"+user_id);
            locationMapper.deleteGroupUserLocation(task_id, user_id);
            return new JsonResult(Configuration.globalSuccessCode, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(Configuration.globalSuccessCode, "操作失败");
        }

    }


    @RequestMapping("/faceCompare")
    @ResponseBody
    public JsonResult faceCompare(@RequestParam int task_id, @RequestParam int user_id, @RequestParam MultipartFile face) throws Exception {
        String prefix = Configuration.faceTaken + task_id + '/';
        String fileName = FileUploadUtils.uploadPicture(prefix, face);//这里返回的是文件名
        long time = System.currentTimeMillis();
        taskMapper.insertTakenFace(task_id, user_id, prefix + fileName, time);
        List<String> srcs = taskMapper.getFacePath(task_id);
        File dst = new File(prefix + fileName);
        List<Float> rates = new ArrayList<>(srcs.size());
        float total = 0;
        for (String each : srcs
        ) {
            System.out.println(each);
            each=each.split("path=")[1].split("\\|")[0];
            System.out.println(each);
            float rate = FaceHelper.compare(dst, new File(each));
            total += rate;
            rates.add(rate);
            System.out.print(rate + "--");
//            if (rate >= 0.9)
//                return new JsonResult(rates, Configuration.globalSuccessCode, "对比成功");
        }
//        System.out.println("");
//        if (total / srcs.size() >= 0.72) {
//            return new JsonResult(rates, Configuration.globalSuccessCode, "对比成功");
//        }
        return new JsonResult(rates, Configuration.globalSuccessCode, "对比成功");
    }

}
