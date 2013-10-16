package dst.examples2;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jeromq.ZMQ;

import com.google.protobuf.InvalidProtocolBufferException;

import dst.four.Variables;
import dst.objects.JoinCommand.Command;
import dst.objects.Response.RegistryResp;

class NodeType {
	String id;
	String requestType;
	int port;
	ZMQ.Context context;
	ZMQ.Socket socket;

	public ZMQ.Socket startConnection(String reqType) {

		context = ZMQ.context(1);
		socket = context.socket(ZMQ.REQ);
		socket.bind("tcp://*:" + this.getPort());
		socket.connect("tcp://localhost:5555");
		if (reqType.equalsIgnoreCase(Variables.HOST)) {
			
			Command reqObj = Command.newBuilder().setCommndVal(requestType)
					.setId(id).setTcpEndPoint(this.getTCPEndPoint()).build();
			RegistryResp resp = this.getRespObject(socket, reqObj, 0);

			if (resp != null
					&& resp.getResponse().equalsIgnoreCase(Variables.ADDED)) {
				System.out.println("Successfully added as a host");
			}

		}

		if (reqType.equalsIgnoreCase(Variables.JOIN)) {
			Command reqObj = Command.newBuilder().setCommndVal(requestType)
					.setId(id).build();
			RegistryResp resp = this.getRespObject(socket, reqObj, 0);

			if (resp != null) {
				if (resp.getResponse().equalsIgnoreCase(Variables.NO_VALS)) {
					System.out.println("no hosted blackboards available");

				} else if (resp.getHostsCount() != 0) {
					System.out.println("Hosted Blackboards are available");
					for (int i = 0; i < resp.getHostsCount(); i++)
						System.out.println(resp.getHosts(i));

				}

			}
		}

		return socket;

	}

	public String sentJoinRequest(ZMQ.Socket socket, String hostId) {

		Command reqObj = Command.newBuilder().setCommndVal(Variables.JOIN_HOST)
				.setId(this.getId()).setLookUp(hostId).setTcpEndPoint(this.getTCPEndPoint()).build();
		RegistryResp respnse = getRespObject(socket, reqObj, 1);
		if (respnse != null
				&& respnse.getResponse().equalsIgnoreCase(
						Variables.ADDED) && respnse.hasTcpEndPoint()) {
			System.out.println("Successfully connected to hosted blackboard");
            return respnse.getTcpEndPoint();
		}
		System.out.println("Your connection request is denied");
		return "";
	}

	public String getTCPEndPoint() {
		String localAddr = null;
		try {
			localAddr = InetAddress.getLocalHost().getHostAddress();

		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		System.out.println(String.valueOf(this.getPort()));
		String tcpEndpoint = "tcp://" + localAddr + ":"
				+ String.valueOf(this.getPort());
		return tcpEndpoint;
	}
	public void closeWhiteBoardSession(ZMQ.Socket socket) {
		Command reqObj = Command.newBuilder().setCommndVal(Variables.CLOSE)
				.setId(this.getId()).build();
		RegistryResp respnse = getRespObject(socket, reqObj, 1);
		if (respnse != null
				&& respnse.getResponse().equalsIgnoreCase(Variables.CLOSE)) {
			System.out.println("Successfully removed from registry");
		}

	}

	public RegistryResp getRespObject(ZMQ.Socket socket, Command commandObj,
			int flag) {

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

	public int getPort() {
		return port;
	}

	NodeType(String id, String reqType, int port) {
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

public class InitialNode {
	public static void main(String[] args) {
		System.out.println("test");
		String id = args[0];
		String reqType = args[1];
		int port = Integer.parseInt(args[2]);
		NodeType nodeVal = new NodeType(id, reqType, port);
		nodeVal.startConnection(reqType);
		//System.out.println(nodeVal.sentJoinRequest(
				//nodeVal.startConnection(reqType), "tom"));
		nodeVal.connectClose();

	}

}
