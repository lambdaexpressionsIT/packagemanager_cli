package com.lambdaexpressions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class APKManagerCli {


    public static void main(String[] args) {
        // remove all {{*}}
        uploadFile("{{your_local_folder}}", "{{your_local_file.apk}}", "http://{{your_ip}}:8080/api/v1/downloadApk", "{{packageName}}", "{{version}}");

        downloadFile("http://{{your_ip}}:8080/api/v1/getApk/{{packageName}}/{{version}}");
    }

    public static void downloadFile(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            ObjectMapper o = new ObjectMapper();
            APKMessageDTO result = o.readValue(sb.toString(), APKMessageDTO.class);
            File f = new File(result.getName() + "_" + result.getVersion() + ".apk");
            FileUtils.writeByteArrayToFile(f, result.getApkFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String folder, String uploadFileName, String upLoadServerUri, String packageName, String version) {

        String fileName = folder + "/" + uploadFileName;
        String finalUrl = upLoadServerUri + "/" + packageName;
        System.out.println(fileName);
        System.out.println(finalUrl);

        HttpURLConnection connection = null;
        DataOutputStream dos = null;

        File sourceFile = new File(fileName);

        if (!sourceFile.isFile()) {
            System.out.println("Source File not exist :" + fileName);
        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(finalUrl);

                // Open a HTTP connection to the URL
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true); // Allow Inputs
                connection.setDoOutput(true); // Allow Outputs
                connection.setUseCaches(false); // Don't use a Cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                APKMessageDTO a = new APKMessageDTO(packageName, version, FileUtils.readFileToByteArray(sourceFile));
                ObjectMapper o = new ObjectMapper();
                String jsonInputString = o.writeValueAsString(a);
                //System.out.println(jsonInputString);

                dos = new DataOutputStream(connection.getOutputStream());

                byte[] input = jsonInputString.getBytes("utf-8");
                dos.write(input, 0, input.length);
                fileInputStream.close();
                dos.flush();
                dos.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                System.out.println(response.toString());

            } catch (Exception ex) {

                ex.printStackTrace();

            }
        }
    }
}

