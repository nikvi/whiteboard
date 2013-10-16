package dst.four;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.jeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;

import dst.objects.JoinCommand.Command;
import dst.objects.Response.RegistryResp;

public class RegisteryNode {
	String id;
	String requestType;
	String port;
	ZMQ.Context context;
	ZMQ.Socket socket;

	public ZMQ.Socket startHostConnection() {

		context = ZMQ.context(1);
		socket = context.socket(ZMQ.REQ);
		socket.bind("tcp://*:" + this.getPort());
		socket.connect("tcp://localhost:5555");
		if (this.requestType.equalsIgnoreCase(Variables.HOST)) {
			String localAddr = null;
			try {
				localAddr = InetAddress.getLocalHost().getHostAddress();

			} catch (UnknownHostException e) {

				e.printStackTrace();
			}
			System.out.println(String.valueOf(this.getPort()));
			String tcpEndpoint = "tcp://" + localAddr + ":"+ String.valueOf(this.getPort());
			Command reqObj = Command.newBuilder().setCommndVal(requestType).setId(id)
					.setTcpEndPoint(tcpEndpoint).build();
			RegistryResp resp = this.getRespObject(socket,reqObj,0);

			if (resp != null
					&& resp.getResponse().equalsIgnoreCase(Variables.ADDED)) {
				System.out.println("Successfully added as a host");
			}

		}
		
		return socket;

	}
	
	public List<String> requestHostedBoards(){
		context = ZMQ.context(1);
		socket = context.socket(ZMQ.REQ);
		socket.bind("tcp://*:" + this.getPort());
		socket.connect("tcp://localhost:5555");
		if (this.requestType.equalsIgnoreCase(Variables.JOIN)) {
			Command reqObj = Command.newBuilder().setCommndVal(requestType).setId(id).build();
			RegistryResp resp = this.getRespObject(socket,reqObj,0);

			if (resp != null) {
				if (resp.getResponse().equalsIgnoreCase(Variables.NO_VALS)) {
					System.out.println("no hosted blackboards available");
					return null;

				} else if (resp.getHostsCount() != 0) {
					System.out.println("Hosted Blackboards are available");
					for (int i = 0; i < resp.getHostsCount(); i++)
						System.out.println(resp.getHosts(i));
					return resp.getHostsList();

				}

			}
		}
		return null;
	}
	public String getHostedWhiteBoardIP(ZMQ.Socket socket,String hostId){
		
		Command reqObj = Command.newBuilder().setCommndVal(Variables.LOOKUP).setId(this.getId())
				.setLookUp(hostId).build();
		RegistryResp respnse = getRespObject(socket,reqObj,1);
		if(respnse!=null && respnse.getResponse().equalsIgnoreCase(Variables.VALS_PRESENT) && respnse.hasTcpEndPoint()){
			
			return  respnse.getTcpEndPoint();
		}
		
		return "";
	}
	
	public void closeWhiteBoardSession(ZMQ.Socket socket){
		Command reqObj = Command.newBuilder().setCommndVal(Variables.CLOSE).setId(this.getId())
				          .build();
		RegistryResp respnse = getRespObject(socket,reqObj,1);
		if(respnse != null && respnse.getResponse().equalsIgnoreCase(Variables.CLOSE)){
			System.out.println("Successfully removed from registry");
		}
		
	}

	public RegistryResp getRespObject( ZMQ.Socket socket,Command commandObj,int flag) {
	
		socket.send(commandObj.toByteArray(), 0);
		byte[] rawBytes = socket.recv(0);
		RegistryResp resp = null;
		try {
			resp = RegistryResp.parseFrom(rawBytes);
		} catch (InvalidProtocolBufferException e) {

			e.printStackTrace();
		}

		return resp;

	}
	

	public String getPort() {
		return port;
	}

	public RegisteryNode (String id, String reqType, String port) {
		this.id = id;
		this.requestType = reqType;
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public String getRequestType() {
		return requestType;
	}

	public void connectClose() {
		socket.close();
		context.term();
	}


	}
