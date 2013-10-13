import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownload {

    public static void download(String location, URL web) throws IOException {

        // Set cookie variable if you need to download pdf's from the authenticated web url,leave it blank for other cases 
        String cookie = "";
        HttpURLConnection conn = (HttpURLConnection) web.openConnection();
        conn.setRequestProperty("Cookie", cookie);
        String HTML = read(conn.getInputStream());
        Pattern p = Pattern.compile("(?<=href=\")([\\w\\d\\S-]*)(?=\\.pdf)"); // Modify this regex if you want to download other multimedia as videos and images
        Matcher m = p.matcher(HTML);
        while (m.find()) {
            String s = m.group();
            s = s + "" + ".pdf";
            String filename = "";
            if (s.contains("/")) {
                filename = s.substring(s.lastIndexOf("/"), s.length());  // extracting the file name from the url   
            } else {
                filename = s;
            }
            location = location.trim();
            File file = new File(location + "\\" + filename);
            if (!s.contains("http")) {
                if (web.toString().contains(s.substring(0, 5))) {
                    s = web.toString().substring(0, web.toString().indexOf(s.substring(0, 5))) + s;    // resolving relative urls
                } else {
                    s = web.toString() + s;
                }

            }

            URL url = new URL(s);
            BufferedInputStream br = new BufferedInputStream((url.openStream()));
            FileOutputStream fout = new FileOutputStream(file);
            byte[] data = new byte[1024];
            int count;
            while ((count = br.read(data, 0, 1024)) != -1) {

                fout.write(data, 0, count);
            }
        }
    }
    
    public static String read(InputStream i) throws MalformedURLException, IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(i));
        } catch (Exception e) {
            return null;
        }
        String HTML = null;
        String line;
        while ((line = br.readLine()) != null) {
            HTML += line + "\n";
        }
        br.close();
        return HTML;
    }
    

    public static void main(String args[]) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println(" Enter URL of WebSite");        // url must start with protocol e.g Http OR Https
        URL url = new URL(s.nextLine());
        System.out.println("Enter Location");               //enter the path of the location at where you want to store your files.: must follow the drive alphabet.
        String location = s.nextLine();
        download(location, url);

    }

    
}
