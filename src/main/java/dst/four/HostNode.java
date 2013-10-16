package dst.four;

import java.io.Console;

import org.jeromq.ZMQ;
import org.jeromq.ZMQ.Context;
import org.jeromq.ZMQ.Socket;





import com.google.protobuf.InvalidProtocolBufferException;

import dst.objects.JoinCommand.Command;
import dst.objects.Response.RegistryResp;


public class HostNode {
	
	
	public static void main(String[] args)  {
		Context context = ZMQ.context(1);
		// Socket to talk to clients
		Socket socket = context.socket(ZMQ.REP);
		

		Console c = System.console();
		if (c == null) {
			System.err.println("No console.");
			System.exit(1);
		}
		
		String port = c.readLine("Enter port to start White Board: ");
		String id  = c.readLine("Enter your uniqueId");
		socket.bind("tcp://*:"+id);
		RegisteryNode hostNode = new RegisteryNode(id, Variables.HOST, port);
		ZMQ.Socket socketNew = hostNode.startHostConnection();
		RegistryResp respObj = null;
		while(true){
			try {
				 respObj = RegistryResp.parseFrom(socket.recv(0));
			} catch (InvalidProtocolBufferException e) {
			
				e.printStackTrace();
			}
			if ((respObj !=null) && (respObj.getResponse().equalsIgnoreCase(Variables.ADD_CLIENT))){
				String clientId = respObj.getRequestId();
				String add = c.readLine(clientId  + "request access to hosted board. Permit access : Y/N");
				if (add.equalsIgnoreCase("Y")){
					Command reqObj = Command.newBuilder().setCommndVal(Variables.ADD_CLIENT)
							.setId(id).build();
					socket.send(reqObj.toByteArray(),0);
					// TODO add to client list
					System.out.println("Added" + clientId + "successfully as client");
					
				}
				else if(add.equalsIgnoreCase("N")) {
					System.out.println("Request denied");
					Command reqObj = Command.newBuilder().setCommndVal(Variables.DENY)
							.setId(id).build();
					socket.send(reqObj.toByteArray(),0);
				}
			}
			
		}
		

	}
	
}
