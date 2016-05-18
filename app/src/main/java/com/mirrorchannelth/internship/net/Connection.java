package com.mirrorchannelth.internship.net;

import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.mirrorchannelth.internship.net.security.MCrypt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Connection extends AsyncTask<Void, Integer, String> {

    private final String TAG = "Connection";
    private String url = null;
    private int connectionTimeOut = 60000;
    private int socketTimeout = 60000;
    private OnConnectionCallBackListener callbackListener = null;
    private OnConnectionUpdateListener updateListener = null;
    private ArrayList<NameValue> postData = null, fileData = null;
    private boolean isDecrypt = false;
    private int delayed = 0;

    public static String CONNECTION_ERROR = "1";
    public static String UNKNOW_HOST = "2";

    public Connection(String url) {
        this.url = url;
    }

    public void addPostData(String name, String value) {
        if (postData == null) {
            postData = new ArrayList<>();
        }
        NameValue nameValue = new NameValue(name, value);
        postData.add(nameValue);
    }

    public void addFileData(String name, String value) {
        if (fileData == null) {
            fileData = new ArrayList<>();
        }
        NameValue nameValue = new NameValue(name, value);
        fileData.add(nameValue);
    }

    public void setOnConnectionCallBackListener(OnConnectionCallBackListener listener) {
        callbackListener = listener;
    }

    public void setOnConnectionUpdateListener(OnConnectionUpdateListener listener) {
        updateListener = listener;
    }

    @Override
    protected String doInBackground(Void... args) {
        //	Sleep n seconds for UX
        if (delayed != 0) {
            try {
                Thread.sleep(delayed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String result = doPost();
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (updateListener != null) {
            updateListener.onUpdate(progress);
//            Log.d(TAG, "Uploading : " + progress[0] + ", " + progress[1]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            super.onPostExecute(result);
            if (callbackListener != null) {
                if (result.equals(Connection.CONNECTION_ERROR)) {
                    callbackListener.onLostConnection();
                } else if (result.equals(Connection.UNKNOW_HOST)) {
                    callbackListener.onUnreachHost();
                } else {
                    callbackListener.onSuccess(result);
                }
            }
        }
    }

    private String doPost() {
        String result = null;
        /*try {
        	HttpParams httpParameters = new BasicHttpParams();
        	HttpConnectionParams.setConnectionTimeout(httpParameters, this.connectionTimeOutSec);
        	HttpConnectionParams.setSoTimeout(httpParameters, this.socketTimeoutSec);
        	DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(this.url);
            if (bodyDataList != null) {
            	httppost.setEntity(new (bodyDataList));
            }
            //	Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
 	        HttpEntity entity = response.getEntity();//,
 	        InputStream is = entity.getContent();
 		    //	Convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				String rawResult = sb.toString();
				result = sb.toString();
				if (isDecrypt) {
					//	Compare & cut string
					String startText = "|s|";
					String endText = "|e|";
					int startPosition = rawResult.indexOf(startText);
					int endPosition = rawResult.indexOf(endText);
					result = MCrypt.decode(rawResult.substring(startPosition + 3, endPosition));
				}
			} catch(Exception e) {
				Log.e("Connection Exception", "Error converting result : " + e.toString());
			}
    	} catch (ConnectTimeoutException e) {
        	result = CONNECTION_ERROR;
    	} catch (UnknownHostException e) {
        	result = UNKNOW_HOST;
        } catch (IOException e) {
        	result = CONNECTION_ERROR;
        } catch (Exception e) {
        	result = CONNECTION_ERROR;
        	e.printStackTrace();
        }
		Log.d(TAG, result);*/

        HttpURLConnection conn = null;
        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            //  Define connection
            URL requestUrl = new URL(url);
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setReadTimeout(socketTimeout);
            conn.setConnectTimeout(connectionTimeOut);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("charset", "utf-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            conn.setUseCaches(false);

            //  Check if upload file
            if (fileData == null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            }

            //  Create output stream
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            //  Check if upload file
            if (fileData == null) {
                //dos.writeBytes(getPostDataString());
                System.out.println("Post data"+getPostDataString());
                String test = getPostDataString();
                //String encodeString = MCrypt.encode(getPostDataString());
                String test2 = MCrypt.encode(getPostDataString());
                //dos.writeBytes("input=" + MCrypt.encode(getPostDataString()));
                dos.writeBytes(getPostDataString());
                //System.out.println(getPostDataString());
                //System.out.println("input=" + encodeString);  //getPostDataString());
            } else {
                //  Parameter - data
                if (postData != null) {
                    String encodeString = MCrypt.encode(getPostDataString());
                    String key = "input"; //URLEncoder.encode(nameValue.getName(), "UTF-8");
                    String value = encodeString; //URLEncoder.encode(nameValue.getValue(), "UTF-8");
                    /*
                    int size = postData.size();
                    for (int i = 0; i < size; i++) {
                        NameValue nameValue = postData.get(i);
                        String key = nameValue.getName(); //URLEncoder.encode(nameValue.getName(), "UTF-8");
                        String value = URLEncoder.encode(nameValue.getValue(), "UTF-8"); */

                        //  Define content data
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                        dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        dos.writeBytes("Content-Length: " + value.length() + lineEnd + lineEnd);
                        dos.writeBytes(value);
                        dos.writeBytes(lineEnd);
                }
                //  Parameter - file
                int fileIndex = 0;
                int size = fileData.size();
                for (int i = 0; i < size; i++) {
                    NameValue nameValue = fileData.get(i);
                    String key = nameValue.getName();
                    String value = nameValue.getValue();
                    File file = new File(value);
                    if (file.exists()) {
                        String mimeType = null;
                        String extension = MimeTypeMap.getFileExtensionFromUrl(value);
                        if (extension != null) {
                            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        }

                        //  Define content data
                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"" + lineEnd);
                        dos.writeBytes("Content-Type: " + mimeType + lineEnd + lineEnd);

                        //  Create input stream
                        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
                        long fileLength = file.length();
                        int bufferSize = 1024;
                        byte data[] = new byte[bufferSize];
                        int count = input.read(data);
                        long total = 0;
                        while (count >= 0) {
                            total += count;
                            dos.write(data, 0, count);
                            if (fileLength > 0) {
                                publishProgress(fileIndex, (int) (total * 100 / fileLength));
                            }
                            count = input.read(data);
                        }
                        input.close();

                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    }
                    fileIndex++;
                }
            }
            dos.flush();
            dos.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line = null, encryptedResult = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    encryptedResult += line;
                }
                //System.out.print(encryptedResult);
                //result = MCrypt.decode(encryptedResult);
                result = encryptedResult;
            } else {
                result = CONNECTION_ERROR;
            }
        } catch (Exception ex) {
            result = CONNECTION_ERROR;
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        //Log.d(TAG, result);

        return result;
    }

    private String getPostDataString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        if (postData != null) {
            int size = postData.size();
            for (int i = 0; i < size; i++) {
                NameValue nameValue = postData.get(i);
                if (isFirst) {
                    isFirst = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(nameValue.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(nameValue.getValue(), "UTF-8"));
            }
        }
        return result.toString();
    }

    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    public void setDecrypt(boolean isDecrypt) {
        this.isDecrypt = isDecrypt;
    }

    /**
     * Connection listener
     */

    public interface OnConnectionCallBackListener {
        public void onSuccess(String result);
        public void onLostConnection();
        public void onUnreachHost();
    }

    public interface OnConnectionUpdateListener {
        public void onUpdate(Integer... progress);
    }

    private class NameValue {
        private String name = null;
        private String value = null;

        public NameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }
	
}
