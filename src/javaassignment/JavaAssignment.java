/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaassignment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kitti Kraithep
 */
public class JavaAssignment {
public static void main(String args[])
    { 
        Calcallservice("promotion1.log");
    }
    
    public static void Calcallservice(String path){
            try{
                //prepare path for read input log file 
                FileInputStream f_logfile = new FileInputStream(path);
                BufferedReader br_reader = new BufferedReader(new InputStreamReader(f_logfile));
                String ln_data; 
                
                //json arr and map for writing data
                JSONArray js_arr = new JSONArray();
                HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
                int i = 0;
                
                //reading and put json data 
                while((ln_data = br_reader.readLine()) != null){
                    
                    //manage data line to get TIME and CALLservice.
                    //Without promotion - shoud take "-" in last index
                    String ln_str[] = ln_data.split("[|]");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date tm_start = format.parse(ln_str[1]);
                    Date tm_stop = format.parse(ln_str[2]);
                    long time = tm_stop.getTime() - tm_start.getTime();
                    double service;
                    
                    //check promotion of service
                    //if one day file log
                    if("P1".equals(ln_str[4])){
                        //time in second and service per second
                        if(time/1000<=60){service = 3.0;
                        }else{service = (((time/1000)-60)/60.0)+3.0;}             
                    }else{service = (time/1000)/20.0;}                    
                     
                    //put data to json array/map
                    JSONObject js_cservice = new JSONObject();
                    js_cservice.put("number",ln_str[3]);
                    js_cservice.put("service",service);                   
                    map.put("json" + i, js_cservice);
                    js_arr.put(map.get("json" + i));
                    i++;
                }
                //System.out.println("The output string is " + js_arr.toString());
                
                f_logfile.close();
                //part of writing json file to project directory
                try(FileWriter file = new FileWriter(path.substring(0,path.length()-4)+".json")){
                    file.write(js_arr.toString());
                    System.out.println("Successfully exported object to .json file...");
                }catch (Exception e) {
                  System.err.println("Error: " + e.getMessage());
                }  
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    
}
