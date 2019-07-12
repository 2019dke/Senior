import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoDatabase;


public class RecoServer implements Runnable {
	static ServerSocket serversocket;
	BufferedReader br1, br2;
	Thread t1, t2;
	String in="",out="";
	Socket so;
	
	int test=0;
	static int ch = 0;
	static int gesu=0;

	public RecoServer() throws IOException {
			int sid = gesu;
			System.out.println(sid+" "+"Server is waiting. . . . ");
			while(true) {
				try {
					so = serversocket.accept();
					ch = 0;
					System.out.println("Client connected with Ip " +so.getInetAddress().getHostAddress());
					Thread th = new Thread(this);
					Thread th1 = new Thread(this);
					th.setName("open");
					//th1.setName("send");
					th.start();
					//th1.start();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
				
	}

	public void run() {
		Socket socket = so;
		while(true) {
		try {
			if (Thread.currentThread().getName() == "send") {
				/*while (!in.equals("END")){
					br1 = new BufferedReader(new InputStreamReader(System.in));
					PrintWriter pr1 = new PrintWriter(socket.getOutputStream(), true);
					in = br1.readLine();
					pr1.println(in);
				}
				System.out.println("END!");*/
			} else {
					br2 = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
					out = br2.readLine();
					System.out.println(socket.getInetAddress().getHostAddress()+" says : : : "+out);
					if(out.equals("END")) {
						System.out.println(socket.getInetAddress().getHostAddress()+"END");
						socket.close();
						break;
					}
			
					String keyword_result; //Ű���忡 ���� ����� �����ϴ� ����
					if ( !(keyword_result = search_general(out)).equals("")) {
						//�Ϲ� ������ ������� �ִٸ� keyword_result�� �־��� �����ͷ� �м�
								PrintWriter pr1 = new PrintWriter(socket.getOutputStream(), true);
								pr1.println(keyword_result);
					}
					else if (!(keyword_result = search_detail(out)).equals("")) {
						//�� ������ ������� �ִٸ� keyword_result�� �־��� �����ͷ� �м�
						PrintWriter pr1 = new PrintWriter(socket.getOutputStream(), true);
						pr1.println(keyword_result);
					}
					else {
						//���� ������ ��� ���ٸ� ��� ���� ���
						PrintWriter pr1 = new PrintWriter(socket.getOutputStream(), true);
						pr1.println("��� ����");
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			break;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		serversocket = new ServerSocket(4995);
		new RecoServer();
	}
	
	public static String search_general(String keyword) {
		String ret = "";
		if(keyword.equals("�Ұ��")) { // �Ϲݻ����� Ű���尡 �ִ��� �˻� �� ret�� ��� �Ѱ���
			ret = "�Ұ�� ���� ����";
		}
		return ret;
	}
	public static String search_detail(String keyword) {
		String ret = "";
		if(keyword.equals("��������Ұ��")) { //�󼼻����� Ű���尡 �ִ��� �˻� �� ret�� ��� �Ѱ���
			ret = "��������Ұ��"; 
		}
		return ret;
	}
}


