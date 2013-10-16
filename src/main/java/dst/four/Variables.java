package dst.four;

public class Variables {

	// request variables
	public static final String JOIN = "join";// request to  see all exsisting hosted boards

	public static final String HOST = "host";// to host a new whiteboard

	public static final String LOOKUP = "lookup";//to obtain tcp point

	public static final String CLOSE = "close";// to exit  white board hosted

	// response variables
	public static final String NO_VALS = "empty";

	public static final String VALS_PRESENT = "present";

	public static final String ADDED = "added"; //add as host

	public static final String INVALID = "invalid";

	public static final String JOIN_HOST = "joinBoard"; // to join

	public static final String ADD_CLIENT= "addClient";

	public static final String DENY = "denyJoinRequest";

	public static final String REMOVE = "removeJoinedClient";

}
