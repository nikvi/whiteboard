package dst.four;


import java.util.Hashtable;
import java.util.Set;

import org.jeromq.ZMQ;
import org.jeromq.ZMQ.Context;
import org.jeromq.ZMQ.Socket;

import dst.objects.JoinCommand.Command;
import dst.objects.Response.RegistryResp;

public class RegistryServer {
	public static void main(String args[]) throws Exception {
		Set<String> hosts;
		Hashtable<String, Information> registry = new Hashtable<String, Information>();
		Context context = ZMQ.context(1);
		// Socket to talk to clients
		Socket socket = context.socket(ZMQ.REP);
		socket.bind("tcp://*:5555");

		while (!Thread.currentThread().isInterrupted()) {
			byte[] reply = socket.recv(0);
			System.out.println("Received Command");
			try {
				Command data = Command.parseFrom(reply); // deserialization
				RegistryResp response = null;
				// request to join existing whiteboards
				if (data.getCommndVal().equalsIgnoreCase(Variables.JOIN)) {
					// returns the list of white board hosts
					hosts = registry.keySet();
					if (hosts.isEmpty()) {
						response = RegistryResp.newBuilder()
								.setResponse(Variables.NO_VALS).build();
					} else {
						response = RegistryResp.newBuilder()
								.setResponse(Variables.VALS_PRESENT)
								.addAllHosts(hosts).build();
						System.out.println("Returned the set of hosts available");
					}
					socket.send(response.toByteArray(), 1);

				}
				// request to start a new board, end point and ports to be
				// stored
				else if (data.getCommndVal().equalsIgnoreCase(Variables.HOST)) {
					// add code to get tcp end point and store in registry
					String keyVal = data.getId();
					String tcpPoint = data.getTcpEndPoint();
					Information id = new Information(tcpPoint);
					registry.put(keyVal, id);
					response = RegistryResp.newBuilder()
							.setResponse(Variables.ADDED).build();
					socket.send(response.toByteArray(), 0);
					System.out.println("Added as host successfully");
				}
				// return the values of board selected
				else if (data.getCommndVal().equalsIgnoreCase(Variables.LOOKUP)
						&& data.hasLookUp()) {
	                //getting value
					Information hostVal = registry.get(data.getLookUp());
					if (hostVal != null) {
						response = RegistryResp.newBuilder()
								.setResponse(Variables.VALS_PRESENT)
								.setTcpEndPoint(hostVal.getAddress()).build();
					} else {
						response = RegistryResp.newBuilder()
								.setResponse(Variables.NO_VALS).build();
					}
					socket.send(response.toByteArray(), 1);

				} else if (data.getCommndVal()
						.equalsIgnoreCase(Variables.CLOSE)) {
					registry.remove(data.getId());
					response = RegistryResp.newBuilder()
							.setResponse(Variables.CLOSE).build();

					socket.send(response.toByteArray(), 1);

				} else {
					response = RegistryResp.newBuilder()
							.setResponse(Variables.INVALID).build();

					socket.send(response.toByteArray(), 1);
				}

			} catch (Exception e) {
			}

		}
		socket.close();
		context.term();
	}
}
