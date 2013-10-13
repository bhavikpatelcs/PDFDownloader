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
        String cookie = "csrf_token=d98P8rIlQ6hdEWc8fpA9; __utma=126444767.1170688142.1366323720.1366323720.1366323720.1; __utmz=126444767.1366323720.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); data-readme-site-outage-announcement=1; __qca=P0-65300248-1370199575761; __204u=926387024-1375081450167; __204r=https%3A%2F%2Fwww.coursera.org%2F; CAUTH=vTofS-CEraJtaFSW92pmad00bdOK1hzi5Cwm-B0hNioeJ3G3zW9xfiranPs9cSzfuGxJYC1gcTfVDcqB7qgyIQ.er3ntwSA7s0ZFDqIU-sGKQ.Q7l8C-p-cRHLTI1omEk6Q_t2kntIz5eZUYv7MP0O3mKePyM2ohy3vr--PRXHfIAS7WkWtCXly9-Siq7vsRuNND06k7-5_qK1pFsCE7ZJtsN6xCJDiiB_UkGeWhFCj6i4t9GbHLAo3htJT2kW6IMdxn4IDwdTcsHaPckcSdg1EawUxlesfptGfmSf5h79vHsl; maestro_login_flag=1; data-readme-quiz-stats-v1=1; __utma=158142248.361837301.1366353727.1381621045.1381621505.109; __utmb=158142248.39.8.1381622602392; __utmc=158142248; __utmz=158142248.1381621505.109.9.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)";
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
