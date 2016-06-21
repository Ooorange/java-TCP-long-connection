package procotol;

public class HeartBeatProcotol extends BasicProtocol{

	public static final String HEART_COMMEND="0000";
	
	@Override
	public String getCommend() {
		return HEART_COMMEND;
	}
}
