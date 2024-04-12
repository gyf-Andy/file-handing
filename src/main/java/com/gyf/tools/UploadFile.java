package com.gyf.tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 郭云飞
 * @date 2022/2/22-15:01
 * @Description TODO
 */

public class UploadFile {

    /**
     * 可用   上传普通类型的数据
     * @param urlStr
     * @param params
     * @return
     */
    public String httpGet(String urlStr, Map<String,String> params){
        URL connect;
        StringBuffer data = new StringBuffer();
        try {
            connect = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)connect.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);//post不能使用缓存
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            OutputStreamWriter paramout = new OutputStreamWriter(  connection.getOutputStream(),"UTF-8");
            String paramsStr = "";   //拼接Post 请求的参数
            for(String param : params.keySet()){
                paramsStr += "&" + param + "=" + params.get(param);
            }
            if(!paramsStr.isEmpty()){
                paramsStr = paramsStr.substring(1);
            }
            paramout.write(paramsStr);
            paramout.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            paramout.close();
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(data.toString());
        return data.toString();
    }

    /**
     * 传输文件
     * @param actionURL 远程服务接口调用地址
     * @param name 参数名
     * @param fileStream 文件流
     * @param fileName 文件名
     * @param fileType 文件类型
     * @param parameters 其他参数
     * @return
     */
    public String singleFileUploadWithParameters(String actionURL, String name,  InputStream fileStream,
                                                        String fileName, String fileType, HashMap<String, String> parameters) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "----WebKitFormBoundary851PD6JXXxfIPFk9";
        String response = "";
        try {
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // 设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // 获取请求内容输出流
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            // 开始写表单格式内容
            // 写参数
            if (parameters != null) {
                Set<String> keys = parameters.keySet();
                for (String key : keys) {
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; name=\"");
                    ds.write(key.getBytes());
                    ds.writeBytes("\"" + end);
                    ds.writeBytes(end);
                    ds.write(parameters.get(key).getBytes());
                    ds.writeBytes(end);
                }
            }
            // 写文件
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + name + "\"; " + "filename=\"");
            // 防止中文乱码
            ds.write(fileName.getBytes());
            ds.writeBytes("\"" + end);
            ds.writeBytes("Content-Type: " + fileType + end);
            ds.writeBytes(end);
            // 根据路径读取文件
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fileStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            fileStream.close();
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.writeBytes(end);
            ds.flush();
            try {
                // 获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String s = "";
                String temp = "";
                while ((temp = reader.readLine()) != null) {
                    s += temp;
                }
                response = s;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("无响应!!!");
            }
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("请求失败!");
        }
        return response;
    }
}
